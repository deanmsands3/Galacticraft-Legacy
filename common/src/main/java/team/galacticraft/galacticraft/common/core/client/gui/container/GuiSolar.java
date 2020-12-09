package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GalacticraftCore;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.core.energy.EnergyDisplayHelper;
import team.galacticraft.galacticraft.core.inventory.ContainerSolar;
import team.galacticraft.galacticraft.core.network.PacketSimple;
import team.galacticraft.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.core.tile.TileEntitySolar;
import team.galacticraft.galacticraft.core.util.EnumColor;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiSolar extends GuiContainerGC<ContainerSolar>
{
    private static final ResourceLocation solarGuiTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/solar.png");

    private final TileEntitySolar solarPanel;

    private Button buttonEnableSolar;
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 107, (this.height - this.imageHeight) / 2 + 101, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiSolar(ContainerSolar container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerSolar(playerInv, solarPanel), playerInv, new TranslationTextComponent(solarPanel.getTierGC() == 1 ? "container.solar_basic" : "container.solar_advanced"));
        this.solarPanel = container.getSolarTile();
        this.imageHeight = 201;
        this.imageWidth = 176;
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ((int) Math.floor(this.solarPanel.getEnergyStoredGC()) + " / " + (int) Math.floor(this.solarPanel.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 96;
        this.electricInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 24;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.0"));
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 151, (this.height - this.imageHeight) / 2 + 82, 18, 18, batterySlotDesc, this.width, this.height, this));
        List<String> sunGenDesc = new ArrayList<String>();
        float sunVisible = Math.round(this.solarPanel.solarStrength / 9.0F * 1000) / 10.0F;
        sunGenDesc.add(this.solarPanel.solarStrength > 0 ? I18n.get("gui.status.sun_visible") + ": " + sunVisible + "%" : I18n.get("gui.status.blockedfully"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 47, (this.height - this.imageHeight) / 2 + 20, 18, 18, sunGenDesc, this.width, this.height, this));
        this.buttons.add(this.buttonEnableSolar = new Button(this.width / 2 - 36, this.height / 2 - 19, 72, 20, I18n.get("gui.button.enable"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.minecraft.level), new Object[]{this.solarPanel.getBlockPos(), 0}));
        }));
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        int offsetY = 35;
        this.buttonEnableSolar.active = this.solarPanel.disableCooldown == 0;
        this.buttonEnableSolar.setMessage(!this.solarPanel.getDisabled(0) ? I18n.get("gui.button.disable") : I18n.get("gui.button.enable"));
        String message = this.title.getColoredString();
        this.font.draw(message, this.imageWidth / 2 - this.font.width(message) / 2, 7, 4210752);
        message = I18n.get("gui.message.status") + ": " + this.getStatus();
        this.font.draw(message, this.imageWidth / 2 - this.font.width(message) / 2, 45 + 23 - 46 + offsetY, 4210752);
        message = I18n.get("gui.message.generating") + ": " + (this.solarPanel.generateWatts > 0 ? EnergyDisplayHelper.getEnergyDisplayS(this.solarPanel.generateWatts) + "/t" : I18n.get("gui.status.not_generating"));
        this.font.draw(message, this.imageWidth / 2 - this.font.width(message) / 2, 34 + 23 - 46 + offsetY, 4210752);
        float boost = Math.round((this.solarPanel.getSolarBoost() - 1) * 1000) / 10.0F;
        message = I18n.get("gui.message.environment") + ": " + boost + "%";
        this.font.draw(message, this.imageWidth / 2 - this.font.width(message) / 2, 56 + 23 - 46 + offsetY, 4210752);
        //		message = ElectricityDisplay.getDisplay(this.solarPanel.getVoltage(), ElectricUnit.VOLTAGE);
        //		this.font.drawString(message, this.xSize / 2 - this.font.getStringWidth(message) / 2, 68 + 23 - 46 + offsetY, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 94, 4210752);
    }

    private String getStatus()
    {
        if (this.solarPanel.getDisabled(0))
        {
            return EnumColor.ORANGE + I18n.get("gui.status.disabled");
        }

        if (!this.solarPanel.getLevel().isDay())
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.blockedfully");
        }

        if (this.solarPanel.getLevel().isRaining() || this.solarPanel.getLevel().isThundering())
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.raining");
        }

        if (this.solarPanel.solarStrength == 0)
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.blockedfully");
        }

        if (this.solarPanel.solarStrength < 9)
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.blockedpartial");
        }

        if (this.solarPanel.generateWatts > 0)
        {
            return EnumColor.DARK_GREEN + I18n.get("gui.status.collectingenergy");
        }

        return EnumColor.ORANGE + I18n.get("gui.status.unknown");
    }

    @Override
    protected void renderBg(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GuiSolar.solarGuiTexture);
        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - this.imageHeight) / 2;
        this.blit(var5, var6, 0, 0, this.imageWidth, this.imageHeight);

        List<String> electricityDesc = new ArrayList<String>();
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.solarPanel.getEnergyStoredGC(), this.solarPanel.getMaxEnergyStoredGC(), electricityDesc);
//		electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
//		electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ((int) Math.floor(this.solarPanel.getEnergyStoredGC()) + " / " + (int) Math.floor(this.solarPanel.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.solarPanel.getEnergyStoredGC() > 0)
        {
            this.blit(var5 + 83, var6 + 24, 176, 0, 11, 10);
        }

        if (this.solarPanel.solarStrength > 0)
        {
            this.blit(var5 + 48, var6 + 21, 176, 10, 16, 16);
        }

        this.blit(var5 + 97, var6 + 25, 187, 0, Math.min(this.solarPanel.getScaledElecticalLevel(54), 54), 7);
    }
}
