package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.api.inventory.IInventoryGC;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nonnull;

public class InventoryExtended implements IInventoryGC
{
    public NonNullList<ItemStack> stacks = NonNullList.withSize(11, ItemStack.EMPTY);

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

    @Override
    public int getContainerSize()
    {
        return this.stacks.size();
    }

    @Override
    @NotNull
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
        }

        return itemstack;
    }

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
    }

//    @Override
//    public String getName()
//    {
//        return "Galacticraft Player Inventory";
//    }
//
//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
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
    public boolean stillValid(Player entityplayer)
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
    public boolean canPlaceItem(int i, ItemStack itemstack)
    {
        return false;
    }

    @Override
    public void dropExtendedItems(Player player)
    {
        for (int i = 0; i < this.stacks.size(); i++)
        {
            ItemStack stack = this.stacks.get(i);

            if (!stack.isEmpty())
            {
                player.drop(stack, true);
            }

            this.stacks.set(i, ItemStack.EMPTY);
        }
    }

    // Backwards compatibility for old inventory
    public void readFromNBTOld(ListTag par1NBTTagList)
    {
        this.stacks = NonNullList.withSize(11, ItemStack.EMPTY);

        for (int i = 0; i < par1NBTTagList.size(); ++i)
        {
            final CompoundTag nbttagcompound = par1NBTTagList.getCompound(i);
            final int j = nbttagcompound.getByte("Slot") & 255;
            final ItemStack itemstack = ItemStack.of(nbttagcompound);

            if (!itemstack.isEmpty())
            {
                if (j >= 200 && j < this.stacks.size() + 200 - 1)
                {
                    this.stacks.set(j - 200, itemstack);
                }
            }
        }
    }

    public void readFromNBT(ListTag tagList)
    {
        this.stacks = NonNullList.withSize(11, ItemStack.EMPTY);

        for (int i = 0; i < tagList.size(); ++i)
        {
            final CompoundTag nbttagcompound = tagList.getCompound(i);
            final int j = nbttagcompound.getByte("Slot") & 255;
            final ItemStack itemstack = ItemStack.of(nbttagcompound);

            if (!itemstack.isEmpty())
            {
                this.stacks.set(j, itemstack);
            }
        }
    }

    public ListTag writeToNBT(ListTag tagList)
    {
        CompoundTag nbttagcompound;

        for (int i = 0; i < this.stacks.size(); ++i)
        {
            if (!this.stacks.get(i).isEmpty())
            {
                nbttagcompound = new CompoundTag();
                nbttagcompound.putByte("Slot", (byte) i);
                this.stacks.get(i).save(nbttagcompound);
                tagList.add(nbttagcompound);
            }
        }

        return tagList;
    }

    @Override
    public void copyInventory(IInventoryGC playerInv)
    {
        InventoryExtended toCopy = (InventoryExtended) playerInv;
        for (int i = 0; i < this.stacks.size(); ++i)
        {
            this.stacks.set(i, toCopy.stacks.get(i).copy());
        }
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
