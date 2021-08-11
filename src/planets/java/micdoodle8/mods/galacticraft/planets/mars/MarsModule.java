package micdoodle8.mods.galacticraft.planets.mars;

import java.lang.reflect.Method;
import java.util.List;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.world.AtmosphereInfo;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.Constants;
import micdoodle8.mods.galacticraft.GalacticraftCore;
import micdoodle8.mods.galacticraft.entities.EntityEvolvedCreeper;
import micdoodle8.mods.galacticraft.entities.EntityEvolvedEnderman;
import micdoodle8.mods.galacticraft.entities.EntityEvolvedSkeleton;
import micdoodle8.mods.galacticraft.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.items.ItemBucketGC;
import micdoodle8.mods.galacticraft.util.ColorUtil;
import micdoodle8.mods.galacticraft.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.util.GCLog;
import micdoodle8.mods.galacticraft.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.GCPlanetDimensions;
import micdoodle8.mods.galacticraft.planets.GCPlanetsBiomes;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockSludge;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.dimension.TeleportTypeMars;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCreeperBoss;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityLandingBalloons;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityProjectileTNT;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySludgeling;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerElectrolyzer;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerGasLiquefier;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerMethaneSynthesizer;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerTerraformer;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemSchematicTier2;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.recipe.RecipeManagerMars;
import micdoodle8.mods.galacticraft.planets.mars.schematic.SchematicCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.schematic.SchematicTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityDungeonSpawnerMars;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityElectrolyzer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityGasLiquefier;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntitySlimelingEgg;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTerraformer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTreasureChestMars;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.biome.BiomeMars;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class MarsModule implements IPlanetsModule
{
	public static Fluid sludge;
	public static Fluid sludgeGC;
	public static Material sludgeMaterial = new MaterialLiquid(MapColor.FOLIAGE);

	public static Planet planetMars;

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		MarsModule.planetMars = (Planet) new Planet("mars").setParentSolarSystem(GalacticraftCore.solarSystemSol).setRingColorRGB(0.67F, 0.1F, 0.1F).setPhaseShift(0.1667F)
				.setRelativeSize(0.5319F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.25F, 1.25F)).setRelativeOrbitTime(1.8811610076670317634173055859803F);

		MinecraftForge.EVENT_BUS.register(new EventHandlerMars());

		if (!FluidRegistry.isFluidRegistered("bacterialsludge"))
		{
			ResourceLocation stillIcon = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/fluids/sludge_still");
			ResourceLocation flowingIcon = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/fluids/sludge_flow");
			sludgeGC = new Fluid("bacterialsludge", stillIcon, flowingIcon).setDensity(800).setViscosity(1500);
			FluidRegistry.registerFluid(sludgeGC);
		} else
		{
			GCLog.info("Galacticraft sludge is not default, issues may occur.");
		}

		sludge = FluidRegistry.getFluid("bacterialsludge");

		if (sludge.getBlock() == null)
		{
			MarsBlocks.blockSludge = new BlockSludge("sludge");
			((BlockSludge) MarsBlocks.blockSludge).setQuantaPerBlock(3);
			MarsBlocks.registerBlock(MarsBlocks.blockSludge, ItemBlockDesc.class);
			sludge.setBlock(MarsBlocks.blockSludge);
		} else
		{
			MarsBlocks.blockSludge = sludge.getBlock();
		}

		if (MarsBlocks.blockSludge != null)
		{
			FluidRegistry.addBucketForFluid(sludge); // Create a Universal Bucket AS WELL AS our type, this is needed to pull fluids
														// out of other mods tanks
			MarsItems.bucketSludge = new ItemBucketGC(MarsBlocks.blockSludge, sludge).setTranslationKey("bucket_sludge");
			MarsItems.registerItem(MarsItems.bucketSludge);
			EventHandlerGC.bucketList.put(MarsBlocks.blockSludge, MarsItems.bucketSludge);
		}

		MarsBlocks.initBlocks();
		MarsItems.initItems();

		MarsModule.planetMars.setBiomeInfo(GCPlanetsBiomes.MARS_FLAT);
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		MarsBlocks.oreDictRegistration();
		this.registerMicroBlocks();
		SchematicRegistry.registerSchematicRecipe(new SchematicTier2Rocket());
		SchematicRegistry.registerSchematicRecipe(new SchematicCargoRocket());

		GalacticraftCore.packetPipeline.addDiscriminator(6, PacketSimpleMars.class);

		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();

		MarsModule.planetMars.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/mars.png"));
		MarsModule.planetMars.setDimensionInfo(ConfigManagerMars.dimensionIDMars, WorldProviderMars.class).setTierRequired(2);
		MarsModule.planetMars.setAtmosphere(new AtmosphereInfo(false, false, false, -1.0F, 0.3F, 0.1F));
		MarsModule.planetMars.atmosphereComponent(EnumAtmosphericGas.CO2).atmosphereComponent(EnumAtmosphericGas.ARGON).atmosphereComponent(EnumAtmosphericGas.NITROGEN);
		MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(EntityEvolvedZombie.class, 8, 2, 3));
		MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(EntityEvolvedSpider.class, 8, 2, 3));
		MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(EntityEvolvedSkeleton.class, 8, 2, 3));
		MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(EntityEvolvedCreeper.class, 8, 2, 3));
		MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(EntityEvolvedEnderman.class, 10, 1, 4));
		MarsModule.planetMars.addChecklistKeys("equip_oxygen_suit", "thermal_padding");

		GalaxyRegistry.registerPlanet(MarsModule.planetMars);
		GalacticraftRegistry.registerTeleportType(WorldProviderMars.class, new TeleportTypeMars());
		GalacticraftRegistry.registerRocketGui(WorldProviderMars.class, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/mars_rocket_gui.png"));
		GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematic, 1, 0));
		GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematic, 1, 1));
		GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematic, 1, 2));

		GalacticraftCore.proxy.registerFluidTexture(MarsModule.sludge, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/misc/underbecterial.png"));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		RecipeManagerMars.loadCompatibilityRecipes();
		GCPlanetDimensions.MARS = WorldUtil.getDimensionTypeById(ConfigManagerMars.dimensionIDMars);
		ItemSchematicTier2.registerSchematicItems();
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event)
	{
	}

	@Override
	public void serverInit(FMLServerStartedEvent event)
	{

	}

	private void registerMicroBlocks()
	{
		try
		{
			Class<?> clazz = Class.forName("codechicken.microblock.MicroMaterialRegistry");
			if (clazz != null)
			{
				Method registerMethod = null;
				Method[] methodz = clazz.getMethods();
				for (Method m : methodz)
				{
					if (m.getName().equals("registerMaterial"))
					{
						registerMethod = m;
						break;
					}
				}
				Class<?> clazzbm = Class.forName("codechicken.microblock.BlockMicroMaterial");
				registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 4), "tile.mars.marscobblestone");
				registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 5), "tile.mars.marsgrass");
				registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 6), "tile.mars.marsdirt");
				registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 7), "tile.mars.marsdungeon");
				registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 8), "tile.mars.marsdeco");
				registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 9), "tile.mars.marsstone");
			}
		} catch (Exception e)
		{
		}
	}

	public void registerTileEntities()
	{
		registerTileEntity(TileEntitySlimelingEgg.class, "GC Slimeling Egg");
		registerTileEntity(TileEntityTreasureChestMars.class, "GC Tier 2 Treasure Chest");
		registerTileEntity(TileEntityTerraformer.class, "GC Planet Terraformer");
		registerTileEntity(TileEntityCryogenicChamber.class, "GC Cryogenic Chamber");
		registerTileEntity(TileEntityGasLiquefier.class, "GC Gas Liquefier");
		registerTileEntity(TileEntityMethaneSynthesizer.class, "GC Methane Synthesizer");
		registerTileEntity(TileEntityElectrolyzer.class, "GC Water Electrolyzer");
		registerTileEntity(TileEntityDungeonSpawnerMars.class, "GC Mars Dungeon Spawner");
		registerTileEntity(TileEntityLaunchController.class, "GC Launch Controller");
	}

	public void registerCreatures()
	{
		this.registerGalacticraftCreature(EntitySludgeling.class, "sludgeling", ColorUtil.to32BitColor(255, 0, 50, 0), ColorUtil.to32BitColor(255, 0, 150, 0));
		this.registerGalacticraftCreature(EntitySlimeling.class, "slimeling", ColorUtil.to32BitColor(255, 0, 50, 0), ColorUtil.to32BitColor(255, 0, 150, 0));
		this.registerGalacticraftCreature(EntityCreeperBoss.class, "creeper_boss", ColorUtil.to32BitColor(255, 0, 50, 0), ColorUtil.to32BitColor(255, 0, 150, 0));
	}

	public void registerOtherEntities()
	{
		MarsModule.registerGalacticraftNonMobEntity(EntityTier2Rocket.class, "rocket_t2", 150, 1, false);
		MarsModule.registerGalacticraftNonMobEntity(EntityProjectileTNT.class, "projectile_tnt", 150, 1, true);
		MarsModule.registerGalacticraftNonMobEntity(EntityLandingBalloons.class, "landing_balloons", 150, 5, true);
		MarsModule.registerGalacticraftNonMobEntity(EntityCargoRocket.class, "rocket_cargo", 150, 1, false);
	}

	public void registerGalacticraftCreature(Class<? extends Entity> clazz, String name, int back, int fore)
	{
		MarsModule.registerGalacticraftNonMobEntity(clazz, name, 80, 3, true);
		int nextEggID = GCCoreUtil.getNextValidID();
		if (nextEggID < 65536)
		{
			ResourceLocation resourcelocation = new ResourceLocation(Constants.MOD_ID_PLANETS, name);

			EntityList.ENTITY_EGGS.put(resourcelocation, new EntityList.EntityEggInfo(resourcelocation, back, fore));
		}
	}

	public static void registerGalacticraftNonMobEntity(Class<? extends Entity> var0, String var1, int trackingDistance, int updateFreq, boolean sendVel)
	{
		ResourceLocation registryName = new ResourceLocation(Constants.MOD_ID_PLANETS, var1);
		EntityRegistry.registerModEntity(registryName, var0, var1, GCCoreUtil.nextInternalID(), GalacticraftPlanets.instance, trackingDistance, updateFreq, sendVel);
	}

	@Override
	public void getGuiIDs(List<Integer> idList)
	{
		idList.add(GuiIdsPlanets.MACHINE_MARS);
	}

	@Override
	public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (side == Side.SERVER)
		{
			BlockPos pos = new BlockPos(x, y, z);
			TileEntity tile = world.getTileEntity(pos);

			if (ID == GuiIdsPlanets.MACHINE_MARS)
			{
				if (tile instanceof TileEntityTerraformer)
				{
					return new ContainerTerraformer(player.inventory, (TileEntityTerraformer) tile, player);
				} else if (tile instanceof TileEntityLaunchController)
				{
					return new ContainerLaunchController(player.inventory, (TileEntityLaunchController) tile, player);
				} else if (tile instanceof TileEntityElectrolyzer)
				{
					return new ContainerElectrolyzer(player.inventory, (TileEntityElectrolyzer) tile, player);
				} else if (tile instanceof TileEntityGasLiquefier)
				{
					return new ContainerGasLiquefier(player.inventory, (TileEntityGasLiquefier) tile, player);
				} else if (tile instanceof TileEntityMethaneSynthesizer)
				{
					return new ContainerMethaneSynthesizer(player.inventory, (TileEntityMethaneSynthesizer) tile, player);
				}
			}
		}

		return null;
	}

	@Override
	public Configuration getConfiguration()
	{
		return ConfigManagerMars.config;
	}

	@Override
	public void syncConfig()
	{
		ConfigManagerMars.syncConfig(false, false);
	}
}
