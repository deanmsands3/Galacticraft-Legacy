package team.galacticraft.galacticraft.common.core.client.fx;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.level.Level;

public class ParticleSparks extends TextureSheetParticle
{
    float smokeParticleScale;
    private final SpriteSet animatedSprite;

    public ParticleSparks(Level par1World, double par2, double par4, double par6, double par8, double par12, SpriteSet animatedSprite)
    {
        super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
        this.xd *= 0.10000000149011612D;
        this.yd *= 0.10000000149011612D;
        this.zd *= 0.10000000149011612D;
        this.xd += par8;
        this.yd += 0.06;
        this.zd += par12;
        this.rCol = 255F / 255F;
        this.gCol = 255F / 255F;
        this.bCol = 0F / 255F + this.random.nextFloat() / 6;
        this.quadSize *= 0.15F;
        this.quadSize *= 1.0F * 3;
        this.smokeParticleScale = this.quadSize;
        this.lifetime = (int) 50.0D;
        this.lifetime = (int) (this.lifetime * 1.0F);
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

        this.move(this.xd, this.yd, this.zd);

        if (this.y == this.yo)
        {
            this.xd *= 1.1D;
            this.zd *= 1.1D;
        }

        this.setSpriteFromAge(this.animatedSprite);
        this.yd -= 0.01D;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.9100000262260437D;
        this.yd *= 0.9100000262260437D;
        this.zd *= 0.9100000262260437D;
    }
}
