package team.galacticraft.galacticraft.common.api.world;

import net.minecraft.world.entity.Entity;

public interface IZeroGDimension
{
    boolean inFreefall(Entity entity);

    void setInFreefall(Entity entity);
}
