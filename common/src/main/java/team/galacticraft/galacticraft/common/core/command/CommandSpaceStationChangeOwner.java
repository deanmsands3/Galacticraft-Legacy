package team.galacticraft.galacticraft.common.core.command;
//
//import team.galacticraft.galacticraft.common.core.GalacticraftCore;
//import team.galacticraft.galacticraft.common.core.dimension.SpaceStationWorldData;
//import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
//import team.galacticraft.galacticraft.common.core.network.PacketSimple;
//import team.galacticraft.galacticraft.common.core.network.PacketSimple.EnumSimplePacket;
//import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
//import team.galacticraft.galacticraft.common.core.util.PlayerUtil;
//import team.galacticraft.galacticraft.common.core.util.WorldUtil;
//import net.minecraft.command.CommandBase;
//import net.minecraft.command.CommandException;
//import net.minecraft.command.ICommandSender;
//import net.minecraft.command.WrongUsageException;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.text.StringTextComponent;
//
//public class CommandSpaceStationChangeOwner extends CommandBase
//{
//    @Override
//    public String getUsage(ICommandSender var1)
//    {
//        return "/" + this.getName() + " <dim#> <player>";
//    }
//
//    @Override
//    public int getRequiredPermissionLevel()
//    {
//        return 2;
//    }
//
//    @Override
//    public String getName()
//    {
//        return "ssnewowner";
//    }
//
//    @Override
//    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
//    {
//        String oldOwner = null;
//        String newOwner = "ERROR";
//        int stationID = -1;
//        ServerPlayerEntity playerAdmin = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
//
//        if (args.length > 1)
//        {
//            newOwner = args[1];
//
//            try
//            {
//                stationID = Integer.parseInt(args[0]);
//            }
//            catch (final Exception var6)
//            {
//                throw new WrongUsageException(I18n.getWithFormat("commands.ssnewowner.wrong_usage", this.getUsage(sender)), new Object[0]);
//            }
//
//            if (stationID < 2)
//            {
//                throw new WrongUsageException(I18n.getWithFormat("commands.ssnewowner.wrong_usage", this.getUsage(sender)), new Object[0]);
//            }
//
//            try
//            {
//                SpaceStationWorldData stationData = SpaceStationWorldData.getMPSpaceStationData(null, stationID, null);
//                if (stationData == null)
//                {
//                    throw new WrongUsageException(I18n.getWithFormat("commands.ssnewowner.wrong_usage", this.getUsage(sender)), new Object[0]);
//                }
//
//                oldOwner = stationData.getOwner();
//                stationData.getAllowedPlayers().remove(oldOwner);
//                if (stationData.getSpaceStationName().equals("Station: " + oldOwner))
//                {
//                    stationData.setSpaceStationName("Station: " + newOwner);
//                }
//                stationData.getAllowedPlayers().add(newOwner);
//                stationData.setOwner(newOwner);
//
//                final ServerPlayerEntity oldPlayer = PlayerUtil.getPlayerBaseServerFromPlayerUsername(oldOwner, true);
//                final ServerPlayerEntity newPlayer = PlayerUtil.getPlayerBaseServerFromPlayerUsername(newOwner, true);
//                if (oldPlayer != null)
//                {
//                    GCPlayerStats stats = GCPlayerStats.get(oldPlayer);
//                    SpaceStationWorldData.updateSSOwnership(oldPlayer, oldOwner, stats, stationID, stationData);
//                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, GCCoreUtil.getDimensionID(oldPlayer.world), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), oldPlayer);
//                }
//                if (newPlayer != null)
//                {
//                    GCPlayerStats stats = GCPlayerStats.get(newPlayer);
//                    SpaceStationWorldData.updateSSOwnership(newPlayer, newOwner.replace(".", ""), stats, stationID, stationData);
//                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, GCCoreUtil.getDimensionID(oldPlayer.world), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), newPlayer);
//                }
//            }
//            catch (final Exception var6)
//            {
//                throw new CommandException(var6.getMessage(), new Object[0]);
//            }
//        }
//        else
//        {
//            throw new WrongUsageException(I18n.getWithFormat("commands.ssinvite.wrong_usage", this.getUsage(sender)), new Object[0]);
//        }
//
//        if (playerAdmin != null)
//        {
//            playerAdmin.sendMessage(new StringTranslatableComponent(WithFormat("gui.spacestation.changesuccess", oldOwner, newOwner)));
//        }
//        else
//        //Console
//        {
//            System.out.println(I18n.getWithFormat("gui.spacestation.changesuccess", oldOwner, newOwner));
//        }
//    }
//}
