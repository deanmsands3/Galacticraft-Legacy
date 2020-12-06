package team.galacticraft.galacticraft.common.api.entity;

import org.jetbrains.annotations.NotNull;
import net.minecraft.world.item.ItemStack;

/**
 * Implement into entities that can be loaded with cargo
 */
public interface ICargoEntity
{
    enum EnumCargoLoadingState
    {
        FULL,
        EMPTY,
        NOTARGET,
        NOINVENTORY,
        SUCCESS
    }

    class RemovalResult
    {
        public final EnumCargoLoadingState resultState;
        @NotNull
        public final ItemStack resultStack;

        public RemovalResult(EnumCargoLoadingState resultState, @NotNull ItemStack resultStack)
        {
            this.resultState = resultState;
            this.resultStack = resultStack;
        }
    }

    EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd);

    RemovalResult removeCargo(boolean doRemove);
}
