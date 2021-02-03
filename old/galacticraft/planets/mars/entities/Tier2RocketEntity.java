package micdoodle8.mods.galacticraft.planets.mars.entities;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.GCParticles;
import micdoodle8.mods.galacticraft.core.client.fx.EntityParticleData;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
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
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.Random;

public class Tier2RocketEntity extends EntityTieredRocket
{
    public Tier2RocketEntity(EntityType<? extends Tier2RocketEntity> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(1.2F, 4.5F);
    }

    public static Tier2RocketEntity createEntityTier2Rocket(Level world, double x, double y, double z, EnumRocketType rocketType)
    {
        Tier2RocketEntity rocket = new Tier2RocketEntity(MarsEntities.TIER_2_ROCKET, world);
        rocket.setPos(x, y, z);
        rocket.xo = x;
        rocket.yo = y;
        rocket.zo = z;
        rocket.rocketType = rocketType;
        rocket.stacks = NonNullList.withSize(rocket.getContainerSize(), ItemStack.EMPTY);
//        rocket.setSize(1.2F, 4.5F);
        return rocket;
    }

    public Tier2RocketEntity createEntityTier2Rocket(Level par1World, double par2, double par4, double par6, boolean reversed, EnumRocketType rocketType, NonNullList<ItemStack> inv)
    {
        Tier2RocketEntity rocket = createEntityTier2Rocket(par1World, par2, par4, par6, rocketType);
        this.stacks = inv;
        return rocket;
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static Item getItemFromType(EnumRocketType rocketType)
    {
        switch (rocketType)
        {
        default:
        case DEFAULT:
            return MarsItems.TIER_2_ROCKET;
        case INVENTORY27:
            return MarsItems.TIER_2_ROCKET_18_INVENTORY;
        case INVENTORY36:
            return MarsItems.TIER_2_ROCKET_36_INVENTORY;
        case INVENTORY54:
            return MarsItems.TIER_2_ROCKET_54_INVENTORY;
        case PREFUELED:
            return MarsItems.CREATIVE_TIER_2_ROCKET;
        }
    }

    public static EnumRocketType getTypeFromItem(Item item)
    {
        if (item == MarsItems.TIER_2_ROCKET)
        {
            return EnumRocketType.DEFAULT;
        }
        if (item == MarsItems.TIER_2_ROCKET_18_INVENTORY)
        {
            return EnumRocketType.INVENTORY27;
        }
        if (item == MarsItems.TIER_2_ROCKET_36_INVENTORY)
        {
            return EnumRocketType.INVENTORY36;
        }
        if (item == MarsItems.TIER_2_ROCKET_54_INVENTORY)
        {
            return EnumRocketType.INVENTORY54;
        }
        if (item == MarsItems.CREATIVE_TIER_2_ROCKET)
        {
            return EnumRocketType.PREFUELED;
        }
        return null;
    }

    @Override
    public double getRidingHeight()
    {
        return 1.5F;
    }

    @Override
    protected void defineSynchedData()
    {
    }

    @Override
    public ItemStack getPickedResult(HitResult target)
    {
        return new ItemStack(getItemFromType(rocketType));
    }

    @Override
    public double getRideHeight()
    {
        return 1.6D;
    }

    @Override
    public float getRotateOffset()
    {
        return 1.25F;
    }

    @Override
    public double getOnPadYOffset()
    {
        return -0.2D;
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
                    d = Math.min(d * 1.2, 1.8);
                }
                else
                {
                    d = Math.min(d, 1.2);
                }

                if (d != 0.0)
                {
//                    this.motionY = -d * 2.0D * Math.cos((this.rotationPitch - 180) / Constants.RADIANS_TO_DEGREES_D);
                    this.setDeltaMovement(this.getDeltaMovement().x, -d * 2.0D * Math.cos((this.xRot - 180) / Constants.RADIANS_TO_DEGREES_D), this.getDeltaMovement().z);
                }
            }
            else
            {
//                this.motionY -= 0.008D;
                this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.008D, this.getDeltaMovement().z);
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

            if (this.timeSinceLaunch % Mth.floor(2 * (1 / multiplier)) == 0)
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
//                this.motionY -= ;
                this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20, this.getDeltaMovement().z);
            }
        }
    }

    @Override
    public void onTeleport(ServerPlayer player)
    {
        ServerPlayer playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

        if (playerBase != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(playerBase);

            if (this.stacks == null || this.stacks.size() == 0)
            {
                stats.setRocketStacks(NonNullList.withSize(2, ItemStack.EMPTY));
            }
            else
            {
                stats.setRocketStacks(this.stacks);
            }

//            stats.setRocketType(this.rocketType.getIndex());
            stats.setRocketItem(getItemFromType(getRocketType()));
            stats.setFuelLevel(this.fuelTank.getFluidAmount());
        }
    }

    protected void spawnParticles(boolean launched)
    {
        if (this.isAlive())
        {
            double sinPitch = Math.sin(this.xRot / Constants.RADIANS_TO_DEGREES_D);
            double x1 = 2.9 * Math.cos(this.yRot / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            double z1 = 2.9 * Math.sin(this.yRot / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            double y1 = 2.9 * Math.cos((this.xRot - 180) / Constants.RADIANS_TO_DEGREES_D);
            if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() && this.targetVec != null)
            {
                double modifier = this.getY() - this.targetVec.getY();
                modifier = Math.min(Math.max(modifier, 80.0), 200.0);
                x1 *= modifier / 100.0D;
                y1 *= modifier / 100.0D;
                z1 *= modifier / 100.0D;
            }

            final double y = this.yo + (this.getY() - this.yo) + y1 - this.getDeltaMovement().y + (!this.getLaunched() ? 2.5D : 1D);

            final double x2 = this.getX() + x1 - this.getDeltaMovement().x;
            final double z2 = this.getZ() + z1 - this.getDeltaMovement().z;
            final double x3 = x2 + x1 / 2D;
            final double y3 = y + y1 / 2D;
            final double z3 = z2 + z1 / 2D;

            if (this.tickCount % 2 == 0 && !this.getLaunched())
            {
                return;
            }

//            String flame = this.getLaunched() ? GCParticles.LAUNCH_FLAME_LAUNCHED : GCParticles.LAUNCH_FLAME_IDLE;

            LivingEntity riddenByEntity = this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof LivingEntity) ? null : (LivingEntity) this.getPassengers().get(0);
            EntityParticleData particleData = new EntityParticleData(this.getLaunched() ? GCParticles.LAUNCH_FLAME_LAUNCHED : GCParticles.LAUNCH_FLAME_IDLE, riddenByEntity.getUUID());
            Random random = this.random;
            this.level.addParticle(particleData, x2 + 0.4 - random.nextDouble() / 10D, y, z2 + 0.4 - random.nextDouble() / 10D, x1, y1, z1);
            this.level.addParticle(particleData, x2 - 0.4 + random.nextDouble() / 10D, y, z2 + 0.4 - random.nextDouble() / 10D, x1, y1, z1);
            this.level.addParticle(particleData, x2 - 0.4 + random.nextDouble() / 10D, y, z2 - 0.4 + random.nextDouble() / 10D, x1, y1, z1);
            this.level.addParticle(particleData, x2 + 0.4 - random.nextDouble() / 10D, y, z2 - 0.4 + random.nextDouble() / 10D, x1, y1, z1);
            this.level.addParticle(particleData, x2, y, z2, x1, y1, z1);
            this.level.addParticle(particleData, x2 + 0.4, y, z2, x1, y1, z1);
            this.level.addParticle(particleData, x2 - 0.4, y, z2, x1, y1, z1);
            this.level.addParticle(particleData, x2, y, z2 + 0.4D, x1, y1, z1);
            this.level.addParticle(particleData, x2, y, z2 - 0.4D, x1, y1, z1);
            //Larger flameball for T2 - positioned behind the smaller one
            double a = 4D;
            double bx = x1 + 0.5D / a;
            double bz = z1 + 0.5D / a;
            this.level.addParticle(particleData, x3 + 0.2 - random.nextDouble() / 6D, y3 + 0.4, z3 + 0.2 - random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 - 0.2 + random.nextDouble() / 6D, y3 + 0.4, z3 + 0.2 - random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 - 0.2 + random.nextDouble() / 6D, y3 + 0.4, z3 - 0.2 + random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 + 0.2 - random.nextDouble() / 6D, y3 + 0.4, z3 - 0.2 + random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 + 0.2 - random.nextDouble() / 6D, y3 - 0.4, z3 + 0.2 - random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 - 0.2 + random.nextDouble() / 6D, y3 - 0.4, z3 + 0.2 - random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 - 0.2 + random.nextDouble() / 6D, y3 - 0.4, z3 - 0.2 + random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 + 0.2 - random.nextDouble() / 6D, y3 - 0.4, z3 - 0.2 + random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 + 0.7 - random.nextDouble() / 8D, y3, z3 + 0.7 - random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 - 0.7 + random.nextDouble() / 8D, y3, z3 + 0.7 - random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 - 0.7 + random.nextDouble() / 8D, y3, z3 - 0.7 + random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 + 0.7 - random.nextDouble() / 8D, y3, z3 - 0.7 + random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 + 0.7 - random.nextDouble() / 8D, y3, z3 - random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 - 0.7 + random.nextDouble() / 8D, y3, z3 - random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 + random.nextDouble() / 8D, y3, z3 + 0.7 + random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.level.addParticle(particleData, x3 - random.nextDouble() / 8D, y3, z3 - 0.7 + random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
//            this.world.addParticle("blueflame", x2 - 0.8, y, z2), motionVec, none);
//            this.world.addParticle("blueflame", x2 + 0.8, y, z2), motionVec, none);
//            this.world.addParticle("blueflame", x2, y, z2 - 0.8), motionVec, none);
//            this.world.addParticle("blueflame", x2, y, z2 + 0.8), motionVec, none);
        }
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.isAlive() && par1EntityPlayer.distanceToSqr(this) <= 64.0D;
    }

//    @Override
//    protected void writeEntityToNBT(CompoundNBT par1NBTTagCompound)
//    {
//        super.writeEntityToNBT(par1NBTTagCompound);
//    }
//
//    @Override
//    protected void readEntityFromNBT(CompoundNBT par1NBTTagCompound)
//    {
//        super.readEntityFromNBT(par1NBTTagCompound);
//    }

    @Override
    public boolean isDockValid(IFuelDock dock)
    {
        return dock instanceof TileEntityLandingPad;
    }

    @Override
    public int getRocketTier()
    {
        return 2;
    }

    @Override
    public int getFuelTankCapacity()
    {
        return 1500;
    }

    @Override
    public int getPreLaunchWait()
    {
        return 400;
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
    public List<ItemStack> getItemsDropped(List<ItemStack> droppedItems)
    {
        super.getItemsDropped(droppedItems);
        ItemStack rocket = new ItemStack(getItemFromType(rocketType));
        rocket.setTag(new CompoundTag());
        rocket.getTag().putInt("RocketFuel", this.fuelTank.getFluidAmount());
        droppedItems.add(rocket);
        return droppedItems;
    }

    @Override
    public float getRenderOffsetY()
    {
        return -0.1F;
    }
}
