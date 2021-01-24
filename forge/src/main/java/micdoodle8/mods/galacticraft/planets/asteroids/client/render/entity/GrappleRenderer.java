package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.GrappleEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class GrappleRenderer extends EntityRenderer<GrappleEntity>
{
    private BakedModel model;

    public GrappleRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
        GCModelCache.INSTANCE.reloadCallback(this::updateModel);
    }

    public void updateModel()
    {
        this.model = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/grapple.obj"), ImmutableList.of("Grapple"));
    }

    @Override
    public void render(GrappleEntity grapple, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        RenderSystem.disableRescaleNormal();
        matrixStack.pushPose();

        Vec3 vec3 = new Vec3(0.0D, -0.2D, 0.0D);
        Player shootingEntity = grapple.getShootingEntity();

        if (shootingEntity != null && grapple.getPullingEntity())
        {
            double d3 = shootingEntity.xo + (shootingEntity.getX() - shootingEntity.xo) * partialTicks + vec3.x;
            double d4 = shootingEntity.yo + (shootingEntity.getY() - shootingEntity.yo) * partialTicks + vec3.y;
            double d5 = shootingEntity.zo + (shootingEntity.getZ() - shootingEntity.zo) * partialTicks + vec3.z;

            Tesselator tessellator = Tesselator.getInstance();
            RenderSystem.disableTexture();
            RenderSystem.disableLighting();
            tessellator.getBuilder().begin(GL11.GL_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
            byte b2 = 16;

            double d14 = grapple.xo + (grapple.getX() - grapple.xo) * partialTicks;
            double d8 = grapple.yo + (grapple.getY() - grapple.yo) * partialTicks + 0.25D;
            double d10 = grapple.zo + (grapple.getZ() - grapple.zo) * partialTicks;
            double d11 = (float) (d3 - d14);
            double d12 = (float) (d4 - d8);
            double d13 = (float) (d5 - d10);
            //            tessellator.getBuffer().setTranslation(0, -0.2F, 0);

            for (int i = 0; i <= b2; ++i)
            {
                float f12 = (float) i / (float) b2;
                tessellator.getBuilder().vertex(d11 * f12, d12 * (f12 * f12 + f12) * 0.5D + 0.15D, d13 * f12).color(203.0F / 255.0F, 203.0F / 255.0F, 192.0F / 255.0F, 1.0F).endVertex();
            }

            tessellator.end();
            //            tessellator.getBuffer().setTranslation(0, 0, 0);
            RenderSystem.enableLighting();
            RenderSystem.enableTexture();
        }

        matrixStack.mulPose(new Quaternion(Vector3f.YP, grapple.yRotO + (grapple.yRot - grapple.yRotO) * partialTicks - 90.0F, true));
        matrixStack.mulPose(new Quaternion(Vector3f.ZP, grapple.xRotO + (grapple.xRot - grapple.xRotO) * partialTicks - 180, true));
        matrixStack.mulPose(new Quaternion(Vector3f.XP, grapple.prevRotationRoll + (grapple.rotationRoll - grapple.prevRotationRoll) * partialTicks, true));

        if (Minecraft.useAmbientOcclusion())
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

        matrixStack.popPose();
    }

    @Override
    public ResourceLocation getEntityTexture(GrappleEntity entity)
    {
        return new ResourceLocation("missing");
    }

    @Override
    public boolean shouldRender(GrappleEntity entity, Frustum camera, double camX, double camY, double camZ)
    {
        return entity.shouldRender(camX, camY, camZ);
    }
}