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
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.TickTrigger;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class GCAdvancements implements Consumer<Consumer<Advancement>>
{
    @Override
    public void accept(Consumer<Advancement> consumer)
    {
        Advancement root = Advancement.Builder.advancement().display(GCItems.TIER_1_ROCKET, this.title("root"), this.description("root"), new ResourceLocation("galacticraftcore:textures/gui/galacticraft_background.png"), FrameType.TASK, false, false, false).withCriterion("tick", new TickTrigger.TriggerInstance()).register(consumer, this.modLoc("root"));
        Advancement coalGen = Advancement.Builder.advancement().parent(root).display(GCBlocks.COAL_GENERATOR, this.title("coal_generator"), this.description("coal_generator"), null, FrameType.TASK, true, true, false).withCriterion("coal_generator", InventoryChangeTrigger.TriggerInstance.hasItem(GCBlocks.COAL_GENERATOR)).register(consumer, this.modLoc("coal_generator"));
        Advancement circuitFab = Advancement.Builder.advancement().parent(coalGen).display(GCBlocks.CIRCUIT_FABRICATOR, this.title("circuit_fabricator"), this.description("circuit_fabricator"), null, FrameType.TASK, true, true, false).withCriterion("circuit_fabricator", InventoryChangeTrigger.TriggerInstance.hasItem(GCBlocks.CIRCUIT_FABRICATOR)).register(consumer, this.modLoc("circuit_fabricator"));
        Advancement basicWafer = Advancement.Builder.advancement().parent(circuitFab).display(GCItems.BASIC_WAFER, this.title("basic_wafer"), this.description("basic_wafer"), null, FrameType.TASK, true, true, false).withCriterion("basic_wafer", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.BASIC_WAFER)).register(consumer, this.modLoc("basic_wafer"));
        Advancement compressor = Advancement.Builder.advancement().parent(basicWafer).display(GCBlocks.COMPRESSOR, this.title("compressor"), this.description("compressor"), null, FrameType.TASK, true, true, false).withCriterion("compressor", InventoryChangeTrigger.TriggerInstance.hasItem(GCBlocks.COMPRESSOR)).register(consumer, this.modLoc("compressor"));
        Advancement.Builder.advancement().parent(basicWafer).display(GCItems.ADVANCED_WAFER, this.title("advanced_wafer"), this.description("advanced_wafer"), null, FrameType.TASK, true, true, false).withCriterion("advanced_wafer", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.ADVANCED_WAFER)).register(consumer, this.modLoc("advanced_wafer"));
        Advancement basicSolarPanel = Advancement.Builder.advancement().parent(basicWafer).display(GCBlocks.BASIC_SOLAR_PANEL, this.title("basic_solar_panel"), this.description("basic_solar_panel"), null, FrameType.TASK, true, true, false).withCriterion("basic_solar_panel", InventoryChangeTrigger.TriggerInstance.hasItem(GCBlocks.BASIC_SOLAR_PANEL)).register(consumer, this.modLoc("basic_solar_panel"));
        Advancement.Builder.advancement().parent(basicSolarPanel).display(GCBlocks.ADVANCED_SOLAR_PANEL, this.title("advanced_solar_panel"), this.description("advanced_solar_panel"), null, FrameType.TASK, true, true, false).withCriterion("advanced_solar_panel", InventoryChangeTrigger.TriggerInstance.hasItem(GCBlocks.ADVANCED_SOLAR_PANEL)).register(consumer, this.modLoc("advanced_solar_panel"));
        Advancement.Builder.advancement().parent(compressor).display(GCBlocks.ELECTRIC_COMPRESSOR, this.title("electric_compressor"), this.description("electric_compressor"), null, FrameType.TASK, true, true, false).withCriterion("electric_compressor", InventoryChangeTrigger.TriggerInstance.hasItem(GCBlocks.ELECTRIC_COMPRESSOR)).register(consumer, this.modLoc("electric_compressor"));
        Advancement rocketWorkbench = Advancement.Builder.advancement().parent(compressor).display(GCBlocks.ROCKET_WORKBENCH, this.title("rocket_workbench"), this.description("rocket_workbench"), null, FrameType.TASK, true, true, false).withCriterion("rocket_workbench", InventoryChangeTrigger.TriggerInstance.hasItem(GCBlocks.ROCKET_WORKBENCH)).register(consumer, this.modLoc("rocket_workbench"));
        Advancement tier1Rocket = Advancement.Builder.advancement().parent(rocketWorkbench).display(GCItems.TIER_1_ROCKET, this.title("tier_1_rocket"), this.description("tier_1_rocket"), null, FrameType.TASK, true, true, false).withRequirementsStrategy(RequirementsStrategy.OR).withCriterion("tier_1_rocket", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.TIER_1_ROCKET)).withCriterion("tier_1_rocket_18_inventory", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.TIER_1_ROCKET_18_INVENTORY)).withCriterion("tier_1_rocket_36_inventory", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.TIER_1_ROCKET_36_INVENTORY)).withCriterion("tier_1_rocket_54_inventory", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.TIER_1_ROCKET_54_INVENTORY)).register(consumer, this.modLoc("tier_1_rocket"));
        Advancement fuelLoader = Advancement.Builder.advancement().parent(tier1Rocket).display(GCBlocks.FUEL_LOADER, this.title("fuel_loader"), this.description("fuel_loader"), null, FrameType.TASK, true, true, false).withCriterion("fuel_loader", InventoryChangeTrigger.TriggerInstance.hasItem(GCBlocks.FUEL_LOADER)).register(consumer, this.modLoc("fuel_loader"));
        Advancement launchRocket = Advancement.Builder.advancement().parent(fuelLoader).display(Items.FLINT_AND_STEEL, this.title("launch_rocket"), this.description("launch_rocket"), null, FrameType.GOAL, true, true, false).withCriterion("launch_rocket", new GCTriggers.LaunchRocketInstance()).register(consumer, this.modLoc("launch_rocket"));
        Advancement toMoon = Advancement.Builder.advancement().parent(launchRocket).display(GCBlocks.MOON_TURF, this.title("moon"), this.description("moon"), null, FrameType.CHALLENGE, true, true, false).withCriterion("traveled_to_moon", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(GCDimensions.MOON_DIMENSION)).register(consumer, this.modLoc("moon"));
        Advancement.Builder.advancement().parent(toMoon).display(GCItems.FULL_SOLAR_MODULE, this.title("create_space_station"), this.description("create_space_station"), null, FrameType.GOAL, true, true, false).withCriterion("create_space_station", new GCTriggers.CreateSpaceStationInstance()).register(consumer, this.modLoc("create_space_station"));
        Advancement moonDungeon = Advancement.Builder.advancement().parent(toMoon).display(GCBlocks.MOON_DUNGEON_BRICKS, this.title("moon_dungeon"), this.description("moon_dungeon"), null, FrameType.GOAL, true, true, false).withCriterion("moon_dungeon", new GCTriggers.FindMoonBossInstance()).register(consumer, this.modLoc("moon_dungeon"));
        //Advancement spinThruster = Advancement.Builder.builder().withParent(createSpaceStation).withDisplay(Items.FLINT_AND_STEEL, title("spin_thruster"), description("spin_thruster"), null, FrameType.TASK, true, true, false).withCriterion("spin_thruster", InventoryChangeTrigger.Instance.forItems(GCBlocks.SPIN_THRUSTER)).register(consumer, this.modLoc("spin_thruster"));
        Advancement tier1DungeonKey = Advancement.Builder.advancement().parent(moonDungeon).display(GCItems.TIER_1_DUNGEON_KEY, this.title("dungeon_key_moon"), this.description("dungeon_key_moon"), null, FrameType.CHALLENGE, true, true, false).withCriterion("dungeon_key_moon", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.TIER_1_DUNGEON_KEY)).register(consumer, this.modLoc("dungeon_key_moon"));
        Advancement moonBuggySchematic = Advancement.Builder.advancement().parent(tier1DungeonKey).display(GCItems.MOON_BUGGY_SCHEMATIC, this.title("moon_buggy_schematic"), this.description("moon_buggy_schematic"), null, FrameType.TASK, true, true, false).withCriterion("moon_buggy_schematic", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.MOON_BUGGY_SCHEMATIC)).register(consumer, this.modLoc("moon_buggy_schematic"));
        Advancement.Builder.advancement().parent(moonBuggySchematic).display(GCItems.BUGGY, this.title("moon_buggy"), this.description("moon_buggy"), null, FrameType.GOAL, true, true, false).withRequirementsStrategy(RequirementsStrategy.OR).withCriterion("buggy", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.BUGGY)).withCriterion("buggy_18_inventory", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.BUGGY_18_INVENTORY)).withCriterion("buggy_36_inventory", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.BUGGY_36_INVENTORY)).withCriterion("buggy_54_inventory", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.BUGGY_54_INVENTORY)).register(consumer, this.modLoc("moon_buggy"));
        Advancement oxygenCollector = Advancement.Builder.advancement().parent(compressor).display(GCBlocks.OXYGEN_COLLECTOR, this.title("oxygen_collector"), this.description("oxygen_collector"), null, FrameType.TASK, true, true, false).withCriterion("oxygen_collector", InventoryChangeTrigger.TriggerInstance.hasItem(GCBlocks.OXYGEN_COLLECTOR)).register(consumer, this.modLoc("oxygen_collector"));
        Advancement oxygenCompressor = Advancement.Builder.advancement().parent(oxygenCollector).display(GCBlocks.OXYGEN_COMPRESSOR, this.title("oxygen_compressor"), this.description("oxygen_compressor"), null, FrameType.TASK, true, true, false).withCriterion("oxygen_compressor", InventoryChangeTrigger.TriggerInstance.hasItem(GCBlocks.OXYGEN_COMPRESSOR)).register(consumer, this.modLoc("oxygen_compressor"));
        Advancement fillOxygenTank = Advancement.Builder.advancement().parent(oxygenCompressor).display(GCItems.MEDIUM_OXYGEN_TANK, this.title("fill_tank"), this.description("fill_tank"), null, FrameType.TASK, true, true, false).withRequirementsStrategy(RequirementsStrategy.OR).withCriterion("light_oxygen_tank", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.LIGHT_OXYGEN_TANK)).withCriterion("medium_oxygen_tank", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.MEDIUM_OXYGEN_TANK)).withCriterion("heavy_oxygen_tank", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.HEAVY_OXYGEN_TANK)).register(consumer, this.modLoc("fill_tank"));
        Advancement.Builder.advancement().parent(fillOxygenTank).display(GCItems.HEAVY_OXYGEN_TANK, this.title("fill_all_tanks"), this.description("fill_all_tanks"), null, FrameType.GOAL, true, true, false).withRequirementsStrategy(RequirementsStrategy.OR).withCriterion("light_oxygen_tank", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.LIGHT_OXYGEN_TANK)).withCriterion("medium_oxygen_tank", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.MEDIUM_OXYGEN_TANK)).withCriterion("heavy_oxygen_tank", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.HEAVY_OXYGEN_TANK)).register(consumer, this.modLoc("fill_all_tanks"));
        Advancement.Builder.advancement().parent(fillOxygenTank).display(GCItems.OXYGEN_MASK, this.title("all_oxygen_gear"), this.description("all_oxygen_gear"), null, FrameType.GOAL, true, true, false)
        .withCriterion("light_oxygen_tank", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.LIGHT_OXYGEN_TANK, GCItems.MEDIUM_OXYGEN_TANK, GCItems.HEAVY_OXYGEN_TANK))
        .withCriterion("oxygen_mask", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.OXYGEN_MASK))
        .withCriterion("oxygen_gear", InventoryChangeTrigger.TriggerInstance.hasItem(GCItems.OXYGEN_GEAR)).register(consumer, this.modLoc("all_oxygen_gear"));
        Advancement oil = Advancement.Builder.advancement().parent(compressor).display(GCFluids.OIL.getBucket(), this.title("oil"), this.description("oil"), null, FrameType.TASK, true, true, false).withCriterion("oil", EnterBlockTrigger.TriggerInstance.entersBlock(GCFluids.OIL.getBlock())).register(consumer, this.modLoc("oil"));
        Advancement refinery = Advancement.Builder.advancement().parent(oil).display(GCBlocks.REFINERY, this.title("refinery"), this.description("refinery"), null, FrameType.TASK, true, true, false).withCriterion("refinery", InventoryChangeTrigger.TriggerInstance.hasItem(GCBlocks.REFINERY)).register(consumer, this.modLoc("refinery"));
        Advancement.Builder.advancement().parent(refinery).display(GCFluids.FUEL.getBucket(), this.title("rocket_fuel"), this.description("rocket_fuel"), null, FrameType.GOAL, true, true, false).withCriterion("fuel_bucket", InventoryChangeTrigger.TriggerInstance.hasItem(GCFluids.FUEL.getBucket())).register(consumer, this.modLoc("rocket_fuel"));
    }

    private TranslatableComponent title(String name)
    {
        return new TranslatableComponent("advancement.galacticraft." + name + ".title");
    }

    private TranslatableComponent description(String name)
    {
        return new TranslatableComponent("advancement.galacticraft." + name + ".description");
    }

    private String modLoc(String name)
    {
        return new ResourceLocation(Constants.MOD_ID_CORE, "galacticraft/" + name).toString();
    }
}