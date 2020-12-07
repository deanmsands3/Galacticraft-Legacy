package team.galacticraft.galacticraft.fabric.compat;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;
import team.galacticraft.galacticraft.common.compat.cap.ComponentWrapper;
import team.galacticraft.galacticraft.common.compat.cap.NbtSerializable;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;
import team.galacticraft.galacticraft.common.compat.item.ItemInventory;
import team.galacticraft.galacticraft.fabric.compat.cap.FabricComponentWrapper;
import team.galacticraft.galacticraft.fabric.compat.fluid.FluidTankImpl;
import team.galacticraft.galacticraft.fabric.compat.item.FabricItemInventory;
import team.galacticraft.galacticraft.fabric.mixin.LevelChunkMixin;

import java.util.List;
import java.util.function.Predicate;

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

    public static FluidTank createFluidInv(int tanks, int millibuckets)
    {
        return new FluidTankImpl(tanks, FluidAmount.of(millibuckets, 1000));
    }

    public static <T extends NbtSerializable> LazyOptional<T> getComponent(Entity entity, ComponentWrapper<T> wrapper)
    {
        return ((FabricComponentWrapper) wrapper).get(entity);
    }

    public static ItemInventory createInventory(int invSize, List<Predicate<ItemStack>> filters)
    {
        return new FabricItemInventory(invSize, filters);
    }
}
