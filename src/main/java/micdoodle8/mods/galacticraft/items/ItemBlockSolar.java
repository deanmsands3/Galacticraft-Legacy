package micdoodle8.mods.galacticraft.items;

import micdoodle8.mods.galacticraft.blocks.BlockSolar;
import micdoodle8.mods.galacticraft.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockSolar extends ItemBlockDesc
{
    public ItemBlockSolar(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getTranslationKey(ItemStack par1ItemStack)
    {
        int index = Math.min(Math.max(par1ItemStack.getItemDamage() / 4, 0), BlockSolar.EnumSolarType.values().length);

        String name = BlockSolar.EnumSolarType.values()[index].getName();

        return this.getBlock().getTranslationKey() + "." + name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
}
