package team.galacticraft.galacticraft.common.core.client.render.entities.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import team.galacticraft.galacticraft.common.core.client.model.ModelEvolvedEnderman;
import team.galacticraft.galacticraft.common.core.entities.EntityEvolvedEnderman;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class LayerEvolvedEndermanHeldBlock extends RenderLayer<EntityEvolvedEnderman, ModelEvolvedEnderman>
{
    public LayerEvolvedEndermanHeldBlock(RenderLayerParent<EntityEvolvedEnderman, ModelEvolvedEnderman> p_i50949_1_) {
        super(p_i50949_1_);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityEvolvedEnderman entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        BlockState blockstate = entitylivingbaseIn.getCarriedBlock();
        if (blockstate != null) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0D, 0.6875D, -0.75D);
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(20.0F));
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(45.0F));
            matrixStackIn.translate(0.25D, 0.1875D, 0.25D);
            float f = 0.5F;
            matrixStackIn.scale(-0.5F, -0.5F, 0.5F);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockstate, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
            matrixStackIn.popPose();
        }
    }
}
