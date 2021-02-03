package micdoodle8.mods.galacticraft.planets.venus.dimension;

import net.minecraft.world.level.biome.BiomeSourceSettings;
import net.minecraft.world.level.storage.LevelData;

public class VenusBiomeProviderSettings implements BiomeSourceSettings
{
    private LevelData worldInfo;
    private VenusGenSettings generatorSettings;

    public VenusBiomeProviderSettings(LevelData worldInfo)
    {
        this.worldInfo = worldInfo;
    }

    public VenusBiomeProviderSettings setGeneratorSettings(VenusGenSettings settings)
    {
        this.generatorSettings = settings;
        return this;
    }

    public LevelData getWorldInfo()
    {
        return this.worldInfo;
    }

    public VenusGenSettings getGeneratorSettings()
    {
        return this.generatorSettings;
    }
}