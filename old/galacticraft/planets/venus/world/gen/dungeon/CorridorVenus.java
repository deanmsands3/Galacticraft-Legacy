package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import java.lang.reflect.Constructor;
import java.util.Random;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_CORRIDOR;

public class CorridorVenus extends SizedPieceVenus
{
    public CorridorVenus(StructureManager templateManager, CompoundTag nbt)
    {
        super(CVENUS_DUNGEON_CORRIDOR, nbt);
    }

    public CorridorVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction direction)
    {
        super(CVENUS_DUNGEON_CORRIDOR, configuration, sizeX, sizeY, sizeZ, direction);
        this.setOrientation(Direction.SOUTH);
        this.boundingBox = new BoundingBox(blockPosX, configuration.getYPosition(), blockPosZ, blockPosX + sizeX, configuration.getYPosition() + sizeY, blockPosZ + sizeZ);
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
                    if (j == 2 && this.getDirection().getAxis() == Direction.Axis.Z && (k + 1) % 4 == 0 && k != this.boundingBox.getZSpan() - 1)
                    {
                        if (i == 0 || i == this.boundingBox.getXSpan() - 1)
                        {
                            this.placeBlock(worldIn, Blocks.LAVA.defaultBlockState(), i, j, k, this.boundingBox);
                        }
                        else if (i == 1 || i == this.boundingBox.getXSpan() - 2)
                        {
                            this.placeBlock(worldIn, Blocks.IRON_BARS.defaultBlockState(), i, j, k, this.boundingBox);
                        }
                        else
                        {
                            this.placeBlock(worldIn, Blocks.AIR.defaultBlockState(), i, j, k, this.boundingBox);
                        }
                    }
                    else if (j == 2 && this.getDirection().getAxis() == Direction.Axis.X && (i + 1) % 4 == 0 && i != this.boundingBox.getXSpan() - 1)
                    {
                        if (k == 0 || k == this.boundingBox.getZSpan() - 1)
                        {
                            this.placeBlock(worldIn, Blocks.LAVA.defaultBlockState(), i, j, k, this.boundingBox);
                        }
                        else if (k == 1 || k == this.boundingBox.getZSpan() - 2)
                        {
                            this.placeBlock(worldIn, Blocks.IRON_BARS.defaultBlockState(), i, j, k, this.boundingBox);
                        }
                        else
                        {
                            this.placeBlock(worldIn, Blocks.AIR.defaultBlockState(), i, j, k, this.boundingBox);
                        }
                    }
                    else if ((this.getDirection().getAxis() == Direction.Axis.Z && (i == 1 || i == this.boundingBox.getXSpan() - 2)) ||
                            j == 0 || j == this.boundingBox.getYSpan() - 1 ||
                            (this.getDirection().getAxis() == Direction.Axis.X && (k == 1 || k == this.boundingBox.getZSpan() - 2)))
                    {
                        DungeonConfigurationVenus venusConfig = this.configuration;
                        this.placeBlock(worldIn, j == 0 || j == this.boundingBox.getYSpan() - 1 ? venusConfig.getBrickBlockFloor() : this.configuration.getBrickBlock(), i, j, k, this.boundingBox);
                    }
                    else if ((this.getDirection().getAxis() == Direction.Axis.Z && (i == 0 || i == this.boundingBox.getXSpan() - 1)) ||
                            (this.getDirection().getAxis() == Direction.Axis.X && (k == 0 || k == this.boundingBox.getZSpan() - 1)))
                    {
                        DungeonConfigurationVenus venusConfig = this.configuration;
                        this.placeBlock(worldIn, j == 0 || j == this.boundingBox.getYSpan() - 1 ? venusConfig.getBrickBlockFloor() : this.configuration.getBrickBlock(), i, j, k, this.boundingBox);
                    }
                    else
                    {
                        this.placeBlock(worldIn, Blocks.AIR.defaultBlockState(), i, j, k, this.boundingBox);
                    }
                }
            }
        }

        return true;
    }

    private <T extends SizedPieceVenus> T getRoom(Class<?> clazz, DungeonStartVenus startPiece, Random rand)
    {
        try
        {
            Constructor<?> c0 = clazz.getConstructor(DungeonConfigurationVenus.class, Random.class, Integer.TYPE, Integer.TYPE, Direction.class);
            T dummy = (T) c0.newInstance(this.configuration, rand, 0, 0, this.getDirection().getOpposite());
            BoundingBox extension = getExtension(this.getDirection(), getDirection().getAxis() == Direction.Axis.X ? dummy.getSizeX() : dummy.getSizeZ(), getDirection().getAxis() == Direction.Axis.X ? dummy.getSizeZ() : dummy.getSizeX());
            if (startPiece.checkIntersection(extension))
            {
                return null;
            }
            int sizeX = extension.x1 - extension.x0;
            int sizeZ = extension.z1 - extension.z0;
            int sizeY = dummy.getSizeY();
            int blockX = extension.x0;
            int blockZ = extension.z0;
            Constructor<?> c1 = clazz.getConstructor(DungeonConfigurationVenus.class, Random.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Direction.class);
            return (T) c1.newInstance(this.configuration, rand, blockX, blockZ, sizeX, sizeY, sizeZ, this.getDirection().getOpposite());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public PieceVenus getNextPiece(DungeonStartVenus startPiece, Random rand)
    {
        int pieceCount = startPiece.attachedComponents.size();
        if (pieceCount > 10 && startPiece.attachedComponents.get(pieceCount - 2) instanceof RoomBossVenus)
        {
            try
            {
                return getRoom(this.configuration.getTreasureRoom(), startPiece, rand);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            int bossRoomChance = Math.max((int) (20.0 / (pieceCount - 10)), 1);
            boolean bossRoom = pieceCount > 25 || (pieceCount > 10 && rand.nextInt(bossRoomChance) == 0);
            if (bossRoom)
            {
                try
                {
                    return getRoom(this.configuration.getBossRoom(), startPiece, rand);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return null;
                }
            }
            else
            {
                BoundingBox extension = getExtension(this.getDirection(), rand.nextInt(4) + 6, rand.nextInt(4) + 6);

                if (startPiece.checkIntersection(extension))
                {
                    return null;
                }

                int sizeX = extension.x1 - extension.x0;
                int sizeZ = extension.z1 - extension.z0;
                int sizeY = configuration.getRoomHeight();
                int blockX = extension.x0;
                int blockZ = extension.z0;

                if (Math.abs(startPiece.getBoundingBox().z1 - boundingBox.z0) > 200)
                {
                    return null;
                }

                if (Math.abs(startPiece.getBoundingBox().x1 - boundingBox.x0) > 200)
                {
                    return null;
                }

                PieceVenus lastPiece = pieceCount <= 2 ? null : (PieceVenus) startPiece.attachedComponents.get(pieceCount - 2);

                if (!(lastPiece instanceof RoomSpawnerVenus))
                {
                    return new RoomSpawnerVenus(this.configuration, rand, blockX, blockZ, sizeX, sizeY, sizeZ, this.getDirection().getOpposite());
                }
                else
                {
                    if (rand.nextInt(2) == 0)
                    {
                        return new RoomEmptyVenus(this.configuration, rand, blockX, blockZ, sizeX, sizeY, sizeZ, this.getDirection().getOpposite());
                    }
                    else
                    {
                        return new RoomChestVenus(this.configuration, rand, blockX, blockZ, sizeX, sizeY, sizeZ, this.getDirection().getOpposite());
                    }
                }
            }

        }

        return null;
    }
}