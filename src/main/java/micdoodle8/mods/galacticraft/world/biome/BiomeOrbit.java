package micdoodle8.mods.galacticraft.world.biome;

import micdoodle8.mods.galacticraft.api.world.GalacticraftBiome;
import micdoodle8.mods.galacticraft.api.world.GalacticraftBiome.BiomeData.DataBuilder;
import micdoodle8.mods.galacticraft.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.entities.EntityEvolvedZombie;

public class BiomeOrbit extends GalacticraftBiome
{
    public BiomeOrbit(DataBuilder dataBuilder)
    {
        super(dataBuilder.build(), true);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedZombie.class, 10, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSpider.class, 10, 4, 4));
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }
}
