package team.galacticraft.galacticraft.common.api.entity;

import net.minecraft.core.BlockPos;

/**
 * An entity which requires a hook into landing pad events should implement this interface
 */
public interface ILandable extends IDockable
{
    /**
     * Called when the entity lands on a dock
     *
     * @param pos coordinates of the dock
     */
    void landEntity(BlockPos pos);
}
