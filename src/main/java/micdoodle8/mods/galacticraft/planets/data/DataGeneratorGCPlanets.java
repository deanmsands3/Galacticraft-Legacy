package micdoodle8.mods.galacticraft.planets.data;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockSlimelingEgg;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockGeothermalGenerator;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.items.VenusItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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

            this.getVariantBuilder(MarsBlocks.BLUE_SLIMELING_EGG).forAllStates(state -> ConfiguredModel.builder().modelFile(this.getSlimelingEggModel(state, "blue_slimeling_egg")).build());
            this.getVariantBuilder(MarsBlocks.RED_SLIMELING_EGG).forAllStates(state -> ConfiguredModel.builder().modelFile(this.getSlimelingEggModel(state, "red_slimeling_egg")).build());
            this.getVariantBuilder(MarsBlocks.YELLOW_SLIMELING_EGG).forAllStates(state -> ConfiguredModel.builder().modelFile(this.getSlimelingEggModel(state, "yellow_slimeling_egg")).build());

            this.getVariantBuilder(MarsBlocks.CAVERNOUS_VINES).partialState().setModels(new ConfiguredModel(this.models().cross("cavernous_vines_1", this.modLoc("block/cavernous_vines_1"))),
                    new ConfiguredModel(this.models().cross("cavernous_vines_2", this.modLoc("block/cavernous_vines_2"))),
                    new ConfiguredModel(this.models().cross("cavernous_vines_3", this.modLoc("block/cavernous_vines_3"))));

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
            this.simpleBlock(AsteroidBlocks.DARK_ASTEROID_ROCK);
            this.simpleBlock(AsteroidBlocks.GRAY_ASTEROID_ROCK);
            this.simpleBlock(AsteroidBlocks.LIGHT_GRAY_ASTEROID_ROCK);
            this.simpleBlock(AsteroidBlocks.DENSE_ICE);
            this.simpleBlock(AsteroidBlocks.ASTRO_MINER_BASE, this.models().cubeAll("astro_miner_base", this.modLoc("block/machine_frame")));
            this.simpleBlock(AsteroidBlocks.FULL_ASTRO_MINER_BASE, this.models().cubeAll("full_astro_miner_base", this.modLoc("block/machine_frame")));
            this.simpleBlock(AsteroidBlocks.SHORT_RANGE_TELEPAD, this.models().cubeAll("short_range_telepad", this.modLoc("block/short_range_telepad_base")));
            this.simpleBlock(AsteroidBlocks.SHORT_RANGE_TELEPAD_DUMMY, this.models().cubeAll("short_range_telepad_dummy", this.modLoc("block/short_range_telepad_base")));
            this.simpleBlock(AsteroidBlocks.ENERGY_BEAM_RECEIVER, this.models().cubeAll("energy_beam_receiver", this.modLoc("block/energy_beam_receiver")));
            this.simpleBlock(AsteroidBlocks.ENERGY_BEAM_REFLECTOR, this.models().cubeAll("energy_beam_reflector", this.modLoc("block/energy_beam_reflector")));

            this.simpleBlock(VenusBlocks.VENUS_SOFT_ROCK);//TODO Random rotation
            this.simpleBlock(VenusBlocks.VENUS_HARD_ROCK);//TODO Random rotation
            this.simpleBlock(VenusBlocks.VENUS_VOLCANIC_ROCK);
            this.simpleBlock(VenusBlocks.PUMICE);//TODO Random rotation
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
            this.simpleCross(VenusBlocks.WEB_STRING);
            this.simpleCross(VenusBlocks.WEB_TORCH);

            model = this.models().cubeTop("vapor_spout", this.modLoc("block/venus_soft_rock"), this.modLoc("block/vapor_spout"));
            this.simpleBlock(VenusBlocks.VAPOR_SPOUT, model);

            model = this.models().cube("crashed_probe", this.modLoc("block/crashed_probe_top"), this.modLoc("block/crashed_probe_bottom"), this.modLoc("block/crashed_probe_side_2"), this.modLoc("block/crashed_probe_side_1"), this.modLoc("block/crashed_probe_side_1"), this.modLoc("block/crashed_probe_side_2")).texture("particle", this.modLoc("block/crashed_probe_side_1"));
            this.simpleBlock(VenusBlocks.CRASHED_PROBE, model);

            ModelFile geothermalOn = this.models().cube("geothermal_generator_on", machineBase, this.modLoc("block/geothermal_generator_top"), this.modLoc("block/geothermal_generator_on"), this.modLoc("block/geothermal_generator_on"), machineOutput, machineSide).texture("particle", this.modLoc("block/geothermal_generator_on")).texture("particle", this.modLoc("block/geothermal_generator_on"));
            ModelFile geothermalOff = this.models().cube("geothermal_generator", machineBase, this.modLoc("block/geothermal_generator_top"), this.modLoc("block/geothermal_generator"), this.modLoc("block/geothermal_generator"), machineOutput, machineSide).texture("particle", this.modLoc("block/geothermal_generator")).texture("particle", this.modLoc("block/geothermal_generator"));

            this.getVariantBuilder(VenusBlocks.GEOTHERMAL_GENERATOR)
            .forAllStates(state -> ConfiguredModel.builder()
                    .modelFile(state.get(BlockGeothermalGenerator.ACTIVE) ? geothermalOn : geothermalOff)
                    .rotationY((int) state.get(BlockGeothermalGenerator.FACING).getOpposite().getHorizontalAngle())
                    .build());

            /*this.simpleFluid(PlanetFluids.GAS_ARGON.getBlock());
            this.simpleFluid(PlanetFluids.GAS_ATMOSPHERIC.getBlock());
            this.simpleFluid(PlanetFluids.GAS_CARBON_DIOXIDE.getBlock());
            this.simpleFluid(PlanetFluids.GAS_HELIUM.getBlock());
            this.simpleFluid(PlanetFluids.GAS_METHANE.getBlock());
            this.simpleFluid(PlanetFluids.GAS_NITROGEN.getBlock());
            this.simpleFluid(PlanetFluids.LIQUID_ARGON.getBlock());
            this.simpleFluid(PlanetFluids.LIQUID_BACTERIAL_SLUDGE.getBlock());
            this.simpleFluid(PlanetFluids.LIQUID_METHANE.getBlock());
            this.simpleFluid(PlanetFluids.LIQUID_NITROGEN.getBlock());
            this.simpleFluid(PlanetFluids.LIQUID_OXYGEN.getBlock());
            this.simpleFluid(PlanetFluids.LIQUID_SULPHURIC_ACID.getBlock());*/
        }

        protected ModelFile getSlimelingEggModel(BlockState state, String name)
        {
            return state.get(BlockSlimelingEgg.CRACKED) ? this.models().withExistingParent(name + "_cracked", this.modLoc("block/slimeling_egg")).texture("texture", this.modLoc("block/" + name + "_cracked")).texture("particle", this.modLoc("block/" + name + "_cracked")) : this.models().withExistingParent(name, this.modLoc("block/slimeling_egg")).texture("texture", this.modLoc("block/" + name)).texture("particle", this.modLoc("block/" + name));
        }

        protected void simpleFluid(FlowingFluidBlock block)
        {
            this.getVariantBuilder(block).partialState().setModels(new ConfiguredModel(this.models().getBuilder(this.toString(block)).texture("particle", this.modLoc(block.getFluid().getAttributes().getStillTexture().getPath()))));
        }

        protected void simpleCross(Block block)
        {
            this.getVariantBuilder(block).partialState().setModels(new ConfiguredModel(this.models().cross(this.toString(block), this.modLoc("block/" + this.toString(block)))));
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
            this.add(MarsBlocks.BLUE_SLIMELING_EGG, "Blue Slimeline Egg");
            this.add(MarsBlocks.RED_SLIMELING_EGG, "Blue Slimeline Egg");
            this.add(MarsBlocks.YELLOW_SLIMELING_EGG, "Blue Slimeline Egg");
            this.add(MarsBlocks.CAVERNOUS_VINES, "Cavernous Vines");
            this.add(MarsBlocks.METHANE_SYNTHESIZER, "Methane Synthesizer");
            this.add(MarsBlocks.GAS_LIQUEFIER, "Gas Liquefier");
            this.add(MarsBlocks.TERRAFORMER, "Terraformer");
            this.add(MarsBlocks.LAUNCH_CONTROLLER, "Launch Controller");
            this.add(MarsBlocks.WATER_ELECTROLYZER, "Water Electrolyzer");
            this.add(MarsBlocks.TIER_2_TREASURE_CHEST, "Tier 2 Treasure Chest");
            this.add(MarsBlocks.CRYOGENIC_CHAMBER, "Cryogenic Chamber");

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
            this.add(MarsItems.TIER_2_HEAVY_DUTY_PLATE, "Tier 2 Heavy Duty Plate");
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
            this.add(AsteroidsItems.TIER_3_HEAVY_DUTY_PLATE, "Tier 3 Heavy Duty Plate");
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
}