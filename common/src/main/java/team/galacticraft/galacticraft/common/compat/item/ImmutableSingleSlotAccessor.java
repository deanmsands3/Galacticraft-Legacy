package team.galacticraft.galacticraft.common.compat.item;

import net.minecraft.world.item.ItemStack;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;

public class ImmutableSingleSlotAccessor implements SingleSlotAccessor
{
    private final ItemStack stack;

    public ImmutableSingleSlotAccessor(ItemStack stack)
    {
        this.stack = stack;
    }

    @Override
    public void set(ItemStack stack)
    {
        PlatformSpecific.getLogger().debug("Tried to set immutable slot view!");
    }

    @Override
    public ItemStack get()
    {
        return stack;
    }
}
