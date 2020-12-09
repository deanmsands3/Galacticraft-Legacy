package team.galacticraft.galacticraft.common.core.client.gui.screen;

import team.galacticraft.galacticraft.common.api.client.tabs.AbstractTab;
import team.galacticraft.galacticraft.core.GCItems;
import team.galacticraft.galacticraft.core.GalacticraftCore;
import team.galacticraft.galacticraft.core.network.PacketSimple;
import team.galacticraft.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class InventoryTabGalacticraft extends AbstractTab
{
    public InventoryTabGalacticraft()
    {
        super(0, new ItemStack(GCItems.oxMask));
    }

    @Override
    public void onTabClicked()
    {
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_EXTENDED_INVENTORY, GCCoreUtil.getDimensionType(Minecraft.getInstance().level), new Object[] {}));
        ClientProxyCore.playerClientHandler.onBuild(0, Minecraft.getInstance().player);
    }

    @Override
    public boolean shouldAddToList()
    {
        return true;
    }
}
