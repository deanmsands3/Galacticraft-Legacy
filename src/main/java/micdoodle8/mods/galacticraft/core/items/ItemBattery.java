package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemBattery extends ItemElectricBase implements ISortable
{
    public ItemBattery(Item.Properties properties)
    {
        super(properties);
//        this.setMaxStackSize(4);
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
    public float getMaxElectricityStored(ItemStack itemStack)
    {
        return 15000;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }

    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        if (stack.getDamageValue() < DAMAGE_RANGE || stack.hasTag() && stack.getTag().contains("electricity"))
        {
            return 1;
        }
        return super.getItemStackLimit(stack);
    }
}
