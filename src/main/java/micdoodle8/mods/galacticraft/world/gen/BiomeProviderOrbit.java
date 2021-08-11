package micdoodle8.mods.galacticraft.world.gen;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeProviderSpace;
import micdoodle8.mods.galacticraft.world.biome.BiomeOrbit;
import micdoodle8.mods.galacticraft.world.biome.GCBiomes;
import net.minecraft.world.biome.Biome;

public class BiomeProviderOrbit extends BiomeProviderSpace
{
    @Override
    public Biome getBiome()
    {
        return GCBiomes.ORBIT;
    }
}
