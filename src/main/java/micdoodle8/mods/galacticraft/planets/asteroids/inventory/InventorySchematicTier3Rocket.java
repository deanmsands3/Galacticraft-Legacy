package micdoodle8.mods.galacticraft.planets.asteroids.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class InventorySchematicTier3Rocket implements Container
{
    private final NonNullList<ItemStack> stacks;
    private final int inventoryWidth;
    private final AbstractContainerMenu eventHandler;

    public InventorySchematicTier3Rocket(AbstractContainerMenu par1Container)
    {
        this.stacks = NonNullList.withSize(22, ItemStack.EMPTY);
        this.eventHandler = par1Container;
        this.inventoryWidth = 5;
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
//        return "container.crafting";
//    }

    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
        ItemStack oldstack = ContainerHelper.takeItem(this.stacks, index);
        if (!oldstack.isEmpty())
        {
            this.setChanged();
            this.eventHandler.slotsChanged(this);
        }
        return oldstack;
    }

    @Override
    public ItemStack removeItem(int index, int count)
    {
        ItemStack itemstack = ContainerHelper.removeItem(this.stacks, index, count);

        if (!itemstack.isEmpty())
        {
            this.setChanged();
            this.eventHandler.slotsChanged(this);
        }

        return itemstack;
    }

    @Override
    public void setItem(int index, ItemStack stack)
    {
        if (stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }

        this.stacks.set(index, stack);
        this.setChanged();
        this.eventHandler.slotsChanged(this);
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
