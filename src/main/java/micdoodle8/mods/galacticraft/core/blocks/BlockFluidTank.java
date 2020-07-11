//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTank;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
//import micdoodle8.mods.galacticraft.core.util.FluidUtil;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.ITileEntityProvider;
//import net.minecraft.block.state.BlockFaceShape;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.BlockRenderLayer;
//import net.minecraft.util.Direction;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//import net.minecraftforge.fluids.FluidActionResult;
//import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class BlockFluidTank extends Block implements IShiftDescription, ISortableBlock, ITileEntityProvider
//{
//    public static final BooleanProperty UP = BooleanProperty.create("up");
//    public static final BooleanProperty DOWN = BooleanProperty.create("down");
//    private static final VoxelShape BOUNDS = Block.makeCuboidShape(0.05F, 0.0F, 0.05F, 0.95F, 1.0F, 0.95F);
//
//    public BlockFluidTank(Properties builder)
//    {
//        super(builder);
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
//    {
//        return BOUNDS;
//    }
//
//    @Override
//    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
//    {
//        TileEntity tile = worldIn.getTileEntity(pos);
//        if (tile instanceof TileEntityFluidTank)
//        {
//            TileEntityFluidTank tank = (TileEntityFluidTank) tile;
//            tank.onBreak();
//        }
//        super.onReplaced(state, worldIn, pos, newState, isMoving);
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }
//
//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }
//
//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.GENERAL;
//    }
//
//    @Override
//    public String getShiftDescription(ItemStack stack)
//    {
//        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
//    }
//
//    @Override
//    public boolean showDescription(ItemStack stack)
//    {
//        return true;
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(UP, DOWN);
//    }
//
//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        BlockState stateAbove = worldIn.getBlockState(pos.up());
//        BlockState stateBelow = worldIn.getBlockState(pos.down());
//        return state.with(UP, stateAbove.getBlock() == this).with(DOWN, stateBelow.getBlock() == this);
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState();
//    }
//
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world)
//    {
//        return new TileEntityFluidTank();
//    }
//
////    @Override
////    public void setBlockBoundsBasedOnState(IBlockReader worldIn, BlockPos pos)
////    {
////        this.setBlockBounds((float) BOUNDS.minX, (float) BOUNDS.minY, (float) BOUNDS.minZ, (float) BOUNDS.maxX, (float) BOUNDS.maxY, (float) BOUNDS.maxZ);
////    }
////
////    @Override
////    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
////    {
////        this.setBlockBoundsBasedOnState(worldIn, pos);
////        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
////    }
////
////    @Override
////    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockReader worldIn, BlockPos pos)
////    {
////        this.setBlockBoundsBasedOnState(worldIn, pos);
////        return super.getCollisionBoundingBox(worldIn, pos, state);
////    }
////
////    @Override
////    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
////    {
////        this.setBlockBoundsBasedOnState(worldIn, pos);
////        return super.getSelectedBoundingBox(worldIn, pos);
////    }
//
//    @Override
//    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
//    {
//        if (super.onBlockActivated(worldIn, pos, state, playerIn, hand, LogicalSide, hitX, hitY, hitZ))
//        {
//            return true;
//        }
//
//        if (hand == Hand.OFF_HAND)
//        {
//        	return false;
//        }
//
//        ItemStack current = playerIn.inventory.getCurrentItem();
//        int slot = playerIn.inventory.currentItem;
//
//        if (!current.isEmpty())
//        {
//            TileEntity tile = worldIn.getTileEntity(pos);
//
//            if (tile instanceof TileEntityFluidTank)
//            {
//                TileEntityFluidTank tank = (TileEntityFluidTank) tile;
//
//                FluidActionResult forgeResult = FluidUtil.interactWithFluidHandler(current, tank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null), playerIn);
//                if (forgeResult.isSuccess())
//                {
//                	playerIn.inventory.setInventorySlotContents(slot, forgeResult.result);
//            		if (playerIn.inventoryContainer != null)
//            		{
//            			playerIn.inventoryContainer.detectAndSendChanges();
//            		}
//            		return true;
//                }
//
//                return false;
//            }
//        }
//
//        return false;
//    }
//
//    @Override
//    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
//    {
//        TileEntity tile = world.getTileEntity(pos);
//
//        if (tile instanceof TileEntityFluidTank)
//        {
//            TileEntityFluidTank tank = (TileEntityFluidTank) tile;
//            return tank.fluidTank.getFluid() == FluidStack.EMPTY || tank.fluidTank.getFluid().getAmount() == 0 ? 0 : tank.fluidTank.getFluid().getFluid().getLuminosity(tank.fluidTank.getFluid());
//        }
//
//        return 0;
//    }
//}
