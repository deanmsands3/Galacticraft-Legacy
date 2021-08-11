package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockSlimelingEgg;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockEgg extends ItemBlockDesc
{
    public ItemBlockEgg(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getTranslationKey(ItemStack itemstack)
    {
        String name = BlockSlimelingEgg.EnumEggColor.values()[itemstack.getItemDamage() % 3].getName();
        return this.getBlock().getTranslationKey() + "." + name;
    }

    @Override
    public String getTranslationKey()
    {
        return this.getBlock().getTranslationKey() + ".0";
    }
}
