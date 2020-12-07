package team.galacticraft.galacticraft.common.compat.cap;

import net.minecraft.nbt.CompoundTag;

public interface NbtSerializable {
    void fromTag(CompoundTag var1);

    CompoundTag toTag(CompoundTag var1);
}