package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBase;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.wrappers.PartialCanister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_PLANETS)
public class AsteroidsItems
{
    @ObjectHolder(AsteroidItemNames.GRAPPLE)
    public static Item GRAPPLE;
    @ObjectHolder(AsteroidItemNames.TIER_3_ROCKET)
    public static Item TIER_3_ROCKET;
    @ObjectHolder(AsteroidItemNames.TIER_3_ROCKET_18_INVENTORY)
    public static Item TIER_3_ROCKET_18_INVENTORY;
    @ObjectHolder(AsteroidItemNames.TIER_3_ROCKET_36_INVENTORY)
    public static Item TIER_3_ROCKET_36_INVENTORY;
    @ObjectHolder(AsteroidItemNames.TIER_3_ROCKET_54_INVENTORY)
    public static Item TIER_3_ROCKET_54_INVENTORY;
    @ObjectHolder(AsteroidItemNames.CREATIVE_TIER_3_ROCKET)
    public static Item CREATIVE_TIER_3_ROCKET;
    @ObjectHolder(AsteroidItemNames.ASTRO_MINER)
    public static Item ASTRO_MINER;
    @ObjectHolder(AsteroidItemNames.THERMAL_HELMET)
    public static Item THERMAL_HELMET;
    @ObjectHolder(AsteroidItemNames.THERMAL_CHESTPLATE)
    public static Item THERMAL_CHESTPLATE;
    @ObjectHolder(AsteroidItemNames.THERMAL_LEGGINGS)
    public static Item THERMAL_LEGGINGS;
    @ObjectHolder(AsteroidItemNames.THERMAL_BOOTS)
    public static Item THERMAL_BOOTS;
    @ObjectHolder(AsteroidItemNames.PARTIAL_METHANE_CANISTER)
    public static Item PARTIAL_METHANE_CANISTER;
    @ObjectHolder(AsteroidItemNames.PARTIAL_LOX_CANISTER)
    public static Item PARTIAL_LOX_CANISTER;
    @ObjectHolder(AsteroidItemNames.PARTIAL_LN2_CANISTER)
    public static Item PARTIAL_LN2_CANISTER;
//    @ObjectHolder(AsteroidItemNames.canisterLAr)
//    public static Item canisterLAr;
    @ObjectHolder(AsteroidItemNames.ATMOSPHERIC_VALVE)
    public static Item ATMOSPHERIC_VALVE;
    @ObjectHolder(AsteroidItemNames.HEAVY_NOSE_CONE)
    public static Item HEAVY_NOSE_CONE;
    @ObjectHolder(AsteroidItemNames.ORION_DRIVE)
    public static Item ORION_DRIVE;
    @ObjectHolder(AsteroidItemNames.TITANIUM_HELMET)
    public static Item TITANIUM_HELMET;
    @ObjectHolder(AsteroidItemNames.TITANIUM_CHESTPLATE)
    public static Item TITANIUM_CHESTPLATE;
    @ObjectHolder(AsteroidItemNames.TITANIUM_LEGGINGS)
    public static Item TITANIUM_LEGGINGS;
    @ObjectHolder(AsteroidItemNames.TITANIUM_BOOTS)
    public static Item TITANIUM_BOOTS;
    @ObjectHolder(AsteroidItemNames.TITANIUM_AXE)
    public static Item TITANIUM_AXE;
    @ObjectHolder(AsteroidItemNames.TITANIUM_PICKAXE)
    public static Item TITANIUM_PICKAXE;
    @ObjectHolder(AsteroidItemNames.TITANIUM_SHOVEL)
    public static Item TITANIUM_SHOVEL;
    @ObjectHolder(AsteroidItemNames.TITANIUM_HOE)
    public static Item TITANIUM_HOE;
    @ObjectHolder(AsteroidItemNames.TITANIUM_SWORD)
    public static Item TITANIUM_SWORD;
    @ObjectHolder(AsteroidItemNames.SMALL_STRANGE_SEED)
    public static Item SMALL_STRANGE_SEED;
    @ObjectHolder(AsteroidItemNames.LARGE_STRANGE_SEED)
    public static Item LARGE_STRANGE_SEED;
    @ObjectHolder(AsteroidItemNames.TITANIUM_INGOT)
    public static Item TITANIUM_INGOT;
    @ObjectHolder(AsteroidItemNames.HEAVY_ROCKET_ENGINE)
    public static Item HEAVY_ROCKET_ENGINE;
    @ObjectHolder(AsteroidItemNames.HEAVY_ROCKET_FINS)
    public static Item HEAVY_ROCKET_FINS;
    @ObjectHolder(AsteroidItemNames.IRON_SHARD)
    public static Item IRON_SHARD;
    @ObjectHolder(AsteroidItemNames.TITANIUM_SHARD)
    public static Item TITANIUM_SHARD;
    @ObjectHolder(AsteroidItemNames.TIER_3_HEAVY_DUTY_PLATE)
    public static Item TIER_3_HEAVY_DUTY_PLATE;
    @ObjectHolder(AsteroidItemNames.COMPRESSED_TITANIUM)
    public static Item COMPRESSED_TITANIUM;
    @ObjectHolder(AsteroidItemNames.THERMAL_CLOTH)
    public static Item THERMAL_CLOTH;
    @ObjectHolder(AsteroidItemNames.BEAM_CORE)
    public static Item BEAM_CORE;
    @ObjectHolder(AsteroidItemNames.TITANIUM_DUST)
    public static Item TITANIUM_DUST;

//    public static Item.ToolMaterial TOOL_TITANIUM = EnumHelper.addToolMaterial("titanium", 4, 760, 14.0F, 4.0F, 16);
//    public static ArmorItem.ArmorMaterial ARMOR_TITANIUM = EnumHelper.addArmorMaterial("titanium", "", 26, new int[] { 5, 7, 10, 5 }, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
    {
        GCBlocks.register(reg, thing, new ResourceLocation(Constants.MOD_ID_PLANETS, name));
    }

    public static Item.Properties defaultBuilder()
    {
        return new Item.Properties().tab(GalacticraftCore.galacticraftItemsTab);
    }

    public static void registerItems(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        register(r, new ItemGrappleHook(defaultBuilder()), AsteroidItemNames.GRAPPLE);
        register(r, new ItemTier3Rocket(defaultBuilder()), AsteroidItemNames.TIER_3_ROCKET);
        register(r, new ItemTier3Rocket(defaultBuilder()), AsteroidItemNames.TIER_3_ROCKET_18_INVENTORY);
        register(r, new ItemTier3Rocket(defaultBuilder()), AsteroidItemNames.TIER_3_ROCKET_36_INVENTORY);
        register(r, new ItemTier3Rocket(defaultBuilder()), AsteroidItemNames.TIER_3_ROCKET_54_INVENTORY);
        register(r, new ItemTier3Rocket(defaultBuilder()), AsteroidItemNames.CREATIVE_TIER_3_ROCKET);
        register(r, new ItemAstroMiner(defaultBuilder().durability(0).stacksTo(1)), AsteroidItemNames.ASTRO_MINER);
        register(r, new ItemThermalPadding(defaultBuilder()), AsteroidItemNames.THERMAL_HELMET);
        register(r, new ItemThermalPadding(defaultBuilder()), AsteroidItemNames.THERMAL_CHESTPLATE);
        register(r, new ItemThermalPadding(defaultBuilder()), AsteroidItemNames.THERMAL_LEGGINGS);
        register(r, new ItemThermalPadding(defaultBuilder()), AsteroidItemNames.THERMAL_BOOTS);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.TITANIUM_INGOT);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.HEAVY_ROCKET_ENGINE);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.HEAVY_ROCKET_FINS);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.IRON_SHARD);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.TITANIUM_SHARD);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.TIER_3_HEAVY_DUTY_PLATE);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.COMPRESSED_TITANIUM);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.THERMAL_CLOTH);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.BEAM_CORE);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.TITANIUM_DUST);
        register(r, new ItemCanisterMethane(defaultBuilder()), AsteroidItemNames.PARTIAL_METHANE_CANISTER);
        register(r, new ItemCanisterLiquidOxygen(defaultBuilder()), AsteroidItemNames.PARTIAL_LOX_CANISTER);
        register(r, new ItemCanisterLiquidNitrogen(defaultBuilder()), AsteroidItemNames.PARTIAL_LN2_CANISTER);
//        register(r, new ItemCanisterLiquidArgon(defaultBuilder()), AsteroidItemNames.canisterLAr);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.ATMOSPHERIC_VALVE);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.HEAVY_NOSE_CONE);
        register(r, new ItemBase(defaultBuilder()), AsteroidItemNames.ORION_DRIVE);
        register(r, new ItemArmorAsteroids(EquipmentSlotType.HEAD, defaultBuilder()), AsteroidItemNames.TITANIUM_HELMET);
        register(r, new ItemArmorAsteroids(EquipmentSlotType.CHEST, defaultBuilder()), AsteroidItemNames.TITANIUM_CHESTPLATE);
        register(r, new ItemArmorAsteroids(EquipmentSlotType.LEGS, defaultBuilder()), AsteroidItemNames.TITANIUM_LEGGINGS);
        register(r, new ItemArmorAsteroids(EquipmentSlotType.FEET, defaultBuilder()), AsteroidItemNames.TITANIUM_BOOTS);
        register(r, new ItemAxeAsteroids(defaultBuilder()), AsteroidItemNames.TITANIUM_AXE);
        register(r, new ItemPickaxeAsteroids(defaultBuilder()), AsteroidItemNames.TITANIUM_PICKAXE);
        register(r, new ItemSpadeAsteroids(defaultBuilder()), AsteroidItemNames.TITANIUM_SHOVEL);
        register(r, new ItemHoeAsteroids(defaultBuilder()), AsteroidItemNames.TITANIUM_HOE);
        register(r, new ItemSwordAsteroids(defaultBuilder()), AsteroidItemNames.TITANIUM_SWORD);
        register(r, new ItemStrangeSeed(defaultBuilder()), AsteroidItemNames.SMALL_STRANGE_SEED);
        register(r, new ItemStrangeSeed(defaultBuilder()), AsteroidItemNames.LARGE_STRANGE_SEED);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
        {
            ClientProxyCore.registerCanister(new PartialCanister(AsteroidsItems.PARTIAL_METHANE_CANISTER, Constants.MOD_ID_PLANETS, "partial_methane_canister", 7));
            ClientProxyCore.registerCanister(new PartialCanister(AsteroidsItems.PARTIAL_LOX_CANISTER, Constants.MOD_ID_PLANETS, "partial_lox_canister", 7));
            ClientProxyCore.registerCanister(new PartialCanister(AsteroidsItems.PARTIAL_LN2_CANISTER, Constants.MOD_ID_PLANETS, "partial_ln2_canister", 7));
        });
    }

//    public static void oreDictRegistrations()
//    {
//        OreDictionary.registerOre("compressedTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 6));
//        OreDictionary.registerOre("ingotTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 0));
//        OreDictionary.registerOre("shardTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 4));
//        OreDictionary.registerOre("shardIron", new ItemStack(AsteroidsItems.basicItem, 1, 3));
//        OreDictionary.registerOre("dustTitanium", new ItemStack(AsteroidsItems.basicItem, 1, 9));
//    }
//
//    public static void registerHarvestLevels()
//    {
//        AsteroidsItems.titaniumPickaxe.setHarvestLevel("pickaxe", 5);
//        AsteroidsItems.titaniumAxe.setHarvestLevel("axe", 5);
//        AsteroidsItems.titaniumSpade.setHarvestLevel("shovel", 5);
//    }

//    private static void registerItems()
//    {
//        registerItem(AsteroidsItems.grapple);
//        registerItem(AsteroidsItems.tier3Rocket);
//        registerItem(AsteroidsItems.astroMiner);
//        registerItem(AsteroidsItems.thermalPadding);
//        registerItem(AsteroidsItems.methaneCanister);
//        registerItem(AsteroidsItems.canisterLOX);
//        registerItem(AsteroidsItems.canisterLN2);
//        //registerItem(AsteroidsItems.canisterLAr);
//        registerItem(AsteroidsItems.atmosphericValve);
//        registerItem(AsteroidsItems.heavyNoseCone);
//        registerItem(AsteroidsItems.orionDrive);
//        registerItem(AsteroidsItems.titaniumHelmet);
//        registerItem(AsteroidsItems.titaniumChestplate);
//        registerItem(AsteroidsItems.titaniumLeggings);
//        registerItem(AsteroidsItems.titaniumBoots);
//        registerItem(AsteroidsItems.titaniumAxe);
//        registerItem(AsteroidsItems.titaniumPickaxe);
//        registerItem(AsteroidsItems.titaniumSpade);
//        registerItem(AsteroidsItems.titaniumHoe);
//        registerItem(AsteroidsItems.titaniumSword);
//        registerItem(AsteroidsItems.strangeSeed);
//
//        ARMOR_TITANIUM.setRepairItem(new ItemStack(AsteroidsItems.basicItem, 1, 0));
//
//        GCItems.canisterTypes.add((ItemCanisterGeneric) AsteroidsItems.canisterLOX);
//        GCItems.canisterTypes.add((ItemCanisterGeneric) AsteroidsItems.methaneCanister);
//        GCItems.canisterTypes.add((ItemCanisterGeneric) AsteroidsItems.canisterLN2);
//    }
//
//    public static void registerItem(Item item)
//    {
//        String name = item.getUnlocalizedName().substring(5);
//        GCCoreUtil.registerGalacticraftItem(name, item);
//        GalacticraftCore.itemListTrue.add(item);
//        item.setRegistryName(name);
//        GalacticraftPlanets.proxy.postRegisterItem(item);
//    }
}
