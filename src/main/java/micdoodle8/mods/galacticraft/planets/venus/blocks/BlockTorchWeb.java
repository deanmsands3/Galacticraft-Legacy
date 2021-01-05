package micdoodle8.mods.galacticraft.planets.venus.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

public class BlockTorchWeb extends Block implements IShearable, IShiftDescription, ISortable
{
    private static final VoxelShape AABB_WEB = VoxelShapes.create(0.35, 0.0, 0.35, 0.65, 1.0, 0.65);
    private static final VoxelShape AABB_WEB_TORCH = VoxelShapes.create(0.35, 0.25, 0.35, 0.65, 1.0, 0.65);

    public BlockTorchWeb(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        if (state.getBlock() == VenusBlocks.WEB_TORCH)
        {
            return AABB_WEB_TORCH;
        }

        return AABB_WEB;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        if (state.getBlock() == VenusBlocks.WEB_TORCH)
        {
            return 15;
        }

        return 0;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        if (world.getBlockState(pos).isReplaceable(context) && this.canBlockStay(world, pos))
        {
            return super.getStateForPlacement(context);
        }
        return world.getBlockState(pos);
    }

    private boolean canBlockStay(World world, BlockPos pos)
    {
        BlockState blockUp = world.getBlockState(pos.up());
        return blockUp.getMaterial().isSolid() && blockUp.getBlock() != VenusBlocks.WEB_TORCH || blockUp.getBlock() == VenusBlocks.WEB_STRING;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        this.checkAndDropBlock(world, pos);
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        this.checkAndDropBlock(world, pos);
    }

    private void checkAndDropBlock(World world, BlockPos pos)
    {
        if (!this.canBlockStay(world, pos))
        {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public boolean isShearable(ItemStack itemStack, IWorldReader world, BlockPos pos)
    {
        return true;
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack itemStack, IWorld world, BlockPos pos, int fortune)
    {
        List<ItemStack> ret = Lists.newArrayList();
        ret.add(new ItemStack(this));
        return ret;
    }

    @Override
    public String getShiftDescription(ItemStack itemStack)
    {
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack itemStack)
    {
        return itemStack.getItem() == VenusBlocks.WEB_TORCH.asItem();
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}