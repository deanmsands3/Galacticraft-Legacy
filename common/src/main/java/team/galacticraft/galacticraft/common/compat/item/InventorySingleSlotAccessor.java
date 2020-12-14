package team.galacticraft.galacticraft.common.compat.item;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class InventorySingleSlotAccessor implements SingleSlotAccessor {
    private final Inventory inventory;
    private final int slot;

    public InventorySingleSlotAccessor(Inventory inventory, int slot) {
        this.inventory = inventory;
        this.slot = slot;
    }
    @Override
    public void set(ItemStack stack) {
        this.inventory.setItem(slot, stack);
    }

    @Override
    public ItemStack get() {
        return this.inventory.getItem(slot).copy();
    }
}
