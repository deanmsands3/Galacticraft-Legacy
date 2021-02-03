package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import java.util.Random;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public abstract class DirectionalPieceVenus extends PieceVenus
{
    private Direction direction;

    public DirectionalPieceVenus(StructurePieceType type, CompoundTag nbt)
    {
        super(type, nbt);
    }

    public DirectionalPieceVenus(StructurePieceType type, DungeonConfigurationVenus configuration, Direction direction)
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
    protected void readStructureFromNBT(CompoundTag tagCompound)
    {
        super.readStructureFromNBT(tagCompound);

        if (tagCompound.contains("direction"))
        {
            this.direction = Direction.from3DDataValue(tagCompound.getInt("direction"));
        }
        else
        {
            this.direction = Direction.NORTH;
        }
    }

    public PieceVenus getCorridor(Random rand, DungeonStartVenus startPiece, int maxAttempts, boolean small)
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
            BoundingBox extension = getExtension(randomDir, this.configuration.getHallwayLengthMin() + rand.nextInt(this.configuration.getHallwayLengthMax() - this.configuration.getHallwayLengthMin()), 5);
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

        return new CorridorVenus(this.configuration, rand, blockX, blockZ, sizeX, small ? 3 : this.configuration.getHallwayHeight(), sizeZ, randomDir);
    }
}
