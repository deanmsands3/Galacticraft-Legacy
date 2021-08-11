package micdoodle8.mods.galacticraft.energy.tile;

import micdoodle8.mods.galacticraft.inventory.IInventoryDefaults;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public abstract class TileBaseElectricBlockWithInventory extends TileBaseElectricBlock implements IInventoryDefaults
{
    public TileBaseElectricBlockWithInventory(String tileName)
    {
        super(tileName);
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return EnumFacing.byHorizontalIndex(((this.getBlockMetadata() & 3) + 1) % 4);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }
}
