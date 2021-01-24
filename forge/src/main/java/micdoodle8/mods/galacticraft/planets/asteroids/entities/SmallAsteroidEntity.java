package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import micdoodle8.mods.galacticraft.planets.mars.entities.TNTProjectileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.NetworkHooks;

public class SmallAsteroidEntity extends Entity
{
    private static final EntityDataAccessor<Float> SPIN_PITCH = SynchedEntityData.defineId(SmallAsteroidEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SPIN_YAW = SynchedEntityData.defineId(SmallAsteroidEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> ASTEROID_TYPE = SynchedEntityData.defineId(SmallAsteroidEntity.class, EntityDataSerializers.INT);
    public float spinPitch;
    public float spinYaw;
    public int type;
    private boolean firstUpdate = true;

    public SmallAsteroidEntity(EntityType<? extends SmallAsteroidEntity> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(1.0F, 1.0F);
//        this.isImmuneToFire = true;
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void baseTick()
    {
        if (!this.firstTick)
        {
            // Kill non-moving entities
            if (Math.abs(this.getX() - this.xo) + Math.abs(this.getZ() - this.zo) <= 0)
            {
                this.remove();
            }

            // Remove entities far outside the build range, or too old (to stop accumulations)
            else if (this.getY() > 288D || this.getY() < -32D || this.tickCount > 3000)
            {
                this.remove();
            }
        }

        super.baseTick();

        if (!this.level.isClientSide)
        {
            this.setSpinPitch(this.spinPitch);
            this.setSpinYaw(this.spinYaw);
            this.setAsteroidType(this.type);
            this.xRot += this.spinPitch;
            this.yRot += this.spinYaw;
        }
        else
        {
            this.xRot += this.getSpinPitch();
            this.yRot += this.getSpinYaw();
        }

        double sqrdMotion = this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().y * this.getDeltaMovement().y + this.getDeltaMovement().z * this.getDeltaMovement().z;

        if (sqrdMotion < 0.05)
        {
            // If the motion is too low (for some odd reason), speed it back up slowly.
//            this.motionX *= 1.001D;
//            this.motionY *= 1.001D;
//            this.motionZ *= 1.001D;
            this.setDeltaMovement(this.getDeltaMovement().x * 1.001, this.getDeltaMovement().y * 1.001, this.getDeltaMovement().z * 1.001);
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.firstTick = false;
    }

    @Override
    protected void defineSynchedData()
    {
        this.entityData.define(SPIN_PITCH, 0.0F);
        this.entityData.define(SPIN_YAW, 0.0F);
        this.entityData.define(ASTEROID_TYPE, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt)
    {
    }

    public CompoundTag writeToNBT(CompoundTag compound)
    {
        return compound;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt)
    {
    }

    public float getSpinPitch()
    {
        return this.entityData.get(SPIN_PITCH);
    }

    public float getSpinYaw()
    {
        return this.entityData.get(SPIN_YAW);
    }

    public void setSpinPitch(float pitch)
    {
        this.entityData.set(SPIN_PITCH, pitch);
    }

    public void setSpinYaw(float yaw)
    {
        this.entityData.set(SPIN_YAW, yaw);
    }

    public int getAsteroidType()
    {
        return this.entityData.get(ASTEROID_TYPE);
    }

    public void setAsteroidType(int type)
    {
        this.entityData.set(ASTEROID_TYPE, type);
    }
}
