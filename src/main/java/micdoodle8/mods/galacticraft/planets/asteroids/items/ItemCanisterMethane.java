package micdoodle8.mods.galacticraft.planets.asteroids.items;

import java.util.List;
import javax.annotation.Nullable;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCanisterMethane extends ItemCanisterGeneric implements ISortableItem
{
//    protected IIcon[] icons = new IIcon[7];

    public ItemCanisterMethane(String assetName)
    {
        super(assetName);
        this.setAllowedFluid("methane");
        // this.setTextureName(GalacticraftPlanets.TEXTURE_PREFIX + assetName);
    }

    /*
     * @Override
     * @SideOnly(Side.CLIENT) public void registerIcons(IIconRegister
     * iconRegister) { for (int i = 0; i < this.icons.length; i++) {
     * this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" +
     * i); } }
     */

    @Override
    public String getTranslationKey(ItemStack itemStack)
    {
        if (itemStack.getMaxDamage() - itemStack.getItemDamage() == 0)
        {
            return "item.empty_gas_canister";
        }

        if (itemStack.getItemDamage() == 1)
        {
            return "item.methane_canister";
        }

        return "item.methane_canister_partial";
    }

    /*
     * @Override public IIcon getIconFromDamage(int par1) { final int damage = 6
     * * par1 / this.getMaxDamage(); if (this.icons.length > damage) { return
     * this.icons[this.icons.length - damage - 1]; } return
     * super.getIconFromDamage(damage); }
     */

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() > 0)
        {
            tooltip.add(GCCoreUtil.translate("item.canister.gas.name") + ": " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
        }
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.CANISTER;
    }
}
