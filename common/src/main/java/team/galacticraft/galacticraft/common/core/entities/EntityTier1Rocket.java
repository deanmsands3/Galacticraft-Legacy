package team.galacticraft.galacticraft.common.core.entities;

import team.galacticraft.galacticraft.common.api.prefab.entity.EntityTieredRocket;
import team.galacticraft.galacticraft.common.api.tile.IFuelDock;
import team.galacticraft.galacticraft.common.api.world.IGalacticraftDimension;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCItems;
import team.galacticraft.galacticraft.common.core.client.GCParticles;
import team.galacticraft.galacticraft.common.core.client.fx.EntityParticleData;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.core.tile.TileEntityLandingPad;
import team.galacticraft.galacticraft.common.core.util.ConfigManagerCore;
import team.galacticraft.galacticraft.common.core.util.PlayerUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;

import java.util.List;

public class EntityTier1Rocket extends EntityTieredRocket
{
    public static int FUEL_CAPACITY = 1000 * ConfigManagerCore.rocketFuelFactor.get();

    public EntityTier1Rocket(EntityType<? extends EntityTier1Rocket> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(1.2F, 3.5F);
//        this.yOffset = 1.5F;
    }

    @Override
    protected void defineSynchedData()
    {
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    //    public EntityTier1Rocket(World par1World, double par2, double par4, double par6, EnumRocketType rocketType)
//    {
//        super(par1World, par2, par4, par6);
//        this.rocketType = rocketType;
//        this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
//        this.setSize(1.2F, 3.5F);
////        this.yOffset = 1.5F;
//    }

    public static Item getItemFromType(EnumRocketType rocketType)
    {
        switch (rocketType)
        {
        default:
        case DEFAULT:
            return GCItems.rocketTierOne;
        case INVENTORY27:
            return GCItems.rocketTierOneCargo1;
        case INVENTORY36:
            return GCItems.rocketTierOneCargo2;
        case INVENTORY54:
            return GCItems.rocketTierOneCargo3;
        case PREFUELED:
            return GCItems.rocketTierOneCreative;
        }
    }

    public static EnumRocketType getTypeFromItem(Item item)
    {
        if (item == GCItems.rocketTierOne)
        {
            return EnumRocketType.DEFAULT;
        }
        if (item == GCItems.rocketTierOneCargo1)
        {
            return EnumRocketType.INVENTORY27;
        }
        if (item == GCItems.rocketTierOneCargo2)
        {
            return EnumRocketType.INVENTORY36;
        }
        if (item == GCItems.rocketTierOneCargo3)
        {
            return EnumRocketType.INVENTORY54;
        }
        if (item == GCItems.rocketTierOneCreative)
        {
            return EnumRocketType.PREFUELED;
        }
        return null;
    }

    @Override
    public double getRideHeight()
    {
        return 0.3D;
    }

    @Override
    public float getRotateOffset()
    {
        return -1.5F;
    }

    @Override
    public ItemStack getPickedResult(HitResult target)
    {
        return new ItemStack(getItemFromType(getRocketType()), 1);
    }

    @Override
    public void tick()
    {
        super.tick();

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

        if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal() && this.hasValidFuel())
        {
            if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal())
            {
                double d = this.timeSinceLaunch / 150;

                if (this.level.getDimension() instanceof IGalacticraftDimension && ((IGalacticraftDimension) this.level.getDimension()).hasNoAtmosphere())
                {
                    d = Math.min(d * 1.2, 1.6);
                }
                else
                {
                    d = Math.min(d, 1);
                }

                if (d != 0.0)
                {
                    setDeltaMovement(getDeltaMovement().x, -d * Math.cos((this.xRot - 180) / Constants.RADIANS_TO_DEGREES_D), getDeltaMovement().z);
                }
            }
            else
            {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.008, 0.0));
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
        else if (!this.hasValidFuel() && this.getLaunched() && !this.level.isClientSide)
        {
            if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
            {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -(Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20), 0.0));
//                this.motionY -= Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
            }
        }
    }

    @Override
    public void onTeleport(ServerPlayer player)
    {
        final ServerPlayer playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

        if (playerBase != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);

            if (this.stacks == null || this.stacks.isEmpty())
            {
                stats.setRocketStacks(NonNullList.withSize(2, ItemStack.EMPTY));
            }
            else
            {
                stats.setRocketStacks(this.stacks);
            }

            stats.setRocketItem(getItemFromType(getRocketType()));
            stats.setFuelLevel(this.fuelTank.getFluidAmount());
        }
    }

    protected void spawnParticles(boolean launched)
    {
        if (this.isAlive())
        {
            double sinPitch = Math.sin(this.xRot / Constants.RADIANS_TO_DEGREES_D);
            double x1 = 2 * Math.cos(this.yRot / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            double z1 = 2 * Math.sin(this.yRot / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            double y1 = 2 * Math.cos((this.xRot - 180) / Constants.RADIANS_TO_DEGREES_D);

            if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() && this.targetVec != null)
            {
                double modifier = this.getY() - this.targetVec.getY();
                modifier = Math.min(Math.max(modifier, 120.0), 300.0);
                x1 *= modifier / 100.0D;
                y1 *= modifier / 100.0D;
                z1 *= modifier / 100.0D;
            }

            double y = this.yo + (this.getY() - this.yo) + y1 - this.getDeltaMovement().y + 1.2D;

            final double x2 = this.getX() + x1 - this.getDeltaMovement().x;
            final double z2 = this.getZ() + z1 - this.getDeltaMovement().z;

            LivingEntity riddenByEntity = !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof LivingEntity ? (LivingEntity) this.getPassengers().get(0) : null;

            if (this.getLaunched())
            {
//                Vector3 motionVec = new Vector3((float)x1, (float)y1, (float)z1);
//                Object[] rider = new Object[] { riddenByEntity };
                EntityParticleData particleData = new EntityParticleData(GCParticles.LAUNCH_FLAME_LAUNCHED, riddenByEntity == null ? null : riddenByEntity.getUUID());
                this.level.addParticle(particleData, x2 + 0.4 - this.random.nextDouble() / 10D, y, z2 + 0.4 - this.random.nextDouble() / 10D, x1, y1, z1);
                this.level.addParticle(particleData, x2 - 0.4 + this.random.nextDouble() / 10D, y, z2 + 0.4 - this.random.nextDouble() / 10D, x1, y1, z1);
                this.level.addParticle(particleData, x2 - 0.4 + this.random.nextDouble() / 10D, y, z2 - 0.4 + this.random.nextDouble() / 10D, x1, y1, z1);
                this.level.addParticle(particleData, x2 + 0.4 - this.random.nextDouble() / 10D, y, z2 - 0.4 + this.random.nextDouble() / 10D, x1, y1, z1);
                this.level.addParticle(particleData, x2, y, z2, x1, y1, z1);
                this.level.addParticle(particleData, x2 + 0.4, y, z2, x1, y1, z1);
                this.level.addParticle(particleData, x2, y, z2 + 0.4D, x1, y1, z1);
                this.level.addParticle(particleData, x2, y, z2 - 0.4D, x1, y1, z1);

            }
            else if (this.tickCount % 2 == 0)
            {
                y += 0.6D;
                EntityParticleData particleData = new EntityParticleData(GCParticles.LAUNCH_FLAME_LAUNCHED, riddenByEntity == null ? null : riddenByEntity.getUUID());
                this.level.addParticle(particleData, x2 + 0.4 - this.random.nextDouble() / 10D, y, z2 + 0.4 - this.random.nextDouble() / 10D, this.random.nextDouble() / 2.0 - 0.25, 0.0, this.random.nextDouble() / 2.0 - 0.25);
                this.level.addParticle(particleData, x2 - 0.4 + this.random.nextDouble() / 10D, y, z2 + 0.4 - this.random.nextDouble() / 10D, this.random.nextDouble() / 2.0 - 0.25, 0.0, this.random.nextDouble() / 2.0 - 0.25);
                this.level.addParticle(particleData, x2 - 0.4 + this.random.nextDouble() / 10D, y, z2 - 0.4 + this.random.nextDouble() / 10D, this.random.nextDouble() / 2.0 - 0.25, 0.0, this.random.nextDouble() / 2.0 - 0.25);
                this.level.addParticle(particleData, x2 + 0.4 - this.random.nextDouble() / 10D, y, z2 - 0.4 + this.random.nextDouble() / 10D, this.random.nextDouble() / 2.0 - 0.25, 0.0, this.random.nextDouble() / 2.0 - 0.25);
            }
        }
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.isAlive() && par1EntityPlayer.distanceToSqr(this) <= 64.0D;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt)
    {
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public int getPreLaunchWait()
    {
        return 400;
    }

    @Override
    public List<ItemStack> getItemsDropped(List<ItemStack> droppedItems)
    {
        super.getItemsDropped(droppedItems);
        ItemStack rocket = new ItemStack(getItemFromType(getRocketType()), 1);
        rocket.setTag(new CompoundTag());
        rocket.getTag().putInt("RocketFuel", this.fuelTank.getFluidAmount());
        droppedItems.add(rocket);
        return droppedItems;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemstack)
    {
        return false;
    }

    @Override
    public boolean isDockValid(IFuelDock dock)
    {
        return dock instanceof TileEntityLandingPad;
    }

    @Override
    public int getRocketTier()
    {
        return 1;
    }

    @Override
    public int getFuelTankCapacity()
    {
        return FUEL_CAPACITY;
    }

    @Override
    public float getCameraZoom()
    {
        return 15.0F;
    }

    @Override
    public boolean defaultThirdPerson()
    {
        return true;
    }

    @Override
    public double getOnPadYOffset()
    {
        return 0.0D;
    }
}
