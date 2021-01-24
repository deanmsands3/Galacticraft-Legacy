package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.tile.TileEntityCrafting;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;

public class SlotCraftingMemory extends ResultSlot
{
    public Container tileEntity;

    public SlotCraftingMemory(Player player, CraftingContainer craftingInventory, Container p_i45790_3_, int slotIndex, int xPosition, int yPosition, Container tile)
    {
        super(player, craftingInventory, p_i45790_3_, slotIndex, xPosition, yPosition);
        this.tileEntity = tile;
    }

    @Override
    public ItemStack onTake(Player thePlayer, ItemStack stack)
    {
        if (!stack.isEmpty() && this.tileEntity instanceof TileEntityCrafting)
        {
            ((TileEntityCrafting) this.tileEntity).updateMemory(stack);
        }
        return super.onTake(thePlayer, stack);
    }
}
