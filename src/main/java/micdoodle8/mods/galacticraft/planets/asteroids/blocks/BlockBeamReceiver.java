package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorageTile;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BlockBeamReceiver extends BlockTileGC implements IShiftDescription, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected static final VoxelShape UP_AABB = Shapes.box(0.3F, 0.3F, 0.3F, 0.7F, 1.0F, 0.7F);
    protected static final VoxelShape DOWN_AABB = Shapes.box(0.2F, 0.0F, 0.2F, 0.8F, 0.42F, 0.8F);
    protected static final VoxelShape EAST_AABB = Shapes.box(0.58F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);
    protected static final VoxelShape WEST_AABB = Shapes.box(0.0F, 0.2F, 0.2F, 0.42F, 0.8F, 0.8F);
    protected static final VoxelShape NORTH_AABB = Shapes.box(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.42F);
    protected static final VoxelShape SOUTH_AABB = Shapes.box(0.2F, 0.2F, 0.58F, 0.8F, 0.8F, 1.0F);

    public BlockBeamReceiver(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        switch (state.getValue(FACING))
        {
        case UP:
            return UP_AABB;
        case DOWN:
            return DOWN_AABB;
        case EAST:
            return EAST_AABB;
        case WEST:
            return WEST_AABB;
        case SOUTH:
            return SOUTH_AABB;
        default:
        case NORTH:
            return NORTH_AABB;
        }
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        int oldMeta = getMetaFromState(worldIn.getBlockState(pos));
//        int meta = this.getMetadataFromAngle(worldIn, pos, Direction.byIndex(oldMeta).getOpposite());
//
//        if (meta == -1)
//        {
//            worldIn.destroyBlock(pos, true);
//        }
//        else if (meta != oldMeta)
//        {
//            worldIn.setBlockState(pos, getStateFromMeta(meta), 3);
//            TileEntity thisTile = worldIn.getTileEntity(pos);
//            if (thisTile instanceof TileEntityBeamReceiver)
//            {
//                TileEntityBeamReceiver thisReceiver = (TileEntityBeamReceiver) thisTile;
//                thisReceiver.setFacing(Direction.byIndex(meta));
//                thisReceiver.invalidateReflector();
//                thisReceiver.initiateReflector();
//            }
//        }
//
//        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
//    } TODO ?

//    @Override
//    public void onBlockAdded(World world, BlockPos pos, BlockState state)
//    {
//        TileEntity thisTile = world.getTileEntity(pos);
//        if (thisTile instanceof TileEntityBeamReceiver)
//        {
//            ((TileEntityBeamReceiver) thisTile).setFacing(Direction.byIndex(getMetaFromState(state)));
//        }
//    } TODO ?

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
//    {
//        int meta = getMetaFromState(world.getBlockState(pos));
//
//        if (meta != -1)
//        {
//            EnumFacing dir = EnumFacing.getFront(meta);
//
//            switch (dir)
//            {
//            case UP:
//                this.setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 1.0F, 0.7F);
//                break;
//            case DOWN:
//                this.setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.42F, 0.8F);
//                break;
//            case EAST:
//                this.setBlockBounds(0.58F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);
//                break;
//            case WEST:
//                this.setBlockBounds(0.0F, 0.2F, 0.2F, 0.42F, 0.8F, 0.8F);
//                break;
//            case NORTH:
//                this.setBlockBounds(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.42F);
//                break;
//            case SOUTH:
//                this.setBlockBounds(0.2F, 0.2F, 0.58F, 0.8F, 0.8F, 1.0F);
//                break;
//            default:
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//    }

    private int getMetadataFromAngle(Level world, BlockPos pos, Direction side)
    {
        Direction direction = side.getOpposite();

        BlockEntity tileAt = world.getBlockEntity(pos.offset(direction.getStepX(), direction.getStepY(), direction.getStepZ()));

        if (tileAt instanceof EnergyStorageTile)
        {
            if (((EnergyStorageTile) tileAt).getModeFromDirection(direction.getOpposite()) != null)
            {
                return direction.ordinal();
            }
            else
            {
                return -1;
            }
        }

        if (EnergyUtil.otherModCanReceive(tileAt, direction.getOpposite()))
        {
            return direction.ordinal();
        }

        for (Direction adjacentDir : Direction.values())
        {
            if (adjacentDir == direction)
            {
                continue;
            }
            tileAt = world.getBlockEntity(pos.offset(adjacentDir.getStepX(), adjacentDir.getStepY(), adjacentDir.getStepZ()));

            if (tileAt instanceof IConductor)
            {
                continue;
            }

            if (tileAt instanceof EnergyStorageTile && ((EnergyStorageTile) tileAt).getModeFromDirection(adjacentDir.getOpposite()) != null)
            {
                return adjacentDir.ordinal();
            }

            if (EnergyUtil.otherModCanReceive(tileAt, adjacentDir.getOpposite()))
            {
                return adjacentDir.ordinal();
            }
        }

        return -1;
    }

//    @Override
//    public BlockState getStateForPlacement(BlockItemUseContext context)
//    {
//        return getStateFromMeta(this.getMetadataFromAngle(context.getWorld(), context.getPos(), context.getFace()));
//    }

//    @Override
//    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side)
//    {
//        if (this.getMetadataFromAngle(worldIn, pos, LogicalSide) != -1)
//        {
//            return true;
//        }
//
//        if (worldIn.isRemote)
//        {
//            this.sendIncorrectSideMessage();
//        }
//
//        return false;
//    }

    @Environment(EnvType.CLIENT)
    private void sendIncorrectSideMessage()
    {
        Minecraft.getInstance().player.sendMessage(new TextComponent(EnumColor.RED + GCCoreUtil.translate("gui.receiver.cannot_attach")));
    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.INVISIBLE;
    }

//    @Override
//    public int damageDropped(BlockState metadata)
//    {
//        return 0;
//    }


    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityBeamReceiver();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        list.add(new ItemStack(this, 1));
//    }

    @Override
    public InteractionResult onMachineActivated(Level worldIn, BlockPos pos, BlockState state, Player playerIn, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        BlockEntity tile = worldIn.getBlockEntity(pos);

        if (tile instanceof TileEntityBeamReceiver)
        {
            return ((TileEntityBeamReceiver) tile).onMachineActivated(worldIn, pos, state, playerIn, hand, heldItem, hit);
        }

        return InteractionResult.PASS;
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getPlayer().getDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }
}
