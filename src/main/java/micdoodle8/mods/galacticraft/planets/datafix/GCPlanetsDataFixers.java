package micdoodle8.mods.galacticraft.planets.datafix;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.datafix.base.AbstractFixer;

public class GCPlanetsDataFixers extends AbstractFixer
{

    private static final int VERSION = 1;

    public GCPlanetsDataFixers()
    {
        super(Constants.MOD_ID_PLANETS, VERSION);
    }

    @Override
    public void registerAll()
    {
        registerFixer(new PlanetsTileEntityFixer());
    }

}
