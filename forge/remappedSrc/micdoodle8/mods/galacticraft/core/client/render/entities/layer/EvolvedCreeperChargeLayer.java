package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.core.client.model.EvolvedCreeperModel;
import micdoodle8.mods.galacticraft.core.entities.EvolvedCreeperEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvolvedCreeperChargeLayer extends EnergyLayer<EvolvedCreeperEntity, EvolvedCreeperModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final EvolvedCreeperModel model = new EvolvedCreeperModel(2.0F);

    public EvolvedCreeperChargeLayer(IEntityRenderer<EvolvedCreeperEntity, EvolvedCreeperModel> renderer)
    {
        super(renderer);
    }

    @Override
    protected float func_225634_a_(float partialTickTime)
    {
        return partialTickTime * 0.01F;
    }

    @Override
    protected ResourceLocation func_225633_a_()
    {
        return TEXTURE;
    }

    @Override
    protected EvolvedCreeperModel func_225635_b_()
    {
        return this.model;
    }
}