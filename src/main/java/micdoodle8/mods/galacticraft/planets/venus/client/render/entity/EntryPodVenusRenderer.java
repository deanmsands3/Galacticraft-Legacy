package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ParachuteBalloonModel;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityEntryPodVenus;
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

public class EntryPodVenusRenderer extends EntityRenderer<EntityEntryPodVenus>
{
    private IBakedModel modelEntryPod;
    private IBakedModel modelFlame;
    private final ParachuteBalloonModel parachuteModel = new ParachuteBalloonModel();
    private static final ResourceLocation PARACHUTE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/parachute/white.png");

    public EntryPodVenusRenderer(EntityRendererManager manager)
    {
        super(manager);
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        ResourceLocation model = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/pod_flame.obj");
        this.modelEntryPod = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("PodBody"));
        this.modelFlame = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("Flame_Sphere"));
    }

    @Override
    public void render(EntityEntryPodVenus entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        RenderSystem.disableRescaleNormal();
        matrixStack.push();
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        matrixStack.rotate(new Quaternion(Vector3f.YP, 180.0F - entityYaw, true));
        matrixStack.rotate(new Quaternion(Vector3f.ZN, pitch, true));
        matrixStack.rotate(new Quaternion(Vector3f.YP, yaw, true));

        matrixStack.scale(0.65F, 0.6F, 0.65F);
        ClientUtil.drawBakedModel(this.modelEntryPod, buffer, matrixStack, packedLight);

        if (entity.getPosY() > 382.0F)
        {
            RenderHelper.disableStandardItemLighting();
            //            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
            RenderSystem.disableLighting();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            //            RenderSystem.cullFace(RenderSystem.CullFace.FRONT);

            matrixStack.push();
            float val = (float) (Math.sin(entity.ticksExisted) / 20.0F + 0.5F);
            matrixStack.scale(1.0F, 1.0F + val, 1.0F);
            matrixStack.rotate(new Quaternion(Vector3f.YP, entity.ticksExisted * 20.0F, true));
            ClientUtil.drawBakedModelColored(this.modelFlame, buffer, matrixStack, packedLight, 255, 255, 255, entity.getPosY() >= 790.0F ? 255 : (int) Math.max(Math.min(255, -(entity.getMotion().y + 0.6F) * 100.0F), 0));
            matrixStack.pop();

            matrixStack.scale(1.0F, 1.0F + val / 6.0F, 1.0F);
            matrixStack.rotate(new Quaternion(Vector3f.YP, entity.ticksExisted * 5.0F, true));
            ClientUtil.drawBakedModelColored(this.modelFlame, buffer, matrixStack, packedLight, 255, 255, 255, entity.getPosY() >= 790.0F ? 255 : (int) Math.max(Math.min(255, -(entity.getMotion().y + 0.6F) * 100.0F), 0));

            //            RenderSystem.cullFace(RenderSystem.CullFace.BACK);
            RenderSystem.enableCull();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.enableStandardItemLighting();
        }

        if (entity.getGroundPosY() != null && entity.getPosY() - entity.getGroundPosY() > 5.0F && entity.getPosY() <= 242.0F)
        {
            matrixStack.push();
            matrixStack.translate(-1.4F, 1.5F, -0.3F);
            matrixStack.scale(2.5F, 3.0F, 2.5F);
            IVertexBuilder ivertexbuilder = buffer.getBuffer(this.parachuteModel.getRenderType(PARACHUTE));
            this.parachuteModel.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pop();
        }
        matrixStack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEntryPodVenus entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public boolean shouldRender(EntityEntryPodVenus entity, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(1D, 2D, 1D);
        return entity.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}