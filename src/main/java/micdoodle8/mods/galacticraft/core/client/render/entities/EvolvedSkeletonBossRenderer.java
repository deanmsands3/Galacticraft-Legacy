package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.EvolvedSkeletonBossModel;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerHeldItemEvolvedSkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.EvolvedSkeletonBossEntity;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvolvedSkeletonBossRenderer extends MobRenderer<EvolvedSkeletonBossEntity, EvolvedSkeletonBossModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/evolved_skeleton_boss.png");

    public EvolvedSkeletonBossRenderer(EntityRendererManager manager)
    {
        super(manager, new EvolvedSkeletonBossModel(), 0.9F);
        this.addLayer(new LayerHeldItemEvolvedSkeletonBoss(this));
    }

    @Override
    public ResourceLocation getEntityTexture(EvolvedSkeletonBossEntity entity)
    {
        return EvolvedSkeletonBossRenderer.TEXTURE;
    }

    @Override
    protected void preRenderCallback(EvolvedSkeletonBossEntity entity, MatrixStack matrixStack, float partialTicks)
    {
        matrixStack.scale(1.2F, 1.2F, 1.2F);
        matrixStack.rotate(new Quaternion(Vector3f.YP, (float) (Math.pow(entity.deathTicks, 2) / 5.0F + (Math.pow(entity.deathTicks, 2) / 5.0F - Math.pow(entity.deathTicks - 1, 2) / 5.0F) * partialTicks), true));
    }
}