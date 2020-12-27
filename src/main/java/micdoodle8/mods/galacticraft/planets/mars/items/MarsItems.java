package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBase;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems.register;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_PLANETS)
public class MarsItems
{
    @ObjectHolder(MarsItemNames.DESH_PICKAXE)
    public static Item DESH_PICKAXE;
    @ObjectHolder(MarsItemNames.STICKY_DESH_PICKAXE)
    public static Item STICKY_DESH_PICKAXE;
    @ObjectHolder(MarsItemNames.DESH_AXE)
    public static Item DESH_AXE;
    @ObjectHolder(MarsItemNames.DESH_HOE)
    public static Item DESH_HOE;
    @ObjectHolder(MarsItemNames.DESH_SHOVEL)
    public static Item DESH_SHOVEL;
    @ObjectHolder(MarsItemNames.DESH_SWORD)
    public static Item DESH_SWORD;
    @ObjectHolder(MarsItemNames.DESH_HELMET)
    public static Item DESH_HELMET;
    @ObjectHolder(MarsItemNames.DESH_CHESTPLATE)
    public static Item DESH_CHESTPLATE;
    @ObjectHolder(MarsItemNames.DESH_LEGGINGS)
    public static Item DESH_LEGGINGS;
    @ObjectHolder(MarsItemNames.DESH_BOOTS)
    public static Item DESH_BOOTS;
    @ObjectHolder(MarsItemNames.TIER_2_ROCKET)
    public static Item TIER_2_ROCKET;
    @ObjectHolder(MarsItemNames.TIER_2_ROCKET_18_INVENTORY)
    public static Item TIER_2_ROCKET_18_INVENTORY;
    @ObjectHolder(MarsItemNames.TIER_2_ROCKET_36_INVENTORY)
    public static Item TIER_2_ROCKET_36_INVENTORY;
    @ObjectHolder(MarsItemNames.TIER_2_ROCKET_54_INVENTORY)
    public static Item TIER_2_ROCKET_54_INVENTORY;
    @ObjectHolder(MarsItemNames.CREATIVE_TIER_2_ROCKET)
    public static Item CREATIVE_TIER_2_ROCKET;
    @ObjectHolder(MarsItemNames.CARGO_ROCKET_18_INVENTORY)
    public static Item CARGO_ROCKET_18_INVENTORY;
    @ObjectHolder(MarsItemNames.CARGO_ROCKET_36_INVENTORY)
    public static Item CARGO_ROCKET_36_INVENTORY;
    @ObjectHolder(MarsItemNames.CARGO_ROCKET_54_INVENTORY)
    public static Item CARGO_ROCKET_54_INVENTORY;
    @ObjectHolder(MarsItemNames.CREATIVE_CARGO_ROCKET)
    public static Item CREATIVE_CARGO_ROCKET;
    @ObjectHolder(MarsItemNames.TIER_2_DUNGEON_KEY)
    public static Item TIER_2_DUNGEON_KEY;
    @ObjectHolder(MarsItemNames.TIER_3_ROCKET_SCHEMATIC)
    public static Item TIER_3_ROCKET_SCHEMATIC;
    @ObjectHolder(MarsItemNames.CARGO_ROCKET_SCHEMATIC)
    public static Item CARGO_ROCKET_SCHEMATIC;
    @ObjectHolder(MarsItemNames.ASTRO_MINER_SCHEMATIC)
    public static Item ASTRO_MINER_SCHEMATIC;
    @ObjectHolder(MarsItemNames.FRAGMENTED_CARBON)
    public static Item FRAGMENTED_CARBON;
    @ObjectHolder(MarsItemNames.UNREFINED_DESH)
    public static Item UNREFINED_DESH;
    @ObjectHolder(MarsItemNames.DESH_STICK)
    public static Item DESH_STICK;
    @ObjectHolder(MarsItemNames.DESH_INGOT)
    public static Item DESH_INGOT;
    @ObjectHolder(MarsItemNames.TIER_2_HEAVY_DUTY_PLATE)
    public static Item TIER_2_HEAVY_DUTY_PLATE;
    @ObjectHolder(MarsItemNames.SLIMELING_INVENTORY_BAG)
    public static Item SLIMELING_INVENTORY_BAG;
    @ObjectHolder(MarsItemNames.COMPRESSED_DESH)
    public static Item COMPRESSED_DESH;
    @ObjectHolder(MarsItemNames.FLUID_MANIPULATOR)
    public static Item FLUID_MANIPULATOR;
//    public static Item bucketSludge;

    public static Item.Properties defaultBuilder()
    {
        return new Item.Properties().group(GalacticraftCore.galacticraftItemsTab);
    }

//    public static ArmorMaterial ARMORDESH = EnumHelper.addArmorMaterial("DESH", "", 42, new int[] { 4, 7, 9, 4 }, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 3.0F);
//    public static ToolMaterial TOOLDESH = EnumHelper.addToolMaterial("DESH", 3, 1024, 5.0F, 2.5F, 10);

    public static void registerItems(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.UNREFINED_DESH);
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.DESH_STICK);
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.DESH_INGOT);
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.TIER_2_HEAVY_DUTY_PLATE);
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.SLIMELING_INVENTORY_BAG);
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.COMPRESSED_DESH);
        register(r, new ItemBase(defaultBuilder()), MarsItemNames.FLUID_MANIPULATOR);
        register(r, new ItemPickaxeMars(defaultBuilder()), MarsItemNames.DESH_PICKAXE);
        register(r, new ItemPickaxeMars(defaultBuilder()), MarsItemNames.STICKY_DESH_PICKAXE);
        register(r, new ItemAxeMars(defaultBuilder()), MarsItemNames.DESH_AXE);
        register(r, new ItemHoeMars(defaultBuilder()), MarsItemNames.DESH_HOE);
        register(r, new ItemShovelMars(defaultBuilder()), MarsItemNames.DESH_SHOVEL);
        register(r, new ItemSwordMars(defaultBuilder()), MarsItemNames.DESH_SWORD);
        register(r, new ItemArmorMars(EquipmentSlotType.HEAD, defaultBuilder()), MarsItemNames.DESH_HELMET);
        register(r, new ItemArmorMars(EquipmentSlotType.CHEST, defaultBuilder()), MarsItemNames.DESH_CHESTPLATE);
        register(r, new ItemArmorMars(EquipmentSlotType.LEGS, defaultBuilder()), MarsItemNames.DESH_LEGGINGS);
        register(r, new ItemArmorMars(EquipmentSlotType.FEET, defaultBuilder()), MarsItemNames.DESH_BOOTS);
//        @ObjectHolder(MarsItemNames.rocketTierTwo) public static Item rocketTierTwo;
//        @ObjectHolder(MarsItemNames.rocketTierTwoCargo1) public static Item rocketTierTwoCargo1;
//        @ObjectHolder(MarsItemNames.rocketTierTwoCargo2) public static Item rocketTierTwoCargo2;
//        @ObjectHolder(MarsItemNames.rocketTierTwoCargo3) public static Item rocketTierTwoCargo3;
//        @ObjectHolder(MarsItemNames.rocketTierTwoCreative) public static Item rocketTierTwoCreative;
//        @ObjectHolder(MarsItemNames.rocketCargo1) public static Item rocketCargo1;
//        @ObjectHolder(MarsItemNames.rocketCargo2) public static Item rocketCargo2;
//        @ObjectHolder(MarsItemNames.rocketCargo3) public static Item rocketCargo3;
//        @ObjectHolder(MarsItemNames.rocketCargoCreative) public static Item rocketCargoCreative;
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.TIER_2_ROCKET);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.TIER_2_ROCKET_18_INVENTORY);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.TIER_2_ROCKET_36_INVENTORY);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.TIER_2_ROCKET_54_INVENTORY);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.CREATIVE_TIER_2_ROCKET);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.CARGO_ROCKET_18_INVENTORY);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.CARGO_ROCKET_36_INVENTORY);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.CARGO_ROCKET_54_INVENTORY);
        register(r, new ItemTier2Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.CREATIVE_CARGO_ROCKET);
        register(r, new ItemKeyMars(defaultBuilder().maxDamage(0).maxStackSize(1)), MarsItemNames.TIER_2_DUNGEON_KEY);
        register(r, new ItemSchematicTier2(defaultBuilder()), MarsItemNames.TIER_3_ROCKET_SCHEMATIC);
        register(r, new ItemSchematicTier2(defaultBuilder()), MarsItemNames.CARGO_ROCKET_SCHEMATIC);
        register(r, new ItemSchematicTier2(defaultBuilder()), MarsItemNames.ASTRO_MINER_SCHEMATIC);
        register(r, new ItemCarbonFragments(defaultBuilder()), MarsItemNames.FRAGMENTED_CARBON);
    }

//    public static void registerHarvestLevels()
//    {
//        MarsItems.deshPickaxe.setHarvestLevel("pickaxe", 4);
//        MarsItems.deshPickSlime.setHarvestLevel("pickaxe", 4);
//        MarsItems.deshAxe.setHarvestLevel("axe", 4);
//        MarsItems.deshSpade.setHarvestLevel("shovel", 4);
//    }
//
//    private static void registerItems()
//    {
//        MarsItems.registerItem(MarsItems.carbonFragments);
//        MarsItems.registerItem(MarsItems.marsItemBasic);
//        MarsItems.registerItem(MarsItems.deshPickaxe);
//        MarsItems.registerItem(MarsItems.deshPickSlime);
//        MarsItems.registerItem(MarsItems.deshAxe);
//        MarsItems.registerItem(MarsItems.deshHoe);
//        MarsItems.registerItem(MarsItems.deshSpade);
//        MarsItems.registerItem(MarsItems.deshSword);
//        MarsItems.registerItem(MarsItems.deshHelmet);
//        MarsItems.registerItem(MarsItems.deshChestplate);
//        MarsItems.registerItem(MarsItems.deshLeggings);
//        MarsItems.registerItem(MarsItems.deshBoots);
//        MarsItems.registerItem(MarsItems.rocketMars);
//        MarsItems.registerItem(MarsItems.key);
//        MarsItems.registerItem(MarsItems.schematic);
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
