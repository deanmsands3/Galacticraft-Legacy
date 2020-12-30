package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntryPodEntity;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;

public class EntryPodRenderer extends EntityRenderer<EntryPodEntity>
{
    private IBakedModel modelEntryPod;

    public EntryPodRenderer(EntityRendererManager manager)
    {
        super(manager);
        GCModelCache.INSTANCE.reloadCallback(this::updateModel);
    }

    public void updateModel()
    {
        this.modelEntryPod = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/entry_pod.obj"), ImmutableList.of("PodBody"));
    }

    @Override
    public void render(EntryPodEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        RenderSystem.disableRescaleNormal();
        matrixStack.push();
        matrixStack.rotate(new Quaternion(Vector3f.YP, 180.0F - entityYaw, true));
        matrixStack.rotate(new Quaternion(Vector3f.ZN, pitch, true));
        matrixStack.rotate(new Quaternion(Vector3f.YP, yaw, true));

        if (Minecraft.isAmbientOcclusionEnabled())
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
        matrixStack.pop();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public ResourceLocation getEntityTexture(EntryPodEntity entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public boolean shouldRender(EntryPodEntity entity, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(1D, 2D, 1D);
        return entity.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}