package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.energy.EnergyDisplayHelper;
import team.galacticraft.galacticraft.common.core.inventory.ContainerCircuitFabricator;
import team.galacticraft.galacticraft.common.core.inventory.ContainerCoalGenerator;
import team.galacticraft.galacticraft.common.core.tile.TileEntityCoalGenerator;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

public class GuiCoalGenerator extends GuiContainerGC<ContainerCoalGenerator>
{
    private static final ResourceLocation COAL_GENERATOR_TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/coal_generator.png");

    private TileEntityCoalGenerator coalGenerator;

    public GuiCoalGenerator(ContainerCoalGenerator container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerCoalGenerator(playerInv, coalGenerator), playerInv, new TranslationTextComponent("tile.machine.0"));
        this.coalGenerator = container.getGenerator();
    }

    @Override
    protected void renderLabels(int mouseX, int mouseY)
    {
        this.font.draw(this.title.getColoredString(), 55, 6, 4210752);
        String displayText = I18n.get("gui.status.generating");

        if (this.coalGenerator.heatGJperTick <= 0 || this.coalGenerator.heatGJperTick < TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK)
        {
            displayText = I18n.get("gui.status.not_generating");
        }

        this.font.draw(displayText, 122 - this.font.width(displayText) / 2, 33, 4210752);

        if (this.coalGenerator.heatGJperTick < TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK)
        {
            displayText = I18n.get("gui.status.hull_heat") + ": " + (int) (this.coalGenerator.heatGJperTick / TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK * 100) + "%";
        }
        else
        {
            displayText = EnergyDisplayHelper.getEnergyDisplayS(this.coalGenerator.heatGJperTick - TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK) + "/t";
        }

        this.font.draw(displayText, 122 - this.font.width(displayText) / 2, 45, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 96 + 2, 4210752);
    }

    @Override
    protected void renderBg(float partialTicks, int mouseX, int mouseY)
    {
        this.minecraft.textureManager.bind(GuiCoalGenerator.COAL_GENERATOR_TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int containerWidth = (this.width - this.imageWidth) / 2;
        int containerHeight = (this.height - this.imageHeight) / 2;
        this.blit(containerWidth, containerHeight, 0, 0, this.imageWidth, this.imageHeight);
    }
}
