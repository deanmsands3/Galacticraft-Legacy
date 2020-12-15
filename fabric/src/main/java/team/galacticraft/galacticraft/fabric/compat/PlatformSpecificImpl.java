package team.galacticraft.galacticraft.fabric.compat;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;
import team.galacticraft.galacticraft.common.compat.component.ComponentWrapper;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;
import team.galacticraft.galacticraft.common.compat.item.ItemInventory;
import team.galacticraft.galacticraft.fabric.compat.component.FabricComponentWrapper;
import team.galacticraft.galacticraft.fabric.compat.fluid.FluidTankImpl;
import team.galacticraft.galacticraft.fabric.compat.fluid.FluidUtilFabric;
import team.galacticraft.galacticraft.fabric.compat.item.FabricItemInventory;
import team.galacticraft.galacticraft.fabric.mixin.LevelChunkMixin;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

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

    public static FluidTank createFluidInv(int tanks, Fraction fraction)
    {
        return new FluidTankImpl(tanks, FluidUtilFabric.toAmountLBA(fraction));
    }

    public static FluidTank createFluidInv(int tanks, Fraction fraction, Int2ObjectMap<Object2BooleanFunction<FluidStack>> validFluids)
    {
        return new FluidTankImpl(tanks, FluidUtilFabric.toAmountLBA(fraction), validFluids);
    }

    public static <T> LazyOptional<T> getComponent(Entity entity, ComponentWrapper<T> wrapper)
    {
        return ((FabricComponentWrapper) wrapper).get(entity);
    }

    public static ItemInventory createInventory(int invSize, List<Predicate<ItemStack>> filters)
    {
        return new FabricItemInventory(invSize, filters);
    }

    public static void executeSided(EnvType envType, Supplier<Runnable> supplier)
    {
        if (FabricLoader.getInstance().getEnvironmentType() == envType) {
            supplier.get().run();
        }
    }
}
