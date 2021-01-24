package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

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
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityEntryPodVenus;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;

public class EntryPodVenusRenderer extends EntityRenderer<EntityEntryPodVenus>
{
    private BakedModel modelEntryPod;
    private BakedModel modelFlame;
    private final ParachuteBalloonModel parachuteModel = new ParachuteBalloonModel();
    private static final ResourceLocation PARACHUTE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/white.png");

    public EntryPodVenusRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        ResourceLocation model = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/entry_pod_flame.obj");
        this.modelEntryPod = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("PodBody"));
        this.modelFlame = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("Flame_Sphere"));
    }

    @Override
    public void render(EntityEntryPodVenus entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        RenderSystem.disableRescaleNormal();
        matrixStack.pushPose();
        float pitch = entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks;
        float yaw = entity.yRotO + (entity.yRot - entity.yRotO) * partialTicks;
        matrixStack.mulPose(new Quaternion(Vector3f.YP, 180.0F - entityYaw, true));
        matrixStack.mulPose(new Quaternion(Vector3f.ZN, pitch, true));
        matrixStack.mulPose(new Quaternion(Vector3f.YP, yaw, true));

        matrixStack.scale(0.65F, 0.6F, 0.65F);
        ClientUtil.drawBakedModel(this.modelEntryPod, buffer, matrixStack, packedLight);

        if (entity.getY() > 382.0F)
        {
            Lighting.turnOff();
            //            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
            RenderSystem.disableLighting();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            //            RenderSystem.cullFace(RenderSystem.CullFace.FRONT);

            matrixStack.pushPose();
            float val = (float) (Math.sin(entity.tickCount) / 20.0F + 0.5F);
            matrixStack.scale(1.0F, 1.0F + val, 1.0F);
            matrixStack.mulPose(new Quaternion(Vector3f.YP, entity.tickCount * 20.0F, true));
            ClientUtil.drawBakedModelColored(this.modelFlame, buffer, matrixStack, packedLight, 255, 255, 255, entity.getY() >= 790.0F ? 255 : (int) Math.max(Math.min(255, -(entity.getDeltaMovement().y + 0.6F) * 100.0F), 0));
            matrixStack.popPose();

            matrixStack.scale(1.0F, 1.0F + val / 6.0F, 1.0F);
            matrixStack.mulPose(new Quaternion(Vector3f.YP, entity.tickCount * 5.0F, true));
            ClientUtil.drawBakedModelColored(this.modelFlame, buffer, matrixStack, packedLight, 255, 255, 255, entity.getY() >= 790.0F ? 255 : (int) Math.max(Math.min(255, -(entity.getDeltaMovement().y + 0.6F) * 100.0F), 0));

            //            RenderSystem.cullFace(RenderSystem.CullFace.BACK);
            RenderSystem.enableCull();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Lighting.turnBackOn();
        }

        if (entity.getGroundPosY() != null && entity.getY() - entity.getGroundPosY() > 5.0F && entity.getY() <= 242.0F)
        {
            matrixStack.pushPose();
            matrixStack.translate(-1.4F, 1.5F, -0.3F);
            matrixStack.scale(2.5F, 3.0F, 2.5F);
            VertexConsumer ivertexbuilder = buffer.getBuffer(this.parachuteModel.renderType(PARACHUTE));
            this.parachuteModel.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
        matrixStack.popPose();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEntryPodVenus entity)
    {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public boolean shouldRender(EntityEntryPodVenus entity, Frustum camera, double camX, double camY, double camZ)
    {
        AABB axisalignedbb = entity.getBoundingBox().inflate(1D, 2D, 1D);
        return entity.shouldRender(camX, camY, camZ) && camera.isVisible(axisalignedbb);
    }
}