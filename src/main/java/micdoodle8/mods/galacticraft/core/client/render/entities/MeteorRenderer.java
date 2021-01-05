package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.MeteorModel;
import micdoodle8.mods.galacticraft.core.entities.MeteorEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MeteorRenderer extends EntityRenderer<MeteorEntity>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/meteor.png");
    private final MeteorModel meteor = new MeteorModel();

    public MeteorRenderer(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 1F;
    }

    @Override
    public ResourceLocation getEntityTexture(MeteorEntity entity)
    {
        return MeteorRenderer.TEXTURE;
    }

    @Override
    public void render(MeteorEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        matrixStack.push();
        float interpolatedPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        matrixStack.rotate(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStack.rotate(new Quaternion(Vector3f.ZN, interpolatedPitch, true));
        float f = entity.getSize();
        matrixStack.scale(f / 2.0F, f / 2.0F, f / 2.0F);
        IVertexBuilder ivertexbuilder = buffer.getBuffer(this.meteor.getRenderType(this.getEntityTexture(entity)));
        this.meteor.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }
}