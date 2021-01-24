package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.EvolvedWitchModel;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerHeldItemEvolvedWitch;
import micdoodle8.mods.galacticraft.core.entities.EvolvedWitchEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvolvedWitchRenderer extends MobRenderer<EvolvedWitchEntity, EvolvedWitchModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/evolved_witch.png");
    private boolean sensorEnabled;

    public EvolvedWitchRenderer(EntityRenderDispatcher manager)
    {
        super(manager, new EvolvedWitchModel(), 0.5F);
        this.addLayer(new LayerHeldItemEvolvedWitch(this));
    }

    @Override
    public void render(EvolvedWitchEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        this.getModel().func_205074_a(entity.getMainHandItem().isEmpty());
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
    protected void preRenderCallback(EvolvedWitchEntity entity, PoseStack matrixStack, float partialTickTime)
    {
        float f1 = 0.9375F;
        matrixStack.scale(f1, f1, f1);

        if (this.sensorEnabled)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public ResourceLocation getEntityTexture(EvolvedWitchEntity entity)
    {
        return this.sensorEnabled ? OverlaySensorGlasses.altTexture : EvolvedWitchRenderer.TEXTURE;
    }
}