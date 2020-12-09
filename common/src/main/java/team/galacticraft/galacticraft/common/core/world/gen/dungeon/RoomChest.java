package team.galacticraft.galacticraft.common.core.world.gen.dungeon;

import team.galacticraft.galacticraft.common.api.world.IGalacticraftDimension;
import team.galacticraft.galacticraft.core.blocks.BlockTier1TreasureChest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import java.util.Random;

import static team.galacticraft.galacticraft.core.world.gen.GCFeatures.CMOON_DUNGEON_CHEST;

public class RoomChest extends RoomEmpty
{
    public RoomChest(StructureManager templateManager, CompoundTag nbt)
    {
        super(CMOON_DUNGEON_CHEST, nbt);
    }

    public RoomChest(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(CMOON_DUNGEON_CHEST, configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    @Override
    public boolean postProcess(LevelAccessor worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, BoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        if (super.postProcess(worldIn, chunkGeneratorIn, randomIn, mutableBoundingBoxIn, chunkPosIn))
        {
            int chestX = this.sizeX / 2;
            int chestY = 1;
            int chestZ = this.sizeZ / 2;
            this.placeBlock(worldIn, Blocks.CHEST.defaultBlockState().setValue(BlockTier1TreasureChest.FACING, this.getDirection().getOpposite()), chestX, chestY, chestZ, boundingBox);

            BlockPos blockpos = new BlockPos(this.getWorldX(chestX, chestZ), this.getWorldY(chestY), this.getWorldZ(chestX, chestZ));
            ChestBlockEntity chest = (ChestBlockEntity) worldIn.getBlockEntity(blockpos);

            if (chest != null)
            {
                ResourceLocation chesttype = RoomTreasure.MOONCHEST;
                if (worldIn.getDimension() instanceof IGalacticraftDimension)
                {
                    chesttype = ((IGalacticraftDimension) worldIn.getDimension()).getDungeonChestType();
                }
                chest.setLootTable(chesttype, randomIn.nextLong());
            }

            return true;
        }

        return false;
    }
}