package team.galacticraft.galacticraft.common.core.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GCBlocks;
import team.galacticraft.galacticraft.core.blocks.BlockEmergencyBox;
import team.galacticraft.galacticraft.core.tile.TileEntityEmergencyBox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class TileEntityEmergencyBoxRenderer extends BlockEntityRenderer<TileEntityEmergencyBox>
{
    private static final float MASKSCALE = 3F;

    public TileEntityEmergencyBoxRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    public class Flap extends Model
    {
        ModelPart model;
        protected float angle;

        public Flap()
        {
            super(RenderType::entitySolid);
            this.angle = 0.0F;
            this.texWidth = 32;
            this.texHeight = 32;
            this.model = new ModelPart(this, 0, 0);
            this.model.addBox(-6F, -6F, 0F, 12, 6, 1);
            this.model.setPos(0F, 6F, -7F);
            this.model.setTexSize(this.texWidth, this.texHeight);
            this.model.mirror = true;
        }

        private void setRotation(ModelPart model, float x, float y, float z)
        {
            model.xRot = x;
            model.yRot = y;
            model.zRot = z;
        }

        @Override
        public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            this.setRotation(this.model, angle / Constants.RADIANS_TO_DEGREES, 0F, 0F);
            model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }

    public class Plinth extends Model
    {
        ModelPart model;

        public Plinth()
        {
            super(RenderType::entitySolid);
            this.texWidth = 16;
            this.texHeight = 16;
            this.model = new ModelPart(this, 0, 0);
            this.model.addBox(-6F, -7F, -6F, 12, 1, 12);
            this.model.setPos(0F, 0F, 0F);
            this.model.setTexSize(this.texWidth, this.texHeight);
            this.model.mirror = true;
        }

        @Override
        public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }

        public void setHeight(float height)
        {
            this.model.setPos(0F, height, 0F);
        }

//        public void render(float height)
//        {
//            this.model.setRotationPoint(0F, height, 0F);
//            this.model.render(1F / 16F);
//        }
    }

    public class Mask extends Model
    {
        ModelPart model;

        public Mask()
        {
            super(RenderType::entityCutout);
            this.texWidth = 128;
            this.texHeight = 64;
            this.model = new ModelPart(this, 0, 0);
            this.model.addBox(-8.0F, -4F, -8.0F, 16, 16, 16, 1.0F);
            this.model.setPos(0F, 0F, 0F);
            this.model.setTexSize(this.texWidth, this.texHeight);
            this.model.mirror = true;
        }

        @Override
        public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            matrixStackIn.pushPose();
            float scale = 1.0F / MASKSCALE;
            matrixStackIn.scale(scale, scale, scale);
            model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStackIn.popPose();
        }

        public void setHeight(float height)
        {
            this.model.setPos(0F, height * MASKSCALE, 0F);
        }

//        public void render(float height)
//        {
//            this.model.setRotationPoint(0F, height * MASKSCALE, 0F);
//            this.model.render(1F / 16F / MASKSCALE);
//        }
    }

    public class Tank extends Model
    {
        ModelPart model;

        public Tank()
        {
            super(RenderType::entitySolid);
            this.texWidth = 128;
            this.texHeight = 64;
            this.model = new ModelPart(this, 0, 0);
            this.model.texOffs(4, 0);   // Green tank
            this.model.addBox(-1.5F, 0F, -1.5F, 3, 7, 3, 1.0F);
            this.model.setPos(0F, 0F, 0F);
            this.model.setTexSize(this.texWidth, this.texHeight);
            this.model.mirror = true;
        }

        @Override
        public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            matrixStackIn.pushPose();
            float scale = 1.0F / MASKSCALE;
            matrixStackIn.scale(scale, scale, scale);
            model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStackIn.popPose();
        }

        public void setHeight(float height)
        {
            this.model.setPos(0F, height * MASKSCALE, 0F);
        }

//        public void render(float height)
//        {
//            this.model.setRotationPoint(0F, height * MASKSCALE, 0F);
//            this.model.render(1F / 16F / MASKSCALE);
//        }
    }

    public class Pack extends Model
    {
        ModelPart model;

        public Pack()
        {
            super(RenderType::entitySolid);
            this.texWidth = 256;
            this.texHeight = 256;
            this.model = new ModelPart(this, 0, 0);
            this.model.texOffs(50, 50);
            this.model.addBox(-6F, -11F, -10F, 12, 1, 20, 1.0F);
            this.model.setPos(0F, 0F, 0F);
            this.model.setTexSize(this.texWidth, this.texHeight);
            this.model.mirror = true;
        }

        @Override
        public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStackIn.popPose();
        }

        public void setHeight(float height)
        {
            this.model.setPos(0F, height * 2F, 0F);
        }
    }

    private static final ResourceLocation boxTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/blocks/emergency_box.png");
    private static final ResourceLocation flapTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/blocks/emergency_box_flap.png");
    private static final ResourceLocation packTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/red.png");
    private static final ResourceLocation oxygenMaskTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/oxygen.png");
    private static final ResourceLocation oxygenTankTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/player.png");
    private final Flap flapA = new Flap();
    private final Flap flapB = new Flap();
    private final Flap flapC = new Flap();
    private final Flap flapD = new Flap();
    private final Plinth plat = new Plinth();
    private final Mask mask = new Mask();
    private final Tank tank = new Tank();
    private final Pack pack = new Pack();

    @Override
    public void render(TileEntityEmergencyBox emergencyBox, float partialTicks, PoseStack matStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        BlockState state = emergencyBox.getBlockState();
        if (!(state.getBlock() instanceof BlockEmergencyBox))
        {
            return;
        }
//        GlStateManager.pushMatrix();
        matStack.pushPose();
//        GlStateManager.translatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        matStack.translate(0.5F, 0.5F, 0.5F);

        flapA.angle = emergencyBox.getAngleA(partialTicks);
        flapB.angle = emergencyBox.getAngleB(partialTicks);
        flapC.angle = emergencyBox.getAngleC(partialTicks);
        flapD.angle = emergencyBox.getAngleD(partialTicks);
        float height = Math.max(Math.max(flapA.angle, flapB.angle), Math.max(flapC.angle, flapD.angle)) / 90F;

        if (height > 0F && state.getBlock() == GCBlocks.emergencyBoxKit)
        {
//            GlStateManager.pushMatrix();
            matStack.pushPose();
            RenderType renderType = RenderType.entitySolid(packTexture);
            VertexConsumer builder = bufferIn.getBuffer(renderType);
            this.pack.setHeight(height);
            this.pack.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//            GlStateManager.rotatef(180F, 1F, 0F, 0F);
            matStack.mulPose(new Quaternion(Vector3f.XP, 180.0F, true));
//            GlStateManager.translatef(0.0F, 0.0F, -0.07F);
            matStack.translate(0.0F, 0.0F, -0.07F);
            renderType = RenderType.entityCutout(oxygenMaskTexture);
            builder = bufferIn.getBuffer(renderType);
            this.mask.setHeight(-height);
            this.mask.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            renderType = RenderType.entitySolid(oxygenTankTexture);
            builder = bufferIn.getBuffer(renderType);
            this.mask.setHeight(-height);
//            this.mask.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//            GlStateManager.translatef(0.1F, 0.11F, 0.3F);
            matStack.translate(0.1F, 0.11F, 0.3F);
            this.tank.setHeight(-height);
            this.tank.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//            GlStateManager.translatef(-0.2F, 0F, 0F);
            matStack.translate(-0.2F, 0.0F, 0.0F);
            this.tank.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//            GlStateManager.popMatrix();
            matStack.popPose();
        }

        RenderType renderType = RenderType.entitySolid(boxTexture);
        VertexConsumer builder = bufferIn.getBuffer(renderType);
        this.plat.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        renderType = RenderType.entitySolid(flapTexture);
        builder = bufferIn.getBuffer(renderType);
        this.flapA.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//        GlStateManager.rotatef(90F, 0, 1F, 0F);
        matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
        this.flapB.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
        this.flapC.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
        this.flapD.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.mulPose(new Quaternion(Vector3f.XP, 180.0F, true));
        this.flapB.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
        this.flapA.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
        this.flapD.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
        this.flapC.renderToBuffer(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//        GlStateManager.popMatrix();
        matStack.popPose();
    }
}
