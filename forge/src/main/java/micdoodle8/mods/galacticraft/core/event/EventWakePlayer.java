package micdoodle8.mods.galacticraft.core.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class EventWakePlayer extends PlayerEvent
{
    public Player.BedSleepingProblem result = null;
    public final BlockPos pos;
    public final boolean immediately;
    public final boolean updateWorld;
    public final boolean setSpawn;
    public final boolean bypassed;

    public EventWakePlayer(Player player, BlockPos pos, boolean immediately, boolean updateWorld, boolean setSpawn, boolean bypassed)
    {
        super(player);
        this.pos = pos;
        this.immediately = immediately;
        this.updateWorld = updateWorld;
        this.setSpawn = setSpawn;
        this.bypassed = bypassed;
    }
}
