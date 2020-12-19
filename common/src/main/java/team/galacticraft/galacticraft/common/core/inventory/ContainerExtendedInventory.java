package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.Constants;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.registries.ObjectHolder;

public class ContainerExtendedInventory extends AbstractContainerMenu
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.EXTENDED_INVENTORY)
    public static MenuType<ContainerExtendedInventory> TYPE;

    public Inventory inventoryPlayer;
    public InventoryExtended extendedInventory;

    public ContainerExtendedInventory(int containerId, Inventory playerInv, InventoryExtended extendedInventory)
    {
        super(TYPE, containerId);
        this.inventoryPlayer = playerInv;
        this.extendedInventory = extendedInventory;

        int i;
        int j;

        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }

        for (i = 0; i < 4; ++i)
        {
            this.addSlot(new SlotArmorGC(playerInv.player, playerInv, 39 - i, 61, 8 + i * 18, i));
        }

        this.addSlot(new SlotExtendedInventory(extendedInventory, 0, 125, 26));
        this.addSlot(new SlotExtendedInventory(extendedInventory, 1, 125, 44));
        this.addSlot(new SlotExtendedInventory(extendedInventory, 2, 116, 62));
        this.addSlot(new SlotExtendedInventory(extendedInventory, 3, 134, 62));
        this.addSlot(new SlotExtendedInventory(extendedInventory, 4, 143, 26));
        this.addSlot(new SlotExtendedInventory(extendedInventory, 5, 107, 26));

        for (i = 0; i < 4; ++i)
        {
            this.addSlot(new SlotExtendedInventory(extendedInventory, 6 + i, 79, 8 + i * 18));
        }

        this.addSlot(new SlotExtendedInventory(extendedInventory, 10, 125, 8));
    }

    @Override
    public boolean stillValid(Player var1)
    {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.slots.get(par1);

        if (slot != null && slot.hasItem())
        {
            final ItemStack stack = slot.getItem();
            var2 = stack.copy();

            if (par1 >= 36)
            {
                if (!this.moveItemStackTo(stack, 0, 36, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                boolean flag = false;
                for (int j = 36; j < 40; j++)
                {
                    if (this.slots.get(j).mayPlace(stack))
                    {
                        if (!this.mergeOneItem(stack, j, j + 1, false))
                        {
                            return ItemStack.EMPTY;
                        }
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
//                    if (stack.getItem() instanceof ItemOxygenTank || stack.getItem() instanceof ItemCanisterOxygenInfinite)
//                    {
//                        if (!this.mergeOneItem(stack, 42, 44, false))
//                        {
//                            return ItemStack.EMPTY;
//                        }
//                        flag = true;
//                    }
//                    else
//                    {
//                        for (int j = 40; j < 51; j++)
//                        {
//                            if (((SlotExtendedInventory) this.inventorySlots.get(j)).isItemValid(stack))
//                            {
//                                if (!this.mergeOneItem(stack, j, j + 1, false))
//                                {
//                                    return ItemStack.EMPTY;
//                                }
//                                flag = true;
//                                break;
//                            }
//                        }
//                    } TODO Oxygen container
                }

                if (!flag)
                {
                    if (par1 < 27)
                    {
                        if (!this.moveItemStackTo(stack, 27, 36, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(stack, 0, 27, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stack.getCount() == 0)
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (stack.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, stack);
        }

        return var2;
    }

    protected boolean mergeOneItem(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        boolean flag1 = false;
        if (par1ItemStack.getCount() > 0)
        {
            Slot slot;
            ItemStack slotStack;

            for (int k = par2; k < par3; k++)
            {
                slot = this.slots.get(k);
                slotStack = slot.getItem();

                if (slotStack.isEmpty())
                {
                    ItemStack stackOneItem = par1ItemStack.copy();
                    stackOneItem.setCount(1);
                    par1ItemStack.shrink(1);
                    slot.set(stackOneItem);
                    slot.setChanged();
                    flag1 = true;
                    break;
                }
            }
        }

        return flag1;
    }
}
