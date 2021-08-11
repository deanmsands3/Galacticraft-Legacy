package micdoodle8.mods.galacticraft.items;

import micdoodle8.mods.galacticraft.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockDummy extends ItemBlock
{
    public ItemBlockDummy(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public IIcon getIconFromDamage(int par1)
//    {
//        return this.getBlock().getIcon(0, par1);
//    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
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
        int metadata = itemstack.getItemDamage();
        String blockName = "";

        switch (metadata)
        {
        case 1:
            blockName = "spaceStationBase";
            break;
        case 2:
            blockName = "launchPad";
            break;
        case 3:
            blockName = "nasaWorkbench";
            break;
        case 4:
            blockName = "solar";
            break;
        case 5:
            blockName = "cryogenicChamber";
            break;
        default:
            blockName = null;
            break;
        }

        return this.getBlock().getTranslationKey() + "." + blockName;
    }

    @Override
    public String getTranslationKey()
    {
        return this.getBlock().getTranslationKey() + ".0";
    }
}
