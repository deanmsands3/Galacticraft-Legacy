package team.galacticraft.galacticraft.common.core.client.render.tile;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.client.obj.GCModelCache;
import team.galacticraft.galacticraft.common.core.tile.TileEntityFluidTank;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import me.shedaniel.architectury.fluid.FluidStack;
import org.lwjgl.opengl.GL11;

public class TileEntityFluidTankRenderer extends BlockEntityRenderer<TileEntityFluidTank>
{
    public TileEntityFluidTankRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityFluidTank tileTank, float partialTicks, PoseStack matStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
//        FluidTankInfo[] info = tank.getTankInfo(Direction.DOWN);

//        if (info.length != 1)
//        {
//            return;
//        }
//        FluidStack tankFluid = info[0].fluid;
        FluidStack tankFluid = tileTank.fluidTank.getFluid();
        if (tankFluid == FluidStack.EMPTY || (!tankFluid.getFluid().getAttributes().isGaseous() && tankFluid.getAmount() == 0))
        {
            return;
        }

        TileEntityFluidTank tankAbove = tileTank.getNextTank(tileTank.getBlockPos());
        TileEntityFluidTank tankBelow = tileTank.getPreviousTank(tileTank.getBlockPos());

//        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
//        Tessellator tess = Tessellator.getInstance();
        TextureAtlasSprite fluidSprite = ModelLoader.defaultTextureGetter().apply(ForgeHooksClient.getBlockMaterial(tankFluid.getFluid().getAttributes().getStillTexture()));
        RenderType renderType = RenderType.entitySolid(tankFluid.getFluid().getAttributes().getStillTexture());
        VertexConsumer builder = bufferIn.getBuffer(renderType);
        final float uMin = fluidSprite.getU0();
        final float uMax = fluidSprite.getU1();
        final float vMin = fluidSprite.getV0();
        final float vMax = fluidSprite.getV1();
//        GL11.glPushMatrix();
        matStack.pushPose();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glTranslatef((float) x, (float) y + 1.5F, (float) z + 1.0F);
        matStack.translate(0.0F, 1.5F, 1.0F);
//        GL11.glScalef(1.0F, -1.0F, -1.0F);
        matStack.scale(1.0F, -1.0F, -1.0F);
//        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        matStack.translate(0.5F, 0.5F, 0.5F);

        GlStateManager._disableLighting();
        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        Tessellator tess = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tess.getBuffer();

        float level = 1.0F;
        float levelInv = 0.0F;
        float opacity = 1.0F;

        boolean compositeGaseous = tankFluid.getFluid().getAttributes().isGaseous();

        if (compositeGaseous)
        {
            opacity = Math.min(tankFluid.getAmount() / (float) tileTank.fluidTank.getCapacity() * 0.8F + 0.2F, 1F);
        }
        else
        {
            level = tileTank.fluidTank.getFluidAmount() / 16400.0F;
            if (level <= 0.012F)
            {
                levelInv = 1.0F;  //Empty tanks render empty - see #3222
            }
            else
            {
                levelInv = 0.988F - level;  //1.2% inset from each end of the tank, to avoid z-fighting with blocks above/below
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, opacity);

        if (levelInv < 1.0F)
        {
            Matrix4f matrix = matStack.last().pose();

            Vector3f norm = Vector3f.ZN;
            // North
            builder.vertex(matrix, -0.4F, levelInv, -0.399F).color(255, 255, 255, 255).uv(uMin, vMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            builder.vertex(matrix, -0.4F, 1.0F, -0.399F).color(255, 255, 255, 255).uv(uMin, vMin + (vMax - vMin) * level).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            builder.vertex(matrix, 0.4F, 1.0F, -0.399F).color(255, 255, 255, 255).uv(uMax, vMin + (vMax - vMin) * level).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            builder.vertex(matrix, 0.4F, levelInv, -0.399F).color(255, 255, 255, 255).uv(uMax, vMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();

            norm = Vector3f.ZP;
            // South
            builder.vertex(matrix, 0.4F, levelInv, 0.399F).color(255, 255, 255, 255).uv(uMax, vMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            builder.vertex(matrix, 0.4F, 1.0F, 0.399F).color(255, 255, 255, 255).uv(uMax, vMin + (vMax - vMin) * level).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            builder.vertex(matrix, -0.4F, 1.0F, 0.399F).color(255, 255, 255, 255).uv(uMin, vMin + (vMax - vMin) * level).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            builder.vertex(matrix, -0.4F, levelInv, 0.399F).color(255, 255, 255, 255).uv(uMin, vMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();

            norm = Vector3f.XN;
            // West
            builder.vertex(matrix, -0.399F, 1.0F, -0.4F).color(255, 255, 255, 255).uv(uMin, vMin + (vMax - vMin) * level).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            builder.vertex(matrix, -0.399F, levelInv, -0.4F).color(255, 255, 255, 255).uv(uMin, vMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            builder.vertex(matrix, -0.399F, levelInv, 0.4F).color(255, 255, 255, 255).uv(uMax, vMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            builder.vertex(matrix, -0.399F, 1.0F, 0.4F).color(255, 255, 255, 255).uv(uMax, vMin + (vMax - vMin) * level).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();

            norm = Vector3f.XP;
            // East
            builder.vertex(matrix, 0.399F, 1.0F, 0.4F).color(255, 255, 255, 255).uv(uMax, vMin + (vMax - vMin) * level).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            builder.vertex(matrix, 0.399F, levelInv, 0.4F).color(255, 255, 255, 255).uv(uMax, vMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            builder.vertex(matrix, 0.399F, levelInv, -0.4F).color(255, 255, 255, 255).uv(uMin, vMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            builder.vertex(matrix, 0.399F, 1.0F, -0.4F).color(255, 255, 255, 255).uv(uMin, vMin + (vMax - vMin) * level).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();

            if (tankAbove == null || (tankAbove.fluidTank.getFluidAmount() == 0 && !compositeGaseous))
            {
                norm = Vector3f.YP;
                builder.vertex(matrix, 0.4F, 0.01F + levelInv, 0.4F).color(255, 255, 255, 255).uv(uMax, vMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
                builder.vertex(matrix, -0.4F, 0.01F + levelInv, 0.4F).color(255, 255, 255, 255).uv(uMax, vMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
                builder.vertex(matrix, -0.4F, 0.01F + levelInv, -0.4F).color(255, 255, 255, 255).uv(uMin, vMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
                builder.vertex(matrix, 0.4F, 0.01F + levelInv, -0.4F).color(255, 255, 255, 255).uv(uMin, vMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            }

            if (tankBelow == null || (tankBelow.fluidTank.getFluidAmount() == 0 && !compositeGaseous))
            {
                norm = Vector3f.YN;
                builder.vertex(matrix, 0.4F, 0.99F, 0.4F).color(255, 255, 255, 255).uv(uMax, vMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
                builder.vertex(matrix, 0.4F, 0.99F, -0.4F).color(255, 255, 255, 255).uv(uMin, vMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
                builder.vertex(matrix, -0.4F, 0.99F, -0.4F).color(255, 255, 255, 255).uv(uMin, vMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
                builder.vertex(matrix, -0.4F, 0.99F, 0.4F).color(255, 255, 255, 255).uv(uMax, vMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLightIn).normal(norm.x(), norm.y(), norm.z()).endVertex();
            }
        }

        GlStateManager._enableLighting();
        GlStateManager._disableBlend();

//        GL11.glPopMatrix();
        matStack.popPose();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
