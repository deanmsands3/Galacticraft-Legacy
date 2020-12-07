package team.galacticraft.galacticraft.fabric.compat.fluid;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.filter.ConstantFluidFilter;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.fluid.volume.SimpleFluidKey;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;

public class FluidTankImpl extends SimpleFixedFluidInv implements FluidTank {
    public FluidTankImpl(int invSize, FluidAmount tankCapacity) {
        super(invSize, tankCapacity);
    }

    /**
     * Returns the number of internal tanks in this tank
     * @return The number of internal tanks in this tank
     */
    @Override
    public int size() {
        return this.getTankCount();
    }

    /**
     * Returns the fluid in the tank
     * @param tank The index of the tank to get the fluid from
     * @return The amount of fluid that is inside the tank
     */
    @Override
    public @NotNull FluidStack get(int tank) {
        FluidVolume fluid = this.getTank(tank).get();
        return FluidStack.create(super.getTank(tank).get().getRawFluid(), Fraction.of(fluid.getAmount_F().numerator, fluid.getAmount_F().denominator), null);
    }

    /**
     * Extracts fluid from a tank
     * @param amount The amount of fluid to extract
     * @param action The action type
     * @return The amount of fluid that was successfully extracted
     */
    @Override
    public FluidStack extract(Fraction amount, ActionType action) {
        FluidVolume volume = this.attemptExtraction(ConstantFluidFilter.ANYTHING, FluidAmount.of(amount.getNumerator(), amount.getDenominator()), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION);
        return FluidStack.create(volume.getRawFluid(), Fraction.of(volume.getAmount_F().numerator, volume.getAmount_F().denominator));
    }

    /**
     * Extracts fluid from a tank
     * @param tank The tank to extract the fluid from
     * @param amount The amount of fluid to extract
     * @param action The action type
     * @return The amount of fluid that was successfully extracted
     */
    @Override
    public FluidStack extract(int tank, Fraction amount, ActionType action) {
        FluidVolume volume = this.getTank(tank).attemptExtraction(ConstantFluidFilter.ANYTHING, FluidAmount.of(amount.getNumerator(), amount.getDenominator()), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION);
        return FluidStack.create(volume.getRawFluid(), Fraction.of(volume.getAmount_F().numerator, volume.getAmount_F().denominator));
    }

    /**
     * Extracts fluid from a tank
     * @param stack The fluid stack to extract
     * @param action The action type
     * @return The amount of fluid that was successfully extracted
     */
    @Override
    public FluidStack extract(FluidStack stack, ActionType action) {
        Fraction amount = stack.getAmount();
        FluidVolume volume = this.attemptExtraction(fluidKey -> stack.getFluid().equals(fluidKey.getRawFluid()), FluidAmount.of(amount.getNumerator(), amount.getDenominator()), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION);
        return FluidStack.create(volume.getRawFluid(), Fraction.of(volume.getAmount_F().numerator, volume.getAmount_F().denominator));
    }

    /**
     * Inserts fluid into the tank
     * @param tank The tank to insert the fluid into
     * @param stack The fluid stack to insert
     * @param action The action type
     * @return The amount of fluid that was NOT inserted
     */
    @Override
    public FluidStack insert(int tank, FluidStack stack, ActionType action) {
        FluidVolume volume = this.getTank(tank).attemptInsertion(new SimpleFluidKey(new FluidKey.FluidKeyBuilder(stack.getFluid())).withAmount(FluidAmount.of(stack.getAmount().getNumerator(), stack.getAmount().getDenominator())), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION);
        return FluidStack.create(volume.getRawFluid(), Fraction.of(volume.getAmount_F().numerator, volume.getAmount_F().denominator));
    }

    /**
     * Deserializes data from a tag into this fluid tank
     * @param tag The tag to read data from
     */
    @Override
    public void fromTag(@NotNull CompoundTag tag) {
        super.fromTag(tag);
    }

    /**
     * Serializes this fluid tank into NBT form
     * @param tag The tag serialize the data onto
     */
    @Override
    public @NotNull CompoundTag toTag(@NotNull CompoundTag tag) {
        return super.toTag(tag);
    }
}
