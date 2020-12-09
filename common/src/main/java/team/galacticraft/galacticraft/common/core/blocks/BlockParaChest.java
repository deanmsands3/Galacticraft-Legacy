package team.galacticraft.galacticraft.common.core.blocks;

import team.galacticraft.galacticraft.core.inventory.ContainerOxygenSealer;
import team.galacticraft.galacticraft.core.inventory.ContainerParaChest;
import team.galacticraft.galacticraft.core.items.IShiftDescription;
import team.galacticraft.galacticraft.core.items.ISortable;
import team.galacticraft.galacticraft.core.tile.TileEntityOxygenSealer;
import team.galacticraft.galacticraft.core.tile.TileEntityParaChest;
import team.galacticraft.galacticraft.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockPlaceContext;
import net.minecraft.world.item.DyeColor;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockParaChest extends Block implements IShiftDescription, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    protected static final VoxelShape NOT_CONNECTED_AABB = Shapes.box(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

    public BlockParaChest(Properties builder)
    {
        super(builder);
        this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return NOT_CONNECTED_AABB;
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getPlayer().getDirection());
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

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

//    @Override
//    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
//    {
//        super.onBlockAdded(worldIn, pos, state);
//    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit)
    {
        if (worldIn.isClientSide)
        {
            return InteractionResult.SUCCESS;
        }
        else
        {
            Container iinventory = this.getInventory(worldIn, pos);

            if (iinventory != null && playerIn instanceof ServerPlayer)
            {
                NetworkHooks.openGui((ServerPlayer) playerIn, getMenuProvider(state, worldIn, pos), buf -> buf.writeBlockPos(pos));
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        }
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        TileEntityParaChest tileentitychest = (TileEntityParaChest) worldIn.getBlockEntity(pos);

        if (tileentitychest != null)
        {
            tileentitychest.clearCache();
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntityParaChest tileentitychest = (TileEntityParaChest) worldIn.getBlockEntity(pos);

        if (tileentitychest != null)
        {
            Random syncRandom = GCCoreUtil.getRandom(pos);
            for (int j1 = 0; j1 < tileentitychest.getContainerSize(); ++j1)
            {
                ItemStack itemstack = tileentitychest.getItem(j1);

                if (itemstack != null)
                {

                    float f = syncRandom.nextFloat() * 0.8F + 0.1F;
                    float f1 = syncRandom.nextFloat() * 0.8F + 0.1F;
                    ItemEntity entityitem;

                    for (float f2 = syncRandom.nextFloat() * 0.8F + 0.1F; !itemstack.isEmpty(); worldIn.addFreshEntity(entityitem))
                    {
                        entityitem = new ItemEntity(worldIn, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, itemstack.split(syncRandom.nextInt(21) + 10));
                        float f3 = 0.05F;
//                        entityitem.motionX = (float) syncRandom.nextGaussian() * f3;
//                        entityitem.motionY = (float) syncRandom.nextGaussian() * f3 + 0.2F;
//                        entityitem.motionZ = (float) syncRandom.nextGaussian() * f3;
                        entityitem.setDeltaMovement(syncRandom.nextGaussian() * f3, syncRandom.nextGaussian() * f3 + 0.2F, syncRandom.nextGaussian() * f3);
                    }
                }
            }

            worldIn.updateNeighbourForOutputSignal(pos, null);
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    public Container getInventory(Level par1World, BlockPos pos)
    {
        Object object = par1World.getBlockEntity(pos);

        if (object == null)
        {
            return null;
        }
        else if (par1World.getBlockState(pos.above()).isRedstoneConductor(par1World, pos.above()))
        {
            return null;
        }
        else if (BlockParaChest.isCatSittingAbove(par1World, pos))
        {
            return null;
        }
        else
        {
            return (Container) object;
        }
    }

    public static boolean isCatSittingAbove(Level worldIn, BlockPos pos)
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

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityParaChest();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
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
        builder.add(COLOR, FACING);
    }

//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        TileEntity tile = worldIn.getTileEntity(pos);
//        if (!(tile instanceof TileEntityParaChest))
//        {
//            return state;
//        }
//        TileEntityParaChest chest = (TileEntityParaChest) tile;
//        return state.with(COLOR, chest.color);
//    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
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
