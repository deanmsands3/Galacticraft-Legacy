package team.galacticraft.galacticraft.common.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * If block requires updates when oxygen is added and removed, implement this
 * into your block class.
 * <p>
 * It is recommended that blocks implementing this should be set to tick
 * randomly, and should override @updateTick() also to carry out oxygen checks.
 */
public interface IOxygenReliantBlock
{
    void onOxygenRemoved(Level world, BlockPos pos, BlockState currentState);

    void onOxygenAdded(Level world, BlockPos pos, BlockState currentState);
}
