package micdoodle8.mods.galacticraft.api.event.wgen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

import java.util.Random;

/**
 * Event is thrown when a chunk is populated on planets.
 * <p/>
 * If you're adding your own dimensions, make sure you post these two events to
 * the forge event bus when decorating your planet/moon
 */
public class GCCoreEventPopulate extends Event
{
    public final Level world;
    public final Random rand;
    public final BlockPos pos;

    public GCCoreEventPopulate(Level world, Random rand, BlockPos pos)
    {
        this.world = world;
        this.rand = rand;
        this.pos = pos;
    }

    public static class Pre extends GCCoreEventPopulate
    {
        public Pre(Level world, Random rand, BlockPos pos)
        {
            super(world, rand, pos);
        }
    }

    public static class Post extends GCCoreEventPopulate
    {
        public Post(Level world, Random rand, BlockPos pos)
        {
            super(world, rand, pos);
        }
    }
}
