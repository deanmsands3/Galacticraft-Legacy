package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelMeteor;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderMeteor extends EntityRenderer<EntityMeteor>
{
    private static final ResourceLocation meteorTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/meteor.png");

    private final ModelMeteor modelMeteor;

    public RenderMeteor(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 1F;
        this.modelMeteor = new ModelMeteor();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityMeteor par1Entity)
    {
        return RenderMeteor.meteorTexture;
    }

    @Override
    public void render(EntityMeteor meteor, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
//        GL11.glPushMatrix();
        matrixStackIn.push();
//        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        final float interpolatedPitch = meteor.prevRotationPitch + (meteor.rotationPitch - meteor.prevRotationPitch) * partialTicks;
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.ZN, interpolatedPitch, true));
        final float f = meteor.getSize();
//        GL11.glScalef(f / 2, f / 2, f / 2);
        matrixStackIn.scale(f / 2.0F, f / 2.0F, f / 2.0F);
//        this.bindEntityTexture(meteor);
//        this.modelMeteor.render(meteor, 0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.1F);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.modelMeteor.getRenderType(this.getEntityTexture(meteor)));
        this.modelMeteor.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glPopMatrix();
        matrixStackIn.pop();
    }
}
