package team.galacticraft.galacticraft.forge;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import team.galactiacraft.galacticraft.common.GalacticraftCommon;
import team.galactiacraft.galacticraft.common.utils.CommonConstants;

@Mod(CommonConstants.MOD_ID)
public class GalacticraftForge {

    public GalacticraftForge() {
        EventBuses.registerModEventBus(CommonConstants.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        GalacticraftCommon.init();
    }
}
