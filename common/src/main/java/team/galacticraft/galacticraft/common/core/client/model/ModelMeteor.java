package team.galacticraft.galacticraft.common.core.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import team.galacticraft.galacticraft.common.core.entities.EntityMeteor;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

public class ModelMeteor extends EntityModel<EntityMeteor>
{
    ModelPart[] shapes = new ModelPart[13];

    public ModelMeteor()
    {
        this.texWidth = 128;
        this.texHeight = 64;

        this.shapes[0] = new ModelPart(this, 0, 0);
        this.shapes[0].addBox(0F, -7F, -13F, 2, 4, 4);
        this.shapes[0].setPos(0F, 0F, 0F);
        this.shapes[0].setTexSize(128, 64);
        this.shapes[0].mirror = true;
        this.setRotation(this.shapes[0], 0F, 0F, 0F);
        this.shapes[1] = new ModelPart(this, 0, 0);
        this.shapes[1].addBox(-10F, -10F, -10F, 20, 20, 20);
        this.shapes[1].setPos(0F, 0F, 0F);
        this.shapes[1].setTexSize(128, 64);
        this.shapes[1].mirror = true;
        this.setRotation(this.shapes[1], 0F, 0F, 0F);
        this.shapes[2] = new ModelPart(this, 0, 0);
        this.shapes[2].addBox(-5F, -8F, -12F, 5, 9, 1);
        this.shapes[2].setPos(0F, 0F, 0F);
        this.shapes[2].setTexSize(128, 64);
        this.shapes[2].mirror = true;
        this.setRotation(this.shapes[2], 0F, 0F, 0F);
        this.shapes[3] = new ModelPart(this, 0, 0);
        this.shapes[3].addBox(0F, -6F, 11F, 4, 13, 1);
        this.shapes[3].setPos(0F, 0F, 0F);
        this.shapes[3].setTexSize(128, 64);
        this.shapes[3].mirror = true;
        this.setRotation(this.shapes[3], 0F, 0F, 0F);
        this.shapes[4] = new ModelPart(this, 0, 0);
        this.shapes[4].addBox(-9F, 10F, -9F, 18, 1, 18);
        this.shapes[4].setPos(0F, 0F, 0F);
        this.shapes[4].setTexSize(128, 64);
        this.shapes[4].mirror = true;
        this.setRotation(this.shapes[4], 0F, 0F, 0F);
        this.shapes[5] = new ModelPart(this, 0, 0);
        this.shapes[5].addBox(11F, 3F, -8F, 1, 5, 5);
        this.shapes[5].setPos(0F, 0F, 0F);
        this.shapes[5].setTexSize(128, 64);
        this.shapes[5].mirror = true;
        this.setRotation(this.shapes[5], 0F, 0F, 0F);
        this.shapes[6] = new ModelPart(this, 0, 0);
        this.shapes[6].addBox(-7F, -8F, 10F, 7, 12, 2);
        this.shapes[6].setPos(0F, 0F, 0F);
        this.shapes[6].setTexSize(128, 64);
        this.shapes[6].mirror = true;
        this.setRotation(this.shapes[6], 0F, 0F, 0F);
        this.shapes[7] = new ModelPart(this, 0, 0);
        this.shapes[7].addBox(-9F, -9F, 10F, 18, 18, 1);
        this.shapes[7].setPos(0F, 0F, 0F);
        this.shapes[7].setTexSize(128, 64);
        this.shapes[7].mirror = true;
        this.setRotation(this.shapes[7], 0F, 0F, 0F);
        this.shapes[8] = new ModelPart(this, 0, 0);
        this.shapes[8].addBox(-11F, -9F, -9F, 1, 18, 18);
        this.shapes[8].setPos(0F, 0F, 0F);
        this.shapes[8].setTexSize(128, 64);
        this.shapes[8].mirror = true;
        this.setRotation(this.shapes[8], 0F, 0F, 0F);
        this.shapes[9] = new ModelPart(this, 0, 0);
        this.shapes[9].addBox(10F, -9F, -9F, 1, 18, 18);
        this.shapes[9].setPos(0F, 0F, 0F);
        this.shapes[9].setTexSize(128, 64);
        this.shapes[9].mirror = true;
        this.setRotation(this.shapes[9], 0F, 0F, 0F);
        this.shapes[10] = new ModelPart(this, 0, 0);
        this.shapes[10].addBox(-9F, -9F, -11F, 18, 18, 1);
        this.shapes[10].setPos(0F, 0F, 0F);
        this.shapes[10].setTexSize(128, 64);
        this.shapes[10].mirror = true;
        this.setRotation(this.shapes[10], 0F, 0F, 0F);
        this.shapes[11] = new ModelPart(this, 0, 0);
        this.shapes[11].addBox(-9F, -9F, -11F, 18, 18, 1);
        this.shapes[11].setPos(0F, 0F, 0F);
        this.shapes[11].setTexSize(128, 64);
        this.shapes[11].mirror = true;
        this.setRotation(this.shapes[11], 0F, 0F, 0F);
        this.shapes[12] = new ModelPart(this, 0, 0);
        this.shapes[12].addBox(-9F, -11F, -9F, 18, 1, 18);
        this.shapes[12].setPos(0F, 0F, 0F);
        this.shapes[12].setTexSize(128, 64);
        this.shapes[12].mirror = true;
        this.setRotation(this.shapes[12], 0F, 0F, 0F);
    }

    @Override
    public void setRotationAngles(EntityMeteor entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {

    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        for (ModelPart shape : shapes)
        {
            shape.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        }
    }

//    @Override
//    public void render(EntityMeteor entity, float f, float f1, float f2, float f3, float f4, float f5)
//    {
//        super.render(entity, f, f1, f2, f3, f4, f5);
//        super.setRotationAngles(entity, f, f1, f2, f3, f4, f5);
//
//        for (final ModelRenderer shape : this.shapes)
//        {
//            shape.render(f5);
//        }
//    }
//
//    public void renderBlock(float f)
//    {
//        for (final ModelRenderer shape : this.shapes)
//        {
//            shape.render(f);
//        }
//    }

    private void setRotation(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}
