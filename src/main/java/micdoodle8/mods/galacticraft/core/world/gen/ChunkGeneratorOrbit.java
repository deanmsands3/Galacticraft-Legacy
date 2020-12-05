package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.dimension.chunk.OrbitGenSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome.SpawnerData;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.List;

public class ChunkGeneratorOrbit extends ChunkGenerator<OrbitGenSettings>
{
    private final WorldGenSpaceStation spaceStation = new WorldGenSpaceStation();
//    private final Random rand;
//    private final World world;

    public ChunkGeneratorOrbit(LevelAccessor worldIn, BiomeSource dimension, OrbitGenSettings settingsIn)
    {
        super(worldIn, dimension, settingsIn);
//        this.rand = new Random(par2);
//        this.world = par1World;
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion region, ChunkAccess chunk)
    {
    }

    @Override
    public int getSpawnHeight()
    {
        return this.level.getSeaLevel() + 1;
    }

    @Override
    public void fillFromNoise(LevelAccessor worldIn, ChunkAccess chunkIn)
    {
        ChunkPos chunkPos = chunkIn.getPos();
        if (chunkPos.x == 0 && chunkPos.z == 0)
        {
            spaceStation.place(chunkIn, new BlockPos(0, 62, 0));
        }
    }

    @Override
    public int getBaseHeight(int p_222529_1_, int p_222529_2_, Heightmap.Types p_222529_3_)
    {
        return 0;
    }

    //    @Override
//    public Chunk generateChunk(int par1, int par2)
//    {
//        ChunkPrimer chunkprimer = new ChunkPrimer();
//        this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
//
//        final Chunk var4 = new Chunk(this.world, chunkprimer, par1, par2);
//
//        final byte b = (byte) Biome.getIdForBiome( BiomeAdaptive.biomeDefault );
//        final byte[] biomesArray = var4.getBiomeArray();
//        for (int i = 0; i < biomesArray.length; ++i)
//        {
//            biomesArray[i] = b;
//        }
//
//        var4.generateSkylightMap();
//        return var4;
//    }
//
//    @Override
//    public void populate(int x, int z)
//    {
//        FallingBlock.fallInstantly = true;
//        final int k = x * 16;
//        final int l = z * 16;
//        this.rand.setSeed(this.world.getSeed());
//        final long i1 = this.rand.nextLong() / 2L * 2L + 1L;
//        final long j1 = this.rand.nextLong() / 2L * 2L + 1L;
//        this.rand.setSeed(x * i1 + z * j1 ^ this.world.getSeed());
//        if (k == 0 && l == 0)
//        {
//            BlockPos pos = new BlockPos(k, 64, l);
//            this.world.setBlockState(pos, GCBlocks.spaceStationBase.getDefaultState(), 2);
//
//            final TileEntity var8 = this.world.getTileEntity(pos);
//
//            if (var8 instanceof IMultiBlock)
//            {
//                ((IMultiBlock) var8).onCreate(this.world, pos);
//            }
//
//            new WorldGenSpaceStation().generate(this.world, this.rand, new BlockPos(k - 10, 62, l - 3));
//        }
//        FallingBlock.fallInstantly = false;
//    }

    @Override
    public List<SpawnerData> getMobsAt(MobCategory creatureType, BlockPos pos)
    {
        return null;
    }

//    @Override
//    public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_)
//    {
//    }
}
