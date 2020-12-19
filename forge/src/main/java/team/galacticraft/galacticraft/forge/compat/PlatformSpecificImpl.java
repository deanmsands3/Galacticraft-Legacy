package team.galacticraft.galacticraft.forge.compat;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import team.galacticraft.galacticraft.forge.compat.cap.ForgeCapability;
import team.galacticraft.galacticraft.forge.mixin.ChunkMixin;

import java.util.function.Supplier;

public class PlatformSpecificImpl
{
    private static final Logger LOGGER = LogManager.getLogger("Galacticraft");

    public static FluidTank createFluidInv(int tanks, int millibuckets)
    {

    }

    public static <T> LazyOptional<T> getComponent(Entity entity, ComponentWrapper<T> wrapper)
    {
        return ((ForgeCapability<T>) wrapper).get(entity);
    }

    public static void executeSided(Dist envType, Supplier<Runnable> supplier)
    {
        DistExecutor.runWhenOn(envType, supplier);
    }

    public Logger getLogger()
    {
        return LOGGER;
    }

    public <T> ResourceLocation getResource(T object, @Nullable Registry<T> registry)
    {
        if (object instanceof ForgeRegistryEntry)
        {
            return ((ForgeRegistryEntry<?>) object).getRegistryName();
        }
        return null;
    }

    public boolean chunkLoaded(Chunk chunk)
    {
        return ((ChunkMixin) chunk).isLoaded();
    }
}
