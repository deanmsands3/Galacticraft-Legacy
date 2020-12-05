package team.galacticraft.galacticraft.common.api.tile;

import team.galacticraft.galacticraft.common.api.entity.IDockable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import java.util.HashSet;

public interface IFuelDock
{
    HashSet<ILandingPadAttachable> getConnectedTiles();

    boolean isBlockAttachable(LevelReader world, BlockPos pos);

    IDockable getDockedEntity();

    void dockEntity(IDockable entity);
}
