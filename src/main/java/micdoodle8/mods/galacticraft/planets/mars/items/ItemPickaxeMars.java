package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemPickaxeMars extends PickaxeItem implements ISortable
{
    public ItemPickaxeMars(Item.Properties builder)
    {
        super(EnumItemTierMars.DESH, 1, -2.8F, builder);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Rarity getRarity(ItemStack stack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(this.getUnlocalizedName().replace("item.", "galacticraftmars:"));
    }*/

//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            list.add(new ItemStack(this, 1, 0));
//        }
//    }
//
//    @Override
//    public int getMetadata(int par1)
//    {
//        return par1;
//    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.TOOLS;
    }
}
