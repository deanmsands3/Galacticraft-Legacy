package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.common.core.energy.EnergyDisplayHelper;
import team.galacticraft.galacticraft.common.core.inventory.ContainerFuelLoader;
import team.galacticraft.galacticraft.common.core.network.PacketSimple;
import team.galacticraft.galacticraft.common.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.common.core.tile.TileEntityFuelLoader;
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

public class GuiFuelLoader extends GuiContainerGC<ContainerFuelLoader>
{
    private static final ResourceLocation fuelLoaderTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/fuel_loader.png");

    private final TileEntityFuelLoader fuelLoader;

    private Button buttonLoadFuel;
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 112, (this.height - this.imageHeight) / 2 + 65, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiFuelLoader(ContainerFuelLoader container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerFuelLoader(playerInv, fuelLoader), playerInv, new TranslationTextComponent("container.fuel_loader"));
        this.fuelLoader = container.getFuelLoader();
        this.imageHeight = 180;
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(I18n.get("gui.fuel_tank.desc.2"));
        fuelTankDesc.add(I18n.get("gui.fuel_tank.desc.3"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 7, (this.height - this.imageHeight) / 2 + 33, 16, 38, fuelTankDesc, this.width, this.height, this));
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.0"));
        batterySlotDesc.add(I18n.get("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 50, (this.height - this.imageHeight) / 2 + 54, 18, 18, batterySlotDesc, this.width, this.height, this));
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ((int) Math.floor(this.fuelLoader.getEnergyStoredGC()) + " / " + (int) Math.floor(this.fuelLoader.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.imageWidth) / 2 + 112;
        this.electricInfoRegion.yPosition = (this.height - this.imageHeight) / 2 + 65;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        this.buttons.add(this.buttonLoadFuel = new Button(this.width / 2 + 2, this.height / 2 - 49, 76, 20, I18n.get("gui.button.loadfuel"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.fuelLoader.getLevel()), new Object[]{this.fuelLoader.getBlockPos(), 0}));
        }));
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(this.title.getColoredString(), 60, 10, 4210752);
        this.buttonLoadFuel.active = this.fuelLoader.disableCooldown == 0 && this.fuelLoader.fuelTank.getFluid() != FluidStack.EMPTY && this.fuelLoader.fuelTank.getFluid().getAmount() > 0;
        this.buttonLoadFuel.setMessage(!this.fuelLoader.getDisabled(0) ? I18n.get("gui.button.stoploading") : I18n.get("gui.button.loadfuel"));
        this.font.draw(I18n.get("gui.message.status") + ": " + this.getStatus(), 28, 45 + 23 - 46, 4210752);
        //this.font.drawString("" + this.fuelLoader.storage.getMaxExtract(), 28, 56 + 23 - 46, 4210752);
        //		this.font.drawString(ElectricityDisplay.getDisplay(this.fuelLoader.getVoltage(), ElectricUnit.VOLTAGE), 28, 68 + 23 - 46, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 118 + 2 + 11, 4210752);
    }

    private String getStatus()
    {
        if (this.fuelLoader.fuelTank.getFluid() == FluidStack.EMPTY || this.fuelLoader.fuelTank.getFluid().getAmount() == 0)
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.nofuel");
        }

        return this.fuelLoader.getGUIstatus();
    }

    @Override
    protected void renderBg(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GuiFuelLoader.fuelLoaderTexture);
        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - this.imageHeight) / 2;
        this.blit(var5, var6 + 5, 0, 0, this.imageWidth, 181);

        final int fuelLevel = this.fuelLoader.getScaledFuelLevel(38);
        this.blit((this.width - this.imageWidth) / 2 + 7, (this.height - this.imageHeight) / 2 + 17 + 54 - fuelLevel, 176, 38 - fuelLevel, 16, fuelLevel);

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(I18n.get("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.fuelLoader.getEnergyStoredGC(), this.fuelLoader.getMaxEnergyStoredGC(), electricityDesc);
//		electricityDesc.add(EnumColor.YELLOW + I18n.get("gui.energy_storage.desc.1") + ((int) Math.floor(this.fuelLoader.getEnergyStoredGC()) + " / " + (int) Math.floor(this.fuelLoader.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.fuelLoader.getEnergyStoredGC() > 0)
        {
            this.blit(var5 + 99, var6 + 65, 192, 7, 11, 10);
        }

        this.blit(var5 + 113, var6 + 66, 192, 0, Math.min(this.fuelLoader.getScaledElecticalLevel(54), 54), 7);
    }
}
