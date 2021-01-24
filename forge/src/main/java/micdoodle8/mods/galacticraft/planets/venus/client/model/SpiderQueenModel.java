package micdoodle8.mods.galacticraft.planets.venus.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import micdoodle8.mods.galacticraft.planets.venus.entities.SpiderQueenEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class SpiderQueenModel extends EntityModel<SpiderQueenEntity>
{
    ModelPart head;
    ModelPart body;
    ModelPart[] leg1 = new ModelPart[3];
    ModelPart[] leg2 = new ModelPart[3];
    ModelPart[] leg3 = new ModelPart[3];
    ModelPart[] leg4 = new ModelPart[3];
    ModelPart[] leg5 = new ModelPart[3];
    ModelPart[] leg6 = new ModelPart[3];
    ModelPart[] leg7 = new ModelPart[3];
    ModelPart[] leg8 = new ModelPart[3];
    ModelPart rearEnd;
    ModelPart rearBack;
    ModelPart rearLeft;
    ModelPart rearRight;

    public SpiderQueenModel()
    {
        this.texWidth = 64;
        this.texHeight = 64;

        this.head = new ModelPart(this, 32, 4);
        this.head.addBox(-4F, -4F, -8F, 8, 8, 8);
        this.head.setPos(0F, 20F, -3F);
        this.head.setTexSize(64, 64);
        this.head.mirror = true;
        this.setRotation(this.head, 0F, 0F, 0F);
        this.body = new ModelPart(this, 0, 0);
        this.body.addBox(-3F, -3F, -3F, 6, 6, 10);
        this.body.setPos(0F, 20F, 0F);
        this.body.setTexSize(64, 64);
        this.body.mirror = true;
        this.setRotation(this.body, 0F, 0F, 0F);

        this.leg1[0] = new ModelPart(this, 28, 0);
        this.leg1[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg1[0].setPos(-3F, 20F, 4F);
        this.leg1[0].setTexSize(64, 64);
        this.leg1[0].mirror = true;
        this.setRotation(this.leg1[0], 0F, 3.631943F, -0.7330383F);
        this.leg1[0].mirror = true;
        this.leg1[1] = new ModelPart(this, 28, 0);
        this.leg1[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg1[1].setPos(-9.7F, 13.2F, 7.6F);
        this.leg1[1].setTexSize(64, 64);
        this.leg1[1].mirror = true;
        this.setRotation(this.leg1[1], 0F, 3.631937F, 0.3823201F);
        this.leg1[2] = new ModelPart(this, 28, 0);
        this.leg1[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg1[2].setPos(-16.4F, 16.2F, 11.1F);
        this.leg1[2].setTexSize(64, 64);
        this.leg1[2].mirror = true;
        this.setRotation(this.leg1[2], 0F, 3.631937F, 1.461656F);

        this.leg2[0] = new ModelPart(this, 28, 0);
        this.leg2[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg2[0].setPos(3F, 20F, 4F);
        this.leg2[0].setTexSize(64, 64);
        this.leg2[0].mirror = true;
        this.setRotation(this.leg2[0], 0F, -0.4903446F, -0.7330383F);
        this.leg2[1] = new ModelPart(this, 28, 0);
        this.leg2[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg2[1].setPos(9.666667F, 13.2F, 7.6F);
        this.leg2[1].setTexSize(64, 64);
        this.leg2[1].mirror = true;
        this.setRotation(this.leg2[1], 0F, -0.4903503F, 0.3823201F);
        this.leg2[2] = new ModelPart(this, 28, 0);
        this.leg2[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg2[2].setPos(16.4F, 16.2F, 11.1F);
        this.leg2[2].setTexSize(64, 64);
        this.leg2[2].mirror = true;
        this.setRotation(this.leg2[2], 0F, -0.4903503F, 1.461656F);

        this.leg3[0] = new ModelPart(this, 28, 0);
        this.leg3[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg3[0].setPos(-3F, 20F, 2F);
        this.leg3[0].setTexSize(64, 64);
        this.leg3[0].mirror = true;
        this.setRotation(this.leg3[0], 0F, 3.335237F, -0.7330383F);
        this.leg3[1] = new ModelPart(this, 28, 0);
        this.leg3[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg3[1].setPos(-10.7F, 13.2F, 3.6F);
        this.leg3[1].setTexSize(64, 64);
        this.leg3[1].mirror = true;
        this.setRotation(this.leg3[1], 0F, 3.335231F, 0.3823201F);
        this.leg3[1].mirror = false;
        this.leg3[2] = new ModelPart(this, 28, 0);
        this.leg3[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg3[2].setPos(-17.6F, 16.2F, 4.9F);
        this.leg3[2].setTexSize(64, 64);
        this.leg3[2].mirror = true;
        this.setRotation(this.leg3[2], 0F, 3.335231F, 1.461656F);

        this.leg4[0] = new ModelPart(this, 28, 0);
        this.leg4[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg4[0].setPos(3F, 20F, 2F);
        this.leg4[0].setTexSize(64, 64);
        this.leg4[0].mirror = true;
        this.setRotation(this.leg4[0], 0F, -0.1936386F, -0.7330383F);
        this.leg4[1] = new ModelPart(this, 28, 0);
        this.leg4[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg4[1].setPos(10.66667F, 13.2F, 3.6F);
        this.leg4[1].setTexSize(64, 64);
        this.leg4[1].mirror = true;
        this.setRotation(this.leg4[1], 0F, -0.1936443F, 0.3823201F);
        this.leg4[2] = new ModelPart(this, 28, 0);
        this.leg4[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg4[2].setPos(17.6F, 16.2F, 4.9F);
        this.leg4[2].setTexSize(64, 64);
        this.leg4[2].mirror = true;
        this.setRotation(this.leg4[2], 0F, -0.1936443F, 1.461656F);

        this.leg5[0] = new ModelPart(this, 28, 0);
        this.leg5[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg5[0].setPos(-3F, 20F, 0.5F);
        this.leg5[0].setTexSize(64, 64);
        this.leg5[0].mirror = true;
        this.setRotation(this.leg5[0], 0F, 2.7838F, -0.7330383F);
        this.leg5[1] = new ModelPart(this, 28, 0);
        this.leg5[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg5[1].setPos(-10F, 13.2F, -2.2F);
        this.leg5[1].setTexSize(64, 64);
        this.leg5[1].mirror = true;
        this.setRotation(this.leg5[1], 0F, 2.783794F, 0.3823201F);
        this.leg5[1].mirror = false;
        this.leg5[2] = new ModelPart(this, 28, 0);
        this.leg5[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg5[2].setPos(-16.5F, 16.2F, -4.7F);
        this.leg5[2].setTexSize(64, 64);
        this.leg5[2].mirror = true;
        this.setRotation(this.leg5[2], 0F, 2.783794F, 1.461656F);

        this.leg6[0] = new ModelPart(this, 28, 0);
        this.leg6[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg6[0].setPos(3F, 20F, 0.5F);
        this.leg6[0].setTexSize(64, 64);
        this.leg6[0].mirror = true;
        this.setRotation(this.leg6[0], 0F, 0.3648668F, -0.7330383F);
        this.leg6[1] = new ModelPart(this, 28, 0);
        this.leg6[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg6[1].setPos(10F, 13.2F, -2.2F);
        this.leg6[1].setTexSize(64, 64);
        this.leg6[1].mirror = true;
        this.setRotation(this.leg6[1], 0F, 0.3648611F, 0.3823201F);
        this.leg6[2] = new ModelPart(this, 28, 0);
        this.leg6[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg6[2].setPos(16.53333F, 16.2F, -4.7F);
        this.leg6[2].setTexSize(64, 64);
        this.leg6[2].mirror = true;
        this.setRotation(this.leg6[2], 0F, 0.3648611F, 1.461656F);

        this.leg7[0] = new ModelPart(this, 28, 0);
        this.leg7[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg7[0].setPos(-3F, 20F, -2F);
        this.leg7[0].setTexSize(64, 64);
        this.leg7[0].mirror = true;
        this.setRotation(this.leg7[0], 0F, 2.495821F, -0.7330383F);
        this.leg7[1] = new ModelPart(this, 28, 0);
        this.leg7[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg7[1].setPos(-8.8F, 13.33333F, -6.466667F);
        this.leg7[1].setTexSize(64, 64);
        this.leg7[1].mirror = true;
        this.setRotation(this.leg7[1], 0F, 2.495821F, 0.4206553F);
        this.leg7[2] = new ModelPart(this, 28, 0);
        this.leg7[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg7[2].setPos(-14.5F, 16.66667F, -10.9F);
        this.leg7[2].setTexSize(64, 64);
        this.leg7[2].mirror = true;
        this.setRotation(this.leg7[2], 0F, 2.495821F, 1.201406F);

        this.leg8[0] = new ModelPart(this, 28, 0);
        this.leg8[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg8[0].setPos(3F, 20F, -2F);
        this.leg8[0].setTexSize(64, 64);
        this.leg8[0].mirror = true;
        this.setRotation(this.leg8[0], 0F, 0.6615727F, -0.7330383F);
        this.leg8[1] = new ModelPart(this, 28, 0);
        this.leg8[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg8[1].setPos(8.8F, 13.33333F, -6.466667F);
        this.leg8[1].setTexSize(64, 64);
        this.leg8[1].mirror = true;
        this.setRotation(this.leg8[1], 0F, 0.6368846F, 0.4206553F);
        this.leg8[2] = new ModelPart(this, 28, 0);
        this.leg8[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg8[2].setPos(14.53333F, 16.66667F, -10.9F);
        this.leg8[2].setTexSize(64, 64);
        this.leg8[2].mirror = true;
        this.setRotation(this.leg8[2], 0F, 0.6615671F, 1.201406F);

        this.rearEnd = new ModelPart(this, 0, 41);
        this.rearEnd.addBox(-6F, -7F, -6F, 12, 11, 12);
        this.rearEnd.setPos(0F, 20F, 13F);
        this.rearEnd.setTexSize(64, 64);
        this.rearEnd.mirror = true;
        this.setRotation(this.rearEnd, 0F, 0F, 0F);
        this.rearBack = new ModelPart(this, 26, 31);
        this.rearBack.addBox(-5F, -6F, 6F, 10, 9, 1);
        this.rearBack.setPos(0F, 20F, 13F);
        this.rearBack.setTexSize(64, 64);
        this.rearBack.mirror = true;
        this.setRotation(this.rearBack, 0F, 0F, 0F);
        this.rearLeft = new ModelPart(this, 0, 22);
        this.rearLeft.addBox(6F, -6F, -5F, 1, 9, 10);
        this.rearLeft.setPos(0F, 20F, 13F);
        this.rearLeft.setTexSize(64, 64);
        this.rearLeft.mirror = true;
        this.setRotation(this.rearLeft, 0F, 0F, 0F);
        this.rearRight = new ModelPart(this, 0, 22);
        this.rearRight.addBox(-7F, -6F, -5F, 1, 9, 10);
        this.rearRight.setPos(0F, 20F, 13F);
        this.rearRight.setTexSize(64, 64);
        this.rearRight.mirror = true;
        this.setRotation(this.rearRight, 0F, 0F, 0F);

        for (int i = 0; i < 2; ++i)
        {
            this.convertToChild(this.leg1[i], this.leg1[i + 1]);
            this.convertToChild(this.leg2[i], this.leg2[i + 1]);
            this.convertToChild(this.leg3[i], this.leg3[i + 1]);
            this.convertToChild(this.leg4[i], this.leg4[i + 1]);
            this.convertToChild(this.leg5[i], this.leg5[i + 1]);
            this.convertToChild(this.leg6[i], this.leg6[i + 1]);
            this.convertToChild(this.leg7[i], this.leg7[i + 1]);
            this.convertToChild(this.leg8[i], this.leg8[i + 1]);
        }
    }

    private void convertToChild(ModelPart parent, ModelPart child)
    {
        //        // move child rotation point to be relative to parent
        //        child.rotationPointX -= parent.rotationPointX;
        //        child.rotationPointY -= parent.rotationPointY;
        //        child.rotationPointZ -= parent.rotationPointZ;
        //        // make rotations relative to parent
        //        child.rotateAngleX -= parent.rotateAngleX;
        //        child.rotateAngleY -= parent.rotateAngleY;
        //        child.rotateAngleZ -= parent.rotateAngleZ;
        //        // create relationship
        //        parent.addChild(child);
    }

    private void setRotation(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        this.head.render(matrixStack, buffer, packedLight, packedOverlay);
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);

        for (int i = 0; i < 3; ++i)
        {
            this.leg1[i].render(matrixStack, buffer, packedLight, packedOverlay);
            this.leg2[i].render(matrixStack, buffer, packedLight, packedOverlay);
            this.leg3[i].render(matrixStack, buffer, packedLight, packedOverlay);
            this.leg4[i].render(matrixStack, buffer, packedLight, packedOverlay);
            this.leg5[i].render(matrixStack, buffer, packedLight, packedOverlay);
            this.leg6[i].render(matrixStack, buffer, packedLight, packedOverlay);
            this.leg7[i].render(matrixStack, buffer, packedLight, packedOverlay);
            this.leg8[i].render(matrixStack, buffer, packedLight, packedOverlay);
        }
        this.rearEnd.render(matrixStack, buffer, packedLight, packedOverlay);
        this.rearBack.render(matrixStack, buffer, packedLight, packedOverlay);
        this.rearLeft.render(matrixStack, buffer, packedLight, packedOverlay);
        this.rearRight.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    private void copyLegAngles(float length, ModelPart parent, ModelPart child)
    {
        child.x = parent.x + length * (Mth.cos(parent.zRot) * Mth.cos(parent.yRot));
        child.y = parent.y + length * Mth.sin(parent.zRot);
        child.z = parent.z + length * (-Mth.sin(parent.yRot) * Mth.cos(parent.zRot));
    }

    private void copyLeftToRight(ModelPart left, ModelPart right)
    {
        right.xRot = left.xRot;
        right.yRot = -left.yRot;
        right.zRot = left.zRot;
    }

    private void copyLeg1LeftToRight(ModelPart left, ModelPart right)
    {
        right.xRot = left.xRot;
        right.yRot = (float) (Math.PI - left.yRot);
        right.zRot = left.zRot;
    }

    @Override
    public void setRotationAngles(SpiderQueenEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        float movement = limbSwing;
        float increment = -1.0F;
        float offset = -0.4903446F;
        this.leg2[0].yRot = Mth.cos(movement) * 0.2F + offset;
        this.leg2[0].zRot = -0.7330383F;
        offset = -0.1936386F;
        movement += increment;
        this.leg4[0].yRot = Mth.cos(movement) * 0.2F + offset;
        this.leg4[0].zRot = -0.7330383F;
        offset = 0.3648668F;
        movement += increment;
        this.leg6[0].yRot = Mth.cos(movement) * 0.2F + offset;
        this.leg6[0].zRot = -0.7330383F;
        offset = 0.6615727F;
        movement += increment;
        this.leg8[0].yRot = Mth.cos(movement) * 0.2F + offset;
        this.leg8[0].zRot = -0.7330383F;

        float updist = -0.5F;

        // Move legs up if they are being moved forward. dx/dy of cos(movement) is -sin(movement)
        movement = limbSwing;
        if (-Mth.sin(movement) * 0.2 > 0.0F)
        {
            this.leg2[0].zRot = -0.7330383F + -Mth.sin(movement) * updist;
        }
        movement += increment;
        if (-Mth.sin(movement) * 0.2 > 0.0F)
        {
            this.leg4[0].zRot = -0.7330383F + -Mth.sin(movement) * updist;
        }
        movement += increment;
        if (-Mth.sin(movement) * 0.2 > 0.0F)
        {
            this.leg6[0].zRot = -0.7330383F + -Mth.sin(movement) * updist;
        }
        movement += increment;
        if (-Mth.sin(movement) * 0.2 > 0.0F)
        {
            this.leg8[0].zRot = -0.7330383F + -Mth.sin(movement) * updist;
        }

        for (int i = 1; i < 3; ++i)
        {
            this.leg1[i].yRot = this.leg1[0].yRot;
            this.leg2[i].yRot = this.leg2[0].yRot;
            this.leg3[i].yRot = this.leg3[0].yRot;
            this.leg4[i].yRot = this.leg4[0].yRot;
            this.leg5[i].yRot = this.leg5[0].yRot;
            this.leg6[i].yRot = this.leg6[0].yRot;
            this.leg7[i].yRot = this.leg7[0].yRot;
            this.leg8[i].yRot = this.leg8[0].yRot;

            this.leg1[i].xRot = this.leg1[0].xRot;
            this.leg2[i].xRot = this.leg2[0].xRot;
            this.leg3[i].xRot = this.leg3[0].xRot;
            this.leg4[i].xRot = this.leg4[0].xRot;
            this.leg5[i].xRot = this.leg5[0].xRot;
            this.leg6[i].xRot = this.leg6[0].xRot;
            this.leg7[i].xRot = this.leg7[0].xRot;
            this.leg8[i].xRot = this.leg8[0].xRot;

            //            this.leg1[i].rotateAngleZ = this.leg1[0].rotateAngleZ;
            //            this.leg2[i].rotateAngleZ = this.leg2[0].rotateAngleZ;
            //            this.leg3[i].rotateAngleZ = this.leg3[0].rotateAngleZ;
            //            this.leg4[i].rotateAngleZ = this.leg4[0].rotateAngleZ;
            //            this.leg5[i].rotateAngleZ = this.leg5[0].rotateAngleZ;
            //            this.leg6[i].rotateAngleZ = this.leg6[0].rotateAngleZ;
            //            this.leg7[i].rotateAngleZ = this.leg7[0].rotateAngleZ;
            //            this.leg8[i].rotateAngleZ = this.leg8[0].rotateAngleZ;
        }

        for (int i = 0; i < 1; ++i)
        {
            this.copyLeg1LeftToRight(this.leg2[i], this.leg1[i]);
            this.copyLeg1LeftToRight(this.leg4[i], this.leg3[i]);
            this.copyLeg1LeftToRight(this.leg6[i], this.leg5[i]);
            this.copyLeg1LeftToRight(this.leg8[i], this.leg7[i]);
        }
        for (int i = 1; i < 2; ++i)
        {
            float length1a = 10.0F;
            float length1b = 10.0F;
            this.copyLegAngles(length1a, this.leg1[0], this.leg1[1]);
            this.copyLegAngles(length1b, this.leg2[0], this.leg2[1]);
            this.copyLegAngles(length1a, this.leg3[0], this.leg3[1]);
            this.copyLegAngles(length1b, this.leg4[0], this.leg4[1]);
            this.copyLegAngles(length1a, this.leg5[0], this.leg5[1]);
            this.copyLegAngles(length1b, this.leg6[0], this.leg6[1]);
            this.copyLegAngles(length1a, this.leg7[0], this.leg7[1]);
            this.copyLegAngles(length1b, this.leg8[0], this.leg8[1]);

            float length2a = 8.0F;
            float length2b = 8.0F;
            this.copyLegAngles(length2a, this.leg1[1], this.leg1[2]);
            this.copyLegAngles(length2b, this.leg2[1], this.leg2[2]);
            this.copyLegAngles(length2a, this.leg3[1], this.leg3[2]);
            this.copyLegAngles(length2b, this.leg4[1], this.leg4[2]);
            this.copyLegAngles(length2a, this.leg5[1], this.leg5[2]);
            this.copyLegAngles(length2b, this.leg6[1], this.leg6[2]);
            this.copyLegAngles(length2a, this.leg7[1], this.leg7[2]);
            this.copyLegAngles(length2b, this.leg8[1], this.leg8[2]);
        }
        //        EntityJuicer juicer = (EntityJuicer) entityIn;
        //        super.setRotationAngles(f1, f2, f3, f4, f5, f6, entityIn);
        //        float movement = f1;
        //        float increment = -1.0F;
        //        float offset = 0.5F;
        //        this.legLeftFront1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        //        this.legLeftFront1.rotateAngleZ = (float) (-Math.PI / 3.0F);
        //        this.legLeftFront2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
        //        this.legLeftFront2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
        //        offset = 0.05F;
        //        movement += increment;
        //        this.legLeftMidFront1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        //        this.legLeftMidFront1.rotateAngleZ = (float) (-Math.PI / 3.0F);
        //        this.legLeftMidFront2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
        //        this.legLeftMidFront2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
        //        offset = -0.1F;
        //        movement += increment;
        //        this.legLeftMidBack1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        //        this.legLeftMidBack1.rotateAngleZ = (float) (-Math.PI / 3.0F);
        //        this.legLeftMidBack2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
        //        this.legLeftMidBack2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
        //        offset = -0.5F;
        //        movement += increment;
        //        this.legLeftBack1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        //        this.legLeftBack1.rotateAngleZ = (float) (-Math.PI / 3.0F);
        //        this.legLeftBack2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
        //        this.legLeftBack2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
        //
        //        this.copyLegAngles(legLeftFront1, legLeftFront2);
        //        this.copyLegAngles(legLeftMidFront1, legLeftMidFront2);
        //        this.copyLegAngles(legLeftMidBack1, legLeftMidBack2);
        //        this.copyLegAngles(legLeftBack1, legLeftBack2);
        //        this.copyLegAngles(legRightFront1, legRightFront2);
        //        this.copyLegAngles(legRightMidFront1, legRightMidFront2);
        //        this.copyLegAngles(legRightMidBack1, legRightMidBack2);
        //        this.copyLegAngles(legRightBack1, legRightBack2);
        //
        //        this.copyLeg1LeftToRight(legLeftFront1, legRightFront1);
        //        this.copyLeg1LeftToRight(legLeftMidFront1, legRightMidFront1);
        //        this.copyLeg1LeftToRight(legLeftMidBack1, legRightMidBack1);
        //        this.copyLeg1LeftToRight(legLeftBack1, legRightBack1);
        //        this.copyLeftToRight(legLeftFront2, legRightFront2);
        //        this.copyLeftToRight(legLeftMidFront2, legRightMidFront2);
        //        this.copyLeftToRight(legLeftMidBack2, legRightMidBack2);
        //        this.copyLeftToRight(legLeftBack2, legRightBack2);
    }
}
