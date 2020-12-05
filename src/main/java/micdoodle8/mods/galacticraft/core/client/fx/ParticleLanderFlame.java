package micdoodle8.mods.galacticraft.core.client.fx;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class ParticleLanderFlame extends TextureSheetParticle
{
    private final SpriteSet animatedSprite;
    private final float smokeParticleScale;
    private final UUID ridingEntity;

    public ParticleLanderFlame(Level world, double x, double y, double z, double mX, double mY, double mZ, EntityParticleData particleData, SpriteSet animatedSprite)
    {
        super(world, x, y, z, mX, mY, mZ);
        this.xd *= 0.10000000149011612D;
        this.zd *= 0.10000000149011612D;
        this.xd += mX;
        this.yd = mY;
        this.zd += mZ;
        this.rCol = 200F / 255F;
        this.gCol = 200F / 255F;
        this.bCol = 200F / 255F + this.random.nextFloat() / 3;
        this.quadSize *= 8F * 1.0F;
        this.smokeParticleScale = this.quadSize;
        this.lifetime = (int) 5.0D;
        this.hasPhysics = true;
        this.ridingEntity = particleData.getEntityUUID();
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

        this.gCol -= 0.09F;
        this.rCol -= 0.09F;

        if (this.y == this.yo)
        {
            this.xd *= 1.1D;
            this.zd *= 1.1D;
        }

        this.quadSize *= 0.9599999785423279D;

        this.xd *= 0.9599999785423279D;
        this.yd *= 0.9599999785423279D;
        this.zd *= 0.9599999785423279D;

        if (this.level.random.nextInt(5) == 1)
        {
            final List<?> var3 = this.level.getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(1.0D, 0.5D, 1.0D));

            if (var3 != null)
            {
                for (int var4 = 0; var4 < var3.size(); ++var4)
                {
                    final Entity var5 = (Entity) var3.get(var4);

                    if (var5 instanceof LivingEntity && var5.isAlive() && !var5.isOnFire() && !var5.getUUID().equals(this.ridingEntity))
                    {
                        var5.setSecondsOnFire(3);
                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_SET_ENTITY_FIRE, GCCoreUtil.getDimensionType(var5.level), new Object[]{var5.getId()}));
                    }
                }
            }
        }
    }

    @Override
    public int getLightColor(float par1)
    {
        return Constants.PACKED_LIGHT_FULL_BRIGHT;
    }

//    @Override
//    public float getBrightness(float par1)
//    {
//        return 1.0F;
//    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<EntityParticleData>
    {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(EntityParticleData typeIn, Level worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new ParticleLanderFlame(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn, this.spriteSet);
        }
    }
}
