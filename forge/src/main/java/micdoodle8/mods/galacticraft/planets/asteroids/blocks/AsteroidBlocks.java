package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.*;
import micdoodle8.mods.galacticraft.core.blocks.DecoBlock;
import micdoodle8.mods.galacticraft.core.blocks.SlabBlockGC;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_PLANETS)
public class AsteroidBlocks
{
    @ObjectHolder(AsteroidBlockNames.WALKWAY)
    public static Block WALKWAY;
    @ObjectHolder(AsteroidBlockNames.FLUID_PIPE_WALKWAY)
    public static Block FLUID_PIPE_WALKWAY;
    @ObjectHolder(AsteroidBlockNames.WIRE_WALKWAY)
    public static Block WIRE_WALKWAY;
    @ObjectHolder(AsteroidBlockNames.ENERGY_BEAM_REFLECTOR)
    public static Block ENERGY_BEAM_REFLECTOR;
    @ObjectHolder(AsteroidBlockNames.ENERGY_BEAM_RECEIVER)
    public static Block ENERGY_BEAM_RECEIVER;
    @ObjectHolder(AsteroidBlockNames.SHORT_RANGE_TELEPAD)
    public static Block SHORT_RANGE_TELEPAD;
    @ObjectHolder(AsteroidBlockNames.SHORT_RANGE_TELEPAD_DUMMY)
    public static Block SHORT_RANGE_TELEPAD_DUMMY;
    @ObjectHolder(AsteroidBlockNames.DENSE_ICE)
    public static Block DENSE_ICE;
    @ObjectHolder(AsteroidBlockNames.ASTRO_MINER_BASE)
    public static Block ASTRO_MINER_BASE;
    @ObjectHolder(AsteroidBlockNames.FULL_ASTRO_MINER_BASE)
    public static Block FULL_ASTRO_MINER_BASE;
    @ObjectHolder(AsteroidBlockNames.DARK_ASTEROID_ROCK)
    public static Block DARK_ASTEROID_ROCK;
    @ObjectHolder(AsteroidBlockNames.GRAY_ASTEROID_ROCK)
    public static Block GRAY_ASTEROID_ROCK;
    @ObjectHolder(AsteroidBlockNames.LIGHT_GRAY_ASTEROID_ROCK)
    public static Block LIGHT_GRAY_ASTEROID_ROCK;
    @ObjectHolder(AsteroidBlockNames.ASTEROID_ALUMINUM_ORE)
    public static Block ASTEROID_ALUMINUM_ORE;
    @ObjectHolder(AsteroidBlockNames.ILMENITE_ORE)
    public static Block ILMENITE_ORE;
    @ObjectHolder(AsteroidBlockNames.ASTEROID_IRON_ORE)
    public static Block ASTEROID_IRON_ORE;
    @ObjectHolder(AsteroidBlockNames.DARK_DECORATION_BLOCK)
    public static Block DARK_DECORATION_BLOCK;
    @ObjectHolder(AsteroidBlockNames.TITANIUM_BLOCK)
    public static Block TITANIUM_BLOCK;
    @ObjectHolder(AsteroidBlockNames.DARK_DECORATION_SLAB)
    public static Block DARK_DECORATION_SLAB;

    public static void registerBlocks(RegistryEvent.Register<Block> evt)
    {
        IForgeRegistry<Block> r = evt.getRegistry();

        Block.Properties builder = Block.Properties.of(Material.METAL).strength(1.0F).sound(SoundType.METAL).noOcclusion();
        register(r, new BlockWalkway(builder), AsteroidBlockNames.WALKWAY);
        register(r, new BlockWalkway(builder), AsteroidBlockNames.FLUID_PIPE_WALKWAY);
        register(r, new BlockWalkway(builder), AsteroidBlockNames.WIRE_WALKWAY);

        builder = Block.Properties.of(Material.STONE).strength(3.0F);
        register(r, new BlockAsteroidRock(builder), AsteroidBlockNames.DARK_ASTEROID_ROCK);
        register(r, new BlockAsteroidRock(builder), AsteroidBlockNames.GRAY_ASTEROID_ROCK);
        register(r, new BlockAsteroidRock(builder), AsteroidBlockNames.LIGHT_GRAY_ASTEROID_ROCK);
        register(r, new OreBlockAsteroids(builder), AsteroidBlockNames.ASTEROID_ALUMINUM_ORE);
        register(r, new OreBlockAsteroids(builder), AsteroidBlockNames.ILMENITE_ORE);
        register(r, new OreBlockAsteroids(builder), AsteroidBlockNames.ASTEROID_IRON_ORE);
        register(r, new DecoBlock(builder), AsteroidBlockNames.DARK_DECORATION_BLOCK);
        register(r, new DecoBlock(builder), AsteroidBlockNames.TITANIUM_BLOCK);
        register(r, new SlabBlockGC(builder), AsteroidBlockNames.DARK_DECORATION_SLAB);

        builder = Block.Properties.of(Material.METAL).sound(SoundType.METAL);
        register(r, new BlockBeamReflector(builder), AsteroidBlockNames.ENERGY_BEAM_REFLECTOR);
        register(r, new BlockBeamReceiver(builder), AsteroidBlockNames.ENERGY_BEAM_RECEIVER);

        builder = Block.Properties.of(Material.METAL).sound(SoundType.METAL).strength(3.0F);
        register(r, new BlockShortRangeTelepad(builder), AsteroidBlockNames.SHORT_RANGE_TELEPAD);

        builder = Block.Properties.of(Material.METAL).sound(SoundType.METAL).strength(100000.0F).noDrops();
        register(r, new BlockTelepadFake(builder), AsteroidBlockNames.SHORT_RANGE_TELEPAD_DUMMY);

        builder = Block.Properties.of(Material.ICE).sound(SoundType.GLASS).strength(0.5F).friction(0.98F).noOcclusion();
        register(r, new BlockIceAsteroids(builder), AsteroidBlockNames.DENSE_ICE);

        builder = Block.Properties.of(Material.STONE).strength(3.0F).sound(SoundType.METAL).noOcclusion();
        register(r, new BlockMinerBase(builder), AsteroidBlockNames.ASTRO_MINER_BASE);
        builder = builder.strength(3.0F, 35.0F);
        register(r, new BlockMinerBaseFull(builder), AsteroidBlockNames.FULL_ASTRO_MINER_BASE);
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
    {
        GCBlocks.register(reg, thing, new ResourceLocation(Constants.MOD_ID_PLANETS, name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, ResourceLocation name, IForgeRegistryEntry<V> thing)
    {
        if (Registry.BLOCK.getOptional(name).get() instanceof IShiftDescription && !(thing instanceof ItemBlockDesc)) {
            System.err.println("IShiftDescription block needs ItemBlockDesc registration!");
        }
        GCBlocks.register(reg, name, thing);
    }

    public static void registerItemBlocks(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        Item.Properties props = GCItems.defaultBuilder().tab(GalacticraftCore.galacticraftBlocksTab);
        register(r, Registry.BLOCK.getKey(WALKWAY), new ItemBlockDesc(WALKWAY, props));
        register(r, Registry.BLOCK.getKey(FLUID_PIPE_WALKWAY), new ItemBlockDesc(FLUID_PIPE_WALKWAY, props));
        register(r, Registry.BLOCK.getKey(WIRE_WALKWAY), new ItemBlockDesc(WIRE_WALKWAY, props));
        register(r, Registry.BLOCK.getKey(DARK_ASTEROID_ROCK), new BlockItem(DARK_ASTEROID_ROCK, props));
        register(r, Registry.BLOCK.getKey(GRAY_ASTEROID_ROCK), new BlockItem(GRAY_ASTEROID_ROCK, props));
        register(r, Registry.BLOCK.getKey(LIGHT_GRAY_ASTEROID_ROCK), new BlockItem(LIGHT_GRAY_ASTEROID_ROCK, props));
        register(r, Registry.BLOCK.getKey(ASTEROID_ALUMINUM_ORE), new BlockItem(ASTEROID_ALUMINUM_ORE, props));
        register(r, Registry.BLOCK.getKey(ILMENITE_ORE), new BlockItem(ILMENITE_ORE, props));
        register(r, Registry.BLOCK.getKey(ASTEROID_IRON_ORE), new BlockItem(ASTEROID_IRON_ORE, props));
        register(r, Registry.BLOCK.getKey(DARK_DECORATION_BLOCK), new BlockItem(DARK_DECORATION_BLOCK, props));
        register(r, Registry.BLOCK.getKey(TITANIUM_BLOCK), new BlockItem(TITANIUM_BLOCK, props));
        register(r, Registry.BLOCK.getKey(ENERGY_BEAM_REFLECTOR), new ItemBlockDesc(ENERGY_BEAM_REFLECTOR, props));
        register(r, Registry.BLOCK.getKey(ENERGY_BEAM_RECEIVER), new ItemBlockDesc(ENERGY_BEAM_RECEIVER, props));
        register(r, Registry.BLOCK.getKey(SHORT_RANGE_TELEPAD), new ItemBlockDesc(SHORT_RANGE_TELEPAD, props));
        register(r, Registry.BLOCK.getKey(DENSE_ICE), new BlockItem(DENSE_ICE, props));
        register(r, Registry.BLOCK.getKey(ASTRO_MINER_BASE), new ItemBlockDesc(ASTRO_MINER_BASE, props));
        register(r, Registry.BLOCK.getKey(DARK_DECORATION_SLAB), new ItemBlockDesc(DARK_DECORATION_SLAB, props));
    }

//    public static void registerBlocks()
//    {
//        registerBlock(AsteroidBlocks.blockBasic, ItemBlockAsteroids.class);
//        registerBlock(AsteroidBlocks.blockWalkway, ItemBlockWalkway.class);
//        registerBlock(AsteroidBlocks.beamReflector, ItemBlockDesc.class);
//        registerBlock(AsteroidBlocks.beamReceiver, ItemBlockDesc.class);
//        registerBlock(AsteroidBlocks.shortRangeTelepad, ItemBlockShortRangeTelepad.class);
//        registerBlock(AsteroidBlocks.fakeTelepad, null);
//        registerBlock(AsteroidBlocks.blockDenseIce, ItemBlockGC.class);
//        registerBlock(AsteroidBlocks.blockMinerBase, ItemBlockDesc.class);
//        registerBlock(AsteroidBlocks.minerBaseFull, null);
//        registerBlock(AsteroidBlocks.spaceWart, null);
//    }

//    public static void setHarvestLevels()
//    {
//        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 0, 0);   //Rock
//        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 0, 1);   //Rock
//        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 0, 2);   //Rock
//        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 2, 3);   //Aluminium
//        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 3, 4);   //Ilmenite
//        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 2, 5);   //Iron
//    }
//
//    public static void oreDictRegistration()
//    {
//        OreDictionary.registerOre("oreAluminum", new ItemStack(AsteroidBlocks.blockBasic, 1, 3));
//        OreDictionary.registerOre("oreAluminium", new ItemStack(AsteroidBlocks.blockBasic, 1, 3));
//        OreDictionary.registerOre("oreNaturalAluminum", new ItemStack(AsteroidBlocks.blockBasic, 1, 3));
//        OreDictionary.registerOre("oreIlmenite", new ItemStack(AsteroidBlocks.blockBasic, 1, 4));
//        OreDictionary.registerOre("oreIron", new ItemStack(AsteroidBlocks.blockBasic, 1, 5));
//
//        OreDictionary.registerOre("blockTitanium", new ItemStack(AsteroidBlocks.blockBasic, 1, 7));
//    }

    @SubscribeEvent
    public static void initTileEntities(RegistryEvent.Register<BlockEntityType<?>> evt)
    {
        IForgeRegistry<BlockEntityType<?>> r = evt.getRegistry();

        register(r, BlockEntityType.Builder.of(TileEntityBeamReceiver::new, ENERGY_BEAM_RECEIVER).build(null), AsteroidBlockNames.ENERGY_BEAM_RECEIVER);
        register(r, BlockEntityType.Builder.of(TileEntityBeamReflector::new, ENERGY_BEAM_REFLECTOR).build(null), AsteroidBlockNames.ENERGY_BEAM_REFLECTOR);
        register(r, BlockEntityType.Builder.of(TileEntityMinerBaseSingle::new, ASTRO_MINER_BASE).build(null), AsteroidBlockNames.ASTRO_MINER_BASE);
        register(r, BlockEntityType.Builder.of(TileEntityMinerBase::new, FULL_ASTRO_MINER_BASE).build(null), AsteroidBlockNames.FULL_ASTRO_MINER_BASE);
        register(r, BlockEntityType.Builder.of(TileEntityShortRangeTelepad::new, SHORT_RANGE_TELEPAD).build(null), AsteroidBlockNames.SHORT_RANGE_TELEPAD);
        register(r, BlockEntityType.Builder.of(TileEntityTelepadFake::new, SHORT_RANGE_TELEPAD_DUMMY).build(null), AsteroidBlockNames.SHORT_RANGE_TELEPAD_DUMMY);
        register(r, BlockEntityType.Builder.of(WalkwayFluidPipeTileEntity::new, FLUID_PIPE_WALKWAY).build(null), GCBlockNames.FLUID_PIPE);
    }
}