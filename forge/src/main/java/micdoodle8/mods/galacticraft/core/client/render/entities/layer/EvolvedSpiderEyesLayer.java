package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.EvolvedSpiderModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvolvedSpiderEyesLayer<T extends Entity, M extends EvolvedSpiderModel<T>> extends EyesLayer<T, M>
{
    private static final RenderType RENDER_TYPE = RenderType.eyes(new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/evolved_spider_eyes.png"));

    public EvolvedSpiderEyesLayer(RenderLayerParent<T, M> renderer)
    {
        super(renderer);
    }

    @Override
    public RenderType renderType()
    {
        return RENDER_TYPE;
    }
}