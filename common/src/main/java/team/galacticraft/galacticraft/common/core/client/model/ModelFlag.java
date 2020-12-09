package team.galacticraft.galacticraft.common.core.client.model;

import team.galacticraft.galacticraft.common.api.vector.Vector3;
import team.galacticraft.galacticraft.common.api.world.IGalacticraftDimension;
import team.galacticraft.galacticraft.core.entities.EntityFlag;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import org.lwjgl.opengl.GL11;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import java.util.function.Function;

public class ModelFlag extends EntityModel<EntityFlag>
{
    ModelPart base;
    ModelPart pole;

    public ModelFlag()
    {
        super();
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
    public void setRotationAngles(EntityFlag entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {

    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        renderPole(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void renderPole(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        this.base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.pole.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    public void renderFlag(EntityFlag entity, float ticks, PoseStack matStack)
    {
        if (entity.flagData != null)
        {
//            GL11.glPushMatrix();
            matStack.pushPose();

//            GL11.glScalef(0.5F, 0.5F, 0.5F);
            matStack.scale(0.5F, 0.5F, 0.5F);
//            GL11.glTranslatef(0.0F, -1.1F, 0.0F);
            matStack.translate(0.0F, -1.1F, 0.0F);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_CULL_FACE);

            float windLevel = 1.0F;

            if (entity.level.getDimension() instanceof IGalacticraftDimension)
            {
                windLevel = ((IGalacticraftDimension) entity.level.getDimension()).getWindLevel();
            }

            for (int i = 0; i < entity.flagData.getWidth(); i++)
            {
                for (int j = 0; j < entity.flagData.getHeight(); j++)
                {
//                    GL11.glPushMatrix();
                    matStack.pushPose();
//                    GL11.glTranslatef(0, -1.0F, 0);
                    matStack.translate(0.0F, -1.0F, 0.0F);
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
                    GL11.glColor3f(col.floatX(), col.floatY(), col.floatZ());

                    Tesselator tess = Tesselator.getInstance();
                    BufferBuilder worldRenderer = tess.getBuilder();
                    worldRenderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormat.POSITION);
                    Matrix4f last = matStack.last().pose();

                    worldRenderer.vertex(last, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offset, offset).endVertex();
                    worldRenderer.vertex(last, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 1.0F / 24.0F + offset, offset).endVertex();
                    worldRenderer.vertex(last, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 1.0F / 24.0F + offsetAhead, offsetAhead).endVertex();

                    worldRenderer.vertex(last, i / 24.0F + 0.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offset, offset).endVertex();
                    worldRenderer.vertex(last, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 1.0F / 24.0F + offsetAhead, offsetAhead).endVertex();
                    worldRenderer.vertex(last, i / 24.0F + 1.0F / 24.0F, j / 24.0F + 0.0F / 24.0F + offsetAhead, offsetAhead).endVertex();

                    tess.end();

                    GL11.glColor3f(1, 1, 1);
//                    GL11.glPopMatrix();
                    matStack.popPose();
                }
            }

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_CULL_FACE);

//            GL11.glPopMatrix();
            matStack.popPose();
        }
    }

    private void setRotation(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}
