package team.galacticraft.galacticraft.common.core.client.fx;

import com.mojang.blaze3d.vertex.VertexConsumer;
import team.galacticraft.galacticraft.common.api.vector.Vector3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class ParticleLaunchSmoke extends LaunchParticle
{
    float smokeParticleScale;
    private final SpriteSet animatedSprite;

    public ParticleLaunchSmoke(Level par1World, double posX, double posY, double posZ, double motX, double motY, double motZ, float size, boolean launched, SpriteSet animatedSprite)
    {
        super(par1World, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        this.xd *= 0.10000000149011612D;
        this.yd *= 0.10000000149011612D;
        this.zd *= 0.10000000149011612D;
        this.setSize(0.2F, 0.2F);
        this.xd += motX;
        this.yd += motY;
        this.zd += motZ;
        this.alpha = 1.0F;
        this.rCol = this.gCol = this.bCol = (float) (Math.random() * 0.30000001192092896D) + 0.6F;
        this.quadSize *= 0.75F;
        this.quadSize *= size * 3;
        this.smokeParticleScale = this.quadSize;
        this.animatedSprite = animatedSprite;

        if (launched)
        {
            this.lifetime = (int) (this.lifetime * size) + 10;
        }
        else
        {
            this.xd += par1World.random.nextDouble() / 2 - 0.25;
            this.yd += par1World.random.nextDouble() / 20;
            this.zd += par1World.random.nextDouble() / 2 - 0.25;
            this.lifetime = 30 + this.lifetime;
        }

        this.hasPhysics = true;
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

        this.setSpriteFromAge(this.animatedSprite);
        this.move(this.xd, this.yd, this.zd);

//        if (this.posY == this.prevPosY)
//        {
////            this.motionX *= 1.0001D;
////            this.motionZ *= 1.0001D;
//        }
//        else
//        {
//            this.motionX *= 0.99D;
//            this.motionZ *= 0.99D;
//        }
    }
}
