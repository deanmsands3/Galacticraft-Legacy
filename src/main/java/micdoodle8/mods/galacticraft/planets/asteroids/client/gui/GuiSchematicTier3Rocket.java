package micdoodle8.mods.galacticraft.planets.asteroids.client.gui;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerSchematicTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class GuiSchematicTier3Rocket extends GuiContainerGC<ContainerSchematicTier3Rocket> implements ISchematicResultPage
{
    private static final ResourceLocation tier2SchematicTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/schematic_rocket_t3.png");

    private int pageIndex;

    public GuiSchematicTier3Rocket(ContainerSchematicTier3Rocket container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
        this.imageHeight = 238;
    }

    @Override
    public void init()
    {
        super.init();
        this.buttons.clear();
        this.buttons.add(new Button(this.width / 2 - 130, this.height / 2 - 110, 40, 20, GCCoreUtil.translate("gui.button.back"), (button) ->
        {
            SchematicRegistry.flipToPrevPage(this, this.pageIndex);
        }));
        this.buttons.add(new Button(this.width / 2 - 130, this.height / 2 - 110 + 25, 40, 20, GCCoreUtil.translate("gui.button.next"), (button) ->
        {
            SchematicRegistry.flipToNextPage(this, this.pageIndex);
        }));
    }

    @Override
    protected void renderLabels(int par1, int par2)
    {
        this.font.draw(AsteroidsItems.rocketTierThree.getName(new ItemStack(AsteroidsItems.rocketTierThree, 1)).getColoredString(), 7, -20 + 27, 4210752);
        this.font.draw(GCCoreUtil.translate("container.inventory"), 8, 220 - 104 + 2 + 27, 4210752);
    }

    @Override
    protected void renderBg(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bind(GuiSchematicTier3Rocket.tier2SchematicTexture);
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
