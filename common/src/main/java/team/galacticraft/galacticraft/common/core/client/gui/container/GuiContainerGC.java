package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.common.core.client.gui.element.GuiElementInfoRegion;
import team.galacticraft.galacticraft.common.core.util.CompatibilityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import java.util.ArrayList;
import java.util.List;

public abstract class GuiContainerGC<T extends AbstractContainerMenu> extends AbstractContainerScreen<T>
{
    public List<GuiElementInfoRegion> infoRegions = new ArrayList<>();

    public GuiContainerGC(T container, Inventory playerInventory, Component title)
    {
        super(container, playerInventory, title);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderTooltip(mouseX, mouseY);

        for (int k = 0; k < this.infoRegions.size(); ++k)
        {
            GuiElementInfoRegion guibutton = this.infoRegions.get(k);
            guibutton.drawRegion(mouseX, mouseY);
        }
    }


    @Override
    public void init(Minecraft mc, int width, int height)
    {
        this.infoRegions.clear();
        super.init(mc, width, height);
    }

    public int getTooltipOffset(int par1, int par2)
    {
        for (int i1 = 0; i1 < this.menu.slots.size(); ++i1)
        {
            Slot slot = this.menu.slots.get(i1);

            if (slot.isActive() && this.isHovering(slot.x, slot.y, 16, 16, par1, par2))
            {
                ItemStack itemStack = slot.getItem();

                if (!itemStack.isEmpty())
                {
                    List list = itemStack.getTooltipLines(this.minecraft.player, this.minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
                    int size = list.size();

                    if (CompatibilityManager.isWailaLoaded())
                    {
                        size++;
                    }

                    return size * 10 + 10;
                }
            }
        }

        return 0;
    }
}
