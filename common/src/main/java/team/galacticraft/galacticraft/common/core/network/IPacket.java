package team.galacticraft.galacticraft.common.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.DimensionType;

public interface IPacket
{
    void encodeInto(ByteBuf buffer);

    void decodeInto(ByteBuf buffer);

    void handleClientSide(Player player);

    void handleServerSide(Player player);

    DimensionType getDimensionID();
}
