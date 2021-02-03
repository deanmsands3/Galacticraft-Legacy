package micdoodle8.mods.galacticraft.api.client;

import net.minecraft.world.level.dimension.LevelStem;

public interface IScreenManager
{
    /**
     * Used by screen renderers to figure out which world they are in
     *
     * @return The WorldProvider of the world where the screen driver is located
     */
    LevelStem getWorldProvider();
}
