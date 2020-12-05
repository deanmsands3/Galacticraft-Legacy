package team.galacticraft.galacticraft.common.api.world;

/**
 * For world providers where you would like to specify height for spacecraft to
 * be teleported
 * <p/>
 * Implement into world providers
 */
public interface IExitHeight
{
    /**
     * @return y-coordinate that spacecraft leaves the dimension
     */
    double getYCoordinateToTeleport();
}
