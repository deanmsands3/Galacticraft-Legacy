package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.api.recipe.ISchematicItem;
import team.galacticraft.galacticraft.common.Constants;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
//import net.minecraftforge.registries.ObjectHolder;

public class ContainerSchematic extends AbstractContainerMenu
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.SCHEMATIC)
    public static MenuType<ContainerSchematic> TYPE;

    public InventorySchematic craftMatrix = new InventorySchematic(this);
    public Container craftResult = new ResultContainer();
    private final Level world;

    public ContainerSchematic(int containerId, Inventory playerInv)
    {
        super(TYPE, containerId);
        this.world = playerInv.player.level;
        this.addSlot(new SlotSpecific(this.craftMatrix, 0, 80, 1, ISchematicItem.class));
        int var6;
        int var7;

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlot(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 111 + var6 * 18 - 59 + 16));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlot(new Slot(playerInv, var6, 8 + var6 * 18, 169 - 59 + 16));
        }

        this.slotsChanged(this.craftMatrix);
    }

    @Override
    public void removed(Player par1EntityPlayer)
    {
        super.removed(par1EntityPlayer);

        if (!this.world.isClientSide)
        {
            for (int var2 = 0; var2 < 1; ++var2)
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
    public boolean stillValid(Player entityplayer)
    {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par2)
    {
        ItemStack var3 = ItemStack.EMPTY;
        final Slot var4 = this.slots.get(par2);

        if (var4 != null && var4.hasItem())
        {
            final ItemStack var5 = var4.getItem();
            var3 = var5.copy();

            if (par2 < 1)
            {
                if (!this.moveItemStackTo(var5, 1, this.slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(var5, 0, 1, false))
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
}
