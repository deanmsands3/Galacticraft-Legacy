package team.galacticraft.galacticraft.common.core.wrappers;

import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import me.shedaniel.architectury.fluid.FluidStack;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;

import javax.annotation.Nonnull;

public interface IFluidHandlerWrapper
{
    int fill(Direction from, FluidStack resource, ActionType fillAction);

    FluidStack drain(Direction from, FluidStack resource, ActionType fillAction);

    FluidStack drain(Direction from, int maxDrain, ActionType fillAction);

    boolean canFill(Direction from, Fluid fluid);

    boolean canDrain(Direction from, Fluid fluid);

    int getTanks();

    @NotNull
    FluidStack getFluidInTank(int tank);

    int getTankCapacity(int tank);

    boolean isFluidValid(int tank, @NotNull FluidStack stack);
}