package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.core.energy.EnergyDisplayHelper;
import team.galacticraft.galacticraft.core.inventory.ContainerOxygenCollector;
import team.galacticraft.galacticraft.core.inventory.ContainerOxygenCompressor;
import team.galacticraft.galacticraft.core.items.ItemOxygenTank;
import team.galacticraft.galacticraft.core.tile.TileEntityOxygenCompressor;
import team.galacticraft.galacticraft.core.util.EnumColor;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiOxygenCompressor extends GuiContainerGC<ContainerOxygenCompressor>
{
    private static final ResourceLocation compressorTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/compressor.png");

    private final TileEntityOxygenCompressor compressor;

    private final GuiElementInfoRegion oxygenInfoRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 112, (this.height - this.imageHeight) / 2 + 24, 56, 9, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 112, (this.height - this.imageHeight) / 2 + 37, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiOxygenCompressor(ContainerOxygenCompressor container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerOxygenCompressor(playerInv, compressor, Minecraft.getInstance().player), playerInv, new TranslationTextComponent("container.oxygen_compressor"));
        this.compressor = container.getCompressor();
        this.imageHeight = 180;
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.0"));
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 46, (this.height - this.imageHeight) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height, this));
        List<String> oxygenSlotDesc = new ArrayList<String>();
        oxygenSlotDesc.add(I18n.get("gui.oxygen_slot.desc.0"));
        oxygenSlotDesc.add(I18n.get("gui.oxygen_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 16, (this.height - this.imageHeight) / 2 + 26, 18, 18, oxygenSlotDesc, this.width, this.height, this));
        List<String> compressorSlotDesc = new ArrayList<String>();
        compressorSlotDesc.add(I18n.get("gui.oxygen_compressor.slot.desc.0"));
        compressorSlotDesc.add(I18n.get("gui.oxygen_compressor.slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 132, (this.height - this.imageHeight) / 2 + 70, 18, 18, compressorSlotDesc, this.width, this.height, this));
        List<String> oxygenDesc = new ArrayList<String>();
        oxygenDesc.add(I18n.get("gui.oxygen_storage.desc.0"));
        oxygenDesc.add(EnumColor.YELLOW + I18n.get("gui.oxygen_storage.desc.1") + ": " + ((int) Math.floor(this.compressor.getOxygenStored()) + " / " + (int) Math.floor(this.compressor.getMaxOxygenStored())));
        this.oxygenInfoRegion.tooltipStrings = oxygenDesc;
        this.oxygenInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 112;
        this.oxygenInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 24;
        this.oxygenInfoRegion.parentWidth = this.width;
        this.oxygenInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.oxygenInfoRegion);
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ((int) Math.floor(this.compressor.getEnergyStoredGC()) + " / " + (int) Math.floor(this.compressor.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 112;
        this.electricInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 37;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(this.title.getColoredString(), 8, 10, 4210752);
        GCCoreUtil.drawStringRightAligned(I18n.get("gui.message.in") + ":", 99, 26, 4210752, this.font);
        GCCoreUtil.drawStringRightAligned(I18n.get("gui.message.in") + ":", 99, 38, 4210752, this.font);
        String status = I18n.get("gui.message.status") + ": " + this.getStatus();
        this.font.draw(status, this.imageWidth / 2 - this.font.width(status) / 2, 50, 4210752);
        status = I18n.get("gui.oxygen_use.desc") + ": " + TileEntityOxygenCompressor.TANK_TRANSFER_SPEED * 20 + I18n.get("gui.per_second");
        this.font.draw(status, this.imageWidth / 2 - this.font.width(status) / 2, 60, 4210752);
        //		status = ElectricityDisplay.getDisplay(this.compressor.ueWattsPerTick * 20, ElectricUnit.WATT);
        //		this.font.drawString(status, this.xSize / 2 - this.font.getStringWidth(status) / 2, 70, 4210752);
        //		status = ElectricityDisplay.getDisplay(this.compressor.getVoltage(), ElectricUnit.VOLTAGE);
        //		this.font.drawString(status, this.xSize / 2 - this.font.getStringWidth(status) / 2, 80, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 104 + 17, 4210752);
    }

    private String getStatus()
    {
        if (this.compressor.getItem(0) == null || !(this.compressor.getItem(0).getItem() instanceof ItemOxygenTank))
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.missingtank");
        }

        if (this.compressor.getItem(0) != null && this.compressor.getItem(0).getDamageValue() == 0)
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.fulltank");
        }

        if (this.compressor.getOxygenStored() < 1.0D)
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.missingoxygen");
        }

        return this.compressor.getGUIstatus();
    }

    @Override
    protected void renderBg(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GuiOxygenCompressor.compressorTexture);
        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - this.imageHeight) / 2;
        this.blit(var5, var6 + 5, 0, 0, this.imageWidth, 181);

        if (this.compressor != null)
        {
            int scale = this.compressor.getCappedScaledOxygenLevel(54);
            this.blit(var5 + 113, var6 + 25, 197, 7, Math.min(scale, 54), 7);
            scale = this.compressor.getScaledElecticalLevel(54);
            this.blit(var5 + 113, var6 + 38, 197, 0, Math.min(scale, 54), 7);

            if (this.compressor.getEnergyStoredGC() > 0)
            {
                this.blit(var5 + 99, var6 + 37, 176, 0, 11, 10);
            }

            if (this.compressor.getOxygenStored() > 0)
            {
                this.blit(var5 + 100, var6 + 24, 187, 0, 10, 10);
            }

            List<String> oxygenDesc = new ArrayList<String>();
            oxygenDesc.add(I18n.get("gui.oxygen_storage.desc.0"));
            oxygenDesc.add(EnumColor.YELLOW + I18n.get("gui.oxygen_storage.desc.1") + ": " + ((int) Math.floor(this.compressor.getOxygenStored()) + " / " + (int) Math.floor(this.compressor.getMaxOxygenStored())));
            this.oxygenInfoRegion.tooltipStrings = oxygenDesc;

            List<String> electricityDesc = new ArrayList<String>();
            electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
            EnergyDisplayHelper.getEnergyDisplayTooltip(this.compressor.getEnergyStoredGC(), this.compressor.getMaxEnergyStoredGC(), electricityDesc);
//			electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ((int) Math.floor(this.compressor.getEnergyStoredGC()) + " / " + (int) Math.floor(this.compressor.getMaxEnergyStoredGC())));
            this.electricInfoRegion.tooltipStrings = electricityDesc;
        }
    }
}
