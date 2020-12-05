package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelFlag;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class RenderFlag extends EntityRenderer<EntityFlag>
{
    public static ResourceLocation flagTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/flag.png");

    protected ModelFlag modelFlag;

    public RenderFlag(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 1F;
        this.modelFlag = new ModelFlag();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityFlag entity)
    {
        return RenderFlag.flagTexture;
    }

    @Override
    public void render(EntityFlag entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
    {
        GlStateManager._disableRescaleNormal();
//        GlStateManager.pushMatrix();
        matrixStackIn.pushPose();
        long seed = entity.getId() * 493286711L;
        seed = seed * seed * 4392167121L + seed * 98761L;
        float seedX = (((seed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedY = (((seed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedZ = (((seed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
//        GlStateManager.translatef(seedX, seedY + 1.5F, seedZ);
        matrixStackIn.translate(seedX, seedY + 1.5F, seedZ);
//        GlStateManager.translatef((float) x, (float) y, (float) z);
//        GlStateManager.rotatef(180.0F - entity.getFacingAngle(), 0.0F, 1.0F, 0.0F);
        matrixStackIn.mulPose(new Quaternion(Vector3f.YP, entity.getFacingAngle(), true));
//        this.bindEntityTexture(entity);
//        GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.modelFlag.renderType(this.getEntityTexture(entity)));
        this.modelFlag.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.modelFlag.renderFlag(entity, entity.tickCount + partialTicks, matrixStackIn);
//        GlStateManager.popMatrix();
        matrixStackIn.popPose();
    }

    @Override
    public boolean shouldRender(EntityFlag flag, Frustum camera, double camX, double camY, double camZ)
    {
        AABB axisalignedbb = flag.getBoundingBox().inflate(1D, 2D, 1D);
        return flag.shouldRender(camX, camY, camZ) && camera.isVisible(axisalignedbb);
    }
}
