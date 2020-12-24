package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.core.blocks.BlockRefinery;
import team.galacticraft.galacticraft.common.core.GCItems;
import team.galacticraft.galacticraft.common.core.energy.item.ItemElectricBase;
import team.galacticraft.galacticraft.common.core.energy.tile.TileBaseElectricBlockWithInventory;
import team.galacticraft.galacticraft.common.core.fluid.GCFluids;
import team.galacticraft.galacticraft.common.core.inventory.ContainerRefinery;
import team.galacticraft.galacticraft.common.core.util.ConfigManagerCore;
import team.galacticraft.galacticraft.common.core.util.FluidUtil;
import team.galacticraft.galacticraft.common.core.wrappers.FluidHandlerWrapper;
import team.galacticraft.galacticraft.common.core.wrappers.IFluidHandlerWrapper;
import team.galacticraft.galacticraft.common.core.Annotations.NetworkedField;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;
//import net.minecraftforge.registries.ObjectHolder;
import me.shedaniel.architectury.fluid.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityRefinery extends TileBaseElectricBlockWithInventory implements WorldlyContainer, IFluidHandlerWrapper, MenuProvider
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.refinery)
    public static BlockEntityType<TileEntityRefinery> TYPE;

    private final int tankCapacity = 24000;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public FluidTank oilTank = new FluidTank(this.tankCapacity);
    @NetworkedField(targetSide = EnvType.CLIENT)
    public FluidTank fuelTank = new FluidTank(this.tankCapacity);

    public static final int PROCESS_TIME_REQUIRED = 2;
    public static final int OUTPUT_PER_SECOND = 1;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public int processTicks = 0;

    public TileEntityRefinery()
    {
        super(TYPE);
        this.storage.setMaxExtract(ConfigManagerCore.hardMode.get() ? 90 : 60);
        this.oilTank.setFluid(FluidStack.create(GCFluids.OIL.getFluid(), 0));
        this.fuelTank.setFluid(FluidStack.create(GCFluids.FUEL.getFluid(), 0));
        this.inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.level.isClientSide)
        {
            final FluidStack liquid = FluidUtil.getFluidContained(this.getInventory().get(1));
            if (FluidUtil.isOil(liquid))
            {

                FluidUtil.loadFromContainer(this.oilTank, GCFluids.OIL.getFluid(), this.getInventory(), 1, liquid.getAmount());
            }

            checkFluidTankTransfer(2, this.fuelTank);

            if (this.canProcess() && this.hasEnoughEnergyToRun)
            {
                if (this.processTicks == 0)
                {
                    this.processTicks = this.getProcessTimeRequired();
                }
                else
                {
                    if (--this.processTicks <= 0)
                    {
                        this.smeltItem();
                        this.processTicks = this.canProcess() ? this.getProcessTimeRequired() : 0;
                    }
                }
            }
            else
            {
                this.processTicks = 0;
            }
        }
    }

    private int getProcessTimeRequired()
    {
        return (this.poweredByTierGC > 1) ? 1 : TileEntityRefinery.PROCESS_TIME_REQUIRED;
    }

    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        FluidUtil.tryFillContainerFuel(tank, this.getInventory(), slot);
    }

    public int getScaledOilLevel(int i)
    {
        return this.oilTank.getFluidAmount() * i / this.oilTank.getCapacity();
    }

    public int getScaledFuelLevel(int i)
    {
        return this.fuelTank.getFluidAmount() * i / this.fuelTank.getCapacity();
    }

    public boolean canProcess()
    {
        if (this.oilTank.getFluidAmount() <= 0)
        {
            return false;
        }

        if (this.fuelTank.getFluidAmount() >= this.fuelTank.getCapacity())
        {
            return false;
        }

        return !this.getDisabled(0);

    }

    public void smeltItem()
    {
        if (this.canProcess())
        {
            final int oilAmount = this.oilTank.getFluidAmount();
            final int fuelSpace = this.fuelTank.getCapacity() - this.fuelTank.getFluidAmount();

            final int amountToDrain = Math.min(Math.min(oilAmount, fuelSpace), TileEntityRefinery.OUTPUT_PER_SECOND);

            this.oilTank.drain(amountToDrain, ActionType.PERFORM);
            this.fuelTank.fill(FluidStack.create(GCFluids.FUEL.getFluid(), amountToDrain)/*FluidRegistry.getFluidStack(ConfigManagerCore.useOldFuelFluidID.get() ? "fuelgc" : "fuel", amountToDrain)*/, ActionType.PERFORM);
        }
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.processTicks = nbt.getInt("smeltingTicks");

        if (nbt.contains("oilTank"))
        {
            this.oilTank.readFromNBT(nbt.getCompound("oilTank"));
        }
        if (this.oilTank.getFluidStack() != FluidStack.empty() && this.oilTank.getFluidStack().getFluid() != GCFluids.OIL.getFluid())
        {
            this.oilTank.setFluid(FluidStack.create(GCFluids.OIL.getFluid(), this.oilTank.getFluidAmount()));
        }


        if (nbt.contains("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompound("fuelTank"));
        }
        if (this.fuelTank.getFluidStack() != FluidStack.empty() && this.fuelTank.getFluidStack().getFluid() != GCFluids.FUEL.getFluid())
        {
            this.fuelTank.setFluid(FluidStack.create(GCFluids.FUEL.getFluid(), this.fuelTank.getFluidAmount()));
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("smeltingTicks", this.processTicks);

        if (this.oilTank.getFluidStack() != FluidStack.empty())
        {
            nbt.put("oilTank", this.oilTank.writeToNBT(new CompoundTag()));
        }

        if (this.fuelTank.getFluidStack() != FluidStack.empty())
        {
            nbt.put("fuelTank", this.fuelTank.writeToNBT(new CompoundTag()));
        }
        return nbt;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0, 1, 2};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (!itemstack.isEmpty() && this.canPlaceItem(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return ItemElectricBase.isElectricItemCharged(itemstack);
            case 1:
                return FluidUtil.isOilContainerAny(itemstack);
            case 2:
                return FluidUtil.isPartialContainer(itemstack, GCItems.fuelCanister);
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (!itemstack.isEmpty() && this.canPlaceItem(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return ItemElectricBase.isElectricItemEmpty(itemstack) || !this.shouldPullEnergy();
            case 1:
                return FluidUtil.isEmptyContainer(itemstack);
            case 2:
                return FluidUtil.isFullContainer(itemstack);
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemstack)
    {
        switch (slotID)
        {
        case 0:
            return !itemstack.isEmpty() && ItemElectricBase.isElectricItem(itemstack.getItem());
        case 1:
        case 2:
            return FluidUtil.isValidContainer(itemstack);
        }

        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.canProcess();
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return Direction.UP;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.level.getBlockState(getBlockPos());
        if (state.getBlock() instanceof BlockRefinery)
        {
            return state.getValue(BlockRefinery.FACING);
        }
        return Direction.NORTH;
    }

    private Direction getOilPipe()
    {
        return getFront().getClockWise();
    }

    private Direction getFuelPipe()
    {
        return getFront().getCounterClockWise();
    }

    @Override
    public boolean canDrain(Direction from, Fluid fluid)
    {
        if (from == getFuelPipe())
        {
            return this.fuelTank.getFluidStack() != FluidStack.empty() && this.fuelTank.getFluidAmount() > 0;
        }

        return false;
    }

    @Override
    public FluidStack drain(Direction from, FluidStack resource, ActionType action)
    {
        if (from == getFuelPipe() && resource != null)
        {
            return this.fuelTank.drain(resource.getAmount(), action);
        }

        return null;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, ActionType action)
    {
        if (from == getFuelPipe())
        {
            return this.drain(from, FluidStack.create(GCFluids.FUEL.getFluid(), maxDrain), action);
        }

        return null;
    }

    @Override
    public boolean canFill(Direction from, Fluid fluid)
    {
        if (from == getOilPipe())
        {
            return this.oilTank.getFluidStack().isEmpty() || this.oilTank.getFluidAmount() < this.oilTank.getCapacity();
        }

        return false;
    }

    @Override
    public int fill(Direction from, FluidStack resource, ActionType action)
    {
        int used = 0;

        if (from == getOilPipe() && resource != null)
        {
            final ResourceLocation liquidName = resource.getFluid().getRegistryName();

            if (FluidUtil.testOil(liquidName))
            {
                if (liquidName.equals(GCFluids.OIL.getFluid().getRegistryName()))
                {
                    used = this.oilTank.fill(resource, action);
                }
                else
                {
                    used = this.oilTank.fill(FluidStack.create(GCFluids.OIL.getFluid(), resource.getAmount()), action);
                }
            }
        }

        return used;
    }

    @Override
    public int getTanks()
    {
        return 2;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        switch (tank)
        {
        case 0:
            return this.oilTank.getFluidStack();
        case 1:
            return this.fuelTank.getFluidStack();
        }
        return FluidStack.empty();
    }

    @Override
    public int getTankCapacity(int tank)
    {
        switch (tank)
        {
        case 0:
            return this.oilTank.getCapacity();
        case 1:
            return this.fuelTank.getCapacity();
        }
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack)
    {
        switch (tank)
        {
        case 0:
            return this.oilTank.isFluidValid(stack);
        case 1:
            return this.fuelTank.isFluidValid(stack);
        }
        return false;
    }

    //    @Override
//    public FluidTankInfo[] getTankInfo(Direction from)
//    {
//        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};
//
//        if (from == getOilPipe())
//        {
//            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.oilTank) };
//        }
//        else if (from == getFuelPipe())
//        {
//            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.fuelTank) };
//        }
//
//        return tankInfo;
//    }
//
//    @Override
//    public boolean hasCapability(Capability<?> capability, @Nullable Direction facing)
//    {
//        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
//    }

    private LazyOptional<IFluidHandler> holder = null;

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            if (holder == null)
            {
                holder = LazyOptional.create(new NonNullSupplier<IFluidHandler>()
                {
                    @NotNull
                    @Override
                    public IFluidHandler get()
                    {
                        return new FluidHandlerWrapper(TileEntityRefinery.this, facing);
                    }
                });
            }
            return holder.cast();
        }
        return super.getCapability(capability, facing);
    }

//    @Nullable
//    @Override
//    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
//    {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
//        {
//            return (T) new FluidHandlerWrapper(this, facing);
//        }
//        return super.getCapability(capability, facing);
//    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        if (direction == null)
        {
            return false;
        }
        if (type == NetworkType.POWER)
        {
            return direction == this.getElectricInputDirection();
        }
        if (type == NetworkType.FLUID)
        {
            Direction pipeSide = getFuelPipe();
            return direction == pipeSide || direction == pipeSide.getOpposite();
        }
        return false;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerRefinery(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.refinery");
    }
}
