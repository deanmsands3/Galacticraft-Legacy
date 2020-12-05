package micdoodle8.mods.galacticraft.planets.mars.dimension.biome;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

public class BiomeMars extends BiomeGC
{
    public static final BiomeMars marsFlat = new BiomeMars();
    //    public static final Biome moonFlat = new BiomeFlatMoon(new BiomeProperties("Moon").setBaseHeight(1.5F).setHeightVariation(0.4F).setRainfall(0.0F));

    BiomeMars()
    {
        super((new Biome.BiomeBuilder()).surfaceBuilder(SurfaceBuilder.DEFAULT, new SurfaceBuilderBaseConfiguration(MarsBlocks.rockSurface.defaultBlockState(), MarsBlocks.rockMiddle.defaultBlockState(), MarsBlocks.rockMiddle.defaultBlockState())).precipitation(Biome.Precipitation.NONE).biomeCategory(BiomeCategory.NONE).depth(2.5F).scale(0.4F).temperature(0.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null), true);
    }

//    @Override
//    public BiomeDecorator createBiomeDecorator()
//    {
//        return getModdedBiomeDecorator(new BiomeDecoratorMoon());
//    }

    @Override
    public float getCreatureProbability()
    {
        return 0.1F;
    }

//    @Override
//    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
//    {
//        this.fillerBlock = MoonChunkGenerator.BLOCK_LOWER;
//        this.topBlock = MoonChunkGenerator.BLOCK_TOP;
//        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
//    }
}

