package team.galacticraft.galacticraft.forge.compat.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryWrapperForge<T extends ForgeRegistryEntry<T>> implements IRegistryWrapper<T> {
    private final IForgeRegistry<T> parent;

    public RegistryWrapperForge(IForgeRegistry<T> parent) {
        this.parent = parent;
    }

    @Override
    public <V extends T> V register(V object, ResourceLocation location) {
        parent.register(object.setRegistryName(location));
        return object;
    }
}
