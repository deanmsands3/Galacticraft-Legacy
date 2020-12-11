package team.galacticraft.galacticraft.common.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * PacketDynamic is used for updating data for regularly updating Entities and TileEntities
 * Two types of PacketDynamic:
 * Type 0: the identifier is an Entity ID
 * Type 1: the identifier is a TileEntity's BlockPos
 */
public class PacketDynamic extends PacketBase
{
    private int type;
    private Object identifier;
    private ArrayList<Object> sendData;
    private ByteBuf payloadData;

    public PacketDynamic()
    {
        super();
    }

    public PacketDynamic(Entity entity)
    {
        super(GCCoreUtil.getDimensionType(entity.level));
        assert entity instanceof IPacketReceiver : "Entity does not implement " + IPacketReceiver.class.getSimpleName();
        this.type = 0;
        this.identifier = entity.getId();
        this.sendData = new ArrayList<>();
        ((IPacketReceiver) entity).getNetworkedData(this.sendData);
    }

    public PacketDynamic(BlockEntity tile)
    {
        super(GCCoreUtil.getDimensionType(tile.getLevel()));
        assert tile instanceof IPacketReceiver : "TileEntity does not implement " + IPacketReceiver.class.getSimpleName();
        this.type = 1;
        this.identifier = tile.getBlockPos();
        this.sendData = new ArrayList<>();
        ((IPacketReceiver) tile).getNetworkedData(this.sendData);
    }

    public static void encode(final PacketDynamic message, final FriendlyByteBuf buf)
    {
        message.encodeInto(buf);
    }

    public static PacketDynamic decode(FriendlyByteBuf buf)
    {
        PacketDynamic packet = new PacketDynamic();
        packet.decodeInto(buf);
        return packet;
    }

    public static void handle(final PacketDynamic message, Supplier<NetworkEvent.Context> ctx)
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

    public boolean isEmpty()
    {
        return sendData.isEmpty();
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        super.encodeInto(buffer);
        buffer.writeInt(this.type);

        switch (this.type)
        {
        case 0:
            buffer.writeInt((Integer) this.identifier);
            break;
        case 1:
            BlockPos bp = (BlockPos) this.identifier;
            buffer.writeInt(bp.getX());
            buffer.writeInt(bp.getY());
            buffer.writeInt(bp.getZ());
            break;
        }

        ByteBuf payloadData = Unpooled.buffer();

        try
        {
            NetworkUtil.encodeData(payloadData, this.sendData);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        int readableBytes = payloadData.readableBytes();
        buffer.writeInt(readableBytes);
        buffer.writeBytes(payloadData);
    }

    @Override
    public void decodeInto(ByteBuf buffer) throws IndexOutOfBoundsException
    {
        super.decodeInto(buffer);
        this.type = buffer.readInt();
//        World world = GalacticraftCore.proxy.getWorldForID(this.getDimensionID());
//
//        if (world == null)
//        {
//            FMLLog.severe("Failed to get world for dimension ID: " + this.getDimensionID());
//        }
//
        switch (this.type)
        {
        case 0:
            this.identifier = new Integer(buffer.readInt());

            int length = buffer.readInt();
            payloadData = Unpooled.copiedBuffer(buffer.readBytes(length));
//                if (entity instanceof IPacketReceiver && buffer.readableBytes() > 0)
            break;
        case 1:
            this.identifier = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());

            length = buffer.readInt();
            payloadData = Unpooled.copiedBuffer(buffer.readBytes(length));

            break;
        }
    }

    @Override
    public void handleClientSide(Player player)
    {
        this.handleData(EnvType.CLIENT, player);
    }

    @Override
    public void handleServerSide(Player player)
    {
        this.handleData(EnvType.SERVER, player);
    }

    private void handleData(EnvType EnvType, Player player)
    {
        switch (this.type)
        {
        case 0:
            Entity entity = player.level.getEntity((Integer) this.identifier);

            if (entity instanceof IPacketReceiver)
            {
                if (this.payloadData.readableBytes() > 0)
                {
                    ((IPacketReceiver) entity).decodePacketdata(payloadData);
                }

                //Treat any packet received by a server from a client as an update request specifically to that client
                if (EnvType == net.minecraftforge.fml.EnvType.SERVER && player instanceof ServerPlayer && entity != null)
                {
                    GalacticraftCore.packetPipeline.sendTo(new PacketDynamic(entity), (ServerPlayer) player);
                }
            }
            break;

        case 1:
            BlockPos bp = (BlockPos) this.identifier;
            if (player.level.hasChunkAt(bp))
            {
                BlockEntity tile = player.level.getBlockEntity(bp);

                if (tile instanceof IPacketReceiver)
                {
                    if (this.payloadData.readableBytes() > 0)
                    {
                        ((IPacketReceiver) tile).decodePacketdata(payloadData);
                    }

                    //Treat any packet received by a server from a client as an update request specifically to that client
                    if (EnvType == net.minecraftforge.fml.EnvType.SERVER && player instanceof ServerPlayer && tile != null)
                    {
                        GalacticraftCore.packetPipeline.sendTo(new PacketDynamic(tile), (ServerPlayer) player);
                    }
                }
            }
            break;
        }
    }
}
