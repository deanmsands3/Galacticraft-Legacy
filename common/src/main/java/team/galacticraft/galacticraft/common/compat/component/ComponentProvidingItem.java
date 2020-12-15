package team.galacticraft.galacticraft.common.compat.component;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;
import team.galacticraft.galacticraft.common.compat.item.SingleSlotAccessor;

import java.util.LinkedList;
import java.util.List;

public abstract class ComponentProvidingItem extends Item {
    public static final List<ComponentProvidingItem> COMPONENT_PROVIDING_ITEMS = new LinkedList<>();

    public ComponentProvidingItem(Properties properties) {
        super(properties);
        COMPONENT_PROVIDING_ITEMS.add(this);
    }

    public abstract ComponentWrapper<?>[] getComponents();

    /**
     * Use {@link PlatformSpecific}, indices should match {@link #getComponents()}
     */
    public abstract Object[] createInstances();

    public <T> T getComponent(ComponentWrapper<T> componentWrapper, Either<ItemStack, SingleSlotAccessor> accessor) {
        return PlatformSpecific.getItemComponent(componentWrapper, accessor);
    }
}
