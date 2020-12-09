package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.transmission.grid.IElectricityNetwork;
import team.galacticraft.galacticraft.common.api.transmission.grid.IGridNetwork;
import team.galacticraft.galacticraft.common.api.transmission.tile.IConnector;
import team.galacticraft.galacticraft.common.api.transmission.tile.INetworkProvider;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.core.GCBlockNames;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.energy.grid.EnergyNetwork;
import team.galacticraft.galacticraft.core.energy.tile.TileBaseConductor;
import team.galacticraft.galacticraft.core.energy.tile.TileBaseUniversalConductor;
import team.galacticraft.galacticraft.core.util.RedstoneUtil;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityAluminumWireSwitch extends TileBaseUniversalConductor
{
    public static class TileEntityAluminumWireSwitchableT1 extends TileEntityAluminumWireSwitch
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.aluminumWireSwitchable)
        public static BlockEntityType<TileEntityAluminumWireSwitchableT1> TYPE;

        public TileEntityAluminumWireSwitchableT1()
        {
            super(TYPE, 1);
        }
    }

    public static class TileEntityAluminumWireSwitchableT2 extends TileEntityAluminumWireSwitch
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.aluminumWireSwitchableHeavy)
        public static BlockEntityType<TileEntityAluminumWireSwitchableT2> TYPE;

        public TileEntityAluminumWireSwitchableT2()
        {
            super(TYPE, 2);
        }
    }

    public int tier;
    private boolean disableConnections;

    public TileEntityAluminumWireSwitch(BlockEntityType<?> type, int tier)
    {
        super(type);
        this.tier = tier;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.tier = nbt.getInt("tier");
        //For legacy worlds (e.g. converted from 1.6.4)
        if (this.tier == 0)
        {
            this.tier = 1;
        }
        this.disableConnections = this.disableConnections();
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("tier", this.tier);
        return nbt;
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }

    @Override
    public int getTierGC()
    {
        return this.tier;
    }

    @Override
    public void refresh()
    {
        boolean newDisableConnections = this.disableConnections();
        if (newDisableConnections && !this.disableConnections)
        {
            this.disableConnections = newDisableConnections;
            if (!this.level.isClientSide)
            {
                this.disConnect();
            }
        }
        else if (!newDisableConnections && this.disableConnections)
        {
            this.disableConnections = newDisableConnections;
            if (!this.level.isClientSide)
            {
                this.setNetwork(null);  //Force a full network refresh of this and conductors either LogicalSide
            }
        }

        if (!this.level.isClientSide)
        {
            this.adjacentConnections = null;
            if (!this.disableConnections)
            {
                this.getNetwork().refresh();

                BlockVec3 thisVec = new BlockVec3(this);
                for (Direction side : Direction.values())
                {
                    if (this.canConnect(side, NetworkType.POWER))
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
        }
    }

    private void disConnect()
    {
        EnergyNetwork net = (EnergyNetwork) this.getNetwork();
        if (net != null)
        {
            net.split(this);
        }
    }

    private boolean disableConnections()
    {
        return RedstoneUtil.isBlockReceivingRedstone(this.level, this.worldPosition);
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        return type == NetworkType.POWER && !this.disableConnections();
    }

    @Override
    public IElectricityNetwork getNetwork()
    {
        if (this.network == null)
        {
            EnergyNetwork network = new EnergyNetwork();
            if (!this.disableConnections)
            {
                network.getTransmitters().add(this);
            }
            this.setNetwork(network);
        }

        return (IElectricityNetwork) this.network;
    }

    @Override
    public BlockEntity[] getAdjacentConnections()
    {
        if (this.adjacentConnections == null)
        {
            this.adjacentConnections = new BlockEntity[6];

            if (!this.disableConnections)
            {
                BlockVec3 thisVec = new BlockVec3(this);
                for (int i = 0; i < 6; i++)
                {
                    Direction side = Direction.from3DDataValue(i);
                    if (this.canConnect(side, NetworkType.POWER))
                    {
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
            }
        }

        return this.adjacentConnections;
    }

//    //IC2
//    @Override
//    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, Direction side)
//    {
//    	return this.disableConnections() ? false : super.acceptsEnergyFrom(emitter, LogicalSide);
//    }
//
//    //IC2
//    @Override
//    public double injectEnergy(Direction directionFrom, double amount, double voltage)
//    {
//    	return this.disableConnections ? amount : super.injectEnergy(directionFrom, amount, voltage);
//    }
//
//    //IC2
//    @Override
//    public boolean emitsEnergyTo(IEnergyAcceptor receiver, Direction side)
//    {
//    	return this.disableConnections() ? false : super.emitsEnergyTo(receiver, LogicalSide);
//    }
//
//    //RF
//    @Override
//    public int receiveEnergy(Direction from, int maxReceive, boolean simulate)
//    {
//    	return this.disableConnections ? 0 : super.receiveEnergy(from, maxReceive, simulate);
//    }
//
//    //RF
//    @Override
//    public boolean canConnectEnergy(Direction from)
//    {
//    	return this.disableConnections() ? false : super.canConnectEnergy(from);
//    }
//
//    //Mekanism
//    @Override
//    public boolean canReceiveEnergy(Direction side)
//    {
//    	return this.disableConnections() ? false : super.canReceiveEnergy(LogicalSide);
//    }
}
