package team.galacticraft.galacticraft.common.core.client.gui.element;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.util.ColorUtil;
import team.galacticraft.galacticraft.core.util.EnumColor;
import team.galacticraft.galacticraft.core.util.GCLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.opengl.GL11;

public class GuiElementCheckboxPreLaunch extends Button
{
    protected static final ResourceLocation texture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/checklist_book.png");
    public Boolean isSelected;
    private final ICheckBoxCallback parentGui;
    private final int textColor;
    private final int texX;
    private final int texY;

    public GuiElementCheckboxPreLaunch(ICheckBoxCallback parentGui, int x, int y, String text, OnPress onPress)
    {
        this(parentGui, x, y, text, 4210752, onPress);
    }

    public GuiElementCheckboxPreLaunch(ICheckBoxCallback parentGui, int x, int y, String text, int textColor, OnPress onPress)
    {
        this(parentGui, x, y, 9, 9, 194, 0, text, textColor, onPress);
    }

    private GuiElementCheckboxPreLaunch(ICheckBoxCallback parentGui, int x, int y, int width, int height, int texX, int texY, String text, int textColor, OnPress onPress)
    {
        super(x, y, width, height, text, onPress);
        this.parentGui = parentGui;
        this.textColor = textColor;
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
            minecraft.getTextureManager().bind(GuiElementCheckboxPreLaunch.texture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height;
            int texWidth = this.isSelected ? 12 : 9;
            int texHeight = this.isSelected ? 16 : 9;
            this.blit(this.x, this.isSelected ? this.y - 7 : this.y, this.isHovered() ? (this.texX + 12) : this.texX, this.isSelected ? this.texY + 9 : this.texY, texWidth, texHeight);
            minecraft.font.drawWordWrap(EnumColor.BLACK + this.getMessage(), this.x + this.width + 3, this.y + (this.height - 6) / 2, 100, ColorUtil.to32BitColor(255, 5, 5, 5));
        }
    }

    @Override
    public void blit(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        int texWidth = this.isSelected ? 12 : 9;
        int texHeight = this.isSelected ? 16 : 9;
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuilder();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
        worldRenderer.vertex(par1 + 0, par2 + par6, this.getBlitOffset()).uv((par3 + 0) * f, (par4 + texHeight) * f1).endVertex();
        worldRenderer.vertex(par1 + par5, par2 + par6, this.getBlitOffset()).uv((par3 + texWidth) * f, (par4 + texHeight) * f1).endVertex();
        worldRenderer.vertex(par1 + par5, par2 + 0, this.getBlitOffset()).uv((par3 + texWidth) * f, (par4 + 0) * f1).endVertex();
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

    public int willFit(int max)
    {
        int size = Minecraft.getInstance().font.split(this.getMessage(), 100).size() * Minecraft.getInstance().font.lineHeight;
        GCLog.debug(getMessage() + " " + size + " " + max);
        if (size > max)
        {
            return -1;
        }
        return size;
    }

    public interface ICheckBoxCallback
    {
        void onSelectionChanged(GuiElementCheckboxPreLaunch checkbox, boolean newSelected);

        boolean canPlayerEdit(GuiElementCheckboxPreLaunch checkbox, Player player);

        boolean getInitiallySelected(GuiElementCheckboxPreLaunch checkbox);

        void onIntruderInteraction();
    }
}
