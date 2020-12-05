package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_EMPTY;
import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_ENTRANCE;

public class RoomEntranceVenus extends SizedPieceVenus
{
    public RoomEntranceVenus(StructureManager templateManager, CompoundTag nbt)
    {
        super(CVENUS_DUNGEON_ENTRANCE, nbt);
    }

    public RoomEntranceVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ)
    {
        super(CVENUS_DUNGEON_ENTRANCE, configuration, rand.nextInt(4) + 6, 12, rand.nextInt(4) + 6, Direction.Plane.HORIZONTAL.getRandomDirection(rand));
        this.setOrientation(Direction.SOUTH);
        int sX = this.sizeX / 2;
        int sZ = this.sizeZ / 2;

        this.boundingBox = new BoundingBox(blockPosX - sX, configuration.getYPosition(), blockPosZ - sZ, blockPosX - sX + this.sizeX, configuration.getYPosition() + this.sizeY, blockPosZ - sZ + this.sizeZ);
    }

    @Override
    public boolean postProcess(LevelAccessor worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, BoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        for (int i = 0; i <= this.sizeX; i++)
        {
            for (int j = 0; j <= this.sizeY; j++)
            {
                for (int k = 0; k <= this.sizeZ; k++)
                {
                    if (i == 0 || i == this.sizeX || j == 0 /*|| j == this.sizeY*/ || k == 0 || k == this.sizeZ)
                    {
                        this.placeBlock(worldIn, this.configuration.getBrickBlock(), i, j, k, boundingBox);
                    }
                    else
                    {
                        this.placeBlock(worldIn, Blocks.AIR.defaultBlockState(), i, j, k, boundingBox);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public PieceVenus getNextPiece(DungeonStartVenus startPiece, Random rand)
    {
        return getCorridor(rand, startPiece, 10, false);
    }
}