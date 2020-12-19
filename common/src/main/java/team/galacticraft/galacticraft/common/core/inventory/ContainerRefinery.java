package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.api.item.IItemElectric;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCItems;
import team.galacticraft.galacticraft.common.core.energy.EnergyUtil;
import team.galacticraft.galacticraft.common.core.tile.TileEntityRefinery;
import team.galacticraft.galacticraft.common.core.util.FluidUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.registries.ObjectHolder;

public class ContainerRefinery extends AbstractContainerMenu
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.REFINERY)
    public static MenuType<ContainerRefinery> TYPE;

    private final TileEntityRefinery refinery;

    public ContainerRefinery(int containerId, Inventory playerInv, TileEntityRefinery refinery)
    {
        super(TYPE, containerId);
        this.refinery = refinery;

        // Electric Input Slot
        this.addSlot(new SlotSpecific(refinery, 0, 38, 51, IItemElectric.class));

        // To be smelted
        this.addSlot(new Slot(refinery, 1, 7, 7));

        // Smelting result
        this.addSlot(new Slot(refinery, 2, 153, 7));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 104 + var3 * 18 - 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(playerInv, var3, 8 + var3 * 18, 144));
        }

        refinery.startOpen(playerInv.player);
    }

    public TileEntityRefinery getRefinery()
    {
        return refinery;
    }

    @Override
    public void removed(Player entityplayer)
    {
        super.removed(entityplayer);
        this.refinery.stopOpen(entityplayer);
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.refinery.stillValid(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.slots.get(par1);

        if (slot != null && slot.hasItem())
        {
            final ItemStack var4 = slot.getItem();
            var2 = var4.copy();

            if (par1 < 3)
            {
                if (!this.moveItemStackTo(var4, 3, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                if (par1 == 2)
                {
                    slot.onQuickCraft(var4, var2);
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(var4.getItem()))
                {
                    if (!this.moveItemStackTo(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (FluidUtil.isOilContainerAny(var4))
                    {
                        if (!this.moveItemStackTo(var4, 1, 2, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (FluidUtil.isPartialContainer(var4, GCItems.fuelCanister))
                    {
                        if (!this.moveItemStackTo(var4, 2, 3, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (par1 < 30)
                    {
                        if (!this.moveItemStackTo(var4, 30, 39, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(var4, 3, 30, false))
                    {
                        return ItemStack.EMPTY;
                    }
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
}
