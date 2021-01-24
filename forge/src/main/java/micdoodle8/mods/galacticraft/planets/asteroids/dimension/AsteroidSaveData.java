package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class AsteroidSaveData extends SavedData
{
    public static final String saveDataID = "GCAsteroidData";
    public CompoundTag datacompound;

    public AsteroidSaveData(String s)
    {
        super(AsteroidSaveData.saveDataID);
        this.datacompound = new CompoundTag();
    }

    @Override
    public void load(CompoundTag nbt)
    {
        this.datacompound = nbt.getCompound("asteroids");
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        nbt.put("asteroids", this.datacompound);
        return nbt;
    }
}
