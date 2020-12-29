package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.SlimelingModel;
import micdoodle8.mods.galacticraft.planets.mars.entities.SlimelingEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SlimelingRenderer extends MobRenderer<SlimelingEntity, SlimelingModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/entity/slimeling.png");
    private boolean sensorEnabled;

    public SlimelingRenderer(EntityRendererManager renderManager)
    {
        super(renderManager, new SlimelingModel(16), 0.5F);
    }

    @Override
    public ResourceLocation getEntityTexture(SlimelingEntity entity)
    {
        return sensorEnabled ? OverlaySensorGlasses.altTexture : SlimelingRenderer.TEXTURE;
    }

    @Override
    protected void preRenderCallback(SlimelingEntity entity, MatrixStack matrixStack, float partialTickTime)
    {
        matrixStack.rotate(Vector3f.YP.rotation(180.0F));
        RenderSystem.color3f(entity.getColorRed(), entity.getColorGreen(), entity.getColorBlue());
        matrixStack.scale(entity.getScale(), entity.getScale(), entity.getScale());
        matrixStack.translate(0.0F, 1.10F, 0.0F);

        if (sensorEnabled)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public void render(SlimelingEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
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

    //    @Override
    //    protected void renderLayers(EntitySlimeling slimeling, float p_177093_2_, float p_177093_3_, float partialTicks, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_)
    //    {
    //        super.renderLayers(slimeling, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
    //
    //        //After rendering the slimeling, reset the color tint to none
    //        GL11.glColor3f(1F, 1F, 1F);
    //    }
    //
    //    @Override
    //    protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
    //    {
    //        if (par1EntityLivingBase.isInvisible())
    //        {
    //            return 0;
    //        }
    //        else if (par2 == 0)
    //        {
    //            this.setRenderPassModel(this.renderPassModel);
    //            GL11.glEnable(GL11.GL_NORMALIZE);
    //            GL11.glEnable(GL11.GL_BLEND);
    //            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    //            return 1;
    //        }
    //        else
    //        {
    //            if (par2 == 1)
    //            {
    //                GL11.glDisable(GL11.GL_BLEND);
    //                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    //            }
    //
    //            return -1;
    //        }
    //    }
    //
    //    @Override
    //    protected void passSpecialRender(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6)
    //    {
    //        Minecraft mc = Minecraft.getInstance();
    //
    //        if (!mc.gameSettings.hideGUI && !par1EntityLivingBase.isInvisible() && (mc.currentScreen == null || !((mc.currentScreen instanceof GuiSlimeling || mc.currentScreen instanceof GuiSlimelingInventory) && GuiSlimeling.renderingOnGui)))
    //        {
    //            this.renderLivingLabelWithColor(par1EntityLivingBase, ((EntitySlimeling) par1EntityLivingBase).getName(), par2, par4 + 0.33, par6, 64, 0, 0, 0);
    //            int health = (int) Math.floor(((EntitySlimeling) par1EntityLivingBase).getHealth() + 0.6D);
    //            int maxHealth = (int) ((EntitySlimeling) par1EntityLivingBase).getMaxHealth();
    //            if (health > maxHealth)
    //            {
    //                health = maxHealth;
    //            }
    //            float difference = health / (float)maxHealth;
    //
    //            if (difference < 0.33333F)
    //            {
    //                this.renderLivingLabelWithColor(par1EntityLivingBase, "" + health + " / " + maxHealth, par2, par4, par6, 64, 1, 0, 0);
    //            }
    //            else if (difference < 0.66666F)
    //            {
    //                this.renderLivingLabelWithColor(par1EntityLivingBase, "" + health + " / " + maxHealth, par2, par4, par6, 64, 1, 1, 0);
    //            }
    //            else
    //            {
    //                this.renderLivingLabelWithColor(par1EntityLivingBase, "" + health + " / " + maxHealth, par2, par4, par6, 64, 0, 1, 0);
    //            }
    //        }
    //
    //        super.passSpecialRender(par1EntityLivingBase, par2, par4, par6);
    //        GL11.glDisable(GL11.GL_NORMALIZE);
    //        GL11.glDisable(GL11.GL_BLEND);
    //    }
    //
    //    protected void renderLivingLabelWithColor(EntityLivingBase par1EntityLivingBase, String par2Str, double par3, double par5, double par7, int par9, float cR, float cG, float cB)
    //    {
    //        double d3 = par1EntityLivingBase.getDistanceSq(this.renderManager.livingPlayer);
    //
    //        if (d3 <= par9 * par9)
    //        {
    //            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
    //            float f = 1.6F;
    //            float f1 = 0.016666668F * f;
    //            GL11.glPushMatrix();
    //            GL11.glTranslatef((float) par3 + 0.0F, (float) par5 + par1EntityLivingBase.height + 0.55F, (float) par7);
    //            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
    //            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
    //            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
    //            GL11.glScalef(-f1, -f1, f1);
    //            GL11.glDisable(GL11.GL_LIGHTING);
    //            GL11.glDepthMask(false);
    //            GL11.glDisable(GL11.GL_DEPTH_TEST);
    //            GL11.glEnable(GL11.GL_BLEND);
    //            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    //            Tessellator tessellator = Tessellator.instance;
    //            byte b0 = 0;
    //
    //            if (par2Str.equals("deadmau5"))
    //            {
    //                b0 = -10;
    //            }
    //
    //            GL11.glDisable(GL11.GL_TEXTURE_2D);
    //            tessellator.startDrawingQuads();
    //            int j = fontrenderer.getStringWidth(par2Str) / 2;
    //            tessellator.setColorRGBA_F(cR, cG, cB, 0.25F);
    //            tessellator.addVertex(-j - 1, -1 + b0, 0.0D);
    //            tessellator.addVertex(-j - 1, 8 + b0, 0.0D);
    //            tessellator.addVertex(j + 1, 8 + b0, 0.0D);
    //            tessellator.addVertex(j + 1, -1 + b0, 0.0D);
    //            tessellator.draw();
    //            GL11.glEnable(GL11.GL_TEXTURE_2D);
    //            fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, b0, 553648127);
    //            GL11.glEnable(GL11.GL_DEPTH_TEST);
    //            GL11.glDepthMask(true);
    //            fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, b0, -1);
    //            GL11.glEnable(GL11.GL_LIGHTING);
    //            GL11.glDisable(GL11.GL_BLEND);
    //            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    //            GL11.glPopMatrix();
    //        }
    //    }
}