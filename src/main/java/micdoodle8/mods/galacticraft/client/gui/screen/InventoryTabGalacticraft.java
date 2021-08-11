package micdoodle8.mods.galacticraft.client.gui.screen;

import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import micdoodle8.mods.galacticraft.init.GCItems;
import micdoodle8.mods.galacticraft.GalacticraftCore;
import micdoodle8.mods.galacticraft.network.PacketSimple;
import micdoodle8.mods.galacticraft.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.util.GCCoreUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;

public class InventoryTabGalacticraft extends AbstractTab
{
    public InventoryTabGalacticraft()
    {
        super(0, 0, 0, new ItemStack(GCItems.oxMask));
    }

    @Override
    public void onTabClicked()
    {
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_EXTENDED_INVENTORY, GCCoreUtil.getDimensionID(FMLClientHandler.instance().getClient().world), new Object[] {}));
        ClientProxyCore.playerClientHandler.onBuild(0, FMLClientHandler.instance().getClientPlayerEntity());
    }

    @Override
    public boolean shouldAddToList()
    {
        return true;
    }
}
