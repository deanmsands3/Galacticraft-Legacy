package micdoodle8.mods.galacticraft.api.world;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IWeatherProvider
{
    @OnlyIn(Dist.CLIENT)
    ParticleOptions getParticle(ClientLevel world, double x, double y, double z);

    void weatherSounds(int j, Minecraft mc, Level world, BlockPos blockpos, double xx, double yy, double zz, Random random);

    int getSoundInterval(float f);
}
