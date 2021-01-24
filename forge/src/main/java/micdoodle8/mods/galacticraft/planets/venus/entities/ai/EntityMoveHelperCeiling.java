package micdoodle8.mods.galacticraft.planets.venus.entities.ai;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;

public class EntityMoveHelperCeiling extends MoveControl
{
    public EntityMoveHelperCeiling(Mob entitylivingIn)
    {
        super(entitylivingIn);
    }

    @Override
    public void tick()
    {
        this.mob.setZza(0.0F);

        if (this.hasWanted())
        {
            this.operation = MovementController.Action.WAIT;
            int i = Mth.floor(this.mob.getBoundingBox().minY + 0.5D);
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedZ - this.mob.getZ();
            double d2 = this.wantedY - (double) i;
            double d3 = d0 * d0 + d1 * d1;

            if (d3 >= 2.500000277905201E-7D)
            {
                float f = (float) Mth.atan2(d1, d0) * Constants.RADIANS_TO_DEGREES - 90.0F;
                this.mob.yRot = f;
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));

                if (d2 > 0.0D && d0 * d0 + d1 * d1 < 1.0D)
                {
                    this.mob.getJumpControl().jump();
                }
            }
        }
    }
}