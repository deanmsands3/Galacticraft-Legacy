package micdoodle8.mods.galacticraft.api.transmission.tile;

/**
 * This interface is to be applied to all TileEntities which stores electricity
 * within them.
 *
 * @author Calclavia
 */
public interface IElectricalStorage
{

    /**
     * Sets the amount of joules this unit has stored.
     */
    void setEnergyStored(float energy);

    /**
     * * @return Get the amount of energy currently stored in the block.
     */
    float getEnergyStored();

    /**
     * @return Get the max amount of energy that can be stored in the block.
     */
    float getMaxEnergyStored();
}
