package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import micdoodle8.mods.galacticraft.core.entities.EntityCelestialFake;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class RenderEntityFake extends EntityRenderer<EntityCelestialFake>
{
    public RenderEntityFake(EntityRenderDispatcher manager)
    {
        super(manager);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityCelestialFake par1Entity)
    {
        return null;
    }

    @Override
    public boolean shouldRender(EntityCelestialFake livingEntityIn, Frustum camera, double camX, double camY, double camZ)
    {
        return false;
    }

    @Override
    public void render(EntityCelestialFake entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
    {
    }
}
