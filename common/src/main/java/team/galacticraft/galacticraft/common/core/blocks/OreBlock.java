package team.galacticraft.galacticraft.common.core.blocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.items.ISortable;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class OreBlock extends Block implements ISortable
{
    public OreBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void spawnAfterBreak(BlockState state, Level world, BlockPos pos, ItemStack stack)
    {
        if (this == GCBlocks.oreSilicon)
        {
            this.popExperience(world, pos, Mth.nextInt(world.getRandom(), 2, 5));
        }
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.ORE;
    }
}
