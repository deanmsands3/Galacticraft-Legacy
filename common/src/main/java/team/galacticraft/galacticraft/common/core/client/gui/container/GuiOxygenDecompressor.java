package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.core.energy.EnergyDisplayHelper;
import team.galacticraft.galacticraft.core.inventory.ContainerOxygenDecompressor;
import team.galacticraft.galacticraft.core.items.ItemOxygenTank;
import team.galacticraft.galacticraft.core.tile.TileEntityOxygenDecompressor;
import team.galacticraft.galacticraft.core.util.EnumColor;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiOxygenDecompressor extends GuiContainerGC<ContainerOxygenDecompressor>
{
    private static final ResourceLocation compressorTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/decompressor.png");

    private final TileEntityOxygenDecompressor decompressor;

    private final GuiElementInfoRegion oxygenInfoRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 112, (this.height - this.imageHeight) / 2 + 24, 56, 9, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 112, (this.height - this.imageHeight) / 2 + 37, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiOxygenDecompressor(ContainerOxygenDecompressor container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerOxygenDecompressor(playerInv, decompressor, Minecraft.getInstance().player), playerInv, new TranslationTextComponent("container.oxygen_decompressor"));
        this.decompressor = container.getDecompressor();
        this.imageHeight = 180;
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.0"));
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 31, (this.height - this.imageHeight) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height, this));
        List<String> compressorSlotDesc = new ArrayList<String>();
        compressorSlotDesc.add(I18n.get("gui.oxygen_decompressor.slot.desc.0"));
        compressorSlotDesc.add(I18n.get("gui.oxygen_decompressor.slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 132, (this.height - this.imageHeight) / 2 + 70, 18, 18, compressorSlotDesc, this.width, this.height, this));
        List<String> oxygenDesc = new ArrayList<String>();
        oxygenDesc.add(I18n.get("gui.oxygen_storage.desc.0"));
        oxygenDesc.add(EnumColor.YELLOW + I18n.get("gui.oxygen_storage.desc.1") + ": " + ((int) Math.floor(this.decompressor.getOxygenStored()) + " / " + (int) Math.floor(this.decompressor.getMaxOxygenStored())));
        this.oxygenInfoRegion.tooltipStrings = oxygenDesc;
        this.oxygenInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 112;
        this.oxygenInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 24;
        this.oxygenInfoRegion.parentWidth = this.width;
        this.oxygenInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.oxygenInfoRegion);
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ((int) Math.floor(this.decompressor.getEnergyStoredGC()) + " / " + (int) Math.floor(this.decompressor.getMaxEnergyStoredGC())));
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
        status = I18n.get("gui.max_output.desc") + ": " + TileEntityOxygenDecompressor.OUTPUT_PER_TICK * 20 + I18n.get("gui.per_second");
        this.font.draw(status, this.imageWidth / 2 - this.font.width(status) / 2, 60, 4210752);
        //		status = ElectricityDisplay.getDisplay(this.decompressor.ueWattsPerTick * 20, ElectricUnit.WATT);
        //		this.font.drawString(status, this.xSize / 2 - this.font.getStringWidth(status) / 2, 70, 4210752);
        //		status = ElectricityDisplay.getDisplay(this.decompressor.getVoltage(), ElectricUnit.VOLTAGE);
        //		this.font.drawString(status, this.xSize / 2 - this.font.getStringWidth(status) / 2, 80, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 104 + 17, 4210752);
    }

    private String getStatus()
    {
        if (this.decompressor.getItem(0) == null || !(this.decompressor.getItem(0).getItem() instanceof ItemOxygenTank))
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.missingtank");
        }

        if (this.decompressor.getItem(0) != null && this.decompressor.getItem(0).getDamageValue() == this.decompressor.getItem(0).getMaxDamage())
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.tank_empty");
        }

        return this.decompressor.getGUIstatus();
    }

    @Override
    protected void renderBg(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GuiOxygenDecompressor.compressorTexture);
        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - this.imageHeight) / 2;
        this.blit(var5, var6 + 5, 0, 0, this.imageWidth, 181);

        if (this.decompressor != null)
        {
            int scale = this.decompressor.getCappedScaledOxygenLevel(54);
            this.blit(var5 + 113, var6 + 25, 197, 7, Math.min(scale, 54), 7);
            scale = this.decompressor.getScaledElecticalLevel(54);
            this.blit(var5 + 113, var6 + 38, 197, 0, Math.min(scale, 54), 7);

            if (this.decompressor.getEnergyStoredGC() > 0)
            {
                this.blit(var5 + 99, var6 + 37, 176, 0, 11, 10);
            }

            if (this.decompressor.getOxygenStored() > 0)
            {
                this.blit(var5 + 100, var6 + 24, 187, 0, 10, 10);
            }

            List<String> oxygenDesc = new ArrayList<String>();
            oxygenDesc.add(I18n.get("gui.oxygen_storage.desc.0"));
            oxygenDesc.add(EnumColor.YELLOW + I18n.get("gui.oxygen_storage.desc.1") + ": " + ((int) Math.floor(this.decompressor.getOxygenStored()) + " / " + (int) Math.floor(this.decompressor.getMaxOxygenStored())));
            this.oxygenInfoRegion.tooltipStrings = oxygenDesc;

            List<String> electricityDesc = new ArrayList<String>();
            electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
            EnergyDisplayHelper.getEnergyDisplayTooltip(this.decompressor.getEnergyStoredGC(), this.decompressor.getMaxEnergyStoredGC(), electricityDesc);
//			electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ((int) Math.floor(this.decompressor.getEnergyStoredGC()) + " / " + (int) Math.floor(this.decompressor.getMaxEnergyStoredGC())));
            this.electricInfoRegion.tooltipStrings = electricityDesc;
        }
    }
}
