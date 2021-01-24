package micdoodle8.mods.galacticraft.planets.asteroids.inventory;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.inventory.SlotRocketBenchResult;
import micdoodle8.mods.galacticraft.planets.mars.util.RecipeUtilMars;
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

public class ContainerSchematicAstroMiner extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + AsteroidsContainerNames.SCHEMATIC_ASTRO_MINER)
    public static MenuType<ContainerSchematicAstroMiner> TYPE;

    public InventorySchematicAstroMiner craftMatrix = new InventorySchematicAstroMiner(this);
    public Container craftResult = new ResultContainer();
    private final Level world;

    public ContainerSchematicAstroMiner(int containerId, Inventory playerInv)
    {
        super(TYPE, containerId);
        this.world = playerInv.player.level;
        this.addSlot(new SlotRocketBenchResult(playerInv.player, this.craftMatrix, this.craftResult, 0, 142, 72));
        int i;
        int j;
        int count = 1;

        // Miner top layer
        for (i = 0; i < 4; i++)
        {
            this.addSlot(new SlotSchematicAstroMiner(this.craftMatrix, count++, 27 + i * 18, 35, playerInv.player));
        }

        // Miner mid layer
        for (i = 0; i < 5; i++)
        {
            this.addSlot(new SlotSchematicAstroMiner(this.craftMatrix, count++, 16 + i * 18, 53, playerInv.player));
        }

        // Miner bottom layer
        for (i = 0; i < 3; i++)
        {
            this.addSlot(new SlotSchematicAstroMiner(this.craftMatrix, count++, 44 + i * 18, 71, playerInv.player));
        }

        // Laser
        for (i = 0; i < 2; ++i)
        {
            this.addSlot(new SlotSchematicAstroMiner(this.craftMatrix, count++, 8 + i * 18, 77, playerInv.player));
        }

        // Player inv:

        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 140 + i * 18 - 26));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 144 + 54 - 26));
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
        this.craftResult.setItem(0, RecipeUtilMars.findMatchingAstroMinerRecipe(this.craftMatrix));
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot var3 = this.slots.get(par1);

        if (var3 != null && var3.hasItem())
        {
            final ItemStack var4 = var3.getItem();
            var2 = var4.copy();

            boolean done = false;
            if (par1 <= 14)
            {
                if (!this.moveItemStackTo(var4, 15, 51, false))
                {
                    return ItemStack.EMPTY;
                }

                var3.onQuickCraft(var4, var2);
            }
            else
            {
                boolean valid = false;
                for (int i = 1; i < 15; i++)
                {
                    Slot testSlot = this.slots.get(i);
                    if (!testSlot.hasItem() && testSlot.mayPlace(var2))
                    {
                        valid = true;
                        break;
                    }
                }
                if (valid)
                {
                    if (!this.mergeOneItemTestValid(var4, 1, 15, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (par1 >= 15 && par1 < 42)
                    {
                        if (!this.moveItemStackTo(var4, 42, 51, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (par1 >= 42 && par1 < 51)
                    {
                        if (!this.moveItemStackTo(var4, 15, 42, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(var4, 15, 51, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (var4.isEmpty())
            {
                var3.set(ItemStack.EMPTY);
            }
            else
            {
                var3.setChanged();
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            var3.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }

    protected boolean mergeOneItemTestValid(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        boolean flag1 = false;
        if (!par1ItemStack.isEmpty())
        {
            Slot slot;
            ItemStack slotStack;

            for (int k = par2; k < par3; k++)
            {
                slot = this.slots.get(k);
                slotStack = slot.getItem();

                if (slotStack.isEmpty() && slot.mayPlace(par1ItemStack))
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
