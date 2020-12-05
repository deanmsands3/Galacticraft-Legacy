package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedEnderman;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedEnderman;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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