package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.LanderEntity;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import java.util.Random;

public class TeleportTypeMoon implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return ConfigManagerCore.disableLander.get();
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
        if (ConfigManagerCore.disableLander.get())
        {
            final float x = (rand.nextFloat() * 2 - 1.0F) * 4.0F;
            final float z = (rand.nextFloat() * 2 - 1.0F) * 4.0F;
            return new Vector3D(player.getX() + x, 220.0, player.getZ() + z);
        }

        return null;
    }

    @Override
    public void onSpaceDimensionChanged(Level newWorld, ServerPlayer player, boolean ridingAutoRocket)
    {
        GCPlayerStats stats = GCPlayerStats.get(player);
        if (!ridingAutoRocket && !ConfigManagerCore.disableLander.get() && stats.getTeleportCooldown() <= 0)
        {
            if (player.abilities.flying)
            {
                player.abilities.flying = false;
            }

            LanderEntity lander = new LanderEntity(player);
            lander.setPos(player.getX(), player.getY(), player.getZ());

            if (!newWorld.isClientSide)
            {
                boolean previous = CompatibilityManager.forceLoadChunks((ServerLevel) newWorld);
                lander.forcedLoading = true;
                newWorld.addFreshEntity(lander);
                lander.setLevel(newWorld);
//                newWorld.updateEntityWithOptionalForce(lander, true);
                ((ServerLevel) newWorld).updateChunkPos(lander);
                player.startRiding(lander);
                CompatibilityManager.forceLoadChunksEnd((ServerLevel) newWorld, previous);
                GCLog.debug("Entering lander at : " + player.getX() + "," + player.getZ() + " lander spawn at: " + lander.getX() + "," + lander.getZ());
            }

            stats.setTeleportCooldown(10);
        }
    }

    @Override
    public void setupAdventureSpawn(ServerPlayer player)
    {
        // TODO Auto-generated method stub

    }
}
