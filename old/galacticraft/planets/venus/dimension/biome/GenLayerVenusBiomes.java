package micdoodle8.mods.galacticraft.planets.venus.dimension.biome;

import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer0;

public enum GenLayerVenusBiomes implements AreaTransformer0
{
    INSTANCE;

    private static final Biome[] biomes = new Biome[] { BiomeVenus.venusFlat, BiomeVenus.venusValley, BiomeVenus.venusMountain };

    @Override
    public int applyPixel(Context noise, int x, int y)
    {
        return Registry.BIOME.getId(biomes[noise.nextRandom(biomes.length)]);
    }
}
