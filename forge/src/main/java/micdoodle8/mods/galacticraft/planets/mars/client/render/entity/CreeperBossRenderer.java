package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.CreeperBossModel;
import micdoodle8.mods.galacticraft.planets.mars.entities.CreeperBossEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CreeperBossRenderer extends MobRenderer<CreeperBossEntity, CreeperBossModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/entity/creeper_boss.png");
    //    private static final ResourceLocation powerTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/power.png");

    public CreeperBossRenderer(EntityRenderDispatcher renderManager)
    {
        super(renderManager, new CreeperBossModel(), 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(CreeperBossEntity entity)
    {
        return CreeperBossRenderer.TEXTURE;
    }

    //    protected int func_27006_a(EntityCreeperBoss par1EntityCreeper, int par2, float par3)
    //    {
    //        if (par1EntityCreeper.headsRemaining == 1)
    //        {
    //            if (par2 == 1)
    //            {
    //                final float var4 = par1EntityCreeper.ticksExisted + par3;
    //                this.bindTexture(RenderCreeperBoss.powerTexture);
    //                GL11.glMatrixMode(GL11.GL_TEXTURE);
    //                GL11.glLoadIdentity();
    //                final float var5 = var4 * 0.01F;
    //                final float var6 = var4 * 0.01F;
    //                GL11.glTranslatef(var5, var6, 0.0F);
    //                this.setRenderPassModel(this.creeperModel);
    //                GL11.glMatrixMode(GL11.GL_MODELVIEW);
    //                GL11.glEnable(GL11.GL_BLEND);
    //                final float var7 = 0.5F;
    //                GL11.glColor4f(var7, var7, var7, 1.0F);
    //                GL11.glDisable(GL11.GL_LIGHTING);
    //                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
    //                return 1;
    //            }
    //
    //            if (par2 == 2)
    //            {
    //                GL11.glMatrixMode(GL11.GL_TEXTURE);
    //                GL11.glLoadIdentity();
    //                GL11.glMatrixMode(GL11.GL_MODELVIEW);
    //                GL11.glEnable(GL11.GL_LIGHTING);
    //                GL11.glDisable(GL11.GL_BLEND);
    //            }
    //        }
    //
    //        return -1;
    //    }

    @Override
    protected void preRenderCallback(CreeperBossEntity entity, PoseStack matrixStack, float partialTickTime)
    {
        matrixStack.scale(4.0F, 4.0F, 4.0F);
        matrixStack.mulPose(new Quaternion(Vector3f.YP, (float) (Math.pow(entity.deathTicks, 2) / 5.0F + (Math.pow(entity.deathTicks, 2) / 5.0F - Math.pow(entity.deathTicks - 1, 2) / 5.0F) * partialTickTime), true));
    }
}