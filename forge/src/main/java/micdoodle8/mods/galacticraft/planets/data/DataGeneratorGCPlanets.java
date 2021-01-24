package micdoodle8.mods.galacticraft.planets.data;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import ItemModelBuilder;
import ModelFile;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.tags.GCTags;
import micdoodle8.mods.galacticraft.planets.PlanetFluids;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockCavernousVine;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockSlimelingEgg;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.entities.MarsEntities;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockGeothermalGenerator;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.entities.VenusEntities;
import micdoodle8.mods.galacticraft.planets.venus.items.VenusItems;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.block.*;
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
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.ConstantIntValue;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.RandomValueBounds;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
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
public class DataGeneratorGCPlanets
{
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if (event.includeClient())
        {
            generator.addProvider(new BlockStates(generator, Constants.MOD_ID_PLANETS, helper));
            generator.addProvider(new ItemModels(generator, Constants.MOD_ID_PLANETS, helper));
            generator.addProvider(new Language(generator, Constants.MOD_ID_PLANETS));
        }
        if (event.includeServer())
        {
            /*BlockTagsProvider blockTagProvider = new BlockTagsBuilder(generator, MineconLiveMod.MOD_ID, helper);
            generator.addProvider(blockTagProvider);
            generator.addProvider(new ItemTagsBuilder(generator, blockTagProvider, MineconLiveMod.MOD_ID, helper));
            generator.addProvider(new FluidTagsBuilder(generator, MineconLiveMod.MOD_ID, helper));
            generator.addProvider(new EntityTypeTagsBuilder(generator, MineconLiveMod.MOD_ID, helper));*/
            BlockTagsProvider blockTagProvider = new BlockTagsBuilder(generator, Constants.MOD_ID_PLANETS, helper);
            generator.addProvider(blockTagProvider);
            generator.addProvider(new ItemTagsBuilder(generator, blockTagProvider, Constants.MOD_ID_PLANETS, helper));
            generator.addProvider(new Recipe(generator, Constants.MOD_ID_PLANETS));
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
            ResourceLocation machineBase = new ResourceLocation(Constants.MOD_ID_CORE, "block/machine_base");
            ResourceLocation machineInput = new ResourceLocation(Constants.MOD_ID_CORE, "block/machine_input");
            ResourceLocation machineOutput = new ResourceLocation(Constants.MOD_ID_CORE, "block/machine_output");
            ResourceLocation machineSide = new ResourceLocation(Constants.MOD_ID_CORE, "block/machine_side");
            ResourceLocation machineAdvBase = new ResourceLocation(Constants.MOD_ID_CORE, "block/advanced_machine_base");
            ResourceLocation machineAdvSide = new ResourceLocation(Constants.MOD_ID_CORE, "block/advanced_machine_side");
            ResourceLocation machineAdvInput = new ResourceLocation(Constants.MOD_ID_CORE, "block/advanced_machine_input");

            this.simpleBlock(MarsBlocks.MARS_FINE_REGOLITH);
            this.simpleBlock(MarsBlocks.MARS_REGOLITH);
            this.simpleBlock(MarsBlocks.MARS_STONE);
            this.simpleBlock(MarsBlocks.MARS_COBBLESTONE);
            this.simpleBlock(MarsBlocks.MARS_DUNGEON_BRICKS);
            this.simpleBlock(MarsBlocks.DESH_BLOCK);
            this.simpleBlock(MarsBlocks.MARS_IRON_ORE);
            this.simpleBlock(MarsBlocks.DESH_ORE);
            this.simpleBlock(MarsBlocks.MARS_TIN_ORE);
            this.simpleBlock(MarsBlocks.MARS_COPPER_ORE);
            this.simpleBlock(MarsBlocks.TIER_2_TREASURE_CHEST);
            this.simpleBlock(MarsBlocks.MARS_BOSS_SPAWNER, this.models().getExistingFile(this.mcLoc("block/barrier")));
            this.simpleBlock(MarsBlocks.CREEPER_EGG, this.models().withExistingParent("creeper_egg", this.mcLoc("block/dragon_egg")).texture("particle", this.modLoc("block/creeper_egg")).texture("all", this.modLoc("block/creeper_egg")));
            this.stairsBlock((StairBlock)MarsBlocks.MARS_COBBLESTONE_STAIRS, this.modLoc("block/mars_cobblestone"));
            this.stairsBlock((StairBlock)MarsBlocks.MARS_DUNGEON_BRICK_STAIRS, this.modLoc("block/mars_dungeon_bricks"));
            this.slabBlock((SlabBlock)MarsBlocks.MARS_COBBLESTONE_SLAB, this.getGeneratedModel(MarsBlocks.MARS_COBBLESTONE), this.modLoc("block/mars_cobblestone"));
            this.slabBlock((SlabBlock)MarsBlocks.MARS_DUNGEON_BRICK_SLAB, this.getGeneratedModel(MarsBlocks.MARS_DUNGEON_BRICKS), this.modLoc("block/mars_dungeon_bricks"));
            this.wallBlock((WallBlock)MarsBlocks.MARS_COBBLESTONE_WALL, this.modLoc("block/mars_cobblestone"));
            this.wallBlock((WallBlock)MarsBlocks.MARS_DUNGEON_BRICK_WALL, this.modLoc("block/mars_dungeon_bricks"));
            this.itemModels().wallInventory(this.toString(MarsBlocks.MARS_COBBLESTONE_WALL), this.modLoc("block/mars_cobblestone"));
            this.itemModels().wallInventory(this.toString(MarsBlocks.MARS_DUNGEON_BRICK_WALL), this.modLoc("block/mars_dungeon_bricks"));

            this.getVariantBuilder(MarsBlocks.BLUE_SLIMELING_EGG).forAllStates(state -> ConfiguredModel.builder().modelFile(this.getSlimelingEggModel(state, "blue_slimeling_egg")).build());
            this.getVariantBuilder(MarsBlocks.RED_SLIMELING_EGG).forAllStates(state -> ConfiguredModel.builder().modelFile(this.getSlimelingEggModel(state, "red_slimeling_egg")).build());
            this.getVariantBuilder(MarsBlocks.YELLOW_SLIMELING_EGG).forAllStates(state -> ConfiguredModel.builder().modelFile(this.getSlimelingEggModel(state, "yellow_slimeling_egg")).build());
            this.getVariantBuilder(MarsBlocks.CAVERNOUS_VINES).forAllStates(state -> ConfiguredModel.builder().modelFile(this.getCavernousVinesModel(state.get(BlockCavernousVine.VINE_TYPE).getTextureName())).build());

            ModelFile model = this.models().cube("methane_synthesizer", machineAdvInput, machineAdvBase, this.modLoc("block/methane_synthesizer"), machineAdvSide, this.modLoc("block/machine_oxygen_input_warning"), this.modLoc("block/machine_oxygen_output_warning")).texture("particle", this.modLoc("block/methane_synthesizer"));
            this.horizontalBlock(MarsBlocks.METHANE_SYNTHESIZER, model);

            model = this.models().cube("gas_liquefier", machineAdvInput, machineAdvBase, this.modLoc("block/gas_liquefier"), machineAdvSide, this.modLoc("block/machine_oxygen_input_warning"), machineAdvSide).texture("particle", this.modLoc("block/gas_liquefier"));
            this.horizontalBlock(MarsBlocks.GAS_LIQUEFIER, model);

            model = this.models().cube("terraformer", machineBase, machineBase, this.modLoc("block/terraformer"), this.modLoc("block/terraformer"), machineInput, machineBase).texture("particle", this.modLoc("block/terraformer"));
            this.horizontalBlock(MarsBlocks.TERRAFORMER, model);

            model = this.models().cube("launch_controller", machineBase, machineBase, this.modLoc("block/launch_controller"), this.modLoc("block/launch_controller"), machineInput, machineBase).texture("particle", this.modLoc("block/launch_controller"));
            this.horizontalBlock(MarsBlocks.LAUNCH_CONTROLLER, model);

            model = this.models().cube("water_electrolyzer", machineAdvInput, machineAdvBase, this.modLoc("block/water_electrolyzer"), this.modLoc("block/machine_oxygen_output_warning"), this.modLoc("block/machine_water_input_warning"), this.modLoc("block/machine_oxygen_output_warning")).texture("particle", this.modLoc("block/water_electrolyzer"));
            this.horizontalBlock(MarsBlocks.WATER_ELECTROLYZER, model);

            this.simpleBlock(AsteroidBlocks.ASTEROID_IRON_ORE);
            this.simpleBlock(AsteroidBlocks.ASTEROID_ALUMINUM_ORE);
            this.simpleBlock(AsteroidBlocks.ILMENITE_ORE);
            this.simpleBlock(AsteroidBlocks.TITANIUM_BLOCK);
            this.simpleBlock(AsteroidBlocks.DARK_DECORATION_BLOCK);
            this.simpleBlock(AsteroidBlocks.DARK_ASTEROID_ROCK, model1 -> ConfiguredModel.allYRotations(model1, 0, false));
            this.simpleBlock(AsteroidBlocks.GRAY_ASTEROID_ROCK, model1 -> ConfiguredModel.allYRotations(model1, 0, false));
            this.simpleBlock(AsteroidBlocks.LIGHT_GRAY_ASTEROID_ROCK, model1 -> ConfiguredModel.allYRotations(model1, 0, false));
            this.simpleBlock(AsteroidBlocks.DENSE_ICE);
            this.simpleBlock(AsteroidBlocks.ASTRO_MINER_BASE, this.models().cubeAll("astro_miner_base", this.modLoc("block/machine_frame")));
            this.simpleBlock(AsteroidBlocks.FULL_ASTRO_MINER_BASE, this.models().cubeAll("full_astro_miner_base", this.modLoc("block/machine_frame")));
            this.simpleBlock(AsteroidBlocks.SHORT_RANGE_TELEPAD, this.models().cubeAll("short_range_telepad", this.modLoc("block/short_range_telepad")));
            this.simpleBlock(AsteroidBlocks.SHORT_RANGE_TELEPAD_DUMMY, this.models().cubeAll("short_range_telepad_dummy", this.modLoc("block/short_range_telepad")));
            this.simpleBlock(AsteroidBlocks.ENERGY_BEAM_RECEIVER, this.models().cubeAll("energy_beam_receiver", this.modLoc("block/energy_beam_receiver")));
            this.simpleBlock(AsteroidBlocks.ENERGY_BEAM_REFLECTOR, this.models().cubeAll("energy_beam_reflector", this.modLoc("block/energy_beam_reflector")));
            this.slabBlock((SlabBlock)AsteroidBlocks.DARK_DECORATION_SLAB, this.getGeneratedModel(AsteroidBlocks.DARK_DECORATION_BLOCK), this.modLoc("block/dark_decoration_block"));

            this.simpleBlock(VenusBlocks.VENUS_SOFT_ROCK, model1 -> ConfiguredModel.allYRotations(model1, 0, false));
            this.simpleBlock(VenusBlocks.VENUS_HARD_ROCK, model1 -> ConfiguredModel.allYRotations(model1, 0, false));
            this.simpleBlock(VenusBlocks.PUMICE, model1 -> ConfiguredModel.allYRotations(model1, 0, false));
            this.simpleBlock(VenusBlocks.VENUS_VOLCANIC_ROCK);
            this.simpleBlock(VenusBlocks.SCORCHED_VENUS_ROCK);
            this.simpleBlock(VenusBlocks.ORANGE_VENUS_DUNGEON_BRICKS);
            this.simpleBlock(VenusBlocks.RED_VENUS_DUNGEON_BRICKS);
            this.simpleBlock(VenusBlocks.GALENA_ORE);
            this.simpleBlock(VenusBlocks.SOLAR_ORE);
            this.simpleBlock(VenusBlocks.VENUS_ALUMINUM_ORE);
            this.simpleBlock(VenusBlocks.VENUS_COPPER_ORE);
            this.simpleBlock(VenusBlocks.VENUS_QUARTZ_ORE);
            this.simpleBlock(VenusBlocks.VENUS_SILICON_ORE);
            this.simpleBlock(VenusBlocks.VENUS_TIN_ORE);
            this.simpleBlock(VenusBlocks.TIER_3_TREASURE_CHEST);
            this.simpleBlock(VenusBlocks.LEAD_BLOCK);
            this.simpleBlock(VenusBlocks.VENUS_BOSS_SPAWNER, this.models().getExistingFile(this.mcLoc("block/barrier")));
            this.simpleBlock(VenusBlocks.SOLAR_ARRAY_MODULE, this.models().getExistingFile(this.modLoc("block/solar_array_module")));
            this.simpleCross(VenusBlocks.WEB_STRING);
            this.simpleCross(VenusBlocks.WEB_TORCH);
            this.horizontalBlock(VenusBlocks.CRASHED_PROBE, this.models().getExistingFile(this.modLoc("block/crashed_probe")), 90);

            model = this.models().cubeTop("vapor_spout", this.modLoc("block/venus_soft_rock"), this.modLoc("block/vapor_spout"));
            this.simpleBlock(VenusBlocks.VAPOR_SPOUT, model);

            ModelFile geothermalOn = this.models().cube("geothermal_generator_on", machineBase, this.modLoc("block/geothermal_generator_top"), this.modLoc("block/geothermal_generator_on"), this.modLoc("block/geothermal_generator_on"), machineOutput, machineSide).texture("particle", this.modLoc("block/geothermal_generator_on")).texture("particle", this.modLoc("block/geothermal_generator_on"));
            ModelFile geothermalOff = this.models().cube("geothermal_generator", machineBase, this.modLoc("block/geothermal_generator_top"), this.modLoc("block/geothermal_generator"), this.modLoc("block/geothermal_generator"), machineOutput, machineSide).texture("particle", this.modLoc("block/geothermal_generator")).texture("particle", this.modLoc("block/geothermal_generator"));

            this.getVariantBuilder(VenusBlocks.GEOTHERMAL_GENERATOR)
            .forAllStates(state -> ConfiguredModel.builder()
                    .modelFile(state.get(BlockGeothermalGenerator.ACTIVE) ? geothermalOn : geothermalOff)
                    .rotationY((int) state.get(BlockGeothermalGenerator.FACING).getOpposite().getHorizontalAngle())
                    .build());

            this.simpleFluid(PlanetFluids.GAS_ARGON.getBlock());
            this.simpleFluid(PlanetFluids.GAS_ATMOSPHERIC.getBlock());
            this.simpleFluid(PlanetFluids.GAS_CARBON_DIOXIDE.getBlock());
            this.simpleFluid(PlanetFluids.GAS_HELIUM.getBlock());
            this.simpleFluid(PlanetFluids.GAS_METHANE.getBlock());
            this.simpleFluid(PlanetFluids.GAS_NITROGEN.getBlock());
            this.simpleFluid(PlanetFluids.LIQUID_ARGON.getBlock());
            this.simpleFluid(PlanetFluids.BACTERIAL_SLUDGE.getBlock());
            this.simpleFluid(PlanetFluids.LIQUID_METHANE.getBlock());
            this.simpleFluid(PlanetFluids.LIQUID_NITROGEN.getBlock());
            this.simpleFluid(PlanetFluids.LIQUID_OXYGEN.getBlock());
            this.simpleFluid(PlanetFluids.SULPHURIC_ACID.getBlock());
        }

        protected ModelFile getSlimelingEggModel(BlockState state, String name)
        {
            return state.getValue(BlockSlimelingEgg.CRACKED) ? this.models().withExistingParent(name + "_cracked", this.modLoc("block/slimeling_egg")).texture("texture", this.modLoc("block/" + name + "_cracked")).texture("particle", this.modLoc("block/" + name + "_cracked")) : this.models().withExistingParent(name, this.modLoc("block/slimeling_egg")).texture("texture", this.modLoc("block/" + name)).texture("particle", this.modLoc("block/" + name));
        }

        protected ModelFile getCavernousVinesModel(String name)
        {
            return this.models().cross(name, this.modLoc("block/" + name));
        }

        protected void simpleFluid(LiquidBlock block)
        {
            this.getVariantBuilder(block).partialState().setModels(new ConfiguredModel(this.models().getBuilder(this.toString(block)).texture("particle", this.modLoc(block.getFluid().getAttributes().getStillTexture().getPath()))));
        }

        protected void simpleCross(Block block)
        {
            this.getVariantBuilder(block).partialState().setModels(new ConfiguredModel(this.models().cross(this.toString(block), this.modLoc("block/" + this.toString(block)))));
        }

        protected ResourceLocation getGeneratedModel(Block block)
        {
            return this.models().generatedModels.get(this.modLoc("block/" + this.toString(block))).getLocation();
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
            this.parentedBlock(MarsBlocks.MARS_FINE_REGOLITH);
            this.parentedBlock(MarsBlocks.MARS_REGOLITH);
            this.parentedBlock(MarsBlocks.MARS_STONE);
            this.parentedBlock(MarsBlocks.MARS_COBBLESTONE);
            this.parentedBlock(MarsBlocks.MARS_DUNGEON_BRICKS);
            this.parentedBlock(MarsBlocks.DESH_BLOCK);
            this.parentedBlock(MarsBlocks.MARS_IRON_ORE);
            this.parentedBlock(MarsBlocks.DESH_ORE);
            this.parentedBlock(MarsBlocks.MARS_TIN_ORE);
            this.parentedBlock(MarsBlocks.MARS_COPPER_ORE);
            this.parentedBlock(MarsBlocks.CREEPER_EGG);
            this.parentedBlock(MarsBlocks.BLUE_SLIMELING_EGG);
            this.parentedBlock(MarsBlocks.RED_SLIMELING_EGG);
            this.parentedBlock(MarsBlocks.YELLOW_SLIMELING_EGG);
            this.parentedBlock(MarsBlocks.TIER_2_TREASURE_CHEST, this.mcLoc("item/chest")).texture("particle", this.modLoc("block/tier_2_treasure_chest"));
            this.parentedBlock(MarsBlocks.METHANE_SYNTHESIZER);
            this.parentedBlock(MarsBlocks.GAS_LIQUEFIER);
            this.parentedBlock(MarsBlocks.TERRAFORMER);
            this.parentedBlock(MarsBlocks.LAUNCH_CONTROLLER);
            this.parentedBlock(MarsBlocks.WATER_ELECTROLYZER);
            this.parentedBlock(MarsBlocks.CRYOGENIC_CHAMBER);
            this.parentedBlock(MarsBlocks.MARS_COBBLESTONE_STAIRS);
            this.parentedBlock(MarsBlocks.MARS_DUNGEON_BRICK_STAIRS);
            this.parentedBlock(MarsBlocks.MARS_COBBLESTONE_SLAB);
            this.parentedBlock(MarsBlocks.MARS_DUNGEON_BRICK_SLAB);

            this.parentedBlock(AsteroidBlocks.ASTEROID_IRON_ORE);
            this.parentedBlock(AsteroidBlocks.ASTEROID_ALUMINUM_ORE);
            this.parentedBlock(AsteroidBlocks.ILMENITE_ORE);
            this.parentedBlock(AsteroidBlocks.TITANIUM_BLOCK);
            this.parentedBlock(AsteroidBlocks.DARK_DECORATION_BLOCK);
            this.parentedBlock(AsteroidBlocks.DARK_ASTEROID_ROCK);
            this.parentedBlock(AsteroidBlocks.GRAY_ASTEROID_ROCK);
            this.parentedBlock(AsteroidBlocks.LIGHT_GRAY_ASTEROID_ROCK);
            this.parentedBlock(AsteroidBlocks.DENSE_ICE);
            this.parentedBlock(AsteroidBlocks.ASTRO_MINER_BASE);
            this.parentedBlock(AsteroidBlocks.DARK_DECORATION_SLAB);
            this.parentedInventoryBlock(AsteroidBlocks.WALKWAY);
            this.parentedInventoryBlock(AsteroidBlocks.FLUID_PIPE_WALKWAY);
            this.parentedInventoryBlock(AsteroidBlocks.WIRE_WALKWAY);

            this.parentedBlock(VenusBlocks.VENUS_SOFT_ROCK);
            this.parentedBlock(VenusBlocks.VENUS_HARD_ROCK);
            this.parentedBlock(VenusBlocks.VENUS_VOLCANIC_ROCK);
            this.parentedBlock(VenusBlocks.PUMICE);
            this.parentedBlock(VenusBlocks.SCORCHED_VENUS_ROCK);
            this.parentedBlock(VenusBlocks.ORANGE_VENUS_DUNGEON_BRICKS);
            this.parentedBlock(VenusBlocks.RED_VENUS_DUNGEON_BRICKS);
            this.parentedBlock(VenusBlocks.GALENA_ORE);
            this.parentedBlock(VenusBlocks.SOLAR_ORE);
            this.parentedBlock(VenusBlocks.VENUS_ALUMINUM_ORE);
            this.parentedBlock(VenusBlocks.VENUS_COPPER_ORE);
            this.parentedBlock(VenusBlocks.VENUS_QUARTZ_ORE);
            this.parentedBlock(VenusBlocks.VENUS_SILICON_ORE);
            this.parentedBlock(VenusBlocks.VENUS_TIN_ORE);
            this.parentedBlock(VenusBlocks.TIER_3_TREASURE_CHEST);
            this.parentedBlock(VenusBlocks.VAPOR_SPOUT);
            this.parentedBlock(VenusBlocks.CRASHED_PROBE);
            this.parentedBlock(VenusBlocks.LEAD_BLOCK);
            this.parentedBlock(VenusBlocks.SOLAR_ARRAY_CONTROLLER);
            this.parentedBlock(VenusBlocks.SOLAR_ARRAY_MODULE);
            this.parentedBlock(VenusBlocks.GEOTHERMAL_GENERATOR, this.modLoc("block/geothermal_generator_on"));
            this.parentedBlock(VenusBlocks.TIER_3_TREASURE_CHEST, this.mcLoc("item/chest")).texture("particle", this.modLoc("block/tier_3_treasure_chest"));

            this.itemGenerated(MarsBlocks.CAVERNOUS_VINES, "cavernous_vines_1");
            this.itemGenerated(VenusBlocks.WEB_STRING);
            this.itemGenerated(VenusBlocks.WEB_TORCH);

            this.itemGenerated(MarsItems.FRAGMENTED_CARBON);
            this.itemGenerated(MarsItems.DESH_AXE);
            this.itemGenerated(MarsItems.DESH_BOOTS);
            this.itemGenerated(MarsItems.DESH_CHESTPLATE);
            this.itemGenerated(MarsItems.DESH_HELMET);
            this.itemGenerated(MarsItems.DESH_HOE);
            this.itemGenerated(MarsItems.DESH_LEGGINGS);
            this.itemGenerated(MarsItems.STICKY_DESH_PICKAXE);
            this.itemGenerated(MarsItems.DESH_PICKAXE);
            this.itemGenerated(MarsItems.DESH_SHOVEL);
            this.itemGenerated(MarsItems.DESH_SWORD);
            this.itemGenerated(MarsItems.TIER_2_DUNGEON_KEY, new ResourceLocation(Constants.MOD_ID_CORE, "item/dungeon_key")).texture("key", "item/tier_2_dungeon_key");
            this.itemGenerated(MarsItems.TIER_2_ROCKET_18_INVENTORY, this.modLoc("item/tier_2_rocket"));
            this.itemGenerated(MarsItems.TIER_2_ROCKET_36_INVENTORY, this.modLoc("item/tier_2_rocket"));
            this.itemGenerated(MarsItems.TIER_2_ROCKET_54_INVENTORY, this.modLoc("item/tier_2_rocket"));
            this.itemGenerated(MarsItems.CREATIVE_TIER_2_ROCKET, this.modLoc("item/tier_2_rocket"));
            this.itemGenerated(MarsItems.CARGO_ROCKET_18_INVENTORY, this.modLoc("item/cargo_rocket"));
            this.itemGenerated(MarsItems.CARGO_ROCKET_36_INVENTORY, this.modLoc("item/cargo_rocket"));
            this.itemGenerated(MarsItems.CARGO_ROCKET_54_INVENTORY, this.modLoc("item/cargo_rocket"));
            this.itemGenerated(MarsItems.CREATIVE_CARGO_ROCKET, this.modLoc("item/cargo_rocket"));
            this.itemGenerated(MarsItems.CARGO_ROCKET_SCHEMATIC);
            this.itemGenerated(MarsItems.ASTRO_MINER_SCHEMATIC);
            this.itemGenerated(MarsItems.TIER_3_ROCKET_SCHEMATIC);
            this.itemGenerated(MarsItems.UNREFINED_DESH);
            this.itemGenerated(MarsItems.DESH_STICK);
            this.itemGenerated(MarsItems.DESH_INGOT);
            this.itemGenerated(MarsItems.TIER_2_HEAVY_DUTY_PLATE);
            this.itemGenerated(MarsItems.SLIMELING_INVENTORY_BAG);
            this.itemGenerated(MarsItems.COMPRESSED_DESH);
            this.itemGenerated(MarsItems.FLUID_MANIPULATOR);

            this.itemGenerated(AsteroidsItems.TITANIUM_SHOVEL);
            this.itemGenerated(AsteroidsItems.TITANIUM_PICKAXE);
            this.itemGenerated(AsteroidsItems.TITANIUM_AXE);
            this.itemGenerated(AsteroidsItems.ORION_DRIVE);
            this.itemGenerated(AsteroidsItems.TITANIUM_BOOTS);
            this.itemGenerated(AsteroidsItems.TITANIUM_HELMET);
            this.itemGenerated(AsteroidsItems.THERMAL_HELMET);
            this.itemGenerated(AsteroidsItems.THERMAL_CHESTPLATE);
            this.itemGenerated(AsteroidsItems.THERMAL_LEGGINGS);
            this.itemGenerated(AsteroidsItems.THERMAL_BOOTS);
            this.itemGenerated(AsteroidsItems.TIER_3_ROCKET_18_INVENTORY, this.modLoc("item/tier_3_rocket"));
            this.itemGenerated(AsteroidsItems.TIER_3_ROCKET_36_INVENTORY, this.modLoc("item/tier_3_rocket"));
            this.itemGenerated(AsteroidsItems.TIER_3_ROCKET_54_INVENTORY, this.modLoc("item/tier_3_rocket"));
            this.itemGenerated(AsteroidsItems.CREATIVE_TIER_3_ROCKET, this.modLoc("item/tier_3_rocket"));
            this.itemGenerated(AsteroidsItems.HEAVY_NOSE_CONE);
            this.itemGenerated(AsteroidsItems.TITANIUM_HOE);
            this.itemGenerated(AsteroidsItems.SMALL_STRANGE_SEED);
            this.itemGenerated(AsteroidsItems.LARGE_STRANGE_SEED);
            this.itemGenerated(AsteroidsItems.ATMOSPHERIC_VALVE);
            this.itemGenerated(AsteroidsItems.TITANIUM_SWORD);
            this.itemGenerated(AsteroidsItems.TITANIUM_CHESTPLATE);
            this.itemGenerated(AsteroidsItems.TITANIUM_LEGGINGS);
            this.itemGenerated(AsteroidsItems.TITANIUM_INGOT);
            this.itemGenerated(AsteroidsItems.HEAVY_ROCKET_ENGINE);
            this.itemGenerated(AsteroidsItems.HEAVY_ROCKET_FINS);
            this.itemGenerated(AsteroidsItems.IRON_SHARD);
            this.itemGenerated(AsteroidsItems.TITANIUM_SHARD);
            this.itemGenerated(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE);
            this.itemGenerated(AsteroidsItems.COMPRESSED_TITANIUM);
            this.itemGenerated(AsteroidsItems.THERMAL_CLOTH);
            this.itemGenerated(AsteroidsItems.BEAM_CORE);
            this.itemGenerated(AsteroidsItems.TITANIUM_DUST);

            this.itemGenerated(VenusItems.ATOMIC_BATTERY);
            this.itemGenerated(VenusItems.SHIELD_CONTROLLER);
            this.itemGenerated(VenusItems.LEAD_INGOT);
            this.itemGenerated(VenusItems.RADIOISOTOPE_CORE);
            this.itemGenerated(VenusItems.ISOTHERMAL_FABRIC);
            this.itemGenerated(VenusItems.SOLAR_DUST);
            this.itemGenerated(VenusItems.SOLAR_ARRAY_PANEL);
            this.itemGenerated(VenusItems.SOLAR_ARRAY_WAFER);
            this.itemGenerated(VenusItems.TIER_2_THERMAL_HELMET);
            this.itemGenerated(VenusItems.TIER_2_THERMAL_CHESTPLATE);
            this.itemGenerated(VenusItems.TIER_2_THERMAL_LEGGINGS);
            this.itemGenerated(VenusItems.TIER_2_THERMAL_BOOTS);
            this.itemGenerated(VenusItems.VOLCANIC_PICKAXE);
            this.itemGenerated(VenusItems.TIER_3_DUNGEON_KEY, new ResourceLocation(Constants.MOD_ID_CORE, "item/dungeon_key")).texture("key", "item/tier_3_dungeon_key");

            this.itemGenerated(PlanetFluids.GAS_ARGON.getBucket());
            this.itemGenerated(PlanetFluids.GAS_ATMOSPHERIC.getBucket());
            this.itemGenerated(PlanetFluids.GAS_CARBON_DIOXIDE.getBucket());
            this.itemGenerated(PlanetFluids.GAS_HELIUM.getBucket());
            this.itemGenerated(PlanetFluids.GAS_METHANE.getBucket());
            this.itemGenerated(PlanetFluids.GAS_NITROGEN.getBucket());
            this.itemGenerated(PlanetFluids.LIQUID_ARGON.getBucket());
            this.itemGenerated(PlanetFluids.BACTERIAL_SLUDGE.getBucket());
            this.itemGenerated(PlanetFluids.LIQUID_METHANE.getBucket());
            this.itemGenerated(PlanetFluids.LIQUID_NITROGEN.getBucket());
            this.itemGenerated(PlanetFluids.LIQUID_OXYGEN.getBucket());
            this.itemGenerated(PlanetFluids.SULPHURIC_ACID.getBucket());
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
            this.add(MarsBlocks.MARS_FINE_REGOLITH, "Mars Fine Regolith");
            this.add(MarsBlocks.MARS_REGOLITH, "Mars Regolith");
            this.add(MarsBlocks.MARS_STONE, "Mars Stone");
            this.add(MarsBlocks.MARS_COBBLESTONE, "Mars Cobblestone");
            this.add(MarsBlocks.MARS_DUNGEON_BRICKS, "Mars Dungeon Bricks");
            this.add(MarsBlocks.DESH_BLOCK, "Block of Desh");
            this.add(MarsBlocks.MARS_IRON_ORE, "Mars Iron Ore");
            this.add(MarsBlocks.DESH_ORE, "Desh Ore");
            this.add(MarsBlocks.MARS_TIN_ORE, "Mars Tin Ore");
            this.add(MarsBlocks.MARS_COPPER_ORE, "Mars Copper Ore");
            this.add(MarsBlocks.CREEPER_EGG, "Creeper Egg");
            this.add(MarsBlocks.BLUE_SLIMELING_EGG, "Blue Slimeling Egg");
            this.add(MarsBlocks.RED_SLIMELING_EGG, "Blue Slimeling Egg");
            this.add(MarsBlocks.YELLOW_SLIMELING_EGG, "Blue Slimeling Egg");
            this.add(MarsBlocks.CAVERNOUS_VINES, "Cavernous Vines");
            this.add(MarsBlocks.METHANE_SYNTHESIZER, "Methane Synthesizer");
            this.add(MarsBlocks.GAS_LIQUEFIER, "Gas Liquefier");
            this.add(MarsBlocks.TERRAFORMER, "Terraformer");
            this.add(MarsBlocks.LAUNCH_CONTROLLER, "Launch Controller");
            this.add(MarsBlocks.WATER_ELECTROLYZER, "Water Electrolyzer");
            this.add(MarsBlocks.TIER_2_TREASURE_CHEST, "Tier 2 Treasure Chest");
            this.add(MarsBlocks.CRYOGENIC_CHAMBER, "Cryogenic Chamber");
            this.add(MarsBlocks.MARS_COBBLESTONE_STAIRS, "Mars Cobblestone Stairs");
            this.add(MarsBlocks.MARS_DUNGEON_BRICK_STAIRS, "Mars Dungeon Brick Stairs");
            this.add(MarsBlocks.MARS_COBBLESTONE_SLAB, "Mars Cobblestone Slab");
            this.add(MarsBlocks.MARS_DUNGEON_BRICK_SLAB, "Mars Dungeon Brick Slab");
            this.add(MarsBlocks.MARS_COBBLESTONE_WALL, "Mars Cobblestone Wall");
            this.add(MarsBlocks.MARS_DUNGEON_BRICK_WALL, "Mars Dungeon Brick Wall");

            this.add(AsteroidBlocks.ASTEROID_IRON_ORE, "Asteroid Iron Ore");
            this.add(AsteroidBlocks.ASTEROID_ALUMINUM_ORE, "Asteroid Aluminum Ore");
            this.add(AsteroidBlocks.ILMENITE_ORE, "Ilmenite Ore");
            this.add(AsteroidBlocks.TITANIUM_BLOCK, "Titanium Block");
            this.add(AsteroidBlocks.DARK_DECORATION_BLOCK, "Dark Decoration Block");
            this.add(AsteroidBlocks.DARK_ASTEROID_ROCK, "Dark Asteroid Rock");
            this.add(AsteroidBlocks.GRAY_ASTEROID_ROCK, "Gray Asteroid Rock");
            this.add(AsteroidBlocks.LIGHT_GRAY_ASTEROID_ROCK, "Light Gray Asteroid Rock");
            this.add(AsteroidBlocks.DENSE_ICE, "Dense Ice");
            this.add(AsteroidBlocks.ASTRO_MINER_BASE, "Astro Miner Base");
            this.add(AsteroidBlocks.WALKWAY, "Walkway");
            this.add(AsteroidBlocks.FLUID_PIPE_WALKWAY, "Fluid Pipe Walkway");
            this.add(AsteroidBlocks.WIRE_WALKWAY, "Wire Walkway");
            this.add(AsteroidBlocks.ENERGY_BEAM_RECEIVER, "Energy Beam Receiver");
            this.add(AsteroidBlocks.ENERGY_BEAM_REFLECTOR, "Energy Beam Reflector");
            this.add(AsteroidBlocks.SHORT_RANGE_TELEPAD, "Short Range Telepad");
            this.add(AsteroidBlocks.DARK_DECORATION_SLAB, "Dark Decoration Slab");

            this.add(VenusBlocks.VENUS_SOFT_ROCK, "Venus Soft Rock");
            this.add(VenusBlocks.VENUS_HARD_ROCK, "Venus Hard Rock");
            this.add(VenusBlocks.VENUS_VOLCANIC_ROCK, "Venus Volcanic Rock");
            this.add(VenusBlocks.PUMICE, "Pumice");
            this.add(VenusBlocks.SCORCHED_VENUS_ROCK, "Scorched Venus Rock");
            this.add(VenusBlocks.ORANGE_VENUS_DUNGEON_BRICKS, "Orange Venus Dungeon Bricks");
            this.add(VenusBlocks.RED_VENUS_DUNGEON_BRICKS, "Red Venus Dungeon Bricks");
            this.add(VenusBlocks.GALENA_ORE, "Galena Ore");
            this.add(VenusBlocks.SOLAR_ORE, "Solar Ore");
            this.add(VenusBlocks.VENUS_ALUMINUM_ORE, "Venus Aluminum Ore");
            this.add(VenusBlocks.VENUS_COPPER_ORE, "Venus Copper Ore");
            this.add(VenusBlocks.VENUS_QUARTZ_ORE, "Venus Quartz Ore");
            this.add(VenusBlocks.VENUS_SILICON_ORE, "Venus Silicon Ore");
            this.add(VenusBlocks.VENUS_TIN_ORE, "Venus Tin Ore");
            this.add(VenusBlocks.TIER_3_TREASURE_CHEST, "Tier 3 Treasure Chest");
            this.add(VenusBlocks.VAPOR_SPOUT, "Vapor Spout");
            this.add(VenusBlocks.CRASHED_PROBE, "Crashed Probe");
            this.add(VenusBlocks.LEAD_BLOCK, "Block of Lead");
            this.add(VenusBlocks.WEB_STRING, "Web String");
            this.add(VenusBlocks.WEB_TORCH, "Web Torch");
            this.add(VenusBlocks.GEOTHERMAL_GENERATOR, "Geothermal Generator");
            this.add(VenusBlocks.SOLAR_ARRAY_CONTROLLER, "Solar Array Controller");
            this.add(VenusBlocks.SOLAR_ARRAY_MODULE, "Solar Array Module");
            this.add(VenusBlocks.LASER_TURRET, "Laser Turret");

            this.add(MarsItems.FRAGMENTED_CARBON, "Fragmented Carbon");
            this.add(MarsItems.DESH_AXE, "Desh Axe");
            this.add(MarsItems.DESH_BOOTS, "Desh Boots");
            this.add(MarsItems.DESH_CHESTPLATE, "Desh Chestplate");
            this.add(MarsItems.DESH_HELMET, "Desh Helmet");
            this.add(MarsItems.DESH_HOE, "Desh Hoe");
            this.add(MarsItems.DESH_LEGGINGS, "Desh Leggings");
            this.add(MarsItems.STICKY_DESH_PICKAXE, "Sticky Desh Pickaxe");
            this.add(MarsItems.DESH_PICKAXE, "Desh Pickaxe");
            this.add(MarsItems.DESH_SHOVEL, "Desh Shovel");
            this.add(MarsItems.DESH_SWORD, "Desh Sword");
            this.add(MarsItems.TIER_2_DUNGEON_KEY, "Tier 2 Dungeon Key");
            this.add(MarsItems.TIER_2_ROCKET, "Tier 2 Rocket");
            this.add(MarsItems.TIER_2_ROCKET_18_INVENTORY, "Tier 2 Rocket");
            this.add(MarsItems.TIER_2_ROCKET_36_INVENTORY, "Tier 2 Rocket");
            this.add(MarsItems.TIER_2_ROCKET_54_INVENTORY, "Tier 2 Rocket");
            this.add(MarsItems.CREATIVE_TIER_2_ROCKET, "Tier 2 Rocket");
            this.add(MarsItems.CARGO_ROCKET_18_INVENTORY, "Cargo Rocket");
            this.add(MarsItems.CARGO_ROCKET_36_INVENTORY, "Cargo Rocket");
            this.add(MarsItems.CARGO_ROCKET_54_INVENTORY, "Cargo Rocket");
            this.add(MarsItems.CREATIVE_CARGO_ROCKET, "Cargo Rocket");
            this.add(MarsItems.CARGO_ROCKET_SCHEMATIC, "Cargo Rocket Schematic");
            this.add(MarsItems.ASTRO_MINER_SCHEMATIC, "Astro Miner Schematic");
            this.add(MarsItems.TIER_3_ROCKET_SCHEMATIC, "Tier 3 Rocket Schematic");
            this.add(MarsItems.UNREFINED_DESH, "Unrefined Desh");
            this.add(MarsItems.DESH_STICK, "Desh Stick");
            this.add(MarsItems.DESH_INGOT, "Desh Ingot");
            this.add(MarsItems.TIER_2_HEAVY_DUTY_PLATE, "Tier 2 Heavy-Duty Plate");
            this.add(MarsItems.SLIMELING_INVENTORY_BAG, "Slimeling Inventory Bag");
            this.add(MarsItems.COMPRESSED_DESH, "Compressed Desh");
            this.add(MarsItems.FLUID_MANIPULATOR, "Fluid Manipulator");

            this.add(AsteroidsItems.TITANIUM_SHOVEL, "Titanium Shovel");
            this.add(AsteroidsItems.TITANIUM_PICKAXE, "Titanium Pickaxe");
            this.add(AsteroidsItems.TITANIUM_AXE, "Titanium Axe");
            this.add(AsteroidsItems.ORION_DRIVE, "Orion Drive");
            this.add(AsteroidsItems.TITANIUM_BOOTS, "Titanium Boots");
            this.add(AsteroidsItems.TITANIUM_HELMET, "Titanium Helmet");
            this.add(AsteroidsItems.THERMAL_HELMET, "Thermal Helmet");
            this.add(AsteroidsItems.THERMAL_CHESTPLATE, "Thermal Chestplate");
            this.add(AsteroidsItems.THERMAL_LEGGINGS, "Thermal Leggings");
            this.add(AsteroidsItems.THERMAL_BOOTS, "Thermal Boots");
            this.add(AsteroidsItems.TIER_3_ROCKET, "Tier 3 Rocket");
            this.add(AsteroidsItems.TIER_3_ROCKET_18_INVENTORY, "Tier 3 Rocket");
            this.add(AsteroidsItems.TIER_3_ROCKET_36_INVENTORY, "Tier 3 Rocket");
            this.add(AsteroidsItems.TIER_3_ROCKET_54_INVENTORY, "Tier 3 Rocket");
            this.add(AsteroidsItems.CREATIVE_TIER_3_ROCKET, "Tier 3 Rocket");
            this.add(AsteroidsItems.HEAVY_NOSE_CONE, "Heavy Nose Cone");
            this.add(AsteroidsItems.TITANIUM_HOE, "Titanium Hoe");
            this.add(AsteroidsItems.SMALL_STRANGE_SEED, "Small Strange Seed");
            this.add(AsteroidsItems.LARGE_STRANGE_SEED, "Large Strange Seed");
            this.add(AsteroidsItems.ATMOSPHERIC_VALVE, "Atmospheric Valve");
            this.add(AsteroidsItems.TITANIUM_SWORD, "Titanium Sword");
            this.add(AsteroidsItems.TITANIUM_CHESTPLATE, "Titanium Chestplate");
            this.add(AsteroidsItems.TITANIUM_LEGGINGS, "Titanium Leggings");
            this.add(AsteroidsItems.TITANIUM_INGOT, "Titanium Ingot");
            this.add(AsteroidsItems.HEAVY_ROCKET_ENGINE, "Heavy Rocket Engine");
            this.add(AsteroidsItems.HEAVY_ROCKET_FINS, "Heavy Rocket Fins");
            this.add(AsteroidsItems.IRON_SHARD, "Iron Shard");
            this.add(AsteroidsItems.TITANIUM_SHARD, "Titanium Shard");
            this.add(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE, "Tier 3 Heavy-Duty Plate");
            this.add(AsteroidsItems.COMPRESSED_TITANIUM, "Compressed Titanium");
            this.add(AsteroidsItems.THERMAL_CLOTH, "Thermal Cloth");
            this.add(AsteroidsItems.BEAM_CORE, "Beam Core");
            this.add(AsteroidsItems.TITANIUM_DUST, "Titanium Dust");

            this.add(VenusItems.ATOMIC_BATTERY, "Atomic Battery");
            this.add(VenusItems.SHIELD_CONTROLLER, "Shield Controller");
            this.add(VenusItems.LEAD_INGOT, "Lead Ingot");
            this.add(VenusItems.RADIOISOTOPE_CORE, "Radioisotope Core");
            this.add(VenusItems.ISOTHERMAL_FABRIC, "Isothermal Fabric");
            this.add(VenusItems.SOLAR_DUST, "Solar Dust");
            this.add(VenusItems.SOLAR_ARRAY_PANEL, "Solar Array Panel");
            this.add(VenusItems.SOLAR_ARRAY_WAFER, "Solar Array Wafer");
            this.add(VenusItems.TIER_2_THERMAL_HELMET, "Tier 2 Thermal Helmet");
            this.add(VenusItems.TIER_2_THERMAL_CHESTPLATE, "Tier 2 Thermal Chestplate");
            this.add(VenusItems.TIER_2_THERMAL_LEGGINGS, "Tier 2 Thermal Leggings");
            this.add(VenusItems.TIER_2_THERMAL_BOOTS, "Tier 2 Thermal Boots");
            this.add(VenusItems.VOLCANIC_PICKAXE, "Volcanic Pickaxe");
            this.add(VenusItems.TIER_3_DUNGEON_KEY, "Tier 3 Dungeon Key");
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
            this.tag(GCTags.DESH_ORES).add(MarsBlocks.DESH_ORE);
            this.tag(GCTags.ILMENITE_ORES).add(AsteroidBlocks.ILMENITE_ORE);
            this.tag(GCTags.LEAD_STORAGE_BLOCKS).add(VenusBlocks.LEAD_BLOCK);
            this.tag(GCTags.DESH_STORAGE_BLOCKS).add(MarsBlocks.DESH_BLOCK);
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
            this.tag(GCTags.LEAD_INGOTS)
            .add(VenusItems.LEAD_INGOT);

            this.tag(GCTags.DESH_INGOTS)
            .add(MarsItems.DESH_INGOT);

            this.tag(GCTags.DESH_ORES_ITEM)
            .add(MarsBlocks.DESH_ORE.asItem());
            this.tag(GCTags.ILMENITE_ORES_ITEM)
            .add(AsteroidBlocks.ILMENITE_ORE.asItem());

            this.tag(GCTags.LEAD_STORAGE_BLOCKS_ITEM).add(VenusBlocks.LEAD_BLOCK.asItem());
            this.tag(GCTags.DESH_STORAGE_BLOCKS_ITEM).add(MarsBlocks.DESH_BLOCK.asItem());
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
            ShapedRecipeBuilder.shaped(AsteroidsItems.BEAM_CORE).define('X', Tags.Items.DUSTS_REDSTONE).key('Y', GCTags.IRON_PLATES).key('Z', Tags.Items.GLASS_PANES_COLORLESS).patternLine("XYX").patternLine("YZY").patternLine("XYX").addCriterion(this.toCriterion(GCTags.IRON_PLATES), this.has(GCTags.IRON_PLATES)).build(consumer);
            ShapedRecipeBuilder.shaped(MarsBlocks.WATER_ELECTROLYZER).define('X', GCTags.BRONZE_PLATES).define('Y', MarsItems.FLUID_MANIPULATOR).define('Z', GCBlocks.FLUID_PIPE).define('W', GCItems.OXYGEN_VENT).define('V', GCItems.HEAVY_OXYGEN_TANK).define('U', GCTags.COPPER_PLATES).pattern("VWV").pattern("ZYZ").pattern("UXU").unlocks(this.toCriterion(MarsItems.FLUID_MANIPULATOR), this.has(MarsItems.FLUID_MANIPULATOR)).save(consumer);
            ShapedRecipeBuilder.shaped(AsteroidsItems.HEAVY_ROCKET_ENGINE).define('X', AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE).define('Y', Items.FLINT_AND_STEEL).define('Z', GCItems.OXYGEN_VENT).define('W', GCItems.TIN_CANISTER).define('V', Items.STONE_BUTTON).pattern(" YV").pattern("XWX").pattern("XZX").unlocks(this.toCriterion(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE), this.has(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE)).save(consumer);
            ShapedRecipeBuilder.shaped(MarsBlocks.GAS_LIQUEFIER).define('X', GCTags.BRONZE_PLATES).define('Y', GCBlocks.OXYGEN_COMPRESSOR).define('Z', MarsItems.FLUID_MANIPULATOR).define('W', GCBlocks.FLUID_PIPE).define('V', GCItems.OXYGEN_VENT).define('U', GCItems.HEAVY_OXYGEN_TANK).define('S', GCItems.MEDIUM_OXYGEN_TANK).define('P', GCBlocks.OXYGEN_DECOMPRESSOR).pattern("UVS").pattern("WZS").pattern("PXY").unlocks(this.toCriterion(MarsItems.FLUID_MANIPULATOR), this.has(MarsItems.FLUID_MANIPULATOR)).save(consumer);
            ShapedRecipeBuilder.shaped(MarsBlocks.METHANE_SYNTHESIZER).define('X', GCTags.BRONZE_PLATES).define('Y', GCBlocks.OXYGEN_COMPRESSOR).define('Z', MarsItems.FLUID_MANIPULATOR).define('W', GCBlocks.FLUID_PIPE).define('V', GCItems.OXYGEN_VENT).define('T', GCItems.HEAVY_OXYGEN_TANK).define('C', GCBlocks.ELECTRIC_FURNACE).pattern("TVT").pattern("WZW").pattern("CXY").unlocks(this.toCriterion(MarsItems.FLUID_MANIPULATOR), this.has(MarsItems.FLUID_MANIPULATOR)).save(consumer);
            ShapedRecipeBuilder.shaped(VenusBlocks.GEOTHERMAL_GENERATOR).define('X', GCTags.BRONZE_PLATES).define('Y', GCTags.LEAD_INGOTS).define('Z', GCBlocks.ALUMINUM_WIRE).define('W', GCBlocks.COAL_GENERATOR).define('V', AsteroidsItems.ATMOSPHERIC_VALVE).pattern("XVX").pattern("ZWZ").pattern("XYX").unlocks(this.toCriterion(GCTags.LEAD_INGOTS), this.has(GCTags.LEAD_INGOTS)).save(consumer);
            ShapedRecipeBuilder.shaped(AsteroidsItems.GRAPPLE).define('X', Tags.Items.INGOTS_IRON).key('Y', Tags.Items.STRING).patternLine("  Y").patternLine("XY ").patternLine("XX ").addCriterion(this.toCriterion(Tags.Items.STRING), this.has(Tags.Items.STRING)).build(consumer);
            ShapedRecipeBuilder.shaped(AsteroidsItems.HEAVY_NOSE_CONE).define('X', AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE).define('Y', Items.REDSTONE_TORCH).pattern(" Y ").pattern(" X ").pattern("X X").unlocks(this.toCriterion(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE), this.has(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE)).save(consumer);
            ShapedRecipeBuilder.shaped(MarsBlocks.DESH_BLOCK).define('X', MarsItems.DESH_INGOT).pattern("XXX").pattern("XXX").pattern("XXX").unlocks(this.toCriterion(MarsItems.DESH_INGOT), this.has(MarsItems.DESH_INGOT)).save(consumer);
            //ShapedRecipeBuilder.shapedRecipe(AsteroidBlocks.TITANIUM_BLOCK).key('X', AsteroidsItems.TITANIUM_INGOT).patternLine("XXX").patternLine("XXX").patternLine("XXX").addCriterion(this.toCriterion(AsteroidsItems.TITANIUM_INGOT), this.hasItem(AsteroidsItems.TITANIUM_INGOT)).build(consumer);
            ShapedRecipeBuilder.shaped(VenusBlocks.LEAD_BLOCK).define('X', VenusItems.LEAD_INGOT).pattern("XXX").pattern("XXX").pattern("XXX").unlocks(this.toCriterion(VenusItems.LEAD_INGOT), this.has(VenusItems.LEAD_INGOT)).save(consumer);
            ShapedRecipeBuilder.shaped(AsteroidsItems.ORION_DRIVE).define('A', Tags.Items.ORES_DIAMOND).key('B', Tags.Items.ORES_LAPIS).key('C', Tags.Items.ORES_GOLD).key('D', Tags.Items.ORES_REDSTONE).key('E', Tags.Items.ORES_COAL).key('F', GCTags.CHEESE_ORES_ITEM).key('G', GCTags.DESH_ORES_ITEM).key('H', GCTags.ILMENITE_ORES_ITEM).key('O', AsteroidsItems.BEAM_CORE).patternLine("ABC").patternLine("DOE").patternLine("FGH").addCriterion(this.toCriterion(AsteroidsItems.BEAM_CORE), this.has(AsteroidsItems.BEAM_CORE)).build(consumer);
            ShapedRecipeBuilder.shaped(AsteroidsItems.HEAVY_ROCKET_FINS).define('X', AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE).define('Y', GCTags.DESH_INGOTS).pattern(" Y ").pattern("XYX").pattern("X X").unlocks(this.toCriterion(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE), this.has(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE)).save(consumer);
            ShapedRecipeBuilder.shaped(MarsItems.SLIMELING_INVENTORY_BAG).define('X', Tags.Items.LEATHER).key('Y', Tags.Items.SLIMEBALLS).key('Z', Tags.Items.CHESTS_WOODEN).key('W', Tags.Items.GEMS_DIAMOND).patternLine("XWX").patternLine("XYX").patternLine(" Z ").addCriterion(this.toCriterion(Tags.Items.SLIMEBALLS), this.has(Tags.Items.SLIMEBALLS)).build(consumer);
            ShapedRecipeBuilder.shaped(VenusBlocks.SOLAR_ARRAY_CONTROLLER).define('X', GCTags.STEEL_PLATES).define('Y', GCBlocks.HEAVY_ALUMINUM_WIRE).define('Z', GCTags.ADVANCED_WAFERS).pattern("X X").pattern("YZY").pattern("XYX").unlocks(this.toCriterion(GCTags.ADVANCED_WAFERS), this.has(GCTags.ADVANCED_WAFERS)).save(consumer);
            ShapedRecipeBuilder.shaped(VenusBlocks.SOLAR_ARRAY_MODULE).define('X', VenusItems.SOLAR_ARRAY_PANEL).define('Y', GCBlocks.HEAVY_ALUMINUM_WIRE).define('Z', GCTags.ADVANCED_WAFERS).pattern("XXX").pattern("YZY").unlocks(this.toCriterion(GCTags.ADVANCED_WAFERS), this.has(GCTags.ADVANCED_WAFERS)).save(consumer);
            ShapedRecipeBuilder.shaped(VenusItems.SOLAR_ARRAY_PANEL).define('X', Tags.Items.GLASS_COLORLESS).key('Y', VenusItems.SOLAR_ARRAY_WAFER).key('Z', GCBlocks.HEAVY_ALUMINUM_WIRE).patternLine("XXX").patternLine("YYY").patternLine("ZZZ").addCriterion(this.toCriterion(VenusItems.SOLAR_ARRAY_WAFER), this.has(VenusItems.SOLAR_ARRAY_WAFER)).build(consumer);
            ShapedRecipeBuilder.shaped(AsteroidsItems.THERMAL_BOOTS).define('X', AsteroidsItems.THERMAL_CLOTH).pattern("X X").pattern("X X").unlocks(this.toCriterion(AsteroidsItems.THERMAL_CLOTH), this.has(AsteroidsItems.THERMAL_CLOTH)).save(consumer);
            ShapedRecipeBuilder.shaped(AsteroidsItems.THERMAL_CHESTPLATE).define('X', AsteroidsItems.THERMAL_CLOTH).pattern("X X").pattern("XXX").pattern("XXX").unlocks(this.toCriterion(AsteroidsItems.THERMAL_CLOTH), this.has(AsteroidsItems.THERMAL_CLOTH)).save(consumer);
            ShapedRecipeBuilder.shaped(AsteroidsItems.THERMAL_HELMET).define('X', AsteroidsItems.THERMAL_CLOTH).pattern("XXX").pattern("X X").unlocks(this.toCriterion(AsteroidsItems.THERMAL_CLOTH), this.has(AsteroidsItems.THERMAL_CLOTH)).save(consumer);
            ShapedRecipeBuilder.shaped(AsteroidsItems.THERMAL_LEGGINGS).define('X', AsteroidsItems.THERMAL_CLOTH).pattern("XXX").pattern("X X").pattern("X X").unlocks(this.toCriterion(AsteroidsItems.THERMAL_CLOTH), this.has(AsteroidsItems.THERMAL_CLOTH)).save(consumer);
            ShapedRecipeBuilder.shaped(VenusItems.TIER_2_THERMAL_BOOTS).define('X', VenusItems.ISOTHERMAL_FABRIC).pattern("X X").pattern("X X").unlocks(this.toCriterion(VenusItems.ISOTHERMAL_FABRIC), this.has(VenusItems.ISOTHERMAL_FABRIC)).save(consumer);
            ShapedRecipeBuilder.shaped(VenusItems.TIER_2_THERMAL_CHESTPLATE).define('X', VenusItems.ISOTHERMAL_FABRIC).pattern("X X").pattern("XXX").pattern("XXX").unlocks(this.toCriterion(VenusItems.ISOTHERMAL_FABRIC), this.has(VenusItems.ISOTHERMAL_FABRIC)).save(consumer);
            ShapedRecipeBuilder.shaped(VenusItems.TIER_2_THERMAL_HELMET).define('X', VenusItems.ISOTHERMAL_FABRIC).pattern("XXX").pattern("X X").unlocks(this.toCriterion(VenusItems.ISOTHERMAL_FABRIC), this.has(VenusItems.ISOTHERMAL_FABRIC)).save(consumer);
            ShapedRecipeBuilder.shaped(VenusItems.TIER_2_THERMAL_LEGGINGS).define('X', VenusItems.ISOTHERMAL_FABRIC).pattern("XXX").pattern("X X").pattern("X X").unlocks(this.toCriterion(VenusItems.ISOTHERMAL_FABRIC), this.has(VenusItems.ISOTHERMAL_FABRIC)).save(consumer);
            ShapedRecipeBuilder.shaped(AsteroidsItems.THERMAL_CLOTH).define('X', ItemTags.WOOL).define('Y', Tags.Items.DUSTS_REDSTONE).patternLine(" X ").patternLine("XYX").patternLine(" X ").addCriterion(this.toCriterion(ItemTags.WOOL), this.has(ItemTags.WOOL)).build(consumer);
            ShapedRecipeBuilder.shaped(MarsBlocks.MARS_COBBLESTONE_STAIRS, 4).define('#', MarsBlocks.MARS_COBBLESTONE).pattern("#  ").pattern("## ").pattern("###").unlocks(this.toCriterion(MarsBlocks.MARS_COBBLESTONE), this.has(MarsBlocks.MARS_COBBLESTONE)).save(consumer);
            ShapedRecipeBuilder.shaped(MarsBlocks.MARS_DUNGEON_BRICK_STAIRS, 4).define('#', MarsBlocks.MARS_DUNGEON_BRICKS).pattern("#  ").pattern("## ").pattern("###").unlocks(this.toCriterion(MarsBlocks.MARS_DUNGEON_BRICKS), this.has(MarsBlocks.MARS_DUNGEON_BRICKS)).save(consumer);
            ShapedRecipeBuilder.shaped(MarsBlocks.MARS_COBBLESTONE_SLAB, 6).define('#', MarsBlocks.MARS_COBBLESTONE).pattern("###").unlocks(this.toCriterion(MarsBlocks.MARS_COBBLESTONE), this.has(MarsBlocks.MARS_COBBLESTONE)).save(consumer);
            ShapedRecipeBuilder.shaped(MarsBlocks.MARS_DUNGEON_BRICK_SLAB, 6).define('#', MarsBlocks.MARS_DUNGEON_BRICKS).pattern("###").unlocks(this.toCriterion(MarsBlocks.MARS_DUNGEON_BRICKS), this.has(MarsBlocks.MARS_DUNGEON_BRICKS)).save(consumer);
            ShapedRecipeBuilder.shaped(AsteroidBlocks.DARK_DECORATION_SLAB, 6).define('#', AsteroidBlocks.DARK_DECORATION_BLOCK).pattern("###").unlocks(this.toCriterion(AsteroidBlocks.DARK_DECORATION_BLOCK), this.has(AsteroidBlocks.DARK_DECORATION_BLOCK)).save(consumer);
            ShapedRecipeBuilder.shaped(MarsBlocks.MARS_COBBLESTONE_WALL, 6).define('#', MarsBlocks.MARS_COBBLESTONE).pattern("###").pattern("###").unlocks(this.toCriterion(MarsBlocks.MARS_COBBLESTONE), this.has(MarsBlocks.MARS_COBBLESTONE)).save(consumer);
            ShapedRecipeBuilder.shaped(MarsBlocks.MARS_DUNGEON_BRICK_WALL, 6).define('#', MarsBlocks.MARS_DUNGEON_BRICKS).pattern("###").pattern("###").unlocks(this.toCriterion(MarsBlocks.MARS_DUNGEON_BRICKS), this.has(MarsBlocks.MARS_DUNGEON_BRICKS)).save(consumer);

            ShapelessRecipeBuilder.shapeless(MarsItems.FRAGMENTED_CARBON, 32).requires(Items.COAL).requires(Items.COAL).requires(Items.COAL).requires(Items.COAL).group("fragmented_carbon").unlocks(this.toCriterion(Items.COAL), this.has(Items.COAL)).save(consumer, this.modLoc("fragmented_carbon_from_4_coals"));
            ShapelessRecipeBuilder.shapeless(MarsItems.FRAGMENTED_CARBON, 16).requires(Items.CHARCOAL).requires(Items.CHARCOAL).requires(Items.CHARCOAL).requires(Items.CHARCOAL).group("fragmented_carbon").unlocks(this.toCriterion(Items.CHARCOAL), this.has(Items.CHARCOAL)).save(consumer, this.modLoc("fragmented_carbon_from_4_charcoals"));
            ShapelessRecipeBuilder.shapeless(MarsItems.FRAGMENTED_CARBON, 4).requires(Items.CHARCOAL).group("fragmented_carbon").unlocks(this.toCriterion(Items.CHARCOAL), this.has(Items.CHARCOAL)).save(consumer, this.modLoc("fragmented_carbon_from_charcoal"));
            ShapelessRecipeBuilder.shapeless(MarsItems.DESH_INGOT, 9).requires(MarsBlocks.DESH_BLOCK).unlocks(this.toCriterion(MarsBlocks.DESH_BLOCK), this.has(MarsBlocks.DESH_BLOCK)).save(consumer);
            ShapelessRecipeBuilder.shapeless(AsteroidsItems.TITANIUM_INGOT, 9).requires(AsteroidBlocks.TITANIUM_BLOCK).unlocks(this.toCriterion(AsteroidBlocks.TITANIUM_BLOCK), this.has(AsteroidBlocks.TITANIUM_BLOCK)).save(consumer);
            ShapelessRecipeBuilder.shapeless(VenusItems.LEAD_INGOT, 9).requires(VenusBlocks.LEAD_BLOCK).unlocks(this.toCriterion(VenusBlocks.LEAD_BLOCK), this.has(VenusBlocks.LEAD_BLOCK)).save(consumer);

            SimpleCookingRecipeBuilder.smelting(Ingredient.of(MarsItems.UNREFINED_DESH), MarsItems.DESH_INGOT, 0.8F, 200).unlocks(this.toCriterion(MarsItems.UNREFINED_DESH), this.has(MarsItems.UNREFINED_DESH)).save(consumer, this.from(MarsItems.DESH_INGOT, MarsItems.UNREFINED_DESH));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(GCTags.DESH_ORES_ITEM), MarsItems.DESH_INGOT, 0.8F, 200).unlocks(this.toCriterion(GCTags.DESH_ORES_ITEM), this.has(GCTags.DESH_ORES_ITEM)).save(consumer, this.toSmelting(MarsItems.DESH_INGOT));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(ItemTags.PLANKS), MarsItems.FRAGMENTED_CARBON, 0.1F, 200).unlocks(this.toCriterion(ItemTags.PLANKS), this.has(ItemTags.PLANKS)).save(consumer, this.toSmelting(MarsItems.FRAGMENTED_CARBON));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(MarsBlocks.MARS_COBBLESTONE), MarsBlocks.MARS_STONE, 0.1F, 200).unlocks(this.toCriterion(MarsBlocks.MARS_COBBLESTONE), this.has(MarsBlocks.MARS_COBBLESTONE)).save(consumer, this.toSmelting(MarsBlocks.MARS_STONE));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(MarsBlocks.MARS_IRON_ORE, AsteroidBlocks.ASTEROID_IRON_ORE, AsteroidsItems.IRON_SHARD), Items.IRON_INGOT, 0.7F, 200).unlocks(this.toCriterion(Tags.Items.ORES_IRON), this.has(Tags.Items.ORES_IRON)).build(consumer, this.from(Items.IRON_INGOT, Tags.Items.ORES_IRON));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(AsteroidsItems.TITANIUM_SHARD, AsteroidsItems.TITANIUM_DUST), AsteroidsItems.TITANIUM_INGOT, 0.8F, 200).unlocks(this.toCriterion(AsteroidsItems.TITANIUM_SHARD), this.has(AsteroidsItems.TITANIUM_SHARD)).save(consumer, this.from(AsteroidsItems.TITANIUM_INGOT, AsteroidsItems.TITANIUM_SHARD));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(AsteroidBlocks.ILMENITE_ORE), AsteroidsItems.TITANIUM_INGOT, 0.8F, 200).unlocks(this.toCriterion(AsteroidBlocks.ILMENITE_ORE), this.has(AsteroidBlocks.ILMENITE_ORE)).save(consumer, this.from(AsteroidsItems.TITANIUM_INGOT, AsteroidBlocks.ILMENITE_ORE));

            SimpleCookingRecipeBuilder.smelting(Ingredient.of(VenusBlocks.GALENA_ORE), VenusItems.LEAD_INGOT, 0.8F, 200).unlocks(this.toCriterion(VenusBlocks.GALENA_ORE), this.has(VenusBlocks.GALENA_ORE)).save(consumer, this.toSmelting(VenusItems.LEAD_INGOT));
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(VenusBlocks.VENUS_QUARTZ_ORE), Items.QUARTZ, 0.2F, 200).unlocks(this.toCriterion(VenusBlocks.VENUS_QUARTZ_ORE), this.has(VenusBlocks.VENUS_QUARTZ_ORE)).save(consumer, this.toSmelting(Items.QUARTZ));

            SimpleCookingRecipeBuilder.blasting(Ingredient.of(MarsItems.UNREFINED_DESH), MarsItems.DESH_INGOT, 0.8F, 100).unlocks(this.toCriterion(MarsItems.UNREFINED_DESH), this.has(MarsItems.UNREFINED_DESH)).save(consumer, this.toBlasting(MarsItems.DESH_INGOT, MarsItems.UNREFINED_DESH));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(GCTags.DESH_ORES_ITEM), MarsItems.DESH_INGOT, 0.8F, 100).unlocks(this.toCriterion(GCTags.DESH_ORES_ITEM), this.has(GCTags.DESH_ORES_ITEM)).save(consumer, this.toBlasting(MarsItems.DESH_INGOT));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(ItemTags.PLANKS), MarsItems.FRAGMENTED_CARBON, 0.1F, 100).unlocks(this.toCriterion(ItemTags.PLANKS), this.has(ItemTags.PLANKS)).save(consumer, this.toBlasting(MarsItems.FRAGMENTED_CARBON));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(MarsBlocks.MARS_COBBLESTONE), MarsBlocks.MARS_STONE, 0.1F, 100).unlocks(this.toCriterion(MarsBlocks.MARS_COBBLESTONE), this.has(MarsBlocks.MARS_COBBLESTONE)).save(consumer, this.toBlasting(MarsBlocks.MARS_STONE));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(MarsBlocks.MARS_IRON_ORE, AsteroidBlocks.ASTEROID_IRON_ORE, AsteroidsItems.IRON_SHARD), Items.IRON_INGOT, 0.7F, 100).unlocks(this.toCriterion(Tags.Items.ORES_IRON), this.has(Tags.Items.ORES_IRON)).build(consumer, this.toBlasting(Items.IRON_INGOT));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(AsteroidsItems.TITANIUM_SHARD, AsteroidsItems.TITANIUM_DUST), AsteroidsItems.TITANIUM_INGOT, 0.8F, 100).unlocks(this.toCriterion(AsteroidsItems.TITANIUM_SHARD), this.has(AsteroidsItems.TITANIUM_SHARD)).save(consumer, this.toBlasting(AsteroidsItems.TITANIUM_INGOT, AsteroidsItems.TITANIUM_SHARD));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(AsteroidBlocks.ILMENITE_ORE), AsteroidsItems.TITANIUM_INGOT, 0.8F, 100).unlocks(this.toCriterion(AsteroidBlocks.ILMENITE_ORE), this.has(AsteroidBlocks.ILMENITE_ORE)).save(consumer, this.toBlasting(AsteroidsItems.TITANIUM_INGOT, AsteroidBlocks.ILMENITE_ORE));

            SimpleCookingRecipeBuilder.blasting(Ingredient.of(VenusBlocks.GALENA_ORE), VenusItems.LEAD_INGOT, 0.8F, 100).unlocks(this.toCriterion(VenusBlocks.GALENA_ORE), this.has(VenusBlocks.GALENA_ORE)).save(consumer, this.toBlasting(VenusItems.LEAD_INGOT));
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(VenusBlocks.VENUS_QUARTZ_ORE), Items.QUARTZ, 0.2F, 100).unlocks(this.toCriterion(VenusBlocks.VENUS_QUARTZ_ORE), this.has(VenusBlocks.VENUS_QUARTZ_ORE)).save(consumer, this.toBlasting(Items.QUARTZ));
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

        protected ResourceLocation toBlasting(ItemLike provider, ItemLike provider2)
        {
            return this.modLoc(this.toString(provider) + "_from_" + this.toString(provider2) + "_blasting");
        }

        protected ResourceLocation from(ItemLike provider1, ItemLike provider2)
        {
            return this.modLoc(this.toString(provider1) + "_from_" + this.toString(provider2));
        }

        protected ResourceLocation from(ItemLike provider1, Tag<?> tag)
        {
            return this.modLoc(this.toString(provider1) + "_from_" + tag.getId().getPath() + "_tag");
        }

        protected String toString(ItemLike provider)
        {
            return provider.asItem().getRegistryName().getPath();
        }

        protected ResourceLocation modLoc(String name)
        {
            return new ResourceLocation(Constants.MOD_ID_PLANETS, name);
        }
    }

    public static class LootTables extends LootTableProvider
    {
        private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables = Lists.newArrayList();
        private static final LootItemCondition.Builder SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));

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

        public LootTables addTable(Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet> table)
        {
            this.tables.add(table);
            return this;
        }

        class BlockLootTable extends BlockLoot
        {
            @Override
            protected void addTables()
            {
                this.dropSelf(MarsBlocks.MARS_FINE_REGOLITH);
                this.dropSelf(MarsBlocks.MARS_REGOLITH);
                this.dropSelf(MarsBlocks.MARS_COBBLESTONE);
                this.dropSelf(MarsBlocks.MARS_DUNGEON_BRICKS);
                this.dropSelf(MarsBlocks.DESH_BLOCK);
                this.dropSelf(MarsBlocks.MARS_IRON_ORE);
                this.dropSelf(MarsBlocks.MARS_TIN_ORE);
                this.dropSelf(MarsBlocks.MARS_COPPER_ORE);
                this.dropSelf(MarsBlocks.BLUE_SLIMELING_EGG);
                this.dropSelf(MarsBlocks.RED_SLIMELING_EGG);
                this.dropSelf(MarsBlocks.YELLOW_SLIMELING_EGG);
                this.dropSelf(MarsBlocks.METHANE_SYNTHESIZER);
                this.dropSelf(MarsBlocks.GAS_LIQUEFIER);
                this.dropSelf(MarsBlocks.TERRAFORMER);
                this.dropSelf(MarsBlocks.LAUNCH_CONTROLLER);
                this.dropSelf(MarsBlocks.WATER_ELECTROLYZER);
                this.dropSelf(MarsBlocks.CRYOGENIC_CHAMBER);
                this.dropSelf(MarsBlocks.CREEPER_EGG);
                this.dropSelf(MarsBlocks.MARS_COBBLESTONE_STAIRS);
                this.dropSelf(MarsBlocks.MARS_DUNGEON_BRICK_STAIRS);
                this.dropSelf(MarsBlocks.MARS_COBBLESTONE_WALL);
                this.dropSelf(MarsBlocks.MARS_DUNGEON_BRICK_WALL);
                this.add(MarsBlocks.MARS_COBBLESTONE_SLAB, BlockLoot::droppingSlab);
                this.add(MarsBlocks.MARS_DUNGEON_BRICK_SLAB, BlockLoot::droppingSlab);
                this.add(MarsBlocks.CAVERNOUS_VINES, BlockLoot::onlyWithShears);
                this.add(MarsBlocks.DESH_ORE, block -> createOreDrop(block, MarsItems.UNREFINED_DESH));
                this.add(MarsBlocks.MARS_STONE, block -> createSingleItemTableWithSilkTouch(block, MarsBlocks.MARS_COBBLESTONE));

                this.dropSelf(AsteroidBlocks.ASTEROID_IRON_ORE);
                this.dropSelf(AsteroidBlocks.ASTEROID_ALUMINUM_ORE);
                this.dropSelf(AsteroidBlocks.TITANIUM_BLOCK);
                this.dropSelf(AsteroidBlocks.DARK_DECORATION_BLOCK);
                this.dropSelf(AsteroidBlocks.DARK_ASTEROID_ROCK);
                this.dropSelf(AsteroidBlocks.GRAY_ASTEROID_ROCK);
                this.dropSelf(AsteroidBlocks.LIGHT_GRAY_ASTEROID_ROCK);
                this.dropWhenSilkTouch(AsteroidBlocks.DENSE_ICE);
                this.dropSelf(AsteroidBlocks.ASTRO_MINER_BASE);
                this.dropSelf(AsteroidBlocks.WALKWAY);
                this.dropSelf(AsteroidBlocks.FLUID_PIPE_WALKWAY);
                this.dropSelf(AsteroidBlocks.WIRE_WALKWAY);
                this.dropSelf(AsteroidBlocks.ENERGY_BEAM_REFLECTOR);
                this.dropSelf(AsteroidBlocks.ENERGY_BEAM_RECEIVER);
                this.dropSelf(AsteroidBlocks.SHORT_RANGE_TELEPAD);
                this.add(AsteroidBlocks.DARK_DECORATION_SLAB, BlockLoot::droppingSlab);
                this.add(AsteroidBlocks.FULL_ASTRO_MINER_BASE, LootTable.lootTable().withPool(applyExplosionCondition(AsteroidBlocks.ASTRO_MINER_BASE, LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(AsteroidBlocks.ASTRO_MINER_BASE).apply(SetItemCountFunction.setCount(ConstantIntValue.exactly(8)))))));
                this.add(AsteroidBlocks.ILMENITE_ORE, block -> createOreDrop(block, AsteroidsItems.TITANIUM_SHARD)
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(AsteroidsItems.IRON_SHARD).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(SILK_TOUCH.invert()))));

                this.dropSelf(VenusBlocks.VENUS_SOFT_ROCK);
                this.dropSelf(VenusBlocks.VENUS_HARD_ROCK);
                this.dropSelf(VenusBlocks.PUMICE);
                this.dropSelf(VenusBlocks.SCORCHED_VENUS_ROCK);
                this.dropSelf(VenusBlocks.ORANGE_VENUS_DUNGEON_BRICKS);
                this.dropSelf(VenusBlocks.RED_VENUS_DUNGEON_BRICKS);
                this.dropSelf(VenusBlocks.GALENA_ORE);
                this.dropSelf(VenusBlocks.VENUS_ALUMINUM_ORE);
                this.dropSelf(VenusBlocks.VENUS_COPPER_ORE);
                this.dropSelf(VenusBlocks.VENUS_QUARTZ_ORE);
                this.dropSelf(VenusBlocks.VENUS_TIN_ORE);
                this.dropSelf(VenusBlocks.CRASHED_PROBE);
                this.dropSelf(VenusBlocks.LEAD_BLOCK);
                this.dropSelf(VenusBlocks.GEOTHERMAL_GENERATOR);
                this.dropSelf(VenusBlocks.WEB_TORCH);
                this.dropSelf(VenusBlocks.SOLAR_ARRAY_MODULE);
                this.dropSelf(VenusBlocks.SOLAR_ARRAY_CONTROLLER);
                this.dropSelf(VenusBlocks.LASER_TURRET);
                this.add(VenusBlocks.SOLAR_ORE, block -> createOreDrop(block, VenusItems.SOLAR_DUST));
                this.add(VenusBlocks.VENUS_SILICON_ORE, block -> createOreDrop(block, GCItems.RAW_SILICON));
                this.add(VenusBlocks.VENUS_QUARTZ_ORE, block -> createOreDrop(block, Items.QUARTZ));
                this.add(VenusBlocks.WEB_STRING, BlockLoot::onlyWithShears);
                this.dropWhenSilkTouch(VenusBlocks.VENUS_VOLCANIC_ROCK);
                this.add(VenusBlocks.VAPOR_SPOUT, block -> createSingleItemTableWithSilkTouch(block, VenusBlocks.VENUS_SOFT_ROCK));
            }

            @Override
            protected Iterable<Block> getKnownBlocks()
            {
                return ForgeRegistries.BLOCKS.getValues().stream().filter(type -> type.getRegistryName().getNamespace().equals(Constants.MOD_ID_PLANETS)).collect(Collectors.toList());
            }
        }

        class EntityLootTable extends EntityLoot
        {
            @Override
            protected void addTables()
            {
                this.add(MarsEntities.SLIMELING, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.SLIME_BALL).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
                this.add(MarsEntities.SLUDGELING, LootTable.lootTable());
                this.add(MarsEntities.CREEPER_BOSS, LootTable.lootTable());
                this.add(VenusEntities.JUICER, LootTable.lootTable());
                this.add(VenusEntities.SPIDER_QUEEN, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(RandomValueBounds.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(Items.SPIDER_EYE).apply(SetItemCountFunction.setCount(RandomValueBounds.between(-1.0F, 1.0F))).apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))).when(LootItemKilledByPlayerCondition.killedByPlayer())));
            }

            @Override
            protected Iterable<EntityType<?>> getKnownEntities()
            {
                return ForgeRegistries.ENTITIES.getValues().stream().filter(type -> type.getRegistryName().getNamespace().equals(Constants.MOD_ID_PLANETS)).collect(Collectors.toList());
            }
        }

        class ChestLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>
        {
            @Override
            public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
            {
                ResourceLocation crashedProbe = new ResourceLocation(Constants.MOD_ID_PLANETS, "chests/crashed_probe");
                ResourceLocation marsDungeon = new ResourceLocation(Constants.MOD_ID_PLANETS, "chests/mars_dungeon");
                ResourceLocation venusDungeon = new ResourceLocation(Constants.MOD_ID_PLANETS, "chests/venus_dungeon");

                consumer.accept(crashedProbe, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(RandomValueBounds.between(4.0F, 6.0F))
                                .add(LootItem.lootTableItem(MarsItems.TIER_2_HEAVY_DUTY_PLATE).setWeight(5).apply(SetItemCountFunction.setCount(RandomValueBounds.between(3.0F, 6.0F))))
                                .add(LootItem.lootTableItem(GCItems.TIER_1_HEAVY_DUTY_PLATE).setWeight(5).apply(SetItemCountFunction.setCount(RandomValueBounds.between(3.0F, 6.0F))))
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(5).apply(SetItemCountFunction.setCount(RandomValueBounds.between(5.0F, 9.0F))))
                                .add(LootItem.lootTableItem(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE).setWeight(5).apply(SetItemCountFunction.setCount(RandomValueBounds.between(3.0F, 6.0F))))
                                .add(LootItem.lootTableItem(AsteroidsItems.COMPRESSED_TITANIUM).setWeight(5).apply(SetItemCountFunction.setCount(RandomValueBounds.between(3.0F, 6.0F))))));

                consumer.accept(marsDungeon, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(RandomValueBounds.between(6.0F, 9.0F))
                                .add(LootItem.lootTableItem(MarsItems.FRAGMENTED_CARBON).setWeight(10).apply(SetItemCountFunction.setCount(RandomValueBounds.between(2.0F, 7.0F))))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_APPLES).setWeight(6).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_CARROTS).setWeight(6).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_MELONS).setWeight(6).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_POTATOES).setWeight(6).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(GCItems.PARTIAL_OIL_CANISTER).setWeight(5).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), compound -> compound.putInt("Damage", 1001)))))
                                .add(LootItem.lootTableItem(MarsItems.DESH_BOOTS).setWeight(2))
                                .add(LootItem.lootTableItem(MarsItems.DESH_LEGGINGS).setWeight(2))
                                .add(LootItem.lootTableItem(MarsItems.DESH_CHESTPLATE).setWeight(2))
                                .add(LootItem.lootTableItem(MarsItems.DESH_HELMET).setWeight(2))
                                .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(RandomValueBounds.between(2.0F, 7.0F))))
                                .add(LootItem.lootTableItem(Items.MUSIC_DISC_MALL).setWeight(4))
                                .add(LootItem.lootTableItem(Items.MUSIC_DISC_MELLOHI).setWeight(4))
                                .add(LootItem.lootTableItem(MarsItems.UNREFINED_DESH).setWeight(5).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(MarsItems.DESH_STICK).setWeight(5))
                                .add(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantWithLevelsFunction.func_215895_a(ConstantIntValue.exactly(22)).func_216059_e()))
                                .addEntry(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantWithLevelsFunction.func_215895_a(ConstantIntValue.exactly(24)).func_216059_e()))));

                consumer.accept(venusDungeon, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(RandomValueBounds.between(6.0F, 9.0F))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_APPLES).setWeight(6).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_CARROTS).setWeight(6).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_MELONS).setWeight(6).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(GCItems.DEHYDRATED_POTATOES).setWeight(6).apply(SetItemCountFunction.setCount(RandomValueBounds.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(GCItems.PARTIAL_OIL_CANISTER).setWeight(5).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), compound -> compound.putInt("Damage", 1001)))))
                                .add(LootItem.lootTableItem(MarsItems.DESH_BOOTS).setWeight(2))
                                .add(LootItem.lootTableItem(MarsItems.DESH_LEGGINGS).setWeight(2))
                                .add(LootItem.lootTableItem(MarsItems.DESH_CHESTPLATE).setWeight(2))
                                .add(LootItem.lootTableItem(MarsItems.DESH_HELMET).setWeight(2))
                                .add(LootItem.lootTableItem(PlanetFluids.SULPHURIC_ACID.getBucket()).weight(4))
                                .addEntry(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(10))
                                .addEntry(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
                                .addEntry(LootItem.lootTableItem(Items.REDSTONE).setWeight(10).apply(SetItemCountFunction.setCount(RandomValueBounds.between(2.0F, 7.0F))))
                                .addEntry(LootItem.lootTableItem(Items.MUSIC_DISC_CHIRP).setWeight(4))
                                .addEntry(LootItem.lootTableItem(Items.MUSIC_DISC_WAIT).setWeight(4))
                                .addEntry(LootItem.lootTableItem(Items.MUSIC_DISC_WARD).setWeight(4))
                                .addEntry(LootItem.lootTableItem(VenusItems.ISOTHERMAL_FABRIC).setWeight(10).apply(SetItemCountFunction.setCount(RandomValueBounds.between(2.0F, 7.0F))))
                                .addEntry(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(10).apply(SetItemCountFunction.setCount(RandomValueBounds.between(2.0F, 4.0F))))
                                .addEntry(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantWithLevelsFunction.func_215895_a(ConstantIntValue.exactly(18)).func_216059_e()))
                                .addEntry(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantWithLevelsFunction.func_215895_a(ConstantIntValue.exactly(27)).func_216059_e()))
                                .addEntry(LootItem.lootTableItem(Items.BOOK).setWeight(8).apply(EnchantWithLevelsFunction.func_215895_a(ConstantIntValue.exactly(30)).func_216059_e()))));
            }
        }
    }
}