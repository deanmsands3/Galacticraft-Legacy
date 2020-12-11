package team.galacticraft.galacticraft.common.core.client.fx;

import team.galacticraft.galacticraft.common.api.vector.Vector3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ParticleOxygen extends TextureSheetParticle
{
    private final float portalParticleScale;
    private final double portalPosX;
    private final double portalPosY;
    private final double portalPosZ;
    private static long tick = -1L;
    private static final Map<BlockPos, Integer> cacheLighting = new HashMap<>();

    public ParticleOxygen(Level par1World, double posX, double posY, double posZ, double motX, double motY, double motZ, float colR, float colG, float colB)
    {
        super(par1World, posX, posY, posZ, motX, motY, motZ);
        this.xd = motX;
        this.yd = motY;
        this.zd = motZ;
        this.portalPosX = this.x = posX;
        this.portalPosY = this.y = posY;
        this.portalPosZ = this.z = posZ;
        this.portalParticleScale = this.quadSize = 0.1F;
        this.rCol = colR;
        this.gCol = colG;
        this.bCol = colB;
        this.lifetime = (int) (Math.random() * 10.0D) + 40;
        this.hasPhysics = false;
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks)
    {
        float var8 = (this.age + partialTicks) / this.lifetime;
        var8 = 1.0F - var8;
        var8 *= var8;
        var8 = 1.0F - var8;
        this.quadSize = this.portalParticleScale * var8;
        super.render(buffer, renderInfo, partialTicks);
    }

    @Override
    public int getLightColor(float par1)
    {
        long time = this.level.getDayTime();
        if (time > tick)
        {
            cacheLighting.clear();
            tick = time;
        }
        BlockPos blockpos = new BlockPos(this.x, this.y + 0.17, this.z);
        int var2;
        if (cacheLighting.containsKey(blockpos))
        {
            var2 = cacheLighting.get(blockpos);
        }
        else
        {
            var2 = this.level.hasChunkAt(blockpos) ? this.level.getMaxLocalRawBrightness(blockpos) : 0;
            cacheLighting.put(blockpos, var2);
        }
        float var3 = (float) this.age / (float) this.lifetime;
        var3 *= var3;
        var3 *= var3;
        final int var4 = var2 & 255;
        int var5 = var2 >> 16 & 255;
        var5 += (int) (var3 * 15.0F * 16.0F);

        if (var5 > 240)
        {
            var5 = 240;
        }

        return var4 | var5 << 16;
    }

    //    @Override
//    public float getBrightness(float par1)
//    {
//        final float var2 = super.getBrightness(par1);
//        float var3 = (float) this.particleAge / (float) this.maxAge;
//        var3 = var3 * var3 * var3 * var3;
//        return var2 * (1.0F - var3) + var3;
//    }

    @Override
    public void tick()
    {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float var1 = (float) this.age / (float) this.lifetime;
        final float var2 = var1;
        var1 = -var1 + var1 * var1 * 2.0F;
        var1 = 1.0F - var1;
        this.x = this.portalPosX + this.xd * var1;
        this.y = this.portalPosY + this.yd * var1 + (1.0F - var2);
        this.z = this.portalPosZ + this.zd * var1;

        if (this.age++ >= this.lifetime)
        {
            this.remove();
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
        public Particle createParticle(SimpleParticleType typeIn, Level worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new ParticleOxygen(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 0.7F, 0.7F, 1.0F);
        }
    }
}
