package micdoodle8.mods.galacticraft.core.tile;

import java.util.List;

import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

public interface IMultiBlock extends TickableBlockEntity
{
    /**
     * Called when activated
     */
    InteractionResult onActivated(Player entityPlayer);

    /**
     * Called when this multiblock is created
     *
     * @param placedPosition - The position the block was placed at
     */
    void onCreate(Level world, BlockPos placedPosition);

    /**
     * Called when one of the multiblocks of this block is destroyed
     *
     * @param callingBlock - The tile entity who called the onDestroy function
     */
    void onDestroy(BlockEntity callingBlock);

    void getPositions(BlockPos placedPosition, List<BlockPos> positions);

    BlockMulti.EnumBlockMultiType getMultiType();
}
