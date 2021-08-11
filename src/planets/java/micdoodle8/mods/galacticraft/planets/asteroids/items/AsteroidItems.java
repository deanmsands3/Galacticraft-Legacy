package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.Constants;
import micdoodle8.mods.galacticraft.GalacticraftCore;
import micdoodle8.mods.galacticraft.init.GCItems;
import micdoodle8.mods.galacticraft.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.wrappers.PartialCanister;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

public class AsteroidItems
{

	public static Item grapple;
	public static Item tier3Rocket;
	public static Item astroMiner;
	public static Item thermalPadding;
	public static Item basicItem;
	public static Item methaneCanister;
	public static Item canisterLOX;
	public static Item canisterLN2;
	public static Item atmosphericValve;
	public static ItemHeavyNoseCone heavyNoseCone;
	public static Item orionDrive;
	public static Item titaniumHelmet;
	public static Item titaniumChestplate;
	public static Item titaniumLeggings;
	public static Item titaniumBoots;
	public static Item titaniumAxe;
	public static Item titaniumPickaxe;
	public static Item titaniumSpade;
	public static Item titaniumHoe;
	public static Item titaniumSword;
	public static Item strangeSeed;

	public static Item.ToolMaterial TOOL_TITANIUM = EnumHelper.addToolMaterial("titanium", 4, 760, 14.0F, 4.0F, 16);
	public static ItemArmor.ArmorMaterial ARMOR_TITANIUM = EnumHelper.addArmorMaterial("titanium", "", 26, new int[]
	{ 5, 7, 10, 5 }, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);

	public static void initItems()
	{
		grapple = new ItemGrappleHook("grapple");
		tier3Rocket = new ItemTier3Rocket("rocket_t3");
		astroMiner = new ItemAstroMiner("astro_miner");
		thermalPadding = new ItemThermalPadding("thermal_padding");
		basicItem = new ItemBasicAsteroids("item_basic_asteroids");
		methaneCanister = new ItemCanisterMethane("methane_canister_partial");
		canisterLOX = new ItemCanisterLiquidOxygen("canister_partial_lox");
		canisterLN2 = new ItemCanisterLiquidNitrogen("canister_partial_ln2");
		atmosphericValve = new ItemAtmosphericValve("atmospheric_valve");
		heavyNoseCone = new ItemHeavyNoseCone("heavy_nose_cone");
		orionDrive = new ItemOrionDrive("orion_drive");
		titaniumHelmet = new ItemArmorAsteroids(EntityEquipmentSlot.HEAD, "helmet");
		titaniumChestplate = new ItemArmorAsteroids(EntityEquipmentSlot.CHEST, "chestplate");
		titaniumLeggings = new ItemArmorAsteroids(EntityEquipmentSlot.LEGS, "leggings");
		titaniumBoots = new ItemArmorAsteroids(EntityEquipmentSlot.FEET, "boots");
		titaniumAxe = new ItemAxeAsteroids("titanium_axe");
		titaniumPickaxe = new ItemPickaxeAsteroids("titanium_pickaxe");
		titaniumSpade = new ItemSpadeAsteroids("titanium_shovel");
		titaniumHoe = new ItemHoeAsteroids("titanium_hoe");
		titaniumSword = new ItemSwordAsteroids("titanium_sword");
		strangeSeed = new ItemStrangeSeed("strange_seed");

		registerItems();

		registerHarvestLevels();

		GalacticraftCore.proxy.registerCanister(new PartialCanister(methaneCanister, Constants.MOD_ID_PLANETS, "methane_canister_partial", 7));
		GalacticraftCore.proxy.registerCanister(new PartialCanister(canisterLOX, Constants.MOD_ID_PLANETS, "canister_partial_lox", 7));
		GalacticraftCore.proxy.registerCanister(new PartialCanister(canisterLN2, Constants.MOD_ID_PLANETS, "canister_partial_ln2", 7));
	}

	public static void oreDictRegistrations()
	{
		OreDictionary.registerOre("compressedTitanium", new ItemStack(basicItem, 1, 6));
		OreDictionary.registerOre("ingotTitanium", new ItemStack(basicItem, 1, 0));
		OreDictionary.registerOre("shardTitanium", new ItemStack(basicItem, 1, 4));
		OreDictionary.registerOre("shardIron", new ItemStack(basicItem, 1, 3));
		OreDictionary.registerOre("dustTitanium", new ItemStack(basicItem, 1, 9));
	}

	public static void registerHarvestLevels()
	{
		titaniumPickaxe.setHarvestLevel("pickaxe", 5);
		titaniumAxe.setHarvestLevel("axe", 5);
		titaniumSpade.setHarvestLevel("shovel", 5);
	}

	private static void registerItems()
	{
		registerItem(grapple);
		registerItem(tier3Rocket);
		registerItem(astroMiner);
		registerItem(thermalPadding);
		registerItem(basicItem);
		registerItem(methaneCanister);
		registerItem(canisterLOX);
		registerItem(canisterLN2);
		registerItem(atmosphericValve);
		registerItem(heavyNoseCone);
		registerItem(orionDrive);
		registerItem(titaniumHelmet);
		registerItem(titaniumChestplate);
		registerItem(titaniumLeggings);
		registerItem(titaniumBoots);
		registerItem(titaniumAxe);
		registerItem(titaniumPickaxe);
		registerItem(titaniumSpade);
		registerItem(titaniumHoe);
		registerItem(titaniumSword);
		registerItem(strangeSeed);

		ARMOR_TITANIUM.setRepairItem(new ItemStack(basicItem, 1, 0));

		GCItems.canisterTypes.add((ItemCanisterGeneric) canisterLOX);
		GCItems.canisterTypes.add((ItemCanisterGeneric) methaneCanister);
		GCItems.canisterTypes.add((ItemCanisterGeneric) canisterLN2);
	}

	public static void registerItem(Item item)
	{
		String name = item.getTranslationKey().substring(5);
		GCCoreUtil.registerGalacticraftItem(name, item);
		GalacticraftCore.itemListTrue.add(item);
		item.setRegistryName(name);
		GalacticraftPlanets.proxy.postRegisterItem(item);
	}
}
