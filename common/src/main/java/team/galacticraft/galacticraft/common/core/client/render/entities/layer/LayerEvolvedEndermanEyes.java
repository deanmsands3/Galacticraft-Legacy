package team.galacticraft.galacticraft.common.core.client.render.entities.layer;

import team.galacticraft.galacticraft.common.core.client.model.ModelEvolvedEnderman;
import team.galacticraft.galacticraft.common.core.entities.EntityEvolvedEnderman;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class LayerEvolvedEndermanEyes extends EyesLayer<EntityEvolvedEnderman, ModelEvolvedEnderman>
{
    private static final RenderType RENDER_TYPE = RenderType.eyes(new ResourceLocation("textures/model/evolved_enderman_eyes.png"));

    public LayerEvolvedEndermanEyes(RenderLayerParent<EntityEvolvedEnderman, ModelEvolvedEnderman> rendererIn) {
        super(rendererIn);
    }

    public RenderType renderType() {
        return RENDER_TYPE;
    }
}