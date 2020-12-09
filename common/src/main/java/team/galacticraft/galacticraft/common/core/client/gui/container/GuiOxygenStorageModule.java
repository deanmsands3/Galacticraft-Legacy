package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.core.inventory.ContainerOxygenSealer;
import team.galacticraft.galacticraft.core.inventory.ContainerOxygenStorageModule;
import team.galacticraft.galacticraft.core.tile.TileEntityOxygenStorageModule;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiOxygenStorageModule extends GuiContainerGC<ContainerOxygenStorageModule>
{
    private static final ResourceLocation batteryBoxTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/oxygen_storage_module.png");

    private final TileEntityOxygenStorageModule tileEntity;

    public GuiOxygenStorageModule(ContainerOxygenStorageModule container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerOxygenStorageModule(playerInv, storageModule), playerInv, new TranslationTextComponent("tile.machine2.6"));
        this.tileEntity = container.getStorageModule();
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> oxygenSlotDesc = new ArrayList<String>();
        oxygenSlotDesc.add(I18n.get("gui.oxygen_slot.desc.0"));
        oxygenSlotDesc.add(I18n.get("gui.oxygen_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 16, (this.height - this.imageHeight) / 2 + 21, 18, 18, oxygenSlotDesc, this.width, this.height, this));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void renderLabels(int par1, int par2)
    {
        String guiTitle = I18n.get("tile.machine2.6");
        this.font.draw(guiTitle, this.imageWidth / 2 - this.font.width(guiTitle) / 2, 6, 4210752);
        String displayJoules = (int) (this.tileEntity.getOxygenStored() + 0.5F) + " " + I18n.get("gui.message.of");
        String displayMaxJoules = "" + this.tileEntity.getMaxOxygenStored();
        String maxOutputLabel = I18n.get("gui.max_output.desc") + ": " + TileEntityOxygenStorageModule.OUTPUT_PER_TICK * 20 + I18n.get("gui.per_second");

        this.font.draw(displayJoules, 122 - this.font.width(displayJoules) / 2 - 35, 30, 4210752);
        this.font.draw(displayMaxJoules, 122 - this.font.width(displayMaxJoules) / 2 - 35, 40, 4210752);
        this.font.draw(maxOutputLabel, 122 - this.font.width(maxOutputLabel) / 2 - 35, 60, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bind(GuiOxygenStorageModule.batteryBoxTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int containerWidth = (this.width - this.imageWidth) / 2;
        int containerHeight = (this.height - this.imageHeight) / 2;
        // Background energy bar
        this.blit(containerWidth, containerHeight, 0, 0, this.imageWidth, this.imageHeight);
        // Foreground energy bar
        int scale = (int) ((double) this.tileEntity.getOxygenStored() / (double) this.tileEntity.getMaxOxygenStored() * 72);
        this.blit(containerWidth + 52, containerHeight + 52, 176, 0, scale, 3);
    }
}
