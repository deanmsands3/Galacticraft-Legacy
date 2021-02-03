package micdoodle8.mods.galacticraft.planets.asteroids.network;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.network.PacketBase;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.GrappleEntity;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class PacketSimpleAsteroids extends PacketBase
{
    public enum EnumSimplePacketAsteroids
    {
        // SERVER
        S_UPDATE_ADVANCED_GUI(LogicalSide.SERVER, Integer.class, BlockPos.class, Integer.class),
        // CLIENT
        C_TELEPAD_SEND(LogicalSide.CLIENT, BlockVec3.class, Integer.class),
        C_UPDATE_GRAPPLE_POS(LogicalSide.CLIENT, Integer.class, Vector3.class);

        private final LogicalSide targetSide;
        private final Class<?>[] decodeAs;

        EnumSimplePacketAsteroids(LogicalSide targetSide, Class<?>... decodeAs)
        {
            this.targetSide = targetSide;
            this.decodeAs = decodeAs;
        }

        public LogicalSide getTargetSide()
        {
            return this.targetSide;
        }

        public Class<?>[] getDecodeClasses()
        {
            return this.decodeAs;
        }
    }

    private EnumSimplePacketAsteroids type;
    private List<Object> data;

    public PacketSimpleAsteroids()
    {
        super();
    }

    public PacketSimpleAsteroids(EnumSimplePacketAsteroids packetType, DimensionType dimID, Object[] data)
    {
        this(packetType, dimID, Arrays.asList(data));
    }

    public PacketSimpleAsteroids(EnumSimplePacketAsteroids packetType, DimensionType dimID, List<Object> data)
    {
        super(dimID);

        if (packetType.getDecodeClasses().length != data.size())
        {
            GCLog.info("Asteroids Simple Packet found data length different than packet type: " + packetType.name());
        }

        this.type = packetType;
        this.data = data;
    }

    public static void encode(final PacketSimpleAsteroids message, final FriendlyByteBuf buf)
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

    public static PacketSimpleAsteroids decode(FriendlyByteBuf buf)
    {
        PacketSimpleAsteroids.EnumSimplePacketAsteroids type = PacketSimpleAsteroids.EnumSimplePacketAsteroids.values()[buf.readInt()];
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
        return new PacketSimpleAsteroids(type, dim, data);
    }

    public static void handle(final PacketSimpleAsteroids message, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
            {
                message.handleClientSide(Minecraft.getInstance().player);
            }
            else
            {
                message.handleServerSide(ctx.get().getSender());
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        super.encodeInto(buffer);
        buffer.writeInt(this.type.ordinal());

        try
        {
            NetworkUtil.encodeData(buffer, this.data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        super.decodeInto(buffer);
        this.type = EnumSimplePacketAsteroids.values()[buffer.readInt()];

        if (this.type.getDecodeClasses().length > 0)
        {
            this.data = NetworkUtil.decodeData(this.type.getDecodeClasses(), buffer);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClientSide(Player player)
    {
        LocalPlayer playerBaseClient = null;

        if (player instanceof LocalPlayer)
        {
            playerBaseClient = (LocalPlayer) player;
        }

        BlockEntity tile;
        switch (this.type)
        {
        case C_TELEPAD_SEND:
            Entity entity = playerBaseClient.level.getEntity((Integer) this.data.get(1));

            if (entity != null && entity instanceof LivingEntity)
            {
                BlockVec3 pos = (BlockVec3) this.data.get(0);
                entity.setPos(pos.x + 0.5, pos.y + 2.2, pos.z + 0.5);
            }
            break;
        case C_UPDATE_GRAPPLE_POS:
            entity = playerBaseClient.level.getEntity((Integer) this.data.get(0));
            if (entity != null && entity instanceof GrappleEntity)
            {
                Vector3 vec = (Vector3) this.data.get(1);
                entity.setPos(vec.x, vec.y, vec.z);
            }
            break;
        default:
            break;
        }
    }

    @Override
    public void handleServerSide(Player player)
    {
        ServerPlayer playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

        switch (this.type)
        {
        case S_UPDATE_ADVANCED_GUI:
            BlockEntity tile = player.level.getBlockEntity((BlockPos) this.data.get(1));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile instanceof TileEntityShortRangeTelepad)
                {
                    TileEntityShortRangeTelepad launchController = (TileEntityShortRangeTelepad) tile;
                    launchController.setAddress((Integer) this.data.get(2));
                }
                break;
            case 1:
                if (tile instanceof TileEntityShortRangeTelepad)
                {
                    TileEntityShortRangeTelepad launchController = (TileEntityShortRangeTelepad) tile;
                    launchController.setTargetAddress((Integer) this.data.get(2));
                }
                break;
            default:
                break;
            }
            break;
        default:
            break;
        }
    }
}
