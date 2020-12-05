package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import java.util.Random;

public class BlockIceAsteroids extends HalfTransparentBlock implements ISortable
{
    public BlockIceAsteroids(Properties builder)
    {
        super(builder);
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.TRANSLUCENT;
//    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, BlockEntity te, ItemStack tool)
    {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.025F);

        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) == 0)
        {
            if (worldIn.dimension.getDimension().getType() == DimensionType.NETHER || worldIn.dimension instanceof IGalacticraftDimension)
            {
                worldIn.removeBlock(pos, false);
                return;
            }

            Material material = worldIn.getBlockState(pos.below()).getMaterial();
            if (material.blocksMotion() || material.isLiquid())
            {
                worldIn.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            }
        }
    }

//    @Override
//    public int quantityDropped(Random rand)
//    {
//        return 0;
//    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random)
    {
        if (worldIn.getBrightness(LightLayer.BLOCK, pos) > 13 - state.getLightBlock(worldIn, pos))
        {
            if (GCCoreUtil.getDimensionType(worldIn) == DimensionType.NETHER || worldIn.dimension instanceof IGalacticraftDimension)
            {
                worldIn.removeBlock(pos, false);
                return;
            }

            popResource(worldIn, pos, new ItemStack(this));
//            this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
            worldIn.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state)
    {
        return PushReaction.NORMAL;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }
}
