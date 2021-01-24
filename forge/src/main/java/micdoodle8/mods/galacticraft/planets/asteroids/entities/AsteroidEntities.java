package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AsteroidEntities
{
    public static final EntityType<AstroMinerEntity> ASTRO_MINER = AsteroidEntities.astroMiner();
    public static final EntityType<EntryPodEntity> ENTRY_POD = AsteroidEntities.entryPod();
    public static final EntityType<GrappleEntity> GRAPPLE = AsteroidEntities.grapple();
    public static final EntityType<SmallAsteroidEntity> SMALL_ASTEROID = AsteroidEntities.smallAsteroid();
    public static final EntityType<Tier3RocketEntity> TIER_3_ROCKET = AsteroidEntities.rocketT3();

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event)
    {
        IForgeRegistry<EntityType<?>> r = event.getRegistry();
        AsteroidBlocks.register(r, ASTRO_MINER, AsteroidEntityNames.ASTRO_MINER);
        AsteroidBlocks.register(r, ENTRY_POD, AsteroidEntityNames.ENTRY_POD);
        AsteroidBlocks.register(r, GRAPPLE, AsteroidEntityNames.GRAPPLE);
        AsteroidBlocks.register(r, SMALL_ASTEROID, AsteroidEntityNames.SMALL_ASTEROID);
        AsteroidBlocks.register(r, TIER_3_ROCKET, AsteroidEntityNames.TIER_3_ROCKET);
    }

    private static EntityType<AstroMinerEntity> astroMiner()
    {
        return EntityType.Builder.of(AstroMinerEntity::new, EntityClassification.MISC)
                .size(2.6F, 1.8F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true)
                .build("");
    }

    private static EntityType<EntryPodEntity> entryPod()
    {
        return EntityType.Builder.of(EntryPodEntity::new, EntityClassification.MISC)
                .size(2.6F, 1.8F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true)
                .build("");
    }

    private static EntityType<GrappleEntity> grapple()
    {
        return EntityType.Builder.of(GrappleEntity::new, EntityClassification.MISC)
                .size(0.75F, 0.75F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true)
                .build("");
    }

    private static EntityType<SmallAsteroidEntity> smallAsteroid()
    {
        return EntityType.Builder.of(SmallAsteroidEntity::new, EntityClassification.MISC)
                .size(1.0F, 1.0F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true)
                .build("");
    }

    private static EntityType<Tier3RocketEntity> rocketT3()
    {
        return EntityType.Builder.of(Tier3RocketEntity::new, EntityClassification.MISC)
                .size(1.8F, 6.0F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true)
                .build("");
    }
}
