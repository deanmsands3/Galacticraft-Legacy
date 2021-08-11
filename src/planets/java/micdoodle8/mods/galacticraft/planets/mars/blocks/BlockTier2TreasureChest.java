package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.blocks.BlockTier1TreasureChest;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTreasureChestMars;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTier2TreasureChest extends BlockTier1TreasureChest
{
    public BlockTier2TreasureChest(String assetName)
    {
        super(assetName);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityTreasureChestMars();
    }
}