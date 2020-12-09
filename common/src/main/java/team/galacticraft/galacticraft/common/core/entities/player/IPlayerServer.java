package team.galacticraft.galacticraft.common.core.entities.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

public interface IPlayerServer
{
    void updateRiddenPre(ServerPlayer player);

    void updateRiddenPost(ServerPlayer player);

    boolean dismountEntity(ServerPlayer player, Entity par1Entity);

    void move(ServerPlayer player, MoverType type, Vec3 pos);

//    boolean wakeUpPlayer(ServerPlayerEntity player, boolean immediately, boolean updateWorldFlag, boolean setSpawn); TODO Cryo chamber

    float attackEntityFrom(ServerPlayer player, DamageSource par1DamageSource, float par2);

    void knockBack(ServerPlayer player, Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ);

    boolean isSpectator(ServerPlayer player);

    void setNoClip(ServerPlayer player, boolean noClip);
}
