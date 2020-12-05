package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockBossSpawner;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityDungeonSpawnerVenus;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;

public class BlockBossSpawnerVenus extends BlockBossSpawner
{
    public BlockBossSpawnerVenus(Properties builder)
    {
        super(builder);
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityDungeonSpawnerVenus();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
}
