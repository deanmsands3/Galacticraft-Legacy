package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.api.item.IItemElectric;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.energy.EnergyUtil;
import team.galacticraft.galacticraft.common.core.tile.TileEntityFuelLoader;
import team.galacticraft.galacticraft.common.core.util.FluidUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.registries.ObjectHolder;

public class ContainerFuelLoader extends AbstractContainerMenu
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.FUEL_LOADER)
    public static MenuType<ContainerFuelLoader> TYPE;

    private final TileEntityFuelLoader fuelLoader;

    public ContainerFuelLoader(int containerId, Inventory playerInv, TileEntityFuelLoader fuelLoader)
    {
        super(TYPE, containerId);
        this.fuelLoader = fuelLoader;
        this.addSlot(new SlotSpecific(fuelLoader, 0, 51, 55, IItemElectric.class));
        this.addSlot(new Slot(fuelLoader, 1, 7, 12));

        int var6;
        int var7;

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlot(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 31 + 58 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlot(new Slot(playerInv, var6, 8 + var6 * 18, 31 + 116));
        }
    }

    public TileEntityFuelLoader getFuelLoader()
    {
        return fuelLoader;
    }

    @Override
    public boolean stillValid(Player var1)
    {
        return this.fuelLoader.stillValid(var1);
    }

    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par2)
    {
        ItemStack var3 = ItemStack.EMPTY;
        final Slot slot = this.slots.get(par2);

        if (slot != null && slot.hasItem())
        {
            final ItemStack var5 = slot.getItem();
            var3 = var5.copy();
            boolean movedToMachineSlot = false;

            if (par2 < 2)
            {
                if (!this.moveItemStackTo(var5, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(var5.getItem()))
                {
                    if (!this.moveItemStackTo(var5, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                    movedToMachineSlot = true;
                }
                else
                {
                    if (FluidUtil.isFuelContainerAny(var5))
                    {
                        if (!this.moveItemStackTo(var5, 1, 2, false))
                        {
                            return ItemStack.EMPTY;
                        }
                        movedToMachineSlot = true;
                    }
                    else if (par2 < 29)
                    {
                        if (!this.moveItemStackTo(var5, 29, 38, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(var5, 2, 29, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (var5.getCount() == 0)
            {
                // Needed where tile has inventoryStackLimit of 1
                if (movedToMachineSlot && var3.getCount() > 1)
                {
                    ItemStack remainder = var3.copy();
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

            if (var5.getCount() == var3.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, var5);
        }

        return var3;
    }
}
