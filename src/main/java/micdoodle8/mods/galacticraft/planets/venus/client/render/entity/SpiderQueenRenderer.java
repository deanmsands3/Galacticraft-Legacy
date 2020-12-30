package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.client.model.SpiderQueenModel;
import micdoodle8.mods.galacticraft.planets.venus.entities.SpiderQueenEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpiderQueenRenderer extends MobRenderer<SpiderQueenEntity, SpiderQueenModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/entity/spider_queen.png");
    private IBakedModel webModel;

    public SpiderQueenRenderer(EntityRendererManager renderManager)
    {
        super(renderManager, new SpiderQueenModel(), 1.0F);
        GCModelCache.INSTANCE.reloadCallback(this::updateModel);
    }

    private void updateModel()
    {
        this.webModel = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/obj/queen_web.obj"), ImmutableList.of("Sphere"));
    }

    @Override
    protected void preRenderCallback(SpiderQueenEntity entity, MatrixStack matrixStack, float partialTickTime)
    {
        if (entity.getBurrowedCount() >= 0)
        {
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStack.translate(0.0F, entity.getHealth(), 0.0F);//TODO Why getHealth?
        }
        matrixStack.scale(1.5F, 1.5F, 1.5F);
        matrixStack.rotate(new Quaternion(Vector3f.YP, (float) (Math.pow(entity.deathTicks, 2) / 5.0F + (Math.pow(entity.deathTicks, 2) / 5.0F - Math.pow(entity.deathTicks - 1, 2) / 5.0F) * partialTickTime), true));
    }

    @Override
    public void render(SpiderQueenEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        matrixStack.push();
        matrixStack.translate(0, 0.8F, 0);
        matrixStack.scale(1.4F, 1.5F, 1.4F);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderHelper.disableStandardItemLighting();

        if (Minecraft.isAmbientOcclusionEnabled())
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
            matrixStack.rotate(Vector3f.YP.rotationDegrees(192.5F));
            ClientUtil.drawBakedModel(this.webModel, buffer, matrixStack, packedLight);
            RenderSystem.enableCull();
        }

        RenderHelper.enableStandardItemLighting();
        matrixStack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(SpiderQueenEntity entity)
    {
        return TEXTURE;
    }
}