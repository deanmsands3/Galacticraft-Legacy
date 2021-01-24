package micdoodle8.mods.galacticraft.core.client.render.item;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;

import static net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

public class ItemModelRocket extends ModelTransformWrapper
{
    public ItemModelRocket(BakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected boolean getTransformForPerspective(ItemTransforms.TransformType cameraTransformType, PoseStack mat)
    {
        if (cameraTransformType == TransformType.GUI)
        {
            float scale = 0.1525F;
            mat.pushPose();
            mat.translate(0.275F, -0.15F, 0.0F);
            mat.mulPose(new Quaternion(0.0F, -45.0F, 0.0F, true));
            mat.mulPose(new Quaternion(65.0F, 0.0F, 0.0F, true));
            mat.mulPose(new Quaternion(0.0F, ClientUtil.getClientTimeTotal() / 1000.0F, 0.0F, false));
            mat.scale(scale, scale, scale);
            mat.translate(0.5D, 0.5D, 0.5D);
            return true;
        }

        if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND || cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND)
        {
            mat.pushPose();
            mat.mulPose(new Quaternion(0.0F, 45.0F, 0.0F, true));
            mat.scale(0.5F, 0.5F, 0.5F);
            mat.mulPose(new Quaternion(Constants.halfPI, 0.0F, 0.0F, false));
            mat.mulPose(new Quaternion(0.0F, 0.0F, -0.65F, false));
            mat.translate(0.5F, -3.2F, -2.6F);
            return true;
        }

        if (cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND || cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND)
        {
            mat.pushPose();
            mat.mulPose(new Quaternion(75.0F, 0.0F, 0.0F, true));
            mat.scale(0.5F, 0.5F, 0.5F);
            mat.mulPose(new Quaternion(0.0F, 0.0F, -Constants.halfPI, false));
            mat.mulPose(new Quaternion(0.0F, Constants.halfPI, 0.0F, false));
            mat.mulPose(new Quaternion(0.2F, 0.0F, 0.0F, false));
            mat.mulPose(new Quaternion(0.0F, 0.0F, 0.5F, false));
            mat.mulPose(new Quaternion(0.0F, 0.0F, -0.65F, false));
            mat.translate(0.4F, -2.8F, 1.2F);
            return true;
        }

        if (cameraTransformType == TransformType.GROUND)
        {
            mat.pushPose();
            mat.scale(0.1F, 0.1F, 0.1F);
            mat.translate(0.5F, 0.0F, 0.5F);
            return true;
        }

        if (cameraTransformType == TransformType.FIXED)
        {
            mat.pushPose();
            mat.scale(0.135F, 0.135F, 0.135F);
            mat.translate(0.5F, -2.75F, 0.5F);
            return true;
        }

        return false;
    }
}
