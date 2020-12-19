package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCItems;
import team.galacticraft.galacticraft.common.core.util.RecipeUtil;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
//import net.minecraftforge.registries.ObjectHolder;

public class ContainerSchematicBuggy extends AbstractContainerMenu
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.SCHEMATIC_BUGGY)
    public static MenuType<ContainerSchematicBuggy> TYPE;

    public InventoryBuggyBench craftMatrix = new InventoryBuggyBench(this);
    public Container craftResult = new ResultContainer();
    private final Level world;

    public ContainerSchematicBuggy(int containerId, Inventory playerInv)
    {
        super(TYPE, containerId);
        final int change = 27;
        this.world = playerInv.player.level;
        this.addSlot(new SlotRocketBenchResult(playerInv.player, this.craftMatrix, this.craftResult, 0, 142, 79 + change));
        int var6;
        int var7;

        // Body
        for (var6 = 0; var6 < 4; ++var6)
        {
            for (var7 = 0; var7 < 3; ++var7)
            {
                this.addSlot(new SlotBuggyBench(this.craftMatrix, var7 * 4 + var6 + 1, 39 + var7 * 18, 14 + var6 * 18 + change, playerInv.player));
            }
        }

        for (var6 = 0; var6 < 2; ++var6)
        {
            for (var7 = 0; var7 < 2; ++var7)
            {
                this.addSlot(new SlotBuggyBench(this.craftMatrix, var7 * 2 + var6 + 13, 21 + var7 * 72, 14 + var6 * 54 + change, playerInv.player));
            }
        }

        // Addons
        for (int var8 = 0; var8 < 3; var8++)
        {
            this.addSlot(new SlotBuggyBench(this.craftMatrix, 17 + var8, 93 + var8 * 26, -15 + change, playerInv.player));
        }

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlot(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 111 + var6 * 18 + change));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlot(new Slot(playerInv, var6, 8 + var6 * 18, 169 + change));
        }

        this.slotsChanged(this.craftMatrix);
    }

    @Override
    public void removed(Player par1EntityPlayer)
    {
        super.removed(par1EntityPlayer);

        if (!this.world.isClientSide)
        {
            for (int var2 = 1; var2 < this.craftMatrix.getContainerSize(); ++var2)
            {
                final ItemStack slot = this.craftMatrix.removeItemNoUpdate(var2);

                if (!slot.isEmpty())
                {
                    par1EntityPlayer.spawnAtLocation(slot, 0.0F);
                }
            }
        }
    }

    @Override
    public void slotsChanged(Container par1IInventory)
    {
        this.craftResult.setItem(0, RecipeUtil.findMatchingBuggy(this.craftMatrix));
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return true;
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.slots.get(par1);
        final int b = this.slots.size();

        if (slot != null && slot.hasItem())
        {
            final ItemStack var4 = slot.getItem();
            var2 = var4.copy();

            if (par1 < b - 36)
            {
                if (!this.moveItemStackTo(var4, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }

                if (par1 == 0)
                {
                    slot.onQuickCraft(var4, var2);
                }
            }
            else
            {
                Item i = var4.getItem();
                if (i == GCItems.heavyPlatingTier1 || i == GCItems.buggyMaterialWheel || i == GCItems.buggyMaterialSeat || i == GCItems.buggyMaterialStorage)
                {
                    for (int j = 1; j < 20; j++)
                    {
                        if (this.slots.get(j).mayPlace(var4))
                        {
                            this.mergeOneItem(var4, j, j + 1, false);
                        }
                    }
                }
                else
                {
                    if (par1 < b - 9)
                    {
                        if (!this.moveItemStackTo(var4, b - 9, b, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else
                    {
                        if (!this.moveItemStackTo(var4, b - 36, b - 9, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (var4.getCount() == 0)
            {
                slot.set(ItemStack.EMPTY);
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.setChanged();
            slot.onTake(par1EntityPlayer, var4);
        }

        return var2;
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
