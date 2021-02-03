package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;

public abstract class SizedPieceVenus extends DirectionalPieceVenus
{
    protected int sizeX;
    protected int sizeY;
    protected int sizeZ;

    public SizedPieceVenus(StructurePieceType type, CompoundTag nbt)
    {
        super(type, nbt);
    }

    public SizedPieceVenus(StructurePieceType type, DungeonConfigurationVenus configuration, int sizeX, int sizeY, int sizeZ, Direction direction)
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
    protected void readStructureFromNBT(CompoundTag tagCompound)
    {
        super.readStructureFromNBT(tagCompound);

        this.sizeX = tagCompound.getInt("sizeX");
        this.sizeY = tagCompound.getInt("sizeY");
        this.sizeZ = tagCompound.getInt("sizeZ");
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
