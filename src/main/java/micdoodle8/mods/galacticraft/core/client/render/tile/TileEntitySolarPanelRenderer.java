package micdoodle8.mods.galacticraft.core.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelSolarPanel;
import micdoodle8.mods.galacticraft.core.dimension.DimensionSpaceStation;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntitySolarPanelRenderer extends BlockEntityRenderer<TileEntitySolar>
{
    private static final ResourceLocation solarPanelTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/solar_panel_basic.png");
    private static final ResourceLocation solarPanelAdvTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/solar_panel_advanced.png");
    private ModelSolarPanel model = new ModelSolarPanel();

    public TileEntitySolarPanelRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntitySolar panel, float partialTicks, PoseStack matStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        boolean doSkyRotation = panel.tierGC == 2 && panel.getLevel().dimension instanceof DimensionSpaceStation;
//        if (panel.tierGC == 2)
//        {
//            this.bindTexture(TileEntitySolarPanelRenderer.solarPanelAdvTexture);
////            doSkyRotation = panel.getWorld().dimension instanceof DimensionSpaceStation; TODO Sky rotation
//        }
//        else
//        {
//            this.bindTexture(TileEntitySolarPanelRenderer.solarPanelTexture);
//        }

        combinedLightIn = LevelRenderer.getLightColor(panel.getLevel(), panel.getBlockPos().above());

//        GL11.glPushMatrix();
        matStack.pushPose();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glTranslatef((float) x, (float) y, (float) z);

//        GL11.glTranslatef(0.5F, 1.0F, 0.5F);
        matStack.translate(0.5F, 1.0F, 0.5F);
        RenderType renderType = RenderType.entitySolid(panel.tierGC == 2 ? solarPanelAdvTexture : solarPanelTexture);
        VertexConsumer builder = bufferIn.getBuffer(renderType);

        matStack.pushPose();
        if (doSkyRotation) matStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), ((DimensionSpaceStation) panel.getLevel().dimension).getSkyRotation(), true));
        this.model.renderPole(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.popPose();

//        GL11.glTranslatef(0.0F, 1.5F, 0.0F);
        matStack.translate(0.0F, 1.5F, 0.0F);

//        GL11.glRotatef(180.0F, 0, 0, 1);
//        GL11.glRotatef(-90.0F, 0, 1, 0);
        matStack.mulPose(new Quaternion(0.0F, 0.0F, 180.0F, true));
        matStack.mulPose(new Quaternion(0.0F, -90.0F, 0.0F, true));

        float celestialAngle = (panel.getLevel().getTimeOfDay(1.0F) - 0.784690560F) * 360.0F;
        float celestialAngle2 = panel.getLevel().getTimeOfDay(1.0F) * 360.0F;

        matStack.pushPose();
        if (doSkyRotation) matStack.mulPose(new Quaternion(new Vector3f(0.0F, -1.0F, 0.0F), ((DimensionSpaceStation) panel.getLevel().dimension).getSkyRotation(), true));
        matStack.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F), panel.currentAngle - (celestialAngle - celestialAngle2), true));
        this.model.renderSolarPanel(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.popPose();

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        GL11.glPopMatrix();
        matStack.popPose();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
