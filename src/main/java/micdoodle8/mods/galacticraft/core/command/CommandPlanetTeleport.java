package micdoodle8.mods.galacticraft.core.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import java.util.Collection;

public class CommandPlanetTeleport
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> literalcommandnode = dispatcher.register(Commands.literal("dimensiontp").requires((src) -> src.hasPermission(2)).executes((ctx) -> {
            ServerPlayer player = ctx.getSource().getPlayerOrException();
            return teleport(ImmutableList.of(player), ctx.getSource().getLevel());
        }).then(Commands.argument("targets", EntityArgument.entities()).executes((ctx) -> {
            return teleport(EntityArgument.getPlayers(ctx, "targets"), ctx.getSource().getLevel());
        })));
    }

    private static int teleport(Collection<ServerPlayer> targets, ServerLevel world)
    {
        for (ServerPlayer target : targets)
        {
//        ServerWorld worldserver = server.getWorld(GCCoreUtil.getDimensionID(server.worlds[0]));
            BlockPos spawnPoint = world.getSharedSpawnPos();
            GCPlayerStats stats = GCPlayerStats.get(target);
            stats.setRocketStacks(NonNullList.withSize(2, ItemStack.EMPTY));
            stats.setRocketItem(EntityTier1Rocket.getItemFromType(IRocketType.EnumRocketType.DEFAULT));
//        stats.setRocketItem(GCItems.rocketTierOne);
            stats.setFuelLevel(1000);
            stats.setCoordsTeleportedFromX(spawnPoint.getX());
            stats.setCoordsTeleportedFromZ(spawnPoint.getZ());

            try
            {
                WorldUtil.toCelestialSelection(target, stats, Integer.MAX_VALUE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw e;
            }
        }

        return targets.size();
    }

//    @Override
//    public String getUsage(ICommandSender var1)
//    {
//        return "/" + this.getName() + " [<player>]";
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
//        return "dimensiontp";
//    }
//
//    @Override
//    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
//    {
//        ServerPlayerEntity playerBase = null;
//
//        if (args.length < 2)
//        {
//            try
//            {
//                if (args.length == 1)
//                {
//                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(args[0], true);
//                }
//                else
//                {
//                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
//                }
//
//                if (playerBase != null)
//                {
//                    ServerWorld worldserver = server.getWorld(GCCoreUtil.getDimensionID(server.worlds[0]));
//                    BlockPos spawnPoint = worldserver.getSpawnPoint();
//                    GCPlayerStats stats = GCPlayerStats.get(playerBase);
//                    stats.setRocketStacks(NonNullList.withSize(2, ItemStack.EMPTY));
//                    stats.setRocketType(IRocketType.EnumRocketType.DEFAULT);
//                    stats.setRocketItem(GCItems.rocketTierOne);
//                    stats.setFuelLevel(1000);
//                    stats.setCoordsTeleportedFromX(spawnPoint.getX());
//                    stats.setCoordsTeleportedFromZ(spawnPoint.getZ());
//
//                    try
//                    {
//                        WorldUtil.toCelestialSelection(playerBase, stats, Integer.MAX_VALUE);
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                        throw e;
//                    }
//
//                    CommandBase.notifyCommandListener(sender, this, "commands.dimensionteleport", new Object[] { String.valueOf(EnumColor.GREY + "[" + playerBase.getName()), "]" });
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
//            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.dimensiontp.too_many", this.getUsage(sender)), new Object[0]);
//        }
//    }
}
