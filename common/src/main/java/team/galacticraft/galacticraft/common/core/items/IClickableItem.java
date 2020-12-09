package team.galacticraft.galacticraft.common.core.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IClickableItem
{
    /**
     * Use to replicate ItemStack version of onItemRightClick as in MC1.10
     */
    ItemStack onItemRightClick(ItemStack itemStack, Level worldIn, Player player);
}
