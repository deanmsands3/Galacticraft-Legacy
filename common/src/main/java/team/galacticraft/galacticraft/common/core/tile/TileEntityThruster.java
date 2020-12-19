package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraftforge.registries.ObjectHolder;

public class TileEntityThruster extends BlockEntity
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.spinThruster)
    public static BlockEntityType<TileEntityThruster> TYPE;

    public TileEntityThruster()
    {
        super(TYPE);
    }

    //    @Override
//    public boolean canUpdate()
//    {
//        return false;
//    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }
}
