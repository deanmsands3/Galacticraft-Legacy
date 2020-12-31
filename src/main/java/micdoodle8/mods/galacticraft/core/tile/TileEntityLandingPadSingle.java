package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCBlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;

import com.google.common.collect.Lists;

public class TileEntityLandingPadSingle extends TileEntity implements ITickableTileEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.ROCKET_LAUNCH_PAD)
    public static TileEntityType<TileEntityLandingPadSingle> TYPE;

    private int corner = 0;

    public TileEntityLandingPadSingle()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.world.isRemote && this.corner == 0)
        {
            List<TileEntity> attachedLaunchPads = Lists.newArrayList();

            for (int x = this.getPos().getX() - 1; x < this.getPos().getX() + 2; x++)
            {
                for (int z = this.getPos().getZ() - 1; z < this.getPos().getZ() + 2; z++)
                {
                    TileEntity tile = this.world.getTileEntity(new BlockPos(x, this.getPos().getY(), z));

                    if (tile instanceof TileEntityLandingPadSingle && !tile.isRemoved() && ((TileEntityLandingPadSingle) tile).corner == 0)
                    {
                        attachedLaunchPads.add(tile);
                    }
                }
            }

            if (attachedLaunchPads.size() == 9)
            {
                for (TileEntity tile : attachedLaunchPads)
                {
                    this.world.removeTileEntity(tile.getPos());
                    ((TileEntityLandingPadSingle) tile).corner = 1;
                }

                this.world.setBlockState(this.getPos(), GCBlocks.FULL_ROCKET_LAUNCH_PAD.getDefaultState(), 2);
            }
        }
    }
}