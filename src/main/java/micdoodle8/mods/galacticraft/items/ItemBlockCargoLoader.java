package micdoodle8.mods.galacticraft.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockCargoLoader extends ItemBlockDesc
{
    public ItemBlockCargoLoader(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getTranslationKey(ItemStack par1ItemStack)
    {
        String name = "";

        if (par1ItemStack.getItemDamage() < 4)
        {
            name = "loader";
        }
        else
        {
            name = "unloader";
        }

        return this.getBlock().getTranslationKey() + "." + name;
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
}
