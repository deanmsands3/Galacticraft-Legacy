package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerMethaneSynthesizer extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsContainerNames.METHANE_SYNTHESIZER)
    public static MenuType<ContainerMethaneSynthesizer> TYPE;

    private final TileEntityMethaneSynthesizer synthesizer;

    public ContainerMethaneSynthesizer(int containerId, Inventory playerInv, TileEntityMethaneSynthesizer synthesizer)
    {
        super(TYPE, containerId);
        this.synthesizer = synthesizer;

        // Electric Input Slot
        this.addSlot(new SlotSpecific(this.synthesizer, 0, 53, 53, IItemElectric.class));

        // Input slot - hydrogen
        this.addSlot(new Slot(this.synthesizer, 1, 7, 7));

        // Input slot - CO2
        this.addSlot(new Slot(this.synthesizer, 2, 28, 7));

        // Carbon slot
        this.addSlot(new SlotSpecific(this.synthesizer, 3, 28, 53, new ItemStack(MarsItems.FRAGMENTED_CARBON, 1)));

        // Output slot
        this.addSlot(new Slot(this.synthesizer, 4, 153, 7));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 104 + var3 * 18 - 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(playerInv, var3, 8 + var3 * 18, 144));
        }

        this.synthesizer.startOpen(playerInv.player);
    }

    public TileEntityMethaneSynthesizer getSynthesizer()
    {
        return synthesizer;
    }

    @Override
    public void removed(Player entityplayer)
    {
        super.removed(entityplayer);
        this.synthesizer.stopOpen(entityplayer);
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.synthesizer.stillValid(par1EntityPlayer);
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

        if (slot != null && slot.hasItem())
        {
            final ItemStack var4 = slot.getItem();
            var2 = var4.copy();

            if (par1 < 5)
            {
                if (!this.moveItemStackTo(var4, 5, 41, true))
                {
                    return ItemStack.EMPTY;
                }

                if (par1 == 2)
                {
                    slot.onQuickCraft(var4, var2);
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(var4.getItem()))
                {
                    if (!this.moveItemStackTo(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (var4.getItem() == AsteroidsItems.ATMOSPHERIC_VALVE)
                    {
                        if (!this.moveItemStackTo(var4, 2, 3, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (var4.getItem() == MarsItems.FRAGMENTED_CARBON)
                    {
                        if (!this.moveItemStackTo(var4, 3, 4, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (FluidUtil.isPartialContainer(var4, AsteroidsItems.PARTIAL_METHANE_CANISTER))
                    {
                        if (!this.moveItemStackTo(var4, 4, 5, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (par1 < 32)
                    {
                        if (!this.moveItemStackTo(var4, 32, 41, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(var4, 5, 32, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (var4.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }
}
