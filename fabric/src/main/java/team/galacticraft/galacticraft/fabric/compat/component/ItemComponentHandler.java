package team.galacticraft.galacticraft.fabric.compat.component;

import team.galacticraft.galacticraft.common.compat.component.ComponentProvidingItem;
import team.galacticraft.galacticraft.common.compat.component.ComponentWrapper;

public class ItemComponentHandler {
    private ItemComponentHandler() {}

    public static void addComponents() { //todo(marcus): this is very cursed code. redo this soon
        for (ComponentProvidingItem item : ComponentProvidingItem.COMPONENT_PROVIDING_ITEMS) {
            ComponentWrapper<?>[] components = item.getComponents();
            Object[] instances = item.createInstances();

            for (int i = 0, componentsLength = components.length; i < componentsLength; i++) {
                ComponentWrapper<?> wrapper = components[i];
                if (!(wrapper instanceof FabricComponentWrapperLBA)) {
                    throw new AssertionError();
                }
                int finalI = i;
                ((FabricComponentWrapperLBA<?, ?>) wrapper).getRawAttribute().appendItemAdder((stack, excess, to) -> {
                    if (item == stack.get().getItem()) {
                        to.offer(instances[finalI]);
                    }
                });
            }
        }
    }
}
