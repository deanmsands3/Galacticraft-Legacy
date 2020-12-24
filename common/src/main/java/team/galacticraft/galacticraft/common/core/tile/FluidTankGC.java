package team.galacticraft.galacticraft.common.core.tile;

import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import me.shedaniel.architectury.fluid.FluidStack;
import org.jetbrains.annotations.NotNull;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;

public class FluidTankGC implements FluidTank
{
    protected BlockEntity tile;
    private final FluidTank tank;

    public FluidTankGC(Fraction capacity, BlockEntity tile)
    {
        this.tile = tile;
        this.tank = PlatformSpecific.createFluidInv(1, capacity);
    }

    public FluidTankGC(FluidStack stack, Fraction capacity, BlockEntity tile)
    {
        this(capacity, tile);
        this.setFluid(0, stack, ActionType.PERFORM);
    }

    public BlockPos getTilePosition()
    {
        return this.tile.getBlockPos();
    }

    public BlockEntity getTile()
    {
        return this.tile;
    }

    @Override
    public int size()
    {
        return 1;
    }

    @Override
    public Fraction getCapacity(int tank)
    {
        return this.tank.getCapacity(tank);
    }

    @Override
    public FluidStack getFluidStack(int tank)
    {
        return this.tank.getFluidStack(tank);
    }

    @Override
    public FluidStack extract(int tank, Fraction amount, ActionType action)
    {
        return this.tank.extract(tank, amount, action);
    }

    @Override
    public FluidStack extract(Fraction amount, ActionType action)
    {
        return this.tank.extract(amount, action);
    }

    @Override
    public FluidStack extract(FluidStack stack, ActionType action)
    {
        return this.tank.extract(stack, action);
    }

    @Override
    public FluidStack insert(int tank, FluidStack stack, ActionType action)
    {
        return this.tank.insert(tank, stack, action);
    }

    @Override
    public boolean setFluid(int tank, FluidStack stack, ActionType actionType)
    {
        return this.tank.setFluid(tank, stack, actionType);
    }

    @Override
    public boolean isValid(int tank, FluidStack stack)
    {
        return this.tank.isValid(tank, stack);
    }

    @Override
    public void fromTag(@NotNull CompoundTag tag)
    {
        this.tank.fromTag(tag);
    }

    @Override
    public @NotNull CompoundTag toTag(@NotNull CompoundTag tag)
    {
        return this.tank.toTag(tag);
    }
}
