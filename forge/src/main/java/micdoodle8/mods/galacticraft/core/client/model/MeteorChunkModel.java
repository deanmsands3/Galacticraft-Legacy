package micdoodle8.mods.galacticraft.core.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import micdoodle8.mods.galacticraft.core.entities.MeteorChunkEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

public class MeteorChunkModel extends EntityModel<MeteorChunkEntity>
{
    private final ModelPart[] boxes = new ModelPart[6];

    public MeteorChunkModel()
    {
        this.texWidth = 16;
        this.texHeight = 16;

        this.boxes[0] = new ModelPart(this, 0, 0);
        this.boxes[0].addBox(0F, 0F, 0F, 1, 8, 6);
        this.boxes[0].setPos(4F, 0.0F, -5F);
        this.boxes[0].setTexSize(16, 16);
        this.setRotation(this.boxes[0], -0F, -0F, -0F);
        this.boxes[0].mirror = false;
        this.boxes[1] = new ModelPart(this, 0, 0);
        this.boxes[1].addBox(0F, 0F, 0F, 5, 8, 1);
        this.boxes[1].setPos(-3F, 1.0F, -7F);
        this.boxes[1].setTexSize(16, 16);
        this.setRotation(this.boxes[1], -0F, -0F, -0F);
        this.boxes[1].mirror = false;
        this.boxes[2] = new ModelPart(this, 0, 0);
        this.boxes[2].addBox(0F, 0F, 0F, 1, 8, 6);
        this.boxes[2].setPos(-6F, -1.0F, -5F);
        this.boxes[2].setTexSize(16, 16);
        this.setRotation(this.boxes[2], -0F, -0F, -0F);
        this.boxes[2].mirror = false;
        this.boxes[3] = new ModelPart(this, 0, 0);
        this.boxes[3].addBox(0F, 0F, 0F, 6, 1, 5);
        this.boxes[3].setPos(-3F, -3.0F, -4F);
        this.boxes[3].setTexSize(16, 16);
        this.setRotation(this.boxes[3], -0F, -0F, -0F);
        this.boxes[3].mirror = false;
        this.boxes[4] = new ModelPart(this, 0, 0);
        this.boxes[4].addBox(0F, 0F, 0F, 5, 8, 1);
        this.boxes[4].setPos(-3F, 0.0F, 3F);
        this.boxes[4].setTexSize(16, 16);
        this.setRotation(this.boxes[4], -0F, -0F, -0F);
        this.boxes[4].mirror = false;
        this.boxes[5] = new ModelPart(this, 0, 0);
        this.boxes[5].addBox(0F, 0F, 0F, 9, 12, 9);
        this.boxes[5].setPos(-5F, -2.0F, -6F);
        this.boxes[5].setTexSize(16, 16);
        this.setRotation(this.boxes[5], -0F, -0F, -0F);
        this.boxes[5].mirror = false;
    }

    @Override
    public void setRotationAngles(MeteorChunkEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        for (ModelPart mr : this.boxes)
        {
            mr.render(matrixStack, buffer, packedLight, packedOverlay);
        }
    }

    private void setRotation(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}