package micdoodle8.mods.galacticraft.core.tick;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.KeyHandler;
import micdoodle8.mods.galacticraft.core.entities.BuggyEntity;
import micdoodle8.mods.galacticraft.core.entities.IControllableEntity;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;

public class KeyHandlerClient extends KeyHandler
{
    public static KeyMapping galaxyMap;
    public static KeyMapping openFuelGui;
    public static KeyMapping toggleAdvGoggles;

    static
    {
        galaxyMap = new KeyMapping(GCCoreUtil.translate("keybind.map"), GLFW.GLFW_KEY_M, Constants.MOD_NAME_SIMPLE);
        openFuelGui = new KeyMapping(GCCoreUtil.translate("keybind.spaceshipinv"), GLFW.GLFW_KEY_F, Constants.MOD_NAME_SIMPLE);
        toggleAdvGoggles = new KeyMapping(GCCoreUtil.translate("keybind.sensortoggle"), GLFW.GLFW_KEY_K, Constants.MOD_NAME_SIMPLE);
        // See ConfigManagerCore.class.get() for actual defaults. These do nothing
    }

    public static KeyMapping accelerateKey;
    public static KeyMapping decelerateKey;
    public static KeyMapping leftKey;
    public static KeyMapping rightKey;
    public static KeyMapping upKey;
    public static KeyMapping downKey;
    public static KeyMapping spaceKey;
    public static KeyMapping leftShiftKey;
    private static final Minecraft mc = Minecraft.getInstance();

    public KeyHandlerClient()
    {
        super(new KeyMapping[]{KeyHandlerClient.galaxyMap, KeyHandlerClient.openFuelGui, KeyHandlerClient.toggleAdvGoggles}, new boolean[]{false, false, false}, KeyHandlerClient.getVanillaKeyBindings(), new boolean[]{false, true, true, true, true, true, true});
    }

    private static KeyMapping[] getVanillaKeyBindings()
    {
        KeyMapping invKey = KeyHandlerClient.mc.options.keyInventory;
        KeyHandlerClient.accelerateKey = KeyHandlerClient.mc.options.keyUp;
        KeyHandlerClient.decelerateKey = KeyHandlerClient.mc.options.keyDown;
        KeyHandlerClient.leftKey = KeyHandlerClient.mc.options.keyLeft;
        KeyHandlerClient.rightKey = KeyHandlerClient.mc.options.keyRight;
        KeyHandlerClient.upKey = KeyHandlerClient.mc.options.keyUp;
        KeyHandlerClient.downKey = KeyHandlerClient.mc.options.keyDown;
        KeyHandlerClient.spaceKey = KeyHandlerClient.mc.options.keyJump;
        KeyHandlerClient.leftShiftKey = KeyHandlerClient.mc.options.keyShift;
        return new KeyMapping[]{invKey, KeyHandlerClient.accelerateKey, KeyHandlerClient.decelerateKey, KeyHandlerClient.leftKey, KeyHandlerClient.rightKey, KeyHandlerClient.spaceKey, KeyHandlerClient.leftShiftKey};
    }

    @Override
    public void keyDown(TickEvent.Type types, KeyMapping kb, boolean tickEnd, boolean isRepeat)
    {
        if (KeyHandlerClient.mc.player != null && tickEnd)
        {
            LocalPlayer playerBase = PlayerUtil.getPlayerBaseClientFromPlayer(KeyHandlerClient.mc.player, false);

            if (playerBase == null)
            {
                return;
            }

            GCPlayerStatsClient stats = GCPlayerStatsClient.get(playerBase);

            if (kb == KeyHandlerClient.galaxyMap)
            {
                if (KeyHandlerClient.mc.screen == null)
                {
//                    KeyHandlerClient.mc.player.openGui(GalacticraftCore.instance, GuiIdsCore.GALAXY_MAP, KeyHandlerClient.mc.world, (int) KeyHandlerClient.mc.player.getPosX(), (int) KeyHandlerClient.mc.player.getPosY(), (int) KeyHandlerClient.mc.player.getPosZ());
                    // TODO Gui
                }
            }
            else if (kb == KeyHandlerClient.openFuelGui)
            {
                if (playerBase.getVehicle() instanceof EntitySpaceshipBase || playerBase.getVehicle() instanceof BuggyEntity)
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_FUEL_GUI, GCCoreUtil.getDimensionType(mc.level), new Object[]{PlayerUtil.getName(playerBase)}));
                }
            }
            else if (kb == KeyHandlerClient.toggleAdvGoggles)
            {
                if (playerBase != null)
                {
                    stats.setUsingAdvancedGoggles(!stats.isUsingAdvancedGoggles());
                }
            }
        }

        if (KeyHandlerClient.mc.player != null && KeyHandlerClient.mc.screen == null)
        {
            int keyNum = -1;

            if (kb == KeyHandlerClient.accelerateKey)
            {
                keyNum = 0;
            }
            else if (kb == KeyHandlerClient.decelerateKey)
            {
                keyNum = 1;
            }
            else if (kb == KeyHandlerClient.leftKey)
            {
                keyNum = 2;
            }
            else if (kb == KeyHandlerClient.rightKey)
            {
                keyNum = 3;
            }
            else if (kb == KeyHandlerClient.spaceKey)
            {
                keyNum = 4;
            }
            else if (kb == KeyHandlerClient.leftShiftKey)
            {
                keyNum = 5;
            }

            Entity entityTest = KeyHandlerClient.mc.player.getVehicle();
            if (entityTest != null && entityTest instanceof IControllableEntity && keyNum != -1)
            {
                IControllableEntity entity = (IControllableEntity) entityTest;

                if (kb == KeyHandlerClient.mc.options.keyInventory)
                {
                    KeyMapping.set(KeyHandlerClient.mc.options.keyInventory.getKey(), false);
                }

                entity.pressKey(keyNum);
            }
            else if (entityTest != null && entityTest instanceof EntityAutoRocket)
            {
                EntityAutoRocket autoRocket = (EntityAutoRocket) entityTest;

                if (autoRocket.launchPhase == EnumLaunchPhase.LANDING.ordinal())
                {
                    if (kb == KeyHandlerClient.leftShiftKey)
                    {
                        autoRocket.setDeltaMovement(autoRocket.getDeltaMovement().x, autoRocket.getDeltaMovement().y - 0.02, autoRocket.getDeltaMovement().z);
                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_SHIP_MOTION_Y, GCCoreUtil.getDimensionType(mc.level), new Object[]{autoRocket.getId(), false}));
                    }

                    if (kb == KeyHandlerClient.spaceKey)
                    {
                        autoRocket.setDeltaMovement(autoRocket.getDeltaMovement().x, autoRocket.getDeltaMovement().y + 0.02, autoRocket.getDeltaMovement().z);
                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_SHIP_MOTION_Y, GCCoreUtil.getDimensionType(mc.level), new Object[]{autoRocket.getId(), true}));
                    }
                }
            }
        }
    }

    @Override
    public void keyUp(TickEvent.Type types, KeyMapping kb, boolean tickEnd)
    {
    }
}