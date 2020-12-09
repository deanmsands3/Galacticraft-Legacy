package team.galacticraft.galacticraft.common.core.entities.player;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

public interface IPlayerClient
{
    void move(LocalPlayer player, MoverType type, Vec3 pos);

    void onUpdate(LocalPlayer player);

    void onTickPre(LocalPlayer player);

    void onTickPost(LocalPlayer player);

    Direction getBedDirection(LocalPlayer player, Direction vanillaDegrees);

    boolean isEntityInsideOpaqueBlock(LocalPlayer player, boolean vanillaInside);

//    boolean wakeUpPlayer(ClientPlayerEntity player, boolean immediately, boolean updateWorldFlag, boolean setSpawn); TODO Cryo chamber

    void onBuild(int i, LocalPlayer player);
}
