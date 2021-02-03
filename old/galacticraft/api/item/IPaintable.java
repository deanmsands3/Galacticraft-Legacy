package micdoodle8.mods.galacticraft.api.item;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;

public interface IPaintable
{
    int setColor(int color, Player player);
}
