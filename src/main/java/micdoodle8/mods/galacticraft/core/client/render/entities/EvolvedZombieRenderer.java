package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.EvolvedZombieModel;
import micdoodle8.mods.galacticraft.core.entities.EvolvedZombieEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvolvedZombieRenderer extends AbstractZombieRenderer<EvolvedZombieEntity, EvolvedZombieModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/evolved_zombie.png");
    private boolean sensorEnabled;

    //TODO Fix zombie rendering
    public EvolvedZombieRenderer(EntityRendererManager manager)
    {
        super(manager, new EvolvedZombieModel(0.0F, false, true), new EvolvedZombieModel(0.5F, false, true), new EvolvedZombieModel(1.0F, false, true));
    }

    @Override
    public ResourceLocation getEntityTexture(EvolvedZombieEntity entity)
    {
        return this.sensorEnabled ? OverlaySensorGlasses.altTexture : EvolvedZombieRenderer.TEXTURE;
    }

    @Override
    protected void preRenderCallback(EvolvedZombieEntity entity, MatrixStack matrixStack, float partialTickTime)
    {
        matrixStack.scale(1.2F, 1.2F, 1.2F);

        if (this.sensorEnabled)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public void render(EvolvedZombieEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
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
    protected void applyRotations(EvolvedZombieEntity entity, MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks)
    {
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.translate(0F, -entity.getHeight() * 0.55F, 0F);
        matrixStack.rotate(new Quaternion(new Vector3f(entity.getTumbleAxisX(), 0F, entity.getTumbleAxisZ()), entity.getTumbleAngle(partialTicks), true));
        matrixStack.translate(0F, entity.getHeight() * 0.55F, 0F);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        super.applyRotations(entity, matrixStack, ageInTicks, rotationYaw, partialTicks);
    }
}