package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.entities.SlimelingEntity;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerSlimeling;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class GuiSlimelingInventory extends GuiContainerGC<ContainerSlimeling>
{
    private static final ResourceLocation slimelingPanelGui = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/slimeling_panel2.png");
    private final SlimelingEntity slimeling;

    private int invX;
    private int invY;
    private final int invWidth = 18;
    private final int invHeight = 18;

    public GuiSlimelingInventory(ContainerSlimeling container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);
        this.slimeling = container.getSlimeling();
        this.imageWidth = 176;
        this.imageHeight = 210;
    }

    @Override
    public void init()
    {
        super.init();
        this.buttons.clear();
        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - this.imageHeight) / 2;
        this.invX = var5 + 151;
        this.invY = var6 + 108;
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public boolean mouseClicked(double px, double py, int par3)
    {
        if (px >= this.invX && px < this.invX + this.invWidth && py >= this.invY && py < this.invY + this.invHeight)
        {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.minecraft.setScreen(new GuiSlimeling(this.slimeling));
            return true;
        }

        return super.mouseClicked(px, py, par3);
    }

    @Override
    public void renderBackground()
    {
    }

    @Override
    protected void renderBg(float f, int i, int j)
    {
        final int var5 = (this.width - this.imageWidth) / 2;
        final int var6 = (this.height - this.imageHeight) / 2;

        GlStateManager._pushMatrix();
        GuiComponent.fill(var5, var6, var5 + this.imageWidth, var6 + this.imageHeight, 0xFF000000);
        GlStateManager._popMatrix();

        int yOffset = (int) Math.floor(30.0D * (1.0F - this.slimeling.getScale()));

        GuiSlimeling.drawSlimelingOnGui(this.slimeling, this.width / 2, var6 + 62 - yOffset, 70, var5 + 51 - i, var6 + 75 - 50 - j);


        GlStateManager._translatef(0, 0, 100);

        GlStateManager._pushMatrix();
        this.minecraft.textureManager.bind(GuiSlimelingInventory.slimelingPanelGui);
        this.blit(var5, var6, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(var5 + this.imageWidth - 15, var6 + 9, 176, 0, 9, 9);
        this.blit(var5 + this.imageWidth - 15, var6 + 22, 185, 0, 9, 9);
        this.blit(var5 + this.imageWidth - 15, var6 + 35, 194, 0, 9, 9);
        String str = "" + Math.round(this.slimeling.getColorRed() * 1000) / 10.0F + "% ";
        this.drawString(this.font, str, var5 + this.imageWidth - 15 - this.font.width(str), var6 + 10, ColorUtil.to32BitColor(255, 255, 0, 0));
        str = "" + Math.round(this.slimeling.getColorGreen() * 1000) / 10.0F + "% ";
        this.drawString(this.font, str, var5 + this.imageWidth - 15 - this.font.width(str), var6 + 23, ColorUtil.to32BitColor(255, 0, 255, 0));
        str = "" + Math.round(this.slimeling.getColorBlue() * 1000) / 10.0F + "% ";
        this.drawString(this.font, str, var5 + this.imageWidth - 15 - this.font.width(str), var6 + 36, ColorUtil.to32BitColor(255, 0, 0, 255));

        this.minecraft.textureManager.bind(GuiSlimelingInventory.slimelingPanelGui);
        GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(this.invX, this.invY, 176, 27, this.invWidth, this.invHeight);
        this.blit(var5 + 8, var6 + 8, 176, 9, 18, 18);
        this.blit(var5 + 8, var6 + 29, 176, 9, 18, 18);

        ItemStack stack = this.slimeling.slimelingInventory.getItem(1);

        if (stack != null && stack.getItem() == MarsItems.SLIMELING_INVENTORY_BAG)
        {
            int offsetX = 7;
            int offsetY = 53;

            for (int y = 0; y < 3; ++y)
            {
                for (int x = 0; x < 9; ++x)
                {
                    this.blit(var5 + offsetX + x * 18, var6 + offsetY + y * 18, 176, 9, 18, 18);
                }
            }
        }

        GlStateManager._popMatrix();
    }
}
