package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
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
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class TileEntityShortRangeTelepadRenderer extends BlockEntityRenderer<TileEntityShortRangeTelepad>
{
    private static BakedModel teleporterTop;
    private static BakedModel teleporterBottom;

    public TileEntityShortRangeTelepadRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        teleporterTop = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/telepad_short.obj"), ImmutableList.of("Top", "Connector"));
        teleporterBottom = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/telepad_short.obj"), ImmutableList.of("Bottom"));
    }

    @Override
    public void render(TileEntityShortRangeTelepad telepad, float partialTicks, PoseStack matStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
//        GL11.glPushMatrix();
        matStack.pushPose();

        Lighting.turnOff();
//        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.useAmbientOcclusion())
        {
            GlStateManager._shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager._shadeModel(GL11.GL_FLAT);
        }

//        GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
        matStack.translate(0.5F, 0.0F, 0.5F);

//        GL11.glScalef(0.745F, 1.0F, 0.745F);
        matStack.scale(0.749F, 1.0F, 0.749F);

        ClientUtil.drawBakedModel(teleporterBottom, bufferIn, matStack, combinedLightIn);
//        GL11.glTranslatef(0.0F, -0.7F, 0.0F);
        matStack.translate(0.0F, -0.7F, 0.0F);
        ClientUtil.drawBakedModel(teleporterTop, bufferIn, matStack, combinedLightIn);

//        GL11.glPopMatrix();
        matStack.popPose();
    }
}
