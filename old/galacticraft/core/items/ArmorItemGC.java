package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.item.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ArmorItemGC extends ArmorItem implements ISortable
{
    public ArmorItemGC(EquipmentSlot slot, Item.Properties builder)
    {
        super(EnumArmorGC.ARMOR_STEEL, slot, builder);
//        this.setUnlocalizedName("steel_" + assetSuffix);
        //this.setTextureName(Constants.TEXTURE_PREFIX + "steel_" + assetSuffix);
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
        if (this.getMaterial() == EnumArmorGC.ARMOR_STEEL)
        {
            if (stack.getItem() == GCItems.HEAVY_DUTY_HELMET)
            {
                return Constants.TEXTURE_PREFIX + "textures/models/armor/steel_layer_1.png";
            }
            else if (stack.getItem() == GCItems.HEAVY_DUTY_CHESTPLATE || stack.getItem() == GCItems.HEAVY_DUTY_BOOTS)
            {
                return Constants.TEXTURE_PREFIX + "textures/models/armor/steel_layer_2.png";
            }
            else if (stack.getItem() == GCItems.HEAVY_DUTY_LEGGINGS)
            {
                return Constants.TEXTURE_PREFIX + "textures/models/armor/steel_layer_3.png";
            }
        }

        return null;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.ARMOR;
    }

//    @Override
//    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
//    {
//        return repair.getItem() == GCItems.basicItem && repair.getDamage() == 9;
//    }
}
