package team.galacticraft.galacticraft.common.core.blocks;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.level.block.EntityBlock;
import team.galacticraft.galacticraft.common.api.vector.Vector3;
import team.galacticraft.galacticraft.common.core.items.IShiftDescription;
import team.galacticraft.galacticraft.common.core.items.ISortable;
import team.galacticraft.galacticraft.common.core.tile.TileEntityFallenMeteor;
import team.galacticraft.galacticraft.common.core.util.ColorUtil;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.Random;

public class BlockFallenMeteor extends Block implements IShiftDescription, ISortable, EntityBlock
{
    private static final VoxelShape BOUNDS = Shapes.box(0.15, 0.05, 0.15, 0.85, 0.75, 0.85);

    public BlockFallenMeteor(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return BOUNDS;
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
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

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

//    @Override
//    public int quantityDroppedWithBonus(int par1, Random par2Random)
//    {
//        return 1 + (int) (par2Random.nextFloat() + 0.75F);
//    }
//
//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return GCItems.meteoricIronRaw;
//    } TODO Meteor drops

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn)
    {
        BlockEntity tile = worldIn.getBlockEntity(pos);

        if (tile instanceof TileEntityFallenMeteor)
        {
            TileEntityFallenMeteor meteor = (TileEntityFallenMeteor) tile;

            if (meteor.getHeatLevel() <= 0)
            {
                return;
            }

            if (entityIn instanceof LivingEntity)
            {
                final LivingEntity livingEntity = (LivingEntity) entityIn;

                worldIn.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.NEUTRAL, 0.5F, 2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F);

                for (int var5 = 0; var5 < 8; ++var5)
                {
                    worldIn.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + Math.random(), pos.getY() + 0.2D + Math.random(), pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                if (!livingEntity.isOnFire())
                {
                    livingEntity.setSecondsOnFire(2);
                }

                double var9 = pos.getX() + 0.5F - livingEntity.getX();
                double var7;

                for (var7 = livingEntity.getZ() - pos.getZ(); var9 * var9 + var7 * var7 < 1.0E-4D; var7 = (Math.random() - Math.random()) * 0.01D)
                {
                    var9 = (Math.random() - Math.random()) * 0.01D;
                }

                livingEntity.knockback(livingEntity, 1, var9, var7);
            }
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack)
    {
        world.setBlock(pos, this.defaultBlockState(), 3);
        BlockEntity tile = world.getBlockEntity(pos);

        if (tile instanceof TileEntityFallenMeteor)
        {
            ((TileEntityFallenMeteor) tile).setHeatLevel(0);
        }
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        worldIn.getBlockTicks().scheduleTick(pos, this, this.getTickDelay(worldIn));
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        worldIn.getBlockTicks().scheduleTick(pos, this, this.getTickDelay(worldIn));
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random)
    {
        if (!worldIn.isClientSide)
        {
            this.tryToFall(worldIn, pos, state);
        }
    }

    private void tryToFall(Level world, BlockPos pos, BlockState state)
    {
        if (this.canFallBelow(world, pos.below()) && pos.getY() >= 0)
        {
            int prevHeatLevel = ((TileEntityFallenMeteor) world.getBlockEntity(pos)).getHeatLevel();
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            BlockPos blockpos1;

            for (blockpos1 = pos.below(); this.canFallBelow(world, blockpos1) && blockpos1.getY() > 0; blockpos1 = blockpos1.below())
            {
            }

            if (blockpos1.getY() >= 0)
            {
                world.setBlock(blockpos1.above(), state, 3);
                ((TileEntityFallenMeteor) world.getBlockEntity(blockpos1.above())).setHeatLevel(prevHeatLevel);
            }
        }
    }

    private boolean canFallBelow(Level world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();

        if (block.getMaterial(world.getBlockState(pos)) == Material.AIR)
        {
            return true;
        }
        else if (block == Blocks.FIRE)
        {
            return true;
        }
        else
        {
            return block.getMaterial(world.getBlockState(pos)) == Material.WATER || block.getMaterial(world.getBlockState(pos)) == Material.LAVA;
        }
    }

    public static int colorMultiplier(BlockGetter worldIn, BlockPos pos)
    {
        if (worldIn != null && pos != null)
        {
            BlockEntity tile = worldIn.getBlockEntity(pos);

            if (tile instanceof TileEntityFallenMeteor)
            {
                TileEntityFallenMeteor meteor = (TileEntityFallenMeteor) tile;

                Vector3 col = new Vector3(198, 108, 58);
                col.translate(200 - meteor.getScaledHeatLevel() * 200);
                col.x = Math.min(255, col.x);
                col.y = Math.min(255, col.y);
                col.z = Math.min(255, col.z);

                return ColorUtil.to32BitColor(255, (byte) col.x, (byte) col.y, (byte) col.z);
            }
        }

        return 16777215;
    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter world)
    {
        return new TileEntityFallenMeteor();
    }

    @Override
    public boolean isEntityBlock()
    {
        return true;
    }

//    @Override
//    public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, World world, BlockPos pos)
//    {
//        float hardness = this.getBlockHardness(state, world, pos);
//        if (hardness < 0.0F)
//        {
//            return 0.0F;
//        }
//
//        int power = canHarvestBlock(this, player, state);
//        if (power > 0)
//        {
//            return power * player.getDigSpeed(state, pos) / hardness / 30F;
//        }
//        else
//        {
//            return player.getDigSpeed(state, pos) / hardness / 30F;
//        }
//    }
//
//    public int canHarvestBlock(Block block, PlayerEntity player, BlockState state)
//    {
//        ItemStack stack = player.inventory.getCurrentItem();
//        String tool = block.getHarvestTool(state);
//        if (stack.isEmpty() || tool == null)
//        {
//            return player.canHarvestBlock(state) ? 1 : 0;
//        }
//
//        int toolLevel = stack.getItem().getHarvestLevel(stack, tool, player, state) - block.getHarvestLevel(state) + 1;
//        if (toolLevel < 1)
//        {
//            return player.canHarvestBlock(state) ? 1 : 0;
//        }
//
//        return toolLevel;
//    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return I18n.get(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }

    @Override
    public void spawnAfterBreak(BlockState state, Level world, BlockPos pos, ItemStack stack)
    {
        if (state.getBlock() != this)
        {
            return;
        }
        this.popExperience(world, pos, Mth.nextInt(world.getRandom(), 3, 7));
    }
}
