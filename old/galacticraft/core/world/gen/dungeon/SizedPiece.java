package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;

public abstract class SizedPiece extends DirectionalPiece
{
    protected int sizeX;
    protected int sizeY;
    protected int sizeZ;

    public SizedPiece(StructurePieceType type, CompoundTag nbt)
    {
        super(type, nbt);
    }

    public SizedPiece(StructurePieceType type, DungeonConfiguration configuration, int sizeX, int sizeY, int sizeZ, Direction direction)
    {
        super(type, configuration, direction);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    @Override
    protected void writeStructureToNBT(CompoundTag tagCompound)
    {
        super.writeStructureToNBT(tagCompound);

        tagCompound.putInt("sizeX", this.sizeX);
        tagCompound.putInt("sizeY", this.sizeY);
        tagCompound.putInt("sizeZ", this.sizeZ);
    }

    @Override
    protected void readStructureFromNBT(CompoundTag nbt)
    {
        super.readStructureFromNBT(nbt);

        this.sizeX = nbt.getInt("sizeX");
        this.sizeY = nbt.getInt("sizeY");
        this.sizeZ = nbt.getInt("sizeZ");
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
}
