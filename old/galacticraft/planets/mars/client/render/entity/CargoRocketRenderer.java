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
import micdoodle8.mods.galacticraft.planets.mars.entities.CargoRocketEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CargoRocketRenderer extends EntityRenderer<CargoRocketEntity>
{
    private BakedModel rocketModel;

    public CargoRocketRenderer(EntityRenderDispatcher renderManager)
    {
        super(renderManager);
        this.shadowRadius = 0.5F;
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
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(CargoRocketEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        float pitch = entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks;
        RenderSystem.disableRescaleNormal();
        matrixStack.pushPose();
        matrixStack.translate(0, entity.getRenderOffsetY(), 0);
        matrixStack.scale(0.4F, 0.4F, 0.4F);
        matrixStack.mulPose(new Quaternion(Vector3f.YP, 180.0F - entityYaw, true));
        matrixStack.mulPose(new Quaternion(Vector3f.ZN, pitch, true));

        if (Minecraft.useAmbientOcclusion())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        ClientUtil.drawBakedModel(this.rocketModel, buffer, matrixStack, packedLight);
        matrixStack.popPose();
        Lighting.turnBackOn();
    }

    @Override
    public boolean shouldRender(CargoRocketEntity entity, Frustum camera, double camX, double camY, double camZ)
    {
        AABB axisalignedbb = entity.getBoundingBox();
        return entity.shouldRender(camX, camY, camZ) && camera.isVisible(axisalignedbb);
    }
}