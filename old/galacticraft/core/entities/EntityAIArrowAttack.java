package micdoodle8.mods.galacticraft.core.entities;

import java.util.EnumSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class EntityAIArrowAttack extends Goal
{
    private final Mob entityHost;

    private final RangedAttackMob rangedAttackEntityHost;
    private LivingEntity attackTarget;

    private int rangedAttackTime;
    private final double entityMoveSpeed;
    private final int field_96561_g;

    private final int maxRangedAttackTime;
    private final float field_96562_i;
    private final float field_82642_h;

    public EntityAIArrowAttack(RangedAttackMob par1IRangedAttackMob, double par2, int par4, float par5)
    {
        this(par1IRangedAttackMob, par2, par4, par4, par5);
    }

    public EntityAIArrowAttack(RangedAttackMob par1IRangedAttackMob, double par2, int par4, int par5, float par6)
    {
        this.rangedAttackTime = -1;

        if (!(par1IRangedAttackMob instanceof LivingEntity))
        {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        else
        {
            this.rangedAttackEntityHost = par1IRangedAttackMob;
            this.entityHost = (Mob) par1IRangedAttackMob;
            this.entityMoveSpeed = par2;
            this.field_96561_g = par4;
            this.maxRangedAttackTime = par5;
            this.field_96562_i = par6;
            this.field_82642_h = par6 * par6;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean canUse()
    {
        LivingEntity entitylivingbase = this.entityHost.getTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else
        {
            this.attackTarget = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean canContinueToUse()
    {
        return this.canUse() || !this.entityHost.getNavigation().isDone();
    }

    /**
     * Resets the task
     */
    @Override
    public void stop()
    {
        this.attackTarget = null;
        this.rangedAttackTime = -1;
    }

    /**
     * Updates the task
     */
    @Override
    public void tick()
    {
        double d0 = this.entityHost.distanceToSqr(this.attackTarget.getX(), this.attackTarget.getBoundingBox().minY, this.attackTarget.getZ());
        boolean flag = this.entityHost.getSensing().canSee(this.attackTarget);

        this.entityHost.getNavigation().moveTo(this.attackTarget, this.entityMoveSpeed);

        this.entityHost.getLookControl().setLookAt(this.attackTarget, 30.0F, 30.0F);
        float f;

        if (--this.rangedAttackTime == 0)
        {
            if (d0 > this.field_82642_h || !flag)
            {
                return;
            }

            f = Mth.sqrt(d0) / this.field_96562_i;
            float f1 = f;

            if (f < 0.1F)
            {
                f1 = 0.1F;
            }

            if (f1 > 1.0F)
            {
                f1 = 1.0F;
            }

            this.rangedAttackEntityHost.performRangedAttack(this.attackTarget, f1);
            this.rangedAttackTime = Mth.floor(f * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
        }
        else if (this.rangedAttackTime < 0)
        {
            f = Mth.sqrt(d0) / this.field_96562_i;
            this.rangedAttackTime = Mth.floor(f * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
        }
    }
}
