package micdoodle8.mods.galacticraft.planets.asteroids.client.gui;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiPositionedContainer;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerSchematicAstroMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class GuiSchematicAstroMiner extends GuiPositionedContainer implements ISchematicResultPage
{

    public static final ResourceLocation schematicTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/schematic_astro_miner.png");

    private int pageIndex;

    public GuiSchematicAstroMiner(InventoryPlayer par1InventoryPlayer, BlockPos pos)
    {
        super(new ContainerSchematicAstroMiner(par1InventoryPlayer, pos), pos);
        this.ySize = 221;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 130, this.height / 2 - 110, 40, 20, GCCoreUtil.translate("gui.button.back.name")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 130, this.height / 2 - 110 + 25, 40, 20, GCCoreUtil.translate("gui.button.next.name")));
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            switch (par1GuiButton.id)
            {
                case 0:
                    SchematicRegistry.flipToLastPage(this, this.pageIndex);
                    break;
                case 1:
                    SchematicRegistry.flipToNextPage(this, this.pageIndex);
                    break;
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(AsteroidsItems.astroMiner.getItemStackDisplayName(new ItemStack(AsteroidsItems.astroMiner, 1, 0)), 7, -20 + 25, 4210752);
        this.fontRenderer.drawString(GCCoreUtil.translate("container.inventory"), 8, 220 - 104 + 2 - 16, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GuiSchematicAstroMiner.schematicTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 26, this.xSize, this.ySize);
    }

    @Override
    public void setPageIndex(int index)
    {
        this.pageIndex = index;
    }
}
