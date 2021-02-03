package team.galacticraft.fabric;

import net.fabricmc.api.ModInitializer;
import team.galacticraft.common.GalacticraftCommon;

public class GalacticraftFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        GalacticraftCommon.init();
    }
}
