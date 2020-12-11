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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerSchematicTier1Rocket extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.SCHEMATIC_T1_ROCKET)
    public static MenuType<ContainerSchematicTier1Rocket> TYPE;

    public InventoryRocketBench craftMatrix = new InventoryRocketBench(this);
    public Container craftResult = new ResultContainer();
    private final Level world;

    public ContainerSchematicTier1Rocket(int containerId, Inventory playerInv)
    {
        super(TYPE, containerId);
        final int change = 27;
        this.world = playerInv.player.level;
        this.addSlot(new SlotRocketBenchResult(playerInv.player, this.craftMatrix, this.craftResult, 0, 142, 69 + change));
        int var6;
        int var7;

        // Cone
        this.addSlot(new SlotRocketBench(this.craftMatrix, 1, 48, -8 + change, playerInv.player));

        // Body
        for (var6 = 0; var6 < 4; ++var6)
        {
            this.addSlot(new SlotRocketBench(this.craftMatrix, 2 + var6, 39, -6 + var6 * 18 + 16 + change, playerInv.player));
        }

        // Body Right
        for (var6 = 0; var6 < 4; ++var6)
        {
            this.addSlot(new SlotRocketBench(this.craftMatrix, 6 + var6, 57, -6 + var6 * 18 + 16 + change, playerInv.player));
        }

        // Left fins
        this.addSlot(new SlotRocketBench(this.craftMatrix, 10, 21, 64 + change, playerInv.player));
        this.addSlot(new SlotRocketBench(this.craftMatrix, 11, 21, 82 + change, playerInv.player));

        // Engine
        this.addSlot(new SlotRocketBench(this.craftMatrix, 12, 48, 82 + change, playerInv.player));

        // Right fins
        this.addSlot(new SlotRocketBench(this.craftMatrix, 13, 75, 64 + change, playerInv.player));
        this.addSlot(new SlotRocketBench(this.craftMatrix, 14, 75, 82 + change, playerInv.player));

        // Addons
        for (int var8 = 0; var8 < 3; var8++)
        {
            this.addSlot(new SlotRocketBench(this.craftMatrix, 15 + var8, 93 + var8 * 26, -15 + change, playerInv.player));
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
            for (int var2 = 1; var2 < 18; ++var2)
            {
                final ItemStack var3 = this.craftMatrix.removeItemNoUpdate(var2);

                if (!var3.isEmpty())
                {
                    par1EntityPlayer.spawnAtLocation(var3, 0.0F);
                }
            }
        }
    }

    @Override
    public void slotsChanged(Container par1IInventory)
    {
        this.craftResult.setItem(0, RecipeUtil.findMatchingSpaceshipRecipe(this.craftMatrix));
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
        final Slot var3 = this.slots.get(par1);

        if (var3 != null && var3.hasItem())
        {
            final ItemStack var4 = var3.getItem();
            var2 = var4.copy();

            if (par1 <= 17)
            {
                if (!this.moveItemStackTo(var4, 18, 54, false))
                {
                    return ItemStack.EMPTY;
                }

                if (par1 == 0)
                {
                    var3.onQuickCraft(var4, var2);
                }
            }
            else if (var2.getItem() == GCItems.partNoseCone)
            {
                if (!this.mergeOneItem(var4, 1, 2, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (var2.getItem() == GCItems.heavyPlatingTier1)
            {
                if (!this.mergeOneItem(var4, 2, 10, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (var2.getItem() == GCItems.partFins)
            {
                if (!this.mergeOneItem(var4, 10, 12, false) && !this.mergeOneItem(var4, 13, 15, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (var2.getItem() == GCItems.rocketEngineT1 || var2.getItem() == GCItems.rocketBoosterT1)
            {
                if (!this.mergeOneItem(var4, 12, 13, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                boolean foundChest = false;
//                for (ItemStack woodChest : OreDictionary.getOres("chestWood"))
//                {
//                    if (var2.getItem() == woodChest.getItem())
//                    {
//                        foundChest = true;
//                        break;
//                    }
//                } TODO PR from github
                if (foundChest)
                {
                    if (!this.mergeOneItem(var4, 15, 18, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 >= 18 && par1 < 45)
                {
                    if (!this.moveItemStackTo(var4, 45, 54, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 >= 45 && par1 < 54)
                {
                    if (!this.moveItemStackTo(var4, 18, 45, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (var4.getCount() == 0)
            {
                if (par1 == 0)
                {
                    var3.onTake(par1EntityPlayer, var4);
                }
                var3.set(ItemStack.EMPTY);
                return var2;
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            var3.onTake(par1EntityPlayer, var4);
            if (par1 == 0)
            {
                var3.setChanged();
            }
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
