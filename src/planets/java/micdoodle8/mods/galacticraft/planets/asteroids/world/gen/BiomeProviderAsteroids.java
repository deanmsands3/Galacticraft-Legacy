package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeProviderSpace;
import micdoodle8.mods.galacticraft.planets.GCPlanetsBiomes;
import net.minecraft.world.biome.Biome;

public class BiomeProviderAsteroids extends BiomeProviderSpace
{
    @Override
    public Biome getBiome()
    {
        return GCPlanetsBiomes.ASTEROIDS;
    }
}
