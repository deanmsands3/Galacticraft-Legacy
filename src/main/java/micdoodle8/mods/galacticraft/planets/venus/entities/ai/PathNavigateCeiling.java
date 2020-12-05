package micdoodle8.mods.galacticraft.planets.venus.entities.ai;

import micdoodle8.mods.galacticraft.planets.venus.entities.EntityJuicer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import java.util.stream.Stream;

public class PathNavigateCeiling extends PathNavigation
{
    protected WalkNodeProcessorCeiling nodeProcessor;

    public PathNavigateCeiling(EntityJuicer entity, Level worldIn)
    {
        super(entity, worldIn);
    }

    @Override
    protected PathFinder createPathFinder(int val)
    {
        this.nodeEvaluator = new WalkNodeProcessorCeiling();
        return new PathFinder(this.nodeEvaluator, val);
    }

    @Override
    protected boolean canUpdatePath()
    {
        return this.mob.onGround || this.mob.getVehicle() != null && this.mob instanceof Zombie && this.mob.getVehicle() instanceof Chicken;
    }

    @Override
    protected Vec3 getTempMobPos()
    {
        return new Vec3(this.mob.getX(), this.getPathablePosY(), this.mob.getZ());
    }

    private int getPathablePosY()
    {
        return (int) (this.mob.getBoundingBox().minY + 0.5D);
    }

    @Override
    protected boolean canMoveDirectly(Vec3 current, Vec3 target, int sizeX, int sizeY, int sizeZ)
    {
        int i = Mth.floor(current.x);
        int j = Mth.floor(current.z);
        double d0 = target.x - current.x;
        double d1 = target.z - current.z;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 < 1.0E-8D)
        {
            return false;
        }
        else
        {
            double d3 = 1.0D / Math.sqrt(d2);
            d0 = d0 * d3;
            d1 = d1 * d3;
            sizeX = sizeX + 2;
            sizeZ = sizeZ + 2;

            if (!this.isSafeToStandAt(i, (int) current.y, j, sizeX, sizeY, sizeZ, current, d0, d1))
            {
                return false;
            }
            else
            {
                sizeX = sizeX - 2;
                sizeZ = sizeZ - 2;
                double d4 = 1.0D / Math.abs(d0);
                double d5 = 1.0D / Math.abs(d1);
                double d6 = (double) (i) - current.x;
                double d7 = (double) (j) - current.z;

                if (d0 >= 0.0D)
                {
                    ++d6;
                }

                if (d1 >= 0.0D)
                {
                    ++d7;
                }

                d6 = d6 / d0;
                d7 = d7 / d1;
                int k = d0 < 0.0D ? -1 : 1;
                int l = d1 < 0.0D ? -1 : 1;
                int i1 = Mth.floor(target.x);
                int j1 = Mth.floor(target.z);
                int k1 = i1 - i;
                int l1 = j1 - j;

                while (k1 * k > 0 || l1 * l > 0)
                {
                    if (d6 < d7)
                    {
                        d6 += d4;
                        i += k;
                        k1 = i1 - i;
                    }
                    else
                    {
                        d7 += d5;
                        j += l;
                        l1 = j1 - j;
                    }

                    if (!this.isSafeToStandAt(i, (int) current.y, j, sizeX, sizeY, sizeZ, current, d0, d1))
                    {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    private boolean isSafeToStandAt(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3 currentPos, double distanceX, double distanceZ)
    {
        int i = x - sizeX / 2;
        int j = z - sizeZ / 2;

        if (!this.isPositionClear(i, y, j, sizeX, sizeY, sizeZ, currentPos, distanceX, distanceZ))
        {
            return false;
        }
        else
        {
            for (int k = i; k < i + sizeX; ++k)
            {
                for (int l = j; l < j + sizeZ; ++l)
                {
                    double d0 = (double) k + 0.5D - currentPos.x;
                    double d1 = (double) l + 0.5D - currentPos.z;

                    if (d0 * distanceX + d1 * distanceZ >= 0.0D)
                    {
                        BlockState state = this.level.getBlockState(new BlockPos(k, y + 1, l));
                        Material material = state.getMaterial();

                        if (material == Material.AIR)
                        {
                            return false;
                        }

                        if (material == Material.WATER && !this.mob.isInWater())
                        {
                            return false;
                        }

                        if (material == Material.LAVA)
                        {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private boolean isPositionClear(int minX, int minY, int minZ, int sizeX, int sizeY, int sizeZ, Vec3 currentPos, double distanceX, double distanceZ)
    {
        Stream<BlockPos> stream = BlockPos.betweenClosedStream(new BlockPos(minX, minY, minZ), new BlockPos(minX + sizeX - 1, minY + sizeY - 1, minZ + sizeZ - 1));
        stream = stream.filter((pos) -> ((double) pos.getX() + 0.5D - currentPos.x) * distanceX + ((double) pos.getZ() + 0.5D - currentPos.z) * distanceZ >= 0.0D);
        return stream.allMatch((pos) ->
        {
            BlockState state = this.level.getBlockState(pos);

            return state.isPathfindable(level, pos, PathComputationType.LAND);
        });
    }
}