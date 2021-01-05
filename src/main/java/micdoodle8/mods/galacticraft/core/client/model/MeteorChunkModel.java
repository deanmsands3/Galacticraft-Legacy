package micdoodle8.mods.galacticraft.core.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import micdoodle8.mods.galacticraft.core.entities.MeteorChunkEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class MeteorChunkModel extends EntityModel<MeteorChunkEntity>
{
    private final ModelRenderer[] boxes = new ModelRenderer[6];

    public MeteorChunkModel()
    {
        this.textureWidth = 16;
        this.textureHeight = 16;

        this.boxes[0] = new ModelRenderer(this, 0, 0);
        this.boxes[0].addBox(0F, 0F, 0F, 1, 8, 6);
        this.boxes[0].setRotationPoint(4F, 0.0F, -5F);
        this.boxes[0].setTextureSize(16, 16);
        this.setRotation(this.boxes[0], -0F, -0F, -0F);
        this.boxes[0].mirror = false;
        this.boxes[1] = new ModelRenderer(this, 0, 0);
        this.boxes[1].addBox(0F, 0F, 0F, 5, 8, 1);
        this.boxes[1].setRotationPoint(-3F, 1.0F, -7F);
        this.boxes[1].setTextureSize(16, 16);
        this.setRotation(this.boxes[1], -0F, -0F, -0F);
        this.boxes[1].mirror = false;
        this.boxes[2] = new ModelRenderer(this, 0, 0);
        this.boxes[2].addBox(0F, 0F, 0F, 1, 8, 6);
        this.boxes[2].setRotationPoint(-6F, -1.0F, -5F);
        this.boxes[2].setTextureSize(16, 16);
        this.setRotation(this.boxes[2], -0F, -0F, -0F);
        this.boxes[2].mirror = false;
        this.boxes[3] = new ModelRenderer(this, 0, 0);
        this.boxes[3].addBox(0F, 0F, 0F, 6, 1, 5);
        this.boxes[3].setRotationPoint(-3F, -3.0F, -4F);
        this.boxes[3].setTextureSize(16, 16);
        this.setRotation(this.boxes[3], -0F, -0F, -0F);
        this.boxes[3].mirror = false;
        this.boxes[4] = new ModelRenderer(this, 0, 0);
        this.boxes[4].addBox(0F, 0F, 0F, 5, 8, 1);
        this.boxes[4].setRotationPoint(-3F, 0.0F, 3F);
        this.boxes[4].setTextureSize(16, 16);
        this.setRotation(this.boxes[4], -0F, -0F, -0F);
        this.boxes[4].mirror = false;
        this.boxes[5] = new ModelRenderer(this, 0, 0);
        this.boxes[5].addBox(0F, 0F, 0F, 9, 12, 9);
        this.boxes[5].setRotationPoint(-5F, -2.0F, -6F);
        this.boxes[5].setTextureSize(16, 16);
        this.setRotation(this.boxes[5], -0F, -0F, -0F);
        this.boxes[5].mirror = false;
    }

    @Override
    public void setRotationAngles(MeteorChunkEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        for (ModelRenderer mr : this.boxes)
        {
            mr.render(matrixStack, buffer, packedLight, packedOverlay);
        }
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}