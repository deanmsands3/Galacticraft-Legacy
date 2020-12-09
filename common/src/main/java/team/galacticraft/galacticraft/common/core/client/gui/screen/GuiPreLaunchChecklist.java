package team.galacticraft.galacticraft.common.core.client.gui.screen;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GalacticraftCore;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementCheckboxPreLaunch;
import team.galacticraft.galacticraft.core.client.gui.screen.GuiPreLaunchChecklist.NextPageButton;
import team.galacticraft.galacticraft.core.network.PacketSimple;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;

public class GuiPreLaunchChecklist extends Screen implements GuiElementCheckboxPreLaunch.ICheckBoxCallback
{
    private static final ResourceLocation bookGuiTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/checklist_book.png");
    private final int bookImageWidth = 192;
    private final int bookImageHeight = 192;
    private final List<List<String>> checklistKeys;
    private int currPage = 0;
    private int bookTotalPages;
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    private final CompoundTag tagCompound;
    private final Map<GuiElementCheckboxPreLaunch, String> checkboxToKeyMap = Maps.newHashMap();

    public GuiPreLaunchChecklist(List<List<String>> checklistKeys, CompoundTag tagCompound)
    {
        super(new TextComponent("Prelaunch Checklist"));
        this.tagCompound = tagCompound != null ? tagCompound : new CompoundTag();
        this.checklistKeys = checklistKeys;
    }

    @Override
    public void init()
    {
        this.buttons.clear();
        this.checkboxToKeyMap.clear();

        int i = (this.width - this.bookImageWidth) / 2;
        int j = 2;
        this.buttons.add(this.buttonNextPage = new NextPageButton(i + 120, j + 154, true, (button) ->
        {
            this.currPage++;
            this.init();
        }));
        this.buttons.add(this.buttonPreviousPage = new NextPageButton(i + 38, j + 154, false, (button) ->
        {
            this.currPage--;
            this.init();
        }));

        int yPos = 25;
        int index = 2;
        int page = 0;

        for (List<String> e : this.checklistKeys)
        {
            if (e.isEmpty())
            {
                continue;
            }
            String title = e.get(0);
            List<String> checkboxes = e.subList(1, e.size());
            GuiElementCheckboxPreLaunch element = new GuiElementCheckboxPreLaunch(this, this.width / 2 - 73 + 11, yPos, I18n.get(title), 0, (button) ->
            {
            });
            int size = element.willFit(152 - yPos);
            if (size >= 0)
            {
                if (page == this.currPage)
                {
                    this.buttons.add(element);
                    this.checkboxToKeyMap.put(element, title);
                    index++;
                }
                yPos += size + minecraft.font.lineHeight / 2;
            }
            else
            {
                page++;
                yPos = 25;
                size = element.willFit(152 - yPos);
                element = new GuiElementCheckboxPreLaunch(this, this.width / 2 - 73 + 11, yPos, I18n.get(title), 0, (button) ->
                {
                });

                if (page == this.currPage)
                {
                    this.buttons.add(element);
                    this.checkboxToKeyMap.put(element, title);
                    index++;
                }
                yPos += size + minecraft.font.lineHeight / 2;
            }

            for (String checkbox : checkboxes)
            {
                element = new GuiElementCheckboxPreLaunch(this, this.width / 2 - 73 + 16, yPos, I18n.get("checklist." + checkbox + ".key"), 0, (button) ->
                {
                });
                size = element.willFit(152 - yPos);
                if (size >= 0)
                {
                    if (page == this.currPage)
                    {
                        this.buttons.add(element);
                        this.checkboxToKeyMap.put(element, title + "." + checkbox + ".key");
                        index++;
                    }
                    yPos += size + minecraft.font.lineHeight / 2;
                }
                else
                {
                    page++;
                    yPos = 25;
                    size = element.willFit(152 - yPos);
                    element = new GuiElementCheckboxPreLaunch(this, this.width / 2 - 73 + 16, yPos, I18n.get("checklist." + checkbox + ".key"), 0, (button) ->
                    {
                    });

                    if (page == this.currPage)
                    {
                        this.buttons.add(element);
                        this.checkboxToKeyMap.put(element, title + "." + checkbox + ".key");
                        index++;
                    }
                    yPos += size + minecraft.font.lineHeight / 2;
                }
            }
        }

        bookTotalPages = page + 1;
        this.updateButtons();
    }

    private void updateButtons()
    {
        this.buttonNextPage.visible = this.currPage < this.bookTotalPages - 1;
        this.buttonPreviousPage.visible = this.currPage > 0;
    }

    private void onDataChange()
    {
        for (AbstractWidget button : this.buttons)
        {
            if (button instanceof GuiElementCheckboxPreLaunch)
            {
                this.tagCompound.putBoolean(this.checkboxToKeyMap.get(button), ((GuiElementCheckboxPreLaunch) button).isSelected);
            }
        }

        // Send changed tag compound to server
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_UPDATE_CHECKLIST, GCCoreUtil.getDimensionType(minecraft.player.level), new Object[]{this.tagCompound}));

        // Update client item
        ItemStack stack = minecraft.player.getItemInHand(InteractionHand.MAIN_HAND /* TODO Support off-hand use */);
        CompoundTag tagCompound = stack.getTag();
        if (tagCompound == null)
        {
            tagCompound = new CompoundTag();
        }
        tagCompound.put("checklistData", this.tagCompound);
        stack.setTag(tagCompound);
    }

    @Override
    public void render(int par1, int par2, float par3)
    {
        GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(bookGuiTexture);
        int i = (this.width - this.bookImageWidth) / 2;
        int j = 2;
        this.blit(i, j, 0, 0, this.bookImageWidth, this.bookImageHeight);

        super.render(par1, par2, par3);
    }

    @Override
    public void onSelectionChanged(GuiElementCheckboxPreLaunch element, boolean newSelected)
    {
        // Set all 'sub-boxes' to the new selection
        boolean started = false;
        for (int i = 2; i < this.buttons.size(); ++i)
        {
            AbstractWidget widget = this.buttons.get(i);
            if (started)
            {
                if (widget.x > element.x)
                {
                    ((GuiElementCheckboxPreLaunch) widget).isSelected = newSelected;
                }
                else
                {
                    break;
                }
            }
            else if (widget == element)
            {
                started = true;
            }
        }

        this.onDataChange();
    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckboxPreLaunch checkbox, Player player)
    {
        return true;
    }

    @Override
    public boolean getInitiallySelected(GuiElementCheckboxPreLaunch checkbox)
    {
        return this.tagCompound.getBoolean(this.checkboxToKeyMap.get(checkbox));
    }

    @Override
    public void onIntruderInteraction()
    {
    }

    @Environment(EnvType.CLIENT)
    static class NextPageButton extends Button
    {
        private final boolean forward;

        public NextPageButton(int x, int y, boolean forward, OnPress onPress)
        {
            super(x, y, 23, 13, "", onPress);
            this.forward = forward;
        }

        @Override
        public void render(int mouseX, int mouseY, float partial)
        {
            if (this.visible)
            {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getInstance().getTextureManager().bind(bookGuiTexture);
                int i = 0;
                int j = 192;

                if (flag)
                {
                    i += 23;
                }

                if (!this.forward)
                {
                    j += 13;
                }

                this.blit(this.x, this.y, i, j, 23, 13);
            }
        }
    }
}
