package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.Comparator;

public class CreativeTabGC extends CreativeModeTab
{
    private ItemStack itemForTab;
    private Comparator<ItemStack> tabSorter;

    public CreativeTabGC(String name, ItemStack itemForTab, Comparator<ItemStack> tabSorter)
    {
        super(name);
        this.itemForTab = itemForTab;
        this.tabSorter = tabSorter;
    }

    @Override
    public ItemStack makeIcon()
    {
        return this.itemForTab;
    }

    public void setItemForTab(ItemStack itemForTab)
    {
        this.itemForTab = itemForTab;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getName()
    {
        return "item_group." + this.getLangId();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillItemList(NonNullList<ItemStack> items)
    {
        for (Item item : Registry.ITEM)
        {
            item.fillItemCategory(this, items);
        }

        if (tabSorter != null)
        {
            try
            {
                Collections.sort(items, tabSorter);
            }
            catch (Exception e)
            {

            }
        }
    }

//    @Override
//    public void displayAllRelevantItems(NonNullList<ItemStack> list)
//    {
//        super.displayAllRelevantItems(list);
//        if (this.tabSorter != null)
//        {
//            try {
//                Collections.sort(list, tabSorter);
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    } TODO Sorting

    public void setTabSorter(Comparator<ItemStack> tabSorter)
    {
        this.tabSorter = tabSorter;
    }
}
