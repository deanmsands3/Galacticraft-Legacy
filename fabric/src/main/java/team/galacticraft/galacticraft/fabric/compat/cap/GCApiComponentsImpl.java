package team.galacticraft.galacticraft.fabric.compat.cap;

import me.shedaniel.architectury.ExpectPlatform;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.resources.ResourceLocation;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.compat.cap.ComponentWrapper;

public class GCApiComponentsImpl {
    private static final ComponentWrapper<GCPlayerStats> GC_PLAYER_STATS = new FabricComponentWrapper<>(ComponentRegistry.INSTANCE.<FabricComponent<GCPlayerStats>>registerIfAbsent(new ResourceLocation(Constants.MOD_ID, "player_stats"), (Class<FabricComponent<GCPlayerStats>>) (Object)FabricComponent.class));

    public static ComponentWrapper<GCPlayerStats> getPlayerStats() {
        return GC_PLAYER_STATS;
    }
}
