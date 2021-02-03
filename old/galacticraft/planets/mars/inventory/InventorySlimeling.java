package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.entities.SlimelingEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class InventorySlimeling implements Container
{
    private NonNullList<ItemStack> stacks = NonNullList.withSize(30, ItemStack.EMPTY);
    private final SlimelingEntity slimeling;
    public AbstractContainerMenu currentContainer;

    public InventorySlimeling(SlimelingEntity slimeling)
    {
        this.slimeling = slimeling;
    }

    @Override
    public int getContainerSize()
    {
        return this.stacks.size();
    }

    @Override
    public ItemStack getItem(int par1)
    {
        return par1 >= this.getContainerSize() ? ItemStack.EMPTY : this.stacks.get(par1);
    }

//    @Override
//    public String getName()
//    {
//        return GCCoreUtil.translate("container.slimeling_inventory");
//    }

    @Override
    public ItemStack removeItemNoUpdate(int par1)
    {
        if (!this.stacks.get(par1).isEmpty())
        {
            final ItemStack var2 = this.stacks.get(par1);
            this.stacks.set(par1, ItemStack.EMPTY);
            this.setChanged();
            return var2;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    private void removeInventoryBagContents()
    {
        if (this.currentContainer instanceof ContainerSlimeling)
        {
            ContainerSlimeling.removeSlots((ContainerSlimeling) this.currentContainer);
        }

        for (int i = 2; i < this.stacks.size(); i++)
        {
            if (!this.stacks.get(i).isEmpty())
            {
                if (!this.slimeling.level.isClientSide)
                {
                    this.slimeling.spawnAtLocation(this.stacks.get(i), 0.5F);
                }

                this.stacks.set(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public ItemStack removeItem(int par1, int par2)
    {
        if (!this.stacks.get(par1).isEmpty())
        {
            ItemStack var3;

            //It's a removal of the Slimeling Inventory Bag
            if (par1 == 1 && this.stacks.get(par1).getCount() <= par2)
            {
                this.removeInventoryBagContents();
                var3 = this.stacks.get(par1);
                this.stacks.set(par1, ItemStack.EMPTY);
                this.setChanged();
                return var3;
            }
            else
            //Normal case of decrStackSize for a slot
            {
                var3 = this.stacks.get(par1).split(par2);

                if (this.stacks.get(par1).isEmpty())
                {
                    //Not sure if this is necessary again, given the above?
                    if (par1 == 1)
                    {
                        this.removeInventoryBagContents();
                    }

                    this.stacks.set(par1, ItemStack.EMPTY);
                }

                this.setChanged();
                return var3;
            }
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int par1, ItemStack par2ItemStack)
    {
        if (par1 == 1 && (par2ItemStack.isEmpty() && !this.stacks.get(par1).isEmpty() || !ItemStack.matches(par2ItemStack, this.stacks.get(par1))))
        {
            ContainerSlimeling.addAdditionalSlots((ContainerSlimeling) this.currentContainer, this.slimeling, par2ItemStack);
        }

        this.stacks.set(par1, par2ItemStack);
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

    public void readFromNBT(ListTag tagList)
    {
        if (tagList == null || tagList.size() <= 0)
        {
            return;
        }

        this.stacks = NonNullList.withSize(this.stacks.size(), ItemStack.EMPTY);

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
        return this.slimeling.isAlive() && par1EntityPlayer.distanceToSqr(this.slimeling) <= 64.0D;
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemstack)
    {
        return false;
    }

    //We don't use these because we use forge containers
    @Override
    public void startOpen(Player player)
    {
    }

    //We don't use these because we use forge containers
    @Override
    public void stopOpen(Player player)
    {
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
//    public boolean hasCustomName()
//    {
//        return false;
//    }

//    @Override
//    public ITextComponent getDisplayName()
//    {
//        return this.hasCustomName() ? new StringTextComponent(this.getName()) : new TranslationTextComponent(this.getName(), new Object[0]);
//    }
}
