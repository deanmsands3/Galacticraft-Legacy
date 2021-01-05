package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.SludgelingModel;
import micdoodle8.mods.galacticraft.planets.mars.entities.SludgelingEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SludgelingRenderer extends MobRenderer<SludgelingEntity, SludgelingModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/entity/sludgeling.png");
    private boolean sensorEnabled;

    public SludgelingRenderer(EntityRendererManager renderManager)
    {
        super(renderManager, new SludgelingModel(), 0.3F);
    }

    @Override
    protected float getDeathMaxRotation(SludgelingEntity entity)
    {
        return 180.0F;
    }

    @Override
    public void render(SludgelingEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
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
    public ResourceLocation getEntityTexture(SludgelingEntity entity)
    {
        return this.sensorEnabled ? OverlaySensorGlasses.altTexture : TEXTURE;
    }

    @Override
    protected void preRenderCallback(SludgelingEntity entity, MatrixStack matrixStack, float partialTickTime)
    {
        if (this.sensorEnabled)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }
}