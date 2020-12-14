package team.galacticraft.galacticraft.fabric.core.item;

import alexiil.mc.lib.attributes.AttributeProviderItem;
import alexiil.mc.lib.attributes.ItemAttributeList;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.misc.LimitedConsumer;
import alexiil.mc.lib.attributes.misc.Reference;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;
import team.galacticraft.galacticraft.common.compat.item.SingleSlotAccessor;
import team.galacticraft.galacticraft.common.core.items.ItemCanisterGeneric;
import team.galacticraft.galacticraft.fabric.compat.fluid.FluidTankImpl;
import team.galacticraft.galacticraft.fabric.compat.fluid.FluidTankWrapper;
import team.galacticraft.galacticraft.fabric.compat.fluid.FluidUtilFabric;

public class ItemCanisterGenericFabric extends Item implements ItemCanisterGeneric, AttributeProviderItem {
    public ItemCanisterGenericFabric(Properties properties) {
        super(properties);

    }

    @Override
    public void addAllAttributes(Reference<ItemStack> stack, LimitedConsumer<ItemStack> excess, ItemAttributeList<?> to) { //todo(marcus): check if this is the correct way to do this
        SimpleFixedFluidInv inv = new SimpleFixedFluidInv(1, FluidUtilFabric.toAmountLBA(ItemCanisterGeneric.SIZE));
        inv.fromTag(stack.get().getTag());
        inv.addListener((inv1, tank, previous, current) -> {
            ItemStack stack1 = stack.get().copy();
            stack1.setDamageValue(SIZE.minus(FluidUtilFabric.toFractionA(current.getAmount_F())).intValue());
            if (!stack.set(stack1)) PlatformSpecific.getLogger().debug("Failed to update canister damage!");
        }, () -> {
            throw new RuntimeException("This listener shouldn't be removed!"); //todo(marcus):
        });
        to.offer(inv);
    }

    @Override
    public FluidTank getTank(SingleSlotAccessor accessor) {
        return new FluidTankWrapper((SimpleFixedFluidInv) FluidAttributes.FIXED_INV.get(new Reference<ItemStack>() {
            @Override
            public ItemStack get() {
                return accessor.get();
            }

            @Override
            public boolean set(ItemStack value) {
                accessor.set(value);
                return accessor.get() == value;
            }

            @Override
            public boolean isValid(ItemStack value) {
                return true;
            }
        }));
    }
}
