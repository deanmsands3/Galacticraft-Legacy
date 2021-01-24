package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockTorchWeb;
import java.util.Random;

import static micdoodle8.mods.galacticraft.core.world.gen.GCFeatures.CMOON_DUNGEON_BOSS;
import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_BOSS;

public class RoomBossVenus extends SizedPieceVenus
{
    private Direction exitDirection;
    private BlockPos chestPos;

    public RoomBossVenus(StructureManager templateManager, CompoundTag nbt)
    {
        super(CVENUS_DUNGEON_BOSS, nbt);
    }

    public RoomBossVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, Direction entranceDir)
    {
        this(configuration, rand, blockPosX, blockPosZ, rand.nextInt(6) + 20, rand.nextInt(2) + 10, rand.nextInt(6) + 20, entranceDir);
    }

    public RoomBossVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(CVENUS_DUNGEON_BOSS, configuration, sizeX, sizeY, sizeZ, entranceDir.getOpposite());
        this.setOrientation(Direction.SOUTH);
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
        this.sizeY = sizeY;
        int yPos = configuration.getYPosition();

        this.boundingBox = new BoundingBox(blockPosX, yPos - 2, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
    }

    @Override
    protected void writeStructureToNBT(CompoundTag tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        if (this.exitDirection != null)
        {
            tagCompound.putInt("direction_exit", this.exitDirection.ordinal());
        }

        tagCompound.putBoolean("chestPosNull", this.chestPos == null);
        if (this.chestPos != null)
        {
            tagCompound.putInt("chestX", this.chestPos.getX());
            tagCompound.putInt("chestY", this.chestPos.getY());
            tagCompound.putInt("chestZ", this.chestPos.getZ());
        }
    }

    @Override
    protected void readStructureFromNBT(CompoundTag tagCompound)
    {
        super.readStructureFromNBT(tagCompound);

        if (tagCompound.contains("direction_exit"))
        {
            this.exitDirection = Direction.from3DDataValue(tagCompound.getInt("direction_exit"));
        }
        else
        {
            this.exitDirection = null;
        }

        if (tagCompound.contains("chestPosNull") && !tagCompound.getBoolean("chestPosNull"))
        {
            this.chestPos = new BlockPos(tagCompound.getInt("chestX"), tagCompound.getInt("chestY"), tagCompound.getInt("chestZ"));
        }
    }

    @Override
    public boolean postProcess(LevelAccessor worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, BoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        BoundingBox box = new BoundingBox(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE});

        for (int i = 0; i <= this.sizeX; i++)
        {
            for (int j = 0; j <= this.sizeY; j++)
            {
                for (int k = 0; k <= this.sizeZ; k++)
                {
                    double dX = (i - this.sizeX / 2);
                    double dZ = (k - this.sizeZ / 2);
                    double dXZ = Math.sqrt(dX * dX + dZ * dZ);

                    double f = -Math.pow((dXZ * 1.5) / (this.sizeX / 2 - 1), 6) + this.sizeY - 1;

                    if (j == 0)
                    {
                        this.placeBlock(worldIn, this.configuration.getBrickBlockFloor(), i, j, k, mutableBoundingBoxIn);
                    }
                    else if (j < f)
                    {
                        this.placeBlock(worldIn, Blocks.AIR.defaultBlockState(), i, j, k, mutableBoundingBoxIn);

                        if (j + 1 >= f && (dXZ > 5) && randomIn.nextInt(12) == 0)
                        {
                            int distFromFloor = randomIn.nextInt(5) + 2;
                            for (int j0 = j; j0 >= distFromFloor + 1; --j0)
                            {
                                Block block;
                                if (j0 == distFromFloor + 1)
                                {
                                    block = VenusBlocks.WEB_TORCH;
                                }
                                else
                                {
                                    block = VenusBlocks.WEB_STRING;
                                }
                                this.placeBlock(worldIn, block.defaultBlockState(), i, j0, k, mutableBoundingBoxIn);
                            }
                        }

                        if (i < box.x0)
                        {
                            box.x0 = i;
                        }
                        if (i > box.x1)
                        {
                            box.x1 = i;
                        }
                        if (j < box.y0)
                        {
                            box.y0 = j;
                        }
                        if (j > box.y1)
                        {
                            box.y1 = j;
                        }
                        if (k < box.z0)
                        {
                            box.z0 = k;
                        }
                        if (k > box.z1)
                        {
                            box.z1 = k;
                        }
                    }
                    else
                    {
                        boolean placeBlock = true;

                        int start = (this.boundingBox.x1 - this.boundingBox.x0) / 2 - 1;
                        int end = (this.boundingBox.x1 - this.boundingBox.x0) / 2 + 1;
                        if (i > start && i <= end && j < 5 && j > 2)
                        {
                            if ((getDirection() == Direction.SOUTH || (this.exitDirection != null && this.exitDirection == Direction.SOUTH)) && k < 7)
                            {
                                placeBlock = false;
                            }
                            if ((getDirection() == Direction.NORTH || (this.exitDirection != null && this.exitDirection == Direction.NORTH)) && k > this.sizeZ - 7)
                            {
                                placeBlock = false;
                            }
                        }

                        start = (this.boundingBox.z1 - this.boundingBox.z0) / 2 - 1;
                        end = (this.boundingBox.z1 - this.boundingBox.z0) / 2 + 1;
                        if (k > start && k <= end && j < 5 && j > 2)
                        {
                            if ((getDirection() == Direction.EAST || (this.exitDirection != null && this.exitDirection == Direction.EAST)) && i < 7)
                            {
                                placeBlock = false;
                            }
                            if ((getDirection() == Direction.WEST || (this.exitDirection != null && this.exitDirection == Direction.WEST)) && i > this.sizeX - 7)
                            {
                                placeBlock = false;
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
            worldIn.setBlock(blockpos, VenusBlocks.VENUS_BOSS_SPAWNER.defaultBlockState(), 2);
            TileEntityDungeonSpawner spawner = (TileEntityDungeonSpawner) worldIn.getBlockEntity(blockpos);
            if (spawner != null)
            {
                if (box.getXSpan() > 10000 || box.getYSpan() > 10000 || box.getZSpan() > 10000)
                {
                    GCLog.severe("Failed to set correct boss room size. This is a bug!");
                }
                else
                {
                    spawner.setRoom(new Vector3(box.x0 + this.boundingBox.x0, box.y0 + this.boundingBox.y0, box.z0 + this.boundingBox.z0), new Vector3(box.x1 - box.x0 + 1, box.y1 - box.y0 + 1, box.z1 - box.z0 + 1));
                    spawner.setChestPos(this.chestPos);
                }
            }
        }

        return true;
    }

    public BlockPos getChestPos()
    {
        return chestPos;
    }

    public void setChestPos(BlockPos chestPos)
    {
        this.chestPos = chestPos;
    }

    @Override
    public PieceVenus getNextPiece(DungeonStartVenus startPiece, Random rand)
    {
        DirectionalPieceVenus corridor = (DirectionalPieceVenus) getCorridor(rand, startPiece, 10, true);
        this.exitDirection = corridor == null ? null : corridor.getDirection().getOpposite();
        return corridor;
    }
}