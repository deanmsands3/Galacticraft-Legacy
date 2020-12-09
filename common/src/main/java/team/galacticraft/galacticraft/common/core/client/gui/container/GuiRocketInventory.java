package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.common.api.prefab.entity.EntityAutoRocket;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntitySpaceshipBase;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntityTieredRocket;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.core.inventory.ContainerRocketInventory;
import team.galacticraft.galacticraft.core.util.EnumColor;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiRocketInventory extends GuiContainerGC<ContainerRocketInventory>
{
    private static final ResourceLocation[] rocketTextures = new ResourceLocation[4];

    static
    {
        for (int i = 0; i < 4; i++)
        {
            GuiRocketInventory.rocketTextures[i] = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/rocket_" + i * 18 + ".png");
        }
    }

    private final EntityAutoRocket rocket;

    public GuiRocketInventory(ContainerRocketInventory container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerRocketInventory(playerInv, rocket, rocketType, Minecraft.getInstance().player), playerInv, rocket.getName());
        this.passEvents = false;
        this.rocket = container.getRocket();
        this.imageHeight = rocket.getContainerSize() <= 3 ? 132 : 145 + rocket.getContainerSize() * 2;
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(I18n.get("gui.fuel_tank.desc.0"));
        fuelTankDesc.add(I18n.get("gui.fuel_tank.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + (((EntityTieredRocket) this.minecraft.player.getVehicle()).rocketType.getInventorySpace() == 2 ? 70 : 71), (this.height - this.imageHeight) / 2 + 6, 36, 40, fuelTankDesc, this.width, this.height, this));
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(I18n.get("gui.message.fuel"), 8, 2 + 3, 4210752);

        this.font.draw(this.title.getColoredString(), 8, 34 + 2 + 3, 4210752);

        if (this.minecraft.player != null && this.minecraft.player.getVehicle() != null && this.minecraft.player.getVehicle() instanceof EntitySpaceshipBase)
        {
            this.font.draw(I18n.get("gui.message.fuel") + ":", 125, 15 + 3, 4210752);
            final double percentage = ((EntitySpaceshipBase) this.minecraft.player.getVehicle()).getScaledFuelLevel(100);
            final String color = percentage > 80.0D ? EnumColor.BRIGHT_GREEN.getCode() : percentage > 40.0D ? EnumColor.ORANGE.getCode() : EnumColor.RED.getCode();
            final String str = percentage + "% " + I18n.get("gui.message.full");
            this.font.draw(color + str, 117 - str.length() / 2, 20 + 8, 4210752);
        }
    }

    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        this.minecraft.getTextureManager().bind(GuiRocketInventory.rocketTextures[(this.rocket.getContainerSize() - 2) / 18]);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - this.imageHeight) / 2;
        this.blit(var5, var6, 0, 0, 176, this.imageHeight);

        if (this.minecraft.player != null && this.minecraft.player.getVehicle() != null && this.minecraft.player.getVehicle() instanceof EntitySpaceshipBase)
        {
            final int fuelLevel = ((EntitySpaceshipBase) this.minecraft.player.getVehicle()).getScaledFuelLevel(38);

            this.blit((this.width - this.imageWidth) / 2 + (((EntityTieredRocket) this.minecraft.player.getVehicle()).rocketType.getInventorySpace() == 2 ? 71 : 72), (this.height - this.imageHeight) / 2 + 45 - fuelLevel, 176, 38 - fuelLevel, 42, fuelLevel);
        }
    }
}
