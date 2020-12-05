package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.vector.Vector2;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketEntityUpdate extends PacketBase
{
    private int entityID;
    private Vector3D position;
    private float rotationYaw;
    private float rotationPitch;
    private Vector3D motion;
    private boolean onGround;

    public PacketEntityUpdate()
    {
        super();
    }

    public PacketEntityUpdate(int entityID, Vector3D position, Vector2 rotation, Vector3D motion, boolean onGround, DimensionType dimID)
    {
        super(dimID);
        this.entityID = entityID;
        this.position = position;
        this.rotationYaw = rotation.x;
        this.rotationPitch = rotation.y;
        this.motion = motion;
        this.onGround = onGround;
    }

    public PacketEntityUpdate(Entity entity)
    {
        this(entity.getId(), new Vector3D(entity.getX(), entity.getY(), entity.getZ()), new Vector2(entity.yRot, entity.xRot), new Vector3D(entity.getDeltaMovement()), entity.onGround, GCCoreUtil.getDimensionType(entity.level));
    }

    public static void encode(final PacketEntityUpdate message, final FriendlyByteBuf buf)
    {
        message.encodeInto(buf);
    }

    public static PacketEntityUpdate decode(FriendlyByteBuf buf)
    {
        PacketEntityUpdate packet = new PacketEntityUpdate();
        packet.decodeInto(buf);
        return packet;
    }

    public static void handle(final PacketEntityUpdate message, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
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
    public void encodeInto(ByteBuf buffer)
    {
        super.encodeInto(buffer);
        buffer.writeInt(this.entityID);
        buffer.writeDouble(this.position.x);
        buffer.writeDouble(this.position.y);
        buffer.writeDouble(this.position.z);
        buffer.writeFloat(this.rotationYaw);
        buffer.writeFloat(this.rotationPitch);
        buffer.writeDouble(this.motion.x);
        buffer.writeDouble(this.motion.y);
        buffer.writeDouble(this.motion.z);
        buffer.writeBoolean(this.onGround);
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        super.decodeInto(buffer);
        this.entityID = buffer.readInt();
        this.position = new Vector3D(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.rotationYaw = buffer.readFloat();
        this.rotationPitch = buffer.readFloat();
        this.motion = new Vector3D(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.onGround = buffer.readBoolean();
    }

    @Override
    public void handleClientSide(Player player)
    {
        this.setEntityData(player);
    }

    @Override
    public void handleServerSide(Player player)
    {
        this.setEntityData(player);
    }

    private void setEntityData(Player player)
    {
        Entity entity = player.level.getEntity(this.entityID);

        if (entity instanceof IEntityFullSync)
        {
            if (player.level.isClientSide || player.getUUID().equals(((IEntityFullSync) entity).getOwnerUUID()) || ((IEntityFullSync) entity).getOwnerUUID() == null)
            {
                IEntityFullSync controllable = (IEntityFullSync) entity;
                controllable.setPositionRotationAndMotion(this.position.x, this.position.y, this.position.z, this.rotationYaw, this.rotationPitch, this.motion.x, this.motion.y, this.motion.z, this.onGround);
            }
        }
    }

    public interface IEntityFullSync
    {
        void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ, boolean onGround);

        UUID getOwnerUUID();
    }
}
