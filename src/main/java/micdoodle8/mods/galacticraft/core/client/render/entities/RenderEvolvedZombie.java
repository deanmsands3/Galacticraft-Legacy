package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedZombie;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderEvolvedZombie extends AbstractZombieRenderer<EntityEvolvedZombie, ModelEvolvedZombie>
{
    private static final ResourceLocation zombieTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/zombie.png");
    private static final ResourceLocation powerTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/power.png");

    private boolean texSwitch;

    public RenderEvolvedZombie(EntityRenderDispatcher manager)
    {
        super(manager, new ModelEvolvedZombie(0.0F, false, true), new ModelEvolvedZombie(0.5F, false, true), new ModelEvolvedZombie(1.0F, false, true));
//        LayerRenderer layerrenderer = (LayerRenderer) this.layerRenderers.get(0);
        this.addLayer(new ItemInHandLayer(this));
//        BipedArmorLayer layerbipedarmor = new BipedArmorLayer(this)
//        {
//            @Override
//            protected void initArmor()
//            {
//                this.modelLeggings = new ZombieModel(0.5F, true);
//                this.modelArmor = new ZombieModel(1.0F, true);
//            }
//        };
//        this.addLayer(layerbipedarmor);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEvolvedZombie par1Entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedZombie.zombieTexture;
    }

    @Override
    protected void preRenderCallback(EntityEvolvedZombie entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime)
    {
        matrixStackIn.scale(1.2F, 1.2F, 1.2F);
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public void render(EntityEvolvedZombie entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
    {
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
    protected void applyRotations(EntityEvolvedZombie zombie, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks)
    {
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.translate(0F, -zombie.getBbHeight() * 0.55F, 0F);
        matrixStackIn.mulPose(new Quaternion(new Vector3f(zombie.getTumbleAxisX(), 0F, zombie.getTumbleAxisZ()), zombie.getTumbleAngle(partialTicks), true));
        matrixStackIn.translate(0F, zombie.getBbHeight() * 0.55F, 0F);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        super.setupRotations(zombie, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }
}
