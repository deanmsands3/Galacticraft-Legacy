package team.galacticraft.galacticraft.common.core.wrappers;

import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ScheduledBlockChange
{
    private BlockPos changePosition;
    private BlockState change;
    private int changeUpdateFlag;

    public ScheduledBlockChange(BlockVec3 changePosition, BlockState change)
    {
        this(changePosition.toBlockPos(), change, 3);
    }

    public ScheduledBlockChange(BlockPos pos, BlockState change, int changeUpdateFlag)
    {
        this.changePosition = pos;
        this.change = change;
        this.changeUpdateFlag = changeUpdateFlag;
    }

    public BlockPos getChangePosition()
    {
        return this.changePosition;
    }

    public void setChangePosition(BlockPos changePosition)
    {
        this.changePosition = changePosition;
    }

    public BlockState getChangeState()
    {
        return this.change;
    }

    public void setChangeState(BlockState change)
    {
        this.change = change;
    }

    public int getChangeUpdateFlag()
    {
        return changeUpdateFlag;
    }

    public void setChangeUpdateFlag(int changeUpdateFlag)
    {
        this.changeUpdateFlag = changeUpdateFlag;
    }
}
