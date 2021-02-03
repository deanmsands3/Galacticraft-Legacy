package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.Tier1RocketModel;
import micdoodle8.mods.galacticraft.core.entities.Tier1RocketEntity;
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
public class Tier1RocketRenderer extends EntityRenderer<Tier1RocketEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/tier_1_rocket.png");
    private final Tier1RocketModel model = new Tier1RocketModel();

    public Tier1RocketRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 0.9F;
    }

    @Override
    public ResourceLocation getEntityTexture(Tier1RocketEntity entity)
    {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(Tier1RocketEntity entity, Frustum camera, double camX, double camY, double camZ)
    {
        AABB axisalignedbb = entity.getBoundingBox().inflate(0.6D, 1D, 0.6D);
        return entity.shouldRender(camX, camY, camZ) && camera.isVisible(axisalignedbb);
    }

    @Override
    public void render(Tier1RocketEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        matrixStack.pushPose();
        float pitch = entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks;
        float yaw = entity.yRotO + (entity.yRot - entity.yRotO) * partialTicks;
        VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getEntityTexture(entity)));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-pitch));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-yaw));
        matrixStack.translate(0.0F, entity.getRenderOffsetY(), 0.0F);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }
}