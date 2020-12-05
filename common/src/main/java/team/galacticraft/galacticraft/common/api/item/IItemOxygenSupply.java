package team.galacticraft.galacticraft.common.api.item;

import net.minecraft.world.item.ItemStack;

public interface IItemOxygenSupply
{
    /*
     * Returns the amount of gas that this oxygen item is able to supply
     */
    int discharge(ItemStack itemStack, int amount);

    int getOxygenStored(ItemStack theItem);
}
