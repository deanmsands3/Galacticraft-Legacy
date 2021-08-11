package micdoodle8.mods.galacticraft.items;

import micdoodle8.mods.galacticraft.GalacticraftCore;
import micdoodle8.mods.galacticraft.init.GCItems;
import micdoodle8.mods.galacticraft.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.util.EnumSortCategoryItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHoeGC extends ItemHoe implements ISortableItem
{
    public ItemHoeGC(String assetName)
    {
        super(GCItems.TOOL_STEEL);
        this.setTranslationKey(assetName);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.TOOLS;
    }
}
