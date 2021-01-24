package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.MeteorModel;
import micdoodle8.mods.galacticraft.core.entities.MeteorEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MeteorRenderer extends EntityRenderer<MeteorEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/meteor.png");
    private final MeteorModel meteor = new MeteorModel();

    public MeteorRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 1F;
    }

    @Override
    public ResourceLocation getEntityTexture(MeteorEntity entity)
    {
        return MeteorRenderer.TEXTURE;
    }

    @Override
    public void render(MeteorEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        matrixStack.pushPose();
        float interpolatedPitch = entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks;
        matrixStack.mulPose(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStack.mulPose(new Quaternion(Vector3f.ZN, interpolatedPitch, true));
        float f = entity.getSize();
        matrixStack.scale(f / 2.0F, f / 2.0F, f / 2.0F);
        VertexConsumer ivertexbuilder = buffer.getBuffer(this.meteor.renderType(this.getEntityTexture(entity)));
        this.meteor.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }
}