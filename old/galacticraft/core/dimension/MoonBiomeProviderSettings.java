package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.dimension.chunk.MoonGenSettings;
import net.minecraft.world.level.biome.BiomeSourceSettings;
import net.minecraft.world.level.storage.LevelData;

public class MoonBiomeProviderSettings implements BiomeSourceSettings
{
    private LevelData worldInfo;
    private MoonGenSettings generatorSettings;

    public MoonBiomeProviderSettings(LevelData worldInfo)
    {
        this.worldInfo = worldInfo;
    }

    public MoonBiomeProviderSettings setGeneratorSettings(MoonGenSettings settings)
    {
        this.generatorSettings = settings;
        return this;
    }

    public LevelData getWorldInfo()
    {
        return this.worldInfo;
    }

    public MoonGenSettings getGeneratorSettings()
    {
        return this.generatorSettings;
    }
}