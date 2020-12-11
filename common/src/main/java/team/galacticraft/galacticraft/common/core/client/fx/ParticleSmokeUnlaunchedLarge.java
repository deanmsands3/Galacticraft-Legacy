package team.galacticraft.galacticraft.common.core.client.fx;

import team.galacticraft.galacticraft.common.api.vector.Vector3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;

public class ParticleSmokeUnlaunchedLarge extends ParticleSmokeUnlaunched
{
    public ParticleSmokeUnlaunchedLarge(Level world, double posX, double posY, double posZ, double motX, double motY, double motZ, SpriteSet sprite)
    {
        super(world, posX, posY, posZ, motX, motY, motZ, 2.5F, sprite);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, Level worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new ParticleSmokeUnlaunchedLarge(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
