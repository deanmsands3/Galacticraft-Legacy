package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class EntranceCrater extends SizedPiece
{
    private final int range = 16;

    public EntranceCrater(StructurePieceType type, CompoundTag nbt)
    {
        super(type, nbt);
    }

    public EntranceCrater(StructurePieceType type, Level world, DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ)
    {
        super(type, configuration, rand.nextInt(4) + 6, 12, rand.nextInt(4) + 6, Direction.Plane.HORIZONTAL.random(rand));
        this.setOrientation(Direction.SOUTH);

        this.boundingBox = new BoundingBox(blockPosX - range, configuration.getYPosition() + 11, blockPosZ - range, blockPosX + range, 150, blockPosZ + range);
    }

    @Override
    public boolean postProcess(LevelAccessor worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, BoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        BlockState block1;

        int maxLevel = 0;

        for (int i = -range; i <= range; i++)
        {
            for (int k = -range; k <= range; k++)
            {
                int j = 150;
                int x = this.getWorldX(i + range, k + range);
                int z = this.getWorldZ(i + range, k + range);

                while (j >= 0)
                {
                    j--;

                    int y = this.getWorldY(j);
                    BlockPos blockpos = new BlockPos(x, y, z);
                    Block block = worldIn.getBlockState(blockpos).getBlock();

                    if (Blocks.AIR != block)
                    {
                        break;
                    }
                }

                maxLevel = Math.max(maxLevel, j + 3);
            }
        }

        Mirror mirror = Mirror.NONE;
        Rotation rotation = Rotation.NONE;
        if (this.getOrientation() != null)
        {
            switch (this.getOrientation())
            {
            case SOUTH:
                mirror = Mirror.LEFT_RIGHT;
                break;
            case WEST:
                mirror = Mirror.LEFT_RIGHT;
                rotation = Rotation.CLOCKWISE_90;
                break;
            case EAST:
                rotation = Rotation.CLOCKWISE_90;
                break;
            default:
                break;
            }
        }

        for (int i = -range; i < range; i++)
        {
            for (int k = -range; k < range; k++)
            {
                final double xDev = i / 20D;
                final double zDev = k / 20D;
                final double distance = xDev * xDev + zDev * zDev;
                final int depth = (int) Math.abs(0.5 / (distance + .00001D));
                int helper = 0;
                for (int j = maxLevel; j > 1 && helper <= depth; j--)
                {
                    block1 = this.getBlock(worldIn, i + range, j, k + range, boundingBox);
//                    if (block1 == this.configuration.getBrickBlock() || j != this.sizeY)
                    {
                        BlockPos blockpos = new BlockPos(this.getWorldX(i + range, k + range), this.getWorldY(j), this.getWorldZ(i + range, k + range));
                        BlockState state = Blocks.AIR.defaultBlockState();

                        if (mirror != Mirror.NONE)
                        {
                            state = state.mirror(mirror);
                        }

                        if (rotation != Rotation.NONE)
                        {
                            state = state.rotate(rotation);
                        }

                        worldIn.setBlock(blockpos, state, 2);
//                        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), i + range, j, k + range, boundingBox);
                        helper++;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Piece getNextPiece(DungeonStart startPiece, Random rand)
    {
        return new RoomEntrance(this.configuration, rand, this.boundingBox.x0 + this.boundingBox.getXSpan() / 2, this.boundingBox.z0 + this.boundingBox.getZSpan() / 2);
    }
}
