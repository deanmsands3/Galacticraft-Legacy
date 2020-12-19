package team.galacticraft.galacticraft.common.compat.item;

import net.minecraft.world.item.ItemStack;

public interface SingleSlotAccessor
{
    void set(ItemStack stack);

    /**
     * DO NOT MODIFY THIS STACK
     */
    ItemStack get();
}
