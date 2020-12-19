package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.api.item.IItemElectric;
import team.galacticraft.galacticraft.common.api.recipe.CircuitFabricatorRecipes;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.energy.EnergyUtil;
import team.galacticraft.galacticraft.common.core.tile.TileEntityCircuitFabricator;
import team.galacticraft.galacticraft.common.core.util.GCLog;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
//import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContainerCircuitFabricator extends AbstractContainerMenu
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.CIRCUIT_FABRICATOR)
    public static MenuType<ContainerCircuitFabricator> TYPE;

    private final TileEntityCircuitFabricator fabricator;

    public ContainerCircuitFabricator(int containerId, Inventory playerInv, TileEntityCircuitFabricator fabricator)
    {
        super(TYPE, containerId);
        this.fabricator = fabricator;

        // Energy slot
        this.addSlot(new SlotSpecific(fabricator, 0, 6, 69, IItemElectric.class));

        // Diamond
        ArrayList<ItemStack> slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(0);
        this.addSlot(new SlotSpecific(fabricator, 1, 15, 17, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));

        // Silicon
        slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(1);
        this.addSlot(new SlotSpecific(fabricator, 2, 74, 46, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));
        slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(2);
        this.addSlot(new SlotSpecific(fabricator, 3, 74, 64, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));

        // Redstone
        slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(3);
        this.addSlot(new SlotSpecific(fabricator, 4, 122, 46, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));

        // Optional
        slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(4);
        this.addSlot(new SlotSpecific(fabricator, 5, 145, 20, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));

        // Smelting result
        this.addSlot(new FurnaceResultSlot(playerInv.player, fabricator, 6, 152, 86));

        int slot;

        for (slot = 0; slot < 3; ++slot)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(playerInv, var4 + slot * 9 + 9, 8 + var4 * 18, 110 + slot * 18));
            }
        }

        for (slot = 0; slot < 9; ++slot)
        {
            this.addSlot(new Slot(playerInv, slot, 8 + slot * 18, 168));
        }
    }

    public TileEntityCircuitFabricator getFabricator()
    {
        return fabricator;
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.fabricator.stillValid(par1EntityPlayer);
    }

    @Override
    public void slotsChanged(Container par1IInventory)
    {
        super.slotsChanged(par1IInventory);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        Slot slot = this.slots.get(par1);
        final int b = this.slots.size();

        if (slot != null && slot.hasItem())
        {
            ItemStack var4 = slot.getItem();
            var2 = var4.copy();

            if (par1 < b - 36)
            {
                if (!this.moveItemStackTo(var4, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }

                if (par1 == 6)
                {
                    slot.onQuickCraft(var4, var2);
                }
            }
            else
            {
                Item i = var4.getItem();
                if (EnergyUtil.isElectricItem(i))
                {
                    if (!this.moveItemStackTo(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (i == Items.DIAMOND)
                {
                    if (!this.moveItemStackTo(var4, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (this.isSilicon(var4))
                {
                    if (!this.mergeEven(var4, 2, 4))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (i == Items.REDSTONE)
                {
                    if (!this.moveItemStackTo(var4, 4, 5, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (i == Items.REPEATER || i == new ItemStack(Blocks.REDSTONE_TORCH).getItem() || i == Items.BLUE_DYE)
                {
                    if (!this.moveItemStackTo(var4, 5, 6, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 < b - 9)
                {
                    if (!this.moveItemStackTo(var4, b - 9, b, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.moveItemStackTo(var4, b - 36, b - 9, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (var4.getCount() == 0)
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }

    private boolean mergeEven(ItemStack stack, int a, int b)
    {
        List<Slot> acceptSlots = new LinkedList<>();
        List<Integer> acceptQuantity = new LinkedList<>();
        int minQuantity = 64;
        int acceptTotal = 0;
        for (int i = a; i < b; i++)
        {
            Slot slot = this.slots.get(i);

            if (slot != null)
            {
                ItemStack target = slot.getItem();
                if (matchingStacks(stack, target))
                {
                    acceptSlots.add(slot);
                    int availSpace = stack.getMaxStackSize() - target.getCount();
                    acceptQuantity.add(availSpace);
                    acceptTotal += availSpace;
                    if (availSpace < minQuantity)
                    {
                        minQuantity = availSpace;
                    }
                }
            }
        }

        for (Slot slot : acceptSlots)
        {
            ItemStack target = slot.getItem();
            if (target.isEmpty())
            {
                target = stack.copy();
                target.setCount(1);
                slot.set(target);
                stack.shrink(1);
                if (stack.isEmpty())
                {
                    return false;
                }
            }
        }

        //The stack more than exceeds what the crafting inventory requires
        if (stack.getCount() >= acceptTotal)
        {
            if (acceptTotal == 0)
            {
                return false;
            }

            for (Slot slot : acceptSlots)
            {
                ItemStack target = slot.getItem();
                stack.shrink(target.getMaxStackSize() - target.getCount());
                target.setCount(target.getMaxStackSize());
                slot.setChanged();
            }
            return true;
        }

        int uneven = 0;
        for (int q : acceptQuantity)
        {
            uneven += q - minQuantity;
        }

        //Use the whole stack to try to even up the neediest slots
        if (stack.getCount() <= uneven)
        {
            do
            {
                Slot neediest = null;
                int smallestStack = 64;
                for (Slot slot : acceptSlots)
                {
                    ItemStack target = slot.getItem();
                    if (target.getCount() < smallestStack)
                    {
                        smallestStack = target.getCount();
                        neediest = slot;
                    }
                }
                neediest.getItem().grow(1);
                stack.shrink(1);
            }
            while (!stack.isEmpty());
            for (Slot slot : acceptSlots)
            {
                slot.setChanged();
            }
            return true;
        }

        //Use some of the stack to even things up
        if (uneven > 0)
        {
            int targetSize = stack.getMaxStackSize() - minQuantity;
            for (Slot slot : acceptSlots)
            {
                ItemStack target = slot.getItem();
                stack.shrink(targetSize - target.getCount());
                acceptTotal -= targetSize - target.getCount();
                target.setCount(targetSize);
                slot.setChanged();
            }
        }

        //Spread the remaining stack over all slots evenly
        int average = stack.getCount() / acceptSlots.size();
        int modulus = stack.getCount() - average * acceptSlots.size();
        for (Slot slot : acceptSlots)
        {
            if (slot != null)
            {
                ItemStack target = slot.getItem();
                int transfer = average;
                if (modulus > 0)
                {
                    transfer++;
                    modulus--;
                }
                if (transfer > stack.getCount())
                {
                    transfer = stack.getCount();
                }
                stack.shrink(transfer);
                target.grow(transfer);
                if (target.getCount() > target.getMaxStackSize())
                {
                    GCLog.info("Shift clicking - slot " + slot.index + " wanted more than it could accept:" + target.getCount());
                    stack.grow(target.getCount() - target.getMaxStackSize());
                    target.setCount(target.getMaxStackSize());
                }
                slot.setChanged();
                if (stack.isEmpty())
                {
                    break;
                }
            }
        }

        return true;
    }

    private boolean isSilicon(ItemStack test)
    {
        for (ItemStack stack : CircuitFabricatorRecipes.slotValidItems.get(1))
        {
            if (stack.sameItem(test))
            {
                return true;
            }
        }
        return false;
    }

    private boolean matchingStacks(ItemStack stack, ItemStack target)
    {
        return target.isEmpty() || target.getItem() == stack.getItem() /*&& (!stack.getHasSubtypes() || stack.getMetadata() == target.getMetadata())*/ && ItemStack.tagMatches(stack, target) && (target.isStackable() && target.getCount() < target.getMaxStackSize());
    }
}
