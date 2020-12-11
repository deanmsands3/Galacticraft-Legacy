package team.galacticraft.galacticraft.common.core.blocks;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import team.galacticraft.galacticraft.common.core.items.IShiftDescription;
import team.galacticraft.galacticraft.common.core.items.ISortable;
import team.galacticraft.galacticraft.common.core.tile.TileEntityAirLock;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;

public class BlockAirLockFrame extends BlockAdvancedTile implements IShiftDescription, ISortable
{
    public BlockAirLockFrame(Properties builder)
    {
        super(builder);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter world)
    {
        return new TileEntityAirLock();
    }

    @Override
    public boolean isEntityBlock()
    {
        return true;
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public String getShiftDescription(ItemStack item)
    {
        return I18n.get(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack item)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }

//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(AIR_LOCK_TYPE);
//    }
}
