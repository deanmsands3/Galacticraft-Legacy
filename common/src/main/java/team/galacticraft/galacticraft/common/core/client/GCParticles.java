package team.galacticraft.galacticraft.common.core.client;

import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.client.fx.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_CORE)
public class GCParticles
{
    @ObjectHolder(ParticleTypeNames.whiteSmoke)
    public static SimpleParticleType WHITE_SMOKE_IDLE;
    @ObjectHolder(ParticleTypeNames.whiteSmokeLaunched)
    public static SimpleParticleType WHITE_SMOKE_LAUNCHED;
    @ObjectHolder(ParticleTypeNames.whiteSmokeLarge)
    public static SimpleParticleType WHITE_SMOKE_IDLE_LARGE;
    @ObjectHolder(ParticleTypeNames.whiteSmokeLaunchedLarge)
    public static SimpleParticleType WHITE_SMOKE_LAUNCHED_LARGE;
    @ObjectHolder(ParticleTypeNames.launchFlame)
    public static ParticleType<EntityParticleData> LAUNCH_FLAME_IDLE;
    @ObjectHolder(ParticleTypeNames.launchFlameLaunched)
    public static ParticleType<EntityParticleData> LAUNCH_FLAME_LAUNCHED;
    @ObjectHolder(ParticleTypeNames.launchSmoke)
    public static SimpleParticleType LAUNCH_SMOKE_TINY;
    @ObjectHolder(ParticleTypeNames.oilDrip)
    public static SimpleParticleType OIL_DRIP;
    @ObjectHolder(ParticleTypeNames.oxygen)
    public static SimpleParticleType OXYGEN;
    @ObjectHolder(ParticleTypeNames.landerFlame)
    public static ParticleType<EntityParticleData> LANDER_FLAME;

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name)
    {
        reg.register(thing.setRegistryName(name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
    {
        register(reg, thing, new ResourceLocation(Constants.MOD_ID_CORE, name));
    }

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> evt)
    {
        IForgeRegistry<ParticleType<?>> r = evt.getRegistry();

        register(r, new SimpleParticleType(false), ParticleTypeNames.whiteSmoke);
        register(r, new SimpleParticleType(false), ParticleTypeNames.whiteSmokeLaunched);
        register(r, new SimpleParticleType(false), ParticleTypeNames.whiteSmokeLarge);
        register(r, new SimpleParticleType(false), ParticleTypeNames.whiteSmokeLaunchedLarge);
        register(r, new ParticleType<>(false, EntityParticleData.DESERIALIZER), ParticleTypeNames.launchFlame);
        register(r, new ParticleType<>(false, EntityParticleData.DESERIALIZER), ParticleTypeNames.launchFlameLaunched);
        register(r, new SimpleParticleType(false), ParticleTypeNames.launchSmoke);
        register(r, new SimpleParticleType(false), ParticleTypeNames.oilDrip);
        register(r, new SimpleParticleType(false), ParticleTypeNames.oxygen);
        register(r, new ParticleType<>(false, EntityParticleData.DESERIALIZER), ParticleTypeNames.landerFlame);
    }
}
