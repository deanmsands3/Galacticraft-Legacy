package team.galacticraft.galacticraft.common.compat.fluid;

import me.shedaniel.architectury.ExpectPlatform;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

/**
 * Fractional Fluid API-API
 */
public class FluidTank {
    /**
     * Returns the number of internal tanks in this tank
     * @return The number of internal tanks in this tank
     */
    @ExpectPlatform
    public int size() {
        throw new AssertionError();
    }

    /**
     * Returns the fluid in the tank at index zero
     * @return The amount of fluid that is inside the tank at index zero
     */
    public @NotNull FluidStack get() {
        return get(0);
    }

    /**
     * Returns the fluid in the tank
     * @param tank The index of the tank to get the fluid from
     * @return The amount of fluid that is inside the tank
     */
    @ExpectPlatform
    public @NotNull FluidStack get(int tank) {
        throw new AssertionError();
    }

    /**
     * Extracts fluid from a tank
     * @param tank The tank to extract the fluid from
     * @param amount The amount of fluid to extract
     * @param action The action type
     * @return The amount of fluid that was successfully extracted
     */
    @ExpectPlatform
    public FluidStack extract(int tank, Fraction amount, ActionType action) {
        throw new AssertionError();
    }

    /**
     * Extracts fluid from a tank
     * @param tank The tank to extract the fluid from
     * @param stack The fluid stack to extract
     * @param action The action type
     * @return The amount of fluid that was successfully extracted
     */
    public FluidStack extract(int tank, FluidStack stack, ActionType action) {
        if (equalIgnoreAmount(stack, get(tank))) return extract(tank, stack.getAmount(), action);
        return FluidStack.empty();
    }

    /**
     * Extracts fluid from a tank
     * @param stack The fluid stack to extract
     * @param action The action type
     * @return The stack of fluid that was successfully extracted
     */
    public FluidStack extract(FluidStack stack, ActionType action) {
        FluidStack amount = stack.copy();
        amount.setAmount(Fraction.zero());
        for (int i = 0; i < size(); i++) {
            if (amount.equals(stack)) return amount;
            if (equalIgnoreAmount(stack, get(i))) {
                amount.setAmount(amount.getAmount().add(this.extract(i, amount.getAmount(), action).getAmount()));
            }
        }
        return amount;
    }

    /**
     * Inserts fluid into the tank
     * @param stack The fluid stack to insert
     * @param action The action type
     * @return The amount of fluid that was NOT inserted
     */
    public FluidStack insert(FluidStack stack, ActionType action) {
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
    @ExpectPlatform
    public FluidStack insert(int tank, FluidStack stack, ActionType action) {
        throw new AssertionError();
    }

    private static boolean equalIgnoreAmount(FluidStack a, FluidStack b) {
        return a.getFluid() == b.getFluid() && (a.getTag() == null ? b.getTag() == null : a.getTag().equals(b.getTag()));
    }

    /**
     * Deserializes data from a tag into this fluid tank
     * @param tag The tag to read data from
     */
    @ExpectPlatform
    public void fromTag(@NotNull CompoundTag tag) {
        throw new AssertionError();
    }

    /**
     * Serializes this fluid tank into NBT form
     * @param tag The tag serialize the data onto
     */
    @ExpectPlatform
    public @NotNull CompoundTag toTag(@NotNull CompoundTag tag) {
        throw new AssertionError();
    }
}
