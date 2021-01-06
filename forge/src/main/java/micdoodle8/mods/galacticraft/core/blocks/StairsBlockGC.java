package micdoodle8.mods.galacticraft.core.blocks;

import java.util.function.Supplier;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

public class StairsBlockGC extends StairsBlock implements ISortable
{
    public StairsBlockGC(Supplier<BlockState> state, Properties properties)
    {
        super(state, properties);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.STAIRS;
    }
}