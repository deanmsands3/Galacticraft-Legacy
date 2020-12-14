package team.galacticraft.galacticraft.common.compat;

import me.shedaniel.architectury.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.logging.log4j.Logger;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;
import team.galacticraft.galacticraft.common.compat.component.ComponentWrapper;
import team.galacticraft.galacticraft.common.compat.component.NbtSerializable;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;
import team.galacticraft.galacticraft.common.compat.item.ItemInventory;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

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

    @ExpectPlatform
    public static void openContainer(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> consumer)
    {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void executeSided(EnvType envType, Supplier<Runnable> supplier)
    {
        throw new AssertionError();
    }
}
