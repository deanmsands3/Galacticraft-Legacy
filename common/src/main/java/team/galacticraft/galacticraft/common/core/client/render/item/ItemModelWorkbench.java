package team.galacticraft.galacticraft.common.core.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import team.galacticraft.galacticraft.common.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.resources.model.BakedModel;

public class ItemModelWorkbench extends ModelTransformWrapper
{
    public ItemModelWorkbench(BakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected boolean getTransformForPerspective(TransformType cameraTransformType, PoseStack mat)
    {
        if (cameraTransformType == TransformType.GUI)
        {
            mat.pushPose();
            mat.translate(0.0F, -0.2F, 0.0F);
            mat.mulPose(new Quaternion(30.0F, 45.0F, 0.0F, true));
            mat.scale(0.25F, 0.25F, 0.25F);
            return true;
        }

//        if (cameraTransformType == TransformType.GUI)
//        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.0F, -0.0F, 0.0F));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(1.0F);
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND || cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND)
//        {
//            Vector3f trans = new Vector3f(-0.025F, 0.4F, 0.3F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(38, 0, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(1.0F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(trans);
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND || cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND)
//        {
//            Vector3f trans = new Vector3f(-0.05F, 0.3F, 0.4F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(-30, 0, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(1.0F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(trans);
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == TransformType.GROUND)
//        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(-30, 0, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.85F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(-0.05F, 0.25F, 0.45F));
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == TransformType.FIXED)
//        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(30, 0, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.8F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(3.15F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.0F, 0.0F, 0.4F));
//            ret.mul(mul);
//            return ret;
//        }
//
//        return null;
        return false;
    }
}
