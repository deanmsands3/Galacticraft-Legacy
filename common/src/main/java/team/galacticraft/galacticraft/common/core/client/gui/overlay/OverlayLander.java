package team.galacticraft.galacticraft.common.core.client.gui.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import team.galacticraft.galacticraft.core.entities.EntityLander;
import team.galacticraft.galacticraft.core.tick.KeyHandlerClient;
import team.galacticraft.galacticraft.core.util.ColorUtil;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
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
                mc.font.draw(I18n.get("gui.warning"), width / 4.0F - mc.font.width(I18n.get("gui.warning")) / 2.0F, height / 8.0F - 20, ColorUtil.to32BitColor(255, 255, 0, 0));
                final int alpha = (int) (200 * (Math.sin(ticks) * 0.5F + 0.5F)) + 5;
                final String press1 = I18n.get("gui.lander.warning2");
                final String press2 = I18n.get("gui.lander.warning3");
                mc.font.draw(press1 + KeyHandlerClient.spaceKey.getTranslatedKeyMessage() + press2, width / 4.0F - mc.font.width(press1 + KeyHandlerClient.spaceKey.getTranslatedKeyMessage() + press2) / 2.0F, height / 8.0F, ColorUtil.to32BitColor(alpha, alpha, alpha, alpha));
            }

            GlStateManager._popMatrix();

            if (mc.player.getVehicle().getDeltaMovement().y != 0.0D)
            {
                String string = I18n.get("gui.lander.velocity") + ": " + Math.round(mc.player.getVehicle().getDeltaMovement().y * 1000) / 100.0D + " " + I18n.get("gui.lander.velocityu");
                int color = ColorUtil.to32BitColor(255, (int) Math.floor(Math.abs(mc.player.getVehicle().getDeltaMovement().y) * 51.0D), 255 - (int) Math.floor(Math.abs(mc.player.getVehicle().getDeltaMovement().y) * 51.0D), 0);
                mc.font.draw(string, width / 2.0F - mc.font.width(string) / 2.0F, height / 3.0F, color);
            }
        }
    }
}
