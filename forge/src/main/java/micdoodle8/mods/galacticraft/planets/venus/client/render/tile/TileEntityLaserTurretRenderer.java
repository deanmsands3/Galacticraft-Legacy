package micdoodle8.mods.galacticraft.planets.venus.client.render.tile;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityLaserTurret;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class TileEntityLaserTurretRenderer extends BlockEntityRenderer<TileEntityLaserTurret>
{
    public static BakedModel laserBase;
    public static BakedModel laserPhalange;
    public static BakedModel laserPhalangeAxle;
    public static BakedModel laserTurrets;
    public static BakedModel laserTurretsOff;
    public static BakedModel orb1;
    public static BakedModel orb2;

    public TileEntityLaserTurretRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        ResourceLocation laser = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/laser_turret.obj");
        ResourceLocation orb = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/laser_turret_orb.obj");
        laserBase = GCModelCache.INSTANCE.getModel(laser, ImmutableList.of("baseConnector0", "baseConnector1"));
        laserPhalange = GCModelCache.INSTANCE.getModel(laser, ImmutableList.of("phalange"));
        laserPhalangeAxle = GCModelCache.INSTANCE.getModel(laser, ImmutableList.of("phalangeAxle"));
        laserTurrets = GCModelCache.INSTANCE.getModel(laser, ImmutableList.of("turretLeft", "turretRight"));
        laserTurretsOff = GCModelCache.INSTANCE.getModel(laser, ImmutableList.of("turretLeft_off", "turretRight_off"));
        orb1 = GCModelCache.INSTANCE.getModel(orb, ImmutableList.of("inner_Icosphere"));
        orb2 = GCModelCache.INSTANCE.getModel(orb, ImmutableList.of("inner_Icosphere.001"));
    }

    @Override
    public void render(TileEntityLaserTurret tile, float partialTicks, PoseStack matStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        matStack.pushPose();
//        GlStateManager.pushMatrix();
        matStack.translate(0.5F, 1.5F, 0.5F);
//        GlStateManager.translatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        matStack.pushPose();
//        GlStateManager.pushMatrix();
        Lighting.turnOff();

        combinedLightIn = LevelRenderer.getLightColor(tile.getLevel(), tile.getBlockPos().above());

//        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

        if (Minecraft.useAmbientOcclusion())
        {
            GlStateManager._shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager._shadeModel(GL11.GL_FLAT);
        }

        matStack.scale(1 / 16.0F, 1 / 16.0F, 1 / 16.0F);
//        GlStateManager.scalef(1 / 16.0F, 1 / 16.0F, 1 / 16.0F);

//        GlStateManager.pushMatrix();
        matStack.pushPose();
//        GlStateManager.scalef(0.9F, 1.0F, 0.9F);
        matStack.scale(0.9F, 1.0F, 0.9F);
        ClientUtil.drawBakedModel(laserBase, bufferIn, matStack, combinedLightIn);
//        GlStateManager.popMatrix();
        matStack.popPose();

//        GlStateManager.rotatef(tile.yaw, 0.0F, 1.0F, 0.0F);
        matStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), tile.yaw, true));

        // Interpolate between yaw and targetYaw

        float partialRot = Math.signum(tile.pitch) * tile.pitch * (tile.pitch / 120.0F);
//        GlStateManager.rotatef(partialRot, 0.0F, 0.0F, -1.0F);
        matStack.mulPose(new Quaternion(new Vector3f(0.0F, 0.0F, -1.0F), partialRot, true));

//        GlStateManager.pushMatrix();
        matStack.pushPose();
//        GlStateManager.scalef(1.1F, 1.0F, 1.0F);
        matStack.scale(1.1F, 1.0F, 1.0F);

        ClientUtil.drawBakedModel(laserPhalange, bufferIn, matStack, combinedLightIn);
//        GlStateManager.popMatrix();
        matStack.popPose();

//        GlStateManager.rotatef(tile.pitch - partialRot, 0.0F, 0.0F, -1.0F);
        matStack.mulPose(new Quaternion(new Vector3f(0.0F, 0.0F, -1.0F), tile.pitch - partialRot, true));

        ClientUtil.drawBakedModel(laserPhalangeAxle, bufferIn, matStack, combinedLightIn);
        ClientUtil.drawBakedModel(tile.active ? laserTurrets : laserTurretsOff, bufferIn, matStack, combinedLightIn);

//        GlStateManager.popMatrix();
        matStack.popPose();
        Tesselator tess = Tesselator.getInstance();

        float inv = (float) (Math.pow(tile.chargeLevel / 5.0F + 1.0F, 2.5F) * 1.0F);
        float invNext = (float) (Math.pow((tile.chargeLevel + 1) / 5.0F + 1.0F, 2.5F) * 1.0F);
        float rotate = inv + (invNext - inv) * partialTicks;

//        float lightMapSaveX = GLX.lastBrightnessX;
//        float lightMapSaveY = GLX.lastBrightnessY;
//        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);

        if (tile.chargeLevel > 0)
        {
//            GlStateManager.pushMatrix();
            matStack.pushPose();
            GlStateManager._enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//            GlStateManager.rotatef(tile.yaw, 0.0F, 1.0F, 0.0F);
            matStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), tile.yaw, true));
//            GlStateManager.rotatef(tile.pitch, 0.0F, 0.0F, -1.0F);
            matStack.mulPose(new Quaternion(new Vector3f(0.0F, 0.0F, -1.0F), tile.pitch, true));
//            GlStateManager.translatef(-0.6F, 0.28F, 0.0F);
            matStack.translate(-0.6F, 0.28F, 0.0F);

            GlStateManager._disableTexture();

            tess.getBuilder().begin(GL11.GL_LINE_LOOP, DefaultVertexFormat.POSITION_COLOR);

            tess.getBuilder().vertex(0.09F, 0.0F, 0.275F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
            tess.getBuilder().vertex(0.0F, 0.0F, 0.0F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
            tess.getBuilder().vertex(0.09F, 0.0F, -0.275F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();

            tess.end();
            GlStateManager._enableTexture();

            float scale = tile.chargeLevel / 600.0F;
//            GlStateManager.scalef(0.01F + scale, 0.01F + scale, 0.01F + scale);
            matStack.scale(0.01F + scale, 0.01F + scale, 0.01F + scale);
//            GlStateManager.rotatef(rotate, 0.0F, 1.0F, 0.0F);
            matStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), rotate, true));
            ClientUtil.drawBakedModel(orb1, bufferIn, matStack, Constants.PACKED_LIGHT_FULL_BRIGHT);
//            GlStateManager.rotatef(rotate, 0.0F, 1.0F, 0.0F);
            matStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), rotate, true));
            ClientUtil.drawBakedModel(orb2, bufferIn, matStack, Constants.PACKED_LIGHT_FULL_BRIGHT);
            GlStateManager._disableBlend();
//            GlStateManager.popMatrix();
            matStack.popPose();
        }

        if (tile.timeSinceShot > 0 && tile.timeSinceShot < 5)
        {
            Entity e = tile.getLevel().getEntity(tile.targettedEntity);

            if (e != null)
            {
                GlStateManager._disableTexture();
//                GlStateManager.pushMatrix();
                matStack.pushPose();
                GlStateManager._enableBlend();
                GlStateManager._disableCull();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//                GlStateManager.rotatef(tile.yaw, 0.0F, 1.0F, 0.0F);
                matStack.mulPose(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), tile.yaw, true));
//                GlStateManager.rotatef(tile.pitch, 0.0F, 0.0F, -1.0F);
                matStack.mulPose(new Quaternion(new Vector3f(0.0F, 0.0F, -1.0F), tile.pitch, true));
//                GlStateManager.translatef(-0.6F, 0.28F, 0.0F);
                matStack.translate(-0.6F, 0.28F, 0.0F);

                BufferBuilder bb = tess.getBuilder();

                bb.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR);

                Vector3D vec = new Vector3D(e.getX(), e.getY() + e.getEyeHeight(), e.getZ());
                vec.translate(new Vector3D(-(tile.getBlockPos().getX() + 0.5F), -(tile.getBlockPos().getY() + 1.78F), -(tile.getBlockPos().getZ() + 0.5F)));
                float dist = (float) vec.getMagnitude() - 0.8F;
                float shotTimer = (float) (Math.pow((5.0F - tile.timeSinceShot) / 5.0F + 1.0F, 2.5F) * 0.5F);
                float shotTimerNext = (float) (Math.pow((5.0F - (tile.timeSinceShot + 1)) / 5.0F + 1.0F, 2.5F) * 0.5F);
                float fade = shotTimer + (shotTimerNext - shotTimer) * partialTicks;
                float yMin = -0.101F * fade;
                float yMax = 0.101F * fade;
                float yMin1 = -0.15F * fade;
                float yMax1 = 0.15F * fade;

                tess.getBuilder().vertex(0.0F, yMax, yMax).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuilder().vertex(0.0F, yMin, yMin).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuilder().vertex(-dist, yMin * 0.4F, yMin * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuilder().vertex(-dist, yMax * 0.4F, yMax * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();

                tess.getBuilder().vertex(0.0F, yMax, yMin).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuilder().vertex(0.0F, yMin, yMax).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuilder().vertex(-dist, yMin * 0.4F, yMax * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuilder().vertex(-dist, yMax * 0.4F, yMin * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();

                tess.getBuilder().vertex(0.0F, yMax1, yMax1).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuilder().vertex(0.0F, yMin1, yMin1).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuilder().vertex(-dist, yMin1 * 0.4F, yMin1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuilder().vertex(-dist, yMax1 * 0.4F, yMax1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();

                tess.getBuilder().vertex(0.0F, yMax1, yMin1).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuilder().vertex(0.0F, yMin1, yMax1).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuilder().vertex(-dist, yMin1 * 0.4F, yMax1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuilder().vertex(-dist, yMax1 * 0.4F, yMin1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).uv2(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();

                tess.end();

                GlStateManager._enableCull();
                GlStateManager._enableTexture();
                matStack.popPose();
//                GlStateManager.popMatrix();
            }
        }
//        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lightMapSaveX, lightMapSaveY);
//        GlStateManager.popMatrix();
        matStack.popPose();
    }
}
