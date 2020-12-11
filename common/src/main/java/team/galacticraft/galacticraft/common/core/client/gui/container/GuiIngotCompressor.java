package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.common.core.inventory.ContainerFuelLoader;
import team.galacticraft.galacticraft.common.core.inventory.ContainerIngotCompressor;
import team.galacticraft.galacticraft.common.core.tile.TileEntityIngotCompressor;
import team.galacticraft.galacticraft.common.core.util.EnumColor;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiIngotCompressor extends GuiContainerGC<ContainerIngotCompressor>
{
    private static final ResourceLocation electricFurnaceTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/ingot_compressor.png");
    private final GuiElementInfoRegion processInfoRegion = new GuiElementInfoRegion(0, 0, 52, 25, null, 0, 0, this);

    private TileEntityIngotCompressor compressor;

    public GuiIngotCompressor(ContainerIngotCompressor container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerIngotCompressor(playerInv, compressor), playerInv, new TranslationTextComponent("tile.machine.3"));
        this.compressor = container.getCompressor();
        this.imageHeight = 192;
    }

    @Override
    protected void init()
    {
        super.init();
        this.processInfoRegion.tooltipStrings = new ArrayList<String>();
        this.processInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 77;
        this.processInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 30;
        this.processInfoRegion.parentWidth = this.width;
        this.processInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.processInfoRegion);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(this.title.getColoredString(), 10, 6, 4210752);
        String displayText = I18n.get("gui.message.fuel") + ":";
        this.font.draw(displayText, 50 - this.font.width(displayText), 79, 4210752);

        if (this.compressor.processTicks > 0)
        {
            displayText = EnumColor.BRIGHT_GREEN + I18n.get("gui.status.compressing");
        }
        else
        {
            displayText = EnumColor.ORANGE + I18n.get("gui.status.idle");
        }

        String str = I18n.get("gui.message.status") + ":";
        this.font.draw(I18n.get("gui.message.status") + ":", 120 - this.font.width(str) / 2, 70, 4210752);
        str = displayText;
        this.font.draw(displayText, 120 - this.font.width(str) / 2, 80, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bind(GuiIngotCompressor.electricFurnaceTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int process;
        int containerWidth = (this.width - this.imageWidth) / 2;
        int containerHeight = (this.height - this.imageHeight) / 2;
        this.blit(containerWidth, containerHeight, 0, 0, this.imageWidth, this.imageHeight);

        if (this.compressor.processTicks > 0)
        {
            process = (int) ((double) this.compressor.processTicks / (double) TileEntityIngotCompressor.PROCESS_TIME_REQUIRED * 100);
        }
        else
        {
            process = 0;
        }

        List<String> processDesc = new ArrayList<String>();
        processDesc.clear();
        processDesc.add(I18n.get("gui.electric_compressor.desc.0") + ": " + process + "%");
        this.processInfoRegion.tooltipStrings = processDesc;

        if (this.compressor.processTicks > 0)
        {
            int scale = (int) ((double) this.compressor.processTicks / (double) TileEntityIngotCompressor.PROCESS_TIME_REQUIRED * 54);
            this.blit(containerWidth + 77, containerHeight + 36, 176, 13, scale, 17);
        }

        if (this.compressor.furnaceBurnTime > 0)
        {
            int scale = (int) ((double) this.compressor.furnaceBurnTime / (double) this.compressor.currentItemBurnTime * 14);
            this.blit(containerWidth + 81, containerHeight + 27 + 14 - scale, 176, 30 + 14 - scale, 14, scale);
        }

        if (this.compressor.processTicks > TileEntityIngotCompressor.PROCESS_TIME_REQUIRED / 2)
        {
            this.blit(containerWidth + 101, containerHeight + 28, 176, 0, 15, 13);
        }
    }
}