package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.venus.items.VenusItems;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityCrashedProbe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockCrashedProbe extends BlockTileGC implements ISortable
{
//    public static final String CRASHED_PROBE = "crashedProbe";
//    private static final List<WeightedRandomChestContent> CONTENTS = Lists.newArrayList(
//            new WeightedRandomChestContent(MarsItems.marsItemBasic, 3, 3, 6, 5), // Tier 2 plate
//            new WeightedRandomChestContent(GCItems.heavyPlatingTier1, 0, 3, 6, 5), // Tier 1 plate
//            new WeightedRandomChestContent(AsteroidsItems.basicItem, 6, 3, 6, 5), // Titanium plate
//            new WeightedRandomChestContent(Items.IRON_INGOT, 0, 5, 9, 5), // Titanium plate
//            new WeightedRandomChestContent(AsteroidsItems.basicItem, 5, 3, 6, 5)); // Tier 3 plate

//    static
//    {
//        net.minecraftforge.common.ChestGenHooks.init(CRASHED_PROBE, CONTENTS, 4, 6);
//    }
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockCrashedProbe(Properties builder)
    {
        super(builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityCrashedProbe();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand)
    {
        worldIn.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.65, pos.getY() + 1.0, pos.getZ() + 0.9, 0.0, 0.0, 0.0);
        worldIn.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.2, pos.getY() + 1.0, pos.getZ() + 0.2, 0.0, 0.0, 0.0);
        worldIn.addParticle(ParticleTypes.SMOKE, pos.getX() + 1.0, pos.getY() + 0.25, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit)
    {
        if (!worldIn.isClientSide)
        {
            NetworkHooks.openGui((ServerPlayer) playerIn, getMenuProvider(state, worldIn, pos), buf -> buf.writeBlockPos(pos));
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos)
    {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        return tileentity instanceof MenuProvider ? (MenuProvider)tileentity : null;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        final BlockEntity tile = worldIn.getBlockEntity(pos);

        if (tile instanceof TileEntityCrashedProbe && ((TileEntityCrashedProbe) tile).getDropCore())
        {
            spawnItem(worldIn, pos);
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    //Drops a Radioisotope Core as well as the Crashed Probe block
    private void spawnItem(Level worldIn, BlockPos pos)
    {
        final float f = 0.7F;
        Random syncRandom = GCCoreUtil.getRandom(pos);
        final double d0 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
        final double d1 = syncRandom.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
        final double d2 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
        final ItemEntity entityitem = new ItemEntity(worldIn, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, new ItemStack(VenusItems.RADIOISOTOPE_CORE, 1));
        entityitem.setDefaultPickUpDelay();
        worldIn.addFreshEntity(entityitem);
    }
}
