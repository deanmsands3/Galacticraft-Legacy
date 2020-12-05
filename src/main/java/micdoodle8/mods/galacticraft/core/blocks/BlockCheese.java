package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockCheese extends Block implements IShiftDescription, ISortable
{
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 6);
    protected static final VoxelShape[] CHEESE_AABB = new VoxelShape[]{
            Shapes.box(0.0625, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            Shapes.box(0.1875, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            Shapes.box(0.3125, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            Shapes.box(0.4375, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            Shapes.box(0.5625, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            Shapes.box(0.6875, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            Shapes.box(0.8125, 0.0, 0.0625, 0.9375, 0.5, 0.9375)
    };

    public BlockCheese(Properties builder)
    {
        super(builder);
        this.registerDefaultState(stateDefinition.any().setValue(BITES, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        int bites = 0;
        if (state.getBlock() instanceof BlockCheese)
        {
            bites = state.getValue(BITES);
        }
        return CHEESE_AABB[bites];
    }

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

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit)
    {
        this.eatCheeseSlice(worldIn, pos, worldIn.getBlockState(pos), playerIn);
        return InteractionResult.SUCCESS;
    }

    private void eatCheeseSlice(Level worldIn, BlockPos pos, BlockState state, Player playerIn)
    {
        if (playerIn.canEat(false))
        {
            playerIn.getFoodData().eat(2, 0.1F);
            int i = state.getValue(BITES);

            if (i < 6)
            {
                worldIn.setBlock(pos, state.setValue(BITES, Integer.valueOf(i + 1)), 3);
            }
            else
            {
                worldIn.removeBlock(pos, false);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        if (!this.canBlockStay(worldIn, pos))
        {
            worldIn.removeBlock(pos, false);
        }
    }

    private boolean canBlockStay(Level worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.below()).getMaterial().isSolid();
    }

//    @Override
//    public int quantityDropped(Random par1Random)
//    {
//        return 0;
//    }

//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return Item.getItemFromBlock(Blocks.AIR);
//    }

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

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(BITES, Integer.valueOf(meta));
//    }
//
//    @Override
//    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state)
//    {
//        return new ItemStack(Items.CAKE, 1, 0);
//    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(BITES);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos)
    {
        return (7 - worldIn.getBlockState(pos).getValue(BITES)) * 2;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
