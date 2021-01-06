package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.block.WallBlock;

public class WallBlockGC extends WallBlock implements ISortable
{
    public WallBlockGC(Properties properties)
    {
        super(properties);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.WALLS;
    }
}