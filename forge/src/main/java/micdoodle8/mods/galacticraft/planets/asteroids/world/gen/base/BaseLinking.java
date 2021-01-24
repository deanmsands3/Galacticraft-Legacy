package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import java.util.Random;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import static micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidFeatures.CBASE_LINKING;

public class BaseLinking extends SizedPiece
{
    public BaseLinking(StructureManager templateManager, CompoundTag nbt)
    {
        super(CBASE_LINKING, nbt);
    }

    public BaseLinking(BaseConfiguration configuration, Random rand, int blockPosX, int blockPosY, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction direction)
    {
        super(CBASE_LINKING, configuration, sizeX, sizeY, sizeZ, direction);
        this.setOrientation(Direction.SOUTH);
        this.boundingBox = new BoundingBox(blockPosX, blockPosY, blockPosZ, blockPosX + sizeX, blockPosY + sizeY, blockPosZ + sizeZ);
    }

    @Override
    public boolean postProcess(LevelAccessor worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, BoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        for (int i = 0; i < this.boundingBox.getXSpan(); i++)
        {
            for (int j = 0; j < this.boundingBox.getYSpan(); j++)
            {
                for (int k = 0; k < this.boundingBox.getZSpan(); k++)
                {
                    if ((this.getDirection().getAxis() == Direction.Axis.Z && (i == 0 || i == this.boundingBox.getXSpan() - 1)) ||
                            j == 0 || j == this.boundingBox.getYSpan() - 1 ||
                            (this.getDirection().getAxis() == Direction.Axis.X && (k == 0 || k == this.boundingBox.getZSpan() - 1)))
                    {
                        this.placeBlock(worldIn, this.configuration.getWallBlock(), i, j, k, this.boundingBox);
                    }
                    else
                    {
                        if (j == this.boundingBox.getYSpan() - 2)
                        {
                            if (this.getDirection().getAxis() == Direction.Axis.Z && (k + 1) % 4 == 0 && (i == 1 || i == this.boundingBox.getXSpan() - 2))
                            {
                                //TODO: windows or decor
//                                this.setBlockState(worldIn, GCBlocks.unlitTorch.getDefaultState().with(BlockUnlitTorch.FACING, i == 1 ? EnumFacing.WEST.getOpposite() : EnumFacing.EAST.getOpposite()), i, j, k, this.boundingBox);
                                continue;
                            }
                            else if (this.getDirection().getAxis() == Direction.Axis.X && (i + 1) % 4 == 0 && (k == 1 || k == this.boundingBox.getZSpan() - 2))
                            {
//                                this.setBlockState(worldIn, GCBlocks.unlitTorch.getDefaultState().with(BlockUnlitTorch.FACING, k == 1 ? EnumFacing.NORTH.getOpposite() : EnumFacing.SOUTH.getOpposite()), i, j, k, this.boundingBox);
                                continue;
                            }
                        }

                        this.placeBlock(worldIn, Blocks.AIR.defaultBlockState(), i, j, k, this.boundingBox);
                    }
                }
            }
        }

        return true;
    }
}