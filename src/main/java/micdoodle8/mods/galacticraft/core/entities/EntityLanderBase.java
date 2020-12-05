package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.fluid.GCFluidRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.network.PacketDynamicInventory;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class EntityLanderBase extends EntityAdvancedMotion implements IInventorySettable, IScaleableFuelLevel
{
    private final int FUEL_TANK_CAPACITY = 5000;
    public FluidTank fuelTank = new FluidTank(this.FUEL_TANK_CAPACITY);
    protected boolean hasReceivedPacket;
    private boolean lastShouldMove;
    private UUID persistantRiderUUID;
    private Boolean shouldMoveClient;
    private Boolean shouldMoveServer;
    private ArrayList prevData;
    private boolean networkDataChanged;
    private boolean syncAdjustFlag = true;

    public EntityLanderBase(EntityType<?> type, Level world)
    {
        super(type, world);
//        this.setSize(3.0F, 3.0F);
    }

    public EntityLanderBase(EntityType<?> type, Level world, double x, double y, double z)
    {
        this(type, world);
        this.setPos(x, y, z);
    }

    public EntityLanderBase(EntityType<?> type, ServerPlayer player)
    {
        this(type, player.level, player.getX(), player.getY(), player.getZ());

        GCPlayerStats stats = GCPlayerStats.get(player);
        this.stacks = NonNullList.withSize(stats.getRocketStacks().size() + 1, ItemStack.EMPTY);
        this.fuelTank.setFluid(new FluidStack(GCFluids.FUEL.getFluid(), stats.getFuelLevel()));

        for (int i = 0; i < stats.getRocketStacks().size(); i++)
        {
            if (!stats.getRocketStacks().get(i).isEmpty())
            {
                this.stacks.set(i, stats.getRocketStacks().get(i).copy());
            }
            else
            {
                this.stacks.get(i).setCount(0);
            }
        }

        this.absMoveTo(player.getX(), player.getY(), player.getZ(), 0, 0);

        player.startRiding(this, true);
    }

    @Override
    public void positionRider(Entity passenger)
    {
        if (this.hasPassenger(passenger))
        {
            passenger.setPos(this.getX(), this.getY() + this.getRideHeight() + passenger.getRidingHeight(), this.getZ());
        }
    }

    @Override
    public boolean shouldSendAdvancedMotionPacket()
    {
        return this.shouldMoveClient != null && this.shouldMoveServer != null;
    }

    @Override
    public boolean canSetPositionClient()
    {
        return this.shouldSendAdvancedMotionPacket();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        super.lerpTo(x, y, z, yaw, pitch, posRotationIncrements, b);
        if (this.syncAdjustFlag && this.level.hasChunkAt(new BlockPos(x, 255D, z)))
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

                this.syncAdjustFlag = false;
            }
        }
    }

    @Override
    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.fuelTank.getFluid() == FluidStack.EMPTY ? 0 : this.fuelTank.getFluid().getAmount();

        return (int) (fuelLevel * i / this.FUEL_TANK_CAPACITY);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.ticks < 40 && this.getY() > 150)
        {
            if (this.getPassengers().isEmpty())
            {
                final Player player = this.level.getNearestPlayer(this, 5);

                if (player != null && player.getVehicle() == null)
                {
                    player.startRiding(this);
                }
            }
        }

        if (!this.level.isClientSide)
        {
            this.checkFluidTankTransfer(this.stacks.size() - 1, this.fuelTank);
        }

        AABB box = this.getBoundingBox().inflate(0.2D, 0.4D, 0.2D);

        final List<Entity> var15 = this.level.getEntities(this, box);

        if (var15 != null && !var15.isEmpty())
        {
            for (Entity entity : var15)
            {
                if (!this.getPassengers().contains(entity))
                {
                    this.pushEntityAway(entity);
                }
            }
        }
    }


    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        FluidUtil.tryFillContainerFuel(tank, this.stacks, slot);
    }

    private void pushEntityAway(Entity entityToPush)
    {
        if (!this.getPassengers().contains(entityToPush) && this.getVehicle() != entityToPush)
        {
            double d0 = this.getX() - entityToPush.getX();
            double d1 = this.getZ() - entityToPush.getZ();
            double d2 = Mth.absMax(d0, d1);

            if (d2 >= 0.009999999776482582D)
            {
                d2 = Mth.sqrt(d2);
                d0 /= d2;
                d1 /= d2;
                double d3 = 1.0D / d2;

                if (d3 > 1.0D)
                {
                    d3 = 1.0D;
                }

                d0 *= d3;
                d1 *= d3;
                d0 *= 0.05000000074505806D;
                d1 *= 0.05000000074505806D;
                d0 *= 1.0F - entityToPush.pushthrough;
                d1 *= 1.0F - entityToPush.pushthrough;
                entityToPush.push(-d0, 0.0D, -d1);
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt)
    {
        int invSize = nbt.getInt("rocketStacksLength");
        if (invSize < 3)
        {
            invSize = 3;
        }
        this.stacks = NonNullList.withSize(invSize, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.stacks);

        if (nbt.contains("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompound("fuelTank"));
        }

        if (nbt.contains("RiderUUID_LSB"))
        {
            this.persistantRiderUUID = new UUID(nbt.getLong("RiderUUID_LSB"), nbt.getLong("RiderUUID_MSB"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        if (level.isClientSide)
        {
            return;
        }
        nbt.putInt("rocketStacksLength", this.stacks.size());

        ContainerHelper.saveAllItems(nbt, this.stacks);

        if (this.fuelTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("fuelTank", this.fuelTank.writeToNBT(new CompoundTag()));
        }

        UUID id = this.getOwnerUUID();

        if (id != null)
        {
            nbt.putLong("RiderUUID_LSB", id.getLeastSignificantBits());
            nbt.putLong("RiderUUID_MSB", id.getMostSignificantBits());
        }
    }

    @Override
    public boolean shouldMove()
    {
        if (this.shouldMoveClient == null || this.shouldMoveServer == null)
        {
            return false;
        }

        if (this.ticks < 40)
        {
            return false;
        }

        return !this.onGround;
    }

    public abstract double getInitialMotionY();

    @Override
    public void tickInAir()
    {
        if (this.level.isClientSide)
        {
            if (!this.shouldMove())
            {
                this.setDeltaMovement(0.0, 0.0, 0.0);
            }

            if (this.shouldMove() && !this.lastShouldMove)
            {
                this.setDeltaMovement(getDeltaMovement().x, this.getInitialMotionY(), this.getDeltaMovement().z);
            }

            this.lastShouldMove = this.shouldMove();
        }
    }

    @Override
    public ArrayList<Object> getNetworkedData()
    {
        final ArrayList<Object> objList = new ArrayList<Object>();

        if (!this.level.isClientSide)
        {
            Integer cargoLength = this.stacks != null ? this.stacks.size() : 0;
            objList.add(cargoLength);
            objList.add(this.fuelTank.getFluid() == FluidStack.EMPTY ? 0 : this.fuelTank.getFluid().getAmount());
        }

        if (this.level.isClientSide)
        {
            this.shouldMoveClient = this.shouldMove();
            objList.add(this.shouldMoveClient);
        }
        else
        {
            this.shouldMoveServer = this.shouldMove();
            objList.add(this.shouldMoveServer);
            //Server send rider information for client to check
            objList.add(this.getPassengers().isEmpty() ? -1 : this.getPassengers().get(0).getId());
        }

        this.networkDataChanged = !objList.equals(this.prevData);
        this.prevData = objList;
        return objList;
    }

    @Override
    public boolean networkedDataChanged()
    {
        return this.networkDataChanged || this.shouldMoveClient == null || this.shouldMoveServer == null;
    }

    @Override
    public boolean canRiderInteract()
    {
        return true;
    }

    @Override
    public int getPacketTickSpacing()
    {
        return 2;
    }

    @Override
    public double getPacketSendDistance()
    {
        return 250.0D;
    }

    @Override
    public void readNetworkedData(ByteBuf buffer)
    {
        try
        {
            if (this.level.isClientSide)
            {
                if (!this.hasReceivedPacket)
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
                    this.hasReceivedPacket = true;
                }

                int cargoLength = buffer.readInt();
                if (this.stacks == null || this.stacks.isEmpty())
                {
                    this.stacks = NonNullList.withSize(cargoLength, ItemStack.EMPTY);
                    GalacticraftCore.packetPipeline.sendToServer(new PacketDynamicInventory(this));
                }

                this.fuelTank.setFluid(new FluidStack(GCFluids.FUEL.getFluid(), buffer.readInt()));

                this.shouldMoveServer = buffer.readBoolean();

                //Check has correct rider on client
                int shouldBeMountedId = buffer.readInt();
                if (this.getPassengers().isEmpty())
                {
                    if (shouldBeMountedId > -1)
                    {
                        Entity e = Minecraft.getInstance().level.getEntity(shouldBeMountedId);
                        if (e != null)
                        {
                            if (e.dimension != this.dimension)
                            {
                                if (e instanceof Player)
                                {
                                    e = WorldUtil.forceRespawnClient(this.dimension, e.level.getLevelData().getGeneratorType(), ((ServerPlayer) e).gameMode.getGameModeForPlayer());
                                    e.startRiding(this);
                                    this.syncAdjustFlag = true;
                                }
                            }
                            else
                            {
                                e.startRiding(this);
                                this.syncAdjustFlag = true;
                            }
                        }
                    }
                }
                else if (this.getPassengers().get(0).getId() != shouldBeMountedId)
                {
                    if (shouldBeMountedId == -1)
                    {
                        this.ejectPassengers();
                    }
                    else
                    {
                        Entity e = Minecraft.getInstance().level.getEntity(shouldBeMountedId);
                        if (e != null)
                        {
                            if (e.dimension != this.dimension)
                            {
                                if (e instanceof Player)
                                {
                                    e = WorldUtil.forceRespawnClient(this.dimension, e.level.getLevelData().getGeneratorType(), ((ServerPlayer) e).gameMode.getGameModeForPlayer());
                                    e.startRiding(this, true);
                                    this.syncAdjustFlag = true;
                                }
                            }
                            else
                            {
                                e.startRiding(this, true);
                                this.syncAdjustFlag = true;
                            }
                        }
                    }
                }
            }
            else
            {
                this.shouldMoveClient = buffer.readBoolean();
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean allowDamageSource(DamageSource damageSource)
    {
        return !damageSource.isExplosion();
    }

    @Override
    public List<ItemStack> getItemsDropped()
    {
        return this.stacks;
    }

    @Override
    public int getContainerSize()
    {
        return this.stacks.size();
    }

    @Override
    public void setSizeInventory(int size)
    {
        this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public boolean canPlaceItem(int var1, ItemStack var2)
    {
        return false;
    }

    @Override
    public double getPacketRange()
    {
        return 50.0D;
    }

    @Override
    public UUID getOwnerUUID()
    {
        if (!this.getPassengers().isEmpty() && !(this.getPassengers().get(0) instanceof Player))
        {
            return null;
        }

        UUID id;

        if (!this.getPassengers().isEmpty())
        {
            id = this.getPassengers().get(0).getUUID();

            this.persistantRiderUUID = id;
        }
        else
        {
            id = this.persistantRiderUUID;
        }

        return id;
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