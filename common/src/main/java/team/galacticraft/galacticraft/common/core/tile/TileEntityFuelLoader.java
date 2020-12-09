package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.entity.IFuelable;
import team.galacticraft.galacticraft.common.api.tile.ILandingPadAttachable;
import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.core.GCBlockNames;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.blocks.BlockFuelLoader;
import team.galacticraft.galacticraft.core.GCItems;
import team.galacticraft.galacticraft.core.energy.item.ItemElectricBase;
import team.galacticraft.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import team.galacticraft.galacticraft.core.fluid.GCFluids;
import team.galacticraft.galacticraft.core.inventory.ContainerFuelLoader;
import team.galacticraft.galacticraft.core.util.FluidUtil;
import team.galacticraft.galacticraft.core.wrappers.FluidHandlerWrapper;
import team.galacticraft.galacticraft.core.wrappers.IFluidHandlerWrapper;
import team.galacticraft.galacticraft.core.Annotations.NetworkedField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;
import FluidStack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityFuelLoader extends TileBaseElectricBlockWithInventory implements WorldlyContainer, IFluidHandlerWrapper, ILandingPadAttachable, IMachineSides, MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.fuelLoader)
    public static BlockEntityType<TileEntityFuelLoader> TYPE;

    private final int tankCapacity = 12000;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public FluidTank fuelTank = new FluidTank(this.tankCapacity);
    public IFuelable attachedFuelable;
    private boolean loadedFuelLastTick = false;

    public TileEntityFuelLoader()
    {
        super(TYPE);
        this.storage.setMaxExtract(30);
        this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.fuelTank.getFluid() == FluidStack.EMPTY ? 0 : this.fuelTank.getFluid().getAmount();

        return (int) (fuelLevel * i / this.tankCapacity);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.level.isClientSide)
        {
            this.loadedFuelLastTick = false;

            final FluidStack liquidContained = FluidUtil.getFluidContained(this.getInventory().get(1));
            if (FluidUtil.isFuel(liquidContained))
            {
                FluidUtil.loadFromContainer(this.fuelTank, GCFluids.FUEL.getFluid(), this.getInventory(), 1, liquidContained.getAmount());
            }

            if (this.ticks % 100 == 0)
            {
                this.attachedFuelable = null;

                BlockVec3 thisVec = new BlockVec3(this);
                for (final Direction dir : Direction.values())
                {
                    final BlockEntity pad = thisVec.getTileEntityOnSide(this.level, dir);

                    if (pad instanceof TileEntityFake)
                    {
                        final BlockEntity mainTile = ((TileEntityFake) pad).getMainBlockTile();

                        if (mainTile instanceof IFuelable)
                        {
                            this.attachedFuelable = (IFuelable) mainTile;
                            break;
                        }
                    }
                    else if (pad instanceof IFuelable)
                    {
                        this.attachedFuelable = (IFuelable) pad;
                        break;
                    }
                }

            }

            if (this.fuelTank != null && this.fuelTank.getFluid() != FluidStack.EMPTY && this.fuelTank.getFluid().getAmount() > 0)
            {
                final FluidStack liquid = new FluidStack(GCFluids.FUEL.getFluid(), 2);

                if (this.attachedFuelable != null && this.hasEnoughEnergyToRun && !this.disabled)
                {
                    int filled = this.attachedFuelable.addFuel(liquid, ActionType.EXECUTE);
                    this.loadedFuelLastTick = filled > 0;
                    this.fuelTank.drain(filled, ActionType.EXECUTE);
                }
            }
        }
    }

    @Override
    public void load(CompoundTag par1NBTTagCompound)
    {
        super.load(par1NBTTagCompound);

        if (par1NBTTagCompound.contains("fuelTank"))
        {
            this.fuelTank.readFromNBT(par1NBTTagCompound.getCompound("fuelTank"));
        }

        this.readMachineSidesFromNBT(par1NBTTagCompound);  //Needed by IMachineSides
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);

        if (this.fuelTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("fuelTank", this.fuelTank.writeToNBT(new CompoundTag()));
        }

        this.addMachineSidesToNBT(nbt);  //Needed by IMachineSides

        return nbt;
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        return this.canPlaceItem(slotID, itemstack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (slotID == 1 && !itemstack.isEmpty())
        {
            return FluidUtil.isEmptyContainer(itemstack);
        }
        return false;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemstack)
    {
        return (slotID == 1 && itemstack != null && itemstack.getItem() == GCItems.fuelCanister) || (slotID == 0 && ItemElectricBase.isElectricItem(itemstack.getItem()));
    }

    @Override
    public boolean canDrain(Direction from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidStack drain(Direction from, FluidStack resource, ActionType action)
    {
        return null;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, ActionType action)
    {
        return null;
    }

    @Override
    public boolean canFill(Direction from, Fluid fluid)
    {
        if (this.getPipeInputDirection().equals(from))
        {
            return this.fuelTank.getFluid() == FluidStack.EMPTY || this.fuelTank.getFluidAmount() < this.fuelTank.getCapacity();
        }
        return false;
    }

    @Override
    public int fill(Direction from, FluidStack resource, ActionType action)
    {
        int used = 0;

        if (this.getPipeInputDirection().equals(from) && resource != null)
        {
            if (FluidUtil.testFuel(resource.getFluid().getRegistryName()))
            {
                used = this.fuelTank.fill(resource, action);
            }
        }

        return used;
    }

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from)
//    {
//        if (this.getPipeInputDirection().equals(from))
//        {
//            return new FluidTankInfo[] { new FluidTankInfo(this.fuelTank) };
//        }
//        return null;
//    }


    @Override
    public int getTanks()
    {
        return this.fuelTank.getTanks();
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return this.fuelTank.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return this.fuelTank.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack)
    {
        return this.fuelTank.isFluidValid(tank, stack);
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.fuelTank.getFluid() != FluidStack.EMPTY && this.fuelTank.getFluid().getAmount() > 0 && !this.getDisabled(0) && loadedFuelLastTick;
    }

    @Override
    public boolean canAttachToLandingPad(LevelReader world, BlockPos pos)
    {
        return true;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.level.getBlockState(getBlockPos());
        if (state.getBlock() instanceof BlockFuelLoader)
        {
            return state.getValue(BlockFuelLoader.FACING);
        }
        return Direction.NORTH;
    }

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
                holder = LazyOptional.of(new NonNullSupplier<IFluidHandler>()
                {
                    @NotNull
                    @Override
                    public IFluidHandler get()
                    {
                        return new FluidHandlerWrapper(TileEntityFuelLoader.this, facing);
                    }
                });
            }
            return holder.cast();
        }
        return super.getCapability(capability, facing);
    }

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
            return direction == this.getPipeInputDirection();
        }
        return false;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_IN))
        {
        case RIGHT:
            return getFront().getCounterClockWise();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return Direction.UP;
        case BOTTOM:
            return Direction.DOWN;
        case LEFT:
        default:
            return getFront().getClockWise();
        }
    }

    @Override
    public Direction getPipeInputDirection()
    {
        switch (this.getSide(MachineSide.PIPE_IN))
        {
        case RIGHT:
        default:
            return getFront().getCounterClockWise();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return Direction.UP;
        case BOTTOM:
            return Direction.DOWN;
        case LEFT:
            return getFront().getClockWise();
        }
    }

    //------------------
    //Added these methods and field to implement IMachineSides properly 
    //------------------
    @Override
    public MachineSide[] listConfigurableSides()
    {
        return new MachineSide[]{MachineSide.ELECTRIC_IN, MachineSide.PIPE_IN};
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[]{Face.LEFT, Face.RIGHT};
    }

    private MachineSidePack[] machineSides;

    @Override
    public synchronized MachineSidePack[] getAllMachineSides()
    {
        if (this.machineSides == null)
        {
            this.initialiseSides();
        }

        return this.machineSides;
    }

    @Override
    public void setupMachineSides(int length)
    {
        this.machineSides = new MachineSidePack[length];
    }

    @Override
    public void onLoad()
    {
        this.clientOnLoad();
    }

    @Override
    public IMachineSidesProperties getConfigurationType()
    {
        return IMachineSidesProperties.TWOFACES_HORIZ;
    }
    //------------------END OF IMachineSides implementation

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerFuelLoader(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.fuel_loader");
    }
}
