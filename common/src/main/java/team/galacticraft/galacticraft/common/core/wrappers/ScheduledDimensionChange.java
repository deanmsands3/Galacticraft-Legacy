package team.galacticraft.galacticraft.common.core.wrappers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.dimension.DimensionType;

public class ScheduledDimensionChange
{
    private ServerPlayer player;
    private DimensionType dimensionName;

    public ScheduledDimensionChange(ServerPlayer player, DimensionType dimensionName)
    {
        this.player = player;
        this.dimensionName = dimensionName;
    }

    public ServerPlayer getPlayer()
    {
        return player;
    }

    public DimensionType getDimensionId()
    {
        return dimensionName;
    }

    public void setPlayer(ServerPlayer player)
    {
        this.player = player;
    }

    public void setDimensionName(DimensionType dimensionName)
    {
        this.dimensionName = dimensionName;
    }
}
