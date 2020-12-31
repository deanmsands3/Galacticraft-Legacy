package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.core.blocks.*;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemStackTileEntityRendererGC;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.items.ItemBlockWallOrFloorDesc;
import micdoodle8.mods.galacticraft.core.tile.*;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockDungeonBrick;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.*;

import com.google.common.collect.Lists;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_CORE)
public class GCBlocks
{
    public static final Block BREATHEABLE_AIR = new BlockBreathableAir(Block.Properties.create(Material.AIR).doesNotBlockMovement().noDrops().hardnessAndResistance(0.0F, 10000.0F).noDrops());
    public static final Block BRIGHT_AIR = new BlockBrightAir(Block.Properties.from(BREATHEABLE_AIR).lightValue(15).noDrops());
    public static final Block BRIGHT_BREATHEABLE_AIR = new BlockBrightBreathableAir(Block.Properties.from(BRIGHT_AIR).noDrops());

    public static final Block ARC_LAMP = new BlockArcLamp(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.1F).sound(SoundType.METAL).lightValue(13));

    public static final Block TIER_1_TREASURE_CHEST = new BlockTier1TreasureChest(Block.Properties.create(Material.ROCK).hardnessAndResistance(100000.0F).sound(SoundType.STONE).lightValue(13).noDrops());

    public static final Block ROCKET_LAUNCH_PAD = new BlockPad(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F).sound(SoundType.METAL));
    public static final Block BUGGY_FUELING_PAD = new BlockPad(Block.Properties.from(ROCKET_LAUNCH_PAD));
    public static final Block FULL_ROCKET_LAUNCH_PAD = new BlockPadFull(Block.Properties.from(ROCKET_LAUNCH_PAD));
    public static final Block FULL_BUGGY_FUELING_PAD = new BlockPadFull(Block.Properties.from(ROCKET_LAUNCH_PAD));

    public static final Block UNLIT_TORCH = new BlockUnlitTorch(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.0F).lightValue(3).sound(SoundType.WOOD));
    public static final Block WALL_UNLIT_TORCH = new BlockUnlitTorchWall(Block.Properties.from(UNLIT_TORCH).lootFrom(UNLIT_TORCH));
    public static final Block LIT_UNLIT_TORCH = new BlockUnlitTorch(Block.Properties.from(UNLIT_TORCH).lightValue(14).lootFrom(UNLIT_TORCH));
    public static final Block WALL_LIT_UNLIT_TORCH = new BlockUnlitTorchWall(Block.Properties.from(UNLIT_TORCH).lightValue(14).lootFrom(UNLIT_TORCH));
    public static final Block GLOWSTONE_TORCH = new BlockGlowstoneTorch(Block.Properties.from(UNLIT_TORCH).lightValue(12));
    public static final Block WALL_GLOWSTONE_TORCH = new BlockGlowstoneTorchWall(Block.Properties.from(UNLIT_TORCH).lightValue(12).lootFrom(GLOWSTONE_TORCH));

    public static final Block OXYGEN_BUBBLE_DISTRIBUTOR = new BlockOxygenDistributor(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.0F).sound(SoundType.METAL));
    public static final Block OXYGEN_COLLECTOR = new BlockOxygenCollector(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block ROCKET_WORKBENCH = new BlockNasaWorkbench(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block AIR_LOCK_FRAME = new BlockAirLockFrame(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block AIR_LOCK_CONTROLLER = new BlockAirLockController(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block COMPRESSOR = new BlockIngotCompressor(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block ELECTRIC_COMPRESSOR = new BlockIngotCompressorElectric(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block ADVANCED_COMPRESSOR = new BlockIngotCompressorElectricAdvanced(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block COAL_GENERATOR = new BlockCoalGenerator(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block CIRCUIT_FABRICATOR = new BlockCircuitFabricator(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block OXYGEN_STORAGE_MODULE = new BlockOxygenStorageModule(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block DECONSTRUCTOR = new BlockDeconstructor(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block CHROMATIC_APPLICATOR = new BlockPainter(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block MAGNETIC_CRAFTING_TABLE = new BlockCrafting(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block REFINERY = new BlockRefinery(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block FUEL_LOADER = new BlockFuelLoader(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block OXYGEN_COMPRESSOR = new BlockOxygenCompressor(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block OXYGEN_DECOMPRESSOR = new BlockOxygenCompressor(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block OXYGEN_SEALER = new BlockOxygenSealer(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block OXYGEN_DETECTOR = new BlockOxygenDetector(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block CARGO_LOADER = new BlockCargoLoader(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block CARGO_UNLOADER = new BlockCargoUnloader(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block BASIC_SOLAR_PANEL = new BlockSolar(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block ADVANCED_SOLAR_PANEL = new BlockSolarAdvanced(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
//    public static final Block radioTelescope = new BlockDish(Block.Properties.from(oxygenDistributor));
    public static final Block ENERGY_STORAGE = new BlockEnergyStorageModule(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block ENERGY_STORAGE_CLUSTER = new BlockEnergyStorageCluster(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block ELECTRIC_FURNACE = new BlockFurnaceElectric(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block ELECTRIC_ARC_FURNACE = new BlockFurnaceArc(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
//    public static final Block panelLighting = new BlockPanelLighting(Block.Properties.from(oxygenDistributor));
//    public static final Block spinThruster = new BlockSpinThruster(Block.Properties.from(oxygenDistributor));
    public static final Block TELEMETRY_UNIT = new BlockTelemetry(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block HIDDEN_REDSTONE_WIRE = new BlockConcealedRedstone(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block HIDDEN_REDSTONE_REPEATER = new BlockConcealedRepeater(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));
    public static final Block PLAYER_DETECTOR = new BlockConcealedDetector(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR));

    public static final Block AIR_LOCK_SEAL = new BlockAirLockWall(Block.Properties.from(OXYGEN_BUBBLE_DISTRIBUTOR).hardnessAndResistance(1000.0F).tickRandomly().noDrops());

    public static final Block FLUID_PIPE = new BlockFluidPipe(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS), BlockFluidPipe.EnumPipeMode.NORMAL);
    public static final Block PULLED_FLUID_PIPE = new BlockFluidPipe(Block.Properties.from(FLUID_PIPE).lootFrom(FLUID_PIPE), BlockFluidPipe.EnumPipeMode.PULL);

    public static final Block FALLEN_METEOR = new BlockFallenMeteor(Block.Properties.create(Material.ROCK).hardnessAndResistance(40.0F).sound(SoundType.STONE));
//    public static final Block spaceGlassVanilla = new BlockSpaceGlass(builder, GlassType.VANILLA, GlassFrame.PLAIN, null);
//    public static final Block spaceGlassClear = new BlockSpaceGlass(builder, GlassType.CLEAR, GlassFrame.PLAIN, null);
//    public static final Block spaceGlassTinVanilla = new BlockSpaceGlass(builder, GlassType.VANILLA, GlassFrame.TIN_DECO, GCBlocks.spaceGlassVanilla);
//    public static final Block spaceGlassTinClear = new BlockSpaceGlass(builder, GlassType.CLEAR, GlassFrame.TIN_DECO, GCBlocks.spaceGlassClear);
//    public static final Block spaceGlassStrong = new BlockSpaceGlass(builder, GlassType.STRONG, GlassFrame.PLAIN, null);
//    public static final Block spaceGlassTinStrong = new BlockSpaceGlass(builder, GlassType.STRONG, GlassFrame.TIN_DECO, GCBlocks.spaceGlassStrong);

    public static final Block SPACE_STATION_BASE = new BlockSpaceStationBase(Block.Properties.create(Material.ROCK).hardnessAndResistance(-1.0F, 3600000.0F).noDrops());

    public static final Block MULTI_BLOCK = new BlockMulti(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 3600000.0F).sound(SoundType.METAL).notSolid().noDrops());

//    public static final Block sealableBlock = new BlockEnclosed(builder);

    public static final Block PARACHEST = new BlockParaChest(Block.Properties.create(Material.WOOD).hardnessAndResistance(3.0F).sound(SoundType.WOOD));

    public static final Block ALUMINUM_WIRE = new BlockAluminumWire(Block.Properties.create(Material.WOOL).hardnessAndResistance(0.2F).sound(SoundType.CLOTH));
    public static final Block HEAVY_ALUMINUM_WIRE = new BlockAluminumWire(Block.Properties.from(ALUMINUM_WIRE));
    public static final Block SWITCHABLE_ALUMINUM_WIRE = new BlockAluminumWire(Block.Properties.from(ALUMINUM_WIRE));
    public static final Block SWITCHABLE_HEAVY_ALUMINUM_WIRE = new BlockAluminumWire(Block.Properties.from(ALUMINUM_WIRE));

    public static final Block CHEESE_BLOCK = new BlockCheese(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH));

    public static final Block DISPLAY_SCREEN = new BlockScreen(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1.0F).sound(SoundType.STONE));

    public static final Block FLUID_TANK = new BlockFluidTank(Block.Properties.create(Material.GLASS).hardnessAndResistance(3.0F, 8.0F).sound(SoundType.GLASS));

    public static final Block MOON_BOSS_SPAWNER = new BlockBossSpawner(Block.Properties.create(Material.ROCK).hardnessAndResistance(1000000.0F).noDrops());

//    public static final Block slabGCHalf = new BlockSlabGC(builder);
//    public static final Block slabGCDouble = new BlockDoubleSlabGC(builder);

//    public static final Block tinStairs1 = new BlockStairsGC(builder);
//    public static final Block tinStairs2 = new BlockStairsGC(builder);
//    public static final Block moonStoneStairs = new BlockStairsGC(builder);
//    public static final Block moonBricksStairs = new BlockStairsGC(builder);

//    public static final Block wallGC = new BlockWallGC(builder);

    public static final Block HYDRAULIC_PLATFORM = new BlockPlatform(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F).sound(SoundType.METAL));

    public static final Block EMERGENCY_POST = new BlockEmergencyBox(Block.Properties.create(Material.IRON).hardnessAndResistance(4.0F, 70.0F).lightValue(15).sound(SoundType.METAL));
    public static final Block EMERGENCY_POST_KIT = new BlockEmergencyBoxKit(Block.Properties.from(EMERGENCY_POST));

    public static final Block COPPER_ORE = new OreBlock(Block.Properties.create(Material.ROCK).sound(SoundType.STONE));
    public static final Block TIN_ORE = new OreBlock(Block.Properties.from(COPPER_ORE));
    public static final Block ALUMINUM_ORE = new OreBlock(Block.Properties.from(COPPER_ORE));
    public static final Block SILICON_ORE = new OreBlock(Block.Properties.from(COPPER_ORE));
    public static final Block MOON_COPPER_ORE = new OreBlock(Block.Properties.from(COPPER_ORE));
    public static final Block MOON_TIN_ORE = new OreBlock(Block.Properties.from(COPPER_ORE));
    public static final Block CHEESE_ORE = new OreBlock(Block.Properties.from(COPPER_ORE));
    public static final Block SAPPHIRE_ORE = new OreBlock(Block.Properties.from(COPPER_ORE));

    public static final Block MOON_DIRT = new BlockSimple(Block.Properties.create(Material.ROCK).sound(SoundType.STONE));
    public static final Block MOON_ROCK = new BlockSimple(Block.Properties.from(MOON_DIRT));
    public static final Block MOON_TURF = new BlockSimple(Block.Properties.from(MOON_DIRT));
    public static final Block MOON_DUNGEON_BRICKS = new BlockDungeonBrick(Block.Properties.from(MOON_DIRT));

    public static final Block TIN_DECORATION_BLOCK_1 = new DecoBlock(Block.Properties.create(Material.ROCK).sound(SoundType.STONE));
    public static final Block TIN_DECORATION_BLOCK_2 = new DecoBlock(Block.Properties.from(TIN_DECORATION_BLOCK_1));
    public static final Block COPPER_BLOCK = new DecoBlock(Block.Properties.from(TIN_DECORATION_BLOCK_1));
    public static final Block TIN_BLOCK = new DecoBlock(Block.Properties.from(TIN_DECORATION_BLOCK_1));
    public static final Block ALUMINUM_BLOCK = new DecoBlock(Block.Properties.from(TIN_DECORATION_BLOCK_1));
    public static final Block METEORIC_IRON_BLOCK = new DecoBlock(Block.Properties.from(TIN_DECORATION_BLOCK_1));
    public static final Block SILICON_BLOCK = new DecoBlock(Block.Properties.from(TIN_DECORATION_BLOCK_1));
//    public static final Block grating = new BlockGrating(builder);
//    public static final Block gratingWater = new BlockGrating(builder);
//    public static final Block gratingLava = new BlockGrating(builder);
//    public static final Material machine = new Material.Builder(MaterialColor.IRON).build();

    public static ArrayList<Block> hiddenBlocks = Lists.newArrayList();
    public static ArrayList<Block> otherModTorchesLit = Lists.newArrayList();
    public static ArrayList<Block> otherModTorchesUnlit = Lists.newArrayList();

    public static HashMap<Block, Block> itemChanges = new HashMap<>(4, 1.0F);

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name)
    {
        reg.register(thing.setRegistryName(name));
        if (thing instanceof BlockItem)
        {
            GalacticraftCore.blocksList.add(name);
        }
        else if (thing instanceof Item)
        {
            GalacticraftCore.itemList.add(name);
        }
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
    {
        ResourceLocation location = new ResourceLocation(Constants.MOD_ID_CORE, name);
        register(reg, thing, location);
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, String name, IForgeRegistryEntry<V> thing) {
        ResourceLocation location = new ResourceLocation(Constants.MOD_ID_CORE, name);
        register(reg, thing, location);
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, ResourceLocation name, IForgeRegistryEntry<V> thing) {
        register(reg, thing, name);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt)
    {
        IForgeRegistry<Block> r = evt.getRegistry();
        register(r, GCBlockNames.BREATHEABLE_AIR, BREATHEABLE_AIR);
        register(r, GCBlockNames.BRIGHT_AIR, BRIGHT_AIR);
        register(r, GCBlockNames.BRIGHT_BREATHEABLE_AIR, BRIGHT_BREATHEABLE_AIR);
        register(r, GCBlockNames.ARC_LAMP, ARC_LAMP);
        register(r, GCBlockNames.TIER_1_TREASURE_CHEST, TIER_1_TREASURE_CHEST);
        register(r, GCBlockNames.ROCKET_LAUNCH_PAD, ROCKET_LAUNCH_PAD);
        register(r, GCBlockNames.BUGGY_FUELING_PAD, BUGGY_FUELING_PAD);
        register(r, GCBlockNames.FULL_ROCKET_LAUNCH_PAD, FULL_ROCKET_LAUNCH_PAD);
        register(r, GCBlockNames.FULL_BUGGY_FUELING_PAD, FULL_BUGGY_FUELING_PAD);
        register(r, GCBlockNames.UNLIT_TORCH, UNLIT_TORCH);
        register(r, GCBlockNames.WALL_UNLIT_TORCH, WALL_UNLIT_TORCH);
        register(r, GCBlockNames.LIT_UNLIT_TORCH, LIT_UNLIT_TORCH);
        register(r, GCBlockNames.WALL_LIT_UNLIT_TORCH, WALL_LIT_UNLIT_TORCH);
        register(r, GCBlockNames.GLOWSTONE_TORCH, GLOWSTONE_TORCH);
        register(r, GCBlockNames.WALL_GLOWSTONE_TORCH, WALL_GLOWSTONE_TORCH);
        register(r, GCBlockNames.OXYGEN_BUBBLE_DISTRIBUTOR, OXYGEN_BUBBLE_DISTRIBUTOR);
        register(r, GCBlockNames.OXYGEN_COLLECTOR, OXYGEN_COLLECTOR);
        register(r, GCBlockNames.ROCKET_WORKBENCH, ROCKET_WORKBENCH);
        register(r, GCBlockNames.AIR_LOCK_FRAME, AIR_LOCK_FRAME);
        register(r, GCBlockNames.AIR_LOCK_CONTROLLER, AIR_LOCK_CONTROLLER);
        register(r, GCBlockNames.COMPRESSOR, COMPRESSOR);
        register(r, GCBlockNames.ELECTRIC_COMPRESSOR, ELECTRIC_COMPRESSOR);
        register(r, GCBlockNames.ADVANCED_COMPRESSOR, ADVANCED_COMPRESSOR);
        register(r, GCBlockNames.COAL_GENERATOR, COAL_GENERATOR);
        register(r, GCBlockNames.CIRCUIT_FABRICATOR, CIRCUIT_FABRICATOR);
        register(r, GCBlockNames.OXYGEN_STORAGE_MODULE, OXYGEN_STORAGE_MODULE);
        register(r, GCBlockNames.DECONSTRUCTOR, DECONSTRUCTOR);
        register(r, GCBlockNames.CHROMATIC_APPLICATOR, CHROMATIC_APPLICATOR);
        register(r, GCBlockNames.MAGNETIC_CRAFTING_TABLE, MAGNETIC_CRAFTING_TABLE);
        register(r, GCBlockNames.REFINERY, REFINERY);
        register(r, GCBlockNames.FUEL_LOADER, FUEL_LOADER);
        register(r, GCBlockNames.OXYGEN_COMPRESSOR, OXYGEN_COMPRESSOR);
        register(r, GCBlockNames.OXYGEN_DECOMPRESSOR, OXYGEN_DECOMPRESSOR);
        register(r, GCBlockNames.OXYGEN_SEALER, OXYGEN_SEALER);
        register(r, GCBlockNames.OXYGEN_DETECTOR, OXYGEN_DETECTOR);
        register(r, GCBlockNames.CARGO_LOADER, CARGO_LOADER);
        register(r, GCBlockNames.CARGO_UNLOADER, CARGO_UNLOADER);
        register(r, GCBlockNames.BASIC_SOLAR_PANEL, BASIC_SOLAR_PANEL);
        register(r, GCBlockNames.ADVANCED_SOLAR_PANEL, ADVANCED_SOLAR_PANEL);
//        register(r, BlockNames.radioTelescope, radioTelescope);
        register(r, GCBlockNames.ENERGY_STORAGE, ENERGY_STORAGE);
        register(r, GCBlockNames.ENERGY_STORAGE_CLUSTER, ENERGY_STORAGE_CLUSTER);
        register(r, GCBlockNames.ELECTRIC_FURNACE, ELECTRIC_FURNACE);
        register(r, GCBlockNames.ELECTRIC_ARC_FURNACE, ELECTRIC_ARC_FURNACE);
//        register(r, BlockNames.panelLighting, panelLighting);
//        register(r, BlockNames.spinThruster, spinThruster);
        register(r, GCBlockNames.TELEMETRY_UNIT, TELEMETRY_UNIT);
        register(r, GCBlockNames.HIDDEN_REDSTONE_WIRE, HIDDEN_REDSTONE_WIRE);
        register(r, GCBlockNames.HIDDEN_REDSTONE_REPEATER, HIDDEN_REDSTONE_REPEATER);
        register(r, GCBlockNames.PLAYER_DETECTOR, PLAYER_DETECTOR);
        register(r, GCBlockNames.AIR_LOCK_SEAL, AIR_LOCK_SEAL);
        register(r, GCBlockNames.FLUID_PIPE, FLUID_PIPE);
        register(r, GCBlockNames.PULLED_FLUID_PIPE, PULLED_FLUID_PIPE);
        register(r, GCBlockNames.FALLEN_METEOR, FALLEN_METEOR);
//        register(r, BlockNames.spaceGlassVanilla, spaceGlassVanilla);
//        register(r, BlockNames.spaceGlassClear, spaceGlassClear);
//        register(r, BlockNames.spaceGlassTinVanilla, spaceGlassTinVanilla);
//        register(r, BlockNames.spaceGlassTinClear, spaceGlassTinClear);
//        register(r, BlockNames.spaceGlassStrong, spaceGlassStrong);
//        register(r, BlockNames.spaceGlassTinStrong, spaceGlassTinStrong);
        register(r, GCBlockNames.SPACE_STATION_BASE, SPACE_STATION_BASE);
        register(r, GCBlockNames.MULTI_BLOCK, MULTI_BLOCK);
//        register(r, BlockNames.sealableBlock, sealableBlock);
        register(r, GCBlockNames.PARACHEST, PARACHEST);
        register(r, GCBlockNames.ALUMINUM_WIRE, ALUMINUM_WIRE);
        register(r, GCBlockNames.HEAVY_ALUMINUM_WIRE, HEAVY_ALUMINUM_WIRE);
        register(r, GCBlockNames.SWITCHABLE_ALUMINUM_WIRE, SWITCHABLE_ALUMINUM_WIRE);
        register(r, GCBlockNames.SWITCHABLE_HEAVY_ALUMINUM_WIRE, SWITCHABLE_HEAVY_ALUMINUM_WIRE);
        register(r, GCBlockNames.CHEESE_BLOCK, CHEESE_BLOCK);
        register(r, GCBlockNames.DISPLAY_SCREEN, DISPLAY_SCREEN);
        register(r, GCBlockNames.FLUID_TANK, FLUID_TANK);
        register(r, GCBlockNames.MOON_BOSS_SPAWNER, MOON_BOSS_SPAWNER);
//        register(r, BlockNames.slabGCHalf, slabGCHalf);
//        register(r, BlockNames.slabGCDouble, slabGCDouble);
//        register(r, BlockNames.tinStairs1, tinStairs1);
//        register(r, BlockNames.tinStairs2, tinStairs2);
//        register(r, BlockNames.moonStoneStairs, moonStoneStairs);
//        register(r, BlockNames.moonBricksStairs, moonBricksStairs);
//        register(r, BlockNames.wallGC, wallGC);
        register(r, GCBlockNames.HYDRAULIC_PLATFORM, HYDRAULIC_PLATFORM);
        register(r, GCBlockNames.EMERGENCY_POST, EMERGENCY_POST);
        register(r, GCBlockNames.EMERGENCY_POST_KIT, EMERGENCY_POST_KIT);
        register(r, GCBlockNames.COPPER_ORE, COPPER_ORE);
        register(r, GCBlockNames.TIN_ORE, TIN_ORE);
        register(r, GCBlockNames.ALUMINUM_ORE, ALUMINUM_ORE);
        register(r, GCBlockNames.SILICON_ORE, SILICON_ORE);
        register(r, GCBlockNames.MOON_COPPER_ORE, MOON_COPPER_ORE);
        register(r, GCBlockNames.MOON_TIN_ORE, MOON_TIN_ORE);
        register(r, GCBlockNames.CHEESE_ORE, CHEESE_ORE);
        register(r, GCBlockNames.SAPPHIRE_ORE, SAPPHIRE_ORE);
        register(r, GCBlockNames.MOON_DIRT, MOON_DIRT);
        register(r, GCBlockNames.MOON_ROCK, MOON_ROCK);
        register(r, GCBlockNames.MOON_TURF, MOON_TURF);
        register(r, GCBlockNames.MOON_DUNGEON_BRICKS, MOON_DUNGEON_BRICKS);
        register(r, GCBlockNames.TIN_DECORATION_BLOCK_1, TIN_DECORATION_BLOCK_1);
        register(r, GCBlockNames.TIN_DECORATION_BLOCK_2, TIN_DECORATION_BLOCK_2);
        register(r, GCBlockNames.COPPER_BLOCK, COPPER_BLOCK);
        register(r, GCBlockNames.TIN_BLOCK, TIN_BLOCK);
        register(r, GCBlockNames.ALUMINUM_BLOCK, ALUMINUM_BLOCK);
        register(r, GCBlockNames.METEORIC_IRON_BLOCK, METEORIC_IRON_BLOCK);
        register(r, GCBlockNames.SILICON_BLOCK, SILICON_BLOCK);
//        register(r, BlockNames.grating, grating);
//        register(r, BlockNames.gratingWater, gratingWater);
//        register(r, BlockNames.gratingLava, gratingLava);

//        register(r, new BlockGrating(builder), BlockNames.grating); TODO
//        register(r, new BlockGrating(builder), BlockNames.gratingWater);
//        register(r, new BlockGrating(builder), BlockNames.gratingLava);

//        GCBlocks.breatheableAir = new BlockBreathableAir("breatheable_air");
//        GCBlocks.brightAir = new BlockBrightAir("bright_air");
//        GCBlocks.brightBreatheableAir = new BlockBrightBreathableAir("bright_breathable_air");
//        GCBlocks.brightLamp = new BlockBrightLamp("arclamp");
//        GCBlocks.treasureChestTier1 = new BlockTier1TreasureChest("treasure_chest");
//        GCBlocks.landingPad = new BlockLandingPad("landing_pad");
//        GCBlocks.landingPadFull = new BlockLandingPadFull("landing_pad_full");
//        GCBlocks.unlitTorch = new BlockUnlitTorch(false, "unlit_torch");
//        GCBlocks.unlitTorchLit = new BlockUnlitTorch(true, "unlit_torch_lit");
//        GCBlocks.oxygenDistributor = new BlockOxygenDistributor("distributor");
//        GCBlocks.oxygenPipe = new BlockFluidPipe("fluid_pipe", BlockFluidPipe.EnumPipeMode.NORMAL);
//        GCBlocks.oxygenPipePull = new BlockFluidPipe("fluid_pipe_pull", BlockFluidPipe.EnumPipeMode.PULL);
//        GCBlocks.oxygenCollector = new BlockOxygenCollector("collector");
//        GCBlocks.nasaWorkbench = new BlockNasaWorkbench("rocket_workbench");
//        GCBlocks.fallenMeteor = new BlockFallenMeteor("fallen_meteor");
//        GCBlocks.basicBlock = new BlockBasic("basic_block_core");
//        GCBlocks.airLockFrame = new BlockAirLockFrame("air_lock_frame");
//        GCBlocks.airLockSeal = new BlockAirLockWall("air_lock_seal");
//        //These glass types have to be registered as 6 separate blocks, (a) to allow different coloring of each one and (b) because the Forge MultiLayer custom model does not allow for different textures to be set for different variants
//        GCBlocks.spaceGlassVanilla = new BlockSpaceGlass("space_glass_vanilla", GlassType.VANILLA, GlassFrame.PLAIN, null).setHardness(0.3F).setResistance(3F);
//        GCBlocks.spaceGlassClear = new BlockSpaceGlass("space_glass_clear", GlassType.CLEAR, GlassFrame.PLAIN, null).setHardness(0.3F).setResistance(3F);
//        GCBlocks.spaceGlassStrong = new BlockSpaceGlass("space_glass_strong", GlassType.STRONG, GlassFrame.PLAIN, null).setHardness(4F).setResistance(35F);
//        GCBlocks.spaceGlassTinVanilla = new BlockSpaceGlass("space_glass_vanilla_tin", GlassType.VANILLA, GlassFrame.TIN_DECO, GCBlocks.spaceGlassVanilla).setHardness(0.3F).setResistance(4F);
//        GCBlocks.spaceGlassTinClear = new BlockSpaceGlass("space_glass_clear_tin", GlassType.CLEAR, GlassFrame.TIN_DECO, GCBlocks.spaceGlassClear).setHardness(0.3F).setResistance(4F);
//        GCBlocks.spaceGlassTinStrong = new BlockSpaceGlass("space_glass_strong_tin", GlassType.STRONG, GlassFrame.TIN_DECO, GCBlocks.spaceGlassStrong).setHardness(4F).setResistance(35F);
//        GCBlocks.crafting = new BlockCrafting("magnetic_table");
//        GCBlocks.refinery = new BlockRefinery("refinery");
//        GCBlocks.oxygenCompressor = new BlockOxygenCompressor(false, "oxygen_compressor");
//        GCBlocks.fuelLoader = new BlockFuelLoader("fuel_loader");
//        GCBlocks.spaceStationBase = new BlockSpaceStationBase("space_station_base");
//        GCBlocks.fakeBlock = new BlockMulti("block_multi");
//        GCBlocks.oxygenSealer = new BlockOxygenSealer("sealer");
//        GCBlocks.sealableBlock = new BlockEnclosed("enclosed");
//        GCBlocks.oxygenDetector = new BlockOxygenDetector("oxygen_detector");
//        GCBlocks.cargoLoader = new BlockCargoLoader("cargo");
//        GCBlocks.parachest = new BlockParaChest("parachest");
//        GCBlocks.solarPanel = new BlockSolar("solar");
//        GCBlocks.radioTelescope = new BlockDish("dishbase");
//        GCBlocks.machineBase = new BlockMachine("machine");
//        GCBlocks.machineBase2 = new BlockMachine2("machine2");
//        GCBlocks.machineBase3 = new BlockMachine3("machine3");
//        GCBlocks.machineBase4 = new BlockMachine4("machine4");
//        GCBlocks.machineTiered = new BlockMachineTiered("machine_tiered");
//        GCBlocks.aluminumWire = new BlockAluminumWire("aluminum_wire");
//        GCBlocks.panelLighting = new BlockPanelLighting("panel_lighting");
//        GCBlocks.glowstoneTorch = new BlockGlowstoneTorch("glowstone_torch");
//        GCBlocks.blockMoon = new BlockBasicMoon("basic_block_moon");
//        GCBlocks.cheeseBlock = new BlockCheese("cheese");
//        GCBlocks.spinThruster = new BlockSpinThruster("spin_thruster");
//        GCBlocks.screen = new BlockScreen("view_screen");
//        GCBlocks.telemetry = new BlockTelemetry("telemetry");
//        GCBlocks.fluidTank = new BlockFluidTank("fluid_tank");
//        GCBlocks.bossSpawner = new BlockBossSpawner("boss_spawner");
//        GCBlocks.slabGCHalf = new BlockSlabGC("slab_gc_half", Material.ROCK);
//        GCBlocks.slabGCDouble = new BlockDoubleSlabGC("slab_gc_double", Material.ROCK);
//        GCBlocks.tinStairs1 = new BlockStairsGC("tin_stairs_1", basicBlock.getDefaultState().with(BlockBasic.BASIC_TYPE, BlockBasic.EnumBlockBasic.ALUMINUM_DECORATION_BLOCK_0)).setHardness(2.0F);
//        GCBlocks.tinStairs2 = new BlockStairsGC("tin_stairs_2", basicBlock.getDefaultState().with(BlockBasic.BASIC_TYPE, BlockBasic.EnumBlockBasic.ALUMINUM_DECORATION_BLOCK_1)).setHardness(2.0F);
//        GCBlocks.moonStoneStairs = new BlockStairsGC("moon_stairs_stone", blockMoon.getDefaultState().with(BlockBasicMoon.BASIC_TYPE_MOON, BlockBasicMoon.EnumBlockBasicMoon.MOON_STONE)).setHardness(1.5F);
//        GCBlocks.moonBricksStairs = new BlockStairsGC("moon_stairs_brick", blockMoon.getDefaultState().with(BlockBasicMoon.BASIC_TYPE_MOON, BlockBasicMoon.EnumBlockBasicMoon.MOON_DUNGEON_BRICK)).setHardness(4.0F);
//        GCBlocks.wallGC = new BlockWallGC("wall_gc");
//        GCBlocks.concealedRedstone = new BlockConcealedRedstone("concealed_redstone");
//        GCBlocks.concealedRepeater_Powered = new BlockConcealedRepeater("concealed_repeater_pow", true);
//        GCBlocks.concealedRepeater_Unpowered = new BlockConcealedRepeater("concealed_repeater", false);
//        GCBlocks.concealedDetector = new BlockConcealedDetector("concealed_detector");
//        GCBlocks.platform = new BlockPlatform("platform");
//        GCBlocks.emergencyBox = new BlockEmergencyBox("emergency_box");
//        GCBlocks.grating = new BlockGrating("grating", ConfigManagerCore.allowLiquidGratings.get() ? Material.CARPET : Material.IRON);
//        GCBlocks.gratingWater = new BlockGrating("grating1", Material.WATER);
//        GCBlocks.gratingLava = new BlockGrating("grating2", Material.LAVA).setLightLevel(1.0F);

        // Hide certain items from NEI
        GCBlocks.hiddenBlocks.add(GCBlocks.AIR_LOCK_SEAL);
        GCBlocks.hiddenBlocks.add(GCBlocks.PULLED_FLUID_PIPE);
        GCBlocks.hiddenBlocks.add(GCBlocks.UNLIT_TORCH);
        GCBlocks.hiddenBlocks.add(GCBlocks.LIT_UNLIT_TORCH);
        GCBlocks.hiddenBlocks.add(GCBlocks.FULL_ROCKET_LAUNCH_PAD);
        GCBlocks.hiddenBlocks.add(GCBlocks.SPACE_STATION_BASE);
        GCBlocks.hiddenBlocks.add(GCBlocks.MOON_BOSS_SPAWNER);
//        GCBlocks.hiddenBlocks.add(GCBlocks.slabGCDouble);

        // Register blocks before register ores, so that the ItemStack picks up the correct item
//        GCBlocks.registerBlocks();
//        GCBlocks.setHarvestLevels();

        BlockUnlitTorch.register((BlockUnlitTorch) GCBlocks.UNLIT_TORCH, (BlockUnlitTorch) GCBlocks.LIT_UNLIT_TORCH, Blocks.TORCH);
        BlockUnlitTorchWall.register((BlockUnlitTorchWall) GCBlocks.WALL_UNLIT_TORCH, (BlockUnlitTorchWall) GCBlocks.WALL_LIT_UNLIT_TORCH, Blocks.WALL_TORCH);
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        Item.Properties props = GCItems.defaultBuilder().group(GalacticraftCore.galacticraftBlocksTab);
        register(r, Registry.BLOCK.getKey(ARC_LAMP), new BlockItem(ARC_LAMP, props));
        register(r, Registry.BLOCK.getKey(TIER_1_TREASURE_CHEST), new ItemBlockDesc(TIER_1_TREASURE_CHEST, props.setISTER(() -> ItemStackTileEntityRendererGC::new)));
        register(r, Registry.BLOCK.getKey(ROCKET_LAUNCH_PAD), new BlockItem(ROCKET_LAUNCH_PAD, props));
        register(r, Registry.BLOCK.getKey(BUGGY_FUELING_PAD), new BlockItem(BUGGY_FUELING_PAD, props));
        register(r, Registry.BLOCK.getKey(GLOWSTONE_TORCH), new ItemBlockWallOrFloorDesc(GLOWSTONE_TORCH, WALL_GLOWSTONE_TORCH, props));
        register(r, Registry.BLOCK.getKey(OXYGEN_BUBBLE_DISTRIBUTOR), new ItemBlockDesc(OXYGEN_BUBBLE_DISTRIBUTOR, props));
        register(r, Registry.BLOCK.getKey(OXYGEN_COLLECTOR), new ItemBlockDesc(OXYGEN_COLLECTOR, props));
        register(r, Registry.BLOCK.getKey(ROCKET_WORKBENCH), new BlockItem(ROCKET_WORKBENCH, props));
        register(r, Registry.BLOCK.getKey(AIR_LOCK_FRAME), new BlockItem(AIR_LOCK_FRAME, props));
        register(r, Registry.BLOCK.getKey(AIR_LOCK_CONTROLLER), new BlockItem(AIR_LOCK_CONTROLLER, props));
        register(r, Registry.BLOCK.getKey(COMPRESSOR), new BlockItem(COMPRESSOR, props));
        register(r, Registry.BLOCK.getKey(ELECTRIC_COMPRESSOR), new BlockItem(ELECTRIC_COMPRESSOR, props));
        register(r, Registry.BLOCK.getKey(ADVANCED_COMPRESSOR), new BlockItem(ADVANCED_COMPRESSOR, props));
        register(r, Registry.BLOCK.getKey(COAL_GENERATOR), new BlockItem(COAL_GENERATOR, props));
        register(r, Registry.BLOCK.getKey(CIRCUIT_FABRICATOR), new BlockItem(CIRCUIT_FABRICATOR, props));
        register(r, Registry.BLOCK.getKey(OXYGEN_STORAGE_MODULE), new BlockItem(OXYGEN_STORAGE_MODULE, props));
        register(r, Registry.BLOCK.getKey(DECONSTRUCTOR), new BlockItem(DECONSTRUCTOR, props));
        register(r, Registry.BLOCK.getKey(CHROMATIC_APPLICATOR), new BlockItem(CHROMATIC_APPLICATOR, props));
        register(r, Registry.BLOCK.getKey(MAGNETIC_CRAFTING_TABLE), new ItemBlockDesc(MAGNETIC_CRAFTING_TABLE, props));
        register(r, Registry.BLOCK.getKey(REFINERY), new ItemBlockDesc(REFINERY, props));
        register(r, Registry.BLOCK.getKey(FUEL_LOADER), new ItemBlockDesc(FUEL_LOADER, props));
        register(r, Registry.BLOCK.getKey(OXYGEN_COMPRESSOR), new ItemBlockDesc(OXYGEN_COMPRESSOR, props));
        register(r, Registry.BLOCK.getKey(OXYGEN_DECOMPRESSOR), new ItemBlockDesc(OXYGEN_DECOMPRESSOR, props));
        register(r, Registry.BLOCK.getKey(OXYGEN_SEALER), new ItemBlockDesc(OXYGEN_SEALER, props));
        register(r, Registry.BLOCK.getKey(OXYGEN_DETECTOR), new ItemBlockDesc(OXYGEN_DETECTOR, props));
        register(r, Registry.BLOCK.getKey(CARGO_LOADER), new ItemBlockDesc(CARGO_LOADER, props));
        register(r, Registry.BLOCK.getKey(CARGO_UNLOADER), new ItemBlockDesc(CARGO_UNLOADER, props));
        register(r, Registry.BLOCK.getKey(BASIC_SOLAR_PANEL), new ItemBlockDesc(BASIC_SOLAR_PANEL, props));
        register(r, Registry.BLOCK.getKey(ADVANCED_SOLAR_PANEL), new ItemBlockDesc(ADVANCED_SOLAR_PANEL, props));
        register(r, Registry.BLOCK.getKey(ENERGY_STORAGE), new ItemBlockDesc(ENERGY_STORAGE, props));
        register(r, Registry.BLOCK.getKey(ENERGY_STORAGE_CLUSTER), new ItemBlockDesc(ENERGY_STORAGE_CLUSTER, props));
        register(r, Registry.BLOCK.getKey(ELECTRIC_FURNACE), new ItemBlockDesc(ELECTRIC_FURNACE, props));
        register(r, Registry.BLOCK.getKey(ELECTRIC_ARC_FURNACE), new ItemBlockDesc(ELECTRIC_ARC_FURNACE, props));
        register(r, Registry.BLOCK.getKey(TELEMETRY_UNIT), new ItemBlockDesc(TELEMETRY_UNIT, props));
        register(r, Registry.BLOCK.getKey(HIDDEN_REDSTONE_WIRE), new BlockItem(HIDDEN_REDSTONE_WIRE, props));
        register(r, Registry.BLOCK.getKey(HIDDEN_REDSTONE_REPEATER), new BlockItem(HIDDEN_REDSTONE_REPEATER, props));
        register(r, Registry.BLOCK.getKey(PLAYER_DETECTOR), new BlockItem(PLAYER_DETECTOR, props));
        register(r, Registry.BLOCK.getKey(FLUID_PIPE), new ItemBlockDesc(FLUID_PIPE, props));
        register(r, Registry.BLOCK.getKey(PULLED_FLUID_PIPE), new ItemBlockDesc(PULLED_FLUID_PIPE, new Item.Properties()));
        register(r, Registry.BLOCK.getKey(FALLEN_METEOR), new ItemBlockDesc(FALLEN_METEOR, props));
        register(r, Registry.BLOCK.getKey(PARACHEST), new ItemBlockDesc(PARACHEST, props));
        register(r, Registry.BLOCK.getKey(ALUMINUM_WIRE), new BlockItem(ALUMINUM_WIRE, props));
        register(r, Registry.BLOCK.getKey(HEAVY_ALUMINUM_WIRE), new BlockItem(HEAVY_ALUMINUM_WIRE, props));
        register(r, Registry.BLOCK.getKey(SWITCHABLE_ALUMINUM_WIRE), new BlockItem(SWITCHABLE_ALUMINUM_WIRE, props));
        register(r, Registry.BLOCK.getKey(SWITCHABLE_HEAVY_ALUMINUM_WIRE), new BlockItem(SWITCHABLE_HEAVY_ALUMINUM_WIRE, props));
        register(r, Registry.BLOCK.getKey(CHEESE_BLOCK), new BlockItem(CHEESE_BLOCK, props));
        register(r, Registry.BLOCK.getKey(DISPLAY_SCREEN), new ItemBlockDesc(DISPLAY_SCREEN, props));
        register(r, Registry.BLOCK.getKey(FLUID_TANK), new ItemBlockDesc(FLUID_TANK, props));
        register(r, Registry.BLOCK.getKey(EMERGENCY_POST), new BlockItem(EMERGENCY_POST, props));
        register(r, Registry.BLOCK.getKey(EMERGENCY_POST_KIT), new BlockItem(EMERGENCY_POST_KIT, props));
        register(r, Registry.BLOCK.getKey(COPPER_ORE), new BlockItem(COPPER_ORE, props));
        register(r, Registry.BLOCK.getKey(TIN_ORE), new BlockItem(TIN_ORE, props));
        register(r, Registry.BLOCK.getKey(ALUMINUM_ORE), new BlockItem(ALUMINUM_ORE, props));
        register(r, Registry.BLOCK.getKey(SILICON_ORE), new BlockItem(SILICON_ORE, props));
        register(r, Registry.BLOCK.getKey(MOON_COPPER_ORE), new BlockItem(MOON_COPPER_ORE, props));
        register(r, Registry.BLOCK.getKey(MOON_TIN_ORE), new BlockItem(MOON_TIN_ORE, props));
        register(r, Registry.BLOCK.getKey(CHEESE_ORE), new BlockItem(CHEESE_ORE, props));
        register(r, Registry.BLOCK.getKey(SAPPHIRE_ORE), new BlockItem(SAPPHIRE_ORE, props));
        register(r, Registry.BLOCK.getKey(MOON_DIRT), new BlockItem(MOON_DIRT, props));
        register(r, Registry.BLOCK.getKey(MOON_ROCK), new BlockItem(MOON_ROCK, props));
        register(r, Registry.BLOCK.getKey(MOON_TURF), new BlockItem(MOON_TURF, props));
        register(r, Registry.BLOCK.getKey(MOON_DUNGEON_BRICKS), new BlockItem(MOON_DUNGEON_BRICKS, props));
        register(r, Registry.BLOCK.getKey(TIN_DECORATION_BLOCK_1), new BlockItem(TIN_DECORATION_BLOCK_1, props));
        register(r, Registry.BLOCK.getKey(TIN_DECORATION_BLOCK_2), new BlockItem(TIN_DECORATION_BLOCK_2, props));
        register(r, Registry.BLOCK.getKey(COPPER_BLOCK), new BlockItem(COPPER_BLOCK, props));
        register(r, Registry.BLOCK.getKey(TIN_BLOCK), new BlockItem(TIN_BLOCK, props));
        register(r, Registry.BLOCK.getKey(ALUMINUM_BLOCK), new BlockItem(ALUMINUM_BLOCK, props));
        register(r, Registry.BLOCK.getKey(METEORIC_IRON_BLOCK), new BlockItem(METEORIC_IRON_BLOCK, props));
        register(r, Registry.BLOCK.getKey(SILICON_BLOCK), new BlockItem(SILICON_BLOCK, props));
        register(r, Registry.BLOCK.getKey(HYDRAULIC_PLATFORM), new ItemBlockDesc(HYDRAULIC_PLATFORM, props));
        props = props.group(null);
        register(r, Registry.BLOCK.getKey(AIR_LOCK_SEAL), new BlockItem(AIR_LOCK_SEAL, props));
        register(r, Registry.BLOCK.getKey(UNLIT_TORCH), new ItemBlockWallOrFloorDesc(UNLIT_TORCH, WALL_UNLIT_TORCH, props));
        register(r, Registry.BLOCK.getKey(LIT_UNLIT_TORCH), new ItemBlockWallOrFloorDesc(LIT_UNLIT_TORCH, WALL_LIT_UNLIT_TORCH, props));
    }

    public static void oreDictRegistrations()
    {
        // TODO
//        OreDictionary.registerOre("oreCopper", new ItemStack(GCBlocks.basicBlock, 1, 5));
//        OreDictionary.registerOre("oreCopper", new ItemStack(GCBlocks.blockMoon, 1, 0));
//        OreDictionary.registerOre("oreTin", new ItemStack(GCBlocks.basicBlock, 1, 6));
//        OreDictionary.registerOre("oreTin", new ItemStack(GCBlocks.blockMoon, 1, 1));
//        OreDictionary.registerOre("oreAluminum", new ItemStack(GCBlocks.basicBlock, 1, 7));
//        OreDictionary.registerOre("oreAluminium", new ItemStack(GCBlocks.basicBlock, 1, 7));
//        OreDictionary.registerOre("oreNaturalAluminum", new ItemStack(GCBlocks.basicBlock, 1, 7));
//        OreDictionary.registerOre("oreSilicon", new ItemStack(GCBlocks.basicBlock, 1, 8));
//        OreDictionary.registerOre("oreCheese", new ItemStack(GCBlocks.blockMoon, 1, 2));
//
//        OreDictionary.registerOre("blockCopper", new ItemStack(GCBlocks.basicBlock, 1, 9));
//        OreDictionary.registerOre("blockTin", new ItemStack(GCBlocks.basicBlock, 1, 10));
//        OreDictionary.registerOre("blockAluminum", new ItemStack(GCBlocks.basicBlock, 1, 11));
//        OreDictionary.registerOre("blockAluminium", new ItemStack(GCBlocks.basicBlock, 1, 11));
//        OreDictionary.registerOre("blockSilicon", new ItemStack(GCBlocks.basicBlock, 1, 13));
//
//        OreDictionary.registerOre("turfMoon", new ItemStack(GCBlocks.blockMoon, 1, EnumBlockBasicMoon.MOON_TURF.getMeta()));
    }

//    private static void setHarvestLevel(Block block, String toolClass, int level, int meta)
//    {
//        block.setHarvestLevel(toolClass, level, block.getStateFromMeta(meta));
//    }

//    public static void doOtherModsTorches(IForgeRegistry<Block> registry)
//    {
//        BlockUnlitTorch torch;
//        BlockUnlitTorch torchLit;
//
//        if (CompatibilityManager.isTConstructLoaded)
//        {
//            Block modTorch = null;
//            try
//            {
//                //tconstruct.world.TinkerWorld.stoneTorch
//                Class clazz = Class.forName("slimeknights.tconstruct.gadgets.TinkerGadgets");
//                modTorch = (Block) clazz.getDeclaredField("stoneTorch").get(null);
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//            if (modTorch != null)
//            {
//                torch = new BlockUnlitTorch(false, "unlit_torch_stone");
//                torchLit = new BlockUnlitTorch(true, "unlit_torch_stone_lit");
//                GCBlocks.hiddenBlocks.add(torch);
//                GCBlocks.hiddenBlocks.add(torchLit);
//                GCBlocks.otherModTorchesUnlit.add(torch);
//                GCBlocks.otherModTorchesLit.add(torchLit);
//                registerBlock(torch, ItemBlockGC.class);
//                registerBlock(torchLit, ItemBlockGC.class);
//                registry.register(torch);
//                registry.register(torchLit);
//                BlockUnlitTorch.register(torch, torchLit, modTorch);
//                GCLog.info("Galacticraft: activating Tinker's Construct compatibility.");
//            }
//        }
//    } TODO

//    public static void registerFuel()
//    {
//        GCBlocks.fuel = new BlockFluidGC(GCFluids.fluidFuel, "fuel");
//        ((BlockFluidGC) GCBlocks.fuel).setQuantaPerBlock(3);
//        GCBlocks.fuel.setUnlocalizedName("fuel");
//        GCBlocks.registerBlock(GCBlocks.fuel, ItemBlockGC.class);
//    }
//
//    public static void registerOil()
//    {
//        GCBlocks.crudeOil = new BlockFluidGC(GCFluids.fluidOil, "oil");
//        ((BlockFluidGC) GCBlocks.crudeOil).setQuantaPerBlock(3);
//        GCBlocks.crudeOil.setUnlocalizedName("crude_oil_still");
//        GCBlocks.registerBlock(GCBlocks.crudeOil, ItemBlockGC.class);
//    } TODO

//    public static void setHarvestLevels()
//    {
//        setHarvestLevel(GCBlocks.basicBlock, "pickaxe", 1, 5); //Copper ore
//        setHarvestLevel(GCBlocks.basicBlock, "pickaxe", 1, 6); //Tin ore
//        setHarvestLevel(GCBlocks.basicBlock, "pickaxe", 1, 7); //Aluminium ore
//        setHarvestLevel(GCBlocks.basicBlock, "pickaxe", 2, 8); //Silicon ore
//        setHarvestLevel(GCBlocks.fallenMeteor, "pickaxe", 3, 0);
//        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 1, 0); //Copper ore
//        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 1, 1); //Tin ore
//        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 1, 2); //Cheese ore
//        setHarvestLevel(GCBlocks.blockMoon, "shovel", 0, 3); //Moon dirt
//        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 0, 4); //Moon rock
//
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 0);
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 1);
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 2);
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 3, 3);
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 4);
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 3, 5);
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 6);
//
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 0);
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 1);
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 2);
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 3, 3);
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 4);
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 3, 5);
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 6);
//
//        setHarvestLevel(GCBlocks.tinStairs1, "pickaxe", 1, 0);
//        setHarvestLevel(GCBlocks.tinStairs1, "pickaxe", 1, 0);
//
//        setHarvestLevel(GCBlocks.moonStoneStairs, "pickaxe", 1, 0);
//        setHarvestLevel(GCBlocks.moonBricksStairs, "pickaxe", 3, 0);
//
//        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 1, 0);
//        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 1, 1);
//        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 1, 2);
//        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 3, 3);
//        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 0, 4);
//        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 3, 5);
//
//        setHarvestLevel(GCBlocks.wallGC, "shovel", 0, 5);
//
//        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 3, 14); //Moon dungeon brick (actually unharvestable)
//    } TODO

//    public static void registerBlock(Block block, Class<? extends BlockItem> itemClass, Object... itemCtorArgs)
//    {
//        String name = block.getUnlocalizedName().substring(5);
//        if (block.getRegistryName() == null)
//        {
//            block.setRegistryName(name);
//        }
//        GCCoreUtil.registerGalacticraftBlock(name, block);
//
//        if (itemClass != null)
//        {
//            BlockItem item = null;
//            Class<?>[] ctorArgClasses = new Class<?>[itemCtorArgs.length + 1];
//            ctorArgClasses[0] = Block.class;
//            for (int idx = 1; idx < ctorArgClasses.length; idx++)
//            {
//                ctorArgClasses[idx] = itemCtorArgs[idx - 1].getClass();
//            }
//
//            try
//            {
//                Constructor<? extends BlockItem> constructor = itemClass.getConstructor(ctorArgClasses);
//                item = constructor.newInstance(ObjectArrays.concat(block, itemCtorArgs));
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//            if (item != null)
//            {
//                GCCoreUtil.registerGalacticraftItem(name, item);
//                if (item.getRegistryName() == null)
//                {
//                    item.setRegistryName(name);
//                }
//            }
//        }
//    }

//    public static void registerBlocks(IForgeRegistry<Block> registry)
//    {
//        for (Block block : GalacticraftCore.blocksList)
//        {
//            registry.register(block);
//        }
//
//        //Complete registration of various types of torches
//        BlockUnlitTorch.register((BlockUnlitTorch) GCBlocks.unlitTorch, (BlockUnlitTorch) GCBlocks.unlitTorchLit, Blocks.TORCH);
//    }

//    public static void registerBlocks()
//    {
//        registerBlock(GCBlocks.landingPad, ItemBlockLandingPad.class);
//        registerBlock(GCBlocks.landingPadFull, ItemBlockGC.class);
//        registerBlock(GCBlocks.unlitTorch, ItemBlockGC.class);
//        registerBlock(GCBlocks.unlitTorchLit, ItemBlockGC.class);
//        registerBlock(GCBlocks.breatheableAir, null);
//        registerBlock(GCBlocks.brightAir, null);
//        registerBlock(GCBlocks.brightBreatheableAir, null);
//        registerBlock(GCBlocks.oxygenDistributor, ItemBlockDesc.class);
//        registerBlock(GCBlocks.oxygenCollector, ItemBlockDesc.class);
//        registerBlock(GCBlocks.oxygenCompressor, ItemBlockOxygenCompressor.class);
//        registerBlock(GCBlocks.oxygenSealer, ItemBlockDesc.class);
//        registerBlock(GCBlocks.oxygenDetector, ItemBlockDesc.class);
//        registerBlock(GCBlocks.aluminumWire, ItemBlockAluminumWire.class);
//        registerBlock(GCBlocks.oxygenPipe, ItemBlockDesc.class);
//        registerBlock(GCBlocks.oxygenPipePull, ItemBlockDesc.class);
//        registerBlock(GCBlocks.refinery, ItemBlockDesc.class);
//        registerBlock(GCBlocks.fuelLoader, ItemBlockDesc.class);
//        registerBlock(GCBlocks.cargoLoader, ItemBlockCargoLoader.class);
//        registerBlock(GCBlocks.nasaWorkbench, ItemBlockNasaWorkbench.class);
//        registerBlock(GCBlocks.basicBlock, ItemBlockBase.class);
//        registerBlock(GCBlocks.airLockFrame, ItemBlockAirLock.class);
//        registerBlock(GCBlocks.airLockSeal, ItemBlockGC.class);
//        registerBlock(GCBlocks.spaceGlassClear, ItemBlockGlassGC.class);
//        registerBlock(GCBlocks.spaceGlassVanilla, ItemBlockGlassGC.class);
//        registerBlock(GCBlocks.spaceGlassStrong, ItemBlockGlassGC.class);
//        registerBlock(GCBlocks.spaceGlassTinClear, null);  //The corresponding item is already registered
//        registerBlock(GCBlocks.spaceGlassTinVanilla, null);  //The corresponding item is already registered
//        registerBlock(GCBlocks.spaceGlassTinStrong, null);  //The corresponding item is already registered
//        registerBlock(GCBlocks.crafting, ItemBlockDesc.class);
//        registerBlock(GCBlocks.sealableBlock, ItemBlockEnclosed.class);
//        registerBlock(GCBlocks.spaceStationBase, ItemBlockGC.class);
//        registerBlock(GCBlocks.fakeBlock, null);
//        registerBlock(GCBlocks.parachest, ItemBlockDesc.class);
//        registerBlock(GCBlocks.solarPanel, ItemBlockSolar.class);
//        registerBlock(GCBlocks.radioTelescope, ItemBlockGC.class);
//        registerBlock(GCBlocks.machineBase, ItemBlockMachine.class);
//        registerBlock(GCBlocks.machineBase2, ItemBlockMachine.class);
//        registerBlock(GCBlocks.machineBase3, ItemBlockMachine.class);
//        registerBlock(GCBlocks.machineTiered, ItemBlockMachine.class);
//        registerBlock(GCBlocks.machineBase4, ItemBlockMachine.class);
//        registerBlock(GCBlocks.panelLighting, ItemBlockPanel.class);
//        registerBlock(GCBlocks.glowstoneTorch, ItemBlockDesc.class);
//        registerBlock(GCBlocks.fallenMeteor, ItemBlockDesc.class);
//        registerBlock(GCBlocks.blockMoon, ItemBlockMoon.class);
//        registerBlock(GCBlocks.cheeseBlock, ItemBlockCheese.class);
//        registerBlock(GCBlocks.spinThruster, ItemBlockThruster.class);
//        registerBlock(GCBlocks.screen, ItemBlockDesc.class);
//        registerBlock(GCBlocks.telemetry, ItemBlockDesc.class);
//        registerBlock(GCBlocks.brightLamp, ItemBlockArclamp.class);
//        registerBlock(GCBlocks.treasureChestTier1, ItemBlockDesc.class);
//        registerBlock(GCBlocks.fluidTank, ItemBlockDesc.class);
//        registerBlock(GCBlocks.bossSpawner, ItemBlockGC.class);
//        registerBlock(GCBlocks.tinStairs1, ItemBlockGC.class);
//        registerBlock(GCBlocks.tinStairs2, ItemBlockGC.class);
//        registerBlock(GCBlocks.moonStoneStairs, ItemBlockGC.class);
//        registerBlock(GCBlocks.moonBricksStairs, ItemBlockGC.class);
//        registerBlock(GCBlocks.wallGC, ItemBlockWallGC.class);
//        registerBlock(GCBlocks.slabGCHalf, ItemBlockSlabGC.class, GCBlocks.slabGCHalf, GCBlocks.slabGCDouble);
//        registerBlock(GCBlocks.slabGCDouble, ItemBlockSlabGC.class, GCBlocks.slabGCHalf, GCBlocks.slabGCDouble);
//        registerBlock(GCBlocks.concealedRedstone, ItemBlockGC.class);
//        registerBlock(GCBlocks.concealedRepeater_Powered, ItemBlockGC.class);
//        registerBlock(GCBlocks.concealedRepeater_Unpowered, ItemBlockGC.class);
//        registerBlock(GCBlocks.concealedDetector, ItemBlockCreativeGC.class);
//        registerBlock(GCBlocks.platform, ItemBlockDesc.class);
//        registerBlock(GCBlocks.emergencyBox, ItemBlockEmergencyBox.class);
//        registerBlock(GCBlocks.grating, ItemBlockGC.class);
//        registerBlock(GCBlocks.gratingWater, null);
//        registerBlock(GCBlocks.gratingLava, null);
////        GCCoreUtil.sortBlock(GCBlocks.aluminumWire, 0, new StackSorted(GCBlocks.landingPad, 1));
////        GCCoreUtil.sortBlock(GCBlocks.aluminumWire, 1, new StackSorted(GCBlocks.aluminumWire, 0));
////        GCCoreUtil.sortBlock(GCBlocks.oxygenPipe, 0, new StackSorted(GCBlocks.aluminumWire, 1));
//    }

    @SubscribeEvent
    public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt)
    {
        IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();

        register(r, TileEntityType.Builder.create(TileEntityTreasureChest::new, TIER_1_TREASURE_CHEST).build(null), GCBlockNames.TIER_1_TREASURE_CHEST);
        register(r, TileEntityType.Builder.create(TileEntityOxygenDistributor::new, OXYGEN_BUBBLE_DISTRIBUTOR).build(null), GCBlockNames.OXYGEN_BUBBLE_DISTRIBUTOR);
        register(r, TileEntityType.Builder.create(TileEntityOxygenCollector::new, OXYGEN_COLLECTOR).build(null), GCBlockNames.OXYGEN_COLLECTOR);
        register(r, TileEntityType.Builder.create(TileEntityFluidPipe::new, FLUID_PIPE, PULLED_FLUID_PIPE).build(null), GCBlockNames.FLUID_PIPE);
        register(r, TileEntityType.Builder.create(TileEntityAirLock::new, AIR_LOCK_FRAME).build(null), GCBlockNames.AIR_LOCK_FRAME);
        register(r, TileEntityType.Builder.create(TileEntityRefinery::new, REFINERY).build(null), GCBlockNames.REFINERY);
        register(r, TileEntityType.Builder.create(TileEntityNasaWorkbench::new, ROCKET_WORKBENCH).build(null), GCBlockNames.ROCKET_WORKBENCH);
        register(r, TileEntityType.Builder.create(TileEntityDeconstructor::new, DECONSTRUCTOR).build(null), GCBlockNames.DECONSTRUCTOR);
        register(r, TileEntityType.Builder.create(TileEntityOxygenCompressor::new, OXYGEN_COMPRESSOR).build(null), GCBlockNames.OXYGEN_COMPRESSOR);
        register(r, TileEntityType.Builder.create(TileEntityOxygenDecompressor::new, OXYGEN_DECOMPRESSOR).build(null), GCBlockNames.OXYGEN_DECOMPRESSOR);
        register(r, TileEntityType.Builder.create(TileEntityFuelLoader::new, FUEL_LOADER).build(null), GCBlockNames.FUEL_LOADER);
        register(r, TileEntityType.Builder.create(TileEntityLandingPadSingle::new, ROCKET_LAUNCH_PAD).build(null), GCBlockNames.ROCKET_LAUNCH_PAD);
        register(r, TileEntityType.Builder.create(TileEntityLandingPad::new, FULL_ROCKET_LAUNCH_PAD).build(null), GCBlockNames.FULL_ROCKET_LAUNCH_PAD);
        register(r, TileEntityType.Builder.create(TileEntitySpaceStationBase::new, SPACE_STATION_BASE).build(null), GCBlockNames.SPACE_STATION_BASE);
        register(r, TileEntityType.Builder.create(TileEntityFake::new, MULTI_BLOCK).build(null), GCBlockNames.MULTI_BLOCK);
        register(r, TileEntityType.Builder.create(TileEntityOxygenSealer::new, OXYGEN_SEALER).build(null), GCBlockNames.OXYGEN_SEALER);
        register(r, TileEntityType.Builder.create(TileEntityDungeonSpawner::new, MOON_BOSS_SPAWNER).build(null), GCBlockNames.MOON_BOSS_SPAWNER);
        register(r, TileEntityType.Builder.create(TileEntityOxygenDetector::new, OXYGEN_DETECTOR).build(null), GCBlockNames.OXYGEN_DETECTOR);
        register(r, TileEntityType.Builder.create(TileEntityBuggyFueler::new, FULL_BUGGY_FUELING_PAD).build(null), GCBlockNames.FULL_BUGGY_FUELING_PAD);
        register(r, TileEntityType.Builder.create(TileEntityBuggyFuelerSingle::new, BUGGY_FUELING_PAD).build(null), GCBlockNames.BUGGY_FUELING_PAD);
        register(r, TileEntityType.Builder.create(TileEntityCargoLoader::new, CARGO_LOADER).build(null), GCBlockNames.CARGO_LOADER);
        register(r, TileEntityType.Builder.create(TileEntityCargoUnloader::new, CARGO_UNLOADER).build(null), GCBlockNames.CARGO_UNLOADER);
        register(r, TileEntityType.Builder.create(TileEntityParaChest::new, PARACHEST).build(null), GCBlockNames.PARACHEST);
        register(r, TileEntityType.Builder.create(TileEntitySolar.TileEntitySolarT1::new, BASIC_SOLAR_PANEL).build(null), GCBlockNames.BASIC_SOLAR_PANEL);
        register(r, TileEntityType.Builder.create(TileEntitySolar.TileEntitySolarT2::new, ADVANCED_SOLAR_PANEL).build(null), GCBlockNames.ADVANCED_SOLAR_PANEL);
//        register(r, TileEntityType.Builder.create(TileEntityDish::new, radioTelescope).build(null), BlockNames.radioTelescope);
        register(r, TileEntityType.Builder.create(TileEntityCrafting::new, MAGNETIC_CRAFTING_TABLE).build(null), GCBlockNames.MAGNETIC_CRAFTING_TABLE);
        register(r, TileEntityType.Builder.create(TileEntityEnergyStorageModule.TileEntityEnergyStorageModuleT1::new, ENERGY_STORAGE).build(null), GCBlockNames.ENERGY_STORAGE);
        register(r, TileEntityType.Builder.create(TileEntityEnergyStorageModule.TileEntityEnergyStorageModuleT2::new, ENERGY_STORAGE_CLUSTER).build(null), GCBlockNames.ENERGY_STORAGE_CLUSTER);
        register(r, TileEntityType.Builder.create(TileEntityCoalGenerator::new, COAL_GENERATOR).build(null), GCBlockNames.COAL_GENERATOR);
        register(r, TileEntityType.Builder.create(TileEntityElectricFurnace.TileEntityElectricFurnaceT1::new, ELECTRIC_FURNACE).build(null), GCBlockNames.ELECTRIC_FURNACE);
        register(r, TileEntityType.Builder.create(TileEntityElectricFurnace.TileEntityElectricFurnaceT2::new, ELECTRIC_ARC_FURNACE).build(null), GCBlockNames.ELECTRIC_ARC_FURNACE);
        register(r, TileEntityType.Builder.create(TileEntityAluminumWire.TileEntityAluminumWireT1::new, ALUMINUM_WIRE).build(null), GCBlockNames.ALUMINUM_WIRE);
        register(r, TileEntityType.Builder.create(TileEntityAluminumWire.TileEntityAluminumWireT2::new, HEAVY_ALUMINUM_WIRE).build(null), GCBlockNames.HEAVY_ALUMINUM_WIRE);
        register(r, TileEntityType.Builder.create(TileEntityAluminumWireSwitch.TileEntityAluminumWireSwitchableT1::new, SWITCHABLE_ALUMINUM_WIRE).build(null), GCBlockNames.SWITCHABLE_ALUMINUM_WIRE);
        register(r, TileEntityType.Builder.create(TileEntityAluminumWireSwitch.TileEntityAluminumWireSwitchableT2::new, SWITCHABLE_HEAVY_ALUMINUM_WIRE).build(null), GCBlockNames.SWITCHABLE_HEAVY_ALUMINUM_WIRE);
//        register(r, TileEntityType.Builder.create(TileEntityAluminumWireSwitch::new, "GC Switchable Aluminum Wire").build(null));
        register(r, TileEntityType.Builder.create(TileEntityFallenMeteor::new, FALLEN_METEOR).build(null), GCBlockNames.FALLEN_METEOR);
        register(r, TileEntityType.Builder.create(TileEntityIngotCompressor::new, COMPRESSOR).build(null), GCBlockNames.COMPRESSOR);
        register(r, TileEntityType.Builder.create(TileEntityElectricIngotCompressor.TileEntityElectricIngotCompressorT1::new, ELECTRIC_COMPRESSOR).build(null), GCBlockNames.ELECTRIC_COMPRESSOR);
        register(r, TileEntityType.Builder.create(TileEntityElectricIngotCompressor.TileEntityElectricIngotCompressorT2::new, ADVANCED_COMPRESSOR).build(null), GCBlockNames.ADVANCED_COMPRESSOR);
        register(r, TileEntityType.Builder.create(TileEntityCircuitFabricator::new, CIRCUIT_FABRICATOR).build(null), GCBlockNames.CIRCUIT_FABRICATOR);
        register(r, TileEntityType.Builder.create(TileEntityAirLockController::new, AIR_LOCK_CONTROLLER).build(null), GCBlockNames.AIR_LOCK_CONTROLLER);
        register(r, TileEntityType.Builder.create(TileEntityOxygenStorageModule::new, OXYGEN_STORAGE_MODULE).build(null), GCBlockNames.OXYGEN_STORAGE_MODULE);
//        register(r, TileEntityType.Builder.create(TileEntityThruster::new, spinThruster).build(null), BlockNames.spinThruster);
        register(r, TileEntityType.Builder.create(TileEntityArclamp::new, ARC_LAMP).build(null), GCBlockNames.ARC_LAMP);
        register(r, TileEntityType.Builder.create(TileEntityScreen::new, DISPLAY_SCREEN).build(null), GCBlockNames.DISPLAY_SCREEN);
//        register(r, TileEntityType.Builder.create(TileEntityPanelLight::new, panelLighting).build(null), BlockNames.panelLighting);
        register(r, TileEntityType.Builder.create(TileEntityTelemetry::new, TELEMETRY_UNIT).build(null), GCBlockNames.TELEMETRY_UNIT);
        register(r, TileEntityType.Builder.create(TileEntityPainter::new, CHROMATIC_APPLICATOR).build(null), GCBlockNames.CHROMATIC_APPLICATOR);
        register(r, TileEntityType.Builder.create(TileEntityFluidTank::new, FLUID_TANK).build(null), GCBlockNames.FLUID_TANK);
        register(r, TileEntityType.Builder.create(TileEntityPlayerDetector::new, PLAYER_DETECTOR).build(null), GCBlockNames.PLAYER_DETECTOR);
        register(r, TileEntityType.Builder.create(TileEntityPlatform::new, HYDRAULIC_PLATFORM).build(null), GCBlockNames.HYDRAULIC_PLATFORM);
        register(r, TileEntityType.Builder.create(TileEntityEmergencyBox::new, EMERGENCY_POST, EMERGENCY_POST_KIT).build(null), GCBlockNames.EMERGENCY_POST);
//        register(r, TileEntityType.Builder.create(TileEntityNull::new, "GC Null Tile").build(null));
    }
}
