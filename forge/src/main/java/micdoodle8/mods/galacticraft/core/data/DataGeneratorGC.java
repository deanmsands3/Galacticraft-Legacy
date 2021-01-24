package micdoodle8.mods.galacticraft.core.data;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import ItemModelBuilder;
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
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.*;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.storage.loot.ConstantIntValue;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.RandomValueBounds;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.functions.*;
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
            generator.addProvider(new AdvancementProviderGC(generator));
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
            this.stairsBlock(GCBlocks.TIN_DECORATION_STAIRS_1, tinDecor);
            this.stairsBlock(GCBlocks.TIN_DECORATION_STAIRS_2, this.modLoc("block/tin_decoration_block_side"), tinDecor, this.modLoc("block/tin_decoration_block_top"));
            this.stairsBlock(GCBlocks.MOON_ROCK_STAIRS, this.modLoc("block/moon_rock"));
            this.stairsBlock(GCBlocks.MOON_DUNGEON_BRICK_STAIRS, this.modLoc("block/moon_dungeon_bricks"));
            this.slabBlock(GCBlocks.TIN_DECORATION_SLAB_1, this.getGeneratedModel(GCBlocks.TIN_DECORATION_BLOCK_1), tinDecor);
            this.slabBlock(GCBlocks.MOON_ROCK_SLAB, this.getGeneratedModel(GCBlocks.MOON_ROCK), this.modLoc("block/moon_rock"));
            this.slabBlock(GCBlocks.MOON_DUNGEON_BRICK_SLAB, this.getGeneratedModel(GCBlocks.MOON_DUNGEON_BRICKS), this.modLoc("block/moon_dungeon_bricks"));
            this.wallBlock(GCBlocks.TIN_DECORATION_WALL_1, this.modLoc("block/tin_decoration_block"));
            this.wallBlock(GCBlocks.TIN_DECORATION_WALL_2, this.models().getExistingFile(this.modLoc("block/tin_decoration_wall_2_post")), this.models().getExistingFile(this.modLoc("block/tin_decoration_wall_2_side")));
            this.wallBlock(GCBlocks.MOON_ROCK_WALL, this.modLoc("block/moon_rock"));
            this.wallBlock(GCBlocks.MOON_DUNGEON_BRICK_WALL, this.modLoc("block/moon_dungeon_bricks"));
            this.itemModels().wallInventory(this.toString(GCBlocks.TIN_DECORATION_WALL_1), this.modLoc("block/tin_decoration_block"));
            this.itemModels().wallInventory(this.toString(GCBlocks.MOON_ROCK_WALL), this.modLoc("block/moon_rock"));
            this.itemModels().wallInventory(this.toString(GCBlocks.MOON_DUNGEON_BRICK_WALL), this.modLoc("block/moon_dungeon_bricks"));

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

            this.slabBlock(GCBlocks.TIN_DECORATION_SLAB_2, this.getGeneratedModel(GCBlocks.TIN_DECORATION_BLOCK_2), this.modLoc("block/tin_decoration_block_side"), tinDecor, this.modLoc("block/tin_decoration_block_top"));

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

        protected void simpleFluid(LiquidBlock block)
        {
            this.getVariantBuilder(block).partialState().setModels(new ConfiguredModel(this.models().getBuilder(this.toString(block)).texture("particle", this.modLoc(block.getFluid().getAttributes().getStillTexture().getPath()))));
        }

        protected String toString(Block block)
        {
            return block.getRegistryName().getPath();
        }

        protected ResourceLocation getGeneratedModel(Block block)
        {
            return this.models().generatedModels.get(this.modLoc("block/" + this.toString(block))).getLocation();
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
            this.parentedBlock(GCBlocks.TIN_DECORATION_STAIRS_1);
            this.parentedBlock(GCBlocks.TIN_DECORATION_STAIRS_2);
            this.parentedBlock(GCBlocks.MOON_ROCK_STAIRS);
            this.parentedBlock(GCBlocks.MOON_DUNGEON_BRICK_STAIRS);
            this.parentedBlock(GCBlocks.TIN_DECORATION_SLAB_1);
            this.parentedBlock(GCBlocks.TIN_DECORATION_SLAB_2);
            this.parentedBlock(GCBlocks.MOON_ROCK_SLAB);
            this.parentedBlock(GCBlocks.MOON_DUNGEON_BRICK_SLAB);
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

        protected String itemToString(ItemLike base)
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
            this.add(GCBlocks.MOON_ROCK_STAIRS, "Moon Rock Stairs");
            this.add(GCBlocks.MOON_DUNGEON_BRICK_STAIRS, "Moon Dungeon Brick Stairs");
            this.add(GCBlocks.MOON_ROCK_SLAB, "Moon Rock Slab");
            this.add(GCBlocks.MOON_DUNGEON_BRICK_SLAB, "Moon Dungeon Brick Slab");
            this.add(GCBlocks.MOON_ROCK_WALL, "Moon Rock Wall");
            this.add(GCBlocks.MOON_DUNGEON_BRICK_WALL, "Moon Dungeon Brick Wall");

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
        protected void addTags()
        {
            this.tag(GCTags.ALUMINUM_ORES).add(GCBlocks.ALUMINUM_ORE)
            .add(AsteroidBlocks.ASTEROID_ALUMINUM_ORE)
            .add(VenusBlocks.VENUS_ALUMINUM_ORE);

            this.tag(GCTags.CHEESE_ORES).add(GCBlocks.CHEESE_ORE);
            this.tag(GCTags.COPPER_ORES).add(GCBlocks.COPPER_ORE)
            .add(GCBlocks.MOON_COPPER_ORE)
            .add(MarsBlocks.MARS_COPPER_ORE)
            .add(VenusBlocks.VENUS_COPPER_ORE);

            this.tag(GCTags.SAPPHIRE_ORES).add(GCBlocks.SAPPHIRE_ORE);
            this.tag(GCTags.SILICON_ORES).add(GCBlocks.SILICON_ORE).add(VenusBlocks.VENUS_SILICON_ORE);

            this.tag(GCTags.TIN_ORES).add(GCBlocks.TIN_ORE)
            .add(GCBlocks.MOON_TIN_ORE)
            .add(MarsBlocks.MARS_TIN_ORE)
            .add(VenusBlocks.VENUS_TIN_ORE);

            this.tag(GCTags.ALUMINUM_STORAGE_BLOCKS).add(GCBlocks.ALUMINUM_BLOCK);
            this.tag(GCTags.COPPER_STORAGE_BLOCKS).add(GCBlocks.COPPER_BLOCK);
            this.tag(GCTags.SILICON_STORAGE_BLOCKS).add(GCBlocks.SILICON_BLOCK);
            this.tag(GCTags.TIN_STORAGE_BLOCKS).add(GCBlocks.TIN_BLOCK);

            this.tag(Tags.Blocks.ORES_IRON).add(MarsBlocks.MARS_IRON_ORE)
            .add(AsteroidBlocks.ASTEROID_IRON_ORE);

            this.tag(Tags.Blocks.ORES_QUARTZ).add(VenusBlocks.VENUS_QUARTZ_ORE);

            this.tag(Tags.Blocks.ORES).add(GCTags.ALUMINUM_ORES)
            .add(GCTags.CHEESE_ORES)
            .add(GCTags.COPPER_ORES)
            .add(GCTags.SAPPHIRE_ORES)
            .add(GCTags.SILICON_ORES)
            .add(GCTags.TIN_ORES)
            .add(GCTags.DESH_ORES)
            .add(GCTags.ILMENITE_ORES)
            .add(VenusBlocks.GALENA_ORE);

            this.tag(Tags.Blocks.STORAGE_BLOCKS).add(GCTags.ALUMINUM_STORAGE_BLOCKS)
            .add(GCTags.COPPER_STORAGE_BLOCKS)
            .add(GCTags.SILICON_STORAGE_BLOCKS)
            .add(GCTags.TIN_STORAGE_BLOCKS)
            .add(GCTags.LEAD_STORAGE_BLOCKS)
            .add(GCTags.DESH_STORAGE_BLOCKS);

            this.tag(BlockTags.STAIRS)
            .add(GCBlocks.TIN_DECORATION_STAIRS_1)
            .add(GCBlocks.TIN_DECORATION_STAIRS_2)
            .add(GCBlocks.MOON_ROCK_STAIRS)
            .add(GCBlocks.MOON_DUNGEON_BRICK_STAIRS)
            .add(MarsBlocks.MARS_COBBLESTONE_STAIRS)
            .add(MarsBlocks.MARS_DUNGEON_BRICK_STAIRS);

            this.tag(BlockTags.SLABS)
            .add(GCBlocks.TIN_DECORATION_SLAB_1)
            .add(GCBlocks.TIN_DECORATION_SLAB_2)
            .add(GCBlocks.MOON_ROCK_SLAB)
            .add(GCBlocks.MOON_DUNGEON_BRICK_SLAB)
            .add(MarsBlocks.MARS_COBBLESTONE_SLAB)
            .add(MarsBlocks.MARS_DUNGEON_BRICK_SLAB)
            .add(AsteroidBlocks.DARK_DECORATION_SLAB);

            this.tag(BlockTags.WALLS)
            .add(GCBlocks.TIN_DECORATION_WALL_1)
            .add(GCBlocks.TIN_DECORATION_WALL_2)
            .add(GCBlocks.MOON_ROCK_WALL)
            .add(GCBlocks.MOON_DUNGEON_BRICK_WALL)
            .add(MarsBlocks.MARS_COBBLESTONE_WALL)
            .add(MarsBlocks.MARS_DUNGEON_BRICK_WALL);
        }
    }

    public static class ItemTagsBuilder extends ItemTagsProvider
    {
        public ItemTagsBuilder(DataGenerator generator, BlockTagsProvider blockTagProvider, String modid, ExistingFileHelper helper)
        {
            super(generator);
        }

        @Override
        protected void addTags()
        {
            this.tag(GCTags.PARACHUTES).add(GCItems.WHITE_PARACHUTE)
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

            this.tag(GCTags.CHEESE_ORES_ITEM).add(GCBlocks.CHEESE_ORE.asItem());

            this.tag(GCTags.ALUMINUM_ORES_ITEM).add(GCBlocks.ALUMINUM_ORE.asItem())
            .add(AsteroidBlocks.ASTEROID_ALUMINUM_ORE.asItem())
            .add(VenusBlocks.VENUS_ALUMINUM_ORE.asItem());

            this.tag(GCTags.CHEESE_ORES_ITEM).add(GCBlocks.CHEESE_ORE.asItem());
            this.tag(GCTags.COPPER_ORES_ITEM)
            .add(GCBlocks.COPPER_ORE.asItem())
            .add(GCBlocks.MOON_COPPER_ORE.asItem())
            .add(MarsBlocks.MARS_COPPER_ORE.asItem())
            .add(VenusBlocks.VENUS_COPPER_ORE.asItem());

            this.tag(GCTags.SAPPHIRE_ORES_ITEM).add(GCBlocks.SAPPHIRE_ORE.asItem());
            this.tag(GCTags.SILICON_ORES_ITEM).add(GCBlocks.SILICON_ORE.asItem()).add(VenusBlocks.VENUS_SILICON_ORE.asItem());
            this.tag(GCTags.TIN_ORES_ITEM).add(GCBlocks.TIN_ORE.asItem())
            .add(GCBlocks.MOON_TIN_ORE.asItem())
            .add(MarsBlocks.MARS_TIN_ORE.asItem())
            .add(VenusBlocks.VENUS_TIN_ORE.asItem());

            this.tag(GCTags.ALUMINUM_STORAGE_BLOCKS_ITEM).add(GCBlocks.ALUMINUM_BLOCK.asItem());
            this.tag(GCTags.COPPER_STORAGE_BLOCKS_ITEM).add(GCBlocks.COPPER_BLOCK.asItem());
            this.tag(GCTags.SILICON_STORAGE_BLOCKS_ITEM).add(GCBlocks.SILICON_BLOCK.asItem());
            this.tag(GCTags.TIN_STORAGE_BLOCKS_ITEM).add(GCBlocks.TIN_BLOCK.asItem());

            this.tag(Tags.Items.ORES_IRON).add(MarsBlocks.MARS_IRON_ORE.asItem()).add(AsteroidBlocks.ASTEROID_IRON_ORE.asItem());

            this.tag(Tags.Items.ORES_QUARTZ).add(VenusBlocks.VENUS_QUARTZ_ORE.asItem());

            this.tag(GCTags.ALUMINUM_INGOTS)
            .add(GCItems.ALUMINUM_INGOT);
            this.tag(GCTags.COPPER_INGOTS)
            .add(GCItems.COPPER_INGOT);
            this.tag(GCTags.METEORIC_IRON_INGOTS)
            .add(GCItems.METEORIC_IRON_INGOT);
            this.tag(GCTags.TIN_INGOTS)
            .add(GCItems.TIN_INGOT);

            this.tag(GCTags.ALUMINUM_PLATES)
            .add(GCItems.COMPRESSED_ALUMINUM);
            this.tag(GCTags.BRONZE_PLATES)
            .add(GCItems.COMPRESSED_BRONZE);
            this.tag(GCTags.COPPER_PLATES)
            .add(GCItems.COMPRESSED_COPPER);
            this.tag(GCTags.IRON_PLATES)
            .add(GCItems.COMPRESSED_IRON);
            this.tag(GCTags.METEORIC_IRON_PLATES)
            .add(GCItems.COMPRESSED_METEORIC_IRON);
            this.tag(GCTags.STEEL_PLATES)
            .add(GCItems.COMPRESSED_STEEL);
            this.tag(GCTags.TIN_PLATES)
            .add(GCItems.COMPRESSED_TIN);

            this.tag(GCTags.ADVANCED_WAFERS)
            .add(GCItems.ADVANCED_WAFER);
            this.tag(GCTags.BASIC_WAFERS)
            .add(GCItems.BASIC_WAFER);
            this.tag(GCTags.SOLAR_WAFERS)
            .add(GCItems.SOLAR_WAFER);

            this.tag(Tags.Items.INGOTS)
            .add(GCTags.ALUMINUM_INGOTS)
            .add(GCTags.COPPER_INGOTS)
            .add(GCTags.METEORIC_IRON_INGOTS)
            .add(GCTags.TIN_INGOTS)
            .add(GCTags.LEAD_INGOTS)
            .add(GCTags.DESH_INGOTS);

            this.tag(GCTags.PLATES)
            .addTag(GCTags.ALUMINUM_PLATES)
            .addTag(GCTags.BRONZE_PLATES)
            .addTag(GCTags.COPPER_PLATES)
            .addTag(GCTags.IRON_PLATES)
            .addTag(GCTags.METEORIC_IRON_PLATES)
            .addTag(GCTags.STEEL_PLATES)
            .addTag(GCTags.TIN_PLATES);

            this.tag(GCTags.WAFERS)
            .addTag(GCTags.ADVANCED_WAFERS)
            .addTag(GCTags.BASIC_WAFERS)
            .addTag(GCTags.SOLAR_WAFERS);

            this.tag(Tags.Items.ORES)
            .add(GCTags.ALUMINUM_ORES_ITEM)
            .add(GCTags.CHEESE_ORES_ITEM)
            .add(GCTags.COPPER_ORES_ITEM)
            .add(GCTags.SAPPHIRE_ORES_ITEM)
            .add(GCTags.SILICON_ORES_ITEM)
            .add(GCTags.TIN_ORES_ITEM)
            .add(GCTags.DESH_ORES_ITEM)
            .add(GCTags.ILMENITE_ORES_ITEM)
            .add(VenusBlocks.GALENA_ORE.asItem());

            this.tag(Tags.Items.STORAGE_BLOCKS).add(GCTags.ALUMINUM_STORAGE_BLOCKS_ITEM)
            .add(GCTags.COPPER_STORAGE_BLOCKS_ITEM)
            .add(GCTags.SILICON_STORAGE_BLOCKS_ITEM)
            .add(GCTags.TIN_STORAGE_BLOCKS_ITEM)
            .add(GCTags.LEAD_STORAGE_BLOCKS_ITEM)
            .add(GCTags.DESH_STORAGE_BLOCKS_ITEM);

            this.tag(ItemTags.STAIRS)
            .add(GCBlocks.TIN_DECORATION_STAIRS_1.asItem())
            .add(GCBlocks.TIN_DECORATION_STAIRS_2.asItem())
            .add(GCBlocks.MOON_ROCK_STAIRS.asItem())
            .add(GCBlocks.MOON_DUNGEON_BRICK_STAIRS.asItem())
            .add(MarsBlocks.MARS_COBBLESTONE_STAIRS.asItem())
            .add(MarsBlocks.MARS_DUNGEON_BRICK_STAIRS.asItem());

            this.tag(ItemTags.SLABS)
            .add(GCBlocks.TIN_DECORATION_SLAB_1.asItem())
            .add(GCBlocks.TIN_DECORATION_SLAB_2.asItem())
            .add(GCBlocks.MOON_ROCK_SLAB.asItem())
            .add(GCBlocks.MOON_DUNGEON_BRICK_SLAB.asItem())
            .add(MarsBlocks.MARS_COBBLESTONE_SLAB.asItem())
            .add(MarsBlocks.MARS_DUNGEON_BRICK_SLAB.asItem())
            .add(AsteroidBlocks.DARK_DECORATION_SLAB.asItem());

            this.tag(ItemTags.WALLS)
            .add(GCBlocks.TIN_DECORATION_WALL_1.asItem())
            .add(GCBlocks.TIN_DECORATION_WALL_2.asItem())
            .add(GCBlocks.MOON_ROCK_WALL.asItem())
            .add(GCBlocks.MOON_DUNGEON_BRICK_WALL.asItem())
            .add(MarsBlocks.MARS_COBBLESTONE_WALL.asItem())
            .add(MarsBlocks.MARS_DUNGEON_BRICK_WALL.asItem());
        }
    }

    public static class Recipe extends RecipeProvider
    {
        public Recipe(DataGenerator generator, String modid)
        {
            super(generator);
        }

        @Override
        protected void buildShapelessRecipes(Consumer<FinishedRecipe> consumer)
        {
            ShapedRecipeBuilder.shaped(GCItems.OXYGEN_FAN).define('Z', GCTags.STEEL_PLATES).define('Y', GCTags.BASIC_WAFERS).define('X', Tags.Items.DUSTS_REDSTONE).patternLine("Z Z").patternLine(" Y ").patternLine("ZXZ").addCriterion(this.toCriterion(GCTags.BASIC_WAFERS), this.has(GCTags.BASIC_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.AIR_LOCK_FRAME, 4).define('Z', GCItems.OXYGEN_CONCENTRATOR).define('Y', GCTags.STEEL_PLATES).define('X', GCTags.ALUMINUM_PLATES).pattern("XXX").pattern("YZY").pattern("XXX").unlocks(this.toCriterion(GCItems.OXYGEN_CONCENTRATOR), this.has(GCItems.OXYGEN_CONCENTRATOR)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.ALUMINUM_WIRE, 6).define('W', ItemTags.WOOL).define('C', GCTags.ALUMINUM_INGOTS).pattern("WWW").pattern("CCC").pattern("WWW").unlocks(this.toCriterion(GCTags.ALUMINUM_INGOTS), this.has(GCTags.ALUMINUM_INGOTS)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.HEAVY_ALUMINUM_WIRE).define('X', ItemTags.WOOL).define('Y', GCBlocks.ALUMINUM_WIRE).define('Z', GCTags.ALUMINUM_INGOTS).pattern("X").pattern("Y").pattern("Z").group("heavy_aluminum_wire").unlocks(this.toCriterion(GCBlocks.ALUMINUM_WIRE), this.has(GCBlocks.ALUMINUM_WIRE)).save(consumer, this.modLoc("heavy_aluminum_wire_1"));
            ShapedRecipeBuilder.shaped(GCBlocks.HEAVY_ALUMINUM_WIRE).define('X', ItemTags.WOOL).define('Y', GCBlocks.ALUMINUM_WIRE).define('Z', GCTags.ALUMINUM_INGOTS).pattern("Z").pattern("Y").pattern("X").group("heavy_aluminum_wire").unlocks(this.toCriterion(GCBlocks.ALUMINUM_WIRE), this.has(GCBlocks.ALUMINUM_WIRE)).save(consumer, this.modLoc("heavy_aluminum_wire_2"));
            ShapedRecipeBuilder.shaped(GCItems.AMBIENT_THERMAL_CONTROLLER).define('X', GCTags.STEEL_PLATES).define('Y', GCTags.BRONZE_PLATES).define('Z', GCTags.BASIC_WAFERS).define('W', Tags.Items.DUSTS_REDSTONE).key('V', GCItems.OXYGEN_VENT).patternLine("WVW").patternLine("YXY").patternLine("YZY").addCriterion(this.toCriterion(GCTags.BASIC_WAFERS), this.has(GCTags.BASIC_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.ALUMINUM_BLOCK).define('X', GCItems.ALUMINUM_INGOT).pattern("XXX").pattern("XXX").pattern("XXX").unlocks(this.toCriterion(GCItems.ALUMINUM_INGOT), this.has(GCItems.ALUMINUM_INGOT)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.COPPER_BLOCK).define('X', GCItems.COPPER_INGOT).pattern("XXX").pattern("XXX").pattern("XXX").unlocks(this.toCriterion(GCItems.COPPER_INGOT), this.has(GCItems.COPPER_INGOT)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.METEORIC_IRON_BLOCK).define('X', GCItems.METEORIC_IRON_INGOT).pattern("XXX").pattern("XXX").pattern("XXX").unlocks(this.toCriterion(GCItems.METEORIC_IRON_INGOT), this.has(GCItems.METEORIC_IRON_INGOT)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.SILICON_BLOCK).define('X', GCItems.RAW_SILICON).pattern("XXX").pattern("XXX").pattern("XXX").unlocks(this.toCriterion(GCItems.RAW_SILICON), this.has(GCItems.RAW_SILICON)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.TIN_BLOCK).define('X', GCItems.TIN_INGOT).pattern("XXX").pattern("XXX").pattern("XXX").unlocks(this.toCriterion(GCItems.TIN_INGOT), this.has(GCItems.TIN_INGOT)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.BUGGY_SEAT).define('X', GCTags.STEEL_PLATES).define('Y', GCTags.IRON_PLATES).pattern("  X").pattern(" YX").pattern("XXX").unlocks(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.BUGGY_STORAGE_BOX).define('X', GCTags.STEEL_PLATES).define('Y', GCTags.IRON_PLATES).define('Z', Tags.Items.CHESTS_WOODEN).patternLine("XXX").patternLine("YZY").patternLine("XXX").addCriterion(this.toCriterion(GCTags.IRON_PLATES), this.has(GCTags.IRON_PLATES)).build(consumer);
            ShapedRecipeBuilder.shaped(GCItems.BUGGY_WHEEL).define('X', Tags.Items.LEATHER).key('Y', GCTags.STEEL_PLATES).patternLine(" X ").patternLine("XYX").patternLine(" X ").addCriterion(this.toCriterion(Tags.Items.LEATHER), this.has(Tags.Items.LEATHER)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.ROCKET_LAUNCH_PAD, 9).define('X', Tags.Items.STORAGE_BLOCKS_IRON).key('Y', GCTags.IRON_PLATES).patternLine("YYY").patternLine("XXX").addCriterion(this.toCriterion(Tags.Items.STORAGE_BLOCKS_IRON), this.has(Tags.Items.STORAGE_BLOCKS_IRON)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.BUGGY_FUELING_PAD, 9).define('X', Tags.Items.STORAGE_BLOCKS_IRON).key('Y', GCTags.STEEL_PLATES).patternLine("YYY").patternLine("XXX").addCriterion(this.toCriterion(Tags.Items.STORAGE_BLOCKS_IRON), this.has(Tags.Items.STORAGE_BLOCKS_IRON)).build(consumer);
            ShapedRecipeBuilder.shaped(GCItems.CANVAS).define('X', Tags.Items.STRING).key('Y', Tags.Items.RODS_WOODEN).patternLine(" XY").patternLine("XXX").patternLine("YX ").addCriterion(this.toCriterion(Tags.Items.STRING), this.has(Tags.Items.STRING)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.CARGO_LOADER).define('X', GCTags.STEEL_PLATES).define('Y', GCTags.ALUMINUM_PLATES).define('Z', Tags.Items.CHESTS_WOODEN).key('W', Items.HOPPER).patternLine("XWX").patternLine("YZY").patternLine("XXX").addCriterion(this.toCriterion(Items.HOPPER), this.has(Items.HOPPER)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.CARGO_UNLOADER).define('X', GCTags.STEEL_PLATES).define('Y', GCTags.ALUMINUM_PLATES).define('Z', Tags.Items.CHESTS_WOODEN).key('W', Items.HOPPER).patternLine("XXX").patternLine("YZY").patternLine("XWX").addCriterion(this.toCriterion(Items.HOPPER), this.has(Items.HOPPER)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.CHEESE_BLOCK).define('X', Items.MILK_BUCKET).define('Y', GCItems.CHEESE_CURD).pattern("YYY").pattern("YXY").pattern("YYY").unlocks(this.toCriterion(GCItems.CHEESE_CURD), this.has(GCItems.CHEESE_CURD)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.CIRCUIT_FABRICATOR).define('X', Items.LEVER).define('Y', Items.FURNACE).define('Z', Items.REDSTONE_TORCH).define('W', GCTags.ALUMINUM_INGOTS).define('V', GCBlocks.ALUMINUM_WIRE).define('U', Items.STONE_BUTTON).pattern("WXW").pattern("UYU").pattern("VZV").unlocks(this.toCriterion(GCTags.ALUMINUM_INGOTS), this.has(GCTags.ALUMINUM_INGOTS)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.ENERGY_STORAGE_CLUSTER).define('X', GCBlocks.ENERGY_STORAGE).define('Y', GCTags.STEEL_PLATES).define('Z', GCTags.ADVANCED_WAFERS).pattern("XYX").pattern("YZY").pattern("XYX").unlocks(this.toCriterion(GCTags.ADVANCED_WAFERS), this.has(GCTags.ADVANCED_WAFERS)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.COAL_GENERATOR).define('X', Tags.Items.INGOTS_IRON).key('Y', GCBlocks.ALUMINUM_WIRE).key('Z', Items.FURNACE).key('W', GCTags.COPPER_INGOTS).patternLine("WWW").patternLine("XZX").patternLine("XYX").addCriterion(this.toCriterion(Items.FURNACE), this.has(Items.FURNACE)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.OXYGEN_COLLECTOR).define('X', GCItems.TIN_CANISTER).define('Y', GCItems.OXYGEN_FAN).define('Z', GCItems.OXYGEN_VENT).define('W', GCTags.STEEL_PLATES).define('V', GCItems.OXYGEN_CONCENTRATOR).define('U', GCTags.ALUMINUM_PLATES).pattern("WWW").pattern("YXZ").pattern("UVU").unlocks(this.toCriterion(GCItems.OXYGEN_CONCENTRATOR), this.has(GCItems.OXYGEN_CONCENTRATOR)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.EMERGENCY_POST).define('X', GCBlocks.GLOWSTONE_TORCH).define('Y', GCTags.TIN_PLATES).pattern("XYX").pattern("Y Y").pattern("XYX").unlocks(this.toCriterion(GCTags.TIN_PLATES), this.has(GCTags.TIN_PLATES)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.HIDDEN_REDSTONE_WIRE, 4).define('X', GCBlocks.TIN_DECORATION_BLOCK_1).define('Y', Tags.Items.DUSTS_REDSTONE).patternLine(" X ").patternLine("XYX").patternLine(" X ").addCriterion(this.toCriterion(Tags.Items.DUSTS_REDSTONE), this.has(Tags.Items.DUSTS_REDSTONE)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.HIDDEN_REDSTONE_REPEATER).define('X', GCBlocks.TIN_DECORATION_BLOCK_1).define('Y', Items.REPEATER).pattern("XYX").unlocks(this.toCriterion(Items.REPEATER), this.has(Items.REPEATER)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.TIN_DECORATION_BLOCK_1, 4).define('X', Tags.Items.STONE).key('Y', GCTags.TIN_PLATES).patternLine("XX ").patternLine("XXY").patternLine("   ").setGroup("tin_decoration").addCriterion(this.toCriterion(Tags.Items.STONE), this.has(Tags.Items.STONE)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.TIN_DECORATION_BLOCK_2, 4).define('X', Tags.Items.STONE).key('Y', GCTags.TIN_PLATES).patternLine("XX ").patternLine("XX ").patternLine(" Y ").setGroup("tin_decoration").addCriterion(this.toCriterion(Tags.Items.STONE), this.has(Tags.Items.STONE)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.DECONSTRUCTOR).define('X', Items.ANVIL).define('Y', Items.FURNACE).define('Z', Items.SHEARS).define('U', GCBlocks.ALUMINUM_WIRE).define('V', GCTags.STEEL_PLATES).pattern("VZV").pattern("UXU").pattern("VYV").unlocks(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.OXYGEN_BUBBLE_DISTRIBUTOR).define('X', GCItems.OXYGEN_FAN).define('Y', GCItems.OXYGEN_VENT).define('Z', GCTags.ALUMINUM_PLATES).define('W', GCTags.STEEL_PLATES).pattern("WXW").pattern("YZY").pattern("WXW").unlocks(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.ELECTRIC_COMPRESSOR).define('X', GCTags.TIN_PLATES).define('Y', GCBlocks.COMPRESSOR).define('Z', GCTags.ADVANCED_WAFERS).define('W', GCTags.STEEL_PLATES).define('V', GCBlocks.ALUMINUM_WIRE).pattern("WXW").pattern("WYW").pattern("VZV").unlocks(this.toCriterion(GCTags.ADVANCED_WAFERS), this.has(GCTags.ADVANCED_WAFERS)).save(consumer, this.modLoc("electric_compressor_from_compressor"));
            ShapedRecipeBuilder.shaped(GCItems.FLAG).define('X', GCItems.STEEL_POLE).define('Y', GCItems.CANVAS).pattern("XYY").pattern("XYY").pattern("X  ").unlocks(this.toCriterion(GCItems.CANVAS), this.has(GCItems.CANVAS)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.FLUID_PIPE, 6).define('X', Tags.Items.GLASS_PANES_COLORLESS).patternLine("XXX").patternLine("   ").patternLine("XXX").addCriterion(this.toCriterion(Tags.Items.GLASS_PANES_COLORLESS), this.has(Tags.Items.GLASS_PANES_COLORLESS)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.FLUID_TANK).define('X', Tags.Items.GLASS_PANES_COLORLESS).patternLine(" X ").patternLine("X X").patternLine("XXX").addCriterion(this.toCriterion(Tags.Items.GLASS_PANES_COLORLESS), this.has(Tags.Items.GLASS_PANES_COLORLESS)).build(consumer);
            ShapedRecipeBuilder.shaped(GCItems.FREQUENCY_MODULE).define('X', GCTags.ALUMINUM_PLATES).define('Y', GCTags.IRON_PLATES).define('Z', Tags.Items.DUSTS_REDSTONE).key('W', GCTags.BASIC_WAFERS).key('U', Items.REPEATER).patternLine(" X ").patternLine("YUY").patternLine("ZWZ").addCriterion(this.toCriterion(GCTags.BASIC_WAFERS), this.has(GCTags.BASIC_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.FUEL_LOADER).define('X', GCTags.COPPER_PLATES).define('Y', GCTags.BASIC_WAFERS).define('Z', GCItems.TIN_CANISTER).define('W', GCTags.TIN_PLATES).pattern("XXX").pattern("XZX").pattern("WYW").unlocks(this.toCriterion(GCTags.BASIC_WAFERS), this.has(GCTags.BASIC_WAFERS)).save(consumer);
            ShapedRecipeBuilder.shaped(Blocks.FURNACE).define('X', GCBlocks.MOON_ROCK).pattern("XXX").pattern("X X").pattern("XXX").unlocks(this.toCriterion(GCBlocks.MOON_ROCK), this.has(GCBlocks.MOON_ROCK)).save(consumer, "furnace_from_moon_rock");
            ShapedRecipeBuilder.shaped(GCBlocks.ELECTRIC_FURNACE).define('X', GCTags.STEEL_PLATES).define('Y', GCTags.BASIC_WAFERS).define('Z', Items.FURNACE).define('W', GCTags.ALUMINUM_PLATES).pattern("XXX").pattern("XZX").pattern("WYW").unlocks(this.toCriterion(GCTags.BASIC_WAFERS), this.has(GCTags.BASIC_WAFERS)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.GLOWSTONE_TORCH, 4).define('X', Tags.Items.RODS_WOODEN).key('Y', Tags.Items.DUSTS_GLOWSTONE).patternLine("Y").patternLine("X").addCriterion(this.toCriterion(Tags.Items.DUSTS_GLOWSTONE), this.has(Tags.Items.DUSTS_GLOWSTONE)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.COMPRESSOR).define('X', Items.ANVIL).define('Y', GCTags.COPPER_INGOTS).define('Z', GCTags.BASIC_WAFERS).define('W', GCTags.ALUMINUM_INGOTS).pattern("WXW").pattern("WYW").pattern("WZW").unlocks(this.toCriterion(GCTags.COPPER_INGOTS), this.has(GCTags.COPPER_INGOTS)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.ELECTRIC_COMPRESSOR).define('X', Items.ANVIL).define('Y', GCTags.BRONZE_PLATES).define('Z', GCTags.ADVANCED_WAFERS).define('W', GCTags.STEEL_PLATES).define('V', GCBlocks.ALUMINUM_WIRE).pattern("WXW").pattern("WYW").pattern("VZV").unlocks(this.toCriterion(GCTags.ADVANCED_WAFERS), this.has(GCTags.ADVANCED_WAFERS)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.ADVANCED_COMPRESSOR).define('X', Items.ANVIL).define('Y', GCTags.METEORIC_IRON_PLATES).define('Z', GCTags.ADVANCED_WAFERS).define('W', GCTags.STEEL_PLATES).define('V', GCBlocks.ALUMINUM_WIRE).pattern("WXW").pattern("WYW").pattern("VZV").unlocks(this.toCriterion(GCTags.ADVANCED_WAFERS), this.has(GCTags.ADVANCED_WAFERS)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.NOSE_CONE).define('X', GCItems.TIER_1_HEAVY_DUTY_PLATE).define('Y', Items.REDSTONE_TORCH).pattern(" Y ").pattern(" X ").pattern("X X").unlocks(this.toCriterion(GCItems.TIER_1_HEAVY_DUTY_PLATE), this.has(GCItems.TIER_1_HEAVY_DUTY_PLATE)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.OXYGEN_COMPRESSOR).define('X', GCTags.STEEL_PLATES).define('Y', GCTags.BRONZE_PLATES).define('Z', GCItems.OXYGEN_CONCENTRATOR).define('W', GCTags.ALUMINUM_PLATES).pattern("XWX").pattern("WZW").pattern("XYX").unlocks(this.toCriterion(GCItems.OXYGEN_CONCENTRATOR), this.has(GCItems.OXYGEN_CONCENTRATOR)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.OXYGEN_DECOMPRESSOR).define('X', GCTags.STEEL_PLATES).define('Y', Items.REDSTONE_TORCH).define('Z', GCItems.OXYGEN_CONCENTRATOR).define('W', GCTags.ALUMINUM_PLATES).define('V', GCItems.OXYGEN_FAN).pattern("XVX").pattern("WZW").pattern("XYX").unlocks(this.toCriterion(GCItems.OXYGEN_CONCENTRATOR), this.has(GCItems.OXYGEN_CONCENTRATOR)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.OXYGEN_CONCENTRATOR).define('X', GCItems.OXYGEN_VENT).define('Y', GCItems.TIN_CANISTER).define('Z', GCTags.STEEL_PLATES).define('W', GCTags.TIN_PLATES).pattern("ZWZ").pattern("WYW").pattern("ZXZ").unlocks(this.toCriterion(GCItems.TIN_CANISTER), this.has(GCItems.TIN_CANISTER)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.OXYGEN_DETECTOR).define('X', GCTags.ALUMINUM_PLATES).define('Y', GCItems.OXYGEN_VENT).define('Z', Tags.Items.DUSTS_REDSTONE).key('W', GCTags.STEEL_PLATES).key('V', GCTags.BASIC_WAFERS).patternLine("WWW").patternLine("YVY").patternLine("ZXZ").addCriterion(this.toCriterion(GCTags.BASIC_WAFERS), this.has(GCTags.BASIC_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shaped(GCItems.OXYGEN_GEAR).define('X', GCItems.OXYGEN_CONCENTRATOR).define('Y', GCBlocks.FLUID_PIPE).pattern(" Y ").pattern("YXY").pattern("Y Y").unlocks(this.toCriterion(GCItems.OXYGEN_CONCENTRATOR), this.has(GCItems.OXYGEN_CONCENTRATOR)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.OXYGEN_MASK).define('X', Tags.Items.GLASS_PANES_COLORLESS).key('Y', Items.IRON_HELMET).patternLine("XXX").patternLine("XYX").patternLine("XXX").addCriterion(this.toCriterion(Items.IRON_HELMET), this.has(Items.IRON_HELMET)).build(consumer);
            ShapedRecipeBuilder.shaped(GCItems.HEAVY_OXYGEN_TANK).define('X', GCItems.TIN_CANISTER).define('Y', GCTags.STEEL_PLATES).define('Z', Items.RED_WOOL).pattern("ZZZ").pattern("XXX").pattern("YYY").unlocks(this.toCriterion(GCItems.TIN_CANISTER), this.has(GCItems.TIN_CANISTER)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.LIGHT_OXYGEN_TANK).define('X', GCItems.TIN_CANISTER).define('Y', GCTags.COPPER_PLATES).define('Z', Items.LIME_WOOL).pattern("ZZZ").pattern("XXX").pattern("YYY").unlocks(this.toCriterion(GCItems.TIN_CANISTER), this.has(GCItems.TIN_CANISTER)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.MEDIUM_OXYGEN_TANK).define('X', GCItems.TIN_CANISTER).define('Y', GCTags.TIN_PLATES).define('Z', Items.ORANGE_WOOL).pattern("ZZZ").pattern("XXX").pattern("YYY").unlocks(this.toCriterion(GCItems.TIN_CANISTER), this.has(GCItems.TIN_CANISTER)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.CHROMATIC_APPLICATOR).define('A', Tags.Items.DYES_RED).key('B', Tags.Items.DYES_MAGENTA).key('C', Tags.Items.DYES_BLUE)
            .key('D', Tags.Items.DYES_ORANGE).key('E', GCTags.STEEL_PLATES).key('F', Tags.Items.DYES_CYAN)
            .key('G', Tags.Items.DYES_YELLOW).key('H', Tags.Items.DYES_LIME).key('I', Tags.Items.DYES_GREEN)
            .patternLine("ABC").patternLine("DEF").patternLine("GHI").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.HYDRAULIC_PLATFORM, 4).define('X', GCTags.BASIC_WAFERS).define('Y', Tags.Items.DUSTS_GLOWSTONE).key('Z', GCTags.STEEL_PLATES).key('W', Items.PISTON).patternLine("WYW").patternLine("ZXZ").patternLine("WYW").addCriterion(this.toCriterion(Items.PISTON), this.has(Items.PISTON)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.REFINERY).define('X', GCTags.STEEL_PLATES).define('Y', Items.FURNACE).define('Z', GCItems.COPPER_CANISTER).define('W', Tags.Items.STONE).patternLine(" Z ").patternLine("WZW").patternLine("XYX").addCriterion(this.toCriterion(Items.FURNACE), this.has(Items.FURNACE)).build(consumer);
            ShapedRecipeBuilder.shaped(GCItems.ROCKET_FINS).define('X', GCItems.TIER_1_HEAVY_DUTY_PLATE).define('Y', GCTags.STEEL_PLATES).pattern(" Y ").pattern("XYX").pattern("X X").unlocks(this.toCriterion(GCItems.TIER_1_HEAVY_DUTY_PLATE), this.has(GCItems.TIER_1_HEAVY_DUTY_PLATE)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.ROCKET_WORKBENCH).define('X', GCTags.STEEL_PLATES).define('Y', Items.CRAFTING_TABLE).define('Z', Items.LEVER).define('W', GCTags.ADVANCED_WAFERS).define('V', Items.REDSTONE_TORCH).pattern("XYX").pattern("ZWZ").pattern("XVX").unlocks(this.toCriterion(GCTags.ADVANCED_WAFERS), this.has(GCTags.ADVANCED_WAFERS)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.OXYGEN_SEALER).define('X', GCItems.OXYGEN_FAN).define('Y', GCItems.OXYGEN_VENT).define('Z', GCTags.STEEL_PLATES).define('W', GCTags.ALUMINUM_PLATES).pattern("WZW").pattern("YXY").pattern("WZW").unlocks(this.toCriterion(GCTags.ALUMINUM_PLATES), this.has(GCTags.ALUMINUM_PLATES)).save(consumer);
            ShapedRecipeBuilder.shaped(Items.SLIME_BALL).define('X', Tags.Items.DYES_GREEN).key('Y', Items.SUGAR).key('Z', GCItems.CHEESE_CURD).patternLine("XYX").patternLine("YZY").patternLine("XYX").addCriterion(this.toCriterion(GCItems.CHEESE_CURD), this.has(GCItems.CHEESE_CURD)).build(consumer, this.modLoc("slime_ball_from_cheese"));
            ShapedRecipeBuilder.shaped(GCBlocks.BASIC_SOLAR_PANEL).define('X', GCTags.STEEL_PLATES).define('Y', GCItems.FULL_SOLAR_MODULE).define('Z', GCItems.STEEL_POLE).define('W', GCTags.BASIC_WAFERS).define('V', GCBlocks.ALUMINUM_WIRE).pattern("XYX").pattern("XZX").pattern("VWV").unlocks(this.toCriterion(GCItems.FULL_SOLAR_MODULE), this.has(GCItems.FULL_SOLAR_MODULE)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.ADVANCED_SOLAR_PANEL).define('X', GCTags.STEEL_PLATES).define('Y', GCItems.FULL_SOLAR_MODULE).define('Z', GCItems.STEEL_POLE).define('W', GCTags.ADVANCED_WAFERS).define('V', GCBlocks.HEAVY_ALUMINUM_WIRE).pattern("XYX").pattern("XZX").pattern("VWV").unlocks(this.toCriterion(GCItems.FULL_SOLAR_MODULE), this.has(GCItems.FULL_SOLAR_MODULE)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.SINGLE_SOLAR_MODULE, 2).define('X', Tags.Items.GLASS_COLORLESS).key('Y', GCTags.SOLAR_WAFERS).key('Z', GCBlocks.ALUMINUM_WIRE).patternLine("XXX").patternLine("YYY").patternLine("ZZZ").addCriterion(this.toCriterion(GCTags.SOLAR_WAFERS), this.has(GCTags.SOLAR_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shaped(GCItems.FULL_SOLAR_MODULE).define('X', GCItems.SINGLE_SOLAR_MODULE).define('Y', GCBlocks.ALUMINUM_WIRE).pattern("XXX").pattern("YYY").pattern("XXX").unlocks(this.toCriterion(GCItems.SINGLE_SOLAR_MODULE), this.has(GCItems.SINGLE_SOLAR_MODULE)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.STANDARD_WRENCH).define('X', GCTags.BRONZE_PLATES).define('Y', GCTags.STEEL_PLATES).pattern("  Y").pattern(" X ").pattern("X  ").unlocks(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.HEAVY_DUTY_AXE).define('#', Tags.Items.RODS_WOODEN).key('X', GCTags.STEEL_PLATES).patternLine("XX").patternLine("X#").patternLine(" #").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shaped(GCItems.HEAVY_DUTY_BOOTS).define('X', GCTags.STEEL_PLATES).pattern("X X").pattern("X X").unlocks(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.HEAVY_DUTY_CHESTPLATE).define('X', GCTags.STEEL_PLATES).pattern("X X").pattern("XXX").pattern("XXX").unlocks(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.HEAVY_DUTY_HELMET).define('X', GCTags.STEEL_PLATES).pattern("XXX").pattern("X X").unlocks(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.HEAVY_DUTY_HOE).define('#', Tags.Items.RODS_WOODEN).key('X', GCTags.STEEL_PLATES).patternLine("XX").patternLine(" #").patternLine(" #").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shaped(GCItems.HEAVY_DUTY_LEGGINGS).define('X', GCTags.STEEL_PLATES).pattern("XXX").pattern("X X").pattern("X X").unlocks(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.HEAVY_DUTY_PICKAXE).define('#', Tags.Items.RODS_WOODEN).key('X', GCTags.STEEL_PLATES).patternLine("XXX").patternLine(" # ").patternLine(" # ").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shaped(GCItems.HEAVY_DUTY_SHOVEL).define('#', Tags.Items.RODS_WOODEN).key('X', GCTags.STEEL_PLATES).patternLine("X").patternLine("#").patternLine("#").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shaped(GCItems.HEAVY_DUTY_SWORD).define('#', Tags.Items.RODS_WOODEN).key('X', GCTags.STEEL_PLATES).patternLine("X").patternLine("X").patternLine("#").addCriterion(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.TELEMETRY_UNIT).define('X', GCTags.TIN_PLATES).define('Y', GCTags.COPPER_PLATES).define('Z', GCTags.BASIC_WAFERS).define('W', GCItems.FREQUENCY_MODULE).pattern("XWX").pattern("XZX").pattern("YYY").unlocks(this.toCriterion(GCItems.FREQUENCY_MODULE), this.has(GCItems.FREQUENCY_MODULE)).save(consumer);
            ShapedRecipeBuilder.shaped(GCItems.TIER_1_ROCKET_ENGINE).define('X', GCItems.TIER_1_HEAVY_DUTY_PLATE).define('Y', Items.FLINT_AND_STEEL).define('Z', GCItems.OXYGEN_VENT).define('W', GCItems.TIN_CANISTER).define('V', Items.STONE_BUTTON).pattern(" YV").pattern("XWX").pattern("XZX").unlocks(this.toCriterion(GCItems.TIER_1_HEAVY_DUTY_PLATE), this.has(GCItems.TIER_1_HEAVY_DUTY_PLATE)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.DISPLAY_SCREEN).define('X', GCTags.STEEL_PLATES).define('Y', GCTags.BASIC_WAFERS).define('Z', Tags.Items.GLASS_COLORLESS).patternLine("XYX").patternLine("YZY").patternLine("XYX").addCriterion(this.toCriterion(GCTags.BASIC_WAFERS), this.has(GCTags.BASIC_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shaped(GCItems.BATTERY).define('T', GCTags.TIN_PLATES).define('C', Items.COAL).define('R', Tags.Items.DUSTS_REDSTONE).patternLine(" T ").patternLine("TRT").patternLine("TCT").addCriterion(this.toCriterion(GCTags.TIN_PLATES), this.has(GCTags.TIN_PLATES)).build(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.TIN_DECORATION_STAIRS_1, 4).define('#', GCBlocks.TIN_DECORATION_BLOCK_1).pattern("#  ").pattern("## ").pattern("###").group("tin_stairs").unlocks(this.toCriterion(GCBlocks.TIN_DECORATION_BLOCK_1), this.has(GCBlocks.TIN_DECORATION_BLOCK_1)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.TIN_DECORATION_STAIRS_2, 4).define('#', GCBlocks.TIN_DECORATION_BLOCK_2).pattern("#  ").pattern("## ").pattern("###").group("tin_stairs").unlocks(this.toCriterion(GCBlocks.TIN_DECORATION_BLOCK_2), this.has(GCBlocks.TIN_DECORATION_BLOCK_2)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.MOON_ROCK_STAIRS, 4).define('#', GCBlocks.MOON_ROCK).pattern("#  ").pattern("## ").pattern("###").unlocks(this.toCriterion(GCBlocks.MOON_ROCK), this.has(GCBlocks.MOON_ROCK)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.MOON_DUNGEON_BRICK_STAIRS, 4).define('#', GCBlocks.MOON_DUNGEON_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlocks(this.toCriterion(GCBlocks.MOON_DUNGEON_BRICKS), this.has(GCBlocks.MOON_DUNGEON_BRICKS)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.TIN_DECORATION_SLAB_1, 6).define('#', GCBlocks.TIN_DECORATION_BLOCK_1).pattern("###").group("tin_slab").unlocks(this.toCriterion(GCBlocks.TIN_DECORATION_BLOCK_1), this.has(GCBlocks.TIN_DECORATION_BLOCK_1)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.TIN_DECORATION_SLAB_2, 6).define('#', GCBlocks.TIN_DECORATION_BLOCK_2).pattern("###").group("tin_slab").unlocks(this.toCriterion(GCBlocks.TIN_DECORATION_BLOCK_2), this.has(GCBlocks.TIN_DECORATION_BLOCK_2)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.MOON_ROCK_SLAB, 6).define('#', GCBlocks.MOON_ROCK).pattern("###").unlocks(this.toCriterion(GCBlocks.MOON_ROCK), this.has(GCBlocks.MOON_ROCK)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.MOON_DUNGEON_BRICK_SLAB, 6).define('#', GCBlocks.MOON_DUNGEON_BRICKS).pattern("###").unlocks(this.toCriterion(GCBlocks.MOON_DUNGEON_BRICKS), this.has(GCBlocks.MOON_DUNGEON_BRICKS)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.TIN_DECORATION_WALL_1, 6).define('#', GCBlocks.TIN_DECORATION_BLOCK_1).pattern("###").pattern("###").group("tin_wall").unlocks(this.toCriterion(GCBlocks.TIN_DECORATION_BLOCK_1), this.has(GCBlocks.TIN_DECORATION_BLOCK_1)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.TIN_DECORATION_WALL_2, 6).define('#', GCBlocks.TIN_DECORATION_BLOCK_2).pattern("###").pattern("###").group("tin_wall").unlocks(this.toCriterion(GCBlocks.TIN_DECORATION_BLOCK_2), this.has(GCBlocks.TIN_DECORATION_BLOCK_2)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.MOON_ROCK_WALL, 6).define('#', GCBlocks.MOON_ROCK).pattern("###").pattern("###").unlocks(this.toCriterion(GCBlocks.MOON_ROCK), this.has(GCBlocks.MOON_ROCK)).save(consumer);
            ShapedRecipeBuilder.shaped(GCBlocks.MOON_DUNGEON_BRICK_WALL, 6).define('#', GCBlocks.MOON_DUNGEON_BRICKS).pattern("###").pattern("###").unlocks(this.toCriterion(GCBlocks.MOON_DUNGEON_BRICKS), this.has(GCBlocks.MOON_DUNGEON_BRICKS)).save(consumer);

            ShapelessRecipeBuilder.shapeless(GCItems.OXYGEN_VENT).requires(GCTags.TIN_PLATES).requires(GCTags.TIN_PLATES).requires(GCTags.TIN_PLATES).requires(GCTags.STEEL_PLATES).unlocks(this.toCriterion(GCTags.STEEL_PLATES), this.has(GCTags.STEEL_PLATES)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCBlocks.SWITCHABLE_ALUMINUM_WIRE).requires(Items.REPEATER).requires(GCBlocks.ALUMINUM_WIRE).unlocks(this.toCriterion(GCBlocks.ALUMINUM_WIRE), this.has(GCBlocks.ALUMINUM_WIRE)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCBlocks.SWITCHABLE_HEAVY_ALUMINUM_WIRE).requires(Items.REPEATER).requires(GCBlocks.HEAVY_ALUMINUM_WIRE).unlocks(this.toCriterion(GCBlocks.HEAVY_ALUMINUM_WIRE), this.has(GCBlocks.HEAVY_ALUMINUM_WIRE)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.GROUND_BEEF, 2).requires(Items.BEEF).unlocks(this.toCriterion(Items.BEEF), this.has(Items.BEEF)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.BURGER_BUN, 2).requires(Items.WHEAT).requires(Items.WHEAT).requires(Items.EGG).requires(Items.MILK_BUCKET).unlocks(this.toCriterion(Items.WHEAT), this.has(Items.WHEAT)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.CANNED_BEEF).requires(GCItems.TIN_CANISTER).requires(GCItems.GROUND_BEEF).requires(GCItems.GROUND_BEEF).unlocks(this.toCriterion(GCItems.GROUND_BEEF), this.has(GCItems.GROUND_BEEF)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.CHEESE_SLICE, 6).requires(GCBlocks.CHEESE_BLOCK).unlocks(this.toCriterion(GCBlocks.CHEESE_BLOCK), this.has(GCBlocks.CHEESE_BLOCK)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.CHEESEBURGER).requires(GCItems.CHEESE_SLICE).requires(GCItems.BURGER_BUN).requires(GCItems.COOKED_BEEF_PATTY).unlocks(this.toCriterion(GCItems.CHEESE_SLICE), this.has(GCItems.CHEESE_SLICE)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.DEHYDRATED_APPLES).requires(GCItems.TIN_CANISTER).requires(Items.APPLE).requires(Items.APPLE).group("dehydrated_food").unlocks(this.toCriterion(Items.APPLE), this.has(Items.APPLE)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.DEHYDRATED_CARROTS).requires(GCItems.TIN_CANISTER).requires(Items.CARROT).requires(Items.CARROT).group("dehydrated_food").unlocks(this.toCriterion(Items.CARROT), this.has(Items.CARROT)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.DEHYDRATED_MELONS).requires(GCItems.TIN_CANISTER).requires(Items.MELON_SLICE).requires(Items.MELON_SLICE).group("dehydrated_food").unlocks(this.toCriterion(Items.MELON_SLICE), this.has(Items.MELON_SLICE)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.DEHYDRATED_POTATOES).requires(GCItems.TIN_CANISTER).requires(Items.POTATO).requires(Items.POTATO).group("dehydrated_food").unlocks(this.toCriterion(Items.POTATO), this.has(Items.POTATO)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCBlocks.EMERGENCY_POST_KIT).requires(GCItems.SPACE_EMERGENCY_KIT).requires(GCBlocks.EMERGENCY_POST).unlocks(this.toCriterion(GCItems.SPACE_EMERGENCY_KIT), this.has(GCItems.SPACE_EMERGENCY_KIT)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.ALUMINUM_INGOT, 9).requires(GCBlocks.ALUMINUM_BLOCK).unlocks(this.toCriterion(GCBlocks.ALUMINUM_BLOCK), this.has(GCBlocks.ALUMINUM_BLOCK)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.TIN_INGOT, 9).requires(GCBlocks.TIN_BLOCK).unlocks(this.toCriterion(GCBlocks.TIN_BLOCK), this.has(GCBlocks.TIN_BLOCK)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.COPPER_INGOT, 9).requires(GCBlocks.COPPER_BLOCK).unlocks(this.toCriterion(GCBlocks.COPPER_BLOCK), this.has(GCBlocks.COPPER_BLOCK)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.METEORIC_IRON_INGOT, 9).requires(GCBlocks.METEORIC_IRON_BLOCK).unlocks(this.toCriterion(GCBlocks.METEORIC_IRON_BLOCK), this.has(GCBlocks.METEORIC_IRON_BLOCK)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.RAW_SILICON, 9).requires(GCBlocks.SILICON_BLOCK).unlocks(this.toCriterion(GCBlocks.SILICON_BLOCK), this.has(GCBlocks.SILICON_BLOCK)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCBlocks.MAGNETIC_CRAFTING_TABLE).requires(Blocks.CRAFTING_TABLE).requires(GCTags.IRON_PLATES).unlocks(this.toCriterion(GCTags.IRON_PLATES), this.has(GCTags.IRON_PLATES)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.METEOR_CHUNK, 3).requires(GCItems.RAW_METEORIC_IRON).unlocks(this.toCriterion(GCItems.RAW_METEORIC_IRON), this.has(GCItems.RAW_METEORIC_IRON)).save(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.WHITE_PARACHUTE).requires(GCItems.CANVAS).requires(Tags.Items.STRING).setGroup("parachute").addCriterion(this.toCriterion(GCItems.CANVAS), this.has(GCItems.CANVAS)).build(consumer, this.modLoc("white_parachute_from_canvas"));
            ShapelessRecipeBuilder.shapeless(GCItems.WHITE_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_WHITE).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.ORANGE_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_ORANGE).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.MAGENTA_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_MAGENTA).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.LIGHT_BLUE_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_LIGHT_BLUE).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.YELLOW_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_YELLOW).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.LIME_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_LIME).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.PINK_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_PINK).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.GRAY_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_GRAY).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.LIGHT_GRAY_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_LIGHT_GRAY).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.CYAN_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_CYAN).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.PURPLE_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_PURPLE).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.BLUE_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_BLUE).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.BROWN_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_BROWN).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.GREEN_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_GREEN).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.RED_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_RED).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.BLACK_PARACHUTE).requires(GCTags.PARACHUTES).requires(Tags.Items.DYES_BLACK).setGroup("parachute").addCriterion(this.toCriterion(GCTags.PARACHUTES), this.has(GCTags.PARACHUTES)).build(consumer);
            ShapelessRecipeBuilder.shapeless(GCItems.PRELAUNCH_CHECKLIST).requires(GCItems.CANVAS).requires(Tags.Items.DYES_RED).addCriterion(this.toCriterion(GCItems.CANVAS), this.has(GCItems.CANVAS)).build(consumer);

            SimpleCookingRecipeBuilder.smelting(Ingredient.of(GCTags.COPPER_ORES_ITEM), GCItems.COPPER_INGOT, 0.5F, 200).unlocks(this.toCriterion(GCTags.COPPER_ORES_ITEM), this.has(GCTags.COPPER_ORES_ITEM)).save(consumer, this.toSmelting(GCItems.COPPER_INGOT));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(GCTags.TIN_ORES_ITEM), GCItems.TIN_INGOT, 0.5F, 200).unlocks(this.toCriterion(GCTags.TIN_ORES_ITEM), this.has(GCTags.TIN_ORES_ITEM)).save(consumer, this.toSmelting(GCItems.TIN_INGOT));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(GCTags.SILICON_ORES_ITEM), GCItems.RAW_SILICON, 0.5F, 200).unlocks(this.toCriterion(GCTags.SILICON_ORES_ITEM), this.has(GCTags.SILICON_ORES_ITEM)).save(consumer, this.toSmelting(GCItems.RAW_SILICON));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(GCTags.ALUMINUM_ORES_ITEM), GCItems.ALUMINUM_INGOT, 0.5F, 200).unlocks(this.toCriterion(GCTags.ALUMINUM_ORES_ITEM), this.has(GCTags.ALUMINUM_ORES_ITEM)).save(consumer, this.toSmelting(GCItems.ALUMINUM_INGOT));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(GCItems.METEOR_CHUNK), GCItems.HOT_METEOR_CHUNK, 0.1F, 200).unlocks(this.toCriterion(GCItems.METEOR_CHUNK), this.has(GCItems.METEOR_CHUNK)).save(consumer, this.toSmelting(GCItems.HOT_METEOR_CHUNK));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(GCItems.RAW_METEORIC_IRON), GCItems.METEORIC_IRON_INGOT, 1.0F, 200).unlocks(this.toCriterion(GCItems.RAW_METEORIC_IRON), this.has(GCItems.RAW_METEORIC_IRON)).save(consumer, this.toSmelting(GCItems.METEORIC_IRON_INGOT));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(GCTags.SAPPHIRE_ORES_ITEM), GCItems.LUNAR_SAPPHIRE, 1.0F, 200).unlocks(this.toCriterion(GCTags.SAPPHIRE_ORES_ITEM), this.has(GCTags.SAPPHIRE_ORES_ITEM)).save(consumer, this.toSmelting(GCItems.LUNAR_SAPPHIRE));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(GCTags.CHEESE_ORES_ITEM), GCItems.CHEESE_CURD, 1.0F, 200).unlocks(this.toCriterion(GCTags.CHEESE_ORES_ITEM), this.has(GCTags.CHEESE_ORES_ITEM)).save(consumer, this.toSmelting(GCItems.CHEESE_CURD));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(GCItems.GROUND_BEEF), GCItems.COOKED_BEEF_PATTY, 1.0F, 200).unlocks(this.toCriterion(GCItems.GROUND_BEEF), this.has(GCItems.GROUND_BEEF)).save(consumer, this.toSmelting(GCItems.COOKED_BEEF_PATTY));

            // Recycling: smelt tin/copper canisters back into ingots
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(GCItems.TIN_CANISTER), GCItems.TIN_INGOT, 1.0F, 200).unlocks(this.toCriterion(GCItems.TIN_CANISTER), this.has(GCItems.TIN_CANISTER)).save(consumer, this.from(GCItems.TIN_INGOT, GCItems.TIN_CANISTER));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(GCItems.COPPER_CANISTER), GCItems.COPPER_INGOT, 1.0F, 200).unlocks(this.toCriterion(GCItems.COPPER_CANISTER), this.has(GCItems.COPPER_CANISTER)).save(consumer, this.from(GCItems.COPPER_INGOT, GCItems.COPPER_CANISTER));

            /*if (CompatibilityManager.useAluDust())
            {
                CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(GCItems.ic2compat, 1, 0), Ingredient.fromItems(GCItems.basicItem, 1, 5), 1.0F);
            }*/

            SimpleCookingRecipeBuilder.blasting(Ingredient.of(GCTags.COPPER_ORES_ITEM), GCItems.COPPER_INGOT, 0.5F, 100).unlocks(this.toCriterion(GCTags.COPPER_ORES_ITEM), this.has(GCTags.COPPER_ORES_ITEM)).save(consumer, this.toBlasting(GCItems.COPPER_INGOT));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(GCTags.TIN_ORES_ITEM), GCItems.TIN_INGOT, 0.5F, 100).unlocks(this.toCriterion(GCTags.TIN_ORES_ITEM), this.has(GCTags.TIN_ORES_ITEM)).save(consumer, this.toBlasting(GCItems.TIN_INGOT));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(GCTags.SILICON_ORES_ITEM), GCItems.RAW_SILICON, 0.5F, 100).unlocks(this.toCriterion(GCTags.SILICON_ORES_ITEM), this.has(GCTags.SILICON_ORES_ITEM)).save(consumer, this.toBlasting(GCItems.RAW_SILICON));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(GCTags.ALUMINUM_ORES_ITEM), GCItems.ALUMINUM_INGOT, 0.5F, 100).unlocks(this.toCriterion(GCTags.ALUMINUM_ORES_ITEM), this.has(GCTags.ALUMINUM_ORES_ITEM)).save(consumer, this.toBlasting(GCItems.ALUMINUM_INGOT));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(GCItems.RAW_METEORIC_IRON), GCItems.METEORIC_IRON_INGOT, 1.0F, 100).unlocks(this.toCriterion(GCItems.RAW_METEORIC_IRON), this.has(GCItems.RAW_METEORIC_IRON)).save(consumer, this.toBlasting(GCItems.METEORIC_IRON_INGOT));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(GCTags.SAPPHIRE_ORES_ITEM), GCItems.LUNAR_SAPPHIRE, 1.0F, 100).unlocks(this.toCriterion(GCTags.SAPPHIRE_ORES_ITEM), this.has(GCTags.SAPPHIRE_ORES_ITEM)).save(consumer, this.toBlasting(GCItems.LUNAR_SAPPHIRE));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(GCTags.CHEESE_ORES_ITEM), GCItems.CHEESE_CURD, 1.0F, 100).unlocks(this.toCriterion(GCTags.CHEESE_ORES_ITEM), this.has(GCTags.CHEESE_ORES_ITEM)).save(consumer, this.toBlasting(GCItems.CHEESE_CURD));

            SimpleCookingRecipeBuilder.cooking(Ingredient.of(GCItems.GROUND_BEEF), GCItems.COOKED_BEEF_PATTY, 1.0F, 100, RecipeSerializer.SMOKING_RECIPE).unlocks(this.toCriterion(GCItems.GROUND_BEEF), this.has(GCItems.GROUND_BEEF)).save(consumer, this.modLoc(this.toString(GCItems.COOKED_BEEF_PATTY) + "_from_smoking"));
            SimpleCookingRecipeBuilder.cooking(Ingredient.of(GCItems.GROUND_BEEF), GCItems.COOKED_BEEF_PATTY, 1.0F, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlocks(this.toCriterion(GCItems.GROUND_BEEF), this.has(GCItems.GROUND_BEEF)).save(consumer, this.modLoc(this.toString(GCItems.COOKED_BEEF_PATTY) + "_from_campfire_cooking"));
        }

        protected String toCriterion(ItemLike provider)
        {
            return "has_" + this.toString(provider);
        }

        protected String toCriterion(Tag<?> tag)
        {
            return "has_" + tag.getId().getPath() + "_tag";
        }

        protected ResourceLocation toSmelting(ItemLike provider)
        {
            return this.modLoc(this.toString(provider) + "_from_smelting");
        }

        protected ResourceLocation toBlasting(ItemLike provider)
        {
            return this.modLoc(this.toString(provider) + "_from_blasting");
        }

        protected ResourceLocation from(ItemLike provider1, ItemLike provider2)
        {
            return this.modLoc(this.toString(provider1) + "_from_" + this.toString(provider2));
        }

        protected String toString(ItemLike provider)
        {
            return provider.asItem().getRegistryName().getPath();
        }

        protected ResourceLocation modLoc(String name)
        {
            return new ResourceLocation(Constants.MOD_ID_CORE, name);
        }
    }

    public static class LootTables extends LootTableProvider
    {
        private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables = Lists.newArrayList();

        public LootTables(DataGenerator generator)
        {
            super(generator);
            this.addTable(Pair.of(BlockLootTable::new, LootContextParamSets.BLOCK)).addTable(Pair.of(EntityLootTable::new, LootContextParamSets.ENTITY)).addTable(Pair.of(ChestLootTables::new, LootContextParamSets.CHEST));
        }

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables()
        {
            return Collections.unmodifiableList(this.tables);
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext tracker)
        {
            map.forEach((resource, loot) -> net.minecraft.world.level.storage.loot.LootTables.func_227508_a_(tracker, resource, loot));//validateLootTable
        }

        protected LootTables addTable(Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet> table)
        {
            this.tables.add(table);
            return this;
        }

        class BlockLootTable extends BlockLoot
        {
            @Override
            protected void addTables()
            {
                this.dropSelf(GCBlocks.COPPER_ORE);
                this.dropSelf(GCBlocks.TIN_ORE);
                this.dropSelf(GCBlocks.ALUMINUM_ORE);
                this.dropSelf(GCBlocks.MOON_COPPER_ORE);
                this.dropSelf(GCBlocks.MOON_TIN_ORE);
                this.dropSelf(GCBlocks.SAPPHIRE_ORE);
                this.dropSelf(GCBlocks.COPPER_BLOCK);
                this.dropSelf(GCBlocks.TIN_BLOCK);
                this.dropSelf(GCBlocks.ALUMINUM_BLOCK);
                this.dropSelf(GCBlocks.SILICON_BLOCK);
                this.dropSelf(GCBlocks.METEORIC_IRON_BLOCK);
                this.dropSelf(GCBlocks.MAGNETIC_CRAFTING_TABLE);
                this.dropSelf(GCBlocks.PARACHEST);
                this.dropOther(GCBlocks.PLAYER_DETECTOR, GCBlocks.TIN_DECORATION_BLOCK_1);
                this.dropSelf(GCBlocks.HIDDEN_REDSTONE_WIRE);
                this.dropSelf(GCBlocks.HIDDEN_REDSTONE_REPEATER);
                this.dropSelf(GCBlocks.EMERGENCY_POST);
                this.dropSelf(GCBlocks.EMERGENCY_POST_KIT);
                this.dropSelf(GCBlocks.MOON_ROCK);
                this.dropSelf(GCBlocks.MOON_DIRT);
                this.dropSelf(GCBlocks.MOON_DUNGEON_BRICKS);
                this.dropSelf(GCBlocks.AIR_LOCK_FRAME);
                this.dropSelf(GCBlocks.TIN_DECORATION_BLOCK_1);
                this.dropSelf(GCBlocks.TIN_DECORATION_BLOCK_2);
                this.dropSelf(GCBlocks.OXYGEN_DETECTOR);
                this.dropSelf(GCBlocks.TELEMETRY_UNIT);
                this.dropSelf(GCBlocks.ROCKET_LAUNCH_PAD);
                this.dropSelf(GCBlocks.BUGGY_FUELING_PAD);
                this.dropSelf(GCBlocks.OXYGEN_BUBBLE_DISTRIBUTOR);
                this.dropSelf(GCBlocks.OXYGEN_COLLECTOR);
                this.dropSelf(GCBlocks.COMPRESSOR);
                this.dropSelf(GCBlocks.COAL_GENERATOR);
                this.dropSelf(GCBlocks.CHROMATIC_APPLICATOR);
                this.dropSelf(GCBlocks.BASIC_SOLAR_PANEL);
                this.dropSelf(GCBlocks.ADVANCED_SOLAR_PANEL);
                this.dropSelf(GCBlocks.OXYGEN_COMPRESSOR);
                this.dropSelf(GCBlocks.OXYGEN_DECOMPRESSOR);
                this.dropSelf(GCBlocks.OXYGEN_SEALER);
                this.dropSelf(GCBlocks.ELECTRIC_COMPRESSOR);
                this.dropSelf(GCBlocks.ELECTRIC_FURNACE);
                this.dropSelf(GCBlocks.REFINERY);
                this.dropSelf(GCBlocks.CARGO_LOADER);
                this.dropSelf(GCBlocks.CARGO_UNLOADER);
                this.dropSelf(GCBlocks.AIR_LOCK_CONTROLLER);
                this.dropSelf(GCBlocks.ROCKET_WORKBENCH);
                this.dropSelf(GCBlocks.ARC_LAMP);
                this.dropSelf(GCBlocks.GLOWSTONE_TORCH);
                this.dropSelf(GCBlocks.UNLIT_TORCH);
                this.dropSelf(GCBlocks.ADVANCED_COMPRESSOR);
                this.dropSelf(GCBlocks.CIRCUIT_FABRICATOR);
                this.dropSelf(GCBlocks.OXYGEN_STORAGE_MODULE);
                this.dropSelf(GCBlocks.DECONSTRUCTOR);
                this.dropSelf(GCBlocks.FUEL_LOADER);
                this.dropSelf(GCBlocks.ENERGY_STORAGE);
                this.dropSelf(GCBlocks.ENERGY_STORAGE_CLUSTER);
                this.dropSelf(GCBlocks.ELECTRIC_ARC_FURNACE);
                this.dropSelf(GCBlocks.FLUID_PIPE);
                this.dropSelf(GCBlocks.ALUMINUM_WIRE);
                this.dropSelf(GCBlocks.HEAVY_ALUMINUM_WIRE);
                this.dropSelf(GCBlocks.SWITCHABLE_ALUMINUM_WIRE);
                this.dropSelf(GCBlocks.SWITCHABLE_HEAVY_ALUMINUM_WIRE);
                this.dropSelf(GCBlocks.DISPLAY_SCREEN);
                this.dropSelf(GCBlocks.FLUID_TANK);
                this.dropSelf(GCBlocks.HYDRAULIC_PLATFORM);
                this.dropSelf(GCBlocks.MOON_TURF);
                this.dropSelf(GCBlocks.TIN_DECORATION_STAIRS_1);
                this.dropSelf(GCBlocks.TIN_DECORATION_STAIRS_2);
                this.dropSelf(GCBlocks.MOON_ROCK_STAIRS);
                this.dropSelf(GCBlocks.MOON_DUNGEON_BRICK_STAIRS);
                this.dropSelf(GCBlocks.TIN_DECORATION_WALL_1);
                this.dropSelf(GCBlocks.TIN_DECORATION_WALL_2);
                this.dropSelf(GCBlocks.MOON_ROCK_WALL);
                this.dropSelf(GCBlocks.MOON_DUNGEON_BRICK_WALL);
                this.add(GCBlocks.TIN_DECORATION_SLAB_1, BlockLoot::droppingSlab);
                this.add(GCBlocks.TIN_DECORATION_SLAB_2, BlockLoot::droppingSlab);
                this.add(GCBlocks.MOON_ROCK_SLAB, BlockLoot::droppingSlab);
                this.add(GCBlocks.MOON_DUNGEON_BRICK_SLAB, BlockLoot::droppingSlab);

                this.add(GCBlocks.CHEESE_BLOCK, func_218482_a());
                this.add(GCBlocks.SILICON_ORE, block -> createOreDrop(block, GCItems.RAW_SILICON));
                this.add(GCBlocks.CHEESE_ORE, block -> createOreDrop(block, GCItems.CHEESE_CURD));
                this.add(GCBlocks.SAPPHIRE_ORE, block -> createOreDrop(block, GCItems.LUNAR_SAPPHIRE));
                this.add(GCBlocks.FALLEN_METEOR, createSingleItemTable(GCItems.RAW_METEORIC_IRON, RandomValueBounds.between(1.0F, 2.0F)));
                this.add(GCBlocks.FULL_ROCKET_LAUNCH_PAD, block -> createSingleItemTableWithSilkTouch(block, GCBlocks.ROCKET_LAUNCH_PAD, ConstantIntValue.exactly(9)));
                this.add(GCBlocks.FULL_BUGGY_FUELING_PAD, block -> createSingleItemTableWithSilkTouch(block, GCBlocks.BUGGY_FUELING_PAD, ConstantIntValue.exactly(9)));
            }

            @Override
            protected Iterable<Block> getKnownBlocks()
            {
                return ForgeRegistries.BLOCKS.getValues().stream().filter(type -> type.getRegistryName().getNamespace().equals(Constants.MOD_ID_CORE)).collect(Collectors.toList());
            }
        }

        class EntityLootTable extends EntityLoot
        {
            @Override
            protected void addTables()
            {
                this.add(GCEntities.EVOLVED_SPIDER, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.SPIDER_EYE).apply(SetItemCountFunction.setCount(RandomValueBounds.between(-1.0F, 1.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))).when(LootItemKilledByPlayerCondition.killedByPlayer()))
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(GCItems.CHEESE_CURD))
                                .add(LootItem.lootTableItem(Items.FERMENTED_SPIDER_EYE))
                                .add(LootItem.lootTableItem(GCItems.MEDIUM_OXYGEN_TANK).apply(SetItemDamageFunction.func_215931_a(RandomValueBounds.between(0.1F, 0.8F))))
                                .addEntry(LootItem.lootTableItem(GCItems.OXYGEN_GEAR))
                                .addEntry(LootItem.lootTableItem(GCItems.OXYGEN_CONCENTRATOR))
                                .addEntry(LootItem.lootTableItem(Items.NETHER_WART))//TODO ConfigManagerCore.challengeMobDropsAndSpawning condition
                                .acceptCondition(LootItemKilledByPlayerCondition.killedByPlayer()).acceptCondition(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.02F))));

                this.add(GCEntities.EVOLVED_ZOMBIE, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(Items.IRON_INGOT))
                                .add(LootItem.lootTableItem(Items.CARROT))
                                .add(LootItem.lootTableItem(Items.POTATO)).when(LootItemKilledByPlayerCondition.killedByPlayer()).when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F)))
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_CARROTS))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_POTATOES))
                                .add(LootItem.lootTableItem(GCItems.RAW_METEORIC_IRON))
                                .add(LootItem.lootTableItem(GCItems.MEDIUM_OXYGEN_TANK).apply(SetItemDamageFunction.func_215931_a(RandomValueBounds.between(0.1F, 0.8F))))
                                .addEntry(LootItem.lootTableItem(GCItems.OXYGEN_MASK))
                                .addEntry(LootItem.lootTableItem(GCItems.OXYGEN_VENT))
                                .addEntry(LootItem.lootTableItem(Items.MELON_SEEDS))//TODO ConfigManagerCore.challengeMobDropsAndSpawning condition
                                .acceptCondition(LootItemKilledByPlayerCondition.killedByPlayer()).acceptCondition(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.02F)))
                        .addLootPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(GCItems.COPPER_INGOT))//TODO ConfigManagerCore.challengeMobDropsAndSpawning condition
                                .when(LootItemKilledByPlayerCondition.killedByPlayer()).when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.0F))));

                this.add(GCEntities.EVOLVED_CREEPER, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
                        .withPool(LootPool.lootPool()
                                .add(TagEntry.func_216176_b(ItemTags.MUSIC_DISCS)).acceptCondition(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS))))
                        .addLootPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(Blocks.SAND))
                                .add(LootItem.lootTableItem(GCItems.MEDIUM_OXYGEN_TANK).apply(SetItemDamageFunction.func_215931_a(RandomValueBounds.between(0.1F, 0.8F))))
                                .addEntry(LootItem.lootTableItem(GCItems.OXYGEN_GEAR))
                                .addEntry(LootItem.lootTableItem(Blocks.ICE))
                                .addEntry(LootItem.lootTableItem(Items.SUGAR_CANE))//TODO ConfigManagerCore.challengeMobDropsAndSpawning condition
                                .acceptCondition(LootItemKilledByPlayerCondition.killedByPlayer()).acceptCondition(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.02F))));

                this.add(GCEntities.EVOLVED_SKELETON, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(GCBlocks.FLUID_PIPE))
                                .add(LootItem.lootTableItem(GCItems.MEDIUM_OXYGEN_TANK).apply(SetItemDamageFunction.func_215931_a(RandomValueBounds.between(0.1F, 0.8F))))
                                .addEntry(LootItem.lootTableItem(GCItems.TIN_CANISTER))
                                .addEntry(LootItem.lootTableItem(Items.PUMPKIN_SEEDS))//TODO ConfigManagerCore.challengeMobDropsAndSpawning condition
                                .acceptCondition(LootItemKilledByPlayerCondition.killedByPlayer()).acceptCondition(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.02F))));

                this.add(GCEntities.EVOLVED_ENDERMAN, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.ENDER_PEARL).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 1.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(GCItems.MEDIUM_OXYGEN_TANK).apply(SetItemDamageFunction.func_215931_a(RandomValueBounds.between(0.1F, 0.8F))))
                                .addEntry(LootItem.lootTableItem(Items.ENDER_EYE))
                                .addEntry(LootItem.lootTableItem(GCItems.OXYGEN_CONCENTRATOR))
                                .addEntry(LootItem.lootTableItem(GCItems.OXYGEN_MASK))
                                .acceptCondition(LootItemKilledByPlayerCondition.killedByPlayer()).acceptCondition(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.02F))));

                this.add(GCEntities.EVOLVED_WITCH, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(RandomValueBounds.between(1.0F, 3.0F))
                                .add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
                                .add(LootItem.lootTableItem(Items.SUGAR).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
                                .add(LootItem.lootTableItem(Items.REDSTONE).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
                                .add(LootItem.lootTableItem(Items.SPIDER_EYE).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
                                .add(LootItem.lootTableItem(Items.GLASS_BOTTLE).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
                                .add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
                                .add(LootItem.lootTableItem(Items.STICK).setWeight(2).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(GCItems.MEDIUM_OXYGEN_TANK).apply(SetItemDamageFunction.func_215931_a(RandomValueBounds.between(0.1F, 0.8F))))
                                .addEntry(LootItem.lootTableItem(GCItems.DEHYDRATED_CARROTS))
                                .addEntry(LootItem.lootTableItem(Blocks.GLOWSTONE))
                                .addEntry(LootItem.lootTableItem(GCItems.AMBIENT_THERMAL_CONTROLLER))
                                .addEntry(LootItem.lootTableItem(GCItems.OXYGEN_MASK))
                                .addEntry(LootItem.lootTableItem(GCItems.OXYGEN_VENT))
                                .acceptCondition(LootItemKilledByPlayerCondition.killedByPlayer()).acceptCondition(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.02F))));

                this.add(GCEntities.EVOLVED_SKELETON_BOSS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
            }

            @Override
            protected Iterable<EntityType<?>> getKnownEntities()
            {
                return ForgeRegistries.ENTITIES.getValues().stream().filter(type -> type.getRegistryName().getNamespace().equals(Constants.MOD_ID_CORE)).collect(Collectors.toList());
            }
        }

        class ChestLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>
        {
            @Override
            public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
            {
                ResourceLocation moonDungeon = new ResourceLocation(Constants.MOD_ID_CORE, "chests/moon_dungeon");

                consumer.accept(moonDungeon, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(RandomValueBounds.between(5.0F, 8.0F))
                                .add(LootItem.lootTableItem(GCItems.CHEESE_CURD).setWeight(10).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 4.0F))))
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(10).apply(SetItemCountFunction.setCount(RandomValueBounds.between(2.0F, 7.0F))))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_APPLES).setWeight(6).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_CARROTS).setWeight(6).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_MELONS).setWeight(6).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_POTATOES).setWeight(6).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(GCItems.PARTIAL_OIL_CANISTER).setWeight(5).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), compound -> compound.putInt("Damage", 1001)))))
                                .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(15))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
                                .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(RandomValueBounds.between(2.0F, 7.0F))))
                                .add(LootItem.lootTableItem(Items.MUSIC_DISC_BLOCKS).setWeight(4))
                                .add(LootItem.lootTableItem(Items.MUSIC_DISC_FAR).setWeight(4))
                                .add(LootItem.lootTableItem(GCItems.HEAVY_DUTY_SHOVEL).setWeight(10))
                                .add(LootItem.lootTableItem(GCItems.LUNAR_SAPPHIRE).setWeight(2).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(GCItems.RAW_METEORIC_IRON).setWeight(5).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 5.0F))))
                                .add(LootItem.lootTableItem(GCItems.FREQUENCY_MODULE).setWeight(1))
                                .add(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantRandomlyFunction.func_215900_c()))
                                .addEntry(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantRandomlyFunction.func_215900_c()))));
            }
        }
    }
}