package micdoodle8.mods.galacticraft.core.client.render.entities;

import org.lwjgl.opengl.GL11;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.entities.BuggyEntity;
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

@OnlyIn(Dist.CLIENT)
public class BuggyRenderer extends EntityRenderer<BuggyEntity>
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
    private static final ResourceLocation OBJ_MODEL = new ResourceLocation(Constants.MOD_ID_CORE, "models/obj/buggy.obj");

    public BuggyRenderer(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 1.0F;
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
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void render(BuggyEntity entity, float entityYaw, float partialTicks, MatrixStack matStack, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        RenderSystem.disableRescaleNormal();
        matStack.push();
        matStack.rotate(new Quaternion(Vector3f.YP, entityYaw, true));
        matStack.rotate(new Quaternion(Vector3f.ZN, pitch, true));
        matStack.scale(0.41F, 0.41F, 0.41F);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        // Front wheels
        matStack.push();
        float dZ = -2.727F;
        float dY = 0.976F;
        float dX = 1.25F;
        float rotation = entity.wheelRotationX;
        matStack.translate(dX, dY, dZ);
        matStack.rotate(new Quaternion(Vector3f.YP, entity.wheelRotationZ, true));
        ClientUtil.drawBakedModel(this.wheelRightCover, bufferIn, matStack, packedLightIn);
        matStack.rotate(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(this.wheelRight, bufferIn, matStack, packedLightIn);
        matStack.pop();

        matStack.push();
        matStack.translate(-dX, dY, dZ);
        matStack.rotate(new Quaternion(Vector3f.YP, entity.wheelRotationZ, true));
        ClientUtil.drawBakedModel(this.wheelLeftCover, bufferIn, matStack, packedLightIn);
        matStack.rotate(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(this.wheelLeft, bufferIn, matStack, packedLightIn);
        matStack.pop();

        // Back wheels
        matStack.push();
        dX = 1.9F;
        dZ = -dZ;
        matStack.translate(dX, dY, dZ);
        matStack.rotate(new Quaternion(Vector3f.YP, -entity.wheelRotationZ, true));
        matStack.rotate(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(this.wheelRight, bufferIn, matStack, packedLightIn);
        matStack.pop();

        matStack.push();
        matStack.translate(-dX, dY, dZ);
        matStack.rotate(new Quaternion(Vector3f.YP, -entity.wheelRotationZ, true));
        matStack.rotate(new Quaternion(Vector3f.XP, rotation, true));
        ClientUtil.drawBakedModel(this.wheelLeft, bufferIn, matStack, packedLightIn);
        matStack.pop();

        ClientUtil.drawBakedModel(this.mainModel, bufferIn, matStack, packedLightIn);

        // Radar Dish
        matStack.push();
        matStack.translate(-1.178F, 4.1F, -2.397F);
        int ticks = entity.ticksExisted + entity.getEntityId() * 10000;
        matStack.rotate(new Quaternion(Vector3f.XP, (float)Math.sin(ticks * 0.05) * 50.0F, true));
        matStack.rotate(new Quaternion(Vector3f.ZP, (float)Math.cos(ticks * 0.1) * 50.0F, true));
        ClientUtil.drawBakedModel(this.radarDish, bufferIn, matStack, packedLightIn);
        matStack.pop();

        switch (entity.buggyType)
        {
        case INVENTORY_3:
            ClientUtil.drawBakedModel(this.cargoRight, bufferIn, matStack, packedLightIn);
        case INVENTORY_2:
            ClientUtil.drawBakedModel(this.cargoMid, bufferIn, matStack, packedLightIn);
        case INVENTORY_1:
            ClientUtil.drawBakedModel(this.cargoLeft, bufferIn, matStack, packedLightIn);
        default:
            break;
        }

        matStack.pop();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public boolean shouldRender(BuggyEntity entity, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(2D, 1D, 2D);
        return entity.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}