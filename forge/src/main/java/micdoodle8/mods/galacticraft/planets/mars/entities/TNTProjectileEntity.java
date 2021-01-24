package micdoodle8.mods.galacticraft.planets.mars.entities;

import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.network.NetworkHooks;

public class TNTProjectileEntity extends AbstractHurtingProjectile
{
    public TNTProjectileEntity(EntityType<? extends TNTProjectileEntity> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(1.0F, 1.0F);
    }

    public static TNTProjectileEntity createEntityProjectileTNT(Level world, LivingEntity entityShooting, double motX, double motY, double motZ)
    {
        TNTProjectileEntity projectileTNT = new TNTProjectileEntity(MarsEntities.TNT_PROJECTILE, world);
//        this.setSize(1.0F, 1.0F);
        projectileTNT.owner = entityShooting;
        projectileTNT.moveTo(entityShooting.getX(), entityShooting.getY(), entityShooting.getZ(), entityShooting.yRot, entityShooting.xRot);
        projectileTNT.setPos(projectileTNT.getX(), projectileTNT.getY(), projectileTNT.getZ());
        projectileTNT.setDeltaMovement(Vec3.ZERO);
        motX = motX + projectileTNT.random.nextGaussian() * 0.4D;
        motY = motY + projectileTNT.random.nextGaussian() * 0.4D;
        motZ = motZ + projectileTNT.random.nextGaussian() * 0.4D;
        double d0 = Mth.sqrt(motX * motX + motY * motY + motZ * motZ);
        projectileTNT.xPower = motX / d0 * 0.1D;
        projectileTNT.yPower = motY / d0 * 0.1D;
        projectileTNT.zPower = motZ / d0 * 0.1D;
        return projectileTNT;
    }
//
//    @OnlyIn(Dist.CLIENT)
//    public EntityProjectileTNT(World par1World, double par2, double par4, double par6, double par8, double par10, double par12)
//    {
//        super(par1World, par2, par4, par6, par8, par10, par12);
////        this.setSize(0.3125F, 0.3125F);
//    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isOnFire()
    {
        return false;
    }

    @Override
    protected void onHit(HitResult movingObjectPosition)
    {
        if (!this.level.isClientSide)
        {
            if (movingObjectPosition.getType() == RayTraceResult.Type.ENTITY)
            {
                EntityHitResult entityResult = (EntityHitResult) movingObjectPosition;
                if (!(entityResult.getEntity() instanceof Creeper))
                {
                    float difficulty = 0;
                    switch (this.level.getDifficulty())
                    {
                    case HARD:
                        difficulty = 2F;
                        break;
                    case NORMAL:
                        difficulty = 1F;
                        break;
                    }
                    entityResult.getEntity().hurt(DamageSource.fireball(this, this.owner), 6.0F + 3.0F * difficulty);
                }
            }

            this.level.explode(null, this.getX(), this.getY(), this.getZ(), 1.0F, false, this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
            this.remove();
        }
    }

    @Override
    public boolean isPickable()
    {
        return true;
    }
}
