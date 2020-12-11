package team.galacticraft.galacticraft.common.core.world.gen;

import team.galacticraft.galacticraft.common.api.world.BiomeGC;
import team.galacticraft.galacticraft.common.core.entities.EntityEvolvedSpider;
import team.galacticraft.galacticraft.common.core.entities.EntityEvolvedZombie;
import team.galacticraft.galacticraft.common.core.entities.GCEntities;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.BiomeDictionary;

import static net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder.CONFIG_STONE;

public class BiomeOrbit extends BiomeGC
{
    public static final Biome space = new BiomeOrbit();

    private BiomeOrbit()
    {
        super((new BiomeBuilder()).surfaceBuilder(SurfaceBuilder.NOPE, CONFIG_STONE).precipitation(Precipitation.NONE).biomeCategory(BiomeCategory.NONE).depth(0.0F).scale(0.0F).temperature(0.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null), true);
        addSpawn(MobCategory.MONSTER, new SpawnerData(GCEntities.EVOLVED_ZOMBIE, 10, 4, 4));
        addSpawn(MobCategory.MONSTER, new SpawnerData(GCEntities.EVOLVED_SPIDER, 10, 4, 4));
    }

    @Override
    public void registerTypes(Biome b)
    {
        BiomeDictionary.addTypes(b, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.SPOOKY);
    }

    @Override
    public float getCreatureProbability()
    {
        return 0.01F;
    }
}
