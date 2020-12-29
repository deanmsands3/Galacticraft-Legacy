package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.EvolvedSpiderModel;
import micdoodle8.mods.galacticraft.core.entities.EvolvedSpiderEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvolvedSpiderRenderer extends MobRenderer<EvolvedSpiderEntity, EvolvedSpiderModel<EvolvedSpiderEntity>>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/evolved_spider.png");
    private boolean sensorEnabled;

    public EvolvedSpiderRenderer(EntityRendererManager manager)
    {
        super(manager, new EvolvedSpiderModel(), 1.0F);
        //this.addLayer(new EvolvedSpiderEyesLayer<>(this));TODO Fix white texture bug
    }

    @Override
    public ResourceLocation getEntityTexture(EvolvedSpiderEntity entity)
    {
        return this.sensorEnabled ? OverlaySensorGlasses.altTexture : EvolvedSpiderRenderer.TEXTURE;
    }

    @Override
    protected void preRenderCallback(EvolvedSpiderEntity entity, MatrixStack matrixStack, float partialTickTime)
    {
        matrixStack.scale(1.2F, 1.2F, 1.2F);

        if (this.sensorEnabled)
        {
            matrixStack.translate(0.0D, -0.03D, 0.0D);
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public void render(EvolvedSpiderEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
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
    protected float getDeathMaxRotation(EvolvedSpiderEntity entity)
    {
        return 180.0F;
    }
}