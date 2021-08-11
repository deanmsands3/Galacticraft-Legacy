package micdoodle8.mods.galacticraft.client.gui.overlay;

import micdoodle8.mods.galacticraft.util.ClientUtil;
import micdoodle8.mods.galacticraft.util.ColorUtil;
import micdoodle8.mods.galacticraft.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OverlayOxygenWarning extends Overlay
{
    /**
     * Render the GUI when player is in inventory
     */
    public static void renderOxygenWarningOverlay(Minecraft mc, long ticks)
    {
        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(mc, mc.displayWidth, mc.displayHeight);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 0.0F);
        GlStateManager.enableBlend();
        mc.fontRenderer.drawString(GCCoreUtil.translate("gui.warning"), width / 4 - mc.fontRenderer.getStringWidth(GCCoreUtil.translate("gui.warning")) / 2, height / 8 - 20, ColorUtil.to32BitColor(255, 255, 0, 0));
        final int alpha = (int) (200 * (Math.sin(ticks / 1.5F) * 0.5F + 0.5F)) + 5;
        mc.fontRenderer.drawString(GCCoreUtil.translate("gui.oxygen.warning"), width / 4 - mc.fontRenderer.getStringWidth(GCCoreUtil.translate("gui.oxygen.warning")) / 2, height / 8, ColorUtil.to32BitColor(alpha, 255, 0, 0));
        GlStateManager.popMatrix();
    }
}
