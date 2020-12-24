package team.galacticraft.galacticraft.common.core.network;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import team.galacticraft.galacticraft.common.api.galaxies.CelestialBody;
import team.galacticraft.galacticraft.common.api.galaxies.GalaxyRegistry;
import team.galacticraft.galacticraft.common.api.galaxies.Satellite;
import team.galacticraft.galacticraft.common.api.galaxies.SolarSystem;
import team.galacticraft.galacticraft.common.api.item.EnumExtendedInventorySlot;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntityAutoRocket;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntitySpaceshipBase;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntityTieredRocket;
import team.galacticraft.galacticraft.common.api.recipe.ISchematicPage;
import team.galacticraft.galacticraft.common.api.recipe.SchematicRegistry;
import team.galacticraft.galacticraft.common.api.tile.IDisableableMachine;
import team.galacticraft.galacticraft.common.api.tile.ITileClientUpdates;
import team.galacticraft.galacticraft.common.api.transmission.tile.INetworkProvider;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.common.api.vector.Vector3;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCItems;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.advancement.GCTriggers;
import team.galacticraft.galacticraft.common.core.client.FootprintRenderer;
import team.galacticraft.galacticraft.common.core.client.gui.screen.GuiCelestialSelection;
import team.galacticraft.galacticraft.common.core.client.sounds.GCSounds;
import team.galacticraft.galacticraft.common.core.dimension.SpaceRace;
import team.galacticraft.galacticraft.common.core.dimension.SpaceRaceManager;
import team.galacticraft.galacticraft.common.core.dimension.SpaceStationWorldData;
import team.galacticraft.galacticraft.common.core.energy.tile.TileBaseConductor;
import team.galacticraft.galacticraft.common.core.entities.EntityBuggy;
import team.galacticraft.galacticraft.common.core.entities.IBubbleProvider;
import team.galacticraft.galacticraft.common.core.entities.IControllableEntity;
import team.galacticraft.galacticraft.common.core.entities.player.GCEntityPlayerMP;
import team.galacticraft.galacticraft.common.core.entities.player.GCPlayerHandler;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.core.fluid.FluidNetwork;
import team.galacticraft.galacticraft.common.core.inventory.ContainerBuggy;
import team.galacticraft.galacticraft.common.core.inventory.ContainerExtendedInventory;
import team.galacticraft.galacticraft.common.core.inventory.ContainerSchematic;
import team.galacticraft.galacticraft.common.core.items.ItemParaChute;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.tick.TickHandlerClient;
import team.galacticraft.galacticraft.common.core.tick.TickHandlerServer;
import team.galacticraft.galacticraft.common.core.tile.*;
import team.galacticraft.galacticraft.common.core.util.*;
import team.galacticraft.galacticraft.common.core.wrappers.FlagData;
import team.galacticraft.galacticraft.common.core.wrappers.Footprint;
import team.galacticraft.galacticraft.common.core.wrappers.PlayerGearData;
import team.galacticraft.galacticraft.common.core.wrappers.ScheduledDimensionChange;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelData;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;

public class PacketSimple extends PacketBase implements Packet<PacketListener>, IGCMsg
{
    public enum EnumSimplePacket
    {
        // SERVER
        S_RESPAWN_PLAYER(EnvType.SERVER, String.class),
        S_TELEPORT_ENTITY(EnvType.SERVER, DimensionType.class),
        S_IGNITE_ROCKET(EnvType.SERVER),
        S_OPEN_SCHEMATIC_PAGE(EnvType.SERVER, Integer.class, Integer.class, Integer.class, Integer.class),
        S_OPEN_FUEL_GUI(EnvType.SERVER, String.class),
        S_UPDATE_SHIP_YAW(EnvType.SERVER, Float.class),
        S_UPDATE_SHIP_PITCH(EnvType.SERVER, Float.class),
        S_SET_ENTITY_FIRE(EnvType.SERVER, Integer.class),
        S_BIND_SPACE_STATION_ID(EnvType.SERVER, DimensionType.class),
        S_UNLOCK_NEW_SCHEMATIC(EnvType.SERVER),
        S_UPDATE_DISABLEABLE_BUTTON(EnvType.SERVER, BlockPos.class, Integer.class),
        S_ON_FAILED_CHEST_UNLOCK(EnvType.SERVER, Integer.class),
        S_RENAME_SPACE_STATION(EnvType.SERVER, String.class, Integer.class),
        S_OPEN_EXTENDED_INVENTORY(EnvType.SERVER),
        S_ON_ADVANCED_GUI_CLICKED_INT(EnvType.SERVER, Integer.class, BlockPos.class, Integer.class),
        S_ON_ADVANCED_GUI_CLICKED_STRING(EnvType.SERVER, Integer.class, BlockPos.class, String.class),
        S_UPDATE_SHIP_MOTION_Y(EnvType.SERVER, Integer.class, Boolean.class),
        S_START_NEW_SPACE_RACE(EnvType.SERVER, Integer.class, String.class, FlagData.class, Vector3.class, String[].class),
        S_REQUEST_FLAG_DATA(EnvType.SERVER, String.class),
        S_INVITE_RACE_PLAYER(EnvType.SERVER, String.class, Integer.class),
        S_REMOVE_RACE_PLAYER(EnvType.SERVER, String.class, Integer.class),
        S_ADD_RACE_PLAYER(EnvType.SERVER, String.class, Integer.class),
        S_COMPLETE_CBODY_HANDSHAKE(EnvType.SERVER, String.class),
        S_REQUEST_GEAR_DATA1(EnvType.SERVER, UUID.class),
        S_REQUEST_GEAR_DATA2(EnvType.SERVER, UUID.class),
        S_REQUEST_OVERWORLD_IMAGE(EnvType.SERVER),
        S_REQUEST_MAP_IMAGE(EnvType.SERVER, Integer.class, Integer.class, Integer.class),
        S_REQUEST_PLAYERSKIN(EnvType.SERVER, String.class),
        S_BUILDFLAGS_UPDATE(EnvType.SERVER, Integer.class),
        S_CONTROL_ENTITY(EnvType.SERVER, Integer.class),
        S_NOCLIP_PLAYER(EnvType.SERVER, Boolean.class),
        S_REQUEST_DATA(EnvType.SERVER, DimensionType.class, BlockPos.class),
        S_UPDATE_CHECKLIST(EnvType.SERVER, CompoundTag.class),
        S_REQUEST_MACHINE_DATA(EnvType.SERVER, BlockPos.class),
        S_REQUEST_CONTAINER_SLOT_REFRESH(EnvType.SERVER, Integer.class),
        S_ROTATE_ROCKET(EnvType.SERVER, Integer.class, Float.class, Float.class),
        // CLIENT
        C_AIR_REMAINING(EnvType.CLIENT, Integer.class, Integer.class, String.class),
        C_UPDATE_DIMENSION_LIST(EnvType.CLIENT, String.class, String.class, Boolean.class),
        C_SPAWN_SPARK_PARTICLES(EnvType.CLIENT, BlockPos.class),
        C_UPDATE_GEAR_SLOT(EnvType.CLIENT, UUID.class, Integer.class, Integer.class, Integer.class),
        C_CLOSE_GUI(EnvType.CLIENT),
        C_RESET_THIRD_PERSON(EnvType.CLIENT),
        C_UPDATE_SPACESTATION_LIST(EnvType.CLIENT, Integer[].class),
        //        C_UPDATE_SPACESTATION_DATA(EnvType.CLIENT, Integer.class, CompoundNBT.class),
        C_UPDATE_SPACESTATION_CLIENT_ID(EnvType.CLIENT, String.class),
        C_UPDATE_PLANETS_LIST(EnvType.CLIENT, Integer[].class),
        C_UPDATE_CONFIGS(EnvType.CLIENT, Integer.class, Double.class, Integer.class, Integer.class, Integer.class, String.class, Float.class, Float.class, Float.class, Float.class, Integer.class, String[].class),
        C_UPDATE_STATS(EnvType.CLIENT, Integer.class, String.class, Integer.class, String.class, Integer.class, String.class, Integer.class, String.class, Integer.class, String.class, Integer.class, Integer.class), //Note: Integer, PANELTYPES_LENGTH * <String, Integer>, Integer - see StatsCapability.getMiscNetworkedStats()
        C_ADD_NEW_SCHEMATIC(EnvType.CLIENT, Integer.class),
        C_UPDATE_SCHEMATIC_LIST(EnvType.CLIENT, Integer[].class),
        C_PLAY_SOUND_BOSS_DEATH(EnvType.CLIENT, Float.class),
        C_PLAY_SOUND_EXPLODE(EnvType.CLIENT),
        C_PLAY_SOUND_BOSS_LAUGH(EnvType.CLIENT),
        C_PLAY_SOUND_BOW(EnvType.CLIENT),
        C_UPDATE_OXYGEN_VALIDITY(EnvType.CLIENT, Boolean.class),
        C_OPEN_PARACHEST_GUI(EnvType.CLIENT, Integer.class, Integer.class, Integer.class),
        C_UPDATE_WIRE_BOUNDS(EnvType.CLIENT, BlockPos.class),
        C_OPEN_SPACE_RACE_GUI(EnvType.CLIENT),
        C_UPDATE_SPACE_RACE_DATA(EnvType.CLIENT, Integer.class, String.class, FlagData.class, Vector3.class, String[].class),
        C_OPEN_JOIN_RACE_GUI(EnvType.CLIENT, Integer.class),
        C_UPDATE_FOOTPRINT_LIST(EnvType.CLIENT, Long.class, Footprint[].class),
        C_UPDATE_DUNGEON_DIRECTION(EnvType.CLIENT, Float.class),
        C_FOOTPRINTS_REMOVED(EnvType.CLIENT, Long.class, BlockVec3.class),
        C_UPDATE_STATION_SPIN(EnvType.CLIENT, Float.class, Boolean.class),
        C_UPDATE_STATION_DATA(EnvType.CLIENT, Double.class, Double.class),
        C_UPDATE_STATION_BOX(EnvType.CLIENT, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class),
        C_UPDATE_THERMAL_LEVEL(EnvType.CLIENT, Integer.class, Boolean.class),
        C_DISPLAY_ROCKET_CONTROLS(EnvType.CLIENT),
        C_GET_CELESTIAL_BODY_LIST(EnvType.CLIENT),
        C_UPDATE_ENERGYUNITS(EnvType.CLIENT, Integer.class),
        C_RESPAWN_PLAYER(EnvType.CLIENT, String.class, Integer.class, String.class, Integer.class),
        C_UPDATE_TELEMETRY(EnvType.CLIENT, BlockPos.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class),
        C_SEND_PLAYERSKIN(EnvType.CLIENT, String.class, String.class, String.class, String.class),
        C_SEND_OVERWORLD_IMAGE(EnvType.CLIENT, Integer.class, Integer.class, byte[].class),
        C_RECOLOR_PIPE(EnvType.CLIENT, BlockPos.class),
        C_RECOLOR_ALL_GLASS(EnvType.CLIENT, Integer.class, Integer.class, Integer.class),  //Number of integers to match number of different blocks of PLAIN glass individually instanced and registered in GCBlocks
        C_UPDATE_MACHINE_DATA(EnvType.CLIENT, BlockPos.class, Integer.class, Integer.class, Integer.class, Integer.class),
        C_SPAWN_HANGING_SCHEMATIC(EnvType.CLIENT, BlockPos.class, Integer.class, Integer.class, Integer.class),
        C_LEAK_DATA(EnvType.CLIENT, BlockPos.class, Integer[].class);

        private final EnvType targetSide;
        private final Class<?>[] decodeAs;

        EnumSimplePacket(EnvType targetSide, Class<?>... decodeAs)
        {
            this.targetSide = targetSide;
            this.decodeAs = decodeAs;
        }

        public EnvType getTargetSide()
        {
            return this.targetSide;
        }

        public Class<?>[] getDecodeClasses()
        {
            return this.decodeAs;
        }
    }

    private EnumSimplePacket type;
    private List<Object> data;
    static private String spamCheckString;
    static private final Map<ServerPlayer, GameType> savedSettings = new HashMap<>();

    public PacketSimple()
    {
        super();
    }

    public PacketSimple(EnumSimplePacket packetType, DimensionType dimID, Object[] data)
    {
        this(packetType, dimID, Arrays.asList(data));
    }

    public PacketSimple(EnumSimplePacket packetType, Level world, Object[] data)
    {
        this(packetType, GCCoreUtil.getDimensionType(world), Arrays.asList(data));
    }

    public PacketSimple(EnumSimplePacket packetType, DimensionType dimID, List<Object> data)
    {
        super(dimID);

        this.type = packetType;
        this.data = data;
    }

    public static void encode(final PacketSimple message, final FriendlyByteBuf buf)
    {
        buf.writeInt(message.type.ordinal());
        NetworkUtil.writeUTF8String(buf, message.getDimensionID().getRegistryName().toString());

        try
        {
            NetworkUtil.encodeData(buf, message.data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static PacketSimple decode(FriendlyByteBuf buf)
    {
        EnumSimplePacket type = EnumSimplePacket.values()[buf.readInt()];
        DimensionType dim = DimensionType.getByName(new ResourceLocation(NetworkUtil.readUTF8String(buf)));
        ArrayList<Object> data = null;

        try
        {
            if (type.getDecodeClasses().length > 0)
            {
                data = NetworkUtil.decodeData(type.getDecodeClasses(), buf);
            }
            if (buf.readableBytes() > 0 && buf.writerIndex() < 0xfff00)
            {
                GCLog.severe("Galacticraft packet length problem for packet type " + type.toString());
            }
        }
        catch (Exception e)
        {
            System.err.println("[Galacticraft] Error handling simple packet type: " + type.toString() + " " + buf.toString());
            e.printStackTrace();
            throw e;
        }
        return new PacketSimple(type, dim, data);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void handleClientSide(Player player)
    {
        LocalPlayer playerBaseClient = null;
        GCPlayerStatsClient stats = null;

        if (player instanceof LocalPlayer)
        {
            playerBaseClient = (LocalPlayer) player;
            stats = GCPlayerStatsClient.get(playerBaseClient);
        }
        else
        {
            if (type != EnumSimplePacket.C_UPDATE_SPACESTATION_LIST && type != EnumSimplePacket.C_UPDATE_PLANETS_LIST && type != EnumSimplePacket.C_UPDATE_CONFIGS)
            {
                return;
            }
        }

        switch (this.type)
        {
        case C_AIR_REMAINING:
            if (String.valueOf(this.data.get(2)).equals(String.valueOf(PlayerUtil.getName(player))))
            {
                TickHandlerClient.airRemaining = (Integer) this.data.get(0);
                TickHandlerClient.airRemaining2 = (Integer) this.data.get(1);
            }
            break;
        case C_UPDATE_DIMENSION_LIST:
            if (String.valueOf(this.data.get(0)).equals(PlayerUtil.getName(player)))
            {
                String dimensionList = (String) this.data.get(1);
                if (ConfigManagerCore.enableDebug.get())
                {
                    if (!dimensionList.equals(PacketSimple.spamCheckString))
                    {
                        GCLog.info("DEBUG info: " + dimensionList);
                        PacketSimple.spamCheckString = dimensionList;
                    }
                }
                final String[] destinations = dimensionList.split("\\?");
                List<CelestialBody> possibleCelestialBodies = Lists.newArrayList();
                Map<DimensionType, Map<String, GuiCelestialSelection.StationDataGUI>> spaceStationData = Maps.newHashMap();
//                Map<String, String> spaceStationNames = Maps.newHashMap();
//                Map<String, Integer> spaceStationIDs = Maps.newHashMap();
//                Map<String, Integer> spaceStationHomes = Maps.newHashMap();

                for (String str : destinations)
                {
                    CelestialBody celestialBody = WorldUtil.getReachableCelestialBodiesForName(str);

                    if (celestialBody == null && str.contains("$"))
                    {
                        String[] values = str.split("\\$");

                        DimensionType homePlanetID = DimensionType.getByName(new ResourceLocation(values[4]));

                        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
                        {
                            if (satellite.getParentPlanet().getDimensionType() == homePlanetID)
                            {
                                celestialBody = satellite;
                                break;
                            }
                        }

                        if (!spaceStationData.containsKey(homePlanetID))
                        {
                            spaceStationData.put(homePlanetID, new HashMap<String, GuiCelestialSelection.StationDataGUI>());
                        }

                        spaceStationData.get(homePlanetID).put(values[1], new GuiCelestialSelection.StationDataGUI(values[2], DimensionType.getByName(new ResourceLocation(values[3]))));

//                        spaceStationNames.put(values[1], values[2]);
//                        spaceStationIDs.put(values[1], Integer.parseInt(values[3]));
//                        spaceStationHomes.put(values[1], Integer.parseInt(values[4]));
                    }

                    if (celestialBody != null)
                    {
                        possibleCelestialBodies.add(celestialBody);
                    }
                }

                if (Minecraft.getInstance().level != null)
                {
                    if (!(Minecraft.getInstance().screen instanceof GuiCelestialSelection))
                    {
                        GuiCelestialSelection gui = new GuiCelestialSelection(false, possibleCelestialBodies, (Boolean) this.data.get(2));
                        gui.spaceStationMap = spaceStationData;
//                        gui.spaceStationNames = spaceStationNames;
//                        gui.spaceStationIDs = spaceStationIDs;
                        Minecraft.getInstance().setScreen(gui);
                    }
                    else
                    {
                        ((GuiCelestialSelection) Minecraft.getInstance().screen).possibleBodies = possibleCelestialBodies;
                        ((GuiCelestialSelection) Minecraft.getInstance().screen).spaceStationMap = spaceStationData;
//                        ((GuiCelestialSelection) Minecraft.getInstance().currentScreen).spaceStationNames = spaceStationNames;
//                        ((GuiCelestialSelection) Minecraft.getInstance().currentScreen).spaceStationIDs = spaceStationIDs;
                    }
                }
            }
            break;
        case C_SPAWN_SPARK_PARTICLES:
//            BlockPos pos = (BlockPos) this.data.get(0);
//            Minecraft mc = Minecraft.getInstance();
//
//            for (int i = 0; i < 4; i++)
//            {
//                if (mc.getRenderViewEntity() != null && mc.effectRenderer != null && mc.world != null)
//                {
//                    final ParticleSparks fx = new ParticleSparks(mc.world, pos.getX() - 0.15 + 0.5, pos.getY() + 1.2, pos.getZ() + 0.15 + 0.5, mc.world.rand.nextDouble() / 20 - mc.world.rand.nextDouble() / 20, mc.world.rand.nextDouble() / 20 - mc.world.rand.nextDouble() / 20);
//
//                    mc.effectRenderer.addEffect(fx);
//                }
//            } TODO
            break;
        case C_UPDATE_GEAR_SLOT:
            int subtype = (Integer) this.data.get(3);
            UUID gearUUID = (UUID) this.data.get(0);

            Player gearDataPlayer = player.level.getPlayerByUUID(gearUUID);

            if (gearDataPlayer != null)
            {
                PlayerGearData gearData = ClientProxyCore.playerItemData.get(PlayerUtil.getName(gearDataPlayer));
                UUID gearDataPlayerID = gearDataPlayer.getUUID();

                if (gearData == null)
                {
                    gearData = new PlayerGearData(player);
                    if (!ClientProxyCore.gearDataRequests.contains(gearDataPlayerID))
                    {
                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_REQUEST_GEAR_DATA1, getDimensionID(), new Object[]{gearDataPlayerID}));
                        ClientProxyCore.gearDataRequests.add(gearDataPlayerID);
                    }
                }
                else
                {
                    ClientProxyCore.gearDataRequests.remove(gearDataPlayerID);
                }

                EnumExtendedInventorySlot type = EnumExtendedInventorySlot.values()[(Integer) this.data.get(2)];
                GCPlayerHandler.EnumModelPacketType typeChange = GCPlayerHandler.EnumModelPacketType.values()[(Integer) this.data.get(1)];

                switch (type)
                {
                case MASK:
                    gearData.setMask(subtype);
                    break;
                case GEAR:
                    gearData.setGear(subtype);
                    break;
                case LEFT_TANK:
                    gearData.setLeftTank(subtype);
                    break;
                case RIGHT_TANK:
                    gearData.setRightTank(subtype);
                    break;
                case PARACHUTE:
                    if (typeChange == GCPlayerHandler.EnumModelPacketType.ADD)
                    {
                        String name;

                        if (subtype != -1)
                        {
                            name = ItemParaChute.names[subtype];
                            gearData.setParachute(new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/" + name + ".png"));
                        }
                    }
                    else
                    {
                        gearData.setParachute(null);
                    }
                    break;
                case FREQUENCY_MODULE:
                    gearData.setFrequencyModule(subtype);
                    break;
                case THERMAL_HELMET:
                    gearData.setThermalPadding(0, subtype);
                    break;
                case THERMAL_CHESTPLATE:
                    gearData.setThermalPadding(1, subtype);
                    break;
                case THERMAL_LEGGINGS:
                    gearData.setThermalPadding(2, subtype);
                    break;
                case THERMAL_BOOTS:
                    gearData.setThermalPadding(3, subtype);
                    break;
                case SHIELD_CONTROLLER:
                    gearData.setShieldController(subtype);
                    break;
                default:
                    break;
                }

                ClientProxyCore.playerItemData.put(gearUUID, gearData);
            }

            break;
        case C_CLOSE_GUI:
            Minecraft.getInstance().setScreen(null);
            break;
        case C_RESET_THIRD_PERSON:
            Minecraft.getInstance().options.thirdPersonView = stats.getThirdPersonView();
            break;
        case C_UPDATE_SPACESTATION_LIST:
            WorldUtil.decodeSpaceStationListClient(data);
            break;
//        case C_UPDATE_SPACESTATION_DATA:
//            SpaceStationWorldData var4 = SpaceStationWorldData.getMPSpaceStationData(player.world, (Integer) this.data.get(0), player);
//            var4.readFromNBT((CompoundNBT) this.data.get(1));
//            break; TODO ?
        case C_UPDATE_SPACESTATION_CLIENT_ID:
            ClientProxyCore.clientSpaceStationID = WorldUtil.stringToSpaceStationData((String) this.data.get(0));
            break;
        case C_UPDATE_PLANETS_LIST:
            WorldUtil.decodePlanetsListClient(data);
            break;
//        case C_UPDATE_CONFIGS:
//            ConfigManagerCore.saveClientConfigOverrideable.get()();
//            ConfigManagerCore.setConfigOverride.get()(data);
//            break;
        case C_ADD_NEW_SCHEMATIC:
            final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID((Integer) this.data.get(0));
            if (!stats.getUnlockedSchematics().contains(page))
            {
                stats.getUnlockedSchematics().add(page);
            }
            break;
        case C_UPDATE_SCHEMATIC_LIST:
            for (Object o : this.data)
            {
                Integer schematicID = (Integer) o;

                if (schematicID != -2)
                {
                    Collections.sort(stats.getUnlockedSchematics());

                    if (!stats.getUnlockedSchematics().contains(SchematicRegistry.getMatchingRecipeForID(schematicID)))
                    {
                        stats.getUnlockedSchematics().add(SchematicRegistry.getMatchingRecipeForID(schematicID));
                    }
                }
            }
            break;
        case C_PLAY_SOUND_BOSS_DEATH:
            player.playSound(GCSounds.bossDeath, 10.0F, (Float) this.data.get(0));
            break;
        case C_PLAY_SOUND_EXPLODE:
            player.playSound(SoundEvents.GENERIC_EXPLODE, 10.0F, 0.7F);
            break;
        case C_PLAY_SOUND_BOSS_LAUGH:
            player.playSound(GCSounds.bossLaugh, 10.0F, 0.2F);
            break;
        case C_PLAY_SOUND_BOW:
            player.playSound(SoundEvents.ARROW_SHOOT, 10.0F, 0.2F);
            break;
        case C_UPDATE_OXYGEN_VALIDITY:
            stats.setOxygenSetupValid((Boolean) this.data.get(0));
            break;
        case C_OPEN_PARACHEST_GUI:
//            switch ((Integer) this.data.get(1)) TODO
//            {
//            case 0:
//                if (player.getRidingEntity() instanceof EntityBuggy)
//                {
//                    Minecraft.getInstance().displayGuiScreen(new GuiBuggy(player.inventory, (EntityBuggy) player.getRidingEntity(), ((EntityBuggy) player.getRidingEntity()).getType()));
//                    player.openContainer.windowId = (Integer) this.data.get(0);
//                }
//                break;
//            case 1:
//                int entityID = (Integer) this.data.get(2);
//                Entity entity = player.world.getEntityByID(entityID);
//
//                if (entity != null && entity instanceof IInventorySettable)
//                {
//                    Minecraft.getInstance().displayGuiScreen(new GuiParaChest(player.inventory, (IInventorySettable) entity));
//                }
//
//                player.openContainer.windowId = (Integer) this.data.get(0);
//                break;
//            }
//            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_CONTAINER_SLOT_REFRESH, GCCoreUtil.getDimensionID(player.world), new Object[] { player.openContainer.windowId }));
            break;
        case C_UPDATE_WIRE_BOUNDS:
            BlockEntity tile = player.level.getBlockEntity((BlockPos) this.data.get(0));

            if (tile instanceof TileBaseConductor)
            {
                ((TileBaseConductor) tile).adjacentConnections = null;
//                player.world.getBlockState(tile.getPos()).getBlock().setBlockBoundsBasedOnState(player.world, tile.getPos()); TODO
            }
            break;
        case C_OPEN_SPACE_RACE_GUI:
//            if (Minecraft.getInstance().currentScreen == null)
//            {
//                TickHandlerClient.spaceRaceGuiScheduled = false;
//                player.openGui(GalacticraftCore.instance, GuiIdsCore.SPACE_RACE_START, player.world, (int) player.getPosX(), (int) player.getPosY(), (int) player.getPosZ());
//            }
//            else
//            {
//                TickHandlerClient.spaceRaceGuiScheduled = true;
//            } TODO
            break;
        case C_UPDATE_SPACE_RACE_DATA:
            Integer teamID = (Integer) this.data.get(0);
            String teamName = (String) this.data.get(1);
            FlagData flagData = (FlagData) this.data.get(2);
            Vector3 teamColor = (Vector3) this.data.get(3);
            List<String> playerList = new ArrayList<String>();

            for (int i = 4; i < this.data.size(); i++)
            {
                String playerName = (String) this.data.get(i);
                ClientProxyCore.flagRequestsSent.remove(playerName);
                playerList.add(playerName);
            }

            SpaceRace race = new SpaceRace(playerList, teamName, flagData, teamColor);
            race.setSpaceRaceID(teamID);
            SpaceRaceManager.addSpaceRace(race);
            break;
        case C_OPEN_JOIN_RACE_GUI:
            stats.setSpaceRaceInviteTeamID((Integer) this.data.get(0));
//            player.openGui(GalacticraftCore.instance, GuiIdsCore.SPACE_RACE_JOIN, player.world, (int) player.getPosX(), (int) player.getPosY(), (int) player.getPosZ()); TODO
            break;
        case C_UPDATE_DUNGEON_DIRECTION:
            stats.setDungeonDirection((Float) this.data.get(0));
            break;
        case C_UPDATE_FOOTPRINT_LIST:
            List<Footprint> printList = new ArrayList<Footprint>();
            long chunkKey = (Long) this.data.get(0);
            for (int i = 1; i < this.data.size(); i++)
            {
                Footprint print = (Footprint) this.data.get(i);
                if (!print.owner.equals(player.getName()))
                {
                    printList.add(print);
                }
            }
            FootprintRenderer.setFootprints(chunkKey, printList);
            break;
        case C_FOOTPRINTS_REMOVED:
            long chunkKey0 = (Long) this.data.get(0);
            BlockVec3 position = (BlockVec3) this.data.get(1);
            List<Footprint> footprintList = FootprintRenderer.footprints.get(chunkKey0);
            List<Footprint> toRemove = new ArrayList<Footprint>();

            if (footprintList != null)
            {
                for (Footprint footprint : footprintList)
                {
                    if (footprint.position.x > position.x && footprint.position.x < position.x + 1 &&
                            footprint.position.z > position.z && footprint.position.z < position.z + 1)
                    {
                        toRemove.add(footprint);
                    }
                }
            }

            if (!toRemove.isEmpty())
            {
                footprintList.removeAll(toRemove);
                FootprintRenderer.footprints.put(chunkKey0, footprintList);
            }
            break;
        case C_UPDATE_STATION_SPIN:
//            if (playerBaseClient.world.getDimension() instanceof DimensionSpaceStation)
//            {
//                ((DimensionSpaceStation) playerBaseClient.world.getDimension()).getSpinManager().setSpinRate((Float) this.data.get(0), (Boolean) this.data.get(1));
//            }
            break;
        case C_UPDATE_STATION_DATA:
//            if (playerBaseClient.world.getDimension() instanceof DimensionSpaceStation)
//            {
//                ((DimensionSpaceStation) playerBaseClient.world.getDimension()).getSpinManager().setSpinCentre((Double) this.data.get(0), (Double) this.data.get(1));
//            }
            break;
        case C_UPDATE_STATION_BOX:
//            if (playerBaseClient.world.getDimension() instanceof DimensionSpaceStation)
//            {
//                ((DimensionSpaceStation) playerBaseClient.world.getDimension()).getSpinManager().setSpinBox((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2), (Integer) this.data.get(3), (Integer) this.data.get(4), (Integer) this.data.get(5));
//            }
            break;
        case C_UPDATE_THERMAL_LEVEL:
            stats.setThermalLevel((Integer) this.data.get(0));
            stats.setThermalLevelNormalising((Boolean) this.data.get(1));
            break;
        case C_DISPLAY_ROCKET_CONTROLS:
//            player.sendMessage(new StringTextComponent(GameSettings.getKeymessage(KeyHandlerClient.spaceKey.getKeyCode()) + "  - " + I18n.get("gui.rocket.launch")));
//            player.sendMessage(new StringTextComponent(GameSettings.getKeymessage(KeyHandlerClient.leftKey.getKeyCode()) + " / " + GameSettings.getKeymessage(KeyHandlerClient.rightKey.getKeyCode()) + "  - " + I18n.get("gui.rocket.turn")));
//            player.sendMessage(new StringTextComponent(GameSettings.getKeymessage(KeyHandlerClient.accelerateKey.getKeyCode()) + " / " + GameSettings.getKeymessage(KeyHandlerClient.decelerateKey.getKeyCode()) + "  - " + I18n.get("gui.rocket.updown")));
//            player.sendMessage(new StringTextComponent(GameSettings.getKeymessage(KeyHandlerClient.openFuelGui.getKeyCode()) + "       - " + I18n.get("gui.rocket.inv"))); TODO
            break;
        case C_GET_CELESTIAL_BODY_LIST:
            String str = "";

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredPlanets().values())
            {
                str = str.concat(cBody.getUnlocalizedName() + ";");
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredMoons().values())
            {
                str = str.concat(cBody.getUnlocalizedName() + ";");
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredSatellites().values())
            {
                str = str.concat(cBody.getUnlocalizedName() + ";");
            }

            for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values())
            {
                str = str.concat(solarSystem.getUnlocalizedName() + ";");
            }

            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_COMPLETE_CBODY_HANDSHAKE, getDimensionID(), new Object[]{str}));
            break;
        case C_UPDATE_ENERGYUNITS:
//            CommandGCEnergyUnits.handleParamClientside((Integer) this.data.get(0)); TODO Commands
            break;
        case C_RESPAWN_PLAYER:
//            final Dimension dimension = WorldUtil.getProviderForNameClient((String) this.data.get(0));
//            final DimensionType dimID = GCCoreUtil.getDimensionID(dimension);
//            if (ConfigManagerCore.enableDebug.get())
//            {
//                GCLog.info("DEBUG: Client receiving respawn packet for dim " + dimID);
//            }
//            int par2 = (Integer) this.data.get(1);
//            String par3 = (String) this.data.get(2);
//            int par4 = (Integer) this.data.get(3);
//            WorldUtil.forceRespawnClient(dimID, par2, par3, par4); TODO
            break;
        case C_UPDATE_STATS:
//            stats.setBuildFlags((Integer) this.data.get(0));
//            BlockPanelLighting.updateClient(this.data); TODO
            break;
        case C_UPDATE_TELEMETRY:
            tile = player.level.getBlockEntity((BlockPos) this.data.get(0));
            if (tile instanceof TileEntityTelemetry)
            {
                ((TileEntityTelemetry) tile).receiveUpdate(data, this.getDimensionID());
            }
            break;
        case C_SEND_PLAYERSKIN:
            String strName = (String) this.data.get(0);
            String s1 = (String) this.data.get(1);
            String s2 = (String) this.data.get(2);
            String strUUID = (String) this.data.get(3);
            GameProfile gp = PlayerUtil.getOtherPlayerProfile(strName);
            if (gp == null)
            {
                gp = PlayerUtil.makeOtherPlayerProfile(strName, strUUID);
            }
            gp.getProperties().put("textures", new Property("textures", s1, s2));
            break;
        case C_SEND_OVERWORLD_IMAGE:
            try
            {
                int cx = (Integer) this.data.get(0);
                int cz = (Integer) this.data.get(1);
                byte[] bytes = (byte[]) this.data.get(2);
                MapUtil.receiveOverworldImageCompressed(cx, cz, bytes);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            break;
        case C_RECOLOR_PIPE:
            BlockEntity tileEntity = player.level.getBlockEntity((BlockPos) this.data.get(0));
            if (tileEntity instanceof TileEntityFluidPipe)
            {
                TileEntityFluidPipe pipe = (TileEntityFluidPipe) tileEntity;
                pipe.getNetwork().split(pipe);
                pipe.setNetwork(null);
            }
            break;
        case C_RECOLOR_ALL_GLASS:
//            BlockSpaceGlass.updateGlassColors((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2)); TODO
            break;
        case C_UPDATE_MACHINE_DATA:
            BlockEntity tile3 = player.level.getBlockEntity((BlockPos) this.data.get(0));
            if (tile3 instanceof ITileClientUpdates)
            {
                ((ITileClientUpdates) tile3).updateClient(this.data);
            }
            break;
        case C_LEAK_DATA:
            BlockEntity tile4 = player.level.getBlockEntity((BlockPos) this.data.get(0));
            if (tile4 instanceof TileEntityOxygenSealer)
            {
                ((ITileClientUpdates) tile4).updateClient(this.data);
            }
            break;
        case C_SPAWN_HANGING_SCHEMATIC:
//            EntityHangingSchematic entity = new EntityHangingSchematic(player.world, (BlockPos) this.data.get(0), Direction.byIndex((Integer) this.data.get(2)), (Integer) this.data.get(3));
//            ((ClientWorld)player.world).addEntityToWorld((Integer) this.data.get(1), entity); TODO
            break;
        default:
            break;
        }
    }

    @Override
    public void handleServerSide(Player player)
    {
        final ServerPlayer playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

        if (playerBase == null)
        {
            return;
        }

        final MinecraftServer server = playerBase.server;
        final GCPlayerStats stats = GCPlayerStats.get(playerBase);

        switch (this.type)
        {
        case S_RESPAWN_PLAYER:
            playerBase.connection.send(new ClientboundRespawnPacket(player.dimension, LevelData.obfuscateSeed(player.level.getLevelData().getSeed()), player.level.getLevelData().getGeneratorType(), playerBase.gameMode.getGameModeForPlayer()));
            break;
        case S_TELEPORT_ENTITY:
            TickHandlerServer.scheduleNewDimensionChange(new ScheduledDimensionChange(playerBase, (DimensionType) PacketSimple.this.data.get(0)));
            stats.setUsingPlanetSelectionGui(false);
            break;
        case S_IGNITE_ROCKET:
            if (!player.level.isClientSide && player.isAlive() && player.getVehicle() != null && player.getVehicle().isAlive() && player.getVehicle() instanceof EntityTieredRocket)
            {
                final EntityTieredRocket ship = (EntityTieredRocket) player.getVehicle();

                if (ship.launchPhase != EnumLaunchPhase.LANDING.ordinal())
                {
                    if (ship.hasValidFuel())
                    {
                        ItemStack stack2 = stats.getExtendedInventory().getItem(4);

                        if (stack2 != ItemStack.EMPTY && stack2.getItem() instanceof ItemParaChute || stats.getLaunchAttempts() > 0)
                        {
                            ship.igniteCheckingCooldown();
                            stats.setLaunchAttempts(0);
                        }
                        else if (stats.getChatCooldown() == 0 && stats.getLaunchAttempts() == 0)
                        {
                            player.sendMessage(new TranslatableComponent(("gui.rocket.warning.noparachute")));
                            stats.setChatCooldown(80);
                            stats.setLaunchAttempts(1);
                        }
                    }
                    else if (stats.getChatCooldown() == 0)
                    {
                        player.sendMessage(new TranslatableComponent(("gui.rocket.warning.nofuel")));
                        stats.setChatCooldown(250);
                    }
                }
            }
            break;
        case S_OPEN_SCHEMATIC_PAGE:
            if (player != null)
            {
                final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID((Integer) this.data.get(0));
                PlatformSpecific.openContainer((ServerPlayer) player, page.getContainerProvider(player));

//                player.openGui(GalacticraftCore.instance, page.getGuiID(), player.world, (Integer) this.data.get(1), (Integer) this.data.get(2), (Integer) this.data.get(3)); TODO
            }
            break;
        case S_OPEN_FUEL_GUI:
            if (player.getVehicle() instanceof EntityBuggy)
            {
                MenuProvider container = new SimpleMenuProvider((w, p, pl) -> new ContainerBuggy(w, p, ((EntityBuggy) player.getVehicle()).getBuggyType()), new TranslatableComponent("container.buggy"));
                PlatformSpecific.openContainer((ServerPlayer) player, container);
            }
            else if (player.getVehicle() instanceof EntitySpaceshipBase)
            {
//                player.openGui(GalacticraftCore.instance, GuiIdsCore.ROCKET_INVENTORY, player.world, (int) player.getPosX(), (int) player.getPosY(), (int) player.getPosZ()); TODO
            }
            break;
        case S_UPDATE_SHIP_YAW:
            if (player.getVehicle() instanceof EntitySpaceshipBase)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.getVehicle();

                if (ship != null)
                {
                    ship.yRot = (Float) this.data.get(0);
                }
            }
            break;
        case S_UPDATE_SHIP_PITCH:
            if (player.getVehicle() instanceof EntitySpaceshipBase)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.getVehicle();

                if (ship != null)
                {
                    ship.xRot = (Float) this.data.get(0);
                }
            }
            break;
        case S_SET_ENTITY_FIRE:
            Entity entity = player.level.getEntity((Integer) this.data.get(0));

            if (entity instanceof LivingEntity)
            {
                entity.setSecondsOnFire(3);
            }
            break;
        case S_BIND_SPACE_STATION_ID:
            DimensionType homeID = (DimensionType) this.data.get(0);
            if ((!stats.getSpaceStationDimensionData().containsKey(homeID) || stats.getSpaceStationDimensionData().get(homeID) == DimensionType.NETHER || stats.getSpaceStationDimensionData().get(homeID) == DimensionType.OVERWORLD)
                    && !ConfigManagerCore.disableSpaceStationCreation.get())
            {
                if (playerBase.abilities.instabuild || WorldUtil.getSpaceStationRecipe(homeID).matches(playerBase, true))
                {
                    GCTriggers.CREATE_SPACE_STATION.trigger(playerBase);
//                    WorldUtil.bindSpaceStationToNewDimension(playerBase.world, playerBase, homeID);
                    DimensionType createdStation = WorldUtil.createNewSpaceStation(playerBase.getUUID(), false);
                    SpaceStationWorldData.getStationData(player.level.getServer(), createdStation.getRegistryName(), homeID, player);
//                    dimNames.put(newID, "Space Station " + newID);
                    stats.getSpaceStationDimensionData().put(homeID, createdStation);
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, GCCoreUtil.getDimensionType(player.level), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), playerBase);
                }
            }
            break;
        case S_UNLOCK_NEW_SCHEMATIC:
            final AbstractContainerMenu container = player.containerMenu;

            if (container instanceof ContainerSchematic)
            {
                final ContainerSchematic schematicContainer = (ContainerSchematic) container;

                ItemStack stack = schematicContainer.craftMatrix.getItem(0);

                if (!stack.isEmpty())
                {
                    final ISchematicPage page = SchematicRegistry.getMatchingRecipeForItemStack(stack);

                    if (page != null)
                    {
                        SchematicRegistry.unlockNewPage(playerBase, stack);
                        SpaceRaceManager.teamUnlockSchematic(playerBase, stack);
                        stack.shrink(1);

                        schematicContainer.craftMatrix.setItem(0, stack);
                        schematicContainer.craftMatrix.setChanged();

                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_ADD_NEW_SCHEMATIC, getDimensionID(), new Object[]{page.getPageID()}), playerBase);
                    }
                }
            }
            break;
        case S_UPDATE_DISABLEABLE_BUTTON:
            final BlockEntity tileAt = player.level.getBlockEntity((BlockPos) this.data.get(0));

            if (tileAt instanceof IDisableableMachine)
            {
                final IDisableableMachine machine = (IDisableableMachine) tileAt;

                machine.setDisabled((Integer) this.data.get(1), !machine.getDisabled((Integer) this.data.get(1)));
            }
            break;
        case S_ON_FAILED_CHEST_UNLOCK:
            if (stats.getChatCooldown() == 0)
            {
                player.sendMessage(new TranslatableComponent(WithFormat("gui.chest.warning.wrongkey", this.data.get(0))));
                stats.setChatCooldown(100);
            }
            break;
        case S_RENAME_SPACE_STATION:
//            final SpaceStationWorldData ssdata = SpaceStationWorldData.getStationData(playerBase.world, (Integer) this.data.get(1), playerBase);
//
//            if (ssdata != null && ssdata.getOwner().equalsIgnoreCase(PlayerUtil.getName(player)))
//            {
//                ssdata.setSpaceStationName((String) this.data.get(0));
//                ssdata.setDirty(true);
//            } TODO
            break;
        case S_OPEN_EXTENDED_INVENTORY:
            MenuProvider containerExtended = new SimpleMenuProvider((w, p, pl) -> new ContainerExtendedInventory(w, p, stats.getExtendedInventory()), new TranslatableComponent("container.cargo_loader"));
            PlatformSpecific.openContainer((ServerPlayer) player, containerExtended);
            break;
        case S_ON_ADVANCED_GUI_CLICKED_INT:
            BlockEntity tile1 = player.level.getBlockEntity((BlockPos) this.data.get(1));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.redstoneActivation = (Integer) this.data.get(2) == 1;
                }
                break;
            case 1:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.playerDistanceActivation = (Integer) this.data.get(2) == 1;
                }
                break;
            case 2:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.playerDistanceSelection = (Integer) this.data.get(2);
                }
                break;
            case 3:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.playerNameMatches = (Integer) this.data.get(2) == 1;
                }
                break;
            case 4:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.invertSelection = (Integer) this.data.get(2) == 1;
                }
                break;
            case 5:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.lastHorizontalModeEnabled = airlockController.horizontalModeEnabled;
                    airlockController.horizontalModeEnabled = (Integer) this.data.get(2) == 1;
                }
                break;
            case 6:
                if (tile1 instanceof IBubbleProvider)
                {
                    IBubbleProvider distributor = (IBubbleProvider) tile1;
                    distributor.setBubbleVisible((Integer) this.data.get(2) == 1);
                }
                break;
            default:
                break;
            }
            break;
        case S_ON_ADVANCED_GUI_CLICKED_STRING:
            BlockEntity tile2 = player.level.getBlockEntity((BlockPos) this.data.get(1));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile2 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile2;
                    airlockController.playerToOpenFor = (String) this.data.get(2);
                }
                break;
            default:
                break;
            }
            break;
        case S_UPDATE_SHIP_MOTION_Y:
            int entityID = (Integer) this.data.get(0);
            boolean up = (Boolean) this.data.get(1);

            Entity entity2 = player.level.getEntity(entityID);

            if (entity2 instanceof EntityAutoRocket)
            {
                EntityAutoRocket autoRocket = (EntityAutoRocket) entity2;
//                autoRocket.motionY += up ? 0.02F : -0.02F; TODO
            }

            break;
        case S_START_NEW_SPACE_RACE:
            Integer teamID = (Integer) this.data.get(0);
            String teamName = (String) this.data.get(1);
            FlagData flagData = (FlagData) this.data.get(2);
            Vector3 teamColor = (Vector3) this.data.get(3);
            List<String> playerList = new ArrayList<String>();

            for (int i = 4; i < this.data.size(); i++)
            {
                playerList.add((String) this.data.get(i));
            }

            boolean previousData = SpaceRaceManager.getSpaceRaceFromID(teamID) != null;

            SpaceRace newRace = new SpaceRace(playerList, teamName, flagData, teamColor);

            if (teamID > 0)
            {
                newRace.setSpaceRaceID(teamID);
            }

            SpaceRaceManager.addSpaceRace(newRace);

            if (previousData)
            {
                SpaceRaceManager.sendSpaceRaceData(server, null, SpaceRaceManager.getSpaceRaceFromPlayer(PlayerUtil.getName(playerBase)));
            }
            break;
        case S_REQUEST_FLAG_DATA:
            SpaceRaceManager.sendSpaceRaceData(server, playerBase, SpaceRaceManager.getSpaceRaceFromPlayer((String) this.data.get(0)));
            break;
        case S_INVITE_RACE_PLAYER:
            ServerPlayer playerInvited = PlayerUtil.getPlayerBaseServerFromPlayerUsername(server, (String) this.data.get(0), true);
            if (playerInvited != null)
            {
                Integer teamInvitedTo = (Integer) this.data.get(1);
                SpaceRace race = SpaceRaceManager.getSpaceRaceFromID(teamInvitedTo);

                if (race != null)
                {
                    GCPlayerStats.get(playerInvited).setSpaceRaceInviteTeamID(teamInvitedTo);
                    String dA = EnumColor.DARK_AQUA.getCode();
                    String bG = EnumColor.BRIGHT_GREEN.getCode();
                    String dB = EnumColor.PURPLE.getCode();
                    String teamNameTotal = "";
                    String[] teamNameSplit = race.getTeamName().split(" ");
                    for (String teamNamePart : teamNameSplit)
                    {
                        teamNameTotal = teamNameTotal.concat(dB + teamNamePart + " ");
                    }
                    playerInvited.sendMessage(new TextComponent(dA + I18n.getWithFormat("gui.space_race.chat.invite_received", bG + PlayerUtil.getName(player) + dA) + "  " + I18n.getWithFormat("gui.space_race.chat.to_join", teamNameTotal, EnumColor.AQUA + "/joinrace" + dA)).setStyle(new Style().setColor(ChatFormatting.DARK_AQUA)));
                }
            }
            break;
        case S_REMOVE_RACE_PLAYER:
            Integer teamInvitedTo = (Integer) this.data.get(1);
            SpaceRace race = SpaceRaceManager.getSpaceRaceFromID(teamInvitedTo);

            if (race != null)
            {
                String playerToRemove = (String) this.data.get(0);

                if (!race.getPlayerNames().remove(playerToRemove))
                {
                    player.sendMessage(new TranslatableComponent(WithFormat("gui.space_race.chat.not_found", playerToRemove)));
                }
                else
                {
                    SpaceRaceManager.onPlayerRemoval(server, playerToRemove, race);
                }
            }
            break;
        case S_ADD_RACE_PLAYER:
            Integer teamToAddPlayer = (Integer) this.data.get(1);
            SpaceRace spaceRaceToAddPlayer = SpaceRaceManager.getSpaceRaceFromID(teamToAddPlayer);

            if (spaceRaceToAddPlayer != null)
            {
                String playerToAdd = (String) this.data.get(0);

                if (!spaceRaceToAddPlayer.getPlayerNames().contains(playerToAdd))
                {
                    SpaceRace oldRace = null;
                    while ((oldRace = SpaceRaceManager.getSpaceRaceFromPlayer(playerToAdd)) != null)
                    {
                        SpaceRaceManager.removeSpaceRace(oldRace);
                    }

                    spaceRaceToAddPlayer.getPlayerNames().add(playerToAdd);
                    SpaceRaceManager.sendSpaceRaceData(server, null, spaceRaceToAddPlayer);

                    for (String member : spaceRaceToAddPlayer.getPlayerNames())
                    {
                        ServerPlayer memberObj = PlayerUtil.getPlayerForUsernameVanilla(server, member);

                        if (memberObj != null)
                        {
                            memberObj.sendMessage(new TextComponent(EnumColor.DARK_AQUA + I18n.getWithFormat("gui.space_race.chat.add_success", EnumColor.BRIGHT_GREEN + playerToAdd + EnumColor.DARK_AQUA)).setStyle(new Style().setColor(ChatFormatting.DARK_AQUA)));
                        }
                    }
                }
                else
                {
                    player.sendMessage(new TranslatableComponent(("gui.space_race.chat.already_part")).setStyle(new Style().setColor(ChatFormatting.DARK_RED)));
                }
            }
            break;
        case S_COMPLETE_CBODY_HANDSHAKE:
            String completeList = (String) this.data.get(0);
            List<String> clientObjects = Arrays.asList(completeList.split(";"));
            List<String> serverObjects = Lists.newArrayList();
            String missingObjects = "";

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredPlanets().values())
            {
                serverObjects.add(cBody.getUnlocalizedName());
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredMoons().values())
            {
                serverObjects.add(cBody.getUnlocalizedName());
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredSatellites().values())
            {
                serverObjects.add(cBody.getUnlocalizedName());
            }

            for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values())
            {
                serverObjects.add(solarSystem.getUnlocalizedName());
            }

            for (String str : serverObjects)
            {
                if (!clientObjects.contains(str))
                {
                    missingObjects = missingObjects.concat(str + "\n");
                }
            }

            if (missingObjects.length() > 0)
            {
                playerBase.connection.disconnect(new TextComponent("Missing Galacticraft Celestial Objects:\n\n " + missingObjects));
            }

            break;
        case S_REQUEST_GEAR_DATA1:
        case S_REQUEST_GEAR_DATA2:
            UUID id = (UUID) this.data.get(0);
            if (id != null)
            {
                Player otherPlayer = player.level.getPlayerByUUID(id);
                if (otherPlayer instanceof ServerPlayer)
                {
                    GCPlayerHandler.checkGear((ServerPlayer) otherPlayer, GCPlayerStats.get(otherPlayer), true);
                }
            }
            break;
        case S_BUILDFLAGS_UPDATE:
            stats.setBuildFlags((Integer) this.data.get(0));
            break;
        case S_REQUEST_OVERWORLD_IMAGE:
            MapUtil.sendOverworldToClient(playerBase);
            break;
        case S_REQUEST_MAP_IMAGE:
            int dim = (Integer) this.data.get(0);
            int cx = (Integer) this.data.get(1);
            int cz = (Integer) this.data.get(2);
            MapUtil.sendOrCreateMap(WorldUtil.getProviderForDimensionServer(DimensionType.getById(dim)).getWorld(), cx, cz, playerBase);
            break;
        case S_REQUEST_PLAYERSKIN:
            String strName = (String) this.data.get(0);
            ServerPlayer playerRequested = server.getPlayerList().getPlayerByName(strName);

            //Player not online
            if (playerRequested == null)
            {
                return;
            }

            GameProfile gp = playerRequested.getGameProfile();
            if (gp == null)
            {
                return;
            }

            Property property = (Property) Iterables.getFirst(gp.getProperties().get("textures"), (Object) null);
            if (property == null)
            {
                return;
            }
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SEND_PLAYERSKIN, getDimensionID(), new Object[]{strName, property.getValue(), property.getSignature(), playerRequested.getUUID().toString()}), playerBase);
            break;
        case S_CONTROL_ENTITY:
            if (player.getVehicle() != null && player.getVehicle() instanceof IControllableEntity)
            {
                ((IControllableEntity) player.getVehicle()).pressKey((Integer) this.data.get(0));
            }
            break;
        case S_NOCLIP_PLAYER:
            boolean noClip = (Boolean) this.data.get(0);
            if (player instanceof GCEntityPlayerMP)
            {
                GalacticraftCore.proxy.player.setNoClip((ServerPlayer) player, noClip);
                if (noClip == false)
                {
                    player.fallDistance = 0.0F;
//                    ((ServerPlayerEntity)player).connection.floatingTickCount = 0; TODO
                }
            }
            else if (player instanceof ServerPlayer)
            {
                ServerPlayer emp = ((ServerPlayer) player);
                try
                {
                    Field f = emp.gameMode.getClass().getDeclaredField(GCCoreUtil.isDeobfuscated() ? "gameType" : "field_73091_c");
                    f.setAccessible(true);
                    if (noClip == false)
                    {
                        emp.fallDistance = 0.0F;
//                        emp.connection.floatingTickCount = 0; TODO
                        GameType gt = savedSettings.get(emp);
                        if (gt != null)
                        {
                            savedSettings.remove(emp);
                            f.set(emp.gameMode, gt);
                        }
                    }
                    else
                    {
                        savedSettings.put(emp, emp.gameMode.getGameModeForPlayer());
                        f.set(emp.gameMode, GameType.SPECTATOR);
                    }
                }
                catch (Exception ee)
                {
                    ee.printStackTrace();
                }
            }
            break;
        case S_REQUEST_DATA:
            ServerLevel worldServer = server.getLevel((DimensionType) this.data.get(0));
            BlockEntity requestedTile = worldServer.getBlockEntity((BlockPos) this.data.get(1));
            if (requestedTile instanceof INetworkProvider)
            {
                if (((INetworkProvider) requestedTile).getNetwork() instanceof FluidNetwork)
                {
                    FluidNetwork network = (FluidNetwork) ((INetworkProvider) requestedTile).getNetwork();
                    network.addUpdate(playerBase);
                }
            }
            break;
        case S_UPDATE_CHECKLIST:
            for (InteractionHand enumhand : InteractionHand.values())
            {
                ItemStack stack = player.getItemInHand(enumhand);
                if (stack.getItem() == GCItems.prelaunchChecklist)
                {
                    CompoundTag tagCompound = stack.getTag();
                    if (tagCompound == null)
                    {
                        tagCompound = new CompoundTag();
                    }
                    CompoundTag tagCompoundRead = (CompoundTag) this.data.get(0);
                    tagCompound.put("checklistData", tagCompoundRead);
                    stack.setTag(tagCompound);
                }
            }
            break;
        case S_REQUEST_MACHINE_DATA:
            BlockEntity tile3 = player.level.getBlockEntity((BlockPos) this.data.get(0));
            if (tile3 instanceof ITileClientUpdates)
            {
                ((ITileClientUpdates) tile3).sendUpdateToClient(playerBase);
            }
            break;
        case S_REQUEST_CONTAINER_SLOT_REFRESH:
            // It seems as though "Update slot" packets sent internally on the minecraft network packets are sent and
            // received before our custom gui open packets are handled. This causes slots to not update, because from
            // the client's perspective the gui isn't open yet. Sending this to the server causes all slots to be updated
            // server -> client
            if (player.containerMenu.containerId == (Integer) this.data.get(0))
            {
//                for (int i = 0; i < player.openContainer.inventoryItemStacks.size(); ++i)
//                {
//                    player.openContainer.inventoryItemStacks.set(i, ItemStack.EMPTY);
//                } TODO
            }
            break;
        case S_ROTATE_ROCKET:
            Integer entityId = (Integer) this.data.get(0);
            if (entityId > 0)
            {
                Entity e = player.level.getEntity(entityId);
                if (e != null)
                {
                    e.xRot = (float) this.data.get(1);
                    e.yRot = (float) this.data.get(2);
                }
            }
        default:
            break;
        }
    }

    /*
     *
     * BEGIN "net.minecraft.network.Packet" IMPLEMENTATION
     *
     * This is for handling server->client packets before the player has joined the world
     *
     */

    @Override
    public void read(FriendlyByteBuf var1)
    {
        this.decodeInto(var1);
    }

    @Override
    public void write(FriendlyByteBuf var1)
    {
        this.encodeInto(var1);
    }

    public static void handle(final PacketSimple message, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            if (GCCoreUtil.getEffectiveSide() == EnvType.CLIENT)
            {
                message.handleClientSide(MinecraftClient.getInstance().player);
            }
            else
            {
                message.handleServerSide(ctx.get().getSender());
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public boolean isSkippable()
    {
        return false;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void handle(PacketListener handler)
    {
        if (this.type != EnumSimplePacket.C_UPDATE_SPACESTATION_LIST && this.type != EnumSimplePacket.C_UPDATE_PLANETS_LIST && this.type != EnumSimplePacket.C_UPDATE_CONFIGS)
        {
            return;
        }

        if (GCCoreUtil.getEffectiveSide() == EnvType.CLIENT)
        {
            this.handleClientSide(Minecraft.getInstance().player);
        }
    }

    /*
     *
     * END "net.minecraft.network.Packet" IMPLEMENTATION
     *
     * This is for handling server->client packets before the player has joined the world
     *
     */
}
