package team.galacticraft.galacticraft.common.compat.component;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;

public interface ComponentProvider
{
    @NotNull <T> LazyOptional<T> getComponent(@NotNull final ComponentWrapper<T> cap, @Nullable Direction side);

    @NotNull default <T> LazyOptional<T> getComponent(@NotNull final ComponentWrapper<T> cap) {
        return getComponent(cap, null);
    }
}
