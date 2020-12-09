package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.api.item.IItemElectric;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.energy.EnergyUtil;
import team.galacticraft.galacticraft.core.tile.TileEntityEnergyStorageModule;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerEnergyStorageModule extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.ENERGY_STORAGE_MODULE)
    public static MenuType<ContainerEnergyStorageModule> TYPE;

    private final TileEntityEnergyStorageModule storageModule;

    public ContainerEnergyStorageModule(int containerId, Inventory playerInv, TileEntityEnergyStorageModule storageModule)
    {
        super(TYPE, containerId);
        this.storageModule = storageModule;
        // Top slot for battery output
        this.addSlot(new SlotSpecific(storageModule, 0, 33, 24, IItemElectric.class));
        // Bottom slot for batter input
        this.addSlot(new SlotSpecific(storageModule, 1, 33, 48, IItemElectric.class));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(playerInv, var3, 8 + var3 * 18, 142));
        }

        this.storageModule.playersUsing.add(playerInv.player);
    }

    public TileEntityEnergyStorageModule getStorageModule()
    {
        return storageModule;
    }

    @Override
    public void removed(Player entityplayer)
    {
        super.removed(entityplayer);
        this.storageModule.playersUsing.remove(entityplayer);
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.storageModule.stillValid(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int slotID)
    {
        ItemStack returnStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotID);
        final int b = this.slots.size();

        if (slot != null && slot.hasItem())
        {
            ItemStack itemStack = slot.getItem();
            returnStack = itemStack.copy();
            boolean movedToMachineSlot = false;

            if (slotID != 0 && slotID != 1)
            {
                if (EnergyUtil.isElectricItem(itemStack.getItem()))
                {
                    if (EnergyUtil.isChargedElectricItem(itemStack))
                    {
                        if (!this.moveItemStackTo(itemStack, 1, 2, false))
                        {
                            if (EnergyUtil.isFillableElectricItem(itemStack) && !this.moveItemStackTo(itemStack, 0, 1, false))
                            {
                                return ItemStack.EMPTY;
                            }
                        }
                        movedToMachineSlot = true;
                    }
                    else
                    {
                        if (!this.moveItemStackTo(itemStack, 0, 1, false))
                        {
                            return ItemStack.EMPTY;
                        }
                        movedToMachineSlot = true;
                    }
                }
                else
                {
                    if (slotID < b - 9)
                    {
                        if (!this.moveItemStackTo(itemStack, b - 9, b, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(itemStack, b - 36, b - 9, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if (!this.moveItemStackTo(itemStack, 2, 38, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemStack.getCount() == 0)
            {
                // Needed where tile has inventoryStackLimit of 1
                if (movedToMachineSlot && returnStack.getCount() > 1)
                {
                    ItemStack remainder = returnStack.copy();
                    remainder.shrink(1);
                    slot.set(remainder);
                }
                else
                {
                    slot.set(ItemStack.EMPTY);
                }
            }
            else
            {
                slot.setChanged();
            }

            if (itemStack.getCount() == returnStack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, itemStack);
        }

        return returnStack;
    }
}
