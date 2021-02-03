package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntryPodEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;

public class EntryPodRenderer extends EntityRenderer<EntryPodEntity>
{
    private BakedModel modelEntryPod;

    public EntryPodRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
        GCModelCache.INSTANCE.reloadCallback(this::updateModel);
    }

    private void updateModel()
    {
        this.modelEntryPod = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/entry_pod.obj"), ImmutableList.of("PodBody"));
    }

    @Override
    public void render(EntryPodEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        float pitch = entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks;
        float yaw = entity.yRotO + (entity.yRot - entity.yRotO) * partialTicks;
        RenderSystem.disableRescaleNormal();
        matrixStack.pushPose();
        matrixStack.mulPose(new Quaternion(Vector3f.YP, 180.0F - entityYaw, true));
        matrixStack.mulPose(new Quaternion(Vector3f.ZN, pitch, true));
        matrixStack.mulPose(new Quaternion(Vector3f.YP, yaw, true));

        if (Minecraft.useAmbientOcclusion())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.scale(0.65F, 0.6F, 0.65F);
        ClientUtil.drawBakedModel(this.modelEntryPod, buffer, matrixStack, packedLight);
        matrixStack.popPose();
        Lighting.turnBackOn();
    }

    @Override
    public ResourceLocation getEntityTexture(EntryPodEntity entity)
    {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public boolean shouldRender(EntryPodEntity entity, Frustum camera, double camX, double camY, double camZ)
    {
        AABB axisalignedbb = entity.getBoundingBox().inflate(1D, 2D, 1D);
        return entity.shouldRender(camX, camY, camZ) && camera.isVisible(axisalignedbb);
    }
}