package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

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
import micdoodle8.mods.galacticraft.planets.mars.entities.Tier2RocketEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Tier2RocketRenderer extends EntityRenderer<Tier2RocketEntity>
{
    private BakedModel model;

    public Tier2RocketRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 2F;
        GCModelCache.INSTANCE.reloadCallback(this::updateModel);
    }

    private void updateModel()
    {
        this.model = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/tier_2_rocket.obj"), ImmutableList.of("Rocket"));
    }

    @Override
    public ResourceLocation getEntityTexture(Tier2RocketEntity entity)
    {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(Tier2RocketEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        float pitch = entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks + 180;
        float rollAmplitude = entity.rollAmplitude / 3 - partialTicks;
        RenderSystem.disableRescaleNormal();
        matrixStack.pushPose();
        matrixStack.mulPose(new Quaternion(Vector3f.YP, 180.0F - entityYaw, true));
        matrixStack.mulPose(new Quaternion(Vector3f.ZN, pitch, true));
        matrixStack.translate(0.0F, entity.getRenderOffsetY(), 0.0F);

        if (rollAmplitude > 0.0F)
        {
            float i = entity.getLaunched() ? (5 - Mth.floor(entity.timeUntilLaunch / 85)) / 10F : 0.3F;
            matrixStack.mulPose(Vector3f.XP.rotation(Mth.sin(rollAmplitude) * rollAmplitude * i * partialTicks));
            matrixStack.mulPose(Vector3f.ZP.rotation(Mth.sin(rollAmplitude) * rollAmplitude * i * partialTicks));
        }

        if (Minecraft.useAmbientOcclusion())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.scale(0.8F, 0.8F, 0.8F);
        ClientUtil.drawBakedModel(this.model, buffer, matrixStack, packedLight);
        matrixStack.popPose();
        Lighting.turnBackOn();
    }

    @Override
    public boolean shouldRender(Tier2RocketEntity entity, Frustum camera, double camX, double camY, double camZ)
    {
        AABB axisalignedbb = entity.getBoundingBox().inflate(0.6D, 1D, 0.6D);
        return entity.shouldRender(camX, camY, camZ) && camera.isVisible(axisalignedbb);
    }
}