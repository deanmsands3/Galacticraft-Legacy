package team.galacticraft.galacticraft.common.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;

public abstract class PacketBase implements IPacket
{
    private DimensionType dimensionID;

    public PacketBase()
    {
        this.dimensionID = null;
    }

    public PacketBase(DimensionType dimensionID)
    {
        this.dimensionID = dimensionID;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        if (dimensionID == null)
        {
            throw new IllegalStateException("Invalid Dimension ID! [GC]");
        }
        NetworkUtil.writeUTF8String(buffer, this.dimensionID.getRegistryName().toString());
//        buffer.writeInt(this.dimensionID);
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        this.dimensionID = DimensionType.getByName(new ResourceLocation(NetworkUtil.readUTF8String(buffer)));
    }

    @Override
    public DimensionType getDimensionID()
    {
        return dimensionID;
    }
}
