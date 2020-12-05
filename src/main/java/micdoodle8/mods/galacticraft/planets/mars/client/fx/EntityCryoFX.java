package micdoodle8.mods.galacticraft.planets.mars.client.fx;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class EntityCryoFX extends TextureSheetParticle
{
    private final SpriteSet animatedSprite;
    float scaleStart;

    public EntityCryoFX(Level worldIn, double x, double y, double z, double mX, double mY, double mZ, SpriteSet animatedSprite)
    {
        super(worldIn, x, y, z, mX, mY, mZ);
        float f = 2.5F;
//        this.motionX *= 0.0;
//        this.motionY *= 0.10000000149011612;
//        this.motionZ *= 0.0;
//        this.motionX += motion.x;
//        this.motionY += motion.y;
//        this.motionZ += motion.z;
        this.rCol = this.gCol = this.bCol = 1.0F - (float) (Math.random() * 0.30000001192092896D);
        this.rCol *= 0.8F;
        this.gCol *= 0.8F;
        this.quadSize *= 0.25F;
        this.quadSize *= f;
        this.scaleStart = this.quadSize;
        this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.3D));
        this.lifetime = (int) ((float) this.lifetime * f);
        this.hasPhysics = false;
        this.animatedSprite = animatedSprite;
        this.setSpriteFromAge(animatedSprite);
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks)
    {
        float f = ((float) this.age + partialTicks) / (float) this.lifetime * 32.0F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        this.quadSize = this.scaleStart * f;
        super.render(buffer, renderInfo, partialTicks);
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
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

//        this.setParticleTextureIndex(7 - this.age * 8 / this.maxAge);
        this.setSpriteFromAge(this.animatedSprite);
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.9599999785423279D;
        this.yd *= 0.9599999785423279D;
        this.zd *= 0.9599999785423279D;
        Player entityplayer = this.level.getNearestPlayer(this.x, y, z, 2.0D, false);

        if (entityplayer != null && this.y > entityplayer.getBoundingBox().minY)
        {
            this.y += (entityplayer.getBoundingBox().minY - this.y) * 0.2D;
            this.yd += (entityplayer.getDeltaMovement().y - this.yd) * 0.2D;
            this.setPos(this.x, this.y, this.z);
        }

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
            return new EntityCryoFX(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
