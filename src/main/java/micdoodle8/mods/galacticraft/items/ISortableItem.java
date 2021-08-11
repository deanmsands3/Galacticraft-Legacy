package micdoodle8.mods.galacticraft.items;

import micdoodle8.mods.galacticraft.util.EnumSortCategoryItem;

public interface ISortableItem
{
    EnumSortCategoryItem getCategory(int meta);
}
