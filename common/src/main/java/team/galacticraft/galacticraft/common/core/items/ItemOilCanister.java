package team.galacticraft.galacticraft.common.core.items;

import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GCItems;
import team.galacticraft.galacticraft.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.List;

public class ItemOilCanister extends ItemCanisterGeneric implements ISortable
{
//    protected IIcon[] icons = new IIcon[7];

    public ItemOilCanister(Item.Properties builder)
    {
        super(builder);
//        this.setAllowedFluid(ConfigManagerCore.useOldOilFluidID.get() ? "oilgc" : "oil");
        this.setAllowedFluid(new ResourceLocation(Constants.MOD_ID_CORE, "oil")); // TODO Other oil support
//        this.setContainerItem(this);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        for (int i = 0; i < this.icons.length; i++)
        {
            this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + i);
        }
    }*/

//    @Override
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        if (itemStack.getMaxDamage() - itemStack.getDamage() == 0)
//        {
//            return "item.empty_liquid_canister";
//        }
//
//        if (itemStack.getDamage() == 1)
//        {
//            return "item.oil_canister";
//        }
//
//        return "item.oil_canister_partial";
//    }

    /*@Override
    public IIcon getIconFromDamage(int par1)
    {
        final int damage = 6 * par1 / this.getMaxDamage();

        if (this.icons.length > damage)
        {
            return this.icons[this.icons.length - damage - 1];
        }

        return super.getIconFromDamage(damage);
    }*/

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        if (stack.getMaxDamage() == stack.getDamageValue())
        {
            return "item.galacticraftcore.empty_liquid_canister";
        }

        if (stack.getDamageValue() == 1)
        {
            return "item.galacticraftcore.oil_canister";
        }

        return "item.galacticraftcore.oil_canister_partial";
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if (stack.getMaxDamage() - stack.getDamageValue() > 0)
        {
            tooltip.add(new TextComponent(I18n.get("gui.message.oil") + ": " + (stack.getMaxDamage() - stack.getDamageValue())));
        }
    }

    @Override
    public void appendStacks(CreativeModeTab group, NonNullList<ItemStack> items)
    {
        if (this.isIn(group))
        {
            items.add(createEmptyCanister(1));
            items.add(new ItemStack(this));
        }
    }


//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
////        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
////        {
////            list.add(new ItemStack(this, 1, this.getMaxDamage()));
////            list.add(new ItemStack(this, 1, 1));
////        }
//    }


    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (ItemCanisterGeneric.EMPTY_CAPACITY == stack.getDamageValue())
        {
//            stack.setTag(null);
        }
        else if (stack.getDamageValue() <= 0)
        {
            stack.setDamageValue(1);
        }
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.CANISTER;
    }

    public static ItemStack createEmptyCanister(int count)
    {
        ItemStack stack = new ItemStack(GCItems.oilCanister, count);
        stack.setDamageValue(ItemCanisterGeneric.EMPTY_CAPACITY);
        return stack;
    }
}
