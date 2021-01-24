package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.RoomBoss;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.mars.world.gen.MarsFeatures.CMARS_DUNGEON_BOSS;

public class RoomBossMars extends RoomBoss
{
    public RoomBossMars(StructureManager templateManager, CompoundTag nbt)
    {
        super(CMARS_DUNGEON_BOSS, nbt);
    }

    public RoomBossMars(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    public RoomBossMars(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, Direction entranceDir)
    {
        super(configuration, rand, blockPosX, blockPosZ, 24, 11, 24, entranceDir);
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
                    if (i == 0 || i == this.sizeX || j == 0 || k == 0 || k == this.sizeZ)
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
                    else if (j == this.sizeY)
                    {
                        if ((i <= 2 || k <= 2 || i >= this.sizeX - 2 || k >= this.sizeZ - 2) && randomIn.nextInt(4) == 0)
                        {
                            this.placeBlock(worldIn, Blocks.GLOWSTONE.defaultBlockState(), i, j, k, mutableBoundingBoxIn);
                        }
                        else
                        {
                            this.placeBlock(worldIn, this.configuration.getBrickBlock(), i, j, k, mutableBoundingBoxIn);
                        }
                    }
                    else if (j == 1 && (i <= 2 || k <= 2 || i >= this.sizeX - 2 || k >= this.sizeZ - 2) && randomIn.nextInt(6) == 0)
                    {
                        this.placeBlock(worldIn, MarsBlocks.CREEPER_EGG.defaultBlockState(), i, j, k, mutableBoundingBoxIn);
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
            worldIn.setBlock(blockpos, MarsBlocks.MARS_BOSS_SPAWNER.defaultBlockState(), 2);
            TileEntityDungeonSpawner spawner = (TileEntityDungeonSpawner) worldIn.getBlockEntity(blockpos);
            if (spawner != null)
            {
                spawner.setRoom(new Vector3(this.boundingBox.x0 + 1, this.boundingBox.y0 + 1, this.boundingBox.z0 + 1), new Vector3(this.sizeX - 1, this.sizeY - 1, this.sizeZ - 1));
            }
        }

        return true;
    }
}
