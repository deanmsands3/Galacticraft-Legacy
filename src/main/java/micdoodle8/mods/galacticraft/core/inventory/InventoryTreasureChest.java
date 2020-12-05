package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class InventoryTreasureChest implements Container
{
    private final String name;
    private final Container upperChest;
    private final Container lowerChest;

    public InventoryTreasureChest(String name, Container upper, Container lower)
    {
        this.name = name;

        if (upper == null)
        {
            upper = lower;
        }

        if (lower == null)
        {
            lower = upper;
        }

        this.upperChest = upper;
        this.lowerChest = lower;
    }

    @Override
    public int getContainerSize()
    {
        return this.upperChest.getContainerSize() + this.lowerChest.getContainerSize();
    }


//    @Override
//    public String getName()
//    {
//        return this.upperChest.hasCustomName() ? this.upperChest.getName() : (this.lowerChest.hasCustomName() ? this.lowerChest.getName() : this.name);
//    }
//
//    @Override
//    public boolean hasCustomName()
//    {
//        return this.upperChest.hasCustomName() || this.lowerChest.hasCustomName();
//    }

    @Override
    public ItemStack getItem(int slot)
    {
        return slot >= this.upperChest.getContainerSize() ? this.lowerChest.getItem(slot - this.upperChest.getContainerSize()) : this.upperChest.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count)
    {
        return slot >= this.upperChest.getContainerSize() ? this.lowerChest.removeItem(slot - this.upperChest.getContainerSize(), count) : this.upperChest.removeItem(slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot)
    {
        return slot >= this.upperChest.getContainerSize() ? this.lowerChest.removeItemNoUpdate(slot - this.upperChest.getContainerSize()) : this.upperChest.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack)
    {
        if (slot >= this.upperChest.getContainerSize())
        {
            this.lowerChest.setItem(slot - this.upperChest.getContainerSize(), stack);
        }
        else
        {
            this.upperChest.setItem(slot, stack);
        }
    }

    @Override
    public int getMaxStackSize()
    {
        return this.upperChest.getMaxStackSize();
    }

    @Override
    public void setChanged()
    {
        this.upperChest.setChanged();
        this.lowerChest.setChanged();
    }

    @Override
    public boolean stillValid(Player p_70300_1_)
    {
        return this.upperChest.stillValid(p_70300_1_) && this.lowerChest.stillValid(p_70300_1_);
    }

    @Override
    public void startOpen(Player player)
    {
        this.upperChest.startOpen(player);
        this.lowerChest.startOpen(player);
    }

    @Override
    public void stopOpen(Player player)
    {
        this.upperChest.stopOpen(player);
        this.lowerChest.stopOpen(player);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public void clearContent()
    {
        this.upperChest.clearContent();
        this.lowerChest.clearContent();
    }

    @Override
    public boolean isEmpty()
    {
        return this.upperChest.isEmpty() && this.lowerChest.isEmpty();
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

//    @Override
//    public ITextComponent getDisplayName()
//    {
//        return this.hasCustomName() ? new StringTextComponent(this.getName()) : new TranslationTextComponent(this.getName(), new Object[0]);
//    }
}
