package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.GCParticles;
import micdoodle8.mods.galacticraft.core.client.fx.EntityParticleData;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
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

public class Tier3RocketEntity extends EntityTieredRocket
{
    public Tier3RocketEntity(EntityType<? extends Tier3RocketEntity> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(1.8F, 6.0F);
    }

    public static Tier3RocketEntity createEntityTier3Rocket(Level world, double x, double y, double z, EnumRocketType rocketType)
    {
        Tier3RocketEntity rocket = new Tier3RocketEntity(AsteroidEntities.TIER_3_ROCKET, world);
        rocket.setPos(x, y, z);
        rocket.xo = x;
        rocket.yo = y;
        rocket.zo = z;
        rocket.rocketType = rocketType;
        rocket.stacks = NonNullList.withSize(rocket.getContainerSize(), ItemStack.EMPTY);
        return rocket;
    }
//
//    public EntityTier3Rocket(World par1World, double par2, double par4, double par6, boolean reversed, EnumRocketType rocketType, NonNullList<ItemStack> inv)
//    {
//        this(par1World, par2, par4, par6, rocketType);
//        this.stacks = inv;
//    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData()
    {
    }

    public static Item getItemFromType(EnumRocketType rocketType)
    {
        switch (rocketType)
        {
        default:
        case DEFAULT:
            return AsteroidsItems.TIER_3_ROCKET;
        case INVENTORY27:
            return AsteroidsItems.TIER_3_ROCKET_18_INVENTORY;
        case INVENTORY36:
            return AsteroidsItems.TIER_3_ROCKET_36_INVENTORY;
        case INVENTORY54:
            return AsteroidsItems.TIER_3_ROCKET_54_INVENTORY;
        case PREFUELED:
            return AsteroidsItems.CREATIVE_TIER_3_ROCKET;
        }
    }

    public static EnumRocketType getTypeFromItem(Item item)
    {
        if (item == AsteroidsItems.TIER_3_ROCKET)
        {
            return EnumRocketType.DEFAULT;
        }
        if (item == AsteroidsItems.TIER_3_ROCKET_18_INVENTORY)
        {
            return EnumRocketType.INVENTORY27;
        }
        if (item == AsteroidsItems.TIER_3_ROCKET_36_INVENTORY)
        {
            return EnumRocketType.INVENTORY36;
        }
        if (item == AsteroidsItems.TIER_3_ROCKET_54_INVENTORY)
        {
            return EnumRocketType.INVENTORY54;
        }
        if (item == AsteroidsItems.CREATIVE_TIER_3_ROCKET)
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
    public ItemStack getPickedResult(HitResult target)
    {
        return new ItemStack(getItemFromType(this.rocketType));
    }

    @Override
    public double getRideHeight()
    {
        return 1.75D;
    }

    @Override
    public float getRotateOffset()
    {
        return 1.5F;
    }

    @Override
    public double getOnPadYOffset()
    {
        return 0.0D;
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
                    d = Math.min(d * 1.2, 2);
                }
                else
                {
                    d = Math.min(d, 1.4);
                }

                if (d != 0.0)
                {
                    this.setDeltaMovement(this.getDeltaMovement().x, -d * 2.5D * Math.cos((this.xRot - 180) / Constants.RADIANS_TO_DEGREES), this.getDeltaMovement().z);
                }
            }
            else
            {
                this.setDeltaMovement(this.getDeltaMovement().x, -0.008, this.getDeltaMovement().z);
//                this.motionY -= 0.008D;
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
//                this.motionY -= Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
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

            if (this.stacks == null || this.stacks.isEmpty())
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
            float sinPitch = (float) Math.sin(this.xRot / Constants.RADIANS_TO_DEGREES_D);
            float x1 = (float) (3.2 * Math.cos(this.yRot / Constants.RADIANS_TO_DEGREES_D) * sinPitch);
            float z1 = (float) (3.2 * Math.sin(this.yRot / Constants.RADIANS_TO_DEGREES_D) * sinPitch);
            float y1 = (float) (3.2 * Math.cos((this.xRot - 180) / Constants.RADIANS_TO_DEGREES_D));
            if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() && this.targetVec != null)
            {
                double modifier = this.getY() - this.targetVec.getY();
                modifier = Math.max(modifier, 180.0);
                x1 *= modifier / 200.0D;
                y1 *= Math.min(modifier / 200.0D, 2.5D);
                z1 *= modifier / 200.0D;
            }

            final float y2 = (float) (this.yo + (this.getY() - this.yo) + y1 - 0.75 * this.getDeltaMovement().y - 0.3 + 1.2D);

            final float x2 = (float) (this.getX() + x1 + this.getDeltaMovement().x);
            final float z2 = (float) (this.getZ() + z1 + this.getDeltaMovement().z);
            Vector3 motionVec = new Vector3(x1 + (float) this.getDeltaMovement().x, y1 + (float) this.getDeltaMovement().y, z1 + (float) this.getDeltaMovement().z);
            Vector3 d1 = new Vector3(y1 * 0.1F, -x1 * 0.1F, z1 * 0.1F).rotate(315 - this.yRot, motionVec);
            Vector3 d2 = new Vector3(x1 * 0.1F, -z1 * 0.1F, y1 * 0.1F).rotate(315 - this.yRot, motionVec);
            Vector3 d3 = new Vector3(-y1 * 0.1F, x1 * 0.1F, z1 * 0.1F).rotate(315 - this.yRot, motionVec);
            Vector3 d4 = new Vector3(x1 * 0.1F, z1 * 0.1F, -y1 * 0.1F).rotate(315 - this.yRot, motionVec);
            Vector3 mv1 = motionVec.clone().translate(d1);
            Vector3 mv2 = motionVec.clone().translate(d2);
            Vector3 mv3 = motionVec.clone().translate(d3);
            Vector3 mv4 = motionVec.clone().translate(d4);
            //T3 - Four flameballs which spread
            makeFlame(x2 + d1.x, y2 + d1.y, z2 + d1.z, mv1, this.getLaunched());
            makeFlame(x2 + d2.x, y2 + d2.y, z2 + d2.z, mv2, this.getLaunched());
            makeFlame(x2 + d3.x, y2 + d3.y, z2 + d3.z, mv3, this.getLaunched());
            makeFlame(x2 + d4.x, y2 + d4.y, z2 + d4.z, mv4, this.getLaunched());
        }
    }

    private void makeFlame(float x2, float y2, float z2, Vector3 motionVec, boolean getLaunched)
    {
        LivingEntity riddenByEntity = this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof LivingEntity) ? null : (LivingEntity) this.getPassengers().get(0);
        EntityParticleData particleData = new EntityParticleData(GCParticles.LAUNCH_FLAME_LAUNCHED, riddenByEntity.getUUID());
        if (getLaunched)
        {
            this.level.addParticle(particleData, x2 + 0.4F - this.random.nextFloat() / 10.0F, y2, z2 + 0.4F - this.random.nextFloat() / 10.0F, motionVec.x, motionVec.y, motionVec.z);
            this.level.addParticle(particleData, x2 - 0.4F + this.random.nextFloat() / 10.0F, y2, z2 + 0.4F - this.random.nextFloat() / 10.0F, motionVec.x, motionVec.y, motionVec.z);
            this.level.addParticle(particleData, x2 - 0.4F + this.random.nextFloat() / 10.0F, y2, z2 - 0.4F + this.random.nextFloat() / 10.0F, motionVec.x, motionVec.y, motionVec.z);
            this.level.addParticle(particleData, x2 + 0.4F - this.random.nextFloat() / 10.0F, y2, z2 - 0.4F + this.random.nextFloat() / 10.0F, motionVec.x, motionVec.y, motionVec.z);
            this.level.addParticle(particleData, x2, y2, z2, motionVec.x, motionVec.y, motionVec.z);
            this.level.addParticle(particleData, x2 + 0.4, y2, z2, motionVec.x, motionVec.y, motionVec.z);
            this.level.addParticle(particleData, x2 - 0.4, y2, z2, motionVec.x, motionVec.y, motionVec.z);
            this.level.addParticle(particleData, x2, y2, z2 + 0.4D, motionVec.x, motionVec.y, motionVec.z);
            this.level.addParticle(particleData, x2, y2, z2 - 0.4D, motionVec.x, motionVec.y, motionVec.z);
            return;
        }

        if (this.tickCount % 2 == 0)
        {
            return;
        }

        y2 += 1.6D;
        double x1 = motionVec.x;
        double y1 = motionVec.y;
        double z1 = motionVec.z;
        this.level.addParticle(particleData, x2 + 0.4 - this.random.nextFloat() / 10D, y2, z2 + 0.4 - this.random.nextFloat() / 10D, x1 + 0.1D + this.random.nextFloat() / 10D, y1 - 0.3D, z1 + 0.1D + this.random.nextFloat() / 10D);
        this.level.addParticle(particleData, x2 - 0.4 + this.random.nextFloat() / 10D, y2, z2 + 0.4 - this.random.nextFloat() / 10D, x1 - 0.1D - this.random.nextFloat() / 10D, y1 - 0.3D, z1 + 0.1D + this.random.nextFloat() / 10D);
        this.level.addParticle(particleData, x2 - 0.4 + this.random.nextFloat() / 10D, y2, z2 - 0.4 + this.random.nextFloat() / 10D, x1 - 0.1D - this.random.nextFloat() / 10D, y1 - 0.3D, z1 - 0.1D - this.random.nextFloat() / 10D);
        this.level.addParticle(particleData, x2 + 0.4 - this.random.nextFloat() / 10D, y2, z2 - 0.4 + this.random.nextFloat() / 10D, x1 + 0.1D + this.random.nextFloat() / 10D, y1 - 0.3D, z1 - 0.1D - this.random.nextFloat() / 10D);
        this.level.addParticle(particleData, x2 + 0.4, y2, z2, x1 + 0.3D, y1 - 0.3D, z1);
        this.level.addParticle(particleData, x2 - 0.4, y2, z2, x1 - 0.3D, y1 - 0.3D, z1);
        this.level.addParticle(particleData, x2, y2, z2 + 0.4D, x1, y1 - 0.3D, z1 + 0.3D);
        this.level.addParticle(particleData, x2, y2, z2 - 0.4D, x1, y1 - 0.3D, z1 - 0.3D);
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.isAlive() && par1EntityPlayer.distanceToSqr(this) <= 64.0D;
    }

    @Override
    public int getRocketTier()
    {
        return 3;
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
        ItemStack rocket = new ItemStack(getItemFromType(getRocketType()), 1);
        rocket.setTag(new CompoundTag());
        rocket.getTag().putInt("RocketFuel", this.fuelTank.getFluidAmount());
        droppedItems.add(rocket);
        return droppedItems;
    }

    @Override
    public float getRenderOffsetY()
    {
        return -1F;
    }
}
