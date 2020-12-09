package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.transmission.grid.IGridNetwork;
import team.galacticraft.galacticraft.common.api.transmission.tile.IBufferTransmitter;
import team.galacticraft.galacticraft.common.api.transmission.tile.INetworkProvider;
import team.galacticraft.galacticraft.common.api.transmission.tile.ITransmitter;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.core.blocks.BlockFluidPipe;
import team.galacticraft.galacticraft.core.fluid.FluidNetwork;
import team.galacticraft.galacticraft.core.tick.TickHandlerServer;
import team.galacticraft.galacticraft.core.util.OxygenUtil;
import team.galacticraft.galacticraft.core.wrappers.FluidHandlerWrapper;
import team.galacticraft.galacticraft.core.wrappers.IFluidHandlerWrapper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import me.shedaniel.architectury.fluid.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileEntityFluidTransmitter extends TileEntityAdvanced implements IBufferTransmitter<FluidStack>, IFluidHandlerWrapper
{
    private IGridNetwork network;
    public BlockEntity[] adjacentConnections = null;
    private final int pullAmount;
    private boolean validated = true;

    public TileEntityFluidTransmitter(BlockEntityType<?> type, int pullAmount)
    {
        super(type);
        this.pullAmount = pullAmount;
    }

    @Override
    public void clearRemoved()
    {
        this.validated = false;
        super.clearRemoved();
    }

    @Override
    public void setRemoved()
    {
        if (!BlockFluidPipe.ignoreDrop)
        {
            this.getNetwork().split(this);
        }
//        else
//        {
//            this.setNetwork(null);
//        }

        super.setRemoved();
    }

    @Override
    public void onChunkUnloaded()
    {
        super.setRemoved();
        super.onChunkUnloaded();
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return false;
//    }

    @Override
    public IGridNetwork getNetwork()
    {
        if (this.network == null)
        {
            this.resetNetwork();
        }

        return this.network;
    }

    @Override
    public boolean hasNetwork()
    {
        return this.network != null;
    }

    @Override
    public void onNetworkChanged()
    {
        level.setBlocksDirty(this.getBlockPos(), this.getBlockState().getBlock().defaultBlockState(), this.getBlockState()); // Forces block render update. Better way to do this?
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide)
        {
            if (!this.validated)
            {
                TickHandlerServer.oxygenTransmitterUpdates.add(this);
                this.validated = true;
            }

            Block blockType = this.getBlockState().getBlock();
            if (blockType instanceof BlockFluidPipe && ((BlockFluidPipe) blockType).getMode() == BlockFluidPipe.EnumPipeMode.PULL)
            {
                BlockEntity[] tiles = OxygenUtil.getAdjacentFluidConnections(this);

                for (Direction side : Direction.values())
                {
                    BlockEntity sideTile = tiles[side.ordinal()];

                    if (sideTile != null && !(sideTile instanceof IBufferTransmitter))
                    {
                        IFluidHandler handler = sideTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()).orElse(null);

                        if (handler != null)
                        {
                            FluidStack received = handler.drain(this.pullAmount, ActionType.SIMULATE);

                            if (received != FluidStack.EMPTY && received.getAmount() != 0)
                            {
                                handler.drain(this.fill(Direction.DOWN, received, ActionType.EXECUTE), ActionType.EXECUTE);
                            }
                        }
                    }
                }
            }
        }

        super.tick();
    }

    protected void resetNetwork()
    {
        FluidNetwork network = new FluidNetwork();
        network.addTransmitter(this);
        network.register();
        this.setNetwork(network);
    }

    @Override
    public void setNetwork(IGridNetwork network)
    {
        if (this.network == network)
        {
            return;
        }

        if (this.level.isClientSide && this.network != null)
        {
            FluidNetwork fluidNetwork = (FluidNetwork) this.network;
            fluidNetwork.removeTransmitter(this);

            if (fluidNetwork.getTransmitters().isEmpty())
            {
                fluidNetwork.unregister();
            }
        }

        this.network = network;

        if (this.level.isClientSide && this.network != null)
        {
            ((FluidNetwork) this.network).pipes.add(this);
        }
    }

    @Override
    public void refresh()
    {
        if (!this.level.isClientSide)
        {
            this.adjacentConnections = null;

            BlockVec3 thisVec = new BlockVec3(this);
            for (Direction side : Direction.values())
            {
                BlockEntity tileEntity = thisVec.getTileEntityOnSide(this.level, side);

                if (tileEntity != null)
                {
                    if (tileEntity.getClass() == this.getClass() && tileEntity instanceof INetworkProvider && ((INetworkProvider) tileEntity).hasNetwork())
                    {
                        if (!(tileEntity instanceof ITransmitter) || (((ITransmitter) tileEntity).canConnect(side.getOpposite(), ((ITransmitter) tileEntity).getNetworkType())))
                        {
                            if (!this.hasNetwork())
                            {
                                this.setNetwork(((INetworkProvider) tileEntity).getNetwork());
                                ((FluidNetwork) this.getNetwork()).addTransmitter(this);
                                ((FluidNetwork) this.getNetwork()).onTransmitterAdded(this);
                            }
                            else if (this.hasNetwork() && !this.getNetwork().equals(((INetworkProvider) tileEntity).getNetwork()))
                            {
                                this.setNetwork((IGridNetwork) this.getNetwork().merge(((INetworkProvider) tileEntity).getNetwork()));
                            }
                        }
                    }
                }
            }

            this.getNetwork().refresh();
        }
    }

    @Override
    public BlockEntity[] getAdjacentConnections()
    {
        /**
         * Cache the adjacentConnections.
         */
        if (this.adjacentConnections == null)
        {
            this.adjacentConnections = OxygenUtil.getAdjacentFluidConnections(this, this.level.isClientSide);
        }

        return this.adjacentConnections;
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        return type == NetworkType.FLUID;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public AABB getRenderBoundingBox()
    {
        return new AABB(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), this.getBlockPos().getX() + 1, this.getBlockPos().getY() + 1, this.getBlockPos().getZ() + 1);
    }

    @Override
    public NetworkType getNetworkType()
    {
        return NetworkType.FLUID;
    }

    @Override
    public double getPacketRange()
    {
        return 0;
    }

    @Override
    public int getPacketCooldown()
    {
        return 0;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }

//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public int receiveGas(Direction side, GasStack stack, boolean doTransfer)
//    {
//        if (!stack.getGas().getName().equals("oxygen"))
//        {
//            return 0;
//        }
//        return stack.amount - (int) Math.floor(((IOxygenNetwork) this.getNetwork()).produce(stack.amount, this));
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public int receiveGas(Direction side, GasStack stack)
//    {
//        return this.receiveGas(LogicalSide, stack, true);
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public GasStack drawGas(Direction side, int amount, boolean doTransfer)
//    {
//        return null;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public GasStack drawGas(Direction side, int amount)
//    {
//        return null;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public boolean canDrawGas(Direction side, Gas type)
//    {
//        return false;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public boolean canReceiveGas(Direction side, Gas type)
//    {
//        return type.getName().equals("oxygen");
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = CompatibilityManager.modidMekanism)
//    public boolean canTubeConnect(Direction side)
//    {
//        return this.canConnect(side, NetworkType.FLUID);
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
                holder = LazyOptional.of(new NonNullSupplier<IFluidHandler>()
                {
                    @NotNull
                    @Override
                    public IFluidHandler get()
                    {
                        return new FluidHandlerWrapper(TileEntityFluidTransmitter.this, facing);
                    }
                });
            }
            return holder.cast();
        }
        return super.getCapability(capability, facing);
    }
}
