package team.galacticraft.galacticraft.common.api.transmission.tile;

public interface IBufferTransmitter<N> extends ITransmitter
{
    N getBuffer();

    int getCapacity();
}
