package team.galacticraft.galacticraft.common.core.client.render.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import team.galacticraft.galacticraft.common.api.recipe.SchematicRegistry;
import team.galacticraft.galacticraft.core.entities.EntityHangingSchematic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class RenderSchematic extends EntityRenderer<EntityHangingSchematic>
{
    public RenderSchematic(EntityRenderDispatcher manager)
    {
        super(manager);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityHangingSchematic entity)
    {
        return SchematicRegistry.getSchematicTexture(entity.schematic);
    }

    @Override
    public void render(EntityHangingSchematic entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
    {
        matrixStackIn.pushPose();
//        GlStateManager.pushMatrix();
//        GlStateManager.translated(x, y, z);
//        GlStateManager.rotatef(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
        matrixStackIn.mulPose(new Quaternion(Vector3f.YP, entityYaw, true));
        GlStateManager._enableRescaleNormal();
//        this.bindEntityTexture(entity);
        float f = 0.0625F;
//        GlStateManager.scalef(f, f, f);
        matrixStackIn.scale(f, f, f);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entitySolid(this.getEntityTexture(entityIn)));
        this.renderPainting(entityIn, ivertexbuilder, matrixStackIn, entityIn.getWidth(), entityIn.getHeight());
        GlStateManager._disableRescaleNormal();
        GlStateManager._popMatrix();
//        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private void renderPainting(EntityHangingSchematic painting, VertexConsumer vertexBuilder, PoseStack matrixStackIn, int width, int height)
    {
        float f = (float) (-width) / 2.0F;
        float f1 = (float) (-height) / 2.0F;
        float f2 = 0.5F;
        float f3 = 0.75F;
        float f4 = 0.8125F;
        float f5 = 0.0F;
        float f6 = 0.0625F;
        float f7 = 0.75F;
        float f8 = 0.8125F;
        float f9 = 0.001953125F;
        float f10 = 0.001953125F;
        float f11 = 0.7519531F;
        float f12 = 0.7519531F;
        float f13 = 0.0F;
        float f14 = 0.0625F;

        for (int i = 0; i < width / 16; ++i)
        {
            for (int j = 0; j < height / 16; ++j)
            {
                float a = f + (float) ((i + 1) * 16);
                float b = f + (float) (i * 16);
                float c = f1 + (float) ((j + 1) * 16);
                float d = f1 + (float) (j * 16);
                int light = this.getLight(painting, (a + b) / 2.0F, (c + d) / 2.0F);
                float f19 = (float) (width - i * 16) / 32.0F;
                float f20 = (float) (width - (i + 1) * 16) / 32.0F;
                float f21 = (float) (height - j * 16) / 32.0F;
                float f22 = (float) (height - (j + 1) * 16) / 32.0F;
                Matrix4f matrix = matrixStackIn.last().pose();
                Matrix3f nMatrix = matrixStackIn.last().normal();
//                Tessellator tessellator = Tessellator.getInstance();
//                BufferBuilder worldrenderer = tessellator.getBuffer();
                addVertex(vertexBuilder, matrix, nMatrix, a, d, (-f2), f20, f21, light, 0.0F, 0.0F, -1.0F);
                addVertex(vertexBuilder, matrix, nMatrix, b, d, (-f2), f19, f21, light, 0.0F, 0.0F, -1.0F);
                addVertex(vertexBuilder, matrix, nMatrix, b, c, (-f2), f19, f22, light, 0.0F, 0.0F, -1.0F);
                addVertex(vertexBuilder, matrix, nMatrix, a, c, (-f2), f20, f22, light, 0.0F, 0.0F, -1.0F);
                addVertex(vertexBuilder, matrix, nMatrix, a, c, f2, f3, f5, light, 0.0F, 0.0F, 1.0F);
                addVertex(vertexBuilder, matrix, nMatrix, b, c, f2, f4, f5, light, 0.0F, 0.0F, 1.0F);
                addVertex(vertexBuilder, matrix, nMatrix, b, d, f2, f4, f6, light, 0.0F, 0.0F, 1.0F);
                addVertex(vertexBuilder, matrix, nMatrix, a, d, f2, f3, f6, light, 0.0F, 0.0F, 1.0F);
                addVertex(vertexBuilder, matrix, nMatrix, a, c, (-f2), f7, f9, light, 0.0F, 1.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, b, c, (-f2), f8, f9, light, 0.0F, 1.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, b, c, f2, f8, f10, light, 0.0F, 1.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, a, c, f2, f7, f10, light, 0.0F, 1.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, a, d, f2, f7, f9, light, 0.0F, -1.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, b, d, f2, f8, f9, light, 0.0F, -1.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, b, d, (-f2), f8, f10, light, 0.0F, -1.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, a, d, (-f2), f7, f10, light, 0.0F, -1.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, a, c, f2, f12, f13, light, -1.0F, 0.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, a, d, f2, f12, f14, light, -1.0F, 0.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, a, d, (-f2), f11, f14, light, -1.0F, 0.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, a, c, (-f2), f11, f13, light, -1.0F, 0.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, b, c, (-f2), f12, f13, light, 1.0F, 0.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, b, d, (-f2), f12, f14, light, 1.0F, 0.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, b, d, f2, f11, f14, light, 1.0F, 0.0F, 0.0F);
                addVertex(vertexBuilder, matrix, nMatrix, b, c, f2, f11, f13, light, 1.0F, 0.0F, 0.0F);
//                tessellator.draw();
            }
        }
    }

    private void addVertex(VertexConsumer vertexBuilder, Matrix4f matrix, Matrix3f nMatrix, float x, float y, float z, float tX, float tY, int light, float nX, float nY, float nZ)
    {
        vertexBuilder.vertex(matrix, x, y, z).color(255, 255, 255, 255).uv(tX, tY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(nMatrix, nX, nY, nZ).endVertex();
    }

    private int getLight(HangingEntity painting, double yOffset, double xzOffset)
    {
        int i1 = Mth.floor(painting.getX());
        int j1 = Mth.floor(painting.getY() + (yOffset / 16.0F));
        int k1 = Mth.floor(painting.getZ());
        Direction direction = painting.getDirection();
        if (direction == Direction.NORTH) {
            i1 = Mth.floor(painting.getX() + (xzOffset / 16.0F));
        }

        if (direction == Direction.WEST) {
            k1 = Mth.floor(painting.getZ() - (xzOffset / 16.0F));
        }

        if (direction == Direction.SOUTH) {
            i1 = Mth.floor(painting.getX() - (xzOffset / 16.0F));
        }

        if (direction == Direction.EAST) {
            k1 = Mth.floor(painting.getZ() + (xzOffset / 16.0F));
        }

        return LevelRenderer.getLightColor(painting.level, new BlockPos(i1, j1, k1));
    }
}
