package team.galacticraft.galacticraft.fabric.compat.item;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.item.filter.ItemFilter;
import alexiil.mc.lib.attributes.item.impl.FullFixedItemInv;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import team.galacticraft.galacticraft.common.compat.item.ItemInventory;

import java.util.List;
import java.util.function.Predicate;

public class FabricItemInventory extends FullFixedItemInv implements ItemInventory {
    private final List<Predicate<ItemStack>> filters;

    public FabricItemInventory(int invSize, List<Predicate<ItemStack>> filters) {
        super(invSize);
        this.filters = filters;
    }

    @Override
    public int size() {
        return getSlotCount();
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {
        return getPredicateForSlot(slot).test(item);
    }

    @Override
    public Predicate<ItemStack> getPredicateForSlot(int slot) {
        return filters.get(slot);
    }

    @Override
    public ItemFilter getFilterForSlot(int slot) {
        return itemStack -> getPredicateForSlot(slot).test(itemStack);
    }

    @Override
    public boolean setStack(int slot, ItemStack to, ActionType simulation) {
        return setInvStack(slot, to, simulation == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        return super.toTag(tag);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
    }

    @Override
    public ItemStack extract(Predicate<ItemStack> filter, int maxAmount, ActionType actionType) {
        return super.attemptExtraction(filter::test, maxAmount, actionType == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION);
    }

    @Override
    public ItemStack extract(ItemStack filter, int maxAmount, ActionType actionType) {
        return super.attemptExtraction((itemStack -> itemStack.equals(filter)), maxAmount, actionType == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION);
    }

    @Override
    public ItemStack extract(int maxAmount, ActionType actionType) {
        return super.attemptAnyExtraction(maxAmount, actionType == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION);
    }

    @Override
    public ItemStack insert(ItemStack stack, ActionType actionType) {
        return super.attemptInsertion(stack, actionType == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION);
    }
}
