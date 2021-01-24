package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
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

@OnlyIn(Dist.CLIENT)
public class LayerHeldItemEvolvedSkeletonBoss<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M>
{
    public LayerHeldItemEvolvedSkeletonBoss(RenderLayerParent<T, M> renderer)
    {
        super(renderer);
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        boolean flag = entity.getMainArm() == HandSide.RIGHT;
        ItemStack itemstack = flag ? entity.getOffhandItem() : entity.getMainHandItem();
        ItemStack itemstack1 = flag ? entity.getMainHandItem() : entity.getOffhandItem();

        if (!itemstack.isEmpty() || !itemstack1.isEmpty())
        {
            matrixStack.pushPose();
            this.renderItemInHand(entity, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStack, buffer, packedLight);
            this.renderItemInHand(entity, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrixStack, buffer, packedLight);
            matrixStack.popPose();
        }
    }

    private void renderItemInHand(LivingEntity entity, ItemStack itemStack, ItemTransforms.TransformType type, HumanoidArm handSide, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight)
    {
        if (!itemStack.isEmpty())
        {
            matrixStack.pushPose();
            this.getParentModel().translateToHand(handSide, matrixStack);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            boolean flag = handSide == HandSide.LEFT;
            matrixStack.translate((flag ? -1 : 1) / 16.0F, 0.125D, -0.625D);
            Minecraft.getInstance().getItemInHandRenderer().renderItem(entity, itemStack, type, flag, matrixStack, buffer, combinedLight);
            matrixStack.popPose();
        }
    }
}