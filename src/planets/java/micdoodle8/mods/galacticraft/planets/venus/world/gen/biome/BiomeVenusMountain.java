package micdoodle8.mods.galacticraft.planets.venus.world.gen.biome;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.world.GalacticraftBiome.BiomeData.DataBuilder;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockBasicVenus;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeVenusMountain extends BiomeVenus
{
    public BiomeVenusMountain(DataBuilder dataBuilder) {
		super(dataBuilder);

	}

    @Override
    public void generateTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        this.fillerBlock = VenusBlocks.venusBlock.getDefaultState().withProperty(BlockBasicVenus.BASIC_TYPE_VENUS, BlockBasicVenus.EnumBlockBasicVenus.ROCK_SOFT);
        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
}
