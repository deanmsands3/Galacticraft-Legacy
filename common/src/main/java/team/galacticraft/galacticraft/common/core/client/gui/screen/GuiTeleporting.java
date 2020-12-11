package team.galacticraft.galacticraft.common.core.client.gui.screen;

import team.galacticraft.galacticraft.common.core.dimension.DimensionSpaceStation;
import team.galacticraft.galacticraft.common.core.tick.TickHandlerClient;
import team.galacticraft.galacticraft.common.core.util.ColorUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.dimension.DimensionType;

public class GuiTeleporting extends Screen
{
    private final DimensionType targetDimensionID;
    private final String message;

    public GuiTeleporting(DimensionType targetDimensionID)
    {
        super(new TextComponent("Teleporting"));
        this.targetDimensionID = targetDimensionID;
        String[] possibleStrings = new String[]{"Taking one small step", "Taking one giant leap", "Prepare for entry!"};
        this.message = possibleStrings[(int) (Math.random() * possibleStrings.length)];
        TickHandlerClient.teleportingGui = this;
    }

    @Override
    protected void init()
    {
        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        super.render(mouseX, mouseY, partialTicks);
        this.renderBackground(0);
        this.drawCenteredString(this.font, this.message, this.width / 2, this.height / 2, ColorUtil.to32BitColor(255, 255, 255, 255));
    }

    @Override
    public void tick()
    {
        super.tick();
        if (minecraft.player != null && minecraft.player.level != null)
        {
            // Screen will exit when the player is in the target dimension and has started moving down
            if (minecraft.player.level.getDimension().getType() == this.targetDimensionID)
            {
                if ((minecraft.player.level.getDimension() instanceof DimensionSpaceStation || (minecraft.player.getY() - minecraft.player.yOld) < 0.0))
                {
                    minecraft.setScreen(null);
                    TickHandlerClient.teleportingGui = null;
                }
            }
        }
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
