package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.planets.client.renderer.ItemStackTileEntityRendererPlanets;
import micdoodle8.mods.galacticraft.planets.venus.tile.*;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks.register;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_PLANETS)
public class VenusBlocks
{
    public static final Block VENUS_SOFT_ROCK = new BlockVenusRock(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.2F));
    public static final Block VENUS_VOLCANIC_ROCK = new BlockVenusRock(Block.Properties.from(VENUS_SOFT_ROCK));
    public static final Block PUMICE = new BlockVenusRock(Block.Properties.from(VENUS_SOFT_ROCK));
    public static final Block LEAD_BLOCK = new BlockVenusRock(Block.Properties.from(VENUS_SOFT_ROCK));

    public static final Block VENUS_HARD_ROCK = new BlockVenusRock(Block.Properties.from(VENUS_SOFT_ROCK).hardnessAndResistance(3.0F));

    public static final Block VENUS_ALUMINUM_ORE = new BlockOreVenus(Block.Properties.from(VENUS_SOFT_ROCK).hardnessAndResistance(5.0F));

    public static final Block VENUS_COPPER_ORE = new BlockOreVenus(Block.Properties.from(VENUS_SOFT_ROCK));
    public static final Block GALENA_ORE = new BlockOreVenus(Block.Properties.from(VENUS_SOFT_ROCK));
    public static final Block VENUS_QUARTZ_ORE = new BlockOreVenus(Block.Properties.from(VENUS_SOFT_ROCK));
    public static final Block VENUS_SILICON_ORE = new BlockOreVenus(Block.Properties.from(VENUS_SOFT_ROCK));
    public static final Block VENUS_TIN_ORE = new BlockOreVenus(Block.Properties.from(VENUS_SOFT_ROCK));
    public static final Block SOLAR_ORE = new BlockOreVenus(Block.Properties.from(VENUS_SOFT_ROCK));

    public static final Block RED_VENUS_DUNGEON_BRICKS = new BlockDungeonBrick(Block.Properties.from(VENUS_SOFT_ROCK).hardnessAndResistance(40.0F));
    public static final Block ORANGE_VENUS_DUNGEON_BRICKS = new BlockDungeonBrick(Block.Properties.from(RED_VENUS_DUNGEON_BRICKS));

    public static final Block VAPOR_SPOUT = new BlockSpout(Block.Properties.create(Material.ROCK).hardnessAndResistance(4.5F));

    public static final Block VENUS_BOSS_SPAWNER = new BlockBossSpawnerVenus(Block.Properties.create(Material.ROCK).hardnessAndResistance(1000000.0F).noDrops());

    public static final Block TIER_3_TREASURE_CHEST = new BlockTier3TreasureChest(Block.Properties.create(Material.ROCK).hardnessAndResistance(100000.0F).sound(SoundType.STONE).lightValue(13).noDrops());

    public static final Block WEB_STRING = new BlockTorchWeb(Block.Properties.create(Material.WEB).doesNotBlockMovement().notSolid());
    public static final Block WEB_TORCH = new BlockTorchWeb(Block.Properties.from(WEB_STRING).lightValue(15).notSolid());

    public static final Block GEOTHERMAL_GENERATOR = new BlockGeothermalGenerator(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.0F).sound(SoundType.METAL));
    public static final Block SOLAR_ARRAY_MODULE = new BlockSolarArrayModule(Block.Properties.from(GEOTHERMAL_GENERATOR));
    public static final Block SOLAR_ARRAY_CONTROLLER = new BlockSolarArrayController(Block.Properties.from(GEOTHERMAL_GENERATOR));
    public static final Block LASER_TURRET = new BlockLaserTurret(Block.Properties.from(GEOTHERMAL_GENERATOR));

    public static final Block CRASHED_PROBE = new BlockCrashedProbe(Block.Properties.create(Material.IRON).tickRandomly().hardnessAndResistance(4.5F).sound(SoundType.METAL).notSolid());

    public static final Block SCORCHED_VENUS_ROCK = new BlockScorchedRock(Block.Properties.create(Material.ROCK).tickRandomly().hardnessAndResistance(0.9F, 2.5F));

    //    @ObjectHolder(VenusBlockNames.venusBlock) public static Block venusBlock;
//    @ObjectHolder(VenusBlockNames.spout)
//    public static Block spout;
//    @ObjectHolder(VenusBlockNames.bossSpawner)
//    public static Block bossSpawner;
//    @ObjectHolder(VenusBlockNames.treasureChestTier3)
//    public static Block treasureChestTier3;
//    @ObjectHolder(VenusBlockNames.torchWeb)
//    public static Block torchWeb;
//    //    public static Block sulphuricAcid; TODO
//    @ObjectHolder(VenusBlockNames.geothermalGenerator)
//    public static Block geothermalGenerator;
//    @ObjectHolder(VenusBlockNames.crashedProbe)
//    public static Block crashedProbe;
//    @ObjectHolder(VenusBlockNames.scorchedRock)
//    public static Block scorchedRock;
//    @ObjectHolder(VenusBlockNames.solarArrayModule)
//    public static Block solarArrayModule;
//    @ObjectHolder(VenusBlockNames.solarArrayController)
//    public static Block solarArrayController;
//    @ObjectHolder(VenusBlockNames.laserTurret)
//    public static Block laserTurret;
//    @ObjectHolder(VenusBlockNames.rockSoft)
//    public static Block rockSoft;
//    @ObjectHolder(VenusBlockNames.rockHard)
//    public static Block rockHard;
//    @ObjectHolder(VenusBlockNames.rockMagma)
//    public static Block rockMagma;
//    @ObjectHolder(VenusBlockNames.rockVolcanicDeposit)
//    public static Block rockVolcanicDeposit;
//    @ObjectHolder(VenusBlockNames.dungeonBrick1)
//    public static Block dungeonBrick1;
//    @ObjectHolder(VenusBlockNames.dungeonBrick2)
//    public static Block dungeonBrick2;
//    @ObjectHolder(VenusBlockNames.oreAluminum)
//    public static Block oreAluminum;
//    @ObjectHolder(VenusBlockNames.oreCopper)
//    public static Block oreCopper;
//    @ObjectHolder(VenusBlockNames.oreGalena)
//    public static Block oreGalena;
//    @ObjectHolder(VenusBlockNames.oreQuartz)
//    public static Block oreQuartz;
//    @ObjectHolder(VenusBlockNames.oreSilicon)
//    public static Block oreSilicon;
//    @ObjectHolder(VenusBlockNames.oreTin)
//    public static Block oreTin;
//    @ObjectHolder(VenusBlockNames.leadBlock)
//    public static Block leadBlock;
//    @ObjectHolder(VenusBlockNames.oreSolarDust)
//    public static Block oreSolarDust;

    public static void registerBlocks(RegistryEvent.Register<Block> evt)
    {
        IForgeRegistry<Block> r = evt.getRegistry();

        register(r, VENUS_SOFT_ROCK, VenusBlockNames.VENUS_SOFT_ROCK);
        register(r, VENUS_VOLCANIC_ROCK, VenusBlockNames.VENUS_VOLCANIC_ROCK);
        register(r, PUMICE, VenusBlockNames.PUMICE);
        register(r, LEAD_BLOCK, VenusBlockNames.LEAD_BLOCK);
        register(r, VENUS_HARD_ROCK, VenusBlockNames.VENUS_HARD_ROCK);
        register(r, VENUS_ALUMINUM_ORE, VenusBlockNames.VENUS_ALUMINUM_ORE);
        register(r, VENUS_COPPER_ORE, VenusBlockNames.VENUS_COPPER_ORE);
        register(r, GALENA_ORE, VenusBlockNames.GALENA_ORE);
        register(r, VENUS_QUARTZ_ORE, VenusBlockNames.VENUS_QUARTZ_ORE);
        register(r, VENUS_SILICON_ORE, VenusBlockNames.VENUS_SILICON_ORE);
        register(r, VENUS_TIN_ORE, VenusBlockNames.VENUS_TIN_ORE);
        register(r, SOLAR_ORE, VenusBlockNames.SOLAR_ORE);
        register(r, RED_VENUS_DUNGEON_BRICKS, VenusBlockNames.RED_VENUS_DUNGEON_BRICKS);
        register(r, ORANGE_VENUS_DUNGEON_BRICKS, VenusBlockNames.ORANGE_VENUS_DUNGEON_BRICKS);
        register(r, VAPOR_SPOUT, VenusBlockNames.VAPOR_SPOUT);
        register(r, VENUS_BOSS_SPAWNER, VenusBlockNames.VENUS_BOSS_SPAWNER);
        register(r, TIER_3_TREASURE_CHEST, VenusBlockNames.TIER_3_TREASURE_CHEST);
        register(r, WEB_STRING, VenusBlockNames.WEB_STRING);
        register(r, WEB_TORCH, VenusBlockNames.WEB_TORCH);
        register(r, GEOTHERMAL_GENERATOR, VenusBlockNames.GEOTHERMAL_GENERATOR);
        register(r, SOLAR_ARRAY_MODULE, VenusBlockNames.SOLAR_ARRAY_MODULE);
        register(r, SOLAR_ARRAY_CONTROLLER, VenusBlockNames.SOLAR_ARRAY_CONTROLLER);
        register(r, LASER_TURRET, VenusBlockNames.LASER_TURRET);
        register(r, CRASHED_PROBE, VenusBlockNames.CRASHED_PROBE);
        register(r, SCORCHED_VENUS_ROCK, VenusBlockNames.SCORCHED_VENUS_ROCK);

//        VenusBlocks.venusBlock = new BlockBasicVenus("venus");
//        VenusBlocks.spout = new BlockSpout("spout");
//        VenusBlocks.bossSpawner = new BlockBossSpawnerVenus("boss_spawner_venus");
//        VenusBlocks.treasureChestTier3 = new BlockTier3TreasureChest("treasure_t3");
//        VenusBlocks.torchWeb = new BlockTorchWeb("web_torch");
//        VenusBlocks.geothermalGenerator = new BlockGeothermalGenerator("geothermal_generator");
//        VenusBlocks.crashedProbe = new BlockCrashedProbe("crashed_probe");
//        VenusBlocks.scorchedRock = new BlockScorchedRock("venus_rock_scorched");
//        VenusBlocks.solarArrayModule = new BlockSolarArrayModule("solar_array_module");
//        VenusBlocks.solarArrayController = new BlockSolarArrayController("solar_array_controller");
//        VenusBlocks.laserTurret = new BlockLaserTurret("laser_turret");

        GCBlocks.hiddenBlocks.add(VenusBlocks.VENUS_BOSS_SPAWNER);

//        VenusBlocks.registerBlocks();
//        VenusBlocks.setHarvestLevels();
    }

//    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name)
//    {
//        reg.register(thing.setRegistryName(name));
//    }
//
//    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
//    {
//        register(reg, thing, new ResourceLocation(Constants.MOD_ID_PLANETS, name));
//    }
//
//    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, ResourceLocation name, IForgeRegistryEntry<V> thing) {
//        reg.register(thing.setRegistryName(name));
//    }

    public static void registerItemBlocks(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        Item.Properties props = GCItems.defaultBuilder().group(GalacticraftCore.galacticraftBlocksTab);
        register(r, Registry.BLOCK.getKey(VENUS_SOFT_ROCK), new BlockItem(VENUS_SOFT_ROCK, props));
        register(r, Registry.BLOCK.getKey(VENUS_VOLCANIC_ROCK), new BlockItem(VENUS_VOLCANIC_ROCK, props));
        register(r, Registry.BLOCK.getKey(PUMICE), new BlockItem(PUMICE, props));
        register(r, Registry.BLOCK.getKey(LEAD_BLOCK), new BlockItem(LEAD_BLOCK, props));
        register(r, Registry.BLOCK.getKey(VENUS_HARD_ROCK), new BlockItem(VENUS_HARD_ROCK, props));
        register(r, Registry.BLOCK.getKey(VENUS_ALUMINUM_ORE), new BlockItem(VENUS_ALUMINUM_ORE, props));
        register(r, Registry.BLOCK.getKey(VENUS_COPPER_ORE), new BlockItem(VENUS_COPPER_ORE, props));
        register(r, Registry.BLOCK.getKey(GALENA_ORE), new BlockItem(GALENA_ORE, props));
        register(r, Registry.BLOCK.getKey(VENUS_QUARTZ_ORE), new BlockItem(VENUS_QUARTZ_ORE, props));
        register(r, Registry.BLOCK.getKey(VENUS_SILICON_ORE), new BlockItem(VENUS_SILICON_ORE, props));
        register(r, Registry.BLOCK.getKey(VENUS_TIN_ORE), new BlockItem(VENUS_TIN_ORE, props));
        register(r, Registry.BLOCK.getKey(SOLAR_ORE), new BlockItem(SOLAR_ORE, props));
        register(r, Registry.BLOCK.getKey(RED_VENUS_DUNGEON_BRICKS), new BlockItem(RED_VENUS_DUNGEON_BRICKS, props));
        register(r, Registry.BLOCK.getKey(ORANGE_VENUS_DUNGEON_BRICKS), new BlockItem(ORANGE_VENUS_DUNGEON_BRICKS, props));
        register(r, Registry.BLOCK.getKey(VAPOR_SPOUT), new BlockItem(VAPOR_SPOUT, props));
        register(r, Registry.BLOCK.getKey(TIER_3_TREASURE_CHEST), new ItemBlockDesc(TIER_3_TREASURE_CHEST, props.setISTER(() -> ItemStackTileEntityRendererPlanets::new)));
        register(r, Registry.BLOCK.getKey(WEB_STRING), new ItemBlockDesc(WEB_STRING, props));
        register(r, Registry.BLOCK.getKey(WEB_TORCH), new ItemBlockDesc(WEB_TORCH, props));
        register(r, Registry.BLOCK.getKey(GEOTHERMAL_GENERATOR), new ItemBlockDesc(GEOTHERMAL_GENERATOR, props));
        register(r, Registry.BLOCK.getKey(SOLAR_ARRAY_MODULE), new ItemBlockDesc(SOLAR_ARRAY_MODULE, props));
        register(r, Registry.BLOCK.getKey(SOLAR_ARRAY_CONTROLLER), new ItemBlockDesc(SOLAR_ARRAY_CONTROLLER, props));
        register(r, Registry.BLOCK.getKey(LASER_TURRET), new ItemBlockDesc(LASER_TURRET, props));
        register(r, Registry.BLOCK.getKey(CRASHED_PROBE), new ItemBlockDesc(CRASHED_PROBE, props));
        register(r, Registry.BLOCK.getKey(SCORCHED_VENUS_ROCK), new BlockItem(SCORCHED_VENUS_ROCK, props));
    }

//    public static void setHarvestLevels()
//    {
//        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 0);
//        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 1);
//        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 2);
//        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 3);
//        setHarvestLevel(VenusBlocks.spout, "pickaxe", 1);
//    }

//    public static void registerBlock(Block block, Class<? extends BlockItem> itemClass)
//    {
//        GCBlocks.registerBlock(block, itemClass);
//    }

//    public static void registerBlocks()
//    {
//        registerBlock(VenusBlocks.venusBlock, ItemBlockBasicVenus.class);
//        registerBlock(VenusBlocks.spout, ItemBlockGC.class);
//        registerBlock(VenusBlocks.bossSpawner, ItemBlockGC.class);
//        registerBlock(VenusBlocks.treasureChestTier3, ItemBlockDesc.class);
//        registerBlock(VenusBlocks.torchWeb, ItemBlockTorchWeb.class);
//        registerBlock(VenusBlocks.geothermalGenerator, ItemBlockDesc.class);
//        registerBlock(VenusBlocks.crashedProbe, ItemBlockDesc.class);
//        registerBlock(VenusBlocks.scorchedRock, ItemBlockGC.class);
//        registerBlock(VenusBlocks.solarArrayModule, ItemBlockDesc.class);
//        registerBlock(VenusBlocks.solarArrayController, ItemBlockDesc.class);
//        registerBlock(VenusBlocks.laserTurret, ItemBlockLaser.class);
//    }
//
//    public static void oreDictRegistration()
//    {
//        OreDictionary.registerOre("oreCopper", BlockBasicVenus.EnumBlockBasicVenus.ORE_COPPER.getItemStack());
//        OreDictionary.registerOre("oreTin", BlockBasicVenus.EnumBlockBasicVenus.ORE_TIN.getItemStack());
//        OreDictionary.registerOre("oreAluminum", BlockBasicVenus.EnumBlockBasicVenus.ORE_ALUMINUM.getItemStack());
//        OreDictionary.registerOre("oreAluminium", BlockBasicVenus.EnumBlockBasicVenus.ORE_ALUMINUM.getItemStack());
//        OreDictionary.registerOre("oreNaturalAluminum", BlockBasicVenus.EnumBlockBasicVenus.ORE_ALUMINUM.getItemStack());
//        OreDictionary.registerOre("oreQuartz", BlockBasicVenus.EnumBlockBasicVenus.ORE_QUARTZ.getItemStack());
//        OreDictionary.registerOre("oreLead", BlockBasicVenus.EnumBlockBasicVenus.ORE_GALENA.getItemStack());
//        OreDictionary.registerOre("oreSilicon", BlockBasicVenus.EnumBlockBasicVenus.ORE_SILICON.getItemStack());
//        OreDictionary.registerOre("oreSolar", BlockBasicVenus.EnumBlockBasicVenus.ORE_SOLAR_DUST.getItemStack());
//
//        OreDictionary.registerOre("blockLead", BlockBasicVenus.EnumBlockBasicVenus.LEAD_BLOCK.getItemStack());
//    }

    @SubscribeEvent
    public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt)
    {
        IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();

        register(r, TileEntityType.Builder.create(TileEntityCrashedProbe::new, CRASHED_PROBE).build(null), VenusBlockNames.CRASHED_PROBE);
        register(r, TileEntityType.Builder.create(TileEntityDungeonSpawner::new, VENUS_BOSS_SPAWNER).build(null), VenusBlockNames.VENUS_BOSS_SPAWNER);
        register(r, TileEntityType.Builder.create(TileEntityGeothermalGenerator::new, GEOTHERMAL_GENERATOR).build(null), VenusBlockNames.GEOTHERMAL_GENERATOR);
        register(r, TileEntityType.Builder.create(TileEntityLaserTurret::new, LASER_TURRET).build(null), VenusBlockNames.LASER_TURRET);
        register(r, TileEntityType.Builder.create(TileEntitySolarArrayController::new, SOLAR_ARRAY_CONTROLLER).build(null), VenusBlockNames.SOLAR_ARRAY_CONTROLLER);
        register(r, TileEntityType.Builder.create(TileEntitySolarArrayModule::new, SOLAR_ARRAY_MODULE).build(null), VenusBlockNames.SOLAR_ARRAY_MODULE);
        register(r, TileEntityType.Builder.create(TileEntitySpout::new, VAPOR_SPOUT).build(null), VenusBlockNames.VAPOR_SPOUT);
        register(r, TileEntityType.Builder.create(TileEntityTreasureChestVenus::new, TIER_3_TREASURE_CHEST).build(null), VenusBlockNames.TIER_3_TREASURE_CHEST);
    }
}
