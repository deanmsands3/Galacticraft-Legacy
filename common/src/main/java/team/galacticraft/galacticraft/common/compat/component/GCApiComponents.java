package team.galacticraft.galacticraft.common.compat.component;

import me.shedaniel.architectury.ExpectPlatform;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;

public class GCApiComponents {
    @ExpectPlatform
    public static ComponentWrapper<? extends GCPlayerStats> getPlayerStats() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ComponentWrapper<? extends FluidTank> getTank() {
        throw new AssertionError();
    }
}
