package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import java.util.LinkedList;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.world.GalacticraftBiome;
import micdoodle8.mods.galacticraft.api.world.GalacticraftBiome.BiomeData.DataBuilder;
import micdoodle8.mods.galacticraft.entities.*;
import micdoodle8.mods.galacticraft.util.ConfigManagerCore;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeAsteroids extends GalacticraftBiome
{
    public BiomeAsteroids(DataBuilder dataBuilder)
    {
        super(dataBuilder.build(), true);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.resetMonsterListByMode(ConfigManagerCore.challengeMobDropsAndSpawning);
    }

    public void resetMonsterListByMode(boolean challengeMode)
    {
        this.spawnableMonsterList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedZombie.class, 3000, 1, 3));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSpider.class, 2000, 1, 2));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSkeleton.class, 1500, 1, 1));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedCreeper.class, 2000, 1, 1));
        if (challengeMode) this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedEnderman.class, 250, 1, 1));
    }
    
    @Override
    public void initialiseMobLists(LinkedList<SpawnListEntry> mobInfo)
    {
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }

	@Override
	public void generateTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
		super.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
	}
}
