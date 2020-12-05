package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.BiomeDictionary;

import java.util.LinkedList;

import static net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder.CONFIG_STONE;

public class BiomeAsteroids extends BiomeGC
{
    public static final BiomeAsteroids asteroid = new BiomeAsteroids();

    private BiomeAsteroids()
    {
        super((new Biome.BiomeBuilder()).surfaceBuilder(SurfaceBuilder.NOPE, CONFIG_STONE).precipitation(Biome.Precipitation.NONE).biomeCategory(BiomeCategory.NONE).depth(1.5F).scale(0.4F).temperature(0.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null), true);
        this.getMobs(MobCategory.MONSTER).clear();
        this.getMobs(MobCategory.WATER_CREATURE).clear();
        this.getMobs(MobCategory.CREATURE).clear();
        this.getMobs(MobCategory.AMBIENT).clear();
        this.getMobs(MobCategory.MISC).clear();
        this.resetMonsterListByMode(ConfigManagerCore.challengeMobDropsAndSpawning);
    }

    @Override
    public void registerTypes(Biome b)
    {
        //Currently unused for Asteroids due to adaptive biomes system
        BiomeDictionary.addTypes(b, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
    }

    public void resetMonsterListByMode(boolean challengeMode)
    {
        this.addSpawn(MobCategory.MONSTER, new SpawnerData(GCEntities.EVOLVED_ZOMBIE, 3000, 1, 3));
        this.addSpawn(MobCategory.MONSTER, new SpawnerData(GCEntities.EVOLVED_SPIDER, 2000, 1, 2));
        this.addSpawn(MobCategory.MONSTER, new SpawnerData(GCEntities.EVOLVED_SKELETON, 1500, 1, 1));
        this.addSpawn(MobCategory.MONSTER, new SpawnerData(GCEntities.EVOLVED_CREEPER, 2000, 1, 1));
        if (challengeMode)
        {
            this.addSpawn(MobCategory.MONSTER, new SpawnerData(GCEntities.EVOLVED_ENDERMAN, 250, 1, 1));
        }
    }

    @Override
    public float getCreatureProbability()
    {
        return 0.01F;
    }
}
