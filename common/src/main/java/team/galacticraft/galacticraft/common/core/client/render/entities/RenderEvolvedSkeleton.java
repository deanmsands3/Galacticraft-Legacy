package team.galacticraft.galacticraft.common.core.client.render.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.client.gui.overlay.OverlaySensorGlasses;
import team.galacticraft.galacticraft.common.core.client.model.ModelEvolvedSkeleton;
import team.galacticraft.galacticraft.common.core.entities.EntityEvolvedSkeleton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderEvolvedSkeleton extends HumanoidMobRenderer<EntityEvolvedSkeleton, ModelEvolvedSkeleton>
{
    private static final ResourceLocation skeletonTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/skeleton.png");
    private static final ResourceLocation powerTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/power.png");

    private final ModelEvolvedSkeleton model = new ModelEvolvedSkeleton(0.2F);
    private boolean texSwitch;

    public RenderEvolvedSkeleton(EntityRenderDispatcher manager)
    {
        super(manager, new ModelEvolvedSkeleton(), 0.6F);
        this.addLayer(new ItemInHandLayer(this));
        this.addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel<>(0.5F, true), new SkeletonModel<>(1.0F, true)));
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEvolvedSkeleton par1Entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedSkeleton.skeletonTexture;
    }

    @Override
    protected void preRenderCallback(EntityEvolvedSkeleton entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime)
    {
        matrixStackIn.scale(1.2F, 1.2F, 1.2F);
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public void render(EntityEvolvedSkeleton entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
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
    protected void applyRotations(EntityEvolvedSkeleton skellie, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks)
    {
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.translate(0F, -skellie.getBbHeight() * 0.55F, 0F);
        matrixStackIn.mulPose(new Quaternion(new Vector3f(skellie.getTumbleAxisX(), 0F, skellie.getTumbleAxisZ()), skellie.getTumbleAngle(partialTicks), true));
        matrixStackIn.translate(0F, skellie.getBbHeight() * 0.55F, 0F);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        super.setupRotations(skellie, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }
}
