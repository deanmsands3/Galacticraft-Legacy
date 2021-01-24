package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.EvolvedEndermanModel;
import micdoodle8.mods.galacticraft.core.entities.EvolvedEndermanEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerEvolvedEndermanEyes extends EyesLayer<EvolvedEndermanEntity, EvolvedEndermanModel>
{
    private static final RenderType RENDER_TYPE = RenderType.eyes(new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/evolved_enderman_eyes.png"));

    public LayerEvolvedEndermanEyes(RenderLayerParent<EvolvedEndermanEntity, EvolvedEndermanModel> renderer)
    {
        super(renderer);
    }

    @Override
    public RenderType renderType()
    {
        return RENDER_TYPE;
    }
}