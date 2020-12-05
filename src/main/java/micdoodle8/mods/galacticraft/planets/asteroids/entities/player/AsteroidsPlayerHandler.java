package micdoodle8.mods.galacticraft.planets.asteroids.entities.player;

import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.DimensionAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.AsteroidEntities;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class AsteroidsPlayerHandler
{
//    @SubscribeEvent
//    public void onPlayerLogin(PlayerLoggedInEvent event)
//    {
//        if (event.player instanceof ServerPlayerEntity)
//        {
//            this.onPlayerLogin((ServerPlayerEntity) event.player);
//        }
//    }
//
//    @SubscribeEvent
//    public void onPlayerLogout(PlayerLoggedOutEvent event)
//    {
//        if (event.player instanceof ServerPlayerEntity)
//        {
//            this.onPlayerLogout((ServerPlayerEntity) event.player);
//        }
//    }
//
//    @SubscribeEvent
//    public void onPlayerRespawn(PlayerRespawnEvent event)
//    {
//        if (event.player instanceof ServerPlayerEntity)
//        {
//            this.onPlayerRespawn((ServerPlayerEntity) event.player);
//        }
//    }

//    @SubscribeEvent
//    public void onEntityConstructing(EntityEvent.EntityConstructing event)
//    {
//        if (event.getEntity() instanceof EntityPlayerMP && GCPlayerStats.get((EntityPlayerMP) event.entity) == null)
//        {
//            GCPlayerStats.register((EntityPlayerMP) event.entity);
//        }
//    }

    public void onPlayerUpdate(ServerPlayer player)
    {
        if (!ConfigManagerPlanets.disableSmallAsteroids.get())
        {
            if (!player.level.isClientSide && player.level.getDimension() instanceof DimensionAsteroids)
            {
                final int f = 50;

                if (player.level.random.nextInt(f) == 0 && player.getY() < 260D)
                {
                    final Player closestPlayer = player.level.getNearestPlayer(player, 100);

                    if (closestPlayer == null || closestPlayer.getId() <= player.getId())
                    {
                        double x, y, z;
                        double motX, motY, motZ;
                        double r = player.level.random.nextInt(60) + 30D;
                        double theta = Math.PI * 2.0 * player.level.random.nextDouble();
                        x = player.getX() + Math.cos(theta) * r;
                        y = player.getY() + player.level.random.nextInt(5);
                        z = player.getZ() + Math.sin(theta) * r;
                        motX = (player.getX() - x + (player.level.random.nextDouble() - 0.5) * 40) / 400.0F;
                        motY = (player.level.random.nextDouble() - 0.5) * 0.4;
                        motZ = (player.getZ() - z + (player.level.random.nextDouble() - 0.5) * 40) / 400.0F;

                        final EntitySmallAsteroid smallAsteroid = new EntitySmallAsteroid(AsteroidEntities.SMALL_ASTEROID.get(), player.level);
                        smallAsteroid.setPos(x, y, z);
//                        smallAsteroid.motionX = motX;
//                        smallAsteroid.motionY = motY;
//                        smallAsteroid.motionZ = motZ;
                        smallAsteroid.setDeltaMovement(motX, motY, motZ);
                        smallAsteroid.spinYaw = player.level.random.nextFloat() * 4;
                        smallAsteroid.spinPitch = player.level.random.nextFloat() * 2;

                        player.level.addFreshEntity(smallAsteroid);
                    }
                }
            }
        }
    }
}
