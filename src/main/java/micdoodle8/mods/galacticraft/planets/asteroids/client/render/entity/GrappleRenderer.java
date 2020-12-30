package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.GrappleEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class GrappleRenderer extends EntityRenderer<GrappleEntity>
{
    private IBakedModel model;

    public GrappleRenderer(EntityRendererManager manager)
    {
        super(manager);
        GCModelCache.INSTANCE.reloadCallback(this::updateModel);
    }

    public void updateModel()
    {
        this.model = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/grapple.obj"), ImmutableList.of("Grapple"));
    }

    @Override
    public void render(GrappleEntity grapple, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        RenderSystem.disableRescaleNormal();
        matrixStack.push();

        Vec3d vec3 = new Vec3d(0.0D, -0.2D, 0.0D);
        PlayerEntity shootingEntity = grapple.getShootingEntity();

        if (shootingEntity != null && grapple.getPullingEntity())
        {
            double d3 = shootingEntity.prevPosX + (shootingEntity.getPosX() - shootingEntity.prevPosX) * partialTicks + vec3.x;
            double d4 = shootingEntity.prevPosY + (shootingEntity.getPosY() - shootingEntity.prevPosY) * partialTicks + vec3.y;
            double d5 = shootingEntity.prevPosZ + (shootingEntity.getPosZ() - shootingEntity.prevPosZ) * partialTicks + vec3.z;

            Tessellator tessellator = Tessellator.getInstance();
            RenderSystem.disableTexture();
            RenderSystem.disableLighting();
            tessellator.getBuffer().begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            byte b2 = 16;

            double d14 = grapple.prevPosX + (grapple.getPosX() - grapple.prevPosX) * partialTicks;
            double d8 = grapple.prevPosY + (grapple.getPosY() - grapple.prevPosY) * partialTicks + 0.25D;
            double d10 = grapple.prevPosZ + (grapple.getPosZ() - grapple.prevPosZ) * partialTicks;
            double d11 = (float) (d3 - d14);
            double d12 = (float) (d4 - d8);
            double d13 = (float) (d5 - d10);
            //            tessellator.getBuffer().setTranslation(0, -0.2F, 0);

            for (int i = 0; i <= b2; ++i)
            {
                float f12 = (float) i / (float) b2;
                tessellator.getBuffer().pos(d11 * f12, d12 * (f12 * f12 + f12) * 0.5D + 0.15D, d13 * f12).color(203.0F / 255.0F, 203.0F / 255.0F, 192.0F / 255.0F, 1.0F).endVertex();
            }

            tessellator.draw();
            //            tessellator.getBuffer().setTranslation(0, 0, 0);
            RenderSystem.enableLighting();
            RenderSystem.enableTexture();
        }

        matrixStack.rotate(new Quaternion(Vector3f.YP, grapple.prevRotationYaw + (grapple.rotationYaw - grapple.prevRotationYaw) * partialTicks - 90.0F, true));
        matrixStack.rotate(new Quaternion(Vector3f.ZP, grapple.prevRotationPitch + (grapple.rotationPitch - grapple.prevRotationPitch) * partialTicks - 180, true));
        matrixStack.rotate(new Quaternion(Vector3f.XP, grapple.prevRotationRoll + (grapple.rotationRoll - grapple.prevRotationRoll) * partialTicks, true));

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        ClientUtil.drawBakedModel(this.model, buffer, matrixStack, packedLight);

        //        this.bindEntityTexture(grapple);
        //        ItemRendererGrappleHook.modelGrapple.renderAll(); TODO

        matrixStack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(GrappleEntity entity)
    {
        return new ResourceLocation("missing");
    }

    @Override
    public boolean shouldRender(GrappleEntity entity, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        return entity.isInRangeToRender3d(camX, camY, camZ);
    }
}