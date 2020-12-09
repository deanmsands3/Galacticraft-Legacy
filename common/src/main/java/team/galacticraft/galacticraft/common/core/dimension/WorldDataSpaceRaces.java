package team.galacticraft.galacticraft.common.core.dimension;

import team.galacticraft.galacticraft.core.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class WorldDataSpaceRaces extends SavedData
{
    public static final String saveDataID = Constants.GCDATAFOLDER + "GCSpaceRaceData";
    private CompoundTag dataCompound;

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
        return world.getDataStorage().computeIfAbsent(WorldDataSpaceRaces::new, WorldDataSpaceRaces.saveDataID);
    }

    @Override
    public boolean isDirty()
    {
        return true;
    }
}
