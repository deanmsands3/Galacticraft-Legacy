package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockTorchWeb;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import javax.annotation.Nullable;

public class ItemBlockTorchWeb extends ItemBlockDesc
{
    public ItemBlockTorchWeb(Block block)
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
        String name = BlockTorchWeb.EnumWebType.values()[itemstack.getItemDamage()].getName();
        return this.getBlock().getTranslationKey() + "." + name;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (stack.getItemDamage() == 1)
        {
            tooltip.add(GCCoreUtil.translate("tile.web_torch.web_torch_1.description"));
        }
    }

    @Override
    public String getTranslationKey()
    {
        return this.getBlock().getTranslationKey() + ".0";
    }
}
