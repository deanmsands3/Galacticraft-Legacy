package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematicTier1Rocket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.opengl.GL11;

public class GuiSchematicTier1Rocket extends GuiContainerGC<ContainerSchematicTier1Rocket> implements ISchematicResultPage
{
    private static final ResourceLocation rocketBenchTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/rocketbench.png");

    private int pageIndex;

    public GuiSchematicTier1Rocket(ContainerSchematicTier1Rocket container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
//        super(new ContainerSchematicTier1Rocket(playerInv, pos), playerInv, new TranslationTextComponent("schematic.rocket_t1"), pos);
        this.imageHeight = 221;
    }

    @Override
    protected void init()
    {
        super.init();
        Button backButton;
        this.buttons.add(backButton = new Button(this.width / 2 - 130, this.height / 2 - 110, 40, 20, GCCoreUtil.translate("gui.button.back"), (button) ->
        {
            SchematicRegistry.flipToPrevPage(this, this.pageIndex);
        }));
        this.buttons.add(new Button(this.width / 2 - 130, this.height / 2 - 110 + 25, 40, 20, GCCoreUtil.translate("gui.button.next"), (button) ->
        {
            SchematicRegistry.flipToNextPage(this, this.pageIndex);
        }));
        backButton.active = false;
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(GCCoreUtil.translate("schematic.rocket_t1"), 7, -20 + 27, 4210752);
        this.font.draw(GCCoreUtil.translate("container.inventory"), 8, 202 - 104 + 2 + 27, 4210752);
    }

    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bind(GuiSchematicTier1Rocket.rocketBenchTexture);
        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - this.imageHeight) / 2;
        this.blit(var5, var6, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void setPageIndex(int index)
    {
        this.pageIndex = index;
    }
}
