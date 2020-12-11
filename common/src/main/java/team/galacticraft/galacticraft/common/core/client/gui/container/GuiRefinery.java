package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.common.core.energy.EnergyDisplayHelper;
import team.galacticraft.galacticraft.common.core.inventory.ContainerRefinery;
import team.galacticraft.galacticraft.common.core.network.PacketSimple;
import team.galacticraft.galacticraft.common.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.common.core.tile.TileEntityRefinery;
import team.galacticraft.galacticraft.common.core.util.EnumColor;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import me.shedaniel.architectury.fluid.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiRefinery extends GuiContainerGC<ContainerRefinery>
{
    private static final ResourceLocation refineryTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/refinery.png");

    private final TileEntityRefinery refinery;

    private Button buttonDisable;

    private final GuiElementInfoRegion fuelTankRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 153, (this.height - this.imageHeight) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion oilTankRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 7, (this.height - this.imageHeight) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 62, (this.height - this.imageHeight) / 2 + 16, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiRefinery(ContainerRefinery container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerRefinery(playerInv, refinery, Minecraft.getInstance().player), playerInv, new TranslationTextComponent("container.refinery"));
        this.refinery = container.getRefinery();
        this.imageHeight = 168;
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> oilTankDesc = new ArrayList<String>();
        oilTankDesc.add(I18n.get("gui.oil_tank.desc.0"));
        oilTankDesc.add(I18n.get("gui.oil_tank.desc.1"));
        int oilLevel = this.refinery.oilTank != null && this.refinery.oilTank.getFluid() != FluidStack.EMPTY ? this.refinery.oilTank.getFluid().getAmount() : 0;
        int oilCapacity = this.refinery.oilTank != null ? this.refinery.oilTank.getCapacity() : 0;
        oilTankDesc.add(EnumColor.YELLOW + I18n.get("gui.message.oil") + ": " + oilLevel + " / " + oilCapacity);
        this.oilTankRegion.tooltipStrings = oilTankDesc;
        this.oilTankRegion.xPosition = (this.width - this.imageWidth) / 2 + 7;
        this.oilTankRegion.yPosition = (this.height - this.imageHeight) / 2 + 28;
        this.oilTankRegion.parentWidth = this.width;
        this.oilTankRegion.parentHeight = this.height;
        this.infoRegions.add(this.oilTankRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.0"));
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 37, (this.height - this.imageHeight) / 2 + 50, 18, 18, batterySlotDesc, this.width, this.height, this));
        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(I18n.get("gui.fuel_tank.desc.4"));
        int fuelLevel = this.refinery.fuelTank != null && this.refinery.fuelTank.getFluid() != FluidStack.EMPTY ? this.refinery.fuelTank.getFluid().getAmount() : 0;
        int fuelCapacity = this.refinery.fuelTank != null ? this.refinery.fuelTank.getCapacity() : 0;
        fuelTankDesc.add(EnumColor.YELLOW + I18n.get("gui.message.fuel") + ": " + fuelLevel + " / " + fuelCapacity);
        this.fuelTankRegion.tooltipStrings = fuelTankDesc;
        this.fuelTankRegion.xPosition = (this.width - this.imageWidth) / 2 + 153;
        this.fuelTankRegion.yPosition = (this.height - this.imageHeight) / 2 + 28;
        this.fuelTankRegion.parentWidth = this.width;
        this.fuelTankRegion.parentHeight = this.height;
        this.infoRegions.add(this.fuelTankRegion);
        List<String> fuelSlotDesc = new ArrayList<String>();
        fuelSlotDesc.add(I18n.get("gui.fuel_output.desc.0"));
        fuelSlotDesc.add(I18n.get("gui.fuel_output.desc.1"));
        fuelSlotDesc.add(I18n.get("gui.fuel_output.desc.2"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 152, (this.height - this.imageHeight) / 2 + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ((int) Math.floor(this.refinery.getEnergyStoredGC()) + " / " + (int) Math.floor(this.refinery.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 62;
        this.electricInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 16;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        this.buttons.add(this.buttonDisable = new Button(this.width / 2 - 39, this.height / 2 - 56, 76, 20, I18n.get("gui.button.refine"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.minecraft.level), new Object[]{this.refinery.getBlockPos(), 0}));
        }));
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        GCCoreUtil.drawStringCentered(this.title.getColoredString(), this.imageWidth / 2, 5, 4210752, this.font);
        String displayText = "";
        int yOffset = -18;

        String missingInput = null;
        if (this.refinery.oilTank.getFluid() == FluidStack.EMPTY || this.refinery.oilTank.getFluidAmount() == 0)
        {
            missingInput = EnumColor.RED + I18n.get("gui.status.nooil");
        }
        String activeString = this.refinery.canProcess() ? EnumColor.BRIGHT_GREEN + I18n.get("gui.status.refining") : null;
        displayText = this.refinery.getGUIstatus(missingInput, activeString, false);

        this.buttonDisable.active = this.refinery.disableCooldown == 0;
        this.buttonDisable.setMessage(this.refinery.processTicks == 0 ? I18n.get("gui.button.refine") : I18n.get("gui.button.stoprefine"));
        this.font.draw(I18n.get("gui.message.status") + ": ", 60, 45 + 23 + yOffset, 4210752);
        this.font.draw(displayText, 60, 45 + 34 + yOffset, 4210752);
        //		this.font.drawString(ElectricityDisplay.getDisplay(this.tileEntity.ueWattsPerTick * 20, ElectricUnit.WATT), 72, 56 + 23 + yOffset, 4210752);
        //		this.font.drawString(ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 72, 68 + 23 + yOffset, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 118 + 2 + 23, 4210752);
    }

    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bind(GuiRefinery.refineryTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int containerWidth = (this.width - this.imageWidth) / 2;
        int containerHeight = (this.height - this.imageHeight) / 2;
        this.blit(containerWidth, containerHeight, 0, 0, this.imageWidth, this.imageHeight);

        int displayInt = this.refinery.getScaledOilLevel(38);
        this.blit((this.width - this.imageWidth) / 2 + 7, (this.height - this.imageHeight) / 2 + 17 + 49 - displayInt, 176, 38 - displayInt, 16, displayInt);

        displayInt = this.refinery.getScaledFuelLevel(38);
        this.blit((this.width - this.imageWidth) / 2 + 153, (this.height - this.imageHeight) / 2 + 17 + 49 - displayInt, 176 + 16, 38 - displayInt, 16, displayInt);

        List<String> oilTankDesc = new ArrayList<String>();
        oilTankDesc.add(I18n.get("gui.oil_tank.desc.0"));
        oilTankDesc.add(I18n.get("gui.oil_tank.desc.1"));
        int oilLevel = this.refinery.oilTank != null && this.refinery.oilTank.getFluid() != FluidStack.EMPTY ? this.refinery.oilTank.getFluid().getAmount() : 0;
        int oilCapacity = this.refinery.oilTank != null ? this.refinery.oilTank.getCapacity() : 0;
        oilTankDesc.add(EnumColor.YELLOW + I18n.get("gui.message.oil") + ": " + oilLevel + " / " + oilCapacity);
        this.oilTankRegion.tooltipStrings = oilTankDesc;

        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(I18n.get("gui.fuel_tank.desc.4"));
        int fuelLevel = this.refinery.fuelTank != null && this.refinery.fuelTank.getFluid() != FluidStack.EMPTY ? this.refinery.fuelTank.getFluid().getAmount() : 0;
        int fuelCapacity = this.refinery.fuelTank != null ? this.refinery.fuelTank.getCapacity() : 0;
        fuelTankDesc.add(EnumColor.YELLOW + I18n.get("gui.message.fuel") + ": " + fuelLevel + " / " + fuelCapacity);
        this.fuelTankRegion.tooltipStrings = fuelTankDesc;

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
//		electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ((int) Math.floor(this.tileEntity.getEnergyStoredGC()) + " / " + (int) Math.floor(this.tileEntity.getMaxEnergyStoredGC())));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.refinery.getEnergyStoredGC(), this.refinery.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.refinery.getEnergyStoredGC() > 0)
        {
            this.blit(containerWidth + 49, containerHeight + 16, 208, 0, 11, 10);
        }

        this.blit(containerWidth + 63, containerHeight + 17, 176, 38, Math.min(this.refinery.getScaledElecticalLevel(54), 54), 7);
    }
}
