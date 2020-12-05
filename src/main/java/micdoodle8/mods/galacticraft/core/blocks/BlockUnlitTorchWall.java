package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BlockUnlitTorchWall extends WallTorchBlock implements IOxygenReliantBlock
{
//    public static final DirectionProperty HORIZONTAL_FACING = HorizontalBlock.HORIZONTAL_FACING;

    public Block litVersion;
    public Block unlitVersion;
    public Block fallback;
    public static HashMap<Block, Block> registeredTorches = new HashMap<>();

    public BlockUnlitTorchWall(Properties builder)
    {
        super(builder);
    }

    public static void register(BlockUnlitTorchWall unlittorch, BlockUnlitTorchWall littorch, Block vanillatorch)
    {
        littorch.litVersion = littorch;
        littorch.unlitVersion = unlittorch;
        littorch.fallback = vanillatorch;
        unlittorch.litVersion = littorch;
        unlittorch.unlitVersion = unlittorch;
        unlittorch.fallback = vanillatorch;
        registeredTorches.put(littorch, vanillatorch);
        GCBlocks.itemChanges.put(unlittorch, littorch);
    }

//    @Override
//    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
//    {
//        if (state.getBlock().getMetaFromState(state) == 0)
//        {
//            this.onBlockAdded(worldIn, pos, state);
//        }
//        else
//        {
//            this.checkOxygen(worldIn, pos, state);
//        }
//    }

//    @Override
//    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
//    {
//        if (this.checkForDrop(worldIn, pos, state))
//        {
//            this.checkOxygen(worldIn, pos, state);
//        }
//    }

//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        if (this.checkForDrop(worldIn, pos, state))
//        {
//            Direction enumfacing = state.get(FACING);
//            Direction.Axis enumfacingAxis = enumfacing.getAxis();
//            Direction enumfacing1 = enumfacing.getOpposite();
//            boolean flag = false;
//
//            if (enumfacingAxis.isHorizontal() && !worldIn.isSideSolid(pos.offset(enumfacing1), enumfacing, true))
//            {
//                flag = true;
//            }
//            else if (enumfacingAxis.isVertical() && !this.canPlaceOn(worldIn, pos.offset(enumfacing1)))
//            {
//                flag = true;
//            }
//
//            if (flag)
//            {
//                this.dropBlockAsItem(worldIn, pos, state, 0);
//                worldIn.removeBlock(pos, false);
//            }
//            else
//            {
//                this.checkOxygen(worldIn, pos, state);
//            }
//        }
//    }

    private void checkOxygen(Level world, BlockPos pos, BlockState state)
    {
        if (world.getDimension() instanceof IGalacticraftDimension)
        {
            if (OxygenUtil.checkTorchHasOxygen(world, pos))
            {
                this.onOxygenAdded(world, pos, state);
            }
            else
            {
                this.onOxygenRemoved(world, pos, state);
            }
        }
        else
        {
            Direction enumfacing = state.getValue(FACING);
            world.setBlock(pos, this.fallback.defaultBlockState().setValue(FACING, enumfacing), 2);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand)
    {
        Direction enumfacing = stateIn.getValue(FACING);
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.7D;
        double d2 = (double) pos.getZ() + 0.5D;
        double d3 = 0.22D;
        double d4 = 0.27D;

        Direction enumfacing1 = enumfacing.getOpposite();
        worldIn.addParticle(ParticleTypes.SMOKE, d0 + d4 * (double) enumfacing1.getStepX(), d1 + d3, d2 + d4 * (double) enumfacing1.getStepZ(), 0.0D, 0.0D, 0.0D);
        if (this == GCBlocks.unlitTorchLit)
        {
            worldIn.addParticle(ParticleTypes.FLAME, d0 + d4 * (double) enumfacing1.getStepX(), d1 + d3, d2 + d4 * (double) enumfacing1.getStepZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void onOxygenRemoved(Level world, BlockPos pos, BlockState state)
    {
        if (world.getDimension() instanceof IGalacticraftDimension)
        {
            Direction enumfacing = state.getValue(FACING);
            world.setBlock(pos, this.unlitVersion.defaultBlockState().setValue(FACING, enumfacing), 2);
        }
    }

    @Override
    public void onOxygenAdded(Level world, BlockPos pos, BlockState state)
    {
        if (world.getDimension() instanceof IGalacticraftDimension)
        {
            Direction enumfacing = state.getValue(FACING);
            world.setBlock(pos, this.litVersion.defaultBlockState().setValue(FACING, enumfacing), 2);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
        return this.litVersion.getDrops(state, builder);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        this.checkOxygen(worldIn, pos, state);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        this.checkOxygen(worldIn, pos, state);
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }
}
