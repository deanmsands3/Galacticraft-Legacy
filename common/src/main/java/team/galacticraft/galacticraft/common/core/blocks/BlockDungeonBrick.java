package team.galacticraft.galacticraft.common.core.blocks;

import team.galacticraft.galacticraft.common.core.items.ISortable;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;

public class BlockDungeonBrick extends Block implements ISortable
{
    public BlockDungeonBrick(Properties properties)
    {
        super(properties);
    }

    @Override
    public MaterialColor getMapColor(BlockState state, BlockGetter worldIn, BlockPos pos)
    {
        return MaterialColor.TERRACOTTA_RED;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.BRICKS;
    }
}
