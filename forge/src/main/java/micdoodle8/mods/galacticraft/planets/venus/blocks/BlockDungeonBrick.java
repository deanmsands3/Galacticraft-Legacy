package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
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
