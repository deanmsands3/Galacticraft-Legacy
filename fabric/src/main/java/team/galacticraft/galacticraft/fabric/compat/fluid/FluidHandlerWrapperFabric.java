package team.galacticraft.galacticraft.fabric.compat.fluid;

import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.FluidExtractable;
import alexiil.mc.lib.attributes.fluid.FluidInsertable;
import alexiil.mc.lib.attributes.fluid.FluidInvTankChangeListener;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import team.galacticraft.galacticraft.common.core.wrappers.FluidHandlerWrapper;
import team.galacticraft.galacticraft.common.core.wrappers.IFluidHandlerWrapper;
import team.galacticraft.galacticraft.fabric.compat.util.Serializable;

public class FluidHandlerWrapperFabric extends FluidHandlerWrapper implements FixedFluidInv, FluidInsertable, FluidExtractable, Serializable
{
    public FluidHandlerWrapperFabric(IFluidHandlerWrapper w, Direction s)
    {
        super(w, s);
    }

    @Override
    public boolean setInvFluid(int tank, FluidVolume to, Simulation simulation)
    {
        return setFluid(tank, FabricFluidUtil.toVolumeA(to), simulation == Simulation.ACTION ? ActionType.PERFORM : ActionType.SIMULATE);
    }

    @Override
    public int getTankCount()
    {
        return this.getTanks();
    }

    @Override
    public FluidVolume getInvFluid(int tank)
    {
        return FabricFluidUtil.toVolumeLBA(this.getFluidInTank(tank));
    }

    @Override
    public boolean isFluidValidForTank(int tank, FluidKey fluid)
    {
        return this.isFluidValid(tank, FabricFluidUtil.toVolumeA(fluid.withAmount(FluidAmount.of(1, 1000))));
    }

    @Override
    public ListenerToken addListener(FluidInvTankChangeListener listener, ListenerRemovalToken removalToken)
    {
        throw new UnsupportedOperationException("NYI"); //todo(marcus): listeners
    }

    @Override
    public FluidVolume attemptInsertion(FluidVolume fluid, Simulation simulation)
    {
        return FabricFluidUtil.toVolumeLBA(this.fill(FabricFluidUtil.toVolumeA(fluid), simulation == Simulation.ACTION ? ActionType.PERFORM : ActionType.SIMULATE));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        return this.serialize(tag);
    }

    @Override
    public void fromTag(CompoundTag tag)
    {
        this.deserialize(tag);
    }
}
