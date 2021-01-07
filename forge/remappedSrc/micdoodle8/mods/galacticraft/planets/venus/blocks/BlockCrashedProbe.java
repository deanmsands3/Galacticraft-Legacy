package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.venus.items.VenusItems;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityCrashedProbe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import ActionResultType;
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
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityCrashedProbe();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        worldIn.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.65, pos.getY() + 1.0, pos.getZ() + 0.9, 0.0, 0.0, 0.0);
        worldIn.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.2, pos.getY() + 1.0, pos.getZ() + 0.2, 0.0, 0.0, 0.0);
        worldIn.addParticle(ParticleTypes.SMOKE, pos.getX() + 1.0, pos.getY() + 0.25, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        if (!worldIn.isRemote)
        {
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, getContainer(state, worldIn, pos), buf -> buf.writeBlockPos(pos));
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider)tileentity : null;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        final TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityCrashedProbe && ((TileEntityCrashedProbe) tile).getDropCore())
        {
            spawnItem(worldIn, pos);
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    //Drops a Radioisotope Core as well as the Crashed Probe block
    private void spawnItem(World worldIn, BlockPos pos)
    {
        final float f = 0.7F;
        Random syncRandom = GCCoreUtil.getRandom(pos);
        final double d0 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
        final double d1 = syncRandom.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
        final double d2 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
        final ItemEntity entityitem = new ItemEntity(worldIn, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, new ItemStack(VenusItems.RADIOISOTOPE_CORE, 1));
        entityitem.setDefaultPickupDelay();
        worldIn.addEntity(entityitem);
    }
}
