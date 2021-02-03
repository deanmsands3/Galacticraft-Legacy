package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.EvolvedSkeletonBossModel;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerHeldItemEvolvedSkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.EvolvedSkeletonBossEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EvolvedSkeletonBossRenderer extends MobRenderer<EvolvedSkeletonBossEntity, EvolvedSkeletonBossModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/entity/evolved_skeleton_boss.png");

    public EvolvedSkeletonBossRenderer(EntityRenderDispatcher manager)
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
    protected void preRenderCallback(EvolvedSkeletonBossEntity entity, PoseStack matrixStack, float partialTicks)
    {
        matrixStack.scale(1.2F, 1.2F, 1.2F);
        matrixStack.mulPose(new Quaternion(Vector3f.YP, (float) (Math.pow(entity.deathTicks, 2) / 5.0F + (Math.pow(entity.deathTicks, 2) / 5.0F - Math.pow(entity.deathTicks - 1, 2) / 5.0F) * partialTicks), true));
    }
}