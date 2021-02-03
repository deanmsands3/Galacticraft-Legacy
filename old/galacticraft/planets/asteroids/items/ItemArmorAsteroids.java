package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemArmorAsteroids extends ArmorItem implements ISortable
{
    public ItemArmorAsteroids(EquipmentSlot slotType, Item.Properties properties)
    {
        super(EnumArmorAsteroids.ARMOR_TITANIUM, slotType, properties);
//        this.setUnlocalizedName("titanium_" + assetSuffix);
        //this.setTextureName(GalacticraftPlanets.TEXTURE_PREFIX + "titanium_" + assetSuffix);
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
    {
        if (this.getMaterial() == EnumArmorAsteroids.ARMOR_TITANIUM)
        {
            if (stack.getItem() == AsteroidsItems.TITANIUM_HELMET)
            {
                return GalacticraftPlanets.TEXTURE_PREFIX + "textures/models/armor/titanium_layer_1.png";
            }
            else if (stack.getItem() == AsteroidsItems.TITANIUM_CHESTPLATE || stack.getItem() == AsteroidsItems.TITANIUM_BOOTS)
            {
                return GalacticraftPlanets.TEXTURE_PREFIX + "textures/models/armor/titanium_layer_2.png";
            }
            else if (stack.getItem() == AsteroidsItems.TITANIUM_LEGGINGS)
            {
                return GalacticraftPlanets.TEXTURE_PREFIX + "textures/models/armor/titanium_layer_3.png";
            }
        }

        return null;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.ARMOR;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == this && repair.getDamageValue() == 6;
    }
}
