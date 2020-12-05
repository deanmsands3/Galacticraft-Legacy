package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelMeteorChunk;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteorChunk;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RenderMeteorChunk extends EntityRenderer<EntityMeteorChunk>
{
    private static final ResourceLocation meteorChunkTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/blocks/meteor_chunk.png");
    private static final ResourceLocation meteorChunkHotTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/blocks/meteor_chunk_hot.png");
    private final ModelMeteorChunk modelMeteor;

    public RenderMeteorChunk(EntityRenderDispatcher renderManager)
    {
        super(renderManager);
        this.shadowRadius = 0.1F;
        this.modelMeteor = new ModelMeteorChunk();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityMeteorChunk entity)
    {
        if (entity.isHot())
        {
            return RenderMeteorChunk.meteorChunkHotTexture;
        }
        else
        {
            return RenderMeteorChunk.meteorChunkTexture;
        }
    }

    @Override
    public void render(EntityMeteorChunk entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
    {
//        GlStateManager.pushMatrix();
        matrixStackIn.pushPose();
        final float interpolatedPitch = entityIn.xRotO + (entityIn.xRot - entityIn.xRotO) * partialTicks;
//        GlStateManager.translatef((float) x, (float) y, (float) z);
//        GlStateManager.scalef(0.3F, 0.3F, 0.3F);
        matrixStackIn.scale(0.3F, 0.3F, 0.3F);
        matrixStackIn.mulPose(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStackIn.mulPose(new Quaternion(Vector3f.ZN, interpolatedPitch, true));
//        GlStateManager.rotatef(yaw, 1.0F, 0.0F, 0.0F);
//        GlStateManager.rotatef(pitch, 0.0F, 0.0F, 1.0F);
//        this.bindEntityTexture(entity);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.modelMeteor.renderType(this.getEntityTexture(entityIn)));
        this.modelMeteor.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//        GlStateManager.popMatrix();
        matrixStackIn.popPose();
    }
}
