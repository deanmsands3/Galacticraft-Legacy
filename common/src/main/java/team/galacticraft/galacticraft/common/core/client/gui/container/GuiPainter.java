package team.galacticraft.galacticraft.common.core.client.gui.container;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.inventory.ContainerPainter;
import team.galacticraft.galacticraft.common.core.network.PacketSimple;
import team.galacticraft.galacticraft.common.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.common.core.tile.TileEntityPainter;
import team.galacticraft.galacticraft.common.core.util.ColorUtil;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

public class GuiPainter extends GuiContainerGC<ContainerPainter>
{
    private static final ResourceLocation painterTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/painter.png");

    private final TileEntityPainter painter;

    public GuiPainter(ContainerPainter container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerPainter(playerInv, painter), playerInv, new TranslationTextComponent("tile.machine3.9"));
        this.painter = container.getPainter();
        this.imageHeight = 186;
    }

    @Override
    protected void init()
    {
        super.init();
        this.buttons.add(new Button(this.width / 2 + 4, this.height / 2 - 48, 76, 20, I18n.get("gui.button.paintapply"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.minecraft.level), new Object[]{this.painter.getBlockPos(), 0}));
            painter.buttonPressed(0, this.minecraft.player);
        }));
        this.buttons.add(new Button(this.width / 2 - 80, this.height / 2 - 48, 76, 20, I18n.get("gui.button.paintmix"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.minecraft.level), new Object[]{this.painter.getBlockPos(), 1}));
            painter.buttonPressed(1, this.minecraft.player);
        }));
        this.buttons.add(new Button(this.width / 2 - 80, this.height / 2 - 48 + 22, 76, 20, I18n.get("gui.button.paintreset"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.minecraft.level), new Object[]{this.painter.getBlockPos(), 2}));
            painter.buttonPressed(2, this.minecraft.player);
        }));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(this.title.getColoredString(), 39, 6, 4210752);
        String displayText = "";

        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bind(GuiPainter.painterTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int guiLeft = (this.width - this.imageWidth) / 2;
        int guiBottom = (this.height - this.imageHeight) / 2;
        this.blit(guiLeft, guiBottom, 0, 0, this.imageWidth, this.imageHeight);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        ColorUtil.setGLColor(painter.guiColor);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuilder();
        int x = guiLeft + this.imageWidth / 2 - 9;
        int y = guiBottom + this.imageHeight / 2 - 69;
        int height = 18;
        int width = 18;
        worldrenderer.begin(7, DefaultVertexFormat.POSITION);
        worldrenderer.vertex(x + 0F, y + height, this.getBlitOffset()).endVertex();
        worldrenderer.vertex(x + width, y + height, this.getBlitOffset()).endVertex();
        worldrenderer.vertex(x + width, y + 0, this.getBlitOffset()).endVertex();
        worldrenderer.vertex(x + 0F, y + 0, this.getBlitOffset()).endVertex();
        tessellator.end();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
