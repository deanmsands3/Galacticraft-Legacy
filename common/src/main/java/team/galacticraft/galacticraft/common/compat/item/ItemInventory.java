package team.galacticraft.galacticraft.common.compat.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;

import java.util.function.Predicate;

public interface ItemInventory
{

    int size();

    boolean isItemValidForSlot(int slot, ItemStack item);

    Predicate<ItemStack> getPredicateForSlot(int slot);

    boolean setStack(int slot, ItemStack to, ActionType simulation);

    CompoundTag toTag(CompoundTag tag);

    void fromTag(CompoundTag tag);

    ItemStack extract(Predicate<ItemStack> filter, int maxAmount, ActionType simulation);

    ItemStack extract(ItemStack filter, int maxAmount, ActionType simulation);

    ItemStack extract(int maxAmount, ActionType simulation);

    ItemStack insert(ItemStack stack, ActionType simulation);

    SingleSlotAccessor getSingleSlot(int slot);
}
