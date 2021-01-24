package micdoodle8.mods.galacticraft.core.blocks;

import java.util.function.Supplier;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class StairsBlockGC extends StairBlock implements ISortable
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