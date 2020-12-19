package team.galacticraft.galacticraft.common.api.item;

import net.minecraft.world.entity.player.Player;
import team.galacticraft.galacticraft.common.api.vector.Vector3;

public interface IHoldableItemCustom extends IHoldableItem
{
    Vector3 getLeftHandRotation(Player player);

    Vector3 getRightHandRotation(Player player);
}
