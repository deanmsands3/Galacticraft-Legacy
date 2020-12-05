package micdoodle8.mods.galacticraft.planets.venus.dimension.biome;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

public class BiomeVenus extends BiomeGC
{
    public static final Biome venusFlat = new BiomeVenus((new Biome.BiomeBuilder()).surfaceBuilder(SurfaceBuilder.DEFAULT, new SurfaceBuilderBaseConfiguration(VenusBlocks.rockHard.defaultBlockState(), VenusBlocks.rockSoft.defaultBlockState(), VenusBlocks.rockSoft.defaultBlockState())).precipitation(Biome.Precipitation.NONE).biomeCategory(BiomeCategory.NONE).depth(0.5F).scale(0.4F).temperature(4.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null));
    public static final Biome venusMountain = new BiomeVenus((new Biome.BiomeBuilder()).surfaceBuilder(SurfaceBuilder.DEFAULT, new SurfaceBuilderBaseConfiguration(VenusBlocks.rockHard.defaultBlockState(), VenusBlocks.rockSoft.defaultBlockState(), VenusBlocks.rockSoft.defaultBlockState())).precipitation(Biome.Precipitation.NONE).biomeCategory(BiomeCategory.NONE).depth(2.0F).scale(1.0F).temperature(4.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null));
    public static final Biome venusValley = new BiomeVenus((new Biome.BiomeBuilder()).surfaceBuilder(SurfaceBuilder.DEFAULT, new SurfaceBuilderBaseConfiguration(VenusBlocks.rockHard.defaultBlockState(), VenusBlocks.rockSoft.defaultBlockState(), VenusBlocks.rockSoft.defaultBlockState())).precipitation(Biome.Precipitation.NONE).biomeCategory(BiomeCategory.NONE).depth(-0.4F).scale(0.2F).temperature(4.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null));

    BiomeVenus(Biome.BiomeBuilder biomeBuilder)
    {
        super(biomeBuilder, true);
    }

//    @Override
//    public BiomeDecorator createBiomeDecorator()
//    {
//        return new BiomeDecoratorVenus();
//    }
//
//    @Override
//    public float getSpawningChance()
//    {
//        return 0.01F;
//    }
//
//    @Override
//    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180622_4_, int p_180622_5_, double p_180622_6_)
//    {
//    }
//
//    public final void generateBiomeTerrainVenus(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180628_4_, int p_180628_5_, double p_180628_6_)
//    {
//        int i = worldIn.getSeaLevel();
//        BlockState topBlock = this.topBlock;
//        BlockState fillerBlock = this.fillerBlock;
//        BlockState stoneBlock = VenusBlocks.venusBlock.getDefaultState().with(BlockVenusRock.BASIC_TYPE_VENUS, BlockVenusRock.EnumBlockBasicVenus.ROCK_HARD);
//        int j = -1;
//        int k = (int)(p_180628_6_ / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
//        int l = p_180628_4_ & 15;
//        int i1 = p_180628_5_ & 15;
//
//        for (int j1 = 255; j1 >= 0; --j1)
//        {
//            if (j1 <= rand.nextInt(5))
//            {
//                chunkPrimerIn.setBlockState(i1, j1, l, Blocks.BEDROCK.getDefaultState());
//            }
//            else
//            {
//                BlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);
//
//                if (iblockstate2.getMaterial() == Material.AIR)
//                {
//                    j = -1;
//                }
//                else if (iblockstate2.getBlock() == VenusBlocks.venusBlock)
//                {
//                    if (j == -1)
//                    {
//                        if (k <= 0)
//                        {
//                            topBlock = null;
//                            fillerBlock = stoneBlock;
//                        }
//                        else if (j1 >= i - 4 && j1 <= i + 1)
//                        {
//                            topBlock = this.topBlock;
//                            fillerBlock = this.fillerBlock;
//                        }
//
//                        j = k;
//
//                        if (j1 >= i - 1)
//                        {
//                            chunkPrimerIn.setBlockState(i1, j1, l, topBlock);
//                        }
//                        else if (j1 < i - 7 - k)
//                        {
//                            topBlock = null;
//                            fillerBlock = stoneBlock;
//                        }
//                        else
//                        {
//                            chunkPrimerIn.setBlockState(i1, j1, l, fillerBlock);
//                        }
//                    }
//                    else if (j > 0)
//                    {
//                        --j;
//                        chunkPrimerIn.setBlockState(i1, j1, l, fillerBlock);
//                    }
//                }
//            }
//        }
//    }
}
