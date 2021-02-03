package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * An advanced block class that is to be extended for wrenching capabilities.
 */
public abstract class BlockTileGC extends BlockAdvanced
{
    public BlockTileGC(Properties builder)
    {
        super(builder);
//        this.hasTileEntity = true;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (worldIn.getBlockEntity(pos) instanceof Container)
        {
            Containers.dropContents(worldIn, pos, (Container) worldIn.getBlockEntity(pos));
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level worldIn, BlockPos pos, int eventID, int eventParam)
    {
        super.triggerEvent(state, worldIn, pos, eventID, eventParam);
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        return tileentity != null && tileentity.triggerEvent(eventID, eventParam);
    }
}
