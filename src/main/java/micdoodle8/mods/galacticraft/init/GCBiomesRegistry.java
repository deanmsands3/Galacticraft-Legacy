package micdoodle8.mods.galacticraft.init;

import static net.minecraftforge.common.BiomeDictionary.Type.COLD;
import static net.minecraftforge.common.BiomeDictionary.Type.DEAD;
import static net.minecraftforge.common.BiomeDictionary.Type.DRY;
import static net.minecraftforge.common.BiomeDictionary.Type.SPARSE;
import static net.minecraftforge.common.BiomeDictionary.Type.SPOOKY;

import micdoodle8.mods.galacticraft.Constants;
import micdoodle8.mods.galacticraft.api.registries.BiomeRegister;
import micdoodle8.mods.galacticraft.api.world.GalacticraftBiome.BiomeData;
import micdoodle8.mods.galacticraft.world.biome.BiomeMoon;
import micdoodle8.mods.galacticraft.world.biome.BiomeOrbit;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Constants.MOD_ID_CORE)
public final class GCBiomesRegistry {

	public static final Type MOON = Type.getType("MOON");
	public static final Type ORBIT = Type.getType("ORBIT");
	public static final Type VACUUM = Type.getType("VACUUM");
	
	@SubscribeEvent
	public static void onRegisterBiomes(Register<Biome> event) {
		
		final BiomeRegister biomes = new BiomeRegister(event.getRegistry());
		
		biomes.register("moon", 
				new BiomeMoon(
						BiomeData.builder()
						.name("Moon")
						.baseHeight(1.5F)
						.heightVariation(0.4F)), 
				MOON, COLD, DRY, DEAD);
		
		biomes.register("orbit",
				new BiomeOrbit(
						BiomeData.builder()
						.name("Orbit")),
				ORBIT, DRY, DEAD, SPARSE, SPOOKY);
	}
}
