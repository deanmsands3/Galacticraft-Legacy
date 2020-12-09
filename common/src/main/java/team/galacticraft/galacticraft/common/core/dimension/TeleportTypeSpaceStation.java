package team.galacticraft.galacticraft.common.core.dimension;

import team.galacticraft.galacticraft.common.api.vector.Vector3D;
import team.galacticraft.galacticraft.common.api.world.ITeleportType;
import team.galacticraft.galacticraft.core.util.ConfigManagerCore;
import team.galacticraft.galacticraft.core.util.EnumColor;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import java.util.Random;

public class TeleportTypeSpaceStation implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return false;
    }

    @Override
    public Vector3D getPlayerSpawnLocation(ServerLevel world, ServerPlayer player)
    {
        return new Vector3D(0.5, 65.0, 0.5);
    }

    @Override
    public Vector3D getEntitySpawnLocation(ServerLevel world, Entity player)
    {
        return new Vector3D(0.5, 65.0, 0.5);
    }

    @Override
    public Vector3D getParaChestSpawnLocation(ServerLevel world, ServerPlayer player, Random rand)
    {
        return null;
    }

    @Override
    public void onSpaceDimensionChanged(Level newWorld, ServerPlayer player, boolean ridingAutoRocket)
    {
        if (ConfigManagerCore.spaceStationsRequirePermission.get() && !newWorld.isClientSide)
        {
            player.sendMessage(new TextComponent(EnumColor.YELLOW + I18n.get("gui.spacestation.type_command") + " " + EnumColor.AQUA + "/ssinvite " + I18n.get("gui.spacestation.playe") + " " + EnumColor.YELLOW + I18n.get("gui.spacestation.to_allow_entry")));
        }
    }

    @Override
    public void setupAdventureSpawn(ServerPlayer player)
    {
        // TODO Auto-generated method stub

    }
}
