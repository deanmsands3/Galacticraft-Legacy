package micdoodle8.mods.galacticraft.planets.data;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockSlimelingEgg;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
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

            this.getVariantBuilder(MarsBlocks.CAVERNOUS_VINES).partialState().setModels(new ConfiguredModel(this.models().cross("cavernous_vines_1", this.getBlockTexture("cavernous_vines_1"))),
                    new ConfiguredModel(this.models().cross("cavernous_vines_2", this.getBlockTexture("cavernous_vines_2"))),
                    new ConfiguredModel(this.models().cross("cavernous_vines_3", this.getBlockTexture("cavernous_vines_3"))));

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
        }

        protected ModelFile getSlimelingEggModel(BlockState state, String name)
        {
            return state.get(BlockSlimelingEgg.CRACKED) ? this.models().withExistingParent(name + "_cracked", this.modLoc("block/slimeling_egg")).texture("texture", this.modLoc("block/" + name + "_cracked")) : this.models().withExistingParent(name, this.modLoc("block/slimeling_egg")).texture("texture", this.modLoc("block/" + name));
        }

        protected void simpleCross(Block block)
        {
            this.simpleCross(block, this.toString(block));
        }

        protected void simpleCross(Block block, String name)
        {
            this.getVariantBuilder(block).partialState().setModels(new ConfiguredModel(this.models().cross(name, this.getBlockTexture(name))));
        }

        protected void simpleFluid(FlowingFluidBlock block)
        {
            this.getVariantBuilder(block).partialState().setModels(new ConfiguredModel(this.models().getBuilder(this.toString(block)).texture("particle", this.modLoc(block.getFluid().getAttributes().getStillTexture().getPath()))));
        }

        protected ResourceLocation getBlockTexture(String name)
        {
            return new ResourceLocation(Constants.MOD_ID_PLANETS, "block/" + name);
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
            this.itemGenerated(MarsBlocks.CAVERNOUS_VINES, "cavernous_vines_1");
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
        }
    }
}