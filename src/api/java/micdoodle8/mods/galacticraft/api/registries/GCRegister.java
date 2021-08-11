package micdoodle8.mods.galacticraft.api.registries;

import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class GCRegister<T extends IForgeRegistryEntry<T>> {

	protected abstract void onRegister(Register<T> event);
	
}
