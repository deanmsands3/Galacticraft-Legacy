package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerElectrolyzer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityElectrolyzer;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiWaterElectrolyzer extends GuiContainerGC<ContainerElectrolyzer>
{
    private static final ResourceLocation refineryTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/gas_liquefier.png");

    private static final ResourceLocation gasTextures = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/gases_methane_oxygen_nitrogen.png");

    private final TileEntityElectrolyzer tileEntity;

    private Button buttonDisable;

    private final GuiElementInfoRegion fuelTankRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 153, (this.height - this.imageHeight) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion fuelTank2Region = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 153, (this.height - this.imageHeight) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion gasTankRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 7, (this.height - this.imageHeight) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 62, (this.height - this.imageHeight) / 2 + 16, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiWaterElectrolyzer(ContainerElectrolyzer container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
        this.tileEntity = container.getElectrolyzer();
        this.imageHeight = 168;
    }

    @Override
    public void init()
    {
        super.init();

        this.gasTankRegion.xPosition = (this.width - this.imageWidth) / 2 + 7;
        this.gasTankRegion.yPosition = (this.height - this.imageHeight) / 2 + 28;
        this.gasTankRegion.parentWidth = this.width;
        this.gasTankRegion.parentHeight = this.height;
        this.infoRegions.add(this.gasTankRegion);

        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 33, (this.height - this.imageHeight) / 2 + 49, 18, 18, batterySlotDesc, this.width, this.height, this));

        this.fuelTankRegion.xPosition = (this.width - this.imageWidth) / 2 + 132;
        this.fuelTankRegion.yPosition = (this.height - this.imageHeight) / 2 + 28;
        this.fuelTankRegion.parentWidth = this.width;
        this.fuelTankRegion.parentHeight = this.height;
        this.infoRegions.add(this.fuelTankRegion);

        this.fuelTank2Region.xPosition = (this.width - this.imageWidth) / 2 + 153;
        this.fuelTank2Region.yPosition = (this.height - this.imageHeight) / 2 + 28;
        this.fuelTank2Region.parentWidth = this.width;
        this.fuelTank2Region.parentHeight = this.height;
        this.infoRegions.add(this.fuelTank2Region);

        //this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 152, (this.height - this.ySize) / 2 + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));
        //this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 131, (this.height - this.ySize) / 2 + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));

        List<String> fuelSlotDesc = new ArrayList<String>();
        fuelSlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.water_bucket_slot.desc"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 6, (this.height - this.imageHeight) / 2 + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));

        this.electricInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 42;
        this.electricInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 16;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);

        this.addToolTips();

        this.buttons.add(this.buttonDisable = new Button(this.width / 2 - 49, this.height / 2 - 56, 76, 20, GCCoreUtil.translate("gui.button.liquefy"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.tileEntity.getLevel()), new Object[]{this.tileEntity.getBlockPos(), 0}));
        }));
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(this.title.getColoredString(), 30, 5, 4210752);
        String displayText = "";
        int yOffset = -18;

        if (RedstoneUtil.isBlockReceivingRedstone(this.tileEntity.getLevel(), this.tileEntity.getBlockPos()))
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.off");
        }
        else if (!this.tileEntity.hasEnoughEnergyToRun)
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.message.low_energy");
        }
        else if (this.tileEntity.waterTank.getFluid() == FluidStack.EMPTY || this.tileEntity.waterTank.getFluidAmount() == 0)
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.message.zero_water");
        }
        else if (this.tileEntity.waterTank.getFluidAmount() > 0 && this.tileEntity.disabled)
        {
            displayText = EnumColor.ORANGE + GCCoreUtil.translate("gui.status.ready");
        }
        else if (this.tileEntity.canProcess())
        {
            displayText = EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.status.running");
        }
        else if (this.tileEntity.liquidTank.getFluidAmount() == this.tileEntity.liquidTank.getCapacity() && this.tileEntity.liquidTank2.getFluidAmount() == this.tileEntity.liquidTank2.getCapacity())
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.tanksfull");
        }
        else
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.unknown");
        }

        this.buttonDisable.active = this.tileEntity.disableCooldown == 0;
        this.buttonDisable.setMessage(this.tileEntity.processTicks == 0 ? GCCoreUtil.translate("gui.button.liquefy") : GCCoreUtil.translate("gui.button.liquefy_stop"));
        this.font.draw(GCCoreUtil.translate("gui.message.status") + ":", 56, 45 + 23 + yOffset, 4210752);
        this.font.draw(displayText, 62, 45 + 33 + yOffset, 4210752);
        //		this.font.drawString(ElectricityDisplay.getDisplay(this.tileEntity.ueWattsPerTick * 20, ElectricUnit.WATT), 72, 56 + 23 + yOffset, 4210752);
        //		this.font.drawString(ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 72, 68 + 23 + yOffset, 4210752);
        this.font.draw(GCCoreUtil.translate("container.inventory"), 8, this.imageHeight - 118 + 2 + 23, 4210752);
    }

    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bind(GuiWaterElectrolyzer.refineryTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int edgeLeft = (this.width - this.imageWidth) / 2;
        int edgeTop = (this.height - this.imageHeight) / 2;
        this.blit(edgeLeft, edgeTop, 0, 0, this.imageWidth, this.imageHeight);

        int waterLevel = this.tileEntity.getScaledGasLevel(38);
        //Water
        this.minecraft.textureManager.bind(GuiWaterElectrolyzer.gasTextures);
        this.blit(edgeLeft + 7, edgeTop + 17 + 49 - waterLevel, 1 + 4 * 17, 38 - waterLevel, 16, waterLevel);
        //Oxygen
        int displayInt = this.tileEntity.getScaledFuelLevel(38);
        this.blit(edgeLeft + 132, edgeTop + 17 + 49 - displayInt, 34 + 1, 38 - displayInt, 16, displayInt);
        //Hydrogen
        displayInt = this.tileEntity.getScaledFuelLevel2(38);
        this.blit(edgeLeft + 153, edgeTop + 17 + 49 - displayInt, 17 + 1, 38 - displayInt, 16, displayInt);

        this.addToolTips();

        this.minecraft.textureManager.bind(GuiWaterElectrolyzer.refineryTexture);

        if (this.tileEntity.getEnergyStoredGC() > 0)
        {
            this.blit(edgeLeft + 28, edgeTop + 16, 208, 0, 11, 10);
        }

        this.blit(edgeLeft + 42, edgeTop + 17, 176, 38, Math.min(this.tileEntity.getScaledElecticalLevel(54), 54), 7);
    }

    private void addToolTips()
    {
        List<String> gasTankDesc = new ArrayList<String>();
        gasTankDesc.add(GCCoreUtil.translate("gui.terraformer.desc.0"));
        this.gasTankRegion.tooltipStrings = gasTankDesc;

        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(GCCoreUtil.translate("gui.gas_tank.desc.0"));
        FluidStack gasTankContents = this.tileEntity.liquidTank != null ? this.tileEntity.liquidTank.getFluid() : null;
        if (gasTankContents != null)
        {
            String gasname = GCCoreUtil.translate(gasTankContents.getFluid().getAttributes().getTranslationKey());
            fuelTankDesc.add("(" + gasname + ")");
        }
        else
        {
            fuelTankDesc.add(" ");
        }
        int fuelLevel = gasTankContents != null ? gasTankContents.getAmount() : 0;
        int fuelCapacity = this.tileEntity.liquidTank != null ? this.tileEntity.liquidTank.getCapacity() : 0;
        fuelTankDesc.add(EnumColor.YELLOW + " " + fuelLevel + " / " + fuelCapacity);
        this.fuelTankRegion.tooltipStrings = fuelTankDesc;

        fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(GCCoreUtil.translate("gui.gas_tank.desc.0"));
        gasTankContents = this.tileEntity.liquidTank2 != null ? this.tileEntity.liquidTank2.getFluid() : null;
        if (gasTankContents != null)
        {
            String gasname = GCCoreUtil.translate(gasTankContents.getFluid().getAttributes().getTranslationKey());
            fuelTankDesc.add("(" + gasname + ")");
        }
        else
        {
            fuelTankDesc.add(" ");
        }
        fuelLevel = gasTankContents != null ? gasTankContents.getAmount() : 0;
        fuelCapacity = this.tileEntity.liquidTank2 != null ? this.tileEntity.liquidTank2.getCapacity() : 0;
        fuelTankDesc.add(EnumColor.YELLOW + " " + fuelLevel + " / " + fuelCapacity);
        this.fuelTank2Region.tooltipStrings = fuelTankDesc;

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
//		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ((int) Math.floor(this.tileEntity.getEnergyStoredGC()) + " / " + (int) Math.floor(this.tileEntity.getMaxEnergyStoredGC())));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.tileEntity.getEnergyStoredGC(), this.tileEntity.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;
    }
}
