package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ParachestModel;
import micdoodle8.mods.galacticraft.core.entities.ParachestEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParachestRenderer extends EntityRenderer<ParachestEntity>
{
    private static final ResourceLocation[] PARACHUTE_TEXTURES = {new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/white.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/orange.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/magenta.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/light_blue.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/yellow.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/lime.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/pink.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/gray.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/light_gray.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/cyan.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/purple.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/blue.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/brown.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/green.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/red.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/black.png")};
    private final ParachestModel model = new ParachestModel();
    private static final ResourceLocation PARACHEST_TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachest.png");

    // TODO Fix rendering
    public ParachestRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 1F;
    }

    @Override
    public ResourceLocation getEntityTexture(ParachestEntity entity)
    {
        return ParachestRenderer.PARACHUTE_TEXTURES[entity.color.ordinal()];
    }

    @Override
    public void render(ParachestEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        matrixStack.pushPose();
        VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getEntityTexture(entity)));
        this.model.renderParachute(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY);
        ivertexbuilder = buffer.getBuffer(this.model.renderType(PARACHEST_TEXTURE));
        this.model.renderChest(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY);
        matrixStack.popPose();
    }
}