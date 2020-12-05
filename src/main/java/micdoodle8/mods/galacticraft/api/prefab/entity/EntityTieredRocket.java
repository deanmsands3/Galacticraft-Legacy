package micdoodle8.mods.galacticraft.api.prefab.entity;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.entity.IWorldTransferCallback;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3Dim;
import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.Dimension;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class EntityTieredRocket extends EntityAutoRocket implements IRocketType, IDockable, IWorldTransferCallback, ICameraZoomEntity
{
    public EnumRocketType rocketType;
    public float rumble;
    public int launchCooldown;
//    private final ArrayList<BlockVec3Dim> preGenList = new ArrayList<>();
//    private Iterator<BlockVec3Dim> preGenIterator = null;
    static boolean preGenInProgress = false;
    static Field marsConfigAllDimsAllowed;
    private boolean heightHalved = false;

    static
    {
        try
        {
            Class<?> marsConfig = Class.forName("micdoodle8.mods.galacticraft.planets.mars.ConfigManagerPlanets");
            marsConfigAllDimsAllowed = marsConfig.getField("launchControllerAllDims");
        }
        catch (Exception ignore)
        {
        }
    }

    public EntityTieredRocket(EntityType<? extends EntityTieredRocket> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(0.98F, 4F);
//        this.yOffset = this.height / 2.0F;
    }

//    public EntityTieredRocket(World world, double getPosX(), double getPosY(), double getPosZ())
//    {
//        super(world, getPosX(), getPosY(), getPosZ());
//    }

    public void igniteCheckingCooldown()
    {
        if (!this.level.isClientSide && this.launchCooldown <= 0)
        {
            this.initiatePlanetsPreGen(this.xChunk, this.zChunk);

            this.ignite();
        }
    }

    private void initiatePlanetsPreGen(int cx, int cz)
    {
//        this.preGenList.clear();
//
//        //Pre-generate terrain on all possible destination planets if the destination is not being controlled by a Launch Controller
//        //(note: this does NOT include the Moon!)
//
//        //This generates with a chunk radius of 12: so for 2 planets that's 1250 chunks to pregen
//        //It starts at the centre and generates in circles radiating out in case it doesn't have time to finish
//        //These will be done: 2 chunks per tick during IGNITE phase (so 800 chunks during the 20 second launch countdown)
//        //then the ones that are left 1 chunk per tick during flight (normally flight will last more than 450 ticks)
//        //If the server is at less than 20tps then maybe some of the outermost chunks won't be pre-generated but that's probably OK
//        if (this.destinationFrequency == -1 && !EntityTieredRocket.preGenInProgress)
//        {
//            ArrayList<DimensionType> toPreGen = new ArrayList<>();
//            for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
//            {
//                if (planet.getDimensionType() == this.dimension)
//                {
//                    continue;
//                }
//                if (planet.getReachable() && planet.getTierRequirement() <= this.getRocketTier() && !planet.getUnlocalizedName().equals("planet.asteroids"))
//                {
//                    toPreGen.add(planet.getDimensionType());
//                }
//            }
//
//            if (toPreGen.size() > 0)
//            {
//                for (DimensionType dimID : toPreGen)
//                {
//                    this.preGenList.add(new BlockVec3Dim(cx, 0, cz, dimID));
//                    if (ConfigManagerCore.enableDebug.get())
//                    {
//                        GCLog.info("Starting terrain pregen for dimension " + dimID + " at " + (cx * 16 + 8) + ", " + (cz * 16 + 8));
//                    }
//                }
//                for (int r = 1; r < 12; r++)  //concentric squares with radius r
//                {
//                    int xmin = cx - r;
//                    int xmax = cx + r;
//                    int zmin = cz - r;
//                    int zmax = cz + r;
//                    for (int i = -r; i < r; i++)  //stop before i == r to avoid doing corners twice
//                    {
//                        for (DimensionType dimID : toPreGen)
//                        {
//                            this.preGenList.add(new BlockVec3Dim(xmin, 0, cz + i, dimID));
//                            this.preGenList.add(new BlockVec3Dim(xmax, 0, cz - i, dimID));
//                            this.preGenList.add(new BlockVec3Dim(cx - i, 0, zmin, dimID));
//                            this.preGenList.add(new BlockVec3Dim(cx + i, 0, zmax, dimID));
//                        }
//                    }
//                }
//                this.preGenIterator = this.preGenList.iterator();
//                EntityTieredRocket.preGenInProgress = true;
//            }
//        }
//        else
//        {
//            this.preGenIterator = null;
//        }
    }

    @Override
    public void tick()
    {
        if (this.getWaitForPlayer())
        {
            if (!this.getPassengers().isEmpty())
            {
                Vec3 motion = getDeltaMovement();
                Entity passenger = this.getPassengers().get(0);
                if (this.ticks >= 40)
                {
                    if (!this.level.isClientSide)
                    {
                        this.ejectPassengers();
                        passenger.startRiding(this, true);
                        GCLog.debug("Remounting player in rocket.");
                    }

                    this.setWaitForPlayer(false);
                    setDeltaMovement(getDeltaMovement().x, -0.5, getDeltaMovement().z);
                }
                else
                {
                    this.setDeltaMovement(Vec3.ZERO);
                    passenger.setDeltaMovement(Vec3.ZERO);
                }
            }
            else
            {
//                this.motionX = this.motionY = this.motionZ = 0.0D;
                this.setDeltaMovement(0.0, 0.0, 0.0);
            }
        }

        super.tick();

        if (!this.level.isClientSide)
        {
            if (this.launchCooldown > 0)
            {
                this.launchCooldown--;
            }

//            if (this.preGenIterator != null)
//            {
//                if (this.preGenIterator.hasNext())
//                {
//                    MinecraftServer mcserver;
//                    if (this.world instanceof ServerWorld)
//                    {
//                        mcserver = ((ServerWorld) this.world).getServer();
//                        BlockVec3Dim coords = this.preGenIterator.next();
//                        World w = mcserver.getWorld(coords.dim);
//                        if (w != null)
//                        {
//                            w.getChunk(coords.x, coords.z);
//                            //Pregen a second chunk if still on launchpad (low strain on server)
//                            if (this.launchPhase < EnumLaunchPhase.LAUNCHED.ordinal() && this.preGenIterator.hasNext())
//                            {
//                                coords = this.preGenIterator.next();
//                                w = mcserver.getWorld(coords.dim);
//                                w.getChunk(coords.x, coords.z);
//                            }
//                        }
//                    }
//                }
//                else
//                {
//                    this.preGenIterator = null;
//                    EntityTieredRocket.preGenInProgress = false;
//                }
//            }
        }

        if (this.rumble > 0)
        {
            this.rumble--;
        }
        else if (this.rumble < 0)
        {
            this.rumble++;
        }

        final double rumbleAmount = this.rumble / (double) (37 - 5 * Math.max(this.getRocketTier(), 5));
        for (Entity passenger : this.getPassengers())
        {
            passenger.setPos(passenger.getX() + rumbleAmount, passenger.getY(), passenger.getZ() + rumbleAmount);
        }

        if (this.launchPhase >= EnumLaunchPhase.IGNITED.ordinal())
        {
            this.animateHurt();

            this.rumble = (float) this.random.nextInt(3) - 3;
        }

        if (!this.level.isClientSide)
        {
            this.lastLastMotionY = this.lastMotionY;
            this.lastMotionY = this.getDeltaMovement().y;
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        this.rocketType = EnumRocketType.values()[buffer.readInt()];
        super.decodePacketdata(buffer);

        if (buffer.readBoolean())
        {
            this.setPosRaw(buffer.readDouble() / 8000.0D, buffer.readDouble() / 8000.0D, buffer.readDouble() / 8000.0D);
        }
    }

    @Override
    public void getNetworkedData(ArrayList<Object> list)
    {
        if (this.level.isClientSide)
        {
            return;
        }
        list.add(this.rocketType != null ? this.rocketType.getIndex() : 0);
        super.getNetworkedData(list);

        boolean sendPosUpdates = this.ticks < 25 || this.launchPhase < EnumLaunchPhase.LAUNCHED.ordinal();
        list.add(sendPosUpdates);

        if (sendPosUpdates)
        {
            list.add(this.getX() * 8000.0D);
            list.add(this.getY() * 8000.0D);
            list.add(this.getZ() * 8000.0D);
        }
    }

    @Override
    public void onReachAtmosphere()
    {
        //Launch controlled
        if (this.destinationFrequency != -1)
        {
            if (this.level.isClientSide)
            {
                //stop the sounds on the client - but do not reset, the rocket may start again
                this.stopRocketSound();
                return;
            }

            this.setTarget(true, this.destinationFrequency);

            if (this.targetVec != null)
            {
                if (this.targetDimension != this.level.getDimension().getType())
                {
                    Dimension targetDim = WorldUtil.getProviderForDimensionServer(this.targetDimension);
                    if (targetDim != null && targetDim.getWorld() instanceof ServerLevel)
                    {
                        boolean dimensionAllowed = this.targetDimension == DimensionType.OVERWORLD;

                        if (targetDim instanceof IGalacticraftDimension)
                        {
                            dimensionAllowed = ((IGalacticraftDimension) targetDim).canSpaceshipTierPass(this.getRocketTier());
                        }
                        else
                            //No rocket flight to non-Galacticraft dimensions other than the Overworld allowed unless config
                            if (/*(this.targetDimension > 1 || this.targetDimension < -1) &&*/ marsConfigAllDimsAllowed != null)
                            {
                                try
                                {
                                    if (marsConfigAllDimsAllowed.getBoolean(null))
                                    {
                                        dimensionAllowed = true;
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }

                        if (dimensionAllowed)
                        {
                            if (!this.getPassengers().isEmpty())
                            {
                                for (Entity passenger : this.getPassengers())
                                {
                                    if (passenger instanceof ServerPlayer)
                                    {
                                        WorldUtil.transferEntityToDimension(passenger, this.targetDimension, (ServerLevel) targetDim.getWorld(), false, this);
                                    }
                                }
                            }
                            else
                            {
                                Entity e = WorldUtil.transferEntityToDimension(this, this.targetDimension, (ServerLevel) targetDim.getWorld(), false, null);
                                if (e instanceof EntityAutoRocket)
                                {
                                    e.setPos(this.targetVec.getX() + 0.5F, this.targetVec.getY() + 800, this.targetVec.getZ() + 0.5f);
                                    ((EntityAutoRocket) e).setLaunchPhase(EnumLaunchPhase.LANDING);
                                    ((EntityAutoRocket) e).setWaitForPlayer(false);
                                }
                                else
                                {
                                    GCLog.info("Error: failed to recreate the unmanned rocket in landing mode on target planet.");
                                    e.remove();
                                    this.remove();
                                }
                            }
                            return;
                        }
                    }
                    //No destination world found - in this situation continue into regular take-off (as if Not launch controlled)
                }
                else
                {
                    //Same dimension controlled rocket flight
                    this.setPos(this.targetVec.getX() + 0.5F, this.targetVec.getY() + 800, this.targetVec.getZ() + 0.5F);
                    //Stop any lateral motion, otherwise it will update to an incorrect x,z position first tick after spawning above target
                    //Small upward motion initially, to keep clear of own flame trail from launch
                    this.setDeltaMovement(0.0, 0.1, 0.0);
                    for (Entity passenger : this.getPassengers())
                    {
                        if (passenger instanceof ServerPlayer)
                        {
//                            WorldUtil.forceMoveEntityToPos(passenger, (ServerWorld) this.world, new Vector3(this.targetVec.getX() + 0.5F, this.targetVec.getY() + 800, this.targetVec.getZ() + 0.5F), false); TODO Force load chunk when tp in same dimension?
                            this.setWaitForPlayer(true);
                            GCLog.debug("Rocket repositioned, waiting for player");
                        }
                    }
                    this.setLaunchPhase(EnumLaunchPhase.LANDING);
                    //Do not destroy the rocket, we still need it!
                    return;
                }
            }
            else
            {
                //Launch controlled launch but no valid target frequency = rocket loss [INVESTIGATE]
                GCLog.info("Error: the launch controlled rocket failed to find a valid landing spot when it reached space.");
                this.fuelTank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
                this.setPosRaw(this.getX(), Math.max(255, (this.level.getDimension() instanceof IExitHeight ? ((IExitHeight) this.level.getDimension()).getYCoordinateToTeleport() : 1200) - 200), this.getZ());
                return;
            }
        }

        //Not launch controlled
        if (!this.level.isClientSide)
        {
            for (Entity e : this.getPassengers())
            {
                if (e instanceof ServerPlayer)
                {
                    ServerPlayer player = (ServerPlayer) e;

                    this.onTeleport(player);
                    GCPlayerStats stats = GCPlayerStats.get(player);
                    WorldUtil.toCelestialSelection(player, stats, this.getRocketTier());
                }
            }

            //Destroy any rocket which reached the top of the atmosphere and is not controlled by a Launch Controller
            this.remove();
        }

        //Client LogicalSide, non-launch controlled, do nothing - no reason why it can't continue flying until the GUICelestialSelection activates
    }

    @Override
    protected boolean shouldCancelExplosion()
    {
        return this.hasValidFuel() && Math.abs(this.lastLastMotionY) < 4;
    }

    public void onTeleport(ServerPlayer player)
    {
    }

    @Override
    protected void onRocketLand(BlockPos pos)
    {
        super.onRocketLand(pos);
        this.launchCooldown = 40;
    }

    @Override
    public void onLaunch()
    {
        super.onLaunch();
    }

    @Override
    protected boolean shouldMoveClientSide()
    {
        return true;
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn)
    {
        if (heightHalved)
        {
            return super.getDimensions(poseIn).scale(1.0F, 0.5F);
        }
        return super.getDimensions(poseIn);
    }

    @Override
    public boolean interact(Player player, InteractionHand hand)
    {
        if (hand != InteractionHand.MAIN_HAND)
        {
            return false;
        }

        if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal())
        {
            return false;
        }

        if (!this.getPassengers().isEmpty() && this.getPassengers().contains(player))
        {
            if (!this.level.isClientSide)
            {
                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, this.level.getDimension().getType(), new Object[]{}), (ServerPlayer) player);
                GCPlayerStats stats = GCPlayerStats.get(player);
                stats.setChatCooldown(0);
                // Prevent player being dropped from the top of the rocket...
                float heightBefore = this.getBbHeight();
                heightHalved = true;
                this.refreshDimensions();
//                this.height = this.height / 2.0F;
                this.ejectPassengers();
//                this.height = heightBefore;
                heightHalved = false;
                this.refreshDimensions();
            }

            return true;
        }
        else if (player instanceof ServerPlayer)
        {
            if (!this.level.isClientSide)
            {
                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_DISPLAY_ROCKET_CONTROLS, this.level.getDimension().getType(), new Object[]{}), (ServerPlayer) player);
                GCPlayerStats stats = GCPlayerStats.get(player);
                stats.setChatCooldown(0);
                player.startRiding(this);
            }

            return true;
        }

        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        if (level.isClientSide)
        {
            return;
        }
        nbt.putInt("Type", this.rocketType.getIndex());
        super.addAdditionalSaveData(nbt);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt)
    {
        this.rocketType = EnumRocketType.values()[nbt.getInt("Type")];
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public EnumRocketType getRocketType()
    {
        return this.rocketType;
    }

    @Override
    public int getContainerSize()
    {
        if (this.rocketType == null)
        {
            return 2;
        }
        return this.rocketType.getInventorySpace();
    }

    @Override
    public void onWorldTransferred(Level world)
    {
        if (this.targetVec != null)
        {
            this.setPos(this.targetVec.getX() + 0.5F, this.targetVec.getY() + 800, this.targetVec.getZ() + 0.5F);
            this.setLaunchPhase(EnumLaunchPhase.LANDING);
            this.setWaitForPlayer(true);
            this.setDeltaMovement(Vec3.ZERO);
//            this.motionX = this.motionY = this.motionZ = 0.0D;
        }
        else
        {
            this.remove();
        }
    }

    @Override
    public float getRotateOffset()
    {
        return -1.5F;
    }

    @Override
    public boolean isPlayerRocket()
    {
        return true;
    }
}
