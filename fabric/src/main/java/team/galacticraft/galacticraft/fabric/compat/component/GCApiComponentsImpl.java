package team.galacticraft.galacticraft.fabric.compat.component;

import alexiil.mc.lib.attributes.AttributeProviderItem;
import nerdhub.cardinal.components.api.ComponentRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.compat.component.ComponentProvidingItem;
import team.galacticraft.galacticraft.common.compat.component.ComponentWrapper;

public class GCApiComponentsImpl {
    private static final ComponentWrapper<GCPlayerStats> GC_PLAYER_STATS = new FabricComponentWrapper<>(ComponentRegistry.INSTANCE.<FabricComponent<GCPlayerStats>>registerIfAbsent(new ResourceLocation(Constants.MOD_ID_CORE, "player_stats"), (Class<FabricComponent<GCPlayerStats>>) (Object)FabricComponent.class));

    private GCApiComponentsImpl() {}

    public static ComponentWrapper<GCPlayerStats> getPlayerStats() {
        return GC_PLAYER_STATS;
    }

    public static void init() {
    }
}
