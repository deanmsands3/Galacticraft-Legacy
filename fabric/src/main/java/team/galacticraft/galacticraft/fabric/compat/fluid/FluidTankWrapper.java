package team.galacticraft.galacticraft.fabric.compat.fluid;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.filter.ConstantFluidFilter;
import alexiil.mc.lib.attributes.fluid.impl.DelegatingFixedFluidInv;
import alexiil.mc.lib.attributes.misc.Saveable;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;
import team.galacticraft.galacticraft.fabric.compat.util.Serializable;

import java.util.Objects;

public class FluidTankWrapper<T extends FixedFluidInv & Saveable> extends DelegatingFixedFluidInv implements FluidTank, Serializable
{
    public FluidTankWrapper(T inv)
    {
        super(inv);
    }

    @Override
    public int size()
    {
        return delegate.getTankCount();
    }

    @Override
    public Fraction getCapacity(int tank)
    {
        return FabricFluidUtil.toFractionA(this.delegate.getMaxAmount_F(tank));
    }

    @Override
    public FluidStack getFluidStack(int tank)
    {
        return FabricFluidUtil.toVolumeA(delegate.getInvFluid(tank));
    }

    @Override
    public FluidStack extract(int tank, Fraction amount, ActionType action)
    {
        return FabricFluidUtil.toVolumeA(this.delegate.getTank(tank).attemptExtraction(ConstantFluidFilter.ANYTHING, FabricFluidUtil.toAmountLBA(amount), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION));
    }

    @Override
    public FluidStack extract(Fraction amount, ActionType action)
    {
        return FabricFluidUtil.toVolumeA(this.delegate.getExtractable().attemptExtraction(ConstantFluidFilter.ANYTHING, FabricFluidUtil.toAmountLBA(amount), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION));
    }

    @Override
    public FluidStack extract(FluidStack stack, ActionType action)
    {
        return FabricFluidUtil.toVolumeA(this.delegate.getExtractable().attemptExtraction(fluidKey -> stack.getFluid().equals(fluidKey.getRawFluid()), FabricFluidUtil.toAmountLBA(stack.getAmount()), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION));
    }

    @Override
    public FluidStack insert(int tank, FluidStack stack, ActionType action)
    {
        return FabricFluidUtil.toVolumeA(this.delegate.getTank(tank).attemptInsertion(FabricFluidUtil.toVolumeLBA(stack), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION));
    }

    @Override
    public boolean setFluid(int tank, FluidStack stack, ActionType actionType)
    {
        return this.delegate.setInvFluid(tank, FabricFluidUtil.toVolumeLBA(stack), actionType == ActionType.PERFORM ? Simulation.ACTION : Simulation.SIMULATE);
    }

    @Override
    public boolean isValid(int tank, FluidStack stack)
    {
        return this.delegate.isFluidValidForTank(tank, FabricFluidUtil.toFluidKey(stack));
    }

    @Override
    public void fromTag(@NotNull CompoundTag tag)
    {
        ((T) (this.delegate)).toTag(tag);
    }

    @Override
    public @NotNull CompoundTag toTag(@NotNull CompoundTag tag)
    {
        ((T) (this.delegate)).fromTag(tag);
        return tag;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FluidTankWrapper that = (FluidTankWrapper) o;
        return Objects.equals(delegate, that.delegate);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(delegate);
    }

    @Override
    public String toString()
    {
        return "FluidTankWrapper{" +
                "delegate=" + delegate +
                '}';
    }
}
