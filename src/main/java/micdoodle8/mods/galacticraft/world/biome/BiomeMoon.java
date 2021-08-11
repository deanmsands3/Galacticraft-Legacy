package micdoodle8.mods.galacticraft.world.biome;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import micdoodle8.mods.galacticraft.api.world.GalacticraftBiome;
import micdoodle8.mods.galacticraft.api.world.GalacticraftBiome.BiomeData.DataBuilder;
import micdoodle8.mods.galacticraft.world.gen.BiomeDecoratorMoon;
import micdoodle8.mods.galacticraft.world.gen.ChunkProviderMoon;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeMoon extends GalacticraftBiome
{
    public BiomeMoon(DataBuilder dataBuilder)
    {
        super(dataBuilder.build(), true);
    }
    
    @Override
    public BiomeDecorator createBiomeDecorator()
    {
        return getModdedBiomeDecorator(new BiomeDecoratorMoon());
    }

    @Override
    public float getSpawningChance()
    {
        return 0.1F;
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        this.fillerBlock = ChunkProviderMoon.BLOCK_LOWER;
        this.topBlock = ChunkProviderMoon.BLOCK_TOP;
        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
}
