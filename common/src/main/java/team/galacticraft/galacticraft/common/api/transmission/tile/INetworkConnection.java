package team.galacticraft.galacticraft.common.api.transmission.tile;

import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Applied to TileEntities.
 *
 * @author Calclavia
 */
public interface INetworkConnection extends IConnector
{

    /**
     * Gets a list of all the connected TileEntities that this conductor is
     * connected to. The array's length should be always the 6 adjacent wires.
     *
     * @return
     */
    BlockEntity[] getAdjacentConnections();

    /**
     * Refreshes the conductor
     */
    void refresh();

    void onNetworkChanged();
}
