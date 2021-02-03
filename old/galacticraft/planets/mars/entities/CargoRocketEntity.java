package micdoodle8.mods.galacticraft.planets.mars.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.entity.IWorldTransferCallback;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.GCParticles;
import micdoodle8.mods.galacticraft.core.client.fx.EntityParticleData;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.Dimension;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;

public class CargoRocketEntity extends EntityAutoRocket implements IRocketType, Container, IWorldTransferCallback
{
    public EnumRocketType rocketType;
    public float rumble;

    public CargoRocketEntity(EntityType<? extends CargoRocketEntity> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(0.98F, 2F);
    }

    public static CargoRocketEntity createEntityCargoRocket(Level world, double x, double y, double z, EnumRocketType rocketType)
    {
        CargoRocketEntity rocket = new CargoRocketEntity(MarsEntities.CARGO_ROCKET, world);
        rocket.setPos(x, y, z);
        rocket.xo = x;
        rocket.yo = y;
        rocket.zo = z;
        rocket.rocketType = rocketType;
        rocket.stacks = NonNullList.withSize(rocket.getContainerSize(), ItemStack.EMPTY);
//        rocket.setSize(0.98F, 2F);
        return rocket;
    }

    public static Item getItemFromType(EnumRocketType rocketType)
    {
        switch (rocketType)
        {
        default:
        case DEFAULT:
        case INVENTORY27:
            return MarsItems.CARGO_ROCKET_18_INVENTORY;
        case INVENTORY36:
            return MarsItems.CARGO_ROCKET_36_INVENTORY;
        case INVENTORY54:
            return MarsItems.CARGO_ROCKET_54_INVENTORY;
        case PREFUELED:
            return MarsItems.CREATIVE_CARGO_ROCKET;
        }
    }

    public static EnumRocketType getTypeFromItem(Item item)
    {
        if (item == MarsItems.CARGO_ROCKET_18_INVENTORY)
        {
            return EnumRocketType.INVENTORY27;
        }
        if (item == MarsItems.CARGO_ROCKET_36_INVENTORY)
        {
            return EnumRocketType.INVENTORY36;
        }
        if (item == MarsItems.CARGO_ROCKET_54_INVENTORY)
        {
            return EnumRocketType.INVENTORY54;
        }
        if (item == MarsItems.CREATIVE_CARGO_ROCKET)
        {
            return EnumRocketType.PREFUELED;
        }
        return null;
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData()
    {
    }

    @Override
    public int getFuelTankCapacity()
    {
        return 2000;
    }

    public float getCargoFilledAmount()
    {
        float weight = 1;

        for (ItemStack stack : this.stacks)
        {
            if (stack != null && !stack.isEmpty())
            {
                weight += 0.1D;
            }
        }

        return weight;
    }

    @Override
    public ItemStack getPickedResult(HitResult target)
    {
//        return new ItemStack(MarsItems.rocketMars, 1, this.rocketType.getIndex() + 10);
        return new ItemStack(getItemFromType(rocketType));
    }

    @Override
    public void tick()
    {
        if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal() && this.hasValidFuel())
        {
            double motionScalar = this.timeSinceLaunch / 250;

            motionScalar = Math.min(motionScalar, 1);

            double modifier = this.getCargoFilledAmount();
            motionScalar *= 5.0D / modifier;

            if (this.launchPhase != EnumLaunchPhase.LANDING.ordinal())
            {
                if (motionScalar != 0.0)
                {
//                    this.motionY = ;
                    this.setDeltaMovement(this.getDeltaMovement().x, -motionScalar * Math.cos((this.xRot - 180) / Constants.RADIANS_TO_DEGREES_D), this.getDeltaMovement().z);
                }
            }

            double multiplier = 1.0D;

            if (this.level.getDimension() instanceof IGalacticraftDimension)
            {
                multiplier = ((IGalacticraftDimension) this.level.getDimension()).getFuelUsageMultiplier();

                if (multiplier <= 0)
                {
                    multiplier = 1;
                }
            }

            if (this.timeSinceLaunch % Mth.floor(3 * (1 / multiplier)) == 0)
            {
                this.removeFuel(1);
                if (!this.hasValidFuel())
                {
                    this.stopRocketSound();
                }
            }
        }
        else if (!this.hasValidFuel() && this.getLaunched())
        {
            if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
            {
//                this.motionY -= ;
                this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20, this.getDeltaMovement().z);
            }
        }

        super.tick();

        if (this.rumble > 0)
        {
            this.rumble--;
        }

        if (this.rumble < 0)
        {
            this.rumble++;
        }

        if (this.launchPhase >= EnumLaunchPhase.IGNITED.ordinal())
        {
            this.animateHurt();

            this.rumble = (float) this.random.nextInt(3) - 3;
        }

        int i;

        if (this.timeUntilLaunch >= 100)
        {
            i = Math.abs(this.timeUntilLaunch / 100);
        }
        else
        {
            i = 1;
        }

        if ((this.getLaunched() || this.launchPhase == EnumLaunchPhase.IGNITED.ordinal() && this.random.nextInt(i) == 0) && !ConfigManagerCore.disableSpaceshipParticles.get() && this.hasValidFuel())
        {
            if (this.level.isClientSide)
            {
                this.spawnParticles(this.getLaunched());
            }
        }
    }

    @Override
    protected boolean shouldMoveClientSide()
    {
        return true;
    }

    protected void spawnParticles(boolean launched)
    {
        double sinPitch = Math.sin(this.xRot / Constants.RADIANS_TO_DEGREES_D);
        double x1 = 2 * Math.cos(this.yRot / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
        double z1 = 2 * Math.sin(this.yRot / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
        double y1 = 2 * Math.cos((this.xRot - 180) / Constants.RADIANS_TO_DEGREES_D);

        if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() && this.targetVec != null)
        {
            double modifier = this.getY() - this.targetVec.getY();
            modifier = Math.max(modifier, 1.0);
            x1 *= modifier / 60.0D;
            y1 *= modifier / 60.0D;
            z1 *= modifier / 60.0D;
        }

        final double y = this.yo + (this.getY() - this.yo) - 0.4;

        if (this.isAlive())
        {
            LivingEntity riddenByEntity = this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof LivingEntity) ? null : (LivingEntity) this.getPassengers().get(0);
            EntityParticleData particleData = new EntityParticleData(this.getLaunched() ? GCParticles.LAUNCH_FLAME_LAUNCHED : GCParticles.LAUNCH_FLAME_IDLE, riddenByEntity.getUUID());
            this.level.addParticle(particleData, this.getX() + 0.2 - this.random.nextDouble() / 10 + x1, y, this.getZ() + 0.2 - this.random.nextDouble() / 10 + z1, x1, y1, z1);
            this.level.addParticle(particleData, this.getX() - 0.2 + this.random.nextDouble() / 10 + x1, y, this.getZ() + 0.2 - this.random.nextDouble() / 10 + z1, x1, y1, z1);
            this.level.addParticle(particleData, this.getX() - 0.2 + this.random.nextDouble() / 10 + x1, y, this.getZ() - 0.2 + this.random.nextDouble() / 10 + z1, x1, y1, z1);
            this.level.addParticle(particleData, this.getX() + 0.2 - this.random.nextDouble() / 10 + x1, y, this.getZ() - 0.2 + this.random.nextDouble() / 10 + z1, x1, y1, z1);
            this.level.addParticle(particleData, this.getX() + x1, y, this.getZ() + z1, x1, y1, z1);
            this.level.addParticle(particleData, this.getX() + 0.2 + x1, y, this.getZ() + z1, x1, y1, z1);
            this.level.addParticle(particleData, this.getX() - 0.2 + x1, y, this.getZ() + z1, x1, y1, z1);
            this.level.addParticle(particleData, this.getX() + x1, y, this.getZ() + 0.2D + z1, x1, y1, z1);
            this.level.addParticle(particleData, this.getX() + x1, y, this.getZ() - 0.2D + z1, x1, y1, z1);
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        this.rocketType = EnumRocketType.values()[buffer.readInt()];
        super.decodePacketdata(buffer);
        this.setPosRaw(buffer.readDouble() / 8000.0D, buffer.readDouble() / 8000.0D, buffer.readDouble() / 8000.0D);
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
        list.add(this.getX() * 8000.0D);
        list.add(this.getY() * 8000.0D);
        list.add(this.getZ() * 8000.0D);
    }

    @Override
    public void onReachAtmosphere()
    {
        if (this.level.isClientSide)
        {
            //stop the sounds on the client - but do not reset, the rocket may start again
            this.stopRocketSound();
            return;
        }

        GCLog.debug("[Serverside] Cargo rocket reached space, heading to " + this.destinationFrequency);
        this.setTarget(true, this.destinationFrequency);

        if (this.targetVec != null)
        {
            GCLog.debug("Destination location = " + this.targetVec.toString());
            if (this.targetDimension != GCCoreUtil.getDimensionType(this.level))
            {
                GCLog.debug("Destination is in different dimension: " + this.targetDimension);
                Dimension targetDim = WorldUtil.getProviderForDimensionServer(this.targetDimension);
                if (targetDim != null && targetDim.getWorld() instanceof ServerLevel)
                {
                    GCLog.debug("Loaded destination dimension " + this.targetDimension);
                    this.setPos(this.targetVec.getX() + 0.5F, this.targetVec.getY() + 800, this.targetVec.getZ() + 0.5F);
                    Entity e = WorldUtil.transferEntityToDimension(this, this.targetDimension, (ServerLevel) targetDim.getWorld(), false, null);

                    if (e instanceof CargoRocketEntity)
                    {
                        GCLog.debug("Cargo rocket arrived at destination dimension, going into landing mode.");
                        e.setPos(this.targetVec.getX() + 0.5F, this.targetVec.getY() + 800, this.targetVec.getZ() + 0.5F);
                        ((CargoRocketEntity) e).setLaunchPhase(EnumLaunchPhase.LANDING);
                        //No setDead() following successful transferEntityToDimension() - see javadoc on that
                    }
                    else
                    {
                        GCLog.info("Error: failed to recreate the cargo rocket in landing mode on target planet.");
                        e.remove();
                        this.remove();
                    }
                    return;
                }
                GCLog.info("Error: the server failed to load the dimension the cargo rocket is supposed to land in. Destroying rocket!");
                this.remove();
                return;
            }
            else
            {
                GCLog.debug("Cargo rocket going into landing mode in same destination.");
                this.setPos(this.targetVec.getX() + 0.5F, this.targetVec.getY() + 800, this.targetVec.getZ() + 0.5F);
                this.setLaunchPhase(EnumLaunchPhase.LANDING);
                return;
            }
        }
        else
        {
            GCLog.info("Error: the cargo rocket failed to find a valid landing spot when it reached space.");
            this.remove();
        }
    }

    @Override
    public boolean interact(Player player, InteractionHand hand)
    {
        if (!this.level.isClientSide && player instanceof ServerPlayer)
        {
//            MarsUtil.openCargoRocketInventory((ServerPlayerEntity) player, this); TODO guis
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
    public void readAdditionalSaveData(CompoundTag nbt)
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
            return 0;
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
        }
        else
        {
            this.remove();
        }
    }

    @Override
    public int getRocketTier()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getPreLaunchWait()
    {
        return 20;
    }

    @Override
    public List<ItemStack> getItemsDropped(List<ItemStack> droppedItemList)
    {
        super.getItemsDropped(droppedItemList);
//        ItemStack rocket = new ItemStack(MarsItems.rocketMars, 1, this.rocketType.getIndex() + 10);
        ItemStack rocket = new ItemStack(getItemFromType(rocketType));
        rocket.setTag(new CompoundTag());
        rocket.getTag().putInt("RocketFuel", this.fuelTank.getFluidAmount());
        droppedItemList.add(rocket);
        return droppedItemList;
    }

    @Override
    public boolean isPlayerRocket()
    {
        return false;
    }

    @Override
    public double getOnPadYOffset()
    {
        return -0.05D;
    }

    @Override
    public float getRenderOffsetY()
    {
        return -0.1F;
    }
}
