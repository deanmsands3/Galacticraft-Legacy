package micdoodle8.mods.galacticraft.planets;

import micdoodle8.mods.galacticraft.Constants;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import static micdoodle8.mods.galacticraft.util.Injection.Null;

@ObjectHolder(Constants.MOD_ID_PLANETS)
public class GCPlanetsBiomes {

	@ObjectHolder("mars")
	public static final Biome MARS_FLAT = Null();
	@ObjectHolder("asteroids")
	public static final Biome ASTEROIDS = Null();
	@ObjectHolder("venus")
	public static final Biome VENUS = Null();
	@ObjectHolder("venus_mountain")
	public static final Biome VENUS_MOUNTAIN = Null();
	@ObjectHolder("venus_valley")
	public static final Biome VENUS_VALLEY = Null();
	
}
