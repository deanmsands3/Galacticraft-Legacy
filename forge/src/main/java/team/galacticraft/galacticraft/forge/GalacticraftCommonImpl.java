package team.galacticraft.galacticraft.forge;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.registries.ForgeRegistryEntry;
import team.galacticraft.galacticraft.forge.mixin.ChunkMixin;

import javax.annotation.Nullable;

public class GalacticraftCommonImpl
{
    public <T> ResourceLocation getResource(T object, @Nullable Registry<T> registry) {
        if (object instanceof ForgeRegistryEntry) {
            return ((ForgeRegistryEntry<?>) object).getRegistryName();
        }
        return null;
    }

    public boolean chunkLoaded(Chunk chunk)
    {
        return ((ChunkMixin)chunk).isLoaded();
    }
}
