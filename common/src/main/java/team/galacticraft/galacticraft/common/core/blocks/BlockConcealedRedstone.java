package team.galacticraft.galacticraft.common.core.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import team.galacticraft.galacticraft.core.items.ISortable;
import team.galacticraft.galacticraft.core.util.EnumSortCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BlockConcealedRedstone extends Block implements ISortable
{
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();
    private boolean canProvidePower = true;

    public BlockConcealedRedstone(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWER, Integer.valueOf(0)));
    }

    protected static boolean canConnectTo(BlockState blockState, BlockGetter world, BlockPos pos, @Nullable Direction side)
    {
        Block block = blockState.getBlock();
        if (block == Blocks.REDSTONE_WIRE)
        {
            return true;
        }
        else if (blockState.getBlock() == Blocks.REPEATER)
        {
            Direction direction = blockState.getValue(RepeaterBlock.FACING);
            return direction == side || direction.getOpposite() == side;
        }
        else if (Blocks.OBSERVER == blockState.getBlock())
        {
            return side == blockState.getValue(ObserverBlock.FACING);
        }
        else
        {
            return blockState.canConnectRedstone(world, pos, side) && side != null;
        }
    }

    @Environment(EnvType.CLIENT)
    public static int colorMultiplier(int p_176337_0_)
    {
        float f = (float) p_176337_0_ / 15.0F;
        float f1 = f * 0.6F + 0.4F;
        if (p_176337_0_ == 0)
        {
            f1 = 0.3F;
        }

        float f2 = f * f * 0.7F - 0.5F;
        float f3 = f * f * 0.6F - 0.7F;
        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        int i = Mth.clamp((int) (f1 * 255.0F), 0, 255);
        int j = Mth.clamp((int) (f2 * 255.0F), 0, 255);
        int k = Mth.clamp((int) (f3 * 255.0F), 0, 255);
        return -16777216 | i << 16 | j << 8 | k;
    }

    private RedstoneSide getSide(BlockGetter worldIn, BlockPos pos, Direction face)
    {
        BlockPos blockpos = pos.relative(face);
        BlockState blockstate = worldIn.getBlockState(blockpos);
        BlockPos blockpos1 = pos.above();
        BlockState blockstate1 = worldIn.getBlockState(blockpos1);
        if (!blockstate1.isRedstoneConductor(worldIn, blockpos1))
        {
            boolean flag = blockstate.isFaceSturdy(worldIn, blockpos, Direction.UP) || blockstate.getBlock() == Blocks.HOPPER;
            if (flag && canConnectTo(worldIn.getBlockState(blockpos.above()), worldIn, blockpos.above(), null))
            {
                if (blockstate.isCollisionShapeFullBlock(worldIn, blockpos))
                {
                    return RedstoneSide.UP;
                }

                return RedstoneSide.SIDE;
            }
        }

        return !canConnectTo(blockstate, worldIn, blockpos, face) && (blockstate.isRedstoneConductor(worldIn, blockpos) || !canConnectTo(worldIn.getBlockState(blockpos.below()), worldIn, blockpos.below(), null)) ? RedstoneSide.NONE : RedstoneSide.SIDE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos)
    {
        BlockPos blockpos = pos.below();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        return blockstate.isFaceSturdy(worldIn, blockpos, Direction.UP) || blockstate.getBlock() == Blocks.HOPPER;
    }

    private BlockState updateSurroundingRedstone(Level worldIn, BlockPos pos, BlockState state)
    {
        state = this.func_212568_b(worldIn, pos, state);
        List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();

        for (BlockPos blockpos : list)
        {
            worldIn.updateNeighborsAt(blockpos, this);
        }

        return state;
    }

    private BlockState func_212568_b(Level p_212568_1_, BlockPos p_212568_2_, BlockState p_212568_3_)
    {
        BlockState blockstate = p_212568_3_;
        int i = p_212568_3_.getValue(POWER);
        this.canProvidePower = false;
        int j = p_212568_1_.getBestNeighborSignal(p_212568_2_);
        this.canProvidePower = true;
        int k = 0;
        if (j < 15)
        {
            for (Direction direction : Direction.Plane.HORIZONTAL)
            {
                BlockPos blockpos = p_212568_2_.relative(direction);
                BlockState blockstate1 = p_212568_1_.getBlockState(blockpos);
                k = this.maxSignal(k, blockstate1);
                BlockPos blockpos1 = p_212568_2_.above();
                if (blockstate1.isRedstoneConductor(p_212568_1_, blockpos) && !p_212568_1_.getBlockState(blockpos1).isRedstoneConductor(p_212568_1_, blockpos1))
                {
                    k = this.maxSignal(k, p_212568_1_.getBlockState(blockpos.above()));
                }
                else if (!blockstate1.isRedstoneConductor(p_212568_1_, blockpos))
                {
                    k = this.maxSignal(k, p_212568_1_.getBlockState(blockpos.below()));
                }
            }
        }

        int l = k - 1;
        if (j > l)
        {
            l = j;
        }

        if (i != l)
        {
            p_212568_3_ = p_212568_3_.setValue(POWER, Integer.valueOf(l));
            if (p_212568_1_.getBlockState(p_212568_2_) == blockstate)
            {
                p_212568_1_.setBlock(p_212568_2_, p_212568_3_, 2);
            }

            this.blocksNeedingUpdate.add(p_212568_2_);

            for (Direction direction1 : Direction.values())
            {
                this.blocksNeedingUpdate.add(p_212568_2_.relative(direction1));
            }
        }

        return p_212568_3_;
    }

    private void notifyWireNeighborsOfStateChange(Level worldIn, BlockPos pos)
    {
        if (worldIn.getBlockState(pos).getBlock() == this)
        {
            worldIn.updateNeighborsAt(pos, this);

            for (Direction direction : Direction.values())
            {
                worldIn.updateNeighborsAt(pos.relative(direction), this);
            }

        }
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (oldState.getBlock() != state.getBlock() && !worldIn.isClientSide)
        {
            this.updateSurroundingRedstone(worldIn, pos, state);

            for (Direction direction : Direction.Plane.VERTICAL)
            {
                worldIn.updateNeighborsAt(pos.relative(direction), this);
            }

            for (Direction direction1 : Direction.Plane.HORIZONTAL)
            {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.relative(direction1));
            }

            for (Direction direction2 : Direction.Plane.HORIZONTAL)
            {
                BlockPos blockpos = pos.relative(direction2);
                if (worldIn.getBlockState(blockpos).isRedstoneConductor(worldIn, blockpos))
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.above());
                }
                else
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.below());
                }
            }

        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (!isMoving && state.getBlock() != newState.getBlock())
        {
            super.onRemove(state, worldIn, pos, newState, isMoving);
            if (!worldIn.isClientSide)
            {
                for (Direction direction : Direction.values())
                {
                    worldIn.updateNeighborsAt(pos.relative(direction), this);
                }

                this.updateSurroundingRedstone(worldIn, pos, state);

                for (Direction direction1 : Direction.Plane.HORIZONTAL)
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, pos.relative(direction1));
                }

                for (Direction direction2 : Direction.Plane.HORIZONTAL)
                {
                    BlockPos blockpos = pos.relative(direction2);
                    if (worldIn.getBlockState(blockpos).isRedstoneConductor(worldIn, blockpos))
                    {
                        this.notifyWireNeighborsOfStateChange(worldIn, blockpos.above());
                    }
                    else
                    {
                        this.notifyWireNeighborsOfStateChange(worldIn, blockpos.below());
                    }
                }

            }
        }
    }

    private int maxSignal(int existingSignal, BlockState neighbor)
    {
        if (neighbor.getBlock() != this)
        {
            return existingSignal;
        }
        else
        {
            int i = neighbor.getValue(POWER);
            return i > existingSignal ? i : existingSignal;
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        if (!worldIn.isClientSide)
        {
            if (state.canSurvive(worldIn, pos))
            {
                this.updateSurroundingRedstone(worldIn, pos, state);
            }
            else
            {
                dropResources(state, worldIn, pos);
                worldIn.removeBlock(pos, false);
            }

        }
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side)
    {
        return !this.canProvidePower ? 0 : blockState.getSignal(blockAccess, pos, side);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side)
    {
        if (!this.canProvidePower)
        {
            return 0;
        }
        else
        {
            int i = blockState.getValue(POWER);
            if (i == 0)
            {
                return 0;
            }
            else if (side == Direction.UP)
            {
                return i;
            }
            else
            {
                EnumSet<Direction> enumset = EnumSet.noneOf(Direction.class);

                for (Direction direction : Direction.Plane.HORIZONTAL)
                {
                    if (this.isPowerSourceAt(blockAccess, pos, direction))
                    {
                        enumset.add(direction);
                    }
                }

                if (side.getAxis().isHorizontal() && enumset.isEmpty())
                {
                    return i;
                }
                else if (enumset.contains(side) && !enumset.contains(side.getCounterClockWise()) && !enumset.contains(side.getClockWise()))
                {
                    return i;
                }
                else
                {
                    return 0;
                }
            }
        }
    }

    private boolean isPowerSourceAt(BlockGetter worldIn, BlockPos pos, Direction side)
    {
        BlockPos blockpos = pos.relative(side);
        BlockState blockstate = worldIn.getBlockState(blockpos);
        boolean flag = blockstate.isRedstoneConductor(worldIn, blockpos);
        BlockPos blockpos1 = pos.above();
        boolean flag1 = worldIn.getBlockState(blockpos1).isRedstoneConductor(worldIn, blockpos1);
        if (!flag1 && flag && canConnectTo(worldIn.getBlockState(blockpos.above()), worldIn, blockpos.above(), null))
        {
            return true;
        }
        else if (canConnectTo(blockstate, worldIn, blockpos, side))
        {
            return true;
        }
        else if (blockstate.getBlock() == Blocks.REPEATER && blockstate.getValue(DiodeBlock.POWERED) && blockstate.getValue(DiodeBlock.FACING) == side)
        {
            return true;
        }
        else
        {
            return !flag && canConnectTo(worldIn.getBlockState(blockpos.below()), worldIn, blockpos.below(), null);
        }
    }

    @Override
    public boolean isSignalSource(BlockState state)
    {
        return this.canProvidePower;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand)
    {
        int i = stateIn.getValue(POWER);
        if (i != 0)
        {
            double d0 = (double) pos.getX() + 0.5D + ((double) rand.nextFloat() - 0.5D) * 0.2D;
            double d1 = (float) pos.getY() + 0.0625F;
            double d2 = (double) pos.getZ() + 0.5D + ((double) rand.nextFloat() - 0.5D) * 0.2D;
            float f = (float) i / 15.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
            float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
            worldIn.addParticle(new DustParticleOptions(f1, f2, f3, 1.0F), d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

//    @Override
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.DECORATION;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(POWER);
    }
}