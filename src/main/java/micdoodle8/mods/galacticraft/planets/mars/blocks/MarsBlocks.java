package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.*;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.planets.client.renderer.ItemStackTileEntityRendererGCMars;
import micdoodle8.mods.galacticraft.planets.mars.tile.*;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_PLANETS)
public class MarsBlocks
{
    //    @ObjectHolder(MarsBlockNames.blockSludge) public static Block blockSludge; TODO liquids
    @ObjectHolder(MarsBlockNames.CAVERNOUS_VINES)
    public static Block CAVERNOUS_VINES;
    @ObjectHolder(MarsBlockNames.RED_SLIMELING_EGG)
    public static Block RED_SLIMELING_EGG;
    @ObjectHolder(MarsBlockNames.BLUE_SLIMELING_EGG)
    public static Block BLUE_SLIMELING_EGG;
    @ObjectHolder(MarsBlockNames.YELLOW_SLIMELING_EGG)
    public static Block YELLOW_SLIMELING_EGG;
    @ObjectHolder(MarsBlockNames.TIER_2_TREASURE_CHEST)
    public static Block TIER_2_TREASURE_CHEST;
    @ObjectHolder(MarsBlockNames.CRYOGENIC_CHAMBER)
    public static Block CRYOGENIC_CHAMBER;
    @ObjectHolder(MarsBlockNames.LAUNCH_CONTROLLER)
    public static Block LAUNCH_CONTROLLER;
    @ObjectHolder(MarsBlockNames.TERRAFORMER)
    public static Block TERRAFORMER;
    @ObjectHolder(MarsBlockNames.METHANE_SYNTHESIZER)
    public static Block METHANE_SYNTHESIZER;
    @ObjectHolder(MarsBlockNames.GAS_LIQUEFIER)
    public static Block GAS_LIQUEFIER;
    @ObjectHolder(MarsBlockNames.WATER_ELECTROLYZER)
    public static Block WATER_ELECTROLYZER;
    @ObjectHolder(MarsBlockNames.CREEPER_EGG)
    public static Block CREEPER_EGG;
    @ObjectHolder(MarsBlockNames.MARS_COBBLESTONE_STAIRS)
    public static Block MARS_COBBLESTONE_STAIRS;
    @ObjectHolder(MarsBlockNames.MARS_DUNGEON_BRICKS_STAIRS)
    public static Block MARS_DUNGEON_BRICKS_STAIRS;
    @ObjectHolder(MarsBlockNames.MARS_BOSS_SPAWNER)
    public static Block MARS_BOSS_SPAWNER;
    @ObjectHolder(MarsBlockNames.MARS_COPPER_ORE)
    public static Block MARS_COPPER_ORE;
    @ObjectHolder(MarsBlockNames.MARS_TIN_ORE)
    public static Block MARS_TIN_ORE;
    @ObjectHolder(MarsBlockNames.DESH_ORE)
    public static Block DESH_ORE;
    @ObjectHolder(MarsBlockNames.MARS_IRON_ORE)
    public static Block MARS_IRON_ORE;
    @ObjectHolder(MarsBlockNames.MARS_COBBLESTONE)
    public static Block MARS_COBBLESTONE;
    @ObjectHolder(MarsBlockNames.MARS_FINE_REGOLITH)
    public static Block MARS_FINE_REGOLITH;
    @ObjectHolder(MarsBlockNames.MARS_REGOLITH)
    public static Block MARS_REGOLITH;
    @ObjectHolder(MarsBlockNames.MARS_DUNGEON_BRICKS)
    public static Block MARS_DUNGEON_BRICKS;
    @ObjectHolder(MarsBlockNames.DESH_BLOCK)
    public static Block DESH_BLOCK;
    @ObjectHolder(MarsBlockNames.MARS_STONE)
    public static Block MARS_STONE;

    public static void registerBlocks(RegistryEvent.Register<Block> evt)
    {
        IForgeRegistry<Block> r = evt.getRegistry();

        Block.Properties builder = Block.Properties.create(Material.ROCK);
        register(r, new BlockBasicMars(builder), MarsBlockNames.MARS_COPPER_ORE);
        register(r, new BlockBasicMars(builder), MarsBlockNames.MARS_TIN_ORE);
        register(r, new BlockBasicMars(builder), MarsBlockNames.DESH_ORE);
        register(r, new BlockBasicMars(builder), MarsBlockNames.MARS_IRON_ORE);
        register(r, new BlockBasicMars(builder), MarsBlockNames.MARS_COBBLESTONE);
        register(r, new BlockBasicMars(builder), MarsBlockNames.MARS_FINE_REGOLITH);
        register(r, new BlockBasicMars(builder), MarsBlockNames.MARS_REGOLITH);
        register(r, new BlockBasicMars(builder), MarsBlockNames.MARS_STONE);

        builder = builder.hardnessAndResistance(5.0F, 20.0F);
        register(r, new BlockBasicMars(builder), MarsBlockNames.DESH_BLOCK);

        builder = builder.hardnessAndResistance(5.0F, 60.0F).tickRandomly();
        register(r, new BlockBasicMars(builder), MarsBlockNames.MARS_DUNGEON_BRICKS);

        builder = Block.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().lightValue(15).tickRandomly().hardnessAndResistance(0.2F).sound(SoundType.PLANT);
        register(r, new BlockCavernousVine(builder), MarsBlockNames.CAVERNOUS_VINES);

        builder = Block.Properties.create(Material.ROCK);
        register(r, new BlockSlimelingEgg(builder), MarsBlockNames.RED_SLIMELING_EGG);
        register(r, new BlockSlimelingEgg(builder), MarsBlockNames.BLUE_SLIMELING_EGG);
        register(r, new BlockSlimelingEgg(builder), MarsBlockNames.YELLOW_SLIMELING_EGG);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(100000.0F).sound(SoundType.STONE).lightValue(13).noDrops();
        register(r, new BlockTier2TreasureChest(builder), MarsBlockNames.TIER_2_TREASURE_CHEST);

        builder = Block.Properties.create(Material.IRON).sound(SoundType.METAL);
        register(r, new BlockCryoChamber(builder), MarsBlockNames.CRYOGENIC_CHAMBER);
        register(r, new BlockLaunchController(builder), MarsBlockNames.LAUNCH_CONTROLLER);
        register(r, new BlockTerraformer(builder), MarsBlockNames.TERRAFORMER);
        register(r, new BlockMethaneSynthesizer(builder), MarsBlockNames.METHANE_SYNTHESIZER);
        register(r, new BlockGasLiquefier(builder), MarsBlockNames.GAS_LIQUEFIER);
        register(r, new BlockElectrolyzer(builder.tickRandomly()), MarsBlockNames.WATER_ELECTROLYZER);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 9.0F).noDrops();
        register(r, new BlockCreeperEgg(builder), MarsBlockNames.CREEPER_EGG);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(1000000.0F).noDrops();
        register(r, new BlockBossSpawnerMars(builder), MarsBlockNames.MARS_BOSS_SPAWNER);

//        register(r, new BlockStairsGC(builder), MarsBlockNames.marsCobblestoneStairs);
//        register(r, new BlockStairsGC(builder), MarsBlockNames.marsBricksStairs); TODO

//        MarsBlocks.marsBlock = new BlockBasicMars("mars").setHardness(2.2F);
//        MarsBlocks.vine = new BlockCavernousVine("cavern_vines").setHardness(0.1F);
//        MarsBlocks.rock = new BlockSlimelingEgg("slimeling_egg").setHardness(0.75F);
//        MarsBlocks.treasureChestTier2 = new BlockTier2TreasureChest("treasure_t2");
//        MarsBlocks.machine = new BlockMachineMars("mars_machine").setHardness(1.8F);
//        MarsBlocks.machineT2 = new BlockMachineMarsT2("mars_machine_t2").setHardness(1.8F);
//        MarsBlocks.creeperEgg = new BlockCreeperEgg("creeper_egg").setHardness(-1.0F);
//        MarsBlocks.bossSpawner = new BlockBossSpawnerMars("boss_spawner_mars");
//        MarsBlocks.marsCobblestoneStairs = new BlockStairsGC("mars_stairs_cobblestone", marsBlock.getDefaultState().with(BlockBasicMars.BASIC_TYPE, BlockBasicMars.EnumBlockBasic.COBBLESTONE)).setHardness(1.5F);
//        MarsBlocks.marsBricksStairs = new BlockStairsGC("mars_stairs_brick", marsBlock.getDefaultState().with(BlockBasicMars.BASIC_TYPE, BlockBasicMars.EnumBlockBasic.DUNGEON_BRICK)).setHardness(4.0F);

        GCBlocks.hiddenBlocks.add(MarsBlocks.MARS_BOSS_SPAWNER);

//        MarsBlocks.registerBlocks();
//        MarsBlocks.setHarvestLevels();
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
    {
        GCBlocks.register(reg, thing, new ResourceLocation(Constants.MOD_ID_PLANETS, name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, ResourceLocation name, IForgeRegistryEntry<V> thing)
    {
        GCBlocks.register(reg, name, thing);
    }

    public static void registerItemBlocks(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        Item.Properties props = GCItems.defaultBuilder().group(GalacticraftCore.galacticraftBlocksTab);
        register(r, Registry.BLOCK.getKey(MARS_COPPER_ORE), new BlockItem(MARS_COPPER_ORE, props));
        register(r, Registry.BLOCK.getKey(MARS_TIN_ORE), new BlockItem(MARS_TIN_ORE, props));
        register(r, Registry.BLOCK.getKey(DESH_ORE), new BlockItem(DESH_ORE, props));
        register(r, Registry.BLOCK.getKey(MARS_IRON_ORE), new BlockItem(MARS_IRON_ORE, props));
        register(r, Registry.BLOCK.getKey(MARS_COBBLESTONE), new BlockItem(MARS_COBBLESTONE, props));
        register(r, Registry.BLOCK.getKey(MARS_FINE_REGOLITH), new BlockItem(MARS_FINE_REGOLITH, props));
        register(r, Registry.BLOCK.getKey(MARS_REGOLITH), new BlockItem(MARS_REGOLITH, props));
        register(r, Registry.BLOCK.getKey(MARS_STONE), new BlockItem(MARS_STONE, props));
        register(r, Registry.BLOCK.getKey(DESH_BLOCK), new BlockItem(DESH_BLOCK, props));
        register(r, Registry.BLOCK.getKey(MARS_DUNGEON_BRICKS), new BlockItem(MARS_DUNGEON_BRICKS, props));
        register(r, Registry.BLOCK.getKey(CAVERNOUS_VINES), new ItemBlockDesc(CAVERNOUS_VINES, props));
        register(r, Registry.BLOCK.getKey(RED_SLIMELING_EGG), new ItemBlockDesc(RED_SLIMELING_EGG, props));
        register(r, Registry.BLOCK.getKey(BLUE_SLIMELING_EGG), new ItemBlockDesc(BLUE_SLIMELING_EGG, props));
        register(r, Registry.BLOCK.getKey(YELLOW_SLIMELING_EGG), new ItemBlockDesc(YELLOW_SLIMELING_EGG, props));
        register(r, Registry.BLOCK.getKey(TIER_2_TREASURE_CHEST), new ItemBlockDesc(TIER_2_TREASURE_CHEST, props.setISTER(() -> ItemStackTileEntityRendererGCMars::new)));
        register(r, Registry.BLOCK.getKey(CRYOGENIC_CHAMBER), new ItemBlockDesc(CRYOGENIC_CHAMBER, props));
        register(r, Registry.BLOCK.getKey(LAUNCH_CONTROLLER), new ItemBlockDesc(LAUNCH_CONTROLLER, props));
        register(r, Registry.BLOCK.getKey(TERRAFORMER), new ItemBlockDesc(TERRAFORMER, props));
        register(r, Registry.BLOCK.getKey(METHANE_SYNTHESIZER), new ItemBlockDesc(METHANE_SYNTHESIZER, props));
        register(r, Registry.BLOCK.getKey(GAS_LIQUEFIER), new ItemBlockDesc(GAS_LIQUEFIER, props));
        register(r, Registry.BLOCK.getKey(WATER_ELECTROLYZER), new ItemBlockDesc(WATER_ELECTROLYZER, props));
        register(r, Registry.BLOCK.getKey(CREEPER_EGG), new ItemBlockDesc(CREEPER_EGG, props));
        register(r, Registry.BLOCK.getKey(MARS_BOSS_SPAWNER), new BlockItem(MARS_BOSS_SPAWNER, props.group(null)));
    }

//    public static void setHarvestLevels()
//    {
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 0); //Copper ore
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 1); //Tin ore
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 3, 2); //Desh ore
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 3); //Iron ore
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 0, 4); //Cobblestone
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 3, 7); //Dungeon brick
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 0, 8); //Decoration block
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 9); //Stone
//        setHarvestLevel(MarsBlocks.marsBlock, "shovel", 0, 5); //Top dirt
//        setHarvestLevel(MarsBlocks.marsBlock, "shovel", 0, 6); //Dirt
//        setHarvestLevel(MarsBlocks.rock, "pickaxe", 3);
////        setHarvestLevel(MarsBlocks.marsCobblestoneStairs, "pickaxe", 0);
////        setHarvestLevel(MarsBlocks.marsBricksStairs, "pickaxe", 3);
//    }

//    public static void registerBlocks()
//    {
//        registerBlock(MarsBlocks.treasureChestTier2, ItemBlockDesc.class);
//        registerBlock(MarsBlocks.marsBlock, ItemBlockMars.class);
//        registerBlock(MarsBlocks.vine, ItemBlockDesc.class);
//        registerBlock(MarsBlocks.rock, ItemBlockEgg.class);
//        registerBlock(MarsBlocks.creeperEgg, ItemBlockDesc.class);
//        registerBlock(MarsBlocks.machine, ItemBlockMachine.class);
//        registerBlock(MarsBlocks.machineT2, ItemBlockMachine.class);
//        registerBlock(MarsBlocks.bossSpawner, ItemBlockGC.class);
//        registerBlock(MarsBlocks.marsCobblestoneStairs, ItemBlockGC.class);
//        registerBlock(MarsBlocks.marsBricksStairs, ItemBlockGC.class);
//    }

//    public static void oreDictRegistration()
//    {
//        OreDictionary.registerOre("oreCopper", new ItemStack(MarsBlocks.marsBlock, 1, 0));
//        OreDictionary.registerOre("oreTin", new ItemStack(MarsBlocks.marsBlock, 1, 1));
//        OreDictionary.registerOre("oreIron", new ItemStack(MarsBlocks.marsBlock, 1, 3));
//        OreDictionary.registerOre("oreDesh", new ItemStack(MarsBlocks.marsBlock, 1, 2));
//        OreDictionary.registerOre("blockDesh", new ItemStack(MarsBlocks.marsBlock, 1, 8));
//    } TODO

    @SubscribeEvent
    public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt)
    {
        IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();

        register(r, TileEntityType.Builder.create(TileEntityCryogenicChamber::new, CRYOGENIC_CHAMBER).build(null), MarsBlockNames.CRYOGENIC_CHAMBER);
        register(r, TileEntityType.Builder.create(TileEntityDungeonSpawnerMars::new, MARS_BOSS_SPAWNER).build(null), MarsBlockNames.MARS_BOSS_SPAWNER);
        register(r, TileEntityType.Builder.create(TileEntityElectrolyzer::new, WATER_ELECTROLYZER).build(null), MarsBlockNames.WATER_ELECTROLYZER);
        register(r, TileEntityType.Builder.create(TileEntityGasLiquefier::new, GAS_LIQUEFIER).build(null), MarsBlockNames.GAS_LIQUEFIER);
        register(r, TileEntityType.Builder.create(TileEntityLaunchController::new, LAUNCH_CONTROLLER).build(null), MarsBlockNames.LAUNCH_CONTROLLER);
        register(r, TileEntityType.Builder.create(TileEntityMethaneSynthesizer::new, METHANE_SYNTHESIZER).build(null), MarsBlockNames.METHANE_SYNTHESIZER);
        register(r, TileEntityType.Builder.create(TileEntitySlimelingEgg::new, RED_SLIMELING_EGG, BLUE_SLIMELING_EGG, YELLOW_SLIMELING_EGG).build(null), MarsBlockNames.SLIMELING_EGG);
        register(r, TileEntityType.Builder.create(TileEntityTerraformer::new, TERRAFORMER).build(null), MarsBlockNames.TERRAFORMER);
        register(r, TileEntityType.Builder.create(TileEntityTreasureChestMars::new, TIER_2_TREASURE_CHEST).build(null), MarsBlockNames.TIER_2_TREASURE_CHEST);
    }
}
