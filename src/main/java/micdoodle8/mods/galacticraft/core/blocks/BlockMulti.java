package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFake;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockCryoChamber;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DirectionalPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BlockMulti extends BlockAdvanced implements IPartialSealableBlock, EntityBlock
{
    public static final EnumProperty<EnumBlockMultiType> MULTI_TYPE = EnumProperty.create("type", EnumBlockMultiType.class);
    public static final IntegerProperty RENDER_TYPE = IntegerProperty.create("rendertype", 0, 7);

    protected static final VoxelShape FULL_BLOCK_AABB = Shapes.box(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    protected static final VoxelShape AABB_PAD = Shapes.box(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
    protected static final VoxelShape AABB_SOLAR = Shapes.box(0.0F, 0.2F, 0.0F, 1.0F, 0.8F, 1.0F);
    protected static final VoxelShape AABB_SOLAR_POLE = Shapes.box(0.3F, 0.0F, 0.3F, 0.7F, 1.0F, 0.7F);
    protected static final VoxelShape AABB_TURRET = Shapes.box(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

    public enum EnumBlockMultiType implements StringRepresentable
    {
        SOLAR_PANEL_0(0, "solar"),
        SPACE_STATION_BASE(1, "ss_base"),
        ROCKET_PAD(2, "rocket_pad"),
        NASA_WORKBENCH(3, "nasa_workbench"),
        SOLAR_PANEL_1(4, "solar_panel"),
        CRYO_CHAMBER(5, "cryo_chamber"),
        BUGGY_FUEL_PAD(6, "buggy_pad"),
        MINER_BASE(7, "miner_base"),  //UNUSED
        DISH_LARGE(8, "dish_large"),
        LASER_TURRET(9, "laser_turret");

        private final int meta;
        private final String name;

        EnumBlockMultiType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        private final static EnumBlockMultiType[] values = values();

        public static EnumBlockMultiType byMetadata(int meta)
        {
            return values[meta % values.length];
        }

        @Override
        public String getSerializedName()
        {
            return this.name;
        }
    }

    public BlockMulti(Properties builder)
    {
        super(builder);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos)
    {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
    {
        return true;
    }

    @Override
    public boolean isSuffocating(BlockState state, BlockGetter worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isRedstoneConductor(BlockState state, BlockGetter worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter worldIn, BlockPos pos, EntityType<?> type)
    {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        switch (state.getValue(MULTI_TYPE))
        {
        case SOLAR_PANEL_0:
        case SOLAR_PANEL_1:
            boolean midPole = worldIn.getBlockState(pos.above()).getBlock() == this;
            boolean topPole = worldIn.getBlockState(pos.below()).getBlock() == this;
            if (topPole || midPole)
            {
                return midPole ? AABB_SOLAR_POLE : AABB_SOLAR;
            }
            else
            {
                return AABB_SOLAR;
            }
        case ROCKET_PAD:
        case BUGGY_FUEL_PAD:
            return AABB_PAD;
        case LASER_TURRET:
            return AABB_TURRET;
        default:
            return FULL_BLOCK_AABB;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter worldIn)
    {
        return null;
    }

    //    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockReader worldIn, BlockPos pos)
//    {
//        int meta = getMetaFromState(worldIn.getBlockState(pos));
//
//        if (meta == 2 || meta == 6)
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
//        }
//        else if (meta == 0 || meta == 4)
//        {
//            this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, worldIn.getBlockState(pos.up()).getBlock() == this ? 1.0F : 0.6F, 0.7F);
//        }
//        else
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//        }
//    }

//    @SuppressWarnings("rawtypes")
//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
//    {
//        int meta = getMetaFromState(state);
//
//        if (meta == 2 || meta == 6)
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
//            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//        }
//        else if (meta == 0 || meta == 4)
//        {
//            this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, worldIn.getBlockState(pos.up()).getBlock() == this ? 1.0F : 0.6F, 0.7F);
//            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//        }
//        /*else if (meta == 7)
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.38F, 1.0F);
//            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
//        }*/
//        else
//        {
//            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//        }
//    }
//
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockReader worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getCollisionBoundingBox(worldIn, pos, state);
//    }
//
//    @Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getSelectedBoundingBox(worldIn, pos);
//    }

    @Override
    public boolean dropFromExplosion(Explosion par1Explosion)
    {
        return false;
    }

    public void makeFakeBlock(Level worldObj, BlockPos pos, BlockPos mainBlock, EnumBlockMultiType type)
    {
        worldObj.setBlockAndUpdate(pos, GCBlocks.fakeBlock.defaultBlockState().setValue(MULTI_TYPE, type));
        worldObj.setBlockEntity(pos, new TileEntityFake(mainBlock));
    }

    public void makeFakeBlock(Level worldObj, Collection<BlockPos> posList, BlockPos mainBlock, EnumBlockMultiType type)
    {
        for (BlockPos pos : posList)
        {
            worldObj.setBlock(pos, this.defaultBlockState().setValue(MULTI_TYPE, type), type == EnumBlockMultiType.CRYO_CHAMBER ? 3 : 0);
            worldObj.setBlockEntity(pos, new TileEntityFake(mainBlock));
        }
    }

    @Override
    public float getDestroySpeed(BlockState blockState, BlockGetter worldIn, BlockPos pos)
    {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);

        if (tileEntity instanceof TileEntityFake)
        {
            BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                return worldIn.getBlockState(mainBlockPosition).getBlock().getDestroySpeed(blockState, worldIn, pos);
            }
        }

        return this.destroySpeed;
    }

    @Override
    public boolean isSealed(Level worldIn, BlockPos pos, Direction direction)
    {
        EnumBlockMultiType type = worldIn.getBlockState(pos).getValue(MULTI_TYPE);

        //Landing pad and refueling pad
        if (type == EnumBlockMultiType.ROCKET_PAD || type == EnumBlockMultiType.BUGGY_FUEL_PAD)
        {
            return direction == Direction.DOWN;
        }

        //Basic solar panel fixed top
        if (type == EnumBlockMultiType.SOLAR_PANEL_1)
        {
            return direction == Direction.UP;
        }

        return false;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);

        if (tileEntity instanceof TileEntityFake)
        {
            ((TileEntityFake) tileEntity).onBlockRemoval();
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public InteractionResult onMachineActivated(Level worldIn, BlockPos pos, BlockState state, Player playerIn, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        TileEntityFake tileEntity = (TileEntityFake) worldIn.getBlockEntity(pos);
        if (tileEntity == null)
        {
            return InteractionResult.PASS;
        }
        return tileEntity.onBlockActivated(worldIn, pos, playerIn);
    }

    @Override
    public InteractionResult onUseWrench(Level world, BlockPos pos, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        TileEntityFake tileEntity = (TileEntityFake) world.getBlockEntity(pos);
        return tileEntity.onBlockWrenched(world, pos, entityPlayer, hand, hit);
    }

//    @Override
//    public int quantityDropped(Random par1Random)
//    {
//        return 0;
//    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public TileEntity createNewTileEntity(World var1, int meta)
//    {
//        return null;
//    }

    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player)
    {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof TileEntityFake)
        {
            BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                BlockState mainBlockState = world.getBlockState(mainBlockPosition);

                if (Blocks.AIR != mainBlockState.getBlock())
                {
                    return mainBlockState.getBlock().getPickBlock(mainBlockState, target, world, mainBlockPosition, player);
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public Direction getBedDirection(BlockState state, LevelReader world, BlockPos pos)
    {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof TileEntityFake)
        {
            BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                BlockState mainState = world.getBlockState(mainBlockPosition);
                return mainState.getBlock().getBedDirection(mainState, world, mainBlockPosition);
            }
        }

        return Direction.UP; // TODO
    }

    @Override
    public boolean isBed(BlockState state, BlockGetter world, BlockPos pos, Entity player)
    {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof TileEntityFake)
        {
            BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                BlockState mainState = world.getBlockState(mainBlockPosition);
                return mainState.getBlock().isBed(state, world, mainBlockPosition, player);
            }
        }

        return super.isBed(state, world, pos, player);
    }

    @Override
    public void setBedOccupied(BlockState state, LevelReader world, BlockPos pos, LivingEntity sleeper, boolean occupied)
    {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
        {
            world.getBlockState(mainBlockPosition).getBlock().setBedOccupied(state, world, pos, sleeper, occupied);
        }
        else
        {
            super.setBedOccupied(state, world, pos, sleeper, occupied);
        }
    }

    @Override
    public Optional<Vec3> getBedSpawnPosition(EntityType<?> entityType, BlockState state, LevelReader world, BlockPos pos, @Nullable LivingEntity sleeper)
    {
        if (!(world instanceof Level))
        {
            return Optional.empty();
        }
        int tries = 3;
        Level worldIn = (Level) world;
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;
        BlockState cryoChamber = worldIn.getBlockState(mainBlockPosition);
        Direction enumfacing = Direction.NORTH;
        if (GalacticraftCore.isPlanetsLoaded && cryoChamber.getBlock() == MarsBlocks.cryoChamber)
        {
            enumfacing = cryoChamber.getValue(BlockCryoChamber.FACING);
        }
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        for (int l = 0; l <= 1; ++l)
        {
            int i1 = i - enumfacing.getStepX() * l - 1;
            int j1 = k - enumfacing.getStepZ() * l - 1;
            int k1 = i1 + 2;
            int l1 = j1 + 2;

            for (int i2 = i1; i2 <= k1; ++i2)
            {
                for (int j2 = j1; j2 <= l1; ++j2)
                {
                    BlockPos blockpos = new BlockPos(i2, j, j2);

                    if (hasRoomForPlayer(worldIn, blockpos))
                    {
                        if (tries <= 0)
                        {
                            return Optional.of(new Vec3(blockpos));
                        }

                        --tries;
                    }
                }
            }
        }

        return Optional.empty();
    }

    private static boolean hasRoomForPlayer(Level worldIn, BlockPos pos)
    {
        return /*worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), Direction.UP) &&*/ !worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.getBlockState(pos.above()).getMaterial().isSolid();
    }

    @Override
    public boolean addHitEffects(BlockState state, Level worldObj, HitResult target, ParticleEngine manager)
    {
        if (target.getType() == HitResult.Type.BLOCK)
        {
            BlockHitResult result = (BlockHitResult) target;
            BlockEntity tileEntity = worldObj.getBlockEntity(result.getBlockPos());

            if (tileEntity instanceof TileEntityFake)
            {
                BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;

                if (mainBlockPosition != null && !mainBlockPosition.equals(result.getBlockPos()))
                {
                    manager.addBlockHitEffects(mainBlockPosition, result);
                }
            }
        }

        return super.addHitEffects(state, worldObj, target, manager);
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(MULTI_TYPE, EnumBlockMultiType.byMetadata(meta));
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(MULTI_TYPE, RENDER_TYPE);
    }

//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        EnumBlockMultiType type = state.get(MULTI_TYPE);
//        int renderType = 0;
//
//        switch (type)
//        {
//        case CRYO_CHAMBER:
//            BlockState stateAbove = worldIn.getBlockState(pos.up());
//            TileEntityFake tile = (TileEntityFake) worldIn.getTileEntity(pos);
//            if (stateAbove.getBlock() == this && (stateAbove.get(MULTI_TYPE)) == EnumBlockMultiType.CRYO_CHAMBER)
//            {
//                renderType = 0;
//            }
//            else
//            {
//                renderType = 4;
//            }
//            if (tile != null && tile.mainBlockPosition != null && GalacticraftCore.isPlanetsLoaded)
//            {
//                BlockState stateMain = worldIn.getBlockState(tile.mainBlockPosition);
//                if (stateMain.getBlock() == MarsBlocks.machine && stateMain.get(BlockMachineMars.TYPE) == BlockMachineMars.EnumMachineType.CRYOGENIC_CHAMBER)
//                {
//                    Direction dir = stateMain.get(BlockMachineMars.FACING);
//                    renderType += dir.getHorizontalIndex();
//                }
//            }
//            break;
//        default:
//            break;
//        }
//
//        return state.with(RENDER_TYPE, renderType);
//    }

    public static void onPlacement(Level worldIn, BlockPos pos, LivingEntity placer, Block callingBlock)
    {
        final BlockEntity tile = worldIn.getBlockEntity(pos);

        if (tile instanceof IMultiBlock)
        {
            boolean validSpot = true;
            List<BlockPos> toCheck = new LinkedList<>();
            ((IMultiBlock) tile).getPositions(pos, toCheck);
            for (BlockPos toTest : toCheck)
            {
                BlockState blockAt = worldIn.getBlockState(toTest);
                if (!blockAt.canBeReplaced(new DirectionalPlaceContext(worldIn, toTest, Direction.DOWN, ItemStack.EMPTY, Direction.UP)))
                {
                    validSpot = false;
                    break;
                }
            }

            if (!validSpot)
            {
                worldIn.removeBlock(pos, false);

                if (!worldIn.isClientSide && placer instanceof ServerPlayer)
                {
                    ServerPlayer player = (ServerPlayer) placer;
                    player.sendMessage(new TextComponent(EnumColor.RED + GCCoreUtil.translate("gui.warning.noroom")));
                    if (!player.abilities.instabuild)
                    {
                        final ItemStack nasaWorkbench = new ItemStack(callingBlock, 1);
                        final ItemEntity entityitem = player.drop(nasaWorkbench, false);
                        entityitem.setPickUpDelay(0);
                        entityitem.setOwner(player.getUUID());
                    }
                }

                return;
            }

            ((IMultiBlock) tile).onCreate(worldIn, pos);
        }
    }
}
