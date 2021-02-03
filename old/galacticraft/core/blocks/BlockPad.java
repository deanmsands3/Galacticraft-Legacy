package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFuelerSingle;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPadSingle;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPad extends BlockAdvancedTile implements IPartialSealableBlock, IShiftDescription, ISortable
{
    private static final VoxelShape SHAPE = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);

    public BlockPad(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    private boolean checkAxis(LevelReader world, BlockPos pos, Direction facing)
    {
        int sameCount = 0;

        for (int i = 1; i <= 3; i++)
        {
            if (world.getBlockState(pos.relative(facing, i)).getBlock() == this)
            {
                sameCount++;
            }
        }

        return sameCount < 3;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos)
    {
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            if (!this.checkAxis(world, pos, Direction.EAST) || !this.checkAxis(world, pos, Direction.WEST) || !this.checkAxis(world, pos, Direction.NORTH) || !this.checkAxis(world, pos, Direction.SOUTH))
            {
                return false;
            }

            if (world.getBlockState(pos.relative(Direction.DOWN)).getBlock() == this && direction == Direction.UP)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return this == GCBlocks.ROCKET_LAUNCH_PAD ? new TileEntityLandingPadSingle() : new TileEntityBuggyFuelerSingle();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public boolean isSealed(Level world, BlockPos pos, Direction direction)
    {
        return direction == Direction.UP;
    }

    @Override
    public String getShiftDescription(ItemStack itemStack)
    {
        return GCCoreUtil.translate(this.getDescriptionId() + ".description");
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