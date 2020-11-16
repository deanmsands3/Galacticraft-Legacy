package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelMeteorChunk;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteorChunk;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class RenderMeteorChunk extends EntityRenderer<EntityMeteorChunk>
{
    private static final ResourceLocation meteorChunkTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/blocks/meteor_chunk.png");
    private static final ResourceLocation meteorChunkHotTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/blocks/meteor_chunk_hot.png");
    private final ModelMeteorChunk modelMeteor;

    public RenderMeteorChunk(EntityRendererManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.1F;
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
    public void render(EntityMeteorChunk entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
//        GlStateManager.pushMatrix();
        matrixStackIn.push();
        final float interpolatedPitch = entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks;
//        GlStateManager.translatef((float) x, (float) y, (float) z);
//        GlStateManager.scalef(0.3F, 0.3F, 0.3F);
        matrixStackIn.scale(0.3F, 0.3F, 0.3F);
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.ZN, interpolatedPitch, true));
//        GlStateManager.rotatef(yaw, 1.0F, 0.0F, 0.0F);
//        GlStateManager.rotatef(pitch, 0.0F, 0.0F, 1.0F);
//        this.bindEntityTexture(entity);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.modelMeteor.getRenderType(this.getEntityTexture(entityIn)));
        this.modelMeteor.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//        GlStateManager.popMatrix();
        matrixStackIn.pop();
    }
}
