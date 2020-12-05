package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.world.gen.GCFeatures;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.ChunkGeneratorSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class MapGenDungeonVenus extends StructureFeature<DungeonConfigurationVenus>
{
//    private DungeonConfiguration configuration;

//    static
//    {
//        try
//        {
//            MapGenDungeon.initiateStructures();
//        }
//        catch (Throwable e)
//        {
//
//        }
//    }

    public MapGenDungeonVenus(Function<Dynamic<?>, ? extends DungeonConfigurationVenus> func)
    {
        super(func);
    }

    @Override
    public String getFeatureName()
    {
        return "GC_Dungeon_Venus";
    }

    @Override
    public StructureStartFactory getStartFactory()
    {
        return Start::new;
    }

    @Override
    public int getLookupRange()
    {
        return 12;
    }

    @Override
    public boolean isFeatureChunk(BiomeManager biomeManagerIn, ChunkGenerator<?> generatorIn, Random randIn, int chunkX, int chunkZ, Biome biomeIn)
    {
        long dungeonPos = getDungeonPosForCoords(generatorIn, chunkX, chunkZ, ((IGalacticraftDimension) generatorIn.level.getDimension()).getDungeonSpacing());
        int i = (int) (dungeonPos >> 32);
        int j = (int) dungeonPos;  //Java automatically gives the 32 least significant bits
        return i == chunkX && j == chunkZ;
    }

    public static long getDungeonPosForCoords(ChunkGenerator<?> generator, int chunkX, int chunkZ, int spacing)
    {
        final int numChunks = spacing / 16;
        if (chunkX < 0)
        {
            chunkX -= numChunks - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= numChunks - 1;
        }

        int k = chunkX / numChunks;
        int l = chunkZ / numChunks;
        long seed = (long) k * 341873128712L + (long) l * 132897987541L + generator.level.getLevelProperties().getSeed() + (long) (10387340 + generator.level.getDimension().getType().getRawId());
        Random random = new Random();
        random.setSeed(seed);
        k = k * numChunks + random.nextInt(numChunks);
        l = l * numChunks + random.nextInt(numChunks);
        return (((long) k) << 32) + l;
    }

    /**
     * This returns an angle between 0 and 360 degrees.  0 degrees means due North from the current (x, z) position
     * Only provides meaningful results in worlds with dungeon generation using this class!
     */
    public static float directionToNearestDungeon(ServerLevel world, double xpos, double zpos)
    {
        int spacing = ((IGalacticraftDimension) world.getDimension()).getDungeonSpacing();
        if (spacing == 0)
        {
            return 0F;
        }
        int x = Mth.floor(xpos);
        int z = Mth.floor(zpos);
        int quadrantX = x % spacing;
        int quadrantZ = z % spacing;
        int searchOffsetX = quadrantX / (spacing / 2);  //0 or 1
        int searchOffsetZ = quadrantZ / (spacing / 2);  //0 or 1
        double nearestX = 0;
        double nearestZ = 0;
        double nearestDistance = Double.MAX_VALUE;
        for (int cx = searchOffsetX - 1; cx < searchOffsetX + 1; cx++)
        {
            for (int cz = searchOffsetZ - 1; cz < searchOffsetZ + 1; cz++)
            {
                long dungeonPos = getDungeonPosForCoords(world.getChunkSource().getGenerator(), (x + cx * spacing) / 16, (z + cz * spacing) / 16, spacing);
                int i = 2 + (((int) (dungeonPos >> 32)) << 4);
                int j = 2 + (((int) dungeonPos) << 4);  //Java automatically gives the 32 least significant bits
                double oX = i - xpos;
                double oZ = j - zpos;
                double distanceSq = oX * oX + oZ * oZ;
                if (distanceSq < nearestDistance)
                {
                    nearestDistance = distanceSq;
                    nearestX = oX;
                    nearestZ = oZ;
                }
            }
        }

        return GCCoreUtil.getAngleForRelativePosition(nearestX, nearestZ);
    }

//    @Override
//    protected StructureStart getStructureStart(int chunkX, int chunkZ)
//    {
//        return new MapGenDungeon.Start(this.world, this.rand, chunkX, chunkZ, this.configuration);
//    }

    @Nullable
    @Override
    public BlockPos getNearestGeneratedFeature(Level worldIn, ChunkGenerator<? extends ChunkGeneratorSettings> chunkGenerator, BlockPos pos, int radius, boolean p_211405_5_)
    {
        return null;
    }

    public static class Start extends StructureStart
    {
        //        private DungeonConfigurationVenus configuration;
        DungeonStartVenus startPiece;

        public Start(StructureFeature<?> structure, int chunkX, int chunkZ, BoundingBox boundsIn, int referenceIn, long seed)
        {
            super(structure, chunkX, chunkZ, boundsIn, referenceIn, seed);
//            this.configuration = configuration;
        }

        @Override
        public void generatePieces(ChunkGenerator<?> generator, StructureManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
        {
            DungeonConfigurationVenus dungeonConfig = generator.getStructureConfiguration(biomeIn, VenusFeatures.VENUS_DUNGEON.get());
            startPiece = new DungeonStartVenus((Level) generator.level, dungeonConfig, random, (chunkX << 4) + 2, (chunkZ << 4) + 2);
            startPiece.addChildren(startPiece, this.pieces, random);
            List<StructurePiece> list = startPiece.attachedComponents;

            while (!list.isEmpty())
            {
                int i = random.nextInt(list.size());
                StructurePiece structurecomponent = list.remove(i);
                structurecomponent.addChildren(startPiece, this.pieces, random);
            }

            this.calculateBoundingBox();
        }
    }
}
