package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.api.item.IPaintable;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.tile.TileEntityPainter;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerPainter extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.PAINTER)
    public static MenuType<ContainerPainter> TYPE;

    private final TileEntityPainter painter;

    public ContainerPainter(int containerId, Inventory playerInv, TileEntityPainter painter)
    {
        super(TYPE, containerId);
        this.painter = painter;

        // To be painted
        this.addSlot(new Slot(painter, 0, 40, 25));
        //TODO: slots which can only accept one item

        // For dyes and other colour giving items
        this.addSlot(new Slot(painter, 1, 122, 25));

        int i;
        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 104 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i)
        {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 162));
        }

        painter.playersUsing.add(playerInv.player);
    }

    public TileEntityPainter getPainter()
    {
        return painter;
    }

    @Override
    public void removed(Player entityplayer)
    {
        super.removed(entityplayer);
        this.painter.playersUsing.remove(entityplayer);
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.painter.stillValid(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int index)
    {
        ItemStack stackOrig = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        final int b = this.slots.size();

        if (slot != null && slot.hasItem())
        {
            ItemStack stack = slot.getItem();
            stackOrig = stack.copy();

            if (index < 2)
            {
                if (!this.moveItemStackTo(stack, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, stackOrig);
            }
            else if (index != 1 && index != 0)
            {
                Item item = stack.getItem();
                if (item instanceof IPaintable || (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof IPaintable))
                {
                    if (!this.mergeOneItem(stack, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < b - 9)
                {
                    if (!this.moveItemStackTo(stack, b - 9, b, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.moveItemStackTo(stack, b - 36, b - 9, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.getCount() == 0)
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (stack.getCount() == stackOrig.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, stack);
        }

        return stackOrig;
    }

    protected boolean mergeOneItem(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        boolean flag1 = false;
        if (par1ItemStack.getCount() > 0)
        {
            Slot slot;
            ItemStack slotStack;

            for (int k = par2; k < par3; k++)
            {
                slot = this.slots.get(k);
                slotStack = slot.getItem();

                if (slotStack.isEmpty())
                {
                    ItemStack stackOneItem = par1ItemStack.copy();
                    stackOneItem.setCount(1);
                    par1ItemStack.shrink(1);
                    slot.set(stackOneItem);
                    slot.setChanged();
                    flag1 = true;
                    break;
                }
            }
        }

        return flag1;
    }
}
