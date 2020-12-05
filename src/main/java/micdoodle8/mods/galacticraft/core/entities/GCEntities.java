package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
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

    public static final EntityType<EntityEvolvedSpider> EVOLVED_SPIDER = EntityType.Builder.of(EntityEvolvedSpider::new, MobCategory.MONSTER)
            .sized(1.5F, 1.0F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityEvolvedZombie> EVOLVED_ZOMBIE = EntityType.Builder.of(EntityEvolvedZombie::new, MobCategory.MONSTER)
            .sized(0.6F, 1.95F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityEvolvedCreeper> EVOLVED_CREEPER = EntityType.Builder.of(EntityEvolvedCreeper::new, MobCategory.MONSTER)
            .sized(0.7F, 2.2F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityEvolvedSkeleton> EVOLVED_SKELETON = EntityType.Builder.of(EntityEvolvedSkeleton::new, MobCategory.MONSTER)
            .sized(0.6F, 1.99F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityEvolvedEnderman> EVOLVED_ENDERMAN = EntityType.Builder.of(EntityEvolvedEnderman::new, MobCategory.MONSTER)
            .sized(0.6F, 2.9F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityEvolvedWitch> EVOLVED_WITCH = EntityType.Builder.of(EntityEvolvedWitch::new, MobCategory.MONSTER)
            .sized(0.6F, 1.95F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntitySkeletonBoss> SKELETON_BOSS = EntityType.Builder.of(EntitySkeletonBoss::new, MobCategory.MONSTER)
            .sized(1.5F, 4.0F)
            .fireImmune()
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    //    public static final EntityType<EntityAlienVillager> ALIEN_VILLAGER = register(GCEntityNames.alienVillager, GCEntities::alienVillager);
    public static final EntityType<EntityTier1Rocket> ROCKET_T1 = EntityType.Builder.of(EntityTier1Rocket::new, MobCategory.MISC)
            .sized(1.2F, 3.5F)
            .setUpdateInterval(1)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityMeteor> METEOR = EntityType.Builder.of(EntityMeteor::new, MobCategory.MISC)
            .sized(1.0F, 1.0F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityMeteor> METEOR_HUGE = EntityType.Builder.of(EntityMeteor::new, MobCategory.MISC)
            .sized(6.0F, 6.0F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityBuggy> BUGGY = EntityType.Builder.of(EntityBuggy::new, MobCategory.MISC)
            .sized(1.4F, 0.6F)
            .fireImmune()
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityFlag> FLAG = EntityType.Builder.<EntityFlag>of(EntityFlag::new, MobCategory.MISC)
            .sized(0.4F, 3F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityParachest> PARA_CHEST = EntityType.Builder.<EntityParachest>of(EntityParachest::new, MobCategory.MISC)
            .sized(1.0F, 1.0F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityLander> LANDER = EntityType.Builder.<EntityLander>of(EntityLander::new, MobCategory.MISC)
            .sized(3.0F, 4.25F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityMeteorChunk> METEOR_CHUNK = EntityType.Builder.<EntityMeteorChunk>of(EntityMeteorChunk::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityCelestialFake> CELESTIAL_FAKE = EntityType.Builder.<EntityCelestialFake>of(EntityCelestialFake::new, MobCategory.MISC)
            .sized(3.0F, 1.0F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityHangingSchematic> HANGING_SCHEMATIC = EntityType.Builder.<EntityHangingSchematic>of(EntityHangingSchematic::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
        IForgeRegistry<EntityType<?>> r = evt.getRegistry();
        register(r, EVOLVED_SPIDER, GCEntityNames.evolvedSpider);
        register(r, EVOLVED_ZOMBIE, GCEntityNames.evolvedZombie);
        register(r, EVOLVED_CREEPER, GCEntityNames.evolvedCreeper);
        register(r, EVOLVED_SKELETON, GCEntityNames.evolvedSkeleton);
        register(r, EVOLVED_ENDERMAN, GCEntityNames.evolvedEnderman);
        register(r, EVOLVED_WITCH, GCEntityNames.evolvedWitch);
        register(r, SKELETON_BOSS, GCEntityNames.skeletonBoss);
        //    register(r, ALIEN_VILLAGER, GCEntityNames.alienVillager);
        register(r, ROCKET_T1, GCEntityNames.rocketTier1);
        register(r, METEOR, GCEntityNames.meteor);
        register(r, METEOR_HUGE, GCEntityNames.meteorHuge);
        register(r, BUGGY, GCEntityNames.buggy);
        register(r, FLAG, GCEntityNames.flag);
        register(r, PARA_CHEST, GCEntityNames.parachest);
        register(r, LANDER, GCEntityNames.lander);
        register(r, METEOR_CHUNK, GCEntityNames.meteorChunk);
        register(r, CELESTIAL_FAKE, GCEntityNames.celestialFake);
        register(r, HANGING_SCHEMATIC, GCEntityNames.hangingSchematic);
    }
}
