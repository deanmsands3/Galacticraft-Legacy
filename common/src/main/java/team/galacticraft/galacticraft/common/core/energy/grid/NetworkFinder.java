package team.galacticraft.galacticraft.common.core.energy.grid;

import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.transmission.tile.IConductor;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NetworkFinder
{
    public Level worldObj;
    public BlockVec3 start;
    private int theDim;
    private final BlockVec3 toIgnore;

    private final Set<BlockVec3> iterated = new HashSet<BlockVec3>();
    public List<IConductor> found = new LinkedList<IConductor>();

    public NetworkFinder(Level world, BlockVec3 location, BlockVec3 ignore)
    {
        worldObj = world;
        start = location;

        toIgnore = ignore;
    }

    private void loopAll(int x, int y, int z, int dirIn)
    {
        BlockVec3 obj = null;
        for (int dir = 0; dir < 6; dir++)
        {
            if (dir == dirIn)
            {
                continue;
            }
            switch (dir)
            {
            case 0:
                obj = new BlockVec3(x, y - 1, z);
                break;
            case 1:
                obj = new BlockVec3(x, y + 1, z);
                break;
            case 2:
                obj = new BlockVec3(x, y, z - 1);
                break;
            case 3:
                obj = new BlockVec3(x, y, z + 1);
                break;
            case 4:
                obj = new BlockVec3(x - 1, y, z);
                break;
            case 5:
                obj = new BlockVec3(x + 1, y, z);
                break;
            }

            if (!iterated.contains(obj))
            {
                iterated.add(obj);

                BlockEntity tileEntity = worldObj.getBlockEntity(new BlockPos(obj.x, obj.y, obj.z));

                if (tileEntity instanceof IConductor && ((IConductor) tileEntity).canConnect(Direction.from3DDataValue(dir ^ 1), NetworkType.POWER))
                {
                    found.add((IConductor) tileEntity);
                    loopAll(obj.x, obj.y, obj.z, dir ^ 1);
                }
            }
        }
    }

    public List<IConductor> exploreNetwork()
    {
        if (start.getTileEntity(worldObj) instanceof IConductor)
        {
            iterated.add(start);
            iterated.add(toIgnore);
            found.add((IConductor) start.getTileEntity(worldObj));
            loopAll(start.x, start.y, start.z, 6);
        }

        return found;
    }
}
