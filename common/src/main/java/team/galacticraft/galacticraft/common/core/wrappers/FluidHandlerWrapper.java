package team.galacticraft.galacticraft.common.core.wrappers;

import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluids;
import me.shedaniel.architectury.fluid.FluidStack;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;

import javax.annotation.Nonnull;

public class FluidHandlerWrapper implements IFluidHandler
{
    public IFluidHandlerWrapper wrapper;

    public Direction side;

    public FluidHandlerWrapper(IFluidHandlerWrapper w, Direction s)
    {
        wrapper = w;
        side = s;
    }

    @Override
    public int getTanks()
    {
        return wrapper.getTanks();
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return wrapper.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return wrapper.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack)
    {
        return wrapper.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, ActionType action)
    {
        if (wrapper.canFill(side, resource != null ? resource.getFluid() : null))
        {
            return wrapper.fill(side, resource, action);
        }

        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, ActionType action)
    {
        if (wrapper.canDrain(side, resource != FluidStack.EMPTY ? resource.getFluid() : Fluids.EMPTY))
        {
            return wrapper.drain(side, resource, action);
        }

        return null;
    }

    @Override
    public FluidStack drain(int maxDrain, ActionType action)
    {
        if (wrapper.canDrain(side, Fluids.EMPTY))
        {
            return wrapper.drain(side, maxDrain, action);
        }

        return null;
    }
}