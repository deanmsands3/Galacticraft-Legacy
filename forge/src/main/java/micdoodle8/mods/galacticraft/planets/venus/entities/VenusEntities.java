package micdoodle8.mods.galacticraft.planets.venus.entities;

import static micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks.register;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VenusEntities
{
    public static final EntityType<EntityEntryPodVenus> VENUS_ENTRY_POD = EntityType.Builder.of(EntityEntryPodVenus::new, EntityClassification.MISC)
            .size(1.5F, 3.0F)
            .immuneToFire()
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<JuicerEntity> JUICER = EntityType.Builder.of(JuicerEntity::new, EntityClassification.MONSTER)
            .size(0.95F, 0.6F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<SpiderQueenEntity> SPIDER_QUEEN = EntityType.Builder.of(SpiderQueenEntity::new, EntityClassification.MONSTER)
            .size(1.4F, 0.9F)
            .immuneToFire()
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<SpiderQueenWebEntity> SPIDER_QUEEN_WEB = EntityType.Builder.of(SpiderQueenWebEntity::new, EntityClassification.MISC)
            .size(0.5F, 0.5F)
            .immuneToFire()
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt)
    {
        IForgeRegistry<EntityType<?>> r = evt.getRegistry();
        register(r, VENUS_ENTRY_POD, VenusEntityNames.VENUS_ENTRY_POD);
        register(r, JUICER, VenusEntityNames.JUICER);
        register(r, SPIDER_QUEEN, VenusEntityNames.SPIDER_QUEEN);
        register(r, SPIDER_QUEEN_WEB, VenusEntityNames.SPIDER_QUEEN_WEB);
    }
}