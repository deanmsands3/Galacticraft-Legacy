package team.galacticraft.galacticraft.common.core.entities.player;

import team.galacticraft.galacticraft.common.api.entity.IIgnoreShift;
import team.galacticraft.galacticraft.core.GalacticraftCore;
import team.galacticraft.galacticraft.core.dimension.DimensionMoon;
import team.galacticraft.galacticraft.core.entities.EntityCelestialFake;
import team.galacticraft.galacticraft.core.util.DamageSourceGC;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import java.util.LinkedList;
import java.util.List;

public class PlayerServer implements IPlayerServer
{
    private boolean updatingRidden = false;
    static List<Player> noClipList = new LinkedList<>();

    @Override
    public void updateRiddenPre(ServerPlayer player)
    {
        this.updatingRidden = true;
    }

    @Override
    public void updateRiddenPost(ServerPlayer player)
    {
        this.updatingRidden = false;
    }

    @Override
    public boolean dismountEntity(ServerPlayer player, Entity par1Entity)
    {
        return updatingRidden && player.getVehicle() instanceof IIgnoreShift && ((IIgnoreShift) player.getVehicle()).shouldIgnoreShiftExit();

    }

    @Override
    public void move(ServerPlayer player, MoverType type, Vec3 motion)
    {
        // If the player is on the moon, not airbourne and not riding anything
        if (player.level.getDimension() instanceof DimensionMoon && !player.level.isClientSide && player.getVehicle() == null)
        {
            GCPlayerHandler.updateFeet(player, motion.x, motion.z);
        }
    }

//    @Override
//    public boolean wakeUpPlayer(ServerPlayerEntity player, boolean immediately, boolean updateWorldFlag, boolean setSpawn)
//    {
//        BlockPos c = player.bedLocation;
//
//        if (c != null)
//        {
//            EventWakePlayer event = new EventWakePlayer(player, c, immediately, updateWorldFlag, setSpawn, false);
//            MinecraftForge.EVENT_BUS.post(event);
//
//            if (event.result == null || event.result == PlayerEntity.SleepResult.OK)
//            {
//                return false;
//            }
//        }
//
//        return true; TODO Cryo chamber
//    }

    @Override
    public float attackEntityFrom(ServerPlayer player, DamageSource par1DamageSource, float par2)
    {
        //No damage while in Celestial Selection screen
        if (player.getVehicle() instanceof EntityCelestialFake)
        {
            return -1F;
        }

        if (GalacticraftCore.isPlanetsLoaded)
        {
            if (par1DamageSource == DamageSource.OUT_OF_WORLD)
            {
//                if (player.world.getDimension() instanceof WorldProviderAsteroids)
//                {
//                    if (player.getPosY() > -120D)
//                    {
//                        return -1F;
//                    }
//                    if (player.getPosY() > -180D)
//                    {
//                        par2 /= 2;
//                    }
//                } TODO Planets
            }
            else if (par1DamageSource == DamageSource.FALL || par1DamageSource == DamageSourceGC.spaceshipCrash)
            {
                int titaniumCount = 0;
//                if (player.inventory != null)
//                {
//                    for (ItemStack armorPiece : player.getArmorInventoryList())
//                    {
//                        if (armorPiece != null && armorPiece.getItem() instanceof ItemArmorAsteroids)
//                        {
//                            titaniumCount++;
//                        }
//                    }
//                } TODO Planets
                if (titaniumCount == 4)
                {
                    titaniumCount = 5;
                }
                par2 *= (1 - 0.15D * titaniumCount);
            }
        }

        return (par2 == -1F) ? -0.9999F : par2;
    }

    @Override
    public void knockBack(ServerPlayer player, Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ)
    {
        int deshCount = 0;
//        if (player.inventory != null && GalacticraftCore.isPlanetsLoaded)
//        {
//            for (int i = 0; i < 4; i++)
//            {
//                for (ItemStack armorPiece : player.getArmorInventoryList())
//                {
//                    if (armorPiece != null && armorPiece.getItem() instanceof ItemArmorMars)
//                    {
//                        deshCount++;
//                    }
//                }
//            }
//        } TODO Planets

        if (player.getRandom().nextDouble() >= player.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getValue())
        {
            player.hasImpulse = deshCount < 2;
            float f1 = Mth.sqrt(impulseX * impulseX + impulseZ * impulseZ);
            float f2 = 0.4F - deshCount * 0.05F;
            double d1 = 2.0D - deshCount * 0.15D;
            player.setDeltaMovement(player.getDeltaMovement().multiply(1.0 / d1, 1.0 / d1, 1.0 / d1));
            player.setDeltaMovement(player.getDeltaMovement().add(-f2 * impulseX / f1, f2, -f2 * impulseZ / f1));

            if (player.getDeltaMovement().y > 0.4D)
            {
                player.setDeltaMovement(player.getDeltaMovement().x, 0.4, player.getDeltaMovement().z);
            }
        }
    }

    @Override
    public boolean isSpectator(ServerPlayer player)
    {
        return noClipList.contains(player);
    }

    @Override
    public void setNoClip(ServerPlayer player, boolean noClip)
    {
        if (noClip)
        {
            if (!noClipList.contains(player))
            {
                noClipList.add(player);
            }
        }
        else
        {
            noClipList.remove(player);
        }
    }
}
