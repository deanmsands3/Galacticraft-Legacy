package micdoodle8.mods.galacticraft.planets.venus.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import micdoodle8.mods.galacticraft.planets.venus.entities.JuicerEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class JuicerModel extends EntityModel<JuicerEntity>
{
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart head;
    private final ModelPart legRightFront1;
    private final ModelPart legRightFront2;
    private final ModelPart legLeftFront1;
    private final ModelPart legLeftFront2;
    private final ModelPart legRightMidFront1;
    private final ModelPart legRightMidFront2;
    private final ModelPart legLeftMidFront1;
    private final ModelPart legLeftMidFront2;
    private final ModelPart legRightMidBack1;
    private final ModelPart legRightMidBack2;
    private final ModelPart legLeftMidBack1;
    private final ModelPart legLeftMidBack2;
    private final ModelPart legRightBack1;
    private final ModelPart legRightBack2;
    private final ModelPart legLeftBack1;
    private final ModelPart legLeftBack2;
    private final ModelPart back;
    private final ModelPart tail0;
    private final ModelPart tail1;
    private final ModelPart stinger;

    private final float legLength0;

    public JuicerModel()
    {
        this.texWidth = 64;
        this.texHeight = 32;

        this.body1 = new ModelPart(this, 24, 22);
        this.body1.addBox(-3F, -1F, -4F, 6, 2, 8);
        this.body1.setPos(0F, 0F, 0F);
        this.body1.setTexSize(64, 32);
        this.body1.mirror = true;
        this.setRotation(this.body1, 0F, 0F, 0F);
        this.body2 = new ModelPart(this, 0, 22);
        this.body2.addBox(-2.5F, -1.5F, -3.5F, 5, 3, 7);
        this.body2.setPos(0F, 0F, 0F);
        this.body2.setTexSize(64, 32);
        this.body2.mirror = true;
        this.setRotation(this.body2, 0F, 0F, 0F);

        this.head = new ModelPart(this, 52, 0);
        this.head.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
        this.head.setPos(0F, -1F, -4F);
        this.head.setTexSize(64, 32);
        this.head.mirror = true;
        this.setRotation(this.head, -0.3717861F, 0F, 0F);

        this.legRightFront1 = new ModelPart(this, 44, 0);
        this.legRightFront1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legRightFront1.setPos(-2.5F, 0F, -3F);
        this.legRightFront1.setTexSize(64, 32);
        this.legRightFront1.mirror = true;
        this.setRotation(this.legRightFront1, 0F, 2.453788F, 1.115358F);
        this.legRightFront2 = new ModelPart(this, 30, 0);
        this.legRightFront2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legRightFront2.setPos(-3.5F, -3F, -4F);
        this.legRightFront2.setTexSize(64, 32);
        this.legRightFront2.mirror = true;
        this.setRotation(this.legRightFront2, 0F, 2.825574F, -0.8551081F);

        this.legLeftFront1 = new ModelPart(this, 44, 0);
        this.legLeftFront1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legLeftFront1.setPos(2.5F, 0F, -3F);
        this.legLeftFront1.setTexSize(64, 32);
        this.legLeftFront1.mirror = true;
        this.setRotation(this.legLeftFront1, 0F, 0.7807508F, -1.115358F);
        this.legLeftFront2 = new ModelPart(this, 30, 0);
        this.legLeftFront2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legLeftFront2.setPos(3.5F, -3F, -4F);
        this.legLeftFront2.setTexSize(64, 32);
        this.legLeftFront2.mirror = true;
        this.setRotation(this.legLeftFront2, 0F, 0.7063936F, 0.8551081F);

        this.legRightMidFront1 = new ModelPart(this, 44, 0);
        this.legRightMidFront1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legRightMidFront1.setPos(-2.5F, 0F, -1F);
        this.legRightMidFront1.setTexSize(64, 32);
        this.legRightMidFront1.mirror = true;
        this.setRotation(this.legRightMidFront1, 0F, 2.93711F, 1.115358F);
        this.legRightMidFront2 = new ModelPart(this, 30, 0);
        this.legRightMidFront2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legRightMidFront2.setPos(-3.5F, -3F, -1F);
        this.legRightMidFront2.setTexSize(64, 32);
        this.legRightMidFront2.mirror = true;
        this.setRotation(this.legRightMidFront2, 0F, 3.011467F, -0.8551081F);

        this.legLeftMidFront1 = new ModelPart(this, 44, 0);
        this.legLeftMidFront1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legLeftMidFront1.setPos(2.5F, 0F, -1F);
        this.legLeftMidFront1.setTexSize(64, 32);
        this.legLeftMidFront1.mirror = true;
        this.setRotation(this.legLeftMidFront1, 0F, 0.1858931F, -1.115358F);
        this.legLeftMidFront2 = new ModelPart(this, 30, 0);
        this.legLeftMidFront2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legLeftMidFront2.setPos(3.5F, -3F, -1F);
        this.legLeftMidFront2.setTexSize(64, 32);
        this.legLeftMidFront2.mirror = true;
        this.setRotation(this.legLeftMidFront2, 0F, 0.3346075F, 0.8551081F);

        this.legRightMidBack1 = new ModelPart(this, 44, 0);
        this.legRightMidBack1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legRightMidBack1.setPos(-2.5F, 0F, 1F);
        this.legRightMidBack1.setTexSize(64, 32);
        this.legRightMidBack1.mirror = true;
        this.setRotation(this.legRightMidBack1, 0F, -3.030057F, 1.115358F);
        this.legRightMidBack2 = new ModelPart(this, 30, 0);
        this.legRightMidBack2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legRightMidBack2.setPos(-3.5F, -3F, 1F);
        this.legRightMidBack2.setTexSize(64, 32);
        this.legRightMidBack2.mirror = true;
        this.setRotation(this.legRightMidBack2, 0F, -2.974289F, -0.8551081F);

        this.legLeftMidBack1 = new ModelPart(this, 44, 0);
        this.legLeftMidBack1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legLeftMidBack1.setPos(2.5F, 0F, 1F);
        this.legLeftMidBack1.setTexSize(64, 32);
        this.legLeftMidBack1.mirror = true;
        this.setRotation(this.legLeftMidBack1, 0F, -0.0371786F, -1.115358F);
        this.legLeftMidBack2 = new ModelPart(this, 30, 0);
        this.legLeftMidBack2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legLeftMidBack2.setPos(3.5F, -3F, 1F);
        this.legLeftMidBack2.setTexSize(64, 32);
        this.legLeftMidBack2.mirror = true;
        this.setRotation(this.legLeftMidBack2, 0F, -0.1487144F, 0.8551081F);

        this.legRightBack1 = new ModelPart(this, 44, 0);
        this.legRightBack1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legRightBack1.setPos(-2.5F, 0F, 3F);
        this.legRightBack1.setTexSize(64, 32);
        this.legRightBack1.mirror = true;
        this.setRotation(this.legRightBack1, 0F, -2.658271F, 1.115358F);
        this.legRightBack2 = new ModelPart(this, 30, 0);
        this.legRightBack2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legRightBack2.setPos(-3.5F, -3F, 4F);
        this.legRightBack2.setTexSize(64, 32);
        this.legRightBack2.mirror = true;
        this.setRotation(this.legRightBack2, 0F, -2.788396F, -0.8551081F);

        this.legLeftBack1 = new ModelPart(this, 44, 0);
        this.legLeftBack1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legLeftBack1.setPos(2.5F, 0F, 3F);
        this.legLeftBack1.setTexSize(64, 32);
        this.legLeftBack1.mirror = true;
        this.setRotation(this.legLeftBack1, 0F, -0.3346075F, -1.115358F);
        this.legLeftBack2 = new ModelPart(this, 30, 0);
        this.legLeftBack2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legLeftBack2.setPos(3.5F, -3F, 3F);
        this.legLeftBack2.setTexSize(64, 32);
        this.legLeftBack2.mirror = true;
        this.setRotation(this.legLeftBack2, 0F, -0.5205006F, 0.8551081F);

        this.back = new ModelPart(this, 0, 16);
        this.back.addBox(-1.5F, -0.5F, -2.5F, 3, 1, 5);
        this.back.setPos(0F, -1.5F, 0F);
        this.back.setTexSize(64, 32);
        this.back.mirror = true;
        this.setRotation(this.back, 0F, 0F, 0F);

        this.tail0 = new ModelPart(this, 42, 17);
        this.tail0.addBox(-0.5F, -0.5F, 0F, 1, 1, 4);
        this.tail0.setPos(0F, -2F, 0F);
        this.tail0.setTexSize(64, 32);
        this.tail0.mirror = true;
        this.setRotation(this.tail0, 0.5205006F, 0F, 0F);
        this.tail1 = new ModelPart(this, 42, 12);
        this.tail1.addBox(-0.5F, 0F, 0F, 1, 1, 4);
        this.tail1.setPos(0F, -3F, 3F);
        this.tail1.setTexSize(64, 32);
        this.tail1.mirror = true;
        this.setRotation(this.tail1, 2.659407F, 0F, 0F);
        this.stinger = new ModelPart(this, 48, 9);
        this.stinger.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.stinger.setPos(0F, -5.2F, -1.133333F);
        this.stinger.setTexSize(64, 32);
        this.stinger.mirror = true;
        this.setRotation(this.stinger, 1.487144F, 0F, 0F);

        this.convertToChild(this.legLeftFront1, this.legLeftFront2);
        this.convertToChild(this.legLeftMidFront1, this.legLeftMidFront2);
        this.convertToChild(this.legLeftMidBack1, this.legLeftMidBack2);
        this.convertToChild(this.legLeftBack1, this.legLeftBack2);
        this.convertToChild(this.legRightFront1, this.legRightFront2);
        this.convertToChild(this.legRightMidFront1, this.legRightMidFront2);
        this.convertToChild(this.legRightMidBack1, this.legRightMidBack2);
        this.convertToChild(this.legRightBack1, this.legRightBack2);

        this.convertToChild(this.tail0, this.tail1);
        this.convertToChild(this.tail1, this.stinger);

        this.legLength0 = this.legLeftFront1.cubes.get(0).maxX - this.legLeftFront1.cubes.get(0).minX;
    }

    private void convertToChild(ModelPart parent, ModelPart child)
    {
        // move child rotation point to be relative to parent
        child.x -= parent.x;
        child.y -= parent.y;
        child.z -= parent.z;
        // make rotations relative to parent
        child.xRot -= parent.xRot;
        child.yRot -= parent.yRot;
        child.zRot -= parent.zRot;
        // create relationship
        parent.addChild(child);
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
        this.body1.render(matrixStack, buffer, packedLight, packedOverlay);
        this.body2.render(matrixStack, buffer, packedLight, packedOverlay);
        this.head.render(matrixStack, buffer, packedLight, packedOverlay);
        this.legRightMidBack1.render(matrixStack, buffer, packedLight, packedOverlay);
        this.legRightFront1.render(matrixStack, buffer, packedLight, packedOverlay);
        this.legRightMidFront1.render(matrixStack, buffer, packedLight, packedOverlay);
        this.legRightBack1.render(matrixStack, buffer, packedLight, packedOverlay);
        this.legLeftBack1.render(matrixStack, buffer, packedLight, packedOverlay);
        this.legLeftMidBack1.render(matrixStack, buffer, packedLight, packedOverlay);
        this.legLeftMidFront1.render(matrixStack, buffer, packedLight, packedOverlay);
        this.legLeftFront1.render(matrixStack, buffer, packedLight, packedOverlay);
        this.back.render(matrixStack, buffer, packedLight, packedOverlay);
        this.tail0.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    private void copyLegAngles(ModelPart parent, ModelPart child)
    {
        child.x = this.legLength0 * (parent.x < 0.0F ? -1.0F : 1.0F) * (Mth.sin(parent.xRot) + Mth.cos(parent.yRot));
        child.y = 0.0F;
        child.z = 0.0F;
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
        right.zRot = -left.zRot;
    }

    @Override
    public void setRotationAngles(JuicerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        float movement = limbSwing;
        float increment = -1.0F;
        float offset = 0.5F;
        this.legLeftFront1.yRot = Mth.cos(movement) * 0.2F + offset;
        this.legLeftFront1.zRot = (float) (-Math.PI / 3.0F);
        this.legLeftFront2.yRot = Mth.cos(movement) * 0.5F + offset;
        this.legLeftFront2.zRot = (float) (2 * Math.PI / 3.0F);
        offset = 0.05F;
        movement += increment;
        this.legLeftMidFront1.yRot = Mth.cos(movement) * 0.2F + offset;
        this.legLeftMidFront1.zRot = (float) (-Math.PI / 3.0F);
        this.legLeftMidFront2.yRot = Mth.cos(movement) * 0.5F + offset;
        this.legLeftMidFront2.zRot = (float) (2 * Math.PI / 3.0F);
        offset = -0.1F;
        movement += increment;
        this.legLeftMidBack1.yRot = Mth.cos(movement) * 0.2F + offset;
        this.legLeftMidBack1.zRot = (float) (-Math.PI / 3.0F);
        this.legLeftMidBack2.yRot = Mth.cos(movement) * 0.5F + offset;
        this.legLeftMidBack2.zRot = (float) (2 * Math.PI / 3.0F);
        offset = -0.5F;
        movement += increment;
        this.legLeftBack1.yRot = Mth.cos(movement) * 0.2F + offset;
        this.legLeftBack1.zRot = (float) (-Math.PI / 3.0F);
        this.legLeftBack2.yRot = Mth.cos(movement) * 0.5F + offset;
        this.legLeftBack2.zRot = (float) (2 * Math.PI / 3.0F);

        this.copyLegAngles(this.legLeftFront1, this.legLeftFront2);
        this.copyLegAngles(this.legLeftMidFront1, this.legLeftMidFront2);
        this.copyLegAngles(this.legLeftMidBack1, this.legLeftMidBack2);
        this.copyLegAngles(this.legLeftBack1, this.legLeftBack2);
        this.copyLegAngles(this.legRightFront1, this.legRightFront2);
        this.copyLegAngles(this.legRightMidFront1, this.legRightMidFront2);
        this.copyLegAngles(this.legRightMidBack1, this.legRightMidBack2);
        this.copyLegAngles(this.legRightBack1, this.legRightBack2);

        this.copyLeg1LeftToRight(this.legLeftFront1, this.legRightFront1);
        this.copyLeg1LeftToRight(this.legLeftMidFront1, this.legRightMidFront1);
        this.copyLeg1LeftToRight(this.legLeftMidBack1, this.legRightMidBack1);
        this.copyLeg1LeftToRight(this.legLeftBack1, this.legRightBack1);
        this.copyLeftToRight(this.legLeftFront2, this.legRightFront2);
        this.copyLeftToRight(this.legLeftMidFront2, this.legRightMidFront2);
        this.copyLeftToRight(this.legLeftMidBack2, this.legRightMidBack2);
        this.copyLeftToRight(this.legLeftBack2, this.legRightBack2);

        this.tail0.y = -1.5F;
        this.tail0.y = 0.0F;
        //        this.tail0.rotateAngleX = MathHelper.cos(movement) * 0.2F + 0.5205006F;
        this.tail0.xRot = entity.getKillCredit() != null ? 0.52F : 0.1F;
        this.tail0.yRot = 0.0F;
        this.tail0.zRot = 0.0F;
        this.tail1.xRot = entity.getKillCredit() != null ? 2.659407F : 2.7F;
        this.tail1.z = 4 * (Mth.sin(this.tail0.zRot) + Mth.cos(this.tail0.yRot));
        this.tail1.y = 0.5F;
        this.tail1.x = 0.0F;
        this.stinger.xRot = -this.tail0.xRot - (this.tail0.xRot - 2.659407F) - 0.5205006F;
        this.stinger.z = 4 * (Mth.sin(this.tail1.zRot) + Mth.cos(this.tail1.yRot));
        this.stinger.y = 0.5F;
        this.stinger.x = 0.0F;
    }
}