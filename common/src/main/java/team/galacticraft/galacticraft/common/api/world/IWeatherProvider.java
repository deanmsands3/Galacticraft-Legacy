package team.galacticraft.galacticraft.common.api.world;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;

public interface IWeatherProvider
{
    @Environment(EnvType.CLIENT)
    ParticleOptions getParticle(ClientLevel world, double x, double y, double z);

    void weatherSounds(int j, Minecraft mc, Level world, BlockPos blockpos, double xx, double yy, double zz, Random random);

    int getSoundInterval(float f);
}
