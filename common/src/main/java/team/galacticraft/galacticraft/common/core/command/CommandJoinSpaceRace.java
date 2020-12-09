package team.galacticraft.galacticraft.common.core.command;
//
//import team.galacticraft.galacticraft.core.GalacticraftCore;
//import team.galacticraft.galacticraft.core.dimension.SpaceRaceManager;
//import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
//import team.galacticraft.galacticraft.core.network.PacketSimple;
//import team.galacticraft.galacticraft.core.network.PacketSimple.EnumSimplePacket;
//import team.galacticraft.galacticraft.core.util.GCCoreUtil;
//import team.galacticraft.galacticraft.core.util.PlayerUtil;
//import net.minecraft.command.CommandBase;
//import net.minecraft.command.CommandException;
//import net.minecraft.command.ICommandSender;
//import net.minecraft.command.WrongUsageException;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.server.MinecraftServer;
//
//public class CommandJoinSpaceRace extends CommandBase
//{
//    @Override
//    public int getRequiredPermissionLevel()
//    {
//        return 0;
//    }
//
//    @Override
//    public String getUsage(ICommandSender var1)
//    {
//        return "/" + this.getName();
//    }
//
//    @Override
//    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
//    {
//        return true;
//    }
//
//    @Override
//    public String getName()
//    {
//        return "joinrace";
//    }
//
//    @Override
//    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
//    {
//        ServerPlayerEntity playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(server, sender.getName(), true);
//
//        if (args.length == 0)
//        {
//            try
//            {
//                if (playerBase != null)
//                {
//                    GCPlayerStats stats = GCPlayerStats.get(playerBase);
//
//                    if (stats.getSpaceRaceInviteTeamID() > 0)
//                    {
//                        SpaceRaceManager.sendSpaceRaceData(server, playerBase, SpaceRaceManager.getSpaceRaceFromID(stats.getSpaceRaceInviteTeamID()));
//                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_JOIN_RACE_GUI, GCCoreUtil.getDimensionID(playerBase.world), new Object[] { stats.getSpaceRaceInviteTeamID() }), playerBase);
//                    }
//                    else
//                    {
//                        throw new Exception("You haven't been invited to a space race team!");
//                    }
//                }
//                else
//                {
//                    throw new Exception("Could not find player with name: " + args[0]);
//                }
//            }
//            catch (final Exception var6)
//            {
//                throw new CommandException(var6.getMessage(), new Object[0]);
//            }
//        }
//        else
//        {
//            throw new WrongUsageException(I18n.getWithFormat("commands.joinrace.no_team", this.getUsage(sender)), new Object[0]);
//        }
//    }
//}
