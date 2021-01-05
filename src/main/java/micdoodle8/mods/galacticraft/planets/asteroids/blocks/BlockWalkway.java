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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class BlockWalkway extends BlockTransmitter implements IShiftDescription, ISortable
{
    //    private Vector3 minVector = new Vector3(0.0, 0.32, 0.0);
    //    private Vector3 maxVector = new Vector3(1.0, 1.0, 1.0);
    protected static final VoxelShape AABB_UNCONNECTED = VoxelShapes.create(0.0, 0.32, 0.0, 1.0, 1.0, 1.0);
    protected static final VoxelShape AABB_CONNECTED_DOWN = VoxelShapes.create(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

    protected BlockWalkway(Properties builder)
    {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(UP, true).with(DOWN, false).with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        if (state.get(DOWN))
        {
            return AABB_CONNECTED_DOWN;
        }

        return AABB_UNCONNECTED;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
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
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack itemStack)
    {
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(NORTH, EAST, SOUTH, WEST, DOWN, UP);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState state = context.getWorld().getBlockState(context.getPos().down());

        if (state.getBlock() != AsteroidBlocks.WALKWAY && state.getBlock() == this)
        {
            return this.getDefaultState().with(DOWN, true);
        }
        return super.getStateForPlacement(context);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos)
    {
        Object[] connectable = new Object[Direction.values().length];
        TileEntity tileEntity = null;

        if (state.getBlock() != AsteroidBlocks.WALKWAY) // pipe or wire
        {
            tileEntity = world.getTileEntity(currentPos);
        }

        for (Direction direction : Direction.values())
        {
            if (direction == Direction.UP || direction == Direction.DOWN && tileEntity == null)
            {
                continue;
            }

            if (state.getBlock() == AsteroidBlocks.WALKWAY)
            {
                BlockPos neighbour = currentPos.offset(direction);
                BlockState neighbourState = world.getBlockState(neighbour);
                boolean sideSolid = neighbourState.isSolidSide(world, neighbour, direction.getOpposite());

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

        return state.with(NORTH, connectable[Direction.NORTH.ordinal()] != null)
                .with(EAST, connectable[Direction.EAST.ordinal()] != null)
                .with(SOUTH, connectable[Direction.SOUTH.ordinal()] != null)
                .with(WEST, connectable[Direction.WEST.ordinal()] != null)
                .with(DOWN, connectable[Direction.DOWN.ordinal()] != null);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}