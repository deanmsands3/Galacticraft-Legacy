package micdoodle8.mods.galacticraft.api.registries;

import java.lang.reflect.Parameter;

@FunctionalInterface
public interface RegisterAction {
	public abstract void call(Parameter... parameters);
}
