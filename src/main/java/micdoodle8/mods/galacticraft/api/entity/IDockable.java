package micdoodle8.mods.galacticraft.api.entity;

import micdoodle8.mods.galacticraft.api.tile.IFuelDock;

/**
 * Implement into entities that are placed on fuel docks to load with cargo and
 * fuel
 */
public interface IDockable extends IFuelable, ICargoEntity
{

    /**
     * Sets the current fuel dock for this entity
     */
    void setPad(IFuelDock pad);

    /**
     * Gets the fuel dock when required. Must return the same one that was set.
     */
    IFuelDock getLandingPad();

    /**
     * When the fuel dock is destroyed. Most likely kills the entity and drops
     * it's containing items.
     */
    void onPadDestroyed();

    /**
     * Whether or not the fuel dock this entity is on is valid. <p> Returning
     * false will not let setPad be called.
     */
    boolean isDockValid(IFuelDock dock);

    boolean inFlight();
}
