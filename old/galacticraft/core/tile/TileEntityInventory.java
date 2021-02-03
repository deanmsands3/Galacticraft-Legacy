package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import java.util.HashMap;

public abstract class TileEntityInventory extends BlockEntity implements WorldlyContainer
{
    public NonNullList<ItemStack> inventory;
    private final HashMap<Direction, LazyOptional<IItemHandlerModifiable>> itemHandlers = new HashMap<>();

    public TileEntityInventory(BlockEntityType<?> type)
    {
        super(type);
    }

    public NonNullList<ItemStack> getInventory()
    {
        return inventory;
    }

    protected boolean handleInventory()
    {
        return true;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack stack : getInventory())
        {
            if (!stack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public void load(CompoundTag tags)
    {
        super.load(tags);

        if (handleInventory())
        {
            NonNullList<ItemStack> stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tags, stacks);
            inventory = stacks;
        }
    }

    @Override
    public CompoundTag save(CompoundTag tags)
    {
        super.save(tags);

        if (handleInventory())
        {
            ContainerHelper.saveAllItems(tags, getInventory());
        }

        return tags;
    }

    @Override
    public int getContainerSize()
    {
        return getInventory() == null ? 0 : getInventory().size();
    }

    @Override
    public ItemStack getItem(int slot)
    {
        return getInventory() == null ? ItemStack.EMPTY : getInventory().get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount)
    {
        if (getInventory() == null)
        {
            return ItemStack.EMPTY;
        }

        return ContainerHelper.removeItem(getInventory(), slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot)
    {
        if (getInventory() == null)
        {
            return ItemStack.EMPTY;
        }

        return ContainerHelper.takeItem(getInventory(), slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack)
    {
        getInventory().set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > getMaxStackSize())
        {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player)
    {
        return !isRemoved() && this.level.hasChunkAt(this.worldPosition);
    }

    @Override
    public int getMaxStackSize()
    {
        return 64;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side)
    {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side)
    {
        return true;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side)
    {
        if (!this.remove && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (!this.itemHandlers.containsKey(side))
            {
                this.itemHandlers.put(side, LazyOptional.of(new NonNullSupplier<IItemHandlerModifiable>()
                {
                    @Nonnull
                    @Override
                    public IItemHandlerModifiable get()
                    {
                        return new SidedInvWrapper(TileEntityInventory.this, side);
                    }
                }));
            }
            else
            {
                return this.itemHandlers.get(side).cast();
            }
        }
        return super.getCapability(cap, side);
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
        for (int i = 0; i < this.getInventory().size(); ++i)
        {
            this.getInventory().set(i, ItemStack.EMPTY);
        }
    }

    @Override
    public synchronized void handleUpdateTag(CompoundTag tag)
    {
        this.load(tag);
    }
}
