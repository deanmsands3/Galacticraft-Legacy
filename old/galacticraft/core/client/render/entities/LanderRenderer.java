package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelLander;
import micdoodle8.mods.galacticraft.core.entities.LanderEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LanderRenderer extends EntityRenderer<LanderEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/lander.png");
    private final ModelLander landerModel = new ModelLander();

    public LanderRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 2F;
    }

    @Override
    public ResourceLocation getEntityTexture(LanderEntity entity)
    {
        return TEXTURE;
    }

    @Override
    public void render(LanderEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        matrixStack.pushPose();
        final float interpolatedPitch = entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks;
        matrixStack.translate(0.0F, 1.55F, 0.0F);
        matrixStack.mulPose(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStack.mulPose(new Quaternion(Vector3f.ZN, interpolatedPitch, true));
        float f6 = entity.timeSinceHit - partialTicks;
        float f7 = entity.currentDamage - partialTicks;

        if (f7 < 0.0F)
        {
            f7 = 0.0F;
        }

        if (f6 > 0.0F)
        {
            matrixStack.mulPose(new Quaternion(Vector3f.XP, (float) Math.sin(f6) * 0.2F * f6 * f7 / 25.0F, true));
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        VertexConsumer ivertexbuilder = buffer.getBuffer(this.landerModel.renderType(this.getEntityTexture(entity)));
        this.landerModel.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }

    @Override
    public boolean shouldRender(LanderEntity entity, Frustum camera, double camX, double camY, double camZ)
    {
        AABB axisalignedbb = entity.getBoundingBox().inflate(2D, 1D, 2D);
        return entity.shouldRender(camX, camY, camZ) && camera.isVisible(axisalignedbb);
    }
}