package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelLander;
import micdoodle8.mods.galacticraft.core.entities.LanderEntity;
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
public class LanderRenderer extends EntityRenderer<LanderEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/lander.png");
    private final ModelLander landerModel = new ModelLander();

    public LanderRenderer(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 2F;
    }

    @Override
    public ResourceLocation getEntityTexture(LanderEntity entity)
    {
        return TEXTURE;
    }

    @Override
    public void render(LanderEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        matrixStack.push();
        final float interpolatedPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        matrixStack.translate(0.0F, 1.55F, 0.0F);
        matrixStack.rotate(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStack.rotate(new Quaternion(Vector3f.ZN, interpolatedPitch, true));
        float f6 = entity.timeSinceHit - partialTicks;
        float f7 = entity.currentDamage - partialTicks;

        if (f7 < 0.0F)
        {
            f7 = 0.0F;
        }

        if (f6 > 0.0F)
        {
            matrixStack.rotate(new Quaternion(Vector3f.XP, (float) Math.sin(f6) * 0.2F * f6 * f7 / 25.0F, true));
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        IVertexBuilder ivertexbuilder = buffer.getBuffer(this.landerModel.getRenderType(this.getEntityTexture(entity)));
        this.landerModel.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }

    @Override
    public boolean shouldRender(LanderEntity entity, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(2D, 1D, 2D);
        return entity.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}