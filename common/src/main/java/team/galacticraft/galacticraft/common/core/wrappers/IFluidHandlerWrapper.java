package team.galacticraft.galacticraft.common.core.wrappers;

import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import me.shedaniel.architectury.fluid.FluidStack;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;

import org.jetbrains.annotations.NotNull;

public interface IFluidHandlerWrapper
{
    FluidStack fill(Direction from, FluidStack resource, ActionType fillAction);

    FluidStack drain(Direction from, FluidStack resource, ActionType fillAction);

    FluidStack drain(Direction from, Fraction maxDrain, ActionType fillAction);

    boolean canFill(Direction from, Fluid fluid);

    boolean canDrain(Direction from, Fluid fluid);

    int getTanks();

    @NotNull
    FluidStack getFluidInTank(int tank);

    Fraction getTankCapacity(int tank);

    boolean isFluidValid(int tank, @NotNull FluidStack stack);

    boolean setFluid(int tank, @NotNull FluidStack stack, ActionType actionType);

    CompoundTag serialize(CompoundTag tag);

    void deserialize(CompoundTag tag);
}