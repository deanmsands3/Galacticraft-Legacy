package micdoodle8.mods.galacticraft.planets.venus.entities.ai;

import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.pathfinding.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;

public class WalkNodeProcessorCeiling extends NodeEvaluator
{
    protected float avoidsWater;
    protected Mob currentEntity;

    @Override
    public void prepare(PathNavigationRegion region, Mob mob) {
        super.prepare(region, mob);
        this.avoidsWater = mob.getPathfindingMalus(PathNodeType.WATER);
    }

    @Override
    public void done()
    {
        this.mob.setPathfindingMalus(PathNodeType.WATER, this.avoidsWater);
        super.done();
    }

    @Override
    public Node getStart()
    {
        int i;
        if (this.canFloat() && this.mob.isInWater())
        {
            i = Mth.floor(this.mob.getBoundingBox().minY);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(this.mob.getX(), i, this.mob.getZ());

            for (BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos); blockstate.getBlock() == Blocks.WATER || blockstate.getFluidState() == Fluids.WATER.getSource(false); blockstate = this.level.getBlockState(blockpos$mutableblockpos))
            {
                ++i;
                blockpos$mutableblockpos.set(this.mob.getX(), i, this.mob.getZ());
            }

            --i;
        }
        else if (this.mob.onGround)
        {
            i = Mth.floor(this.mob.getBoundingBox().minY + 0.5D);
        }
        else
        {
            BlockPos blockpos;
            for (blockpos = new BlockPos(this.mob); (this.level.getBlockState(blockpos).isAir() || this.level.getBlockState(blockpos).isPathfindable(this.level, blockpos, PathType.LAND)) && blockpos.getY() > 0; blockpos = blockpos.below())
            {
            }

            i = blockpos.above().getY();
        }

        BlockPos blockpos2 = new BlockPos(this.mob);
        BlockPathTypes pathnodetype1 = this.getPathNodeType(this.mob, blockpos2.getX(), i, blockpos2.getZ());
        if (this.mob.getPathfindingMalus(pathnodetype1) < 0.0F)
        {
            Set<BlockPos> set = Sets.newHashSet();
            set.add(new BlockPos(this.mob.getBoundingBox().minX, i, this.mob.getBoundingBox().minZ));
            set.add(new BlockPos(this.mob.getBoundingBox().minX, i, this.mob.getBoundingBox().maxZ));
            set.add(new BlockPos(this.mob.getBoundingBox().maxX, i, this.mob.getBoundingBox().minZ));
            set.add(new BlockPos(this.mob.getBoundingBox().maxX, i, this.mob.getBoundingBox().maxZ));

            for (BlockPos blockpos1 : set)
            {
                BlockPathTypes pathnodetype = this.getPathNodeType(this.mob, blockpos1);
                if (this.mob.getPathfindingMalus(pathnodetype) >= 0.0F)
                {
                    return this.getNode(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                }
            }
        }

        return this.getNode(blockpos2.getX(), i, blockpos2.getZ());
    }

    @Override
    public Target getGoal(double x, double y, double z)
    {
        return new Target(this.getNode(Mth.floor(x), Mth.floor(y), Mth.floor(z)));
    }

    @Override
    public int getNeighbors(Node[] pathOptions, Node fromPoint)
    {
        int i = 0;
        int j = 0;
        BlockPathTypes pathnodetype = this.getPathNodeType(this.mob, fromPoint.x, fromPoint.y + 1, fromPoint.z);
        if (this.mob.getPathfindingMalus(pathnodetype) >= 0.0F)
        {
            j = Mth.floor(Math.max(1.0F, this.mob.maxUpStep));
        }

        double d0 = getGroundY(this.level, new BlockPos(fromPoint.x, fromPoint.y, fromPoint.z));
        Node pathpoint = this.getSafePoint(fromPoint.x, fromPoint.y, fromPoint.z + 1, j, d0, Direction.SOUTH);
        if (pathpoint != null && !pathpoint.closed && pathpoint.costMalus >= 0.0F)
        {
            pathOptions[i++] = pathpoint;
        }

        Node pathpoint1 = this.getSafePoint(fromPoint.x - 1, fromPoint.y, fromPoint.z, j, d0, Direction.WEST);
        if (pathpoint1 != null && !pathpoint1.closed && pathpoint1.costMalus >= 0.0F)
        {
            pathOptions[i++] = pathpoint1;
        }

        Node pathpoint2 = this.getSafePoint(fromPoint.x + 1, fromPoint.y, fromPoint.z, j, d0, Direction.EAST);
        if (pathpoint2 != null && !pathpoint2.closed && pathpoint2.costMalus >= 0.0F)
        {
            pathOptions[i++] = pathpoint2;
        }

        Node pathpoint3 = this.getSafePoint(fromPoint.x, fromPoint.y, fromPoint.z - 1, j, d0, Direction.NORTH);
        if (pathpoint3 != null && !pathpoint3.closed && pathpoint3.costMalus >= 0.0F)
        {
            pathOptions[i++] = pathpoint3;
        }

        Node pathpoint4 = this.getSafePoint(fromPoint.x - 1, fromPoint.y, fromPoint.z - 1, j, d0, Direction.NORTH);
        if (this.func_222860_a(fromPoint, pathpoint1, pathpoint3, pathpoint4))
        {
            pathOptions[i++] = pathpoint4;
        }

        Node pathpoint5 = this.getSafePoint(fromPoint.x + 1, fromPoint.y, fromPoint.z - 1, j, d0, Direction.NORTH);
        if (this.func_222860_a(fromPoint, pathpoint2, pathpoint3, pathpoint5))
        {
            pathOptions[i++] = pathpoint5;
        }

        Node pathpoint6 = this.getSafePoint(fromPoint.x - 1, fromPoint.y, fromPoint.z + 1, j, d0, Direction.SOUTH);
        if (this.func_222860_a(fromPoint, pathpoint1, pathpoint, pathpoint6))
        {
            pathOptions[i++] = pathpoint6;
        }

        Node pathpoint7 = this.getSafePoint(fromPoint.x + 1, fromPoint.y, fromPoint.z + 1, j, d0, Direction.SOUTH);
        if (this.func_222860_a(fromPoint, pathpoint2, pathpoint, pathpoint7))
        {
            pathOptions[i++] = pathpoint7;
        }

        return i;
    }

    private boolean func_222860_a(Node p_222860_1_, @Nullable Node p_222860_2_, @Nullable Node p_222860_3_, @Nullable Node p_222860_4_)
    {
        if (p_222860_4_ != null && p_222860_3_ != null && p_222860_2_ != null)
        {
            if (p_222860_4_.closed)
            {
                return false;
            }
            else if (p_222860_3_.y <= p_222860_1_.y && p_222860_2_.y <= p_222860_1_.y)
            {
                return p_222860_4_.costMalus >= 0.0F && (p_222860_3_.y < p_222860_1_.y || p_222860_3_.costMalus >= 0.0F) && (p_222860_2_.y < p_222860_1_.y || p_222860_2_.costMalus >= 0.0F);
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public static double getGroundY(BlockGetter p_197682_0_, BlockPos pos)
    {
        BlockPos blockpos = pos.below();
        VoxelShape voxelshape = p_197682_0_.getBlockState(blockpos).getCollisionShape(p_197682_0_, blockpos);
        return (double) blockpos.getY() + (voxelshape.isEmpty() ? 0.0D : voxelshape.max(Direction.Axis.Y));
    }

    /**
     * Returns a point that the entity can safely move to
     */
    @Nullable
    private Node getSafePoint(int x, int y, int z, int stepHeight, double groundYIn, Direction facing)
    {
        Node pathpoint = null;
        BlockPos blockpos = new BlockPos(x, y, z);
        double d0 = getGroundY(this.level, blockpos);
        if (d0 - groundYIn > 1.125D)
        {
            return null;
        }
        else
        {
            BlockPathTypes pathnodetype = this.getPathNodeType(this.mob, x, y, z);
            float f = this.mob.getPathfindingMalus(pathnodetype);
            double d1 = (double) this.mob.getBbWidth() / 2.0D;
            if (f >= 0.0F)
            {
                pathpoint = this.getNode(x, y, z);
                pathpoint.type = pathnodetype;
                pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
            }

            if (pathnodetype == PathNodeType.WALKABLE)
            {
                return pathpoint;
            }
            else
            {
                if ((pathpoint == null || pathpoint.costMalus < 0.0F) && stepHeight > 0 && pathnodetype != PathNodeType.FENCE && pathnodetype != PathNodeType.TRAPDOOR)
                {
                    pathpoint = this.getSafePoint(x, y + 1, z, stepHeight - 1, groundYIn, facing);
                    if (pathpoint != null && (pathpoint.type == PathNodeType.OPEN || pathpoint.type == PathNodeType.WALKABLE) && this.mob.getBbWidth() < 1.0F)
                    {
                        double d2 = (double) (x - facing.getStepX()) + 0.5D;
                        double d3 = (double) (z - facing.getStepZ()) + 0.5D;
                        AABB axisalignedbb = new AABB(d2 - d1, getGroundY(this.level, new BlockPos(d2, y + 1, d3)) + 0.001D, d3 - d1, d2 + d1, (double) this.mob.getBbHeight() + getGroundY(this.level, new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z)) - 0.002D, d3 + d1);
                        if (!this.level.noCollision(this.mob, axisalignedbb))
                        {
                            pathpoint = null;
                        }
                    }
                }

                if (pathnodetype == PathNodeType.WATER && !this.canFloat())
                {
                    if (this.getPathNodeType(this.mob, x, y - 1, z) != PathNodeType.WATER)
                    {
                        return pathpoint;
                    }

                    while (y > 0)
                    {
                        --y;
                        pathnodetype = this.getPathNodeType(this.mob, x, y, z);
                        if (pathnodetype != PathNodeType.WATER)
                        {
                            return pathpoint;
                        }

                        pathpoint = this.getNode(x, y, z);
                        pathpoint.type = pathnodetype;
                        pathpoint.costMalus = Math.max(pathpoint.costMalus, this.mob.getPathfindingMalus(pathnodetype));
                    }
                }

                if (pathnodetype == PathNodeType.OPEN)
                {
                    AABB axisalignedbb1 = new AABB((double) x - d1 + 0.5D, (double) y + 0.001D, (double) z - d1 + 0.5D, (double) x + d1 + 0.5D, (float) y + this.mob.getBbHeight(), (double) z + d1 + 0.5D);
                    if (!this.level.noCollision(this.mob, axisalignedbb1))
                    {
                        return null;
                    }

                    if (this.mob.getBbWidth() >= 1.0F)
                    {
                        BlockPathTypes pathnodetype1 = this.getPathNodeType(this.mob, x, y - 1, z);
                        if (pathnodetype1 == PathNodeType.BLOCKED)
                        {
                            pathpoint = this.getNode(x, y, z);
                            pathpoint.type = PathNodeType.WALKABLE;
                            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                            return pathpoint;
                        }
                    }

                    int i = 0;
                    int j = y;

                    while (pathnodetype == PathNodeType.OPEN)
                    {
                        --y;
                        if (y < 0)
                        {
                            Node pathpoint2 = this.getNode(x, j, z);
                            pathpoint2.type = PathNodeType.BLOCKED;
                            pathpoint2.costMalus = -1.0F;
                            return pathpoint2;
                        }

                        Node pathpoint1 = this.getNode(x, y, z);
                        if (i++ >= this.mob.getMaxFallDistance())
                        {
                            pathpoint1.type = PathNodeType.BLOCKED;
                            pathpoint1.costMalus = -1.0F;
                            return pathpoint1;
                        }

                        pathnodetype = this.getPathNodeType(this.mob, x, y, z);
                        f = this.mob.getPathfindingMalus(pathnodetype);
                        if (pathnodetype != PathNodeType.OPEN && f >= 0.0F)
                        {
                            pathpoint = pathpoint1;
                            pathpoint1.type = pathnodetype;
                            pathpoint1.costMalus = Math.max(pathpoint1.costMalus, f);
                            break;
                        }

                        if (f < 0.0F)
                        {
                            pathpoint1.type = PathNodeType.BLOCKED;
                            pathpoint1.costMalus = -1.0F;
                            return pathpoint1;
                        }
                    }
                }

                return pathpoint;
            }
        }
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter blockaccessIn, int x, int y, int z, Mob entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn)
    {
        EnumSet<BlockPathTypes> enumset = EnumSet.noneOf(BlockPathTypes.class);
        BlockPathTypes pathnodetype = PathNodeType.BLOCKED;
        double d0 = (double) entitylivingIn.getBbWidth() / 2.0D;
        BlockPos blockpos = new BlockPos(entitylivingIn);
        this.currentEntity = entitylivingIn;
        pathnodetype = this.getPathNodeType(blockaccessIn, x, y, z, xSize, ySize, zSize, canBreakDoorsIn, canEnterDoorsIn, enumset, pathnodetype, blockpos);
        this.currentEntity = entitylivingIn;
        if (enumset.contains(PathNodeType.FENCE))
        {
            return PathNodeType.FENCE;
        }
        else
        {
            BlockPathTypes pathnodetype1 = PathNodeType.BLOCKED;

            for (BlockPathTypes pathnodetype2 : enumset)
            {
                if (entitylivingIn.getPathfindingMalus(pathnodetype2) < 0.0F)
                {
                    return pathnodetype2;
                }

                if (entitylivingIn.getPathfindingMalus(pathnodetype2) >= entitylivingIn.getPathfindingMalus(pathnodetype1))
                {
                    pathnodetype1 = pathnodetype2;
                }
            }

            if (pathnodetype == PathNodeType.OPEN && entitylivingIn.getPathfindingMalus(pathnodetype1) == 0.0F)
            {
                return PathNodeType.OPEN;
            }
            else
            {
                return pathnodetype1;
            }
        }
    }

    public BlockPathTypes getPathNodeType(BlockGetter p_193577_1_, int x, int y, int z, int xSize, int ySize, int zSize, boolean canOpenDoorsIn, boolean canEnterDoorsIn, EnumSet<BlockPathTypes> nodeTypeEnum, BlockPathTypes nodeType, BlockPos pos)
    {
        for (int i = 0; i < xSize; ++i)
        {
            for (int j = 0; j < ySize; ++j)
            {
                for (int k = 0; k < zSize; ++k)
                {
                    int l = i + x;
                    int i1 = j + y;
                    int j1 = k + z;
                    BlockPathTypes pathnodetype = this.getBlockPathType(p_193577_1_, l, i1, j1);
                    pathnodetype = this.func_215744_a(p_193577_1_, canOpenDoorsIn, canEnterDoorsIn, pos, pathnodetype);
                    if (i == 0 && j == 0 && k == 0)
                    {
                        nodeType = pathnodetype;
                    }

                    nodeTypeEnum.add(pathnodetype);
                }
            }
        }

        return nodeType;
    }

    protected BlockPathTypes func_215744_a(BlockGetter p_215744_1_, boolean p_215744_2_, boolean p_215744_3_, BlockPos p_215744_4_, BlockPathTypes p_215744_5_)
    {
        if (p_215744_5_ == PathNodeType.DOOR_WOOD_CLOSED && p_215744_2_ && p_215744_3_)
        {
            p_215744_5_ = PathNodeType.WALKABLE;
        }

        if (p_215744_5_ == PathNodeType.DOOR_OPEN && !p_215744_3_)
        {
            p_215744_5_ = PathNodeType.BLOCKED;
        }

        if (p_215744_5_ == PathNodeType.RAIL && !(p_215744_1_.getBlockState(p_215744_4_).getBlock() instanceof BaseRailBlock) && !(p_215744_1_.getBlockState(p_215744_4_.below()).getBlock() instanceof BaseRailBlock))
        {
            p_215744_5_ = PathNodeType.FENCE;
        }

        if (p_215744_5_ == PathNodeType.LEAVES)
        {
            p_215744_5_ = PathNodeType.BLOCKED;
        }

        return p_215744_5_;
    }

    private BlockPathTypes getPathNodeType(Mob entitylivingIn, BlockPos pos)
    {
        return this.getPathNodeType(entitylivingIn, pos.getX(), pos.getY(), pos.getZ());
    }

    private BlockPathTypes getPathNodeType(Mob entitylivingIn, int x, int y, int z)
    {
        return this.getBlockPathType(this.level, x, y, z, entitylivingIn, this.entityWidth, this.entityHeight, this.entityDepth, this.canOpenDoors(), this.canPassDoors());
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter blockaccessIn, int x, int y, int z)
    {
        BlockPathTypes pathnodetype = this.getPathNodeTypeRaw(blockaccessIn, x, y, z);
        if (pathnodetype == PathNodeType.OPEN && y >= 1)
        {
            Block block = blockaccessIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
            BlockPathTypes pathnodetype1 = this.getPathNodeTypeRaw(blockaccessIn, x, y - 1, z);
            pathnodetype = pathnodetype1 != PathNodeType.WALKABLE && pathnodetype1 != PathNodeType.OPEN && pathnodetype1 != PathNodeType.WATER && pathnodetype1 != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;
            if (pathnodetype1 == PathNodeType.DAMAGE_FIRE || block == Blocks.MAGMA_BLOCK || block == Blocks.CAMPFIRE)
            {
                pathnodetype = PathNodeType.DAMAGE_FIRE;
            }

            if (pathnodetype1 == PathNodeType.DAMAGE_CACTUS)
            {
                pathnodetype = PathNodeType.DAMAGE_CACTUS;
            }

            if (pathnodetype1 == PathNodeType.DAMAGE_OTHER)
            {
                pathnodetype = PathNodeType.DAMAGE_OTHER;
            }
            if (pathnodetype1 == PathNodeType.DAMAGE_OTHER)
            {
                pathnodetype = PathNodeType.DAMAGE_OTHER; // Forge: consider modded damage types
            }
        }

        pathnodetype = this.checkNeighborBlocks(blockaccessIn, x, y, z, pathnodetype);
        return pathnodetype;
    }

    public BlockPathTypes checkNeighborBlocks(BlockGetter blockaccessIn, int x, int y, int z, BlockPathTypes nodeType)
    {
        if (nodeType == PathNodeType.WALKABLE)
        {
            try (BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.acquire())
            {
                for (int i = -1; i <= 1; ++i)
                {
                    for (int j = -1; j <= 1; ++j)
                    {
                        if (i != 0 || j != 0)
                        {
                            BlockState state = blockaccessIn.getBlockState(blockpos$pooledmutableblockpos.set(i + x, y, j + z));
                            Block block = state.getBlock();
                            BlockPathTypes type = block.getAiPathNodeType(state, blockaccessIn, blockpos$pooledmutableblockpos, this.currentEntity);
                            if (block == Blocks.CACTUS || type == PathNodeType.DAMAGE_CACTUS)
                            {
                                nodeType = PathNodeType.DANGER_CACTUS;
                            }
                            else if (block == Blocks.FIRE || type == PathNodeType.DAMAGE_FIRE)
                            {
                                nodeType = PathNodeType.DANGER_FIRE;
                            }
                            else if (block == Blocks.SWEET_BERRY_BUSH || type == PathNodeType.DAMAGE_OTHER)
                            {
                                nodeType = PathNodeType.DANGER_OTHER;
                            }
                        }
                    }
                }
            }
        }

        return nodeType;
    }

    protected BlockPathTypes getPathNodeTypeRaw(BlockGetter blockaccessIn, int x, int y, int z)
    {
        BlockPos blockpos = new BlockPos(x, y, z);
        BlockState blockstate = blockaccessIn.getBlockState(blockpos);
        BlockPathTypes type = blockstate.getAiPathNodeType(blockaccessIn, blockpos, this.currentEntity);
        if (type != null)
        {
            return type;
        }
        Block block = blockstate.getBlock();
        Material material = blockstate.getMaterial();
        if (blockstate.isAir(blockaccessIn, blockpos))
        {
            return PathNodeType.OPEN;
        }
        else if (!block.is(BlockTags.TRAPDOORS) && block != Blocks.LILY_PAD)
        {
            if (block == Blocks.FIRE)
            {
                return PathNodeType.DAMAGE_FIRE;
            }
            else if (block == Blocks.CACTUS)
            {
                return PathNodeType.DAMAGE_CACTUS;
            }
            else if (block == Blocks.SWEET_BERRY_BUSH)
            {
                return PathNodeType.DAMAGE_OTHER;
            }
            else if (block instanceof DoorBlock && material == Material.WOOD && !blockstate.getValue(DoorBlock.OPEN))
            {
                return PathNodeType.DOOR_WOOD_CLOSED;
            }
            else if (block instanceof DoorBlock && material == Material.METAL && !blockstate.getValue(DoorBlock.OPEN))
            {
                return PathNodeType.DOOR_IRON_CLOSED;
            }
            else if (block instanceof DoorBlock && blockstate.getValue(DoorBlock.OPEN))
            {
                return PathNodeType.DOOR_OPEN;
            }
            else if (block instanceof BaseRailBlock)
            {
                return PathNodeType.RAIL;
            }
            else if (block instanceof LeavesBlock)
            {
                return PathNodeType.LEAVES;
            }
            else if (!block.is(BlockTags.FENCES) && !block.is(BlockTags.WALLS) && (!(block instanceof FenceGateBlock) || blockstate.getValue(FenceGateBlock.OPEN)))
            {
                FluidState ifluidstate = blockaccessIn.getFluidState(blockpos);
                if (ifluidstate.is(FluidTags.WATER))
                {
                    return PathNodeType.WATER;
                }
                else if (ifluidstate.is(FluidTags.LAVA))
                {
                    return PathNodeType.LAVA;
                }
                else
                {
                    return blockstate.isPathfindable(blockaccessIn, blockpos, PathType.LAND) ? PathNodeType.OPEN : PathNodeType.BLOCKED;
                }
            }
            else
            {
                return PathNodeType.FENCE;
            }
        }
        else
        {
            return PathNodeType.TRAPDOOR;
        }
    }
}