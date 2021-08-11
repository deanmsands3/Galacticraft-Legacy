package micdoodle8.mods.galacticraft.planets.mars.world.gen.biome;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeProviderSpace;
import micdoodle8.mods.galacticraft.planets.GCPlanetsBiomes;
import net.minecraft.world.biome.Biome;

public class BiomeProviderMars extends BiomeProviderSpace
{
    @Override
    public Biome getBiome()
    {
        return GCPlanetsBiomes.MARS_FLAT;
    }
}
