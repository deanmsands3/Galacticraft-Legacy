package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;

public class TileEntityLandingPadSingle extends BlockEntity implements TickableBlockEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.landingPad)
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
            final ArrayList<BlockEntity> attachedLaunchPads = new ArrayList<>();

            for (int x = this.getBlockPos().getX() - 1; x < this.getBlockPos().getX() + 2; x++)
            {
                for (int z = this.getBlockPos().getZ() - 1; z < this.getBlockPos().getZ() + 2; z++)
                {
                    final BlockEntity tile = this.level.getBlockEntity(new BlockPos(x, this.getBlockPos().getY(), z));

                    if (tile instanceof TileEntityLandingPadSingle && !tile.isRemoved() && ((TileEntityLandingPadSingle) tile).corner == 0)
                    {
                        attachedLaunchPads.add(tile);
                    }
                }
            }

            if (attachedLaunchPads.size() == 9)
            {
                for (final BlockEntity tile : attachedLaunchPads)
                {
                    this.level.removeBlockEntity(tile.getBlockPos());
                    ((TileEntityLandingPadSingle) tile).corner = 1;
                }

                this.level.setBlock(this.getBlockPos(), GCBlocks.landingPadFull.defaultBlockState(), 2);
            }
        }
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }


}
