package team.galacticraft.galacticraft.common.core.client.fx;

import com.mojang.blaze3d.vertex.VertexConsumer;
import team.galacticraft.galacticraft.common.api.vector.Vector3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class ParticleSmokeSmall extends TextureSheetParticle
{
    float smokeParticleScale;
    private final SpriteSet animatedSprite;

    public ParticleSmokeSmall(Level par1World, double posX, double posY, double posZ, double motX, double motY, double motZ, SpriteSet animatedSprite)
    {
        super(par1World, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        this.xd *= 0.01D;
        this.yd *= 0.01D;
        this.zd *= 0.01D;
        this.setSize(0.05F, 0.05F);
        this.xd += motX;
        this.yd += motY;
        this.zd += motZ;
        this.alpha = 0.8F;
        this.rCol = this.gCol = this.bCol = (float) (Math.random() * 0.2D) + 0.7F;
        this.quadSize *= 0.3F;
        this.smokeParticleScale = this.quadSize;
        this.lifetime = 110;
        this.hasPhysics = true;
        this.animatedSprite = animatedSprite;
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks)
    {
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        float var8 = (this.age + partialTicks) / this.lifetime * 32.0F;

        if (var8 < 0.0F)
        {
            var8 = 0.0F;
        }

        if (var8 > 1.0F)
        {
            var8 = 1.0F;
        }

        this.quadSize = this.smokeParticleScale * var8;
        super.render(buffer, renderInfo, partialTicks);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }

    @Override
    public void tick()
    {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime)
        {
            this.remove();
        }

        this.setSpriteFromAge(animatedSprite);
        this.move(this.xd, this.yd, this.zd);

        if (this.y == this.yo)
        {
            this.xd *= 1.03D;
            this.zd *= 1.03D;
        }

        this.xd *= 0.99D;
        this.yd *= 0.99D;
        this.zd *= 0.99D;
        this.setSpriteFromAge(animatedSprite);
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
        public Particle makeParticle(SimpleParticleType typeIn, Level worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            ParticleSmokeSmall particleSmokeSmall = new ParticleSmokeSmall(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            particleSmokeSmall.pickSprite(this.spriteSet);
            return particleSmokeSmall;
        }
    }
}
