package micdoodle8.mods.galacticraft.planets.venus.inventory;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.inventory.ContainerBuggy;
import micdoodle8.mods.galacticraft.core.inventory.GCContainerNames;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityCrashedProbe;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerCrashedProbe extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + VenusContainerNames.CRASHED_PROBE)
    public static MenuType<ContainerCrashedProbe> TYPE;

    private final TileEntityCrashedProbe probe;

    public ContainerCrashedProbe(int containerId, Inventory playerInv, TileEntityCrashedProbe probe)
    {
        super(TYPE, containerId);
        this.probe = probe;

        for (int i = 0; i < 6; ++i)
        {
            this.addSlot(new Slot(probe, i, 35 + i * 18, 18));
        }

        int var6;
        int var7;

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlot(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 52 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlot(new Slot(playerInv, var6, 8 + var6 * 18, 110));
        }
    }

    public TileEntityCrashedProbe getProbe()
    {
        return probe;
    }

    @Override
    public boolean stillValid(Player var1)
    {
        return this.probe.stillValid(var1);
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

            if (par1 == 0)
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
                    if (!this.moveItemStackTo(stack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
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

            if (stack.isEmpty())
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
}
