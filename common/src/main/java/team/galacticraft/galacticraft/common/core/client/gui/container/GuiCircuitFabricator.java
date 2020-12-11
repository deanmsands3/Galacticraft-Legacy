package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.common.core.energy.EnergyDisplayHelper;
import team.galacticraft.galacticraft.common.core.inventory.ContainerCircuitFabricator;
import team.galacticraft.galacticraft.common.core.tile.TileEntityCircuitFabricator;
import team.galacticraft.galacticraft.common.core.util.EnumColor;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiCircuitFabricator extends GuiContainerGC<ContainerCircuitFabricator>
{
    private static final ResourceLocation CIRCUIT_FABRICATOR_TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/circuit_fabricator.png");
    private final TileEntityCircuitFabricator fabricator;
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 56, 9, null, 0, 0, this);
    private final GuiElementInfoRegion processInfoRegion = new GuiElementInfoRegion(0, 0, 53, 12, null, 0, 0, this);

    public GuiCircuitFabricator(ContainerCircuitFabricator container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
        this.fabricator = container.getFabricator();
        this.imageHeight = 192;
    }

    @Override
    protected void init()
    {
        super.init();
        this.electricInfoRegion.tooltipStrings = new ArrayList<String>();
        this.electricInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 17;
        this.electricInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 88;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.0"));
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 5, (this.height - this.imageHeight) / 2 + 68, 18, 18, batterySlotDesc, this.width, this.height, this));
        this.processInfoRegion.tooltipStrings = new ArrayList<String>();
        this.processInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 87;
        this.processInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 19;
        this.processInfoRegion.parentWidth = this.width;
        this.processInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.processInfoRegion);
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(this.title.getColoredString(), 10, 6, 4210752);
        String displayText;

        if (this.fabricator.processTicks > 0)
        {
            displayText = EnumColor.BRIGHT_GREEN + I18n.get("gui.status.running");
        }
        else
        {
            displayText = EnumColor.ORANGE + I18n.get("gui.status.idle");
        }

        String str = I18n.get("gui.message.status") + ":";
        this.font.draw(str, 115 - this.font.width(str) / 2, 80, 4210752);
        displayText = this.fabricator.getGUIstatus(displayText, null, true);
        this.font.draw(displayText, 115 - this.font.width(displayText) / 2, 90, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 93, 4210752);
//		str = "" + this.tileEntity.storage.getMaxExtract();
//		this.font.drawString(str, 5, 42, 4210752);
//		//		str = ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE);
//		this.font.drawString(str, 5, 52, 4210752);
    }

    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bind(GuiCircuitFabricator.CIRCUIT_FABRICATOR_TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int containerWidth = (this.width - this.imageWidth) / 2;
        int containerHeight = (this.height - this.imageHeight) / 2;
        this.blit(containerWidth, containerHeight, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(containerWidth + 5, containerHeight + 68, 176, 47, 18, 18);
        this.blit(containerWidth + 5, containerHeight + 89, 194, 47, 9, 8);
        this.blit(containerWidth + 17, containerHeight + 88, 176, 65, 56, 9);
        int scale;

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.fabricator.getEnergyStoredGC(), this.fabricator.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.fabricator.processTicks > 0)
        {
            scale = (this.fabricator.processTicks * 100) / this.fabricator.getProcessTimeRequired();
        }
        else
        {
            scale = 0;
        }

        List<String> processDesc = new ArrayList<String>();
        processDesc.clear();
        processDesc.add(I18n.get("gui.electric_compressor.desc.0") + ": " + scale + "%");
        this.processInfoRegion.tooltipStrings = processDesc;

        if (this.fabricator.processTicks > 0)
        {
            scale = (this.fabricator.processTicks * 51) / this.fabricator.getProcessTimeRequired();
            this.blit(containerWidth + 88, containerHeight + 20, 176, 17 + this.fabricator.processTicks % 9 / 3 * 10, scale, 10);
        }

        if (this.fabricator.getEnergyStoredGC() > 0)
        {
            scale = this.fabricator.getScaledElecticalLevel(54);
            this.blit(containerWidth + 116 - 98, containerHeight + 89, 176, 0, scale, 7);
            this.blit(containerWidth + 4, containerHeight + 88, 176, 7, 11, 10);
        }
    }
}
