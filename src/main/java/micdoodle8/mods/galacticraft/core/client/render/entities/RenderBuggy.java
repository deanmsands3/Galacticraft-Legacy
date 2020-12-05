package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderBuggy extends EntityRenderer<EntityBuggy>
{
    private BakedModel mainModel;
    private BakedModel radarDish;
    private BakedModel wheelLeftCover;
    private BakedModel wheelRight;
    private BakedModel wheelLeft;
    private BakedModel wheelRightCover;
    private BakedModel cargoLeft;
    private BakedModel cargoMid;
    private BakedModel cargoRight;

    public RenderBuggy(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 1.0F;
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        mainModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(Constants.MOD_ID_CORE, "models/buggy.obj"), ImmutableList.of("MainBody"));
        mainModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(Constants.MOD_ID_CORE, "models/buggy.obj"), ImmutableList.of("RadarDish_Dish"));
        mainModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(Constants.MOD_ID_CORE, "models/buggy.obj"), ImmutableList.of("Wheel_Left_Cover"));
        mainModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(Constants.MOD_ID_CORE, "models/buggy.obj"), ImmutableList.of("Wheel_Right"));
        mainModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(Constants.MOD_ID_CORE, "models/buggy.obj"), ImmutableList.of("Wheel_Left"));
        mainModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(Constants.MOD_ID_CORE, "models/buggy.obj"), ImmutableList.of("Wheel_Right_Cover"));
        mainModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(Constants.MOD_ID_CORE, "models/buggy.obj"), ImmutableList.of("CargoLeft"));
        mainModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(Constants.MOD_ID_CORE, "models/buggy.obj"), ImmutableList.of("CargoMid"));
        mainModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(Constants.MOD_ID_CORE, "models/buggy.obj"), ImmutableList.of("CargoRight"));
    }

    @Override
    public ResourceLocation getEntityTexture(EntityBuggy entity)
    {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(EntityBuggy entity, float entityYaw, float partialTicks, PoseStack matStack, MultiBufferSource bufferIn, int packedLightIn)
    {
        float pitch = entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks;
        GlStateManager._disableRescaleNormal();
//        GlStateManager.pushMatrix();
        matStack.pushPose();
//        GlStateManager.translatef((float) x, (float) y, (float) z);
//        GlStateManager.scalef(1.0F, 1.0F, 1.0F);
//        GlStateManager.rotatef(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotatef(-pitch, 0.0F, 0.0F, 1.0F);
        matStack.mulPose(new Quaternion(Vector3f.YP, entityYaw, true));
        matStack.mulPose(new Quaternion(Vector3f.ZN, pitch, true));
//        GlStateManager.scalef(0.41F, 0.41F, 0.41F);
        matStack.scale(0.41F, 0.41F, 0.41F);

//        this.updateModels();
//        this.bindEntityTexture(entity);

        if (Minecraft.useAmbientOcclusion())
        {
            GlStateManager._shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager._shadeModel(GL11.GL_FLAT);
        }

        // Front wheels
//        GlStateManager.pushMatrix();
        matStack.pushPose();
        float dZ = -2.727F;
        float dY = 0.976F;
        float dX = 1.25F;
        float rotation = entity.wheelRotationX;
//        GlStateManager.translatef(dX, dY, dZ);
        matStack.translate(dX, dY, dZ);
//        GlStateManager.rotatef(entity.wheelRotationZ, 0, 1, 0);
        matStack.mulPose(new Quaternion(Vector3f.YP, entity.wheelRotationZ, true));
        ClientUtil.drawBakedModel(wheelRightCover, bufferIn, matStack, packedLightIn);
//        GlStateManager.rotatef(rotation, 1, 0, 0);
        matStack.mulPose(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(wheelRight, bufferIn, matStack, packedLightIn);
//        GlStateManager.popMatrix();
        matStack.popPose();

//        GlStateManager.pushMatrix();
        matStack.pushPose();
//        GlStateManager.translatef(-dX, dY, dZ);
        matStack.translate(-dX, dY, dZ);
//        GlStateManager.rotatef(entity.wheelRotationZ, 0, 1, 0);
        matStack.mulPose(new Quaternion(Vector3f.YP, entity.wheelRotationZ, true));
        ClientUtil.drawBakedModel(wheelLeftCover, bufferIn, matStack, packedLightIn);
//        GlStateManager.rotatef(rotation, 1, 0, 0);
        matStack.mulPose(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(wheelLeft, bufferIn, matStack, packedLightIn);
//        GlStateManager.popMatrix();
        matStack.popPose();

        // Back wheels
//        GlStateManager.pushMatrix();
        matStack.pushPose();
        dX = 1.9F;
        dZ = -dZ;
//        GlStateManager.translatef(dX, dY, dZ);
        matStack.translate(dX, dY, dZ);
//        GlStateManager.rotatef(-entity.wheelRotationZ, 0, 1, 0);
        matStack.mulPose(new Quaternion(Vector3f.YP, -entity.wheelRotationZ, true));
//        GlStateManager.rotatef(rotation, 1, 0, 0);
        matStack.mulPose(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(wheelRight, bufferIn, matStack, packedLightIn);
//        GlStateManager.popMatrix();
        matStack.popPose();

//        GlStateManager.pushMatrix();
        matStack.pushPose();
//        GlStateManager.translatef(-dX, dY, dZ);
        matStack.translate(-dX, dY, dZ);
//        GlStateManager.rotatef(-entity.wheelRotationZ, 0, 1, 0);
        matStack.mulPose(new Quaternion(Vector3f.YP, -entity.wheelRotationZ, true));
//        GlStateManager.rotatef(rotation, 1, 0, 0);
        matStack.mulPose(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(wheelLeft, bufferIn, matStack, packedLightIn);
//        GlStateManager.popMatrix();
        matStack.popPose();

        ClientUtil.drawBakedModel(mainModel, bufferIn, matStack, packedLightIn);

        // Radar Dish
//        GlStateManager.pushMatrix();
        matStack.pushPose();
//        GlStateManager.translatef(-1.178F, 4.1F, -2.397F);
        matStack.translate(-1.178F, 4.1F, -2.397F);
        int ticks = entity.tickCount + entity.getId() * 10000;
//        GlStateManager.rotatef((float) Math.sin(ticks * 0.05) * 50.0F, 1, 0, 0);
        matStack.mulPose(new Quaternion(Vector3f.XP, (float)Math.sin(ticks * 0.05) * 50.0F, true));
//        GlStateManager.rotatef((float) Math.cos(ticks * 0.1) * 50.0F, 0, 0, 1);
        matStack.mulPose(new Quaternion(Vector3f.ZP, (float)Math.cos(ticks * 0.1) * 50.0F, true));
        ClientUtil.drawBakedModel(radarDish, bufferIn, matStack, packedLightIn);
//        GlStateManager.popMatrix();
        matStack.popPose();

        switch (entity.buggyType)
        {
        case INVENTORY_3:
            ClientUtil.drawBakedModel(cargoRight, bufferIn, matStack, packedLightIn);
        case INVENTORY_2:
            ClientUtil.drawBakedModel(cargoMid, bufferIn, matStack, packedLightIn);
        case INVENTORY_1:
            ClientUtil.drawBakedModel(cargoLeft, bufferIn, matStack, packedLightIn);
        }

//        GlStateManager.popMatrix();
        matStack.popPose();
        Lighting.turnBackOn();
    }

    @Override
    public boolean shouldRender(EntityBuggy buggy, Frustum camera, double camX, double camY, double camZ)
    {
        AABB axisalignedbb = buggy.getBoundingBox().inflate(2D, 1D, 2D);
        return buggy.shouldRender(camX, camY, camZ) && camera.isVisible(axisalignedbb);
    }
}
