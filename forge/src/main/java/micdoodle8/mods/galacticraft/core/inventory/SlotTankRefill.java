package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotTankRefill extends Slot
{
    public SlotTankRefill(Container par3IInventory, int par4, int par5, int par6)
    {
        super(par3IInventory, par4, par5, par6);
    }

    @Override
    public boolean mayPlace(ItemStack par1ItemStack)
    {
        if (this.index == 49)
        {
            return par1ItemStack.getItem() instanceof ItemParaChute;
        }

        return OxygenUtil.isItemValidForPlayerTankInv(this.index - 45, par1ItemStack);
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }
}
