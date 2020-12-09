package team.galacticraft.galacticraft.common.core.client.render.entities.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class LayerHeldItemEvolvedSkeletonBoss<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M>
{
    public LayerHeldItemEvolvedSkeletonBoss(RenderLayerParent<T, M> p_i50934_1_) {
        super(p_i50934_1_);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean flag = entitylivingbaseIn.getMainArm() == HumanoidArm.RIGHT;
        ItemStack itemstack = flag ? entitylivingbaseIn.getOffhandItem() : entitylivingbaseIn.getMainHandItem();
        ItemStack itemstack1 = flag ? entitylivingbaseIn.getMainHandItem() : entitylivingbaseIn.getOffhandItem();
        if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
            matrixStackIn.pushPose();
            if (this.getParentModel().young) {
                float f = 0.5F;
                matrixStackIn.translate(0.0D, 0.75D, 0.0D);
                matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            }

            this.renderItemInHand(entitylivingbaseIn, itemstack1, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, matrixStackIn, bufferIn, packedLightIn);
            this.renderItemInHand(entitylivingbaseIn, itemstack, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
        }
    }

    private void renderItemInHand(LivingEntity entity, ItemStack stack, ItemTransforms.TransformType type, HumanoidArm handSide, PoseStack matStack, MultiBufferSource bufferIn, int combinedLightIn) {
        if (!stack.isEmpty()) {
            matStack.pushPose();
            this.getParentModel().translateToHand(handSide, matStack);
            matStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            matStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            boolean flag = handSide == HumanoidArm.LEFT;
            matStack.translate((double)((float)(flag ? -1 : 1) / 16.0F), 0.125D, -0.625D);
            Minecraft.getInstance().getItemInHandRenderer().renderItem(entity, stack, type, flag, matStack, bufferIn, combinedLightIn);
            matStack.popPose();
        }
    }
}
