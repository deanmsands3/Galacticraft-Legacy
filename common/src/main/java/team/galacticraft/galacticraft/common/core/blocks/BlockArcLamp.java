package team.galacticraft.galacticraft.common.core.blocks;

import team.galacticraft.galacticraft.core.items.IShiftDescription;
import team.galacticraft.galacticraft.core.items.ISortable;
import team.galacticraft.galacticraft.core.tile.TileEntityArclamp;
import team.galacticraft.galacticraft.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import team.galacticraft.galacticraft.core.util.RedstoneUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockArcLamp extends BlockAdvanced implements IShiftDescription, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
//    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    protected static final VoxelShape DOWN_AABB = Shapes.box(0.2F, 0.0F, 0.2F, 0.8F, 0.6F, 0.8F);
    protected static final VoxelShape UP_AABB = Shapes.box(0.2F, 0.4F, 0.2F, 0.8F, 1.0F, 0.8F);
    protected static final VoxelShape NORTH_AABB = Shapes.box(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.6F);
    protected static final VoxelShape SOUTH_AABB = Shapes.box(0.2F, 0.2F, 0.4F, 0.8F, 0.8F, 1.0F);
    protected static final VoxelShape WEST_AABB = Shapes.box(0.0F, 0.2F, 0.2F, 0.6F, 0.8F, 0.8F);
    protected static final VoxelShape EAST_AABB = Shapes.box(0.4F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);

    //Metadata: bits 0-2 are the LogicalSide of the base plate using standard LogicalSide convention (0-5)

    public BlockArcLamp(Properties builder)
    {
        super(builder);
        this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));  //.with(ACTIVE, true));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        switch (state.getValue(FACING))
        {
        case EAST:
            return EAST_AABB;
        case WEST:
            return WEST_AABB;
        case SOUTH:
            return SOUTH_AABB;
        case NORTH:
            return NORTH_AABB;
        case DOWN:
            return DOWN_AABB;
        case UP:
        default:
            return UP_AABB;
        }
    }

    @Override
    public int getLightValue(BlockState state, BlockGetter world, BlockPos pos)
    {
        Block block = state.getBlock();
        if (block != this)
        {
            return block.getLightEmission(state);
        }
        /**
         * Gets the light value of the specified block coords. Args: x, y, z
         */

        if (world instanceof Level)
        {
            return RedstoneUtil.isBlockReceivingRedstone((Level) world, pos) ? 0 : this.lightEmission;
        }

        return 0;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos)
    {
        return 1;
    }

//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockReader worldIn, BlockPos pos)
//    {
//        double boundsMin = 0.2D;
//        double boundsMax = 0.8D;
//        return VoxelShapes.create(pos.getX() + boundsMin, pos.getY() + boundsMin, pos.getZ() + boundsMin, pos.getX() + boundsMax, pos.getY() + boundsMax, pos.getZ() + boundsMax);
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

//    @Override
//    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
//    {
//        for (Direction side : Direction.values())
//        {
//            BlockPos offsetPos = pos.offset(LogicalSide);
//            BlockState state = worldIn.getBlockState(offsetPos);
//            if (state.getBlock().isSideSolid(state, worldIn, offsetPos, side.getOpposite()))
//            {
//                return true;
//            }
//        }
//        return false;
//    } TODO

//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//    {
//        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
//    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }

//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        Direction side = state.get(FACING);
//
//        BlockPos offsetPos = pos.offset(LogicalSide);
//        BlockState state1 = worldIn.getBlockState(offsetPos);
//        if (state1.getBlock().isSideSolid(state1, worldIn, offsetPos, Direction.byIndex(LogicalSide.getIndex() ^ 1)))
//        {
//            return;
//        }
//
//        this.dropBlockAsItem(worldIn, pos, state, 0);
//        worldIn.removeBlock(pos, false);
//    } TODO

//    @Override
//    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
//    {
//        EnumFacing LogicalSide = worldIn.getBlockState(pos).getValue(FACING);
//        float var8 = 0.3F;
//
//        if (LogicalSide == EnumFacing.WEST)
//        {
//            this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
//        }
//        else if (LogicalSide == EnumFacing.EAST)
//        {
//            this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
//        }
//        else if (LogicalSide == EnumFacing.NORTH)
//        {
//            this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
//        }
//        else if (LogicalSide == EnumFacing.SOUTH)
//        {
//            this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
//        }
//        else if (LogicalSide == EnumFacing.DOWN)
//        {
//            this.setBlockBounds(0.5F - var8, 0.0F, 0.5F - var8, 0.5F + var8, 0.6F, 0.5F + var8);
//        }
//        else
//        {
//            this.setBlockBounds(0.5F - var8, 0.4F, 0.5F - var8, 0.5F + var8, 1.0F, 0.5F + var8);
//        }
//
//        return super.collisionRayTrace(worldIn, pos, start, end);
//    }

    @Override
    public InteractionResult onUseWrench(Level world, BlockPos pos, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        if (!world.isClientSide)
        {
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof TileEntityArclamp)
            {
                ((TileEntityArclamp) tile).facingChanged();
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityArclamp();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    //    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
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
//        Direction enumfacing = Direction.byIndex(meta);
//        return this.getDefaultState().with(FACING, enumfacing);
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);  //, ACTIVE });
    }

//    @Override
//    public IBlockState getActualState(IBlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        return state.with(ACTIVE, ((TileEntityArclamp) worldIn.getTileEntity(pos)).getEnabled());
//    }
//
    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }
}
