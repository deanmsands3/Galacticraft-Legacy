package team.galacticraft.galacticraft.common.api.prefab.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.language.I18n;
import team.galacticraft.galacticraft.common.api.GalacticraftRegistry;
import team.galacticraft.galacticraft.common.api.client.GameScreenText;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.api.entity.IIgnoreShift;
import team.galacticraft.galacticraft.common.api.entity.ITelemetry;
import team.galacticraft.galacticraft.common.api.event.GalacticraftEvents;
import team.galacticraft.galacticraft.common.api.tile.ITelemetryReceiver;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3Dim;
import team.galacticraft.galacticraft.common.api.world.IExitHeight;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Predicate;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;

import java.util.ArrayList;
import java.util.List;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class EntitySpaceshipBase extends Entity implements /*IPacketReceiver,*/ IIgnoreShift, ITelemetry
{
    public enum EnumLaunchPhase
    {
        UNIGNITED,
        IGNITED,
        LAUNCHED,
        LANDING
    }

    public int launchPhase;

    protected long ticks = 0;
    protected double dragAir;
    public int timeUntilLaunch;
    public float timeSinceLaunch;
    public float rollAmplitude;
    public float shipDamage;
    private final ArrayList<BlockVec3Dim> telemetryList = new ArrayList<>();
    private boolean addToTelemetry = false;
    public FluidTank fuelTank = PlatformSpecific.createFluidInv(1, this.getFuelTankCapacity());
    private double syncAdjustX = 0D;
    private double syncAdjustY = 0D;
    private double syncAdjustZ = 0D;
    private boolean syncAdjustFlag = false;
    public static final Predicate<Entity> ROCKET_SELECTOR = e -> e instanceof EntitySpaceshipBase && e.isAlive();

    public EntitySpaceshipBase(EntityType<? extends EntitySpaceshipBase> type, Level worldIn)
    {
        super(type, worldIn);
        this.launchPhase = EnumLaunchPhase.UNIGNITED.ordinal();
        this.blocksBuilding = true;
        this.noCulling = true;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance)
    {
        double d0 = this.getBoundingBox().getSize();

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * 5.0;
        return distance < d0 * d0;
    }

    public abstract int getFuelTankCapacity();

    public abstract int getMaxFuel();

    public abstract int getScaledFuelLevel(int i);

    public abstract int getPreLaunchWait();

    @Override
    protected boolean isMovementNoisy()
    {
        return false;
    }

    @Override
    public AABB getCollideAgainstBox(Entity par1Entity)
    {
        return null;
    }

    @Override
    public AABB getCollideBox()
    {
        return null;
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    public boolean hurt(DamageSource par1DamageSource, float par2)
    {
        if (!this.level.isClientSide && this.isAlive())
        {
            Entity e = par1DamageSource.getEntity();
            boolean flag = e instanceof Player && ((Player) e).abilities.instabuild;
            if (this.isInvulnerableTo(par1DamageSource) || this.getY() > 300 || (e instanceof LivingEntity && !(e instanceof Player)))
            {
                return false;
            }
            else
            {
                this.rollAmplitude = 10;
                this.markHurt();
                this.shipDamage += par2 * 10;

                if (e instanceof Player && ((Player) e).abilities.instabuild)
                {
                    this.shipDamage = 100;
                }

                if (flag || this.shipDamage > 90 && !this.level.isClientSide)
                {
                    this.ejectPassengers();

                    if (flag)
                    {
                        this.remove();
                    }
                    else
                    {
                        this.remove();
                        this.dropShipAsItem();
                    }
                    return true;
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    public void dropShipAsItem()
    {
        if (this.level.isClientSide)
        {
            return;
        }

        for (final ItemStack item : this.getItemsDropped(new ArrayList<>()))
        {
            ItemEntity entityItem = this.spawnAtLocation(item, 0);

            if (item.hasTag())
            {
                entityItem.getItem().setTag(item.getTag().copy());
            }
        }
    }

    public abstract List<ItemStack> getItemsDropped(List<ItemStack> droppedItemList);

    @Override
    public void animateHurt()
    {
        this.rollAmplitude = 5;
        this.shipDamage += this.shipDamage * 10;
    }

    @Override
    public boolean isPickable()
    {
        return this.isAlive();
    }

//    @Override
//    public boolean shouldRiderSit()
//    {
//        return false;
//    }

    @Override
    public void tick()
    {
        this.ticks++;

        super.tick();

        if (this.addToTelemetry)
        {
            this.addToTelemetry = false;
            for (BlockVec3Dim vec : new ArrayList<>(this.telemetryList))
            {
                BlockEntity t1 = vec.getTileEntityNoLoad();
                if (t1 instanceof ITelemetryReceiver && !t1.isRemoved())
                {
                    if (((ITelemetryReceiver) t1).getLinkedEntity() == this)
                    {
                        ((ITelemetryReceiver) t1).addTrackedEntity(this);
                    }
                }
            }
        }

        for (Entity e : this.getPassengers())
        {
            e.fallDistance = 0.0F;
        }

        if (this.getY() > (this.level.getDimension().getType() instanceof IExitHeight ? ((IExitHeight) this.level.getDimension().getType()).getYCoordinateToTeleport() : 500) && this.launchPhase != EnumLaunchPhase.LANDING.ordinal())
        {
            this.onReachAtmosphere();
//            if (this.world.isRemote)
//            	this.getPosY() = 1 + (this.world.getDimension() instanceof IExitHeight ? ((IExitHeight) this.world.getDimension()).getYCoordinateToTeleport() : 1200);
        }

        if (this.rollAmplitude > 0)
        {
            this.rollAmplitude--;
        }

        if (this.shipDamage > 0)
        {
            this.shipDamage--;
        }

        if (!this.level.isClientSide)
        {
            if (this.getY() < 0.0D)
            {
                this.remove();
            }
            else if (this.getY() > (this.level.getDimension() instanceof IExitHeight ? ((IExitHeight) this.level.getDimension()).getYCoordinateToTeleport() : 1200) + (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() ? 355 : 100))
            {
                for (Entity e : this.getPassengers())
                {
                    if (e instanceof ServerPlayer)
                    {
                        GCPlayerStats stats = GCPlayerStats.get((ServerPlayer) e);
                        if (stats.isUsingPlanetSelectionGui())
                        {
                            this.remove();
                        }
                    }
                    else
                    {
                        this.remove();
                    }
                }
            }

            if (this.timeSinceLaunch > 50 && this.onGround)
            {
                this.failRocket();
            }
        }

        if (this.launchPhase == EnumLaunchPhase.UNIGNITED.ordinal())
        {
            this.timeUntilLaunch = this.getPreLaunchWait();
        }

        if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal())
        {
            this.timeSinceLaunch++;
        }
        else
        {
            this.timeSinceLaunch = 0;
        }

        if (this.timeUntilLaunch > 0 && this.launchPhase == EnumLaunchPhase.IGNITED.ordinal())
        {
            this.timeUntilLaunch--;
        }

        AABB box = this.getBoundingBox().inflate(0.2D);

        final List<?> var15 = this.level.getEntities(this, box);

        if (var15 != null && !var15.isEmpty())
        {
            for (int var52 = 0; var52 < var15.size(); ++var52)
            {
                final Entity var17 = (Entity) var15.get(var52);

                if (this.getPassengers().contains(var17))
                {
                    var17.push(this);
                }
            }
        }

        if (this.timeUntilLaunch == 0 && this.launchPhase == EnumLaunchPhase.IGNITED.ordinal())
        {
            this.setLaunchPhase(EnumLaunchPhase.LAUNCHED);
            this.onLaunch();
        }

        if (this.xRot > 90)
        {
            this.xRot = 90;
        }

        if (this.xRot < -90)
        {
            this.xRot = -90;
        }

        double motionX = -(50 * Math.cos(this.yRot / Constants.RADIANS_TO_DEGREES_D) * Math.sin(this.xRot * 0.01 / Constants.RADIANS_TO_DEGREES_D));
        double motionY = getDeltaMovement().y;
        double motionZ = -(50 * Math.sin(this.yRot / Constants.RADIANS_TO_DEGREES_D) * Math.sin(this.xRot * 0.01 / Constants.RADIANS_TO_DEGREES_D));

        if (this.launchPhase < EnumLaunchPhase.LAUNCHED.ordinal())
        {
            motionX = motionY = motionZ = 0.0;
        }

        setDeltaMovement(motionX, motionY, motionZ);

        if (this.level.isClientSide)
        {
            this.setPos(this.getX(), this.getY(), this.getZ());

            if (this.shouldMoveClientSide())
            {
                this.move(MoverType.SELF, this.getDeltaMovement());
            }
        }
        else
        {
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

        this.setRot(this.yRot, this.xRot);

        if (this.level.isClientSide)
        {
            this.setPos(this.getX(), this.getY(), this.getZ());
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        if (!this.level.isClientSide && this.ticks % 3 == 0)
        {
            //todo networking
//            GalacticraftCore.packetPipeline.sendToDimension(new PacketDynamic(this), this.level.getDimension().getType());
            // PacketDispatcher.sendPacketToAllInDimension(GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES,
            // this, this.getNetworkedData(new ArrayList())),
            // this.world.getDimension().getType());
        }

//        if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal()) //TODO: advancement triggers should be packed with api 
//        {
//            if (getPassengers().size() >= 1) //When the screen changes to the map, the player is not riding the rocket anymore.
//            {
//                if (getPassengers().get(0) instanceof ServerPlayer)
//                {
//                    GCTriggers.LAUNCH_ROCKET.trigger(((ServerPlayer) getPassengers().get(0)));
//                }
//            }
//        }
    }

    protected boolean shouldMoveClientSide()
    {
        return true;
    }

    public abstract boolean hasValidFuel();

//    @Override //todo
    public void decodePacketdata(ByteBuf buffer)
    {
        if (!this.level.isClientSide)
        {
            new Exception().printStackTrace();
        }
        int newLaunchPhase = buffer.readInt();
        if (this.launchPhase != newLaunchPhase)
        {
            this.setLaunchPhase(EnumLaunchPhase.values()[newLaunchPhase]);
            if (newLaunchPhase == EnumLaunchPhase.LANDING.ordinal())
            {
                this.syncAdjustFlag = true;
            }
        }
        else if (this.hasValidFuel())
        {
            double motionX = this.getDeltaMovement().x;
            double motionY = this.getDeltaMovement().y;
            double motionZ = this.getDeltaMovement().z;
            if (Math.abs(this.syncAdjustX - this.getX()) > 0.2D)
            {
                motionX += (this.syncAdjustX - this.getX()) / 40D;
            }
            if (Math.abs(this.syncAdjustY - this.getY()) > 0.2D)
            {
                motionY += (this.syncAdjustY - this.getY()) / 40D;
            }
            if (Math.abs(this.syncAdjustZ - this.getZ()) > 0.2D)
            {
                motionZ += (this.syncAdjustZ - this.getZ()) / 40D;
            }
            setDeltaMovement(motionX, motionY, motionZ);
        }
        this.timeSinceLaunch = buffer.readFloat();
        this.timeUntilLaunch = buffer.readInt();
    }

//    @Override
    public void getNetworkedData(ArrayList<Object> list)
    {
        if (this.level.isClientSide)
        {
            return;
        }
        list.add(this.launchPhase);
        list.add(this.timeSinceLaunch);
        list.add(this.timeUntilLaunch);
    }

    public void turnYaw(float f)
    {
        this.yRot += f;
    }

    public void turnPitch(float f)
    {
        this.xRot += f;
    }

    protected void failRocket()
    {
        for (Entity passenger : this.getPassengers())
        {
            passenger.hurt(DamageSourceGC.spaceshipCrash, (int) (4.0D * 20 + 1.0D));
        }

        if (!ConfigManagerCore.disableSpaceshipGrief.get())
        {
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), 5, Explosion.BlockInteraction.NONE);
        }

        this.remove();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        this.setRot(yaw, pitch);
        if (this.syncAdjustFlag && this.level.hasChunkAt(new BlockPos(x, 255D, z)) && this.hasValidFuel())
        {
            Player p = Minecraft.getInstance().player;
            double dx = x - p.getX();
            double dz = z - p.getZ();
            if (dx * dx + dz * dz < 1024)
            {
                if (this.level.getEntity(this.getId()) == null)
                {
                    try
                    {
                        ((ClientLevel) this.level).putNonPlayerEntity(this.getId(), this);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                this.setPosRaw(x, y, z);

                int cx = Mth.floor(x / 16.0D);
                int cz = Mth.floor(z / 16.0D);

                if (!this.inChunk || this.xChunk != cx || this.zChunk != cz)
                {
                    if (this.inChunk && this.level.hasChunkAt(new BlockPos(this.xChunk << 4, 255, this.zChunk << 4)))
                    {
                        this.level.getChunk(this.xChunk, this.zChunk).removeEntity(this, this.yChunk);
                    }

                    this.inChunk = true;
                    this.level.getChunk(cx, cz).addEntity(this);
                }

                this.syncAdjustX = 0D;
                this.syncAdjustY = 0D;
                this.syncAdjustZ = 0D;
                this.syncAdjustFlag = false;
            }
        }
        else
        {
            this.syncAdjustX = x;
            this.syncAdjustY = y;
            this.syncAdjustZ = z;
        }
    }


    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        nbt.putInt("launchPhase", this.launchPhase + 1);
        nbt.putInt("timeUntilLaunch", this.timeUntilLaunch);
        if (telemetryList.size() > 0)
        {
            ListTag teleNBTList = new ListTag();
            for (BlockVec3Dim vec : new ArrayList<>(this.telemetryList))
            {
                CompoundTag tag = new CompoundTag();
                vec.writeToNBT(tag);
                teleNBTList.add(tag);
            }
            nbt.put("telemetryList", teleNBTList);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt)
    {
        this.timeUntilLaunch = nbt.getInt("timeUntilLaunch");

        boolean hasOldTags = false;

        // Backwards compatibility:
        if (nbt.getAllKeys().contains("launched"))
        {
            hasOldTags = true;

            boolean launched = nbt.getBoolean("launched");

            if (launched)
            {
                this.setLaunchPhase(EnumLaunchPhase.LAUNCHED);
            }
        }

        // Backwards compatibility:
        if (nbt.getAllKeys().contains("ignite"))
        {
            hasOldTags = true;

            int ignite = nbt.getInt("ignite");

            if (ignite == 1)
            {
                this.setLaunchPhase(EnumLaunchPhase.IGNITED);
            }
        }

        // Backwards compatibility:
        if (hasOldTags)
        {
            if (this.launchPhase < EnumLaunchPhase.LAUNCHED.ordinal() && this.launchPhase != EnumLaunchPhase.IGNITED.ordinal())
            {
                this.setLaunchPhase(EnumLaunchPhase.UNIGNITED);
            }
        }
        else
        {
            this.setLaunchPhase(EnumLaunchPhase.values()[nbt.getInt("launchPhase") - 1]);
        }

        //Update all Telemetry Units which are still tracking this rocket
        this.telemetryList.clear();
        if (nbt.contains("telemetryList"))
        {
            ListTag teleNBT = nbt.getList("telemetryList", 10);
            if (teleNBT.size() > 0)
            {
                for (int j = teleNBT.size() - 1; j >= 0; j--)
                {
                    CompoundTag tag1 = teleNBT.getCompound(j);
                    if (tag1 != null)
                    {
                        this.telemetryList.add(BlockVec3Dim.readFromNBT(tag1));
                    }
                }
                this.addToTelemetry = true;
            }
        }
    }

    public boolean getLaunched()
    {
        return this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal();
    }

    public boolean canBeRidden()
    {
        return false;
    }

    public void ignite()
    {
        this.setLaunchPhase(EnumLaunchPhase.IGNITED);
    }

    @Override
    public double getRideHeight()
    {
        return -0.9D;
    }

    public double getOnPadYOffset()
    {
        return 0D;
    }

    public void onLaunch()
    {
        GalacticraftEvents.ROCKET_LAUNCH_EVENT.invoker().invoke(this);
    }

    public void onReachAtmosphere()
    {
    }

//    @Override
//    public boolean canRiderInteract()
//    {
//        return true;
//    }

    public ResourceLocation getSpaceshipGui()
    {
        return GalacticraftRegistry.getResouceLocationForDimension(this.level.getDimension().getClass());
    }

    public void setLaunchPhase(EnumLaunchPhase phase)
    {
        this.launchPhase = phase.ordinal();
    }

    @Override
    public boolean shouldIgnoreShiftExit()
    {
        return this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal();
    }


    public void addTelemetry(ITelemetryReceiver tile)
    {
        this.telemetryList.add(new BlockVec3Dim((BlockEntity) tile));
    }

    public ArrayList<ITelemetryReceiver> getTelemetry()
    {
        ArrayList<ITelemetryReceiver> returnList = new ArrayList<>();
        for (BlockVec3Dim vec : new ArrayList<>(this.telemetryList))
        {
            BlockEntity t1 = vec.getTileEntity();
            if (t1 instanceof ITelemetryReceiver && !t1.isRemoved())
            {
                if (((ITelemetryReceiver) t1).getLinkedEntity() == this)
                {
                    returnList.add((ITelemetryReceiver) t1);
                }
            }
        }
        return returnList;
    }

    @Override
    public void transmitData(int[] data)
    {
        data[0] = this.timeUntilLaunch;
        data[1] = (int) this.getY();
        //data[2] is entity speed already set as default by ITelemetryReceiver
        data[3] = this.getScaledFuelLevel(100);
        data[4] = (int) this.xRot;
    }

    @Override
    public void receiveData(int[] data, String[] str)
    {
        //Spaceships:
        //  data0 = launch countdown
        //  data1 = height
        //  data2 = speed
        //  data3 = fuel remaining
        //  data4 = pitch angle
        int countdown = data[0];
        str[0] = "";
        str[1] = (countdown == 400) ? I18n.get("gui.rocket.on_launchpad") : ((countdown > 0) ? I18n.get("gui.rocket.countdown") + ": " + countdown / 20 : I18n.get("gui.rocket.launched"));
        str[2] = I18n.get("gui.rocket.height") + ": " + data[1];
        str[3] = GameScreenText.makeSpeedString(data[2]);
        str[4] = I18n.get("gui.message.fuel") + ": " + data[3] + "%";
    }

    @Override
    public void adjustDisplay(int[] data)
    {
        GL11.glRotatef(data[4], -1, 0, 0);
        GL11.glTranslatef(0, this.getBbHeight() / 4, 0);
    }

    /**
     * Used in RenderTier1Rocket for standard rendering. Ignore if using a custom renderer.
     *
     * @return Y-axis offset for rendering at correct position
     */
    public float getRenderOffsetY()
    {
        return 1.34F;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public int getBrightnessForRender()
//    {
//        double height = this.getPosY() + (double) this.getEyeHeight();
//        if (height > 255D)
//        {
//            height = 255D;
//        }
//        BlockPos blockpos = new BlockPos(this.getPosX(), height, this.getPosZ());
//        return this.world.isBlockLoaded(blockpos) ? this.world.getCombinedLight(blockpos, 0) : 0;
//    }
}