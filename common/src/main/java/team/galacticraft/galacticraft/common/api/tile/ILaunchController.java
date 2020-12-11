package team.galacticraft.galacticraft.common.api.tile;

public interface ILaunchController {
    boolean isValidFrequency();

    int getDestinationFrequency();

    int getFrequency();
}
