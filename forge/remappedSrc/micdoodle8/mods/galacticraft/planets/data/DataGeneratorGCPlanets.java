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
import micdoodle8.mods.galacticraft.planets.data.DataGeneratorGCPlanets.LootTables.BlockLootTable;
import micdoodle8.mods.galacticraft.planets.data.DataGeneratorGCPlanets.LootTables.ChestLootTables;
import micdoodle8.mods.galacticraft.planets.data.DataGeneratorGCPlanets.LootTables.EntityLootTable;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockCavernousVine;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockSlimelingEgg;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.entities.MarsEntities;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockGeothermalGenerator;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.entities.VenusEntities;
import micdoodle8.mods.galacticraft.planets.venus.items.VenusItems;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.*;
import net.minecraft.data.*;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.MatchTool;
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
            this.stairsBlock((StairsBlock)MarsBlocks.MARS_COBBLESTONE_STAIRS, this.modLoc("block/mars_cobblestone"));
            this.stairsBlock((StairsBlock)MarsBlocks.MARS_DUNGEON_BRICK_STAIRS, this.modLoc("block/mars_dungeon_bricks"));
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
            return state.get(BlockSlimelingEgg.CRACKED) ? this.models().withExistingParent(name + "_cracked", this.modLoc("block/slimeling_egg")).texture("texture", this.modLoc("block/" + name + "_cracked")).texture("particle", this.modLoc("block/" + name + "_cracked")) : this.models().withExistingParent(name, this.modLoc("block/slimeling_egg")).texture("texture", this.modLoc("block/" + name)).texture("particle", this.modLoc("block/" + name));
        }

        protected ModelFile getCavernousVinesModel(String name)
        {
            return this.models().cross(name, this.modLoc("block/" + name));
        }

        protected void simpleFluid(FlowingFluidBlock block)
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
        protected void registerTags()
        {
            this.getBuilder(GCTags.DESH_ORES).add(MarsBlocks.DESH_ORE);
            this.getBuilder(GCTags.ILMENITE_ORES).add(AsteroidBlocks.ILMENITE_ORE);
            this.getBuilder(GCTags.LEAD_STORAGE_BLOCKS).add(VenusBlocks.LEAD_BLOCK);
            this.getBuilder(GCTags.DESH_STORAGE_BLOCKS).add(MarsBlocks.DESH_BLOCK);
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
            this.getBuilder(GCTags.LEAD_INGOTS)
            .add(VenusItems.LEAD_INGOT);

            this.getBuilder(GCTags.DESH_INGOTS)
            .add(MarsItems.DESH_INGOT);

            this.getBuilder(GCTags.DESH_ORES_ITEM)
            .add(MarsBlocks.DESH_ORE.asItem());
            this.getBuilder(GCTags.ILMENITE_ORES_ITEM)
            .add(AsteroidBlocks.ILMENITE_ORE.asItem());

            this.getBuilder(GCTags.LEAD_STORAGE_BLOCKS_ITEM).add(VenusBlocks.LEAD_BLOCK.asItem());
            this.getBuilder(GCTags.DESH_STORAGE_BLOCKS_ITEM).add(MarsBlocks.DESH_BLOCK.asItem());
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
            ShapedRecipeBuilder.shapedRecipe(AsteroidsItems.BEAM_CORE).key('X', Tags.Items.DUSTS_REDSTONE).key('Y', GCTags.IRON_PLATES).key('Z', Tags.Items.GLASS_PANES_COLORLESS).patternLine("XYX").patternLine("YZY").patternLine("XYX").addCriterion(this.toCriterion(GCTags.IRON_PLATES), this.hasItem(GCTags.IRON_PLATES)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MarsBlocks.WATER_ELECTROLYZER).key('X', GCTags.BRONZE_PLATES).key('Y', MarsItems.FLUID_MANIPULATOR).key('Z', GCBlocks.FLUID_PIPE).key('W', GCItems.OXYGEN_VENT).key('V', GCItems.HEAVY_OXYGEN_TANK).key('U', GCTags.COPPER_PLATES).patternLine("VWV").patternLine("ZYZ").patternLine("UXU").addCriterion(this.toCriterion(MarsItems.FLUID_MANIPULATOR), this.hasItem(MarsItems.FLUID_MANIPULATOR)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(AsteroidsItems.HEAVY_ROCKET_ENGINE).key('X', AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE).key('Y', Items.FLINT_AND_STEEL).key('Z', GCItems.OXYGEN_VENT).key('W', GCItems.TIN_CANISTER).key('V', Items.STONE_BUTTON).patternLine(" YV").patternLine("XWX").patternLine("XZX").addCriterion(this.toCriterion(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE), this.hasItem(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MarsBlocks.GAS_LIQUEFIER).key('X', GCTags.BRONZE_PLATES).key('Y', GCBlocks.OXYGEN_COMPRESSOR).key('Z', MarsItems.FLUID_MANIPULATOR).key('W', GCBlocks.FLUID_PIPE).key('V', GCItems.OXYGEN_VENT).key('U', GCItems.HEAVY_OXYGEN_TANK).key('S', GCItems.MEDIUM_OXYGEN_TANK).key('P', GCBlocks.OXYGEN_DECOMPRESSOR).patternLine("UVS").patternLine("WZS").patternLine("PXY").addCriterion(this.toCriterion(MarsItems.FLUID_MANIPULATOR), this.hasItem(MarsItems.FLUID_MANIPULATOR)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MarsBlocks.METHANE_SYNTHESIZER).key('X', GCTags.BRONZE_PLATES).key('Y', GCBlocks.OXYGEN_COMPRESSOR).key('Z', MarsItems.FLUID_MANIPULATOR).key('W', GCBlocks.FLUID_PIPE).key('V', GCItems.OXYGEN_VENT).key('T', GCItems.HEAVY_OXYGEN_TANK).key('C', GCBlocks.ELECTRIC_FURNACE).patternLine("TVT").patternLine("WZW").patternLine("CXY").addCriterion(this.toCriterion(MarsItems.FLUID_MANIPULATOR), this.hasItem(MarsItems.FLUID_MANIPULATOR)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(VenusBlocks.GEOTHERMAL_GENERATOR).key('X', GCTags.BRONZE_PLATES).key('Y', GCTags.LEAD_INGOTS).key('Z', GCBlocks.ALUMINUM_WIRE).key('W', GCBlocks.COAL_GENERATOR).key('V', AsteroidsItems.ATMOSPHERIC_VALVE).patternLine("XVX").patternLine("ZWZ").patternLine("XYX").addCriterion(this.toCriterion(GCTags.LEAD_INGOTS), this.hasItem(GCTags.LEAD_INGOTS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(AsteroidsItems.GRAPPLE).key('X', Tags.Items.INGOTS_IRON).key('Y', Tags.Items.STRING).patternLine("  Y").patternLine("XY ").patternLine("XX ").addCriterion(this.toCriterion(Tags.Items.STRING), this.hasItem(Tags.Items.STRING)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(AsteroidsItems.HEAVY_NOSE_CONE).key('X', AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE).key('Y', Items.REDSTONE_TORCH).patternLine(" Y ").patternLine(" X ").patternLine("X X").addCriterion(this.toCriterion(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE), this.hasItem(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MarsBlocks.DESH_BLOCK).key('X', MarsItems.DESH_INGOT).patternLine("XXX").patternLine("XXX").patternLine("XXX").addCriterion(this.toCriterion(MarsItems.DESH_INGOT), this.hasItem(MarsItems.DESH_INGOT)).build(consumer);
            //ShapedRecipeBuilder.shapedRecipe(AsteroidBlocks.TITANIUM_BLOCK).key('X', AsteroidsItems.TITANIUM_INGOT).patternLine("XXX").patternLine("XXX").patternLine("XXX").addCriterion(this.toCriterion(AsteroidsItems.TITANIUM_INGOT), this.hasItem(AsteroidsItems.TITANIUM_INGOT)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(VenusBlocks.LEAD_BLOCK).key('X', VenusItems.LEAD_INGOT).patternLine("XXX").patternLine("XXX").patternLine("XXX").addCriterion(this.toCriterion(VenusItems.LEAD_INGOT), this.hasItem(VenusItems.LEAD_INGOT)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(AsteroidsItems.ORION_DRIVE).key('A', Tags.Items.ORES_DIAMOND).key('B', Tags.Items.ORES_LAPIS).key('C', Tags.Items.ORES_GOLD).key('D', Tags.Items.ORES_REDSTONE).key('E', Tags.Items.ORES_COAL).key('F', GCTags.CHEESE_ORES_ITEM).key('G', GCTags.DESH_ORES_ITEM).key('H', GCTags.ILMENITE_ORES_ITEM).key('O', AsteroidsItems.BEAM_CORE).patternLine("ABC").patternLine("DOE").patternLine("FGH").addCriterion(this.toCriterion(AsteroidsItems.BEAM_CORE), this.hasItem(AsteroidsItems.BEAM_CORE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(AsteroidsItems.HEAVY_ROCKET_FINS).key('X', AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE).key('Y', GCTags.DESH_INGOTS).patternLine(" Y ").patternLine("XYX").patternLine("X X").addCriterion(this.toCriterion(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE), this.hasItem(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MarsItems.SLIMELING_INVENTORY_BAG).key('X', Tags.Items.LEATHER).key('Y', Tags.Items.SLIMEBALLS).key('Z', Tags.Items.CHESTS_WOODEN).key('W', Tags.Items.GEMS_DIAMOND).patternLine("XWX").patternLine("XYX").patternLine(" Z ").addCriterion(this.toCriterion(Tags.Items.SLIMEBALLS), this.hasItem(Tags.Items.SLIMEBALLS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(VenusBlocks.SOLAR_ARRAY_CONTROLLER).key('X', GCTags.STEEL_PLATES).key('Y', GCBlocks.HEAVY_ALUMINUM_WIRE).key('Z', GCTags.ADVANCED_WAFERS).patternLine("X X").patternLine("YZY").patternLine("XYX").addCriterion(this.toCriterion(GCTags.ADVANCED_WAFERS), this.hasItem(GCTags.ADVANCED_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(VenusBlocks.SOLAR_ARRAY_MODULE).key('X', VenusItems.SOLAR_ARRAY_PANEL).key('Y', GCBlocks.HEAVY_ALUMINUM_WIRE).key('Z', GCTags.ADVANCED_WAFERS).patternLine("XXX").patternLine("YZY").addCriterion(this.toCriterion(GCTags.ADVANCED_WAFERS), this.hasItem(GCTags.ADVANCED_WAFERS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(VenusItems.SOLAR_ARRAY_PANEL).key('X', Tags.Items.GLASS_COLORLESS).key('Y', VenusItems.SOLAR_ARRAY_WAFER).key('Z', GCBlocks.HEAVY_ALUMINUM_WIRE).patternLine("XXX").patternLine("YYY").patternLine("ZZZ").addCriterion(this.toCriterion(VenusItems.SOLAR_ARRAY_WAFER), this.hasItem(VenusItems.SOLAR_ARRAY_WAFER)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(AsteroidsItems.THERMAL_BOOTS).key('X', AsteroidsItems.THERMAL_CLOTH).patternLine("X X").patternLine("X X").addCriterion(this.toCriterion(AsteroidsItems.THERMAL_CLOTH), this.hasItem(AsteroidsItems.THERMAL_CLOTH)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(AsteroidsItems.THERMAL_CHESTPLATE).key('X', AsteroidsItems.THERMAL_CLOTH).patternLine("X X").patternLine("XXX").patternLine("XXX").addCriterion(this.toCriterion(AsteroidsItems.THERMAL_CLOTH), this.hasItem(AsteroidsItems.THERMAL_CLOTH)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(AsteroidsItems.THERMAL_HELMET).key('X', AsteroidsItems.THERMAL_CLOTH).patternLine("XXX").patternLine("X X").addCriterion(this.toCriterion(AsteroidsItems.THERMAL_CLOTH), this.hasItem(AsteroidsItems.THERMAL_CLOTH)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(AsteroidsItems.THERMAL_LEGGINGS).key('X', AsteroidsItems.THERMAL_CLOTH).patternLine("XXX").patternLine("X X").patternLine("X X").addCriterion(this.toCriterion(AsteroidsItems.THERMAL_CLOTH), this.hasItem(AsteroidsItems.THERMAL_CLOTH)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(VenusItems.TIER_2_THERMAL_BOOTS).key('X', VenusItems.ISOTHERMAL_FABRIC).patternLine("X X").patternLine("X X").addCriterion(this.toCriterion(VenusItems.ISOTHERMAL_FABRIC), this.hasItem(VenusItems.ISOTHERMAL_FABRIC)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(VenusItems.TIER_2_THERMAL_CHESTPLATE).key('X', VenusItems.ISOTHERMAL_FABRIC).patternLine("X X").patternLine("XXX").patternLine("XXX").addCriterion(this.toCriterion(VenusItems.ISOTHERMAL_FABRIC), this.hasItem(VenusItems.ISOTHERMAL_FABRIC)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(VenusItems.TIER_2_THERMAL_HELMET).key('X', VenusItems.ISOTHERMAL_FABRIC).patternLine("XXX").patternLine("X X").addCriterion(this.toCriterion(VenusItems.ISOTHERMAL_FABRIC), this.hasItem(VenusItems.ISOTHERMAL_FABRIC)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(VenusItems.TIER_2_THERMAL_LEGGINGS).key('X', VenusItems.ISOTHERMAL_FABRIC).patternLine("XXX").patternLine("X X").patternLine("X X").addCriterion(this.toCriterion(VenusItems.ISOTHERMAL_FABRIC), this.hasItem(VenusItems.ISOTHERMAL_FABRIC)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(AsteroidsItems.THERMAL_CLOTH).key('X', ItemTags.WOOL).key('Y', Tags.Items.DUSTS_REDSTONE).patternLine(" X ").patternLine("XYX").patternLine(" X ").addCriterion(this.toCriterion(ItemTags.WOOL), this.hasItem(ItemTags.WOOL)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MarsBlocks.MARS_COBBLESTONE_STAIRS, 4).key('#', MarsBlocks.MARS_COBBLESTONE).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion(this.toCriterion(MarsBlocks.MARS_COBBLESTONE), this.hasItem(MarsBlocks.MARS_COBBLESTONE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MarsBlocks.MARS_DUNGEON_BRICK_STAIRS, 4).key('#', MarsBlocks.MARS_DUNGEON_BRICKS).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion(this.toCriterion(MarsBlocks.MARS_DUNGEON_BRICKS), this.hasItem(MarsBlocks.MARS_DUNGEON_BRICKS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MarsBlocks.MARS_COBBLESTONE_SLAB, 6).key('#', MarsBlocks.MARS_COBBLESTONE).patternLine("###").addCriterion(this.toCriterion(MarsBlocks.MARS_COBBLESTONE), this.hasItem(MarsBlocks.MARS_COBBLESTONE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MarsBlocks.MARS_DUNGEON_BRICK_SLAB, 6).key('#', MarsBlocks.MARS_DUNGEON_BRICKS).patternLine("###").addCriterion(this.toCriterion(MarsBlocks.MARS_DUNGEON_BRICKS), this.hasItem(MarsBlocks.MARS_DUNGEON_BRICKS)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(AsteroidBlocks.DARK_DECORATION_SLAB, 6).key('#', AsteroidBlocks.DARK_DECORATION_BLOCK).patternLine("###").addCriterion(this.toCriterion(AsteroidBlocks.DARK_DECORATION_BLOCK), this.hasItem(AsteroidBlocks.DARK_DECORATION_BLOCK)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MarsBlocks.MARS_COBBLESTONE_WALL, 6).key('#', MarsBlocks.MARS_COBBLESTONE).patternLine("###").patternLine("###").addCriterion(this.toCriterion(MarsBlocks.MARS_COBBLESTONE), this.hasItem(MarsBlocks.MARS_COBBLESTONE)).build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MarsBlocks.MARS_DUNGEON_BRICK_WALL, 6).key('#', MarsBlocks.MARS_DUNGEON_BRICKS).patternLine("###").patternLine("###").addCriterion(this.toCriterion(MarsBlocks.MARS_DUNGEON_BRICKS), this.hasItem(MarsBlocks.MARS_DUNGEON_BRICKS)).build(consumer);

            ShapelessRecipeBuilder.shapelessRecipe(MarsItems.FRAGMENTED_CARBON, 32).addIngredient(Items.COAL).addIngredient(Items.COAL).addIngredient(Items.COAL).addIngredient(Items.COAL).setGroup("fragmented_carbon").addCriterion(this.toCriterion(Items.COAL), this.hasItem(Items.COAL)).build(consumer, this.modLoc("fragmented_carbon_from_4_coals"));
            ShapelessRecipeBuilder.shapelessRecipe(MarsItems.FRAGMENTED_CARBON, 16).addIngredient(Items.CHARCOAL).addIngredient(Items.CHARCOAL).addIngredient(Items.CHARCOAL).addIngredient(Items.CHARCOAL).setGroup("fragmented_carbon").addCriterion(this.toCriterion(Items.CHARCOAL), this.hasItem(Items.CHARCOAL)).build(consumer, this.modLoc("fragmented_carbon_from_4_charcoals"));
            ShapelessRecipeBuilder.shapelessRecipe(MarsItems.FRAGMENTED_CARBON, 4).addIngredient(Items.CHARCOAL).setGroup("fragmented_carbon").addCriterion(this.toCriterion(Items.CHARCOAL), this.hasItem(Items.CHARCOAL)).build(consumer, this.modLoc("fragmented_carbon_from_charcoal"));
            ShapelessRecipeBuilder.shapelessRecipe(MarsItems.DESH_INGOT, 9).addIngredient(MarsBlocks.DESH_BLOCK).addCriterion(this.toCriterion(MarsBlocks.DESH_BLOCK), this.hasItem(MarsBlocks.DESH_BLOCK)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(AsteroidsItems.TITANIUM_INGOT, 9).addIngredient(AsteroidBlocks.TITANIUM_BLOCK).addCriterion(this.toCriterion(AsteroidBlocks.TITANIUM_BLOCK), this.hasItem(AsteroidBlocks.TITANIUM_BLOCK)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(VenusItems.LEAD_INGOT, 9).addIngredient(VenusBlocks.LEAD_BLOCK).addCriterion(this.toCriterion(VenusBlocks.LEAD_BLOCK), this.hasItem(VenusBlocks.LEAD_BLOCK)).build(consumer);

            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(MarsItems.UNREFINED_DESH), MarsItems.DESH_INGOT, 0.8F, 200).addCriterion(this.toCriterion(MarsItems.UNREFINED_DESH), this.hasItem(MarsItems.UNREFINED_DESH)).build(consumer, this.from(MarsItems.DESH_INGOT, MarsItems.UNREFINED_DESH));
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(GCTags.DESH_ORES_ITEM), MarsItems.DESH_INGOT, 0.8F, 200).addCriterion(this.toCriterion(GCTags.DESH_ORES_ITEM), this.hasItem(GCTags.DESH_ORES_ITEM)).build(consumer, this.toSmelting(MarsItems.DESH_INGOT));
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(ItemTags.PLANKS), MarsItems.FRAGMENTED_CARBON, 0.1F, 200).addCriterion(this.toCriterion(ItemTags.PLANKS), this.hasItem(ItemTags.PLANKS)).build(consumer, this.toSmelting(MarsItems.FRAGMENTED_CARBON));
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(MarsBlocks.MARS_COBBLESTONE), MarsBlocks.MARS_STONE, 0.1F, 200).addCriterion(this.toCriterion(MarsBlocks.MARS_COBBLESTONE), this.hasItem(MarsBlocks.MARS_COBBLESTONE)).build(consumer, this.toSmelting(MarsBlocks.MARS_STONE));
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(MarsBlocks.MARS_IRON_ORE, AsteroidBlocks.ASTEROID_IRON_ORE, AsteroidsItems.IRON_SHARD), Items.IRON_INGOT, 0.7F, 200).addCriterion(this.toCriterion(Tags.Items.ORES_IRON), this.hasItem(Tags.Items.ORES_IRON)).build(consumer, this.from(Items.IRON_INGOT, Tags.Items.ORES_IRON));
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(AsteroidsItems.TITANIUM_SHARD, AsteroidsItems.TITANIUM_DUST), AsteroidsItems.TITANIUM_INGOT, 0.8F, 200).addCriterion(this.toCriterion(AsteroidsItems.TITANIUM_SHARD), this.hasItem(AsteroidsItems.TITANIUM_SHARD)).build(consumer, this.from(AsteroidsItems.TITANIUM_INGOT, AsteroidsItems.TITANIUM_SHARD));
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(AsteroidBlocks.ILMENITE_ORE), AsteroidsItems.TITANIUM_INGOT, 0.8F, 200).addCriterion(this.toCriterion(AsteroidBlocks.ILMENITE_ORE), this.hasItem(AsteroidBlocks.ILMENITE_ORE)).build(consumer, this.from(AsteroidsItems.TITANIUM_INGOT, AsteroidBlocks.ILMENITE_ORE));

            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(VenusBlocks.GALENA_ORE), VenusItems.LEAD_INGOT, 0.8F, 200).addCriterion(this.toCriterion(VenusBlocks.GALENA_ORE), this.hasItem(VenusBlocks.GALENA_ORE)).build(consumer, this.toSmelting(VenusItems.LEAD_INGOT));
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(VenusBlocks.VENUS_QUARTZ_ORE), Items.QUARTZ, 0.2F, 200).addCriterion(this.toCriterion(VenusBlocks.VENUS_QUARTZ_ORE), this.hasItem(VenusBlocks.VENUS_QUARTZ_ORE)).build(consumer, this.toSmelting(Items.QUARTZ));

            CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(MarsItems.UNREFINED_DESH), MarsItems.DESH_INGOT, 0.8F, 100).addCriterion(this.toCriterion(MarsItems.UNREFINED_DESH), this.hasItem(MarsItems.UNREFINED_DESH)).build(consumer, this.toBlasting(MarsItems.DESH_INGOT, MarsItems.UNREFINED_DESH));
            CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(GCTags.DESH_ORES_ITEM), MarsItems.DESH_INGOT, 0.8F, 100).addCriterion(this.toCriterion(GCTags.DESH_ORES_ITEM), this.hasItem(GCTags.DESH_ORES_ITEM)).build(consumer, this.toBlasting(MarsItems.DESH_INGOT));
            CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(ItemTags.PLANKS), MarsItems.FRAGMENTED_CARBON, 0.1F, 100).addCriterion(this.toCriterion(ItemTags.PLANKS), this.hasItem(ItemTags.PLANKS)).build(consumer, this.toBlasting(MarsItems.FRAGMENTED_CARBON));
            CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(MarsBlocks.MARS_COBBLESTONE), MarsBlocks.MARS_STONE, 0.1F, 100).addCriterion(this.toCriterion(MarsBlocks.MARS_COBBLESTONE), this.hasItem(MarsBlocks.MARS_COBBLESTONE)).build(consumer, this.toBlasting(MarsBlocks.MARS_STONE));
            CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(MarsBlocks.MARS_IRON_ORE, AsteroidBlocks.ASTEROID_IRON_ORE, AsteroidsItems.IRON_SHARD), Items.IRON_INGOT, 0.7F, 100).addCriterion(this.toCriterion(Tags.Items.ORES_IRON), this.hasItem(Tags.Items.ORES_IRON)).build(consumer, this.toBlasting(Items.IRON_INGOT));
            CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(AsteroidsItems.TITANIUM_SHARD, AsteroidsItems.TITANIUM_DUST), AsteroidsItems.TITANIUM_INGOT, 0.8F, 100).addCriterion(this.toCriterion(AsteroidsItems.TITANIUM_SHARD), this.hasItem(AsteroidsItems.TITANIUM_SHARD)).build(consumer, this.toBlasting(AsteroidsItems.TITANIUM_INGOT, AsteroidsItems.TITANIUM_SHARD));
            CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(AsteroidBlocks.ILMENITE_ORE), AsteroidsItems.TITANIUM_INGOT, 0.8F, 100).addCriterion(this.toCriterion(AsteroidBlocks.ILMENITE_ORE), this.hasItem(AsteroidBlocks.ILMENITE_ORE)).build(consumer, this.toBlasting(AsteroidsItems.TITANIUM_INGOT, AsteroidBlocks.ILMENITE_ORE));

            CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(VenusBlocks.GALENA_ORE), VenusItems.LEAD_INGOT, 0.8F, 100).addCriterion(this.toCriterion(VenusBlocks.GALENA_ORE), this.hasItem(VenusBlocks.GALENA_ORE)).build(consumer, this.toBlasting(VenusItems.LEAD_INGOT));
            CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(VenusBlocks.VENUS_QUARTZ_ORE), Items.QUARTZ, 0.2F, 100).addCriterion(this.toCriterion(VenusBlocks.VENUS_QUARTZ_ORE), this.hasItem(VenusBlocks.VENUS_QUARTZ_ORE)).build(consumer, this.toBlasting(Items.QUARTZ));
        }

        protected String toCriterion(IItemProvider provider)
        {
            return "has_" + this.toString(provider);
        }

        protected String toCriterion(Tag<?> tag)
        {
            return "has_" + tag.getId().getPath() + "_tag";
        }

        protected ResourceLocation toSmelting(IItemProvider provider)
        {
            return this.modLoc(this.toString(provider) + "_from_smelting");
        }

        protected ResourceLocation toBlasting(IItemProvider provider)
        {
            return this.modLoc(this.toString(provider) + "_from_blasting");
        }

        protected ResourceLocation toBlasting(IItemProvider provider, IItemProvider provider2)
        {
            return this.modLoc(this.toString(provider) + "_from_" + this.toString(provider2) + "_blasting");
        }

        protected ResourceLocation from(IItemProvider provider1, IItemProvider provider2)
        {
            return this.modLoc(this.toString(provider1) + "_from_" + this.toString(provider2));
        }

        protected ResourceLocation from(IItemProvider provider1, Tag<?> tag)
        {
            return this.modLoc(this.toString(provider1) + "_from_" + tag.getId().getPath() + "_tag");
        }

        protected String toString(IItemProvider provider)
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
        private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> tables = Lists.newArrayList();
        private static final ILootCondition.IBuilder SILK_TOUCH = MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))));

        public LootTables(DataGenerator generator)
        {
            super(generator);
            this.addTable(Pair.of(BlockLootTable::new, LootParameterSets.BLOCK)).addTable(Pair.of(EntityLootTable::new, LootParameterSets.ENTITY)).addTable(Pair.of(ChestLootTables::new, LootParameterSets.CHEST));
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

        public LootTables addTable(Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet> table)
        {
            this.tables.add(table);
            return this;
        }

        class BlockLootTable extends BlockLootTables
        {
            @Override
            protected void addTables()
            {
                this.registerDropSelfLootTable(MarsBlocks.MARS_FINE_REGOLITH);
                this.registerDropSelfLootTable(MarsBlocks.MARS_REGOLITH);
                this.registerDropSelfLootTable(MarsBlocks.MARS_COBBLESTONE);
                this.registerDropSelfLootTable(MarsBlocks.MARS_DUNGEON_BRICKS);
                this.registerDropSelfLootTable(MarsBlocks.DESH_BLOCK);
                this.registerDropSelfLootTable(MarsBlocks.MARS_IRON_ORE);
                this.registerDropSelfLootTable(MarsBlocks.MARS_TIN_ORE);
                this.registerDropSelfLootTable(MarsBlocks.MARS_COPPER_ORE);
                this.registerDropSelfLootTable(MarsBlocks.BLUE_SLIMELING_EGG);
                this.registerDropSelfLootTable(MarsBlocks.RED_SLIMELING_EGG);
                this.registerDropSelfLootTable(MarsBlocks.YELLOW_SLIMELING_EGG);
                this.registerDropSelfLootTable(MarsBlocks.METHANE_SYNTHESIZER);
                this.registerDropSelfLootTable(MarsBlocks.GAS_LIQUEFIER);
                this.registerDropSelfLootTable(MarsBlocks.TERRAFORMER);
                this.registerDropSelfLootTable(MarsBlocks.LAUNCH_CONTROLLER);
                this.registerDropSelfLootTable(MarsBlocks.WATER_ELECTROLYZER);
                this.registerDropSelfLootTable(MarsBlocks.CRYOGENIC_CHAMBER);
                this.registerDropSelfLootTable(MarsBlocks.CREEPER_EGG);
                this.registerDropSelfLootTable(MarsBlocks.MARS_COBBLESTONE_STAIRS);
                this.registerDropSelfLootTable(MarsBlocks.MARS_DUNGEON_BRICK_STAIRS);
                this.registerDropSelfLootTable(MarsBlocks.MARS_COBBLESTONE_WALL);
                this.registerDropSelfLootTable(MarsBlocks.MARS_DUNGEON_BRICK_WALL);
                this.registerLootTable(MarsBlocks.MARS_COBBLESTONE_SLAB, BlockLootTables::droppingSlab);
                this.registerLootTable(MarsBlocks.MARS_DUNGEON_BRICK_SLAB, BlockLootTables::droppingSlab);
                this.registerLootTable(MarsBlocks.CAVERNOUS_VINES, BlockLootTables::onlyWithShears);
                this.registerLootTable(MarsBlocks.DESH_ORE, block -> droppingItemWithFortune(block, MarsItems.UNREFINED_DESH));
                this.registerLootTable(MarsBlocks.MARS_STONE, block -> droppingWithSilkTouch(block, MarsBlocks.MARS_COBBLESTONE));

                this.registerDropSelfLootTable(AsteroidBlocks.ASTEROID_IRON_ORE);
                this.registerDropSelfLootTable(AsteroidBlocks.ASTEROID_ALUMINUM_ORE);
                this.registerDropSelfLootTable(AsteroidBlocks.TITANIUM_BLOCK);
                this.registerDropSelfLootTable(AsteroidBlocks.DARK_DECORATION_BLOCK);
                this.registerDropSelfLootTable(AsteroidBlocks.DARK_ASTEROID_ROCK);
                this.registerDropSelfLootTable(AsteroidBlocks.GRAY_ASTEROID_ROCK);
                this.registerDropSelfLootTable(AsteroidBlocks.LIGHT_GRAY_ASTEROID_ROCK);
                this.registerSilkTouch(AsteroidBlocks.DENSE_ICE);
                this.registerDropSelfLootTable(AsteroidBlocks.ASTRO_MINER_BASE);
                this.registerDropSelfLootTable(AsteroidBlocks.WALKWAY);
                this.registerDropSelfLootTable(AsteroidBlocks.FLUID_PIPE_WALKWAY);
                this.registerDropSelfLootTable(AsteroidBlocks.WIRE_WALKWAY);
                this.registerDropSelfLootTable(AsteroidBlocks.ENERGY_BEAM_REFLECTOR);
                this.registerDropSelfLootTable(AsteroidBlocks.ENERGY_BEAM_RECEIVER);
                this.registerDropSelfLootTable(AsteroidBlocks.SHORT_RANGE_TELEPAD);
                this.registerLootTable(AsteroidBlocks.DARK_DECORATION_SLAB, BlockLootTables::droppingSlab);
                this.registerLootTable(AsteroidBlocks.FULL_ASTRO_MINER_BASE, LootTable.builder().addLootPool(withSurvivesExplosion(AsteroidBlocks.ASTRO_MINER_BASE, LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(AsteroidBlocks.ASTRO_MINER_BASE).acceptFunction(SetCount.builder(ConstantRange.of(8)))))));
                this.registerLootTable(AsteroidBlocks.ILMENITE_ORE, block -> droppingItemWithFortune(block, AsteroidsItems.TITANIUM_SHARD)
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(AsteroidsItems.IRON_SHARD).acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE)).acceptCondition(SILK_TOUCH.inverted()))));

                this.registerDropSelfLootTable(VenusBlocks.VENUS_SOFT_ROCK);
                this.registerDropSelfLootTable(VenusBlocks.VENUS_HARD_ROCK);
                this.registerDropSelfLootTable(VenusBlocks.PUMICE);
                this.registerDropSelfLootTable(VenusBlocks.SCORCHED_VENUS_ROCK);
                this.registerDropSelfLootTable(VenusBlocks.ORANGE_VENUS_DUNGEON_BRICKS);
                this.registerDropSelfLootTable(VenusBlocks.RED_VENUS_DUNGEON_BRICKS);
                this.registerDropSelfLootTable(VenusBlocks.GALENA_ORE);
                this.registerDropSelfLootTable(VenusBlocks.VENUS_ALUMINUM_ORE);
                this.registerDropSelfLootTable(VenusBlocks.VENUS_COPPER_ORE);
                this.registerDropSelfLootTable(VenusBlocks.VENUS_QUARTZ_ORE);
                this.registerDropSelfLootTable(VenusBlocks.VENUS_TIN_ORE);
                this.registerDropSelfLootTable(VenusBlocks.CRASHED_PROBE);
                this.registerDropSelfLootTable(VenusBlocks.LEAD_BLOCK);
                this.registerDropSelfLootTable(VenusBlocks.GEOTHERMAL_GENERATOR);
                this.registerDropSelfLootTable(VenusBlocks.WEB_TORCH);
                this.registerDropSelfLootTable(VenusBlocks.SOLAR_ARRAY_MODULE);
                this.registerDropSelfLootTable(VenusBlocks.SOLAR_ARRAY_CONTROLLER);
                this.registerDropSelfLootTable(VenusBlocks.LASER_TURRET);
                this.registerLootTable(VenusBlocks.SOLAR_ORE, block -> droppingItemWithFortune(block, VenusItems.SOLAR_DUST));
                this.registerLootTable(VenusBlocks.VENUS_SILICON_ORE, block -> droppingItemWithFortune(block, GCItems.RAW_SILICON));
                this.registerLootTable(VenusBlocks.VENUS_QUARTZ_ORE, block -> droppingItemWithFortune(block, Items.QUARTZ));
                this.registerLootTable(VenusBlocks.WEB_STRING, BlockLootTables::onlyWithShears);
                this.registerSilkTouch(VenusBlocks.VENUS_VOLCANIC_ROCK);
                this.registerLootTable(VenusBlocks.VAPOR_SPOUT, block -> droppingWithSilkTouch(block, VenusBlocks.VENUS_SOFT_ROCK));
            }

            @Override
            protected Iterable<Block> getKnownBlocks()
            {
                return ForgeRegistries.BLOCKS.getValues().stream().filter(type -> type.getRegistryName().getNamespace().equals(Constants.MOD_ID_PLANETS)).collect(Collectors.toList());
            }
        }

        class EntityLootTable extends EntityLootTables
        {
            @Override
            protected void addTables()
            {
                this.registerLootTable(MarsEntities.SLIMELING, LootTable.builder()
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(Items.SLIME_BALL).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))));
                this.registerLootTable(MarsEntities.SLUDGELING, LootTable.builder());
                this.registerLootTable(MarsEntities.CREEPER_BOSS, LootTable.builder());
                this.registerLootTable(VenusEntities.JUICER, LootTable.builder());
                this.registerLootTable(VenusEntities.SPIDER_QUEEN, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(Items.STRING).acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F))))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(Items.SPIDER_EYE).acceptFunction(SetCount.builder(RandomValueRange.of(-1.0F, 1.0F))).acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))).acceptCondition(KilledByPlayer.builder())));
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

                consumer.accept(crashedProbe, LootTable.builder()
                        .addLootPool(LootPool.builder().rolls(RandomValueRange.of(4.0F, 6.0F))
                                .addEntry(ItemLootEntry.builder(MarsItems.TIER_2_HEAVY_DUTY_PLATE).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3.0F, 6.0F))))
                                .addEntry(ItemLootEntry.builder(GCItems.TIER_1_HEAVY_DUTY_PLATE).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3.0F, 6.0F))))
                                .addEntry(ItemLootEntry.builder(Items.IRON_INGOT).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(5.0F, 9.0F))))
                                .addEntry(ItemLootEntry.builder(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3.0F, 6.0F))))
                                .addEntry(ItemLootEntry.builder(AsteroidsItems.COMPRESSED_TITANIUM).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(3.0F, 6.0F))))));

                consumer.accept(marsDungeon, LootTable.builder()
                        .addLootPool(LootPool.builder().rolls(RandomValueRange.of(6.0F, 9.0F))
                                .addEntry(ItemLootEntry.builder(MarsItems.FRAGMENTED_CARBON).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 7.0F))))
                                .addEntry(ItemLootEntry.builder(GCItems.DEHYDRATED_APPLES).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F))))
                                .addEntry(ItemLootEntry.builder(GCItems.DEHYDRATED_CARROTS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F))))
                                .addEntry(ItemLootEntry.builder(GCItems.DEHYDRATED_MELONS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F))))
                                .addEntry(ItemLootEntry.builder(GCItems.DEHYDRATED_POTATOES).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F))))
                                .addEntry(ItemLootEntry.builder(GCItems.PARTIAL_OIL_CANISTER).weight(5).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), compound -> compound.putInt("Damage", 1001)))))
                                .addEntry(ItemLootEntry.builder(MarsItems.DESH_BOOTS).weight(2))
                                .addEntry(ItemLootEntry.builder(MarsItems.DESH_LEGGINGS).weight(2))
                                .addEntry(ItemLootEntry.builder(MarsItems.DESH_CHESTPLATE).weight(2))
                                .addEntry(ItemLootEntry.builder(MarsItems.DESH_HELMET).weight(2))
                                .addEntry(ItemLootEntry.builder(Items.REDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 7.0F))))
                                .addEntry(ItemLootEntry.builder(Items.MUSIC_DISC_MALL).weight(4))
                                .addEntry(ItemLootEntry.builder(Items.MUSIC_DISC_MELLOHI).weight(4))
                                .addEntry(ItemLootEntry.builder(MarsItems.UNREFINED_DESH).weight(5).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F))))
                                .addEntry(ItemLootEntry.builder(MarsItems.DESH_STICK).weight(5))
                                .addEntry(ItemLootEntry.builder(Items.BOOK).weight(8).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(22)).func_216059_e()))
                                .addEntry(ItemLootEntry.builder(Items.BOOK).weight(8).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(24)).func_216059_e()))));

                consumer.accept(venusDungeon, LootTable.builder()
                        .addLootPool(LootPool.builder().rolls(RandomValueRange.of(6.0F, 9.0F))
                                .addEntry(ItemLootEntry.builder(GCItems.DEHYDRATED_APPLES).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F))))
                                .addEntry(ItemLootEntry.builder(GCItems.DEHYDRATED_CARROTS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F))))
                                .addEntry(ItemLootEntry.builder(GCItems.DEHYDRATED_MELONS).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F))))
                                .addEntry(ItemLootEntry.builder(GCItems.DEHYDRATED_POTATOES).weight(6).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F))))
                                .addEntry(ItemLootEntry.builder(GCItems.PARTIAL_OIL_CANISTER).weight(5).acceptFunction(SetNBT.builder(Util.make(new CompoundNBT(), compound -> compound.putInt("Damage", 1001)))))
                                .addEntry(ItemLootEntry.builder(MarsItems.DESH_BOOTS).weight(2))
                                .addEntry(ItemLootEntry.builder(MarsItems.DESH_LEGGINGS).weight(2))
                                .addEntry(ItemLootEntry.builder(MarsItems.DESH_CHESTPLATE).weight(2))
                                .addEntry(ItemLootEntry.builder(MarsItems.DESH_HELMET).weight(2))
                                .addEntry(ItemLootEntry.builder(PlanetFluids.SULPHURIC_ACID.getBucket()).weight(4))
                                .addEntry(ItemLootEntry.builder(Items.GOLDEN_APPLE).weight(10))
                                .addEntry(ItemLootEntry.builder(Items.ENCHANTED_GOLDEN_APPLE).weight(2))
                                .addEntry(ItemLootEntry.builder(Items.REDSTONE).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 7.0F))))
                                .addEntry(ItemLootEntry.builder(Items.MUSIC_DISC_CHIRP).weight(4))
                                .addEntry(ItemLootEntry.builder(Items.MUSIC_DISC_WAIT).weight(4))
                                .addEntry(ItemLootEntry.builder(Items.MUSIC_DISC_WARD).weight(4))
                                .addEntry(ItemLootEntry.builder(VenusItems.ISOTHERMAL_FABRIC).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 7.0F))))
                                .addEntry(ItemLootEntry.builder(Items.GOLD_NUGGET).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 4.0F))))
                                .addEntry(ItemLootEntry.builder(Items.BOOK).weight(8).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(18)).func_216059_e()))
                                .addEntry(ItemLootEntry.builder(Items.BOOK).weight(8).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(27)).func_216059_e()))
                                .addEntry(ItemLootEntry.builder(Items.BOOK).weight(8).acceptFunction(EnchantWithLevels.func_215895_a(ConstantRange.of(30)).func_216059_e()))));
            }
        }
    }
}