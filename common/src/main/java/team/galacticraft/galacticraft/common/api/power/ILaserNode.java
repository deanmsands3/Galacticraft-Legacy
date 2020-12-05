package team.galacticraft.galacticraft.common.api.power;

import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.common.api.vector.Vector3;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ILaserNode extends IEnergyHandlerGC
{
    Vector3 getInputPoint();

    Vector3 getOutputPoint(boolean offset);

    ILaserNode getTarget();

    BlockEntity getTile();

    boolean canConnectTo(ILaserNode node);

    Vector3 getColor();

    void addNode(ILaserNode node);

    void removeNode(ILaserNode node);

    int compareTo(ILaserNode otherNode, BlockVec3 origin);
}
