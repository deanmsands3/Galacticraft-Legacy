package micdoodle8.mods.galacticraft.core.client.render.entities;

import org.lwjgl.opengl.GL11;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.entities.BuggyEntity;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
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
public class BuggyRenderer extends EntityRenderer<BuggyEntity>
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
    private static final ResourceLocation OBJ_MODEL = new ResourceLocation(Constants.MOD_ID_CORE, "models/obj/buggy.obj");

    public BuggyRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
        this.shadowRadius = 1.0F;
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        this.mainModel = GCModelCache.INSTANCE.getModel(OBJ_MODEL, ImmutableList.of("MainBody"));
        this.radarDish = GCModelCache.INSTANCE.getModel(OBJ_MODEL, ImmutableList.of("RadarDish_Dish"));
        this.wheelLeftCover = GCModelCache.INSTANCE.getModel(OBJ_MODEL, ImmutableList.of("Wheel_Left_Cover"));
        this.wheelRight = GCModelCache.INSTANCE.getModel(OBJ_MODEL, ImmutableList.of("Wheel_Right"));
        this.wheelLeft = GCModelCache.INSTANCE.getModel(OBJ_MODEL, ImmutableList.of("Wheel_Left"));
        this.wheelRightCover = GCModelCache.INSTANCE.getModel(OBJ_MODEL, ImmutableList.of("Wheel_Right_Cover"));
        this.cargoLeft = GCModelCache.INSTANCE.getModel(OBJ_MODEL, ImmutableList.of("CargoLeft"));
        this.cargoMid = GCModelCache.INSTANCE.getModel(OBJ_MODEL, ImmutableList.of("CargoMid"));
        this.cargoRight = GCModelCache.INSTANCE.getModel(OBJ_MODEL, ImmutableList.of("CargoRight"));
    }

    @Override
    public ResourceLocation getEntityTexture(BuggyEntity entity)
    {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(BuggyEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        float pitch = entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks;
        RenderSystem.disableRescaleNormal();
        matrixStack.pushPose();
        matrixStack.mulPose(new Quaternion(Vector3f.YP, entityYaw, true));
        matrixStack.mulPose(new Quaternion(Vector3f.ZN, pitch, true));
        matrixStack.scale(0.41F, 0.41F, 0.41F);

        if (Minecraft.useAmbientOcclusion())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        // Front wheels
        matrixStack.pushPose();
        float dZ = -2.727F;
        float dY = 0.976F;
        float dX = 1.25F;
        float rotation = entity.wheelRotationX;
        matrixStack.translate(dX, dY, dZ);
        matrixStack.mulPose(new Quaternion(Vector3f.YP, entity.wheelRotationZ, true));
        ClientUtil.drawBakedModel(this.wheelRightCover, buffer, matrixStack, packedLight);
        matrixStack.mulPose(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(this.wheelRight, buffer, matrixStack, packedLight);
        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(-dX, dY, dZ);
        matrixStack.mulPose(new Quaternion(Vector3f.YP, entity.wheelRotationZ, true));
        ClientUtil.drawBakedModel(this.wheelLeftCover, buffer, matrixStack, packedLight);
        matrixStack.mulPose(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(this.wheelLeft, buffer, matrixStack, packedLight);
        matrixStack.popPose();

        // Back wheels
        matrixStack.pushPose();
        dX = 1.9F;
        dZ = -dZ;
        matrixStack.translate(dX, dY, dZ);
        matrixStack.mulPose(new Quaternion(Vector3f.YP, -entity.wheelRotationZ, true));
        matrixStack.mulPose(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(this.wheelRight, buffer, matrixStack, packedLight);
        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(-dX, dY, dZ);
        matrixStack.mulPose(new Quaternion(Vector3f.YP, -entity.wheelRotationZ, true));
        matrixStack.mulPose(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(this.wheelLeft, buffer, matrixStack, packedLight);
        matrixStack.popPose();

        ClientUtil.drawBakedModel(this.mainModel, buffer, matrixStack, packedLight);

        // Radar Dish
        matrixStack.pushPose();
        matrixStack.translate(-1.178F, 4.1F, -2.397F);
        int ticks = entity.tickCount + entity.getId() * 10000;
        matrixStack.mulPose(new Quaternion(Vector3f.XP, (float)Math.sin(ticks * 0.05) * 50.0F, true));
        matrixStack.mulPose(new Quaternion(Vector3f.ZP, (float)Math.cos(ticks * 0.1) * 50.0F, true));
        ClientUtil.drawBakedModel(this.radarDish, buffer, matrixStack, packedLight);
        matrixStack.popPose();

        switch (entity.buggyType)
        {
        case INVENTORY_3:
            ClientUtil.drawBakedModel(this.cargoRight, buffer, matrixStack, packedLight);
        case INVENTORY_2:
            ClientUtil.drawBakedModel(this.cargoMid, buffer, matrixStack, packedLight);
        case INVENTORY_1:
            ClientUtil.drawBakedModel(this.cargoLeft, buffer, matrixStack, packedLight);
        default:
            break;
        }

        matrixStack.popPose();
        Lighting.turnBackOn();
    }

    @Override
    public boolean shouldRender(BuggyEntity entity, Frustum camera, double camX, double camY, double camZ)
    {
        AABB axisalignedbb = entity.getBoundingBox().inflate(2D, 1D, 2D);
        return entity.shouldRender(camX, camY, camZ) && camera.isVisible(axisalignedbb);
    }
}