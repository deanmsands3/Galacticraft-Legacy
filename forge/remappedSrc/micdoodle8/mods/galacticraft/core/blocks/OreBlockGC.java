package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

public class OreBlockGC extends Block implements ISortable
{
    public OreBlockGC(Properties properties)
    {
        super(properties);
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch)
    {
        if (this == GCBlocks.SILICON_ORE)
        {
            MathHelper.nextInt(RANDOM, 2, 5);
        }

        return super.getExpDrop(state, world, pos, fortune, silktouch);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.ORE;
    }
}
