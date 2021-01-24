package micdoodle8.mods.galacticraft.api.galaxies;

import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class Satellite extends CelestialBody implements IChildBody
{
    protected Planet parentCelestialBody = null;
    protected int dimensionIdStatic = 0;

    public Satellite(String satelliteName)
    {
        super(satelliteName);
        this.autoRegisterDimension = false; // Addons need to register satellites manually
        this.isReachable = true;
    }

    @Override
    public Planet getParentPlanet()
    {
        return this.parentCelestialBody;
    }

    public Satellite setParentBody(Planet parentCelestialBody)
    {
        this.parentCelestialBody = parentCelestialBody;
        return this;
    }

    @Override
    @Deprecated
    public CelestialBody setDimensionInfo(DimensionType type, Class<? extends LevelStem> providerClass, boolean autoRegister)
    {
        throw new UnsupportedOperationException("Cannot set individual satellite types");
    }

    @Override
    public int getID()
    {
        return GalaxyRegistry.getSatelliteID(this.bodyName);
    }

    @Override
    public String getUnlocalizedNamePrefix()
    {
        return "satellite";
    }

    public int getDimensionIdStatic()
    {
        return dimensionIdStatic;
    }
}
