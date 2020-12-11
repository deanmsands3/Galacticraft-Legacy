package team.galacticraft.galacticraft.common.core.tile;

import java.util.LinkedList;
import java.util.List;

import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.blocks.BlockMulti;
import team.galacticraft.galacticraft.common.core.blocks.BlockMulti.EnumBlockMultiType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntitySpaceStationBase extends TileEntityFake implements IMultiBlock
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.spaceStationBase)
    public static BlockEntityType<TileEntitySpaceStationBase> TYPE;

    public TileEntitySpaceStationBase()
    {
        super(TYPE);
    }

    private boolean initialised;

    @Override
    public void tick()
    {
        if (!this.initialised)
        {
            this.initialised = this.initialiseMultiTiles(this.getBlockPos(), this.level);
        }
    }

    @Override
    public InteractionResult onActivated(Player entityPlayer)
    {
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onCreate(Level world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.setChanged();

        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(placedPosition, positions);
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public EnumBlockMultiType getMultiType()
    {
        return EnumBlockMultiType.SPACE_STATION_BASE;
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.level.getMaxBuildHeight() - 1;

        for (int y = 1; y < 3; y++)
        {
            if (placedPosition.getY() + y > buildHeight)
            {
                return;
            }
            positions.add(new BlockPos(placedPosition.getX(), placedPosition.getY() + y, placedPosition.getZ()));
        }
    }

    @Override
    public void onDestroy(BlockEntity callingBlock)
    {
        final BlockPos thisBlock = getBlockPos();
        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            BlockState stateAt = this.level.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.fakeBlock && stateAt.getValue(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.SPACE_STATION_BASE)
            {
                this.level.removeBlock(pos, false);
            }
        }
        this.level.destroyBlock(this.getBlockPos(), false);
    }
}
