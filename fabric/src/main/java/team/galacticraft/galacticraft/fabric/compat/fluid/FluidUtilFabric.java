package team.galacticraft.galacticraft.fabric.compat.fluid;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.fluid.volume.SimpleFluidKey;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import org.jetbrains.annotations.NotNull;

public class FluidUtilFabric
{
    private FluidUtilFabric() {}

    public static @NotNull FluidAmount toAmountLBA(@NotNull Fraction stack)
    {
        return FluidAmount.of(stack.getNumerator(), stack.getDenominator());
    }

    public static @NotNull Fraction toFractionA(@NotNull FluidAmount stack)
    {
        return Fraction.of(stack.numerator, stack.denominator);
    }

    public static @NotNull FluidVolume toVolumeLBA(@NotNull FluidStack stack)
    {
        return new SimpleFluidKey(new FluidKey.FluidKeyBuilder(stack.getFluid())).withAmount(toAmountLBA(stack.getAmount()));
    }

    public static @NotNull SimpleFluidKey toFluidKey(@NotNull FluidStack stack)
    {
        return new SimpleFluidKey(new FluidKey.FluidKeyBuilder(stack.getFluid()));
    }

    public static @NotNull FluidStack toVolumeA(@NotNull FluidVolume stack)
    {
        return FluidStack.create(stack.getRawFluid(), toFractionA(stack.getAmount_F()));
    }
}
