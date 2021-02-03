package team.galacticraft.forge;

import java.io.File;

import net.minecraftforge.fml.loading.FMLPaths;

public class GalacticraftForge {
	
	public static File getConfigDirectory() {
		return FMLPaths.CONFIGDIR.get().toFile();
	}
}
