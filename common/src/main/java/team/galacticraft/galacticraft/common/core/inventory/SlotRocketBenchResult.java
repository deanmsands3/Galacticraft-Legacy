package team.galacticraft.galacticraft.common.core.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotRocketBenchResult extends Slot
{
    private final Container craftMatrix;
    private final Player thePlayer;

    public SlotRocketBenchResult(Player par1EntityPlayer, Container par2IInventory, Container par3IInventory, int par4, int par5, int par6)
    {
        super(par3IInventory, par4, par5, par6);
        this.thePlayer = par1EntityPlayer;
        this.craftMatrix = par2IInventory;
    }

    @Override
    public boolean mayPlace(ItemStack par1ItemStack)
    {
        return false;
    }


    @Override
    public ItemStack onTake(Player par1EntityPlayer, ItemStack stack)
    {
        for (int var2 = 0; var2 < this.craftMatrix.getContainerSize(); ++var2)
        {
            final ItemStack var3 = this.craftMatrix.getItem(var2);

            if (!var3.isEmpty())
            {
                this.craftMatrix.removeItem(var2, 1);

                if (var3.getItem().hasContainerItem(var3))
                {
                    final ItemStack var4 = new ItemStack(var3.getItem().getCraftingRemainingItem());

                    if (!this.thePlayer.inventory.add(var4))
                    {
                        if (this.craftMatrix.getItem(var2).isEmpty())
                        {
                            this.craftMatrix.setItem(var2, var4);
                        }
                        else
                        {
                            this.thePlayer.spawnAtLocation(var4, 0.0F);
                        }
                    }
                }
            }
        }

        return stack;
    }
}
