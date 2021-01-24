package micdoodle8.mods.galacticraft.planets.mars.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlockNames;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityTreasureChestMars extends TileEntityTreasureChest
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsBlockNames.TIER_2_TREASURE_CHEST)
    public static BlockEntityType<TileEntityTreasureChestMars> TYPE;

    public TileEntityTreasureChestMars()
    {
        super(TYPE, 2);
    }
}