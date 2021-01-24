package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.fluid.OxygenPressureProtocol;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BlockBreathableAir extends BlockThermalAir
{
    public BlockBreathableAir(Properties builder)
    {
        super(builder);
        this.registerDefaultState(stateDefinition.any().setValue(THERMAL, false));
    }

//    @Override
//    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
//    {
//        return true;
//    }
//
//    @Override
//    public PushReaction getMobilityFlag(BlockState state)
//    {
//        return PushReaction.DESTROY;
//    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block oldBlock, BlockPos fromPos, boolean isMoving)
    {
        if (Blocks.AIR != oldBlock)
        //Do no check if replacing breatheableAir with a solid block, although that could be dividing a sealed space
        {
            // Check if replacing a passthrough breathable block, like a torch - if so replace with BreathableAir not Air
            if (Blocks.AIR == state.getBlock())
            {
                Direction side;
                if (pos.getX() != fromPos.getX())
                {
                    side = pos.getX() > fromPos.getX() ? Direction.EAST : Direction.WEST;
                }
                else if (pos.getY() != fromPos.getY())
                {
                    side = pos.getY() > fromPos.getY() ? Direction.UP : Direction.DOWN;
                }
                else
                {
                    side = pos.getZ() > fromPos.getZ() ? Direction.SOUTH : Direction.NORTH;
                }
                if (OxygenPressureProtocol.canBlockPassAir(worldIn, state, fromPos, side))
                {
                    worldIn.setBlock(fromPos, GCBlocks.BREATHEABLE_AIR.defaultBlockState(), 6);
                }
            }
            // In all cases, trigger a leak check at this point
            OxygenPressureProtocol.onEdgeBlockUpdated(worldIn, pos);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(THERMAL);
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(THERMAL, meta % 2 == 1);
//    }

//    @Override
//    public int getLightOpacity(BlockState state)
//    {
//        return 0;
//    }

//    @Override
//    public void breakBlock(World worldIn, BlockPos vec, BlockState state)
//    {
//    }
}
