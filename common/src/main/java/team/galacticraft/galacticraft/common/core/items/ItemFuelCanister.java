package team.galacticraft.galacticraft.common.core.items;

import net.minecraft.network.chat.TranslatableComponent;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

import org.jetbrains.annotations.Nullable;

public class ItemFuelCanister extends ItemCanisterGeneric implements ISortable
{
//    protected IIcon[] icons = new IIcon[7];

    public ItemFuelCanister(Item.Properties builder)
    {
        super(builder);
//        this.setAllowedFluid(ConfigManagerCore.useOldFuelFluidID.get() ? "fuelgc" : "fuel");
        this.setAllowed(stack -> fuel); //todo(marcus): fluids
//        this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
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
//        if (itemStack.getDamage() == 1)
//        {
//            return "item.fuel_canister";
//        }
//
//        return "item.fuel_canister_partial";
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
    public String getDescriptionId(ItemStack stack)
    {
        if (stack.getDamageValue() == 1)
        {
            return "item.galacticraftcore.fuel_canister";
        }

        return "item.galacticraftcore.fuel_canister_partial";
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if (stack.getMaxDamage() - stack.getDamageValue() > 0)
        {
            tooltip.add(new TranslatableComponent(("gui.message.fuel") + ": " + (stack.getMaxDamage() - stack.getDamageValue())));
        }
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.CANISTER;
    }
}
