package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import micdoodle8.mods.galacticraft.planets.venus.tick.VenusTickHandlerServer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TileEntitySolarTransmitter extends TileEntityAdvanced implements ITransmitter
{
    private IGridNetwork network;
    public BlockEntity[] adjacentConnections = null;
    private boolean validated = true;

    public TileEntitySolarTransmitter(BlockEntityType<? extends TileEntitySolarTransmitter> type)
    {
        super(type);
    }

    @Override
    public void clearRemoved()
    {
        this.validated = false;
        super.clearRemoved();
    }

    @Override
    public void setRemoved()
    {
//        if (!BlockFluidPipe.ignoreDrop)
        {
            this.getNetwork().split(this);
        }
//        else
//        {
//            this.setNetwork(null);
//        }

        super.setRemoved();
    }

    @Override
    public void onChunkUnloaded()
    {
        super.setRemoved();
        super.onChunkUnloaded();
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return false;
//    }

    @Override
    public IGridNetwork getNetwork()
    {
        if (this.network == null)
        {
            this.resetNetwork();
        }

        return this.network;
    }

    @Override
    public boolean hasNetwork()
    {
        return this.network != null;
    }

    @Override
    public void onNetworkChanged()
    {
//        this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
        BlockState state = this.getBlockState();
        level.setBlocksDirty(worldPosition, state.getBlock().defaultBlockState(), state); // Forces block render update. Better way to do this?
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide)
        {
            if (!this.validated)
            {
                VenusTickHandlerServer.solarTransmitterUpdates.add(this);
                this.validated = true;
            }
        }

        super.tick();
    }

    protected void resetNetwork()
    {
        SolarModuleNetwork network = new SolarModuleNetwork();
        this.setNetwork(network);
        network.addTransmitter(this);
        network.register();
    }

    @Override
    public void setNetwork(IGridNetwork network)
    {
        if (this.network == network)
        {
            return;
        }

        if (this.level.isClientSide && this.network != null)
        {
            SolarModuleNetwork solarNetwork = (SolarModuleNetwork) this.network;
            solarNetwork.removeTransmitter(this);

            if (solarNetwork.getTransmitters().isEmpty())
            {
                solarNetwork.unregister();
            }
        }

        this.network = network;

        if (this.level.isClientSide && this.network != null)
        {
            ((SolarModuleNetwork) this.network).getTransmitters().add(this);
        }
    }

    @Override
    public void refresh()
    {
        if (!this.level.isClientSide)
        {
            this.adjacentConnections = null;

            BlockVec3 thisVec = new BlockVec3(this);
            for (Direction side : Direction.values())
            {
                BlockEntity tileEntity = thisVec.getTileEntityOnSide(this.level, side);

                if (tileEntity != null)
                {
                    if (tileEntity.getClass() == this.getClass() && tileEntity instanceof INetworkProvider && ((INetworkProvider) tileEntity).hasNetwork())
                    {
                        if (!(tileEntity instanceof ITransmitter) || (((ITransmitter) tileEntity).canConnect(side.getOpposite(), ((ITransmitter) tileEntity).getNetworkType())))
                        {
                            if (!this.hasNetwork())
                            {
                                this.setNetwork(((INetworkProvider) tileEntity).getNetwork());
                                ((SolarModuleNetwork) this.getNetwork()).addTransmitter(this);
                            }
                            else if (this.hasNetwork() && !this.getNetwork().equals(((INetworkProvider) tileEntity).getNetwork()))
                            {
                                this.setNetwork((IGridNetwork) this.getNetwork().merge(((INetworkProvider) tileEntity).getNetwork()));
                            }
                        }
                    }
                }
            }

            this.getNetwork().refresh();
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
            this.adjacentConnections = new BlockEntity[Direction.values().length];


            BlockVec3 thisVec = new BlockVec3(this);
            for (Direction direction : Direction.values())
            {
                BlockEntity tileEntity = thisVec.getTileEntityOnSide(this.level, direction);

                if (tileEntity instanceof TileEntitySolarArrayModule)
                {
                    if (this.level.isClientSide || ((TileEntitySolarArrayModule) tileEntity).canConnect(direction.getOpposite(), NetworkType.SOLAR_MODULE))
                    {
                        this.adjacentConnections[direction.ordinal()] = tileEntity;
                    }
                }
            }
        }

        return this.adjacentConnections;
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        return type == NetworkType.SOLAR_MODULE;
    }

    @Override
    public NetworkType getNetworkType()
    {
        return NetworkType.SOLAR_MODULE;
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
}
