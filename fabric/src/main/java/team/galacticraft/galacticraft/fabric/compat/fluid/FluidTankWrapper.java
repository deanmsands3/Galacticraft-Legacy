package team.galacticraft.galacticraft.fabric.compat.fluid;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.filter.ConstantFluidFilter;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;

import java.util.Objects;

public class FluidTankWrapper implements FluidTank {
    private final SimpleFixedFluidInv inv;

    public FluidTankWrapper(SimpleFixedFluidInv inv) {
        this.inv = inv;
    }

    @Override
    public int size() {
        return inv.getTankCount();
    }

    @Override
    public FluidStack get(int tank) {
        return FluidUtilFabric.toVolumeA(inv.getInvFluid(tank));
    }

    @Override
    public FluidStack extract(int tank, Fraction amount, ActionType action) {
        return FluidUtilFabric.toVolumeA(this.inv.getTank(tank).attemptExtraction(ConstantFluidFilter.ANYTHING, FluidUtilFabric.toAmountLBA(amount), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION));
    }

    @Override
    public FluidStack extract(Fraction amount, ActionType action) {
        return FluidUtilFabric.toVolumeA(this.inv.attemptExtraction(ConstantFluidFilter.ANYTHING, FluidUtilFabric.toAmountLBA(amount), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION));
    }

    @Override
    public FluidStack extract(FluidStack stack, ActionType action) {
        return FluidUtilFabric.toVolumeA(this.inv.attemptExtraction(fluidKey -> stack.getFluid().equals(fluidKey.getRawFluid()), FluidUtilFabric.toAmountLBA(stack.getAmount()), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION));
    }

    @Override
    public FluidStack insert(int tank, FluidStack stack, ActionType action) {
        return FluidUtilFabric.toVolumeA(this.inv.getTank(tank).attemptInsertion(FluidUtilFabric.toVolumeLBA(stack), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION));
    }

    @Override
    public void fromTag(@NotNull CompoundTag tag) {
        this.inv.toTag(tag);
    }

    @Override
    public @NotNull CompoundTag toTag(@NotNull CompoundTag tag) {
        this.inv.fromTag(tag);
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FluidTankWrapper that = (FluidTankWrapper) o;
        return Objects.equals(inv, that.inv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inv);
    }

    @Override
    public String toString() {
        return "FluidTankWrapper{" +
                "inv=" + inv +
                '}';
    }
}
