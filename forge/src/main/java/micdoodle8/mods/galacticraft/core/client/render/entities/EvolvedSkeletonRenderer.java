package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.EvolvedSkeletonModel;
import micdoodle8.mods.galacticraft.core.entities.EvolvedSkeletonEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvolvedSkeletonRenderer extends BipedRenderer<EvolvedSkeletonEntity, EvolvedSkeletonModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/evolved_skeleton.png");
    private boolean sensorEnabled;

    public EvolvedSkeletonRenderer(EntityRendererManager manager)
    {
        super(manager, new EvolvedSkeletonModel(), 0.6F);
        this.addLayer(new HeldItemLayer(this));
        this.addLayer(new BipedArmorLayer<>(this, new SkeletonModel<>(0.5F, true), new SkeletonModel<>(1.0F, true)));
    }

    @Override
    public ResourceLocation getEntityTexture(EvolvedSkeletonEntity entity)
    {
        return this.sensorEnabled ? OverlaySensorGlasses.altTexture : EvolvedSkeletonRenderer.TEXTURE;
    }

    @Override
    protected void preRenderCallback(EvolvedSkeletonEntity entity, MatrixStack matrixStack, float partialTickTime)
    {
        matrixStack.scale(1.2F, 1.2F, 1.2F);

        if (this.sensorEnabled)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public void render(EvolvedSkeletonEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
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
    protected void applyRotations(EvolvedSkeletonEntity entity, MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks)
    {
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.translate(0F, -entity.getHeight() * 0.55F, 0F);
        matrixStack.rotate(new Quaternion(new Vector3f(entity.getTumbleAxisX(), 0F, entity.getTumbleAxisZ()), entity.getTumbleAngle(partialTicks), true));
        matrixStack.translate(0F, entity.getHeight() * 0.55F, 0F);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        super.applyRotations(entity, matrixStack, ageInTicks, rotationYaw, partialTicks);
    }
}