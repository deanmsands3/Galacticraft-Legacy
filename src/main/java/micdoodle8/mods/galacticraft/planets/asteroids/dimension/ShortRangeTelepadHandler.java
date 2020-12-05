package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import com.google.common.collect.Maps;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.planets.asteroids.tick.AsteroidsTickHandlerServer;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.Map;
import java.util.Objects;

public class ShortRangeTelepadHandler extends SavedData
{
    public static final String saveDataID = "ShortRangeTelepads";
    private static final Map<Integer, TelepadEntry> tileMap = Maps.newHashMap();

    public ShortRangeTelepadHandler(String saveDataID)
    {
        super(saveDataID);
    }

    public static class TelepadEntry
    {
        public DimensionType dimensionID;
        public BlockVec3 position;
        public boolean enabled;

        public TelepadEntry(DimensionType dimID, BlockVec3 position, boolean enabled)
        {
            this.dimensionID = dimID;
            this.position = position;
            this.enabled = enabled;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TelepadEntry that = (TelepadEntry) o;
            return enabled == that.enabled && Objects.equals(dimensionID, that.dimensionID) && Objects.equals(position, that.position);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(dimensionID, position, enabled);
        }
    }

    @Override
    public void load(CompoundTag nbt)
    {
        ListTag tagList = nbt.getList("TelepadList", 10);
        tileMap.clear();

        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundTag nbt2 = tagList.getCompound(i);
            int address = nbt2.getInt("Address");
            int dimId = nbt.getInt("dimension");
            DimensionType dimType = DimensionType.getById(dimId);
            if (dimType == null)
            {
                throw new IllegalArgumentException("Invalid map dimension: " + i);
            }
            else
            {
                int posX = nbt2.getInt("PosX");
                int posY = nbt2.getInt("PosY");
                int posZ = nbt2.getInt("PosZ");
                boolean enabled = true;
                if (nbt2.contains("Enabled"))
                {
                    enabled = nbt2.getBoolean("Enabled");
                }
                tileMap.put(address, new TelepadEntry(dimType, new BlockVec3(posX, posY, posZ), enabled));
            }
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        ListTag tagList = new ListTag();

        for (Map.Entry<Integer, TelepadEntry> e : tileMap.entrySet())
        {
            CompoundTag nbt2 = new CompoundTag();
            nbt2.putInt("Address", e.getKey());
            nbt2.putInt("DimID", e.getValue().dimensionID.getId());
            nbt2.putInt("PosX", e.getValue().position.x);
            nbt2.putInt("PosY", e.getValue().position.y);
            nbt2.putInt("PosZ", e.getValue().position.z);
            nbt2.putBoolean("Enabled", e.getValue().enabled);
            tagList.add(nbt2);
        }

        nbt.put("TelepadList", tagList);
        return nbt;
    }

    public static void addShortRangeTelepad(TileEntityShortRangeTelepad telepad)
    {
        if (!telepad.getLevel().isClientSide)
        {
            if (telepad.addressValid)
            {
                TelepadEntry newEntry = new TelepadEntry(telepad.getLevel().dimension.getDimension().getType(), new BlockVec3(telepad), !telepad.getDisabled(0));
                TelepadEntry previous = tileMap.put(telepad.address, newEntry);

                if (previous == null || !previous.equals(newEntry))
                {
                    AsteroidsTickHandlerServer.spaceRaceData.setDirty(true);
                }
            }
        }
    }

    public static void removeShortRangeTeleporter(TileEntityShortRangeTelepad telepad)
    {
        if (!telepad.getLevel().isClientSide)
        {
            if (telepad.addressValid)
            {
                tileMap.remove(telepad.address);
                AsteroidsTickHandlerServer.spaceRaceData.setDirty(true);
            }
        }
    }

    public static TelepadEntry getLocationFromAddress(int address)
    {
        return tileMap.get(address);
    }
}
