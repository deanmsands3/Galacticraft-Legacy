package team.galacticraft.galacticraft.common.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.client.gui.overlay.OverlaySensorGlasses;
import team.galacticraft.galacticraft.common.core.client.model.ModelEvolvedSpider;
import team.galacticraft.galacticraft.common.core.entities.EntityEvolvedSpider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderEvolvedSpider extends MobRenderer<EntityEvolvedSpider, ModelEvolvedSpider>
{
    private static final ResourceLocation spiderTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/spider.png");
    private boolean texSwitch;

    public RenderEvolvedSpider(EntityRenderDispatcher manager)
    {
        super(manager, new ModelEvolvedSpider(), 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEvolvedSpider spider)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedSpider.spiderTexture;
    }

    @Override
    protected void preRenderCallback(EntityEvolvedSpider spider, PoseStack matStack, float partialTickTime)
    {
        matStack.scale(1.2F, 1.2F, 1.2F);
        if (texSwitch)
        {
            matStack.translate(0.0, -0.03, 0.0);
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public void render(EntityEvolvedSpider entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
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

    @Override
    protected float getDeathMaxRotation(EntityEvolvedSpider spider)
    {
        return 180.0F;
    }
}
