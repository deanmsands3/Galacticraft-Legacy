package team.galacticraft.galacticraft.common.core.client.model;

import com.google.common.collect.ImmutableList;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.entities.EntityEvolvedCreeper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class ModelEvolvedCreeper extends AgeableListModel<EntityEvolvedCreeper>
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
//    ModelRenderer tubeLeft8;
    ModelPart oxygenMask;
    public ModelPart head;
    public ModelPart body;
    public ModelPart leg1;
    public ModelPart leg2;
    public ModelPart leg3;
    public ModelPart leg4;

    public ModelEvolvedCreeper()
    {
        this(0.0F);
    }

    public ModelEvolvedCreeper(float par1)
    {
        this.texWidth = 128;
        this.texHeight = 64;
        this.leftOxygenTank = new ModelPart(this, 40, 20);
        this.leftOxygenTank.addBox(-1.5F, 0F, -1.5F, 3, 7, 3, par1);
        this.leftOxygenTank.setPos(2F, 5F, 3.8F);
        this.leftOxygenTank.setTexSize(128, 64);
        this.leftOxygenTank.mirror = true;
        this.setRotation(this.leftOxygenTank, 0F, 0F, 0F);
        this.rightOxygenTank = new ModelPart(this, 40, 20);
        this.rightOxygenTank.addBox(-1.5F, 0F, -1.5F, 3, 7, 3, par1);
        this.rightOxygenTank.setPos(-2F, 5F, 3.8F);
        this.rightOxygenTank.setTexSize(128, 64);
        this.rightOxygenTank.mirror = true;
        this.setRotation(this.rightOxygenTank, 0F, 0F, 0F);
        this.tubeRight2 = new ModelPart(this, 40, 30);
        this.tubeRight2.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight2.setPos(-2F, 5F, 6.8F);
        this.tubeRight2.setTexSize(128, 64);
        this.tubeRight2.mirror = true;
        this.setRotation(this.tubeRight2, 0F, 0F, 0F);
        this.tubeLeft1 = new ModelPart(this, 40, 30);
        this.tubeLeft1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft1.setPos(2F, 6F, 5.8F);
        this.tubeLeft1.setTexSize(128, 64);
        this.tubeLeft1.mirror = true;
        this.setRotation(this.tubeLeft1, 0F, 0F, 0F);
        this.tubeRight3 = new ModelPart(this, 40, 30);
        this.tubeRight3.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight3.setPos(-2F, 4F, 6.8F);
        this.tubeRight3.setTexSize(128, 64);
        this.tubeRight3.mirror = true;
        this.setRotation(this.tubeRight3, 0F, 0F, 0F);
        this.tubeRight4 = new ModelPart(this, 40, 30);
        this.tubeRight4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight4.setPos(-2F, 3F, 6.8F);
        this.tubeRight4.setTexSize(128, 64);
        this.tubeRight4.mirror = true;
        this.setRotation(this.tubeRight4, 0F, 0F, 0F);
        this.tubeRight5 = new ModelPart(this, 40, 30);
        this.tubeRight5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight5.setPos(-2F, 2F, 6.8F);
        this.tubeRight5.setTexSize(128, 64);
        this.tubeRight5.mirror = true;
        this.setRotation(this.tubeRight5, 0F, 0F, 0F);
        this.tubeLeft6 = new ModelPart(this, 40, 30);
        this.tubeLeft6.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft6.setPos(2F, 1F, 5.8F);
        this.tubeLeft6.setTexSize(128, 64);
        this.tubeLeft6.mirror = true;
        this.setRotation(this.tubeLeft6, 0F, 0F, 0F);
        this.tubeRight7 = new ModelPart(this, 40, 30);
        this.tubeRight7.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight7.setPos(-2F, 0F, 4.8F);
        this.tubeRight7.setTexSize(128, 64);
        this.tubeRight7.mirror = true;
        this.setRotation(this.tubeRight7, 0F, 0F, 0F);
        this.tubeRight1 = new ModelPart(this, 40, 30);
        this.tubeRight1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight1.setPos(-2F, 6F, 5.8F);
        this.tubeRight1.setTexSize(128, 64);
        this.tubeRight1.mirror = true;
        this.setRotation(this.tubeRight1, 0F, 0F, 0F);
        this.tubeLeft2 = new ModelPart(this, 40, 30);
        this.tubeLeft2.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft2.setPos(2F, 5F, 6.8F);
        this.tubeLeft2.setTexSize(128, 64);
        this.tubeLeft2.mirror = true;
        this.setRotation(this.tubeLeft2, 0F, 0F, 0F);
        this.tubeLeft3 = new ModelPart(this, 40, 30);
        this.tubeLeft3.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft3.setPos(2F, 4F, 6.8F);
        this.tubeLeft3.setTexSize(128, 64);
        this.tubeLeft3.mirror = true;
        this.setRotation(this.tubeLeft3, 0F, 0F, 0F);
        this.tubeLeft4 = new ModelPart(this, 40, 30);
        this.tubeLeft4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft4.setPos(2F, 3F, 6.8F);
        this.tubeLeft4.setTexSize(128, 64);
        this.tubeLeft4.mirror = true;
        this.setRotation(this.tubeLeft4, 0F, 0F, 0F);
        this.tubeLeft5 = new ModelPart(this, 40, 30);
        this.tubeLeft5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft5.setPos(2F, 2F, 6.8F);
        this.tubeLeft5.setTexSize(128, 64);
        this.tubeLeft5.mirror = true;
        this.setRotation(this.tubeLeft5, 0F, 0F, 0F);
        this.tubeLeft7 = new ModelPart(this, 40, 30);
        this.tubeLeft7.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeLeft7.setPos(2F, 0F, 4.8F);
        this.tubeLeft7.setTexSize(128, 64);
        this.tubeLeft7.mirror = true;
        this.setRotation(this.tubeLeft7, 0F, 0F, 0F);
        this.tubeRight6 = new ModelPart(this, 40, 30);
        this.tubeRight6.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
        this.tubeRight6.setPos(-2F, 1F, 5.8F);
        this.tubeRight6.setTexSize(128, 64);
        this.tubeRight6.mirror = true;
        this.setRotation(this.tubeRight6, 0F, 0F, 0F);
//        this.tubeLeft8 = new ModelRenderer(this, 40, 30);
//        this.tubeLeft8.addBox(0F, 0F, 0F, 1, 1, 1, par1);
//        this.tubeLeft8.setRotationPoint(0F, -2F, 0F);
//        this.tubeLeft8.setTextureSize(128, 64);
//        this.tubeLeft8.mirror = true;
//        this.setRotation(this.tubeLeft8, 0F, 0F, 0F);
        this.oxygenMask = new ModelPart(this, 40, 0);
        this.oxygenMask.addBox(-5F, -9F, -5F, 10, 10, 10, par1);
        this.oxygenMask.setPos(0F, 4F, 0F);
        this.oxygenMask.setTexSize(128, 64);
        this.oxygenMask.mirror = true;
        this.setRotation(this.oxygenMask, 0F, 0F, 0F);

        final byte var2 = 4;
        this.head = new ModelPart(this, 0, 0);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1);
        this.head.setPos(0.0F, var2, 0.0F);
        this.head.setTexSize(128, 64);
        this.body = new ModelPart(this, 16, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, par1);
        this.body.setPos(0.0F, var2, 0.0F);
        this.body.setTexSize(128, 64);
        this.leg1 = new ModelPart(this, 0, 16);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);
        this.leg1.setPos(-2.0F, 12 + var2, 4.0F);
        this.leg1.setTexSize(128, 64);
        this.leg2 = new ModelPart(this, 0, 16);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);
        this.leg2.setPos(2.0F, 12 + var2, 4.0F);
        this.leg2.setTexSize(128, 64);
        this.leg3 = new ModelPart(this, 0, 16);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);
        this.leg3.setPos(-2.0F, 12 + var2, -4.0F);
        this.leg3.setTexSize(128, 64);
        this.leg4 = new ModelPart(this, 0, 16);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);
        this.leg4.setPos(2.0F, 12 + var2, -4.0F);
        this.leg4.setTexSize(128, 64);
    }

    private void setRotation(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    protected Iterable<ModelPart> headParts()
    {
        return ImmutableList.of(head, oxygenMask);
    }

    @Override
    protected Iterable<ModelPart> bodyParts()
    {
        return ImmutableList.of(leftOxygenTank, rightOxygenTank, tubeLeft1, tubeLeft2, tubeLeft3, tubeLeft4, tubeLeft5, tubeLeft6, tubeLeft7, tubeRight1, tubeRight2, tubeRight3, tubeRight4, tubeRight5, tubeRight6, tubeRight7, body, leg1, leg2, leg3, leg4);
    }

//    @Override
////    public void render(EntityEvolvedCreeper entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
//    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
//    {
//        this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
////        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
//
//        if (this.isChild)
//        {
//            float f6 = 2.0F;
//            GL11.glPushMatrix();
//            GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
//            GL11.glTranslatef(0.0F, 16.0F * scale, 0.0F);
//            this.head.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.oxygenMask.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            GL11.glPopMatrix();
//            GL11.glPushMatrix();
//            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
//            GL11.glTranslatef(0.0F, 24.0F * scale, 0.0F);
//            this.leftOxygenTank.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.rightOxygenTank.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft6.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight7.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft7.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight6.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft8.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.leg1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.leg2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.leg3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.leg4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            GL11.glPopMatrix();
//        }
//        else
//        {
//            this.leftOxygenTank.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.rightOxygenTank.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft6.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight7.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft7.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeRight6.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.tubeLeft8.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.oxygenMask.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.head.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.leg1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.leg2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.leg3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//            this.leg4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
//        }
//    }

    @Override
    public void setRotationAngles(EntityEvolvedCreeper entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.oxygenMask.yRot = netHeadYaw / Constants.RADIANS_TO_DEGREES;
        this.oxygenMask.xRot = headPitch / Constants.RADIANS_TO_DEGREES;
        this.head.yRot = netHeadYaw / Constants.RADIANS_TO_DEGREES;
        this.head.xRot = headPitch / Constants.RADIANS_TO_DEGREES;
        this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
        this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
        this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

//    @Override
//    public void setRotationAngles(EntityEvolvedCreeper entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
//    {
//        this.oxygenMask.rotateAngleY = netHeadYaw / Constants.RADIANS_TO_DEGREES;
//        this.oxygenMask.rotateAngleX = headPitch / Constants.RADIANS_TO_DEGREES;
//        this.head.rotateAngleY = netHeadYaw / Constants.RADIANS_TO_DEGREES;
//        this.head.rotateAngleX = headPitch / Constants.RADIANS_TO_DEGREES;
//        this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
//        this.leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
//        this.leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
//        this.leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
//    }
}
