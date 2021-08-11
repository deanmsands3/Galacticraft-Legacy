package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockBasicVenus;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockBasicVenus extends ItemBlock
{
    public ItemBlockBasicVenus(Block block)
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
        String name;

        switch (itemstack.getItemDamage())
        {
        default:
            name = BlockBasicVenus.EnumBlockBasicVenus.values()[itemstack.getItemDamage()].getName();
        }

        return this.getBlock().getTranslationKey() + "." + name;
    }

    @Override
    public String getTranslationKey()
    {
        return this.getBlock().getTranslationKey() + ".0";
    }
}
