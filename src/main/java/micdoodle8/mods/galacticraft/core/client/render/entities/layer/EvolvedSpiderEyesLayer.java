package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.EvolvedSpiderModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvolvedSpiderEyesLayer<T extends Entity, M extends EvolvedSpiderModel<T>> extends AbstractEyesLayer<T, M>
{
    private static final RenderType RENDER_TYPE = RenderType.getEyes(new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/evolved_spider_eyes.png"));

    public EvolvedSpiderEyesLayer(IEntityRenderer<T, M> renderer)
    {
        super(renderer);
    }

    @Override
    public RenderType getRenderType()
    {
        return RENDER_TYPE;
    }
}