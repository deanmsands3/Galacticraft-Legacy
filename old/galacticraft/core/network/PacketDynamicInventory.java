package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCrafting;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.function.Supplier;

public class PacketDynamicInventory extends PacketBase
{
    private int type;
    private Object identifier;
    private ItemStack[] stacks;

    public PacketDynamicInventory()
    {
        super();
    }

    public PacketDynamicInventory(Entity entity)
    {
        super(GCCoreUtil.getDimensionType(entity.level));
        assert entity instanceof Container : "Entity does not implement " + Container.class.getSimpleName();
        this.type = 0;
        this.identifier = new Integer(entity.getId());
        this.stacks = new ItemStack[((Container) entity).getContainerSize()];

        for (int i = 0; i < this.stacks.length; i++)
        {
            this.stacks[i] = ((Container) entity).getItem(i);
        }
    }

    public PacketDynamicInventory(BlockEntity tile)
    {
        super(GCCoreUtil.getDimensionType(tile.getLevel()));
        assert tile instanceof Container : "Tile does not implement " + Container.class.getSimpleName();
        Container chest = ((Container) tile);
        this.type = 1;
        this.identifier = tile.getBlockPos();
        int size = chest.getContainerSize();
        if (chest instanceof TileEntityCrafting)
        {
            this.stacks = new ItemStack[size + 1];
            this.stacks[size] = ((TileEntityCrafting) chest).getMemoryHeld();
        }
        else
        {
            this.stacks = new ItemStack[size];
        }

        for (int i = 0; i < size; i++)
        {
            this.stacks[i] = chest.getItem(i);
        }
    }

    public static void encode(final PacketDynamicInventory message, final FriendlyByteBuf buf)
    {
        message.encodeInto(buf);
    }

    public static PacketDynamicInventory decode(FriendlyByteBuf buf)
    {
        PacketDynamicInventory packet = new PacketDynamicInventory();
        packet.decodeInto(buf);
        return packet;
    }

    public static void handle(final PacketDynamicInventory message, Supplier<NetworkEvent.Context> ctx)
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
        buffer.writeInt(this.type);

        switch (this.type)
        {
        case 0:
            buffer.writeInt((Integer) this.identifier);
            break;
        case 1:
            BlockPos pos = (BlockPos) this.identifier;
            buffer.writeInt(pos.getX());
            buffer.writeInt(pos.getY());
            buffer.writeInt(pos.getZ());
            break;
        }

        buffer.writeInt(this.stacks.length);

        for (int i = 0; i < this.stacks.length; i++)
        {
            try
            {
                NetworkUtil.writeItemStack(this.stacks[i], buffer);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        super.decodeInto(buffer);
        this.type = buffer.readInt();

        switch (this.type)
        {
        case 0:
            this.identifier = new Integer(buffer.readInt());
            break;
        case 1:
            this.identifier = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
            break;
        }

        this.stacks = new ItemStack[buffer.readInt()];

        for (int i = 0; i < this.stacks.length; i++)
        {
            try
            {
                this.stacks[i] = NetworkUtil.readItemStack(buffer);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleClientSide(Player player)
    {
        if (player.level == null)
        {
            return;
        }

        switch (this.type)
        {
        case 0:
            Entity entity = player.level.getEntity((Integer) this.identifier);

            if (entity instanceof IInventorySettable)
            {
                this.setInventoryStacks((IInventorySettable) entity);
            }

            break;
        case 1:
            BlockEntity tile = player.level.getBlockEntity((BlockPos) this.identifier);

            if (tile instanceof TileEntityCrafting)
            {
                ((TileEntityCrafting) tile).setStacksClientSide(this.stacks);
            }
            else if (tile instanceof IInventorySettable)
            {
                this.setInventoryStacks((IInventorySettable) tile);
            }

            break;
        }
    }

    @Override
    public void handleServerSide(Player player)
    {
        switch (this.type)
        {
        case 0:
            Entity entity = player.level.getEntity((Integer) this.identifier);

            if (entity instanceof IInventorySettable)
            {
                GalacticraftCore.packetPipeline.sendTo(new PacketDynamicInventory(entity), (ServerPlayer) player);
            }

            break;
        case 1:
            BlockEntity tile = player.level.getBlockEntity((BlockPos) this.identifier);

            if (tile instanceof IInventorySettable)
            {
                GalacticraftCore.packetPipeline.sendTo(new PacketDynamicInventory(tile), (ServerPlayer) player);
            }

            break;
        }
    }

    private void setInventoryStacks(IInventorySettable inv)
    {
        inv.setSizeInventory(this.stacks.length);

        for (int i = 0; i < this.stacks.length; i++)
        {
            inv.setItem(i, this.stacks[i]);
        }
    }
}
