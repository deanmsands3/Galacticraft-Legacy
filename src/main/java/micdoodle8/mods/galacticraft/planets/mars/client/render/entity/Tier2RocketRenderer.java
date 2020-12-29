package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.entities.Tier2RocketEntity;
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
public class Tier2RocketRenderer extends EntityRenderer<Tier2RocketEntity>
{
    private IBakedModel model;

    public Tier2RocketRenderer(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 2F;
    }

    private void updateModel()
    {
        if (this.model == null)
        {
            this.model = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/tier_2_rocket.obj"), ImmutableList.of("Rocket"));
        }
    }

    @Override
    public ResourceLocation getEntityTexture(Tier2RocketEntity entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void render(Tier2RocketEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 180;
        float rollAmplitude = entity.rollAmplitude / 3 - partialTicks;
        RenderSystem.disableRescaleNormal();
        matrixStack.push();
        matrixStack.rotate(new Quaternion(Vector3f.YP, 180.0F - entityYaw, true));
        matrixStack.rotate(new Quaternion(Vector3f.ZN, pitch, true));
        matrixStack.translate(0.0F, entity.getRenderOffsetY(), 0.0F);

        if (rollAmplitude > 0.0F)
        {
            float i = entity.getLaunched() ? (5 - MathHelper.floor(entity.timeUntilLaunch / 85)) / 10F : 0.3F;
            matrixStack.rotate(Vector3f.XP.rotation(MathHelper.sin(rollAmplitude) * rollAmplitude * i * partialTicks));
            matrixStack.rotate(Vector3f.ZP.rotation(MathHelper.sin(rollAmplitude) * rollAmplitude * i * partialTicks));
        }

        this.updateModel();

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
        ClientUtil.drawBakedModel(this.model, buffer, matrixStack, packedLight);
        matrixStack.pop();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public boolean shouldRender(Tier2RocketEntity entity, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(0.6D, 1D, 0.6D);
        return entity.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}