package team.galacticraft.galacticraft.common.core.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.entities.EntitySkeletonBoss;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class ModelEvolvedSkeletonBoss extends EntityModel<EntitySkeletonBoss>
{
    private final ModelPart upperHead;
    private final ModelPart pelvis;
    private final ModelPart sternum;
    private final ModelPart rightLeg;
    private final ModelPart rightArm;
    private final ModelPart spine;
    private final ModelPart leftArm;
    private final ModelPart leftLeg;
    private final ModelPart leftFrontBotRib;
    private final ModelPart leftFrontTopRib;
    private final ModelPart leftFront2ndRib;
    private final ModelPart leftFront3rdRib;
    private final ModelPart leftSideBotRib;
    private final ModelPart leftSide3rdRib;
    private final ModelPart leftSide2ndRib;
    private final ModelPart leftSideTopRib;
    private final ModelPart rightSideTopRib;
    private final ModelPart rightSide2ndRib;
    private final ModelPart rightSide3rdRib;
    private final ModelPart rightSideBotRib;
    private final ModelPart rightFrontBotRib;
    private final ModelPart rightFront3rdRib;
    private final ModelPart rightFront2ndRib;
    private final ModelPart rightFrontTopRib;
    private final ModelPart leftBackTopRib;
    private final ModelPart leftBack2ndRib;
    private final ModelPart leftBack3rdRib;
    private final ModelPart leftBackBotRib;
    private final ModelPart rightBackBotRib;
    private final ModelPart rightBack3rdRib;
    private final ModelPart rightBack2ndRib;
    private final ModelPart rightBackTopRib;

    public ModelEvolvedSkeletonBoss()
    {
        this.texWidth = 128;
        this.texHeight = 128;

        float halfPI = Constants.HALF_PI;

        this.upperHead = new ModelPart(this, 0, 16);
        this.upperHead.addBox(-4F, -8F, -6F, 8, 8, 8);
        this.upperHead.setPos(0F, -24F, 6F);
        this.upperHead.setTexSize(64, 32);
        this.upperHead.mirror = true;
        this.setRotation(this.upperHead, 0.122173F, 0F, 0F);
        this.pelvis = new ModelPart(this, 32, 19);
        this.pelvis.addBox(-6F, 0F, -3F, 12, 5, 5);
        this.pelvis.setPos(0F, -2F, 5F);
        this.pelvis.setTexSize(64, 32);
        this.pelvis.mirror = true;
        this.setRotation(this.pelvis, 0F, 0F, 0F);
        this.sternum = new ModelPart(this, 0, 0);
        this.sternum.addBox(-1.5F, 0F, -1F, 3, 9, 1);
        this.sternum.setPos(0F, -21F, 2F);
        this.sternum.setTexSize(64, 32);
        this.sternum.mirror = true;
        this.setRotation(this.sternum, 0F, 0F, 0F);
        this.rightLeg = new ModelPart(this, 56, 33);
        this.rightLeg.mirror = true;
        this.rightLeg.addBox(-2F, 0F, -2F, 3, 26, 3);
        this.rightLeg.setPos(-5F, 0F, 5F);
        this.rightLeg.setTexSize(64, 32);
        this.rightLeg.mirror = true;
        this.setRotation(this.rightLeg, 0F, 0F, 0F);
        this.rightLeg.mirror = false;
        this.rightArm = new ModelPart(this, 56, 33);
        this.rightArm.addBox(-2F, -2F, -1.5F, 3, 24, 3);
        this.rightArm.setPos(-8F, -20F, 5F);
        this.rightArm.setTexSize(64, 32);
        this.rightArm.mirror = true;
        this.setRotation(this.rightArm, 0F, 0F, 0F);
        this.spine = new ModelPart(this, 32, 33);
        this.spine.addBox(-1.5F, 0F, -1F, 3, 22, 2);
        this.spine.setPos(0F, -24F, 6F);
        this.spine.setTexSize(64, 32);
        this.spine.mirror = true;
        this.setRotation(this.spine, 0F, 0F, 0F);
        this.leftArm = new ModelPart(this, 56, 33);
        this.leftArm.addBox(-1F, -2F, -1.5F, 3, 24, 3);
        this.leftArm.setPos(8F, -20F, 5F);
        this.leftArm.setTexSize(64, 32);
        this.leftArm.mirror = true;
        this.setRotation(this.leftArm, 0F, 0F, 0F);
        this.leftLeg = new ModelPart(this, 56, 33);
        this.leftLeg.addBox(-2F, 0F, -2F, 3, 26, 3);
        this.leftLeg.setPos(6F, 0F, 5F);
        this.leftLeg.setTexSize(64, 32);
        this.leftLeg.mirror = true;
        this.setRotation(this.leftLeg, 0F, 0F, 0F);
        this.leftFrontBotRib = new ModelPart(this, 0, 0);
        this.leftFrontBotRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftFrontBotRib.setPos(7F, -13F, 2F);
        this.leftFrontBotRib.setTexSize(64, 32);
        this.leftFrontBotRib.mirror = true;
        this.setRotation(this.leftFrontBotRib, 0F, -halfPI, 0F);
        this.leftFrontTopRib = new ModelPart(this, 0, 0);
        this.leftFrontTopRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftFrontTopRib.setPos(7F, -22F, 2F);
        this.leftFrontTopRib.setTexSize(64, 32);
        this.leftFrontTopRib.mirror = true;
        this.setRotation(this.leftFrontTopRib, 0F, -halfPI, 0F);
        this.leftFront2ndRib = new ModelPart(this, 0, 0);
        this.leftFront2ndRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftFront2ndRib.setPos(7F, -19F, 2F);
        this.leftFront2ndRib.setTexSize(64, 32);
        this.leftFront2ndRib.mirror = true;
        this.setRotation(this.leftFront2ndRib, 0F, -halfPI, 0F);
        this.leftFront3rdRib = new ModelPart(this, 0, 0);
        this.leftFront3rdRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftFront3rdRib.setPos(7F, -16F, 2F);
        this.leftFront3rdRib.setTexSize(64, 32);
        this.leftFront3rdRib.mirror = true;
        this.setRotation(this.leftFront3rdRib, 0F, -halfPI, 0F);
        this.leftSideBotRib = new ModelPart(this, 0, 0);
        this.leftSideBotRib.addBox(-1F, 0F, -6F, 1, 2, 6);
        this.leftSideBotRib.setPos(7F, -13F, 7F);
        this.leftSideBotRib.setTexSize(64, 32);
        this.leftSideBotRib.mirror = true;
        this.setRotation(this.leftSideBotRib, 0F, 0F, 0F);
        this.leftSide3rdRib = new ModelPart(this, 0, 0);
        this.leftSide3rdRib.addBox(-1F, 0F, -6F, 1, 2, 6);
        this.leftSide3rdRib.setPos(7F, -16F, 7F);
        this.leftSide3rdRib.setTexSize(64, 32);
        this.leftSide3rdRib.mirror = true;
        this.setRotation(this.leftSide3rdRib, 0F, 0F, 0F);
        this.leftSide2ndRib = new ModelPart(this, 0, 0);
        this.leftSide2ndRib.addBox(-1F, 0F, -6F, 1, 2, 6);
        this.leftSide2ndRib.setPos(7F, -19F, 7F);
        this.leftSide2ndRib.setTexSize(64, 32);
        this.leftSide2ndRib.mirror = true;
        this.setRotation(this.leftSide2ndRib, 0F, 0F, 0F);
        this.leftSideTopRib = new ModelPart(this, 0, 0);
        this.leftSideTopRib.addBox(-1F, 0F, -6F, 1, 2, 6);
        this.leftSideTopRib.setPos(7F, -22F, 7F);
        this.leftSideTopRib.setTexSize(64, 32);
        this.leftSideTopRib.mirror = true;
        this.setRotation(this.leftSideTopRib, 0F, 0F, 0F);
        this.rightSideTopRib = new ModelPart(this, 0, 0);
        this.rightSideTopRib.addBox(0F, 0F, -6F, 1, 2, 6);
        this.rightSideTopRib.setPos(-7F, -22F, 7F);
        this.rightSideTopRib.setTexSize(64, 32);
        this.rightSideTopRib.mirror = true;
        this.setRotation(this.rightSideTopRib, 0F, 0F, 0F);
        this.rightSide2ndRib = new ModelPart(this, 0, 0);
        this.rightSide2ndRib.addBox(0F, 0F, -6F, 1, 2, 6);
        this.rightSide2ndRib.setPos(-7F, -19F, 7F);
        this.rightSide2ndRib.setTexSize(64, 32);
        this.rightSide2ndRib.mirror = true;
        this.setRotation(this.rightSide2ndRib, 0F, 0F, 0F);
        this.rightSide3rdRib = new ModelPart(this, 0, 0);
        this.rightSide3rdRib.addBox(0F, 0F, -6F, 1, 2, 6);
        this.rightSide3rdRib.setPos(-7F, -16F, 7F);
        this.rightSide3rdRib.setTexSize(64, 32);
        this.rightSide3rdRib.mirror = true;
        this.setRotation(this.rightSide3rdRib, 0F, 0F, 0F);
        this.rightSideBotRib = new ModelPart(this, 0, 0);
        this.rightSideBotRib.addBox(0F, 0F, -6F, 1, 2, 6);
        this.rightSideBotRib.setPos(-7F, -13F, 7F);
        this.rightSideBotRib.setTexSize(64, 32);
        this.rightSideBotRib.mirror = true;
        this.setRotation(this.rightSideBotRib, 0F, 0F, 0F);
        this.rightFrontBotRib = new ModelPart(this, 0, 0);
        this.rightFrontBotRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightFrontBotRib.setPos(-7F, -13F, 2F);
        this.rightFrontBotRib.setTexSize(64, 32);
        this.rightFrontBotRib.mirror = true;
        this.setRotation(this.rightFrontBotRib, 0F, halfPI, 0F);
        this.rightFront3rdRib = new ModelPart(this, 0, 0);
        this.rightFront3rdRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightFront3rdRib.setPos(-7F, -16F, 2F);
        this.rightFront3rdRib.setTexSize(64, 32);
        this.rightFront3rdRib.mirror = true;
        this.setRotation(this.rightFront3rdRib, 0F, halfPI, 0F);
        this.rightFront2ndRib = new ModelPart(this, 0, 0);
        this.rightFront2ndRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightFront2ndRib.setPos(-7F, -19F, 2F);
        this.rightFront2ndRib.setTexSize(64, 32);
        this.rightFront2ndRib.mirror = true;
        this.setRotation(this.rightFront2ndRib, 0F, halfPI, 0F);
        this.rightFrontTopRib = new ModelPart(this, 0, 0);
        this.rightFrontTopRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightFrontTopRib.setPos(-7F, -22F, 2F);
        this.rightFrontTopRib.setTexSize(64, 32);
        this.rightFrontTopRib.mirror = true;
        this.setRotation(this.rightFrontTopRib, 0F, halfPI, 0F);
        this.leftBackTopRib = new ModelPart(this, 0, 0);
        this.leftBackTopRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftBackTopRib.setPos(7F, -22F, 7F);
        this.leftBackTopRib.setTexSize(64, 32);
        this.leftBackTopRib.mirror = true;
        this.setRotation(this.leftBackTopRib, 0F, -halfPI, 0F);
        this.leftBack2ndRib = new ModelPart(this, 0, 0);
        this.leftBack2ndRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftBack2ndRib.setPos(7F, -19F, 7F);
        this.leftBack2ndRib.setTexSize(64, 32);
        this.leftBack2ndRib.mirror = true;
        this.setRotation(this.leftBack2ndRib, 0F, -halfPI, 0F);
        this.leftBack3rdRib = new ModelPart(this, 0, 0);
        this.leftBack3rdRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftBack3rdRib.setPos(7F, -16F, 7F);
        this.leftBack3rdRib.setTexSize(64, 32);
        this.leftBack3rdRib.mirror = true;
        this.setRotation(this.leftBack3rdRib, 0F, -halfPI, 0F);
        this.leftBackBotRib = new ModelPart(this, 0, 0);
        this.leftBackBotRib.addBox(-1F, 0F, 0F, 1, 2, 6);
        this.leftBackBotRib.setPos(7F, -13F, 7F);
        this.leftBackBotRib.setTexSize(64, 32);
        this.leftBackBotRib.mirror = true;
        this.setRotation(this.leftBackBotRib, 0F, -halfPI, 0F);
        this.rightBackBotRib = new ModelPart(this, 0, 0);
        this.rightBackBotRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightBackBotRib.setPos(-7F, -13F, 7F);
        this.rightBackBotRib.setTexSize(64, 32);
        this.rightBackBotRib.mirror = true;
        this.setRotation(this.rightBackBotRib, 0F, halfPI, 0F);
        this.rightBack3rdRib = new ModelPart(this, 0, 0);
        this.rightBack3rdRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightBack3rdRib.setPos(-7F, -16F, 7F);
        this.rightBack3rdRib.setTexSize(64, 32);
        this.rightBack3rdRib.mirror = true;
        this.setRotation(this.rightBack3rdRib, 0F, halfPI, 0F);
        this.rightBack2ndRib = new ModelPart(this, 0, 0);
        this.rightBack2ndRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightBack2ndRib.setPos(-7F, -19F, 7F);
        this.rightBack2ndRib.setTexSize(64, 32);
        this.rightBack2ndRib.mirror = true;
        this.setRotation(this.rightBack2ndRib, 0F, halfPI, 0F);
        this.rightBackTopRib = new ModelPart(this, 0, 0);
        this.rightBackTopRib.addBox(0F, 0F, 0F, 1, 2, 6);
        this.rightBackTopRib.setPos(-7F, -22F, 7F);
        this.rightBackTopRib.setTexSize(64, 32);
        this.rightBackTopRib.mirror = true;
        this.setRotation(this.rightBackTopRib, 0F, halfPI, 0F);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        this.upperHead.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.pelvis.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sternum.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightLeg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightArm.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.spine.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftArm.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftLeg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftFrontBotRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftFrontTopRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftFront2ndRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftFront3rdRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftSideBotRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftSide3rdRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftSide2ndRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftSideTopRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightSideTopRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightSide2ndRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightSide3rdRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightSideBotRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightFrontBotRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightFront3rdRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightFront2ndRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightFrontTopRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftBackTopRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftBack2ndRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftBack3rdRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leftBackBotRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightBackBotRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightBack3rdRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightBack2ndRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rightBackTopRib.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    private void setRotation(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void setRotationAngles(EntitySkeletonBoss entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        final float floatPI = 3.1415927F;
        this.upperHead.yRot = netHeadYaw / Constants.RADIANS_TO_DEGREES;
        this.upperHead.xRot = headPitch / Constants.RADIANS_TO_DEGREES;
        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + floatPI) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + floatPI) * 1.4F * limbSwingAmount;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;

//        if (this.isRiding)
//        {
//            this.rightArm.rotateAngleX += -(floatPI / 5F);
//            this.leftArm.rotateAngleX += -(floatPI / 5F);
//            this.rightLeg.rotateAngleX = -(floatPI * 2F / 5F);
//            this.leftLeg.rotateAngleX = -(floatPI * 2F / 5F);
//            this.rightLeg.rotateAngleY = floatPI / 10F;
//            this.leftLeg.rotateAngleY = -(floatPI / 10F);
//        }

        this.rightArm.yRot = netHeadYaw / Constants.RADIANS_TO_DEGREES;
        this.leftArm.yRot = netHeadYaw / Constants.RADIANS_TO_DEGREES;
        float var7;
        float var8;

        if (this.attackTime > -9990.0F)
        {
            var7 = this.attackTime;
            this.spine.yRot = Mth.sin(Mth.sqrt(var7) * Constants.TWO_PI) * 0.2F;

            this.rightArm.z = Mth.sin(this.spine.yRot) * 5.0F;
            this.rightArm.x = -Mth.cos(this.spine.yRot) * 5.0F;
            this.leftArm.z = -Mth.sin(this.spine.yRot) * 5.0F;
            this.leftArm.x = Mth.cos(this.spine.yRot) * 5.0F;
            this.rightArm.yRot += this.spine.yRot;
            this.leftArm.yRot += this.spine.yRot;
            this.leftArm.xRot += this.spine.yRot;
            var7 = 1.0F - this.attackTime;
            var7 *= var7;
            var7 *= var7;
            var7 = 1.0F - var7;
            var8 = Mth.sin(var7 * floatPI);
            final float var9 = Mth.sin(this.attackTime * floatPI) * -(this.upperHead.xRot - 0.7F) * 0.75F;
            this.rightArm.xRot = (float) (this.rightArm.xRot - (var8 * 1.2D + var9));
            this.rightArm.yRot += this.spine.yRot * 2.0F;
            this.rightArm.zRot = Mth.sin(this.attackTime * floatPI) * -0.4F;
        }

        final float f6 = Mth.sin(this.attackTime * floatPI);
        final float f7 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * floatPI);
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.rightArm.yRot = 0.5F * (netHeadYaw / Constants.RADIANS_TO_DEGREES) + -(0.1F - f6 * 0.6F);
        this.leftArm.yRot = 0.5F * (netHeadYaw / Constants.RADIANS_TO_DEGREES) + 0.1F - f6 * 0.6F;
        this.rightArm.xRot = -(floatPI / 2F);
        this.leftArm.xRot = -(floatPI / 2F);
        this.rightArm.xRot -= f6 * 1.2F - f7 * 0.4F;
        this.leftArm.xRot -= f6 * 1.2F - f7 * 0.4F;
        this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
        this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;

        if (entityIn.deathTicks > 0)
        {
            this.leftArm.xRot = -(floatPI / 2F) + (float) (Math.pow(entityIn.deathTicks, 2) / 5.0F) / 3.0F / Constants.RADIANS_TO_DEGREES;
            this.rightArm.xRot = -(floatPI / 2F) + (float) (Math.pow(entityIn.deathTicks, 2) / 5.0F) / 2.0F / Constants.RADIANS_TO_DEGREES;
        }

        if (entityIn.throwTimer + entityIn.postThrowDelay > 0)
        {
            this.rightArm.xRot -= Mth.cos((entityIn.throwTimer + entityIn.postThrowDelay) * 0.05F) * 1.2F + 0.05F;
            this.leftArm.xRot -= Mth.cos((entityIn.throwTimer + entityIn.postThrowDelay) * 0.05F) * 1.2F + 0.05F;
        }
    }

//    @Override
//    public void setRotationAngles(EntitySkeletonBoss entity, float par1, float par2, float par3, float par4, float par5, float par6)
//    {
//        final float floatPI = 3.1415927F;
//
//        final EntitySkeletonBoss boss = entity;
//        super.setRotationAngles(entity, par1, par2, par3, par4, par5, par6);
//        this.upperHead.rotateAngleY = par4 / Constants.RADIANS_TO_DEGREES;
//        this.upperHead.rotateAngleX = par5 / Constants.RADIANS_TO_DEGREES;
//        this.rightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + floatPI) * 2.0F * par2 * 0.5F;
//        this.leftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
//        this.rightArm.rotateAngleZ = 0.0F;
//        this.leftArm.rotateAngleZ = 0.0F;
//        this.rightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
//        this.leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + floatPI) * 1.4F * par2;
//        this.rightLeg.rotateAngleY = 0.0F;
//        this.leftLeg.rotateAngleY = 0.0F;
//    }

//    public void postRenderArm(float scale, ItemCameraTransforms.TransformType type)
//    {
//        if (type == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
//        {
//            this.rightArm.postRender(scale);
//        }
//        else
//        {
//            this.leftArm.postRender(scale);
//        }
//    }
}
