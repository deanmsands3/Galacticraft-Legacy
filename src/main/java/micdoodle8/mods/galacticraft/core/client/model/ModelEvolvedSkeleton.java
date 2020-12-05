package micdoodle8.mods.galacticraft.core.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;

public class ModelEvolvedSkeleton extends SkeletonModel<EntityEvolvedSkeleton>
{
    ModelPart leftOxygenTank;
    ModelPart rightOxygenTank;
    ModelPart tubeRight2;
    ModelPart tubeLeft1;
    ModelPart tubeRight3;
    ModelPart tubeRight4;
    ModelPart tubeRight5;
    ModelPart tubeLeft6;
    ModelPart tubeRight7;
    ModelPart tubeRight1;
    ModelPart tubeLeft2;
    ModelPart tubeLeft3;
    ModelPart tubeLeft4;
    ModelPart tubeLeft5;
    ModelPart tubeLeft7;
    ModelPart tubeRight6;
    ModelPart oxygenMask;

    public ModelEvolvedSkeleton()
    {
        this(0.0F);
    }

    public ModelEvolvedSkeleton(float par1)
    {
        this.texWidth = 128;
        this.texHeight = 64;
        final int par2 = 0;
        this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
        this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
        this.leftOxygenTank = new ModelPart(this, 48, 20);
        this.leftOxygenTank.addBox(-1.5F, 0F, -1.5F, 3, 7, 3, par1);
        this.leftOxygenTank.setPos(2F, 2F, 3.8F);
        this.leftOxygenTank.setTexSize(128, 64);
        this.leftOxygenTank.mirror = true;
        this.setRotation(this.leftOxygenTank, 0F, 0F, 0F);
        this.rightOxygenTank = new ModelPart(this, 48, 20);
        this.rightOxygenTank.addBox(-1.5F, 0F, -1.5F, 3, 7, 3, par1);
        this.rightOxygenTank.setPos(-2F, 2F, 3.8F);
        this.rightOxygenTank.setTexSize(128, 64);
        this.rightOxygenTank.mirror = true;
        this.setRotation(this.rightOxygenTank, 0F, 0F, 0F);
        this.tubeRight2 = new ModelPart(this, 48, 30);
        this.tubeRight2.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight2.setPos(-2F, 2F, 6.8F);
        this.tubeRight2.setTexSize(128, 64);
        this.tubeRight2.mirror = true;
        this.setRotation(this.tubeRight2, 0F, 0F, 0F);
        this.tubeLeft1 = new ModelPart(this, 48, 30);
        this.tubeLeft1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft1.setPos(2F, 3F, 5.8F);
        this.tubeLeft1.setTexSize(128, 64);
        this.tubeLeft1.mirror = true;
        this.setRotation(this.tubeLeft1, 0F, 0F, 0F);
        this.tubeRight3 = new ModelPart(this, 48, 30);
        this.tubeRight3.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight3.setPos(-2F, 1F, 6.8F);
        this.tubeRight3.setTexSize(128, 64);
        this.tubeRight3.mirror = true;
        this.setRotation(this.tubeRight3, 0F, 0F, 0F);
        this.tubeRight4 = new ModelPart(this, 48, 30);
        this.tubeRight4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight4.setPos(-2F, 0F, 6.8F);
        this.tubeRight4.setTexSize(128, 64);
        this.tubeRight4.mirror = true;
        this.setRotation(this.tubeRight4, 0F, 0F, 0F);
        this.tubeRight5 = new ModelPart(this, 48, 30);
        this.tubeRight5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight5.setPos(-2F, -1F, 6.8F);
        this.tubeRight5.setTexSize(128, 64);
        this.tubeRight5.mirror = true;
        this.setRotation(this.tubeRight5, 0F, 0F, 0F);
        this.tubeLeft6 = new ModelPart(this, 48, 30);
        this.tubeLeft6.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft6.setPos(2F, -2F, 5.8F);
        this.tubeLeft6.setTexSize(128, 64);
        this.tubeLeft6.mirror = true;
        this.setRotation(this.tubeLeft6, 0F, 0F, 0F);
        this.tubeRight7 = new ModelPart(this, 48, 30);
        this.tubeRight7.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight7.setPos(-2F, -3F, 4.8F);
        this.tubeRight7.setTexSize(128, 64);
        this.tubeRight7.mirror = true;
        this.setRotation(this.tubeRight7, 0F, 0F, 0F);
        this.tubeRight1 = new ModelPart(this, 48, 30);
        this.tubeRight1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight1.setPos(-2F, 3F, 5.8F);
        this.tubeRight1.setTexSize(128, 64);
        this.tubeRight1.mirror = true;
        this.setRotation(this.tubeRight1, 0F, 0F, 0F);
        this.tubeLeft2 = new ModelPart(this, 48, 30);
        this.tubeLeft2.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft2.setPos(2F, 2F, 6.8F);
        this.tubeLeft2.setTexSize(128, 64);
        this.tubeLeft2.mirror = true;
        this.setRotation(this.tubeLeft2, 0F, 0F, 0F);
        this.tubeLeft3 = new ModelPart(this, 48, 30);
        this.tubeLeft3.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft3.setPos(2F, 1F, 6.8F);
        this.tubeLeft3.setTexSize(128, 64);
        this.tubeLeft3.mirror = true;
        this.setRotation(this.tubeLeft3, 0F, 0F, 0F);
        this.tubeLeft4 = new ModelPart(this, 48, 30);
        this.tubeLeft4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft4.setPos(2F, 0F, 6.8F);
        this.tubeLeft4.setTexSize(128, 64);
        this.tubeLeft4.mirror = true;
        this.setRotation(this.tubeLeft4, 0F, 0F, 0F);
        this.tubeLeft5 = new ModelPart(this, 48, 30);
        this.tubeLeft5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft5.setPos(2F, -1F, 6.8F);
        this.tubeLeft5.setTexSize(128, 64);
        this.tubeLeft5.mirror = true;
        this.setRotation(this.tubeLeft5, 0F, 0F, 0F);
        this.tubeLeft7 = new ModelPart(this, 48, 30);
        this.tubeLeft7.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft7.setPos(2F, -3F, 4.8F);
        this.tubeLeft7.setTexSize(128, 64);
        this.tubeLeft7.mirror = true;
        this.setRotation(this.tubeLeft7, 0F, 0F, 0F);
        this.tubeRight6 = new ModelPart(this, 48, 30);
        this.tubeRight6.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight6.setPos(-2F, -2F, 5.8F);
        this.tubeRight6.setTexSize(128, 64);
        this.tubeRight6.mirror = true;
        this.setRotation(this.tubeRight6, 0F, 0F, 0F);
        this.oxygenMask = new ModelPart(this, 48, 0);
        this.oxygenMask.addBox(-5F, -9F, -5F, 10, 10, 10, par1);
        this.oxygenMask.setPos(0F, 0F, 0F);
        this.oxygenMask.setTexSize(128, 64);
        this.oxygenMask.mirror = true;
        this.setRotation(this.oxygenMask, 0F, 0F, 0F);
//        this.bipedCloak = new ModelRenderer(this, 0, 0);
//        this.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, par1);
//        this.bipedEars = new ModelRenderer(this, 24, 0);
//        this.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, par1);
        this.head = new ModelPart(this, 0, 0);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1);
        this.head.setPos(0.0F, 0.0F + par2, 0.0F);
        this.hat = new ModelPart(this, 32, 0);
        this.hat.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1 + 0.5F);
        this.hat.setPos(0.0F, 0.0F + par2, 0.0F);
        this.body = new ModelPart(this, 16, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, par1);
        this.body.setPos(0.0F, 0.0F + par2, 0.0F);
        this.rightArm = new ModelPart(this, 40, 16);
        this.rightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, par1);
        this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.leftArm = new ModelPart(this, 40, 16);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, par1);
        this.leftArm.setPos(5.0F, 2.0F, 0.0F);
        this.rightLeg = new ModelPart(this, 0, 16);
        this.rightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, par1);
        this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
        this.leftLeg = new ModelPart(this, 0, 16);
        this.leftLeg.mirror = true;
        this.leftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, par1);
        this.leftLeg.setPos(2.0F, 12.0F, 0.0F);
    }

    @Override
    public void setRotationAngles(EntityEvolvedSkeleton entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ClientUtil.copyModelAngles(this.head, this.oxygenMask);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftOxygenTank.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightOxygenTank.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeRight2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeLeft1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeRight3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeRight4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeRight5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeLeft6.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeRight7.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeRight1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeLeft2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeLeft3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeLeft4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeLeft5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeLeft7.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tubeRight6.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.oxygenMask.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.head.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightArm.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftArm.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightLeg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftLeg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.hat.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    private void setRotation(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    //    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity)
//    {
//        this.bipedHead.rotateAngleY = par4 / Constants.RADIANS_TO_DEGREES;
//        this.bipedHead.rotateAngleX = par5 / Constants.RADIANS_TO_DEGREES;
//        this.oxygenMask.rotateAngleY = par4 / Constants.RADIANS_TO_DEGREES;
//        this.oxygenMask.rotateAngleX = par5 / Constants.RADIANS_TO_DEGREES;
//        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
//        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
//        this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 2.0F * par2 * 0.5F;
//        this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
//        this.bipedRightArm.rotateAngleZ = 0.0F;
//        this.bipedLeftArm.rotateAngleZ = 0.0F;
//        this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
//        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
//        this.bipedRightLeg.rotateAngleY = 0.0F;
//        this.bipedLeftLeg.rotateAngleY = 0.0F;
//
//        if (this.isRiding)
//        {
//            this.bipedRightArm.rotateAngleX += -((float) Math.PI / 5F);
//            this.bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
//            this.bipedRightLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
//            this.bipedLeftLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
//            this.bipedRightLeg.rotateAngleY = (float) Math.PI / 10F;
//            this.bipedLeftLeg.rotateAngleY = -((float) Math.PI / 10F);
//        }
//
//        this.bipedRightArm.rotateAngleY = 0.0F;
//        this.bipedLeftArm.rotateAngleY = 0.0F;
//        float var7;
//        float var8;
//
//        if (this.onGround > -9990.0F)
//        {
//            var7 = this.onGround;
//            this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(var7) * (float) Math.PI * 2.0F) * 0.2F;
//            this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
//            this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
//            this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
//            this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
//            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
//            this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
//            this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
//            var7 = 1.0F - this.onGround;
//            var7 *= var7;
//            var7 *= var7;
//            var7 = 1.0F - var7;
//            var8 = MathHelper.sin(var7 * (float) Math.PI);
//            final float var9 = MathHelper.sin(this.onGround * (float) Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
//            this.bipedRightArm.rotateAngleX = (float) (this.bipedRightArm.rotateAngleX - (var8 * 1.2D + var9));
//            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
//            this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float) Math.PI) * -0.4F;
//        }
//
//        this.bipedBody.rotateAngleX = 0.0F;
//        this.bipedRightLeg.rotationPointZ = 0.0F;
//        this.bipedLeftLeg.rotationPointZ = 0.0F;
//        this.bipedRightLeg.rotationPointY = 12.0F;
//        this.bipedLeftLeg.rotationPointY = 12.0F;
//        this.bipedHead.rotationPointY = 0.0F;
//
//        this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
//        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
//        this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
//        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
//
//        MathHelper.sin(this.onGround * (float) Math.PI);
//        MathHelper.sin((1.0F - (1.0F - this.onGround) * (1.0F - this.onGround)) * (float) Math.PI);
//
//        this.aimedBow = true;
//
//        if (this.aimedBow)
//        {
//            var7 = 0.0F;
//            var8 = 0.0F;
//            this.bipedRightArm.rotateAngleZ = 0.0F;
//            this.bipedLeftArm.rotateAngleZ = 0.0F;
//            this.bipedRightArm.rotateAngleY = -(0.1F - var7 * 0.6F) + this.bipedHead.rotateAngleY;
//            this.bipedLeftArm.rotateAngleY = 0.1F - var7 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
//            this.bipedRightArm.rotateAngleX = -(Constants.halfPI) + this.bipedHead.rotateAngleX;
//            this.bipedLeftArm.rotateAngleX = -(Constants.halfPI) + this.bipedHead.rotateAngleX;
//            this.bipedRightArm.rotateAngleX -= var7 * 1.2F - var8 * 0.4F;
//            this.bipedLeftArm.rotateAngleX -= var7 * 1.2F - var8 * 0.4F;
//            this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
//            this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
//            this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
//            this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
//        }
//
//        final float f6 = MathHelper.sin(this.onGround * (float) Math.PI);
//        final float f7 = MathHelper.sin((1.0F - (1.0F - this.onGround) * (1.0F - this.onGround)) * (float) Math.PI);
//        this.bipedRightArm.rotateAngleZ = 0.0F;
//        this.bipedLeftArm.rotateAngleZ = 0.0F;
//        this.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
//        this.bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
//        this.bipedRightArm.rotateAngleX = -(Constants.halfPI);
//        this.bipedLeftArm.rotateAngleX = -(Constants.halfPI);
//        this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
//        this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
//        this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
//        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
//        this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
//        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
//    }
}
