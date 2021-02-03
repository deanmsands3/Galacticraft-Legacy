package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import net.minecraft.core.BlockPos;
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

import static micdoodle8.mods.galacticraft.core.world.gen.GCFeatures.CMOON_DUNGEON_BOSS;

public class RoomBoss extends SizedPiece
{
    private BlockPos chestPos;

    public RoomBoss(StructureManager templateManager, CompoundTag nbt)
    {
        super(CMOON_DUNGEON_BOSS, nbt);
    }

    protected RoomBoss(StructurePieceType type, CompoundTag nbt)
    {
        super(type, nbt);
    }

    public RoomBoss(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, Direction entranceDir)
    {
        this(configuration, rand, blockPosX, blockPosZ, rand.nextInt(6) + 14, rand.nextInt(2) + 8, rand.nextInt(6) + 14, entranceDir);
    }

    public RoomBoss(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(CMOON_DUNGEON_BOSS, configuration, sizeX, sizeY, sizeZ, entranceDir.getOpposite());
        this.setOrientation(Direction.SOUTH);
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
        this.sizeY = sizeY;
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
                            if (i > start && i <= end && j < 3 && j > 0)
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
                            if (k > start && k <= end && j < 3 && j > 0)
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
                            this.placeBlock(worldIn, this.configuration.getBrickBlock(), i, j, k, mutableBoundingBoxIn);
                        }
                        else
                        {
                            this.placeBlock(worldIn, Blocks.AIR.defaultBlockState(), i, j, k, mutableBoundingBoxIn);
                        }
                    }
                    else if ((i == 1 && k == 1) || (i == 1 && k == this.sizeZ - 1) || (i == this.sizeX - 1 && k == 1) || (i == this.sizeX - 1 && k == this.sizeZ - 1))
                    {
                        this.placeBlock(worldIn, Blocks.LAVA.defaultBlockState(), i, j, k, mutableBoundingBoxIn);
                    }
                    else if (j % 3 == 0 && j >= 2 && ((i == 1 || i == this.sizeX - 1 || k == 1 || k == this.sizeZ - 1) || (i == 2 && k == 2) || (i == 2 && k == this.sizeZ - 2) || (i == this.sizeX - 2 && k == 2) || (i == this.sizeX - 2 && k == this.sizeZ - 2)))
                    {
                        // Horizontal bars
                        this.placeBlock(worldIn, Blocks.IRON_BARS.defaultBlockState(), i, j, k, mutableBoundingBoxIn);
                    }
                    else if ((i == 1 && k == 2) || (i == 2 && k == 1) ||
                            (i == 1 && k == this.sizeZ - 2) || (i == 2 && k == this.sizeZ - 1) ||
                            (i == this.sizeX - 1 && k == 2) || (i == this.sizeX - 2 && k == 1) ||
                            (i == this.sizeX - 1 && k == this.sizeZ - 2) || (i == this.sizeX - 2 && k == this.sizeZ - 1))
                    {
                        // Vertical bars
                        this.placeBlock(worldIn, Blocks.IRON_BARS.defaultBlockState(), i, j, k, mutableBoundingBoxIn);
                    }
                    else
                    {
                        this.placeBlock(worldIn, Blocks.AIR.defaultBlockState(), i, j, k, mutableBoundingBoxIn);
                    }
                }
            }
        }

        int spawnerX = this.sizeX / 2;
        int spawnerY = 1;
        int spawnerZ = this.sizeZ / 2;
        BlockPos blockpos = new BlockPos(this.getWorldX(spawnerX, spawnerZ), this.getWorldY(spawnerY), this.getWorldZ(spawnerX, spawnerZ));
        //Is this position inside the chunk currently being generated?
        if (mutableBoundingBoxIn.isInside(blockpos))
        {
            worldIn.setBlock(blockpos, GCBlocks.MOON_BOSS_SPAWNER.defaultBlockState(), 2);
            TileEntityDungeonSpawner spawner = (TileEntityDungeonSpawner) worldIn.getBlockEntity(blockpos);
            if (spawner != null)
            {
                spawner.setRoom(new Vector3(this.boundingBox.x0 + 1, this.boundingBox.y0 + 1, this.boundingBox.z0 + 1), new Vector3(this.sizeX - 1, this.sizeY - 1, this.sizeZ - 1));
                spawner.setChestPos(this.chestPos);
            }
        }

        return true;
    }

    @Override
    protected void writeStructureToNBT(CompoundTag tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        if (this.chestPos != null)
        {
            tagCompound.putInt("chestX", this.chestPos.getX());
            tagCompound.putInt("chestY", this.chestPos.getY());
            tagCompound.putInt("chestZ", this.chestPos.getZ());
        }
    }

    @Override
    protected void readStructureFromNBT(CompoundTag nbt)
    {
        super.readStructureFromNBT(nbt);
        this.chestPos = new BlockPos(nbt.getInt("chestX"), nbt.getInt("chestY"), nbt.getInt("chestZ"));
    }

    @Override
    public Piece getNextPiece(DungeonStart startPiece, Random rand)
    {
        return getCorridor(rand, startPiece, 10, true);
    }

    public BlockPos getChestPos()
    {
        return chestPos;
    }

    public void setChestPos(BlockPos chestPos)
    {
        this.chestPos = chestPos;
    }
}