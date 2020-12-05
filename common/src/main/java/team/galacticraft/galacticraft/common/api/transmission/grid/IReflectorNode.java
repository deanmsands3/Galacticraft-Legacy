package team.galacticraft.galacticraft.common.api.transmission.grid;

import team.galacticraft.galacticraft.common.api.vector.Vector3;

public interface IReflectorNode
{
    Vector3 getInputPoint();

    Vector3 getOutputPoint(boolean offset);
}
