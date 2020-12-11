package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.core.energy.tile.TileBaseElectricBlockWithInventory;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class TileEntityCargoBase extends TileBaseElectricBlockWithInventory
{
    public TileEntityCargoBase(BlockEntityType<?> type)
    {
        super(type);
    }
}
