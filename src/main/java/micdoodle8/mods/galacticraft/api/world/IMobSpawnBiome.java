package micdoodle8.mods.galacticraft.api.world;

import java.util.LinkedList;
import java.util.Map;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome.SpawnerData;
import com.google.common.collect.BiMap;

/**
 * Implement this on any Galacticraft World dimension biome registered for a Celestial Body
 */
public interface IMobSpawnBiome
{
    void initialiseMobLists(Map<SpawnerData, MobCategory> mobInfo);
}
