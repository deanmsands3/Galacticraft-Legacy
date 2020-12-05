package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class OverlayLander extends Overlay
{
    /**
     * Render the GUI when player is in inventory
     */
    public static void renderLanderOverlay(long ticks)
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.getVehicle() instanceof EntityLander)
        {
            int width = (int) (mc.mouseHandler.xpos() * (double) mc.getWindow().getGuiScaledWidth() / (double) mc.getWindow().getScreenWidth());
            int height = (int) (mc.mouseHandler.ypos() * (double) mc.getWindow().getGuiScaledHeight() / (double) mc.getWindow().getScreenHeight());
//        mc.entityRenderer.setupOverlayRendering();

            GlStateManager._pushMatrix();
            GlStateManager._scalef(2.0F, 2.0F, 0.0F);

            if (mc.player.getVehicle().getDeltaMovement().y < -2.0)
            {
                mc.font.draw(GCCoreUtil.translate("gui.warning"), width / 4.0F - mc.font.width(GCCoreUtil.translate("gui.warning")) / 2.0F, height / 8.0F - 20, ColorUtil.to32BitColor(255, 255, 0, 0));
                final int alpha = (int) (200 * (Math.sin(ticks) * 0.5F + 0.5F)) + 5;
                final String press1 = GCCoreUtil.translate("gui.lander.warning2");
                final String press2 = GCCoreUtil.translate("gui.lander.warning3");
                mc.font.draw(press1 + KeyHandlerClient.spaceKey.getTranslatedKeyMessage() + press2, width / 4.0F - mc.font.width(press1 + KeyHandlerClient.spaceKey.getTranslatedKeyMessage() + press2) / 2.0F, height / 8.0F, ColorUtil.to32BitColor(alpha, alpha, alpha, alpha));
            }

            GlStateManager._popMatrix();

            if (mc.player.getVehicle().getDeltaMovement().y != 0.0D)
            {
                String string = GCCoreUtil.translate("gui.lander.velocity") + ": " + Math.round(mc.player.getVehicle().getDeltaMovement().y * 1000) / 100.0D + " " + GCCoreUtil.translate("gui.lander.velocityu");
                int color = ColorUtil.to32BitColor(255, (int) Math.floor(Math.abs(mc.player.getVehicle().getDeltaMovement().y) * 51.0D), 255 - (int) Math.floor(Math.abs(mc.player.getVehicle().getDeltaMovement().y) * 51.0D), 0);
                mc.font.draw(string, width / 2.0F - mc.font.width(string) / 2.0F, height / 3.0F, color);
            }
        }
    }
}
