package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.entities.HangingSchematicEntity;
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

@OnlyIn(Dist.CLIENT)
public class HangingSchematicRenderer extends EntityRenderer<HangingSchematicEntity>
{
    public HangingSchematicRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
    }

    @Override
    public ResourceLocation getEntityTexture(HangingSchematicEntity entity)
    {
        return SchematicRegistry.getSchematicTexture(entity.schematic);
    }

    @Override
    public void render(HangingSchematicEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        matrixStack.pushPose();
        matrixStack.mulPose(new Quaternion(Vector3f.YP, entityYaw, true));
        RenderSystem.enableRescaleNormal();
        float f = 0.0625F;
        matrixStack.scale(f, f, f);
        VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.entitySolid(this.getEntityTexture(entity)));
        this.renderPainting(entity, ivertexbuilder, matrixStack, entity.getWidth(), entity.getHeight());
        RenderSystem.disableRescaleNormal();
        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    private void renderPainting(HangingSchematicEntity entity, VertexConsumer builder, PoseStack matrixStack, int width, int height)
    {
        float f = -width / 2.0F;
        float f1 = -height / 2.0F;
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
                float a = f + (i + 1) * 16;
                float b = f + i * 16;
                float c = f1 + (j + 1) * 16;
                float d = f1 + j * 16;
                int light = this.getLight(entity, (a + b) / 2.0F, (c + d) / 2.0F);
                float f19 = (width - i * 16) / 32.0F;
                float f20 = (width - (i + 1) * 16) / 32.0F;
                float f21 = (height - j * 16) / 32.0F;
                float f22 = (height - (j + 1) * 16) / 32.0F;
                Matrix4f matrix = matrixStack.last().pose();
                Matrix3f nMatrix = matrixStack.last().normal();
                this.addVertex(builder, matrix, nMatrix, a, d, -f2, f20, f21, light, 0.0F, 0.0F, -1.0F);
                this.addVertex(builder, matrix, nMatrix, b, d, -f2, f19, f21, light, 0.0F, 0.0F, -1.0F);
                this.addVertex(builder, matrix, nMatrix, b, c, -f2, f19, f22, light, 0.0F, 0.0F, -1.0F);
                this.addVertex(builder, matrix, nMatrix, a, c, -f2, f20, f22, light, 0.0F, 0.0F, -1.0F);
                this.addVertex(builder, matrix, nMatrix, a, c, f2, f3, f5, light, 0.0F, 0.0F, 1.0F);
                this.addVertex(builder, matrix, nMatrix, b, c, f2, f4, f5, light, 0.0F, 0.0F, 1.0F);
                this.addVertex(builder, matrix, nMatrix, b, d, f2, f4, f6, light, 0.0F, 0.0F, 1.0F);
                this.addVertex(builder, matrix, nMatrix, a, d, f2, f3, f6, light, 0.0F, 0.0F, 1.0F);
                this.addVertex(builder, matrix, nMatrix, a, c, -f2, f7, f9, light, 0.0F, 1.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, b, c, -f2, f8, f9, light, 0.0F, 1.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, b, c, f2, f8, f10, light, 0.0F, 1.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, a, c, f2, f7, f10, light, 0.0F, 1.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, a, d, f2, f7, f9, light, 0.0F, -1.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, b, d, f2, f8, f9, light, 0.0F, -1.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, b, d, -f2, f8, f10, light, 0.0F, -1.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, a, d, -f2, f7, f10, light, 0.0F, -1.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, a, c, f2, f12, f13, light, -1.0F, 0.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, a, d, f2, f12, f14, light, -1.0F, 0.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, a, d, -f2, f11, f14, light, -1.0F, 0.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, a, c, -f2, f11, f13, light, -1.0F, 0.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, b, c, -f2, f12, f13, light, 1.0F, 0.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, b, d, -f2, f12, f14, light, 1.0F, 0.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, b, d, f2, f11, f14, light, 1.0F, 0.0F, 0.0F);
                this.addVertex(builder, matrix, nMatrix, b, c, f2, f11, f13, light, 1.0F, 0.0F, 0.0F);
            }
        }
    }

    private void addVertex(VertexConsumer vertexBuilder, Matrix4f matrix, Matrix3f nMatrix, float x, float y, float z, float tX, float tY, int light, float nX, float nY, float nZ)
    {
        vertexBuilder.vertex(matrix, x, y, z).color(255, 255, 255, 255).uv(tX, tY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(nMatrix, nX, nY, nZ).endVertex();
    }

    private int getLight(HangingEntity entity, double yOffset, double xzOffset)
    {
        int i1 = Mth.floor(entity.getX());
        int j1 = Mth.floor(entity.getY() + yOffset / 16.0F);
        int k1 = Mth.floor(entity.getZ());
        Direction direction = entity.getDirection();

        if (direction == Direction.NORTH)
        {
            i1 = Mth.floor(entity.getX() + xzOffset / 16.0F);
        }

        if (direction == Direction.WEST)
        {
            k1 = Mth.floor(entity.getZ() - xzOffset / 16.0F);
        }

        if (direction == Direction.SOUTH)
        {
            i1 = Mth.floor(entity.getX() - xzOffset / 16.0F);
        }

        if (direction == Direction.EAST)
        {
            k1 = Mth.floor(entity.getZ() + xzOffset / 16.0F);
        }

        return LevelRenderer.getLightColor(entity.level, new BlockPos(i1, j1, k1));
    }
}