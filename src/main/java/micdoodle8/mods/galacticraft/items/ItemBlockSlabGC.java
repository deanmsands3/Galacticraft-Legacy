package micdoodle8.mods.galacticraft.items;

import micdoodle8.mods.galacticraft.blocks.BlockDoubleSlabGC;
import micdoodle8.mods.galacticraft.blocks.BlockSlabGC;
import micdoodle8.mods.galacticraft.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockSlabGC extends ItemSlab
{
	public ItemBlockSlabGC(Block block, BlockSlabGC singleSlab, BlockDoubleSlabGC doubleSlab)
	{
		super(block, singleSlab, doubleSlab);
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta & 7;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	public String getTranslationKey(ItemStack itemStack)
	{
		BlockSlabGC slab = (BlockSlabGC)Block.getBlockFromItem(itemStack.getItem());
		return super.getTranslationKey() + "." + slab.getTranslationKey(itemStack.getItemDamage());
	}
}