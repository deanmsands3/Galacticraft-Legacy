package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import micdoodle8.mods.galacticraft.core.entities.CelestialScreenEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CelestialScreenRenderer extends EntityRenderer<CelestialScreenEntity>
{
    public CelestialScreenRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
    }

    @Override
    public ResourceLocation getEntityTexture(CelestialScreenEntity entity)
    {
        return null;
    }

    @Override
    public boolean shouldRender(CelestialScreenEntity entity, Frustum camera, double camX, double camY, double camZ)
    {
        return false;
    }

    @Override
    public void render(CelestialScreenEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
    }
}