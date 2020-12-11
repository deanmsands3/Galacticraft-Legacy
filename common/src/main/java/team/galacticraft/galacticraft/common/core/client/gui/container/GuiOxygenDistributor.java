package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.client.gui.element.GuiElementCheckbox;
import team.galacticraft.galacticraft.common.core.client.gui.element.GuiElementCheckbox.ICheckBoxCallback;
import team.galacticraft.galacticraft.common.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.common.core.energy.EnergyDisplayHelper;
import team.galacticraft.galacticraft.common.core.inventory.ContainerOxygenDistributor;
import team.galacticraft.galacticraft.common.core.network.PacketSimple;
import team.galacticraft.galacticraft.common.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.common.core.tile.TileEntityOxygenDistributor;
import team.galacticraft.galacticraft.common.core.util.EnumColor;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiOxygenDistributor extends GuiContainerGC<ContainerOxygenDistributor> implements ICheckBoxCallback
{
    private static final ResourceLocation distributorTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/oxygen_distributor.png");

    private final TileEntityOxygenDistributor distributor;

    private final GuiElementInfoRegion oxygenInfoRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 112, (this.height - this.imageHeight) / 2 + 24, 56, 9, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 112, (this.height - this.imageHeight) / 2 + 37, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    private GuiElementCheckbox checkboxRenderBubble;

    public GuiOxygenDistributor(ContainerOxygenDistributor container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerOxygenDistributor(playerInv, distributor), playerInv, new TranslationTextComponent("container.oxygen_distributor"));
        this.distributor = container.getDistributor();
        this.imageHeight = 180;
    }

    @Override
    protected void init()
    {
        super.init();
        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - this.imageHeight) / 2;
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.0"));
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 46, (this.height - this.imageHeight) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height, this));
        List<String> oxygenSlotDesc = new ArrayList<String>();
        oxygenSlotDesc.add(I18n.get("gui.oxygen_slot.desc.0"));
        oxygenSlotDesc.add(I18n.get("gui.oxygen_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 16, (this.height - this.imageHeight) / 2 + 26, 18, 18, oxygenSlotDesc, this.width, this.height, this));
        List<String> oxygenDesc = new ArrayList<String>();
        oxygenDesc.add(I18n.get("gui.oxygen_storage.desc.0"));
        oxygenDesc.add(EnumColor.YELLOW + I18n.get("gui.oxygen_storage.desc.1") + ": " + ((int) Math.floor(this.distributor.getOxygenStored()) + " / " + (int) Math.floor(this.distributor.getMaxOxygenStored())));
        this.oxygenInfoRegion.tooltipStrings = oxygenDesc;
        this.oxygenInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 112;
        this.oxygenInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 24;
        this.oxygenInfoRegion.parentWidth = this.width;
        this.oxygenInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.oxygenInfoRegion);
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ((int) Math.floor(this.distributor.getEnergyStoredGC()) + " / " + (int) Math.floor(this.distributor.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 112;
        this.electricInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 37;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        this.checkboxRenderBubble = new GuiElementCheckbox(this, var5 + 85, var6 + 87, I18n.get("gui.message.bubble_visible"));
        this.buttons.add(this.checkboxRenderBubble);
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(this.title.getColoredString(), 8, 10, 4210752);
        GCCoreUtil.drawStringRightAligned(I18n.get("gui.message.in") + ":", 99, 26, 4210752, this.font);
        GCCoreUtil.drawStringRightAligned(I18n.get("gui.message.in") + ":", 99, 38, 4210752, this.font);
        String status = I18n.get("gui.message.status") + ": " + this.getStatus();
        this.font.draw(status, this.imageWidth / 2 - this.font.width(status) / 2, 50, 4210752);
        status = I18n.get("gui.oxygen_use.desc") + ": " + this.distributor.oxygenPerTick * 20 + I18n.get("gui.per_second");
        this.font.draw(status, this.imageWidth / 2 - this.font.width(status) / 2, 60, 4210752);
        //		status = ElectricityDisplay.getDisplay(this.distributor.ueWattsPerTick * 20, ElectricUnit.WATT);
        //		this.font.drawString(status, this.xSize / 2 - this.font.getStringWidth(status) / 2, 70, 4210752);
        //		status = ElectricityDisplay.getDisplay(this.distributor.getVoltage(), ElectricUnit.VOLTAGE);
        //		this.font.drawString(status, this.xSize / 2 - this.font.getStringWidth(status) / 2, 80, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 90 + 3, 4210752);
    }

    private String getStatus()
    {
        if (this.distributor.getOxygenStored() < this.distributor.oxygenPerTick)
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.missingoxygen");
        }

        return this.distributor.getGUIstatus();
    }

    @Override
    protected void renderBg(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GuiOxygenDistributor.distributorTexture);
        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - this.imageHeight) / 2;
        this.blit(var5, var6 + 5, 0, 0, this.imageWidth, 181);

        if (this.distributor != null)
        {
            int scale = this.distributor.getCappedScaledOxygenLevel(54);
            this.blit(var5 + 113, var6 + 25, 197, 7, Math.min(scale, 54), 7);
            scale = this.distributor.getScaledElecticalLevel(54);
            this.blit(var5 + 113, var6 + 38, 197, 0, Math.min(scale, 54), 7);

            if (this.distributor.getEnergyStoredGC() > 0)
            {
                this.blit(var5 + 99, var6 + 37, 176, 0, 11, 10);
            }

            if (this.distributor.getOxygenStored() > 0)
            {
                this.blit(var5 + 100, var6 + 24, 187, 0, 10, 10);
            }

            List<String> oxygenDesc = new ArrayList<String>();
            oxygenDesc.add(I18n.get("gui.oxygen_storage.desc.0"));
            oxygenDesc.add(EnumColor.YELLOW + I18n.get("gui.oxygen_storage.desc.1") + ": " + ((int) Math.floor(this.distributor.getOxygenStored()) + " / " + (int) Math.floor(this.distributor.getMaxOxygenStored())));
            this.oxygenInfoRegion.tooltipStrings = oxygenDesc;

            List<String> electricityDesc = new ArrayList<String>();
            electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
            EnergyDisplayHelper.getEnergyDisplayTooltip(this.distributor.getEnergyStoredGC(), this.distributor.getMaxEnergyStoredGC(), electricityDesc);
//			electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ((int) Math.floor(this.distributor.getEnergyStoredGC()) + " / " + (int) Math.floor(this.distributor.getMaxEnergyStoredGC())));
            this.electricInfoRegion.tooltipStrings = electricityDesc;

            this.checkboxRenderBubble.isSelected = this.distributor.shouldRenderBubble;
        }
    }

    @Override
    public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
    {
        this.distributor.setBubbleVisible(newSelected);
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionType(minecraft.level), new Object[]{6, this.distributor.getBlockPos(), newSelected ? 1 : 0}));
    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckbox checkbox, Player player)
    {
        return true;
    }

    @Override
    public boolean getInitiallySelected(GuiElementCheckbox checkbox)
    {
        return this.distributor.shouldRenderBubble;
    }

    @Override
    public void onIntruderInteraction()
    {

    }
}
