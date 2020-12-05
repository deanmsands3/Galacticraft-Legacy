package micdoodle8.mods.galacticraft.api.inventory;

import java.lang.reflect.Method;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

/**
 * A static method for other mods to access the Galacticraft
 * extended inventory.
 * <p>
 * Call: AccessInventoryGC.getGCInventoryForPlayer(player)
 */
public class AccessInventoryGC
{
    private static Class<?> playerStatsClass;
    private static Method getStats;
    private static Method getExtendedInventory;

    public static IInventoryGC getGCInventoryForPlayer(ServerPlayer player)
    {
        try
        {
            if (playerStatsClass == null || getStats == null || getExtendedInventory == null)
            {
                playerStatsClass = Class.forName("micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats");
                getStats = playerStatsClass.getMethod("get", Entity.class);
                getExtendedInventory = playerStatsClass.getMethod("getExtendedInventory");
            }

            Object stats = getStats.invoke(null, player);
            if (stats == null)
            {
                return null;
            }
            return (IInventoryGC) getExtendedInventory.invoke(stats);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
