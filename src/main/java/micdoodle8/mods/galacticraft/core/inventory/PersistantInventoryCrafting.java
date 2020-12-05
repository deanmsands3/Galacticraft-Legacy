package micdoodle8.mods.galacticraft.core.inventory;

import javax.annotation.Nonnull;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

public class PersistantInventoryCrafting extends CraftingContainer
{
    public NonNullList<ItemStack> stacks;

    private final int inventoryWidth;
    private final int inventoryHeight;

    public AbstractContainerMenu eventHandler;

    public PersistantInventoryCrafting()
    {
        super(null, 3, 3);
        int k = 9;
        this.stacks = NonNullList.withSize(k, ItemStack.EMPTY);
        this.inventoryWidth = 3;
        this.inventoryHeight = 3;
    }

    @Override
    public int getContainerSize()
    {
        return this.stacks.size();
    }

    @Override
    @Nonnull
    public ItemStack getItem(int index)
    {
        return this.stacks.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count)
    {
        ItemStack itemstack = ContainerHelper.removeItem(this.stacks, index, count);

        if (!itemstack.isEmpty())
        {
            this.setChanged();
            if (this.eventHandler != null)
            {
                this.eventHandler.slotsChanged(this);
            }
        }

        return itemstack;
    }

//    @Override
//    public ItemStack getStackInRowAndColumn(int row, int column)
//    {
//        return row >= 0 && row < this.inventoryWidth && column >= 0 && column < this.inventoryHeight ? this.getStackInSlot(row + column * this.inventoryWidth) : ItemStack.EMPTY;
//    }

    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
        return ContainerHelper.takeItem(this.stacks, index);
    }

    @Override
    public void setItem(int index, ItemStack stack)
    {
        this.stacks.set(index, stack);

        if (stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
        if (this.eventHandler != null)
        {
            this.eventHandler.slotsChanged(this);
        }
    }

    public void setInventorySlotContentsNoUpdate(int index, ItemStack stack)
    {
        this.stacks.set(index, stack);

        if (stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.stacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

//    @Override
//    public String getName()
//    {
//        return "container.crafting";
//    }
//
//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }

    @Override
    public int getMaxStackSize()
    {
        return 64;
    }

    @Override
    public void setChanged()
    {
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return true;
    }

    @Override
    public void startOpen(Player player)
    {
    }

    @Override
    public void stopOpen(Player player)
    {
    }

    @Override
    public boolean canPlaceItem(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

//    @Override
//    public int getField(int id)
//    {
//        return 0;
//    }
//
//    @Override
//    public void setField(int id, int value)
//    {
//
//    }

//    @Override
//    public int getFieldCount()
//    {
//        return 0;
//    }

    @Override
    public void clearContent()
    {

    }

//    @Override
//    public ITextComponent getDisplayName()
//    {
//        return null;
//    }
}
