package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ParachuteBalloonModel;
import micdoodle8.mods.galacticraft.planets.mars.entities.LandingBalloonsEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandingBalloonsRenderer extends EntityRenderer<LandingBalloonsEntity>
{
    private IBakedModel balloonModel;
    private final ParachuteBalloonModel parachute = new ParachuteBalloonModel();

    public LandingBalloonsRenderer(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 1.2F;
        GCModelCache.INSTANCE.reloadCallback(this::updateModel);
    }

    public void updateModel()
    {
        this.balloonModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/landing_balloon.obj"), ImmutableList.of("Sphere"));
    }

    @Override
    public ResourceLocation getEntityTexture(LandingBalloonsEntity entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void render(LandingBalloonsEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        RenderSystem.disableRescaleNormal();
        matrixStack.push();
        matrixStack.translate(0, 0.8F, 0);
        matrixStack.rotate(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStack.rotate(new Quaternion(Vector3f.ZN, pitch, true));

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        ClientUtil.drawBakedModel(this.balloonModel, buffer, matrixStack, packedLight);
        matrixStack.pop();

        if (entity.getPosY() >= 500.0F)
        {
            matrixStack.push();
            matrixStack.translate(-1.25F, -0.93F, -0.3F);
            matrixStack.scale(2.5F, 3.0F, 2.5F);
            IVertexBuilder ivertexbuilder = buffer.getBuffer(this.parachute.getRenderType(this.getEntityTexture(entity)));
            this.parachute.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pop();
        }
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public boolean shouldRender(LandingBalloonsEntity entity, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(2D, 1D, 2D);
        return entity.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}