package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.tile.TileEntityCoalGenerator;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerCoalGenerator extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.COAL_GENERATOR)
    public static MenuType<ContainerCoalGenerator> TYPE;

    private final TileEntityCoalGenerator generator;

//    public ContainerCoalGenerator(int containerId, PlayerInventory playerInv)
//    {
//        this(containerId, playerInv, new Inventory(1));
//    }

    public ContainerCoalGenerator(int containerId, Inventory playerInv, TileEntityCoalGenerator generator)
    {
        super(TYPE, containerId);
        this.generator = generator;
        this.addSlot(new SlotSpecific(generator, 0, 33, 34, new ItemStack(Items.COAL), new ItemStack(Item.byBlock(Blocks.COAL_BLOCK))));
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
    }

    public TileEntityCoalGenerator getGenerator()
    {
        return generator;
    }

    @Override
    public void removed(Player entityplayer)
    {
        super.removed(entityplayer);
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.generator.stillValid(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        Slot var3 = this.slots.get(par1);

        if (var3 != null && var3.hasItem())
        {
            ItemStack var4 = var3.getItem();
            var2 = var4.copy();

            if (par1 != 0)
            {
                if (var4.getItem() == Items.COAL)
                {
                    if (!this.moveItemStackTo(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 >= 28)
                {
                    if (!this.moveItemStackTo(var4, 1, 28, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.moveItemStackTo(var4, 28, 37, false))
                {
                    return ItemStack.EMPTY;
                }

            }
            else if (!this.moveItemStackTo(var4, 1, 37, false))
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

            var3.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }
}
