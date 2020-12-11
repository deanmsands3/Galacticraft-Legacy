package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.common.api.recipe.ISchematicResultPage;
import team.galacticraft.galacticraft.common.api.recipe.SchematicRegistry;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.common.core.inventory.ContainerSchematic;
import team.galacticraft.galacticraft.common.core.network.PacketSimple;
import team.galacticraft.galacticraft.common.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiSchematicInput extends GuiContainerGC<ContainerSchematic> implements ISchematicResultPage
{
    private static final ResourceLocation schematicInputTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/schematicpage.png");

    private int pageIndex;

    public GuiSchematicInput(ContainerSchematic container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerSchematic(playerInv, pos), playerInv, new TranslationTextComponent("gui.message.addnewsch"), pos);
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> schematicSlotDesc = new ArrayList<String>();
        Button nextButton;
        schematicSlotDesc.add(I18n.get("gui.new_schematic.slot.desc.0"));
        schematicSlotDesc.add(I18n.get("gui.new_schematic.slot.desc.1"));
        schematicSlotDesc.add(I18n.get("gui.new_schematic.slot.desc.2"));
        schematicSlotDesc.add(I18n.get("gui.new_schematic.slot.desc.3"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 79, (this.height - this.imageHeight) / 2, 18, 18, schematicSlotDesc, this.width, this.height, this));
        this.buttons.add(new Button(this.width / 2 - 130, this.height / 2 - 110, 40, 20, I18n.get("gui.button.back"), (button) ->
        {
            SchematicRegistry.flipToPrevPage(this, this.pageIndex);
        }));
        this.buttons.add(nextButton = new Button(this.width / 2 - 130, this.height / 2 - 110 + 25, 40, 20, I18n.get("gui.button.next"), (button) ->
        {
            SchematicRegistry.flipToNextPage(this, this.pageIndex);
        }));
        this.buttons.add(new Button(this.width / 2 - 92 / 2, this.height / 2 - 52, 92, 20, I18n.get("gui.button.unlockschematic"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UNLOCK_NEW_SCHEMATIC, GCCoreUtil.getDimensionType(minecraft.level), new Object[]{}));
        }));
        nextButton.active = false;
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(I18n.get("gui.message.addnewsch"), 7, -22, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, 56, 4210752);
    }

    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bind(GuiSchematicInput.schematicInputTexture);
        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - 220) / 2;
        this.blit(var5, var6, 0, 0, this.imageWidth, 220);
    }

    @Override
    public void setPageIndex(int index)
    {
        this.pageIndex = index;
    }
}
