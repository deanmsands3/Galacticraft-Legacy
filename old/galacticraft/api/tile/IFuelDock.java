package micdoodle8.mods.galacticraft.api.tile;

import micdoodle8.mods.galacticraft.api.entity.IDockable;
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
