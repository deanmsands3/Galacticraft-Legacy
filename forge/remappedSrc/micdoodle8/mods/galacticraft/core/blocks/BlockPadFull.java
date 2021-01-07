package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFueler;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class BlockPadFull extends BlockAdvancedTile implements IPartialSealableBlock
{
    private static final VoxelShape SHAPE = VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);

    public BlockPadFull(Properties builder)
    {
        super(builder);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof IMultiBlock)
        {
            ((IMultiBlock)tile).onDestroy(tile);
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return this == GCBlocks.FULL_ROCKET_LAUNCH_PAD ? new TileEntityLandingPad() : new TileEntityBuggyFueler();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        world.notifyBlockUpdate(pos, state, state, Constants.BlockFlags.DEFAULT);
    }

    @Override
    public boolean isSealed(World world, BlockPos pos, Direction direction)
    {
        return direction == Direction.UP;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        return new ItemStack(this == GCBlocks.FULL_ROCKET_LAUNCH_PAD ? GCBlocks.ROCKET_LAUNCH_PAD : GCBlocks.BUGGY_FUELING_PAD, 1);
    }
}