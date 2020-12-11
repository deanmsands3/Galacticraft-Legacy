package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.api.item.IItemElectric;
import team.galacticraft.galacticraft.common.api.item.IItemOxygenSupply;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.energy.EnergyUtil;
import team.galacticraft.galacticraft.common.core.energy.tile.TileBaseElectricBlock;
import team.galacticraft.galacticraft.common.core.tile.TileEntityOxygenDistributor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerOxygenDistributor extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.OXYGEN_DISTRIBUTOR)
    public static MenuType<ContainerOxygenDistributor> TYPE;

    private final TileEntityOxygenDistributor distributor;

    public ContainerOxygenDistributor(int containerId, Inventory playerInv, TileEntityOxygenDistributor distributor)
    {
        super(TYPE, containerId);
        this.distributor = distributor;
        this.addSlot(new SlotSpecific(distributor, 0, 47, 27, IItemElectric.class));
        this.addSlot(new SlotSpecific(distributor, 1, 17, 27, IItemOxygenSupply.class));

        int var6;
        int var7;

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlot(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 46 + 58 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlot(new Slot(playerInv, var6, 8 + var6 * 18, 46 + 116));
        }
    }

    public TileEntityOxygenDistributor getDistributor()
    {
        return distributor;
    }

    @Override
    public boolean stillValid(Player var1)
    {
        return this.distributor.stillValid(var1);
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

            if (par1 < 2)
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
                else if (stack.getItem() instanceof IItemOxygenSupply)
                {
                    if (!this.moveItemStackTo(stack, 1, 2, false))
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
}
