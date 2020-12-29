package micdoodle8.mods.galacticraft.planets.mars.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import static micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks.register;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MarsEntities
{
//    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Constants.MOD_ID_PLANETS);

    public static final EntityType<CargoRocketEntity> CARGO_ROCKET = EntityType.Builder.create(CargoRocketEntity::new, EntityClassification.MISC)
            .size(0.98F, 2F)
            .immuneToFire()
            .setUpdateInterval(1)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<CreeperBossEntity> CREEPER_BOSS = EntityType.Builder.create(CreeperBossEntity::new, EntityClassification.MONSTER)
            .size(2.0F, 7.0F)
            .immuneToFire()
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<LandingBalloonsEntity> LANDING_BALLOONS = EntityType.Builder.create(LandingBalloonsEntity::new, EntityClassification.MISC)
            .size(2.0F, 2.0F)
            .immuneToFire()
            .setUpdateInterval(5)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<TNTProjectileEntity> TNT_PROJECTILE = EntityType.Builder.create(TNTProjectileEntity::new, EntityClassification.MISC)
            .size(1.0F, 1.0F)
            .immuneToFire()
            .setUpdateInterval(1)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<SlimelingEntity> SLIMELING = EntityType.Builder.create(SlimelingEntity::new, EntityClassification.CREATURE)
            .size(0.45F, 0.7F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<SludgelingEntity> SLUDGELING = EntityType.Builder.create(SludgelingEntity::new, EntityClassification.MONSTER)
            .size(0.3F, 0.2F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<Tier2RocketEntity> TIER_2_ROCKET = EntityType.Builder.create(Tier2RocketEntity::new, EntityClassification.MISC)
            .size(1.2F, 4.5F)
            .immuneToFire()
            .setUpdateInterval(1)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
        IForgeRegistry<EntityType<?>> r = evt.getRegistry();
        register(r, CARGO_ROCKET, MarsEntityNames.CARGO_ROCKET);
        register(r, CREEPER_BOSS, MarsEntityNames.CREEPER_BOSS);
        register(r, LANDING_BALLOONS, MarsEntityNames.LANDING_BALLOONS);
        register(r, TNT_PROJECTILE, MarsEntityNames.TNT_PROJECTILE);
        register(r, SLIMELING, MarsEntityNames.SLIMELING);
        register(r, SLUDGELING, MarsEntityNames.SLUDGELING);
        register(r, TIER_2_ROCKET, MarsEntityNames.TIER_2_ROCKET);
    }
}
