package micdoodle8.mods.galacticraft.core.entities;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import micdoodle8.mods.galacticraft.api.entity.ILaserTrackableFast;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.math.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.network.NetworkHooks;

public class MeteorEntity extends Entity implements ILaserTrackableFast
{
    private static final EntityDataAccessor<Integer> SIZE = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.INT);

    public MeteorEntity(EntityType<MeteorEntity> type, Level world)
    {
        super(type, world);
        this.noPhysics = true;
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick()
    {
        this.setRot(this.yRot + 2F, this.xRot + 2F);
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        this.setDeltaMovement(this.getDeltaMovement().subtract(0.0, 0.03999999910593033, 0.0));
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.level.isClientSide)
        {
            this.spawnParticles();
        }

        Vec3 currentPosition = new Vec3(this.getX(), this.getY(), this.getZ());
        Vec3 nextPosition = new Vec3(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
        HitResult collisionIntercept = this.level.clip(new ClipContext(currentPosition, nextPosition, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        currentPosition = new Vec3(this.getX(), this.getY(), this.getZ());
        nextPosition = new Vec3(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);

        if (collisionIntercept.getType() != RayTraceResult.Type.MISS)
        {
            nextPosition = new Vec3(collisionIntercept.getLocation().x, collisionIntercept.getLocation().y, collisionIntercept.getLocation().z);
        }

        Entity collidingEntity = null;
        List<?> nearbyEntities = this.level.getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(2.0D, 2.0D, 2.0D));
        double distanceToCollidingEntityIntercept = 0.0D;
        Iterator<?> nearbyEntitiesIterator = nearbyEntities.iterator();
        double entityBBPadding = 0.01D;

        while (nearbyEntitiesIterator.hasNext())
        {
            Entity nearbyEntity = (Entity) nearbyEntitiesIterator.next();

            if (nearbyEntity.isPickable())
            {
                AABB nearbyEntityPaddedBB = nearbyEntity.getBoundingBox().inflate(entityBBPadding, entityBBPadding, entityBBPadding);
                Optional<Vec3> nearbyEntityIntercept = nearbyEntityPaddedBB.clip(currentPosition, nextPosition);

                if (nearbyEntityIntercept.isPresent())
                {
                    double distanceToNearbyEntityIntercept = currentPosition.distanceTo(nearbyEntityIntercept.get());

                    if (distanceToNearbyEntityIntercept < distanceToCollidingEntityIntercept || distanceToCollidingEntityIntercept == 0.0D)
                    {
                        collidingEntity = nearbyEntity;
                        distanceToCollidingEntityIntercept = distanceToNearbyEntityIntercept;
                    }
                }
            }
        }

        if (collidingEntity != null)
        {
            collisionIntercept = new EntityHitResult(collidingEntity);
        }

        if (collisionIntercept.getType() != RayTraceResult.Type.MISS)
        {
            this.onImpact(collisionIntercept);
        }

        if (this.getY() <= -20 || this.getY() >= 400)
        {
            this.remove();
        }
    }

    protected void spawnParticles()
    {
        this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 1D + Math.random(), this.getZ(), 0.0D, 0.0D, 0.0D);
        this.level.addParticle(ParticleTypes.SMOKE, this.getX() + Math.random() / 2, this.getY() + 1D + Math.random() / 2, this.getZ(), 0.0D, 0.0D, 0.0D);
        this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 1D + Math.random(), this.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
        this.level.addParticle(ParticleTypes.SMOKE, this.getX() - Math.random() / 2, this.getY() + 1D + Math.random() / 2, this.getZ(), 0.0D, 0.0D, 0.0D);
        this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 1D + Math.random(), this.getZ() - Math.random(), 0.0D, 0.0D, 0.0D);
    }

    protected void onImpact(HitResult movingObjPos)
    {
        if (!this.level.isClientSide)
        {
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), this.getSize() / 3 + 2, false, Explosion.Mode.BREAK);

            if (movingObjPos != null)
            {
                BlockPos pos;
                if (movingObjPos.getType() != RayTraceResult.Type.BLOCK)
                {
                    pos = new BlockPos(movingObjPos.getLocation());
                }
                else
                {
                    if (movingObjPos.getType() == RayTraceResult.Type.ENTITY)
                    {
                        pos = this.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ((EntityHitResult) movingObjPos).getEntity().getCommandSenderBlockPosition());
                    }
                    else
                    {
                        pos = this.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, this.getCommandSenderBlockPosition());
                    }
                }
                BlockPos above = pos.above();
                if (this.level.getBlockState(above).getBlock() instanceof AirBlock)
                {
                    this.level.setBlock(above, GCBlocks.FALLEN_METEOR.defaultBlockState(), 3);
                }

                if (movingObjPos.getType() == RayTraceResult.Type.ENTITY)
                {
                    ((EntityHitResult) movingObjPos).getEntity().hurt(MeteorEntity.causeMeteorDamage(this), ConfigManagerCore.hardMode.get() ? 12F : 6F);
                }
            }
        }

        this.remove();
    }

    public static DamageSource causeMeteorDamage(MeteorEntity par0EntityMeteor)
    {
        //        if (par1Entity != null && par1Entity instanceof PlayerEntity)
        //        {
        //            LanguageMap.getInstance().translateKeyFormatted("death." + "meteor", PlayerUtil.getName(((PlayerEntity) par1Entity)) + " was hit by a meteor! That's gotta hurt!");
        //        } TODO Was this ever sent?
        return new IndirectEntityDamageSource("explosion", par0EntityMeteor, null).setProjectile();
    }

    @Override
    protected void defineSynchedData()
    {
        this.entityData.define(SIZE, 1);
    }

    public int getSize()
    {
        return this.entityData.get(SIZE);
    }

    public void setSize(int size)
    {
        this.entityData.set(SIZE, size);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound)
    {
        this.setSize(compound.getInt("Size"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound)
    {
        compound.putInt("Size", this.getSize());
    }
}