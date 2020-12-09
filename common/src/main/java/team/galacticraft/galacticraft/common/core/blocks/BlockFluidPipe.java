package team.galacticraft.galacticraft.common.core.blocks;

import team.galacticraft.galacticraft.common.api.tile.IColorable;
import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.transmission.tile.ITransmitter;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.core.GalacticraftCore;
import team.galacticraft.galacticraft.core.items.IShiftDescription;
import team.galacticraft.galacticraft.core.items.ISortable;
import team.galacticraft.galacticraft.core.network.PacketSimple;
import team.galacticraft.galacticraft.core.tile.TileEntityFluidPipe;
import team.galacticraft.galacticraft.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import team.galacticraft.galacticraft.core.util.OxygenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockPlaceContext;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Random;

public class BlockFluidPipe extends BlockTransmitter implements IShiftDescription, ISortable
{
    public static final BooleanProperty MIDDLE = BooleanProperty.create("middle");
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public static boolean ignoreDrop = false;

    private final EnumPipeMode mode;

    private static final float MIN = 0.35F;
    private static final float MAX = 0.65F;
    protected static final VoxelShape[] BOUNDING_BOXES = new VoxelShape[]{

            Shapes.box(MIN, MIN, MIN, MAX, MAX, MAX),  // No connection                                  000000
            Shapes.box(MIN, MIN, MIN, MAX, MAX, 1.0D), // South                                          000001
            Shapes.box(0.0D, MIN, MIN, MAX, MAX, MAX), // West                                           000010
            Shapes.box(0.0D, MIN, MIN, MAX, MAX, 1.0D), // South West                                    000011
            Shapes.box(MIN, MIN, 0.0D, MAX, MAX, MAX), // North                                          000100
            Shapes.box(MIN, MIN, 0.0D, MAX, MAX, 1.0D), // North South                                   000101
            Shapes.box(0.0D, MIN, 0.0D, MAX, MAX, MAX), // North West                                    000110
            Shapes.box(0.0D, MIN, 0.0D, MAX, MAX, 1.0D), // North South West                             000111
            Shapes.box(MIN, MIN, MIN, 1.0D, MAX, MAX), // East                                           001000
            Shapes.box(MIN, MIN, MIN, 1.0D, MAX, 1.0D), // East South                                    001001
            Shapes.box(0.0D, MIN, MIN, 1.0D, MAX, MAX), // West East                                     001010
            Shapes.box(0.0D, MIN, MIN, 1.0D, MAX, 1.0D), // South West East                              001011
            Shapes.box(MIN, MIN, 0.0D, 1.0D, MAX, MAX), // North East                                    001100
            Shapes.box(MIN, MIN, 0.0D, 1.0D, MAX, 1.0D), // North South East                             001101
            Shapes.box(0.0D, MIN, 0.0D, 1.0D, MAX, MAX), // North East West                              001110
            Shapes.box(0.0D, MIN, 0.0D, 1.0D, MAX, 1.0D), // North South East West                       001111

            Shapes.box(MIN, 0.0D, MIN, MAX, MAX, MAX),  // Down                                          010000
            Shapes.box(MIN, 0.0D, MIN, MAX, MAX, 1.0D), // Down South                                    010001
            Shapes.box(0.0D, 0.0D, MIN, MAX, MAX, MAX), // Down West                                     010010
            Shapes.box(0.0D, 0.0D, MIN, MAX, MAX, 1.0D), // Down South West                              010011
            Shapes.box(MIN, 0.0D, 0.0D, MAX, MAX, MAX), // Down North                                    010100
            Shapes.box(MIN, 0.0D, 0.0D, MAX, MAX, 1.0D), // Down North South                             010101
            Shapes.box(0.0D, 0.0D, 0.0D, MAX, MAX, MAX), // Down North West                              010110
            Shapes.box(0.0D, 0.0D, 0.0D, MAX, MAX, 1.0D), // Down North South West                       010111
            Shapes.box(MIN, 0.0D, MIN, 1.0D, MAX, MAX), // Down East                                     011000
            Shapes.box(MIN, 0.0D, MIN, 1.0D, MAX, 1.0D), // Down East South                              011001
            Shapes.box(0.0D, 0.0D, MIN, 1.0D, MAX, MAX), // Down West East                               011010
            Shapes.box(0.0D, 0.0D, MIN, 1.0D, MAX, 1.0D), // Down South West East                        011011
            Shapes.box(MIN, 0.0D, 0.0D, 1.0D, MAX, MAX), // Down North East                              011100
            Shapes.box(MIN, 0.0D, 0.0D, 1.0D, MAX, 1.0D), // Down North South East                       011101
            Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, MAX, MAX), // Down North East West                        011110
            Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, MAX, 1.0D), // Down North South East West                 011111

            Shapes.box(MIN, MIN, MIN, MAX, 1.0D, MAX),  // Up                                            100000
            Shapes.box(MIN, MIN, MIN, MAX, 1.0D, 1.0D), // Up South                                      100001
            Shapes.box(0.0D, MIN, MIN, MAX, 1.0D, MAX), // Up West                                       100010
            Shapes.box(0.0D, MIN, MIN, MAX, 1.0D, 1.0D), // Up South West                                100011
            Shapes.box(MIN, MIN, 0.0D, MAX, 1.0D, MAX), // Up North                                      100100
            Shapes.box(MIN, MIN, 0.0D, MAX, 1.0D, 1.0D), // Up North South                               100101
            Shapes.box(0.0D, MIN, 0.0D, MAX, 1.0D, MAX), // Up North West                                100110
            Shapes.box(0.0D, MIN, 0.0D, MAX, 1.0D, 1.0D), // Up North South West                         100111
            Shapes.box(MIN, MIN, MIN, 1.0D, 1.0D, MAX), // Up East                                       101000
            Shapes.box(MIN, MIN, MIN, 1.0D, 1.0D, 1.0D), // Up East South                                101001
            Shapes.box(0.0D, MIN, MIN, 1.0D, 1.0D, MAX), // Up West East                                 101010
            Shapes.box(0.0D, MIN, MIN, 1.0D, 1.0D, 1.0D), // Up South West East                          101011
            Shapes.box(MIN, MIN, 0.0D, 1.0D, 1.0D, MAX), // Up North East                                101100
            Shapes.box(MIN, MIN, 0.0D, 1.0D, 1.0D, 1.0D), // Up North South East                         101101
            Shapes.box(0.0D, MIN, 0.0D, 1.0D, 1.0D, MAX), // Up North East West                          101110
            Shapes.box(0.0D, MIN, 0.0D, 1.0D, 1.0D, 1.0D), // Up North South East West                   101111

            Shapes.box(MIN, 0.0D, MIN, MAX, 1.0D, MAX),  // Up Down                                      110000
            Shapes.box(MIN, 0.0D, MIN, MAX, 1.0D, 1.0D), // Up Down South                                110001
            Shapes.box(0.0D, 0.0D, MIN, MAX, 1.0D, MAX), // Up Down West                                 110010
            Shapes.box(0.0D, 0.0D, MIN, MAX, 1.0D, 1.0D), // Up Down South West                          110011
            Shapes.box(MIN, 0.0D, 0.0D, MAX, 1.0D, MAX), // Up Down North                                110100
            Shapes.box(MIN, 0.0D, 0.0D, MAX, 1.0D, 1.0D), // Up Down North South                         110101
            Shapes.box(0.0D, 0.0D, 0.0D, MAX, 1.0D, MAX), // Up Down North West                          110110
            Shapes.box(0.0D, 0.0D, 0.0D, MAX, 1.0D, 1.0D), // Up Down North South West                   110111
            Shapes.box(MIN, 0.0D, MIN, 1.0D, 1.0D, MAX), // Up Down East                                 111000
            Shapes.box(MIN, 0.0D, MIN, 1.0D, 1.0D, 1.0D), // Up Down East South                          111001
            Shapes.box(0.0D, 0.0D, MIN, 1.0D, 1.0D, MAX), // Up Down West East                           111010
            Shapes.box(0.0D, 0.0D, MIN, 1.0D, 1.0D, 1.0D), // Up Down South West East                    111011
            Shapes.box(MIN, 0.0D, 0.0D, 1.0D, 1.0D, MAX), // Up Down North East                          111100
            Shapes.box(MIN, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), // Up Down North South East                   111101
            Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, MAX), // Up Down North East West                    111110
            Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)}; // Up Down North South East West            111111

    public BlockFluidPipe(Properties builder, EnumPipeMode mode)
    {
        super(builder);
        this.registerDefaultState(stateDefinition.any().setValue(COLOR, DyeColor.WHITE));
        this.mode = mode;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
//        state = this.getActualState(state, source, pos);
        return BOUNDING_BOXES[getBoundingBoxIdx(state)];
    }

    private static int getBoundingBoxIdx(BlockState state)
    {
        int i = 0;

        if (state.getValue(NORTH).booleanValue())
        {
            i |= 1 << Direction.NORTH.get2DDataValue();
        }

        if (state.getValue(EAST).booleanValue())
        {
            i |= 1 << Direction.EAST.get2DDataValue();
        }

        if (state.getValue(SOUTH).booleanValue())
        {
            i |= 1 << Direction.SOUTH.get2DDataValue();
        }

        if (state.getValue(WEST).booleanValue())
        {
            i |= 1 << Direction.WEST.get2DDataValue();
        }

        if (state.getValue(DOWN).booleanValue())
        {
            i |= 1 << 4;
        }

        if (state.getValue(UP).booleanValue())
        {
            i |= 1 << 5;
        }

        return i;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        final TileEntityFluidPipe tile = (TileEntityFluidPipe) worldIn.getBlockEntity(pos);
        DyeColor pipeColor = state.getValue(COLOR);

        if (!ignoreDrop && tile != null && pipeColor != DyeColor.WHITE)
        {
            spawnItem(worldIn, pos, pipeColor);
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return Item.getItemFromBlock(GCBlocks.oxygenPipe);
//        //Never drop the 'pull' variety of pipe
//    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        worldIn.getChunkSource().getLightEngine().checkBlock(pos);
//        worldIn.notifyLightSet(pos); TODO Light set? Does above work?
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        if (this.mode == EnumPipeMode.NORMAL)
//        {
//            return GalacticraftCore.galacticraftBlocksTab;
//        }
//
//        return null;
//    }

    @Override
    public InteractionResult onUseWrench(Level world, BlockPos pos, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        if (!world.isClientSide)
        {
            TileEntityFluidPipe tile = (TileEntityFluidPipe) world.getBlockEntity(pos);
            tile.switchType();
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit)
    {
        final TileEntityFluidPipe tileEntity = (TileEntityFluidPipe) worldIn.getBlockEntity(pos);

        if (super.use(state, worldIn, pos, playerIn, hand, hit) == InteractionResult.SUCCESS)
        {
            return InteractionResult.SUCCESS;
        }

        if (!worldIn.isClientSide)
        {
            final ItemStack stack = playerIn.inventory.getSelected();

            if (!stack.isEmpty())
            {
                if (stack.getItem() instanceof DyeItem)
                {
                    final DyeColor dyeColor = ((DyeItem) stack.getItem()).getDyeColor();

                    final DyeColor colorBefore = DyeColor.byId(tileEntity.getColor(state));

                    tileEntity.onColorUpdate();

                    worldIn.setBlockAndUpdate(pos, state.setValue(COLOR, dyeColor));

                    GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(PacketSimple.EnumSimplePacket.C_RECOLOR_PIPE, GCCoreUtil.getDimensionType(worldIn), new Object[]{pos}), new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 40.0, GCCoreUtil.getDimensionType(worldIn)));

                    if (colorBefore != dyeColor && !playerIn.abilities.instabuild)
                    {
                        playerIn.inventory.getSelected().shrink(1);
                    }

                    if (colorBefore != dyeColor && colorBefore != DyeColor.WHITE)
                    {
                        spawnItem(worldIn, pos, colorBefore);
                    }

                    //					GCCorePacketManager.sendPacketToClients(GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, tileEntity, tileEntity.getColor(), (byte) -1)); TODO Fix pipe color

                    BlockPos tileVec = tileEntity.getBlockPos();
                    for (final Direction dir : Direction.values())
                    {
                        final BlockEntity tileAt = worldIn.getBlockEntity(tileVec.relative(dir));

                        if (tileAt != null && tileAt instanceof IColorable)
                        {
                            ((IColorable) tileAt).onAdjacentColorChanged(dir);
                        }
                    }

                    return InteractionResult.SUCCESS;
                }
            }

        }

        return InteractionResult.PASS;
    }

    private void spawnItem(Level worldIn, BlockPos pos, DyeColor colorBefore)
    {
        final float f = 0.7F;
        Random syncRandom = GCCoreUtil.getRandom(pos);
        final double d0 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
        final double d1 = syncRandom.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
        final double d2 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
        final ItemEntity entityitem = new ItemEntity(worldIn, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, new ItemStack(DyeItem.byColor(colorBefore), 1));
        entityitem.setDefaultPickUpDelay();
        worldIn.addFreshEntity(entityitem);
    }

//    @Override
//    public boolean shouldSideBeRendered(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
//    {
//        return true;
//    }

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
//
//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityFluidPipe();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
//    {
//        return this.getCollisionBoundingBox(worldIn.getBlockState(pos), worldIn, pos);
//    }

    @Override
    public NetworkType getNetworkType(BlockState state)
    {
        return NetworkType.FLUID;
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return I18n.get(this.getDescriptionId() + ".description");
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(COLOR, UP, DOWN, NORTH, EAST, SOUTH, WEST, MIDDLE);
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(COLOR, DyeColor.byId(meta));
//    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockEntity[] connectable = OxygenUtil.getAdjacentFluidConnections(new BlockVec3(context.getClickedPos()), context.getLevel(), false);

        return defaultBlockState().setValue(COLOR, DyeColor.WHITE)
                .setValue(DOWN, connectable[Direction.DOWN.ordinal()] != null)
                .setValue(UP, connectable[Direction.UP.ordinal()] != null)
                .setValue(NORTH, connectable[Direction.NORTH.ordinal()] != null)
                .setValue(EAST, connectable[Direction.EAST.ordinal()] != null)
                .setValue(SOUTH, connectable[Direction.SOUTH.ordinal()] != null)
                .setValue(WEST, connectable[Direction.WEST.ordinal()] != null);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        BlockEntity tileEntity = worldIn.getBlockEntity(currentPos);

        if (tileEntity instanceof ITransmitter)
        {
            BlockEntity[] connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);

            return stateIn.setValue(DOWN, connectable[Direction.DOWN.ordinal()] != null)
                    .setValue(UP, connectable[Direction.UP.ordinal()] != null)
                    .setValue(NORTH, connectable[Direction.NORTH.ordinal()] != null)
                    .setValue(EAST, connectable[Direction.EAST.ordinal()] != null)
                    .setValue(SOUTH, connectable[Direction.SOUTH.ordinal()] != null)
                    .setValue(WEST, connectable[Direction.WEST.ordinal()] != null);
        }

        return stateIn;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.TRANSMITTER;
    }

    public EnumPipeMode getMode()
    {
        return mode;
    }

    public enum EnumPipeMode implements StringRepresentable
    {
        NORMAL(0, "normal"),
        PULL(1, "pull");

        private final int meta;
        private final String name;

        EnumPipeMode(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return meta;
        }

        public static EnumPipeMode byMetadata(int ordinal)
        {
            return EnumPipeMode.values()[ordinal];
        }

        @Override
        public String getSerializedName()
        {
            return this.name;
        }
    }
}
