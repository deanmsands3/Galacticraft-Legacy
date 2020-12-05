package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSkeletonBoss;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerHeldItemEvolvedSkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public class RenderEvolvedSkeletonBoss extends MobRenderer<EntitySkeletonBoss, ModelEvolvedSkeletonBoss>
{
    private static final ResourceLocation skeletonBossTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/skeletonboss.png");

    public RenderEvolvedSkeletonBoss(EntityRenderDispatcher manager)
    {
        super(manager, new ModelEvolvedSkeletonBoss(), 0.9F);
        this.addLayer(new LayerHeldItemEvolvedSkeletonBoss(this));
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySkeletonBoss entity)
    {
        return RenderEvolvedSkeletonBoss.skeletonBossTexture;
    }

    @Override
    protected void preRenderCallback(EntitySkeletonBoss entity, PoseStack matrixStackIn, float partialTicks)
    {
        matrixStackIn.scale(1.2F, 1.2F, 1.2F);
        matrixStackIn.mulPose(new Quaternion(Vector3f.YP, (float) (Math.pow(entity.deathTicks, 2) / 5.0F + (Math.pow(entity.deathTicks, 2) / 5.0F - Math.pow(entity.deathTicks - 1, 2) / 5.0F) * partialTicks), true));
    }
}
