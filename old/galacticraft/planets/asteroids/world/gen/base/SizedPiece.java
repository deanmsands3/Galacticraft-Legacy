package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Random;

public abstract class SizedPiece extends Piece
{
    protected Direction direction;
    protected int sizeX;
    protected int sizeY;
    protected int sizeZ;

    public SizedPiece(StructurePieceType type, CompoundTag nbt)
    {
        super(type, nbt);
    }

    public SizedPiece(StructurePieceType type, BaseConfiguration configuration, int sizeX, int sizeY, int sizeZ, Direction direction)
    {
        super(type, configuration);
        this.direction = direction;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    @Override
    protected void writeStructureToNBT(CompoundTag tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        tagCompound.putInt("dir", this.direction.ordinal());
        tagCompound.putInt("sX", this.sizeX);
        tagCompound.putInt("sY", this.sizeY);
        tagCompound.putInt("sZ", this.sizeZ);
    }

    @Override
    protected void readStructureFromNBT(CompoundTag tagCompound)
    {
        super.readStructureFromNBT(tagCompound);

        this.sizeX = tagCompound.getInt("sX");
        this.sizeY = tagCompound.getInt("sY");
        this.sizeZ = tagCompound.getInt("sZ");

        if (tagCompound.contains("dir"))
        {
            this.direction = Direction.from3DDataValue(tagCompound.getInt("dir"));
        }
        else
        {
            this.direction = Direction.NORTH;
        }
    }

    public int getSizeX()
    {
        return sizeX;
    }

    public int getSizeY()
    {
        return sizeY;
    }

    public int getSizeZ()
    {
        return sizeZ;
    }

    @Override
    protected int getWorldX(int x, int z)
    {
        if (this.getOrientation() == null)
        {
            return x;
        }
        else
        {
            switch (this.getOrientation())
            {
            case NORTH:
                return this.boundingBox.x0 + x;
            case SOUTH:
                return this.boundingBox.x1 - x;
            case WEST:
                return this.boundingBox.x1 - z;
            case EAST:
                return this.boundingBox.x0 + z;
            default:
                return x;
            }
        }
    }

    @Override
    protected int getWorldZ(int x, int z)
    {
        if (this.getOrientation() == null)
        {
            return z;
        }
        else
        {
            switch (this.getOrientation())
            {
            case NORTH:
                return this.boundingBox.z0 + z;
            case SOUTH:
                return this.boundingBox.z1 - z;
            case WEST:
                return this.boundingBox.z0 + x;
            case EAST:
                return this.boundingBox.z1 - x;
            default:
                return z;
            }
        }
    }

    //Unused currently
    public Piece getDoorway(Random rand, BaseStart startPiece, int maxAttempts, boolean small)
    {
        Direction randomDir;
        int blockX;
        int blockZ;
        int sizeX;
        int sizeZ;
        boolean valid;
        int attempts = maxAttempts;
        do
        {
            int randDir = rand.nextInt(4);
            randomDir = Direction.from2DDataValue((randDir == getDirection().getOpposite().get2DDataValue() ? randDir + 1 : randDir) % 4);
            BoundingBox extension = getExtension(randomDir, 1, 3);
            blockX = extension.x0;
            blockZ = extension.z0;
            sizeX = extension.x1 - extension.x0;
            sizeZ = extension.z1 - extension.z0;
            valid = true;
            attempts--;
        }
        while (!valid && attempts > 0);

        if (!valid)
        {
            return null;
        }

        return new BaseLinking(this.configuration, rand, blockX, this.boundingBox.y0, blockZ, sizeX, 3, sizeZ, randomDir);
    }
}
