package micdoodle8.mods.galacticraft.core.network;

import com.google.common.collect.Sets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.event.EventNetworkChannel;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.*;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConnectionPacket
{
    private static final ResourceLocation NAME = new ResourceLocation(Constants.MOD_ID_CORE, "event_channel");
    public static final EventNetworkChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(NAME)
            .clientAcceptedVersions(getProtocolVersion()::equals)
            .serverAcceptedVersions(getProtocolVersion()::equals)
            .networkProtocolVersion(ConnectionPacket::getProtocolVersion).eventNetworkChannel();

    private static String getProtocolVersion()
    {
        return GalacticraftCore.instance == null ? "999.999.999" : GalacticraftCore.instance.versionNumber.toString();
    }

//    public static SimpleChannel bus;

    public void handle(ByteBuf payload, Player player)
    {
        int packetId = payload.readByte();
        List<Object> data = new ArrayList<Object>();
        switch (packetId)
        {
        case 101:
            int length = payload.readInt();
            for (int i = 0; i < length; i++)
            {
                data.add(payload.readInt());
            }
            WorldUtil.decodePlanetsListClient(data);
            break;
        case 102:
            int llength = payload.readInt();
            for (int i = 0; i < llength; i++)
            {
                data.add(payload.readInt());
            }
            WorldUtil.decodeSpaceStationListClient(data);
            break;
        case 103:
//            try
//            {
//                data = NetworkUtil.decodeData(EnumSimplePacket.C_UPDATE_CONFIGS.getDecodeClasses(), payload);
//                ConfigManagerCore.saveClientConfigOverrideable();
//                ConfigManagerCore.setConfigOverride(data);
//                if (ConfigManagerCore.enableDebug.get())
//                {
//                    GCLog.info("Server-set configs received OK on client.");
//                }
//            }
//            catch (Exception e)
//            {
//                System.err.println("[Galacticraft] Error handling connection packet - maybe the player's Galacticraft version does not match the server version?");
//                e.printStackTrace();
//            } TODO Sync configs
            break;
        default:
        }
        if (payload.readInt() != 3519)
        {
            GCLog.severe("Packet completion problem for connection packet " + packetId + " - maybe the player's Galacticraft version does not match the server version?");
        }
    }

    public static ClientboundCustomPayloadPacket createDimPacket(List<DimensionType> dims)
    {
        ArrayList<DimensionType> data = new ArrayList<>(dims);
        return createPacket((byte) 101, data);
    }

    public static ClientboundCustomPayloadPacket createSSPacket(Set<DimensionType> dims)
    {
        HashSet<DimensionType> data = new HashSet<>(dims);
        return createPacket((byte) 102, data);
    }

    public static ClientboundCustomPayloadPacket createPacket(byte packetId, Collection<DimensionType> data)
    {
        FriendlyByteBuf payload = new FriendlyByteBuf(Unpooled.buffer());

        payload.writeByte(packetId);
        payload.writeInt(data.size());
        for (DimensionType i : data)
        {
            payload.writeInt(i.getId());
        }
        payload.writeInt(3519); //signature
        return new ClientboundCustomPayloadPacket(NAME, payload);
    }
//
//    public static FMLProxyPacket createConfigPacket(List<Object> data)
//    {
//        PacketBuffer payload = new PacketBuffer(Unpooled.buffer());
//        payload.writeByte(103);
//        try
//        {
//            NetworkUtil.encodeData(payload, data);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        payload.writeInt(3519); //signature
//        return new FMLProxyPacket(payload, CHANNEL);
//    }

    @SubscribeEvent
    @Environment(EnvType.CLIENT)
    public void onPacketData(NetworkEvent.ClientCustomPayloadEvent event)
    {
        onData(event.getPayload(), Minecraft.getInstance().player);
    }

    @SubscribeEvent
    public void onPacketData(NetworkEvent.ServerCustomPayloadEvent event)
    {
        onData(event.getPayload(), event.getSource().get().getSender());
    }

    public void onData(FriendlyByteBuf buffer, Player player)
    {
        try
        {
            if (buffer == null)
            {
                throw new RuntimeException("Empty packet sent to Galacticraft channel");
            }
            this.handle(buffer, player);
        }
        catch (Exception e)
        {
            GCLog.severe("GC login packet handler: Failed to read packet");
            GCLog.severe(e.toString());
            e.printStackTrace();
        }
    }
}
