package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.Tier3RocketEntity;
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
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Tier3RocketRenderer extends EntityRenderer<Tier3RocketEntity>
{
    private IBakedModel rocketModel;
    private IBakedModel coneModel;
    private IBakedModel cubeModel;

    public Tier3RocketRenderer(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 2F;
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
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void render(Tier3RocketEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 180;
        RenderSystem.disableRescaleNormal();
        matrixStack.push();
        matrixStack.rotate(new Quaternion(Vector3f.YP, 180.0F - entityYaw, true));
        matrixStack.rotate(new Quaternion(Vector3f.ZN, pitch, true));
        matrixStack.translate(0.0F, entity.getRenderOffsetY(), 0.0F);
        float rollAmplitude = entity.rollAmplitude / 3 - partialTicks;

        if (rollAmplitude > 0.0F)
        {
            float i = entity.getLaunched() ? (5 - MathHelper.floor(entity.timeUntilLaunch / 85)) / 10F : 0.3F;
            matrixStack.rotate(Vector3f.XP.rotation(MathHelper.sin(rollAmplitude) * rollAmplitude * i * partialTicks));
            matrixStack.rotate(Vector3f.ZP.rotation(MathHelper.sin(rollAmplitude) * rollAmplitude * i * partialTicks));
        }

        if (Minecraft.isAmbientOcclusionEnabled())
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

        boolean red = Minecraft.getInstance().player.ticksExisted / 10 % 2 < 1;
        ClientUtil.drawBakedModelColored(this.cubeModel, buffer, matrixStack, packedLight, 0, red ? 0 : 1.0F, red ? 1.0F : 0, 1.0F);

        RenderSystem.enableTexture();
        RenderSystem.enableLighting();

        RenderSystem.color3f(1F, 1F, 1F);
        matrixStack.pop();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public boolean shouldRender(Tier3RocketEntity rocket, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = rocket.getBoundingBox().grow(0.5D, 0, 0.5D);
        return rocket.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}