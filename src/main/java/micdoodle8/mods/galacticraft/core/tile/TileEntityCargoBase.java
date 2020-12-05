package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class TileEntityCargoBase extends TileBaseElectricBlockWithInventory
{
    public TileEntityCargoBase(BlockEntityType<?> type)
    {
        super(type);
    }
}
