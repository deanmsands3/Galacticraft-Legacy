package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlockNames;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;

public class TileEntityMinerBaseSingle extends BlockEntity implements TickableBlockEntity
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + AsteroidBlockNames.blockMinerBase)
    public static BlockEntityType<TileEntityMinerBaseSingle> TYPE;

    private int corner = 0;

    public TileEntityMinerBaseSingle()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide && this.corner == 0)
        {
            final ArrayList<BlockEntity> attachedBaseBlocks = new ArrayList<BlockEntity>();

            final int thisX = this.getBlockPos().getX();
            final int thisY = this.getBlockPos().getY();
            final int thisZ = this.getBlockPos().getZ();

            boolean success = true;
            SEARCH:
            for (int x = 0; x < 2; x++)
            {
                for (int y = 0; y < 2; y++)
                {
                    for (int z = 0; z < 2; z++)
                    {
                        BlockPos pos = new BlockPos(x + thisX, y + thisY, z + thisZ);
                        final BlockEntity tile = this.level.hasChunkAt(pos) ? this.level.getBlockEntity(pos) : null;

                        if (tile instanceof TileEntityMinerBaseSingle && !tile.isRemoved() && ((TileEntityMinerBaseSingle) tile).corner == 0)
                        {
                            attachedBaseBlocks.add(tile);
                        }
                        else
                        {
                            success = false;
                            break SEARCH;
                        }
                    }
                }
            }

            if (success)
            {
                TileEntityMinerBase.addNewMinerBase(GCCoreUtil.getDimensionType(this), this.getBlockPos());
                for (final BlockEntity tile : attachedBaseBlocks)
                {
                    ((TileEntityMinerBaseSingle) tile).corner = 1;
                    this.level.removeBlock(this.getBlockPos(), false);
                }
                //Don't try setting a new block with a TileEntity, because new tiles can
                //get removed after the end of this tileEntity.tick() tick - setting a new block
                //here would automatically remove this tile.
                //
                //(It's because if this tileEntity is now invalid, World.updateEntities() removes
                // *any* tileEntity at this position - see call to Chunk.removeTileEntity(pos)nee!)
                //
                //Equally if any others of these TileEntityMinerBaseSingle are ticked AFTER this
                //in the same server tick tick, then those new ones will also be removed
                //because their TileEntityMinerBaseSingle is now, inevitably, invalid!
            }
        }
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }
}
