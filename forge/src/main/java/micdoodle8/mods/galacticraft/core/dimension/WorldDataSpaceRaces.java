package micdoodle8.mods.galacticraft.core.dimension;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class WorldDataSpaceRaces extends SavedData
{
    public static final String saveDataID = "GCSpaceRaceData";

    public WorldDataSpaceRaces()
    {
        super(WorldDataSpaceRaces.saveDataID);
    }

    @Override
    public void load(CompoundTag nbt)
    {
        SpaceRaceManager.loadSpaceRaces(nbt);
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        return SpaceRaceManager.saveSpaceRaces(nbt);
    }

    public static WorldDataSpaceRaces initWorldData(ServerLevel world)
    {
        return world.getDataStorage().computeIfAbsent(WorldDataSpaceRaces::new, saveDataID);
    }

    @Override
    public boolean isDirty()
    {
        return true;
    }
}
