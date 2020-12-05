package team.galacticraft.galacticraft.common.api.recipe;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

/**
 * These events are used internally to perform actions when Galacticraft is
 * installed, without needing to include unnecessary classes. There is no need
 * to subscribe to these events.
 */
public abstract class SchematicEvent extends Event
{
    public ISchematicPage page;

    public SchematicEvent(ISchematicPage page)
    {
        this.page = page;
    }

    public static class Unlock extends SchematicEvent
    {
        public ServerPlayer player;

        public Unlock(ServerPlayer player, ISchematicPage page)
        {
            super(page);
            this.player = player;
        }
    }

    public static class FlipPage extends SchematicEvent
    {
        public int index;
        public int direction;
        public Screen currentGui;

        public FlipPage(Screen cs, ISchematicPage page, int index, int direction)
        {
            super(page);
            this.currentGui = cs;
            this.index = index;
            this.direction = direction;
        }
    }
}
