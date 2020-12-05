package team.galacticraft.galacticraft.common.api.transmission.tile;

import team.galacticraft.galacticraft.common.api.transmission.NetworkType;

public interface ITransmitter extends INetworkProvider, INetworkConnection
{
    NetworkType getNetworkType();

    boolean canTransmit();
}
