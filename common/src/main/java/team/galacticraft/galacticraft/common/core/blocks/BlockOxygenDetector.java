package team.galacticraft.galacticraft.common.core.blocks;

import team.galacticraft.galacticraft.core.items.IShiftDescription;
import team.galacticraft.galacticraft.core.items.ISortable;
import team.galacticraft.galacticraft.core.tile.TileEntityOxygenDetector;
import team.galacticraft.galacticraft.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import javax.annotation.Nullable;

public class BlockOxygenDetector extends Block implements IShiftDescription, ISortable
{
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockOxygenDetector(Properties builder)
    {
        super(builder);
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityOxygenDetector();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

//    public void updateOxygenState(World worldIn, BlockPos pos, boolean valid)
//    {
//        if (valid)
//        {
//            worldIn.setBlockState(pos, getStateFromMeta(1), 3);
//        }
//        else
//        {
//            worldIn.setBlockState(pos, getStateFromMeta(0), 3);
//        }
//    }

    @Override
    public boolean isSignalSource(BlockState state)
    {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side)
    {
        return blockState.getValue(ACTIVE) ? 15 : 0;
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(ACTIVE, meta > 0);
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(ACTIVE);
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
//    public boolean isSideSolid(BlockState base_state, IBlockReader world, BlockPos pos, Direction side)
//    {
//        return true;
//    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
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
