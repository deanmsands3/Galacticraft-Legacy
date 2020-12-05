package micdoodle8.mods.galacticraft.core.util;

import com.mojang.authlib.GameProfile;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PlayerUtil
{
    public static HashMap<String, GameProfile> knownSkins = new HashMap<>();

    public static ServerPlayer getPlayerForUsernameVanilla(MinecraftServer server, String username)
    {
        return server.getPlayerList().getPlayerByName(username);
    }

    public static ServerPlayer getPlayerBaseServerFromPlayerUsername(String username, boolean ignoreCase)
    {
        MinecraftServer server = GCCoreUtil.getServer();
        return getPlayerBaseServerFromPlayerUsername(server, username, ignoreCase);
    }

    public static ServerPlayer getPlayerBaseServerFromPlayerUsername(MinecraftServer server, String username, boolean ignoreCase)
    {
        if (server != null)
        {
            if (ignoreCase)
            {
                return getPlayerForUsernameVanilla(server, username);
            }
            else
            {
                Iterator iterator = server.getPlayerList().getPlayers().iterator();
                ServerPlayer entityplayermp;

                do
                {
                    if (!iterator.hasNext())
                    {
                        return null;
                    }

                    entityplayermp = (ServerPlayer) iterator.next();
                }
                while (!entityplayermp.getName().getString().equalsIgnoreCase(username));

                return entityplayermp;
            }
        }

        GCLog.severe("Warning: Could not find player base server instance for player " + username);

        return null;
    }

    public static ServerPlayer getPlayerBaseServerFromPlayer(Player player, boolean ignoreCase)
    {
        if (player == null)
        {
            return null;
        }

        if (player instanceof ServerPlayer)
        {
            return (ServerPlayer) player;
        }

        return PlayerUtil.getPlayerBaseServerFromPlayerUsername(player.getName().getString(), ignoreCase);
    }

    @Environment(EnvType.CLIENT)
    public static LocalPlayer getPlayerBaseClientFromPlayer(Player player, boolean ignoreCase)
    {
        LocalPlayer clientPlayer = Minecraft.getInstance().player;

        if (clientPlayer == null && player != null)
        {
            GCLog.severe("Warning: Could not find player base client instance for player " + PlayerUtil.getName(player));
        }

        return clientPlayer;
    }

    @Environment(EnvType.CLIENT)
    public static GameProfile getOtherPlayerProfile(String name)
    {
        return knownSkins.get(name);
    }

    @Environment(EnvType.CLIENT)
    public static GameProfile makeOtherPlayerProfile(String strName, String strUUID)
    {
        GameProfile profile = null;
        for (Object e : Minecraft.getInstance().level.entitiesForRendering())
        {
            if (e instanceof AbstractClientPlayer)
            {
                GameProfile gp2 = ((AbstractClientPlayer) e).getGameProfile();
                if (gp2.getName().equals(strName))
                {
                    profile = gp2;
                    break;
                }
            }
        }
        if (profile == null)
        {
            UUID uuid = strUUID.isEmpty() ? UUID.randomUUID() : UUID.fromString(strUUID);
            profile = new GameProfile(uuid, strName);
        }

        PlayerUtil.knownSkins.put(strName, profile);
        return profile;
    }

    @Environment(EnvType.CLIENT)
    public static GameProfile getSkinForName(String strName, String strUUID, DimensionType dimID)
    {
        GameProfile profile = Minecraft.getInstance().player.getGameProfile();
        if (!strName.equals(profile.getName()))
        {
            profile = PlayerUtil.getOtherPlayerProfile(strName);
            if (profile == null)
            {
                profile = PlayerUtil.makeOtherPlayerProfile(strName, strUUID);
            }
            if (!profile.getProperties().containsKey("textures"))
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_REQUEST_PLAYERSKIN, dimID, new Object[]{strName}));
            }
        }
        return profile;
    }

    public static ServerPlayer getPlayerByUUID(UUID theUUID)
    {
        List<ServerPlayer> players = PlayerUtil.getPlayersOnline();
        ServerPlayer entityplayermp;
        for (int i = players.size() - 1; i >= 0; --i)
        {
            entityplayermp = players.get(i);

            if (entityplayermp.getUUID().equals(theUUID))
            {
                return entityplayermp;
            }
        }
        return null;
    }


    public static List<ServerPlayer> getPlayersOnline()
    {
        return GCCoreUtil.getServer().getPlayerList().getPlayers();
    }


    public static boolean isPlayerOnline(ServerPlayer player)
    {
        return player.server.getPlayerList().getPlayers().contains(player);
    }

    public static String getName(Player player)
    {
        if (player == null)
        {
            return null;
        }

        if (player.getGameProfile() == null)
        {
            return null;
        }

        return player.getGameProfile().getName();
    }
}
