package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.AstroMinerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class AstroMinerRenderer extends EntityRenderer<AstroMinerEntity>
{
    private static final float LSIZE = 0.12F;
    private static final float RETRACTIONSPEED = 0.02F;
    private float lastPartTime;

    public static ResourceLocation scanTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/misc/gradient.png");
    private IBakedModel mainModel;
    private IBakedModel hoverPadMain;
    private IBakedModel hoverPadGlow;
    private IBakedModel mainModelInactive;
    private IBakedModel modellaser1;
    private IBakedModel modellaser3;
    private IBakedModel modellasergl;

    private final NoiseModule wobbleX;
    private final NoiseModule wobbleY;
    private final NoiseModule wobbleZ;
    private final NoiseModule wobbleXX;
    private final NoiseModule wobbleYY;
    private final NoiseModule wobbleZZ;

    public AstroMinerRenderer(EntityRendererManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 2F;

        Random rand = new Random();
        this.wobbleX = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleX.amplitude = 0.5F;
        this.wobbleX.frequencyX = 0.025F;

        this.wobbleY = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleY.amplitude = 0.6F;
        this.wobbleY.frequencyX = 0.025F;

        this.wobbleZ = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleZ.amplitude = 0.1F;
        this.wobbleZ.frequencyX = 0.025F;

        this.wobbleXX = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleXX.amplitude = 0.1F;
        this.wobbleXX.frequencyX = 0.8F;

        this.wobbleYY = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleYY.amplitude = 0.15F;
        this.wobbleYY.frequencyX = 0.8F;

        this.wobbleZZ = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleZZ.amplitude = 0.04F;
        this.wobbleZZ.frequencyX = 0.8F;
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        ResourceLocation model = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/astro_miner_full.obj");
        this.mainModel = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("Hull", "Lasers"));
        this.hoverPadMain = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("HoverPad"));
        this.hoverPadGlow = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("Glow"));
        this.modellaser1 = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("Main_Laser_Front"));
        this.modellaser3 = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("Main_Laser_Center"));
        this.modellasergl = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("Main_Laser_Left_Guard"));
        this.mainModelInactive = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("Hull", "Lasers", "HoverPad"));
    }

    @Override
    public void render(AstroMinerEntity astroMiner, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        int ais = astroMiner.AIstate;
        boolean active = ais > AstroMinerEntity.AISTATE_ATBASE;
        float time = astroMiner.ticksExisted + partialTicks;
        float sinOfTheTime = (MathHelper.sin(time / 4) + 1F) / 4F + 0.5F;
        float wx = active ? this.wobbleX.getNoise(time) + this.wobbleXX.getNoise(time) : 0F;
        float wy = active ? this.wobbleY.getNoise(time) + this.wobbleYY.getNoise(time) : 0F;
        float wz = active ? this.wobbleZ.getNoise(time) + this.wobbleZZ.getNoise(time) : 0F;
        float partTime = partialTicks - this.lastPartTime;
        this.lastPartTime = partialTicks;

        while (partTime < 0)
        {
            partTime += 1F;
        }

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        RenderSystem.enableRescaleNormal();
        matrixStack.push();

        float rotPitch = astroMiner.prevRotationPitch + (astroMiner.rotationPitch - astroMiner.prevRotationPitch) * partialTicks;
        float rotYaw = astroMiner.prevRotationYaw + (astroMiner.rotationYaw - astroMiner.prevRotationYaw) * partialTicks;

        matrixStack.translate(0, 1.4F, 0);
        float partBlock;

        switch (astroMiner.facing)
        {
        case DOWN:
            partBlock = (float) (astroMiner.getPosY() % 2D);
            break;
        case UP:
            partBlock = 1F - (float) (astroMiner.getPosY() % 2D);
            break;
        case NORTH:
            partBlock = (float) (astroMiner.getPosZ() % 2D);
            break;
        case SOUTH:
            partBlock = 1F - (float) (astroMiner.getPosZ() % 2D);
            break;
        case WEST:
            partBlock = (float) (astroMiner.getPosX() % 2D);
            break;
        case EAST:
            partBlock = 1F - (float) (astroMiner.getPosX() % 2D);
            break;
        default:
            partBlock = 0F;
        }
        partBlock /= 0.06F;

        matrixStack.rotate(new Quaternion(Vector3f.YP, rotYaw + 180F, true));

        if (rotPitch != 0F)
        {
            matrixStack.translate(-0.65F, -0.65F, 0);
            matrixStack.rotate(new Quaternion(Vector3f.XP, rotPitch / 4F, true));
            matrixStack.translate(0.65F, 0.65F, 0);
        }

        matrixStack.translate(0F, -0.42F, 0.28F);
        matrixStack.scale(0.0495F, 0.0495F, 0.0495F);
        matrixStack.translate(wx, wy, wz);

        if (active)
        {
            ClientUtil.drawBakedModel(this.mainModel, buffer, matrixStack, packedLight);
            this.renderLaserModel(matrixStack, buffer, astroMiner.retraction, packedLight);

            //            float lightMapSaveX = GLX.lastBrightnessX;
            //            float lightMapSaveY = GLX.lastBrightnessY;
            //            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
            RenderSystem.disableLighting();
            RenderSystem.color4f(sinOfTheTime, sinOfTheTime, sinOfTheTime, 1.0F);
            ClientUtil.drawBakedModel(this.hoverPadMain, buffer, matrixStack, packedLight);

            RenderSystem.disableCull();
            RenderSystem.disableAlphaTest();
            RenderSystem.depthMask(false);
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            RenderSystem.enableBlend();
            RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            RenderSystem.color4f(sinOfTheTime, sinOfTheTime, sinOfTheTime, 0.6F);
            ClientUtil.drawBakedModel(this.hoverPadGlow, buffer, matrixStack, packedLight);
            RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            if (ais < AstroMinerEntity.AISTATE_DOCKING)
            {
                //This is the scanning lasers:
                Minecraft.getInstance().textureManager.bindTexture(scanTexture);
                final Tessellator tess = Tessellator.getInstance();
                RenderSystem.color4f(0, 0.6F, 1.0F, 0.2F);
                BufferBuilder worldRenderer = tess.getBuffer();
                float scanProgress = MathHelper.cos(partBlock * 0.012F * 6.283F) * 0.747F;
                float scanAngle = 0.69866F - scanProgress * scanProgress;
                float scanEndX = 38.77F * MathHelper.sin(scanAngle);
                float scanEndY = 32F;
                float scanEndZ = 38.77F * MathHelper.cos(scanAngle);
                scanEndZ += 20F;
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                worldRenderer.pos(15.6F, -0.6F, -20F).tex(0F, 0F).endVertex();
                worldRenderer.pos(15.6F + scanEndX, scanEndY - 0.6F, -scanEndZ).tex(1F, 0F).endVertex();
                worldRenderer.pos(15.6F + scanEndX, -0.6F - scanEndY, -scanEndZ).tex(1F, 1F).endVertex();
                worldRenderer.pos(15.6F, -0.7F, -20F).tex(0F, 1F).endVertex();
                tess.draw();
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                worldRenderer.pos(-15.6F, -0.6F, -20F).tex(0F, 0F).endVertex();
                worldRenderer.pos(-15.6F - scanEndX, scanEndY - 0.6F, -scanEndZ).tex(1F, 0F).endVertex();
                worldRenderer.pos(-15.6F - scanEndX, -0.6F - scanEndY, -scanEndZ).tex(1F, 1F).endVertex();
                worldRenderer.pos(-15.6F, -0.7F, -20F).tex(0F, 1F).endVertex();
                tess.draw();

                int removeCount = 0;
                int afterglowCount = 0;

                matrixStack.pop();
                matrixStack.push();
                matrixStack.translate((float) -astroMiner.getPosX(), (float) -astroMiner.getPosY(), (float) -astroMiner.getPosZ());

                for (Integer blockTime : astroMiner.laserTimes)
                {
                    if (blockTime < astroMiner.ticksExisted - 19)
                    {
                        removeCount++;
                    }
                    else if (blockTime < astroMiner.ticksExisted - 3)
                    {
                        afterglowCount++;
                    }
                }
                if (removeCount > 0)
                {
                    astroMiner.removeLaserBlocks(removeCount);
                }
                int count = 0;
                for (BlockVec3 blockLaser : astroMiner.laserBlocks)
                {
                    if (count < afterglowCount)
                    {
                        int fade = astroMiner.ticksExisted - astroMiner.laserTimes.get(count) - 8;
                        if (fade < 0)
                        {
                            fade = 0;
                        }
                        this.doAfterGlow(matrixStack, blockLaser, fade);
                    }
                    else
                    {
                        this.doLaser(matrixStack, astroMiner, blockLaser);
                    }
                    count++;
                }
                if (astroMiner.retraction > 0F)
                {
                    astroMiner.retraction -= RETRACTIONSPEED * partTime;
                    if (astroMiner.retraction < 0F)
                    {
                        astroMiner.retraction = 0F;
                    }
                }
                matrixStack.pop();
            }
            else
            {
                if (astroMiner.retraction < 1F)
                {
                    astroMiner.retraction += RETRACTIONSPEED * partTime;
                    if (astroMiner.retraction > 1F)
                    {
                        astroMiner.retraction = 1F;
                    }
                }
                matrixStack.pop();
            }
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderSystem.enableCull();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableLighting();
            RenderSystem.depthMask(true);
            //            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lightMapSaveX, lightMapSaveY);
        }
        else
        {
            ClientUtil.drawBakedModel(this.mainModelInactive, buffer, matrixStack, packedLight);
            this.renderLaserModel(matrixStack, buffer, astroMiner.retraction, packedLight);
            if (astroMiner.retraction < 1F)
            {
                astroMiner.retraction += RETRACTIONSPEED * partTime;
                if (astroMiner.retraction > 1F)
                {
                    astroMiner.retraction = 1F;
                }
            }
            matrixStack.pop();
        }
    }

    private void doAfterGlow(MatrixStack matrixStack, BlockVec3 blockLaser, int level)
    {
        matrixStack.push();
        matrixStack.translate(blockLaser.x, blockLaser.y, blockLaser.z);
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        RenderSystem.color4f(1.0F, 0.7F, 0.7F, 0.016667F * (12 - level));
        float cA = -0.01F;
        float cB = 1.01F;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cB, cA).tex(0F, 1F).endVertex();
        worldRenderer.pos(cB, cB, cA).tex(1F, 1F).endVertex();
        worldRenderer.pos(cB, cB, cB).tex(1F, 0F).endVertex();
        worldRenderer.pos(cA, cB, cB).tex(0F, 0F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cA, cA).tex(0F, 0F).endVertex();
        worldRenderer.pos(cA, cA, cB).tex(0F, 1F).endVertex();
        worldRenderer.pos(cB, cA, cB).tex(1F, 1F).endVertex();
        worldRenderer.pos(cB, cA, cA).tex(1F, 0F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cA, cA).tex(1F, 0F).endVertex();
        worldRenderer.pos(cA, cB, cA).tex(0F, 0F).endVertex();
        worldRenderer.pos(cA, cB, cB).tex(0F, 1F).endVertex();
        worldRenderer.pos(cA, cA, cB).tex(1F, 1F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cB, cA, cA).tex(1F, 1F).endVertex();
        worldRenderer.pos(cB, cA, cB).tex(1F, 0F).endVertex();
        worldRenderer.pos(cB, cB, cB).tex(0F, 0F).endVertex();
        worldRenderer.pos(cB, cB, cA).tex(0F, 1F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cA, cA).tex(1F, 0F).endVertex();
        worldRenderer.pos(1F, cA, cA).tex(0F, 0F).endVertex();
        worldRenderer.pos(1F, 1F, cA).tex(0F, 1F).endVertex();
        worldRenderer.pos(cA, 1F, cA).tex(1F, 1F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(1F, cA, 1F).tex(1F, 1F).endVertex();
        worldRenderer.pos(cA, cA, 1F).tex(1F, 0F).endVertex();
        worldRenderer.pos(cA, 1F, 1F).tex(0F, 0F).endVertex();
        worldRenderer.pos(1F, 1F, 1F).tex(0F, 1F).endVertex();
        tess.draw();
        matrixStack.pop();
    }

    private void doLaser(MatrixStack matrixStack, AstroMinerEntity entity, BlockVec3 blockLaser)
    {
        matrixStack.push();
        matrixStack.translate(blockLaser.x, blockLaser.y, blockLaser.z);
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        RenderSystem.color4f(1.0F, 0.7F, 0.7F, 0.2F);
        float cA = -0.01F;
        float cB = 1.01F;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cB, cA).tex(0F, 1F).endVertex();
        worldRenderer.pos(cB, cB, cA).tex(1F, 1F).endVertex();
        worldRenderer.pos(cB, cB, cB).tex(1F, 0F).endVertex();
        worldRenderer.pos(cA, cB, cB).tex(0F, 0F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cA, cA).tex(0F, 0F).endVertex();
        worldRenderer.pos(cA, cA, cB).tex(0F, 1F).endVertex();
        worldRenderer.pos(cB, cA, cB).tex(1F, 1F).endVertex();
        worldRenderer.pos(cB, cA, cA).tex(1F, 0F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cA, cA).tex(1F, 0F).endVertex();
        worldRenderer.pos(cA, cB, cA).tex(0F, 0F).endVertex();
        worldRenderer.pos(cA, cB, cB).tex(0F, 1F).endVertex();
        worldRenderer.pos(cA, cA, cB).tex(1F, 1F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cB, cA, cA).tex(1F, 1F).endVertex();
        worldRenderer.pos(cB, cA, cB).tex(1F, 0F).endVertex();
        worldRenderer.pos(cB, cB, cB).tex(0F, 0F).endVertex();
        worldRenderer.pos(cB, cB, cA).tex(0F, 1F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(cA, cA, cA).tex(1F, 0F).endVertex();
        worldRenderer.pos(1F, cA, cA).tex(0F, 0F).endVertex();
        worldRenderer.pos(1F, 1F, cA).tex(0F, 1F).endVertex();
        worldRenderer.pos(cA, 1F, cA).tex(1F, 1F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(1F, cA, 1F).tex(1F, 1F).endVertex();
        worldRenderer.pos(cA, cA, 1F).tex(1F, 0F).endVertex();
        worldRenderer.pos(cA, 1F, 1F).tex(0F, 0F).endVertex();
        worldRenderer.pos(1F, 1F, 1F).tex(0F, 1F).endVertex();
        tess.draw();

        RenderSystem.color4f(1.0F, 0.79F, 0.79F, 0.17F);
        float bb = 1.7F;
        float cc = 0.4F;
        float radiansYaw = entity.rotationYaw / Constants.RADIANS_TO_DEGREES;
        float radiansPitch = entity.rotationPitch / Constants.RADIANS_TO_DEGREES / 4F;
        float mainLaserX = bb * MathHelper.sin(radiansYaw) * MathHelper.cos(radiansPitch);
        float mainLaserY = cc + bb * MathHelper.sin(radiansPitch);
        float mainLaserZ = bb * MathHelper.cos(radiansYaw) * MathHelper.cos(radiansPitch);

        mainLaserX += entity.getPosX() - blockLaser.x;
        mainLaserY += entity.getPosY() - blockLaser.y;
        mainLaserZ += entity.getPosZ() - blockLaser.z;

        float xD = mainLaserX - 0.5F;
        float yD = mainLaserY - 0.5F;
        float zD = mainLaserZ - 0.5F;
        float xx, yy, zz;

        if (entity.facing.getIndex() > Direction.SOUTH.getIndex())
        {
            xx = xD < 0 ? cA : cB;
            this.drawLaserX(mainLaserX, mainLaserY, mainLaserZ, xx, 0.5F, 0.5F);
        }
        else if (entity.facing.getIndex() <= Direction.UP.getIndex())
        {
            yy = yD < 0 ? cA : cB;
            this.drawLaserY(mainLaserX, mainLaserY, mainLaserZ, 0.5F, yy, 0.5F);
        }
        else
        {
            zz = zD < 0 ? cA : cB;
            this.drawLaserZ(mainLaserX, mainLaserY, mainLaserZ, 0.5F, 0.5F, zz);
        }

        matrixStack.pop();
    }

    private void drawLaserX(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1, y1 - 0.01F, z1 - 0.01F).endVertex();
        worldRenderer.pos(x2, y2 - LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(x2, y2 + LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(x1, y1 + 0.01F, z1 - 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1, y1 - 0.01F, z1 + 0.01F).endVertex();
        worldRenderer.pos(x2, y2 - LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(x2, y2 + LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(x1, y1 + 0.01F, z1 + 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1, y1 - 0.01F, z1 - 0.01F).endVertex();
        worldRenderer.pos(x2, y2 - LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(x2, y2 - LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(x1, y1 - 0.01F, z1 + 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1, y1 + 0.01F, z1 + 0.01F).endVertex();
        worldRenderer.pos(x2, y2 + LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(x2, y2 + LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(x1, y1 + 0.01F, z1 - 0.01F).endVertex();
        tess.draw();
    }

    private void drawLaserY(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 - 0.01F, y1, z1 - 0.01F).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2, z2 - LSIZE).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2, z2 - LSIZE).endVertex();
        worldRenderer.pos(x1 + 0.01F, y1, z1 - 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 - 0.01F, y1, z1 + 0.01F).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2, z2 + LSIZE).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2, z2 + LSIZE).endVertex();
        worldRenderer.pos(x1 + 0.01F, y1, z1 + 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 - 0.01F, y1, z1 - 0.01F).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2, z2 - LSIZE).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2, z2 + LSIZE).endVertex();
        worldRenderer.pos(x1 - 0.01F, y1, z1 + 0.01F).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 + 0.01F, y1, z1 + 0.01F).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2, z2 + LSIZE).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2, z2 - LSIZE).endVertex();
        worldRenderer.pos(x1 + 0.01F, y1, z1 - 0.01F).endVertex();
        tess.draw();
    }

    private void drawLaserZ(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 - 0.01F, y1 - 0.01F, z1).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2 - LSIZE, z2).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2 + LSIZE, z2).endVertex();
        worldRenderer.pos(x1 - 0.01F, y1 + 0.01F, z1).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 + 0.01F, y1 - 0.01F, z1).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2 - LSIZE, z2).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2 + LSIZE, z2).endVertex();
        worldRenderer.pos(x1 + 0.01F, y1 + 0.01F, z1).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1 - 0.01F, y1 - 0.01F, z1).endVertex();
        worldRenderer.pos(x2 - LSIZE, y2 - LSIZE, z2).endVertex();
        worldRenderer.pos(x2 + LSIZE, y2 - LSIZE, z2).endVertex();
        worldRenderer.pos(x1 + 0.01F, y1 - 0.01F, z1).endVertex();
        tess.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x1, y1 + 0.01F, z1 + 0.01F).endVertex();
        worldRenderer.pos(x2, y2 + LSIZE, z2 + LSIZE).endVertex();
        worldRenderer.pos(x2, y2 + LSIZE, z2 - LSIZE).endVertex();
        worldRenderer.pos(x1, y1 + 0.01F, z1 - 0.01F).endVertex();
        tess.draw();
    }

    private void renderLaserModel(MatrixStack matrixStack, IRenderTypeBuffer buffer, float retraction, int packedLight)
    {
        float laserretraction = retraction / 0.8F;
        if (laserretraction > 1F)
        {
            laserretraction = 1F;
        }
        float guardmovement = (retraction - 0.6F) / 0.4F * 1.875F;
        if (guardmovement < 0F)
        {
            guardmovement = 0F;
        }
        matrixStack.push();
        float zadjust = laserretraction * 5F;
        float yadjust = zadjust;

        if (yadjust > 0.938F)
        {
            yadjust = 0.938F;
            zadjust = (zadjust - yadjust) * 2.5F + yadjust;
        }
        matrixStack.translate(0F, yadjust, zadjust);
        ClientUtil.drawBakedModel(this.modellaser1, buffer, matrixStack, packedLight);
        if (yadjust == 0.938F)
        {
            //Do not move laser centre into body
            matrixStack.translate(0F, 0F, -zadjust + 0.938F);
        }
        ClientUtil.drawBakedModel(this.modellaser3, buffer, matrixStack, packedLight);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(guardmovement, 0F, 0F);
        ClientUtil.drawBakedModel(this.modellasergl, buffer, matrixStack, packedLight);
        matrixStack.translate(-2 * guardmovement + 8.75F, 0F, 0F);
        ClientUtil.drawBakedModel(this.modellasergl, buffer, matrixStack, packedLight);
        matrixStack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(AstroMinerEntity entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public boolean shouldRender(AstroMinerEntity entity, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        return entity.isInRangeToRender3d(camX, camY, camZ);
    }
}