package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBase;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems.register;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_PLANETS)
public class VenusItems
{
    @ObjectHolder(VenusItemNames.SHIELD_CONTROLLER)
    public static Item SHIELD_CONTROLLER;
    @ObjectHolder(VenusItemNames.LEAD_INGOT)
    public static Item LEAD_INGOT;
    @ObjectHolder(VenusItemNames.RADIOISOTOPE_CORE)
    public static Item RADIOISOTOPE_CORE;
    @ObjectHolder(VenusItemNames.ISOTHERMAL_FABRIC)
    public static Item ISOTHERMAL_FABRIC;
    @ObjectHolder(VenusItemNames.SOLAR_DUST)
    public static Item SOLAR_DUST;
    @ObjectHolder(VenusItemNames.SOLAR_ARRAY_PANEL)
    public static Item SOLAR_ARRAY_PANEL;
    @ObjectHolder(VenusItemNames.SOLAR_ARRAY_WAFER)
    public static Item SOLAR_ARRAY_WAFER;
    @ObjectHolder(VenusItemNames.TIER_2_THERMAL_HELMET)
    public static Item TIER_2_THERMAL_HELMET;
    @ObjectHolder(VenusItemNames.TIER_2_THERMAL_CHESTPLATE)
    public static Item TIER_2_THERMAL_CHESTPLATE;
    @ObjectHolder(VenusItemNames.TIER_2_THERMAL_LEGGINGS)
    public static Item TIER_2_THERMAL_LEGGINGS;
    @ObjectHolder(VenusItemNames.TIER_2_THERMAL_BOOTS)
    public static Item TIER_2_THERMAL_BOOTS;
    @ObjectHolder(VenusItemNames.VOLCANIC_PICKAXE)
    public static Item VOLCANIC_PICKAXE;
    @ObjectHolder(VenusItemNames.TIER_3_DUNGEON_KEY)
    public static Item TIER_3_DUNGEON_KEY;
    @ObjectHolder(VenusItemNames.ATOMIC_BATTERY)
    public static Item ATOMIC_BATTERY;

    public static Item.Properties defaultBuilder()
    {
        return new Item.Properties().tab(GalacticraftCore.galacticraftItemsTab);
    }

    public static void registerItems(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.SHIELD_CONTROLLER);
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.LEAD_INGOT);
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.RADIOISOTOPE_CORE);
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.ISOTHERMAL_FABRIC);
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.SOLAR_DUST);
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.SOLAR_ARRAY_PANEL);
        register(r, new ItemBase(defaultBuilder()), VenusItemNames.SOLAR_ARRAY_WAFER);
        register(r, new ItemThermalPaddingTier2(defaultBuilder()), VenusItemNames.TIER_2_THERMAL_HELMET);
        register(r, new ItemThermalPaddingTier2(defaultBuilder()), VenusItemNames.TIER_2_THERMAL_CHESTPLATE);
        register(r, new ItemThermalPaddingTier2(defaultBuilder()), VenusItemNames.TIER_2_THERMAL_LEGGINGS);
        register(r, new ItemThermalPaddingTier2(defaultBuilder()), VenusItemNames.TIER_2_THERMAL_BOOTS);
        register(r, new ItemVolcanicPickaxe(defaultBuilder()), VenusItemNames.VOLCANIC_PICKAXE);
        register(r, new ItemKeyVenus(defaultBuilder().stacksTo(1).durability(0)), VenusItemNames.TIER_3_DUNGEON_KEY);
        register(r, new ItemBatteryAtomic(defaultBuilder()), VenusItemNames.ATOMIC_BATTERY);

//        VenusItems.registerItems();
//        VenusItems.registerHarvestLevels();
    }

//    public static void registerHarvestLevels()
//    {
//    }
//
//    private static void registerItems()
//    {
//        VenusItems.registerItem(VenusItems.thermalPaddingTier2);
//        VenusItems.registerItem(VenusItems.basicItem);
//        VenusItems.registerItem(VenusItems.volcanicPickaxe);
//        VenusItems.registerItem(VenusItems.key);
//        VenusItems.registerItem(VenusItems.atomicBattery);
//    }
//
//    public static void registerItem(Item item)
//    {
//        String name = item.getUnlocalizedName().substring(5);
//        GCCoreUtil.registerGalacticraftItem(name, item);
//        GalacticraftCore.itemListTrue.add(item);
//        item.setRegistryName(name);
//    }
}
