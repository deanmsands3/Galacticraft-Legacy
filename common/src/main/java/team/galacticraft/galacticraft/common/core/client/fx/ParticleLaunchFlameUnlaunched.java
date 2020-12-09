package team.galacticraft.galacticraft.common.core.client.fx;

import team.galacticraft.galacticraft.common.api.vector.Vector3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class ParticleLaunchFlameUnlaunched extends ParticleLaunchFlame
{
    public ParticleLaunchFlameUnlaunched(Level par1World, double posX, double posY, double posZ, double motX, double motY, double motZ, EntityParticleData particleData, SpriteSet sprite)
    {
        super(par1World, posX, posY, posZ, motX, motY, motZ, false, particleData, sprite);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<EntityParticleData>
    {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle makeParticle(EntityParticleData typeIn, Level worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new ParticleLaunchFlameUnlaunched(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn, this.spriteSet);
        }
    }
}
