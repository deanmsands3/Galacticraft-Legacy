package micdoodle8.mods.galacticraft.items;

import micdoodle8.mods.galacticraft.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockWallGC extends ItemBlock
{
    private static final String[] types = new String[] {
            "tin",
            "tin",
            "moon",
            "moon_bricks",
            "mars",
            "mars_bricks"
    };

    public ItemBlockWallGC(Block block)
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
        int meta = itemstack.getItemDamage();

        if (meta < 0 || meta >= types.length)
        {
            meta = 0;
        }
        return super.getTranslationKey() + "." + types[meta];
    }
}