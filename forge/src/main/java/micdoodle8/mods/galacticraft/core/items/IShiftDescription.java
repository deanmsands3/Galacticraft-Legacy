package micdoodle8.mods.galacticraft.core.items;

import net.minecraft.world.item.ItemStack;

public interface IShiftDescription
{
    String getShiftDescription(ItemStack stack);

    boolean showDescription(ItemStack stack);
}
