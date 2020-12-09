package team.galacticraft.galacticraft.common.core.entities;

import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GCItems;
import team.galacticraft.galacticraft.core.TransformerHooks;
import team.galacticraft.galacticraft.core.util.ConfigManagerCore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.math.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class EntityMeteorChunk extends Entity implements Projectile
{
    private static final EntityDataAccessor<Boolean> IS_HOT = SynchedEntityData.defineId(EntityMeteorChunk.class, EntityDataSerializers.BOOLEAN);
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    @Nullable
    private BlockState inBlockState;
    public int canBePickedUp;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private boolean inGround;
    private final float randYawInc;
    private final float randPitchInc;

    private int knockbackStrength;

    public boolean isHot;

    public EntityMeteorChunk(EntityType<EntityMeteorChunk> type, Level world)
    {
        super(type, world);
        this.randPitchInc = random.nextFloat() * 20 - 10;
        this.randYawInc = random.nextFloat() * 20 - 10;
    }

    public EntityMeteorChunk(Level world, LivingEntity par2EntityLivingBase, float speed)
    {
        super(GCEntities.METEOR_CHUNK, world);
        this.randPitchInc = random.nextFloat() * 20 - 10;
        this.randYawInc = random.nextFloat() * 20 - 10;
        this.shootingEntity = par2EntityLivingBase;

        if (par2EntityLivingBase instanceof Player)
        {
            this.canBePickedUp = 1;
        }

        this.moveTo(par2EntityLivingBase.getX(), par2EntityLivingBase.getY() + par2EntityLivingBase.getEyeHeight(), par2EntityLivingBase.getZ(), par2EntityLivingBase.yRot, par2EntityLivingBase.xRot);
        this.setPosRaw(this.getX() - Mth.cos(this.yRot / Constants.RADIANS_TO_DEGREES) * 0.16F,
                this.getY() - 0.10000000149011612D,
                this.getZ() - Mth.sin(this.yRot / Constants.RADIANS_TO_DEGREES) * 0.16F);
        this.setPos(this.getX(), this.getY(), this.getZ());
        float motionX = -Mth.sin(this.yRot / Constants.RADIANS_TO_DEGREES) * Mth.cos(this.xRot / Constants.RADIANS_TO_DEGREES);
        float motionZ = Mth.cos(this.yRot / Constants.RADIANS_TO_DEGREES) * Mth.cos(this.xRot / Constants.RADIANS_TO_DEGREES);
        float motionY = -Mth.sin(this.xRot / Constants.RADIANS_TO_DEGREES);
        setDeltaMovement(motionX, motionY, motionZ);
        this.shoot(motionX, motionY, motionZ, speed * 1.5F, 1.0F);
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void shoot(double headingX, double headingY, double headingZ, float speed, float randMod)
    {
        float f2 = Mth.sqrt(headingX * headingX + headingY * headingY + headingZ * headingZ);
        headingX /= f2;
        headingY /= f2;
        headingZ /= f2;
        headingX += this.random.nextGaussian() * (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * randMod;
        headingY += this.random.nextGaussian() * (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * randMod;
        headingZ += this.random.nextGaussian() * (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * randMod;
        headingX *= speed;
        headingY *= speed;
        headingZ *= speed;
        this.setDeltaMovement(headingX, headingY, headingZ);
        float f3 = Mth.sqrt(headingX * headingX + headingZ * headingZ);
        this.yRotO = this.yRot = (float) Math.atan2(headingX, headingZ) * Constants.RADIANS_TO_DEGREES;
        this.xRotO = this.xRot = (float) Math.atan2(headingY, f3) * Constants.RADIANS_TO_DEGREES;
        this.ticksInGround = 0;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void lerpMotion(double x, double y, double z)
    {
        this.setDeltaMovement(x, y, z);

        if (this.xRotO == 0.0F && this.yRotO == 0.0F)
        {
            float f = Mth.sqrt(x * x + z * z);
            this.yRotO = this.yRot = (float) Math.atan2(x, z) * Constants.RADIANS_TO_DEGREES;
            this.xRotO = this.xRot = (float) Math.atan2(y, f) * Constants.RADIANS_TO_DEGREES;
            this.xRotO = this.xRot;
            this.yRotO = this.yRot;
            this.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
            this.ticksInGround = 0;
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.tickCount > 400)
        {
            if (this.isHot)
            {
                this.isHot = false;
                this.setHot(this.isHot);
            }
        }
        else if (!this.level.isClientSide)
        {
            this.setHot(this.isHot);
        }

        if (this.xRotO == 0.0F && this.yRotO == 0.0F)
        {
            float f = Mth.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
            this.yRotO = this.yRot = (float) Math.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z) * Constants.RADIANS_TO_DEGREES;
            this.xRotO = this.xRot = (float) Math.atan2(this.getDeltaMovement().y, f) * Constants.RADIANS_TO_DEGREES;
        }

        BlockPos pos = new BlockPos(this.xTile, this.yTile, this.zTile);
        BlockState stateIn = this.level.getBlockState(pos);

        if (!stateIn.getBlock().isAir(this.level.getBlockState(pos), this.level, pos))
        {
            VoxelShape voxelshape = stateIn.getCollisionShape(this.level, pos);
            if (!voxelshape.isEmpty())
            {
                for (AABB axisalignedbb : voxelshape.toAabbs())
                {
                    if (axisalignedbb.move(pos).contains(new Vec3(this.getX(), this.getY(), this.getZ())))
                    {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }

        if (this.inGround)
        {
            BlockState currentInState = this.level.getBlockState(pos);

            if (this.inBlockState == currentInState)
            {
                ++this.ticksInGround;

                if (this.ticksInGround == 1200)
                {
                    this.remove();
                }
            }
            else
            {
                this.inGround = false;
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.2, 0.2, 0.2));
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        }
        else
        {
            ++this.ticksInAir;
            Vec3 vec3 = new Vec3(this.getX(), this.getY(), this.getZ());
            Vec3 vec31 = new Vec3(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
            HitResult castResult = this.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            vec3 = new Vec3(this.getX(), this.getY(), this.getZ());
            vec31 = new Vec3(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);

            if (castResult.getType() != HitResult.Type.MISS)
            {
                vec31 = new Vec3(castResult.getLocation().x, castResult.getLocation().y, castResult.getLocation().z);
            }

            this.xRot += 1F;

            Entity entity = null;
            List<Entity> list = this.level.getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int l;
            float f1;
            final double border = 0.3D;

            for (l = 0; l < list.size(); ++l)
            {
                Entity entity1 = list.get(l);

                if (entity1.isPickable() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
                {
                    AABB axisalignedbb1 = entity1.getBoundingBox().inflate(border, border, border);
                    Optional<Vec3> movingobjectposition1 = axisalignedbb1.clip(vec3, vec31);

                    if (movingobjectposition1.isPresent())
                    {
                        double d1 = vec3.distanceTo(movingobjectposition1.get());

                        if (d1 < d0 || d0 == 0.0D)
                        {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null)
            {
                castResult = new EntityHitResult(entity);
            }

            if (castResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult) castResult).getEntity() instanceof Player)
            {
                Player entityplayer = (Player) ((EntityHitResult) castResult).getEntity();

                if (entityplayer.abilities.invulnerable || this.shootingEntity instanceof Player && !((Player) this.shootingEntity).canHarmPlayer(entityplayer))
                {
                    castResult = null;
                }
            }

            float f2;
            float f3;
            double damage = ConfigManagerCore.hardMode.get() ? 3.2D : 1.6D;

            if (castResult != null)
            {
                if (castResult.getType() == HitResult.Type.ENTITY)
                {
                    EntityHitResult entityResult = (EntityHitResult) castResult;
                    f2 = Mth.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().y * this.getDeltaMovement().y + this.getDeltaMovement().z * this.getDeltaMovement().z);
                    int i1 = Mth.ceil(f2 * damage);

                    DamageSource damagesource = null;

                    if (this.shootingEntity == null)
                    {
                        damagesource = new IndirectEntityDamageSource("meteor_chunk", this, this).setProjectile();
                    }
                    else
                    {
                        damagesource = new IndirectEntityDamageSource("meteor_chunk", this, this.shootingEntity).setProjectile();
                    }

                    if (this.isOnFire() && !(entityResult.getEntity() instanceof EnderMan))
                    {
                        entityResult.getEntity().setSecondsOnFire(2);
                    }

                    if (entityResult.getEntity().hurt(damagesource, i1))
                    {
                        if (entityResult.getEntity() instanceof LivingEntity)
                        {
                            LivingEntity entitylivingbase = (LivingEntity) entityResult.getEntity();

                            if (!this.level.isClientSide)
                            {
                                entitylivingbase.setArrowCount(entitylivingbase.getArrowCount() + 1);
                            }

                            if (this.knockbackStrength > 0)
                            {
                                f3 = Mth.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);

                                if (f3 > 0.0F)
                                {
                                    entitylivingbase.push(this.getDeltaMovement().x * this.knockbackStrength * 0.6000000238418579D / f3, 0.1D, this.getDeltaMovement().z * this.knockbackStrength * 0.6000000238418579D / f3);
                                }
                            }

                            if (this.shootingEntity != null)
                            {
                                EnchantmentHelper.doPostHurtEffects(entitylivingbase, this.shootingEntity);
                                EnchantmentHelper.doPostDamageEffects((LivingEntity) this.shootingEntity, entitylivingbase);
                            }

                            if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof Player && this.shootingEntity instanceof ServerPlayer)
                            {
                                ((ServerPlayer) this.shootingEntity).connection.send(new ClientboundGameEventPacket(6, 0.0F));
                            }
                        }

                        if (!(entityResult.getEntity() instanceof EnderMan))
                        {
                            this.remove();
                        }
                    }
                    else
                    {
                        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.10000000149011612, -0.10000000149011612, -0.10000000149011612));
                        this.yRot += 180.0F;
                        this.yRotO += 180.0F;
                        this.ticksInAir = 0;
                    }
                }
                else if (castResult.getType() == HitResult.Type.ENTITY)
                {
                    BlockHitResult blockResult = (BlockHitResult) castResult;
                    this.xTile = blockResult.getBlockPos().getX();
                    this.yTile = blockResult.getBlockPos().getY();
                    this.zTile = blockResult.getBlockPos().getZ();
                    BlockState state = this.level.getBlockState(blockResult.getBlockPos());
                    this.inBlockState = state;
                    float motionX = (float) (castResult.getLocation().x - this.getX());
                    float motionY = (float) (castResult.getLocation().y - this.getY());
                    float motionZ = (float) (castResult.getLocation().z - this.getZ());
                    this.setDeltaMovement(motionX, motionY, motionZ);
                    f2 = Mth.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                    this.setPosRaw(this.getX() - this.getDeltaMovement().x / (double) f2 * 0.05000000074505806D, this.getY() - this.getDeltaMovement().y / (double) f2 * 0.05000000074505806D, this.getZ() - this.getDeltaMovement().z / (double) f2 * 0.05000000074505806D);
                    this.inGround = true;

                    if (!this.inBlockState.isAir(this.level, blockResult.getBlockPos()))
                    {
                        this.inBlockState.entityInside(this.level, blockResult.getBlockPos(), this);
                    }
                }
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
            f2 = Mth.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);

            if (!this.onGround)
            {
                this.xRot += this.randPitchInc;
                this.yRot += this.randYawInc;
            }

            float f4 = 0.99F;
            f1 = 0.05F;

            if (this.isInWater())
            {
                for (int j1 = 0; j1 < 4; ++j1)
                {
                    f3 = 0.25F;
                    this.level.addParticle(ParticleTypes.BUBBLE, this.getX() - this.getDeltaMovement().x * f3, this.getY() - this.getDeltaMovement().y * f3, this.getZ() - this.getDeltaMovement().z * f3, this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
                }
                this.isHot = false;
                f4 = 0.8F;
            }

            this.setDeltaMovement(this.getDeltaMovement().multiply(f4, f4, f4));
//            this.motionX *= f4;
//            this.motionY *= f4;
//            this.motionZ *= f4;
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -TransformerHooks.getGravityForEntity(this), 0.0));
            this.setPos(this.getX(), this.getY(), this.getZ());
            this.checkInsideBlocks();
        }
    }

    @Override
    protected void defineSynchedData()
    {
        this.entityData.define(IS_HOT, false);
    }

    public boolean isHot()
    {
        return this.entityData.get(IS_HOT);
    }

    public void setHot(boolean isHot)
    {
        this.entityData.set(IS_HOT, isHot);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbttagcompound)
    {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbttagcompound)
    {

    }

    @Override
    public void playerTouch(Player par1EntityPlayer)
    {
        if (!this.level.isClientSide && this.inGround)
        {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.abilities.instabuild;

            if (this.canBePickedUp == 1 && !par1EntityPlayer.inventory.add(new ItemStack(GCItems.meteorChunk, 1)))
            {
                flag = false;
            }

            if (flag)
            {
                this.playSound(SoundEvents.ITEM_PICKUP, 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                par1EntityPlayer.take(this, 1);
                this.remove();
            }
        }
    }

    @Override
    public boolean isAttackable()
    {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance)
    {
        double d0 = this.getBoundingBox().getSize();

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * 10.0;
        return distance < d0 * d0;
    }
}
