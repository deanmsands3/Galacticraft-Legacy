package team.galacticraft.galacticraft.common.core.blocks;

import team.galacticraft.galacticraft.core.GCBlocks;
import team.galacticraft.galacticraft.core.items.ISortable;
import team.galacticraft.galacticraft.core.util.EnumSortCategory;
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
    public int getExpDrop(BlockState state, LevelReader world, BlockPos pos, int fortune, int silktouch)
    {
        if (this == GCBlocks.oreSilicon)
        {
            Mth.nextInt(RANDOM, 2, 5);
        }

        return super.getExpDrop(state, world, pos, fortune, silktouch);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.ORE;
    }
}
