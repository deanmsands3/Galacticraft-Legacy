package team.galacticraft.galacticraft.common.api.world;

import java.util.Map;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import team.galacticraft.galacticraft.common.compat.GalacticraftCommon;

/**
 * This extension of BiomeGenBase contains the default initialiseMobLists()
 * called on CelestialBody registration to register mob spawn data
 */
public abstract class BiomeGC extends Biome implements IMobSpawnBiome
{
//    public final boolean isAdaptiveBiome;

    protected BiomeGC(Biome.BiomeBuilder biomeBuilder)
    {
        super(biomeBuilder);
        GalacticraftCommon.BIOMES_LIST.add(this);
//        this.isAdaptiveBiome = false;
    }

    protected BiomeGC(Biome.BiomeBuilder biomeBuilder, boolean adaptive)
    {
        super(biomeBuilder);
//        this.isAdaptiveBiome = adaptive;
    }

    /**
     * Override this in your biomes
     * <br>
     * (Note: if adaptive biomes, only the FIRST to register the adaptive biome will have its
     * types registered in the BiomeDictionary - sorry, that's a Forge limitation.)
     */
    public void registerTypes(Biome registering)
    {
    }

    /**
     * The default implementation in BiomeGenBaseGC will attempt to allocate each
     * SpawnListEntry in the CelestialBody's mobInfo to this biome's
     * Water, Cave, Monster or Creature lists according to whether the
     * spawnable entity's class is a subclass of EntityWaterMob, EntityAmbientCreature,
     * EntityMob or anything else (passive mobs or plain old EntityLiving).
     * <p>
     * Override this if different behaviour is required.
     */
    @Override
    public void initialiseMobLists(Map<SpawnerData, MobCategory> mobInfo)
    {
        for (MobCategory classification : MobCategory.values())
        {
            this.getMobs(classification).clear();
        }
        for (Map.Entry<SpawnerData, MobCategory> entry : mobInfo.entrySet())
        {
            getMobs(entry.getValue()).add(entry.getKey());
        }
    }
}
