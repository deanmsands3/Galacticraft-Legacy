package team.galacticraft.galacticraft.common.core;

import net.fabricmc.api.EnvType;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;
import team.galacticraft.galacticraft.common.compat.registry.IRegistryWrapper;
import team.galacticraft.galacticraft.common.core.energy.item.ItemElectricBase;
import team.galacticraft.galacticraft.common.core.items.*;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.wrappers.PartialCanister;
import net.minecraft.item.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

import static team.galacticraft.galacticraft.common.core.GCBlocks.register;

//@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GCItems
{
    public static Item oxTankLight;
    public static Item oxTankMedium;
    public static Item oxTankHeavy;
    public static Item oxMask;
    public static Item rocketTierOne;
    public static Item rocketTierOneCargo1;
    public static Item rocketTierOneCargo2;
    public static Item rocketTierOneCargo3;
    public static Item rocketTierOneCreative;
    public static Item sensorGlasses;
    public static Item sensorLens;
    public static Item steelPickaxe;
    public static Item steelAxe;
    public static Item steelHoe;
    public static Item steelSpade;
    public static Item steelSword;
    public static Item steelHelmet;
    public static Item steelChestplate;
    public static Item steelLeggings;
    public static Item steelBoots;
    public static Item oxygenVent;
    public static Item oxygenFan;
    public static Item oxygenConcentrator;
    //    @ObjectHolder(ItemNames.rocketEngine) public static Item rocketEngine;
    public static Item heavyPlatingTier1;
    public static Item partNoseCone;
    public static Item partFins;
    public static Item buggy;
    public static Item buggyInventory1;
    public static Item buggyInventory2;
    public static Item buggyInventory3;
    public static Item flag;
    public static Item oxygenGear;
    public static Item canvas;
    public static Item flagPole;
    public static Item oilCanister;
    public static Item fuelCanister;
    public static Item oxygenCanisterInfinite;
    public static Item schematicBuggy;
    public static Item schematicRocketT2;
    public static Item key;
    //    @ObjectHolder(ItemNames.foodItem) public static Item foodItem;
    public static Item battery;
    public static Item infiniteBatery;
    public static Item wrench;
    public static Item cheeseCurd;
    public static Item meteoricIronRaw;
    public static Item cheeseBlock;
    public static Item prelaunchChecklist;
    public static Item dungeonFinder;
    //    @ObjectHolder(ItemNames.ic2compat) public static Item ic2compat;
    public static Item emergencyKit;
    public static Item solarModule0;
    public static Item solarModule1;
    public static Item rawSilicon;
    public static Item ingotCopper;
    public static Item ingotTin;
    public static Item ingotAluminum;
    public static Item compressedCopper;
    public static Item compressedTin;
    public static Item compressedAluminum;
    public static Item compressedSteel;
    public static Item compressedBronze;
    public static Item compressedIron;
    public static Item compressedWaferSolar;
    public static Item compressedWaferBasic;
    public static Item compressedWaferAdvanced;
    public static Item frequencyModule;
    public static Item ambientThermalController;
    public static Item buggyMaterialWheel;
    public static Item buggyMaterialSeat;
    public static Item buggyMaterialStorage;
    public static Item canisterTin;
    public static Item canisterCopper;
    public static Item dehydratedApple;
    public static Item dehydratedCarrot;
    public static Item dehydratedMelon;
    public static Item dehydratedPotato;
    public static Item cheeseSlice;
    public static Item burgerBun;
    public static Item beefPattyRaw;
    public static Item beefPattyCooked;
    public static Item cheeseburger;
    public static Item cannedBeef;
    public static Item meteorChunk;
    public static Item meteorChunkHot;
    public static Item ingotMeteoricIron;
    public static Item compressedMeteoricIron;
    public static Item lunarSapphire;
    public static Item parachutePlain;
    public static Item parachuteBlack;
    public static Item parachuteBlue;
    public static Item parachuteLime;
    public static Item parachuteBrown;
    public static Item parachuteDarkBlue;
    public static Item parachuteDarkGray;
    public static Item parachuteDarkGreen;
    public static Item parachuteGray;
    public static Item parachuteMagenta;
    public static Item parachuteOrange;
    public static Item parachutePink;
    public static Item parachutePurple;
    public static Item parachuteRed;
    public static Item parachuteTeal;
    public static Item parachuteYellow;
    public static Item rocketEngineT1;
    public static Item rocketBoosterT1;

//    public static ArmorMaterial ARMOR_SENSOR_GLASSES = EnumHelper.addArmorMaterial("SENSORGLASSES", "", 200, new int[] { 0, 0, 0, 0 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
//    public static ArmorMaterial ARMOR_STEEL = EnumHelper.addArmorMaterial("steel", "", 30, new int[] { 3, 6, 8, 3 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
//    public static ToolMaterial TOOL_STEEL = EnumHelper.addToolMaterial("steel", 3, 768, 5.0F, 2, 8);

    public static ArrayList<Item> hiddenItems = new ArrayList<Item>();
    public static LinkedList<ItemCanisterGeneric> canisterTypes = new LinkedList<ItemCanisterGeneric>();
    public static HashMap<ItemStack, ItemStack> itemChanges = new HashMap<>(4, 1.0F);

    public static Item.Properties defaultBuilder()
    {
        return new Item.Properties().tab(GalacticraftCore.galacticraftItemsTab);
    }

    public static void registerItems(IRegistryWrapper<Item> r)
    {
        register(r, new ItemOxygenTank(defaultBuilder().durability(900)), GCItemNames.oxTankLight);
        register(r, new ItemOxygenTank(defaultBuilder().durability(1800)), GCItemNames.oxTankMedium);
        register(r, new ItemOxygenTank(defaultBuilder().durability(2700)), GCItemNames.oxTankHeavy);
        register(r, new ItemOxygenMask(defaultBuilder().stacksTo(1)), GCItemNames.oxMask);
        register(r, new ItemTier1Rocket(defaultBuilder().durability(0).stacksTo(1)), GCItemNames.rocketTierOne);
        register(r, new ItemTier1Rocket(defaultBuilder().durability(0).stacksTo(1)), GCItemNames.rocketTierOneCargo1);
        register(r, new ItemTier1Rocket(defaultBuilder().durability(0).stacksTo(1)), GCItemNames.rocketTierOneCargo2);
        register(r, new ItemTier1Rocket(defaultBuilder().durability(0).stacksTo(1)), GCItemNames.rocketTierOneCargo3);
        register(r, new ItemTier1Rocket(defaultBuilder().durability(0).stacksTo(1)), GCItemNames.rocketTierOneCreative);
        register(r, new ItemSensorGlasses(defaultBuilder()), GCItemNames.sensorGlasses);
        register(r, new ItemPickaxeGC(defaultBuilder()), GCItemNames.steelPickaxe);
        register(r, new ItemAxeGC(defaultBuilder()), GCItemNames.steelAxe);
        register(r, new ItemHoeGC(defaultBuilder()), GCItemNames.steelHoe);
        register(r, new ItemShovelGC(defaultBuilder()), GCItemNames.steelSpade);
        register(r, new ItemSwordGC(defaultBuilder()), GCItemNames.steelSword);
        register(r, new ArmorItemGC(EquipmentSlot.HEAD, defaultBuilder()), GCItemNames.steelHelmet);
        register(r, new ArmorItemGC(EquipmentSlot.CHEST, defaultBuilder()), GCItemNames.steelChestplate);
        register(r, new ArmorItemGC(EquipmentSlot.LEGS, defaultBuilder()), GCItemNames.steelLeggings);
        register(r, new ArmorItemGC(EquipmentSlot.FEET, defaultBuilder()), GCItemNames.steelBoots);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.oxygenVent);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.oxygenFan);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.oxygenConcentrator);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.heavyPlatingTier1);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.rocketEngineT1);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.rocketBoosterT1);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.partFins);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.partNoseCone);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.sensorLens);
        register(r, new ItemBuggy(defaultBuilder().stacksTo(1)), GCItemNames.buggy);
        register(r, new ItemBuggy(defaultBuilder().stacksTo(1)), GCItemNames.buggyInventory1);
        register(r, new ItemBuggy(defaultBuilder().stacksTo(1)), GCItemNames.buggyInventory2);
        register(r, new ItemBuggy(defaultBuilder().stacksTo(1)), GCItemNames.buggyInventory3);
        register(r, new ItemFlag(defaultBuilder().durability(0)), GCItemNames.flag);
        register(r, new ItemOxygenGear(defaultBuilder()), GCItemNames.oxygenGear);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.canvas);
        register(r, new ItemOilCanister(defaultBuilder().durability(ItemCanisterGeneric.EMPTY_CAPACITY)), GCItemNames.oilCanister);
        register(r, new ItemFuelCanister(defaultBuilder().durability(ItemCanisterGeneric.EMPTY_CAPACITY)), GCItemNames.fuelCanister);
        register(r, new ItemCanisterOxygenInfinite(defaultBuilder()), GCItemNames.oxygenCanisterInfinite);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.flagPole);
        register(r, new ItemSchematic(defaultBuilder().durability(0).stacksTo(1)), GCItemNames.schematicBuggy);
        register(r, new ItemSchematic(defaultBuilder().durability(0).stacksTo(1)), GCItemNames.schematicRocketT2);
        register(r, new ItemKey(defaultBuilder()), GCItemNames.key);
//        register(r, new ItemFood(defaultBuilder()), ItemNames.foodItem);
        register(r, new ItemBattery(defaultBuilder().durability(ItemElectricBase.DAMAGE_RANGE)), GCItemNames.battery);
        register(r, new ItemBatteryInfinite(defaultBuilder()), GCItemNames.infiniteBatery);
        register(r, new ItemMeteorChunk(defaultBuilder().stacksTo(16)), GCItemNames.meteorChunk);
        register(r, new ItemMeteorChunk(defaultBuilder().stacksTo(16)), GCItemNames.meteorChunkHot);
        register(r, new ItemUniversalWrench(defaultBuilder().durability(256)), GCItemNames.wrench);
        register(r, new ItemBase(defaultBuilder().food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).fast().build())), GCItemNames.cheeseCurd);
//		GCItems.cheeseBlock = new ItemBlockCheese(GCBlocks.cheeseBlock, "cheeseBlock");
        register(r, new ItemBase(defaultBuilder()), GCItemNames.meteoricIronRaw);
        register(r, new ItemPreLaunchChecklist(defaultBuilder()), GCItemNames.prelaunchChecklist);
        register(r, new ItemDungeonFinder(defaultBuilder()), GCItemNames.dungeonFinder);
//        register(r, new ItemIC2Compat(defaultBuilder()), ItemNames.ic2compat); TODO
        register(r, new ItemEmergencyKit(defaultBuilder().durability(0)), GCItemNames.emergencyKit);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.solarModule0);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.solarModule1);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.rawSilicon);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.ingotCopper);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.ingotTin);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.ingotAluminum);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedCopper);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedTin);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedAluminum);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedSteel);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedBronze);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedIron);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedWaferSolar);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedWaferBasic);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedWaferAdvanced);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.frequencyModule);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.ambientThermalController);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.buggyMaterialWheel);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.buggyMaterialSeat);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.buggyMaterialStorage);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.canisterTin);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.canisterCopper);
        register(r, new ItemBase(defaultBuilder().food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.3F).build())), GCItemNames.dehydratedApple);
        register(r, new ItemBase(defaultBuilder().food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.6F).build())), GCItemNames.dehydratedCarrot);
        register(r, new ItemBase(defaultBuilder().food((new FoodProperties.Builder()).nutrition(4).saturationMod(0.3F).build())), GCItemNames.dehydratedMelon);
        register(r, new ItemBase(defaultBuilder().food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).build())), GCItemNames.dehydratedPotato);
        register(r, new ItemBase(defaultBuilder().food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build())), GCItemNames.cheeseSlice);
        register(r, new ItemBase(defaultBuilder().food((new FoodProperties.Builder()).nutrition(4).saturationMod(0.8F).build())), GCItemNames.burgerBun);
        register(r, new ItemBase(defaultBuilder().food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).build())), GCItemNames.beefPattyRaw);
        register(r, new ItemBase(defaultBuilder().food((new FoodProperties.Builder()).nutrition(4).saturationMod(0.6F).build())), GCItemNames.beefPattyCooked);
        register(r, new ItemBase(defaultBuilder().food((new FoodProperties.Builder()).nutrition(14).saturationMod(1.0F).build())), GCItemNames.cheeseburger);
        register(r, new ItemBase(defaultBuilder().food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.6F).build())), GCItemNames.cannedBeef);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.ingotMeteoricIron);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedMeteoricIron);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.lunarSapphire);
        Item.Properties parachuteProps = defaultBuilder().durability(0).stacksTo(1);
        register(r, new ItemParaChute(DyeColor.WHITE, parachuteProps), GCItemNames.parachuteWhite);
        register(r, new ItemParaChute(DyeColor.BLACK, parachuteProps), GCItemNames.parachuteBlack);
        register(r, new ItemParaChute(DyeColor.LIGHT_BLUE, parachuteProps), GCItemNames.parachuteLightBlue);
        register(r, new ItemParaChute(DyeColor.LIME, parachuteProps), GCItemNames.parachuteLime);
        register(r, new ItemParaChute(DyeColor.BROWN, parachuteProps), GCItemNames.parachuteBrown);
        register(r, new ItemParaChute(DyeColor.BLUE, parachuteProps), GCItemNames.parachuteBlue);
        register(r, new ItemParaChute(DyeColor.GRAY, parachuteProps), GCItemNames.parachuteGray);
        register(r, new ItemParaChute(DyeColor.GREEN, parachuteProps), GCItemNames.parachuteGreen);
        register(r, new ItemParaChute(DyeColor.LIGHT_GRAY, parachuteProps), GCItemNames.parachuteLightGray);
        register(r, new ItemParaChute(DyeColor.MAGENTA, parachuteProps), GCItemNames.parachuteMagenta);
        register(r, new ItemParaChute(DyeColor.ORANGE, parachuteProps), GCItemNames.parachuteOrange);
        register(r, new ItemParaChute(DyeColor.PINK, parachuteProps), GCItemNames.parachutePink);
        register(r, new ItemParaChute(DyeColor.PURPLE, parachuteProps), GCItemNames.parachutePurple);
        register(r, new ItemParaChute(DyeColor.RED, parachuteProps), GCItemNames.parachuteRed);
        register(r, new ItemParaChute(DyeColor.CYAN, parachuteProps), GCItemNames.parachuteTeal);
        register(r, new ItemParaChute(DyeColor.YELLOW, parachuteProps), GCItemNames.parachuteYellow);

//        GCItems.registerHarvestLevels();
//
//        GCItems.registerItems();
//        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 15), new ItemStack(GCItems.foodItem, 1, 0));
//        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 16), new ItemStack(GCItems.foodItem, 1, 1));
//        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 17), new ItemStack(GCItems.foodItem, 1, 2));
//        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 18), new ItemStack(GCItems.foodItem, 1, 3));

        PlatformSpecific.executeSided(EnvType.CLIENT, () -> () ->
        {
            ClientProxyCore.registerCanister(new PartialCanister(GCItems.oilCanister, Constants.MOD_ID_CORE, "oil_canister_partial", 7));
            ClientProxyCore.registerCanister(new PartialCanister(GCItems.fuelCanister, Constants.MOD_ID_CORE, "fuel_canister_partial", 7));
        });

        GCItems.canisterTypes.add((ItemCanisterGeneric) GCItems.fuelCanister);
        GCItems.canisterTypes.add((ItemCanisterGeneric) GCItems.oilCanister);
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
