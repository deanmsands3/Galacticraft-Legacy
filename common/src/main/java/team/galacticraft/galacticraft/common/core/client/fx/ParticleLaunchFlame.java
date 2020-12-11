package team.galacticraft.galacticraft.common.core.client.fx;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.client.GCParticles;
import team.galacticraft.galacticraft.common.core.network.PacketSimple;
import team.galacticraft.galacticraft.common.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.lwjgl.opengl.GL11;

import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.UUID;

public class ParticleLaunchFlame extends TextureSheetParticle
{
    private final SpriteSet animatedSprite;
    private final float smokeParticleScale;
    private final boolean spawnSmokeShort;
    private final UUID ridingEntity;

    public ParticleLaunchFlame(Level par1World, double posX, double posY, double posZ, double motX, double motY, double motZ, boolean launched, EntityParticleData particleData, SpriteSet animatedSprite)
    {
        super(par1World, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        this.xd = motX;
        this.yd = motY;
        this.zd = motZ;
        this.rCol = 255F / 255F;
        this.gCol = 120F / 255F + this.random.nextFloat() / 3;
        this.bCol = 55F / 255F;
        this.quadSize *= launched ? 4F : 0.1F;
        this.smokeParticleScale = this.quadSize;
        this.lifetime = (int) (this.lifetime * 1F);
        this.hasPhysics = true;
        this.spawnSmokeShort = launched;
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
            this.level.addParticle(this.spawnSmokeShort ? GCParticles.WHITE_SMOKE_LAUNCHED : GCParticles.WHITE_SMOKE_IDLE, this.x, this.y + this.random.nextDouble() * 2, this.z, this.xd, this.yd, this.zd);
            this.level.addParticle(this.spawnSmokeShort ? GCParticles.WHITE_SMOKE_LAUNCHED_LARGE : GCParticles.WHITE_SMOKE_IDLE_LARGE, this.x, this.y + this.random.nextDouble() * 2, this.z, this.xd, this.yd, this.zd);
            if (!this.spawnSmokeShort)
            {
                this.level.addParticle(GCParticles.WHITE_SMOKE_IDLE, this.x, this.y + this.random.nextDouble() * 2, this.z, this.xd, this.yd, this.zd);
                this.level.addParticle(GCParticles.WHITE_SMOKE_IDLE_LARGE, this.x, this.y + this.random.nextDouble() * 2, this.z, this.xd, this.yd, this.zd);
            }
            this.remove();
        }
        else
        {
            this.setSpriteFromAge(animatedSprite);
            this.yd += 0.001D;
            this.move(this.xd, this.yd, this.zd);

            this.gCol += 0.01F;

            if (this.y == this.yo)
            {
                this.xd *= 1.1D;
                this.zd *= 1.1D;
            }

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

        @Nullable
        @Override
        public Particle createParticle(EntityParticleData typeIn, Level worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new ParticleLaunchFlame(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, true, typeIn, this.spriteSet);
        }
    }
}
