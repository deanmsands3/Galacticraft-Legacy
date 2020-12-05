package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class OverlayDockingRocket extends Overlay
{
    /**
     * Render the GUI when player is docking a vehicle
     */
    public static void renderDockingOverlay(long ticks)
    {
        Minecraft mc = Minecraft.getInstance();
//        mc.entityRenderer.setupOverlayRendering(); TODO Needed?
        int width = (int) (mc.mouseHandler.xpos() * (double) mc.getWindow().getGuiScaledWidth() / (double) mc.getWindow().getScreenWidth());
        int height = (int) (mc.mouseHandler.ypos() * (double) mc.getWindow().getGuiScaledHeight() / (double) mc.getWindow().getScreenHeight());

        if (mc.player.getVehicle() instanceof EntityAutoRocket)
        {
            EntityAutoRocket rocket = (EntityAutoRocket) mc.player.getVehicle();

            if (rocket.launchPhase == EnumLaunchPhase.LANDING.ordinal() && rocket.targetVec != null)
            {
                double dX = Math.round((rocket.getX() - rocket.targetVec.getX()) * 100.0D) / 100.0D;
                double dY = Math.round((rocket.getY() - rocket.targetVec.getY()) * 100.0D) / 100.0D;
                double dZ = Math.round((rocket.getZ() - rocket.targetVec.getZ()) * 100.0D) / 100.0D;
                String dXStr = String.valueOf(dX);
                String dYStr = String.valueOf(dY);
                String dZStr = String.valueOf(dZ);

                double targetMotionY = Math.round(Math.max((rocket.getY() - rocket.targetVec.getY()) / -100.0D, -0.9D) * 100.0D) / 100.0D;
                double currentMotionY = Math.round(rocket.getDeltaMovement().y * 100.0D) / 100.0D;
                double dMY = Math.floor((targetMotionY - currentMotionY) * 300);
                int dMotionY = (int) Math.max(1, Math.min(255, dMY));
                int dMotionYN = (int) Math.max(1, Math.min(255, -dMY));
                String targetMotionYStr = GCCoreUtil.translate("gui.docking_rocket.target_vel") + ": " + String.format("%.2f", targetMotionY);
                String currentMotionYStr = GCCoreUtil.translate("gui.docking_rocket.current_vel") + ": " + String.format("%.2f", currentMotionY);

                int red = ColorUtil.to32BitColor(dMY > 0 ? 0 : dMotionYN, 255, 255, 255);
                int green = ColorUtil.to32BitColor(dMY < 0 ? 0 : dMotionY, 255, 255, 255);
                int grey = ColorUtil.to32BitColor(255, 220, 220, 220);

//                if (dMY > 25)
//                {
//                    String warning = GCCoreUtil.translateWithFormat("gui.docking_rocket.warning.name.0", GameSettings.getKeymessage(KeyHandlerClient.spaceKey.getKeyCode()));
//                    mc.fontRenderer.drawString(warning, width / 2 - mc.fontRenderer.getStringWidth(warning) / 2, height / 3 - 50, green);
//                }
//                else if (dMY < -25)
//                {
//                    String warning2 = GCCoreUtil.translateWithFormat("gui.docking_rocket.warning.name.1", GameSettings.getKeymessage(KeyHandlerClient.leftShiftKey.getKeyCode()));
//                    mc.fontRenderer.drawString(warning2, width / 2 - mc.fontRenderer.getStringWidth(warning2) / 2, height / 3 - 35, red);
//                }

                mc.font.draw(targetMotionYStr, width - mc.font.width(targetMotionYStr) - 50, height / 3.0F + 50, grey);
                mc.font.draw(currentMotionYStr, width - mc.font.width(currentMotionYStr) - 50, height / 3.0F + 35, grey);

                mc.font.draw(GCCoreUtil.translate("gui.docking_rocket.distance_from"), 50, height / 3.0F + 15, grey);
                mc.font.draw("X: " + dXStr, 50, height / 3.0F + 35, Math.abs(dX) > 15 ? red : grey);
                mc.font.draw("Y: " + dYStr, 50, height / 3.0F + 45, Math.abs(dY) > 50 || Math.abs(dY) < 1.9 ? grey : ticks / 10 % 2 == 0 ? red : grey);
                mc.font.draw("Z: " + dZStr, 50, height / 3.0F + 55, Math.abs(dZ) > 15 ? red : grey);
            }
        }
    }
}
