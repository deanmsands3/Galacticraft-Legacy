package micdoodle8.mods.galacticraft.planets.venus.client.gui;

import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerCrashedProbe;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityCrashedProbe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

public class GuiCrashedProbe extends GuiContainerGC<ContainerCrashedProbe>
{
    private static final ResourceLocation guiTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/crashed_probe.png");

    private final TileEntityCrashedProbe probe;

    public GuiCrashedProbe(ContainerCrashedProbe container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
        this.probe = container.getProbe();
        this.imageHeight = 133;
        this.imageWidth = 176;
    }

    @Override
    protected void renderBg(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GuiCrashedProbe.guiTexture);
        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - this.imageHeight) / 2;
        this.blit(var5, var6, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        String displayString = this.getTitle().getColoredString();
        this.font.draw(displayString, this.imageWidth / 2 - this.font.width(displayString) / 2, 7, 4210752);
        this.font.draw(GCCoreUtil.translate("container.inventory"), 8, this.imageHeight - 94, 4210752);
    }
}
