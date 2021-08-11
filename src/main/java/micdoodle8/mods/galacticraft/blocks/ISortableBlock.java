package micdoodle8.mods.galacticraft.blocks;

import micdoodle8.mods.galacticraft.util.EnumSortCategoryBlock;

public interface ISortableBlock
{
    EnumSortCategoryBlock getCategory(int meta);
}
