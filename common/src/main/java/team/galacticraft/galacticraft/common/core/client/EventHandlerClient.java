package team.galacticraft.galacticraft.common.core.client;

import com.mojang.blaze3d.platform.GlStateManager;
import team.galacticraft.galacticraft.common.api.entity.ICameraZoomEntity;
import team.galacticraft.galacticraft.common.api.event.client.CelestialBodyRenderEvent;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GalacticraftCore;
import team.galacticraft.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import team.galacticraft.galacticraft.core.network.PacketSimple;
import team.galacticraft.galacticraft.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE)
public class EventHandlerClient
{
    public static Minecraft mc = Minecraft.getInstance();
    public static boolean sneakRenderOverride;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    //Lowest priority to do the PushMatrix last, just before vanilla RenderPlayer - this also means if it gets cancelled first by another mod, this will never be called
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event)
    {
        GL11.glPushMatrix();

        final Player player = event.getPlayer();

        if (player.getVehicle() instanceof ICameraZoomEntity && player == Minecraft.getInstance().player
                && Minecraft.getInstance().options.thirdPersonView == 0)
        {
            Entity entity = player.getVehicle();
            float rotateOffset = ((ICameraZoomEntity) entity).getRotateOffset();
            if (rotateOffset > -10F)
            {
                rotateOffset += ClientProxyCore.PLAYER_Y_OFFSET;
                GL11.glTranslatef(0, -rotateOffset, 0);
                float anglePitch = entity.xRotO + (entity.xRot - entity.xRotO) * event.getPartialRenderTick();
                float angleYaw = entity.yRotO + (entity.yRot - entity.yRotO) * event.getPartialRenderTick();
                GL11.glRotatef(-angleYaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(anglePitch, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(0, rotateOffset, 0);
            }
        }

        if (player instanceof LocalPlayer)
        {
            sneakRenderOverride = true;
        }

        //Gravity - freefall - jetpack changes in player model orientation can go here
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    //Highest priority to do the PushMatrix first, just after vanilla RenderPlayer
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event)
    {
        GL11.glPopMatrix();

        if (event.getPlayer() instanceof LocalPlayer)
        {
            sneakRenderOverride = false;
        }
    }

    @SubscribeEvent
    public static void onRenderPlanetPre(CelestialBodyRenderEvent.Pre event)
    {
        if (event.celestialBody == GalacticraftCore.planetOverworld)
        {
            if (!ClientProxyCore.overworldTextureRequestSent)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_OVERWORLD_IMAGE, GCCoreUtil.getDimensionType(mc.level), new Object[]{}));
                ClientProxyCore.overworldTextureRequestSent = true;
            }

            if (ClientProxyCore.overworldTexturesValid)
            {
                event.celestialBodyTexture = null;
                GlStateManager._bindTexture(ClientProxyCore.overworldTextureClient.getId());
            }
        }
    }

    @SubscribeEvent
    public static void onRenderPlanetPost(CelestialBodyRenderEvent.Post event)
    {
        if (mc.screen instanceof GuiCelestialSelection)
        {
            if (event.celestialBody == GalacticraftCore.planetSaturn)
            {
                mc.textureManager.bindTexture(ClientProxyCore.saturnRingTexture);
                float size = ((GuiCelestialSelection) mc.screen).getWidthForCelestialBody(event.celestialBody) / 6.0F;
                ((GuiCelestialSelection) mc.screen).blit(-7.5F * size, -1.75F * size, 15.0F * size, 3.5F * size, 0, 0, 30, 7, false, false, 32, 32);
            }
            else if (event.celestialBody == GalacticraftCore.planetUranus)
            {
                mc.textureManager.bindTexture(ClientProxyCore.uranusRingTexture);
                float size = ((GuiCelestialSelection) mc.screen).getWidthForCelestialBody(event.celestialBody) / 6.0F;
                ((GuiCelestialSelection) mc.screen).blit(-1.75F * size, -7.0F * size, 3.5F * size, 14.0F * size, 0, 0, 7, 28, false, false, 32, 32);
            }
        }
    }
}
