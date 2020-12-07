package team.galacticraft.galacticraft.fabric.compat.cap;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.nbt.CompoundTag;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;
import team.galacticraft.galacticraft.common.compat.cap.ComponentWrapper;
import team.galacticraft.galacticraft.common.compat.cap.NbtSerializable;

public class FabricComponentWrapper<T extends NbtSerializable> implements ComponentWrapper<T> {
    private final ComponentType<FabricComponent<T>> type;

    public FabricComponentWrapper(ComponentType<FabricComponent<T>> type) {
        this.type = type;
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
        return LazyOptional.create(() -> type.maybeGet(provider).get().getValue());
    }
}
