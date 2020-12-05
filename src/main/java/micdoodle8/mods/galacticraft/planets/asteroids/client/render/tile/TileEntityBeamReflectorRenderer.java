package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReflector;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class TileEntityBeamReflectorRenderer extends BlockEntityRenderer<TileEntityBeamReflector>
{
    private BakedModel reflectorModelBase;
    private BakedModel reflectorModelAxle;
    private BakedModel reflectorModelEnergyBlaster;
    private BakedModel reflectorModelRing;

    public TileEntityBeamReflectorRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        reflectorModelBase = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/block/reflector.obj"), ImmutableList.of("Base"));
        reflectorModelAxle = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/block/reflector.obj"), ImmutableList.of("Axle"));
        reflectorModelEnergyBlaster = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/block/reflector.obj"), ImmutableList.of("EnergyBlaster"));
        reflectorModelRing = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/block/reflector.obj"), ImmutableList.of("Ring"));
    }

    @Override
    public void render(TileEntityBeamReflector tile, float partialTicks, PoseStack matStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        GlStateManager._disableRescaleNormal();
//        GlStateManager.pushMatrix();
        matStack.pushPose();
//        GlStateManager.translatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
        matStack.translate(0.5F, 0.0F, 0.5F);
//        GlStateManager.scalef(0.5F, 0.5F, 0.5F);
        matStack.scale(0.5F, 0.5F, 0.5F);

        if (Minecraft.useAmbientOcclusion())
        {
            GlStateManager._shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager._shadeModel(GL11.GL_FLAT);
        }

        ClientUtil.drawBakedModel(reflectorModelBase, bufferIn, matStack, combinedLightIn);
//        GlStateManager.rotatef(tile.yaw, 0, 1, 0);
        matStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), tile.yaw, true));
        ClientUtil.drawBakedModel(reflectorModelAxle, bufferIn, matStack, combinedLightIn);
        float dX = 0.0F;
        float dY = 1.13228F;
        float dZ = 0.0F;
//        GlStateManager.translatef(dX, dY, dZ);
        matStack.translate(dX, dY, dZ);
//        GlStateManager.rotatef(tile.pitch, 1, 0, 0);
        matStack.mulPose(new Quaternion(new Vector3f(1, 0, 0), tile.pitch, true));
//        GlStateManager.translatef(-dX, -dY, -dZ);
        matStack.translate(-dX, -dY, -dZ);
        ClientUtil.drawBakedModel(reflectorModelEnergyBlaster, bufferIn, matStack, combinedLightIn);
//        GlStateManager.translatef(dX, dY, dZ);
        matStack.translate(dX, dY, dZ);
//        GlStateManager.rotatef(tile.ticks * 5, 0, 0, 1);
        matStack.mulPose(new Quaternion(new Vector3f(0, 0, 1), tile.ticks * 5, true));
//        GlStateManager.translatef(-dX, -dY, -dZ);
        matStack.translate(-dX, -dY, -dZ);
        ClientUtil.drawBakedModel(reflectorModelRing, bufferIn, matStack, combinedLightIn);
//        GlStateManager.popMatrix();
        matStack.popPose();
        Lighting.turnBackOn();
    }
}
