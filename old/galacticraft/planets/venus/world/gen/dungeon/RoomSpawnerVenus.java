package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
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

import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_ENTRANCE;
import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_SPAWNER;

public class RoomSpawnerVenus extends RoomEmptyVenus
{
    public RoomSpawnerVenus(StructureManager templateManager, CompoundTag nbt)
    {
        super(CVENUS_DUNGEON_SPAWNER, nbt);
    }

    public RoomSpawnerVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(CVENUS_DUNGEON_SPAWNER, configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
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

            this.placeMobSpawner(worldIn, randomIn, mutableBoundingBoxIn, 1, 0, 1);
            this.placeMobSpawner(worldIn, randomIn, mutableBoundingBoxIn, this.sizeX - 1, 0, this.sizeZ - 1);

            return true;
        }

        return false;
    }

    private void placeMobSpawner(LevelAccessor worldIn, Random random, BoundingBox chunkBox, int x, int y, int z)
    {
        this.placeBlock(worldIn, Blocks.SPAWNER.defaultBlockState(), 1, 0, 1, boundingBox);
        BlockPos blockpos = new BlockPos(this.getWorldX(1, 1), this.getWorldY(0), this.getWorldZ(1, 1));
        SpawnerBlockEntity spawner = (SpawnerBlockEntity) worldIn.getBlockEntity(blockpos);

        if (spawner != null)
        {
            spawner.getSpawner().setEntityId(getMob(random));
        }
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