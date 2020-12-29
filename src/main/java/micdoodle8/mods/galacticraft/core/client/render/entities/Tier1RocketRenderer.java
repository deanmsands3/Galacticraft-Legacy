package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.Tier1RocketModel;
import micdoodle8.mods.galacticraft.core.entities.Tier1RocketEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
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
public class Tier1RocketRenderer extends EntityRenderer<Tier1RocketEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/tier_1_rocket.png");
    private final Tier1RocketModel model = new Tier1RocketModel();

    public Tier1RocketRenderer(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 0.9F;
    }

    @Override
    public ResourceLocation getEntityTexture(Tier1RocketEntity entity)
    {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(Tier1RocketEntity entity, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(0.6D, 1D, 0.6D);
        return entity.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }

    @Override
    public void render(Tier1RocketEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        matrixStack.push();
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        IVertexBuilder ivertexbuilder = buffer.getBuffer(this.model.getRenderType(this.getEntityTexture(entity)));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(-pitch));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-yaw));
        matrixStack.translate(0.0F, entity.getRenderOffsetY(), 0.0F);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.model.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }
}