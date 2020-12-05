package team.galacticraft.galacticraft.common.api.item;

import net.minecraft.world.item.ItemStack;

/**
 * Interface version of team.galacticraft.galacticraft.common.core.energy.item.ItemElectricBase
 * For use in electrical ItemArmor, or other items which must extend different classes
 */
public interface IItemElectricBase extends IItemElectric
{
    float getMaxTransferGC(ItemStack itemStack);
}
