package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import micdoodle8.mods.galacticraft.planets.mars.entities.SlimelingEntity;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Collections;

public class ContainerSlimeling extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsContainerNames.SLIMELING)
    public static MenuType<ContainerSlimeling> TYPE;

    private final SlimelingEntity slimeling;

    public ContainerSlimeling(int containerId, Inventory playerInv, SlimelingEntity slimeling)
    {
        super(TYPE, containerId);
        this.slimeling = slimeling;
        this.slimeling.slimelingInventory.currentContainer = this;

        ContainerSlimeling.addSlots(this, playerInv, slimeling);
        ContainerSlimeling.addAdditionalSlots(this, slimeling, this.slimeling.slimelingInventory.getItem(1));

        this.slimeling.slimelingInventory.startOpen(playerInv.player);
    }

    public SlimelingEntity getSlimeling()
    {
        return slimeling;
    }

    public static void addSlots(ContainerSlimeling container, Inventory playerInventory, SlimelingEntity slimeling)
    {
        Slot slot = new SlotSpecific(slimeling.slimelingInventory, 1, 9, 30, new ItemStack(MarsItems.SLIMELING_INVENTORY_BAG, 1));
        container.addSlot(slot);

        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                slot = new Slot(playerInventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 129 + var3 * 18);
                container.addSlot(slot);
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            slot = new Slot(playerInventory, var3, 8 + var3 * 18, 187);
            container.addSlot(slot);
        }
    }

    public static void removeSlots(ContainerSlimeling container)
    {
        Collections.copy(container.lastSlots, container.lastSlots.subList(0, 37));
        container.slots.clear();
        container.slots.addAll(container.slots.subList(0, 37));
    }

    public static void addAdditionalSlots(ContainerSlimeling container, SlimelingEntity slimeling, ItemStack stack)
    {
        if (!stack.isEmpty() && stack.getItem() == MarsItems.SLIMELING_INVENTORY_BAG)
        {
            //Note that if NEI is installed, this can be called by InventorySlimeling.setInventorySlotContents even if the container already has the slots
            if (container.slots.size() < 63)
            {
                for (int var3 = 0; var3 < 3; ++var3)
                {
                    for (int var4 = 0; var4 < 9; ++var4)
                    {
                        Slot slot = new Slot(slimeling.slimelingInventory, var4 + var3 * 9 + 2, 8 + var4 * 18, 54 + var3 * 18);
                        slot.index = container.slots.size();
                        container.slots.add(slot);
                        container.lastSlots.add(ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    @Override
    public void removed(Player entityplayer)
    {
        this.slimeling.slimelingInventory.stopOpen(entityplayer);
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.slimeling.slimelingInventory.stillValid(par1EntityPlayer);
    }

    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.slots.get(par1);
        final int b = this.slots.size();

        if (slot != null && slot.hasItem())
        {
            final ItemStack var4 = slot.getItem();
            var2 = var4.copy();

            if (b < 39)
            {
                if (par1 < b - 36)
                {
                    if (!this.moveItemStackTo(var4, b - 36, b, true))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (var4.getItem() == MarsItems.SLIMELING_INVENTORY_BAG)
                    {
                        if (!this.moveItemStackTo(var4, 0, 1, false))
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
            }
            else
            {
                //With inventory bag, slot 0 is a bag slot
                //Slots 1-36 are regular inventory (27 inventory, 9 hotbar)
                //Slots 37-63 are the inventory bag slots
                if (par1 == 0)
                {
                    return ItemStack.EMPTY;
                }

                if (par1 > 36)
                {
                    if (!this.moveItemStackTo(var4, 1, 37, true))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (par1 < 28)
                    {
                        if (!this.moveItemStackTo(var4, 37, 64, false))
                        {
                            if (!this.moveItemStackTo(var4, 28, 37, false))
                            {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                    else if (!this.moveItemStackTo(var4, 37, 64, false))
                    {
                        if (!this.moveItemStackTo(var4, 1, 28, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (var4.isEmpty())
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
}
