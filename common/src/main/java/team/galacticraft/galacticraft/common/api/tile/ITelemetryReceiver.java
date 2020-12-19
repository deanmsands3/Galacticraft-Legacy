package team.galacticraft.galacticraft.common.api.tile;

import net.minecraft.world.entity.Entity;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3Dim;

public interface ITelemetryReceiver
{
    Entity getLinkedEntity();

    void addTrackedEntity(Entity entity);

    boolean isValid();

    BlockVec3Dim toPosition();
}
