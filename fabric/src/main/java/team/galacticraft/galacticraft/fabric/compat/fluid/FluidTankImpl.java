package team.galacticraft.galacticraft.fabric.compat.fluid;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;

public class FluidTankImpl extends SimpleFixedFluidInv {
    public FluidTankImpl(int invSize, FluidAmount tankCapacity) {
        super(invSize, tankCapacity);
    }

    public int size() {
        return getTankCount();
    }

    public
}
