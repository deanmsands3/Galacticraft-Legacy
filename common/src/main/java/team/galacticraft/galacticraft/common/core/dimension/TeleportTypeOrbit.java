package team.galacticraft.galacticraft.common.core.dimension;

import team.galacticraft.galacticraft.common.api.vector.Vector3;
import team.galacticraft.galacticraft.common.api.vector.Vector3D;
import team.galacticraft.galacticraft.common.api.world.ITeleportType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import java.util.Random;

public class TeleportTypeOrbit implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return false;
    }

    @Override
    public Vector3D getPlayerSpawnLocation(ServerLevel world, ServerPlayer player)
    {
        return new Vector3D(0.5, 65.0, 0.5);
    }

    @Override
    public Vector3D getEntitySpawnLocation(ServerLevel world, Entity player)
    {
        return new Vector3D(0.5, 65.0, 0.5);
    }

    @Override
    public Vector3D getParaChestSpawnLocation(ServerLevel world, ServerPlayer player, Random rand)
    {
        return new Vector3D(-8.5, 90.0, -1.5);
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
