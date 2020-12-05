package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelParaChest;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class RenderParaChest extends EntityRenderer<EntityParachest>
{
    private static final ResourceLocation[] textures = {new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/plain.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/orange.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/magenta.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/blue.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/yellow.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/lime.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/pink.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/darkgray.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/gray.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/teal.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/purple.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/darkblue.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/brown.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/darkgreen.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/red.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/black.png")};
    public static final ResourceLocation PARACHEST_TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachest.png");

    private final ModelParaChest chestModel;

    public RenderParaChest(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 1F;
        this.chestModel = new ModelParaChest();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityParachest entity)
    {
        return RenderParaChest.textures[entity.color.ordinal()];
    }

    @Override
    public void render(EntityParachest entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
    {
        matrixStackIn.pushPose();
//        GL11.glPushMatrix();
//        GL11.glTranslatef((float) x - 0.5F, (float) y, (float) z);

//        this.bindEntityTexture(entityIn);

        if (entityIn.isAlive())
        {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.chestModel.renderType(this.getEntityTexture(entityIn)));
            this.chestModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }

//        GL11.glPopMatrix();
        matrixStackIn.popPose();
    }
}
