package team.galacticraft.galacticraft.forge.compat;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.galacticraft.galacticraft.forge.compat.cap.ForgeCapability;
import team.galacticraft.galacticraft.forge.mixin.ChunkMixin;

import javax.annotation.Nullable;

public class PlatformSpecificImpl
{
    private static final Logger LOGGER = LogManager.getLogger("Galacticraft");

    public Logger getLogger() {
        return LOGGER;
    }

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

    public static FluidTank createFluidInv(int tanks, int millibuckets) {

    }

    public static <T> LazyOptional<T> getComponent(Entity entity, ComponentWrapper<T> wrapper) {
        return ((ForgeCapability<T>) wrapper).get(entity);
    }
}
