package team.galacticraft.galacticraft.common.compat.component;

import net.minecraft.nbt.CompoundTag;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;

public interface ComponentWrapper<T>
{
    void fromTag(CompoundTag tag, T instance);

    CompoundTag toTag(CompoundTag tag, T instance);

    LazyOptional<T> get(Object provider);
}
