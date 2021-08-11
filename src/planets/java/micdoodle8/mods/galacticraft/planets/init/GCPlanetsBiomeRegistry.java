package micdoodle8.mods.galacticraft.planets.init;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

import micdoodle8.mods.galacticraft.Constants;
import micdoodle8.mods.galacticraft.api.registries.BiomeRegister;
import micdoodle8.mods.galacticraft.api.registries.GCRegister;
import micdoodle8.mods.galacticraft.api.world.GalacticraftBiome.BiomeData;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.BiomeAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.biome.BiomeMars;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.biome.BiomeVenus;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.biome.BiomeVenusMountain;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.biome.BiomeVenusValley;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Constants.MOD_ID_PLANETS)
public class GCPlanetsBiomeRegistry extends GCRegister<Biome> {

	public static final Type ASTEROIDS = Type.getType("ASTEROIDS");
	public static final Type MARS = Type.getType("MARS");
	public static final Type VENUS = Type.getType("VENUS");
	
	@Override
	protected void onRegister(Register<Biome> event) {
		final BiomeRegister biome = new BiomeRegister(event);
		
		biome.register("asteroids",
				new BiomeAsteroids(
						BiomeData.builder()
						.name("Asteroids")),
				ASTEROIDS, COLD, DRY, DEAD
		);
		biome.register("mars",
				new BiomeMars(
						BiomeData.builder()
		        		.name("Mars")
		        		.baseHeight(2.5F)
		        		.heightVariation(0.4F)),
				ASTEROIDS, COLD, DRY, DEAD
		);
		biome.register("venus",
				new BiomeVenus(
						BiomeData.builder()
		        		.name("Venus")
		        		.baseHeight(0.5F)
		        		.heightVariation(0.4F)
		        		.temp(4.0F)),
				VENUS, HOT, DRY, DEAD, SANDY
		);
		biome.register("venus_mountain",
				new BiomeVenusMountain(
						BiomeData.builder()
		        		.name("Venus Mountain")
		        		.baseHeight(2.0F)
		        		.heightVariation(1.0F)
		        		.temp(4.0F)),
				VENUS, HOT, DRY, DEAD, SANDY
		);
		biome.register("venus_valley",
				new BiomeVenusValley(
						BiomeData.builder()
		        		.name("Venus Valley")
		        		.baseHeight(-0.4F)
		        		.heightVariation(0.2F)
		        		.temp(4.0F)),
				VENUS, HOT, DRY, DEAD, SANDY
		);
	}
}
