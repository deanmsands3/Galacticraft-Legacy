package team.galacticraft.galacticraft.common.core.inventory;

import net.minecraft.world.Container;

public interface IInventorySettable extends Container
{
    void setSizeInventory(int size);
}
