package micdoodle8.mods.galacticraft.items;

import micdoodle8.mods.galacticraft.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockMoon extends ItemBlockDesc
{
    public ItemBlockMoon(Block block)
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
        case 0:
        {
            name = "coppermoon";
            break;
        }
        case 1:
        {
            name = "tinmoon";
            break;
        }
        case 2:
        {
            name = "cheesestone";
            break;
        }
        case 3:
        {
            name = "moondirt";
            break;
        }
        case 4:
        {
            name = "moonstone";
            break;
        }
        case 5:
        {
            name = "moongrass";
            break;
        }
        case 6:
        {
            name = "sapphiremoon";
            break;
        }
        case 14:
        {
            name = "bricks";
            break;
        }
        default:
            name = "null";
        }

        return this.getBlock().getTranslationKey() + "." + name;
    }

    @Override
    public String getTranslationKey()
    {
        return this.getBlock().getTranslationKey() + ".0";
    }
}
