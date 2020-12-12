package team.galacticraft.galacticraft.fabric.compat.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import team.galacticraft.galacticraft.common.compat.registry.IRegistryWrapper;

public class RegistryWrapperFabric<T> implements IRegistryWrapper<T> {
    private final Registry<T> parent;

    public RegistryWrapperFabric(Registry<T> parent) {
        this.parent = parent;
    }

    @Override
    public <V extends T> V register(V object, ResourceLocation location) {
        return Registry.register(parent, location, object);
    }
}
