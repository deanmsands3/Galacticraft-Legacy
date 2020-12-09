package team.galacticraft.galacticraft.common.core.client.render.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import team.galacticraft.galacticraft.core.client.model.ModelEvolvedCreeper;
import team.galacticraft.galacticraft.core.client.render.entities.layer.LayerEvolvedCreeperCharge;
import team.galacticraft.galacticraft.core.entities.EntityEvolvedCreeper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class RenderEvolvedCreeper extends MobRenderer<EntityEvolvedCreeper, ModelEvolvedCreeper>
{
    private static final ResourceLocation creeperTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/creeper.png");
    private boolean texSwitch;

    public RenderEvolvedCreeper(EntityRenderDispatcher manager)
    {
        super(manager, new ModelEvolvedCreeper(), 0.5F);
        this.addLayer(new LayerEvolvedCreeperCharge(this));
    }

    @Override
    protected void preRenderCallback(EntityEvolvedCreeper entity, PoseStack matrixStackIn, float partialTickTime)
    {
        float f = entity.getSwelling(partialTickTime);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        matrixStackIn.scale(0.2F + f2, 0.2F + f3, 0.2F + f2);

        if (this.texSwitch)
        {
            matrixStackIn.translate(0.0F, -0.03F, 0.0F);
            OverlaySensorGlasses.preRenderMobs();
        }
        matrixStackIn.translate(0.0F, 0.125F, 0.0F);
    }

    @Override
    protected float getOverlayProgress(EntityEvolvedCreeper livingEntityIn, float partialTicks) {
        float f = livingEntityIn.getSwelling(partialTicks);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }


//    @Override
//    protected int getColorMultiplier(EntityEvolvedCreeper entity, float lightBrightness, float partialTicks)
//    {
//        float f = entity.getCreeperFlashIntensity(partialTicks);
//
//        if ((int) (f * 10.0F) % 2 == 0)
//        {
//            return 0;
//        }
//        else
//        {
//            int i = (int) (f * 0.2F * 255.0F);
//            i = MathHelper.clamp(i, 0, 255);
//            return i << 24 | 16777215;
//        }
//    }

    @Override
    public ResourceLocation getEntityTexture(EntityEvolvedCreeper entity)
    {
        return this.texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedCreeper.creeperTexture;
    }

    @Override
    public void render(EntityEvolvedCreeper entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
    {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            texSwitch = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }
}
