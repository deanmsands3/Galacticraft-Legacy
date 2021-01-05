package micdoodle8.mods.galacticraft.core.entities;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import micdoodle8.mods.galacticraft.api.entity.ILaserTrackableFast;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.block.AirBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.network.NetworkHooks;

public class MeteorEntity extends Entity implements ILaserTrackableFast
{
    private static final DataParameter<Integer> SIZE = EntityDataManager.createKey(MeteorEntity.class, DataSerializers.VARINT);

    public MeteorEntity(EntityType<MeteorEntity> type, World world)
    {
        super(type, world);
        this.noClip = true;
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick()
    {
        this.setRotation(this.rotationYaw + 2F, this.rotationPitch + 2F);
        this.prevPosX = this.getPosX();
        this.prevPosY = this.getPosY();
        this.prevPosZ = this.getPosZ();
        this.setMotion(this.getMotion().subtract(0.0, 0.03999999910593033, 0.0));
        this.move(MoverType.SELF, this.getMotion());

        if (this.world.isRemote)
        {
            this.spawnParticles();
        }

        Vec3d currentPosition = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ());
        Vec3d nextPosition = new Vec3d(this.getPosX() + this.getMotion().x, this.getPosY() + this.getMotion().y, this.getPosZ() + this.getMotion().z);
        RayTraceResult collisionIntercept = this.world.rayTraceBlocks(new RayTraceContext(currentPosition, nextPosition, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        currentPosition = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ());
        nextPosition = new Vec3d(this.getPosX() + this.getMotion().x, this.getPosY() + this.getMotion().y, this.getPosZ() + this.getMotion().z);

        if (collisionIntercept.getType() != RayTraceResult.Type.MISS)
        {
            nextPosition = new Vec3d(collisionIntercept.getHitVec().x, collisionIntercept.getHitVec().y, collisionIntercept.getHitVec().z);
        }

        Entity collidingEntity = null;
        List<?> nearbyEntities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(this.getMotion()).grow(2.0D, 2.0D, 2.0D));
        double distanceToCollidingEntityIntercept = 0.0D;
        Iterator<?> nearbyEntitiesIterator = nearbyEntities.iterator();
        double entityBBPadding = 0.01D;

        while (nearbyEntitiesIterator.hasNext())
        {
            Entity nearbyEntity = (Entity) nearbyEntitiesIterator.next();

            if (nearbyEntity.canBeCollidedWith())
            {
                AxisAlignedBB nearbyEntityPaddedBB = nearbyEntity.getBoundingBox().grow(entityBBPadding, entityBBPadding, entityBBPadding);
                Optional<Vec3d> nearbyEntityIntercept = nearbyEntityPaddedBB.rayTrace(currentPosition, nextPosition);

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
            collisionIntercept = new EntityRayTraceResult(collidingEntity);
        }

        if (collisionIntercept.getType() != RayTraceResult.Type.MISS)
        {
            this.onImpact(collisionIntercept);
        }

        if (this.getPosY() <= -20 || this.getPosY() >= 400)
        {
            this.remove();
        }
    }

    protected void spawnParticles()
    {
        this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 1D + Math.random(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
        this.world.addParticle(ParticleTypes.SMOKE, this.getPosX() + Math.random() / 2, this.getPosY() + 1D + Math.random() / 2, this.getPosZ(), 0.0D, 0.0D, 0.0D);
        this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 1D + Math.random(), this.getPosZ() + Math.random(), 0.0D, 0.0D, 0.0D);
        this.world.addParticle(ParticleTypes.SMOKE, this.getPosX() - Math.random() / 2, this.getPosY() + 1D + Math.random() / 2, this.getPosZ(), 0.0D, 0.0D, 0.0D);
        this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 1D + Math.random(), this.getPosZ() - Math.random(), 0.0D, 0.0D, 0.0D);
    }

    protected void onImpact(RayTraceResult movingObjPos)
    {
        if (!this.world.isRemote)
        {
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), this.getSize() / 3 + 2, false, Explosion.Mode.BREAK);

            if (movingObjPos != null)
            {
                BlockPos pos;
                if (movingObjPos.getType() != RayTraceResult.Type.BLOCK)
                {
                    pos = new BlockPos(movingObjPos.getHitVec());
                }
                else
                {
                    if (movingObjPos.getType() == RayTraceResult.Type.ENTITY)
                    {
                        pos = this.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ((EntityRayTraceResult) movingObjPos).getEntity().getPosition());
                    }
                    else
                    {
                        pos = this.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, this.getPosition());
                    }
                }
                BlockPos above = pos.up();
                if (this.world.getBlockState(above).getBlock() instanceof AirBlock)
                {
                    this.world.setBlockState(above, GCBlocks.FALLEN_METEOR.getDefaultState(), 3);
                }

                if (movingObjPos.getType() == RayTraceResult.Type.ENTITY)
                {
                    ((EntityRayTraceResult) movingObjPos).getEntity().attackEntityFrom(MeteorEntity.causeMeteorDamage(this), ConfigManagerCore.hardMode.get() ? 12F : 6F);
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
    protected void registerData()
    {
        this.dataManager.register(SIZE, 1);
    }

    public int getSize()
    {
        return this.dataManager.get(SIZE);
    }

    public void setSize(int size)
    {
        this.dataManager.set(SIZE, size);
    }

    @Override
    protected void readAdditional(CompoundNBT compound)
    {
        this.setSize(compound.getInt("Size"));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound)
    {
        compound.putInt("Size", this.getSize());
    }
}