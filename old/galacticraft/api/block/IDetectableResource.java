package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Implement this interface to let Sensor Goggles see your block.
 */
public interface IDetectableResource
{
    /**
     * @return array of metadata values that are considered valueable.
     */
    boolean isValueable(BlockState metadata);
}
