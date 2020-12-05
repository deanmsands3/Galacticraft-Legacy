package micdoodle8.mods.galacticraft.planets.venus.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.util.math.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.Optional;

public class EntityWebShot extends Entity implements Projectile
{
    public int canBePickedUp;
    public int arrowShake;
    public Entity shootingEntity;
    private int ticksInAir;

    public EntityWebShot(EntityType<? extends EntityWebShot> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(0.5F, 0.5F);
    }

    public static EntityWebShot createEntityWebShot(Level worldIn, double x, double y, double z)
    {
        EntityWebShot webShot = new EntityWebShot(VenusEntities.WEB_SHOT, worldIn);
//        this.setSize(0.5F, 0.5F);
        webShot.setPos(x, y, z);
        return webShot;
    }

    public static EntityWebShot createEntityWebShot(Level worldIn, LivingEntity shooter, LivingEntity target, float p_i1755_4_, float p_i1755_5_)
    {
        EntityWebShot webShot = new EntityWebShot(VenusEntities.WEB_SHOT, worldIn);
        webShot.shootingEntity = shooter;

        if (shooter instanceof Player)
        {
            webShot.canBePickedUp = 1;
        }

        webShot.setPosRaw(webShot.getX(), shooter.getY() + (double) shooter.getEyeHeight() - 0.10000000149011612D, webShot.getZ());
        double d0 = target.getX() - shooter.getX();
        double d1 = target.getBoundingBox().minY + (double) (target.getBbHeight() / 3.0F) - webShot.getY();
        double d2 = target.getZ() - shooter.getZ();
        double d3 = Mth.sqrt(d0 * d0 + d2 * d2);

        if (d3 >= 1.0E-7D)
        {
            float f = (float) Mth.atan2(d2, d0) * Constants.RADIANS_TO_DEGREES - 90.0F;
            float f1 = (float) Mth.atan2(d1, d3) * -Constants.RADIANS_TO_DEGREES;
            double d4 = d0 / d3;
            double d5 = d2 / d3;
            webShot.moveTo(shooter.getX() + d4, webShot.getY(), shooter.getZ() + d5, f, f1);
            float f2 = (float) (d3 * 0.20000000298023224D);
            webShot.shoot(d0, d1 + (double) f2, d2, p_i1755_4_, p_i1755_5_);
        }

        return webShot;
    }

    public static EntityWebShot createEntityWebShot(Level worldIn, LivingEntity shooter, float velocity)
    {
        EntityWebShot webShot = new EntityWebShot(VenusEntities.WEB_SHOT, worldIn);
        webShot.shootingEntity = shooter;

        if (shooter instanceof Player)
        {
            webShot.canBePickedUp = 1;
        }

//        webShot.setSize(0.5F, 0.5F);
        webShot.moveTo(shooter.getX(), shooter.getY() + (double) shooter.getEyeHeight(), shooter.getZ(), shooter.yRot, shooter.xRot);
        webShot.setPosRaw(webShot.getX() - Mth.cos(webShot.yRot / Constants.RADIANS_TO_DEGREES) * 0.16F,
                webShot.getY() - 0.10000000149011612D,
                webShot.getZ() - Mth.sin(webShot.yRot / Constants.RADIANS_TO_DEGREES) * 0.16F);
        webShot.setPos(webShot.getX(), webShot.getY(), webShot.getZ());
        double motionX = -Mth.sin(webShot.yRot / Constants.RADIANS_TO_DEGREES) * Mth.cos(webShot.xRot / Constants.RADIANS_TO_DEGREES);
        double motionZ = Mth.cos(webShot.yRot / Constants.RADIANS_TO_DEGREES) * Mth.cos(webShot.xRot / Constants.RADIANS_TO_DEGREES);
        double motionY = -Mth.sin(webShot.xRot / Constants.RADIANS_TO_DEGREES);
        webShot.shoot(motionX, motionY, motionZ, velocity * 1.5F, 1.0F);
        return webShot;
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

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f = Mth.sqrt(x * x + y * y + z * z);
        x = x / (double) f;
        y = y / (double) f;
        z = z / (double) f;
        x = x + this.random.nextGaussian() * (double) (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) inaccuracy;
        y = y + this.random.nextGaussian() * (double) (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) inaccuracy;
        z = z + this.random.nextGaussian() * (double) (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) inaccuracy;
        x = x * (double) velocity;
        y = y * (double) velocity;
        z = z * (double) velocity;
        this.setDeltaMovement(x, y, z);
        float f1 = Mth.sqrt(x * x + z * z);
        this.yRotO = this.yRot = (float) Mth.atan2(x, z) * Constants.RADIANS_TO_DEGREES;
        this.xRotO = this.xRot = (float) Mth.atan2(y, f1) * Constants.RADIANS_TO_DEGREES;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
    {
        this.setPos(x, y, z);
        this.setRot(yaw, pitch);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void lerpMotion(double x, double y, double z)
    {
        this.setDeltaMovement(x, y, z);

        if (this.xRotO == 0.0F && this.yRotO == 0.0F)
        {
            float f = Mth.sqrt(x * x + z * z);
            this.yRotO = this.yRot = (float) Mth.atan2(x, z) * Constants.RADIANS_TO_DEGREES;
            this.xRotO = this.xRot = (float) Mth.atan2(y, f) * Constants.RADIANS_TO_DEGREES;
            this.xRotO = this.xRot;
            this.yRotO = this.yRot;
            this.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.xRotO == 0.0F && this.yRotO == 0.0F)
        {
            float f = Mth.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
            this.yRotO = this.yRot = (float) Mth.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z) * Constants.RADIANS_TO_DEGREES;
            this.xRotO = this.xRot = (float) Mth.atan2(this.getDeltaMovement().y, f) * Constants.RADIANS_TO_DEGREES;
        }

        if (this.arrowShake > 0)
        {
            --this.arrowShake;
        }

        if (this.tickCount > 1000)
        {
            this.remove();
        }

        ++this.ticksInAir;
        Vec3 vec31 = new Vec3(this.getX(), this.getY(), this.getZ());
        Vec3 vec3 = new Vec3(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
        HitResult castResult = this.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        vec31 = new Vec3(this.getX(), this.getY(), this.getZ());
        vec3 = new Vec3(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);

        if (castResult.getType() != HitResult.Type.MISS)
        {
            vec3 = new Vec3(castResult.getLocation().x, castResult.getLocation().y, castResult.getLocation().z);
        }

        Entity entity = null;
        List<Entity> list = this.level.getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z).inflate(1.0D, 1.0D, 1.0D));
        double d0 = 0.0D;
        final double border = 0.3D;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity1 = list.get(i);

            if (entity1.isPickable() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
            {
                AABB axisalignedbb1 = entity1.getBoundingBox().inflate(border, border, border);
                Optional<Vec3> result = axisalignedbb1.clip(vec31, vec3);

                if (result.isPresent())
                {
                    double d1 = vec31.distanceToSqr(result.get());

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

        if (castResult.getType() == HitResult.Type.ENTITY)
        {
            EntityHitResult entityResult = (EntityHitResult) castResult;
            if (entityResult.getEntity() instanceof Player)
            {
                Player entityplayer = (Player) entityResult.getEntity();

                if (entityplayer.abilities.invulnerable || this.shootingEntity instanceof Player && !((Player) this.shootingEntity).canHarmPlayer(entityplayer))
                {
                    castResult = null;
                }
            }
        }

        if (castResult != null)
        {
            if (castResult.getType() == HitResult.Type.ENTITY)
            {
                EntityHitResult entityResult = (EntityHitResult) castResult;
                if (entityResult.getEntity() != this.shootingEntity && !this.level.isClientSide)
                {
                    if (entityResult.getEntity() instanceof LivingEntity)
                    {
                        ((LivingEntity) entityResult.getEntity()).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 180, 2, true, true));
                        this.remove();
                    }
                    else
                    {
//                        this.motionX *= -0.10000000149011612D;
//                        this.motionY *= -0.10000000149011612D;
//                        this.motionZ *= -0.10000000149011612D;
                        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.10000000149011612D, -0.10000000149011612D, -0.10000000149011612D));
                        this.yRot += 180.0F;
                        this.yRotO += 180.0F;
                        this.ticksInAir = 0;
                    }
                }
            }
            else
            {
                this.setDeltaMovement((float) (castResult.getLocation().x - this.getX()), (float) (castResult.getLocation().y - this.getY()), (float) (castResult.getLocation().z - this.getZ()));
                float f5 = Mth.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().y * this.getDeltaMovement().y + this.getDeltaMovement().z * this.getDeltaMovement().z);
                this.setPosRaw(this.getX() - this.getDeltaMovement().x / (double) f5 * 0.05000000074505806D, this.getY() - this.getDeltaMovement().y / (double) f5 * 0.05000000074505806D, this.getZ() - this.getDeltaMovement().z / (double) f5 * 0.05000000074505806D);
                this.arrowShake = 7;
                this.remove();
            }
        }

        this.setPosRaw(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
        float f3 = Mth.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
        this.yRot = (float) Mth.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z) * Constants.RADIANS_TO_DEGREES;

        for (this.xRot = (float) Mth.atan2(this.getDeltaMovement().y, f3) * Constants.RADIANS_TO_DEGREES; this.xRot - this.xRotO < -180.0F; this.xRotO -= 360.0F)
        {
        }

        while (this.xRot - this.xRotO >= 180.0F)
        {
            this.xRotO += 360.0F;
        }

        while (this.yRot - this.yRotO < -180.0F)
        {
            this.yRotO -= 360.0F;
        }

        while (this.yRot - this.yRotO >= 180.0F)
        {
            this.yRotO += 360.0F;
        }

        this.xRot = this.xRotO + (this.xRot - this.xRotO) * 0.2F;
        this.yRot = this.yRotO + (this.yRot - this.yRotO) * 0.2F;

        if (this.isInWater())
        {
            for (int i1 = 0; i1 < 4; ++i1)
            {
                float f8 = 0.25F;
                this.level.addParticle(ParticleTypes.BUBBLE, this.getX() - this.getDeltaMovement().x * (double) f8, this.getY() - this.getDeltaMovement().y * (double) f8, this.getZ() - this.getDeltaMovement().z * (double) f8, this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
            }
        }

        if (this.isInWaterOrRain())
        {
            this.clearFire();
        }

        this.setPos(this.getX(), this.getY(), this.getZ());
        this.checkInsideBlocks();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt)
    {
        this.arrowShake = nbt.getByte("shake") & 255;

        if (nbt.contains("pickup", 99))
        {
            this.canBePickedUp = nbt.getByte("pickup");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt)
    {
        nbt.putByte("shake", (byte) this.arrowShake);
        nbt.putByte("pickup", (byte) this.canBePickedUp);
    }

    @Override
    protected boolean isMovementNoisy()
    {
        return false;
    }

    @Override
    public float getEyeHeight(Pose pose)
    {
        return 0.0F;
    }
}