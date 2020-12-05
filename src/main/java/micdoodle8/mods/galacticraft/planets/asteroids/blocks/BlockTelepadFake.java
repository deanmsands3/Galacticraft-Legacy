package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockAdvancedTile;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityTelepadFake;
import net.minecraft.block.*;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockTelepadFake extends BlockAdvancedTile
{
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty CONNECTABLE = BooleanProperty.create("connectable");
    protected static final VoxelShape AABB_TOP = Shapes.box(0.0F, 0.55F, 0.0F, 1.0F, 1.0F, 1.0F);
    protected static final VoxelShape AABB_BOTTOM = Shapes.box(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);

    public BlockTelepadFake(Properties builder)
    {
        super(builder);
    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return state.getValue(TOP) ? AABB_TOP : AABB_BOTTOM;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
//    {
//        IBlockState state = world.getBlockState(pos);
//        boolean top = state.get(TOP);
//
//        if (top)
//        {
//            this.setBlockBounds(0.0F, 0.55F, 0.0F, 1.0F, 1.0F, 1.0F);
//        }
//        else
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
//        }
//    }

//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        boolean top = state.get(TOP);
//
//        if (top)
//        {
//            this.setBlockBounds(0.0F, 0.55F, 0.0F, 1.0F, 1.0F, 1.0F);
//            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//        }
//        else
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
//            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//        }
//    }
//
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getCollisionBoundingBox(worldIn, pos, state);
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
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

    public void makeFakeBlock(Level worldObj, BlockPos pos, BlockPos mainBlock, BlockState state)
    {
        worldObj.setBlock(pos, state, 3);
        ((TileEntityTelepadFake) worldObj.getBlockEntity(pos)).setMainBlock(mainBlock);
    }

    @Override
    public float getDestroySpeed(BlockState blockState, BlockGetter worldIn, BlockPos pos)
    {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);

        if (tileEntity instanceof TileEntityTelepadFake)
        {
            BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null)
            {
                return worldIn.getBlockState(mainBlockPosition).getBlock().getDestroySpeed(worldIn.getBlockState(mainBlockPosition), worldIn, mainBlockPosition);
            }
        }

        return this.destroySpeed;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);

        if (tileEntity instanceof TileEntityTelepadFake)
        {
            ((TileEntityTelepadFake) tileEntity).onBlockRemoval();
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit)
    {
        TileEntityTelepadFake tileEntity = (TileEntityTelepadFake) worldIn.getBlockEntity(pos);
        return tileEntity.onActivated(playerIn);
    }

//    @Override
//    public int quantityDropped(Random par1Random)
//    {
//        return 0;
//    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.INVISIBLE;
    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }


    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityTelepadFake();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand)
    {
    }

    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player)
    {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            Block mainBlockID = world.getBlockState(mainBlockPosition).getBlock();

            if (Blocks.AIR != mainBlockID)
            {
                return mainBlockID.getPickBlock(world.getBlockState(mainBlockPosition), target, world, mainBlockPosition, player);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public Direction getBedDirection(BlockState state, LevelReader world, BlockPos pos)
    {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            return world.getBlockState(pos).getBlock().getBedDirection(world.getBlockState(mainBlockPosition), world, mainBlockPosition);
        }

        return state.getValue(DirectionalBlock.FACING);
    }

    @Override
    public boolean isBed(BlockState state, BlockGetter world, BlockPos pos, @Nullable Entity player)
    {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            return world.getBlockState(pos).getBlock().isBed(world.getBlockState(mainBlockPosition), world, mainBlockPosition, player);
        }

        return super.isBed(state, world, pos, player);
    }

    @Override
    public void setBedOccupied(BlockState state, LevelReader world, BlockPos pos, LivingEntity sleeper, boolean occupied)
    {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            world.getBlockState(pos).getBlock().setBedOccupied(state, world, pos, sleeper, occupied);
        }
        else
        {
            super.setBedOccupied(state, world, pos, sleeper, occupied);
        }
    }

    @Override
    public boolean addHitEffects(BlockState state, Level worldObj, HitResult target, ParticleEngine manager)
    {
        BlockEntity tileEntity = worldObj.getBlockEntity(new BlockPos(target.getLocation()));

        if (tileEntity instanceof TileEntityTelepadFake && target instanceof BlockHitResult)
        {
            BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null)
            {
                manager.addBlockHitEffects(mainBlockPosition, (BlockHitResult) target);
            }
        }

        return super.addHitEffects(state, worldObj, target, manager);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(TOP, CONNECTABLE);
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(TOP, meta % 2 == 1).with(CONNECTABLE, meta > 1);
//    }
}