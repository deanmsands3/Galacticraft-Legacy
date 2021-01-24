package micdoodle8.mods.galacticraft.planets.mars.client.fx;

import micdoodle8.mods.galacticraft.core.client.fx.BlockPosParticleData;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleDrip extends TextureSheetParticle
{
    private final SpriteSet animatedSprite;
    private int bobTimer;

    public ParticleDrip(Level world, double x, double y, double z, SpriteSet animatedSprite)
    {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.xd = this.yd = this.zd = 0.0D;
//        this.setParticleTextureIndex(113);
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
        this.bobTimer = 40;
        this.lifetime = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
        this.xd = this.yd = this.zd = 0.0D;
        this.animatedSprite = animatedSprite;
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
        this.rCol = 0.0F;
        this.gCol = 0.2F;
        this.bCol = 0.1F;
        this.yd -= this.gravity;

        if (this.bobTimer-- > 0)
        {
            this.xd *= 0.02D;
            this.yd *= 0.02D;
            this.zd *= 0.02D;
//            this.setParticleTextureIndex(113);
        }
        else
        {
//            this.setParticleTextureIndex(112);
        }

        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.9800000190734863D;
        this.yd *= 0.9800000190734863D;
        this.zd *= 0.9800000190734863D;

        if (this.lifetime-- <= 0)
        {
            this.remove();
        }

        if (this.onGround)
        {
            this.remove();
        }

        BlockPos pos = new BlockPos(this.x, this.y, this.z);
        BlockState state = this.level.getBlockState(pos);
        Material material = state.getMaterial();

        if (material.isLiquid() || material.isSolid())
        {
            double d0 = 0.0D;

            if (state.getBlock() instanceof LiquidBlock)
            {
                d0 = ((LiquidBlock) state.getBlock()).getFluid().getHeight(((LiquidBlock) state.getBlock()).getFluidState(state));
            }

            double d1 = Mth.floor(this.y) + 1 - d0;

            if (this.y < d1)
            {
                this.remove();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
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
            return new ParticleDrip(worldIn, x, y, z, this.spriteSet);
        }
    }
}