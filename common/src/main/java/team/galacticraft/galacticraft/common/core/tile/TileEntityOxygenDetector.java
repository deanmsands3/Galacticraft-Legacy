package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.world.IGalacticraftDimension;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.blocks.BlockOxygenDetector;
import team.galacticraft.galacticraft.common.core.util.OxygenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
//import net.minecraftforge.registries.ObjectHolder;

public class TileEntityOxygenDetector extends BlockEntity implements TickableBlockEntity
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.oxygenDetector)
    public static BlockEntityType<TileEntityOxygenDetector> TYPE;

    private int ticks = 49;
    private AABB oxygenSearch;

    public TileEntityOxygenDetector()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide && ++this.ticks == 50)
        {
            this.ticks = 0;
            if (this.getBlockState().getBlock() instanceof BlockOxygenDetector)
            {
                boolean oxygenFound = false;
                if (this.level.getDimension() instanceof IGalacticraftDimension && !((IGalacticraftDimension) this.level.getDimension()).hasBreathableAtmosphere())
                {
                    oxygenFound = OxygenUtil.isAABBInBreathableAirBlock(this.level, this.oxygenSearch, false);
                }
                else
                {
                    for (Direction side : Direction.values())
                    {
                        BlockPos offset = this.worldPosition.relative(side, 1);
                        BlockState bs = this.level.getBlockState(offset);
                        if (!bs.isFaceSturdy(level, offset, side.getOpposite())) // TODO Test... Not solid?
//                        if (!bs.getBlock().isSideSolid(bs, world, offset, side.getOpposite()))
                        {
                            oxygenFound = true;
                            break;
                        }
                    }
                }
                this.level.setBlockAndUpdate(this.worldPosition, this.level.getBlockState(worldPosition).setValue(BlockOxygenDetector.ACTIVE, oxygenFound));
            }
        }
    }

    @Override
    public void onLoad()
    {
        this.oxygenSearch = new AABB(this.getBlockPos().getX() - 0.6, this.getBlockPos().getY() - 0.6, this.getBlockPos().getZ() - 0.6, this.getBlockPos().getX() + 1.6, this.getBlockPos().getY() + 1.6, this.getBlockPos().getZ() + 1.6);
    }
}
