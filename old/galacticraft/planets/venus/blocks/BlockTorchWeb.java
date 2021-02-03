package micdoodle8.mods.galacticraft.planets.venus.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

public class BlockTorchWeb extends Block implements IShearable, IShiftDescription, ISortable
{
    private static final VoxelShape AABB_WEB = Shapes.box(0.35, 0.0, 0.35, 0.65, 1.0, 0.65);
    private static final VoxelShape AABB_WEB_TORCH = Shapes.box(0.35, 0.25, 0.35, 0.65, 1.0, 0.65);

    public BlockTorchWeb(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        if (state.getBlock() == VenusBlocks.WEB_TORCH)
        {
            return AABB_WEB_TORCH;
        }

        return AABB_WEB;
    }

    @Override
    public int getLightValue(BlockState state, BlockGetter world, BlockPos pos)
    {
        if (state.getBlock() == VenusBlocks.WEB_TORCH)
        {
            return 15;
        }

        return 0;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (world.getBlockState(pos).canBeReplaced(context) && this.canBlockStay(world, pos))
        {
            return super.getStateForPlacement(context);
        }
        return world.getBlockState(pos);
    }

    private boolean canBlockStay(Level world, BlockPos pos)
    {
        BlockState blockUp = world.getBlockState(pos.above());
        return blockUp.getMaterial().isSolid() && blockUp.getBlock() != VenusBlocks.WEB_TORCH || blockUp.getBlock() == VenusBlocks.WEB_STRING;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        this.checkAndDropBlock(world, pos);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random)
    {
        this.checkAndDropBlock(world, pos);
    }

    private void checkAndDropBlock(Level world, BlockPos pos)
    {
        if (!this.canBlockStay(world, pos))
        {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public boolean isShearable(ItemStack itemStack, LevelReader world, BlockPos pos)
    {
        return true;
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack itemStack, LevelAccessor world, BlockPos pos, int fortune)
    {
        List<ItemStack> ret = Lists.newArrayList();
        ret.add(new ItemStack(this));
        return ret;
    }

    @Override
    public String getShiftDescription(ItemStack itemStack)
    {
        return GCCoreUtil.translate(this.getDescriptionId() + ".description");
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