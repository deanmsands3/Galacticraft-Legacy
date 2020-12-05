package team.galacticraft.galacticraft.common.api.transmission.tile;

import team.galacticraft.galacticraft.common.api.transmission.grid.IElectricityNetwork;

public interface IConductor extends ITransmitter
{
    /**
     * @return The tier of this conductor - must be 1 or 2
     */
    int getTierGC();

    /**
     * @return This conductor's electricity network.
     */
    @Override
    IElectricityNetwork getNetwork();
}
