package team.galacticraft.galacticraft.common.core.blocks;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.level.block.EntityBlock;
import team.galacticraft.galacticraft.common.api.block.IPartialSealableBlock;
import team.galacticraft.galacticraft.common.core.items.IShiftDescription;
import team.galacticraft.galacticraft.common.core.items.ISortable;
import team.galacticraft.galacticraft.common.core.tile.TileEntityScreen;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockScreen extends BlockAdvanced implements IShiftDescription, IPartialSealableBlock, ISortable, EntityBlock
{
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty LEFT = BooleanProperty.create("left");
    public static final BooleanProperty RIGHT = BooleanProperty.create("right");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    protected static final float boundsFront = 0.094F;
    protected static final float boundsBack = 1.0F - boundsFront;
    protected static final VoxelShape DOWN_AABB = Shapes.box(0F, 0F, 0F, 1.0F, boundsBack, 1.0F);
    protected static final VoxelShape UP_AABB = Shapes.box(0F, boundsFront, 0F, 1.0F, 1.0F, 1.0F);
    protected static final VoxelShape NORTH_AABB = Shapes.box(0F, 0F, boundsFront, 1.0F, 1.0F, 1.0F);
    protected static final VoxelShape SOUTH_AABB = Shapes.box(0F, 0F, 0F, 1.0F, 1.0F, boundsBack);
    protected static final VoxelShape WEST_AABB = Shapes.box(boundsFront, 0F, 0F, 1.0F, 1.0F, 1.0F);
    protected static final VoxelShape EAST_AABB = Shapes.box(0F, 0F, 0F, boundsBack, 1.0F, 1.0F);

    //Metadata: 0-5 = direction of screen back;  bit 3 = reserved for future use
    public BlockScreen(Properties builder)
    {
        super(builder);
        this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LEFT, false).setValue(RIGHT, false).setValue(UP, false).setValue(DOWN, false));
    }

//    @Override
//    public boolean isSideSolid(BlockState base_state, IBlockReader world, BlockPos pos, Direction direction)
//    {
//        return direction.ordinal() != getMetaFromState(world.getBlockState(pos));
//    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        ((TileEntityScreen) worldIn.getBlockEntity(pos)).breakScreen(state);
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

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
//        return face.ordinal() == getMetaFromState(state) ? BlockFaceShape.UNDEFINED : BlockFaceShape.BOWL;
//    }

//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//    {
////        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
////        int change = Direction.byHorizontalIndex(angle).getOpposite().getIndex();
//        worldIn.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing()), 3);
//    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult onUseWrench(Level world, BlockPos pos, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        world.setBlock(pos, world.getBlockState(pos).setValue(FACING, world.getBlockState(pos).getValue(FACING).getClockWise()), 3);
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter world)
    {
        return new TileEntityScreen();
    }

    @Override
    public boolean isEntityBlock()
    {
        return true;
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public InteractionResult onMachineActivated(Level world, BlockPos pos, BlockState state, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof TileEntityScreen)
        {
            ((TileEntityScreen) tile).changeChannel();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (tile instanceof TileEntityScreen)
        {
            ((TileEntityScreen) tile).refreshConnections(true);
        }
    }

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

    @Override
    public boolean isSealed(Level worldIn, BlockPos pos, Direction direction)
    {
        return true;
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

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byIndex(meta);
//        return this.getDefaultState().with(FACING, enumfacing);
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, LEFT, RIGHT, UP, DOWN);
    }

//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        TileEntityScreen screen = (TileEntityScreen) worldIn.getTileEntity(pos);
//        return state.with(LEFT, screen.connectedLeft)
//                .with(RIGHT, screen.connectedRight)
//                .with(UP, screen.connectedUp)
//                .with(DOWN, screen.connectedDown);
//    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }
}
