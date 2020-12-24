package team.galacticraft.galacticraft.common.core.wrappers;

import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluids;
import me.shedaniel.architectury.fluid.FluidStack;
import team.galacticraft.galacticraft.common.compat.component.ComponentWrapper;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;

import org.jetbrains.annotations.NotNull;

public abstract class FluidHandlerWrapper
{
    public IFluidHandlerWrapper wrapper;

    public Direction side;

    public FluidHandlerWrapper(IFluidHandlerWrapper w, Direction s)
    {
        wrapper = w;
        side = s;
    }

//    @Override
    public int getTanks()
    {
        return wrapper.getTanks();
    }

    @NotNull
//    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return wrapper.getFluidInTank(tank);
    }

//    @Override
    public Fraction getTankCapacity(int tank)
    {
        return wrapper.getTankCapacity(tank);
    }

//    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack)
    {
        return wrapper.isFluidValid(tank, stack);
    }

//    @Override
    public FluidStack fill(FluidStack resource, ActionType action)
    {
        if (wrapper.canFill(side, resource != null ? resource.getFluid() : null))
        {
            return wrapper.fill(side, resource, action);
        }

        return FluidStack.empty()();
    }

//    @Override
    public FluidStack drain(FluidStack resource, ActionType action)
    {
        if (wrapper.canDrain(side, resource != FluidStack.empty()() ? resource.getFluid() : Fluids.EMPTY))
        {
            return wrapper.drain(side, resource, action);
        }

        return null;
    }

//    @Override
    public FluidStack drain(Fraction maxDrain, ActionType action)
    {
        if (wrapper.canDrain(side, Fluids.EMPTY))
        {
            return wrapper.drain(side, maxDrain, action);
        }

        return null;
    }

    public boolean setFluid(int tank, FluidStack stack, ActionType action)
    {
        return wrapper.setFluid(tank, stack, action);
    }

    public CompoundTag serialize(CompoundTag tag) {
        return wrapper.serialize(tag);
    }

    public void deserialize(CompoundTag tag) {
        wrapper.deserialize(tag);
    }
}