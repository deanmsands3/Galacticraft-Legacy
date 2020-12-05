package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class OverlayLaunchCountdown extends Overlay
{
    public static void renderCountdownOverlay()
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.getVehicle() instanceof EntitySpaceshipBase && !((EntitySpaceshipBase) mc.player.getVehicle()).getLaunched())
        {
            GlStateManager._disableLighting();
            int count = ((EntitySpaceshipBase) mc.player.getVehicle()).timeUntilLaunch / 2;

            count = (int) Math.floor(count / 10.0F);

            int width = (int) (mc.mouseHandler.xpos() * (double) mc.getWindow().getGuiScaledWidth() / (double) mc.getWindow().getScreenWidth());
            int height = (int) (mc.mouseHandler.ypos() * (double) mc.getWindow().getGuiScaledHeight() / (double) mc.getWindow().getScreenHeight());
//        mc.entityRenderer.setupOverlayRendering();

            GlStateManager._pushMatrix();

            if (count <= 10)
            {
                GlStateManager._scalef(4.0F, 4.0F, 0.0F);
                mc.font.draw(String.valueOf(count), width / 8.0F - mc.font.width(String.valueOf(count)) / 2.0F, height / 20.0F, ColorUtil.to32BitColor(255, 255, 0, 0));
            }
            else
            {
                GlStateManager._scalef(2.0F, 2.0F, 0.0F);
                mc.font.draw(String.valueOf(count), width / 4.0F - mc.font.width(String.valueOf(count)) / 2.0F, height / 8.0F, ColorUtil.to32BitColor(255, 255, 0, 0));
            }

            GlStateManager._popMatrix();
            GlStateManager._enableLighting();
        }
    }
}
