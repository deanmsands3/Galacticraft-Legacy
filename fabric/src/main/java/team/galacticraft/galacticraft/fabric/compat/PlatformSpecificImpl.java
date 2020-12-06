package team.galacticraft.galacticraft.fabric.compat;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import team.galacticraft.galacticraft.fabric.mixin.LevelChunkMixin;

public class PlatformSpecificImpl
{
    private static final Logger LOGGER = LogManager.getLogger("Galacticraft");

    public static Logger getLogger() {
        return LOGGER;
    }

    public <T> ResourceLocation getResource(T object, @NotNull Registry<T> registry)
    {
        return registry.getKey(object);
    }

    public boolean chunkLoaded(LevelChunk chunk)
    {
        return ((LevelChunkMixin)chunk).isLoaded();
    }

}
