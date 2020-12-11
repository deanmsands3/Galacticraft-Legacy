package team.galacticraft.galacticraft.common.core.items;

import team.galacticraft.galacticraft.common.api.item.IKeyItem;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class ItemKey extends Item implements IKeyItem, ISortable
{
    public ItemKey(Properties properties)
    {
        super(properties);
//        this.setMaxStackSize(1);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setUnlocalizedName(assetName);
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

//    @Override
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        return this.getUnlocalizedName() + ".t1";
//    }

    @Override
    @Environment(EnvType.CLIENT)
    public Rarity getRarity(ItemStack stack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            list.add(new ItemStack(this, 1, 0));
//        }
//    }

//    @Override
//    public int getMetadata(int par1)
//    {
//        return par1;
//    }

    @Override
    public int getTier(ItemStack keyStack)
    {
        return 1;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.KEYS;
    }
}
