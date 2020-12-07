package team.galacticraft.galacticraft.common.compat;

import me.shedaniel.architectury.ExpectPlatform;
import me.shedaniel.architectury.networking.NetworkChannel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.logging.log4j.Logger;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;
import team.galacticraft.galacticraft.common.compat.cap.ComponentWrapper;
import team.galacticraft.galacticraft.common.compat.cap.NbtSerializable;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;
import team.galacticraft.galacticraft.common.compat.item.ItemInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PlatformSpecific
{

    @ExpectPlatform
    public static Logger getLogger()
    {
        throw new AssertionError();
    }

    /**
     * @param registry Cannot be null on fabric
     */
    @ExpectPlatform
    public static <T> ResourceLocation getResource(T object, Registry<T> registry)
    {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean chunkLoaded(LevelChunk chunk)
    {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static FluidTank createFluidInv(int tanks, int millibuckets)
    {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends NbtSerializable> LazyOptional<T> getComponent(Entity entity, ComponentWrapper<T> wrapper)
    {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ItemInventory createInventory(int invSize, List<Predicate<ItemStack>> filters)
    {
        throw new AssertionError();
    }
}
