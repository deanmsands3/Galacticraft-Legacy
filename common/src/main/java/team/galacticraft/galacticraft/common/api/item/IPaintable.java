package team.galacticraft.galacticraft.common.api.item;

import net.minecraft.world.entity.player.Player;
import net.fabricmc.api.EnvType;

public interface IPaintable
{
    int setColor(int color, Player player);
}
