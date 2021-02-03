package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.MeteorChunkModel;
import micdoodle8.mods.galacticraft.core.entities.MeteorChunkEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class MeteorChunkRenderer extends EntityRenderer<MeteorChunkEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/item/meteor_chunk.png");
    private static final ResourceLocation HOT_TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/item/hot_meteor_chunk.png");
    private final MeteorChunkModel model = new MeteorChunkModel();

    public MeteorChunkRenderer(EntityRenderDispatcher renderManager)
    {
        super(renderManager);
        this.shadowRadius = 0.1F;
    }

    @Override
    public ResourceLocation getEntityTexture(MeteorChunkEntity entity)
    {
        if (entity.isHot())
        {
            return MeteorChunkRenderer.HOT_TEXTURE;
        }
        else
        {
            return MeteorChunkRenderer.TEXTURE;
        }
    }

    @Override
    public void render(MeteorChunkEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        matrixStack.pushPose();
        float interpolatedPitch = entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks;
        matrixStack.scale(0.3F, 0.3F, 0.3F);
        matrixStack.mulPose(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStack.mulPose(new Quaternion(Vector3f.ZN, interpolatedPitch, true));
        VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getEntityTexture(entity)));
        this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }
}