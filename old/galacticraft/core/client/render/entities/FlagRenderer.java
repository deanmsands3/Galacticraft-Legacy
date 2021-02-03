package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.FlagModel;
import micdoodle8.mods.galacticraft.core.entities.FlagEntity;
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
public class FlagRenderer extends EntityRenderer<FlagEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/flag/flag.png");
    private final FlagModel model = new FlagModel();

    public FlagRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 1F;
    }

    @Override
    public ResourceLocation getEntityTexture(FlagEntity entity)
    {
        return FlagRenderer.TEXTURE;
    }

    @Override
    public void render(FlagEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        RenderSystem.disableRescaleNormal();
        matrixStack.pushPose();
        long seed = entity.getId() * 493286711L;
        seed = seed * seed * 4392167121L + seed * 98761L;
        float seedX = (((seed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedY = (((seed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedZ = (((seed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        matrixStack.translate(seedX, seedY + 1.5F, seedZ);
        matrixStack.mulPose(new Quaternion(Vector3f.YP, entity.getFacingAngle(), true));
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getEntityTexture(entity)));
        this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.model.renderFlag(entity, entity.tickCount + partialTicks, matrixStack);
        matrixStack.popPose();
    }

    @Override
    public boolean shouldRender(FlagEntity entity, Frustum camera, double camX, double camY, double camZ)
    {
        AABB axisalignedbb = entity.getBoundingBox().inflate(1D, 2D, 1D);
        return entity.shouldRender(camX, camY, camZ) && camera.isVisible(axisalignedbb);
    }
}