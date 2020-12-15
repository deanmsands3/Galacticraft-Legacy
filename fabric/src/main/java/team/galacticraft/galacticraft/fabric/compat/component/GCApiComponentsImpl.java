package team.galacticraft.galacticraft.fabric.compat.component;

import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import nerdhub.cardinal.components.api.ComponentRegistry;
import net.minecraft.resources.ResourceLocation;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.compat.component.ComponentWrapper;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;
import team.galacticraft.galacticraft.fabric.compat.fluid.FluidTankWrapper;
import team.galacticraft.galacticraft.fabric.compat.fluid.SerializableFixedFluidInv;

public class GCApiComponentsImpl {
    private static final FabricComponentWrapper<GCPlayerStats> GC_PLAYER_STATS = new FabricComponentWrapper<>(ComponentRegistry.INSTANCE.registerIfAbsent(new ResourceLocation(Constants.MOD_ID_CORE, "player_stats"), (Class<FabricComponent<GCPlayerStats>>) (Object)FabricComponent.class));
    private static final FabricComponentWrapperLBA<FluidTankWrapper<? extends FixedFluidInv>, FixedFluidInv> FLUID_TANK = new FabricComponentWrapperLBA<>(FluidAttributes.FIXED_INV, SerializableFixedFluidInv::create);

    private GCApiComponentsImpl() {}

    public static ComponentWrapper<? extends GCPlayerStats> getPlayerStats() {
        return GC_PLAYER_STATS;
    }

    public static ComponentWrapper<? extends FluidTank> getTank() {
        return FLUID_TANK;
    }

    public static void init() {
    }
}
