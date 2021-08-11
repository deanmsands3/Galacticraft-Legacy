package micdoodle8.mods.galacticraft.planets.mars.world.gen.biome;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.world.GalacticraftBiome;
import micdoodle8.mods.galacticraft.api.world.GalacticraftBiome.BiomeData.DataBuilder;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockBasicMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeMars extends GalacticraftBiome
{
    public BiomeMars(DataBuilder dataBuilder)
    {
        super(dataBuilder.build(), true);
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }

    @Override
    public void generateTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        this.fillerBlock = MarsBlocks.marsBlock.getDefaultState().withProperty(BlockBasicMars.BASIC_TYPE, BlockBasicMars.EnumBlockBasic.MARS_STONE);
        this.topBlock = MarsBlocks.marsBlock.getDefaultState().withProperty(BlockBasicMars.BASIC_TYPE, BlockBasicMars.EnumBlockBasic.SURFACE);
        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
}
