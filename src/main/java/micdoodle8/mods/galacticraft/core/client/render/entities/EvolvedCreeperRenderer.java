package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.EvolvedCreeperModel;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.EvolvedCreeperChargeLayer;
import micdoodle8.mods.galacticraft.core.entities.EvolvedCreeperEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvolvedCreeperRenderer extends MobRenderer<EvolvedCreeperEntity, EvolvedCreeperModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/evolved_creeper.png");
    private boolean sensorEnabled;

    public EvolvedCreeperRenderer(EntityRendererManager manager)
    {
        super(manager, new EvolvedCreeperModel(), 0.5F);
        this.addLayer(new EvolvedCreeperChargeLayer(this));
    }

    @Override
    protected void preRenderCallback(EvolvedCreeperEntity entity, MatrixStack matrixStack, float partialTickTime)
    {
        float f = entity.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        matrixStack.scale(0.2F + f2, 0.2F + f3, 0.2F + f2);

        if (this.sensorEnabled)
        {
            matrixStack.translate(0.0F, -0.03F, 0.0F);
            OverlaySensorGlasses.preRenderMobs();
        }
        matrixStack.translate(0.0F, 0.125F, 0.0F);
    }

    @Override
    protected float getOverlayProgress(EvolvedCreeperEntity entity, float partialTicks)
    {
        float f = entity.getCreeperFlashIntensity(partialTicks);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(EvolvedCreeperEntity entity)
    {
        return this.sensorEnabled ? OverlaySensorGlasses.altTexture : EvolvedCreeperRenderer.TEXTURE;
    }

    @Override
    public void render(EvolvedCreeperEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
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
}