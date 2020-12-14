package team.galacticraft.galacticraft.forge.core.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class ComponentProvidingItem extends Item {
    public ComponentProvidingItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return null;
    }
}
