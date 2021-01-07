package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.EvolvedEndermanModel;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerEvolvedEndermanEyes;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerEvolvedEndermanHeldBlock;
import micdoodle8.mods.galacticraft.core.entities.EvolvedEndermanEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvolvedEndermanRenderer extends MobRenderer<EvolvedEndermanEntity, EvolvedEndermanModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/evolved_enderman.png");
    private final EvolvedEndermanModel endermanModel = new EvolvedEndermanModel();
    private boolean sensorEnabled;

    //TODO Fix enderman rendering
    public EvolvedEndermanRenderer(EntityRendererManager manager)
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
    public Vec3d getRenderOffset(EvolvedEndermanEntity entity, float partialTicks)
    {
        if (entity.isScreaming())
        {
            double d0 = 0.02D;
            return new Vec3d(entity.getRNG().nextGaussian() * d0, 0.0D, entity.getRNG().nextGaussian() * d0);
        }
        else
        {
            return super.getRenderOffset(entity, partialTicks);
        }
    }

    @Override
    public void render(EvolvedEndermanEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        BlockState iblockstate = entity.getHeldBlockState();
        this.endermanModel.isCarrying = iblockstate != null;
        this.endermanModel.isAttacking = entity.isScreaming();
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
    protected void preRenderCallback(EvolvedEndermanEntity entity, MatrixStack matrixStack, float partialTickTime)
    {
        if (this.sensorEnabled)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }
}