package team.galacticraft.galacticraft.common.core.entities;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class InventoryEntity extends NetworkedEntity implements Container
{
    protected NonNullList<ItemStack> stacks = NonNullList.withSize(0, ItemStack.EMPTY);

    public InventoryEntity(EntityType<?> type, Level world)
    {
        super(type, world);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt)
    {
        ContainerHelper.loadAllItems(nbt, this.stacks);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt)
    {
        ContainerHelper.saveAllItems(nbt, this.stacks);
    }

    @Override
    public ItemStack getItem(int var1)
    {
        return this.stacks.get(var1);
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
    public boolean stillValid(Player entityplayer)
    {
        return true;
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

    @Override
    public void clearContent()
    {
        for (int i = 0; i < this.stacks.size(); ++i)
        {
            this.stacks.set(i, ItemStack.EMPTY);
        }
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }
}
