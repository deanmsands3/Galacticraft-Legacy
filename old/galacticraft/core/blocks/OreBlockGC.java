package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class OreBlockGC extends Block implements ISortable
{
    public OreBlockGC(Properties properties)
    {
        super(properties);
    }

    @Override
    public int getExpDrop(BlockState state, LevelReader world, BlockPos pos, int fortune, int silktouch)
    {
        if (this == GCBlocks.SILICON_ORE)
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
