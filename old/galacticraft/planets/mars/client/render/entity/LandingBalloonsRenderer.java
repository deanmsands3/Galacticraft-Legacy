package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ParachuteBalloonModel;
import micdoodle8.mods.galacticraft.planets.mars.entities.LandingBalloonsEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandingBalloonsRenderer extends EntityRenderer<LandingBalloonsEntity>
{
    private BakedModel balloonModel;
    private final ParachuteBalloonModel parachute = new ParachuteBalloonModel();
    private static final ResourceLocation PARACHUTE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/white.png");

    public LandingBalloonsRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 1.2F;
        GCModelCache.INSTANCE.reloadCallback(this::updateModel);
    }

    public void updateModel()
    {
        this.balloonModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/landing_balloon.obj"), ImmutableList.of("Sphere"));
    }

    @Override
    public ResourceLocation getEntityTexture(LandingBalloonsEntity entity)
    {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(LandingBalloonsEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        float pitch = entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks;
        RenderSystem.disableRescaleNormal();
        matrixStack.pushPose();
        matrixStack.translate(0, 0.8F, 0);
        matrixStack.mulPose(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStack.mulPose(new Quaternion(Vector3f.ZN, pitch, true));

        if (Minecraft.useAmbientOcclusion())
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
        matrixStack.popPose();

        if (entity.getY() >= 500.0F)
        {
            matrixStack.pushPose();
            matrixStack.translate(-1.25F, -0.93F, -0.3F);
            matrixStack.scale(2.5F, 3.0F, 2.5F);
            VertexConsumer ivertexbuilder = buffer.getBuffer(this.parachute.renderType(PARACHUTE));
            this.parachute.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
        Lighting.turnBackOn();
    }

    @Override
    public boolean shouldRender(LandingBalloonsEntity entity, Frustum camera, double camX, double camY, double camZ)
    {
        AABB axisalignedbb = entity.getBoundingBox().inflate(2D, 1D, 2D);
        return entity.shouldRender(camX, camY, camZ) && camera.isVisible(axisalignedbb);
    }
}