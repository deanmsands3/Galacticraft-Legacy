package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockCavernousVine extends Block implements IShearable, IShiftDescription, ISortable
{
    public static final EnumProperty<EnumVineType> VINE_TYPE = EnumProperty.create("vinetype", EnumVineType.class);

    public enum EnumVineType implements StringRepresentable
    {
        VINE_0(0, "vine_0"),
        VINE_1(1, "vine_1"),
        VINE_2(2, "vine_2");

        private final int meta;
        private final String name;

        EnumVineType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        private final static EnumVineType[] values = values();

        public static EnumVineType byId(int id)
        {
            return values[id % values.length];
        }

        @Override
        public String getSerializedName()
        {
            return this.name;
        }
    }

    public BlockCavernousVine(Properties builder)
    {
        super(builder);
    }

    @Override
    public boolean removedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid)
    {
        if (world.removeBlock(pos, false))
        {
            int y2 = pos.getY() - 1;
            while (world.getBlockState(new BlockPos(pos.getX(), y2, pos.getZ())).getBlock() == this)
            {
                world.removeBlock(new BlockPos(pos.getX(), y2, pos.getZ()), false);
                y2--;
            }

            return true;
        }

        return false;
    }

    public boolean canBlockStay(Level worldIn, BlockPos pos)
    {
        BlockState stateAbove = worldIn.getBlockState(pos.above());
        return (stateAbove.getBlock() == this || stateAbove.getMaterial().isSolid());
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);

        if (!this.canBlockStay(worldIn, pos))
        {
            worldIn.removeBlock(pos, false);
        }
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn)
    {
        if (entityIn instanceof LivingEntity)
        {
            if (entityIn instanceof Player && ((Player) entityIn).abilities.flying)
            {
                return;
            }

//            entityIn.motionY = 0.06F;
            entityIn.setDeltaMovement(entityIn.getDeltaMovement().x, 0.06F, entityIn.getDeltaMovement().z);
            entityIn.yRot += 0.4F;

            ((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.POISON, 5, 20, false, true));
        }
    }

    @Override
    public int getLightValue(BlockState state, BlockGetter world, BlockPos pos)
    {
        return this.getVineLight(world, pos);
    }

//    @OnlyIn(Dist.CLIENT)
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
//    public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockReader worldIn, BlockPos pos)
//    {
//        return null;
//    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return Shapes.empty();
    }

//    @Override
//    public boolean canPlaceBlockOnSide(World world, BlockPos pos, Direction facing)
//    {
//        return facing == Direction.DOWN && world.getBlockState(pos.up()).getBlockFaceShape(world, pos.up(), facing) == BlockFaceShape.SOLID;
//    }

    public int getVineLength(BlockGetter world, BlockPos pos)
    {
        int vineCount = 0;
        int y2 = pos.getY();

        while (world.getBlockState(new BlockPos(pos.getX(), y2, pos.getZ())).getBlock() == MarsBlocks.vine)
        {
            vineCount++;
            y2++;
        }

        return vineCount;
    }

    public int getVineLight(BlockGetter world, BlockPos pos)
    {
        int vineCount = 0;
        int y2 = pos.getY();

        while (world.getBlockState(new BlockPos(pos.getX(), y2, pos.getZ())).getBlock() == MarsBlocks.vine)
        {
            vineCount += 4;
            y2--;
        }

        return Math.max(19 - vineCount, 0);
    }

//    @Override
//    public int tickRate(World par1World)
//    {
//        return 50;
//    }


    @Override
    public int getTickDelay(LevelReader worldIn)
    {
        return 50;
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random)
    {
        if (!worldIn.isClientSide)
        {
            for (int y2 = pos.getY() - 1; y2 >= pos.getY() - 2; y2--)
            {
                BlockPos pos1 = new BlockPos(pos.getX(), y2, pos.getZ());
                Block blockID = worldIn.getBlockState(pos1).getBlock();

                if (!blockID.isAir(worldIn.getBlockState(pos1), worldIn, pos1))
                {
                    return;
                }
            }

            worldIn.setBlock(pos.below(), state.setValue(VINE_TYPE, EnumVineType.byId(this.getVineLength(worldIn, pos))), 2);
            worldIn.getChunkSource().getLightEngine().checkBlock(pos);
        }

    }

//    @Override
//    public void onBlockAdded(World world, int x, int y, int z)
//    {
//        if (!world.isRemote)
//        {
//            // world.scheduleBlockUpdate(x, y, z, this,
//            // this.tickRate(world) + world.rand.nextInt(10));
//        }
//    }

//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return Item.getItemFromBlock(Blocks.AIR);
//    }
//
//    @Override
//    public int quantityDropped(Random par1Random)
//    {
//        return 0;
//    }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, LevelReader world, BlockPos pos)
    {
        return true;
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, LevelAccessor world, BlockPos pos, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<>();
        ret.add(new ItemStack(this, 1));
        return ret;
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity)
    {
        return true;
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

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(VINE_TYPE, EnumVineType.byMetadata(meta));
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(VINE_TYPE);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
