package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityCryogenicChamber;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class BlockCryoChamber extends BlockTileGC implements IShiftDescription, IPartialSealableBlock, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockCryoChamber(Properties builder)
    {
        super(builder);
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        final BlockEntity var9 = worldIn.getBlockEntity(pos);

        if (var9 instanceof IMultiBlock)
        {
            ((IMultiBlock) var9).onDestroy(var9);
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
//        int metadata = getMetaFromState(state);

//        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
//        int change = Direction.byHorizontalIndex(angle).getOpposite().getHorizontalIndex();

//        worldIn.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 3);
        BlockMulti.onPlacement(worldIn, pos, placer, this);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult onMachineActivated(Level worldIn, BlockPos pos, BlockState state, Player playerIn, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        ((IMultiBlock) worldIn.getBlockEntity(pos)).onActivated(playerIn);
        return InteractionResult.SUCCESS;
    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        EnumMachineType type = state.get(TYPE);
//        return type == EnumMachineType.CRYOGENIC_CHAMBER ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
//    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityCryogenicChamber();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public boolean isBed(BlockState state, BlockGetter world, BlockPos pos, @Nullable Entity player)
    {
        return true;
    }

    @Override
    public Optional<Vec3> getBedSpawnPosition(EntityType<?> entityType, BlockState state, LevelReader world, BlockPos pos, @Nullable LivingEntity sleeper)
    {
        return Optional.of(new Vec3(pos.above()));
    }

    @Override
    public void setBedOccupied(BlockState state, LevelReader world, BlockPos pos, LivingEntity sleeper, boolean occupied)
    {
        BlockEntity tile = world.getBlockEntity(pos);

        if (tile instanceof TileEntityCryogenicChamber)
        {
            ((TileEntityCryogenicChamber) tile).isOccupied = true;
        }
    }

    @Override
    public Direction getBedDirection(BlockState state, LevelReader world, BlockPos pos)
    {
        return state.getValue(FACING);
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public boolean shouldSideBeRendered(BlockState blockState, IWorldReader blockAccess, BlockPos pos, Direction side)
//    {
//        return true;
//    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byHorizontalIndex(meta % 4);
//        EnumMachineType type = EnumMachineType.byMetadata((int) Math.floor(meta / 4.0));
//        return this.getDefaultState().with(FACING, enumfacing).with(TYPE, type);
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level worldIn, BlockPos pos, Random rand)
    {
//        GalacticraftPlanets.addParticle("cryoFreeze", new Vector3(pos.getX() + 0.3 + rand.nextDouble() * 0.4, pos.getY(), pos.getZ() + 0.3 + rand.nextDouble() * 0.4), new Vector3(0.0, 0.05 + rand.nextDouble() * 0.01, 0.0));
//        GalacticraftPlanets.addParticle("cryoFreeze", new Vector3(pos.getX() + 0.3 + rand.nextDouble() * 0.4, pos.getY(), pos.getZ() + 0.3 + rand.nextDouble() * 0.4), new Vector3(0.0, 0.05 + rand.nextDouble() * 0.01, 0.0));
//        GalacticraftPlanets.addParticle("cryoFreeze", new Vector3(pos.getX() + 0.3 + rand.nextDouble() * 0.4, pos.getY(), pos.getZ() + 0.3 + rand.nextDouble() * 0.4), new Vector3(0.0, 0.05 + rand.nextDouble() * 0.01, 0.0));
//
//        GalacticraftPlanets.addParticle("cryoFreeze", new Vector3(pos.getX() + 0.3 + rand.nextDouble() * 0.4, pos.getY() + 2.9F, pos.getZ() + 0.3 + rand.nextDouble() * 0.4), new Vector3(0.0, -0.05 - rand.nextDouble() * 0.01, 0.0));
//        GalacticraftPlanets.addParticle("cryoFreeze", new Vector3(pos.getX() + 0.3 + rand.nextDouble() * 0.4, pos.getY() + 2.9F, pos.getZ() + 0.3 + rand.nextDouble() * 0.4), new Vector3(0.0, -0.05 - rand.nextDouble() * 0.01, 0.0));
//        GalacticraftPlanets.addParticle("cryoFreeze", new Vector3(pos.getX() + 0.3 + rand.nextDouble() * 0.4, pos.getY() + 2.9F, pos.getZ() + 0.3 + rand.nextDouble() * 0.4), new Vector3(0.0, -0.05 - rand.nextDouble() * 0.01, 0.0));
        // TODO Particles
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }

    @Override
    public boolean isSealed(Level world, BlockPos pos, Direction direction)
    {
        return false;
    }
}
