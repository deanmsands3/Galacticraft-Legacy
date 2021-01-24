package micdoodle8.mods.galacticraft.core.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCrafting;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCrafting;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class GuiCrafting extends GuiContainerGC<ContainerCrafting>
{
    private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/crafting_table.png");
    TileEntityCrafting tileCrafting;
    List<String> memorySlotDesc = new ArrayList<String>();

    public GuiCrafting(ContainerCrafting containerCrafting, Inventory playerInv, Component title)
    {
        super(containerCrafting, playerInv, title);
        this.tileCrafting = containerCrafting.tileCrafting;
    }

    @Override
    protected void init()
    {
        super.init();
        ItemStack mem = this.tileCrafting.getMemoryHeld();
        boolean memoryStored = !mem.isEmpty();
        memorySlotDesc.add(GCCoreUtil.translate(memoryStored ? "gui.crafting_memory.desc.0" : "gui.crafting_memory.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.imageWidth) / 2 + 123, (this.height - this.imageHeight) / 2 + 59, 18, 18, memorySlotDesc, this.width, this.height, this));
    }

    @Override
    protected void renderLabels(int mouseX, int mouseY)
    {
        this.font.draw(I18n.get("container.crafting"), 28, 6, 4210752);
        this.font.draw(I18n.get("container.inventory"), 8, this.imageHeight - 96 + 2, 4210752);
    }

    @Override
    protected void renderBg(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(craftingTableGuiTextures);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(i, j, 0, 0, this.imageWidth, this.imageHeight);

        ItemStack mem = this.tileCrafting.getMemoryHeld();
        boolean memoryStored = !mem.isEmpty();
        memorySlotDesc.clear();
        memorySlotDesc.add(GCCoreUtil.translate(memoryStored ? "gui.crafting_memory.desc.0" : "gui.crafting_memory.desc.1"));
        if (mem != null && !mem.isEmpty())
        {
            this.itemRenderer.renderAndDecorateItem(mem, i + 124, j + 59);
        }
    }
}