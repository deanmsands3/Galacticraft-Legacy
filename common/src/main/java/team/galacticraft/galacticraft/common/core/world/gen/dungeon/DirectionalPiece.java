package team.galacticraft.galacticraft.common.core.world.gen.dungeon;

import java.util.Random;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public abstract class DirectionalPiece extends Piece
{
    private Direction direction;

    public DirectionalPiece(StructurePieceType type, CompoundTag nbt)
    {
        super(type, nbt);
    }

    public DirectionalPiece(StructurePieceType type, DungeonConfiguration configuration, Direction direction)
    {
        super(type, configuration);
        this.direction = direction;
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

        tagCompound.putInt("direction", this.direction.ordinal());
    }

    @Override
    protected void readStructureFromNBT(CompoundTag nbt)
    {
        super.readStructureFromNBT(nbt);

        if (nbt.contains("direction"))
        {
            this.direction = Direction.from3DDataValue(nbt.getInt("direction"));
        }
        else
        {
            this.direction = Direction.NORTH;
        }
    }

    public Piece getCorridor(Random rand, DungeonStart startPiece, int maxAttempts, boolean small)
    {
        Direction randomDir;
        int blockX;
        int blockZ;
        int sizeX;
        int sizeZ;
        boolean valid;
        int attempts = maxAttempts;
        int randDir = rand.nextInt(3);
        do
        {
            randomDir = Direction.from2DDataValue((getDirection().getOpposite().get2DDataValue() + 1 + randDir) % 4);
            BoundingBox extension = getExtension(randomDir, this.configuration.getHallwayLengthMin() + rand.nextInt(this.configuration.getHallwayLengthMax() - this.configuration.getHallwayLengthMin()), 3);
            blockX = extension.x0;
            blockZ = extension.z0;
            sizeX = extension.x1 - extension.x0;
            sizeZ = extension.z1 - extension.z0;
            valid = !startPiece.checkIntersection(extension);
            attempts--;
            randDir++;
        }
        while (!valid && attempts > 0);

        if (!valid)
        {
            return null;
        }

        return new Corridor(this.configuration, rand, blockX, blockZ, sizeX, small ? 3 : this.configuration.getHallwayHeight(), sizeZ, randomDir);
    }
}
