package team.galacticraft.galacticraft.common.core.network;

import com.google.common.collect.Sets;
import io.netty.buffer.ByteBuf;
import team.galacticraft.galacticraft.common.api.transmission.tile.IBufferTransmitter;
import team.galacticraft.galacticraft.common.core.fluid.FluidNetwork;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Fluid;
import me.shedaniel.architectury.fluid.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.function.Supplier;

public class PacketFluidNetworkUpdate extends PacketBase
{
    private PacketType type;
    private BlockPos pos;

    private FluidStack stack;
    private Fluid fluidType;
    private boolean didTransfer;

    private boolean newNetwork;
    private Collection<IBufferTransmitter<FluidStack>> transmittersAdded;
    private Collection<BlockPos> transmittersCoords;

    public PacketFluidNetworkUpdate()
    {
    }

    public static PacketFluidNetworkUpdate getFluidUpdate(DimensionType dimensionID, BlockPos pos, FluidStack stack, boolean didTransfer)
    {
        return new PacketFluidNetworkUpdate(PacketType.FLUID, dimensionID, pos, stack, didTransfer);
    }

    public static PacketFluidNetworkUpdate getAddTransmitterUpdate(DimensionType dimensionID, BlockPos pos, boolean newNetwork, Collection<IBufferTransmitter<FluidStack>> transmittersAdded)
    {
        return new PacketFluidNetworkUpdate(PacketType.ADD_TRANSMITTER, dimensionID, pos, newNetwork, transmittersAdded);
    }

    public static void encode(final PacketFluidNetworkUpdate message, final FriendlyByteBuf buf)
    {
        message.encodeInto(buf);
    }

    public static PacketFluidNetworkUpdate decode(FriendlyByteBuf buf)
    {
        PacketFluidNetworkUpdate packet = new PacketFluidNetworkUpdate();
        packet.decodeInto(buf);
        return packet;
    }

    public static void handle(final PacketFluidNetworkUpdate message, Supplier<NetworkEvent.Context> ctx)
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

    private PacketFluidNetworkUpdate(PacketType type, DimensionType dimensionID, BlockPos pos, FluidStack stack, boolean didTransfer)
    {
        super(dimensionID);
        this.type = type;
        this.pos = pos;
        this.stack = stack;
        this.didTransfer = didTransfer;
    }

    private PacketFluidNetworkUpdate(PacketType type, DimensionType dimensionID, BlockPos pos, boolean newNetwork, Collection<IBufferTransmitter<FluidStack>> transmittersAdded)
    {
        super(dimensionID);
        this.type = type;
        this.pos = pos;
        this.newNetwork = newNetwork;
        this.transmittersAdded = transmittersAdded;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        super.encodeInto(buffer);
        buffer.writeInt(this.pos.getX());
        buffer.writeInt(this.pos.getY());
        buffer.writeInt(this.pos.getZ());
        buffer.writeInt(this.type.ordinal());

        switch (this.type)
        {
        case ADD_TRANSMITTER:
            buffer.writeBoolean(this.newNetwork);
            buffer.writeInt(this.transmittersAdded.size());

            for (IBufferTransmitter transmitter : this.transmittersAdded)
            {
                BlockEntity tile = (BlockEntity) transmitter;
                buffer.writeInt(tile.getBlockPos().getX());
                buffer.writeInt(tile.getBlockPos().getY());
                buffer.writeInt(tile.getBlockPos().getZ());
            }
            break;
        case FLUID:
            if (this.stack != null)
            {
                buffer.writeBoolean(true);
                NetworkUtil.writeUTF8String(buffer, this.stack.getFluid().getRegistryName().toString());
                buffer.writeInt(this.stack.getAmount());
            }
            else
            {
                buffer.writeBoolean(false);
            }

            buffer.writeBoolean(this.didTransfer);
            break;
        }
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        super.decodeInto(buffer);
        this.pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        this.type = PacketType.values()[buffer.readInt()];

        switch (this.type)
        {
        case ADD_TRANSMITTER:
            this.newNetwork = buffer.readBoolean();
            this.transmittersCoords = Sets.newHashSet();
            int transmitterCount = buffer.readInt();

            for (int i = 0; i < transmitterCount; ++i)
            {
                this.transmittersCoords.add(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
            }
            break;
        case FLUID:
            if (buffer.readBoolean())
            {
                this.fluidType = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(NetworkUtil.readUTF8String(buffer)));
                if (this.fluidType != null)
                {
                    this.stack = FluidStack.create(this.fluidType, buffer.readInt());
                }
            }
            else
            {
                this.fluidType = null;
                this.stack = null;
            }

            this.didTransfer = buffer.readBoolean();
            break;
        }
    }

    @Override
    public void handleClientSide(Player player)
    {
        BlockEntity tile = player.level.getBlockEntity(this.pos);

        if (tile instanceof IBufferTransmitter)
        {
            IBufferTransmitter<FluidStack> transmitter = (IBufferTransmitter<FluidStack>) tile;

            switch (this.type)
            {
            case ADD_TRANSMITTER:
            {
                FluidNetwork network = transmitter.hasNetwork() && !this.newNetwork ? (FluidNetwork) transmitter.getNetwork() : new FluidNetwork();
                network.register();
                transmitter.setNetwork(network);

                for (BlockPos pos : this.transmittersCoords)
                {
                    BlockEntity transmitterTile = player.level.getBlockEntity(pos);

                    if (transmitterTile instanceof IBufferTransmitter)
                    {
                        ((IBufferTransmitter) transmitterTile).setNetwork(network);
                    }
                }

                network.updateCapacity();
            }
            break;
            case FLUID:
                if (transmitter.getNetwork() != null)
                {
                    FluidNetwork network = (FluidNetwork) transmitter.getNetwork();

                    if (this.fluidType != null)
                    {
                        network.refFluid = this.fluidType;
                    }

                    network.buffer = this.stack;
                    network.didTransfer = this.didTransfer;
                }
                break;
            }
        }
    }

    @Override
    public void handleServerSide(Player player)
    {

    }

    public enum PacketType
    {
        ADD_TRANSMITTER,
        FLUID,
    }
}
