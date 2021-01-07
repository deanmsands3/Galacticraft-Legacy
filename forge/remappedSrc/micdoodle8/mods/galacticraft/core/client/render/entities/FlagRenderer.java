package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.FlagModel;
import micdoodle8.mods.galacticraft.core.entities.FlagEntity;
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
public class FlagRenderer extends EntityRenderer<FlagEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/flag/flag.png");
    private final FlagModel model = new FlagModel();

    public FlagRenderer(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 1F;
    }

    @Override
    public ResourceLocation getEntityTexture(FlagEntity entity)
    {
        return FlagRenderer.TEXTURE;
    }

    @Override
    public void render(FlagEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        RenderSystem.disableRescaleNormal();
        matrixStack.push();
        long seed = entity.getEntityId() * 493286711L;
        seed = seed * seed * 4392167121L + seed * 98761L;
        float seedX = (((seed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedY = (((seed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedZ = (((seed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        matrixStack.translate(seedX, seedY + 1.5F, seedZ);
        matrixStack.rotate(new Quaternion(Vector3f.YP, entity.getFacingAngle(), true));
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        IVertexBuilder ivertexbuilder = buffer.getBuffer(this.model.getRenderType(this.getEntityTexture(entity)));
        this.model.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.model.renderFlag(entity, entity.ticksExisted + partialTicks, matrixStack);
        matrixStack.pop();
    }

    @Override
    public boolean shouldRender(FlagEntity entity, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(1D, 2D, 1D);
        return entity.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}