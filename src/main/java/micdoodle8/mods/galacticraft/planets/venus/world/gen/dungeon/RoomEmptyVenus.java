package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_EMPTY;
import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_ENTRANCE;

public class RoomEmptyVenus extends SizedPieceVenus
{
    public RoomEmptyVenus(StructureManager templateManager, CompoundTag nbt)
    {
        this(CVENUS_DUNGEON_EMPTY, nbt);
    }

    public RoomEmptyVenus(StructurePieceType type, CompoundTag nbt)
    {
        super(type, nbt);
    }

    public RoomEmptyVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        this(CVENUS_DUNGEON_EMPTY, configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir.getOpposite());
    }

    public RoomEmptyVenus(StructurePieceType type, DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(type, configuration, sizeX, sizeY, sizeZ, entranceDir.getOpposite());
        this.setOrientation(Direction.SOUTH);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        int yPos = configuration.getYPosition();

        this.boundingBox = new BoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
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
                    if (i == 0 || i == this.sizeX || j == 0 || j == this.sizeY || k == 0 || k == this.sizeZ)
                    {
                        boolean placeBlock = true;
                        if (getDirection().getAxis() == Direction.Axis.Z)
                        {
                            int start = (this.boundingBox.x1 - this.boundingBox.x0) / 2 - 1;
                            int end = (this.boundingBox.x1 - this.boundingBox.x0) / 2 + 1;
                            if (i > start && i <= end && j < this.configuration.getHallwayHeight() && j > 0)
                            {
                                if (getDirection() == Direction.SOUTH && k == 0)
                                {
                                    placeBlock = false;
                                }
                                else if (getDirection() == Direction.NORTH && k == this.sizeZ)
                                {
                                    placeBlock = false;
                                }
                            }
                        }
                        else
                        {
                            int start = (this.boundingBox.z1 - this.boundingBox.z0) / 2 - 1;
                            int end = (this.boundingBox.z1 - this.boundingBox.z0) / 2 + 1;
                            if (k > start && k <= end && j < this.configuration.getHallwayHeight() && j > 0)
                            {
                                if (getDirection() == Direction.EAST && i == 0)
                                {
                                    placeBlock = false;
                                }
                                else if (getDirection() == Direction.WEST && i == this.sizeX)
                                {
                                    placeBlock = false;
                                }
                            }
                        }
                        if (placeBlock)
                        {
                            DungeonConfigurationVenus venusConfig = this.configuration;
                            this.placeBlock(worldIn, j == 0 || j == this.sizeY ? venusConfig.getBrickBlockFloor() : this.configuration.getBrickBlock(), i, j, k, boundingBox);
                        }
                        else
                        {
                            this.placeBlock(worldIn, Blocks.AIR.defaultBlockState(), i, j, k, boundingBox);
                        }
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
        if (Math.abs(startPiece.getBoundingBox().z1 - boundingBox.z0) > 200)
        {
            return null;
        }

        if (Math.abs(startPiece.getBoundingBox().x1 - boundingBox.x0) > 200)
        {
            return null;
        }

        return getCorridor(rand, startPiece, 10, false);
    }
}