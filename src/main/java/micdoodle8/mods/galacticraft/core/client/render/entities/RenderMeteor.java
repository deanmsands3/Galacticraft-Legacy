package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelMeteor;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderMeteor extends EntityRenderer<EntityMeteor>
{
    private static final ResourceLocation meteorTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/meteor.png");

    private final ModelMeteor modelMeteor;

    public RenderMeteor(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 1F;
        this.modelMeteor = new ModelMeteor();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityMeteor par1Entity)
    {
        return RenderMeteor.meteorTexture;
    }

    @Override
    public void render(EntityMeteor meteor, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
    {
//        GL11.glPushMatrix();
        matrixStackIn.pushPose();
//        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        final float interpolatedPitch = meteor.xRotO + (meteor.xRot - meteor.xRotO) * partialTicks;
        matrixStackIn.mulPose(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStackIn.mulPose(new Quaternion(Vector3f.ZN, interpolatedPitch, true));
        final float f = meteor.getSize();
//        GL11.glScalef(f / 2, f / 2, f / 2);
        matrixStackIn.scale(f / 2.0F, f / 2.0F, f / 2.0F);
//        this.bindEntityTexture(meteor);
//        this.modelMeteor.render(meteor, 0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.1F);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.modelMeteor.renderType(this.getEntityTexture(meteor)));
        this.modelMeteor.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glPopMatrix();
        matrixStackIn.popPose();
    }
}
