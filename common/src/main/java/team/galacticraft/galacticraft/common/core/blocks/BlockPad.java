package team.galacticraft.galacticraft.common.core.blocks;

import net.minecraft.client.resources.language.I18n;
import team.galacticraft.galacticraft.common.api.block.IPartialSealableBlock;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.items.IShiftDescription;
import team.galacticraft.galacticraft.common.core.items.ISortable;
import team.galacticraft.galacticraft.common.core.tile.TileEntityBuggyFuelerSingle;
import team.galacticraft.galacticraft.common.core.tile.TileEntityLandingPadSingle;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPad extends BlockAdvancedTile implements IPartialSealableBlock, IShiftDescription, ISortable
{
    protected static final VoxelShape AABB = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.1875, 1.0);

    public BlockPad(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return AABB;
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        for (int i = 0; i < 2; i++)
//        {
//            list.add(new ItemStack(this, 1, i));
//        }
//    }

    private boolean checkAxis(Level worldIn, BlockPos pos, Block block, Direction facing)
    {
        int sameCount = 0;
        for (int i = 1; i <= 3; i++)
        {
            if (worldIn.getBlockState(pos.relative(facing, i)).getBlock() == block)
            {
                sameCount++;
            }
        }

        return sameCount < 3;
    }

//    @Override
//    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side)
//    {
//        final Block id = GCBlocks.landingPad;
//
//        if (!checkAxis(worldIn, pos, id, Direction.EAST) ||
//                !checkAxis(worldIn, pos, id, Direction.WEST) ||
//                !checkAxis(worldIn, pos, id, Direction.NORTH) ||
//                !checkAxis(worldIn, pos, id, Direction.SOUTH))
//        {
//            return false;
//        }
//
//        if (worldIn.getBlockState(pos.offset(Direction.DOWN)).getBlock() == GCBlocks.landingPad && EnvType == Direction.UP)
//        {
//            return false;
//        }
//        else
//        {
//            return this.canPlaceBlockAt(worldIn, pos);
//        }
//    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter world)
    {
        return this == GCBlocks.landingPad ? new TileEntityLandingPadSingle() : new TileEntityBuggyFuelerSingle();
    }

    @Override
    public boolean isEntityBlock()
    {
        return true;
    }

    @Override
    public boolean isSealed(Level worldIn, BlockPos pos, Direction direction)
    {
        return direction == Direction.UP;
    }

//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return getMetaFromState(state);
//    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return I18n.get(this.getDescriptionId() + ".description");
//        return I18n.get("tile.buggy_pad.description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.PAD;
    }
}
