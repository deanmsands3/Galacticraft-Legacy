package micdoodle8.mods.galacticraft.core.data;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
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
            this.simpleBlock(GCBlocks.FULL_BUGGY_FUELING_PAD, this.models().getExistingFile(this.modLoc("block/full_buggy_fueling_pad")));
            this.simpleBlock(GCBlocks.FULL_ROCKET_LAUNCH_PAD, this.models().getExistingFile(this.modLoc("block/full_rocket_launch_pad")));

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
            this.itemGenerated(GCBlocks.LIT_UNLIT_TORCH);
            this.itemGenerated(GCBlocks.CHEESE_BLOCK.asItem());
            this.itemGenerated(GCBlocks.ALUMINUM_WIRE.asItem());
            this.itemGenerated(GCBlocks.HEAVY_ALUMINUM_WIRE.asItem());
            this.itemGenerated(GCBlocks.SWITCHABLE_ALUMINUM_WIRE.asItem());
            this.itemGenerated(GCBlocks.SWITCHABLE_HEAVY_ALUMINUM_WIRE.asItem());

            this.itemGenerated(GCItems.ADVANCED_WAFER);
            this.itemGenerated(GCItems.ALUMINUM_INGOT);
            this.itemGenerated(GCItems.AMBIENT_THERMAL_CONTROLLER);
            this.itemGenerated(GCItems.BASIC_WAFER);
            this.itemGenerated(GCItems.BATTERY);
            this.itemGenerated(GCItems.BUGGY_SEAT);
            this.itemGenerated(GCItems.BUGGY_STORAGE_BOX);
            this.itemGenerated(GCItems.BUGGY_WHEEL);
            this.itemGenerated(GCItems.BURGER_BUN);
            this.itemGenerated(GCItems.CANNED_BEEF);
            this.itemGenerated(GCItems.CANVAS);
            this.itemGenerated(GCItems.CHEESE_CURD);
            this.itemGenerated(GCItems.CHEESE_SLICE);
            this.itemGenerated(GCItems.CHEESEBURGER);
            this.itemGenerated(GCItems.COMPRESSED_ALUMINUM);
            this.itemGenerated(GCItems.COMPRESSED_BRONZE);
            this.itemGenerated(GCItems.COMPRESSED_COPPER);
            this.itemGenerated(GCItems.COMPRESSED_IRON);
            this.itemGenerated(GCItems.COMPRESSED_METEORIC_IRON);
            this.itemGenerated(GCItems.COMPRESSED_STEEL);
            this.itemGenerated(GCItems.COMPRESSED_TIN);
            this.itemGenerated(GCItems.COOKED_BEEF_PATTY);
            this.itemGenerated(GCItems.COPPER_CANISTER);
            this.itemGenerated(GCItems.COPPER_INGOT);
            this.itemGenerated(GCItems.DEHYDRATED_APPLE_CAN);
            this.itemGenerated(GCItems.DEHYDRATED_CARROT_CAN);
            this.itemGenerated(GCItems.DEHYDRATED_MELON_CAN);
            this.itemGenerated(GCItems.DEHYDRATED_POTATO_CAN);
            this.itemGenerated(GCItems.FREQUENCY_MODULE);
            this.itemGenerated(GCItems.FULL_SOLAR_MODULE);
            this.itemGenerated(GCItems.HEAVY_DUTY_AXE);
            this.itemGenerated(GCItems.HEAVY_DUTY_BOOTS);
            this.itemGenerated(GCItems.HEAVY_DUTY_CHESTPLATE);
            this.itemGenerated(GCItems.HEAVY_DUTY_HELMET);
            this.itemGenerated(GCItems.HEAVY_DUTY_HOE);
            this.itemGenerated(GCItems.HEAVY_DUTY_LEGGINGS);
            this.itemGenerated(GCItems.HEAVY_DUTY_PICKAXE);
            this.itemGenerated(GCItems.HEAVY_DUTY_SHOVEL);
            this.itemGenerated(GCItems.HEAVY_DUTY_SWORD);
            this.itemGenerated(GCItems.HEAVY_OXYGEN_TANK);
            this.itemGenerated(GCItems.INFINITE_BATTERY, "battery");
            this.itemGenerated(GCItems.INFINITE_OXYGEN_TANK);
            this.itemGenerated(GCItems.LIGHT_OXYGEN_TANK);
            this.itemGenerated(GCItems.LUNAR_SAPPHIRE);
            this.itemGenerated(GCItems.MEDIUM_OXYGEN_TANK);
            this.itemGenerated(GCItems.METEORIC_IRON_INGOT);
            this.itemGenerated(GCItems.MOON_BUGGY_SCHEMATIC);
            this.itemGenerated(GCItems.NOSE_CONE);
            this.itemGenerated(GCItems.OXYGEN_CONCENTRATOR);
            this.itemGenerated(GCItems.OXYGEN_FAN);
            this.itemGenerated(GCItems.OXYGEN_GEAR);
            this.itemGenerated(GCItems.OXYGEN_MASK);
            this.itemGenerated(GCItems.OXYGEN_VENT);
            this.itemGenerated(GCItems.PRELAUNCH_CHECKLIST);
            this.itemGenerated(GCItems.RAW_GROUND_BEEF);
            this.itemGenerated(GCItems.RAW_METEORIC_IRON);
            this.itemGenerated(GCItems.RAW_SILICON);
            this.itemGenerated(GCItems.ROCKET_FINS);
            this.itemGenerated(GCItems.SENSOR_GLASSES);
            this.itemGenerated(GCItems.SENSOR_LENS);
            this.itemGenerated(GCItems.SINGLE_SOLAR_MODULE);
            this.itemGenerated(GCItems.SOLAR_WAFER);
            this.itemGenerated(GCItems.SPACE_EMERGENCY_KIT);
            this.itemGenerated(GCItems.STANDARD_WRENCH);
            this.itemGenerated(GCItems.STEEL_POLE);
            this.itemGenerated(GCItems.TIER_1_BOOSTER);
            this.itemGenerated(GCItems.TIER_1_HEAVY_DUTY_PLATE);
            this.itemGenerated(GCItems.TIER_1_ROCKET_ENGINE);
            this.itemGenerated(GCItems.TIER_1_DUNGEON_KEY, this.modLoc("item/dungeon_key")).texture("key", "item/tier_1_dungeon_key");
            this.itemGenerated(GCItems.TIER_2_ROCKET_SCHEMATIC);
            this.itemGenerated(GCItems.TIN_CANISTER);
            this.itemGenerated(GCItems.TIN_INGOT);
            this.itemGenerated(GCItems.WHITE_PARACHUTE);
            this.itemGenerated(GCItems.ORANGE_PARACHUTE);
            this.itemGenerated(GCItems.MAGENTA_PARACHUTE);
            this.itemGenerated(GCItems.LIGHT_BLUE_PARACHUTE);
            this.itemGenerated(GCItems.YELLOW_PARACHUTE);
            this.itemGenerated(GCItems.LIME_PARACHUTE);
            this.itemGenerated(GCItems.PINK_PARACHUTE);
            this.itemGenerated(GCItems.GRAY_PARACHUTE);
            this.itemGenerated(GCItems.LIGHT_GRAY_PARACHUTE);
            this.itemGenerated(GCItems.CYAN_PARACHUTE);
            this.itemGenerated(GCItems.PURPLE_PARACHUTE);
            this.itemGenerated(GCItems.BLUE_PARACHUTE);
            this.itemGenerated(GCItems.BROWN_PARACHUTE);
            this.itemGenerated(GCItems.GREEN_PARACHUTE);
            this.itemGenerated(GCItems.RED_PARACHUTE);
            this.itemGenerated(GCItems.BLACK_PARACHUTE);
            this.itemGenerated(GCItems.BUGGY_18_INVENTORY, this.modLoc("item/buggy"));
            this.itemGenerated(GCItems.BUGGY_36_INVENTORY, this.modLoc("item/buggy"));
            this.itemGenerated(GCItems.BUGGY_54_INVENTORY, this.modLoc("item/buggy"));
            this.itemGenerated(GCItems.TIER_1_ROCKET_18_INVENTORY, this.modLoc("item/tier_1_rocket"));
            this.itemGenerated(GCItems.TIER_1_ROCKET_36_INVENTORY, this.modLoc("item/tier_1_rocket"));
            this.itemGenerated(GCItems.TIER_1_ROCKET_54_INVENTORY, this.modLoc("item/tier_1_rocket"));
            this.itemGenerated(GCItems.CREATIVE_TIER_1_ROCKET, this.modLoc("item/tier_1_rocket"));
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

        protected ItemModelBuilder itemGenerated(Item item, ResourceLocation model)
        {
            return this.getBuilder(item.getRegistryName().getPath()).parent(this.getExistingFile(model));
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

            this.add(GCItems.ADVANCED_WAFER, "Advanced Wafer");
            this.add(GCItems.ALUMINUM_INGOT, "Aluminum Ingot");
            this.add(GCItems.AMBIENT_THERMAL_CONTROLLER, "Ambient Thermal Controller");
            this.add(GCItems.BASIC_WAFER, "Basic Wafer");
            this.add(GCItems.BATTERY, "Battery");
            this.add(GCItems.BUGGY_SEAT, "Buggy Seat");
            this.add(GCItems.BUGGY_STORAGE_BOX, "Buggy Storage Box");
            this.add(GCItems.BUGGY_WHEEL, "Buggy Wheel");
            this.add(GCItems.BURGER_BUN, "Burger Bun");
            this.add(GCItems.CANNED_BEEF, "Canned Beef");
            this.add(GCItems.CANVAS, "Canvas");
            this.add(GCItems.CHEESE_CURD, "Cheese Curd");
            this.add(GCItems.CHEESE_SLICE, "Cheese Slice");
            this.add(GCItems.CHEESEBURGER, "Cheeseburger");
            this.add(GCItems.COMPRESSED_ALUMINUM, "Compressed Aluminum");
            this.add(GCItems.COMPRESSED_BRONZE, "Compressed Bronze");
            this.add(GCItems.COMPRESSED_COPPER, "Compressed Copper");
            this.add(GCItems.COMPRESSED_IRON, "Compressed Iron");
            this.add(GCItems.COMPRESSED_METEORIC_IRON, "Compressed Meteoric Iron");
            this.add(GCItems.COMPRESSED_STEEL, "Compressed Steel");
            this.add(GCItems.COMPRESSED_TIN, "Compressed Tin");
            this.add(GCItems.COOKED_BEEF_PATTY, "Cooked Beef Patty");
            this.add(GCItems.COPPER_CANISTER, "Copper Canister");
            this.add(GCItems.COPPER_INGOT, "Copper Ingot");
            this.add(GCItems.DEHYDRATED_APPLE_CAN, "Dehydrated Apple Can");
            this.add(GCItems.DEHYDRATED_CARROT_CAN, "Dehydrated Carrot Can");
            this.add(GCItems.DEHYDRATED_MELON_CAN, "Dehydrated Melon Can");
            this.add(GCItems.DEHYDRATED_POTATO_CAN, "Dehydrated Potato Can");
            this.add(GCItems.FREQUENCY_MODULE, "Frequency Module");
            this.add(GCItems.FULL_SOLAR_MODULE, "Full Solar Module");
            this.add(GCItems.HEAVY_DUTY_AXE, "Heavy Duty Axe");
            this.add(GCItems.HEAVY_DUTY_BOOTS, "Heavy Duty Boots");
            this.add(GCItems.HEAVY_DUTY_CHESTPLATE, "Heavy Duty Chestplate");
            this.add(GCItems.HEAVY_DUTY_HELMET, "Heavy Duty Helmet");
            this.add(GCItems.HEAVY_DUTY_HOE, "Heavy Duty Hoe");
            this.add(GCItems.HEAVY_DUTY_LEGGINGS, "Heavy Duty Leggings");
            this.add(GCItems.HEAVY_DUTY_PICKAXE, "Heavy Duty Pickaxe");
            this.add(GCItems.HEAVY_DUTY_SHOVEL, "Heavy Duty Shovel");
            this.add(GCItems.HEAVY_DUTY_SWORD, "Heavy Duty Sword");
            this.add(GCItems.HEAVY_OXYGEN_TANK, "Heavy Oxygen Tank");
            this.add(GCItems.INFINITE_BATTERY, "Infinite Battery");
            this.add(GCItems.INFINITE_OXYGEN_TANK, "Infinite Oxygen Tank");
            this.add(GCItems.LIGHT_OXYGEN_TANK, "Light Oxygen Tank");
            this.add(GCItems.LUNAR_SAPPHIRE, "Lunar Sapphire");
            this.add(GCItems.MEDIUM_OXYGEN_TANK, "Medium Oxygen Tank");
            this.add(GCItems.METEORIC_IRON_INGOT, "Meteoric Iron Ingot");
            this.add(GCItems.MOON_BUGGY_SCHEMATIC, "Moon Buggy Schematic");
            this.add(GCItems.NOSE_CONE, "Nose Cone");
            this.add(GCItems.OXYGEN_CONCENTRATOR, "Oxygen Concentrator");
            this.add(GCItems.OXYGEN_FAN, "Oxygen Fan");
            this.add(GCItems.OXYGEN_GEAR, "Oxygen Gear");
            this.add(GCItems.OXYGEN_MASK, "Oxygen Mask");
            this.add(GCItems.OXYGEN_VENT, "Oxygen Vent");
            this.add(GCItems.PRELAUNCH_CHECKLIST, "Pre-Launch Checklist");
            this.add(GCItems.RAW_GROUND_BEEF, "Raw Ground Beef");
            this.add(GCItems.RAW_METEORIC_IRON, "Raw Meteoric Iron");
            this.add(GCItems.RAW_SILICON, "Raw Silicon");
            this.add(GCItems.ROCKET_FINS, "Rocket Fins");
            this.add(GCItems.SENSOR_GLASSES, "Sensor Glasses");
            this.add(GCItems.SENSOR_LENS, "Sensor Lens");
            this.add(GCItems.SINGLE_SOLAR_MODULE, "Single Solar Module");
            this.add(GCItems.SOLAR_WAFER, "Solar Wafer");
            this.add(GCItems.SPACE_EMERGENCY_KIT, "Space Emergency Kit");
            this.add(GCItems.STANDARD_WRENCH, "Standard Wrench");
            this.add(GCItems.STEEL_POLE, "Steel Pole");
            this.add(GCItems.TIER_1_BOOSTER, "Tier 1 Booster");
            this.add(GCItems.TIER_1_HEAVY_DUTY_PLATE, "Tier 1 Heavy Duty Plate");
            this.add(GCItems.TIER_1_ROCKET_ENGINE, "Tier 1 Rocket Engine");
            this.add(GCItems.TIER_1_DUNGEON_KEY, "Tier 1 Dungeon Key");
            this.add(GCItems.TIER_2_ROCKET_SCHEMATIC, "Tier 2 Rocket Schematic");
            this.add(GCItems.TIN_CANISTER, "Tin Canister");
            this.add(GCItems.TIN_INGOT, "Tin Ingot");
            this.add(GCItems.WHITE_PARACHUTE, "White Parachute");
            this.add(GCItems.ORANGE_PARACHUTE, "Orange Parachute");
            this.add(GCItems.MAGENTA_PARACHUTE, "Magenta Parachute");
            this.add(GCItems.LIGHT_BLUE_PARACHUTE, "Light Blue Parachute");
            this.add(GCItems.YELLOW_PARACHUTE, "Yellow Parachute");
            this.add(GCItems.LIME_PARACHUTE, "Lime Parachute");
            this.add(GCItems.PINK_PARACHUTE, "Pink Parachute");
            this.add(GCItems.GRAY_PARACHUTE, "Gray Parachute");
            this.add(GCItems.LIGHT_GRAY_PARACHUTE, "Light Gray Parachute");
            this.add(GCItems.CYAN_PARACHUTE, "Cyan Parachute");
            this.add(GCItems.PURPLE_PARACHUTE, "Purple Parachute");
            this.add(GCItems.BLUE_PARACHUTE, "Blue Parachute");
            this.add(GCItems.BROWN_PARACHUTE, "Brown Parachute");
            this.add(GCItems.GREEN_PARACHUTE, "Green Parachute");
            this.add(GCItems.RED_PARACHUTE, "Red Parachute");
            this.add(GCItems.BLACK_PARACHUTE, "Black Parachute");
            this.add(GCItems.BUGGY, "Buggy");
            this.add(GCItems.BUGGY_18_INVENTORY, "Buggy");
            this.add(GCItems.BUGGY_36_INVENTORY, "Buggy");
            this.add(GCItems.BUGGY_54_INVENTORY, "Buggy");
            this.add(GCItems.TIER_1_ROCKET, "Tier 1 Rocket");
            this.add(GCItems.TIER_1_ROCKET_18_INVENTORY, "Tier 1 Rocket");
            this.add(GCItems.TIER_1_ROCKET_36_INVENTORY, "Tier 1 Rocket");
            this.add(GCItems.TIER_1_ROCKET_54_INVENTORY, "Tier 1 Rocket");
            this.add(GCItems.CREATIVE_TIER_1_ROCKET, "Tier 1 Rocket");
            this.add(GCItems.FLAG, "Flag");
            this.add(GCItems.METEOR_CHUNK, "Meteor Chunk");
            this.add(GCItems.HOT_METEOR_CHUNK, "Hot Meteor Chunk");
            this.add(GCItems.DUNGEON_FINDER, "Dungeon Finder");
        }
    }
}