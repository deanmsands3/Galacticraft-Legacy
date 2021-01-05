package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.client.model.JuicerModel;
import micdoodle8.mods.galacticraft.planets.venus.entities.JuicerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class JuicerRenderer extends MobRenderer<JuicerEntity, JuicerModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/entity/juicer.png");
    private boolean sensorEnabled;

    public JuicerRenderer(EntityRendererManager renderManager)
    {
        super(renderManager, new JuicerModel(), 0.5F);
    }

    @Override
    protected void preRenderCallback(JuicerEntity entity, MatrixStack matrixStack, float partialTickTime)
    {
        matrixStack.rotate(Vector3f.XP.rotationDegrees(entity.isHanging() ? 180.0F : 0.0F));
        matrixStack.translate(0.0F, entity.isHanging() ? 1.8F : 1.3F, 0.0F);

        if (this.sensorEnabled)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public void render(JuicerEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);

        if (OverlaySensorGlasses.overrideMobTexture())
        {
            this.sensorEnabled = true;
            super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
            this.sensorEnabled = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }

    @Override
    public ResourceLocation getEntityTexture(JuicerEntity entity)
    {
        return this.sensorEnabled ? OverlaySensorGlasses.altTexture : TEXTURE;
    }
}