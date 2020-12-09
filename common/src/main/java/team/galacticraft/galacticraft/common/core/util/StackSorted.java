package team.galacticraft.galacticraft.common.core.util;

import java.util.Objects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class StackSorted
{
    private final Item item;
//    private final int meta;

    public StackSorted(Block block)
    {
        this(Item.byBlock(block));
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
