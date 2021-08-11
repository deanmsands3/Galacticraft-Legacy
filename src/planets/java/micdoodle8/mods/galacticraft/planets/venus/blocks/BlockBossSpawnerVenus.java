package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.blocks.BlockBossSpawner;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityDungeonSpawnerVenus;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBossSpawnerVenus extends BlockBossSpawner
{
    public BlockBossSpawnerVenus(String assetName)
    {
        super(assetName);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityDungeonSpawnerVenus();
    }
}
