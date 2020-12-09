package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.energy.EnergyDisplayHelper;
import team.galacticraft.galacticraft.core.inventory.ContainerElectricIngotCompressor;
import team.galacticraft.galacticraft.core.inventory.ContainerEnergyStorageModule;
import team.galacticraft.galacticraft.core.tile.TileEntityEnergyStorageModule;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

public class GuiEnergyStorageModule extends GuiContainerGC<ContainerEnergyStorageModule>
{
    private static final ResourceLocation batteryBoxTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/energy_storage_module.png");

    private TileEntityEnergyStorageModule storageModule;

    public GuiEnergyStorageModule(ContainerEnergyStorageModule container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerEnergyStorageModule(playerInv, storageModule), playerInv, new TranslationTextComponent(storageModule.getTierGC() == 1 ? "tile.machine.1" : "tile.machine.8"));
        this.storageModule = container.getStorageModule();
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(this.title.getColoredString(), this.imageWidth / 2 - this.font.width(this.title.getColoredString()) / 2, 6, 4210752);
        float energy = this.storageModule.getEnergyStoredGC();
        if (energy + 49 > this.storageModule.getMaxEnergyStoredGC())
        {
            energy = this.storageModule.getMaxEnergyStoredGC();
        }
        String displayStr = EnergyDisplayHelper.getEnergyDisplayS(energy);
        this.font.draw(displayStr, 122 - this.font.width(displayStr) / 2, 25, 4210752);
        displayStr = I18n.get("gui.message.of") + " " + EnergyDisplayHelper.getEnergyDisplayS(this.storageModule.getMaxEnergyStoredGC());
        this.font.draw(displayStr, 122 - this.font.width(displayStr) / 2, 34, 4210752);
        displayStr = I18n.get("gui.max_output.desc") + ": " + EnergyDisplayHelper.getEnergyDisplayS(this.storageModule.storage.getMaxExtract()) + "/t";
        this.font.draw(displayStr, 114 - this.font.width(displayStr) / 2, 64, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bind(GuiEnergyStorageModule.batteryBoxTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int containerWidth = (this.width - this.imageWidth) / 2;
        int containerHeight = (this.height - this.imageHeight) / 2;
        // Background energy bar
        this.blit(containerWidth, containerHeight, 0, 0, this.imageWidth, this.imageHeight);
        // Foreground energy bar
        int scale = (int) ((this.storageModule.getEnergyStoredGC() + 49) / this.storageModule.getMaxEnergyStoredGC() * 72);
        this.blit(containerWidth + 87, containerHeight + 52, 176, 0, scale, 3);
    }
}
