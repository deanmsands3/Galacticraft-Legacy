package micdoodle8.mods.galacticraft.planets.venus.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import micdoodle8.mods.galacticraft.planets.venus.entities.SpiderQueenEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class SpiderQueenModel extends EntityModel<SpiderQueenEntity>
{
    ModelRenderer head;
    ModelRenderer body;
    ModelRenderer[] leg1 = new ModelRenderer[3];
    ModelRenderer[] leg2 = new ModelRenderer[3];
    ModelRenderer[] leg3 = new ModelRenderer[3];
    ModelRenderer[] leg4 = new ModelRenderer[3];
    ModelRenderer[] leg5 = new ModelRenderer[3];
    ModelRenderer[] leg6 = new ModelRenderer[3];
    ModelRenderer[] leg7 = new ModelRenderer[3];
    ModelRenderer[] leg8 = new ModelRenderer[3];
    ModelRenderer rearEnd;
    ModelRenderer rearBack;
    ModelRenderer rearLeft;
    ModelRenderer rearRight;

    public SpiderQueenModel()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.head = new ModelRenderer(this, 32, 4);
        this.head.addBox(-4F, -4F, -8F, 8, 8, 8);
        this.head.setRotationPoint(0F, 20F, -3F);
        this.head.setTextureSize(64, 64);
        this.head.mirror = true;
        this.setRotation(this.head, 0F, 0F, 0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-3F, -3F, -3F, 6, 6, 10);
        this.body.setRotationPoint(0F, 20F, 0F);
        this.body.setTextureSize(64, 64);
        this.body.mirror = true;
        this.setRotation(this.body, 0F, 0F, 0F);

        this.leg1[0] = new ModelRenderer(this, 28, 0);
        this.leg1[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg1[0].setRotationPoint(-3F, 20F, 4F);
        this.leg1[0].setTextureSize(64, 64);
        this.leg1[0].mirror = true;
        this.setRotation(this.leg1[0], 0F, 3.631943F, -0.7330383F);
        this.leg1[0].mirror = true;
        this.leg1[1] = new ModelRenderer(this, 28, 0);
        this.leg1[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg1[1].setRotationPoint(-9.7F, 13.2F, 7.6F);
        this.leg1[1].setTextureSize(64, 64);
        this.leg1[1].mirror = true;
        this.setRotation(this.leg1[1], 0F, 3.631937F, 0.3823201F);
        this.leg1[2] = new ModelRenderer(this, 28, 0);
        this.leg1[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg1[2].setRotationPoint(-16.4F, 16.2F, 11.1F);
        this.leg1[2].setTextureSize(64, 64);
        this.leg1[2].mirror = true;
        this.setRotation(this.leg1[2], 0F, 3.631937F, 1.461656F);

        this.leg2[0] = new ModelRenderer(this, 28, 0);
        this.leg2[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg2[0].setRotationPoint(3F, 20F, 4F);
        this.leg2[0].setTextureSize(64, 64);
        this.leg2[0].mirror = true;
        this.setRotation(this.leg2[0], 0F, -0.4903446F, -0.7330383F);
        this.leg2[1] = new ModelRenderer(this, 28, 0);
        this.leg2[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg2[1].setRotationPoint(9.666667F, 13.2F, 7.6F);
        this.leg2[1].setTextureSize(64, 64);
        this.leg2[1].mirror = true;
        this.setRotation(this.leg2[1], 0F, -0.4903503F, 0.3823201F);
        this.leg2[2] = new ModelRenderer(this, 28, 0);
        this.leg2[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg2[2].setRotationPoint(16.4F, 16.2F, 11.1F);
        this.leg2[2].setTextureSize(64, 64);
        this.leg2[2].mirror = true;
        this.setRotation(this.leg2[2], 0F, -0.4903503F, 1.461656F);

        this.leg3[0] = new ModelRenderer(this, 28, 0);
        this.leg3[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg3[0].setRotationPoint(-3F, 20F, 2F);
        this.leg3[0].setTextureSize(64, 64);
        this.leg3[0].mirror = true;
        this.setRotation(this.leg3[0], 0F, 3.335237F, -0.7330383F);
        this.leg3[1] = new ModelRenderer(this, 28, 0);
        this.leg3[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg3[1].setRotationPoint(-10.7F, 13.2F, 3.6F);
        this.leg3[1].setTextureSize(64, 64);
        this.leg3[1].mirror = true;
        this.setRotation(this.leg3[1], 0F, 3.335231F, 0.3823201F);
        this.leg3[1].mirror = false;
        this.leg3[2] = new ModelRenderer(this, 28, 0);
        this.leg3[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg3[2].setRotationPoint(-17.6F, 16.2F, 4.9F);
        this.leg3[2].setTextureSize(64, 64);
        this.leg3[2].mirror = true;
        this.setRotation(this.leg3[2], 0F, 3.335231F, 1.461656F);

        this.leg4[0] = new ModelRenderer(this, 28, 0);
        this.leg4[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg4[0].setRotationPoint(3F, 20F, 2F);
        this.leg4[0].setTextureSize(64, 64);
        this.leg4[0].mirror = true;
        this.setRotation(this.leg4[0], 0F, -0.1936386F, -0.7330383F);
        this.leg4[1] = new ModelRenderer(this, 28, 0);
        this.leg4[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg4[1].setRotationPoint(10.66667F, 13.2F, 3.6F);
        this.leg4[1].setTextureSize(64, 64);
        this.leg4[1].mirror = true;
        this.setRotation(this.leg4[1], 0F, -0.1936443F, 0.3823201F);
        this.leg4[2] = new ModelRenderer(this, 28, 0);
        this.leg4[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg4[2].setRotationPoint(17.6F, 16.2F, 4.9F);
        this.leg4[2].setTextureSize(64, 64);
        this.leg4[2].mirror = true;
        this.setRotation(this.leg4[2], 0F, -0.1936443F, 1.461656F);

        this.leg5[0] = new ModelRenderer(this, 28, 0);
        this.leg5[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg5[0].setRotationPoint(-3F, 20F, 0.5F);
        this.leg5[0].setTextureSize(64, 64);
        this.leg5[0].mirror = true;
        this.setRotation(this.leg5[0], 0F, 2.7838F, -0.7330383F);
        this.leg5[1] = new ModelRenderer(this, 28, 0);
        this.leg5[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg5[1].setRotationPoint(-10F, 13.2F, -2.2F);
        this.leg5[1].setTextureSize(64, 64);
        this.leg5[1].mirror = true;
        this.setRotation(this.leg5[1], 0F, 2.783794F, 0.3823201F);
        this.leg5[1].mirror = false;
        this.leg5[2] = new ModelRenderer(this, 28, 0);
        this.leg5[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg5[2].setRotationPoint(-16.5F, 16.2F, -4.7F);
        this.leg5[2].setTextureSize(64, 64);
        this.leg5[2].mirror = true;
        this.setRotation(this.leg5[2], 0F, 2.783794F, 1.461656F);

        this.leg6[0] = new ModelRenderer(this, 28, 0);
        this.leg6[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg6[0].setRotationPoint(3F, 20F, 0.5F);
        this.leg6[0].setTextureSize(64, 64);
        this.leg6[0].mirror = true;
        this.setRotation(this.leg6[0], 0F, 0.3648668F, -0.7330383F);
        this.leg6[1] = new ModelRenderer(this, 28, 0);
        this.leg6[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg6[1].setRotationPoint(10F, 13.2F, -2.2F);
        this.leg6[1].setTextureSize(64, 64);
        this.leg6[1].mirror = true;
        this.setRotation(this.leg6[1], 0F, 0.3648611F, 0.3823201F);
        this.leg6[2] = new ModelRenderer(this, 28, 0);
        this.leg6[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg6[2].setRotationPoint(16.53333F, 16.2F, -4.7F);
        this.leg6[2].setTextureSize(64, 64);
        this.leg6[2].mirror = true;
        this.setRotation(this.leg6[2], 0F, 0.3648611F, 1.461656F);

        this.leg7[0] = new ModelRenderer(this, 28, 0);
        this.leg7[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg7[0].setRotationPoint(-3F, 20F, -2F);
        this.leg7[0].setTextureSize(64, 64);
        this.leg7[0].mirror = true;
        this.setRotation(this.leg7[0], 0F, 2.495821F, -0.7330383F);
        this.leg7[1] = new ModelRenderer(this, 28, 0);
        this.leg7[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg7[1].setRotationPoint(-8.8F, 13.33333F, -6.466667F);
        this.leg7[1].setTextureSize(64, 64);
        this.leg7[1].mirror = true;
        this.setRotation(this.leg7[1], 0F, 2.495821F, 0.4206553F);
        this.leg7[2] = new ModelRenderer(this, 28, 0);
        this.leg7[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg7[2].setRotationPoint(-14.5F, 16.66667F, -10.9F);
        this.leg7[2].setTextureSize(64, 64);
        this.leg7[2].mirror = true;
        this.setRotation(this.leg7[2], 0F, 2.495821F, 1.201406F);

        this.leg8[0] = new ModelRenderer(this, 28, 0);
        this.leg8[0].addBox(0F, -1F, -1F, 10, 2, 2);
        this.leg8[0].setRotationPoint(3F, 20F, -2F);
        this.leg8[0].setTextureSize(64, 64);
        this.leg8[0].mirror = true;
        this.setRotation(this.leg8[0], 0F, 0.6615727F, -0.7330383F);
        this.leg8[1] = new ModelRenderer(this, 28, 0);
        this.leg8[1].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg8[1].setRotationPoint(8.8F, 13.33333F, -6.466667F);
        this.leg8[1].setTextureSize(64, 64);
        this.leg8[1].mirror = true;
        this.setRotation(this.leg8[1], 0F, 0.6368846F, 0.4206553F);
        this.leg8[2] = new ModelRenderer(this, 28, 0);
        this.leg8[2].addBox(0F, -1F, -1F, 8, 2, 2);
        this.leg8[2].setRotationPoint(14.53333F, 16.66667F, -10.9F);
        this.leg8[2].setTextureSize(64, 64);
        this.leg8[2].mirror = true;
        this.setRotation(this.leg8[2], 0F, 0.6615671F, 1.201406F);

        this.rearEnd = new ModelRenderer(this, 0, 41);
        this.rearEnd.addBox(-6F, -7F, -6F, 12, 11, 12);
        this.rearEnd.setRotationPoint(0F, 20F, 13F);
        this.rearEnd.setTextureSize(64, 64);
        this.rearEnd.mirror = true;
        this.setRotation(this.rearEnd, 0F, 0F, 0F);
        this.rearBack = new ModelRenderer(this, 26, 31);
        this.rearBack.addBox(-5F, -6F, 6F, 10, 9, 1);
        this.rearBack.setRotationPoint(0F, 20F, 13F);
        this.rearBack.setTextureSize(64, 64);
        this.rearBack.mirror = true;
        this.setRotation(this.rearBack, 0F, 0F, 0F);
        this.rearLeft = new ModelRenderer(this, 0, 22);
        this.rearLeft.addBox(6F, -6F, -5F, 1, 9, 10);
        this.rearLeft.setRotationPoint(0F, 20F, 13F);
        this.rearLeft.setTextureSize(64, 64);
        this.rearLeft.mirror = true;
        this.setRotation(this.rearLeft, 0F, 0F, 0F);
        this.rearRight = new ModelRenderer(this, 0, 22);
        this.rearRight.addBox(-7F, -6F, -5F, 1, 9, 10);
        this.rearRight.setRotationPoint(0F, 20F, 13F);
        this.rearRight.setTextureSize(64, 64);
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

    private void convertToChild(ModelRenderer parent, ModelRenderer child)
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

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
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

    private void copyLegAngles(float length, ModelRenderer parent, ModelRenderer child)
    {
        child.rotationPointX = parent.rotationPointX + length * (MathHelper.cos(parent.rotateAngleZ) * MathHelper.cos(parent.rotateAngleY));
        child.rotationPointY = parent.rotationPointY + length * MathHelper.sin(parent.rotateAngleZ);
        child.rotationPointZ = parent.rotationPointZ + length * (-MathHelper.sin(parent.rotateAngleY) * MathHelper.cos(parent.rotateAngleZ));
    }

    private void copyLeftToRight(ModelRenderer left, ModelRenderer right)
    {
        right.rotateAngleX = left.rotateAngleX;
        right.rotateAngleY = -left.rotateAngleY;
        right.rotateAngleZ = left.rotateAngleZ;
    }

    private void copyLeg1LeftToRight(ModelRenderer left, ModelRenderer right)
    {
        right.rotateAngleX = left.rotateAngleX;
        right.rotateAngleY = (float) (Math.PI - left.rotateAngleY);
        right.rotateAngleZ = left.rotateAngleZ;
    }

    @Override
    public void setRotationAngles(SpiderQueenEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        float movement = limbSwing;
        float increment = -1.0F;
        float offset = -0.4903446F;
        this.leg2[0].rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.leg2[0].rotateAngleZ = -0.7330383F;
        offset = -0.1936386F;
        movement += increment;
        this.leg4[0].rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.leg4[0].rotateAngleZ = -0.7330383F;
        offset = 0.3648668F;
        movement += increment;
        this.leg6[0].rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.leg6[0].rotateAngleZ = -0.7330383F;
        offset = 0.6615727F;
        movement += increment;
        this.leg8[0].rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.leg8[0].rotateAngleZ = -0.7330383F;

        float updist = -0.5F;

        // Move legs up if they are being moved forward. dx/dy of cos(movement) is -sin(movement)
        movement = limbSwing;
        if (-MathHelper.sin(movement) * 0.2 > 0.0F)
        {
            this.leg2[0].rotateAngleZ = -0.7330383F + -MathHelper.sin(movement) * updist;
        }
        movement += increment;
        if (-MathHelper.sin(movement) * 0.2 > 0.0F)
        {
            this.leg4[0].rotateAngleZ = -0.7330383F + -MathHelper.sin(movement) * updist;
        }
        movement += increment;
        if (-MathHelper.sin(movement) * 0.2 > 0.0F)
        {
            this.leg6[0].rotateAngleZ = -0.7330383F + -MathHelper.sin(movement) * updist;
        }
        movement += increment;
        if (-MathHelper.sin(movement) * 0.2 > 0.0F)
        {
            this.leg8[0].rotateAngleZ = -0.7330383F + -MathHelper.sin(movement) * updist;
        }

        for (int i = 1; i < 3; ++i)
        {
            this.leg1[i].rotateAngleY = this.leg1[0].rotateAngleY;
            this.leg2[i].rotateAngleY = this.leg2[0].rotateAngleY;
            this.leg3[i].rotateAngleY = this.leg3[0].rotateAngleY;
            this.leg4[i].rotateAngleY = this.leg4[0].rotateAngleY;
            this.leg5[i].rotateAngleY = this.leg5[0].rotateAngleY;
            this.leg6[i].rotateAngleY = this.leg6[0].rotateAngleY;
            this.leg7[i].rotateAngleY = this.leg7[0].rotateAngleY;
            this.leg8[i].rotateAngleY = this.leg8[0].rotateAngleY;

            this.leg1[i].rotateAngleX = this.leg1[0].rotateAngleX;
            this.leg2[i].rotateAngleX = this.leg2[0].rotateAngleX;
            this.leg3[i].rotateAngleX = this.leg3[0].rotateAngleX;
            this.leg4[i].rotateAngleX = this.leg4[0].rotateAngleX;
            this.leg5[i].rotateAngleX = this.leg5[0].rotateAngleX;
            this.leg6[i].rotateAngleX = this.leg6[0].rotateAngleX;
            this.leg7[i].rotateAngleX = this.leg7[0].rotateAngleX;
            this.leg8[i].rotateAngleX = this.leg8[0].rotateAngleX;

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
