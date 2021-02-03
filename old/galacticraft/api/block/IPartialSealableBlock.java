package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public interface IPartialSealableBlock
{
    boolean isSealed(Level world, BlockPos pos, Direction direction);
}
