package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidFeatures.CBASE_PLATE;
import static micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidFeatures.CBASE_START;

public class BasePlate extends SizedPiece
{
    public BasePlate(StructureManager templateManager, CompoundTag nbt)
    {
        super(CBASE_PLATE, nbt);
    }

    public BasePlate(BaseConfiguration configuration, int blockPosX, int yPos, int blockPosZ, int sizeX, int sizeZ, Direction dir)
    {
        super(CBASE_PLATE, configuration, sizeX, 1, sizeZ, dir);
        this.setOrientation(dir);
        this.boundingBox = new BoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos, blockPosZ + this.sizeZ);
    }

    @Override
    public boolean postProcess(LevelAccessor worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, BoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        BlockState blockWall = this.configuration.getWallBlock();
        boolean axisEW = getDirection().getAxis() == Direction.Axis.X;
        int maxX = axisEW ? this.sizeZ : this.sizeX;
        int maxZ = axisEW ? this.sizeX : this.sizeZ;
        for (int xx = 0; xx <= maxX; xx++)
        {
            for (int zz = 0; zz <= maxZ; zz++)
            {
                this.placeBlock(worldIn, blockWall, xx, 0, zz, boundingBox);
            }
        }

        return true;
    }

}