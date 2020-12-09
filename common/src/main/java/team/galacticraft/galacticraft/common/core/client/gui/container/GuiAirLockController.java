package team.galacticraft.galacticraft.common.core.client.gui.container;

import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GalacticraftCore;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementCheckbox;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementCheckbox.ICheckBoxCallback;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementDropdown;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementDropdown.IDropboxCallback;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementTextBox;
import team.galacticraft.galacticraft.core.client.gui.element.GuiElementTextBox.ITextBoxCallback;
import team.galacticraft.galacticraft.core.network.PacketSimple;
import team.galacticraft.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.core.tile.TileEntityAirLockController;
import team.galacticraft.galacticraft.core.util.ColorUtil;
import team.galacticraft.galacticraft.core.util.EnumColor;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import team.galacticraft.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

public class GuiAirLockController extends Screen implements ICheckBoxCallback, IDropboxCallback, ITextBoxCallback
{
    private final int xSize;
    private final int ySize;
    private static final ResourceLocation AIR_LOCK_TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/air_lock_controller.png");
    private final TileEntityAirLockController controller;
    private GuiElementCheckbox checkboxRedstoneSignal;
    private GuiElementCheckbox checkboxPlayerDistance;
    private GuiElementDropdown dropdownPlayerDistance;
    private GuiElementCheckbox checkboxOpenForPlayer;
    private GuiElementTextBox textBoxPlayerToOpenFor;
    private GuiElementCheckbox checkboxInvertSelection;
    private GuiElementCheckbox checkboxHorizontalMode;
    private int cannotEditTimer;

    public GuiAirLockController(TileEntityAirLockController controller)
    {
        super(new TranslatableComponent("gui.title.air_lock"));
        this.controller = controller;
        this.ySize = 139;
        this.xSize = 181;
    }

    @Override
    protected void init()
    {
        super.init();
        this.buttons.clear();
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.checkboxRedstoneSignal = new GuiElementCheckbox(this, this.width / 2 - 84, var6 + 18, I18n.get("gui.checkbox.redstone_signal"));
        this.checkboxPlayerDistance = new GuiElementCheckbox(this, this.width / 2 - 84, var6 + 33, I18n.get("gui.checkbox.player_within") + ": ");
        String[] dropboxStrings = {I18n.get("gui.dropbox.player_distance.name.0"), I18n.get("gui.dropbox.player_distance.name.1"), I18n.get("gui.dropbox.player_distance.name.2"), I18n.get("gui.dropbox.player_distance.name.3")};
        this.dropdownPlayerDistance = new GuiElementDropdown(this, var5 + 99, var6 + 32, dropboxStrings);
        this.checkboxOpenForPlayer = new GuiElementCheckbox(this, this.width / 2 - 68, var6 + 49, I18n.get("gui.checkbox.player") + ": ");
        this.textBoxPlayerToOpenFor = new GuiElementTextBox(this, this.width / 2 - 61, var6 + 64, 110, 15, "", false, 16, false);
        this.checkboxInvertSelection = new GuiElementCheckbox(this, this.width / 2 - 84, var6 + 80, I18n.get("gui.checkbox.invert"));
        this.checkboxHorizontalMode = new GuiElementCheckbox(this, this.width / 2 - 84, var6 + 96, I18n.get("gui.checkbox.horizontal"));
        this.buttons.add(this.checkboxRedstoneSignal);
        this.buttons.add(this.checkboxPlayerDistance);
        this.buttons.add(this.dropdownPlayerDistance);
        this.buttons.add(this.checkboxOpenForPlayer);
        this.buttons.add(this.textBoxPlayerToOpenFor);
        this.buttons.add(this.checkboxInvertSelection);
        this.buttons.add(this.checkboxHorizontalMode);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers)
    {
        if (key != GLFW.GLFW_KEY_ESCAPE)
        {
            if (this.textBoxPlayerToOpenFor.keyPressed(key, scanCode, modifiers))
            {
                return true;
            }
        }

        return super.keyPressed(key, scanCode, scanCode);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

//    @Override
//    protected void actionPerformed(Button par1GuiButton)
//    {
//        if (par1GuiButton.enabled)
//        {
//            switch (par1GuiButton.id)
//            {
//            case 0:
//                break;
//            }
//        }
//    }


    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;

        this.minecraft.textureManager.bind(GuiAirLockController.AIR_LOCK_TEXTURE);
        this.blit(var5, var6, 0, 0, this.xSize, this.ySize);

        this.blit(var5 + 11, var6 + 51, 181, 0, 7, 9);

        String message = I18n.getWithFormat("gui.title.air_lock", this.controller.ownerName);
        this.font.draw(message, this.width / 2 - this.font.width(message) / 2, this.height / 2 - 65, 4210752);

        if (this.cannotEditTimer > 0)
        {
            this.font.draw(this.controller.ownerName, this.width / 2 - this.font.width(message) / 2, this.height / 2 - 65, this.cannotEditTimer % 30 < 15 ? ColorUtil.to32BitColor(255, 255, 100, 100) : 4210752);
            this.cannotEditTimer--;
        }

        message = I18n.get("gui.message.status") + ":";
        this.font.draw(message, this.width / 2 - this.font.width(message) / 2, this.height / 2 + 45, 4210752);
        message = EnumColor.RED + I18n.get("gui.status.air_lock_closed");

        if (!this.controller.active)
        {
            message = EnumColor.BRIGHT_GREEN + I18n.get("gui.status.air_lock_open");
        }

        this.font.draw(message, this.width / 2 - this.font.width(message) / 2, this.height / 2 + 55, 4210752);

        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
    {
        if (checkbox.equals(this.checkboxRedstoneSignal))
        {
            this.controller.redstoneActivation = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionType(minecraft.level), new Object[]{0, this.controller.getBlockPos(), this.controller.redstoneActivation ? 1 : 0}));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 0,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.redstoneActivation ? 1 :
            // 0 }));
        }
        else if (checkbox.equals(this.checkboxPlayerDistance))
        {
            this.controller.playerDistanceActivation = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionType(minecraft.level), new Object[]{1, this.controller.getBlockPos(), this.controller.playerDistanceActivation ? 1 : 0}));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 1,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.playerDistanceActivation
            // ? 1 : 0 }));
        }
        else if (checkbox.equals(this.checkboxOpenForPlayer))
        {
            this.controller.playerNameMatches = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionType(minecraft.level), new Object[]{3, this.controller.getBlockPos(), this.controller.playerNameMatches ? 1 : 0}));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 3,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.playerNameMatches ? 1 : 0
            // }));
        }
        else if (checkbox.equals(this.checkboxInvertSelection))
        {
            this.controller.invertSelection = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionType(minecraft.level), new Object[]{4, this.controller.getBlockPos(), this.controller.invertSelection ? 1 : 0}));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 4,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.invertSelection ? 1 : 0
            // }));
        }
        else if (checkbox.equals(this.checkboxHorizontalMode))
        {
            this.controller.lastHorizontalModeEnabled = this.controller.horizontalModeEnabled;
            this.controller.horizontalModeEnabled = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionType(minecraft.level), new Object[]{5, this.controller.getBlockPos(), this.controller.horizontalModeEnabled ? 1 : 0}));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 5,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.horizontalModeEnabled ? 1
            // : 0 }));
        }
    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckbox checkbox, Player player)
    {
        return PlayerUtil.getName(player).equals(this.controller.ownerName);
    }

    @Override
    public boolean getInitiallySelected(GuiElementCheckbox checkbox)
    {
        if (checkbox.equals(this.checkboxRedstoneSignal))
        {
            return this.controller.redstoneActivation;
        }
        else if (checkbox.equals(this.checkboxPlayerDistance))
        {
            return this.controller.playerDistanceActivation;
        }
        else if (checkbox.equals(this.checkboxOpenForPlayer))
        {
            return this.controller.playerNameMatches;
        }
        else if (checkbox.equals(this.checkboxInvertSelection))
        {
            return this.controller.invertSelection;
        }
        else if (checkbox.equals(this.checkboxHorizontalMode))
        {
            return this.controller.horizontalModeEnabled;
        }

        return false;
    }

    @Override
    public boolean canBeClickedBy(GuiElementDropdown dropdown, Player player)
    {
        return PlayerUtil.getName(player).equals(this.controller.ownerName);
    }

    @Override
    public void onSelectionChanged(GuiElementDropdown dropdown, int selection)
    {
        if (dropdown.equals(this.dropdownPlayerDistance))
        {
            this.controller.playerDistanceSelection = selection;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionType(minecraft.level), new Object[]{2, this.controller.getBlockPos(), this.controller.playerDistanceSelection}));
        }
    }

    @Override
    public int getInitialSelection(GuiElementDropdown dropdown)
    {
        return this.controller.playerDistanceSelection;
    }

    @Override
    public boolean canPlayerEdit(GuiElementTextBox textBox, Player player)
    {
        return PlayerUtil.getName(player).equals(this.controller.ownerName);
    }

    @Override
    public void onTextChanged(GuiElementTextBox textBox, String newText)
    {
        if (textBox.equals(this.textBoxPlayerToOpenFor))
        {
            this.controller.playerToOpenFor = newText != null ? newText : "";
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_STRING, GCCoreUtil.getDimensionType(minecraft.level), new Object[]{0, this.controller.getBlockPos(), this.controller.playerToOpenFor}));
        }
    }

    @Override
    public String getInitialText(GuiElementTextBox textBox)
    {
        if (textBox.equals(this.textBoxPlayerToOpenFor))
        {
            return this.controller.playerToOpenFor;
        }

        return null;
    }

    @Override
    public int getTextColor(GuiElementTextBox textBox)
    {
        return ColorUtil.to32BitColor(255, 200, 200, 200);
    }

    @Override
    public void onIntruderInteraction()
    {
        this.cannotEditTimer = 50;
    }

    @Override
    public void onIntruderInteraction(GuiElementTextBox textBox)
    {
        this.cannotEditTimer = 50;
    }
}
