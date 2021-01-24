package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.Tier3RocketEntity;
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
public class Tier3RocketRenderer extends EntityRenderer<Tier3RocketEntity>
{
    private BakedModel rocketModel;
    private BakedModel coneModel;
    private BakedModel cubeModel;

    public Tier3RocketRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 2F;
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        ResourceLocation model = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/tier_3_rocket.obj");
        this.rocketModel = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("Boosters", "Rocket"));
        this.coneModel = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("NoseCone"));
        this.cubeModel = GCModelCache.INSTANCE.getModel(model, ImmutableList.of("Cube"));
    }

    @Override
    public ResourceLocation getEntityTexture(Tier3RocketEntity entity)
    {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(Tier3RocketEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        float pitch = entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks + 180;
        RenderSystem.disableRescaleNormal();
        matrixStack.pushPose();
        matrixStack.mulPose(new Quaternion(Vector3f.YP, 180.0F - entityYaw, true));
        matrixStack.mulPose(new Quaternion(Vector3f.ZN, pitch, true));
        matrixStack.translate(0.0F, entity.getRenderOffsetY(), 0.0F);
        float rollAmplitude = entity.rollAmplitude / 3 - partialTicks;

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
        ClientUtil.drawBakedModel(this.rocketModel, buffer, matrixStack, packedLight);

        Vector3 teamColor = ClientUtil.updateTeamColor(PlayerUtil.getName(Minecraft.getInstance().player), true);

        if (teamColor != null)
        {
            RenderSystem.disableTexture();
            ClientUtil.drawBakedModelColored(this.coneModel, buffer, matrixStack, packedLight, teamColor.floatY(), teamColor.floatX(), teamColor.floatZ(), 1.0F);
        }
        else
        {
            ClientUtil.drawBakedModel(this.coneModel, buffer, matrixStack, packedLight);
            RenderSystem.disableTexture();
        }

        RenderSystem.disableLighting();

        boolean red = Minecraft.getInstance().player.tickCount / 10 % 2 < 1;
        ClientUtil.drawBakedModelColored(this.cubeModel, buffer, matrixStack, packedLight, 0, red ? 0 : 1.0F, red ? 1.0F : 0, 1.0F);

        RenderSystem.enableTexture();
        RenderSystem.enableLighting();

        RenderSystem.color3f(1F, 1F, 1F);
        matrixStack.popPose();
        Lighting.turnBackOn();
    }

    @Override
    public boolean shouldRender(Tier3RocketEntity rocket, Frustum camera, double camX, double camY, double camZ)
    {
        AABB axisalignedbb = rocket.getBoundingBox().inflate(0.5D, 0, 0.5D);
        return rocket.shouldRender(camX, camY, camZ) && camera.isVisible(axisalignedbb);
    }
}