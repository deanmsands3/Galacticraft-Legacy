package micdoodle8.mods.galacticraft.core.data;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.blocks.BlockAirLockWall;
import micdoodle8.mods.galacticraft.core.blocks.BlockCheese;
import micdoodle8.mods.galacticraft.core.blocks.BlockParaChest;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.tags.GCTags;
import micdoodle8.mods.galacticraft.planets.tags.GCPlanetsTags;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.data.*;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

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
        if (event.includeServer())
        {
            BlockTagsProvider blockTagProvider = new BlockTagsBuilder(generator, Constants.MOD_ID_CORE, helper);
            generator.addProvider(blockTagProvider);
            generator.addProvider(new ItemTagsBuilder(generator, blockTagProvider, Constants.MOD_ID_CORE, helper));
            generator.addProvider(new Recipe(generator, Constants.MOD_ID_CORE));
            generator.addProvider(new LootTables(generator));
        }
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
            this.simpleBlock(GCBlocks.EMERGENCY_POST_KIT, this.models().getExistingFile(this.modLoc("block/emergency_post")));
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

            this.getVariantBuilder(GCBlocks.AIR_LOCK_SEAL).partialState().with(BlockAirLockWall.CONNECTION_TYPE, BlockAirLockWall.EnumAirLockSealConnection.X).modelForState().modelFile(this.models().getExistingFile(this.modLoc("block/air_lock_seal"))).addModel()
            .partialState().with(BlockAirLockWall.CONNECTION_TYPE, BlockAirLockWall.EnumAirLockSealConnection.Z).modelForState().rotationY(90).modelFile(this.models().getExistingFile(this.modLoc("block/air_lock_seal"))).addModel()
            .partialState().with(BlockAirLockWall.CONNECTION_TYPE, BlockAirLockWall.EnumAirLockSealConnection.FLAT).modelForState().modelFile(this.models().getExistingFile(this.modLoc("block/flat_air_lock_seal"))).addModel();

            this.getVariantBuilder(GCBlocks.CHEESE_BLOCK).forAllStates(state ->
            {
                int slice = state.get(BlockCheese.BITES);
                String model = slice > 0 ? "cheese_block_slice" + slice : "cheese_block";
                return ConfiguredModel.builder().modelFile(this.models().getExistingFile(this.modLoc("block/" + model))).build();
            });

            this.getVariantBuilder(GCBlocks.PARACHEST).forAllStates(state -> ConfiguredModel.builder().modelFile(this.models().getExistingFile(this.modLoc("block/parachest"))).rotationY((int) state.get(BlockParaChest.FACING).getOpposite().getHorizontalAngle()).build());

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
            this.parentedBlock(GCBlocks.PARACHEST);
            this.parentedBlock(GCBlocks.PLAYER_DETECTOR);
            this.parentedBlock(GCBlocks.HIDDEN_REDSTONE_WIRE);
            this.parentedBlock(GCBlocks.HIDDEN_REDSTONE_REPEATER);
            this.parentedBlock(GCBlocks.EMERGENCY_POST);
            this.parentedBlock(GCBlocks.EMERGENCY_POST_KIT, this.modLoc("block/emergency_post_kit"));
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
            this.parentedBlock(GCBlocks.ROCKET_WORKBENCH);
            this.parentedBlock(GCBlocks.DISPLAY_SCREEN);
            this.parentedBlock(GCBlocks.ELECTRIC_ARC_FURNACE);
            this.parentedBlock(GCBlocks.FLUID_TANK, this.modLoc("block/fluid_tank_ud"));
            this.parentedInventoryBlock(GCBlocks.ARC_LAMP);
            this.parentedInventoryBlock(GCBlocks.FLUID_PIPE);
            this.parentedInventoryBlock(GCBlocks.FUEL_LOADER);
            this.parentedInventoryBlock(GCBlocks.ENERGY_STORAGE);
            this.parentedInventoryBlock(GCBlocks.ENERGY_STORAGE_CLUSTER);
            this.parentedInventoryBlock(GCBlocks.CIRCUIT_FABRICATOR);
            this.parentedInventoryBlock(GCBlocks.OXYGEN_STORAGE_MODULE);
            this.parentedInventoryBlock(GCBlocks.ADVANCED_COMPRESSOR);
            this.parentedInventoryBlock(GCBlocks.DECONSTRUCTOR);
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
            this.itemGenerated(GCItems.DEHYDRATED_APPLES);
            this.itemGenerated(GCItems.DEHYDRATED_CARROTS);
            this.itemGenerated(GCItems.DEHYDRATED_MELONS);
            this.itemGenerated(GCItems.DEHYDRATED_POTATOES);
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
            this.itemGenerated(GCItems.GROUND_BEEF);
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
            this.itemGenerated(GCFluids.FUEL.getBucket());
            this.itemGenerated(GCFluids.HYDROGEN.getBucket());
            this.itemGenerated(GCFluids.OIL.getBucket());
            this.itemGenerated(GCFluids.OXYGEN.getBucket());
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

        protected ItemModelBuilder parentedInventoryBlock(Block block)
        {
            return this.getBuilder(block.getRegistryName().getPath()).parent(new ModelFile.UncheckedModelFile(this.modLoc("block/" + block.getRegistryName().getPath() + "_inventory")));
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
            this.add(GCItems.DEHYDRATED_APPLES, "Dehydrated Apples");
            this.add(GCItems.DEHYDRATED_CARROTS, "Dehydrated Carrots");
            this.add(GCItems.DEHYDRATED_MELONS, "Dehydrated Melons");
            this.add(GCItems.DEHYDRATED_POTATOES, "Dehydrated Potatoes");
            this.add(GCItems.FREQUENCY_MODULE, "Frequency Module");
            this.add(GCItems.FULL_SOLAR_MODULE, "Full Solar Module");
            this.add(GCItems.HEAVY_DUTY_AXE, "Heavy-Duty Axe");
            this.add(GCItems.HEAVY_DUTY_BOOTS, "Heavy-Duty Boots");
            this.add(GCItems.HEAVY_DUTY_CHESTPLATE, "Heavy-Duty Chestplate");
            this.add(GCItems.HEAVY_DUTY_HELMET, "Heavy-Duty Helmet");
            this.add(GCItems.HEAVY_DUTY_HOE, "Heavy-Duty Hoe");
            this.add(GCItems.HEAVY_DUTY_LEGGINGS, "Heavy-Duty Leggings");
            this.add(GCItems.HEAVY_DUTY_PICKAXE, "Heavy-Duty Pickaxe");
            this.add(GCItems.HEAVY_DUTY_SHOVEL, "Heavy-Duty Shovel");
            this.add(GCItems.HEAVY_DUTY_SWORD, "Heavy-Duty Sword");
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
            this.add(GCItems.GROUND_BEEF, "Ground Beef");
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
            this.add(GCItems.TIER_1_HEAVY_DUTY_PLATE, "Tier 1 Heavy-Duty Plate");
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
            this.add(GCItems.DUNGEON_LOCATOR, "Dungeon Locator");
        }
    }

    public static class BlockTagsBuilder extends BlockTagsProvider
    {
        public BlockTagsBuilder(DataGenerator generator, String modid, ExistingFileHelper helper)
        {
            super(generator);
        }

        @Override
        protected void registerTags()
        {
            this.getBuilder(GCTags.ALUMINUM_ORES).add(GCBlocks.ALUMINUM_ORE);
            this.getBuilder(GCTags.CHEESE_ORES).add(GCBlocks.CHEESE_ORE);
            this.getBuilder(GCTags.COPPER_ORES).add(GCBlocks.COPPER_ORE).add(GCBlocks.MOON_COPPER_ORE);
            this.getBuilder(GCTags.SAPPHIRE_ORES).add(GCBlocks.SAPPHIRE_ORE);
            this.getBuilder(GCTags.SILICON_ORES).add(GCBlocks.SILICON_ORE);
            this.getBuilder(GCTags.TIN_ORES).add(GCBlocks.TIN_ORE).add(GCBlocks.MOON_TIN_ORE);

            this.getBuilder(GCTags.ALUMINUM_STORAGE_BLOCKS).add(GCBlocks.ALUMINUM_BLOCK);
            this.getBuilder(GCTags.COPPER_STORAGE_BLOCKS).add(GCBlocks.COPPER_BLOCK);
            this.getBuilder(GCTags.SILICON_STORAGE_BLOCKS).add(GCBlocks.SILICON_BLOCK);
            this.getBuilder(GCTags.TIN_STORAGE_BLOCKS).add(GCBlocks.TIN_BLOCK);

            this.getBuilder(Tags.Blocks.ORES).add(GCTags.ALUMINUM_ORES)
            .add(GCTags.CHEESE_ORES)
            .add(GCTags.COPPER_ORES)
            .add(GCTags.SAPPHIRE_ORES)
            .add(GCTags.SILICON_ORES)
            .add(GCTags.TIN_ORES)
            .add(GCPlanetsTags.DESH_ORES)
            .add(GCPlanetsTags.ILMENITE_ORES);

            this.getBuilder(Tags.Blocks.STORAGE_BLOCKS).add(GCTags.ALUMINUM_STORAGE_BLOCKS)
            .add(GCTags.COPPER_STORAGE_BLOCKS)
            .add(GCTags.SILICON_STORAGE_BLOCKS)
            .add(GCTags.TIN_STORAGE_BLOCKS)
            .add(GCPlanetsTags.LEAD_STORAGE_BLOCKS)
            .add(GCPlanetsTags.DESH_STORAGE_BLOCKS);
        }
    }

    public static class ItemTagsBuilder extends ItemTagsProvider
    {
        public ItemTagsBuilder(DataGenerator generator, BlockTagsProvider blockTagProvider, String modid, ExistingFileHelper helper)
        {
            super(generator);
        }

        @Override
        protected void registerTags()
        {
            this.getBuilder(GCTags.PARACHUTES).add(GCItems.WHITE_PARACHUTE)
            .add(GCItems.ORANGE_PARACHUTE)
            .add(GCItems.MAGENTA_PARACHUTE)
            .add(GCItems.LIGHT_BLUE_PARACHUTE)
            .add(GCItems.YELLOW_PARACHUTE)
            .add(GCItems.LIME_PARACHUTE)
            .add(GCItems.PINK_PARACHUTE)
            .add(GCItems.GRAY_PARACHUTE)
            .add(GCItems.LIGHT_GRAY_PARACHUTE)
            .add(GCItems.CYAN_PARACHUTE)
            .add(GCItems.PURPLE_PARACHUTE)
            .add(GCItems.BLUE_PARACHUTE)
            .add(GCItems.BROWN_PARACHUTE)
            .add(GCItems.GREEN_PARACHUTE)
            .add(GCItems.RED_PARACHUTE)
            .add(GCItems.BLACK_PARACHUTE);

            this.getBuilder(GCTags.ALUMINUM_INGOTS)
            .add(GCItems.ALUMINUM_INGOT);
            this.getBuilder(GCTags.COPPER_INGOTS)
            .add(GCItems.COPPER_INGOT);
            this.getBuilder(GCTags.METEORIC_IRON_INGOTS)
            .add(GCItems.METEORIC_IRON_INGOT);
            this.getBuilder(GCTags.TIN_INGOTS)
            .add(GCItems.TIN_INGOT);

            this.getBuilder(GCTags.ALUMINUM_PLATES)
            .add(GCItems.COMPRESSED_ALUMINUM);
            this.getBuilder(GCTags.BRONZE_PLATES)
            .add(GCItems.COMPRESSED_BRONZE);
            this.getBuilder(GCTags.COPPER_PLATES)
            .add(GCItems.COMPRESSED_COPPER);
            this.getBuilder(GCTags.IRON_PLATES)
            .add(GCItems.COMPRESSED_IRON);
            this.getBuilder(GCTags.METEORIC_IRON_PLATES)
            .add(GCItems.COMPRESSED_METEORIC_IRON);
            this.getBuilder(GCTags.STEEL_PLATES)
            .add(GCItems.COMPRESSED_STEEL);
            this.getBuilder(GCTags.TIN_PLATES)
            .add(GCItems.COMPRESSED_TIN);

            this.getBuilder(GCTags.ADVANCED_WAFERS)
            .add(GCItems.ADVANCED_WAFER);
            this.getBuilder(GCTags.BASIC_WAFERS)
            .add(GCItems.BASIC_WAFER);
            this.getBuilder(GCTags.SOLAR_WAFERS)
            .add(GCItems.SOLAR_WAFER);

            this.getBuilder(Tags.Items.INGOTS)
            .add(GCTags.ALUMINUM_INGOTS)
            .add(GCTags.COPPER_INGOTS)
            .add(GCTags.METEORIC_IRON_INGOTS)
            .add(GCTags.TIN_INGOTS)
            .add(GCPlanetsTags.LEAD_INGOTS)
            .add(GCPlanetsTags.DESH_INGOTS);

            this.getBuilder(GCTags.PLATES)
            .add(GCTags.ALUMINUM_PLATES)
            .add(GCTags.BRONZE_PLATES)
            .add(GCTags.COPPER_PLATES)
            .add(GCTags.IRON_PLATES)
            .add(GCTags.METEORIC_IRON_PLATES)
            .add(GCTags.STEEL_PLATES)
            .add(GCTags.TIN_PLATES);

            this.getBuilder(GCTags.WAFERS)
            .add(GCTags.ADVANCED_WAFERS)
            .add(GCTags.BASIC_WAFERS)
            .add(GCTags.SOLAR_WAFERS);

            this.getBuilder(Tags.Items.ORES)
            .add(GCTags.CHEESE_ORES_ITEM)
            .add(GCPlanetsTags.DESH_ORES_ITEM)
            .add(GCPlanetsTags.ILMENITE_ORES_ITEM);

            this.getBuilder(GCTags.CHEESE_ORES_ITEM).add(GCBlocks.CHEESE_ORE.asItem());
        }
    }

    public static class Recipe extends RecipeProvider
    {
        public Recipe(DataGenerator generator, String modid)
        {
            super(generator);
        }

        @Override
        protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
        {
            ShapedRecipeBuilder.shapedRecipe(GCItems.OXYGEN_FAN).key('Z', GCTags.STEEL_PLATES).key('Y', GCTags.BASIC_WAFERS).key('X', Tags.Items.DUSTS_REDSTONE).patternLine("Z Z").patternLine(" Y ").patternLine("ZXZ").addCriterion(this.toCriterion(GCTags.BASIC_WAFERS), this.hasItem(GCTags.BASIC_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.AIR_LOCK_FRAME, 4).key('Z', GCItems.OXYGEN_CONCENTRATOR).key('Y', GCTags.STEEL_PLATES).key('X', GCTags.ALUMINUM_PLATES).patternLine("XXX").patternLine("YZY").patternLine("XXX").addCriterion(this.toCriterion(GCItems.OXYGEN_CONCENTRATOR), this.hasItem(GCItems.OXYGEN_CONCENTRATOR)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.ALUMINUM_WIRE, 6).key('W', ItemTags.WOOL).key('C', GCTags.ALUMINUM_INGOTS).patternLine("WWW").patternLine("CCC").patternLine("WWW").addCriterion(this.toCriterion(GCTags.ALUMINUM_INGOTS), this.hasItem(GCTags.ALUMINUM_INGOTS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.HEAVY_ALUMINUM_WIRE).key('X', ItemTags.WOOL).key('Y', GCBlocks.ALUMINUM_WIRE).key('Z', GCTags.ALUMINUM_INGOTS).patternLine("X").patternLine("Y").patternLine("Z").setGroup("heavy_aluminum_wire").addCriterion(this.toCriterion(GCBlocks.ALUMINUM_WIRE), this.hasItem(GCBlocks.ALUMINUM_WIRE)).build(consumer, this.modLoc("heavy_aluminum_wire_1"));
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.HEAVY_ALUMINUM_WIRE).key('X', ItemTags.WOOL).key('Y', GCBlocks.ALUMINUM_WIRE).key('Z', GCTags.ALUMINUM_INGOTS).patternLine("Z").patternLine("Y").patternLine("X").setGroup("heavy_aluminum_wire").addCriterion(this.toCriterion(GCBlocks.ALUMINUM_WIRE), this.hasItem(GCBlocks.ALUMINUM_WIRE)).build(consumer, this.modLoc("heavy_aluminum_wire_2"));
            ShapedRecipeBuilder.shapedRecipe(GCItems.AMBIENT_THERMAL_CONTROLLER).key('X', GCTags.STEEL_PLATES).key('Y', GCTags.BRONZE_PLATES).key('Z', GCTags.BASIC_WAFERS).key('W', Tags.Items.DUSTS_REDSTONE).key('V', GCItems.OXYGEN_VENT).patternLine("WVW").patternLine("YXY").patternLine("YZY").addCriterion(this.toCriterion(GCTags.BASIC_WAFERS), this.hasItem(GCTags.BASIC_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.ALUMINUM_BLOCK).key('X', GCItems.ALUMINUM_INGOT).patternLine("XXX").patternLine("XXX").patternLine("XXX").addCriterion(this.toCriterion(GCItems.ALUMINUM_INGOT), this.hasItem(GCItems.ALUMINUM_INGOT)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.COPPER_BLOCK).key('X', GCItems.COPPER_INGOT).patternLine("XXX").patternLine("XXX").patternLine("XXX").addCriterion(this.toCriterion(GCItems.COPPER_INGOT), this.hasItem(GCItems.COPPER_INGOT)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.METEORIC_IRON_BLOCK).key('X', GCItems.METEORIC_IRON_INGOT).patternLine("XXX").patternLine("XXX").patternLine("XXX").addCriterion(this.toCriterion(GCItems.METEORIC_IRON_INGOT), this.hasItem(GCItems.METEORIC_IRON_INGOT)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.SILICON_BLOCK).key('X', GCItems.RAW_SILICON).patternLine("XXX").patternLine("XXX").patternLine("XXX").addCriterion(this.toCriterion(GCItems.RAW_SILICON), this.hasItem(GCItems.RAW_SILICON)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.TIN_BLOCK).key('X', GCItems.TIN_INGOT).patternLine("XXX").patternLine("XXX").patternLine("XXX").addCriterion(this.toCriterion(GCItems.TIN_INGOT), this.hasItem(GCItems.TIN_INGOT)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.BUGGY_SEAT).key('X', GCTags.STEEL_PLATES).key('Y', GCTags.IRON_PLATES).patternLine("  X").patternLine(" YX").patternLine("XXX").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.BUGGY_STORAGE_BOX).key('X', GCTags.STEEL_PLATES).key('Y', GCTags.IRON_PLATES).key('Z', Tags.Items.CHESTS_WOODEN).patternLine("XXX").patternLine("YZY").patternLine("XXX").addCriterion(this.toCriterion(GCTags.IRON_PLATES), this.hasItem(GCTags.IRON_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.BUGGY_WHEEL).key('X', Tags.Items.LEATHER).key('Y', GCTags.STEEL_PLATES).patternLine(" X ").patternLine("XYX").patternLine(" X ").addCriterion(this.toCriterion(Tags.Items.LEATHER), this.hasItem(Tags.Items.LEATHER)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.ROCKET_LAUNCH_PAD, 9).key('X', Tags.Items.STORAGE_BLOCKS_IRON).key('Y', GCTags.IRON_PLATES).patternLine("YYY").patternLine("XXX").addCriterion(this.toCriterion(Tags.Items.STORAGE_BLOCKS_IRON), this.hasItem(Tags.Items.STORAGE_BLOCKS_IRON)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.BUGGY_FUELING_PAD, 9).key('X', Tags.Items.STORAGE_BLOCKS_IRON).key('Y', GCTags.STEEL_PLATES).patternLine("YYY").patternLine("XXX").addCriterion(this.toCriterion(Tags.Items.STORAGE_BLOCKS_IRON), this.hasItem(Tags.Items.STORAGE_BLOCKS_IRON)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.CANVAS).key('X', Tags.Items.STRING).key('Y', Tags.Items.RODS_WOODEN).patternLine(" XY").patternLine("XXX").patternLine("YX ").addCriterion(this.toCriterion(Tags.Items.STRING), this.hasItem(Tags.Items.STRING)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.CARGO_LOADER).key('X', GCTags.STEEL_PLATES).key('Y', GCTags.ALUMINUM_PLATES).key('Z', Tags.Items.CHESTS_WOODEN).key('W', Items.HOPPER).patternLine("XWX").patternLine("YZY").patternLine("XXX").addCriterion(this.toCriterion(Items.HOPPER), this.hasItem(Items.HOPPER)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.CARGO_UNLOADER).key('X', GCTags.STEEL_PLATES).key('Y', GCTags.ALUMINUM_PLATES).key('Z', Tags.Items.CHESTS_WOODEN).key('W', Items.HOPPER).patternLine("XXX").patternLine("YZY").patternLine("XWX").addCriterion(this.toCriterion(Items.HOPPER), this.hasItem(Items.HOPPER)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.CHEESE_BLOCK).key('X', Items.MILK_BUCKET).key('Y', GCItems.CHEESE_CURD).patternLine("YYY").patternLine("YXY").patternLine("YYY").addCriterion(this.toCriterion(GCItems.CHEESE_CURD), this.hasItem(GCItems.CHEESE_CURD)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.CIRCUIT_FABRICATOR).key('X', Items.LEVER).key('Y', Items.FURNACE).key('Z', Items.REDSTONE_TORCH).key('W', GCTags.ALUMINUM_INGOTS).key('V', GCBlocks.ALUMINUM_WIRE).key('U', Items.STONE_BUTTON).patternLine("WXW").patternLine("UYU").patternLine("VZV").addCriterion(this.toCriterion(GCTags.ALUMINUM_INGOTS), this.hasItem(GCTags.ALUMINUM_INGOTS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.ENERGY_STORAGE_CLUSTER).key('X', GCBlocks.ENERGY_STORAGE).key('Y', GCTags.STEEL_PLATES).key('Z', GCTags.ADVANCED_WAFERS).patternLine("XYX").patternLine("YZY").patternLine("XYX").addCriterion(this.toCriterion(GCTags.ADVANCED_WAFERS), this.hasItem(GCTags.ADVANCED_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.COAL_GENERATOR).key('X', Tags.Items.INGOTS_IRON).key('Y', GCBlocks.ALUMINUM_WIRE).key('Z', Items.FURNACE).key('W', GCTags.COPPER_INGOTS).patternLine("WWW").patternLine("XZX").patternLine("XYX").addCriterion(this.toCriterion(Items.FURNACE), this.hasItem(Items.FURNACE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.OXYGEN_COLLECTOR).key('X', GCItems.TIN_CANISTER).key('Y', GCItems.OXYGEN_FAN).key('Z', GCItems.OXYGEN_VENT).key('W', GCTags.STEEL_PLATES).key('V', GCItems.OXYGEN_CONCENTRATOR).key('U', GCTags.ALUMINUM_PLATES).patternLine("WWW").patternLine("YXZ").patternLine("UVU").addCriterion(this.toCriterion(GCItems.OXYGEN_CONCENTRATOR), this.hasItem(GCItems.OXYGEN_CONCENTRATOR)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.EMERGENCY_POST).key('X', GCBlocks.GLOWSTONE_TORCH).key('Y', GCTags.TIN_PLATES).patternLine("XYX").patternLine("Y Y").patternLine("XYX").addCriterion(this.toCriterion(GCTags.TIN_PLATES), this.hasItem(GCTags.TIN_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.HIDDEN_REDSTONE_WIRE, 4).key('X', GCBlocks.TIN_DECORATION_BLOCK_1).key('Y', Tags.Items.DUSTS_REDSTONE).patternLine(" X ").patternLine("XYX").patternLine(" X ").addCriterion(this.toCriterion(Tags.Items.DUSTS_REDSTONE), this.hasItem(Tags.Items.DUSTS_REDSTONE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.HIDDEN_REDSTONE_REPEATER).key('X', GCBlocks.TIN_DECORATION_BLOCK_1).key('Y', Items.REPEATER).patternLine("XYX").addCriterion(this.toCriterion(Items.REPEATER), this.hasItem(Items.REPEATER)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.TIN_DECORATION_BLOCK_1, 4).key('X', Tags.Items.STONE).key('Y', GCTags.TIN_PLATES).patternLine("XX ").patternLine("XXY").patternLine("   ").setGroup("tin_decoration").addCriterion(this.toCriterion(Tags.Items.STONE), this.hasItem(Tags.Items.STONE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.TIN_DECORATION_BLOCK_2, 4).key('X', Tags.Items.STONE).key('Y', GCTags.TIN_PLATES).patternLine("XX ").patternLine("XX ").patternLine(" Y ").setGroup("tin_decoration").addCriterion(this.toCriterion(Tags.Items.STONE), this.hasItem(Tags.Items.STONE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.DECONSTRUCTOR).key('X', Items.ANVIL).key('Y', Items.FURNACE).key('Z', Items.SHEARS).key('U', GCBlocks.ALUMINUM_WIRE).key('V', GCTags.STEEL_PLATES).patternLine("VZV").patternLine("UXU").patternLine("VYV").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.OXYGEN_BUBBLE_DISTRIBUTOR).key('X', GCItems.OXYGEN_FAN).key('Y', GCItems.OXYGEN_VENT).key('Z', GCTags.ALUMINUM_PLATES).key('W', GCTags.STEEL_PLATES).patternLine("WXW").patternLine("YZY").patternLine("WXW").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.ELECTRIC_COMPRESSOR).key('X', GCTags.TIN_PLATES).key('Y', GCBlocks.COMPRESSOR).key('Z', GCTags.ADVANCED_WAFERS).key('W', GCTags.STEEL_PLATES).key('V', GCBlocks.ALUMINUM_WIRE).patternLine("WXW").patternLine("WYW").patternLine("VZV").addCriterion(this.toCriterion(GCTags.ADVANCED_WAFERS), this.hasItem(GCTags.ADVANCED_WAFERS)).build(consumer, this.modLoc("electric_compressor_from_compressor"));
            ShapedRecipeBuilder.shapedRecipe(GCItems.FLAG).key('X', GCItems.STEEL_POLE).key('Y', GCItems.CANVAS).patternLine("XYY").patternLine("XYY").patternLine("X  ").addCriterion(this.toCriterion(GCItems.CANVAS), this.hasItem(GCItems.CANVAS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.FLUID_PIPE, 6).key('X', Tags.Items.GLASS_PANES_COLORLESS).patternLine("XXX").patternLine("   ").patternLine("XXX").addCriterion(this.toCriterion(Tags.Items.GLASS_PANES_COLORLESS), this.hasItem(Tags.Items.GLASS_PANES_COLORLESS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.FLUID_TANK).key('X', Tags.Items.GLASS_PANES_COLORLESS).patternLine(" X ").patternLine("X X").patternLine("XXX").addCriterion(this.toCriterion(Tags.Items.GLASS_PANES_COLORLESS), this.hasItem(Tags.Items.GLASS_PANES_COLORLESS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.FREQUENCY_MODULE).key('X', GCTags.ALUMINUM_PLATES).key('Y', GCTags.IRON_PLATES).key('Z', Tags.Items.DUSTS_REDSTONE).key('W', GCTags.BASIC_WAFERS).key('U', Items.REPEATER).patternLine(" X ").patternLine("YUY").patternLine("ZWZ").addCriterion(this.toCriterion(GCTags.BASIC_WAFERS), this.hasItem(GCTags.BASIC_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.FUEL_LOADER).key('X', GCTags.COPPER_PLATES).key('Y', GCTags.BASIC_WAFERS).key('Z', GCItems.TIN_CANISTER).key('W', GCTags.TIN_PLATES).patternLine("XXX").patternLine("XZX").patternLine("WYW").addCriterion(this.toCriterion(GCTags.BASIC_WAFERS), this.hasItem(GCTags.BASIC_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(Blocks.FURNACE).key('X', GCBlocks.MOON_ROCK).patternLine("XXX").patternLine("X X").patternLine("XXX").addCriterion(this.toCriterion(GCBlocks.MOON_ROCK), this.hasItem(GCBlocks.MOON_ROCK)).build(consumer, "furnace_from_moon_rock");
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.ELECTRIC_FURNACE).key('X', GCTags.STEEL_PLATES).key('Y', GCTags.BASIC_WAFERS).key('Z', Items.FURNACE).key('W', GCTags.ALUMINUM_PLATES).patternLine("XXX").patternLine("XZX").patternLine("WYW").addCriterion(this.toCriterion(GCTags.BASIC_WAFERS), this.hasItem(GCTags.BASIC_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.GLOWSTONE_TORCH, 4).key('X', Tags.Items.RODS_WOODEN).key('Y', Tags.Items.DUSTS_GLOWSTONE).patternLine("Y").patternLine("X").addCriterion(this.toCriterion(Tags.Items.DUSTS_GLOWSTONE), this.hasItem(Tags.Items.DUSTS_GLOWSTONE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.COMPRESSOR).key('X', Items.ANVIL).key('Y', GCTags.COPPER_INGOTS).key('Z', GCTags.BASIC_WAFERS).key('W', GCTags.ALUMINUM_INGOTS).patternLine("WXW").patternLine("WYW").patternLine("WZW").addCriterion(this.toCriterion(GCTags.COPPER_INGOTS), this.hasItem(GCTags.COPPER_INGOTS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.ELECTRIC_COMPRESSOR).key('X', Items.ANVIL).key('Y', GCTags.BRONZE_PLATES).key('Z', GCTags.ADVANCED_WAFERS).key('W', GCTags.STEEL_PLATES).key('V', GCBlocks.ALUMINUM_WIRE).patternLine("WXW").patternLine("WYW").patternLine("VZV").addCriterion(this.toCriterion(GCTags.ADVANCED_WAFERS), this.hasItem(GCTags.ADVANCED_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.ADVANCED_COMPRESSOR).key('X', Items.ANVIL).key('Y', GCTags.METEORIC_IRON_PLATES).key('Z', GCTags.ADVANCED_WAFERS).key('W', GCTags.STEEL_PLATES).key('V', GCBlocks.ALUMINUM_WIRE).patternLine("WXW").patternLine("WYW").patternLine("VZV").addCriterion(this.toCriterion(GCTags.ADVANCED_WAFERS), this.hasItem(GCTags.ADVANCED_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.NOSE_CONE).key('X', GCItems.TIER_1_HEAVY_DUTY_PLATE).key('Y', Items.REDSTONE_TORCH).patternLine(" Y ").patternLine(" X ").patternLine("X X").addCriterion(this.toCriterion(GCItems.TIER_1_HEAVY_DUTY_PLATE), this.hasItem(GCItems.TIER_1_HEAVY_DUTY_PLATE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.OXYGEN_COMPRESSOR).key('X', GCTags.STEEL_PLATES).key('Y', GCTags.BRONZE_PLATES).key('Z', GCItems.OXYGEN_CONCENTRATOR).key('W', GCTags.ALUMINUM_PLATES).patternLine("XWX").patternLine("WZW").patternLine("XYX").addCriterion(this.toCriterion(GCItems.OXYGEN_CONCENTRATOR), this.hasItem(GCItems.OXYGEN_CONCENTRATOR)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.OXYGEN_DECOMPRESSOR).key('X', GCTags.STEEL_PLATES).key('Y', Items.REDSTONE_TORCH).key('Z', GCItems.OXYGEN_CONCENTRATOR).key('W', GCTags.ALUMINUM_PLATES).key('V', GCItems.OXYGEN_FAN).patternLine("XVX").patternLine("WZW").patternLine("XYX").addCriterion(this.toCriterion(GCItems.OXYGEN_CONCENTRATOR), this.hasItem(GCItems.OXYGEN_CONCENTRATOR)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.OXYGEN_CONCENTRATOR).key('X', GCItems.OXYGEN_VENT).key('Y', GCItems.TIN_CANISTER).key('Z', GCTags.STEEL_PLATES).key('W', GCTags.TIN_PLATES).patternLine("ZWZ").patternLine("WYW").patternLine("ZXZ").addCriterion(this.toCriterion(GCItems.TIN_CANISTER), this.hasItem(GCItems.TIN_CANISTER)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.OXYGEN_DETECTOR).key('X', GCTags.ALUMINUM_PLATES).key('Y', GCItems.OXYGEN_VENT).key('Z', Tags.Items.DUSTS_REDSTONE).key('W', GCTags.STEEL_PLATES).key('V', GCTags.BASIC_WAFERS).patternLine("WWW").patternLine("YVY").patternLine("ZXZ").addCriterion(this.toCriterion(GCTags.BASIC_WAFERS), this.hasItem(GCTags.BASIC_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.OXYGEN_GEAR).key('X', GCItems.OXYGEN_CONCENTRATOR).key('Y', GCBlocks.FLUID_PIPE).patternLine(" Y ").patternLine("YXY").patternLine("Y Y").addCriterion(this.toCriterion(GCItems.OXYGEN_CONCENTRATOR), this.hasItem(GCItems.OXYGEN_CONCENTRATOR)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.OXYGEN_MASK).key('X', Tags.Items.GLASS_PANES_COLORLESS).key('Y', Items.IRON_HELMET).patternLine("XXX").patternLine("XYX").patternLine("XXX").addCriterion(this.toCriterion(Items.IRON_HELMET), this.hasItem(Items.IRON_HELMET)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.HEAVY_OXYGEN_TANK).key('X', GCItems.TIN_CANISTER).key('Y', GCTags.STEEL_PLATES).key('Z', Items.RED_WOOL).patternLine("ZZZ").patternLine("XXX").patternLine("YYY").addCriterion(this.toCriterion(GCItems.TIN_CANISTER), this.hasItem(GCItems.TIN_CANISTER)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.LIGHT_OXYGEN_TANK).key('X', GCItems.TIN_CANISTER).key('Y', GCTags.COPPER_PLATES).key('Z', Items.LIME_WOOL).patternLine("ZZZ").patternLine("XXX").patternLine("YYY").addCriterion(this.toCriterion(GCItems.TIN_CANISTER), this.hasItem(GCItems.TIN_CANISTER)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.MEDIUM_OXYGEN_TANK).key('X', GCItems.TIN_CANISTER).key('Y', GCTags.TIN_PLATES).key('Z', Items.ORANGE_WOOL).patternLine("ZZZ").patternLine("XXX").patternLine("YYY").addCriterion(this.toCriterion(GCItems.TIN_CANISTER), this.hasItem(GCItems.TIN_CANISTER)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.CHROMATIC_APPLICATOR).key('A', Tags.Items.DYES_RED).key('B', Tags.Items.DYES_MAGENTA).key('C', Tags.Items.DYES_BLUE)
            .key('D', Tags.Items.DYES_ORANGE).key('E', GCTags.STEEL_PLATES).key('F', Tags.Items.DYES_CYAN)
            .key('G', Tags.Items.DYES_YELLOW).key('H', Tags.Items.DYES_LIME).key('I', Tags.Items.DYES_GREEN)
            .patternLine("ABC").patternLine("DEF").patternLine("GHI").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.HYDRAULIC_PLATFORM, 4).key('X', GCTags.BASIC_WAFERS).key('Y', Tags.Items.DUSTS_GLOWSTONE).key('Z', GCTags.STEEL_PLATES).key('W', Items.PISTON).patternLine("WYW").patternLine("ZXZ").patternLine("WYW").addCriterion(this.toCriterion(Items.PISTON), this.hasItem(Items.PISTON)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.REFINERY).key('X', GCTags.STEEL_PLATES).key('Y', Items.FURNACE).key('Z', GCItems.COPPER_CANISTER).key('W', Tags.Items.STONE).patternLine(" Z ").patternLine("WZW").patternLine("XYX").addCriterion(this.toCriterion(Items.FURNACE), this.hasItem(Items.FURNACE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.ROCKET_FINS).key('X', GCItems.TIER_1_HEAVY_DUTY_PLATE).key('Y', GCTags.STEEL_PLATES).patternLine(" Y ").patternLine("XYX").patternLine("X X").addCriterion(this.toCriterion(GCItems.TIER_1_HEAVY_DUTY_PLATE), this.hasItem(GCItems.TIER_1_HEAVY_DUTY_PLATE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.ROCKET_WORKBENCH).key('X', GCTags.STEEL_PLATES).key('Y', Items.CRAFTING_TABLE).key('Z', Items.LEVER).key('W', GCTags.ADVANCED_WAFERS).key('V', Items.REDSTONE_TORCH).patternLine("XYX").patternLine("ZWZ").patternLine("XVX").addCriterion(this.toCriterion(GCTags.ADVANCED_WAFERS), this.hasItem(GCTags.ADVANCED_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.OXYGEN_SEALER).key('X', GCItems.OXYGEN_FAN).key('Y', GCItems.OXYGEN_VENT).key('Z', GCTags.STEEL_PLATES).key('W', GCTags.ALUMINUM_PLATES).patternLine("WZW").patternLine("YXY").patternLine("WZW").addCriterion(this.toCriterion(GCTags.ALUMINUM_PLATES), this.hasItem(GCTags.ALUMINUM_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(Items.SLIME_BALL).key('X', Tags.Items.DYES_GREEN).key('Y', Items.SUGAR).key('Z', GCItems.CHEESE_CURD).patternLine("XYX").patternLine("YZY").patternLine("XYX").addCriterion(this.toCriterion(GCItems.CHEESE_CURD), this.hasItem(GCItems.CHEESE_CURD)).build(consumer, this.modLoc("slime_ball_from_cheese"));
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.BASIC_SOLAR_PANEL).key('X', GCTags.STEEL_PLATES).key('Y', GCItems.FULL_SOLAR_MODULE).key('Z', GCItems.STEEL_POLE).key('W', GCTags.BASIC_WAFERS).key('V', GCBlocks.ALUMINUM_WIRE).patternLine("XYX").patternLine("XZX").patternLine("VWV").addCriterion(this.toCriterion(GCItems.FULL_SOLAR_MODULE), this.hasItem(GCItems.FULL_SOLAR_MODULE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.ADVANCED_SOLAR_PANEL).key('X', GCTags.STEEL_PLATES).key('Y', GCItems.FULL_SOLAR_MODULE).key('Z', GCItems.STEEL_POLE).key('W', GCTags.ADVANCED_WAFERS).key('V', GCBlocks.HEAVY_ALUMINUM_WIRE).patternLine("XYX").patternLine("XZX").patternLine("VWV").addCriterion(this.toCriterion(GCItems.FULL_SOLAR_MODULE), this.hasItem(GCItems.FULL_SOLAR_MODULE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.SINGLE_SOLAR_MODULE, 2).key('X', Tags.Items.GLASS_COLORLESS).key('Y', GCTags.SOLAR_WAFERS).key('Z', GCBlocks.ALUMINUM_WIRE).patternLine("XXX").patternLine("YYY").patternLine("ZZZ").addCriterion(this.toCriterion(GCTags.SOLAR_WAFERS), this.hasItem(GCTags.SOLAR_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.FULL_SOLAR_MODULE).key('X', GCItems.SINGLE_SOLAR_MODULE).key('Y', GCBlocks.ALUMINUM_WIRE).patternLine("XXX").patternLine("YYY").patternLine("XXX").addCriterion(this.toCriterion(GCItems.SINGLE_SOLAR_MODULE), this.hasItem(GCItems.SINGLE_SOLAR_MODULE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.STANDARD_WRENCH).key('X', GCTags.BRONZE_PLATES).key('Y', GCTags.STEEL_PLATES).patternLine("  Y").patternLine(" X ").patternLine("X  ").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.HEAVY_DUTY_AXE).key('#', Tags.Items.RODS_WOODEN).key('X', GCTags.STEEL_PLATES).patternLine("XX").patternLine("X#").patternLine(" #").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.HEAVY_DUTY_BOOTS).key('X', GCTags.STEEL_PLATES).patternLine("X X").patternLine("X X").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.HEAVY_DUTY_CHESTPLATE).key('X', GCTags.STEEL_PLATES).patternLine("X X").patternLine("XXX").patternLine("XXX").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.HEAVY_DUTY_HELMET).key('X', GCTags.STEEL_PLATES).patternLine("XXX").patternLine("X X").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.HEAVY_DUTY_HOE).key('#', Tags.Items.RODS_WOODEN).key('X', GCTags.STEEL_PLATES).patternLine("XX").patternLine(" #").patternLine(" #").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.HEAVY_DUTY_LEGGINGS).key('X', GCTags.STEEL_PLATES).patternLine("XXX").patternLine("X X").patternLine("X X").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.HEAVY_DUTY_PICKAXE).key('#', Tags.Items.RODS_WOODEN).key('X', GCTags.STEEL_PLATES).patternLine("XXX").patternLine(" # ").patternLine(" # ").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.HEAVY_DUTY_SHOVEL).key('#', Tags.Items.RODS_WOODEN).key('X', GCTags.STEEL_PLATES).patternLine("X").patternLine("#").patternLine("#").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.HEAVY_DUTY_SWORD).key('#', Tags.Items.RODS_WOODEN).key('X', GCTags.STEEL_PLATES).patternLine("X").patternLine("X").patternLine("#").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.TELEMETRY_UNIT).key('X', GCTags.TIN_PLATES).key('Y', GCTags.COPPER_PLATES).key('Z', GCTags.BASIC_WAFERS).key('W', GCItems.FREQUENCY_MODULE).patternLine("XWX").patternLine("XZX").patternLine("YYY").addCriterion(this.toCriterion(GCItems.FREQUENCY_MODULE), this.hasItem(GCItems.FREQUENCY_MODULE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.TIER_1_ROCKET_ENGINE).key('X', GCItems.TIER_1_HEAVY_DUTY_PLATE).key('Y', Items.FLINT_AND_STEEL).key('Z', GCItems.OXYGEN_VENT).key('W', GCItems.TIN_CANISTER).key('V', Items.STONE_BUTTON).patternLine(" YV").patternLine("XWX").patternLine("XZX").addCriterion(this.toCriterion(GCItems.TIER_1_HEAVY_DUTY_PLATE), this.hasItem(GCItems.TIER_1_HEAVY_DUTY_PLATE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCBlocks.DISPLAY_SCREEN).key('X', GCTags.STEEL_PLATES).key('Y', GCTags.BASIC_WAFERS).key('Z', Tags.Items.GLASS_COLORLESS).patternLine("XYX").patternLine("YZY").patternLine("XYX").addCriterion(this.toCriterion(GCTags.BASIC_WAFERS), this.hasItem(GCTags.BASIC_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(GCItems.BATTERY).key('T', GCTags.TIN_PLATES).key('C', Items.COAL).key('R', Tags.Items.DUSTS_REDSTONE).patternLine(" T ").patternLine("TRT").patternLine("TCT").addCriterion(this.toCriterion(GCTags.TIN_PLATES), this.hasItem(GCTags.TIN_PLATES)).build(consumer);

            ShapelessRecipeBuilder.shapelessRecipe(GCItems.OXYGEN_VENT).addIngredient(GCTags.TIN_PLATES).addIngredient(GCTags.TIN_PLATES).addIngredient(GCTags.TIN_PLATES).addIngredient(GCTags.STEEL_PLATES).addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.hasItem(GCTags.STEEL_PLATES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCBlocks.SWITCHABLE_ALUMINUM_WIRE).addIngredient(Items.REPEATER).addIngredient(GCBlocks.ALUMINUM_WIRE).addCriterion(this.toCriterion(GCBlocks.ALUMINUM_WIRE), this.hasItem(GCBlocks.ALUMINUM_WIRE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCBlocks.SWITCHABLE_HEAVY_ALUMINUM_WIRE).addIngredient(Items.REPEATER).addIngredient(GCBlocks.HEAVY_ALUMINUM_WIRE).addCriterion(this.toCriterion(GCBlocks.HEAVY_ALUMINUM_WIRE), this.hasItem(GCBlocks.HEAVY_ALUMINUM_WIRE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.GROUND_BEEF, 2).addIngredient(Items.BEEF).addCriterion(this.toCriterion(Items.BEEF), this.hasItem(Items.BEEF)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.BURGER_BUN, 2).addIngredient(Items.WHEAT).addIngredient(Items.WHEAT).addIngredient(Items.EGG).addIngredient(Items.MILK_BUCKET).addCriterion(this.toCriterion(Items.WHEAT), this.hasItem(Items.WHEAT)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.CANNED_BEEF).addIngredient(GCItems.TIN_CANISTER).addIngredient(GCItems.GROUND_BEEF).addIngredient(GCItems.GROUND_BEEF).addCriterion(this.toCriterion(GCItems.GROUND_BEEF), this.hasItem(GCItems.GROUND_BEEF)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.CHEESE_SLICE, 6).addIngredient(GCBlocks.CHEESE_BLOCK).addCriterion(this.toCriterion(GCBlocks.CHEESE_BLOCK), this.hasItem(GCBlocks.CHEESE_BLOCK)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.CHEESEBURGER).addIngredient(GCItems.CHEESE_SLICE).addIngredient(GCItems.BURGER_BUN).addIngredient(GCItems.COOKED_BEEF_PATTY).addCriterion(this.toCriterion(GCItems.CHEESE_SLICE), this.hasItem(GCItems.CHEESE_SLICE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.DEHYDRATED_APPLES).addIngredient(GCItems.TIN_CANISTER).addIngredient(Items.APPLE).addIngredient(Items.APPLE).setGroup("dehydrated_food").addCriterion(this.toCriterion(Items.APPLE), this.hasItem(Items.APPLE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.DEHYDRATED_CARROTS).addIngredient(GCItems.TIN_CANISTER).addIngredient(Items.CARROT).addIngredient(Items.CARROT).setGroup("dehydrated_food").addCriterion(this.toCriterion(Items.CARROT), this.hasItem(Items.CARROT)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.DEHYDRATED_MELONS).addIngredient(GCItems.TIN_CANISTER).addIngredient(Items.MELON_SLICE).addIngredient(Items.MELON_SLICE).setGroup("dehydrated_food").addCriterion(this.toCriterion(Items.MELON_SLICE), this.hasItem(Items.MELON_SLICE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.DEHYDRATED_POTATOES).addIngredient(GCItems.TIN_CANISTER).addIngredient(Items.POTATO).addIngredient(Items.POTATO).setGroup("dehydrated_food").addCriterion(this.toCriterion(Items.POTATO), this.hasItem(Items.POTATO)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCBlocks.EMERGENCY_POST_KIT).addIngredient(GCItems.SPACE_EMERGENCY_KIT).addIngredient(GCBlocks.EMERGENCY_POST).addCriterion(this.toCriterion(GCItems.SPACE_EMERGENCY_KIT), this.hasItem(GCItems.SPACE_EMERGENCY_KIT)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.ALUMINUM_INGOT, 9).addIngredient(GCBlocks.ALUMINUM_BLOCK).addCriterion(this.toCriterion(GCBlocks.ALUMINUM_BLOCK), this.hasItem(GCBlocks.ALUMINUM_BLOCK)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.TIN_INGOT, 9).addIngredient(GCBlocks.TIN_BLOCK).addCriterion(this.toCriterion(GCBlocks.TIN_BLOCK), this.hasItem(GCBlocks.TIN_BLOCK)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.COPPER_INGOT, 9).addIngredient(GCBlocks.COPPER_BLOCK).addCriterion(this.toCriterion(GCBlocks.COPPER_BLOCK), this.hasItem(GCBlocks.COPPER_BLOCK)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.METEORIC_IRON_INGOT, 9).addIngredient(GCBlocks.METEORIC_IRON_BLOCK).addCriterion(this.toCriterion(GCBlocks.METEORIC_IRON_BLOCK), this.hasItem(GCBlocks.METEORIC_IRON_BLOCK)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.RAW_SILICON, 9).addIngredient(GCBlocks.SILICON_BLOCK).addCriterion(this.toCriterion(GCBlocks.SILICON_BLOCK), this.hasItem(GCBlocks.SILICON_BLOCK)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCBlocks.MAGNETIC_CRAFTING_TABLE).addIngredient(Blocks.CRAFTING_TABLE).addIngredient(GCTags.IRON_PLATES).addCriterion(this.toCriterion(GCTags.IRON_PLATES), this.hasItem(GCTags.IRON_PLATES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.METEOR_CHUNK, 3).addIngredient(GCItems.RAW_METEORIC_IRON).addCriterion(this.toCriterion(GCItems.RAW_METEORIC_IRON), this.hasItem(GCItems.RAW_METEORIC_IRON)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.WHITE_PARACHUTE).addIngredient(GCItems.CANVAS).addIngredient(Tags.Items.STRING).setGroup("parachute").addCriterion(this.toCriterion(GCItems.CANVAS), this.hasItem(GCItems.CANVAS)).build(consumer, this.modLoc("white_parachute_from_canvas"));
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.WHITE_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_WHITE).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.ORANGE_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_ORANGE).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.MAGENTA_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_MAGENTA).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.LIGHT_BLUE_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_LIGHT_BLUE).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.YELLOW_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_YELLOW).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.LIME_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_LIME).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.PINK_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_PINK).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.GRAY_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_GRAY).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.LIGHT_GRAY_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_LIGHT_GRAY).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.CYAN_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_CYAN).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.PURPLE_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_PURPLE).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.BLUE_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_BLUE).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.BROWN_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_BROWN).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.GREEN_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_GREEN).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.RED_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_RED).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.BLACK_PARACHUTE).addIngredient(GCTags.PARACHUTES).addIngredient(Tags.Items.DYES_BLACK).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.hasItem(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(GCItems.PRELAUNCH_CHECKLIST).addIngredient(GCItems.CANVAS).addIngredient(Tags.Items.DYES_RED).addCriterion(this.toCriterion(GCItems.CANVAS), this.hasItem(GCItems.CANVAS)).build(consumer);
        }

        protected String toCriterion(IItemProvider provider)
        {
            return "has_" + provider.asItem().getRegistryName().getPath();
        }

        protected String toCriterion(Tag<?> tag)
        {
            return "has_" + tag.getId().getPath() + "_tag";
        }

        protected ResourceLocation modLoc(String name)
        {
            return new ResourceLocation(Constants.MOD_ID_CORE, name);
        }
    }

    public static class LootTables extends LootTableProvider
    {
        private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> tables = Lists.newArrayList();

        public LootTables(DataGenerator generator)
        {
            super(generator);
            this.addTable(Pair.of(BlockLootTable::new, LootParameterSets.BLOCK)).addTable(Pair.of(EntityLootTable::new, LootParameterSets.ENTITY));
        }

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
        {
            return Collections.unmodifiableList(this.tables);
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker tracker)
        {
            map.forEach((resource, loot) -> LootTableManager.func_227508_a_(tracker, resource, loot));//validateLootTable
        }

        protected LootTables addTable(Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet> table)
        {
            this.tables.add(table);
            return this;
        }

        class BlockLootTable extends BlockLootTables
        {
            @Override
            protected void addTables()
            {
                this.registerDropSelfLootTable(GCBlocks.COPPER_ORE);
                this.registerDropSelfLootTable(GCBlocks.TIN_ORE);
                this.registerDropSelfLootTable(GCBlocks.ALUMINUM_ORE);
                this.registerDropSelfLootTable(GCBlocks.MOON_COPPER_ORE);
                this.registerDropSelfLootTable(GCBlocks.MOON_TIN_ORE);
                this.registerDropSelfLootTable(GCBlocks.SAPPHIRE_ORE);
                this.registerDropSelfLootTable(GCBlocks.COPPER_BLOCK);
                this.registerDropSelfLootTable(GCBlocks.TIN_BLOCK);
                this.registerDropSelfLootTable(GCBlocks.ALUMINUM_BLOCK);
                this.registerDropSelfLootTable(GCBlocks.SILICON_BLOCK);
                this.registerDropSelfLootTable(GCBlocks.METEORIC_IRON_BLOCK);
                this.registerDropSelfLootTable(GCBlocks.MAGNETIC_CRAFTING_TABLE);
                this.registerDropSelfLootTable(GCBlocks.PARACHEST);
                this.registerDropping(GCBlocks.PLAYER_DETECTOR, GCBlocks.TIN_DECORATION_BLOCK_1);
                this.registerDropSelfLootTable(GCBlocks.HIDDEN_REDSTONE_WIRE);
                this.registerDropSelfLootTable(GCBlocks.HIDDEN_REDSTONE_REPEATER);
                this.registerDropSelfLootTable(GCBlocks.EMERGENCY_POST);
                this.registerDropSelfLootTable(GCBlocks.EMERGENCY_POST_KIT);
                this.registerDropSelfLootTable(GCBlocks.MOON_ROCK);
                this.registerDropSelfLootTable(GCBlocks.MOON_DIRT);
                this.registerDropSelfLootTable(GCBlocks.MOON_DUNGEON_BRICKS);
                this.registerDropSelfLootTable(GCBlocks.AIR_LOCK_FRAME);
                this.registerDropSelfLootTable(GCBlocks.TIN_DECORATION_BLOCK_1);
                this.registerDropSelfLootTable(GCBlocks.TIN_DECORATION_BLOCK_2);
                this.registerDropSelfLootTable(GCBlocks.OXYGEN_DETECTOR);
                this.registerDropSelfLootTable(GCBlocks.TELEMETRY_UNIT);
                this.registerDropSelfLootTable(GCBlocks.ROCKET_LAUNCH_PAD);
                this.registerDropSelfLootTable(GCBlocks.BUGGY_FUELING_PAD);
                this.registerDropSelfLootTable(GCBlocks.OXYGEN_BUBBLE_DISTRIBUTOR);
                this.registerDropSelfLootTable(GCBlocks.OXYGEN_COLLECTOR);
                this.registerDropSelfLootTable(GCBlocks.COMPRESSOR);
                this.registerDropSelfLootTable(GCBlocks.COAL_GENERATOR);
                this.registerDropSelfLootTable(GCBlocks.CHROMATIC_APPLICATOR);
                this.registerDropSelfLootTable(GCBlocks.BASIC_SOLAR_PANEL);
                this.registerDropSelfLootTable(GCBlocks.ADVANCED_SOLAR_PANEL);
                this.registerDropSelfLootTable(GCBlocks.OXYGEN_COMPRESSOR);
                this.registerDropSelfLootTable(GCBlocks.OXYGEN_DECOMPRESSOR);
                this.registerDropSelfLootTable(GCBlocks.OXYGEN_SEALER);
                this.registerDropSelfLootTable(GCBlocks.ELECTRIC_COMPRESSOR);
                this.registerDropSelfLootTable(GCBlocks.ELECTRIC_FURNACE);
                this.registerDropSelfLootTable(GCBlocks.REFINERY);
                this.registerDropSelfLootTable(GCBlocks.CARGO_LOADER);
                this.registerDropSelfLootTable(GCBlocks.CARGO_UNLOADER);
                this.registerDropSelfLootTable(GCBlocks.AIR_LOCK_CONTROLLER);
                this.registerDropSelfLootTable(GCBlocks.ROCKET_WORKBENCH);
                this.registerDropSelfLootTable(GCBlocks.ARC_LAMP);
                this.registerDropSelfLootTable(GCBlocks.GLOWSTONE_TORCH);
                this.registerDropSelfLootTable(GCBlocks.UNLIT_TORCH);
                this.registerDropSelfLootTable(GCBlocks.ADVANCED_COMPRESSOR);
                this.registerDropSelfLootTable(GCBlocks.CIRCUIT_FABRICATOR);
                this.registerDropSelfLootTable(GCBlocks.OXYGEN_STORAGE_MODULE);
                this.registerDropSelfLootTable(GCBlocks.DECONSTRUCTOR);
                this.registerDropSelfLootTable(GCBlocks.FUEL_LOADER);
                this.registerDropSelfLootTable(GCBlocks.ENERGY_STORAGE);
                this.registerDropSelfLootTable(GCBlocks.ENERGY_STORAGE_CLUSTER);
                this.registerDropSelfLootTable(GCBlocks.ELECTRIC_ARC_FURNACE);
                this.registerDropSelfLootTable(GCBlocks.FLUID_PIPE);
                this.registerDropSelfLootTable(GCBlocks.ALUMINUM_WIRE);
                this.registerDropSelfLootTable(GCBlocks.HEAVY_ALUMINUM_WIRE);
                this.registerDropSelfLootTable(GCBlocks.SWITCHABLE_ALUMINUM_WIRE);
                this.registerDropSelfLootTable(GCBlocks.SWITCHABLE_HEAVY_ALUMINUM_WIRE);
                this.registerDropSelfLootTable(GCBlocks.DISPLAY_SCREEN);
                this.registerDropSelfLootTable(GCBlocks.FLUID_TANK);
                this.registerDropSelfLootTable(GCBlocks.HYDRAULIC_PLATFORM);
                this.registerDropSelfLootTable(GCBlocks.MOON_TURF);
                this.registerLootTable(GCBlocks.CHEESE_BLOCK, func_218482_a());
                this.registerLootTable(GCBlocks.SILICON_ORE, block -> droppingItemWithFortune(block, GCItems.RAW_SILICON));
                this.registerLootTable(GCBlocks.CHEESE_ORE, block -> droppingItemWithFortune(block, GCItems.CHEESE_CURD));
                this.registerLootTable(GCBlocks.SAPPHIRE_ORE, block -> droppingItemWithFortune(block, GCItems.LUNAR_SAPPHIRE));
                this.registerLootTable(GCBlocks.FALLEN_METEOR, droppingRandomly(GCItems.RAW_METEORIC_IRON, RandomValueRange.of(1.0F, 2.0F)));
                this.registerLootTable(GCBlocks.FULL_ROCKET_LAUNCH_PAD, block -> droppingWithSilkTouchOrRandomly(block, GCBlocks.ROCKET_LAUNCH_PAD, ConstantRange.of(9)));
                this.registerLootTable(GCBlocks.FULL_BUGGY_FUELING_PAD, block -> droppingWithSilkTouchOrRandomly(block, GCBlocks.BUGGY_FUELING_PAD, ConstantRange.of(9)));
            }

            @Override
            protected Iterable<Block> getKnownBlocks()
            {
                return ForgeRegistries.BLOCKS.getValues().stream().filter(type -> type.getRegistryName().getNamespace().equals(Constants.MOD_ID_CORE)).collect(Collectors.toList());
            }
        }

        class EntityLootTable extends EntityLootTables
        {
            @Override
            protected void addTables()
            {
                this.registerLootTable(GCEntities.EVOLVED_SPIDER, LootTable.builder()
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(Items.STRING).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))))
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(Items.SPIDER_EYE).acceptFunction(SetCount.builder(RandomValueRange.of(-1.0F, 1.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))).acceptCondition(KilledByPlayer.builder()))
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(GCItems.CHEESE_CURD))
                                .addEntry(ItemLootEntry.builder(Items.FERMENTED_SPIDER_EYE))
                                .addEntry(ItemLootEntry.builder(GCItems.MEDIUM_OXYGEN_TANK).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.1F, 0.8F))))
                                .addEntry(ItemLootEntry.builder(GCItems.OXYGEN_GEAR))
                                .addEntry(ItemLootEntry.builder(GCItems.OXYGEN_CONCENTRATOR))
                                .addEntry(ItemLootEntry.builder(Items.NETHER_WART))//TODO ConfigManagerCore.challengeMobDropsAndSpawning condition
                                .acceptCondition(KilledByPlayer.builder()).acceptCondition(RandomChanceWithLooting.builder(0.025F, 0.02F))));

                this.registerLootTable(GCEntities.EVOLVED_ZOMBIE, LootTable.builder()
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(Items.ROTTEN_FLESH).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))))
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(Items.IRON_INGOT))
                                .addEntry(ItemLootEntry.builder(Items.CARROT))
                                .addEntry(ItemLootEntry.builder(Items.POTATO)).acceptCondition(KilledByPlayer.builder()).acceptCondition(RandomChanceWithLooting.builder(0.025F, 0.01F)))
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(GCItems.DEHYDRATED_CARROTS))
                                .addEntry(ItemLootEntry.builder(GCItems.DEHYDRATED_POTATOES))
                                .addEntry(ItemLootEntry.builder(GCItems.RAW_METEORIC_IRON))
                                .addEntry(ItemLootEntry.builder(GCItems.MEDIUM_OXYGEN_TANK).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.1F, 0.8F))))
                                .addEntry(ItemLootEntry.builder(GCItems.OXYGEN_MASK))
                                .addEntry(ItemLootEntry.builder(GCItems.OXYGEN_VENT))
                                .addEntry(ItemLootEntry.builder(Items.MELON_SEEDS))//TODO ConfigManagerCore.challengeMobDropsAndSpawning condition
                                .acceptCondition(KilledByPlayer.builder()).acceptCondition(RandomChanceWithLooting.builder(0.025F, 0.02F)))
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(GCItems.COPPER_INGOT))//TODO ConfigManagerCore.challengeMobDropsAndSpawning condition
                                .acceptCondition(KilledByPlayer.builder()).acceptCondition(RandomChanceWithLooting.builder(0.5F, 0.0F))));

                this.registerLootTable(GCEntities.EVOLVED_CREEPER, LootTable.builder()
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(Items.GUNPOWDER).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))))
                        .addLootPool(LootPool.builder()
                                .addEntry(TagLootEntry.func_216176_b(ItemTags.MUSIC_DISCS)).acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.create().type(EntityTypeTags.SKELETONS))))
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(Blocks.SAND))
                                .addEntry(ItemLootEntry.builder(GCItems.MEDIUM_OXYGEN_TANK).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.1F, 0.8F))))
                                .addEntry(ItemLootEntry.builder(GCItems.OXYGEN_GEAR))
                                .addEntry(ItemLootEntry.builder(Blocks.ICE))
                                .addEntry(ItemLootEntry.builder(Items.SUGAR_CANE))//TODO ConfigManagerCore.challengeMobDropsAndSpawning condition
                                .acceptCondition(KilledByPlayer.builder()).acceptCondition(RandomChanceWithLooting.builder(0.025F, 0.02F))));

                this.registerLootTable(GCEntities.EVOLVED_SKELETON, LootTable.builder()
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(Items.ARROW).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))))
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(Items.BONE).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))))
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(GCBlocks.FLUID_PIPE))
                                .addEntry(ItemLootEntry.builder(GCItems.MEDIUM_OXYGEN_TANK).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.1F, 0.8F))))
                                .addEntry(ItemLootEntry.builder(GCItems.TIN_CANISTER))
                                .addEntry(ItemLootEntry.builder(Items.PUMPKIN_SEEDS))//TODO ConfigManagerCore.challengeMobDropsAndSpawning condition
                                .acceptCondition(KilledByPlayer.builder()).acceptCondition(RandomChanceWithLooting.builder(0.025F, 0.02F))));

                this.registerLootTable(GCEntities.EVOLVED_ENDERMAN, LootTable.builder()
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(Items.ENDER_PEARL).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 1.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))))
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(GCItems.MEDIUM_OXYGEN_TANK).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.1F, 0.8F))))
                                .addEntry(ItemLootEntry.builder(Items.ENDER_EYE))
                                .addEntry(ItemLootEntry.builder(GCItems.OXYGEN_CONCENTRATOR))
                                .addEntry(ItemLootEntry.builder(GCItems.OXYGEN_MASK))
                                .acceptCondition(KilledByPlayer.builder()).acceptCondition(RandomChanceWithLooting.builder(0.025F, 0.02F))));

                this.registerLootTable(GCEntities.EVOLVED_WITCH, LootTable.builder()
                        .addLootPool(LootPool.builder().rolls(RandomValueRange.of(1.0F, 3.0F))
                                .addEntry(ItemLootEntry.builder(Items.GLOWSTONE_DUST).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))
                                .addEntry(ItemLootEntry.builder(Items.SUGAR).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))
                                .addEntry(ItemLootEntry.builder(Items.REDSTONE).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))
                                .addEntry(ItemLootEntry.builder(Items.SPIDER_EYE).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))
                                .addEntry(ItemLootEntry.builder(Items.GLASS_BOTTLE).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))
                                .addEntry(ItemLootEntry.builder(Items.GUNPOWDER).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))
                                .addEntry(ItemLootEntry.builder(Items.STICK).weight(2).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))))
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(GCItems.MEDIUM_OXYGEN_TANK).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.1F, 0.8F))))
                                .addEntry(ItemLootEntry.builder(GCItems.DEHYDRATED_CARROTS))
                                .addEntry(ItemLootEntry.builder(Blocks.GLOWSTONE))
                                .addEntry(ItemLootEntry.builder(GCItems.AMBIENT_THERMAL_CONTROLLER))
                                .addEntry(ItemLootEntry.builder(GCItems.OXYGEN_MASK))
                                .addEntry(ItemLootEntry.builder(GCItems.OXYGEN_VENT))
                                .acceptCondition(KilledByPlayer.builder()).acceptCondition(RandomChanceWithLooting.builder(0.025F, 0.02F))));

                this.registerLootTable(GCEntities.EVOLVED_SKELETON_BOSS, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(Items.ARROW).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(Items.BONE).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));
            }

            @Override
            protected Iterable<EntityType<?>> getKnownEntities()
            {
                return ForgeRegistries.ENTITIES.getValues().stream().filter(type -> type.getRegistryName().getNamespace().equals(Constants.MOD_ID_CORE)).collect(Collectors.toList());
            }
        }
    }
}