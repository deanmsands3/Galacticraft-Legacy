package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenCompressor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerOxygenCompressor extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.OXYGEN_COMPRESSOR)
    public static MenuType<ContainerOxygenCompressor> TYPE;

    private final TileEntityOxygenCompressor compressor;

    public ContainerOxygenCompressor(int containerId, Inventory playerInv, TileEntityOxygenCompressor compressor)
    {
        super(TYPE, containerId);
        this.compressor = compressor;
        this.addSlot(new Slot(compressor, 0, 133, 71));
        this.addSlot(new SlotSpecific(compressor, 1, 47, 27, IItemElectric.class));
        this.addSlot(new SlotSpecific(compressor, 2, 17, 27, IItemOxygenSupply.class));

        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 20 + 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(playerInv, var3, 8 + var3 * 18, 20 + 142));
        }

        compressor.startOpen(playerInv.player);
    }

    public TileEntityOxygenCompressor getCompressor()
    {
        return compressor;
    }

    @Override
    public boolean stillValid(Player var1)
    {
        return this.compressor.stillValid(var1);
    }

    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.slots.get(par1);
        final int b = this.slots.size();

        if (slot != null && slot.hasItem())
        {
            final ItemStack stack = slot.getItem();
            var2 = stack.copy();
            boolean movedToMachineSlot = false;

            if (par1 < 3)
            {
                if (!this.moveItemStackTo(stack, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(stack.getItem()))
                {
                    if (!this.moveItemStackTo(stack, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                    movedToMachineSlot = true;
                }
                else if (stack.getItem() instanceof IItemOxygenSupply)
                {
                    if (!this.moveItemStackTo(stack, 2, 3, false))
                    {
                        return ItemStack.EMPTY;
                    }
                    movedToMachineSlot = true;
                }
                else if (stack.getItem() instanceof ItemOxygenTank && stack.getDamageValue() > 0)
                {
                    if (!this.moveItemStackTo(stack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                    movedToMachineSlot = true;
                }
                else
                {
                    if (par1 < b - 9)
                    {
                        if (!this.moveItemStackTo(stack, b - 9, b, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(stack, b - 36, b - 9, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stack.getCount() == 0)
            {
                // Needed where tile has inventoryStackLimit of 1
                if (movedToMachineSlot && var2.getCount() > 1)
                {
                    ItemStack remainder = var2.copy();
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

            if (stack.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, stack);
        }

        return var2;
    }
}
