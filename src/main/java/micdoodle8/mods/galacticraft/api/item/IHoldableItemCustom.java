package micdoodle8.mods.galacticraft.api.item;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.world.entity.player.Player;

public interface IHoldableItemCustom extends IHoldableItem
{
    Vector3 getLeftHandRotation(Player player);

    Vector3 getRightHandRotation(Player player);
}
