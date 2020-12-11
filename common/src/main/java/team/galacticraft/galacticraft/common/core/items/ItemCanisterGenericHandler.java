package team.galacticraft.galacticraft.common.core.items;

import me.shedaniel.architectury.fluid.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import team.galacticraft.galacticraft.common.core.GCItems;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class ItemCanisterGenericHandler implements IFluidHandlerItem, ICapabilityProvider
{
    private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);
    @NotNull
    protected ItemStack container;

    public ItemCanisterGenericHandler(@NotNull ItemStack container)
    {
        this.container = container;
    }

    @NotNull
    @Override
    public ItemStack getContainer()
    {
        return container;  //never varies
    }

    @NotNull
    public FluidStack getFluid()
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
            return ((ItemCanisterGeneric) container.getItem()).getFluid(container);
        }
        return FluidStack.EMPTY;
    }

    protected void setFluid(FluidStack fluid)
    {
        if (this.canFillFluidType(fluid))
        {
            container.getItem().setDamage(container, ItemCanisterGeneric.EMPTY_CAPACITY - fluid.getAmount());
        }
    }

    @Override
    public int getTanks()
    {
        return 1;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return getFluid();
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return ItemCanisterGeneric.EMPTY_CAPACITY - 1;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack)
    {
        int capacityPlusOne = container.getDamageValue();
        if (capacityPlusOne >= ItemCanisterGeneric.EMPTY_CAPACITY)
        {
            for (ItemCanisterGeneric i : GCItems.canisterTypes)
            {
                if (stack.getFluid().getRegistryName() == i.getAllowedFluid())
                {
                    return true;
                }
            }
        }

        return stack.getFluid().getRegistryName() == ((ItemCanisterGeneric) container.getItem()).getAllowedFluid();
    }

    //    @Override
//    public IFluidTankProperties[] getTankProperties()
//    {
//        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
//        {
//        	return new FluidTankProperties[] { new FluidTankProperties(getFluid(), ItemCanisterGeneric.EMPTY - 1) };
//        }
//        return new FluidTankProperties[0];
//    }

    @Override
    public int fill(FluidStack resource, ActionType action)
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
            return ((ItemCanisterGeneric) container.getItem()).fill(container, resource, action);
        }
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, ActionType action)
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
            return ((ItemCanisterGeneric) container.getItem()).drain(container, maxDrain, action);
        }
        return null;
    }

    @Override
    public FluidStack drain(FluidStack resource, ActionType action)
    {
        if (this.canDrainFluidType(resource))
        {
            return ((ItemCanisterGeneric) container.getItem()).drain(container, resource.getAmount(), action);
        }
        return null;
    }

    public boolean canFillFluidType(FluidStack fluid)
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric && fluid != null && fluid != FluidStack.EMPTY)
        {
            return ((ItemCanisterGeneric) container.getItem()).getAllowedFluid() == fluid.getFluid().getRegistryName();
        }
        return false;
    }

    public boolean canDrainFluidType(FluidStack fluid)
    {
        return this.canFillFluidType(fluid);
    }

    protected void setContainerToEmpty()
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
            container.getItem().setDamage(container, ItemCanisterGeneric.EMPTY_CAPACITY);
        }
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing)
    {
        return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.orEmpty(capability, holder);
    }
}

