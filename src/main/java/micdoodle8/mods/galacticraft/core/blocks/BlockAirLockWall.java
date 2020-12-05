package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockAirLockWall extends Block implements IPartialSealableBlock, ISortable
{
    public static final EnumProperty<EnumAirLockSealConnection> CONNECTION_TYPE = EnumProperty.create("connection", EnumAirLockSealConnection.class);
    protected static final VoxelShape AABB_X = Shapes.box(0.25, 0.0, 0.0, 0.75, 1.0, 1.0);
    protected static final VoxelShape AABB_Z = Shapes.box(0.0, 0.0, 0.25, 1.0, 1.0, 0.75);
    protected static final VoxelShape AABB_FLAT = Shapes.box(0.0, 0.25, 0.0, 1.0, 0.75, 1.0);

    public enum EnumAirLockSealConnection implements StringRepresentable
    {
        X("x"),
        Z("z"),
        FLAT("flat");

        private final String name;

        EnumAirLockSealConnection(String name)
        {
            this.name = name;
        }

        @Override
        public String getSerializedName()
        {
            return this.name;
        }
    }

    public BlockAirLockWall(Properties builder)
    {
        super(builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONNECTION_TYPE, EnumAirLockSealConnection.X));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        switch (getConnection(worldIn, pos))
        {
        case X:
            return AABB_X;
        case Z:
            return AABB_Z;
        default:
        case FLAT:
            return AABB_FLAT;
        }
    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public boolean shouldSideBeRendered(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
//    {
//        return true;
//    }

//    @Override
//    public int quantityDropped(Random par1Random)
//    {
//        return 0;
//    }

    @Override
    public boolean isSealed(Level worldIn, BlockPos pos, Direction direction)
    {
        return true;
    }

//    @Override
//    public Item getItem(World world, BlockPos pos)
//    {
//        return null;
//    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }

    public static EnumAirLockSealConnection getConnection(BlockGetter worldIn, BlockPos pos)
    {
        EnumAirLockSealConnection connection;

        Block frameID = GCBlocks.airLockFrame;
        Block sealID = GCBlocks.airLockSeal;

        Block idXMin = worldIn.getBlockState(pos.relative(Direction.WEST)).getBlock();
        Block idXMax = worldIn.getBlockState(pos.relative(Direction.WEST)).getBlock();

        if (idXMin != frameID && idXMax != frameID && idXMin != sealID && idXMax != sealID)
        {
            connection = EnumAirLockSealConnection.X;
        }
        else
        {
            int adjacentCount = 0;

            for (Direction dir : Direction.values())
            {
                if (dir.getAxis().isHorizontal())
                {
                    Block blockID = worldIn.getBlockState(pos.relative(dir)).getBlock();

                    if (blockID == GCBlocks.airLockFrame || blockID == GCBlocks.airLockSeal)
                    {
                        adjacentCount++;
                    }
                }
            }

            if (adjacentCount == 4)
            {
                connection = EnumAirLockSealConnection.FLAT;
            }
            else
            {
                connection = EnumAirLockSealConnection.Z;
            }
        }

        return connection;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(CONNECTION_TYPE);
    }
}
