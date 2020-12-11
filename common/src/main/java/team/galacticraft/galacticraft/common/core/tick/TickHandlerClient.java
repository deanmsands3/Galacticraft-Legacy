package team.galacticraft.galacticraft.common.core.tick;

import com.google.common.collect.Sets;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.platform.GlStateManager;
import team.galacticraft.galacticraft.common.api.block.IDetectableResource;
import team.galacticraft.galacticraft.common.api.entity.IEntityNoisy;
import team.galacticraft.galacticraft.common.api.entity.IIgnoreShift;
import team.galacticraft.galacticraft.common.api.item.ISensorGlassesArmor;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntitySpaceshipBase;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.common.api.vector.Vector3;
import team.galacticraft.galacticraft.common.api.world.IGalacticraftDimension;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.client.*;
import team.galacticraft.galacticraft.common.core.client.gui.overlay.*;
import team.galacticraft.galacticraft.common.core.client.gui.screen.GuiCelestialSelection;
import team.galacticraft.galacticraft.common.core.client.gui.screen.GuiTeleporting;
import team.galacticraft.galacticraft.common.core.dimension.DimensionMoon;
import team.galacticraft.galacticraft.common.core.entities.IBubbleProviderColored;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStatsClient;
import team.galacticraft.galacticraft.common.core.fluid.FluidNetwork;
import team.galacticraft.galacticraft.common.core.network.PacketSimple;
import team.galacticraft.galacticraft.common.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.tile.TileEntityOxygenSealer;
import team.galacticraft.galacticraft.common.core.tile.TileEntityScreen;
import team.galacticraft.galacticraft.common.core.util.*;
import team.galacticraft.galacticraft.common.core.wrappers.Footprint;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.NormalDimension;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.util.*;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE)
public class TickHandlerClient
{
    public static int airRemaining;
    public static int airRemaining2;
    public static boolean checkedVersion = true;
    private static boolean lastInvKeyPressed;
    private static long tickCount;
    public static boolean spaceRaceGuiScheduled = false;
    //    private static List<GalacticraftPacketHandler> packetHandlers = Lists.newCopyOnWriteArrayList();
    private static final Set<FluidNetwork> fluidNetworks = Sets.newHashSet();
    public static GuiTeleporting teleportingGui;
    public static volatile boolean menuReset = true;
    public static volatile boolean updateJEIhiding = false;

    public static void resetClient()
    {
        ClientProxyCore.playerItemData.clear();
        ClientProxyCore.overworldTextureRequestSent = false;
        ClientProxyCore.flagRequestsSent.clear();
        TickHandlerClient.clearLiquidNetworks();
        ClientProxyCore.clientSpaceStationID.clear();
        ConfigManagerCore.challengeModeUpdate();

        if (TickHandlerClient.missingRequirementThread == null)
        {
            TickHandlerClient.missingRequirementThread = new ThreadRequirementMissing(EnvType.CLIENT);
            TickHandlerClient.missingRequirementThread.start();
        }

        MapUtil.resetClient();
//        GCBlocks.spaceGlassVanilla.resetColor();
//        GCBlocks.spaceGlassClear.resetColor();
//        GCBlocks.spaceGlassStrong.resetColor();
//        GCBlocks.spaceGlassTinVanilla.resetColor();
//        GCBlocks.spaceGlassTinClear.resetColor();
//        GCBlocks.spaceGlassTinStrong.resetColor(); TODO Space glass
    }

    public static void addFluidNetwork(FluidNetwork network)
    {
        fluidNetworks.add(network);
    }

    public static void removeFluidNetwork(FluidNetwork network)
    {
        fluidNetworks.remove(network);
    }

    public static void clearLiquidNetworks()
    {
        fluidNetworks.clear();
    }

//    public static void addPacketHandler(GalacticraftPacketHandler handler)
//    {
//        TickHandlerClient.packetHandlers.add(handler);
//    }
//
//    @SubscribeEvent
//    public void worldUnloadEvent(WorldEvent.Unload event)
//    {
//        for (GalacticraftPacketHandler packetHandler : packetHandlers)
//        {
//            packetHandler.unload(event.getWorld());
//        }
//    }

    private static ThreadRequirementMissing missingRequirementThread;

    public static HashSet<TileEntityScreen> screenConnectionsUpdateList = new HashSet<TileEntityScreen>();

    static
    {
        registerDetectableBlocks(true);
    }

    public static void registerDetectableBlocks(boolean logging)
    {
        ClientProxyCore.detectableBlocks.clear();

        for (final String s : ConfigManagerCore.detectableIDs.get())
        {
            // TODO Blockstate property parsing? To replace metadata
            Block bt = ConfigManagerCore.stringToBlock(new ResourceLocation(s), "External Detectable IDs", logging);
            if (bt == null)
            {
                continue;
            }

            ClientProxyCore.detectableBlocks.add(bt);

//            int meta = bt.meta;
//            if (meta == -1)
//            {
//                meta = 0;
//            }
//
//            boolean flag = false;
//            for (BlockMetaList blockMetaList : ClientProxyCore.detectableBlocks)
//            {
//                if (blockMetaList.getBlock() == bt.block)
//                {
//                    if (!blockMetaList.getMetaList().contains(meta))
//                    {
//                        blockMetaList.getMetaList().add(meta);
//                    }
//                    flag = true;
//                    break;
//                }
//            }
//
//            if (!flag)
//            {
//                List<Integer> metaList = Lists.newArrayList();
//                metaList.add(meta);
//                ClientProxyCore.detectableBlocks.add(new BlockMetaList(bt.block, metaList));
//            }
        }
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event)
    {
        final Minecraft minecraft = Minecraft.getInstance();
        final LocalPlayer player = minecraft.player;
        final LocalPlayer playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);
        if (player == null || playerBaseClient == null || !player.isAlive())
        {
            return;
        }

        GCPlayerStatsClient stats = GCPlayerStatsClient.get(playerBaseClient);

        if (event.phase == TickEvent.Phase.END)
        {
            if (minecraft.screen instanceof PauseScreen)
            {
//                int i = Mouse.getEventX() * minecraft.currentScreen.width / minecraft.displayWidth;
//                int j = minecraft.currentScreen.height - Mouse.getEventY() * minecraft.currentScreen.height / minecraft.displayHeight - 1;
//
//                int k = Mouse.getEventButton();
//
//                int deltaColor = 0;
//
//                if (i > minecraft.currentScreen.width - 100 && j > minecraft.currentScreen.height - 35)
//                {
//                    deltaColor = 20;
//
//                    if (k == 0)
//                    {
//                        if (Mouse.getEventButtonState())
//                        {
//                            minecraft.displayGuiScreen(new GuiNewSpaceRace(playerBaseClient));
//                        }
//                    }
//                } TODO Space race button

//                this.fillGradient(minecraft.currentScreen.width - 100, minecraft.currentScreen.height - 35, minecraft.currentScreen.width, minecraft.currentScreen.height, ColorUtil.to32BitColor(150, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor), ColorUtil.to32BitColor(250, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor));
                minecraft.font.draw(I18n.get("gui.space_race.create.title.name.0"), minecraft.screen.width - 50 - minecraft.font.width(I18n.get("gui.space_race.create.title.name.0")) / 2, minecraft.screen.height - 26, ColorUtil.to32BitColor(255, 240, 240, 240));
                minecraft.font.draw(I18n.get("gui.space_race.create.title.name.1"), minecraft.screen.width - 50 - minecraft.font.width(I18n.get("gui.space_race.create.title.name.1")) / 2, minecraft.screen.height - 16, ColorUtil.to32BitColor(255, 240, 240, 240));
                GuiComponent.fill(minecraft.screen.width - 100, minecraft.screen.height - 35, minecraft.screen.width - 99, minecraft.screen.height, ColorUtil.to32BitColor(255, 0, 0, 0));
                GuiComponent.fill(minecraft.screen.width - 100, minecraft.screen.height - 35, minecraft.screen.width, minecraft.screen.height - 34, ColorUtil.to32BitColor(255, 0, 0, 0));
            }

            ClientProxyCore.playerPosX = player.xo + (player.getX() - player.xo) * event.renderTickTime;
            ClientProxyCore.playerPosY = player.yo + (player.getY() - player.yo) * event.renderTickTime;
            ClientProxyCore.playerPosZ = player.zo + (player.getZ() - player.zo) * event.renderTickTime;
            ClientProxyCore.playerRotationYaw = player.yRotO + (player.yRot - player.yRotO) * event.renderTickTime;
            ClientProxyCore.playerRotationPitch = player.xRotO + (player.xRot - player.xRotO) * event.renderTickTime;

            if (minecraft.screen == null && minecraft.options.thirdPersonView != 0 && !minecraft.options.hideGui)
            {
                if (player.getVehicle() instanceof EntitySpaceshipBase)
                {
                }

//                OverlayRocket.renderSpaceshipOverlay();
//                OverlayLander.renderLanderOverlay(TickHandlerClient.tickCount);
//                OverlayDockingRocket.renderDockingOverlay(TickHandlerClient.tickCount);
//                OverlayLaunchCountdown.renderCountdownOverlay(); TODO Overlays
            }

            if (player.level.getDimension() instanceof IGalacticraftDimension && OxygenUtil.shouldDisplayTankGui(minecraft.screen) && OxygenUtil.noAtmosphericCombustion(player.level.getDimension()) && !(playerBaseClient.isCreative() || playerBaseClient.isSpectator()) && !minecraft.options.renderDebug)
            {
                int var6 = (TickHandlerClient.airRemaining - 90) * -1;

                if (TickHandlerClient.airRemaining <= 0)
                {
                    var6 = 90;
                }

                int var7 = (TickHandlerClient.airRemaining2 - 90) * -1;

                if (TickHandlerClient.airRemaining2 <= 0)
                {
                    var7 = 90;
                }

                int thermalLevel = stats.getThermalLevel() + 22;
//                OverlayOxygenTanks.renderOxygenTankIndicator(minecraft, thermalLevel, var6, var7, !ConfigManagerCore.oxygenIndicatorLeft.get(), !ConfigManagerCore.oxygenIndicatorBottom.get(), Math.abs(thermalLevel - 22) >= 10 && !stats.isThermalLevelNormalising());
                // TODO Overlays
            }

            if (playerBaseClient != null && player.level.getDimension() instanceof IGalacticraftDimension && !stats.isOxygenSetupValid() && OxygenUtil.noAtmosphericCombustion(player.level.getDimension()) && minecraft.screen == null && !minecraft.options.hideGui && !(playerBaseClient.isCreative() || playerBaseClient.isSpectator()))
            {
                OverlayOxygenWarning.renderOxygenWarningOverlay(TickHandlerClient.tickCount);
            }
        }
    }

    @SubscribeEvent
    public static void onPreGuiRender(RenderGameOverlayEvent.Pre event)
    {
        final Minecraft minecraft = Minecraft.getInstance();
        final LocalPlayer player = minecraft.player;

        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            if (player != null && player.getVehicle() != null && player.getVehicle() instanceof IIgnoreShift && ((IIgnoreShift) player.getVehicle()).shouldIgnoreShiftExit())
            {
                // Remove "Press shift to dismount" message when shift-exiting is disabled (not ideal, but the only option)
                String str = I18n.get("mount.onboard", minecraft.options.keyShift.getTranslatedKeyMessage());
                if (minecraft.gui.overlayMessageString.equals(str))
                {
                    minecraft.gui.overlayMessageString = "";
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        final Minecraft minecraft = Minecraft.getInstance();
        final ClientLevel world = minecraft.level;
        final LocalPlayer player = minecraft.player;

        if (teleportingGui != null)
        {
            if (minecraft.screen != teleportingGui)
            {
                minecraft.screen = teleportingGui;
            }
        }

        if (menuReset)
        {
            TickHandlerClient.resetClient();
            menuReset = false;
        }

        if (event.phase == TickEvent.Phase.START && player != null)
        {
            if (ClientProxyCore.playerHead == null && player.getGameProfile() != null)
            {
                Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().getInsecureSkinInformation(player.getGameProfile());

                if (map.containsKey(Type.SKIN))
                {
                    ClientProxyCore.playerHead = minecraft.getSkinManager().registerTexture(map.get(Type.SKIN), Type.SKIN);
                }
                else
                {
                    ClientProxyCore.playerHead = DefaultPlayerSkin.getDefaultSkin(Player.createPlayerUUID(player.getGameProfile()));
                }
            }

            TickHandlerClient.tickCount++;

            if (!GalacticraftCore.proxy.isPaused())
            {
                Iterator<FluidNetwork> it = TickHandlerClient.fluidNetworks.iterator();
                while (it.hasNext())
                {
                    FluidNetwork network = it.next();

                    if (network.getTransmitters().size() == 0)
                    {
                        it.remove();
                    }
                    else
                    {
                        network.clientTick();
                    }
                }
            }

            if (TickHandlerClient.tickCount % 20 == 0)
            {
//                BubbleRenderer.clearBubbles();
//
//                for (TileEntity tile : player.world.tickableTileEntities)
//                {
//                    if (tile instanceof IBubbleProviderColored)
//                    {
//                        BubbleRenderer.addBubble((IBubbleProviderColored) tile);
//                    }
//                } TODO Bubble Rendering

                if (updateJEIhiding)
                {
                    updateJEIhiding = false;
                    // Update JEI to hide the ingot compressor recipe for GC steel in hard mode
                    // Update JEI to hide adventure mode recipes when not in adventure mode
//                    GalacticraftJEI.updateHidden(CompressorRecipes.steelIngotsPresent && ConfigManagerCore.hardMode.get() && !ConfigManagerCore.challengeRecipes.get(), !ConfigManagerCore.challengeRecipes.get()); TODO JEI
                }

                for (List<Footprint> fpList : FootprintRenderer.footprints.values())
                {
                    Iterator<Footprint> fpIt = fpList.iterator();
                    while (fpIt.hasNext())
                    {
                        Footprint fp = fpIt.next();
                        fp.age += 20;
                        fp.lightmapVal = player.level.getMaxLocalRawBrightness(new BlockPos(fp.position.x, fp.position.y, fp.position.z));

                        if (fp.age >= Footprint.MAX_AGE)
                        {
                            fpIt.remove();
                        }
                    }
                }

                if (!player.inventory.getArmor(3).isEmpty() && player.inventory.getArmor(3).getItem() instanceof ISensorGlassesArmor)
                {
                    ClientProxyCore.valueableBlocks.clear();

                    for (int i = -4; i < 5; i++)
                    {
                        int x = Mth.floor(player.getX() + i);
                        for (int j = -4; j < 5; j++)
                        {
                            int y = Mth.floor(player.getY() + j);
                            for (int k = -4; k < 5; k++)
                            {
                                int z = Mth.floor(player.getZ() + k);
                                BlockPos pos = new BlockPos(x, y, z);

                                BlockState state = player.level.getBlockState(pos);
                                final Block block = state.getBlock();

                                if (block.getMaterial(state) != Material.AIR)
                                {
//                                    int metadata = block.getMetaFromState(state);
                                    boolean isDetectable = false;

                                    for (Block detectableBlock : ClientProxyCore.detectableBlocks)
                                    {
//                                        if (blockMetaList.getBlock() == block && blockMetaList.getMetaList().contains(metadata))
                                        if (detectableBlock == block)
                                        {
                                            isDetectable = true;
                                            break;
                                        }
                                    }

                                    if (isDetectable || (block instanceof IDetectableResource && ((IDetectableResource) block).isValueable(state)))
                                    {
                                        ClientProxyCore.valueableBlocks.add(new BlockVec3(x, y, z));
                                    }
                                }
                            }
                        }
                    }

                    TileEntityOxygenSealer nearestSealer = TileEntityOxygenSealer.getNearestSealer(world, Mth.floor(player.getX()), Mth.floor(player.getY()), Mth.floor(player.getZ()));
                    if (nearestSealer != null && !nearestSealer.sealed)
                    {
                        ClientProxyCore.leakTrace = nearestSealer.getLeakTraceClient();
                    }
                    else
                    {
                        ClientProxyCore.leakTrace = null;
                    }
                }
                else
                {
                    ClientProxyCore.leakTrace = null;
                }

                if (world != null)
                {
                    if (MapUtil.resetClientFlag.getAndSet(false))
                    {
                        MapUtil.resetClientBody();
                    }
                }
            }

            if (ClientProxyCore.leakTrace != null)
            {
                spawnLeakParticles();
            }

            if (world != null && TickHandlerClient.spaceRaceGuiScheduled && minecraft.screen == null && ConfigManagerCore.enableSpaceRaceManagerPopup.get())
            {
//                player.openGui(GalacticraftCore.instance, GuiIdsCore.SPACE_RACE_START, player.world, (int) player.getPosX(), (int) player.getPosY(), (int) player.getPosZ()); TODO Gui
                TickHandlerClient.spaceRaceGuiScheduled = false;
            }

            if (world != null && TickHandlerClient.checkedVersion)
            {
                ThreadVersionCheck.startCheck();
                TickHandlerClient.checkedVersion = false;
            }

            boolean inSpaceShip = false;
            if (player.getVehicle() instanceof EntitySpaceshipBase)
            {
                inSpaceShip = true;
                EntitySpaceshipBase rocket = (EntitySpaceshipBase) player.getVehicle();
                if (rocket.xRotO != rocket.xRot || rocket.yRotO != rocket.yRot)
                {
//                    GalacticraftCore.packetPipeline.sendToServer(new PacketRotateRocket(player.getRidingEntity()));
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ROTATE_ROCKET, rocket.dimension, new Object[]{rocket.getId(), rocket.xRot, rocket.yRot}));
                }
            }

            if (world != null)
            {
                if (world.getDimension() instanceof NormalDimension)
                {
//                    if (world.getDimension().getSkyRenderer() == null && inSpaceShip &&
//                            player.getRidingEntity().posY > Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
//                    {
//                        world.getDimension().setSkyRenderer(new SkyProviderOverworld());
//                    }
//                    else if (world.getDimension().getSkyRenderer() instanceof SkyProviderOverworld && player.getPosY() <= Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
//                    {
//                        world.getDimension().setSkyRenderer(null);
//                    }  TODO Sky rendering
                }
//                else if (world.getDimension() instanceof DimensionSpaceStation)
//                {
//                    if (world.getDimension().getSkyRenderer() == null)
//                    {
//                        ((DimensionSpaceStation) world.getDimension()).createSkyProvider();
//                    }
//                } TODO Space stations
                else if (world.getDimension() instanceof DimensionMoon)
                {
//                    if (world.getDimension().getSkyRenderer() == null)
//                    {
//                        world.getDimension().setSkyRenderer(new SkyProviderMoon());
//                    }  TODO Sky rendering

                    if (world.getDimension().getCloudRenderer() == null)
                    {
                        world.getDimension().setCloudRenderer(new CloudRenderer());
                    }
                }
            }

            if (inSpaceShip)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.getVehicle();
                boolean hasChanged = false;

                if (minecraft.options.keyLeft.isDown())
                {
                    ship.turnYaw(-1.0F);
                    hasChanged = true;
                }

                if (minecraft.options.keyRight.isDown())
                {
                    ship.turnYaw(1.0F);
                    hasChanged = true;
                }

                if (minecraft.options.keyUp.isDown())
                {
                    if (ship.getLaunched())
                    {
                        ship.turnPitch(-0.7F);
                        hasChanged = true;
                    }
                }

                if (minecraft.options.keyDown.isDown())
                {
                    if (ship.getLaunched())
                    {
                        ship.turnPitch(0.7F);
                        hasChanged = true;
                    }
                }

                if (hasChanged)
                {
//                    GalacticraftCore.packetPipeline.sendToServer(new PacketRotateRocket(ship));
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ROTATE_ROCKET, ship.dimension, new Object[]{ship.getId(), ship.xRot, ship.yRot}));
                }
            }

            if (world != null)
            {
                Iterable<Entity> entityList = world.entitiesForRendering();
                for (Object e : entityList)
                {
                    if (e instanceof IEntityNoisy)
                    {
                        IEntityNoisy vehicle = (IEntityNoisy) e;
                        if (vehicle.getSoundUpdater() == null)
                        {
                            SoundInstance noise = vehicle.setSoundUpdater(Minecraft.getInstance().player);
                            if (noise != null)
                            {
                                Minecraft.getInstance().getSoundManager().play(noise);
                            }
                        }
                    }
                }
            }

            if (Minecraft.getInstance().screen instanceof GuiCelestialSelection)
            {
                player.setDeltaMovement(player.getDeltaMovement().x, 0.0, player.getDeltaMovement().z);
            }

            if (world != null && world.getDimension() instanceof IGalacticraftDimension && OxygenUtil.noAtmosphericCombustion(world.getDimension()) && ((IGalacticraftDimension) world.getDimension()).shouldDisablePrecipitation())
            {
                world.setRainLevel(0.0F);
            }

            boolean isPressed = KeyHandlerClient.spaceKey.isDown();

            if (!isPressed)
            {
                ClientProxyCore.lastSpacebarDown = false;
            }

            if (player.getVehicle() != null && isPressed && !ClientProxyCore.lastSpacebarDown)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_IGNITE_ROCKET, GCCoreUtil.getDimensionType(player.level), new Object[]{}));
                ClientProxyCore.lastSpacebarDown = true;
            }

            if (!(screenConnectionsUpdateList.isEmpty()))
            {
                HashSet<TileEntityScreen> updateListCopy = (HashSet<TileEntityScreen>) screenConnectionsUpdateList.clone();
                screenConnectionsUpdateList.clear();
                for (TileEntityScreen te : updateListCopy)
                {
                    if (te.getLevel().getBlockState(te.getBlockPos()).getBlock() == GCBlocks.screen)
                    {
                        if (te.refreshOnUpdate)
                        {
                            te.refreshConnections(true);
                        }
//                        te.getWorld().markBlockRangeForRenderUpdate(te.getPos(), te.getPos());
                    }
                }
            }
        }
        else if (event.phase == TickEvent.Phase.END)
        {
            if (world != null)
            {
//                for (GalacticraftPacketHandler handler : packetHandlers)
//                {
//                    handler.tick(world);
//                } TODO Packet handler ticking?
            }
        }
    }

    private static void spawnLeakParticles()
    {
        Random rand = new Random();
        for (int i = ClientProxyCore.leakTrace.size() - 1; i >= 0; i--)
        {
            if (i == 1)
            {
                continue;
            }
            BlockVec3 curr = ClientProxyCore.leakTrace.get(i);
            int nx = i - 2;
            if (i > 2 && rand.nextInt(3) == 0)
            {
                nx--;
            }
            BlockVec3 vec;
            if (i > 1)
            {
                vec = ClientProxyCore.leakTrace.get(nx).clone();
            }
            else
            {
                vec = curr.clone().translate(0, -2, 0);
            }
            Vector3 mot = new Vector3(vec.subtract(curr));
            Vector3 rnd = new Vector3(rand.nextFloat() / 2.0F - 0.25F, rand.nextFloat() / 2.0F - 0.25F, rand.nextFloat() / 2.0F - 0.25F);
            Vector3 offset = curr.midPoint().add(rnd);
            Minecraft.getInstance().level.addParticle(GCParticles.OXYGEN, offset.x, offset.y, offset.z, mot.x, mot.y, mot.z);
        }
    }

    private boolean alreadyContainsBlock(int x1, int y1, int z1)
    {
        return ClientProxyCore.valueableBlocks.contains(new BlockVec3(x1, y1, z1));
    }

    public static void zoom(float value)
    {
//        Minecraft.getInstance().entityRenderer.thirdPersonDistance = value;
//        Minecraft.getInstance().entityRenderer.thirdPersonDistancePrev = value;
    }

//    private void fillGradient(int par1, int par2, int par3, int par4, int par5, int par6)
//    {
//        float f = (par5 >> 24 & 255) / 255.0F;
//        float f1 = (par5 >> 16 & 255) / 255.0F;
//        float f2 = (par5 >> 8 & 255) / 255.0F;
//        float f3 = (par5 & 255) / 255.0F;
//        float f4 = (par6 >> 24 & 255) / 255.0F;
//        float f5 = (par6 >> 16 & 255) / 255.0F;
//        float f6 = (par6 >> 8 & 255) / 255.0F;
//        float f7 = (par6 & 255) / 255.0F;
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glDisable(GL11.GL_ALPHA_TEST);
//        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
//        GL11.glShadeModel(GL11.GL_SMOOTH);
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tessellator.getBuffer();
//        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
//        worldRenderer.pos(par3, par2, 0.0D).color(f1, f2, f3, f).endVertex();
//        worldRenderer.pos(par1, par2, 0.0D).color(f1, f2, f3, f).endVertex();
//        worldRenderer.pos(par1, par4, 0.0D).color(f5, f6, f7, f4).endVertex();
//        worldRenderer.pos(par3, par4, 0.0D).color(f5, f6, f7, f4).endVertex();
//        tessellator.draw();
//        GL11.glShadeModel(GL11.GL_FLAT);
//        GL11.glDisable(GL11.GL_BLEND);
//        GL11.glEnable(GL11.GL_ALPHA_TEST);
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//    }
}
