package micdoodle8.mods.galacticraft.planets.asteroids.client.fx;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.fx.BlockPosParticleData;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.lang.ref.WeakReference;

@OnlyIn(Dist.CLIENT)
public class ParticleTelepad extends TextureSheetParticle
{
    private final float portalParticleScale;
    private final double portalPosX;
    private final double portalPosY;
    private final double portalPosZ;
    private final WeakReference<TileEntityShortRangeTelepad> telepad;
    private final boolean direction;

    public ParticleTelepad(Level par1World, double x, double y, double z, double mX, double mY, double mZ, TileEntityShortRangeTelepad telepad, boolean direction)
    {
        super(par1World, x, y, z, mX, mY, mZ);
        this.xd = mX;
        this.yd = mY;
        this.zd = mZ;
        this.portalPosX = this.x = x;
        this.portalPosY = this.y = y;
        this.portalPosZ = this.z = z;
        this.portalParticleScale = this.quadSize = this.random.nextFloat() * 0.2F + 0.5F;
        this.lifetime = (int) (Math.random() * 10.0D) + 40;
        this.hasPhysics = false;
        this.hasPhysics = false;
//        this.setParticleTextureIndex((int) (Math.random() * 8.0D));
        this.telepad = new WeakReference<>(telepad);
        this.direction = direction;
//        float f = rand.nextFloat() * 0.6F + 0.4F;
//        float teleportTimeScaled = Math.min(1.0F, telepad.teleportTime / (float) TileEntityShortRangeTelepad.MAX_TELEPORT_TIME);
//        this.particleRed = f * 0.3F;
//        this.particleGreen = f * (0.3F + (teleportTimeScaled * 0.7F));
//        this.particleBlue = f * (1.0F - (teleportTimeScaled * 0.7F));
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks)
    {
        float f6 = (this.age + partialTicks) / this.lifetime;
        f6 = 1.0F - f6;
        f6 *= f6;
        f6 = 1.0F - f6;
        this.quadSize = this.portalParticleScale * f6;
        super.render(buffer, renderInfo, partialTicks);
    }

    @Override
    public int getLightColor(float par1)
    {
        int i = super.getLightColor(par1);
        float f1 = (float) this.age / (float) this.lifetime;
        f1 *= f1;
        f1 *= f1;
        int j = i & 255;
        int k = i >> 16 & 255;
        k += (int) (f1 * 15.0F * 16.0F);

        if (k > 240)
        {
            k = 240;
        }

        return j | k << 16;
    }

//    @Override
//    public float getBrightness(float par1)
//    {
//        float f1 = super.getBrightness(par1);
//        float f2 = (float) this.particleAge / (float) this.particleMaxAge;
//        f2 = f2 * f2 * f2 * f2;
//        return f1 * (1.0F - f2) + f2;
//    }

    @Override
    public void tick()
    {
        TileEntityShortRangeTelepad telepad1 = this.telepad.get();

        if (telepad1 != null)
        {
            Vector3 color = telepad1.getParticleColor(this.random, this.direction);
            this.rCol = color.floatX();
            this.gCol = color.floatY();
            this.bCol = color.floatZ();
        }

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float f = (float) this.age / (float) this.lifetime;
        float f1 = f;
        f = -f + f * f * 2.0F;
        f = 1.0F - f;
        this.x = this.portalPosX + this.xd * f;
        this.y = this.portalPosY + this.yd * f + (1.0F - f1);
        this.z = this.portalPosZ + this.zd * f;

        if (this.age++ >= this.lifetime)
        {
            this.remove();
        }
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class DownFactory implements ParticleProvider<BlockPosParticleData>
    {
        private final SpriteSet spriteSet;

        public DownFactory(SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(BlockPosParticleData typeIn, Level worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            BlockPos pos = typeIn.getBlockPos();
            return new ParticleTelepad(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, (TileEntityShortRangeTelepad) worldIn.getBlockEntity(pos), true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class UpFactory implements ParticleProvider<BlockPosParticleData>
    {
        private final SpriteSet spriteSet;

        public UpFactory(SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(BlockPosParticleData typeIn, Level worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            BlockPos pos = typeIn.getBlockPos();
            return new ParticleTelepad(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, (TileEntityShortRangeTelepad) worldIn.getBlockEntity(pos), false);
        }
    }
}