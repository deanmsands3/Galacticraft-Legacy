package micdoodle8.mods.galacticraft.core.client.model;

import org.lwjgl.opengl.GL11;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.entities.FlagEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

public class FlagModel extends EntityModel<FlagEntity>
{
    ModelPart base;
    ModelPart pole;

    public FlagModel()
    {
        this.texWidth = 128;
        this.texHeight = 64;
        this.base = new ModelPart(this, 4, 0);
        this.base.addBox(-1.5F, 0F, -1.5F, 3, 1, 3);
        this.base.setPos(0F, 23F, 0F);
        this.base.setTexSize(128, 64);
        this.base.mirror = true;
        this.setRotation(this.base, 0F, 0F, 0F);
        this.pole = new ModelPart(this, 0, 0);
        this.pole.addBox(-0.5F, -40F, -0.5F, 1, 40, 1);
        this.pole.setPos(0F, 23F, 0F);
        this.pole.setTexSize(128, 64);
        this.pole.mirror = true;
        this.setRotation(this.pole, 0F, 0F, 0F);
    }

    @Override
    public void setRotationAngles(FlagEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        this.base.render(matrixStack, buffer, packedLight, packedOverlay);
        this.pole.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void renderFlag(FlagEntity entity, float ticks, PoseStack matrixStack)
    {
        if (entity.flagData != null)
        {
            matrixStack.pushPose();
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.translate(0.0F, -1.1F, 0.0F);

            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.disableCull();

            float windLevel = 1.0F;

            if (entity.level.getDimension() instanceof IGalacticraftDimension)
            {
                windLevel = ((IGalacticraftDimension) entity.level.getDimension()).getWindLevel();
            }

            for (int i = 0; i < entity.flagData.getWidth(); i++)
            {
                for (int j = 0; j < entity.flagData.getHeight(); j++)
                {
                    matrixStack.pushPose();
                    matrixStack.translate(0.0F, -1.0F, 0.0F);
                    float offset = 0.0F;
                    float offsetAhead = 0.0F;

                    if (windLevel > 0)
                    {
                        offset = (float) (Math.sin(ticks / 2.0F + i * 50 + 3) / 25.0F) * i / 30.0F;
                        offsetAhead = (float) (Math.sin(ticks / 2.0F + (i + 1) * 50 + 3) / 25.0F) * (i + 1) / 30.0F;
                        offset *= windLevel;
                        offsetAhead *= windLevel;
                    }

                    Vector3 col = entity.flagData.getColorAt(i, j);
                    RenderSystem.color3f(col.floatX(), col.floatY(), col.floatZ());

                    Tesselator tess = Tesselator.getInstance();
                    BufferBuilder worldRenderer = tess.getBuilder();
                    worldRenderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormat.POSITION);
                    Matrix4f last = matrixStack.last().pose();

                    worldRenderer.vertex(last, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offset, offset).endVertex();
                    worldRenderer.vertex(last, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 1.0F / 24.0F + offset, offset).endVertex();
                    worldRenderer.vertex(last, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 1.0F / 24.0F + offsetAhead, offsetAhead).endVertex();

                    worldRenderer.vertex(last, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offset, offset).endVertex();
                    worldRenderer.vertex(last, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 1.0F / 24.0F + offsetAhead, offsetAhead).endVertex();
                    worldRenderer.vertex(last, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offsetAhead, offsetAhead).endVertex();

                    tess.end();

                    RenderSystem.color3f(1.0F, 1.0F, 1.0F);
                    matrixStack.popPose();
                }
            }

            RenderSystem.disableDepthTest();
            RenderSystem.enableTexture();
            RenderSystem.enableCull();

            matrixStack.popPose();
        }
    }

    private void setRotation(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}