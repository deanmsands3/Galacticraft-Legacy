package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlockNames;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidPipe;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class WalkwayFluidPipeTileEntity extends TileEntityFluidPipe
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + GCBlockNames.FLUID_PIPE)
    public static TileEntityType<TileEntityFluidPipe> TYPE;

    public WalkwayFluidPipeTileEntity()
    {
        super(TYPE);
    }
}