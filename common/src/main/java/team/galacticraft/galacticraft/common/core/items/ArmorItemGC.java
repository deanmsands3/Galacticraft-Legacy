package team.galacticraft.galacticraft.common.core.items;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCItems;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import net.minecraft.item.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ArmorItemGC extends ArmorItem implements ISortable
{
    public ArmorItemGC(EquipmentSlot slot, Properties builder)
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
    @Environment(EnvType.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
    {
        if (this.getMaterial() == EnumArmorGC.ARMOR_STEEL)
        {
            if (stack.getItem() == GCItems.steelHelmet)
            {
                return Constants.TEXTURE_PREFIX + "textures/model/armor/steel_1.png";
            }
            else if (stack.getItem() == GCItems.steelChestplate || stack.getItem() == GCItems.steelBoots)
            {
                return Constants.TEXTURE_PREFIX + "textures/model/armor/steel_2.png";
            }
            else if (stack.getItem() == GCItems.steelLeggings)
            {
                return Constants.TEXTURE_PREFIX + "textures/model/armor/steel_3.png";
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
