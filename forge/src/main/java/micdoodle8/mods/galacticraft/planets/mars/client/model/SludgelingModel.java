package micdoodle8.mods.galacticraft.planets.mars.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import micdoodle8.mods.galacticraft.planets.mars.entities.SludgelingEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class SludgelingModel extends EntityModel<SludgelingEntity>
{
    ModelPart tail4;
    ModelPart body;
    ModelPart tail1;
    ModelPart tail2;
    ModelPart tail3;

    public SludgelingModel()
    {
        this.texWidth = 64;
        this.texHeight = 32;

        this.tail4 = new ModelPart(this, 0, 0);
        this.tail4.addBox(-0.5F, 0.3F, 4.5F, 1, 1, 1);
        this.tail4.setPos(0F, 23.5F, -2F);
        this.tail4.setTexSize(64, 32);
        this.tail4.mirror = true;
        this.setRotation(this.tail4, 0F, 0F, 0F);
        this.body = new ModelPart(this, 4, 0);
        this.body.addBox(-1F, -0.5F, -1.5F, 2, 1, 3);
        this.body.setPos(0F, 23.5F, -2F);
        this.body.setTexSize(64, 32);
        this.body.mirror = true;
        this.setRotation(this.body, 0F, 0F, 0F);
        this.tail1 = new ModelPart(this, 0, 0);
        this.tail1.addBox(-0.5F, -0.3F, 1.5F, 1, 1, 1);
        this.tail1.setPos(0F, 23.5F, -2F);
        this.tail1.setTexSize(64, 32);
        this.tail1.mirror = true;
        this.setRotation(this.tail1, 0F, 0F, 0F);
        this.tail2 = new ModelPart(this, 0, 0);
        this.tail2.addBox(-0.5F, -0.1F, 2.5F, 1, 1, 1);
        this.tail2.setPos(0F, 23.5F, -2F);
        this.tail2.setTexSize(64, 32);
        this.tail2.mirror = true;
        this.setRotation(this.tail2, 0F, 0F, 0F);
        this.tail3 = new ModelPart(this, 0, 0);
        this.tail3.addBox(-0.5F, 0.1F, 3.5F, 1, 1, 1);
        this.tail3.setPos(0F, 23.5F, -2F);
        this.tail3.setTexSize(64, 32);
        this.tail3.mirror = true;
        this.setRotation(this.tail3, 0F, 0F, 0F);
    }

    private void setRotation(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void setRotationAngles(SludgelingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.tail1.yRot = Mth.cos(ageInTicks * 0.3F + 0 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.025F * (1 + Math.abs(0 - 2));
        this.tail2.yRot = Mth.cos(ageInTicks * 0.3F + 1 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.025F * (1 + Math.abs(1 - 2));
        this.tail3.yRot = Mth.cos(ageInTicks * 0.3F + 2 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.025F * (1 + Math.abs(1 - 2));
        this.tail4.yRot = Mth.cos(ageInTicks * 0.3F + 3 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.025F * (1 + Math.abs(1 - 2));
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
        this.tail1.render(matrixStack, buffer, packedLight, packedOverlay);
        this.tail2.render(matrixStack, buffer, packedLight, packedOverlay);
        this.tail3.render(matrixStack, buffer, packedLight, packedOverlay);
        this.tail4.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}