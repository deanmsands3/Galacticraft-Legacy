package team.galacticraft.galacticraft.fabric.compat.component;

import alexiil.mc.lib.attributes.Attribute;
import alexiil.mc.lib.attributes.misc.Reference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;
import team.galacticraft.galacticraft.common.compat.component.ComponentWrapper;
import team.galacticraft.galacticraft.common.compat.item.SingleSlotAccessor;
import team.galacticraft.galacticraft.fabric.compat.util.Serializable;

import java.util.function.Function;

public class FabricComponentWrapperLBA<T extends Serializable, R> implements ComponentWrapper<T> {
    private final Attribute<R> attribute;
    private final Function<R, T> function;

    public FabricComponentWrapperLBA(Attribute<R> attribute, Function<R, T> function) {
        this.attribute = attribute;
        this.function = function;
    }

    @Override
    public void fromTag(CompoundTag tag, T instance) {
        instance.fromTag(tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag, T instance) {
        return instance.toTag(tag);
    }

    @Override
    public LazyOptional<T> get(Object provider) {
        return LazyOptional.create(() -> {
            if (provider instanceof ItemStack) {
                return function.apply(attribute.getFirstOrNull((ItemStack) provider));
            } else if (provider instanceof BlockEntity) {
                return function.apply(attribute.getFirstOrNull(((BlockEntity) provider).getLevel(), ((BlockEntity) provider).getBlockPos()));
            } else if (provider instanceof SingleSlotAccessor) {
                return function.apply(attribute.getFirstOrNull(new Reference<ItemStack>() {
                    @Override
                    public ItemStack get() {
                        return ((SingleSlotAccessor) provider).get();
                    }

                    @Override
                    public boolean set(ItemStack value) {
                        ((SingleSlotAccessor) provider).set(value);
                        return value == get();
                    }

                    @Override
                    public boolean isValid(ItemStack value) {
                        return true;
                    }
                }));
            }
            return null;
        });
    }

    public Attribute<R> getRawAttribute() {
        return attribute;
    }

    public Function<R, T> getFunction() {
        return function;
    }
}
