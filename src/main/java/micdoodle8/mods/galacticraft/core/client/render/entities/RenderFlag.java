package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelFlag;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderFlag extends EntityRenderer<EntityFlag>
{
    public static ResourceLocation flagTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/flag.png");

    protected ModelFlag modelFlag;

    public RenderFlag(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 1F;
        this.modelFlag = new ModelFlag();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityFlag entity)
    {
        return RenderFlag.flagTexture;
    }

    @Override
    public void render(EntityFlag entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        GlStateManager.disableRescaleNormal();
//        GlStateManager.pushMatrix();
        matrixStackIn.push();
        long seed = entity.getEntityId() * 493286711L;
        seed = seed * seed * 4392167121L + seed * 98761L;
        float seedX = (((seed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedY = (((seed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedZ = (((seed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
//        GlStateManager.translatef(seedX, seedY + 1.5F, seedZ);
        matrixStackIn.translate(seedX, seedY + 1.5F, seedZ);
//        GlStateManager.translatef((float) x, (float) y, (float) z);
//        GlStateManager.rotatef(180.0F - entity.getFacingAngle(), 0.0F, 1.0F, 0.0F);
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, entity.getFacingAngle(), true));
//        this.bindEntityTexture(entity);
//        GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.modelFlag.getRenderType(this.getEntityTexture(entity)));
        this.modelFlag.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.modelFlag.renderFlag(entity, entity.ticksExisted + partialTicks, matrixStackIn);
//        GlStateManager.popMatrix();
        matrixStackIn.pop();
    }

    @Override
    public boolean shouldRender(EntityFlag flag, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = flag.getBoundingBox().grow(1D, 2D, 1D);
        return flag.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
