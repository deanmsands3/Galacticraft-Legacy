package team.galacticraft.galacticraft.fabric.compat.fluid;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.filter.ConstantFluidFilter;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;

public class FluidTankImpl extends SimpleFixedFluidInv implements FluidTank
{
    private final Int2ObjectMap<Object2BooleanFunction<FluidStack>> validMap;

    public FluidTankImpl(int invSize, FluidAmount tankCapacity)
    {
        this(invSize, tankCapacity, new Int2ObjectArrayMap<>());
    }

    public FluidTankImpl(int invSize, FluidAmount tankCapacity, Int2ObjectMap<Object2BooleanFunction<FluidStack>> allowed)
    {
        super(invSize, tankCapacity);
        this.validMap = allowed;
    }

    /**
     * Returns the number of internal tanks in this tank
     *
     * @return The number of internal tanks in this tank
     */
    @Override
    public int size()
    {
        return this.getTankCount();
    }

    /**
     * Returns the fluid in the tank
     *
     * @param tank The index of the tank to get the fluid from
     * @return The amount of fluid that is inside the tank
     */
    @Override
    public @NotNull FluidStack get(int tank)
    {
        return FluidUtilFabric.toVolumeA(this.getTank(tank).get());
    }

    /**
     * Extracts fluid from a tank
     *
     * @param amount The amount of fluid to extract
     * @param action The action type
     * @return The amount of fluid that was successfully extracted
     */
    @Override
    public FluidStack extract(Fraction amount, ActionType action)
    {
        return FluidUtilFabric.toVolumeA(this.attemptExtraction(ConstantFluidFilter.ANYTHING, FluidUtilFabric.toAmountLBA(amount), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION));
    }

    /**
     * Extracts fluid from a tank
     *
     * @param tank   The tank to extract the fluid from
     * @param amount The amount of fluid to extract
     * @param action The action type
     * @return The amount of fluid that was successfully extracted
     */
    @Override
    public FluidStack extract(int tank, Fraction amount, ActionType action)
    {
        return FluidUtilFabric.toVolumeA(this.getTank(tank).attemptExtraction(ConstantFluidFilter.ANYTHING, FluidUtilFabric.toAmountLBA(amount), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION));
    }

    /**
     * Extracts fluid from a tank
     *
     * @param stack  The fluid stack to extract
     * @param action The action type
     * @return The amount of fluid that was successfully extracted
     */
    @Override
    public FluidStack extract(FluidStack stack, ActionType action)
    {
        return FluidUtilFabric.toVolumeA(this.attemptExtraction(fluidKey -> stack.getFluid().equals(fluidKey.getRawFluid()), FluidUtilFabric.toAmountLBA(stack.getAmount()), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION));
    }

    /**
     * Inserts fluid into the tank
     *
     * @param tank   The tank to insert the fluid into
     * @param stack  The fluid stack to insert
     * @param action The action type
     * @return The amount of fluid that was NOT inserted
     */
    @Override
    public FluidStack insert(int tank, FluidStack stack, ActionType action)
    {
        return FluidUtilFabric.toVolumeA(this.getTank(tank).attemptInsertion(FluidUtilFabric.toVolumeLBA(stack), action == ActionType.SIMULATE ? Simulation.SIMULATE : Simulation.ACTION));
    }

    @Override
    public boolean isValid(int tank, FluidStack stack)
    {
        return validMap.getOrDefault(tank, s -> true).getBoolean(stack);
    }

    /**
     * Deserializes data from a tag into this fluid tank
     *
     * @param tag The tag to read data from
     */
    @Override
    public void fromTag(@NotNull CompoundTag tag)
    {
        super.fromTag(tag);
    }

    /**
     * Serializes this fluid tank into NBT form
     *
     * @param tag The tag serialize the data onto
     */
    @Override
    public @NotNull CompoundTag toTag(@NotNull CompoundTag tag)
    {
        return super.toTag(tag);
    }
}
