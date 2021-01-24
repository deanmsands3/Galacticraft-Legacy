package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCBlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;

import com.google.common.collect.Lists;

public class TileEntityLandingPadSingle extends BlockEntity implements TickableBlockEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.ROCKET_LAUNCH_PAD)
    public static BlockEntityType<TileEntityLandingPadSingle> TYPE;

    private int corner = 0;

    public TileEntityLandingPadSingle()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide && this.corner == 0)
        {
            List<BlockEntity> attachedLaunchPads = Lists.newArrayList();

            for (int x = this.getBlockPos().getX() - 1; x < this.getBlockPos().getX() + 2; x++)
            {
                for (int z = this.getBlockPos().getZ() - 1; z < this.getBlockPos().getZ() + 2; z++)
                {
                    BlockEntity tile = this.level.getBlockEntity(new BlockPos(x, this.getBlockPos().getY(), z));

                    if (tile instanceof TileEntityLandingPadSingle && !tile.isRemoved() && ((TileEntityLandingPadSingle) tile).corner == 0)
                    {
                        attachedLaunchPads.add(tile);
                    }
                }
            }

            if (attachedLaunchPads.size() == 9)
            {
                for (BlockEntity tile : attachedLaunchPads)
                {
                    this.level.removeBlockEntity(tile.getBlockPos());
                    ((TileEntityLandingPadSingle) tile).corner = 1;
                }

                this.level.setBlock(this.getBlockPos(), GCBlocks.FULL_ROCKET_LAUNCH_PAD.defaultBlockState(), 2);
            }
        }
    }
}