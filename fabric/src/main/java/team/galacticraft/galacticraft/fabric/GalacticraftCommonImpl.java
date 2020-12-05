package team.galacticraft.galacticraft.fabric;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.galacticraft.galacticraft.fabric.mixin.LevelChunkMixin;

public class GalacticraftCommonImpl
{
    public <T> ResourceLocation getResource(T object, @NotNull Registry<T> registry)
    {
        return registry.getKey(object);
    }

    public boolean chunkLoaded(LevelChunk chunk)
    {
        return ((LevelChunkMixin)chunk).isLoaded();
    }

}
