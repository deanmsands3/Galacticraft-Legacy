package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricFurnace;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerElectricFurnace extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.ELECTRIC_FURNACE)
    public static MenuType<ContainerElectricFurnace> TYPE;

    private final TileEntityElectricFurnace furnace;

    public ContainerElectricFurnace(int containerId, Inventory playerInv, TileEntityElectricFurnace furnace)
    {
        super(TYPE, containerId);
        this.furnace = furnace;

        // Electric Input Slot
        this.addSlot(new SlotSpecific(furnace, 0, 8, 49, IItemElectric.class));

        // To be smelted
        this.addSlot(new Slot(furnace, 1, 56, 25));

        // Smelting result
        this.addSlot(new FurnaceResultSlot(playerInv.player, furnace, 2, 109, 25));
        if (furnace.getContainerSize() > 2)
        {
            this.addSlot(new FurnaceResultSlot(playerInv.player, furnace, 3, 127, 25));
        }
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(playerInv, var3, 8 + var3 * 18, 142));
        }

        furnace.startOpen(playerInv.player);
//        inventory.playersUsing.add(playerInv.player);
    }

    public TileEntityElectricFurnace getFurnace()
    {
        return furnace;
    }

    @Override
    public void removed(Player player)
    {
        super.removed(player);
        furnace.stopOpen(player);
//        this.inventory.playersUsing.remove(entityplayer);
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.furnace.stillValid(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack quickMoveStack(Player player, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        Slot var3 = this.slots.get(par1);
        int off = this.furnace.getContainerSize() > 2 ? 1 : 0;

        if (var3 != null && var3.hasItem())
        {
            ItemStack var4 = var3.getItem();
            var2 = var4.copy();

            if (par1 >= 2 && par1 <= 2 + off)
            {
                if (!this.moveItemStackTo(var4, 3 + off, 39 + off, true))
                {
                    return ItemStack.EMPTY;
                }

                var3.onQuickCraft(var4, var2);
            }
            else if (par1 != 1 && par1 != 0)
            {
                if (EnergyUtil.isElectricItem(var4.getItem()))
                {
                    if (!this.moveItemStackTo(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (checkRoastable(player.level, var4))
                {
                    if (!this.moveItemStackTo(var4, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 >= 3 + off && par1 < 30 + off)
                {
                    if (!this.moveItemStackTo(var4, 30 + off, 39 + off, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 >= 30 + off && par1 < 39 + off && !this.moveItemStackTo(var4, 3 + off, 30 + off, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(var4, 3 + off, 39 + off, false))
            {
                return ItemStack.EMPTY;
            }

            if (var4.getCount() == 0)
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

            var3.onTake(player, var4);
        }

        return var2;
    }

    protected boolean checkRoastable(Level world, ItemStack stack)
    {
        return world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), world).isPresent();
    }
}
