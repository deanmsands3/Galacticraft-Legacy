package team.galacticraft.galacticraft.common.api.item;

import team.galacticraft.galacticraft.common.api.vector.Vector3;
import net.minecraft.world.entity.player.Player;

public interface IHoldableItemCustom extends IHoldableItem
{
    Vector3 getLeftHandRotation(Player player);

    Vector3 getRightHandRotation(Player player);
}
