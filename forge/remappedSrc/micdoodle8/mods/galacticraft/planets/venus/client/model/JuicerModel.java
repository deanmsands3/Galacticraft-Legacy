package micdoodle8.mods.galacticraft.planets.venus.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import micdoodle8.mods.galacticraft.planets.venus.entities.JuicerEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class JuicerModel extends EntityModel<JuicerEntity>
{
    private final ModelRenderer body1;
    private final ModelRenderer body2;
    private final ModelRenderer head;
    private final ModelRenderer legRightFront1;
    private final ModelRenderer legRightFront2;
    private final ModelRenderer legLeftFront1;
    private final ModelRenderer legLeftFront2;
    private final ModelRenderer legRightMidFront1;
    private final ModelRenderer legRightMidFront2;
    private final ModelRenderer legLeftMidFront1;
    private final ModelRenderer legLeftMidFront2;
    private final ModelRenderer legRightMidBack1;
    private final ModelRenderer legRightMidBack2;
    private final ModelRenderer legLeftMidBack1;
    private final ModelRenderer legLeftMidBack2;
    private final ModelRenderer legRightBack1;
    private final ModelRenderer legRightBack2;
    private final ModelRenderer legLeftBack1;
    private final ModelRenderer legLeftBack2;
    private final ModelRenderer back;
    private final ModelRenderer tail0;
    private final ModelRenderer tail1;
    private final ModelRenderer stinger;

    private final float legLength0;

    public JuicerModel()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;

        this.body1 = new ModelRenderer(this, 24, 22);
        this.body1.addBox(-3F, -1F, -4F, 6, 2, 8);
        this.body1.setRotationPoint(0F, 0F, 0F);
        this.body1.setTextureSize(64, 32);
        this.body1.mirror = true;
        this.setRotation(this.body1, 0F, 0F, 0F);
        this.body2 = new ModelRenderer(this, 0, 22);
        this.body2.addBox(-2.5F, -1.5F, -3.5F, 5, 3, 7);
        this.body2.setRotationPoint(0F, 0F, 0F);
        this.body2.setTextureSize(64, 32);
        this.body2.mirror = true;
        this.setRotation(this.body2, 0F, 0F, 0F);

        this.head = new ModelRenderer(this, 52, 0);
        this.head.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
        this.head.setRotationPoint(0F, -1F, -4F);
        this.head.setTextureSize(64, 32);
        this.head.mirror = true;
        this.setRotation(this.head, -0.3717861F, 0F, 0F);

        this.legRightFront1 = new ModelRenderer(this, 44, 0);
        this.legRightFront1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legRightFront1.setRotationPoint(-2.5F, 0F, -3F);
        this.legRightFront1.setTextureSize(64, 32);
        this.legRightFront1.mirror = true;
        this.setRotation(this.legRightFront1, 0F, 2.453788F, 1.115358F);
        this.legRightFront2 = new ModelRenderer(this, 30, 0);
        this.legRightFront2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legRightFront2.setRotationPoint(-3.5F, -3F, -4F);
        this.legRightFront2.setTextureSize(64, 32);
        this.legRightFront2.mirror = true;
        this.setRotation(this.legRightFront2, 0F, 2.825574F, -0.8551081F);

        this.legLeftFront1 = new ModelRenderer(this, 44, 0);
        this.legLeftFront1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legLeftFront1.setRotationPoint(2.5F, 0F, -3F);
        this.legLeftFront1.setTextureSize(64, 32);
        this.legLeftFront1.mirror = true;
        this.setRotation(this.legLeftFront1, 0F, 0.7807508F, -1.115358F);
        this.legLeftFront2 = new ModelRenderer(this, 30, 0);
        this.legLeftFront2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legLeftFront2.setRotationPoint(3.5F, -3F, -4F);
        this.legLeftFront2.setTextureSize(64, 32);
        this.legLeftFront2.mirror = true;
        this.setRotation(this.legLeftFront2, 0F, 0.7063936F, 0.8551081F);

        this.legRightMidFront1 = new ModelRenderer(this, 44, 0);
        this.legRightMidFront1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legRightMidFront1.setRotationPoint(-2.5F, 0F, -1F);
        this.legRightMidFront1.setTextureSize(64, 32);
        this.legRightMidFront1.mirror = true;
        this.setRotation(this.legRightMidFront1, 0F, 2.93711F, 1.115358F);
        this.legRightMidFront2 = new ModelRenderer(this, 30, 0);
        this.legRightMidFront2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legRightMidFront2.setRotationPoint(-3.5F, -3F, -1F);
        this.legRightMidFront2.setTextureSize(64, 32);
        this.legRightMidFront2.mirror = true;
        this.setRotation(this.legRightMidFront2, 0F, 3.011467F, -0.8551081F);

        this.legLeftMidFront1 = new ModelRenderer(this, 44, 0);
        this.legLeftMidFront1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legLeftMidFront1.setRotationPoint(2.5F, 0F, -1F);
        this.legLeftMidFront1.setTextureSize(64, 32);
        this.legLeftMidFront1.mirror = true;
        this.setRotation(this.legLeftMidFront1, 0F, 0.1858931F, -1.115358F);
        this.legLeftMidFront2 = new ModelRenderer(this, 30, 0);
        this.legLeftMidFront2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legLeftMidFront2.setRotationPoint(3.5F, -3F, -1F);
        this.legLeftMidFront2.setTextureSize(64, 32);
        this.legLeftMidFront2.mirror = true;
        this.setRotation(this.legLeftMidFront2, 0F, 0.3346075F, 0.8551081F);

        this.legRightMidBack1 = new ModelRenderer(this, 44, 0);
        this.legRightMidBack1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legRightMidBack1.setRotationPoint(-2.5F, 0F, 1F);
        this.legRightMidBack1.setTextureSize(64, 32);
        this.legRightMidBack1.mirror = true;
        this.setRotation(this.legRightMidBack1, 0F, -3.030057F, 1.115358F);
        this.legRightMidBack2 = new ModelRenderer(this, 30, 0);
        this.legRightMidBack2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legRightMidBack2.setRotationPoint(-3.5F, -3F, 1F);
        this.legRightMidBack2.setTextureSize(64, 32);
        this.legRightMidBack2.mirror = true;
        this.setRotation(this.legRightMidBack2, 0F, -2.974289F, -0.8551081F);

        this.legLeftMidBack1 = new ModelRenderer(this, 44, 0);
        this.legLeftMidBack1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legLeftMidBack1.setRotationPoint(2.5F, 0F, 1F);
        this.legLeftMidBack1.setTextureSize(64, 32);
        this.legLeftMidBack1.mirror = true;
        this.setRotation(this.legLeftMidBack1, 0F, -0.0371786F, -1.115358F);
        this.legLeftMidBack2 = new ModelRenderer(this, 30, 0);
        this.legLeftMidBack2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legLeftMidBack2.setRotationPoint(3.5F, -3F, 1F);
        this.legLeftMidBack2.setTextureSize(64, 32);
        this.legLeftMidBack2.mirror = true;
        this.setRotation(this.legLeftMidBack2, 0F, -0.1487144F, 0.8551081F);

        this.legRightBack1 = new ModelRenderer(this, 44, 0);
        this.legRightBack1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legRightBack1.setRotationPoint(-2.5F, 0F, 3F);
        this.legRightBack1.setTextureSize(64, 32);
        this.legRightBack1.mirror = true;
        this.setRotation(this.legRightBack1, 0F, -2.658271F, 1.115358F);
        this.legRightBack2 = new ModelRenderer(this, 30, 0);
        this.legRightBack2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legRightBack2.setRotationPoint(-3.5F, -3F, 4F);
        this.legRightBack2.setTextureSize(64, 32);
        this.legRightBack2.mirror = true;
        this.setRotation(this.legRightBack2, 0F, -2.788396F, -0.8551081F);

        this.legLeftBack1 = new ModelRenderer(this, 44, 0);
        this.legLeftBack1.addBox(0F, -0.5F, -0.5F, 3, 1, 1);
        this.legLeftBack1.setRotationPoint(2.5F, 0F, 3F);
        this.legLeftBack1.setTextureSize(64, 32);
        this.legLeftBack1.mirror = true;
        this.setRotation(this.legLeftBack1, 0F, -0.3346075F, -1.115358F);
        this.legLeftBack2 = new ModelRenderer(this, 30, 0);
        this.legLeftBack2.addBox(0F, -0.5F, -0.5F, 6, 1, 1);
        this.legLeftBack2.setRotationPoint(3.5F, -3F, 3F);
        this.legLeftBack2.setTextureSize(64, 32);
        this.legLeftBack2.mirror = true;
        this.setRotation(this.legLeftBack2, 0F, -0.5205006F, 0.8551081F);

        this.back = new ModelRenderer(this, 0, 16);
        this.back.addBox(-1.5F, -0.5F, -2.5F, 3, 1, 5);
        this.back.setRotationPoint(0F, -1.5F, 0F);
        this.back.setTextureSize(64, 32);
        this.back.mirror = true;
        this.setRotation(this.back, 0F, 0F, 0F);

        this.tail0 = new ModelRenderer(this, 42, 17);
        this.tail0.addBox(-0.5F, -0.5F, 0F, 1, 1, 4);
        this.tail0.setRotationPoint(0F, -2F, 0F);
        this.tail0.setTextureSize(64, 32);
        this.tail0.mirror = true;
        this.setRotation(this.tail0, 0.5205006F, 0F, 0F);
        this.tail1 = new ModelRenderer(this, 42, 12);
        this.tail1.addBox(-0.5F, 0F, 0F, 1, 1, 4);
        this.tail1.setRotationPoint(0F, -3F, 3F);
        this.tail1.setTextureSize(64, 32);
        this.tail1.mirror = true;
        this.setRotation(this.tail1, 2.659407F, 0F, 0F);
        this.stinger = new ModelRenderer(this, 48, 9);
        this.stinger.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.stinger.setRotationPoint(0F, -5.2F, -1.133333F);
        this.stinger.setTextureSize(64, 32);
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

        this.legLength0 = this.legLeftFront1.cubeList.get(0).posX2 - this.legLeftFront1.cubeList.get(0).posX1;
    }

    private void convertToChild(ModelRenderer parent, ModelRenderer child)
    {
        // move child rotation point to be relative to parent
        child.rotationPointX -= parent.rotationPointX;
        child.rotationPointY -= parent.rotationPointY;
        child.rotationPointZ -= parent.rotationPointZ;
        // make rotations relative to parent
        child.rotateAngleX -= parent.rotateAngleX;
        child.rotateAngleY -= parent.rotateAngleY;
        child.rotateAngleZ -= parent.rotateAngleZ;
        // create relationship
        parent.addChild(child);
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

    private void copyLegAngles(ModelRenderer parent, ModelRenderer child)
    {
        child.rotationPointX = this.legLength0 * (parent.rotationPointX < 0.0F ? -1.0F : 1.0F) * (MathHelper.sin(parent.rotateAngleX) + MathHelper.cos(parent.rotateAngleY));
        child.rotationPointY = 0.0F;
        child.rotationPointZ = 0.0F;
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
        right.rotateAngleZ = -left.rotateAngleZ;
    }

    @Override
    public void setRotationAngles(JuicerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        float movement = limbSwing;
        float increment = -1.0F;
        float offset = 0.5F;
        this.legLeftFront1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.legLeftFront1.rotateAngleZ = (float) (-Math.PI / 3.0F);
        this.legLeftFront2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
        this.legLeftFront2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
        offset = 0.05F;
        movement += increment;
        this.legLeftMidFront1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.legLeftMidFront1.rotateAngleZ = (float) (-Math.PI / 3.0F);
        this.legLeftMidFront2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
        this.legLeftMidFront2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
        offset = -0.1F;
        movement += increment;
        this.legLeftMidBack1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.legLeftMidBack1.rotateAngleZ = (float) (-Math.PI / 3.0F);
        this.legLeftMidBack2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
        this.legLeftMidBack2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);
        offset = -0.5F;
        movement += increment;
        this.legLeftBack1.rotateAngleY = MathHelper.cos(movement) * 0.2F + offset;
        this.legLeftBack1.rotateAngleZ = (float) (-Math.PI / 3.0F);
        this.legLeftBack2.rotateAngleY = MathHelper.cos(movement) * 0.5F + offset;
        this.legLeftBack2.rotateAngleZ = (float) (2 * Math.PI / 3.0F);

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

        this.tail0.rotationPointY = -1.5F;
        this.tail0.rotationPointY = 0.0F;
        //        this.tail0.rotateAngleX = MathHelper.cos(movement) * 0.2F + 0.5205006F;
        this.tail0.rotateAngleX = entity.getAttackingEntity() != null ? 0.52F : 0.1F;
        this.tail0.rotateAngleY = 0.0F;
        this.tail0.rotateAngleZ = 0.0F;
        this.tail1.rotateAngleX = entity.getAttackingEntity() != null ? 2.659407F : 2.7F;
        this.tail1.rotationPointZ = 4 * (MathHelper.sin(this.tail0.rotateAngleZ) + MathHelper.cos(this.tail0.rotateAngleY));
        this.tail1.rotationPointY = 0.5F;
        this.tail1.rotationPointX = 0.0F;
        this.stinger.rotateAngleX = -this.tail0.rotateAngleX - (this.tail0.rotateAngleX - 2.659407F) - 0.5205006F;
        this.stinger.rotationPointZ = 4 * (MathHelper.sin(this.tail1.rotateAngleZ) + MathHelper.cos(this.tail1.rotateAngleY));
        this.stinger.rotationPointY = 0.5F;
        this.stinger.rotationPointX = 0.0F;
    }
}