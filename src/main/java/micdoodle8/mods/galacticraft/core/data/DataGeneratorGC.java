package micdoodle8.mods.galacticraft.core.data;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneratorGC
{
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if (event.includeClient())
        {
            generator.addProvider(new BlockStates(generator, Constants.MOD_ID_CORE, helper));
            generator.addProvider(new ItemModels(generator, Constants.MOD_ID_CORE, helper));
            generator.addProvider(new Language(generator, Constants.MOD_ID_CORE));
        }
        //        if (event.includeServer())
        //        {
        //            BlockTagsProvider blockTagProvider = new BlockTagsBuilder(generator, MineconLiveMod.MOD_ID, helper);
        //            generator.addProvider(blockTagProvider);
        //            generator.addProvider(new ItemTagsBuilder(generator, blockTagProvider, MineconLiveMod.MOD_ID, helper));
        //            generator.addProvider(new FluidTagsBuilder(generator, MineconLiveMod.MOD_ID, helper));
        //            generator.addProvider(new EntityTypeTagsBuilder(generator, MineconLiveMod.MOD_ID, helper));
        //            generator.addProvider(new Recipe(generator, MineconLiveMod.MOD_ID));
        //            generator.addProvider(new LootTables(generator));
        //        }
    }

    public static class BlockStates extends BlockStateProvider
    {
        public BlockStates(DataGenerator generator, String modid, ExistingFileHelper helper)
        {
            super(generator, modid, helper);
        }

        @Override
        protected void registerStatesAndModels()
        {
            ResourceLocation tinDecor = this.modLoc("block/tin_decoration_block");
            ResourceLocation machineBase = this.modLoc("block/machine_base");
            ResourceLocation machineSide = this.modLoc("block/machine_side");
            ResourceLocation machineInput = this.modLoc("block/machine_input");
            ResourceLocation machineOutput = this.modLoc("block/machine_output");

            this.simpleBlock(GCBlocks.COPPER_ORE);
            this.simpleBlock(GCBlocks.TIN_ORE);
            this.simpleBlock(GCBlocks.ALUMINUM_ORE);
            this.simpleBlock(GCBlocks.SILICON_ORE);
            this.simpleBlock(GCBlocks.MOON_COPPER_ORE);
            this.simpleBlock(GCBlocks.MOON_TIN_ORE);
            this.simpleBlock(GCBlocks.CHEESE_ORE);
            this.simpleBlock(GCBlocks.SAPPHIRE_ORE);
            this.simpleBlock(GCBlocks.COPPER_BLOCK);
            this.simpleBlock(GCBlocks.TIN_BLOCK);
            this.simpleBlock(GCBlocks.ALUMINUM_BLOCK);
            this.simpleBlock(GCBlocks.SILICON_BLOCK);
            this.simpleBlock(GCBlocks.METEORIC_IRON_BLOCK);
            this.simpleBlock(GCBlocks.MOON_ROCK);
            this.simpleBlock(GCBlocks.MOON_DIRT);
            this.simpleBlock(GCBlocks.MOON_DUNGEON_BRICKS);
            this.simpleBlock(GCBlocks.EMERGENCY_POST, this.models().getExistingFile(this.modLoc("block/emergency_post")));
            this.simpleBlock(GCBlocks.EMERGENCY_POST_KIT, this.models().getExistingFile(this.modLoc("block/emergency_post_kit")));
            this.simpleBlock(GCBlocks.FALLEN_METEOR, this.models().getExistingFile(this.modLoc("block/fallen_meteor")));
            this.simpleBlock(GCBlocks.AIR_LOCK_FRAME);
            this.simpleBlock(GCBlocks.TIER_1_TREASURE_CHEST);
            this.simpleBlock(GCBlocks.TIN_DECORATION_BLOCK_1, this.models().cubeAll("tin_decoration_block_1", tinDecor));
            this.simpleBlock(GCBlocks.OXYGEN_DETECTOR, this.models().cubeColumn("oxygen_detector", this.modLoc("block/oxygen_detector"), machineBase));
            this.simpleBlock(GCBlocks.TELEMETRY_UNIT);
            this.simpleBlock(GCBlocks.BREATHEABLE_AIR, this.models().getExistingFile(this.mcLoc("block/barrier")));
            this.simpleBlock(GCBlocks.BRIGHT_AIR, this.models().getExistingFile(this.mcLoc("block/barrier")));
            this.simpleBlock(GCBlocks.BRIGHT_BREATHEABLE_AIR, this.models().getExistingFile(this.mcLoc("block/barrier")));
            this.simpleBlock(GCBlocks.MOON_BOSS_SPAWNER, this.models().getExistingFile(this.mcLoc("block/barrier")));
            this.simpleBlock(GCBlocks.BUGGY_FUELING_PAD, this.models().getExistingFile(this.modLoc("block/buggy_fueling_pad")));
            this.simpleBlock(GCBlocks.ROCKET_LAUNCH_PAD, this.models().getExistingFile(this.modLoc("block/rocket_launch_pad")));
            this.simpleBlock(GCBlocks.ROCKET_WORKBENCH, this.models().getExistingFile(this.modLoc("block/rocket_workbench")));

            ModelFile model = this.models().cubeBottomTop("air_lock_controller", this.modLoc("block/air_lock_controller"), this.modLoc("block/air_lock_frame"), this.modLoc("block/air_lock_frame"));
            this.simpleBlock(GCBlocks.AIR_LOCK_CONTROLLER, model);

            model = this.models().cube("oxygen_bubble_distributor", machineBase, machineBase, this.modLoc("block/oxygen_bubble_distributor"), this.modLoc("block/oxygen_bubble_distributor"), machineInput, this.modLoc("block/machine_oxygen_input")).texture("particle", this.modLoc("block/oxygen_bubble_distributor"));
            this.horizontalBlock(GCBlocks.OXYGEN_BUBBLE_DISTRIBUTOR, model);

            model = this.models().cube("oxygen_collector", this.modLoc("block/oxygen_collector"), this.modLoc("block/oxygen_collector"), this.modLoc("block/oxygen_collector"), this.modLoc("block/oxygen_collector"), machineInput, this.modLoc("block/machine_oxygen_output")).texture("particle", this.modLoc("block/oxygen_collector"));
            this.horizontalBlock(GCBlocks.OXYGEN_COLLECTOR, model);

            model = this.models().cubeBottomTop("tin_decoration_block_2", this.modLoc("block/tin_decoration_block_side"), tinDecor, this.modLoc("block/tin_decoration_block_top"));
            this.simpleBlock(GCBlocks.TIN_DECORATION_BLOCK_2, model);

            model = this.models().cubeBottomTop("space_station_base", this.modLoc("block/space_station_base_side"), this.modLoc("block/space_station_base_side"), this.modLoc("block/space_station_base_top"));
            this.simpleBlock(GCBlocks.SPACE_STATION_BASE, model);

            model = this.models().orientable("compressor", machineSide, this.modLoc("block/compressor"), machineBase).texture("particle", this.modLoc("block/compressor"));
            this.horizontalBlock(GCBlocks.COMPRESSOR, model);

            //model = this.models().cube("electric_compressor", machineBase, machineBase, this.modLoc("block/electric_compressor"), machineSide, machineInput, machineSide).texture("particle", this.modLoc("block/electric_compressor"));
            //this.horizontalBlock(GCBlocks.ELECTRIC_COMPRESSOR, model);

            model = this.models().cube("coal_generator", machineBase, machineBase, this.modLoc("block/coal_generator"), machineSide, machineOutput, machineSide).texture("particle", this.modLoc("block/coal_generator"));
            this.horizontalBlock(GCBlocks.COAL_GENERATOR, model);

            model = this.models().cube("chromatic_applicator", this.modLoc("block/chromatic_applicator"), this.modLoc("block/chromatic_applicator"), this.modLoc("block/chromatic_applicator"), this.modLoc("block/chromatic_applicator"), machineSide, machineSide).texture("particle", this.modLoc("block/chromatic_applicator"));
            this.horizontalBlock(GCBlocks.CHROMATIC_APPLICATOR, model);

            model = this.models().orientableWithBottom("basic_solar_panel", this.modLoc("block/basic_solar_panel_side"), machineOutput, machineBase, this.modLoc("block/basic_solar_panel_top")).texture("particle", this.modLoc("block/basic_solar_panel_side"));
            this.horizontalBlock(GCBlocks.BASIC_SOLAR_PANEL, model);
            model = this.models().orientableWithBottom("advanced_solar_panel", this.modLoc("block/advanced_solar_panel_side"), machineOutput, machineBase, this.modLoc("block/advanced_solar_panel_top")).texture("particle", this.modLoc("block/advanced_solar_panel_side"));
            this.horizontalBlock(GCBlocks.ADVANCED_SOLAR_PANEL, model);

            model = this.models().cube("oxygen_compressor", machineBase, machineBase, this.modLoc("block/oxygen_compressor_back"), this.modLoc("block/oxygen_compressor"), machineInput, this.modLoc("block/machine_oxygen_input")).texture("particle", this.modLoc("block/oxygen_compressor"));
            this.horizontalBlock(GCBlocks.OXYGEN_COMPRESSOR, model);
            model = this.models().cube("oxygen_decompressor", machineBase, machineBase, this.modLoc("block/oxygen_compressor_back"), this.modLoc("block/oxygen_decompressor"), machineInput, this.modLoc("block/machine_oxygen_output")).texture("particle", this.modLoc("block/oxygen_decompressor"));
            this.horizontalBlock(GCBlocks.OXYGEN_DECOMPRESSOR, model);

            model = this.models().cube("oxygen_sealer", machineBase, this.modLoc("block/oxygen_sealer"), machineBase, machineBase, machineInput, this.modLoc("block/machine_oxygen_input")).texture("particle", this.modLoc("block/oxygen_sealer"));
            this.horizontalBlock(GCBlocks.OXYGEN_SEALER, model);

            model = this.models().cube("refinery", machineBase, this.modLoc("block/refinery_top"), this.modLoc("block/refinery_front"), this.modLoc("block/refinery_back"), this.modLoc("block/machine_oil_input"), this.modLoc("block/machine_fuel_input")).texture("particle", this.modLoc("block/refinery_front"));
            this.horizontalBlock(GCBlocks.REFINERY, model);

            model = this.models().cube("cargo_loader", machineBase, machineBase, this.modLoc("block/cargo_loader"), this.modLoc("block/cargo_loader"), machineInput, this.modLoc("block/machine_item_input")).texture("particle", this.modLoc("block/cargo_loader"));
            this.horizontalBlock(GCBlocks.CARGO_LOADER, model);
            model = this.models().cube("cargo_unloader", machineBase, machineBase, this.modLoc("block/cargo_unloader"), this.modLoc("block/cargo_unloader"), machineInput, this.modLoc("block/machine_item_output")).texture("particle", this.modLoc("block/cargo_unloader"));
            this.horizontalBlock(GCBlocks.CARGO_UNLOADER, model);

            model = this.models().cubeAll("player_detector", tinDecor);
            this.simpleBlock(GCBlocks.PLAYER_DETECTOR, model);
            model = this.models().cubeAll("hidden_redstone_wire", tinDecor);
            this.simpleBlock(GCBlocks.HIDDEN_REDSTONE_WIRE, model);
            model = this.models().cubeAll("hidden_redstone_repeater", tinDecor);
            this.simpleBlock(GCBlocks.HIDDEN_REDSTONE_REPEATER, model);

            model = this.models().cubeBottomTop("magnetic_crafting_table", this.modLoc("block/magnetic_crafting_table_side"), this.modLoc("block/magnetic_crafting_table_bottom"), this.modLoc("block/magnetic_crafting_table_top"));
            this.directionalBlock(GCBlocks.MAGNETIC_CRAFTING_TABLE, model);

            this.simpleBlock(GCBlocks.GLOWSTONE_TORCH, this.models().torch("glowstone_torch", this.modLoc("block/glowstone_torch")));
            this.horizontalBlock(GCBlocks.WALL_GLOWSTONE_TORCH, this.models().torchWall("wall_glowstone_torch", this.modLoc("block/glowstone_torch")), 90);

            this.simpleBlock(GCBlocks.UNLIT_TORCH, this.models().torch("unlit_torch", this.modLoc("block/unlit_torch")));
            this.horizontalBlock(GCBlocks.WALL_UNLIT_TORCH, this.models().torchWall("wall_unlit_torch", this.modLoc("block/unlit_torch")), 90);

            this.simpleBlock(GCBlocks.LIT_UNLIT_TORCH, this.models().torch("lit_unlit_torch", this.modLoc("block/lit_unlit_torch")));
            this.horizontalBlock(GCBlocks.WALL_LIT_UNLIT_TORCH, this.models().torchWall("wall_lit_unlit_torch", this.modLoc("block/lit_unlit_torch")), 90);

            model = this.models().cubeBottomTop("moon_turf", this.modLoc("block/moon_turf_side"), this.modLoc("block/moon_dirt"), this.modLoc("block/moon_turf_top"));
            this.simpleBlock(GCBlocks.MOON_TURF, model);

            this.simpleFluid(GCFluids.OIL.getBlock());
            this.simpleFluid(GCFluids.FUEL.getBlock());
            this.simpleFluid(GCFluids.OXYGEN.getBlock());
            this.simpleFluid(GCFluids.HYDROGEN.getBlock());
        }

        protected void simpleFluid(FlowingFluidBlock block)
        {
            this.getVariantBuilder(block).partialState().setModels(new ConfiguredModel(this.models().getBuilder(this.toString(block)).texture("particle", this.modLoc(block.getFluid().getAttributes().getStillTexture().getPath()))));
        }

        protected String toString(Block block)
        {
            return block.getRegistryName().getPath();
        }
    }

    public static class ItemModels extends ItemModelProvider
    {
        public ItemModels(DataGenerator generator, String modid, ExistingFileHelper helper)
        {
            super(generator, modid, helper);
        }

        @Override
        protected void registerModels()
        {
            this.parentedBlock(GCBlocks.COPPER_ORE);
            this.parentedBlock(GCBlocks.TIN_ORE);
            this.parentedBlock(GCBlocks.ALUMINUM_ORE);
            this.parentedBlock(GCBlocks.SILICON_ORE);
            this.parentedBlock(GCBlocks.MOON_COPPER_ORE);
            this.parentedBlock(GCBlocks.MOON_TIN_ORE);
            this.parentedBlock(GCBlocks.CHEESE_ORE);
            this.parentedBlock(GCBlocks.SAPPHIRE_ORE);
            this.parentedBlock(GCBlocks.COPPER_BLOCK);
            this.parentedBlock(GCBlocks.TIN_BLOCK);
            this.parentedBlock(GCBlocks.ALUMINUM_BLOCK);
            this.parentedBlock(GCBlocks.SILICON_BLOCK);
            this.parentedBlock(GCBlocks.METEORIC_IRON_BLOCK);
            this.parentedBlock(GCBlocks.MAGNETIC_CRAFTING_TABLE);
            this.parentedBlock(GCBlocks.TIER_1_TREASURE_CHEST, this.mcLoc("item/chest")).texture("particle", this.modLoc("block/tier_1_treasure_chest"));
            this.parentedBlock(GCBlocks.PARACHEST, this.mcLoc("item/chest")).texture("particle", this.modLoc("block/parachest"));
            this.parentedBlock(GCBlocks.PLAYER_DETECTOR);
            this.parentedBlock(GCBlocks.HIDDEN_REDSTONE_WIRE);
            this.parentedBlock(GCBlocks.HIDDEN_REDSTONE_REPEATER);
            this.parentedBlock(GCBlocks.EMERGENCY_POST);
            this.parentedBlock(GCBlocks.EMERGENCY_POST_KIT);
            this.parentedBlock(GCBlocks.MOON_TURF);
            this.parentedBlock(GCBlocks.MOON_ROCK);
            this.parentedBlock(GCBlocks.MOON_DIRT);
            this.parentedBlock(GCBlocks.MOON_DUNGEON_BRICKS);
            this.parentedBlock(GCBlocks.AIR_LOCK_FRAME);
            this.parentedBlock(GCBlocks.TIN_DECORATION_BLOCK_1);
            this.parentedBlock(GCBlocks.TIN_DECORATION_BLOCK_2);
            this.parentedBlock(GCBlocks.OXYGEN_DETECTOR);
            this.parentedBlock(GCBlocks.TELEMETRY_UNIT);
            this.parentedBlock(GCBlocks.FALLEN_METEOR);
            this.parentedBlock(GCBlocks.ROCKET_LAUNCH_PAD);
            this.parentedBlock(GCBlocks.BUGGY_FUELING_PAD);
            this.parentedBlock(GCBlocks.OXYGEN_BUBBLE_DISTRIBUTOR);
            this.parentedBlock(GCBlocks.OXYGEN_COLLECTOR);
            this.parentedBlock(GCBlocks.COMPRESSOR);
            this.parentedBlock(GCBlocks.COAL_GENERATOR);
            this.parentedBlock(GCBlocks.CHROMATIC_APPLICATOR);
            this.parentedBlock(GCBlocks.BASIC_SOLAR_PANEL);
            this.parentedBlock(GCBlocks.ADVANCED_SOLAR_PANEL);
            this.parentedBlock(GCBlocks.OXYGEN_COMPRESSOR);
            this.parentedBlock(GCBlocks.OXYGEN_DECOMPRESSOR);
            this.parentedBlock(GCBlocks.OXYGEN_SEALER);
            this.parentedBlock(GCBlocks.ELECTRIC_COMPRESSOR);
            this.parentedBlock(GCBlocks.ELECTRIC_FURNACE);
            this.parentedBlock(GCBlocks.REFINERY);
            this.parentedBlock(GCBlocks.CARGO_LOADER);
            this.parentedBlock(GCBlocks.CARGO_UNLOADER);
            this.parentedBlock(GCBlocks.AIR_LOCK_CONTROLLER);
            this.parentedBlock(GCBlocks.MOON_BOSS_SPAWNER, this.mcLoc("item/air"));
            this.parentedBlock(GCBlocks.ROCKET_WORKBENCH);
            this.itemGenerated(GCBlocks.GLOWSTONE_TORCH);
            this.itemGenerated(GCBlocks.UNLIT_TORCH);
            this.itemGenerated(GCBlocks.CHEESE_BLOCK.asItem());
            this.itemGenerated(GCBlocks.LIT_UNLIT_TORCH);
            this.itemGenerated(GCBlocks.ALUMINUM_WIRE.asItem());
            this.itemGenerated(GCBlocks.HEAVY_ALUMINUM_WIRE.asItem());
            this.itemGenerated(GCBlocks.SWITCHABLE_ALUMINUM_WIRE.asItem());
            this.itemGenerated(GCBlocks.SWITCHABLE_HEAVY_ALUMINUM_WIRE.asItem());
        }

        protected ItemModelBuilder parentedBlock(Block block)
        {
            return this.getBuilder(block.getRegistryName().getPath()).parent(new ModelFile.UncheckedModelFile(this.modLoc("block/" + block.getRegistryName().getPath())));
        }

        protected ItemModelBuilder parentedBlock(Block block, String model)
        {
            return this.getBuilder(block.getRegistryName().getPath()).parent(new ModelFile.UncheckedModelFile(this.modLoc(model)));
        }

        protected ItemModelBuilder parentedBlock(Block block, ResourceLocation resource)
        {
            return this.getBuilder(block.getRegistryName().getPath()).parent(new ModelFile.UncheckedModelFile(resource));
        }

        protected ItemModelBuilder parentedItem(Item item, String model)
        {
            return this.getBuilder(item.getRegistryName().getPath()).parent(new ModelFile.UncheckedModelFile(this.modLoc(model)));
        }

        protected void itemGenerated(Block block)
        {
            this.itemGenerated(block, this.itemToString(block));
        }

        protected void itemGenerated(Block block, String texture)
        {
            this.getBuilder(block.getRegistryName().getPath()).parent(this.getExistingFile(this.mcLoc("item/generated"))).texture("layer0", this.modLoc("block/" + texture));
        }

        protected void itemGenerated(Item item)
        {
            this.itemGenerated(item, this.itemToString(item));
        }

        protected void itemGenerated(Item item, String texture)
        {
            this.getBuilder(item.getRegistryName().getPath()).parent(this.getExistingFile(this.mcLoc("item/generated"))).texture("layer0", this.modLoc("item/" + texture));
        }

        protected void spawnEgg(Item item)
        {
            this.getBuilder(item.getRegistryName().getPath()).parent(this.getExistingFile(this.mcLoc("item/template_spawn_egg")));
        }

        protected String itemToString(IItemProvider base)
        {
            return base.asItem().getRegistryName().getPath();
        }
    }

    public static class Language extends LanguageProvider
    {
        public Language(DataGenerator generator, String modid)
        {
            super(generator, modid, "en_us");
        }

        @Override
        protected void addTranslations()
        {
            this.add(GCBlocks.COPPER_ORE, "Copper Ore");
            this.add(GCBlocks.TIN_ORE, "Tin Ore");
            this.add(GCBlocks.ALUMINUM_ORE, "Aluminum Ore");
            this.add(GCBlocks.SILICON_ORE, "Silicon Ore");
            this.add(GCBlocks.MOON_COPPER_ORE, "Moon Copper Ore");
            this.add(GCBlocks.MOON_TIN_ORE, "Moon Tin Ore");
            this.add(GCBlocks.CHEESE_ORE, "Cheese Ore");
            this.add(GCBlocks.SAPPHIRE_ORE, "Sapphire Ore");
            this.add(GCBlocks.COPPER_BLOCK, "Block of Copper");
            this.add(GCBlocks.TIN_BLOCK, "Block of Tin");
            this.add(GCBlocks.ALUMINUM_BLOCK, "Block of Aluminum");
            this.add(GCBlocks.SILICON_BLOCK, "Block of Silicon");
            this.add(GCBlocks.METEORIC_IRON_BLOCK, "Block of Meteoric Iron");
            this.add(GCBlocks.MAGNETIC_CRAFTING_TABLE, "Magnetic Crafting Table");
            this.add(GCBlocks.MOON_TURF, "Moon Turf");
            this.add(GCBlocks.MOON_ROCK, "Moon Rock");
            this.add(GCBlocks.MOON_DIRT, "Moon Dirt");
            this.add(GCBlocks.PLAYER_DETECTOR, "Player Detector");
            this.add(GCBlocks.HIDDEN_REDSTONE_WIRE, "Hidden Redstone Wire");
            this.add(GCBlocks.HIDDEN_REDSTONE_REPEATER, "Hidden Redstone Repeater");
            this.add(GCBlocks.MOON_DUNGEON_BRICKS, "Moon Dungeon Bricks");
            this.add(GCBlocks.GLOWSTONE_TORCH, "Glowstone Torch");
            this.add(GCBlocks.FALLEN_METEOR, "Fallen Meteor");
            this.add(GCBlocks.PARACHEST, "Parachest");
            this.add(GCBlocks.CHEESE_BLOCK, "Block of Cheese");
            this.add(GCBlocks.FLUID_TANK, "Fluid Tank");
            this.add(GCBlocks.EMERGENCY_POST, "Emergency Post");
            this.add(GCBlocks.EMERGENCY_POST_KIT, "Emergency Post (with Kit)");
            this.add(GCBlocks.ROCKET_LAUNCH_PAD, "Rocket Launch Pad");
            this.add(GCBlocks.BUGGY_FUELING_PAD, "Buggy Fueling Pad");
            this.add(GCBlocks.FLUID_PIPE, "Fluid Pipe");
            this.add(GCBlocks.PULLED_FLUID_PIPE, "Pulled Fluid Pipe");
            this.add(GCBlocks.ALUMINUM_WIRE, "Aluminum Wire");
            this.add(GCBlocks.HEAVY_ALUMINUM_WIRE, "Heavy Aluminum Wire");
            this.add(GCBlocks.SWITCHABLE_ALUMINUM_WIRE, "Switchable Aluminum Wire");
            this.add(GCBlocks.SWITCHABLE_HEAVY_ALUMINUM_WIRE, "Switchable Heavy Aluminum Wire");
            this.add(GCBlocks.ARC_LAMP, "Arc Lamp");
            this.add(GCBlocks.OXYGEN_BUBBLE_DISTRIBUTOR, "Oxygen Bubble Distributor");
            this.add(GCBlocks.OXYGEN_COLLECTOR, "Oxygen Collector");
            this.add(GCBlocks.ROCKET_WORKBENCH, "NASA Workbench");
            this.add(GCBlocks.AIR_LOCK_FRAME, "Air Lock Frame");
            this.add(GCBlocks.AIR_LOCK_CONTROLLER, "Air Lock Controller");
            this.add(GCBlocks.COMPRESSOR, "Compressor");
            this.add(GCBlocks.ELECTRIC_COMPRESSOR, "Electric Compressor");
            this.add(GCBlocks.ADVANCED_COMPRESSOR, "Advanced Compressor");
            this.add(GCBlocks.COAL_GENERATOR, "Coal Generator");
            this.add(GCBlocks.CIRCUIT_FABRICATOR, "Circuit Fabricator");
            this.add(GCBlocks.OXYGEN_STORAGE_MODULE, "Oxygen Storage Module");
            this.add(GCBlocks.DECONSTRUCTOR, "Deconstructor");
            this.add(GCBlocks.CHROMATIC_APPLICATOR, "Chromatic Applicator");
            this.add(GCBlocks.REFINERY, "Refinery");
            this.add(GCBlocks.FUEL_LOADER, "Fuel Loader");
            this.add(GCBlocks.OXYGEN_COMPRESSOR, "Oxygen Compressor");
            this.add(GCBlocks.OXYGEN_DECOMPRESSOR, "Oxygen Decompressor");
            this.add(GCBlocks.OXYGEN_SEALER, "Oxygen Sealer");
            this.add(GCBlocks.OXYGEN_DETECTOR, "Oxygen Detector");
            this.add(GCBlocks.CARGO_LOADER, "Cargo Loader");
            this.add(GCBlocks.CARGO_UNLOADER, "Cargo Unloader");
            this.add(GCBlocks.BASIC_SOLAR_PANEL, "Basic Solar Panel");
            this.add(GCBlocks.ADVANCED_SOLAR_PANEL, "Advanced Solar Panel");
            this.add(GCBlocks.ENERGY_STORAGE, "Energy Storage");
            this.add(GCBlocks.ENERGY_STORAGE_CLUSTER, "Energy Storage Cluster");
            this.add(GCBlocks.ELECTRIC_FURNACE, "Electric Furnace");
            this.add(GCBlocks.ELECTRIC_ARC_FURNACE, "Electric Arc Furnace");
            this.add(GCBlocks.TELEMETRY_UNIT, "Telemetry Unit");
            this.add(GCBlocks.DISPLAY_SCREEN, "Display Screen");
            this.add(GCBlocks.HYDRAULIC_PLATFORM, "Hydraulic Platform");
            this.add(GCBlocks.TIER_1_TREASURE_CHEST, "Tier 1 Treasure Chest");
        }
    }
}