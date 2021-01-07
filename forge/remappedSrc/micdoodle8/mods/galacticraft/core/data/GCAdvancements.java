package micdoodle8.mods.galacticraft.core.data;

import java.util.function.Consumer;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.advancement.GCTriggers;
import micdoodle8.mods.galacticraft.core.dimension.GCDimensions;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.ChangeDimensionTrigger;
import net.minecraft.advancements.criterion.EnterBlockTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.TickTrigger;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class GCAdvancements implements Consumer<Consumer<Advancement>>
{
    @Override
    public void accept(Consumer<Advancement> consumer)
    {
        Advancement root = Advancement.Builder.builder().withDisplay(GCItems.TIER_1_ROCKET, this.title("root"), this.description("root"), new ResourceLocation("galacticraftcore:textures/gui/galacticraft_background.png"), FrameType.TASK, false, false, false).withCriterion("tick", new TickTrigger.Instance()).register(consumer, this.modLoc("root"));
        Advancement coalGen = Advancement.Builder.builder().withParent(root).withDisplay(GCBlocks.COAL_GENERATOR, this.title("coal_generator"), this.description("coal_generator"), null, FrameType.TASK, true, true, false).withCriterion("coal_generator", InventoryChangeTrigger.Instance.forItems(GCBlocks.COAL_GENERATOR)).register(consumer, this.modLoc("coal_generator"));
        Advancement circuitFab = Advancement.Builder.builder().withParent(coalGen).withDisplay(GCBlocks.CIRCUIT_FABRICATOR, this.title("circuit_fabricator"), this.description("circuit_fabricator"), null, FrameType.TASK, true, true, false).withCriterion("circuit_fabricator", InventoryChangeTrigger.Instance.forItems(GCBlocks.CIRCUIT_FABRICATOR)).register(consumer, this.modLoc("circuit_fabricator"));
        Advancement basicWafer = Advancement.Builder.builder().withParent(circuitFab).withDisplay(GCItems.BASIC_WAFER, this.title("basic_wafer"), this.description("basic_wafer"), null, FrameType.TASK, true, true, false).withCriterion("basic_wafer", InventoryChangeTrigger.Instance.forItems(GCItems.BASIC_WAFER)).register(consumer, this.modLoc("basic_wafer"));
        Advancement compressor = Advancement.Builder.builder().withParent(basicWafer).withDisplay(GCBlocks.COMPRESSOR, this.title("compressor"), this.description("compressor"), null, FrameType.TASK, true, true, false).withCriterion("compressor", InventoryChangeTrigger.Instance.forItems(GCBlocks.COMPRESSOR)).register(consumer, this.modLoc("compressor"));
        Advancement.Builder.builder().withParent(basicWafer).withDisplay(GCItems.ADVANCED_WAFER, this.title("advanced_wafer"), this.description("advanced_wafer"), null, FrameType.TASK, true, true, false).withCriterion("advanced_wafer", InventoryChangeTrigger.Instance.forItems(GCItems.ADVANCED_WAFER)).register(consumer, this.modLoc("advanced_wafer"));
        Advancement basicSolarPanel = Advancement.Builder.builder().withParent(basicWafer).withDisplay(GCBlocks.BASIC_SOLAR_PANEL, this.title("basic_solar_panel"), this.description("basic_solar_panel"), null, FrameType.TASK, true, true, false).withCriterion("basic_solar_panel", InventoryChangeTrigger.Instance.forItems(GCBlocks.BASIC_SOLAR_PANEL)).register(consumer, this.modLoc("basic_solar_panel"));
        Advancement.Builder.builder().withParent(basicSolarPanel).withDisplay(GCBlocks.ADVANCED_SOLAR_PANEL, this.title("advanced_solar_panel"), this.description("advanced_solar_panel"), null, FrameType.TASK, true, true, false).withCriterion("advanced_solar_panel", InventoryChangeTrigger.Instance.forItems(GCBlocks.ADVANCED_SOLAR_PANEL)).register(consumer, this.modLoc("advanced_solar_panel"));
        Advancement.Builder.builder().withParent(compressor).withDisplay(GCBlocks.ELECTRIC_COMPRESSOR, this.title("electric_compressor"), this.description("electric_compressor"), null, FrameType.TASK, true, true, false).withCriterion("electric_compressor", InventoryChangeTrigger.Instance.forItems(GCBlocks.ELECTRIC_COMPRESSOR)).register(consumer, this.modLoc("electric_compressor"));
        Advancement rocketWorkbench = Advancement.Builder.builder().withParent(compressor).withDisplay(GCBlocks.ROCKET_WORKBENCH, this.title("rocket_workbench"), this.description("rocket_workbench"), null, FrameType.TASK, true, true, false).withCriterion("rocket_workbench", InventoryChangeTrigger.Instance.forItems(GCBlocks.ROCKET_WORKBENCH)).register(consumer, this.modLoc("rocket_workbench"));
        Advancement tier1Rocket = Advancement.Builder.builder().withParent(rocketWorkbench).withDisplay(GCItems.TIER_1_ROCKET, this.title("tier_1_rocket"), this.description("tier_1_rocket"), null, FrameType.TASK, true, true, false).withRequirementsStrategy(IRequirementsStrategy.OR).withCriterion("tier_1_rocket", InventoryChangeTrigger.Instance.forItems(GCItems.TIER_1_ROCKET)).withCriterion("tier_1_rocket_18_inventory", InventoryChangeTrigger.Instance.forItems(GCItems.TIER_1_ROCKET_18_INVENTORY)).withCriterion("tier_1_rocket_36_inventory", InventoryChangeTrigger.Instance.forItems(GCItems.TIER_1_ROCKET_36_INVENTORY)).withCriterion("tier_1_rocket_54_inventory", InventoryChangeTrigger.Instance.forItems(GCItems.TIER_1_ROCKET_54_INVENTORY)).register(consumer, this.modLoc("tier_1_rocket"));
        Advancement fuelLoader = Advancement.Builder.builder().withParent(tier1Rocket).withDisplay(GCBlocks.FUEL_LOADER, this.title("fuel_loader"), this.description("fuel_loader"), null, FrameType.TASK, true, true, false).withCriterion("fuel_loader", InventoryChangeTrigger.Instance.forItems(GCBlocks.FUEL_LOADER)).register(consumer, this.modLoc("fuel_loader"));
        Advancement launchRocket = Advancement.Builder.builder().withParent(fuelLoader).withDisplay(Items.FLINT_AND_STEEL, this.title("launch_rocket"), this.description("launch_rocket"), null, FrameType.GOAL, true, true, false).withCriterion("launch_rocket", new GCTriggers.LaunchRocketInstance()).register(consumer, this.modLoc("launch_rocket"));
        Advancement toMoon = Advancement.Builder.builder().withParent(launchRocket).withDisplay(GCBlocks.MOON_TURF, this.title("moon"), this.description("moon"), null, FrameType.CHALLENGE, true, true, false).withCriterion("traveled_to_moon", ChangeDimensionTrigger.Instance.changedDimensionTo(GCDimensions.MOON_DIMENSION)).register(consumer, this.modLoc("moon"));
        Advancement.Builder.builder().withParent(toMoon).withDisplay(GCItems.FULL_SOLAR_MODULE, this.title("create_space_station"), this.description("create_space_station"), null, FrameType.GOAL, true, true, false).withCriterion("create_space_station", new GCTriggers.CreateSpaceStationInstance()).register(consumer, this.modLoc("create_space_station"));
        Advancement moonDungeon = Advancement.Builder.builder().withParent(toMoon).withDisplay(GCBlocks.MOON_DUNGEON_BRICKS, this.title("moon_dungeon"), this.description("moon_dungeon"), null, FrameType.GOAL, true, true, false).withCriterion("moon_dungeon", new GCTriggers.FindMoonBossInstance()).register(consumer, this.modLoc("moon_dungeon"));
        //Advancement spinThruster = Advancement.Builder.builder().withParent(createSpaceStation).withDisplay(Items.FLINT_AND_STEEL, title("spin_thruster"), description("spin_thruster"), null, FrameType.TASK, true, true, false).withCriterion("spin_thruster", InventoryChangeTrigger.Instance.forItems(GCBlocks.SPIN_THRUSTER)).register(consumer, this.modLoc("spin_thruster"));
        Advancement tier1DungeonKey = Advancement.Builder.builder().withParent(moonDungeon).withDisplay(GCItems.TIER_1_DUNGEON_KEY, this.title("dungeon_key_moon"), this.description("dungeon_key_moon"), null, FrameType.CHALLENGE, true, true, false).withCriterion("dungeon_key_moon", InventoryChangeTrigger.Instance.forItems(GCItems.TIER_1_DUNGEON_KEY)).register(consumer, this.modLoc("dungeon_key_moon"));
        Advancement moonBuggySchematic = Advancement.Builder.builder().withParent(tier1DungeonKey).withDisplay(GCItems.MOON_BUGGY_SCHEMATIC, this.title("moon_buggy_schematic"), this.description("moon_buggy_schematic"), null, FrameType.TASK, true, true, false).withCriterion("moon_buggy_schematic", InventoryChangeTrigger.Instance.forItems(GCItems.MOON_BUGGY_SCHEMATIC)).register(consumer, this.modLoc("moon_buggy_schematic"));
        Advancement.Builder.builder().withParent(moonBuggySchematic).withDisplay(GCItems.BUGGY, this.title("moon_buggy"), this.description("moon_buggy"), null, FrameType.GOAL, true, true, false).withRequirementsStrategy(IRequirementsStrategy.OR).withCriterion("buggy", InventoryChangeTrigger.Instance.forItems(GCItems.BUGGY)).withCriterion("buggy_18_inventory", InventoryChangeTrigger.Instance.forItems(GCItems.BUGGY_18_INVENTORY)).withCriterion("buggy_36_inventory", InventoryChangeTrigger.Instance.forItems(GCItems.BUGGY_36_INVENTORY)).withCriterion("buggy_54_inventory", InventoryChangeTrigger.Instance.forItems(GCItems.BUGGY_54_INVENTORY)).register(consumer, this.modLoc("moon_buggy"));
        Advancement oxygenCollector = Advancement.Builder.builder().withParent(compressor).withDisplay(GCBlocks.OXYGEN_COLLECTOR, this.title("oxygen_collector"), this.description("oxygen_collector"), null, FrameType.TASK, true, true, false).withCriterion("oxygen_collector", InventoryChangeTrigger.Instance.forItems(GCBlocks.OXYGEN_COLLECTOR)).register(consumer, this.modLoc("oxygen_collector"));
        Advancement oxygenCompressor = Advancement.Builder.builder().withParent(oxygenCollector).withDisplay(GCBlocks.OXYGEN_COMPRESSOR, this.title("oxygen_compressor"), this.description("oxygen_compressor"), null, FrameType.TASK, true, true, false).withCriterion("oxygen_compressor", InventoryChangeTrigger.Instance.forItems(GCBlocks.OXYGEN_COMPRESSOR)).register(consumer, this.modLoc("oxygen_compressor"));
        Advancement fillOxygenTank = Advancement.Builder.builder().withParent(oxygenCompressor).withDisplay(GCItems.MEDIUM_OXYGEN_TANK, this.title("fill_tank"), this.description("fill_tank"), null, FrameType.TASK, true, true, false).withRequirementsStrategy(IRequirementsStrategy.OR).withCriterion("light_oxygen_tank", InventoryChangeTrigger.Instance.forItems(GCItems.LIGHT_OXYGEN_TANK)).withCriterion("medium_oxygen_tank", InventoryChangeTrigger.Instance.forItems(GCItems.MEDIUM_OXYGEN_TANK)).withCriterion("heavy_oxygen_tank", InventoryChangeTrigger.Instance.forItems(GCItems.HEAVY_OXYGEN_TANK)).register(consumer, this.modLoc("fill_tank"));
        Advancement.Builder.builder().withParent(fillOxygenTank).withDisplay(GCItems.HEAVY_OXYGEN_TANK, this.title("fill_all_tanks"), this.description("fill_all_tanks"), null, FrameType.GOAL, true, true, false).withRequirementsStrategy(IRequirementsStrategy.OR).withCriterion("light_oxygen_tank", InventoryChangeTrigger.Instance.forItems(GCItems.LIGHT_OXYGEN_TANK)).withCriterion("medium_oxygen_tank", InventoryChangeTrigger.Instance.forItems(GCItems.MEDIUM_OXYGEN_TANK)).withCriterion("heavy_oxygen_tank", InventoryChangeTrigger.Instance.forItems(GCItems.HEAVY_OXYGEN_TANK)).register(consumer, this.modLoc("fill_all_tanks"));
        Advancement.Builder.builder().withParent(fillOxygenTank).withDisplay(GCItems.OXYGEN_MASK, this.title("all_oxygen_gear"), this.description("all_oxygen_gear"), null, FrameType.GOAL, true, true, false)
        .withCriterion("light_oxygen_tank", InventoryChangeTrigger.Instance.forItems(GCItems.LIGHT_OXYGEN_TANK, GCItems.MEDIUM_OXYGEN_TANK, GCItems.HEAVY_OXYGEN_TANK))
        .withCriterion("oxygen_mask", InventoryChangeTrigger.Instance.forItems(GCItems.OXYGEN_MASK))
        .withCriterion("oxygen_gear", InventoryChangeTrigger.Instance.forItems(GCItems.OXYGEN_GEAR)).register(consumer, this.modLoc("all_oxygen_gear"));
        Advancement oil = Advancement.Builder.builder().withParent(compressor).withDisplay(GCFluids.OIL.getBucket(), this.title("oil"), this.description("oil"), null, FrameType.TASK, true, true, false).withCriterion("oil", EnterBlockTrigger.Instance.forBlock(GCFluids.OIL.getBlock())).register(consumer, this.modLoc("oil"));
        Advancement refinery = Advancement.Builder.builder().withParent(oil).withDisplay(GCBlocks.REFINERY, this.title("refinery"), this.description("refinery"), null, FrameType.TASK, true, true, false).withCriterion("refinery", InventoryChangeTrigger.Instance.forItems(GCBlocks.REFINERY)).register(consumer, this.modLoc("refinery"));
        Advancement.Builder.builder().withParent(refinery).withDisplay(GCFluids.FUEL.getBucket(), this.title("rocket_fuel"), this.description("rocket_fuel"), null, FrameType.GOAL, true, true, false).withCriterion("fuel_bucket", InventoryChangeTrigger.Instance.forItems(GCFluids.FUEL.getBucket())).register(consumer, this.modLoc("rocket_fuel"));
    }

    private TranslationTextComponent title(String name)
    {
        return new TranslationTextComponent("advancement.galacticraft." + name + ".title");
    }

    private TranslationTextComponent description(String name)
    {
        return new TranslationTextComponent("advancement.galacticraft." + name + ".description");
    }

    private String modLoc(String name)
    {
        return new ResourceLocation(Constants.MOD_ID_CORE, "galacticraft/" + name).toString();
    }
}