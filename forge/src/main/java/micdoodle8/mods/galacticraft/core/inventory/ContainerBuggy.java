package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.entities.BuggyEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerBuggy extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.BUGGY)
    public static MenuType<ContainerBuggy> TYPE;

    private final Container playerInv;
    private final Container buggyInv;

    public BuggyEntity.BuggyType buggyType;

    public ContainerBuggy(int containerId, Inventory playerInv, BuggyEntity.BuggyType type)
    {
        this(containerId, playerInv, new SimpleContainer(type.getInvSize()), type);
    }

    public ContainerBuggy(int containerId, Inventory playerInv, Container buggyInv, BuggyEntity.BuggyType type)
    {
        super(TYPE, containerId);
        this.playerInv = playerInv;
        this.buggyInv = buggyInv;
        this.buggyType = type;
        buggyInv.startOpen(playerInv.player);

        int var4;
        int var5;

        if (type != BuggyEntity.BuggyType.NO_INVENTORY)
        {
            for (int i = 0; i < type.getInvSize(); ++i)
            {
                int row = i / 9;
                this.addSlot(new Slot(this.buggyInv, i, 8 + row * 18, 50 + row * 18));
            }
        }

        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlot(new Slot(this.playerInv, var5 + var4 * 9 + 9, 8 + var5 * 18, 49 + var4 * 18 + 14 + type.ordinal() * 36));
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlot(new Slot(this.playerInv, var4, 8 + var4 * 18, 107 + 14 + type.ordinal() * 36));
        }
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.buggyInv.stillValid(par1EntityPlayer);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or
     * you will crash when someone does that.
     */
    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par2)
    {
        ItemStack var3 = ItemStack.EMPTY;
        final Slot var4 = this.slots.get(par2);
        final int b = this.slots.size() - 36;

        if (var4 != null && var4.hasItem())
        {
            final ItemStack var5 = var4.getItem();
            var3 = var5.copy();

            if (par2 < b)
            {
                if (!this.moveItemStackTo(var5, b, b + 36, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(var5, 0, b, false))
            {
                return ItemStack.EMPTY;
            }

            if (var5.getCount() == 0)
            {
                var4.set(ItemStack.EMPTY);
            }
            else
            {
                var4.setChanged();
            }
        }

        return var3;
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    @Override
    public void removed(Player par1EntityPlayer)
    {
        super.removed(par1EntityPlayer);
        this.playerInv.stopOpen(par1EntityPlayer);
    }

    /**
     * Return this chest container's lower chest inventory.
     */
    public Container getPlayerInv()
    {
        return this.playerInv;
    }
}
