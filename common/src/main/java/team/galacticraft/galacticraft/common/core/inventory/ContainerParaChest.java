package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.tile.TileEntityParaChest;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerParaChest extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.PARACHEST)
    public static MenuType<ContainerParaChest> TYPE;

    private final TileEntityParaChest paraChest;
    public int numRows;

    public ContainerParaChest(int containerId, Inventory playerInv, TileEntityParaChest paraChest)
    {
        super(TYPE, containerId);
        this.paraChest = paraChest;
        this.numRows = (paraChest.getContainerSize() - 3) / 9;
        paraChest.startOpen(playerInv.player);
        int i = (this.numRows - 4) * 18 + 19;
        int j;
        int k;

        for (j = 0; j < this.numRows; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlot(new Slot(paraChest, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        this.addSlot(new Slot(paraChest, paraChest.getContainerSize() - 3, 125 + 0 * 18, (this.numRows == 0 ? 24 : 26) + this.numRows * 18));
        this.addSlot(new Slot(paraChest, paraChest.getContainerSize() - 2, 125 + 1 * 18, (this.numRows == 0 ? 24 : 26) + this.numRows * 18));
        this.addSlot(new Slot(paraChest, paraChest.getContainerSize() - 1, 75, (this.numRows == 0 ? 24 : 26) + this.numRows * 18));

        for (j = 0; j < 3; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlot(new Slot(playerInv, k + j * 9 + 9, 8 + k * 18, (this.numRows == 0 ? 116 : 118) + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j)
        {
            this.addSlot(new Slot(playerInv, j, 8 + j * 18, (this.numRows == 0 ? 174 : 176) + i));
        }
    }

    public TileEntityParaChest getParaChest()
    {
        return paraChest;
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.paraChest.stillValid(par1EntityPlayer);
    }

    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par2)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(par2);
        final int b = this.slots.size();

        if (slot != null && slot.hasItem())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (par2 < this.paraChest.getContainerSize())
            {
                if (!this.moveItemStackTo(itemstack1, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 0, this.paraChest.getContainerSize(), false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0)
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    @Override
    public void removed(Player par1EntityPlayer)
    {
        super.removed(par1EntityPlayer);
        this.paraChest.stopOpen(par1EntityPlayer);
    }

    /**
     * Return this chest container's lower chest inventory.
     */
    public Container getparachestInventory()
    {
        return this.paraChest;
    }
}
