package micdoodle8.mods.galacticraft.planets.mars.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.mars.entities.CreeperBossEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class CreeperBossModel extends EntityModel<CreeperBossEntity>
{
    ModelPart headMain;
    ModelPart bodyMain;
    ModelPart rightLegFront;
    ModelPart leftLegFront;
    ModelPart rightLeg;
    ModelPart leftLeg;
    ModelPart oxygenTank;
    ModelPart headLeft;
    ModelPart headRight;
    ModelPart neckRight;
    ModelPart neckLeft;

    public CreeperBossModel()
    {
        this(0.0F);
    }

    public CreeperBossModel(float scale)
    {
        this.texWidth = 128;
        this.texHeight = 64;
        this.neckRight = new ModelPart(this, 16, 20);
        this.neckRight.mirror = true;
        this.neckRight.addBox(-2.5F, -9F, -1.5F, 5, 9, 3, scale);
        this.neckRight.setPos(-3F, 10F, 0F);
        this.neckRight.setTexSize(128, 64);
        this.neckRight.mirror = true;
        this.setRotation(this.neckRight, 0F, 0F, -1.169371F);
        this.neckRight.mirror = false;
        this.neckLeft = new ModelPart(this, 16, 20);
        this.neckLeft.addBox(-2.5F, -9F, -1.5F, 5, 9, 3, scale);
        this.neckLeft.setPos(3F, 10F, 0F);
        this.neckLeft.setTexSize(128, 64);
        this.neckLeft.mirror = true;
        this.setRotation(this.neckLeft, 0F, 0F, 1.169371F);
        this.headMain = new ModelPart(this, 0, 0);
        this.headMain.addBox(-4F, -8F, -4F, 8, 8, 8, scale);
        this.headMain.setPos(0F, 6F, 0F);
        this.headMain.setTexSize(128, 64);
        this.headMain.mirror = true;
        this.setRotation(this.headMain, 0F, 0F, 0F);
        this.bodyMain = new ModelPart(this, 16, 16);
        this.bodyMain.addBox(-4F, 0F, -2F, 8, 12, 4, scale);
        this.bodyMain.setPos(0F, 6F, 0F);
        this.bodyMain.setTexSize(128, 64);
        this.bodyMain.mirror = true;
        this.setRotation(this.bodyMain, 0F, 0F, 0F);
        this.rightLegFront = new ModelPart(this, 0, 16);
        this.rightLegFront.addBox(-2F, 0F, -2F, 4, 6, 4, scale);
        this.rightLegFront.setPos(-2F, 18F, -4F);
        this.rightLegFront.setTexSize(128, 64);
        this.rightLegFront.mirror = true;
        this.setRotation(this.rightLegFront, 0F, 0F, 0F);
        this.leftLegFront = new ModelPart(this, 0, 16);
        this.leftLegFront.addBox(-2F, 0F, -2F, 4, 6, 4, scale);
        this.leftLegFront.setPos(2F, 18F, -4F);
        this.leftLegFront.setTexSize(128, 64);
        this.leftLegFront.mirror = true;
        this.setRotation(this.leftLegFront, 0F, 0F, 0F);
        this.rightLeg = new ModelPart(this, 0, 16);
        this.rightLeg.addBox(0F, 0F, -2F, 4, 6, 4, scale);
        this.rightLeg.setPos(-4F, 18F, 4F);
        this.rightLeg.setTexSize(128, 64);
        this.rightLeg.mirror = true;
        this.setRotation(this.rightLeg, 0F, 0F, 0F);
        this.leftLeg = new ModelPart(this, 0, 16);
        this.leftLeg.addBox(-2F, 0F, -2F, 4, 6, 4, scale);
        this.leftLeg.setPos(2F, 18F, 4F);
        this.leftLeg.setTexSize(128, 64);
        this.leftLeg.mirror = true;
        this.setRotation(this.leftLeg, 0F, 0F, 0F);
        this.oxygenTank = new ModelPart(this, 40, 0);
        this.oxygenTank.addBox(-5F, -9F, -5F, 10, 10, 10, scale);
        this.oxygenTank.setPos(0F, 6F, 0F);
        this.oxygenTank.setTexSize(128, 64);
        this.oxygenTank.mirror = true;
        this.setRotation(this.oxygenTank, 0F, 0F, 0F);
        this.headLeft = new ModelPart(this, 0, 0);
        this.headLeft.addBox(1F, -9F, -4F, 8, 8, 8, scale);
        this.headLeft.setPos(3F, 6F, 0.1F);
        this.headLeft.setTexSize(128, 64);
        this.headLeft.mirror = true;
        this.setRotation(this.headLeft, 0F, 0F, 0.7853982F);
        this.headRight = new ModelPart(this, 0, 0);
        this.headRight.addBox(-9F, -9F, -4F, 8, 8, 8, scale);
        this.headRight.setPos(-3F, 6F, -0.1F);
        this.headRight.setTexSize(128, 64);
        this.headRight.mirror = true;
        this.setRotation(this.headRight, 0F, 0F, -0.7853982F);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        //        if (creeper.headsRemaining > 2)TODO
        {
            this.headLeft.render(matrixStack, buffer, packedLight, packedOverlay);
            this.neckLeft.render(matrixStack, buffer, packedLight, packedOverlay);
            this.headRight.render(matrixStack, buffer, packedLight, packedOverlay);
            this.neckRight.render(matrixStack, buffer, packedLight, packedOverlay);
            this.headMain.render(matrixStack, buffer, packedLight, packedOverlay);
            this.oxygenTank.render(matrixStack, buffer, packedLight, packedOverlay);
        }
        //        else if (creeper.headsRemaining > 1)
        //        {
        //            this.headRight.render(scale);
        //            this.neckRight.render(scale);
        //            this.headMain.render(scale);
        //            this.oxygenTank.render(scale);
        //        }
        //        else if (creeper.headsRemaining > 0)
        //        {
        //            this.headMain.render(scale);
        //            this.oxygenTank.render(scale);
        //        }

        this.bodyMain.render(matrixStack, buffer, packedLight, packedOverlay);
        this.rightLegFront.render(matrixStack, buffer, packedLight, packedOverlay);
        this.leftLegFront.render(matrixStack, buffer, packedLight, packedOverlay);
        this.rightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        this.leftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    private void setRotation(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void setRotationAngles(CreeperBossEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.headMain.yRot = netHeadYaw / Constants.RADIANS_TO_DEGREES;
        this.headMain.xRot = headPitch / Constants.RADIANS_TO_DEGREES;
        this.oxygenTank.yRot = netHeadYaw / Constants.RADIANS_TO_DEGREES;
        this.oxygenTank.xRot = headPitch / Constants.RADIANS_TO_DEGREES;
        this.rightLegFront.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLegFront.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2F * limbSwingAmount;
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 2F * limbSwingAmount;
    }
}
