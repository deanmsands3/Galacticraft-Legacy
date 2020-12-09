package team.galacticraft.galacticraft.common.core.blocks;

import team.galacticraft.galacticraft.common.api.block.IPartialSealableBlock;
import team.galacticraft.galacticraft.core.items.IShiftDescription;
import team.galacticraft.galacticraft.core.items.ISortable;
import team.galacticraft.galacticraft.core.tile.TileEntityEmergencyBox;
import team.galacticraft.galacticraft.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;

public class BlockEmergencyBoxKit extends BlockEmergencyBox implements IShiftDescription, IPartialSealableBlock, ISortable
{
    public BlockEmergencyBoxKit(Properties builder)
    {
        super(builder);
    }
}
