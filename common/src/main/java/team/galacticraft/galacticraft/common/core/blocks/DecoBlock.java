package team.galacticraft.galacticraft.common.core.blocks;

import team.galacticraft.galacticraft.common.core.items.ISortable;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import net.minecraft.world.level.block.Block;

public class DecoBlock extends Block implements ISortable
{
    public DecoBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.DECORATION;
    }
}
