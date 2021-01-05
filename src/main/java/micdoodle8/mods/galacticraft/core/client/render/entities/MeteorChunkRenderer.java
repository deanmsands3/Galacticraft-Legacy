package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.MeteorChunkModel;
import micdoodle8.mods.galacticraft.core.entities.MeteorChunkEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class MeteorChunkRenderer extends EntityRenderer<MeteorChunkEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/item/meteor_chunk.png");
    private static final ResourceLocation HOT_TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/item/hot_meteor_chunk.png");
    private final MeteorChunkModel model = new MeteorChunkModel();

    public MeteorChunkRenderer(EntityRendererManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.1F;
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
    public void render(MeteorChunkEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        matrixStack.push();
        float interpolatedPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        matrixStack.scale(0.3F, 0.3F, 0.3F);
        matrixStack.rotate(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStack.rotate(new Quaternion(Vector3f.ZN, interpolatedPitch, true));
        IVertexBuilder ivertexbuilder = buffer.getBuffer(this.model.getRenderType(this.getEntityTexture(entity)));
        this.model.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }
}