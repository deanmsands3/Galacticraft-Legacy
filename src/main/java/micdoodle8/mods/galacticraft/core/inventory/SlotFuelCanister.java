package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.ItemFuelCanister;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFuelCanister extends Slot
{

    public SlotFuelCanister(IInventory par1iInventory, int par2, int par3, int par4)
    {
        super(par1iInventory, par2, par3, par4);
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return par1ItemStack.getItem() instanceof ItemFuelCanister && par1ItemStack.getItemDamage() > 0;

    }
}
