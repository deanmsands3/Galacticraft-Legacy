package micdoodle8.mods.galacticraft.planets.venus.client.fx;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class ParticleAcidVapor extends TextureSheetParticle
{
    private final SpriteSet animatedSprite;
    private final float smokeParticleScale;

    public ParticleAcidVapor(Level worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, SpriteSet animatedSprite)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.xd *= 0.10000000149011612D;
        this.yd *= 0.10000000149011612D;
        this.zd *= 0.10000000149011612D;
        this.xd += motionX / 10.0F;
        this.yd += motionY;
        this.zd += motionZ / 10.0F;
        this.rCol = (float) (Math.random() * 0.10000001192092896D + 0.8);
        this.gCol = rCol;
        this.bCol = (float) (Math.random() * 0.10000001192092896D);
        this.alpha = 1.0F;
//        this.particleScale *= 0.75F;
        this.quadSize *= 2.5F;
        this.smokeParticleScale = this.quadSize;
        this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.lifetime = (int) ((float) this.lifetime * 2.5F);
        this.hasPhysics = true;
        this.animatedSprite = animatedSprite;
        this.setSpriteFromAge(animatedSprite);
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks)
    {
        GlStateManager._disableLighting();
        float f = (float) Math.pow(this.age / 11.0, 2.0F);
        f = Math.max(f, 0.1F);
        this.alpha = this.alpha * 0.994F;
        this.quadSize = this.smokeParticleScale * f;
        super.render(buffer, renderInfo, partialTicks);
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
        this.yd += 0.004D;
        this.move(this.xd, this.yd, this.zd);

        this.yd *= 0.9899999785423279D;
        this.xd *= 1.001D;
        this.zd *= 1.001D;

        if (this.onGround)
        {
            this.xd *= 0.699999988079071D;
            this.zd *= 0.699999988079071D;
        }
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
            return new ParticleAcidVapor(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        }
    }
}