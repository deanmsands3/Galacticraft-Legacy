package micdoodle8.mods.galacticraft.items;

import micdoodle8.mods.galacticraft.blocks.BlockAluminumWire;
import micdoodle8.mods.galacticraft.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockAluminumWire extends ItemBlockDesc
{
    public ItemBlockAluminumWire(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)
    {
        return this.getBlock().getIcon(0, par1);
    }*/

    @Override
    public String getTranslationKey(ItemStack itemstack)
    {
        String name = BlockAluminumWire.EnumWireType.values()[itemstack.getItemDamage()].getName();
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
