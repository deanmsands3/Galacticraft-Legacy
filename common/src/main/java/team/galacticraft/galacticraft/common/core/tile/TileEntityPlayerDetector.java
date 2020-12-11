package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.blocks.BlockConcealedDetector;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityPlayerDetector extends BlockEntity implements TickableBlockEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.concealedDetector)
    public static BlockEntityType<TileEntityPlayerDetector> TYPE;

    private int ticks = 24;
    private AABB playerSearch;
    private boolean result = false;

    public TileEntityPlayerDetector()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide && ++this.ticks >= 25)
        {
            this.ticks = 0;
            Direction facing = Direction.NORTH;
            BlockState state = this.level.getBlockState(this.worldPosition);
            if (state.getBlock() == GCBlocks.concealedDetector)
            {
                facing = state.getValue(BlockConcealedDetector.FACING);
            }
            int x = this.getBlockPos().getX();
            double y = this.getBlockPos().getY();
            int z = this.getBlockPos().getZ();
            double range = 14D;
            double hysteresis = result ? 3D : 0D;
            switch (facing)
            {
            case SOUTH:
                this.playerSearch = new AABB(x - range / 2 + 0.5D - hysteresis, y - 6 - hysteresis, z - range - hysteresis, x + range / 2 + 0.5D + hysteresis, y + 2 + hysteresis, z + hysteresis);
                break;
            case WEST:
                this.playerSearch = new AABB(x + 1 - hysteresis, y - 6 - hysteresis, z - range / 2 + 0.5D - hysteresis, x + range + 1 + hysteresis, y + 2 + hysteresis, z + range / 2 + 0.5D + hysteresis);
                break;
            case NORTH:
                this.playerSearch = new AABB(x - range / 2 + 0.5D - hysteresis, y - 6 - hysteresis, z + 1 - hysteresis, x + range / 2 + 0.5D + hysteresis, y + 2 + hysteresis, z + range + 1D + hysteresis);
                break;
            case EAST:
                this.playerSearch = new AABB(x - range - hysteresis, y - 6 - hysteresis, z - range / 2 + 0.5D - hysteresis, x + hysteresis, y + 2 + hysteresis, z + range / 2 + 0.5D + hysteresis);
            }
            result = !this.level.getEntitiesOfClass(Player.class, playerSearch).isEmpty();
            if (this.getBlockState().getBlock() instanceof BlockConcealedDetector)
            {
                ((BlockConcealedDetector) this.getBlockState().getBlock()).updateState(this.level, this.getBlockPos(), result);
            }
        }
    }

    public boolean detectingPlayer()
    {
        return result;
    }
}
