package team.galacticraft.galacticraft.common.core.blocks;

import net.minecraft.client.resources.language.I18n;
import team.galacticraft.galacticraft.common.api.block.IPartialSealableBlock;
import team.galacticraft.galacticraft.common.api.world.IZeroGDimension;
import team.galacticraft.galacticraft.common.core.items.IShiftDescription;
import team.galacticraft.galacticraft.common.core.items.ISortable;
import team.galacticraft.galacticraft.common.core.tile.TileEntityPlatform;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPlatform extends BlockAdvancedTile implements IPartialSealableBlock, IShiftDescription, ISortable
{
    public static final EnumProperty<EnumCorner> CORNER = EnumProperty.create("type", EnumCorner.class);
    public static final float HEIGHT = 0.875F;
    protected static final VoxelShape BOUNDING_BOX = Shapes.box(0.0D, 6 / 16.0D, 0.0D, 1.0D, HEIGHT, 1.0D);
    protected static final VoxelShape BOUNDING_BOX_ZEROG = Shapes.box(0.0D, 6 / 16.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    public static boolean ignoreCollisionTests;

    public enum EnumCorner implements StringRepresentable
    {
        NONE(0, "none"),
        NW(1, "sw"),
        SW(2, "nw"),
        NE(3, "se"),
        SE(4, "ne");
        // Yes these labels are the wrong way round, n should be s!  But the BlockState model is hard-coded to work with this as it is.

        private final int id;
        private final String name;

        EnumCorner(int id, String name)
        {
            this.id = id;
            this.name = name;
        }

        public int getId()
        {
            return this.id;
        }

        private final static EnumCorner[] values = values();

        public static EnumCorner byId(int meta)
        {
            return values[meta % values.length];
        }

        @Override
        public String getSerializedName()
        {
            return this.name;
        }
    }

    public BlockPlatform(Properties builder)
    {
        super(builder);
        this.registerDefaultState(stateDefinition.any().setValue(CORNER, EnumCorner.NONE));
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    private boolean checkAxis(Level worldIn, BlockPos pos, Block block, Direction facing)
    {
        int sameCount = 0;
        for (int i = 1; i <= 2; i++)
        {
            BlockState bs = worldIn.getBlockState(pos.relative(facing, i));
            if (bs.getBlock() == block && bs.getValue(BlockPlatform.CORNER) == EnumCorner.NONE)
            {
                sameCount++;
            }
        }

        return sameCount > 1;
    }

//    @Override
//    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side)
//    {
//        final Block id = GCBlocks.platform;
//
//        if (checkAxis(worldIn, pos, id, Direction.EAST) ||
//                checkAxis(worldIn, pos, id, Direction.WEST) ||
//                checkAxis(worldIn, pos, id, Direction.NORTH) ||
//                checkAxis(worldIn, pos, id, Direction.SOUTH))
//        {
//            return false;
//        }
//
//        if (worldIn.getBlockState(pos.offset(Direction.DOWN)).getBlock() == GCBlocks.platform && EnvType == Direction.UP)
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

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public boolean shouldSideBeRendered(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
//    {
//        return true;
//    }

//    @Override
//    public boolean doesSideBlockRendering(BlockState state, IBlockReader world, BlockPos pos, Direction face)
//    {
//        return false;
//    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter world)
    {
        return new TileEntityPlatform(state.getValue(CORNER)); //todo(marcus): check for corner when given pos
    }

    @Override
    public boolean isEntityBlock()
    {
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        final BlockEntity var9 = worldIn.getBlockEntity(pos);

        if (state.getBlock() != this || newState.getBlock() != this)
        {
            if (var9 instanceof TileEntityPlatform)
            {
                ((TileEntityPlatform) var9).onDestroy(var9);
            }
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean isSealed(Level worldIn, BlockPos pos, Direction direction)
    {
        return direction == Direction.UP;
    }

//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return 0;
//    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return I18n.get(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(CORNER, EnumCorner.byMetadata(meta));
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(CORNER);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.DECORATION;
    }

//    @Override
//    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, @Nullable Entity entityIn, boolean p_185477_7_)
//    {
//        if (ignoreCollisionTests) return;
//        TileEntity te = worldIn.getTileEntity(pos);
//        if (te instanceof TileEntityPlatform)
//        {
//            if (((TileEntityPlatform) te).noCollide()) return;
//        }
//        AxisAlignedBB axisalignedbb = this.getShape(state, worldIn, pos).offset(pos);
//
//        if (axisalignedbb != null && mask.intersects(axisalignedbb))
//        {
//            list.add(axisalignedbb);
//        }
//    }
//
////    @Override
////    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader world, BlockPos pos)
////    {
////        if (world instanceof World && ((World) world).dimension instanceof IZeroGDimension)
////            return BOUNDING_BOX_ZEROG;
////        return BOUNDING_BOX;
////    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        if (ignoreCollisionTests)
        {
            return Shapes.empty();
        }

        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (tile instanceof TileEntityPlatform)
        {
            if (((TileEntityPlatform) tile).noCollide())
            {
                return Shapes.empty();
            }
        }

        return super.getCollisionShape(state, worldIn, pos, context);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        if (worldIn instanceof Level && ((Level) worldIn).dimension instanceof IZeroGDimension)
        {
            return BOUNDING_BOX_ZEROG;
        }

        return BOUNDING_BOX;
    }

//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public AxisAlignedBB getSelectedBoundingBox(BlockState bs, World worldIn, BlockPos pos)
//    {
//        TileEntity te = worldIn.getTileEntity(pos);
//        if (te instanceof TileEntityPlatform)
//        {
//            if (((TileEntityPlatform) te).noCollide())
//            {
//                if (bs.getBlock() == this && bs.get(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.SE)
//                    return VoxelShapes.create((double)pos.getX() + 9/16D, (double)pos.getY(), (double)pos.getZ() + 9/16D, (double)pos.getX() + 1.0D, (double)pos.getY() + HEIGHT, (double)pos.getZ() + 1.0D);
//                else
//                    return VoxelShapes.create((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)pos.getX() + 7/16D, (double)pos.getY() + HEIGHT, (double)pos.getZ() + 7/16D);
//            }
//        }
//        return super.getSelectedBoundingBox(bs, worldIn, pos);
//    }
}
