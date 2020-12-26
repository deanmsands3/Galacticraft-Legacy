package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
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
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderBuggy extends EntityRenderer<EntityBuggy>
{
    private IBakedModel mainModel;
    private IBakedModel radarDish;
    private IBakedModel wheelLeftCover;
    private IBakedModel wheelRight;
    private IBakedModel wheelLeft;
    private IBakedModel wheelRightCover;
    private IBakedModel cargoLeft;
    private IBakedModel cargoMid;
    private IBakedModel cargoRight;

    public RenderBuggy(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 1.0F;
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        ResourceLocation buggy = new ResourceLocation(Constants.MOD_ID_CORE, "models/obj/buggy.obj");
        mainModel = GCModelCache.INSTANCE.getModel(buggy, ImmutableList.of("MainBody"));
        mainModel = GCModelCache.INSTANCE.getModel(buggy, ImmutableList.of("RadarDish_Dish"));
        mainModel = GCModelCache.INSTANCE.getModel(buggy, ImmutableList.of("Wheel_Left_Cover"));
        mainModel = GCModelCache.INSTANCE.getModel(buggy, ImmutableList.of("Wheel_Right"));
        mainModel = GCModelCache.INSTANCE.getModel(buggy, ImmutableList.of("Wheel_Left"));
        mainModel = GCModelCache.INSTANCE.getModel(buggy, ImmutableList.of("Wheel_Right_Cover"));
        mainModel = GCModelCache.INSTANCE.getModel(buggy, ImmutableList.of("CargoLeft"));
        mainModel = GCModelCache.INSTANCE.getModel(buggy, ImmutableList.of("CargoMid"));
        mainModel = GCModelCache.INSTANCE.getModel(buggy, ImmutableList.of("CargoRight"));
    }

    @Override
    public ResourceLocation getEntityTexture(EntityBuggy entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void render(EntityBuggy entity, float entityYaw, float partialTicks, MatrixStack matStack, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        GlStateManager.disableRescaleNormal();
//        GlStateManager.pushMatrix();
        matStack.push();
//        GlStateManager.translatef((float) x, (float) y, (float) z);
//        GlStateManager.scalef(1.0F, 1.0F, 1.0F);
//        GlStateManager.rotatef(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotatef(-pitch, 0.0F, 0.0F, 1.0F);
        matStack.rotate(new Quaternion(Vector3f.YP, entityYaw, true));
        matStack.rotate(new Quaternion(Vector3f.ZN, pitch, true));
//        GlStateManager.scalef(0.41F, 0.41F, 0.41F);
        matStack.scale(0.41F, 0.41F, 0.41F);

//        this.updateModels();
//        this.bindEntityTexture(entity);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        // Front wheels
//        GlStateManager.pushMatrix();
        matStack.push();
        float dZ = -2.727F;
        float dY = 0.976F;
        float dX = 1.25F;
        float rotation = entity.wheelRotationX;
//        GlStateManager.translatef(dX, dY, dZ);
        matStack.translate(dX, dY, dZ);
//        GlStateManager.rotatef(entity.wheelRotationZ, 0, 1, 0);
        matStack.rotate(new Quaternion(Vector3f.YP, entity.wheelRotationZ, true));
        ClientUtil.drawBakedModel(wheelRightCover, bufferIn, matStack, packedLightIn);
//        GlStateManager.rotatef(rotation, 1, 0, 0);
        matStack.rotate(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(wheelRight, bufferIn, matStack, packedLightIn);
//        GlStateManager.popMatrix();
        matStack.pop();

//        GlStateManager.pushMatrix();
        matStack.push();
//        GlStateManager.translatef(-dX, dY, dZ);
        matStack.translate(-dX, dY, dZ);
//        GlStateManager.rotatef(entity.wheelRotationZ, 0, 1, 0);
        matStack.rotate(new Quaternion(Vector3f.YP, entity.wheelRotationZ, true));
        ClientUtil.drawBakedModel(wheelLeftCover, bufferIn, matStack, packedLightIn);
//        GlStateManager.rotatef(rotation, 1, 0, 0);
        matStack.rotate(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(wheelLeft, bufferIn, matStack, packedLightIn);
//        GlStateManager.popMatrix();
        matStack.pop();

        // Back wheels
//        GlStateManager.pushMatrix();
        matStack.push();
        dX = 1.9F;
        dZ = -dZ;
//        GlStateManager.translatef(dX, dY, dZ);
        matStack.translate(dX, dY, dZ);
//        GlStateManager.rotatef(-entity.wheelRotationZ, 0, 1, 0);
        matStack.rotate(new Quaternion(Vector3f.YP, -entity.wheelRotationZ, true));
//        GlStateManager.rotatef(rotation, 1, 0, 0);
        matStack.rotate(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(wheelRight, bufferIn, matStack, packedLightIn);
//        GlStateManager.popMatrix();
        matStack.pop();

//        GlStateManager.pushMatrix();
        matStack.push();
//        GlStateManager.translatef(-dX, dY, dZ);
        matStack.translate(-dX, dY, dZ);
//        GlStateManager.rotatef(-entity.wheelRotationZ, 0, 1, 0);
        matStack.rotate(new Quaternion(Vector3f.YP, -entity.wheelRotationZ, true));
//        GlStateManager.rotatef(rotation, 1, 0, 0);
        matStack.rotate(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(wheelLeft, bufferIn, matStack, packedLightIn);
//        GlStateManager.popMatrix();
        matStack.pop();

        ClientUtil.drawBakedModel(mainModel, bufferIn, matStack, packedLightIn);

        // Radar Dish
//        GlStateManager.pushMatrix();
        matStack.push();
//        GlStateManager.translatef(-1.178F, 4.1F, -2.397F);
        matStack.translate(-1.178F, 4.1F, -2.397F);
        int ticks = entity.ticksExisted + entity.getEntityId() * 10000;
//        GlStateManager.rotatef((float) Math.sin(ticks * 0.05) * 50.0F, 1, 0, 0);
        matStack.rotate(new Quaternion(Vector3f.XP, (float)Math.sin(ticks * 0.05) * 50.0F, true));
//        GlStateManager.rotatef((float) Math.cos(ticks * 0.1) * 50.0F, 0, 0, 1);
        matStack.rotate(new Quaternion(Vector3f.ZP, (float)Math.cos(ticks * 0.1) * 50.0F, true));
        ClientUtil.drawBakedModel(radarDish, bufferIn, matStack, packedLightIn);
//        GlStateManager.popMatrix();
        matStack.pop();

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
        matStack.pop();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public boolean shouldRender(EntityBuggy buggy, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = buggy.getBoundingBox().grow(2D, 1D, 2D);
        return buggy.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
