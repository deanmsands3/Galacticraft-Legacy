package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBase extends Item implements ISortable
{
    float smeltingXP = -1F;

    public ItemBase(Item.Properties properties)
    {
        super(properties);
//        this.setUnlocalizedName(assetName);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
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
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if (stack != null && this == GCItems.heavyPlatingTier1)
        {
            tooltip.add(new TextComponent(GCCoreUtil.translate("item.tier1.desc")));
        }
        else if (stack != null && this == GCItems.dungeonFinder)
        {
            tooltip.add(new TextComponent(EnumColor.RED + GCCoreUtil.translate("gui.creative_only.desc")));
        }
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }

    public Item setSmeltingXP(float f)
    {
        this.smeltingXP = f;
        return this;
    }

    @Override
    public float getSmeltingExperience(ItemStack item)
    {
        return this.smeltingXP;
    }
}
