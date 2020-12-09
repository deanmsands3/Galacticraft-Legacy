package team.galacticraft.galacticraft.common.core.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

public class EventLandingPadRemoval extends Event
{
    public boolean allow = true;
    public final BlockPos pos;
    public final Level world;

    public EventLandingPadRemoval(Level world, BlockPos pos)
    {
        this.world = world;
        this.pos = pos;
    }
}
