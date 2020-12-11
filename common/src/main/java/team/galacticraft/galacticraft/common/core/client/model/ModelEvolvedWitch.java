package team.galacticraft.galacticraft.common.core.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import team.galacticraft.galacticraft.common.core.entities.EntityEvolvedWitch;
import net.minecraft.client.model.WitchModel;
import net.minecraft.client.model.geom.ModelPart;

public class ModelEvolvedWitch extends WitchModel<EntityEvolvedWitch>
{
    ModelPart tank1;
    ModelPart tank2;
    ModelPart oxygenMask;
    ModelPart tube1;
    ModelPart tube2;
    ModelPart tube3;
    ModelPart tube4;
    ModelPart tube5;
    ModelPart tube6;
    ModelPart tube7;
    ModelPart tube8;
    ModelPart tube9;
    ModelPart tube10;
    ModelPart tube11;
    ModelPart tube12;
    ModelPart tube13;
    ModelPart tube14;
    ModelPart tube15;
    ModelPart tube16;
    ModelPart tube17;
    ModelPart tube18;

    public ModelEvolvedWitch()
    {
        super(0.0F);
        this.texWidth = 64;
        this.texHeight = 128;

        this.tank1 = new ModelPart(this, 52, 66);
        this.tank1.addBox(0F, 0F, 0F, 3, 7, 3);
        this.tank1.setPos(0F, 4F, 3F);

        this.tank2 = new ModelPart(this, 52, 66);
        this.tank2.addBox(0F, 0F, 0F, 3, 7, 3);
        this.tank2.setPos(-3F, 4F, 3F);

        this.oxygenMask = new ModelPart(this, 24, 90);
        this.oxygenMask.addBox(-5F, -9F, -5F, 10, 10, 10);
        this.oxygenMask.setPos(0F, 1F, 0F);

        this.tube1 = new ModelPart(this, 60, 76);
        this.tube1.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube1.setPos(1F, 4.5F, 6F);

        this.tube2 = new ModelPart(this, 60, 76);
        this.tube2.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube2.setPos(1F, 3.5F, 7F);

        this.tube3 = new ModelPart(this, 60, 76);
        this.tube3.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube3.setPos(1F, 2.5F, 7.5F);

        this.tube4 = new ModelPart(this, 60, 76);
        this.tube4.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube4.setPos(1F, 1.5F, 7.5F);

        this.tube5 = new ModelPart(this, 60, 76);
        this.tube5.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube5.setPos(1F, 0.5F, 7.5F);

        this.tube6 = new ModelPart(this, 60, 76);
        this.tube6.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube6.setPos(1F, -0.5F, 7F);

        this.tube7 = new ModelPart(this, 60, 76);
        this.tube7.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube7.setPos(1F, -1.5F, 6F);

        this.tube8 = new ModelPart(this, 60, 76);
        this.tube8.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube8.setPos(1F, -2.5F, 5F);

        this.tube9 = new ModelPart(this, 60, 76);
        this.tube9.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube9.setPos(1F, -3F, 4F);

        this.tube10 = new ModelPart(this, 60, 76);
        this.tube10.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube10.setPos(-2F, 4.5F, 6F);

        this.tube11 = new ModelPart(this, 60, 76);
        this.tube11.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube11.setPos(-2F, 3.5F, 7F);

        this.tube12 = new ModelPart(this, 60, 76);
        this.tube12.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube12.setPos(-2F, 2.5F, 7.5F);

        this.tube13 = new ModelPart(this, 60, 76);
        this.tube13.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube13.setPos(-2F, 1.5F, 7.5F);

        this.tube14 = new ModelPart(this, 60, 76);
        this.tube14.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube14.setPos(-2F, 0.5F, 7.5F);

        this.tube15 = new ModelPart(this, 60, 76);
        this.tube15.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube15.setPos(-2F, -0.5F, 7F);

        this.tube16 = new ModelPart(this, 60, 76);
        this.tube16.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube16.setPos(-2F, -1.5F, 6F);

        this.tube17 = new ModelPart(this, 60, 76);
        this.tube17.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube17.setPos(-2F, -2.5F, 5F);

        this.tube18 = new ModelPart(this, 60, 76);
        this.tube18.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube18.setPos(-2F, -3F, 4F);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tank1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tank2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.oxygenMask.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube6.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube7.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube8.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube9.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube10.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube11.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube12.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube13.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube14.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube15.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube16.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube17.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube18.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    @Override
    public void setRotationAngles(EntityEvolvedWitch entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.oxygenMask.yRot = this.head.yRot;
        this.oxygenMask.xRot = this.head.xRot;
    }
}