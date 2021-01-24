package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.world.level.biome.BiomeSourceType;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import static micdoodle8.mods.galacticraft.core.GCBlocks.register;

public class MoonBiomeProviderTypes
{
    public static final DeferredRegister<BiomeSourceType<?, ?>> BIOME_PROVIDER_TYPES = new DeferredRegister<>(ForgeRegistries.BIOME_PROVIDER_TYPES, Constants.MOD_ID_PLANETS);

    public static final BiomeSourceType<MoonBiomeProviderSettings, MoonBiomeProvider> MOON_TYPE = newType();

    // TODO Yikes, fix this once forge makes BiomeProviderType constructor public
    private static BiomeSourceType<MoonBiomeProviderSettings, MoonBiomeProvider> newType()
    {
        Class<?> c = BiomeSourceType.class;
        try
        {
            Constructor<?> cons = c.getDeclaredConstructor(Function.class, Function.class);
            cons.setAccessible(true);
            Function<MoonBiomeProviderSettings, MoonBiomeProvider> f1 = MoonBiomeProvider::new;
            Function<LevelData, MoonBiomeProviderSettings> f2 = MoonBiomeProviderSettings::new;
            return (BiomeSourceType<MoonBiomeProviderSettings, MoonBiomeProvider>)cons.newInstance(f1, f2);
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return null;
    }

//    @SubscribeEvent
//    public static void registerBiomeTypes(RegistryEvent.Register<BiomeProviderType<?, ?>> evt)
//    {
//        IForgeRegistry<BiomeProviderType<?, ?>> r = evt.getRegistry();
//        register(r, "moon_biome_provider", MOON_TYPE);
//    }

//    public static final RegistryObject<BiomeProviderType<MoonBiomeProviderSettings, MoonBiomeProvider>> MOON = register(
//            "moon_biome_provider_type", () -> {
//
//            });
}
