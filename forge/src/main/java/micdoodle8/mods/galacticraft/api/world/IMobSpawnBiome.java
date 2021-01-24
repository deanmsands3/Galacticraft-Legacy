package micdoodle8.mods.galacticraft.api.world;

import java.util.Map;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

/**
 * Implement this on any Galacticraft World dimension biome registered for a Celestial Body
 */
public interface IMobSpawnBiome
{
    void initialiseMobLists(Map<MobSpawnSettings.SpawnerData, MobCategory> mobInfo);
}
