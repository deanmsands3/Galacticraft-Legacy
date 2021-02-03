package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.EvolvedEndermanModel;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerEvolvedEndermanEyes;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerEvolvedEndermanHeldBlock;
import micdoodle8.mods.galacticraft.core.entities.EvolvedEndermanEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvolvedEndermanRenderer extends MobRenderer<EvolvedEndermanEntity, EvolvedEndermanModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/evolved_enderman.png");
    private final EvolvedEndermanModel endermanModel = new EvolvedEndermanModel();
    private boolean sensorEnabled;

    //TODO Fix enderman rendering
    public EvolvedEndermanRenderer(EntityRenderDispatcher manager)
    {
        super(manager, new EvolvedEndermanModel(), 0.5F);
        this.addLayer(new LayerEvolvedEndermanEyes(this));
        this.addLayer(new LayerEvolvedEndermanHeldBlock(this));
    }

    @Override
    public ResourceLocation getEntityTexture(EvolvedEndermanEntity entity)
    {
        return this.sensorEnabled ? OverlaySensorGlasses.altTexture : EvolvedEndermanRenderer.TEXTURE;
    }

    @Override
    public Vec3 getRenderOffset(EvolvedEndermanEntity entity, float partialTicks)
    {
        if (entity.isCreepy())
        {
            double d0 = 0.02D;
            return new Vec3(entity.getRandom().nextGaussian() * d0, 0.0D, entity.getRandom().nextGaussian() * d0);
        }
        else
        {
            return super.getRenderOffset(entity, partialTicks);
        }
    }

    @Override
    public void render(EvolvedEndermanEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        BlockState iblockstate = entity.getCarriedBlock();
        this.endermanModel.isCarrying = iblockstate != null;
        this.endermanModel.isAttacking = entity.isCreepy();
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
    protected void preRenderCallback(EvolvedEndermanEntity entity, PoseStack matrixStack, float partialTickTime)
    {
        if (this.sensorEnabled)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }
}