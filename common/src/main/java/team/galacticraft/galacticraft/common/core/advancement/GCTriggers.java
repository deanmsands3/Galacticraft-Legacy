package team.galacticraft.galacticraft.common.core.advancement;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntitySpaceshipBase;
import team.galacticraft.galacticraft.common.core.advancement.criterion.GenericTrigger;
import team.galacticraft.galacticraft.common.core.entities.GCEntities;

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
                    return ((ServerLevel) player.level).getEntities(GCEntities.SKELETON_BOSS, entity -> entity.distanceToSqr(player) < 400).size() > 0;
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
        CriteriaTriggers.register(GCTriggers.LAUNCH_ROCKET);
        CriteriaTriggers.register(GCTriggers.FIND_MOON_BOSS);
        CriteriaTriggers.register(GCTriggers.CREATE_SPACE_STATION);
    }
}