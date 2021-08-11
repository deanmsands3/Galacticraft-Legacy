package micdoodle8.mods.galacticraft.api.registries;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.IForgeRegistry;

public class BiomeRegister {
	
	private final IForgeRegistry<Biome> registry;

	public BiomeRegister(Register<Biome> registry) {

		this.registry = registry.getRegistry();
	}

	public void register(String registryName, Biome biome, Type... biomeTypes) {
		registry.register(biome);
		BiomeDictionary.addTypes(biome, biomeTypes);
	}
}
