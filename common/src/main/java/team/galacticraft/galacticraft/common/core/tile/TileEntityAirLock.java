package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraftforge.registries.ObjectHolder;

public class TileEntityAirLock extends TileEntityAdvanced
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.airLockFrame)
    public static BlockEntityType<TileEntityAirLock> TYPE;

    public TileEntityAirLock()
    {
        super(TYPE);
    }

    @Override
    public double getPacketRange()
    {
        return 0;
    }

    @Override
    public int getPacketCooldown()
    {
        return 0;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }
}
