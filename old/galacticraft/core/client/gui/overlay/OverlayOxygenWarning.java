package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OverlayOxygenWarning extends Overlay
{
    /**
     * Render the GUI when player is in inventory
     */
    public static void renderOxygenWarningOverlay(long ticks)
    {
        Minecraft mc = Minecraft.getInstance();
        int width = (int) (mc.mouseHandler.xpos() * (double) mc.getWindow().getGuiScaledWidth() / (double) mc.getWindow().getScreenWidth());
        int height = (int) (mc.mouseHandler.ypos() * (double) mc.getWindow().getGuiScaledHeight() / (double) mc.getWindow().getScreenHeight());
//        mc.entityRenderer.setupOverlayRendering();
        GlStateManager._enableColorMaterial();
        GlStateManager._pushMatrix();
        GlStateManager._scalef(2.0F, 2.0F, 0.0F);
        GlStateManager._enableBlend();
        mc.font.draw(GCCoreUtil.translate("gui.warning"), width / 4.0F - mc.font.width(GCCoreUtil.translate("gui.warning")) / 2.0F, height / 8.0F - 20, ColorUtil.to32BitColor(255, 255, 0, 0));
        final int alpha = (int) (200 * (Math.sin(ticks / 1.5F) * 0.5F + 0.5F)) + 5;
        mc.font.draw(GCCoreUtil.translate("gui.oxygen.warning"), width / 4.0F - mc.font.width(GCCoreUtil.translate("gui.oxygen.warning")) / 2.0F, height / 8.0F, ColorUtil.to32BitColor(alpha, 255, 0, 0));
        GlStateManager._popMatrix();
    }
}
