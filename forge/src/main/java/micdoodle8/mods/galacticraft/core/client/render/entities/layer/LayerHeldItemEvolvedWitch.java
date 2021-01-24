package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.client.model.EvolvedWitchModel;
import micdoodle8.mods.galacticraft.core.entities.EvolvedWitchEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerHeldItemEvolvedWitch extends CrossedArmsItemLayer<EvolvedWitchEntity, EvolvedWitchModel>
{
    public LayerHeldItemEvolvedWitch(RenderLayerParent<EvolvedWitchEntity, EvolvedWitchModel> p_i50916_1_) {
        super(p_i50916_1_);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EvolvedWitchEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entitylivingbaseIn.getMainHandItem();
        matrixStackIn.pushPose();
        if (itemstack.getItem() == Items.POTION) {
            this.getParentModel().getHead().translateAndRotate(matrixStackIn);
            this.getParentModel().func_205073_b().translateRotate(matrixStackIn);
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
