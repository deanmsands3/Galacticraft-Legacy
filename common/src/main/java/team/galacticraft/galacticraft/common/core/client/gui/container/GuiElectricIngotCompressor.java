package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.common.core.energy.EnergyDisplayHelper;
import team.galacticraft.galacticraft.common.core.inventory.ContainerElectricFurnace;
import team.galacticraft.galacticraft.common.core.inventory.ContainerElectricIngotCompressor;
import team.galacticraft.galacticraft.common.core.tile.TileEntityElectricIngotCompressor;
import team.galacticraft.galacticraft.common.core.util.EnumColor;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiElectricIngotCompressor extends GuiContainerGC<ContainerElectricIngotCompressor>
{
    private static final ResourceLocation electricFurnaceTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/electric_ingot_compressor.png");
    private TileEntityElectricIngotCompressor compressor;
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 56, 9, null, 0, 0, this);
    private final GuiElementInfoRegion processInfoRegion = new GuiElementInfoRegion(0, 0, 52, 25, null, 0, 0, this);

    public GuiElectricIngotCompressor(ContainerElectricIngotCompressor container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerElectricIngotCompressor(playerInv, compressor), playerInv, new TranslationTextComponent(compressor.getTierGC() == 2 ? "tile.machine2.4" : "tile.machine4.11"));
        this.compressor = container.getCompressor();
        this.imageHeight = 199;
    }

    @Override
    protected void init()
    {
        super.init();
        this.electricInfoRegion.tooltipStrings = new ArrayList<String>();
        this.electricInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 17;
        this.electricInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 95;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.0"));
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 54, (this.height - this.imageHeight) / 2 + 74, 18, 18, batterySlotDesc, this.width, this.height, this));
        this.processInfoRegion.tooltipStrings = new ArrayList<String>();
        this.processInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 77;
        this.processInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 30;
        this.processInfoRegion.parentWidth = this.width;
        this.processInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.processInfoRegion);
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(this.title.getColoredString(), 10, 6, 4210752);
        String displayText;

        if (this.compressor.processTicks > 0)
        {
            displayText = EnumColor.BRIGHT_GREEN + I18n.get("gui.status.running");
        }
        else
        {
            displayText = EnumColor.ORANGE + I18n.get("gui.status.idle");
        }

        String str = I18n.get("gui.message.status") + ": " + this.compressor.getGUIstatus(displayText, null, true);
        this.font.draw(str, 120 - this.font.width(str) / 2, 75, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 93, 4210752);
//		str = "" + this.tileEntity.storage.getMaxExtract();
//		this.font.drawString(str, 120 - this.font.getStringWidth(str) / 2, 85, 4210752);
//		//		str = ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE);
//		this.font.drawString(str, 120 - this.font.getStringWidth(str) / 2, 95, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bind(GuiElectricIngotCompressor.electricFurnaceTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int containerWidth = (this.width - this.imageWidth) / 2;
        int containerHeight = (this.height - this.imageHeight) / 2;
        this.blit(containerWidth, containerHeight, 0, 0, this.imageWidth, this.imageHeight);
        int scale;

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.compressor.getEnergyStoredGC(), this.compressor.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.compressor.processTicks > 0)
        {
            scale = (int) ((double) this.compressor.processTicks / (double) this.compressor.processTimeRequired * 100);
        }
        else
        {
            scale = 0;
        }

        List<String> processDesc = new ArrayList<String>();
        processDesc.clear();
        processDesc.add(I18n.get("gui.electric_compressor.desc.0") + ": " + scale + "%");
        this.processInfoRegion.tooltipStrings = processDesc;

        if (this.compressor.processTicks > 0)
        {
            scale = (int) ((double) this.compressor.processTicks / (double) this.compressor.processTimeRequired * 54);
            this.blit(containerWidth + 77, containerHeight + 38, 176, 13, scale, 17);
        }

        if (this.compressor.getEnergyStoredGC() > 0)
        {
            scale = this.compressor.getScaledElecticalLevel(54);
            this.blit(containerWidth + 116 - 98, containerHeight + 96, 176, 30, scale, 7);
            this.blit(containerWidth + 4, containerHeight + 95, 176, 37, 11, 10);
        }

        if (this.compressor.processTicks > this.compressor.processTimeRequired / 2)
        {
            this.blit(containerWidth + 101, containerHeight + 30, 176, 0, 15, 13);
        }
    }
}
