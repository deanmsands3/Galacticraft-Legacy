package micdoodle8.mods.galacticraft.api.world;

import java.util.LinkedList;
import java.util.Random;

import lombok.Getter;
import micdoodle8.mods.galacticraft.GalacticraftCore;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

/**
 * This extension of BiomeGenBase contains the default initialiseMobLists()
 * called on CelestialBody registration to register mob spawn data
 */
public abstract class GalacticraftBiome extends Biome implements IMobSpawnBiome {
	public boolean isAdaptiveBiome = false;
	private BiomeData biomeData;

	protected GalacticraftBiome(BiomeData biomeData) {
		super(biomeData);
		this.setRegistryName(biomeData.biomeName);
		GalacticraftCore.biomesList.add(this);
		this.biomeData = biomeData;
	}

	protected GalacticraftBiome(BiomeData biomeData, boolean adaptive) {
		super(biomeData);
		this.setRegistryName(biomeData.biomeName);
		this.isAdaptiveBiome = adaptive;
		this.biomeData = biomeData;
	}
	
	public BiomeData getBiomeData() {
		return biomeData;
	}

	/**
	 * Override this in your biomes <br>
	 * (Note: if adaptive biomes, only the FIRST to register the adaptive biome will
	 * have its types registered in the BiomeDictionary - sorry, that's a Forge
	 * limitation.)
	 */
	public void registerTypes(Biome registering) {
	}

	/**
	 * The default implementation in BiomeGenBaseGC will attempt to allocate each
	 * SpawnListEntry in the CelestialBody's mobInfo to this biome's Water, Cave,
	 * Monster or Creature lists according to whether the spawnable entity's class
	 * is a subclass of EntityWaterMob, EntityAmbientCreature, EntityMob or anything
	 * else (passive mobs or plain old EntityLiving).
	 * 
	 * Override this if different behaviour is required.
	 */
	@Override
	public void initialiseMobLists(LinkedList<SpawnListEntry> mobInfo) {
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		for (SpawnListEntry entry : mobInfo) {
			Class<?> mobClass = entry.entityClass;
			if (EntityWaterMob.class.isAssignableFrom(mobClass)) {
				this.spawnableWaterCreatureList.add(entry);
			} else if (EntityAmbientCreature.class.isAssignableFrom(mobClass)) {
				this.spawnableCaveCreatureList.add(entry);
			} else if (EntityMob.class.isAssignableFrom(mobClass)) {
				this.spawnableMonsterList.add(entry);
			} else {
				this.spawnableCreatureList.add(entry);
			}
		}
	}
	
    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
    }
    
    public abstract void generateTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal);

	@Getter
	public static class BiomeData extends BiomeProperties {

		protected String biomeName;
		protected float baseHeight = 0.1F;
		protected float heightVariation = 0.2F;
		protected float temperature = 0.5F;
		protected float rainfall = 0.0F;
		protected int waterColor = 16777215;
		protected boolean enableSnow = false;
		protected boolean enableRain = false;
		protected String baseBiomeRegName;

		private BiomeData(DataBuilder builder) {
			super(builder.biomeName);
			this.setTemperature(builder.temperature);
			this.setRainfall(builder.rainfall);
			this.setBaseHeight(builder.baseHeight);
			this.setHeightVariation(builder.heightVariation);
			if (!builder.enableRain)
				this.setRainDisabled();
			if (builder.enableSnow)
				this.setSnowEnabled();
			this.setWaterColor(builder.waterColor);
			this.setBaseBiome(builder.baseBiomeRegName);
		}
		
		private BiomeData() {
			super(null);
		}
		
		public static DataBuilder builder() {
			return new DataBuilder();
		}
		
		public float getTemperature() {
			return temperature;
		}

		public static class DataBuilder extends BiomeData {


			public DataBuilder name(String biomeName) { 
				if (biomeName != null)
					this.biomeName = biomeName;
				return this;
			}

			public DataBuilder temp(Float temperature) {
				if (temperature != null)
					this.temperature = temperature;
				return this;
			}

			public DataBuilder rainfall(Float rainfall) {
				if (rainfall != null)
					this.rainfall = rainfall;
				return this;
			}

			public DataBuilder baseHeight(Float baseHeight) {
				if (baseHeight != null)
					this.baseHeight = baseHeight;
				return this;
			}

			public DataBuilder heightVariation(Float heightVariation) {
				if (heightVariation != null)
					this.heightVariation = heightVariation;
				return this;
			}

			public DataBuilder enableRain() {
				this.enableRain = true;
				return this;
			}

			public DataBuilder allowSnow() {
				this.enableSnow = true;
				return this;
			}

			public DataBuilder waterColor(Integer waterColor) {
				if (waterColor != null)
					this.waterColor = waterColor;
				return this;
			}

			public DataBuilder baseBiome(String name) {
				if (name != null)
					this.baseBiomeRegName = name;
				return this;
			}

			public BiomeData build() {
				return new BiomeData(this);
			}
		}
	}
}
