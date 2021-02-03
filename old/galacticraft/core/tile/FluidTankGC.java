package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidTankGC extends FluidTank
{
    protected BlockEntity tile;

    public FluidTankGC(int capacity, BlockEntity tile)
    {
        super(capacity);
        this.tile = tile;
    }

    public FluidTankGC(FluidStack stack, int capacity, BlockEntity tile)
    {
        this(capacity, tile);
        setFluid(stack);
    }

    public BlockPos getTilePosition()
    {
        return this.tile.getBlockPos();
    }

    public BlockEntity getTile()
    {
        return this.tile;
    }
}
