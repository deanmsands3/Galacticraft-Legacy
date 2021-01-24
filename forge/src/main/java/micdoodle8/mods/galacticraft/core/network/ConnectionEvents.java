package micdoodle8.mods.galacticraft.core.network;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.DimensionSpaceStation;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.*;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

public class ConnectionEvents
{
    private static ConnectionEvents INSTANCE;

//    private static boolean clientConnected = false;
    public static void register(IEventBus bus)
    {
        INSTANCE = new ConnectionEvents();
        bus.register(INSTANCE);
    }

    static
    {
        ConnectionProtocol.PROTOCOL_BY_PACKET.put(PacketSimple.class, ProtocolType.PLAY);
        registerPacket(PacketDirection.CLIENTBOUND, PacketSimple.class, PacketSimple::new);
    }

    protected static <P extends Packet<?>> void registerPacket(PacketFlow direction, Class<P> clazz, Supplier<P> supplier)
    {
        Object o = ProtocolType.PLAY.field_229711_h_.get(direction);
        Class<? extends ConnectionProtocol> outerClass = ProtocolType.PLAY.getClass();
        Class<?>[] innerClasses = outerClass.getDeclaredClasses();
        Class<?> packetListClass = innerClasses[1];
        try
        {
            Method registerMethod = packetListClass.getDeclaredMethod("func_229721_a_", Class.class, Supplier.class);
            registerMethod.setAccessible(true);
            registerMethod.invoke(o, clazz, supplier);
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }

//        if (bimap == null)
//        {
//            bimap = HashBiMap.create();
//            ProtocolType.PLAY.field_229711_h_.put(direction, bimap);
//        }
//
//        if (bimap.containsValue(packetClass))
//        {
//            String s = direction + " packet " + packetClass + " is already known to ID " + bimap.inverse().get(packetClass);
//            LogManager.getLogger().fatal(s);
//            throw new IllegalArgumentException(s);
//        }
//        else
//        {
//            bimap.put(Integer.valueOf(bimap.size()), packetClass);
//            return ProtocolType.PLAY;
//        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
    {
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getPlayer() instanceof ServerPlayer)
        {
            ServerPlayer thePlayer = (ServerPlayer) event.getPlayer();
            GCPlayerStats stats = GCPlayerStats.get(thePlayer);
//            SpaceStationWorldData.checkAllStations(thePlayer, stats); TODO
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, GCCoreUtil.getDimensionType(thePlayer.level), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), thePlayer);
            SpaceRace raceForPlayer = SpaceRaceManager.getSpaceRaceFromPlayer(PlayerUtil.getName(thePlayer));
            if (raceForPlayer != null)
            {
                SpaceRaceManager.sendSpaceRaceData(thePlayer.server, thePlayer, raceForPlayer);
            }
        }

        if (event.getPlayer().world.getDimension() instanceof DimensionSpaceStation && event.getPlayer() instanceof ServerPlayer)
        {
//            ((DimensionSpaceStation) event.getPlayer().world.getDimension()).getSpinManager().sendPackets((ServerPlayerEntity) event.getPlayer()); TODO Spin Manager
        }
    }

    @SubscribeEvent
    public void onConnectionReceived(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (ConfigManagerCore.enableDebug.get())
        {
            List<DimensionType> idList = WorldUtil.getPlanetList();
            StringBuilder ids = new StringBuilder();
            for (DimensionType dimensionType : idList)
            {
                ids.append(dimensionType.toString()).append(" ");
            }
            GCLog.info("Galacticraft server sending dimension IDs to connecting client: " + ids);
        }
        Connection networkManager = ((ServerPlayer) event.getPlayer()).connection.connection;
        networkManager.send(ConnectionPacket.createDimPacket(WorldUtil.getPlanetListInts()));
        networkManager.send(ConnectionPacket.createSSPacket(WorldUtil.getSpaceStationListInts()));
//        networkManager.sendPacket(ConnectionPacket.createConfigPacket(ConfigManagerCore.getServerConfigOverride.get()()));
    }

//    @SubscribeEvent
//    public void onConnectionOpened(ClientConnectedToServerEvent event)
//    {
//        if (!event.isLocal())
//        {
//            ConnectionEvents.clientConnected = true;
//        }
//    }
//
//    @SubscribeEvent
//    public void onConnectionClosed(ClientDisconnectionFromServerEvent event)
//    {
//        TickHandlerClient.menuReset = true;
//        if (ConnectionEvents.clientConnected)
//        {
//            ConnectionEvents.clientConnected = false;
//            WorldUtil.unregisterPlanets();
//            WorldUtil.unregisterSpaceStations();
//            ConfigManagerCore.restoreClientConfigOverrideable.get()();
//        }
//    }
}
