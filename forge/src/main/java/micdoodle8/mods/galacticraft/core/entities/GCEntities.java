package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

import static micdoodle8.mods.galacticraft.core.GCBlocks.register;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GCEntities
{
//    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Constants.MOD_ID_CORE);

    public static final EntityType<EvolvedSpiderEntity> EVOLVED_SPIDER = EntityType.Builder.create(EvolvedSpiderEntity::new, EntityClassification.MONSTER)
            .size(1.5F, 1.0F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EvolvedZombieEntity> EVOLVED_ZOMBIE = EntityType.Builder.create(EvolvedZombieEntity::new, EntityClassification.MONSTER)
            .size(0.6F, 1.95F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EvolvedCreeperEntity> EVOLVED_CREEPER = EntityType.Builder.create(EvolvedCreeperEntity::new, EntityClassification.MONSTER)
            .size(0.7F, 2.2F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EvolvedSkeletonEntity> EVOLVED_SKELETON = EntityType.Builder.create(EvolvedSkeletonEntity::new, EntityClassification.MONSTER)
            .size(0.6F, 1.99F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EvolvedEndermanEntity> EVOLVED_ENDERMAN = EntityType.Builder.create(EvolvedEndermanEntity::new, EntityClassification.MONSTER)
            .size(0.6F, 2.9F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EvolvedWitchEntity> EVOLVED_WITCH = EntityType.Builder.create(EvolvedWitchEntity::new, EntityClassification.MONSTER)
            .size(0.6F, 1.95F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EvolvedSkeletonBossEntity> EVOLVED_SKELETON_BOSS = EntityType.Builder.create(EvolvedSkeletonBossEntity::new, EntityClassification.MONSTER)
            .size(1.5F, 4.0F)
            .immuneToFire()
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    //    public static final EntityType<EntityAlienVillager> ALIEN_VILLAGER = register(GCEntityNames.alienVillager, GCEntities::alienVillager);
    public static final EntityType<Tier1RocketEntity> TIER_1_ROCKET = EntityType.Builder.create(Tier1RocketEntity::new, EntityClassification.MISC)
            .size(1.2F, 3.5F)
            .setUpdateInterval(1)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<MeteorEntity> METEOR = EntityType.Builder.create(MeteorEntity::new, EntityClassification.MISC)
            .size(1.0F, 1.0F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<MeteorEntity> LARGE_METEOR = EntityType.Builder.create(MeteorEntity::new, EntityClassification.MISC)
            .size(6.0F, 6.0F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<BuggyEntity> BUGGY = EntityType.Builder.create(BuggyEntity::new, EntityClassification.MISC)
            .size(1.4F, 0.6F)
            .immuneToFire()
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<FlagEntity> FLAG = EntityType.Builder.<FlagEntity>create(FlagEntity::new, EntityClassification.MISC)
            .size(0.4F, 3F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<ParachestEntity> PARACHEST = EntityType.Builder.<ParachestEntity>create(ParachestEntity::new, EntityClassification.MISC)
            .size(1.0F, 1.0F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<LanderEntity> LANDER = EntityType.Builder.<LanderEntity>create(LanderEntity::new, EntityClassification.MISC)
            .size(3.0F, 4.25F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<MeteorChunkEntity> METEOR_CHUNK = EntityType.Builder.<MeteorChunkEntity>create(MeteorChunkEntity::new, EntityClassification.MISC)
            .size(0.25F, 0.25F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<CelestialScreenEntity> CELESTIAL_SCREEN = EntityType.Builder.<CelestialScreenEntity>create(CelestialScreenEntity::new, EntityClassification.MISC)
            .size(3.0F, 1.0F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<HangingSchematicEntity> HANGING_SCHEMATIC = EntityType.Builder.<HangingSchematicEntity>create(HangingSchematicEntity::new, EntityClassification.MISC)
            .size(0.5F, 0.5F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
        IForgeRegistry<EntityType<?>> r = evt.getRegistry();
        register(r, EVOLVED_SPIDER, GCEntityNames.EVOLVED_SPIDER);
        register(r, EVOLVED_ZOMBIE, GCEntityNames.EVOLVED_ZOMBIE);
        register(r, EVOLVED_CREEPER, GCEntityNames.EVOLVED_CREEPER);
        register(r, EVOLVED_SKELETON, GCEntityNames.EVOLVED_SKELETON);
        register(r, EVOLVED_ENDERMAN, GCEntityNames.EVOLVED_ENDERMAN);
        register(r, EVOLVED_WITCH, GCEntityNames.EVOLVED_WITCH);
        register(r, EVOLVED_SKELETON_BOSS, GCEntityNames.EVOLVED_SKELETON_BOSS);
        //    register(r, ALIEN_VILLAGER, GCEntityNames.ALIEN_VILLAGER);
        register(r, TIER_1_ROCKET, GCEntityNames.TIER_1_ROCKET);
        register(r, METEOR, GCEntityNames.METEOR);
        register(r, LARGE_METEOR, GCEntityNames.LARGE_METEOR);
        register(r, BUGGY, GCEntityNames.BUGGY);
        register(r, FLAG, GCEntityNames.FLAG);
        register(r, PARACHEST, GCEntityNames.PARACHEST);
        register(r, LANDER, GCEntityNames.LANDER);
        register(r, METEOR_CHUNK, GCEntityNames.METEOR_CHUNK);
        register(r, CELESTIAL_SCREEN, GCEntityNames.CELESTIAL_SCREEN);
        register(r, HANGING_SCHEMATIC, GCEntityNames.HANGING_SCHEMATIC);
    }
}
