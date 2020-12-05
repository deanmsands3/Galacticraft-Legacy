package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.Objects;

public class StackSorted
{
    private final Item item;
//    private final int meta;

    public StackSorted(Block block)
    {
        this(Item.getItemFromBlock(block));
    }

    public StackSorted(Item item)
    {
        this.item = item;
//        this.meta = meta;
    }

    public Item getItem()
    {
        return item;
    }

//    public int getMeta()
//    {
//        return meta;
//    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StackSorted that = (StackSorted) o;
        return Objects.equals(getItem(), that.getItem());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getItem());
    }

    @Override
    public String toString()
    {
        return "Item:(" + item + ")";
    }
}
