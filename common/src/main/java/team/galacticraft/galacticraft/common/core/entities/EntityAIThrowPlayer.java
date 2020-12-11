package team.galacticraft.galacticraft.common.core.entities;

import team.galacticraft.galacticraft.common.Constants;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import java.util.EnumSet;

public class EntityAIThrowPlayer extends Goal
{
    EntitySkeletonBoss skeletonBoss;

    Player targetPlayer;

    public EntityAIThrowPlayer(EntitySkeletonBoss boss)
    {
        this.skeletonBoss = boss;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse()
    {
        final Player player = this.skeletonBoss.level.getNearestPlayer(this.skeletonBoss, 5.0F);

        if (player == null)
        {
            return false;
        }
        else
        {
            this.targetPlayer = player;
            return true;
        }
    }

    @Override
    public void start()
    {
        this.skeletonBoss.setTarget(this.targetPlayer);

        // if (this.skeletonBoss.getDistanceToEntity(this.targetPlayer) <= 5.0F)
        {
            double d0 = this.skeletonBoss.getX() - this.targetPlayer.getX();
            double d1;

            for (d1 = this.skeletonBoss.getZ() - this.targetPlayer.getZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D)
            {
                d0 = (Math.random() - Math.random()) * 0.01D;
            }

            this.targetPlayer.hurtDir = (float) Math.atan2(d1, d0) * Constants.RADIANS_TO_DEGREES - this.targetPlayer.yRot;

            this.targetPlayer.knockback(this.skeletonBoss, 20, d0, d1);
        }

        super.start();
    }
}
