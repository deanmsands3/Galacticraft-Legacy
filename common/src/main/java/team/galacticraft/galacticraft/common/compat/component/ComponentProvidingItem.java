package team.galacticraft.galacticraft.common.compat.component;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ComponentProvidingItem extends Item {
    public ComponentProvidingItem(Properties properties) {
        super(properties);
    }

    public abstract @NotNull List<ComponentWrapper<?>> getValidComponents();
}
