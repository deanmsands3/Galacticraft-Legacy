package team.galacticraft.galacticraft.common.core.util;

import team.galacticraft.galacticraft.common.core.client.gui.screen.GuiMissingCore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

public class ThreadRequirementMissing extends Thread
{
    private static EnvType threadSide;
    public static ThreadRequirementMissing INSTANCE;

    public ThreadRequirementMissing(EnvType threadSide)
    {
        super("Galacticraft Requirement Check Thread");
        this.setDaemon(true);
        ThreadRequirementMissing.threadSide = threadSide;
    }

    public static void beginCheck(EnvType threadSide)
    {
        INSTANCE = new ThreadRequirementMissing(threadSide);
        INSTANCE.start();
    }

    @Override
    public void run()
    {
//        if (!ModList.get().isLoaded("micdoodlecore"))
//        {
//            if (ThreadRequirementMissing.threadSide.isServer())
//            {
//                FMLCommonHandler.instance().getMinecraftServerInstance().logSevere("===================================================================");
//                FMLCommonHandler.instance().getMinecraftServerInstance().logSevere("MicdoodleCore not found in mods folder. Galacticraft will not load.");
//                FMLCommonHandler.instance().getMinecraftServerInstance().logSevere("===================================================================");
//            }
//            else
//            {
//                ThreadRequirementMissing.openGuiClient();
//            }
//        }
    }

    @Environment(EnvType.CLIENT)
    private static void openGuiClient()
    {
        Minecraft.getInstance().setScreen(new GuiMissingCore());
    }
}
