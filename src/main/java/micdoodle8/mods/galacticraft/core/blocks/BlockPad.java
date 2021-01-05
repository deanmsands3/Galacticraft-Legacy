package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFuelerSingle;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPadSingle;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockPad extends BlockAdvancedTile implements IPartialSealableBlock, IShiftDescription, ISortable
{
    private static final VoxelShape SHAPE = VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);

    public BlockPad(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    private boolean checkAxis(IWorldReader world, BlockPos pos, Direction facing)
    {
        int sameCount = 0;

        for (int i = 1; i <= 3; i++)
        {
            if (world.getBlockState(pos.offset(facing, i)).getBlock() == this)
            {
                sameCount++;
            }
        }

        return sameCount < 3;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos)
    {
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            if (!this.checkAxis(world, pos, Direction.EAST) || !this.checkAxis(world, pos, Direction.WEST) || !this.checkAxis(world, pos, Direction.NORTH) || !this.checkAxis(world, pos, Direction.SOUTH))
            {
                return false;
            }

            if (world.getBlockState(pos.offset(Direction.DOWN)).getBlock() == this && direction == Direction.UP)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return this == GCBlocks.ROCKET_LAUNCH_PAD ? new TileEntityLandingPadSingle() : new TileEntityBuggyFuelerSingle();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public boolean isSealed(World world, BlockPos pos, Direction direction)
    {
        return direction == Direction.UP;
    }

    @Override
    public String getShiftDescription(ItemStack itemStack)
    {
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack itemStack)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.PAD;
    }
}