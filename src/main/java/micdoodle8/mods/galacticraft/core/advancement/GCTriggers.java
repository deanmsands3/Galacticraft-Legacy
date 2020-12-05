package micdoodle8.mods.galacticraft.core.advancement;

import java.lang.reflect.Method;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.advancement.criterion.GenericTrigger;
import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class GCTriggers
{
    public static final GenericTrigger LAUNCH_ROCKET = new GenericTrigger("launch_rocket")
    {
        @Override
        public CriterionTriggerInstance createInstance(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext)
        {
            return new Instance(getId())
            {
                @Override
                public boolean test(ServerPlayer player)
                {
                    if (player.getVehicle() instanceof EntitySpaceshipBase)
                    {
                        if (((EntitySpaceshipBase) player.getVehicle()).launchPhase >= EntitySpaceshipBase.EnumLaunchPhase.LAUNCHED.ordinal())
                        {
                            return player.getVehicle().getPassengers().size() >= 1 && player.getVehicle().getPassengers().get(0) instanceof ServerPlayer;
                        }
                    }
                    return false;
                }
            };
        }
    };
    public static final GenericTrigger FIND_MOON_BOSS = new GenericTrigger("boss_moon")
    {
        @Override
        public CriterionTriggerInstance createInstance(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext)
        {
            return new Instance(getId())
            {
                @Override
                public boolean test(ServerPlayer player)
                {
                    return ((ServerLevel) player.level).getEntities().filter((entity -> entity instanceof EntitySkeletonBoss && entity.squaredDistanceTo(player) < 400)).count() >= 1;
                }
            };
        }
    };
    public static final GenericTrigger CREATE_SPACE_STATION = new GenericTrigger("create_space_station")
    {
        @Override
        public CriterionTriggerInstance createInstance(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext)
        {
            return new Instance(getId())
            {
                @Override
                public boolean test(ServerPlayer player)
                {
                    return !GCPlayerStats.get(player).getSpaceStationDimensionData().isEmpty();
                }
            };
        }
    };

    public static void registerTriggers()
    {
        Method register = null;
        try
        {
            Class clazz = CriteriaTriggers.class;
            Method[] mm = clazz.getDeclaredMethods();
            for (Method m : mm)
            {
                Class<?>[] params = m.getParameterTypes();
                if (params != null && params.length == 1 && params[0] == CriterionTrigger.class)
                {
                    register = m;
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (register != null)
        {
            try
            {
                register.invoke(null, GCTriggers.LAUNCH_ROCKET);
                register.invoke(null, GCTriggers.FIND_MOON_BOSS);
                register.invoke(null, GCTriggers.CREATE_SPACE_STATION);
            }
            catch (Exception ignore)
            {
            }
        }
    }
}