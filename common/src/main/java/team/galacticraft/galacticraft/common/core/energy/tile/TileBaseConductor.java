package team.galacticraft.galacticraft.common.core.energy.tile;

import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.transmission.grid.IElectricityNetwork;
import team.galacticraft.galacticraft.common.api.transmission.grid.IGridNetwork;
import team.galacticraft.galacticraft.common.api.transmission.tile.IConductor;
import team.galacticraft.galacticraft.common.api.transmission.tile.IConnector;
import team.galacticraft.galacticraft.common.api.transmission.tile.INetworkProvider;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.core.energy.grid.EnergyNetwork;
import team.galacticraft.galacticraft.core.tick.TickHandlerServer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * This tile entity pre-fabricated for all conductors.
 *
 * @author Calclavia
 */
@SuppressWarnings({"rawtypes"})
public abstract class TileBaseConductor extends BlockEntity implements IConductor
{
    protected IGridNetwork network;

    public BlockEntity[] adjacentConnections = null;

    public TileBaseConductor(BlockEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void clearRemoved()
    {
        super.clearRemoved();
        if (!this.level.isClientSide)
        {
            TickHandlerServer.energyTransmitterUpdates.add(this);
        }
    }

    @Override
    public void setRemoved()
    {
        if (!this.level.isClientSide)
        {
            this.getNetwork().split(this);
        }

        super.setRemoved();
    }

    @Override
    public void onChunkUnloaded()
    {
        super.setRemoved();
        super.onChunkUnloaded();
    }

    @Override
    public IElectricityNetwork getNetwork()
    {
        if (this.network == null)
        {
            EnergyNetwork network = new EnergyNetwork();
            network.getTransmitters().add(this);
            this.setNetwork(network);
        }

        return (IElectricityNetwork) this.network;
    }

    @Override
    public void setNetwork(IGridNetwork network)
    {
        this.network = network;
    }

    @Override
    public void refresh()
    {
        if (!this.level.isClientSide)
        {
            this.adjacentConnections = null;

            this.getNetwork().refresh();

            BlockVec3 thisVec = new BlockVec3(this);
            for (Direction side : Direction.values())
            {
                BlockEntity tileEntity = thisVec.getTileEntityOnSide(this.level, side);

                if (tileEntity instanceof TileBaseConductor && ((TileBaseConductor) tileEntity).canConnect(side.getOpposite(), NetworkType.POWER))
                {
                    IGridNetwork otherNet = ((INetworkProvider) tileEntity).getNetwork();
                    if (!this.getNetwork().equals(otherNet))
                    {
                        if (!otherNet.getTransmitters().isEmpty())
                        {
                            otherNet.merge(this.getNetwork());
                        }
                    }
                }
            }
        }
    }

    @Override
    public BlockEntity[] getAdjacentConnections()
    {
        /**
         * Cache the adjacentConnections.
         */
        if (this.adjacentConnections == null)
        {
            this.adjacentConnections = new BlockEntity[6];

            BlockVec3 thisVec = new BlockVec3(this);
            for (int i = 0; i < 6; i++)
            {
                Direction side = Direction.from3DDataValue(i);
                BlockEntity tileEntity = thisVec.getTileEntityOnSide(this.level, side);

                if (tileEntity instanceof IConnector)
                {
                    if (((IConnector) tileEntity).canConnect(side.getOpposite(), NetworkType.POWER))
                    {
                        this.adjacentConnections[i] = tileEntity;
                    }
                }
            }
        }

        return this.adjacentConnections;
    }

    @Override
    public boolean hasNetwork()
    {
        return this.network != null;
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        return type == NetworkType.POWER;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public AABB getRenderBoundingBox()
    {
        return new AABB(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), this.getBlockPos().getX() + 1, this.getBlockPos().getY() + 1, this.getBlockPos().getZ() + 1);
    }

    @Override
    public NetworkType getNetworkType()
    {
        return NetworkType.POWER;
    }

    @Override
    public boolean canTransmit()
    {
        return true;
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }
}
