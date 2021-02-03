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
import net.minecraft.world.item.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BlockCavernousVine extends Block implements IShearable, IShiftDescription, ISortable
{
    public static final EnumProperty<EnumVineType> VINE_TYPE = EnumProperty.create("vine_type", EnumVineType.class);

    public enum EnumVineType implements StringRepresentable
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
        public String getSerializedName()
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
        this.registerDefaultState(this.stateDefinition.any().setValue(VINE_TYPE, EnumVineType.VINE_0));
    }

    public boolean canBlockStay(Level world, BlockPos pos)
    {
        BlockState stateAbove = world.getBlockState(pos.above());
        return stateAbove.getBlock() == this || stateAbove.getMaterial().isSolid();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (world.getBlockState(pos).canBeReplaced(context) && this.canBlockStay(world, pos))
        {
            return super.getStateForPlacement(context);
        }
        return world.getBlockState(pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        this.checkAndDropBlock(world, pos);
    }

    protected void checkAndDropBlock(Level world, BlockPos pos)
    {
        if (!this.canBlockStay(world, pos))
        {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity)
    {
        if (entity instanceof LivingEntity)
        {
            if (entity instanceof Player && ((Player) entity).abilities.flying)
            {
                return;
            }

            entity.setDeltaMovement(entity.getDeltaMovement().x, 0.06F, entity.getDeltaMovement().z);
            entity.yRot += 0.4F;

            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.POISON, 5, 20, false, true));
        }
    }

    @Override
    public int getLightValue(BlockState state, BlockGetter world, BlockPos pos)
    {
        return this.getVineLight(world, pos);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        return Shapes.empty();
    }

    public int getVineLength(BlockGetter world, BlockPos pos)
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

    public int getVineLight(BlockGetter world, BlockPos pos)
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
    public int getTickDelay(LevelReader world)
    {
        return 50;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand)
    {
        if (!world.isClientSide)
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

            world.setBlock(pos.below(), state.setValue(VINE_TYPE, EnumVineType.byId(this.getVineLength(world, pos))), 2);
            world.getChunkSource().getLightEngine().checkBlock(pos);
        }
        this.checkAndDropBlock(world, pos);
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack itemStack, LevelReader world, BlockPos pos)
    {
        return true;
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack itemStack, LevelAccessor world, BlockPos pos, int fortune)
    {
        List<ItemStack> ret = Lists.newArrayList();
        ret.add(new ItemStack(this));
        return ret;
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity)
    {
        return true;
    }

    @Override
    public String getShiftDescription(ItemStack itemStack)
    {
        return GCCoreUtil.translate(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack itemStack)
    {
        return true;
    }

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