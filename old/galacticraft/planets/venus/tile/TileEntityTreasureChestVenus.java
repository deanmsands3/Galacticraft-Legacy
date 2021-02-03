package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlockNames;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityTreasureChestVenus extends TileEntityTreasureChest
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + VenusBlockNames.TIER_3_TREASURE_CHEST)
    public static BlockEntityType<TileEntityTreasureChestVenus> TYPE;

    public TileEntityTreasureChestVenus()
    {
        super(TYPE, 3);
    }
}