package team.galacticraft.galacticraft.common.api.entity;

/**
 * Implement into entities to allow transmission of data via telemetry
 */
public interface ITelemetry
{
    void transmitData(int[] data);

    void receiveData(int[] data, String[] str);

    void adjustDisplay(int[] data);
}
