package team.galacticraft.galacticraft.common.core.network;
//
//import com.google.common.collect.ImmutableMap;
//import com.google.common.collect.Maps;
//import com.google.common.collect.Queues;
//import io.netty.channel.ChannelHandler.Sharable;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import team.galacticraft.galacticraft.common.core.GalacticraftCore;
//import team.galacticraft.galacticraft.common.core.tick.TickHandlerClient;
//import team.galacticraft.galacticraft.common.core.tick.TickHandlerServer;
//import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.network.INetHandler;
//import net.minecraft.network.NetworkManager;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.World;
//import net.minecraft.world.dimension.DimensionType;
//import net.minecraftforge.fml.EnvType;
//import net.minecraftforge.fml.network.NetworkRegistry;
//import net.minecraftforge.fml.network.simple.SimpleChannel;
//
//import java.util.Map;
//import java.util.Queue;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Sharable
//public class GalacticraftPacketHandler extends SimpleChannelInboundHandler<IPacket>
//{
//    private final Map<EnvType, Map<Integer, Queue<PacketPlayerPair>>> packetMap;
//    private static volatile int livePacketCount = 0;
//
//    public GalacticraftPacketHandler()
//    {
//        Map<EnvType, Map<Integer, Queue<PacketPlayerPair>>> map = Maps.newHashMap();
//        for (EnvType EnvType : EnvType.values())
//        {
//            Map<Integer, Queue<PacketPlayerPair>> sideMap = new ConcurrentHashMap<Integer, Queue<PacketPlayerPair>>();
//            map.put(EnvType, sideMap);
//        }
//
//        packetMap = ImmutableMap.copyOf(map);
//        if (GCCoreUtil.getEffectiveSide() == EnvType.CLIENT)
//        {
//            TickHandlerClient.addPacketHandler(this);
//        }
//        TickHandlerServer.addPacketHandler(this);
//    }
//
//    public void unload(World world)
//    {
//        EnvType side = world.isRemote ? EnvType.CLIENT : EnvType.SERVER;
//        DimensionType dimID = GCCoreUtil.getDimensionID(world);
//        Queue<PacketPlayerPair> queue = getQueue(side, dimId);
//        queue.clear();
//    }
//
//    public void tick(World world)
//    {
//        PacketPlayerPair pair;
//        EnvType side = world.isRemote ? EnvType.CLIENT : EnvType.SERVER;
//        DimensionType dimID = GCCoreUtil.getDimensionType(world);
//        Queue<PacketPlayerPair> queue = getQueue(EnvType, dimID);
//        while ((pair = queue.poll()) != null)
//        {
//            switch (side)
//            {
//            case CLIENT:
//                pair.getPacket().handleClientSide(pair.getPlayer());
//                break;
//            case SERVER:
//                pair.getPacket().handleServerSide(pair.getPlayer());
//                break;
//            }
//        }
//    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, IPacket msg) throws Exception
//    {
//        PlayerEntity player = GalacticraftCore.proxy.getPlayerFromNetHandler(netHandler);
//
//        if (player == null)
//        {
//            return;
//        }
//
//        if (EnvType != null)
//        {
//            getQueue(EnvType, msg.getDimensionID()).add(new PacketPlayerPair(msg, player));
//            livePacketCount++;
//        }
//    }
//
//    private Queue<PacketPlayerPair> getQueue(EnvType EnvType, DimensionType dimID)
//    {
//        Map<Integer, Queue<PacketPlayerPair>> map = packetMap.get(EnvType);
//        if (!map.containsKey(dimID))
//        {
//            map.put(dimID, Queues.<PacketPlayerPair>newConcurrentLinkedQueue());
//        }
//        return map.get(dimID);
//    }
//
//    private final class PacketPlayerPair
//    {
//        private IPacket packet;
//        private PlayerEntity player;
//
//        public PacketPlayerPair(IPacket packet, PlayerEntity player)
//        {
//            this.packet = packet;
//            this.player = player;
//        }
//
//        public IPacket getPacket()
//        {
//            return packet;
//        }
//
//        public void setPacket(IPacket packet)
//        {
//            this.packet = packet;
//        }
//
//        public PlayerEntity getPlayer()
//        {
//            return player;
//        }
//
//        public void setPlayer(PlayerEntity player)
//        {
//            this.player = player;
//        }
//    }
//}
