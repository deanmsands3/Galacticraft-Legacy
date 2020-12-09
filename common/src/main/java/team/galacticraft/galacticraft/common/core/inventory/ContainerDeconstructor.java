package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.api.item.IItemElectric;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.energy.EnergyUtil;
import team.galacticraft.galacticraft.core.tile.TileEntityDeconstructor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerDeconstructor extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.DECONSTRUCTOR)
    public static MenuType<ContainerDeconstructor> TYPE;

    private final TileEntityDeconstructor deconstructor;

//    public ContainerDeconstructor(int containerId, PlayerInventory playerInv)
//    {
//        this(containerId, playerInv, new Inventory(11));
//    }

    public ContainerDeconstructor(int containerId, Inventory playerInv, TileEntityDeconstructor deconstructor)
    {
        super(TYPE, containerId);
        this.deconstructor = deconstructor;

        // Battery Slot
        this.addSlot(new SlotSpecific(deconstructor, 0, 55, 75, IItemElectric.class));

        // Input slot
        this.addSlot(new Slot(deconstructor, 1, 26, 36));

        int count = 2;
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 3; y++)
            {
                this.addSlot(new Slot(deconstructor, count++, 112 + y * 18, 18 + x * 18));
            }
        }

        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 117 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(playerInv, var3, 8 + var3 * 18, 175));
        }
    }

    public TileEntityDeconstructor getDeconstructor()
    {
        return deconstructor;
    }

    @Override
    public void removed(Player entityplayer)
    {
        super.removed(entityplayer);
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.deconstructor.stillValid(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        Slot var3 = this.slots.get(par1);

        if (var3 != null && var3.hasItem())
        {
            ItemStack var4 = var3.getItem();
            var2 = var4.copy();

            if (par1 <= 10)
            {
                if (!this.moveItemStackTo(var4, 11, 47, true))
                {
                    return ItemStack.EMPTY;
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
                else if (par1 < 38)
                {
                    if (!this.moveItemStackTo(var4, 1, 2, false) && !this.moveItemStackTo(var4, 38, 47, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.moveItemStackTo(var4, 1, 2, false) && !this.moveItemStackTo(var4, 11, 38, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (var4.getCount() == 0)
            {
                var3.set(ItemStack.EMPTY);
            }
            else
            {
                var3.setChanged();
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            var3.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }
}
