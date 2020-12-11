package team.galacticraft.galacticraft.common.core.blocks;

import net.minecraft.world.level.block.EntityBlock;
import team.galacticraft.galacticraft.common.core.items.ISortable;
import team.galacticraft.galacticraft.common.core.tile.TileEntityPlayerDetector;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.common.core.util.RedstoneUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class BlockConcealedDetector extends Block implements ISortable, EntityBlock
{
    public static final IntegerProperty VARIANT = IntegerProperty.create("var", 0, 1);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty DETECTED = BooleanProperty.create("det");

    public BlockConcealedDetector(Properties builder)
    {
        super(builder);
        this.registerDefaultState(stateDefinition.any().setValue(VARIANT, 0).setValue(DETECTED, false));
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public EnumSortCategory getCategory()
    {
         return EnumSortCategory.DECORATION;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        int facing = meta & 3;
//        int var = (meta >> 2) & 1;
//        return this.getDefaultState().with(FACING, Integer.valueOf(facing)).with(VARIANT, Integer.valueOf(var)).with(DETECTED, Boolean.valueOf(meta >= 8));
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, VARIANT, DETECTED);
    }

//    @Override
//    public int getLightOpacity(BlockState state)
//    {
//        return 0;
//    }
//
//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return true;
//    }
//
//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return true;
//    }
//
//    @Override
//    public boolean isSideSolid(BlockState state, IBlockReader world, BlockPos pos, Direction side)
//    {
//        return true;
//    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter world)
    {
        return new TileEntityPlayerDetector();
    }

    @Override
    public boolean isEntityBlock()
    {
        return true;
    }

    @Override
    public boolean isSignalSource(BlockState state)
    {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side)
    {
        if (blockAccess instanceof Level && RedstoneUtil.isBlockReceivingDirectRedstone((Level) blockAccess, pos))
        {
            return 0;
        }

        return blockAccess.getBlockState(pos).getValue(DETECTED) ? 0 : 15;
    }

    public void updateState(Level worldObj, BlockPos pos, boolean result)
    {
        BlockState bs = worldObj.getBlockState(pos);
        if (result != bs.getValue(DETECTED))
        {
            worldObj.setBlock(pos, bs.setValue(DETECTED, result), 3);
        }
    }

//    @Override
//    public int quantityDropped(Random random)
//    {
//        return 0;
//    }
//
//    @Override
//    protected boolean canSilkHarvest()
//    {
//        return false;
//    }
}
