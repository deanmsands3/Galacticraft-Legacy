package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRocketInventory;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class GuiCargoRocket extends GuiContainerGC<ContainerRocketInventory>
{
    private static final ResourceLocation[] rocketTextures = new ResourceLocation[4];

    static
    {
        for (int i = 0; i < 4; i++)
        {
            GuiCargoRocket.rocketTextures[i] = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/rocket_" + i * 18 + ".png");
        }
    }

    private final EntityCargoRocket rocket;
    private Button launchButton;

//    public GuiCargoRocket(IInventory par1IInventory, EntityCargoRocket rocket)
//    {
//        this(par1IInventory, rocket, rocket.rocketType);
//    }

    public GuiCargoRocket(ContainerRocketInventory container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
        this.rocket = (EntityCargoRocket) container.getRocket();
//        this.allowUserInput = false;
        this.imageHeight = rocket.getContainerSize() <= 3 ? 132 : 145 + rocket.getContainerSize() * 2;
    }

    @Override
    public void init()
    {
        super.init();
        final int var6 = (this.height - this.imageHeight) / 2;
        final int var7 = (this.width - this.imageWidth) / 2;
        this.launchButton = new Button(var7 + 116, var6 + 26, 50, 20, GCCoreUtil.translate("gui.message.launch"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_CARGO_ROCKET_STATUS, GCCoreUtil.getDimensionType(minecraft.level), new Object[]{this.rocket.getId(), 0}));
        });
        this.buttons.add(this.launchButton);
        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.0"));
        fuelTankDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + (this.rocket.rocketType.getInventorySpace() == 2 ? 70 : 71), (this.height - this.imageHeight) / 2 + 6, 36, 40, fuelTankDesc, this.width, this.height, this));
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        String title = getTitle().getColoredString();
        if (this.rocket.rocketType.getInventorySpace() == 2)
        {
            this.font.draw(title, 8, 76 + (this.rocket.rocketType.getInventorySpace() - 20) / 9 * 18, 4210752);
        }
        else
        {
            this.font.draw(title, 8, 89 + (this.rocket.rocketType.getInventorySpace() - 20) / 9 * 18, 4210752);
        }

        String str = GCCoreUtil.translate("gui.message.fuel") + ":";
        this.font.draw(str, 140 - this.font.width(str) / 2, 5, 4210752);
        final double percentage = this.rocket.getScaledFuelLevel(100);
        String color = percentage > 80.0D ? EnumColor.BRIGHT_GREEN.getCode() : percentage > 40.0D ? EnumColor.ORANGE.getCode() : EnumColor.RED.getCode();
        str = percentage + "% " + GCCoreUtil.translate("gui.message.full");
        this.font.draw(color + str, 140 - this.font.width(str) / 2, 15, 4210752);
        str = GCCoreUtil.translate("gui.message.status") + ":";
        this.font.draw(str, 40 - this.font.width(str) / 2, 9, 4210752);

        String[] spltString = {""};
        String colour = EnumColor.YELLOW.toString();

        if (this.rocket.statusMessageCooldown == 0 || this.rocket.statusMessage == null)
        {
            spltString = new String[2];
            spltString[0] = GCCoreUtil.translate("gui.cargorocket.status.waiting.0");
            spltString[1] = GCCoreUtil.translate("gui.cargorocket.status.waiting.1");

            if (this.rocket.launchPhase != EnumLaunchPhase.UNIGNITED.ordinal())
            {
                spltString = new String[2];
                spltString[0] = GCCoreUtil.translate("gui.cargorocket.status.launched.0");
                spltString[1] = GCCoreUtil.translate("gui.cargorocket.status.launched.1");
                this.launchButton.active = false;
            }
        }
        else
        {
            spltString = this.rocket.statusMessage.split("#");
            colour = this.rocket.statusColour;
        }

        int y = 2;
        for (String splitString : spltString)
        {
            this.font.draw(colour + splitString, 35 - this.font.width(splitString) / 2, 9 * y, 4210752);
            y++;
        }

        if (this.rocket.statusValid && this.rocket.statusMessageCooldown > 0 && this.rocket.statusMessageCooldown < 4)
        {
            this.minecraft.setScreen(null);
        }
    }

    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        this.minecraft.getTextureManager().bind(GuiCargoRocket.rocketTextures[(this.rocket.getContainerSize() - 2) / 18]);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - this.imageHeight) / 2;
        this.blit(var5, var6, 0, 0, 176, this.imageHeight);

        final int fuelLevel = this.rocket.getScaledFuelLevel(38);
        this.blit((this.width - this.imageWidth) / 2 + (this.rocket.rocketType.getInventorySpace() == 2 ? 71 : 72), (this.height - this.imageHeight) / 2 + 45 - fuelLevel, 176, 38 - fuelLevel, 42, fuelLevel);
    }
}
