package team.galacticraft.galacticraft.common.api.tile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.List;


/***
 * Sends basic tile load configuration data
 * e.g. facing  (if not obtainable from blockState)
 * to the client.
 *
 * IMPORTANT: call this.clientValidate() from the tile's validate() method
 */
public interface ITileClientUpdates
{
    /**
     * The supplied data array of ints
     * ALWAYS has length 4.  You don't
     * have to use all of them!
     */
    void buildDataPacket(int[] data);

    /**
     * The supplied data list has 4 ints
     * of data to use at positions 1 through 4.
     */
    @Environment(EnvType.CLIENT)
    void updateClient(List<Object> data);
//todo scary networking
//    /**
//     * Implement validate() in the tile and call this!
//     */
//    default void clientOnLoad()
//    {
//        BlockEntity tile = (BlockEntity) this;
//        if (tile.getLevel().isClientSide)
//        {
//            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_REQUEST_MACHINE_DATA, tile.getLevel(), new Object[]{tile.getBlockPos()}));
//        }
//    }
//
//    /**
//     * Do not override unless you want to use a custom data packet
//     * with more than 4 ints of data.
//     * (If overriding this you must override all other methods in
//     * ITileClientUpdates as well ... in which case, why are you using it?)
//     */
//    default void sendUpdateToClient(ServerPlayer player)
//    {
//        int[] data = new int[4];
//        this.buildDataPacket(data);
//        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_MACHINE_DATA, GCCoreUtil.getType(player.level), new Object[]{((BlockEntity) this).getBlockPos(), data[0], data[1], data[2], data[3]}), player);
//    }
//
//    /**
//     * Used to push updates out to clients
//     */
//    default void updateAllInDimension()
//    {
//        int[] data = new int[4];
//        this.buildDataPacket(data);
//        DimensionType dimID = GCCoreUtil.getType(((BlockEntity) this).getLevel());
//        GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_MACHINE_DATA, dimID, new Object[]{((BlockEntity) this).getBlockPos(), data[0], data[1], data[2], data[3]}), dimID);
//    }
}
