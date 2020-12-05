package micdoodle8.mods.galacticraft.core.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteorChunk;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

public class ModelMeteorChunk extends EntityModel<EntityMeteorChunk>
{
    private final ModelPart[] boxes = new ModelPart[6];

    public ModelMeteorChunk()
    {
        texWidth = 16;
        texHeight = 16;

        boxes[0] = new ModelPart(this, 0, 0);
        boxes[0].addBox(0F, 0F, 0F, 1, 8, 6);
        boxes[0].setPos(4F, 0.0F, -5F);
        boxes[0].setTexSize(16, 16);
        setRotation(boxes[0], -0F, -0F, -0F);
        boxes[0].mirror = false;
        boxes[1] = new ModelPart(this, 0, 0);
        boxes[1].addBox(0F, 0F, 0F, 5, 8, 1);
        boxes[1].setPos(-3F, 1.0F, -7F);
        boxes[1].setTexSize(16, 16);
        setRotation(boxes[1], -0F, -0F, -0F);
        boxes[1].mirror = false;
        boxes[2] = new ModelPart(this, 0, 0);
        boxes[2].addBox(0F, 0F, 0F, 1, 8, 6);
        boxes[2].setPos(-6F, -1.0F, -5F);
        boxes[2].setTexSize(16, 16);
        setRotation(boxes[2], -0F, -0F, -0F);
        boxes[2].mirror = false;
        boxes[3] = new ModelPart(this, 0, 0);
        boxes[3].addBox(0F, 0F, 0F, 6, 1, 5);
        boxes[3].setPos(-3F, -3.0F, -4F);
        boxes[3].setTexSize(16, 16);
        setRotation(boxes[3], -0F, -0F, -0F);
        boxes[3].mirror = false;
        boxes[4] = new ModelPart(this, 0, 0);
        boxes[4].addBox(0F, 0F, 0F, 5, 8, 1);
        boxes[4].setPos(-3F, 0.0F, 3F);
        boxes[4].setTexSize(16, 16);
        setRotation(boxes[4], -0F, -0F, -0F);
        boxes[4].mirror = false;
        boxes[5] = new ModelPart(this, 0, 0);
        boxes[5].addBox(0F, 0F, 0F, 9, 12, 9);
        boxes[5].setPos(-5F, -2.0F, -6F);
        boxes[5].setTexSize(16, 16);
        setRotation(boxes[5], -0F, -0F, -0F);
        boxes[5].mirror = false;
    }

    @Override
    public void setRotationAngles(EntityMeteorChunk entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {

    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        for (ModelPart mr : boxes)
        {
            mr.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        }
    }

    private void setRotation(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}
