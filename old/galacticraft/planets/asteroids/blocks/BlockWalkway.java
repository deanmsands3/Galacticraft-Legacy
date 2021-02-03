package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import javax.annotation.Nullable;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.core.blocks.BlockTransmitter;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.WalkwayFluidPipeTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockWalkway extends BlockTransmitter implements IShiftDescription, ISortable
{
    //    private Vector3 minVector = new Vector3(0.0, 0.32, 0.0);
    //    private Vector3 maxVector = new Vector3(1.0, 1.0, 1.0);
    protected static final VoxelShape AABB_UNCONNECTED = Shapes.box(0.0, 0.32, 0.0, 1.0, 1.0, 1.0);
    protected static final VoxelShape AABB_CONNECTED_DOWN = Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

    protected BlockWalkway(Properties builder)
    {
        super(builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(UP, true).setValue(DOWN, false).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        if (state.getValue(DOWN))
        {
            return AABB_CONNECTED_DOWN;
        }

        return AABB_UNCONNECTED;
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        if (this == AsteroidBlocks.FLUID_PIPE_WALKWAY)
        {
            return new WalkwayFluidPipeTileEntity();
        }

        if (this == AsteroidBlocks.WIRE_WALKWAY)
        {
            return new TileEntityAluminumWire.TileEntityAluminumWireT2();
        }

        return null;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return this != AsteroidBlocks.WALKWAY;
    }

    @Override
    public NetworkType getNetworkType(BlockState state)
    {
        if (this == AsteroidBlocks.WIRE_WALKWAY)
        {
            return NetworkType.FLUID;
        }

        if (this == AsteroidBlocks.FLUID_PIPE_WALKWAY)
        {
            return NetworkType.POWER;
        }

        return null;
    }

    @Override
    public String getShiftDescription(ItemStack itemStack)
    {
        return GCCoreUtil.translate(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack itemStack)
    {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(NORTH, EAST, SOUTH, WEST, DOWN, UP);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos().below());

        if (state.getBlock() != AsteroidBlocks.WALKWAY && state.getBlock() == this)
        {
            return this.defaultBlockState().setValue(DOWN, true);
        }
        return super.getStateForPlacement(context);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos)
    {
        Object[] connectable = new Object[Direction.values().length];
        BlockEntity tileEntity = null;

        if (state.getBlock() != AsteroidBlocks.WALKWAY) // pipe or wire
        {
            tileEntity = world.getBlockEntity(currentPos);
        }

        for (Direction direction : Direction.values())
        {
            if (direction == Direction.UP || direction == Direction.DOWN && tileEntity == null)
            {
                continue;
            }

            if (state.getBlock() == AsteroidBlocks.WALKWAY)
            {
                BlockPos neighbour = currentPos.relative(direction);
                BlockState neighbourState = world.getBlockState(neighbour);
                boolean sideSolid = neighbourState.isFaceSturdy(world, neighbour, direction.getOpposite());

                if (neighbourState.getBlock() == this || sideSolid)
                {
                    connectable[direction.ordinal()] = neighbourState.getBlock();
                }
            }
            else if (tileEntity != null && state.getBlock() == AsteroidBlocks.FLUID_PIPE_WALKWAY)
            {
                connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);
            }
            else if (tileEntity != null && state.getBlock() == AsteroidBlocks.WIRE_WALKWAY)
            {
                connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
            }
        }

        return state.setValue(NORTH, connectable[Direction.NORTH.ordinal()] != null)
                .setValue(EAST, connectable[Direction.EAST.ordinal()] != null)
                .setValue(SOUTH, connectable[Direction.SOUTH.ordinal()] != null)
                .setValue(WEST, connectable[Direction.WEST.ordinal()] != null)
                .setValue(DOWN, connectable[Direction.DOWN.ordinal()] != null);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}