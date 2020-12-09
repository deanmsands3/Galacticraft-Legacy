package team.galacticraft.galacticraft.common.core.fluid;

import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.transmission.grid.IElectricityNetwork;
import team.galacticraft.galacticraft.common.api.transmission.tile.IConnector;
import team.galacticraft.galacticraft.common.api.transmission.tile.INetworkProvider;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * A helper class that provides additional useful functions to interact with the
 * ElectricityNetwork
 *
 * @author Calclavia
 */
public class NetworkHelper
{
    public static EnumSet<Direction> getDirections(BlockEntity tileEntity, NetworkType type)
    {
        EnumSet<Direction> possibleSides = EnumSet.noneOf(Direction.class);

        if (tileEntity instanceof IConnector)
        {
            for (int i = 0; i < 6; i++)
            {
                Direction direction = Direction.from3DDataValue(i);
                if (((IConnector) tileEntity).canConnect(direction, type))
                {
                    possibleSides.add(direction);
                }
            }
        }

        return possibleSides;
    }

    /**
     * @param tileEntity           - The TileEntity's sides.
     * @param approachingDirection - The directions that can be connected.
     * @return A list of networks from all specified sides. There will be no
     * repeated ElectricityNetworks and it will never return null.
     */
    public static Set<IElectricityNetwork> getNetworksFromMultipleSides(BlockEntity tileEntity, EnumSet<Direction> approachingDirection)
    {
        final Set<IElectricityNetwork> connectedNetworks = new HashSet<IElectricityNetwork>();

        BlockVec3 tileVec = new BlockVec3(tileEntity);
        for (Direction side : Direction.values())
        {
            if (approachingDirection.contains(side))
            {
                BlockEntity outputConductor = tileVec.getTileEntityOnSide(tileEntity.getLevel(), side);
                IElectricityNetwork electricityNetwork = NetworkHelper.getElectricalNetworkFromTileEntity(outputConductor, side);

                if (electricityNetwork != null)
                {
                    connectedNetworks.add(electricityNetwork);
                }
            }
        }

        return connectedNetworks;
    }

    /**
     * Tries to find the electricity network based in a tile entity and checks
     * to see if it is a conductor. All machines should use this function to
     * search for a connecting conductor around it.
     *
     * @param tileEntity        - The TileEntity conductor
     * @param approachDirection - The direction you are approaching this wire from.
     * @return The ElectricityNetwork or null if not found.
     */
    public static IElectricityNetwork getElectricalNetworkFromTileEntity(BlockEntity tileEntity, Direction approachDirection)
    {
        if (tileEntity != null)
        {
            if (tileEntity instanceof INetworkProvider)
            {
                if (tileEntity instanceof IConnector)
                {
                    if (((IConnector) tileEntity).canConnect(approachDirection.getOpposite(), NetworkType.POWER))
                    {
                        if (((INetworkProvider) tileEntity).getNetwork() instanceof IElectricityNetwork)
                        {
                            return (IElectricityNetwork) ((INetworkProvider) tileEntity).getNetwork();
                        }
                    }
                }
                else
                {
                    if (((INetworkProvider) tileEntity).getNetwork() instanceof IElectricityNetwork)
                    {
                        return (IElectricityNetwork) ((INetworkProvider) tileEntity).getNetwork();
                    }
                }
            }
        }

        return null;
    }

    public static FluidNetwork getFluidNetworkFromTile(BlockEntity tileEntity, Direction approachDirection)
    {
        if (tileEntity != null)
        {
            if (tileEntity instanceof INetworkProvider)
            {
                if (tileEntity instanceof IConnector)
                {
                    if (((IConnector) tileEntity).canConnect(approachDirection.getOpposite(), NetworkType.FLUID))
                    {
                        if (((INetworkProvider) tileEntity).getNetwork() instanceof FluidNetwork)
                        {
                            return (FluidNetwork) ((INetworkProvider) tileEntity).getNetwork();
                        }
                    }
                }
                else
                {
                    if (((INetworkProvider) tileEntity).getNetwork() instanceof FluidNetwork)
                    {
                        return (FluidNetwork) ((INetworkProvider) tileEntity).getNetwork();
                    }
                }
            }
        }

        return null;
    }

//    public static IHydrogenNetwork getHydrogenNetworkFromTileEntity(TileEntity tileEntity, EnumFacing approachDirection)
//    {
//        if (tileEntity != null)
//        {
//            if (tileEntity instanceof INetworkProvider)
//            {
//                if (tileEntity instanceof IConnector)
//                {
//                    if (((IConnector) tileEntity).canConnect(approachDirection.getOpposite(), NetworkType.HYDROGEN))
//                    {
//                        if (((INetworkProvider) tileEntity).getNetwork() instanceof IHydrogenNetwork)
//                        {
//                            return (IHydrogenNetwork) ((INetworkProvider) tileEntity).getNetwork();
//                        }
//                    }
//                }
//                else
//                {
//                    if (((INetworkProvider) tileEntity).getNetwork() instanceof IHydrogenNetwork)
//                    {
//                        return (IHydrogenNetwork) ((INetworkProvider) tileEntity).getNetwork();
//                    }
//                }
//            }
//        }
//
//        return null;
//    }
}
