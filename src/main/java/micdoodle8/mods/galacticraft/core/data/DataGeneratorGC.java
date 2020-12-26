package micdoodle8.mods.galacticraft.core.data;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.block.Block;
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
            this.simpleBlock(GCBlocks.AIR_LOCK_FRAME);
            this.simpleBlock(GCBlocks.TIER_1_TREASURE_CHEST);

            ModelFile model = this.models().cubeBottomTop("air_lock_controller", this.modLoc("block/air_lock_controller"), this.modLoc("block/air_lock_frame"), this.modLoc("block/air_lock_frame"));
            this.simpleBlock(GCBlocks.AIR_LOCK_CONTROLLER, model);

            model = this.models().cubeAll("player_detector", this.modLoc("block/tin_decoration"));
            this.simpleBlock(GCBlocks.PLAYER_DETECTOR, model);
            model = this.models().cubeAll("hidden_redstone_wire", this.modLoc("block/tin_decoration"));
            this.simpleBlock(GCBlocks.HIDDEN_REDSTONE_WIRE, model);
            model = this.models().cubeAll("hidden_redstone_repeater", this.modLoc("block/tin_decoration"));
            this.simpleBlock(GCBlocks.HIDDEN_REDSTONE_REPEATER, model);

            model = this.models().cubeBottomTop("magnetic_crafting_table", this.modLoc("block/magnetic_crafting_table_side"), this.modLoc("block/magnetic_crafting_table_bottom"), this.modLoc("block/magnetic_crafting_table_top"));
            this.directionalBlock(GCBlocks.MAGNETIC_CRAFTING_TABLE, model);

            model = this.models().withExistingParent("glowstone_torch", "block/template_torch").texture("torch", this.modLoc("block/glowstone_torch"));
            this.simpleBlock(GCBlocks.GLOWSTONE_TORCH, model);
            model = this.models().withExistingParent("unlit_torch", "block/template_torch").texture("torch", this.modLoc("block/unlit_torch"));
            this.simpleBlock(GCBlocks.UNLIT_TORCH, model);
            model = this.models().withExistingParent("lit_unlit_torch", "block/template_torch").texture("torch", this.modLoc("block/lit_unlit_torch"));
            this.simpleBlock(GCBlocks.LIT_UNLIT_TORCH, model);

            model = this.models().withExistingParent("wall_glowstone_torch", "block/torch_wall").texture("torch", this.modLoc("block/glowstone_torch"));
            this.horizontalBlock(GCBlocks.WALL_GLOWSTONE_TORCH, model, 90);
            model = this.models().withExistingParent("wall_unlit_torch", "block/torch_wall").texture("torch", this.modLoc("block/unlit_torch"));
            this.horizontalBlock(GCBlocks.WALL_UNLIT_TORCH, model, 90);
            model = this.models().withExistingParent("wall_lit_unlit_torch", "block/torch_wall").texture("torch", this.modLoc("block/lit_unlit_torch"));
            this.horizontalBlock(GCBlocks.WALL_LIT_UNLIT_TORCH, model, 90);

            model = this.models().cubeBottomTop("moon_turf", this.modLoc("block/moon_turf_side"), this.modLoc("block/moon_dirt"), this.modLoc("block/moon_turf_top"));
            this.simpleBlock(GCBlocks.MOON_TURF, model);
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
            this.itemGenerated(GCBlocks.GLOWSTONE_TORCH);
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