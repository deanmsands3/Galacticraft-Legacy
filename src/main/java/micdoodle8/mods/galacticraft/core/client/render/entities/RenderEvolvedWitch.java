package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedWitch;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerHeldItemEvolvedWitch;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedWitch;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderEvolvedWitch extends MobRenderer<EntityEvolvedWitch, ModelEvolvedWitch>
{
    private static final ResourceLocation witchTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/evolved_witch.png");
    private boolean texSwitch;

    public RenderEvolvedWitch(EntityRendererManager manager)
    {
        super(manager, new ModelEvolvedWitch(), 0.5F);
        this.addLayer(new LayerHeldItemEvolvedWitch(this));
    }

    @Override
    public void render(EntityEvolvedWitch entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        this.getEntityModel().func_205074_a(entityIn.getHeldItemMainhand().isEmpty());
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            texSwitch = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }

    @Override
    protected void preRenderCallback(EntityEvolvedWitch entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime)
    {
        float f1 = 0.9375F;
        matrixStackIn.scale(f1, f1, f1);
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEvolvedWitch entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedWitch.witchTexture;
    }
}