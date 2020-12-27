package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.items.*;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.wrappers.PartialCanister;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.*;

import static micdoodle8.mods.galacticraft.core.GCBlocks.register;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_CORE)
public class GCItems
{
    @ObjectHolder(GCItemNames.LIGHT_OXYGEN_TANK)
    public static Item LIGHT_OXYGEN_TANK;
    @ObjectHolder(GCItemNames.MEDIUM_OXYGEN_TANK)
    public static Item MEDIUM_OXYGEN_TANK;
    @ObjectHolder(GCItemNames.HEAVY_OXYGEN_TANK)
    public static Item HEAVY_OXYGEN_TANK;
    @ObjectHolder(GCItemNames.OXYGEN_MASK)
    public static Item OXYGEN_MASK;
    @ObjectHolder(GCItemNames.TIER_1_ROCKET)
    public static Item TIER_1_ROCKET;
    @ObjectHolder(GCItemNames.TIER_1_ROCKET_18_INVENTORY)
    public static Item TIER_1_ROCKET_18_INVENTORY;
    @ObjectHolder(GCItemNames.TIER_1_ROCKET_36_INVENTORY)
    public static Item TIER_1_ROCKET_36_INVENTORY;
    @ObjectHolder(GCItemNames.TIER_1_ROCKET_54_INVENTORY)
    public static Item TIER_1_ROCKET_54_INVENTORY;
    @ObjectHolder(GCItemNames.CREATIVE_TIER_1_ROCKET)
    public static Item CREATIVE_TIER_1_ROCKET;
    @ObjectHolder(GCItemNames.SENSOR_GLASSES)
    public static Item SENSOR_GLASSES;
    @ObjectHolder(GCItemNames.SENSOR_LENS)
    public static Item SENSOR_LENS;
    @ObjectHolder(GCItemNames.HEAVY_DUTY_PICKAXE)
    public static Item HEAVY_DUTY_PICKAXE;
    @ObjectHolder(GCItemNames.HEAVY_DUTY_AXE)
    public static Item HEAVY_DUTY_AXE;
    @ObjectHolder(GCItemNames.HEAVY_DUTY_HOE)
    public static Item HEAVY_DUTY_HOE;
    @ObjectHolder(GCItemNames.HEAVY_DUTY_SHOVEL)
    public static Item HEAVY_DUTY_SHOVEL;
    @ObjectHolder(GCItemNames.HEAVY_DUTY_SWORD)
    public static Item HEAVY_DUTY_SWORD;
    @ObjectHolder(GCItemNames.HEAVY_DUTY_HELMET)
    public static Item HEAVY_DUTY_HELMET;
    @ObjectHolder(GCItemNames.HEAVY_DUTY_CHESTPLATE)
    public static Item HEAVY_DUTY_CHESTPLATE;
    @ObjectHolder(GCItemNames.HEAVY_DUTY_LEGGINGS)
    public static Item HEAVY_DUTY_LEGGINGS;
    @ObjectHolder(GCItemNames.HEAVY_DUTY_BOOTS)
    public static Item HEAVY_DUTY_BOOTS;
    @ObjectHolder(GCItemNames.OXYGEN_VENT)
    public static Item OXYGEN_VENT;
    @ObjectHolder(GCItemNames.OXYGEN_FAN)
    public static Item OXYGEN_FAN;
    @ObjectHolder(GCItemNames.OXYGEN_CONCENTRATOR)
    public static Item OXYGEN_CONCENTRATOR;
    @ObjectHolder(GCItemNames.TIER_1_HEAVY_DUTY_PLATE)
    public static Item TIER_1_HEAVY_DUTY_PLATE;
    @ObjectHolder(GCItemNames.NOSE_CONE)
    public static Item NOSE_CONE;
    @ObjectHolder(GCItemNames.ROCKET_FINS)
    public static Item ROCKET_FINS;
    @ObjectHolder(GCItemNames.BUGGY)
    public static Item BUGGY;
    @ObjectHolder(GCItemNames.BUGGY_18_INVENTORY)
    public static Item BUGGY_18_INVENTORY;
    @ObjectHolder(GCItemNames.BUGGY_36_INVENTORY)
    public static Item BUGGY_36_INVENTORY;
    @ObjectHolder(GCItemNames.BUGGY_54_INVENTORY)
    public static Item BUGGY_54_INVENTORY;
    @ObjectHolder(GCItemNames.FLAG)
    public static Item FLAG;
    @ObjectHolder(GCItemNames.OXYGEN_GEAR)
    public static Item OXYGEN_GEAR;
    @ObjectHolder(GCItemNames.CANVAS)
    public static Item CANVAS;
    @ObjectHolder(GCItemNames.STEEL_POLE)
    public static Item STEEL_POLE;
    @ObjectHolder(GCItemNames.PARTIAL_OIL_CANISTER)
    public static Item PARTIAL_OIL_CANISTER;
    @ObjectHolder(GCItemNames.PARTIAL_FUEL_CANISTER)
    public static Item PARTIAL_FUEL_CANISTER;
    @ObjectHolder(GCItemNames.INFINITE_OXYGEN_TANK)
    public static Item INFINITE_OXYGEN_TANK;
    @ObjectHolder(GCItemNames.MOON_BUGGY_SCHEMATIC)
    public static Item MOON_BUGGY_SCHEMATIC;
    @ObjectHolder(GCItemNames.TIER_2_ROCKET_SCHEMATIC)
    public static Item TIER_2_ROCKET_SCHEMATIC;
    @ObjectHolder(GCItemNames.TIER_1_DUNGEON_KEY)
    public static Item TIER_1_DUNGEON_KEY;
    @ObjectHolder(GCItemNames.BATTERY)
    public static Item BATTERY;
    @ObjectHolder(GCItemNames.INFINITE_BATTERY)
    public static Item INFINITE_BATTERY;
    @ObjectHolder(GCItemNames.STANDARD_WRENCH)
    public static Item STANDARD_WRENCH;
    @ObjectHolder(GCItemNames.CHEESE_CURD)
    public static Item CHEESE_CURD;
    @ObjectHolder(GCItemNames.RAW_METEORIC_IRON)
    public static Item RAW_METEORIC_IRON;
    @ObjectHolder(GCItemNames.PRELAUNCH_CHECKLIST)
    public static Item PRELAUNCH_CHECKLIST;
    @ObjectHolder(GCItemNames.DUNGEON_FINDER)
    public static Item DUNGEON_FINDER;
    //    @ObjectHolder(ItemNames.ic2compat) public static Item ic2compat;
    @ObjectHolder(GCItemNames.SPACE_EMERGENCY_KIT)
    public static Item SPACE_EMERGENCY_KIT;
    @ObjectHolder(GCItemNames.SINGLE_SOLAR_MODULE)
    public static Item SINGLE_SOLAR_MODULE;
    @ObjectHolder(GCItemNames.FULL_SOLAR_MODULE)
    public static Item FULL_SOLAR_MODULE;
    @ObjectHolder(GCItemNames.RAW_SILICON)
    public static Item RAW_SILICON;
    @ObjectHolder(GCItemNames.COPPER_INGOT)
    public static Item COPPER_INGOT;
    @ObjectHolder(GCItemNames.TIN_INGOT)
    public static Item TIN_INGOT;
    @ObjectHolder(GCItemNames.ALUMINUM_INGOT)
    public static Item ALUMINUM_INGOT;
    @ObjectHolder(GCItemNames.COMPRESSED_COPPER)
    public static Item COMPRESSED_COPPER;
    @ObjectHolder(GCItemNames.COMPRESSED_TIN)
    public static Item COMPRESSED_TIN;
    @ObjectHolder(GCItemNames.COMPRESSED_ALUMINUM)
    public static Item COMPRESSED_ALUMINUM;
    @ObjectHolder(GCItemNames.COMPRESSED_STEEL)
    public static Item COMPRESSED_STEEL;
    @ObjectHolder(GCItemNames.COMPRESSED_BRONZE)
    public static Item COMPRESSED_BRONZE;
    @ObjectHolder(GCItemNames.COMPRESSED_IRON)
    public static Item COMPRESSED_IRON;
    @ObjectHolder(GCItemNames.SOLAR_WAFER)
    public static Item SOLAR_WAFER;
    @ObjectHolder(GCItemNames.BASIC_WAFER)
    public static Item BASIC_WAFER;
    @ObjectHolder(GCItemNames.ADVANCED_WAFER)
    public static Item ADVANCED_WAFER;
    @ObjectHolder(GCItemNames.FREQUENCY_MODULE)
    public static Item FREQUENCY_MODULE;
    @ObjectHolder(GCItemNames.AMBIENT_THERMAL_CONTROLLER)
    public static Item AMBIENT_THERMAL_CONTROLLER;
    @ObjectHolder(GCItemNames.BUGGY_WHEEL)
    public static Item BUGGY_WHEEL;
    @ObjectHolder(GCItemNames.BUGGY_SEAT)
    public static Item BUGGY_SEAT;
    @ObjectHolder(GCItemNames.BUGGY_STORAGE_BOX)
    public static Item BUGGY_STORAGE_BOX;
    @ObjectHolder(GCItemNames.TIN_CANISTER)
    public static Item TIN_CANISTER;
    @ObjectHolder(GCItemNames.COPPER_CANISTER)
    public static Item COPPER_CANISTER;
    @ObjectHolder(GCItemNames.DEHYDRATED_APPLE_CAN)
    public static Item DEHYDRATED_APPLE_CAN;
    @ObjectHolder(GCItemNames.DEHYDRATED_CARROT_CAN)
    public static Item DEHYDRATED_CARROT_CAN;
    @ObjectHolder(GCItemNames.DEHYDRATED_MELON_CAN)
    public static Item DEHYDRATED_MELON_CAN;
    @ObjectHolder(GCItemNames.DEHYDRATED_POTATO_CAN)
    public static Item DEHYDRATED_POTATO_CAN;
    @ObjectHolder(GCItemNames.CHEESE_SLICE)
    public static Item CHEESE_SLICE;
    @ObjectHolder(GCItemNames.BURGER_BUN)
    public static Item BURGER_BUN;
    @ObjectHolder(GCItemNames.GROUND_BEEF)
    public static Item GROUND_BEEF;
    @ObjectHolder(GCItemNames.COOKED_BEEF_PATTY)
    public static Item COOKED_BEEF_PATTY;
    @ObjectHolder(GCItemNames.CHEESEBURGER)
    public static Item CHEESEBURGER;
    @ObjectHolder(GCItemNames.CANNED_BEEF)
    public static Item CANNED_BEEF;
    @ObjectHolder(GCItemNames.METEOR_CHUNK)
    public static Item METEOR_CHUNK;
    @ObjectHolder(GCItemNames.HOT_METEOR_CHUNK)
    public static Item HOT_METEOR_CHUNK;
    @ObjectHolder(GCItemNames.METEORIC_IRON_INGOT)
    public static Item METEORIC_IRON_INGOT;
    @ObjectHolder(GCItemNames.COMPRESSED_METEORIC_IRON)
    public static Item COMPRESSED_METEORIC_IRON;
    @ObjectHolder(GCItemNames.LUNAR_SAPPHIRE)
    public static Item LUNAR_SAPPHIRE;
    @ObjectHolder(GCItemNames.WHITE_PARACHUTE)
    public static Item WHITE_PARACHUTE;
    @ObjectHolder(GCItemNames.ORANGE_PARACHUTE)
    public static Item ORANGE_PARACHUTE;
    @ObjectHolder(GCItemNames.MAGENTA_PARACHUTE)
    public static Item MAGENTA_PARACHUTE;
    @ObjectHolder(GCItemNames.LIGHT_BLUE_PARACHUTE)
    public static Item LIGHT_BLUE_PARACHUTE;
    @ObjectHolder(GCItemNames.YELLOW_PARACHUTE)
    public static Item YELLOW_PARACHUTE;
    @ObjectHolder(GCItemNames.LIME_PARACHUTE)
    public static Item LIME_PARACHUTE;
    @ObjectHolder(GCItemNames.PINK_PARACHUTE)
    public static Item PINK_PARACHUTE;
    @ObjectHolder(GCItemNames.GRAY_PARACHUTE)
    public static Item GRAY_PARACHUTE;
    @ObjectHolder(GCItemNames.LIGHT_GRAY_PARACHUTE)
    public static Item LIGHT_GRAY_PARACHUTE;
    @ObjectHolder(GCItemNames.CYAN_PARACHUTE)
    public static Item CYAN_PARACHUTE;
    @ObjectHolder(GCItemNames.PURPLE_PARACHUTE)
    public static Item PURPLE_PARACHUTE;
    @ObjectHolder(GCItemNames.BLUE_PARACHUTE)
    public static Item BLUE_PARACHUTE;
    @ObjectHolder(GCItemNames.BROWN_PARACHUTE)
    public static Item BROWN_PARACHUTE;
    @ObjectHolder(GCItemNames.GREEN_PARACHUTE)
    public static Item GREEN_PARACHUTE;
    @ObjectHolder(GCItemNames.RED_PARACHUTE)
    public static Item RED_PARACHUTE;
    @ObjectHolder(GCItemNames.BLACK_PARACHUTE)
    public static Item BLACK_PARACHUTE;
    @ObjectHolder(GCItemNames.TIER_1_ROCKET_ENGINE)
    public static Item TIER_1_ROCKET_ENGINE;
    @ObjectHolder(GCItemNames.TIER_1_BOOSTER)
    public static Item TIER_1_BOOSTER;

//    public static ArmorMaterial ARMOR_SENSOR_GLASSES = EnumHelper.addArmorMaterial("SENSORGLASSES", "", 200, new int[] { 0, 0, 0, 0 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
//    public static ArmorMaterial ARMOR_STEEL = EnumHelper.addArmorMaterial("steel", "", 30, new int[] { 3, 6, 8, 3 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
//    public static ToolMaterial TOOL_STEEL = EnumHelper.addToolMaterial("steel", 3, 768, 5.0F, 2, 8);

    public static ArrayList<Item> hiddenItems = new ArrayList<Item>();
    public static LinkedList<ItemCanisterGeneric> canisterTypes = new LinkedList<ItemCanisterGeneric>();
    public static HashMap<ItemStack, ItemStack> itemChanges = new HashMap<>(4, 1.0F);

    public static Item.Properties defaultBuilder()
    {
        return new Item.Properties().group(GalacticraftCore.galacticraftItemsTab);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        register(r, new ItemOxygenTank(defaultBuilder().maxDamage(900)), GCItemNames.LIGHT_OXYGEN_TANK);
        register(r, new ItemOxygenTank(defaultBuilder().maxDamage(1800)), GCItemNames.MEDIUM_OXYGEN_TANK);
        register(r, new ItemOxygenTank(defaultBuilder().maxDamage(2700)), GCItemNames.HEAVY_OXYGEN_TANK);
        register(r, new ItemOxygenMask(defaultBuilder().maxStackSize(1)), GCItemNames.OXYGEN_MASK);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.TIER_1_ROCKET);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.TIER_1_ROCKET_18_INVENTORY);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.TIER_1_ROCKET_36_INVENTORY);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.TIER_1_ROCKET_54_INVENTORY);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.CREATIVE_TIER_1_ROCKET);
        register(r, new ItemSensorGlasses(defaultBuilder()), GCItemNames.SENSOR_GLASSES);
        register(r, new ItemPickaxeGC(defaultBuilder()), GCItemNames.HEAVY_DUTY_PICKAXE);
        register(r, new ItemAxeGC(defaultBuilder()), GCItemNames.HEAVY_DUTY_AXE);
        register(r, new ItemHoeGC(defaultBuilder()), GCItemNames.HEAVY_DUTY_HOE);
        register(r, new ItemShovelGC(defaultBuilder()), GCItemNames.HEAVY_DUTY_SHOVEL);
        register(r, new ItemSwordGC(defaultBuilder()), GCItemNames.HEAVY_DUTY_SWORD);
        register(r, new ArmorItemGC(EquipmentSlotType.HEAD, defaultBuilder()), GCItemNames.HEAVY_DUTY_HELMET);
        register(r, new ArmorItemGC(EquipmentSlotType.CHEST, defaultBuilder()), GCItemNames.HEAVY_DUTY_CHESTPLATE);
        register(r, new ArmorItemGC(EquipmentSlotType.LEGS, defaultBuilder()), GCItemNames.HEAVY_DUTY_LEGGINGS);
        register(r, new ArmorItemGC(EquipmentSlotType.FEET, defaultBuilder()), GCItemNames.HEAVY_DUTY_BOOTS);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.OXYGEN_VENT);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.OXYGEN_FAN);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.OXYGEN_CONCENTRATOR);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.TIER_1_HEAVY_DUTY_PLATE);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.TIER_1_ROCKET_ENGINE);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.TIER_1_BOOSTER);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.ROCKET_FINS);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.NOSE_CONE);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.SENSOR_LENS);
        register(r, new ItemBuggy(defaultBuilder().maxStackSize(1)), GCItemNames.BUGGY);
        register(r, new ItemBuggy(defaultBuilder().maxStackSize(1)), GCItemNames.BUGGY_18_INVENTORY);
        register(r, new ItemBuggy(defaultBuilder().maxStackSize(1)), GCItemNames.BUGGY_36_INVENTORY);
        register(r, new ItemBuggy(defaultBuilder().maxStackSize(1)), GCItemNames.BUGGY_54_INVENTORY);
        register(r, new ItemFlag(defaultBuilder().maxDamage(0)), GCItemNames.FLAG);
        register(r, new ItemOxygenGear(defaultBuilder()), GCItemNames.OXYGEN_GEAR);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.CANVAS);
        register(r, new ItemOilCanister(defaultBuilder().maxDamage(ItemCanisterGeneric.EMPTY_CAPACITY)), GCItemNames.PARTIAL_OIL_CANISTER);
        register(r, new ItemFuelCanister(defaultBuilder().maxDamage(ItemCanisterGeneric.EMPTY_CAPACITY)), GCItemNames.PARTIAL_FUEL_CANISTER);
        register(r, new ItemCanisterOxygenInfinite(defaultBuilder()), GCItemNames.INFINITE_OXYGEN_TANK);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.STEEL_POLE);
        register(r, new ItemSchematic(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.MOON_BUGGY_SCHEMATIC);
        register(r, new ItemSchematic(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.TIER_2_ROCKET_SCHEMATIC);
        register(r, new ItemKey(defaultBuilder()), GCItemNames.TIER_1_DUNGEON_KEY);
        register(r, new ItemBattery(defaultBuilder().maxDamage(ItemElectricBase.DAMAGE_RANGE)), GCItemNames.BATTERY);
        register(r, new ItemBatteryInfinite(defaultBuilder()), GCItemNames.INFINITE_BATTERY);
        register(r, new ItemMeteorChunk(defaultBuilder().maxStackSize(16)), GCItemNames.METEOR_CHUNK);
        register(r, new ItemMeteorChunk(defaultBuilder().maxStackSize(16)), GCItemNames.HOT_METEOR_CHUNK);
        register(r, new ItemUniversalWrench(defaultBuilder().maxDamage(256)), GCItemNames.STANDARD_WRENCH);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(1).saturation(0.1F).fastToEat().build())), GCItemNames.CHEESE_CURD);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.RAW_METEORIC_IRON);
        register(r, new ItemPreLaunchChecklist(defaultBuilder()), GCItemNames.PRELAUNCH_CHECKLIST);
        register(r, new ItemDungeonFinder(defaultBuilder()), GCItemNames.DUNGEON_FINDER);
//        register(r, new ItemIC2Compat(defaultBuilder()), ItemNames.ic2compat); TODO
        register(r, new ItemEmergencyKit(defaultBuilder().maxDamage(0)), GCItemNames.SPACE_EMERGENCY_KIT);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.SINGLE_SOLAR_MODULE);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.FULL_SOLAR_MODULE);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.RAW_SILICON);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.COPPER_INGOT);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.TIN_INGOT);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.ALUMINUM_INGOT);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.COMPRESSED_COPPER);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.COMPRESSED_TIN);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.COMPRESSED_ALUMINUM);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.COMPRESSED_STEEL);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.COMPRESSED_BRONZE);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.COMPRESSED_IRON);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.SOLAR_WAFER);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.BASIC_WAFER);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.ADVANCED_WAFER);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.FREQUENCY_MODULE);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.AMBIENT_THERMAL_CONTROLLER);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.BUGGY_WHEEL);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.BUGGY_SEAT);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.BUGGY_STORAGE_BOX);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.TIN_CANISTER);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.COPPER_CANISTER);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(8).saturation(0.3F).build())), GCItemNames.DEHYDRATED_APPLE_CAN);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(8).saturation(0.6F).build())), GCItemNames.DEHYDRATED_CARROT_CAN);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(4).saturation(0.3F).build())), GCItemNames.DEHYDRATED_MELON_CAN);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(2).saturation(0.3F).build())), GCItemNames.DEHYDRATED_POTATO_CAN);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(2).saturation(0.1F).build())), GCItemNames.CHEESE_SLICE);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(4).saturation(0.8F).build())), GCItemNames.BURGER_BUN);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(2).saturation(0.3F).build())), GCItemNames.GROUND_BEEF);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(4).saturation(0.6F).build())), GCItemNames.COOKED_BEEF_PATTY);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(14).saturation(1.0F).build())), GCItemNames.CHEESEBURGER);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(8).saturation(0.6F).build())), GCItemNames.CANNED_BEEF);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.METEORIC_IRON_INGOT);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.COMPRESSED_METEORIC_IRON);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.LUNAR_SAPPHIRE);
        Item.Properties parachuteProps = defaultBuilder().maxDamage(0).maxStackSize(1);
        register(r, new ItemParaChute(DyeColor.WHITE, parachuteProps), GCItemNames.WHITE_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.BLACK, parachuteProps), GCItemNames.BLACK_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.LIGHT_BLUE, parachuteProps), GCItemNames.LIGHT_BLUE_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.LIME, parachuteProps), GCItemNames.LIME_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.BROWN, parachuteProps), GCItemNames.BROWN_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.BLUE, parachuteProps), GCItemNames.BLUE_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.GRAY, parachuteProps), GCItemNames.GRAY_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.GREEN, parachuteProps), GCItemNames.GREEN_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.LIGHT_GRAY, parachuteProps), GCItemNames.LIGHT_GRAY_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.MAGENTA, parachuteProps), GCItemNames.MAGENTA_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.ORANGE, parachuteProps), GCItemNames.ORANGE_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.PINK, parachuteProps), GCItemNames.PINK_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.PURPLE, parachuteProps), GCItemNames.PURPLE_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.RED, parachuteProps), GCItemNames.RED_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.CYAN, parachuteProps), GCItemNames.CYAN_PARACHUTE);
        register(r, new ItemParaChute(DyeColor.YELLOW, parachuteProps), GCItemNames.YELLOW_PARACHUTE);

//        GCItems.registerHarvestLevels();
//
//        GCItems.registerItems();
//        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 15), new ItemStack(GCItems.foodItem, 1, 0));
//        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 16), new ItemStack(GCItems.foodItem, 1, 1));
//        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 17), new ItemStack(GCItems.foodItem, 1, 2));
//        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 18), new ItemStack(GCItems.foodItem, 1, 3));

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
        {
            ClientProxyCore.registerCanister(new PartialCanister(GCItems.PARTIAL_OIL_CANISTER, Constants.MOD_ID_CORE, GCItemNames.PARTIAL_OIL_CANISTER, 7));
            ClientProxyCore.registerCanister(new PartialCanister(GCItems.PARTIAL_FUEL_CANISTER, Constants.MOD_ID_CORE, GCItemNames.PARTIAL_FUEL_CANISTER, 7));
        });

        GCItems.canisterTypes.add((ItemCanisterGeneric) GCItems.PARTIAL_FUEL_CANISTER);
        GCItems.canisterTypes.add((ItemCanisterGeneric) GCItems.PARTIAL_OIL_CANISTER);
    }

//    public static void oreDictRegistrations()
//    {
//        for (int i = 0; i < ItemBasic.names.length; i++)
//        {
//            if (ItemBasic.names[i].contains("ingot") || ItemBasic.names[i].contains("compressed") || ItemBasic.names[i].contains("wafer"))
//            {
//                String name = ItemBasic.names[i];
//                while (name.contains("_"))
//                {
//                    int loc = name.indexOf("_");
//                    name = name.substring(0, loc) + name.substring(loc + 1, loc + 2).toUpperCase() + name.substring(loc + 2, name.length());
//                }
//                OreDictionary.registerOre(name, new ItemStack(GCItems.basicItem, 1, i));
//            }
//        }
//
//        OreDictionary.registerOre("foodCheese", new ItemStack(GCItems.cheeseCurd, 1, 0));
//        OreDictionary.registerOre("compressedMeteoricIron", new ItemStack(GCItems.itemBasicMoon, 1, 1));
//        OreDictionary.registerOre("ingotMeteoricIron", new ItemStack(GCItems.itemBasicMoon, 1, 0));
//        if (CompatibilityManager.useAluDust())
//        {
//            OreDictionary.registerOre("dustAluminum", new ItemStack(GCItems.ic2compat, 1, 0));
//            OreDictionary.registerOre("dustAluminium", new ItemStack(GCItems.ic2compat, 1, 0));
//        }
//        if (CompatibilityManager.isIc2Loaded())
//        {
//            OreDictionary.registerOre("crushedAluminum", new ItemStack(GCItems.ic2compat, 1, 2));
//            OreDictionary.registerOre("crushedPurifiedAluminum", new ItemStack(GCItems.ic2compat, 1, 1));
//            OreDictionary.registerOre("dustTinyTitanium", new ItemStack(GCItems.ic2compat, 1, 7));
//        }
//
//        GalacticraftCore.proxy.registerCanister(new PartialCanister(GCItems.oilCanister, Constants.MOD_ID_CORE, "oil_canister_partial", 7));
//        GalacticraftCore.proxy.registerCanister(new PartialCanister(GCItems.fuelCanister, Constants.MOD_ID_CORE, "fuel_canister_partial", 7));
//        OreDictionary.registerOre(ConfigManagerCore.otherModsSilicon.get(), new ItemStack(GCItems.basicItem, 1, 2));
//    }


//    /**
//     * Do not call this until after mod loading is complete
//     * because JEI doesn't have an internal item blacklist
//     * until it services an FMLLoadCompleteEvent.
//     * (Seriously?!)
//     */
//    public static void hideItemsJEI(IIngredientBlacklist jeiHidden)
//    {
//        if (jeiHidden != null)
//        {
//            for (ItemStack item : GCItems.itemChanges.keySet())
//            {
//                jeiHidden.addIngredientToBlacklist(item.copy());
//            }
//
//            for (Item item : GCItems.hiddenItems)
//            {
//                jeiHidden.addIngredientToBlacklist(new ItemStack(item, 1, 0));
//            }
//
//            for (Block block : GCBlocks.hiddenBlocks)
//            {
//                jeiHidden.addIngredientToBlacklist(new ItemStack(block, 1, 0));
//                if (block == GCBlocks.slabGCDouble)
//                {
//                    for (int j = 1; j < (GalacticraftCore.isPlanetsLoaded ? 7 : 4); j++)
//                        jeiHidden.addIngredientToBlacklist(new ItemStack(block, 1, j));
//                }
//            }
//        }
//    }

//
//    public static void registerHarvestLevels()
//    {
//        GCItems.steelPickaxe.setHarvestLevel("pickaxe", 4);
//        GCItems.steelAxe.setHarvestLevel("axe", 4);
//        GCItems.steelSpade.setHarvestLevel("shovel", 4);
//    } TODO

//    public static void registerItems()
//    {
//        GCItems.registerItem(GCItems.rocketTier1);
//        GCItems.registerItem(GCItems.oxMask);
//        GCItems.registerItem(GCItems.oxygenGear);
//        GCItems.registerItem(GCItems.oxTankLight);
//        GCItems.registerItem(GCItems.oxTankMedium);
//        GCItems.registerItem(GCItems.oxTankHeavy);
//        GCItems.registerItem(GCItems.oxygenCanisterInfinite);
//        GCItems.registerItem(GCItems.sensorLens);
//        GCItems.registerItem(GCItems.sensorGlasses);
//        GCItems.registerItem(GCItems.wrench);
//        GCItems.registerItem(GCItems.steelPickaxe);
//        GCItems.registerItem(GCItems.steelAxe);
//        GCItems.registerItem(GCItems.steelHoe);
//        GCItems.registerItem(GCItems.steelSpade);
//        GCItems.registerItem(GCItems.steelSword);
//        GCItems.registerItem(GCItems.steelHelmet);
//        GCItems.registerItem(GCItems.steelChestplate);
//        GCItems.registerItem(GCItems.steelLeggings);
//        GCItems.registerItem(GCItems.steelBoots);
//        GCItems.registerItem(GCItems.oxygenVent);
//        GCItems.registerItem(GCItems.oxygenFan);
//        GCItems.registerItem(GCItems.oxygenConcentrator);
//        GCItems.registerItem(GCItems.rocketEngine);
//        GCItems.registerItem(GCItems.heavyPlatingTier1);
//        GCItems.registerItem(GCItems.partNoseCone);
//        GCItems.registerItem(GCItems.partFins);
//        GCItems.registerItem(GCItems.flagPole);
//        GCItems.registerItem(GCItems.canvas);
//        GCItems.registerItem(GCItems.oilCanister);
//        GCItems.registerItem(GCItems.fuelCanister);
//        GCItems.registerItem(GCItems.schematic);
//        GCItems.registerItem(GCItems.key);
//        GCItems.registerItem(GCItems.buggy);
//        GCItems.registerItem(GCItems.foodItem);
//        GCItems.registerItem(GCItems.battery);
//        GCItems.registerItem(GCItems.infiniteBatery);
//        GCItems.registerItem(GCItems.meteorChunk);
//        GCItems.registerItem(GCItems.cheeseCurd);
//        GCItems.registerItem(GCItems.meteoricIronRaw);
////		GCItems.registerItem(GCItems.cheeseBlock);
//        GCItems.registerItem(GCItems.flag);
//        GCItems.registerItem(GCItems.prelaunchChecklist);
//        GCItems.registerItem(GCItems.dungeonFinder);
//        GCItems.registerItem(GCItems.emergencyKit);
//
//        GCItems.canisterTypes.add((ItemCanisterGeneric) GCItems.fuelCanister);
//        GCItems.canisterTypes.add((ItemCanisterGeneric) GCItems.oilCanister);
//
//        if (CompatibilityManager.useAluDust()) GCItems.registerItem(GCItems.ic2compat);
//    }

//    public static void registerItem(Item item)
//    {
//        String name = item.getUnlocalizedName().substring(5);
//        if (item.getRegistryName() == null)
//        {
//            item.setRegistryName(name);
//        }
//        GCCoreUtil.registerGalacticraftItem(name, item);
//        GalacticraftCore.itemListTrue.add(item);
//        GalacticraftCore.proxy.postRegisterItem(item);
//    }
//
//    public static void registerItems(IForgeRegistry<Item> registry)
//    {
//        for (ItemStack item : GalacticraftCore.itemList)
//        {
//            registry.register(item.getItem());
//        }
//    }
}
