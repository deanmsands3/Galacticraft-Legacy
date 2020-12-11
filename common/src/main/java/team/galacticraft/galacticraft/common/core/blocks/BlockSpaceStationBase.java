package team.galacticraft.galacticraft.common.core.blocks;

import net.minecraft.world.level.block.EntityBlock;
import team.galacticraft.galacticraft.common.core.tile.IMultiBlock;
import team.galacticraft.galacticraft.common.core.tile.TileEntitySpaceStationBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class BlockSpaceStationBase extends Block implements EntityBlock
{
    public BlockSpaceStationBase(Properties builder)
    {
        super(builder);
    }

    @Override
    public float getDestroySpeed(BlockState blockState, BlockGetter worldIn, BlockPos pos)
    {
        return -1.0F;
    }

    //    @Override
//    public float getBlockHardness(BlockState blockState, World worldIn, BlockPos pos)
//    {
//        return -1.0F;
//    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        final BlockEntity tileAt = worldIn.getBlockEntity(pos);

        if (tileAt instanceof IMultiBlock)
        {
            ((IMultiBlock) tileAt).onDestroy(tileAt);
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter world)
    {
        return new TileEntitySpaceStationBase();
    }

    @Override
    public boolean isEntityBlock()
    {
        return true;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(worldIn, pos, state, placer, stack);

        BlockEntity tile = worldIn.getBlockEntity(pos);

        if (tile instanceof IMultiBlock)
        {
            ((IMultiBlock) tile).onCreate(worldIn, pos);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean triggerEvent(BlockState state, Level worldIn, BlockPos pos, int id, int param)
    {
        super.triggerEvent(state, worldIn, pos, id, param);
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        return tileentity != null && tileentity.triggerEvent(id, param);
    }

    @Override
    @Nullable
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos)
    {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        return tileentity instanceof MenuProvider ? (MenuProvider) tileentity : null;
    }
}
