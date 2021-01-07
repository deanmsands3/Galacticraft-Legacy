package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BlockCavernousVine extends Block implements IShearable, IShiftDescription, ISortable
{
    public static final EnumProperty<EnumVineType> VINE_TYPE = EnumProperty.create("vine_type", EnumVineType.class);

    public enum EnumVineType implements IStringSerializable
    {
        VINE_0("cavernous_vines_1"),
        VINE_1("cavernous_vines_2"),
        VINE_2("cavernous_vines_3");

        private final String textureName;

        EnumVineType(String textureName)
        {
            this.textureName = textureName;
        }

        private final static EnumVineType[] values = values();

        public static EnumVineType byId(int id)
        {
            return values[id % values.length];
        }

        @Override
        public String getName()
        {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public String getTextureName()
        {
            return this.textureName;
        }
    }

    public BlockCavernousVine(Properties builder)
    {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(VINE_TYPE, EnumVineType.VINE_0));
    }

    public boolean canBlockStay(World world, BlockPos pos)
    {
        BlockState stateAbove = world.getBlockState(pos.up());
        return stateAbove.getBlock() == this || stateAbove.getMaterial().isSolid();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos pos = context.getPos();

        if (world.getBlockState(pos).isReplaceable(context) && this.canBlockStay(world, pos))
        {
            return super.getStateForPlacement(context);
        }
        return world.getBlockState(pos);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        this.checkAndDropBlock(world, pos);
    }

    protected void checkAndDropBlock(World world, BlockPos pos)
    {
        if (!this.canBlockStay(world, pos))
        {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
    {
        if (entity instanceof LivingEntity)
        {
            if (entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.isFlying)
            {
                return;
            }

            entity.setMotion(entity.getMotion().x, 0.06F, entity.getMotion().z);
            entity.rotationYaw += 0.4F;

            ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.POISON, 5, 20, false, true));
        }
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        return this.getVineLight(world, pos);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return VoxelShapes.empty();
    }

    public int getVineLength(IBlockReader world, BlockPos pos)
    {
        int vineCount = 0;
        int y2 = pos.getY();

        while (world.getBlockState(new BlockPos(pos.getX(), y2, pos.getZ())).getBlock() == MarsBlocks.CAVERNOUS_VINES)
        {
            vineCount++;
            y2++;
        }

        return vineCount;
    }

    public int getVineLight(IBlockReader world, BlockPos pos)
    {
        int vineCount = 0;
        int y2 = pos.getY();

        while (world.getBlockState(new BlockPos(pos.getX(), y2, pos.getZ())).getBlock() == MarsBlocks.CAVERNOUS_VINES)
        {
            vineCount += 4;
            y2--;
        }

        return Math.max(19 - vineCount, 0);
    }

    @Override
    public int tickRate(IWorldReader world)
    {
        return 50;
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
    {
        if (!world.isRemote)
        {
            for (int y2 = pos.getY() - 1; y2 >= pos.getY() - 2; y2--)
            {
                BlockPos pos1 = new BlockPos(pos.getX(), y2, pos.getZ());
                Block blockID = world.getBlockState(pos1).getBlock();

                if (!blockID.isAir(world.getBlockState(pos1), world, pos1))
                {
                    return;
                }
            }

            world.setBlockState(pos.down(), state.with(VINE_TYPE, EnumVineType.byId(this.getVineLength(world, pos))), 2);
            world.getChunkProvider().getLightManager().checkBlock(pos);
        }
        this.checkAndDropBlock(world, pos);
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack itemStack, IWorldReader world, BlockPos pos)
    {
        return true;
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack itemStack, IWorld world, BlockPos pos, int fortune)
    {
        List<ItemStack> ret = Lists.newArrayList();
        ret.add(new ItemStack(this));
        return ret;
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity)
    {
        return true;
    }

    @Override
    public String getShiftDescription(ItemStack itemStack)
    {
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack itemStack)
    {
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(VINE_TYPE);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}