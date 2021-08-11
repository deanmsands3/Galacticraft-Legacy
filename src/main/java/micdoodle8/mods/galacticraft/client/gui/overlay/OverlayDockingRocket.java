package micdoodle8.mods.galacticraft.client.gui.overlay;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.util.ClientUtil;
import micdoodle8.mods.galacticraft.util.ColorUtil;
import micdoodle8.mods.galacticraft.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OverlayDockingRocket extends Overlay
{
    /**
     * Render the GUI when player is docking a vehicle
     */
    public static void renderDockingOverlay(Minecraft mc, long ticks)
    {
        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(mc, mc.displayWidth, mc.displayHeight);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        mc.entityRenderer.setupOverlayRendering();

        if (mc.player.getRidingEntity() instanceof EntityAutoRocket)
        {
            EntityAutoRocket rocket = (EntityAutoRocket) mc.player.getRidingEntity();

            if (rocket.launchPhase == EnumLaunchPhase.LANDING.ordinal() && rocket.targetVec != null)
            {
                double dX = Math.round((rocket.posX - rocket.targetVec.getX()) * 100.0D) / 100.0D;
                double dY = Math.round((rocket.posY - rocket.targetVec.getY()) * 100.0D) / 100.0D;
                double dZ = Math.round((rocket.posZ - rocket.targetVec.getZ()) * 100.0D) / 100.0D;
                String dXStr = String.valueOf(dX);
                String dYStr = String.valueOf(dY);
                String dZStr = String.valueOf(dZ);

                double targetMotionY = Math.round(Math.max((rocket.posY - rocket.targetVec.getY()) / -100.0D, -0.9D) * 100.0D) / 100.0D;
                double currentMotionY = Math.round(rocket.motionY * 100.0D) / 100.0D;
                double dMY = Math.floor((targetMotionY - currentMotionY) * 300);
                int dMotionY = (int) Math.max(1, Math.min(255, dMY));
                int dMotionYN = (int) Math.max(1, Math.min(255, -dMY));
                String targetMotionYStr = GCCoreUtil.translate("gui.docking_rocket.target_vel.name") + ": " + String.format("%.2f", targetMotionY);
                String currentMotionYStr = GCCoreUtil.translate("gui.docking_rocket.current_vel.name") + ": " + String.format("%.2f", currentMotionY);

                int red = ColorUtil.to32BitColor(dMY > 0 ? 0 : dMotionYN, 255, 255, 255);
                int green = ColorUtil.to32BitColor(dMY < 0 ? 0 : dMotionY, 255, 255, 255);
                int grey = ColorUtil.to32BitColor(255, 220, 220, 220);

//                if (dMY > 25)
//                {
//                    String warning = GCCoreUtil.translateWithFormat("gui.docking_rocket.warning.name.0", GameSettings.getKeyDisplayString(KeyHandlerClient.spaceKey.getKeyCode()));
//                    mc.fontRenderer.drawString(warning, width / 2 - mc.fontRenderer.getStringWidth(warning) / 2, height / 3 - 50, green);
//                }
//                else if (dMY < -25)
//                {
//                    String warning2 = GCCoreUtil.translateWithFormat("gui.docking_rocket.warning.name.1", GameSettings.getKeyDisplayString(KeyHandlerClient.leftShiftKey.getKeyCode()));
//                    mc.fontRenderer.drawString(warning2, width / 2 - mc.fontRenderer.getStringWidth(warning2) / 2, height / 3 - 35, red);
//                }

                mc.fontRenderer.drawString(targetMotionYStr, width - mc.fontRenderer.getStringWidth(targetMotionYStr) - 50, height / 3 + 50, grey);
                mc.fontRenderer.drawString(currentMotionYStr, width - mc.fontRenderer.getStringWidth(currentMotionYStr) - 50, height / 3 + 35, grey);

                mc.fontRenderer.drawString(GCCoreUtil.translate("gui.docking_rocket.distance_from.name"), 50, height / 3 + 15, grey);
                mc.fontRenderer.drawString("X: " + dXStr, 50, height / 3 + 35, Math.abs(dX) > 15 ? red : grey);
                mc.fontRenderer.drawString("Y: " + dYStr, 50, height / 3 + 45, Math.abs(dY) > 50 || Math.abs(dY) < 1.9 ? grey : ticks / 10 % 2 == 0 ? red : grey);
                mc.fontRenderer.drawString("Z: " + dZStr, 50, height / 3 + 55, Math.abs(dZ) > 15 ? red : grey);
            }
        }
    }
}
