package team.galacticraft.galacticraft.common.core.client.gui.element;

import team.galacticraft.galacticraft.core.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import org.lwjgl.opengl.GL11;

public class GuiElementCheckbox extends AbstractWidget
{
    protected static final ResourceLocation texture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/gui.png");
    public Boolean isSelected;
    private final ICheckBoxCallback parentGui;
    private final int textColor;
    private final int texWidth;
    private final int texHeight;
    private final int texX;
    private final int texY;
    private final boolean shiftOnHover;

    public GuiElementCheckbox(ICheckBoxCallback parentGui, int x, int y, String text)
    {
        this(parentGui, x, y, text, 4210752);
    }

    public GuiElementCheckbox(ICheckBoxCallback parentGui, int x, int y, String text, int textColor)
    {
        this(parentGui, x, y, 13, 13, 20, 24, text, textColor);
    }

    private GuiElementCheckbox(ICheckBoxCallback parentGui, int x, int y, int width, int height, int texX, int texY, String text, int textColor)
    {
        this(parentGui, x, y, width, height, width, height, texX, texY, text, textColor, true);
    }

    public GuiElementCheckbox(ICheckBoxCallback parentGui, int x, int y, int width, int height, int texWidth, int texHeight, int texX, int texY, String text, int textColor, boolean shiftOnHover)
    {
        super(x, y, width, height, text);
        this.parentGui = parentGui;
        this.textColor = textColor;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.shiftOnHover = shiftOnHover;
        this.texX = texX;
        this.texY = texY;
    }

    @Override
    public void renderButton(int par2, int par3, float partial)
    {
        if (this.isSelected == null)
        {
            this.isSelected = this.parentGui.getInitiallySelected(this);
        }

        if (this.visible)
        {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.getTextureManager().bind(GuiElementCheckbox.texture);
            GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height;
            this.blit(this.x, this.y, this.isSelected ? this.texX + this.texWidth : this.texX, this.isHovered() ? this.shiftOnHover ? this.texY + this.texHeight : this.texY : this.texY, this.width, this.height);
            minecraft.font.draw(this.getMessage(), this.x + this.width + 3, this.y + (this.height - 6) / 2, this.textColor);
        }
    }

    @Override
    public void blit(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuilder();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
        worldRenderer.vertex(par1 + 0, par2 + par6, this.getBlitOffset()).uv((par3 + 0) * f, (par4 + this.texHeight) * f1).endVertex();
        worldRenderer.vertex(par1 + par5, par2 + par6, this.getBlitOffset()).uv((par3 + this.texWidth) * f, (par4 + this.texHeight) * f1).endVertex();
        worldRenderer.vertex(par1 + par5, par2 + 0, this.getBlitOffset()).uv((par3 + this.texWidth) * f, (par4 + 0) * f1).endVertex();
        worldRenderer.vertex(par1 + 0, par2 + 0, this.getBlitOffset()).uv((par3 + 0) * f, (par4 + 0) * f1).endVertex();
        tessellator.end();
    }

    @Override
    protected boolean clicked(double p_clicked_1_, double p_clicked_3_)
    {
        if (super.clicked(p_clicked_1_, p_clicked_3_))
        {
            boolean canInteract = this.parentGui.canPlayerEdit(this, Minecraft.getInstance().player);
            if (!canInteract)
            {
                this.parentGui.onIntruderInteraction();
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(double mouseX, double mouseY)
    {
        this.parentGui.onSelectionChanged(this, this.isSelected);
    }

    public interface ICheckBoxCallback
    {
        void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected);

        boolean canPlayerEdit(GuiElementCheckbox checkbox, Player player);

        boolean getInitiallySelected(GuiElementCheckbox checkbox);

        void onIntruderInteraction();
    }
}
