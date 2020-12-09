package team.galacticraft.galacticraft.common.core.client.gui.element;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import team.galacticraft.galacticraft.common.api.vector.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.opengl.GL11;

import java.util.Collections;

public class GuiElementSlider extends Button
{
    private final Vector3 firstColor;
    private final Vector3 lastColor;
    private final boolean isVertical;
    private int sliderPos;

    public GuiElementSlider(int x, int y, int width, int height, boolean vertical, Vector3 firstColor, Vector3 lastColor)
    {
        this(x, y, width, height, vertical, firstColor, lastColor, "");
    }

    public GuiElementSlider(int x, int y, int width, int height, boolean vertical, Vector3 firstColor, Vector3 lastColor, String message)
    {
        super(x, y, width, height, message, (button) ->
        {
        });
        this.isVertical = vertical;
        this.firstColor = firstColor;
        this.lastColor = lastColor;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partial)
    {
        if (this.visible)
        {
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

//            if (Mouse.isButtonDown(0) && this.isHovered())
//            {
//                if (this.isVertical)
//                {
//                    this.sliderPos = mouseY - this.y;
//                }
//                else
//                {
//                    this.sliderPos = mouseX - this.x;
//                }
//            } TODO Slider element

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GlStateManager._blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value,
                    GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder worldRenderer = tessellator.getBuilder();

            if (this.isVertical)
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR);
                worldRenderer.vertex((double) this.x + this.width, this.y, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.vertex(this.x, this.y, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.vertex(this.x, (double) this.y + this.height, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.vertex((double) this.x + this.width, (double) this.y + this.height, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                tessellator.end();

                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR);
                worldRenderer.vertex((double) this.x + this.width - 1, (double) this.y + 1, this.getBlitOffset()).color(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F).endVertex();
                worldRenderer.vertex((double) this.x + 1, (double) this.y + 1, this.getBlitOffset()).color(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F).endVertex();
                worldRenderer.vertex((double) this.x + 1, (double) this.y + this.height - 1, this.getBlitOffset()).color(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F).endVertex();
                worldRenderer.vertex((double) this.x + this.width - 1, (double) this.y + this.height - 1, this.getBlitOffset()).color(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F).endVertex();
                tessellator.end();

                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR);
                worldRenderer.vertex((double) this.x + this.width, (double) this.y + this.sliderPos - 1, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.vertex(this.x, (double) this.y + this.sliderPos - 1, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.vertex(this.x, (double) this.y + this.sliderPos + 1, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.vertex((double) this.x + this.width, (double) this.y + this.sliderPos + 1, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                tessellator.end();
            }
            else
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR);
                worldRenderer.vertex((double) this.x + this.width, this.y, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.vertex(this.x, this.y, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.vertex(this.x, (double) this.y + this.height, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.vertex((double) this.x + this.width, (double) this.y + this.height, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                tessellator.end();

                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR);
                worldRenderer.vertex((double) this.x + this.width - 1, (double) this.y + 1, this.getBlitOffset()).color(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F).endVertex();
                worldRenderer.vertex((double) this.x + 1, (double) this.y + 1, this.getBlitOffset()).color(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F).endVertex();
                worldRenderer.vertex((double) this.x + 1, (double) this.y + this.height - 1, this.getBlitOffset()).color(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F).endVertex();
                worldRenderer.vertex((double) this.x + this.width - 1, (double) this.y + this.height - 1, this.getBlitOffset()).color(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F).endVertex();
                tessellator.end();

                GL11.glShadeModel(GL11.GL_FLAT);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GlStateManager._blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value,
                        GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);
                GL11.glShadeModel(GL11.GL_SMOOTH);

                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR);
                worldRenderer.vertex((double) this.x + this.sliderPos + 1, this.y, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.vertex((double) this.x + this.sliderPos - 1, this.y, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.vertex((double) this.x + this.sliderPos - 1, (double) this.y + this.height, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.vertex((double) this.x + this.sliderPos + 1, (double) this.y + this.height, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                tessellator.end();
            }

            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    protected void renderTooltip(ItemStack p_renderTooltip_1_, int p_renderTooltip_2_, int p_renderTooltip_3_)
    {
    }

    public void drawHoveringText()
    {
        if (this.isHovered())
        {
            Minecraft minecraft = Minecraft.getInstance();
            int i = (int) (minecraft.mouseHandler.xpos() * (double) minecraft.getWindow().getGuiScaledWidth() / (double) minecraft.getWindow().getScreenWidth());
            int j = (int) (minecraft.mouseHandler.ypos() * (double) minecraft.getWindow().getGuiScaledHeight() / (double) minecraft.getWindow().getScreenHeight());
            Font font = Minecraft.getInstance().font;
            GuiUtils.drawHoveringText(Collections.singletonList(this.getMessage()), i, j, width, height, -1, font);
        }
    }

    public void setSliderPos(float pos)
    {
        this.sliderPos = (int) Math.floor(this.height * pos);
    }

    public int getSliderPos()
    {
        return this.sliderPos;
    }

    public float getNormalizedValue()
    {
        return this.sliderPos / (float) this.height;
    }

    public double getColorValueD()
    {
        return (this.sliderPos * 255.0D) / (this.height - 1);
    }

    public float getColorValueF()
    {
        return (float) getColorValueD();
    }

    public int getButtonHeight()
    {
        return this.height;
    }
}
