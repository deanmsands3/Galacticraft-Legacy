package team.galacticraft.galacticraft.common.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.client.model.ModelLander;
import team.galacticraft.galacticraft.core.entities.EntityLander;
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
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderLander extends EntityRenderer<EntityLander>
{
    private static final ResourceLocation landerTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/lander.png");

    protected ModelLander landerModel;

    public RenderLander(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 2F;
        this.landerModel = new ModelLander();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityLander par1Entity)
    {
        return RenderLander.landerTexture;
    }

    @Override
    public void render(EntityLander lander, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
    {
//        GL11.glPushMatrix();
        matrixStackIn.pushPose();
        final float interpolatedPitch = lander.xRotO + (lander.xRot - lander.xRotO) * partialTicks;
//        GL11.glTranslatef((float) par2, (float) par4 + 1.55F, (float) par6);
        matrixStackIn.translate(0.0F, 1.55F, 0.0F);
//        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(-interpolatedPitch, 0.0F, 0.0F, 1.0F);
        matrixStackIn.mulPose(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStackIn.mulPose(new Quaternion(Vector3f.ZN, interpolatedPitch, true));

        float f6 = lander.timeSinceHit - partialTicks;
        float f7 = lander.currentDamage - partialTicks;

        if (f7 < 0.0F)
        {
            f7 = 0.0F;
        }

        if (f6 > 0.0F)
        {
            GL11.glRotatef((float) Math.sin(f6) * 0.2F * f6 * f7 / 25.0F, 1.0F, 0.0F, 0.0F);
        }

//        this.bindEntityTexture(lander);
//        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
//        this.landerModel.render(lander, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.landerModel.renderType(this.getEntityTexture(lander)));
        this.landerModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glPopMatrix();
        matrixStackIn.popPose();
    }

    @Override
    public boolean shouldRender(EntityLander lander, Frustum camera, double camX, double camY, double camZ)
    {
        AABB axisalignedbb = lander.getBoundingBox().inflate(2D, 1D, 2D);
        return lander.shouldRender(camX, camY, camZ) && camera.isVisible(axisalignedbb);
    }
}
