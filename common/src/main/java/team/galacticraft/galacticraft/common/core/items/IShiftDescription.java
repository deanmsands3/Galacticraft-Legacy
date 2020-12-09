package team.galacticraft.galacticraft.common.core.items;

import net.minecraft.world.item.ItemStack;

public interface IShiftDescription
{
    String getShiftDescription(ItemStack stack);

    boolean showDescription(ItemStack stack);
}
