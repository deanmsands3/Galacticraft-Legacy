package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public abstract class Piece extends StructurePiece
{
    protected BaseConfiguration configuration;

    public Piece(StructurePieceType type)
    {
        super(type, 0);
    }

    public Piece(StructurePieceType type, BaseConfiguration configuration)
    {
        this(type);
        this.configuration = configuration;
    }

    public Piece(StructurePieceType type, CompoundTag tagCompound)
    {
        super(type, tagCompound);
        this.readStructureFromNBT(tagCompound);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tagCompound)
    {
        // This is actually write, incorrect name mapping
        this.writeStructureToNBT(tagCompound);
    }

    protected void writeStructureToNBT(CompoundTag tagCompound)
    {
        this.configuration.writeToNBT(tagCompound);
    }

    protected void readStructureFromNBT(CompoundTag tagCompound)
    {
        if (this.configuration == null)
        {
            this.configuration = new BaseConfiguration();
            this.configuration.readFromNBT(tagCompound);
        }
    }

    protected BoundingBox getExtension(Direction direction, int length, int width)
    {
        int blockX, blockZ, sizeX, sizeZ;
        switch (direction)
        {
        case NORTH:
            sizeX = width;
            sizeZ = length;
            blockX = this.boundingBox.x0 + (this.boundingBox.x1 - this.boundingBox.x0) / 2 - sizeX / 2;
            blockZ = this.boundingBox.z0 - sizeZ;
            break;
        case EAST:
            sizeX = length;
            sizeZ = width;
            blockX = this.boundingBox.x1;
            blockZ = this.boundingBox.z0 + (this.boundingBox.z1 - this.boundingBox.z0) / 2 - sizeZ / 2;
            break;
        case SOUTH:
            sizeX = width;
            sizeZ = length;
            blockX = this.boundingBox.x0 + (this.boundingBox.x1 - this.boundingBox.x0) / 2 - sizeX / 2;
            blockZ = this.boundingBox.z1;
            break;
        case WEST:
        default:
            sizeX = length;
            sizeZ = width;
            blockX = this.boundingBox.x0 - sizeX;
            blockZ = this.boundingBox.z0 + (this.boundingBox.z1 - this.boundingBox.z0) / 2 - sizeZ / 2;
            break;
        }
        return new BoundingBox(blockX, blockZ, blockX + sizeX, blockZ + sizeZ);
    }
}