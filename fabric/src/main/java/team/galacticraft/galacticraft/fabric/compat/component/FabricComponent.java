package team.galacticraft.galacticraft.fabric.compat.component;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.extension.TypeAwareComponent;
import net.minecraft.nbt.CompoundTag;
import team.galacticraft.galacticraft.common.compat.component.NbtSerializable;

public class FabricComponent<T extends NbtSerializable> implements TypeAwareComponent {
    private final ComponentType<FabricComponent<T>> type;
    private final T value;

    public FabricComponent(ComponentType<FabricComponent<T>> type, T value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return this.type;
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        this.value.fromTag(compoundTag);
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        return this.value.toTag(compoundTag);
    }

    public T getValue() {


    }
}
