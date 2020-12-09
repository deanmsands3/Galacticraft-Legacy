package team.galacticraft.galacticraft.common.core.blocks;

import team.galacticraft.galacticraft.core.items.IShiftDescription;
import team.galacticraft.galacticraft.core.items.ISortable;
import team.galacticraft.galacticraft.core.tile.TileEntityTreasureChest;
import team.galacticraft.galacticraft.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.animal.Cat;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTier1TreasureChest extends Block implements IShiftDescription, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape AABB = Shapes.box(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);

    public BlockTier1TreasureChest(Properties builder)
    {
        super(builder);
        this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
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
//
//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockReader worldIn, BlockPos pos)
//    {
//        if (worldIn.getBlockState(pos.north()).getBlock() == this)
//        {
//            this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
//        }
//        else if (worldIn.getBlockState(pos.south()).getBlock() == this)
//        {
//            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
//        }
//        else if (worldIn.getBlockState(pos.west()).getBlock() == this)
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
//        }
//        else if (worldIn.getBlockState(pos.east()).getBlock() == this)
//        {
//            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
//        }
//        else
//        {
//            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
//        }
//    }

//    @Override
//    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
//    {
////        this.checkForSurroundingChests(worldIn, pos, state);
////        Iterator iterator = Direction.Plane.HORIZONTAL.iterator();
////
////        while (iterator.hasNext())
////        {
////            Direction enumfacing = (Direction) iterator.next();
////            BlockPos blockpos1 = pos.offset(enumfacing);
////            BlockState iblockstate1 = worldIn.getBlockState(blockpos1);
////
////            if (iblockstate1.getBlock() == this)
////            {
////                this.checkForSurroundingChests(worldIn, blockpos1, iblockstate1);
////            }
////        }
//    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getPlayer().getDirection());
    }

//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//    {
//        Direction enumfacing = Direction.byHorizontalIndex(MathHelper.floor((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
//        state = state.with(FACING, enumfacing);
//        BlockPos blockpos1 = pos.north();
//        BlockPos blockpos2 = pos.south();
//        BlockPos blockpos3 = pos.west();
//        BlockPos blockpos4 = pos.east();
//        boolean flag = this == worldIn.getBlockState(blockpos1).getBlock();
//        boolean flag1 = this == worldIn.getBlockState(blockpos2).getBlock();
//        boolean flag2 = this == worldIn.getBlockState(blockpos3).getBlock();
//        boolean flag3 = this == worldIn.getBlockState(blockpos4).getBlock();
//
//        if (!flag && !flag1 && !flag2 && !flag3)
//        {
//            worldIn.setBlockState(pos, state, 3);
//        }
//        else if (enumfacing.getAxis() == Direction.Axis.X && (flag || flag1))
//        {
//            if (flag)
//            {
//                worldIn.setBlockState(blockpos1, state, 3);
//            }
//            else
//            {
//                worldIn.setBlockState(blockpos2, state, 3);
//            }
//
//            worldIn.setBlockState(pos, state, 3);
//        }
//        else if (enumfacing.getAxis() == Direction.Axis.Z && (flag2 || flag3))
//        {
//            if (flag2)
//            {
//                worldIn.setBlockState(blockpos3, state, 3);
//            }
//            else
//            {
//                worldIn.setBlockState(blockpos4, state, 3);
//            }
//
//            worldIn.setBlockState(pos, state, 3);
//        }
//    }

//    @Override
//    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
//    {
//        int i = 0;
//        BlockPos blockpos1 = pos.west();
//        BlockPos blockpos2 = pos.east();
//        BlockPos blockpos3 = pos.north();
//        BlockPos blockpos4 = pos.south();
//
//        if (worldIn.getBlockState(blockpos1).getBlock() == this)
//        {
//            ++i;
//        }
//
//        if (worldIn.getBlockState(blockpos2).getBlock() == this)
//        {
//            ++i;
//        }
//
//        if (worldIn.getBlockState(blockpos3).getBlock() == this)
//        {
//            ++i;
//        }
//
//        if (worldIn.getBlockState(blockpos4).getBlock() == this)
//        {
//            ++i;
//        }
//
//        return i <= 1;
//    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        BlockEntity tileentity = worldIn.getBlockEntity(pos);

        if (tileentity instanceof TileEntityTreasureChest)
        {
            tileentity.clearCache();
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);

        if (tileentity instanceof Container)
        {
            Containers.dropContents(worldIn, pos, (Container) tileentity);
            worldIn.updateNeighbourForOutputSignal(pos, this);
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit)
    {
        if (!worldIn.isClientSide)
        {
//            TileEntity tile = worldIn.getTileEntity(pos);
            NetworkHooks.openGui((ServerPlayer) playerIn, getMenuProvider(state, worldIn, pos), buf -> buf.writeBlockPos(pos));
//            playerIn.displayGUIChest((IInventory) tile);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityTreasureChest();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    public int isProvidingWeakPower(BlockGetter worldIn, BlockPos pos, BlockState state, Direction side)
    {
        if (!this.isSignalSource(state))
        {
            return 0;
        }
        else
        {
            int i = 0;
            BlockEntity tileentity = worldIn.getBlockEntity(pos);

            if (tileentity instanceof TileEntityTreasureChest)
            {
                i = ((TileEntityTreasureChest) tileentity).numPlayersUsing;
            }

            return Mth.clamp(i, 0, 15);
        }
    }

    public int isProvidingStrongPower(BlockGetter worldIn, BlockPos pos, BlockState state, Direction side)
    {
        return side == Direction.UP ? this.isProvidingWeakPower(worldIn, pos, state, side) : 0;
    }

//    private boolean isBlocked(World worldIn, BlockPos pos)
//    {
//        return this.isBelowSolidBlock(worldIn, pos) || this.isOcelotSittingOnChest(worldIn, pos);
//    }

//    private boolean isBelowSolidBlock(World worldIn, BlockPos pos)
//    {
//        return worldIn.isSideSolid(pos.up(), Direction.DOWN, false);
//    }

    private boolean isOcelotSittingOnChest(Level worldIn, BlockPos pos)
    {
        List<Cat> list = worldIn.getEntitiesOfClass(Cat.class, new AABB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1));
        if (!list.isEmpty())
        {
            for (Cat catentity : list)
            {
                if (catentity.isSitting())
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byIndex(meta);
//
//        if (enumfacing.getAxis() == Direction.Axis.Y)
//        {
//            enumfacing = Direction.NORTH;
//        }
//
//        return this.getDefaultState().with(FACING, enumfacing);
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
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

//    @Override
//    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
//    {
//        return false;
//    }
//
    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.TREASURE;
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