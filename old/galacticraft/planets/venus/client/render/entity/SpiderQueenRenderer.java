package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.client.model.SpiderQueenModel;
import micdoodle8.mods.galacticraft.planets.venus.entities.SpiderQueenEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpiderQueenRenderer extends MobRenderer<SpiderQueenEntity, SpiderQueenModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/entity/spider_queen.png");
    private BakedModel webModel;

    public SpiderQueenRenderer(EntityRenderDispatcher renderManager)
    {
        super(renderManager, new SpiderQueenModel(), 1.0F);
        GCModelCache.INSTANCE.reloadCallback(this::updateModel);
    }

    private void updateModel()
    {
        this.webModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/queen_web.obj"), ImmutableList.of("Sphere"));
    }

    @Override
    protected void preRenderCallback(SpiderQueenEntity entity, PoseStack matrixStack, float partialTickTime)
    {
        if (entity.getBurrowedCount() >= 0)
        {
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStack.translate(0.0F, entity.getHealth(), 0.0F);//TODO Why getHealth?
        }
        matrixStack.scale(1.5F, 1.5F, 1.5F);
        matrixStack.mulPose(new Quaternion(Vector3f.YP, (float) (Math.pow(entity.deathTicks, 2) / 5.0F + (Math.pow(entity.deathTicks, 2) / 5.0F - Math.pow(entity.deathTicks - 1, 2) / 5.0F) * partialTickTime), true));
    }

    @Override
    public void render(SpiderQueenEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        matrixStack.pushPose();
        matrixStack.translate(0, 0.8F, 0);
        matrixStack.scale(1.4F, 1.5F, 1.4F);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Lighting.turnOff();

        if (Minecraft.useAmbientOcclusion())
        {
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            RenderSystem.shadeModel(GL11.GL_FLAT);
        }

        if (entity.getBurrowedCount() >= 0)
        {
            RenderSystem.disableCull();
            ClientUtil.drawBakedModel(this.webModel, buffer, matrixStack, packedLight);
            matrixStack.scale(1.05F, 1.1F, 1.05F);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(192.5F));
            ClientUtil.drawBakedModel(this.webModel, buffer, matrixStack, packedLight);
            RenderSystem.enableCull();
        }

        Lighting.turnBackOn();
        matrixStack.popPose();
    }

    @Override
    public ResourceLocation getEntityTexture(SpiderQueenEntity entity)
    {
        return TEXTURE;
    }
}