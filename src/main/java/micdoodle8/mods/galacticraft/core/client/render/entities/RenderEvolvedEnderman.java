package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedEnderman;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerEvolvedEndermanEyes;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerEvolvedEndermanHeldBlock;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedEnderman;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class RenderEvolvedEnderman extends MobRenderer<EntityEvolvedEnderman, ModelEvolvedEnderman>
{
    private static final ResourceLocation endermanTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/evolved_enderman.png");
    private final ModelEvolvedEnderman endermanModel;
    private final Random rnd = new Random();
    private boolean texSwitch;

    public RenderEvolvedEnderman(EntityRenderDispatcher manager)
    {
        super(manager, new ModelEvolvedEnderman(), 0.5F);
        this.endermanModel = super.model;
        this.addLayer(new LayerEvolvedEndermanEyes(this));
        this.addLayer(new LayerEvolvedEndermanHeldBlock(this));
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEvolvedEnderman entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedEnderman.endermanTexture;
    }

    @Override
    public Vec3 getRenderOffset(EntityEvolvedEnderman entityIn, float partialTicks) {
        if (entityIn.isCreepy()) {
            double d0 = 0.02D;
            return new Vec3(this.rnd.nextGaussian() * d0, 0.0D, this.rnd.nextGaussian() * d0);
        } else {
            return super.getRenderOffset(entityIn, partialTicks);
        }
    }

    @Override
    public void render(EntityEvolvedEnderman entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
    {
        BlockState iblockstate = entityIn.getCarriedBlock();
        this.endermanModel.isCarrying = iblockstate != null;
        this.endermanModel.isAttacking = entityIn.isCreepy();

//        if (entityIn.isScreaming())
//        {
//            double d3 = 0.02D;
//            x += this.rnd.nextGaussian() * d3;
//            z += this.rnd.nextGaussian() * d3;
//        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            texSwitch = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }

    @Override
    protected void preRenderCallback(EntityEvolvedEnderman entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime)
    {
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }
}