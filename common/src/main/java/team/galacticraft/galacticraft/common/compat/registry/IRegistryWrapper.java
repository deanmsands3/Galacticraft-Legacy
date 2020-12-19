package team.galacticraft.galacticraft.common.compat.registry;

import net.minecraft.resources.ResourceLocation;

public interface IRegistryWrapper<T>
{
    <V extends T> V register(V object, ResourceLocation location);
}
