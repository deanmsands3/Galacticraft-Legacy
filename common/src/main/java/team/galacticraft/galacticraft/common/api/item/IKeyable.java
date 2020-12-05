package team.galacticraft.galacticraft.common.api.item;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Implement into tile entities that do something upon being activated by a key
 */
public interface IKeyable
{
    /**
     * @return -1 for any tier, or return tier required for key activated to
     * pass
     */
    int getTierOfKeyRequired();

    /**
     * called when key of correct tier is clicked
     *
     * @param key  the key itemstack
     * @param face the block face clicked
     * @return true if something was done, false if not
     */
    boolean onValidKeyActivated(Player player, ItemStack key, Direction face);

    /**
     * called when player is not holding correct tier of key, or any key at all
     *
     * @param player
     * @return true if something was done, false if not
     */
    boolean onActivatedWithoutKey(Player player, Direction face);

    boolean canBreak();
}
