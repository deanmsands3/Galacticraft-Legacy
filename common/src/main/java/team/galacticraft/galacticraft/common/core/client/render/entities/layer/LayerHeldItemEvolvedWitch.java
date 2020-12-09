package team.galacticraft.galacticraft.common.core.client.render.entities.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import team.galacticraft.galacticraft.core.client.model.ModelEvolvedWitch;
import team.galacticraft.galacticraft.core.entities.EntityEvolvedWitch;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class LayerHeldItemEvolvedWitch extends CrossedArmsItemLayer<EntityEvolvedWitch, ModelEvolvedWitch>
{
    public LayerHeldItemEvolvedWitch(RenderLayerParent<EntityEvolvedWitch, ModelEvolvedWitch> p_i50916_1_) {
        super(p_i50916_1_);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityEvolvedWitch entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entitylivingbaseIn.getMainHandItem();
        matrixStackIn.pushPose();
        if (itemstack.getItem() == Items.POTION) {
            this.getParentModel().getHead().translateAndRotate(matrixStackIn);
            this.getParentModel().getNose().translateAndRotate(matrixStackIn);
            matrixStackIn.translate(0.0625D, 0.25D, 0.0D);
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(140.0F));
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(10.0F));
            matrixStackIn.translate(0.0D, (double)-0.4F, (double)0.4F);
        }

        super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        matrixStackIn.popPose();
    }
}
