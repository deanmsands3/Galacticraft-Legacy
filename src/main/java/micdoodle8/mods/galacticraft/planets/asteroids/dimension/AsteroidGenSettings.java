package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import net.minecraft.world.level.levelgen.ChunkGeneratorSettings;

public class AsteroidGenSettings extends ChunkGeneratorSettings
{
    public int getBiomeSize()
    {
        return 4;
    }

    public int getRiverSize()
    {
        return 4;
    }

    public int getBiomeId()
    {
        return -1;
    }

    @Override
    public int getBedrockFloorPosition()
    {
        return 0;
    }

    public int getHomeTreeDistance()
    {
        return 20;
    }

    public int getHomeTreeSeparation()
    {
        return 4;
    }
}