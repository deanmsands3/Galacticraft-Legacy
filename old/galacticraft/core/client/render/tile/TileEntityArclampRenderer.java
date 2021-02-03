package micdoodle8.mods.galacticraft.core.client.render.tile;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockArcLamp;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.tile.TileEntityArclamp;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileEntityArclampRenderer extends BlockEntityRenderer<TileEntityArclamp>
{
    public static BakedModel lampMetal;

    public TileEntityArclampRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        lampMetal = GCModelCache.INSTANCE.getModel(new ResourceLocation(Constants.MOD_ID_CORE, "models/obj/arc_lamp.obj"), ImmutableList.of("main"));
    }

    @Override
    public void render(TileEntityArclamp arclamp, float partialTicks, PoseStack matStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        int metaFacing = arclamp.facing;

        RenderSystem.disableRescaleNormal();
        matStack.pushPose();
        matStack.translate(0.5F, 0.5F, 0.5F);

        Lighting.turnBackOn();
        RenderSystem.enableRescaleNormal();

        switch (arclamp.getBlockState().getValue(BlockArcLamp.FACING))
        {
        case DOWN:
            break;
        case UP:
            matStack.mulPose(new Quaternion(Vector3f.XP, 180.0F, true));
            if (metaFacing < 2)
            {
                metaFacing ^= 1;
            }
            break;
        case NORTH:
            matStack.mulPose(new Quaternion(Vector3f.XP, 90.0F, true));
            metaFacing ^= 1;
            break;
        case SOUTH:
            matStack.mulPose(new Quaternion(Vector3f.XN, 90.0F, true));
            break;
        case WEST:
            matStack.mulPose(new Quaternion(Vector3f.ZN, 90.0F, true));
            metaFacing -= 2;
            if (metaFacing < 0)
            {
                metaFacing = 1 - metaFacing;
            }
            break;
        case EAST:
            matStack.mulPose(new Quaternion(Vector3f.ZP, 90.0F, true));
            metaFacing += 2;
            if (metaFacing > 3)
            {
                metaFacing = 5 - metaFacing;
            }
            break;
        }

        matStack.translate(0.0F, -0.175F, 0.0F);

        switch (metaFacing)
        {
        case 0:
            break;
        case 1:
            matStack.mulPose(new Quaternion(Vector3f.YP, 180.0F, true));
            break;
        case 2:
            matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
            break;
        case 3:
            matStack.mulPose(new Quaternion(Vector3f.YP, 270.0F, true));
            break;
        }

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        matStack.mulPose(new Quaternion(Vector3f.XN, 45.0F, true));
        matStack.scale(0.048F, 0.048F, 0.048F);
        ClientUtil.drawBakedModel(lampMetal, bufferIn, matStack, combinedLightIn);
        Lighting.turnOff();

        float greyLevel = arclamp.getEnabled() ? 1.0F : 26F / 255F;
        //Save the lighting state
//        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
        RenderSystem.disableLighting();

        ClientUtil.drawBakedModel(lampMetal, bufferIn, matStack, Constants.PACKED_LIGHT_FULL_BRIGHT);
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableTexture();
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder worldRenderer = tess.getBuilder();
        RenderSystem.color4f(greyLevel, greyLevel, greyLevel, 1.0F);
        RenderSystem.enableCull();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);
        float frameA = -3.4331F;  //These co-ordinates came originally from arclamp_light.obj model
        float frameB = -frameA;  //These co-ordinates came originally from arclamp_light.obj model
        float frameY = 2.3703F;  //These co-ordinates came originally from arclamp_light.obj model
        Matrix4f last = matStack.last().pose();
        worldRenderer.vertex(last, frameA, frameY, frameB).endVertex();
        worldRenderer.vertex(last, frameB, frameY, frameB).endVertex();
        worldRenderer.vertex(last, frameB, frameY, frameA).endVertex();
        worldRenderer.vertex(last, frameA, frameY, frameA).endVertex();
        tess.end();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableTexture();
        //? need to undo RenderSystem.glBlendFunc()?

        //Restore the lighting state
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
        matStack.popPose();
    }
}
