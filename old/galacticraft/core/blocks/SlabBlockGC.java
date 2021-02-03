package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.world.level.block.SlabBlock;

public class SlabBlockGC extends SlabBlock implements ISortable
{
    public SlabBlockGC(Properties properties)
    {
        super(properties);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.SLABS;
    }
}