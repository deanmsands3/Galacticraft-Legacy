package team.galacticraft.galacticraft.common.api.power;

import net.minecraft.core.Direction;

import java.util.List;

public abstract class EnergySource
{
    public static class EnergySourceWireless extends EnergySource
    {
        public final List<ILaserNode> nodes;

        public EnergySourceWireless(List<ILaserNode> nodes)
        {
            this.nodes = nodes;
        }
    }

    public static class EnergySourceAdjacent extends EnergySource
    {
        public final Direction direction;

        public EnergySourceAdjacent(Direction direction)
        {
            this.direction = direction;
        }
    }
}
