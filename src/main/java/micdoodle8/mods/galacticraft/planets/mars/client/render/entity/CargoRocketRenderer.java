package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.entities.CargoRocketEntity;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CargoRocketRenderer extends EntityRenderer<CargoRocketEntity>
{
    private IBakedModel rocketModel;

    public CargoRocketRenderer(EntityRendererManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.5F;
        GCModelCache.INSTANCE.reloadCallback(this::updateModel);
    }

    private void updateModel()
    {
        if (this.rocketModel == null)
        {
            this.rocketModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/cargo_rocket.obj"), ImmutableList.of("Rocket"));
        }
    }

    @Override
    public ResourceLocation getEntityTexture(CargoRocketEntity entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void render(CargoRocketEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        RenderSystem.disableRescaleNormal();
        matrixStack.push();
        matrixStack.translate(0, entity.getRenderOffsetY(), 0);
        matrixStack.scale(0.4F, 0.4F, 0.4F);
        matrixStack.rotate(new Quaternion(Vector3f.YP, 180.0F - entityYaw, true));
        matrixStack.rotate(new Quaternion(Vector3f.ZN, pitch, true));

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        ClientUtil.drawBakedModel(this.rocketModel, buffer, matrixStack, packedLight);
        matrixStack.pop();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public boolean shouldRender(CargoRocketEntity entity, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox();
        return entity.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}