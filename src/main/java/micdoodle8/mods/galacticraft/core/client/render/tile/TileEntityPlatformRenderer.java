package micdoodle8.mods.galacticraft.core.client.render.tile;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockPlatform;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class TileEntityPlatformRenderer extends BlockEntityRenderer<TileEntityPlatform>
{
    public static class ModelPlatform extends Model
    {
        ModelPart panelMain;

        public ModelPlatform()
        {
            super(RenderType::entitySolid);
            this.texWidth = 128;
            this.texHeight = 128;
            this.panelMain = new ModelPart(this, 0, 0);
            this.panelMain.addBox(-22F, -3.5F, -22F, 44, 7, 44);
            this.panelMain.setPos(0F, 0F, 0F);
            this.panelMain.setTexSize(128, 128);
            this.panelMain.mirror = true;
            this.setRotation(this.panelMain, 0F, 0F, 0F);
        }

        private void setRotation(ModelPart model, float x, float y, float z)
        {
            model.xRot = x;
            model.yRot = y;
            model.zRot = z;
        }

        @Override
        public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.409090909091F, 0.409090909091F, 0.409090909091F);
            this.panelMain.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStackIn.popPose();
        }
    }

    public static final ResourceLocation platformTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/platform_moving.png");
    public static final ResourceLocation lightTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/light.png");
    private final ModelPlatform platform = new ModelPlatform();
    private static final Map<Integer, Float> lastYMap = new HashMap<>();
    private static float lastPartialTicks = -1F;
    private static Field yPosField = null;

    public TileEntityPlatformRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
        try
        {
            yPosField = Matrix4f.class.getDeclaredField("m13");
            yPosField.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void render(TileEntityPlatform platform, float partialTicks, PoseStack matStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        float currentyPos = 0.0F;
        try
        {
            currentyPos = (float) yPosField.get(matStack.last().pose());
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        if (partialTicks != lastPartialTicks)
        {
            lastPartialTicks = partialTicks;
            lastYMap.clear();
        }
        BlockState state = platform.getLevel().getBlockState(platform.getBlockPos());
        float yOffset = platform.getYOffset(partialTicks);
        if (state.getBlock() == GCBlocks.platform && state.getValue(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.NW)
        {
//            GlStateManager.pushMatrix();
            matStack.pushPose();
//            GlStateManager.translatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
            matStack.translate(0.5F, 0.5F, 0.5F);
//            GlStateManager.rotatef(90F, 0, 1F, 0F);
            matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
//            GlStateManager.translatef(-0.5F, -0.5F, 0.5F);
            matStack.translate(-0.5F, -0.5F, 0.5F);
            Lighting.turnOff();
            GlStateManager._disableRescaleNormal();

            // Render a moving platform
            boolean renderPlatformForThisTE = false;
            float newY = currentyPos + yOffset;
            int xz = tenLSB(platform.getBlockPos().getX()) << 10;
            xz += tenLSB(platform.getBlockPos().getZ());
            Float lastYF = lastYMap.get(xz);
            float lastY = lastYF == null ? -1 : lastYF;
            if (!platform.isMoving() || Math.abs(newY - lastY) > 0.001F || Math.abs(partialTicks - lastPartialTicks) > 0.001F)
            {
                renderPlatformForThisTE = true;
                lastYMap.put(xz, newY);
//                GlStateManager.pushMatrix();
                matStack.pushPose();
//                if (platform.isMoving())
//                {
//                    GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, platform.getMeanLightX(yOffset), platform.getMeanLightZ(yOffset));
//                }
//                else
//                {
//                    int light = platform.getBlendedLight();
//                    GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) (light % 65536), (float) (light / 65536));
//                }
//                GlStateManager.translatef(0F, 0.79F + yOffset, 0F);
                matStack.translate(0.0F, 0.79F + yOffset, 0.0F);
//                this.bindTexture(TileEntityPlatformRenderer.platformTexture);
                RenderType renderType = RenderType.entitySolid(platformTexture);
                VertexConsumer builder = bufferIn.getBuffer(renderType);
                this.platform.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//                GlStateManager.popMatrix();
                matStack.popPose();
            }

            if (platform.lightEnabled() || platform.isMoving())
            {
//                GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
                GlStateManager._disableLighting();
                GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager._disableTexture();

                RenderType renderType = RenderType.entitySolid(lightTexture);
                VertexConsumer builder = bufferIn.getBuffer(renderType);
//                this.bindTexture(TileEntityPlatformRenderer.lightTexture);
//                final Tessellator tess = Tessellator.getInstance();
//                BufferBuilder worldRenderer = tess.getBuffer();
                float frameA, frameB, frameC, frameD;

                // Draw the moving platform LogicalSide-lights
                if (platform.isMoving() && renderPlatformForThisTE)
                {
//                    GlStateManager.pushMatrix();
                    matStack.pushPose();
//                    GlStateManager.translatef(0F, 0.26F + yOffset, 0F);
                    matStack.translate(0.0F, 0.26F + yOffset, 0.0F);
//                    GlStateManager.color4f(1.0F, 0.84F, 65F / 255F, 1.0F);
                    float r = 1.0F;
                    float g = 0.84F;
                    float b = 65F / 255F;

                    float frameRadius = 1.126F / 2F;
                    frameB = frameRadius - 0.04F;
                    frameC = -frameB;
                    frameA = 0.552F;
                    frameD = 0.578F;
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    frameA = 0.482F;
                    frameD = 0.504666F;
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
//                    GlStateManager.rotatef(90F, 0, 1F, 0F);
                    matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
                    frameA = 0.552F;
                    frameD = 0.578F;
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    frameA = 0.482F;
                    frameD = 0.504666F;
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
//                    GlStateManager.rotatef(90F, 0, 1F, 0F);
                    matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
                    frameA = 0.552F;
                    frameD = 0.578F;
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    frameA = 0.482F;
                    frameD = 0.504666F;
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
//                    GlStateManager.rotatef(90F, 0, 1F, 0F);
                    matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
                    frameA = 0.552F;
                    frameD = 0.578F;
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    frameA = 0.482F;
                    frameD = 0.504666F;
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);

//                    GlStateManager.popMatrix();
                    matStack.popPose();
                }

                // Draw the activation lights
                if (platform.lightEnabled())
                {
                    float greyLevel = 125F / 255F;
                    float r = 0.0F;
                    float g = 0.0F;
                    float b = 0.0F;
                    switch (platform.lightColor())
                    {
                    case 1:
                        r = 1.0F;
                        g = 115F / 255F;
                        b = 115F / 255F;
                        break;
                    default:
                        r = greyLevel;
                        g = 1.0F;
                        b = greyLevel;
                    }

                    float frameY = 0.9376F;
                    frameC = 0.7F;
                    frameD = 0.58F;
                    frameB = frameC + 0.02F;
                    frameA = -frameD;
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameA, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameD, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameD, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameA, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
//                    GlStateManager.rotatef(90F, 0, 1F, 0F);
                    matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameA, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameD, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameD, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameA, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
//                    GlStateManager.rotatef(90F, 0, 1F, 0F);
                    matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameA, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameD, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameD, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameA, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
//                    GlStateManager.rotatef(90F, 0, 1F, 0F);
                    matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameA, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameD, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameD, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.last().pose(), matStack.last().normal(), builder, frameA, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                }

                // Restore the lighting state
                GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
                //? need to undo GlStateManager.glBlendFunc()?
                GlStateManager._enableLighting();
                GlStateManager._enableTexture();
//                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
            }

            GlStateManager._enableRescaleNormal();
            Lighting.turnBackOn();
//            GlStateManager.popMatrix();
            matStack.popPose();
        }
    }

    private void addVertex(Matrix4f mat, Matrix3f matNorm, VertexConsumer builder, float pX, float pY, float pZ, float tX, float tY, int nX, int nY, int nZ, int light) {
        addVertex(mat, matNorm, builder, pX, pY, pZ, tX, tY, nX, nY, nZ, light, 1.0F, 1.0F, 1.0F);
    }

    private void addVertex(Matrix4f mat, Matrix3f matNorm, VertexConsumer builder, float pX, float pY, float pZ, float tX, float tY, int nX, int nY, int nZ, int light, float r, float g, float b) {
        builder.vertex(mat, pX, pY, pZ).color(r, g, b, 1.0F).uv(tX, tY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matNorm, (float)nX, (float)nY, (float)nZ).endVertex();
    }

    private int tenLSB(int x)
    {
        if (x < 0)
        {
            return x % 1024 + 1024;
        }

        return x % 1024;
    }
}
