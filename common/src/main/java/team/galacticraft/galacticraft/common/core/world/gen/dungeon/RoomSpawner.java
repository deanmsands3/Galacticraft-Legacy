package team.galacticraft.galacticraft.common.core.world.gen.dungeon;

import team.galacticraft.galacticraft.core.entities.GCEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import java.util.Random;

import static team.galacticraft.galacticraft.core.world.gen.GCFeatures.CMOON_DUNGEON_SPAWNER;

public class RoomSpawner extends RoomEmpty
{
    public RoomSpawner(StructureManager templateManager, CompoundTag nbt)
    {
        super(CMOON_DUNGEON_SPAWNER, nbt);
    }

    public RoomSpawner(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(CMOON_DUNGEON_SPAWNER, configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    @Override
    public boolean postProcess(LevelAccessor worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, BoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        if (super.postProcess(worldIn, chunkGeneratorIn, randomIn, mutableBoundingBoxIn, chunkPosIn))
        {
            for (int i = 1; i <= this.sizeX - 1; ++i)
            {
                for (int j = 1; j <= this.sizeY - 1; ++j)
                {
                    for (int k = 1; k <= this.sizeZ - 1; ++k)
                    {
                        if (randomIn.nextFloat() < 0.05F)
                        {
                            this.placeBlock(worldIn, Blocks.COBWEB.defaultBlockState(), i, j, k, boundingBox);
                        }
                    }
                }
            }

            this.placeBlock(worldIn, Blocks.SPAWNER.defaultBlockState(), 1, 0, 1, boundingBox);
            this.placeBlock(worldIn, Blocks.SPAWNER.defaultBlockState(), this.sizeX - 1, 0, this.sizeZ - 1, boundingBox);

            BlockPos blockpos = new BlockPos(this.getWorldX(1, 1), this.getWorldY(0), this.getWorldZ(1, 1));
            SpawnerBlockEntity spawner = (SpawnerBlockEntity) worldIn.getBlockEntity(blockpos);

            if (spawner != null)
            {
                spawner.getSpawner().setEntityId(getMob(randomIn));
            }

            blockpos = new BlockPos(this.getWorldX(this.sizeX - 1, this.sizeZ - 1), this.getWorldY(0), this.getWorldZ(this.sizeX - 1, this.sizeZ - 1));
            spawner = (SpawnerBlockEntity) worldIn.getBlockEntity(blockpos);

            if (spawner != null)
            {
                spawner.getSpawner().setEntityId(getMob(randomIn));
            }

            return true;
        }

        return false;
    }

    private static EntityType<?> getMob(Random rand)
    {
        switch (rand.nextInt(4))
        {
        case 0:
            return GCEntities.EVOLVED_SPIDER;
        case 1:
            return GCEntities.EVOLVED_CREEPER;
        case 2:
            return GCEntities.EVOLVED_SKELETON;
        case 3:
        default:
            return GCEntities.EVOLVED_ZOMBIE;
        }
    }
}