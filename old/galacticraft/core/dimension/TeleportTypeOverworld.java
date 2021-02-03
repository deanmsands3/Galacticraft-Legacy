package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import java.util.Random;

public class TeleportTypeOverworld implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return true;
    }

    @Override
    public Vector3D getPlayerSpawnLocation(ServerLevel world, ServerPlayer player)
    {
        if (player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            return new Vector3D(stats.getCoordsTeleportedFromX(), 250.0F, stats.getCoordsTeleportedFromZ());
        }

        return null;
    }

    @Override
    public Vector3D getEntitySpawnLocation(ServerLevel world, Entity entity)
    {
        return new Vector3D((float) entity.getX(), 250.0F, (float) entity.getZ());
    }

    @Override
    public Vector3D getParaChestSpawnLocation(ServerLevel world, ServerPlayer player, Random rand)
    {
        final float x = (rand.nextFloat() * 2 - 1.0F) * 5.0F;
        final float z = (rand.nextFloat() * 2 - 1.0F) * 5.0F;

        return new Vector3D((float) player.getX() + x, 230.0F, (float) player.getZ() + z);
    }

    @Override
    public void onSpaceDimensionChanged(Level newWorld, ServerPlayer player, boolean ridingAutoRocket)
    {
    }

    @Override
    public void setupAdventureSpawn(ServerPlayer player)
    {
        // TODO Auto-generated method stub

    }
}
