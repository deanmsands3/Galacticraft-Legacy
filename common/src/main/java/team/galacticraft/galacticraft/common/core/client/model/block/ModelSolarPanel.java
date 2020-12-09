package team.galacticraft.galacticraft.common.core.client.model.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;

public class ModelSolarPanel extends Model
{
    ModelPart panelMain;
    ModelPart sideHorizontal0;
    ModelPart sideVertical0;
    ModelPart sideVertical2;
    ModelPart sideVertical1;
    ModelPart sideHorizontal1;
    ModelPart sideHorizontal3;
    ModelPart sideHorizontal2;
    ModelPart pole;

    public ModelSolarPanel()
    {
        super(RenderType::entitySolid);
        this.texWidth = 256;
        this.texHeight = 128;
        this.panelMain = new ModelPart(this, 0, 0);
        this.panelMain.addBox(-23F, -0.5F, -23F, 46, 1, 46);
        this.panelMain.setPos(0F, 0F, 0F);
        this.panelMain.setTexSize(256, 128);
        this.panelMain.mirror = true;
        this.setRotation(this.panelMain, 0F, 0F, 0F);
        this.sideHorizontal0 = new ModelPart(this, 0, 48);
        this.sideHorizontal0.addBox(-24F, -1.111F, -23F, 1, 1, 46);
        this.sideHorizontal0.setPos(0F, 0F, 0F);
        this.sideHorizontal0.setTexSize(256, 128);
        this.sideHorizontal0.mirror = true;
        this.setRotation(this.sideHorizontal0, 0F, 0F, 0F);
        this.sideVertical0 = new ModelPart(this, 94, 48);
        this.sideVertical0.addBox(-24F, -1.1F, 23F, 48, 1, 1);
        this.sideVertical0.setPos(0F, 0F, 0F);
        this.sideVertical0.setTexSize(256, 128);
        this.sideVertical0.mirror = true;
        this.setRotation(this.sideVertical0, 0F, 0F, 0F);
        this.sideVertical2 = new ModelPart(this, 94, 48);
        this.sideVertical2.addBox(-24F, -1.1F, -24F, 48, 1, 1);
        this.sideVertical2.setPos(0F, 0F, 0F);
        this.sideVertical2.setTexSize(256, 128);
        this.sideVertical2.mirror = true;
        this.setRotation(this.sideVertical2, 0F, 0F, 0F);
        this.sideVertical1 = new ModelPart(this, 94, 48);
        this.sideVertical1.addBox(-24F, -1.1F, -0.5F, 48, 1, 1);
        this.sideVertical1.setPos(0F, 0F, 0F);
        this.sideVertical1.setTexSize(256, 128);
        this.sideVertical1.mirror = true;
        this.setRotation(this.sideVertical1, 0F, 0F, 0F);
        this.sideHorizontal1 = new ModelPart(this, 0, 48);
        this.sideHorizontal1.addBox(-9F, -1.111F, -23F, 1, 1, 46);
        this.sideHorizontal1.setPos(0F, 0F, 0F);
        this.sideHorizontal1.setTexSize(256, 128);
        this.sideHorizontal1.mirror = true;
        this.setRotation(this.sideHorizontal1, 0F, 0F, 0F);
        this.sideHorizontal3 = new ModelPart(this, 0, 48);
        this.sideHorizontal3.addBox(23F, -1.111F, -23F, 1, 1, 46);
        this.sideHorizontal3.setPos(0F, 0F, 0F);
        this.sideHorizontal3.setTexSize(256, 128);
        this.sideHorizontal3.mirror = true;
        this.setRotation(this.sideHorizontal3, 0F, 0F, 0F);
        this.sideHorizontal2 = new ModelPart(this, 0, 48);
        this.sideHorizontal2.addBox(8F, -1.111F, -23F, 1, 1, 46);
        this.sideHorizontal2.setPos(0F, 0F, 0F);
        this.sideHorizontal2.setTexSize(256, 128);
        this.sideHorizontal2.mirror = true;
        this.setRotation(this.sideHorizontal2, 0F, 0F, 0F);
        this.pole = new ModelPart(this, 94, 50);
        this.pole.addBox(-1.5F, 0.0F, -1.5F, 3, 24, 3);
        this.pole.setPos(0F, 0F, 0F);
        this.pole.setTexSize(256, 128);
        this.pole.mirror = true;
        this.setRotation(this.pole, 0F, 0F, 0F);
    }

    private void setRotation(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void renderToBuffer(PoseStack matStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        renderSolarPanel(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        renderPole(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void renderSolarPanel(PoseStack matStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        this.panelMain.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideHorizontal0.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideVertical0.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideVertical1.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideVertical2.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideHorizontal1.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideHorizontal2.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.sideHorizontal3.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void renderPole(PoseStack matStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        this.pole.render(matStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
