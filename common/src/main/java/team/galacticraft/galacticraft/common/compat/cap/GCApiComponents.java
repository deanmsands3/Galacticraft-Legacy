package team.galacticraft.galacticraft.common.compat.cap;

import me.shedaniel.architectury.ExpectPlatform;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;

public class GCApiComponents {
    @ExpectPlatform
    public static ComponentWrapper<GCPlayerStats> getPlayerStats() {
        throw new AssertionError();
    }
}
