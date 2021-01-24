package micdoodle8.mods.galacticraft.planets.mars.dimension;

import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.mars.entities.LandingBalloonsEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import java.util.Random;

public class TeleportTypeMars implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return false;
    }

    @Override
    public Vector3D getPlayerSpawnLocation(ServerLevel world, ServerPlayer player)
    {
        if (player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            double x = stats.getCoordsTeleportedFromX();
            double z = stats.getCoordsTeleportedFromZ();
            int limit = ConfigManagerCore.otherPlanetWorldBorders.get() - 2;
            if (limit > 20)
            {
                if (x > limit)
                {
                    z *= limit / x;
                    x = limit;
                }
                else if (x < -limit)
                {
                    z *= -limit / x;
                    x = -limit;
                }
                if (z > limit)
                {
                    x *= limit / z;
                    z = limit;
                }
                else if (z < -limit)
                {
                    x *= -limit / z;
                    z = -limit;
                }
            }
            return new Vector3D(x, ConfigManagerCore.disableLander.get() ? 250.0 : 900.0, z);
        }

        return null;
    }

    @Override
    public Vector3D getEntitySpawnLocation(ServerLevel world, Entity entity)
    {
        return new Vector3D(entity.getX(), ConfigManagerCore.disableLander.get() ? 250.0 : 900.0, entity.getZ());
    }

    @Override
    public Vector3D getParaChestSpawnLocation(ServerLevel world, ServerPlayer player, Random rand)
    {
        return null;
    }

    @Override
    public void onSpaceDimensionChanged(Level newWorld, ServerPlayer player, boolean ridingAutoRocket)
    {
        if (!ridingAutoRocket && player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);

            if (stats.getTeleportCooldown() <= 0)
            {
                if (player.abilities.flying)
                {
                    player.abilities.flying = false;
                }

                LandingBalloonsEntity lander = LandingBalloonsEntity.createEntityLandingBalloons(player);

                if (!newWorld.isClientSide)
                {
                    boolean previous = CompatibilityManager.forceLoadChunks((ServerLevel) newWorld);
                    lander.forcedLoading = true;
                    newWorld.addFreshEntity(lander);
                    CompatibilityManager.forceLoadChunksEnd((ServerLevel) newWorld, previous);
                }

                stats.setTeleportCooldown(10);
            }
        }
    }

    @Override
    public void setupAdventureSpawn(ServerPlayer player)
    {
        // TODO Auto-generated method stub

    }
}
