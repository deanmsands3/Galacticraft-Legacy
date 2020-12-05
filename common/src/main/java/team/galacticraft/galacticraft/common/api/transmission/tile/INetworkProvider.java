package team.galacticraft.galacticraft.common.api.transmission.tile;

import team.galacticraft.galacticraft.common.api.transmission.grid.IGridNetwork;

public interface INetworkProvider
{
    IGridNetwork getNetwork();

    boolean hasNetwork();

    void setNetwork(IGridNetwork network);
}
