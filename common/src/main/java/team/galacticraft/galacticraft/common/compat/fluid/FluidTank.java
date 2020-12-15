package team.galacticraft.galacticraft.common.compat.fluid;

import me.shedaniel.architectury.ExpectPlatform;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import team.galacticraft.galacticraft.common.compat.component.NbtSerializable;

/**
 * Fluid api-api, fractional
 */
public interface FluidTank extends NbtSerializable {
    /**
     * Returns the number of internal tanks in this tank
     * @return The number of internal tanks in this tank
     */
    int size();

    /**
     * Returns the fluid in the tank at index zero
     * @return The amount of fluid that is inside the tank at index zero
     */
    default FluidStack get() {
        return get(0);
    }

    /**
     * Returns the fluid in the tank
     * @param tank The index of the tank to get the fluid from
     * @return The amount of fluid that is inside the tank
     */
    FluidStack get(int tank);

    /**
     * Extracts fluid from a tank
     * @param tank The tank to extract the fluid from
     * @param amount The amount of fluid to extract
     * @param action The action type
     * @return The amount of fluid that was successfully extracted
     */
    FluidStack extract(int tank, Fraction amount, ActionType action);

    /**
     * Extracts fluid from a tank
     * @param amount The amount of fluid to extract
     * @param action The action type
     * @return The amount of fluid that was successfully extracted
     */
    @ExpectPlatform
    FluidStack extract(Fraction amount, ActionType action);

    /**
     * Extracts fluid from a tank
     * @param stack The fluid stack to extract
     * @param action The action type
     * @return The amount of fluid that was successfully extracted
     */
    @ExpectPlatform
    FluidStack extract(FluidStack stack, ActionType action);

    /**
     * Inserts fluid into the tank
     * @param stack The fluid stack to insert
     * @param action The action type
     * @return The amount of fluid that was NOT inserted
     */
    default FluidStack insert(FluidStack stack, ActionType action) {
        for (int i = 0; i < size(); i++) {
            if (stack.isEmpty()) return FluidStack.empty();
            stack = this.insert(i, stack, action);
        }
        return stack;
    }

    /**
     * Inserts fluid into the tank
     * @param tank The tank to insert the fluid into
     * @param stack The fluid stack to insert
     * @param action The action type
     * @return The amount of fluid that was NOT inserted
     */
    FluidStack insert(int tank, FluidStack stack, ActionType action);

    boolean isValid(int tank, FluidStack stack);

    /**
     * Deserializes data from a tag into this fluid tank
     * @param tag The tag to read data from
     */
    void fromTag(@NotNull CompoundTag tag);

    /**
     * Serializes this fluid tank into NBT form
     * @param tag The tag serialize the data onto
     */
    @NotNull CompoundTag toTag(@NotNull CompoundTag tag);
}
