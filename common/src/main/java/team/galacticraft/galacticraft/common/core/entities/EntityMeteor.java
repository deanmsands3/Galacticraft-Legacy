package team.galacticraft.galacticraft.common.core.entities;

import team.galacticraft.galacticraft.common.api.entity.ILaserTrackableFast;
import team.galacticraft.galacticraft.common.api.vector.Vector3;
import team.galacticraft.galacticraft.core.GCBlocks;
import team.galacticraft.galacticraft.core.GalacticraftCore;
import team.galacticraft.galacticraft.core.util.ConfigManagerCore;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class EntityMeteor extends Entity implements ILaserTrackableFast
{
    private static final EntityDataAccessor<Integer> SIZE = SynchedEntityData.defineId(EntityMeteor.class, EntityDataSerializers.INT);
    public Mob shootingEntity;
    public int size;

    public EntityMeteor(EntityType<EntityMeteor> type, Level world)
    {
        super(type, world);
//        this.setSize(1.0F, 1.0F);
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    //    public EntityMeteor(World world, double x, double y, double z, double motX, double motY, double motZ, int size)
//    {
//        this(world);
//        this.size = size;
//        this.setSize(1.0F, 1.0F);
//        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
//        this.setPosition(x, y, z);
//        this.motionX = motX;
//        this.motionY = motY;
//        this.motionZ = motZ;
//        this.setSize(size);
//    }

    @Override
    public void tick()
    {
        this.setRot(this.yRot + 2F, this.xRot + 2F);
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
//        this.motionY -= 0.03999999910593033D;
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.03999999910593033, 0.0));
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.level.isClientSide)
        {
            this.spawnParticles();
        }

        Vec3 currentPosition = new Vec3(this.getX(), this.getY(), this.getZ());
        Vec3 nextPosition = new Vec3(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
        // currentPosition, nextPosition, true, true, false
        HitResult collisionIntercept = this.level.clip(new ClipContext(currentPosition, nextPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        currentPosition = new Vec3(this.getX(), this.getY(), this.getZ());
        nextPosition = new Vec3(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);

        if (collisionIntercept.getType() != HitResult.Type.MISS)
        {
            nextPosition = new Vec3(collisionIntercept.getLocation().x, collisionIntercept.getLocation().y, collisionIntercept.getLocation().z);
        }

        Entity collidingEntity = null;
        final List<?> nearbyEntities = this.level.getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(2.0D, 2.0D, 2.0D));
        double distanceToCollidingEntityIntercept = 0.0D;
        final Iterator<?> nearbyEntitiesIterator = nearbyEntities.iterator();
        final double entityBBPadding = 0.01D;

        while (nearbyEntitiesIterator.hasNext())
        {
            final Entity nearbyEntity = (Entity) nearbyEntitiesIterator.next();

            if (nearbyEntity.isPickable() && !nearbyEntity.is(this.shootingEntity))
            {
                final AABB nearbyEntityPaddedBB = nearbyEntity.getBoundingBox().inflate(entityBBPadding, entityBBPadding, entityBBPadding);
                final Optional<Vec3> nearbyEntityIntercept = nearbyEntityPaddedBB.clip(currentPosition, nextPosition);

                if (nearbyEntityIntercept.isPresent())
                {
                    final double distanceToNearbyEntityIntercept = currentPosition.distanceTo(nearbyEntityIntercept.get());

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

        if (collisionIntercept.getType() != HitResult.Type.MISS)
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
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), this.size / 3 + 2, false, Explosion.BlockInteraction.BREAK);

            if (movingObjPos != null)
            {
                BlockPos pos;
                if (movingObjPos.getType() != HitResult.Type.BLOCK)
                {
                    pos = new BlockPos(movingObjPos.getLocation());
                }
                else
                {
                    if (movingObjPos.getType() == HitResult.Type.ENTITY)
                    {
                        pos = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ((EntityHitResult) movingObjPos).getEntity().getCommandSenderBlockPosition());
                    }
                    else
                    {
                        pos = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.getCommandSenderBlockPosition());
                    }
                }
                BlockPos above = pos.above();
                if (this.level.getBlockState(above).getBlock() instanceof AirBlock)
                {
                    this.level.setBlock(above, GCBlocks.fallenMeteor.defaultBlockState(), 3);
                }

                if (movingObjPos.getType() == HitResult.Type.ENTITY)
                {
                    ((EntityHitResult) movingObjPos).getEntity().hurt(EntityMeteor.causeMeteorDamage(this, this.shootingEntity), ConfigManagerCore.hardMode.get() ? 12F : 6F);
                }
            }
        }

        this.remove();
    }

    public static DamageSource causeMeteorDamage(EntityMeteor par0EntityMeteor, Entity par1Entity)
    {
//        if (par1Entity != null && par1Entity instanceof PlayerEntity)
//        {
//            LanguageMap.getInstance().translateKeyFormatted("death." + "meteor", PlayerUtil.getName(((PlayerEntity) par1Entity)) + " was hit by a meteor! That's gotta hurt!");
//        } TODO Was this ever sent?
        return new IndirectEntityDamageSource("explosion", par0EntityMeteor, par1Entity).setProjectile();
    }

    @Override
    protected void defineSynchedData()
    {
        this.entityData.define(SIZE, this.size);
        this.noPhysics = true;
    }

    public int getSize()
    {
        return this.entityData.get(SIZE);
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setSize(int par1)
    {
        this.entityData.set(SIZE, Integer.valueOf(par1));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound)
    {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound)
    {

    }
}
